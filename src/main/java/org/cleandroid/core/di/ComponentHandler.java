/* 
 * Cleandroid Framework 
 * @author: Douraid Arfaoui <douraid.arfaoui@gmail.com>
 *
 * Copyright (c) 2015, Douraid Arfaoui, or third-party contributors as 
 * indicated by the @author tags or express copyright attribution 
 * statements applied by the authors. 
 * 
 * This copyrighted material is made available to anyone wishing to use, modify, 
 * copy, or redistribute it subject to the terms and conditions of the Apache 2 
 * License, as published by the Apache Software Foundation.  
 * 
 */ 
package org.cleandroid.core.di;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.cleandroid.core.application.App;

public class ComponentHandler implements DependencyProvider{

	@Override
	public  <T> T handle(Class<T> varClass, Object context) {
		if(App.getContainer().isRegistered(varClass,varClass.getName()))
			return App.getContainer().getObject(varClass,varClass.getName());

		T returnObject = null;

		try{
			returnObject = varClass.newInstance();
			injectDependencies(returnObject, context);
		}
			catch(Exception e){
				throw new RuntimeException(e);
			}


		App.getContainer().registerObject(varClass.getName(),returnObject);
		return returnObject;

	}

	


	public static void injectDependencies(Object object, Object context) throws IllegalAccessException {
		for(Field f:object.getClass().getDeclaredFields()){
			f.setAccessible(true);

			for(Annotation fieldAnnotation:f.getAnnotations()){
				Object fieldValue = DependencyManager.getDependencyFromAnnotation(fieldAnnotation, context, f.getType());
				if(fieldValue!=null)
					f.set(object, fieldValue);
			}
		}
	}

}
