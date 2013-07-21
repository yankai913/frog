package com.zoo.frog.hook;


import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.zoo.frog.bean.ControllerBean;
import com.zoo.frog.parser.AnnotationParseHandler;


/**
 * 
 * @author <a href="mailto:yankai913@gmail.com">yankai</a>
 * @date 2013-7-8
 */
public abstract class AbstractHook implements InitializingBean, BeanPostProcessor, ApplicationContextAware {

	public abstract AnnotationParseHandler getHandler(); 
	
	public abstract ApplicationContext getApplicationContext();
	
	public abstract ConcurrentMap<Class<?>, ControllerBean> getControllerMap();
}
