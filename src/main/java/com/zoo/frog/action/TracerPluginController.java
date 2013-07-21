package com.zoo.frog.action;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ValueConstants;

import com.zoo.frog.bean.ControllerBean;
import com.zoo.frog.bean.MethodBean;
import com.zoo.frog.common.JavassistTool;

@Controller
@RequestMapping("/tracer-interface")
public class TracerPluginController {

	@Resource(name = "hookHelper")
	private HookHelper hookHelper;

	//<path, controller> , path like /test.do?id=&name=[POST,GET]
	Map<String, Object> path2object = new HashMap<String, Object>();
	
	//<path, Method>
	Map<String, MethodBean> path2method = new HashMap<String, MethodBean>();
			
	@RequestMapping(value = {"/list"}, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, TreeSet<String>> interfaceList() throws Exception {
		Map<String, TreeSet<String>> result = new HashMap<String, TreeSet<String>>();
		if (path2object.keySet().size() > 0) {
			result.put("ilist", new TreeSet<String>(path2object.keySet()));
			return result;
		}
		ConcurrentMap<Class<?>, ControllerBean> controllerMap = hookHelper.getControllerMap();
		for (ControllerBean cb :  controllerMap.values()) {
			String cPath = "";
			if (cb.getRequestMapping() != null ) {
				String[] cPathArr = cb.getRequestMapping().value();
				if (cPathArr != null && cPathArr.length > 0) {
					cPath = cPathArr[0];
					if (cPath.startsWith("/")) {
						cPath = "";
					}
				}
			}
			//
			List<MethodBean> mbList = cb.getMethodBeanList();
			if (mbList != null && mbList.size() > 0) {
				for (MethodBean mb : mbList) {
					String mPath = "";
					String[] mPathArr = mb.getRequestMapping().value();
					if (mPathArr != null && mPathArr.length > 0) {
						mPath = mPathArr[0];
					}
					if (!mPath.startsWith("/")) {
						mPath = "/" + mPath;
					}
					//
					String path = cPath + mPath;
					String[] params = mb.getRequestMapping().params();
					if (params != null && params.length > 0) {
						for (int i = 0; i < params.length; i++) {
							if (i == 0) {
								path = path + "?";
							}
							path = path + params[i] + "=";
							if (i != params.length - 1) {
								path = path + "&";
							}
						}
					}
					RequestMethod[] requestMethodArr = mb.getRequestMapping().method();
					if (requestMethodArr != null && requestMethodArr.length > 0) {
						path = path + Arrays.toString(requestMethodArr);
					}
					path2object.put(path, cb.getObject());
					path2method.put(path, mb);
				}
			}
		}
		result.put("ilist", new TreeSet<String>(path2object.keySet()));
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET, params = "post_tracer_path")
	public Map<String, Object> getParameterName(@RequestParam(value="post_tracer_path") String path) throws Exception {
		if (path2method.isEmpty()) {
			interfaceList();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		Method method = path2method.get(path).getMethod();
		Annotation[][] anno2Arr = path2method.get(path).getParamAnnotations();
		if (method != null) {
			String[] paramNames = JavassistTool.getMethodParamNames(method);
			result.put("paramNames", Arrays.asList(paramNames));
			//
			Class<?>[] parametertypes = method.getParameterTypes();
			StringBuilder sb = new StringBuilder(method.getReturnType().getName());
			sb.append(" ").append(method.getName()).append("(");
			for (int i = 0; i < paramNames.length; i++) {
				//param annotation
				sb.append(Arrays.toString(anno2Arr[i])).append(" ");
				//param type
				sb.append(parametertypes[i].getSimpleName()).append(" ");
				//param name
				sb.append(paramNames[i]);
				
				if (i != paramNames.length - 1) {
					sb.append(", ");
				}
			}
			result.put("methodSign", sb.append(")").toString());
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/invoke", method = RequestMethod.POST)
	public Map<String, Object> invoke(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, Model model, String post_tracer_path) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		//path2object path2method
		Method method  = path2method.get(post_tracer_path).getMethod();
		//invoker
		Object obj = path2object.get(post_tracer_path);
		if (method == null || obj == null) {
			result.put("error", "method is null or obj is null");
			return result;
		}
		String[] paramNames = JavassistTool.getMethodParamNames(method);
		Class<?>[] parameterTypes = method.getParameterTypes(); 
		if (parameterTypes.length != paramNames.length) {
			result.put("error", "method.getParameterTypes().length != paramNames.length");
			return result;
		}
		//
		Object[] paramValues = new Object[paramNames.length];
		MethodBean mb = path2method.get(post_tracer_path);
		for (int i = 0; i < paramNames.length; i++) {
			String name = paramNames[i];
			Class<?> parameterType =  parameterTypes[i];
			// HttpServletRequest, HttpServletResponse, ModelMap, Model
			if (HttpServletRequest.class.isAssignableFrom(parameterType)) {//request
				paramValues[i] = request;
				continue;
			} else if (HttpServletResponse.class.isAssignableFrom(parameterType)) {//response
				paramValues[i] = response;
				continue;
			} else if (ModelMap.class.isAssignableFrom(parameterType)) {//modelMap
				paramValues[i] = modelMap;
				result.put("ModelMap", modelMap);
				continue;
			} else if (Model.class.isAssignableFrom(parameterType)) {//model
				paramValues[i] = model;
				result.put("Model", model);
				continue;
			}
			String value = request.getParameter(name);
			// judge annotation
			Annotation[] ann2Arr = mb.getParamAnnotations()[i];

			for (Annotation ano : ann2Arr) {
				//@RequestParam
				if (ano.annotationType() == RequestParam.class) {
					RequestParam rp = (RequestParam)ano;
					//bind yet
					if (rp.value() != null && !rp.value().equals("")) {
						value = request.getParameter(rp.value());
					}
					if (value == null || value == "") {
						//default
						if (!ValueConstants.DEFAULT_NONE.equals(rp.defaultValue())) {
							value = rp.defaultValue();
						}
					}
					//required
					if ((value == null || value == "") && rp.required()) {
						result.put("error", name + " is blank");
						return result;
					}
				}
				//@PathVariable
				if (ano.annotationType() == PathVariable.class) {
					//nothing
				}
			}
			// value format
			if (parameterType == int.class) {
				paramValues[i] = Integer.parseInt(value);
			} else if (parameterType == Integer.class) {
				paramValues[i] = new Integer(value);
			} else if (parameterType == long.class) {
				paramValues[i] = Long.parseLong(value);
			} else if (parameterType == Long.class) {
				paramValues[i] = new Long(value);
			} else if (parameterType == boolean.class) {
				paramValues[i] = Boolean.parseBoolean(value);
			} else if (parameterType == Boolean.class) {
				paramValues[i] = new Boolean(value);
			} else {//default String
				paramValues[i] = value;
			}
		}
		//invoke
		try {
			Object res = method.invoke(obj, paramValues);
			result.put("result", res);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("error", e.getMessage());
		}
		return result;
	}
}
