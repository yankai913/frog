package com.zoo.frog.parser;

import java.lang.reflect.Method;

import com.zoo.frog.bean.ControllerBean;
import com.zoo.frog.bean.MethodBean;
import com.zoo.frog.exception.TracerException;


/**
 * 
 * @author <a href="mailto:yankai913@gmail.com">yankai</a>
 * @date 2013-7-11
 */
public interface AnnotationParseHandler {

	ControllerBean parseController(Object controller) throws TracerException;
	
	MethodBean parseMethod(Method method) throws TracerException;
	
}
