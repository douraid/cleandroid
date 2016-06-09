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


import android.app.Activity;
import android.util.Log;

public class FieldsDependencyInjector {

	private static org.cleandroid.core.di.FieldsDependencyInjector instance;

	private FieldsDependencyInjector() {
	}

	public static org.cleandroid.core.di.FieldsDependencyInjector getInstance() {
		if (instance == null)
			instance = new org.cleandroid.core.di.FieldsDependencyInjector();
		return instance;
	}

	public void injectDependencies(Activity activity){
		for (Field field : activity.getClass().getDeclaredFields()) {
			field.setAccessible(true);

			for (Annotation a : field.getDeclaredAnnotations()) {
				Object value = DependencyManager.getDependencyFromAnnotation(a, activity, field.getType());
				if(value!=null)
					try{
						field.set(activity, value);
					}
					catch(Exception e){
						Log.e("CleanDroid", e.getMessage(), e);
					}
			}

		}
	}

}
