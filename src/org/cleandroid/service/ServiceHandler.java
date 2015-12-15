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
package org.cleandroid.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.cleandroid.di.DependencyManager;

import android.app.Activity;
import android.util.Log;

public class ServiceHandler {
	
	public static <T> T handle(Class<T> varClass,Activity activity) {
		T returnObject = null;
		try{
			returnObject = varClass.newInstance();
			for(Field f:varClass.getDeclaredFields()){
				f.setAccessible(true);
		
				for(Annotation fieldAnnotation:f.getAnnotations()){
					Object fieldValue = DependencyManager.getDependencyFromAnnotation(fieldAnnotation, activity, f.getType());
					if(fieldValue!=null)
						f.set(returnObject, fieldValue);
				}
			}
		}
			catch(Exception e){
				Log.e("Cleandroid", e.getMessage(),e);
			}
		
		
		return returnObject;
		
	}
	
	

}
