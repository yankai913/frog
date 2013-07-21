package com.zoo.frog.action;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import com.zoo.frog.DefaultAnnotationParseHandler;
import com.zoo.frog.bean.ControllerBean;
import com.zoo.frog.bean.MethodBean;
import com.zoo.frog.exception.TracerException;
import com.zoo.frog.hook.AbstractHook;
import com.zoo.frog.parser.AnnotationParseHandler;

/**
 * 
 * @author <a href="mailto:yankai913@gmail.com">yankai</a>
 * @date 2013-7-13
 */
@Controller("hookHelper")
public class HookHelper extends AbstractHook {

	private ApplicationContext ctx;
	
	//<Class, ControllerBean>  
	private final ConcurrentMap<Class<?>, ControllerBean> class2controller = 
				new ConcurrentHashMap<Class<?>, ControllerBean>();

	private AnnotationParseHandler parseHandler = new DefaultAnnotationParseHandler();
	
	public ConcurrentMap<Class<?>, ControllerBean> getControllerMap() {
		return class2controller;
	}
	
	@Override
	public AnnotationParseHandler getHandler() {
		return parseHandler;
	}
	
	public void afterPropertiesSet() throws Exception {
		
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		//parse controller
		try {
			ControllerBean cb = getHandler().parseController(bean);
			if (cb == null) {
				return bean;
			}
			
			class2controller.put(bean.getClass(), cb);

			Method[] methods = bean.getClass().getDeclaredMethods();
			
			//parse method
			for (Method method : methods) {
				MethodBean mb = getHandler().parseMethod(method);
				if (mb == null) {
					continue;
				}
				cb.getMethodBeanList().add(mb);
				mb.setParentClass(cb);
			}
			
		} catch (TracerException e) {
			throw new BeanInstantiationException(HookHelper.class, e.getMessage());
		}
		
		return bean;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.ctx = applicationContext;
	}
	
	public ApplicationContext getApplicationContext() {
		return ctx;
	}

}
