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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

public class CollectionUtils {
	
	public static Class<?> getCollectionType(Field field){
		if(field.getGenericType() instanceof ParameterizedType){
			ParameterizedType type = (ParameterizedType) field.getGenericType();
			return (Class<?>) type.getActualTypeArguments()[0];
		}
		return Object.class;
	}
	
	public static <T> T[] toTypedArray(Collection<?> collection, Class<T> type){
		@SuppressWarnings("unchecked")
		T[] array = (T[]) Array.newInstance(type, collection.size());
		Object[] data = collection.toArray();
		for(int i=0;i<array.length;i++){
			array[i] = type.cast(data[i]);
		}
		return array;
		
	}

}
