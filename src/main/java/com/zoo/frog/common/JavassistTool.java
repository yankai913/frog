package com.zoo.frog.common;


import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
/**
 * 
 * @author <a href="mailto:yankai913@gmail.com">yankai</a>
 * @date 2013-7-17
 */
public class JavassistTool {
	
	static final Map<ClassLoader, ClassPool> pool2map = new ConcurrentHashMap<ClassLoader, ClassPool>(); //ClassLoader - ClassPool
	
	public static String[] getMethodParamNames(Method method)  throws Exception {
		Class<?> clazz =  method.getDeclaringClass();
		String methodName = method.getName();
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = null;
		try {
			cc = pool.get(clazz.getName());
		} catch (Exception e) {
			if (e instanceof NotFoundException) {
				pool = pool2map.get(clazz.getClassLoader());
				if (pool == null) {
					pool = new ClassPool(true);
					pool.appendClassPath(new LoaderClassPath(clazz.getClassLoader()));
					pool2map.put(clazz.getClassLoader(), pool);
				}
			}
		}
		cc = pool.get(clazz.getName());
		CtMethod cm = cc.getDeclaredMethod(methodName);
		MethodInfo methodInfo = cm.getMethodInfo();
		CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
		LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
				
		if (attr == null) {
			throw new IllegalStateException("LocalVariableAttribute is null");
		}
		String[] paramNames = new String[cm.getParameterTypes().length];
		int offset = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
		for (int i = 0; i < paramNames.length; i++) {
			paramNames[i] = attr.variableName(i + offset);
		}
		return paramNames;
	}
	
}
