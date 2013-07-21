package com.zoo.frog.bean;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * action` method and it`s annotation
 * 
 * @author <a href="mailto:yankai913@gmail.com">yankai</a>
 * @date 2013-7-11
 */
public class MethodBean implements Serializable {

	private static final long serialVersionUID = 9151356652033832981L;

	/** myself */
	private Method method;

	/** belong class */
	private ControllerBean parentClass;

	/** @RequestMapping */
	private RequestMapping requestMapping;

	/** @ResponseBody */
	private ResponseBody responseBody;

	/**  parameters */
	private Object[] parameters;
	
	/** param`s annotation */
	private Annotation[][] paramAnnotations;

	private HttpServletRequest request;

	private HttpServletResponse response;

	/** ModelMap or Model serves the same purpose. */
	private ModelMap modelMap;
	/** ModelMap or Model serves the same purpose. */
	private Model model;
	
	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public ControllerBean getParentClass() {
		return parentClass;
	}

	public void setParentClass(ControllerBean parentClass) {
		this.parentClass = parentClass;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public ModelMap getModelMap() {
		return modelMap;
	}

	public void setModelMap(ModelMap modelMap) {
		this.modelMap = modelMap;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public RequestMapping getRequestMapping() {
		return requestMapping;
	}

	public void setRequestMapping(RequestMapping requestMapping) {
		this.requestMapping = requestMapping;
	}

	public ResponseBody getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(ResponseBody responseBody) {
		this.responseBody = responseBody;
	}

	public Annotation[][] getParamAnnotations() {
		return paramAnnotations;
	}

	public void setParamAnnotations(Annotation[][] paramAnnotations) {
		this.paramAnnotations = paramAnnotations;
	}
	
}
