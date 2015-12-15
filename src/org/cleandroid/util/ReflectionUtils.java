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
package org.cleandroid.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {
	
	public static List<Field> getAnnotatedFields(Class<?> targetClass, Class<? extends Annotation> annotationType){
		List<Field> fields = new ArrayList<Field>();
		for(Field f: targetClass.getDeclaredFields()){
			if(f.isAnnotationPresent(annotationType))
				fields.add(f);
		}
		return fields;
	}

}
