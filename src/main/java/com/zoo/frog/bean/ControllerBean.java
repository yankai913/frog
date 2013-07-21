package com.zoo.frog.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * controller and it`s annotations
 * 
 * @author <a href="mailto:yankai913@gmail.com">yankai</a>
 * @date 2013-7-11
 */
public class ControllerBean implements Serializable {

	private static final long serialVersionUID = -2586650473844595173L;

	/** controller reference*/
	private Object object;
	
	/** Class */
	private Class<?> clazz;

	/** action method */
	private List<MethodBean> methodBeanList = new ArrayList<MethodBean>();
	
	/** @Controller */
	private Controller controller;

	/** @RequestMapping */
	private RequestMapping requestMapping;

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public List<MethodBean> getMethodBeanList() {
		return methodBeanList;
	}

	public void setMethodBeanList(List<MethodBean> methodBeanList) {
		this.methodBeanList = methodBeanList;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public RequestMapping getRequestMapping() {
		return requestMapping;
	}

	public void setRequestMapping(RequestMapping requestMapping) {
		this.requestMapping = requestMapping;
	}
	
}
