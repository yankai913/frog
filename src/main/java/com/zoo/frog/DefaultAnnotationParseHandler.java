package com.zoo.frog;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zoo.frog.action.HookHelper;
import com.zoo.frog.action.TracerPluginController;
import com.zoo.frog.bean.ControllerBean;
import com.zoo.frog.bean.MethodBean;
import com.zoo.frog.exception.TracerException;
import com.zoo.frog.parser.AnnotationParseHandler;

/**
 * parse annotation parser, ThreadSafe and Singleton 
 * @author <a href="mailto:yankai913@gmail.com">yankai</a>
 * @date 2013-7-13
 */
public class DefaultAnnotationParseHandler implements AnnotationParseHandler {

	public ControllerBean parseController(Object bean) throws TracerException {
		
		if (!bean.getClass().isAnnotationPresent(Controller.class)
				|| bean.getClass() == HookHelper.class
				|| bean.getClass() == TracerPluginController.class) {
			return null;
		}
		
		ControllerBean cb = new ControllerBean();
		cb.setClazz(bean.getClass());
		cb.setObject(bean);
		//@Controller
		if (bean.getClass().isAnnotationPresent(Controller.class)) {
			Controller controller = bean.getClass().getAnnotation(Controller.class);
			cb.setController(controller);
	    }
		
		//@RequestMapping
		if (bean.getClass().isAnnotationPresent(RequestMapping.class)) {
			RequestMapping requestMapping = bean.getClass().getAnnotation(RequestMapping.class);
			cb.setRequestMapping(requestMapping);
		}
		return cb;
	}

	@Override
	public MethodBean parseMethod(Method method) throws TracerException {
		if (!method.isAnnotationPresent(RequestMapping.class)) {
			return null;
		}
		MethodBean mb = new MethodBean();
		mb.setMethod(method);
		//@RequestMapping
		if (method.isAnnotationPresent(RequestMapping.class)) {
			RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
			mb.setRequestMapping(requestMapping);
		}
		//@ResponseBody
		if (method.isAnnotationPresent(ResponseBody.class)) {
			ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
			mb.setResponseBody(responseBody);
		}
		//parse parameter` annotations
		Annotation[][] paramAnnotations = method.getParameterAnnotations();
		mb.setParamAnnotations(paramAnnotations);
		return mb;
	}

}
