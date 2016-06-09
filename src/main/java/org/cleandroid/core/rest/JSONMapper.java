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
package org.cleandroid.core.rest;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.cleandroid.core.util.StringUtils;
import org.cleandroid.core.util.TypeUtils;
import org.cleandroid.core.rest.annotation.DateFormat;
import org.json.JSONArray;
import org.json.JSONObject;


public class JSONMapper {
	
	@SuppressWarnings("unchecked")
	public static <T> T parseObject(String jsonString, Class<T> type){
		try{
			JSONObject dataObject = new JSONObject(jsonString);
			T object = type.newInstance();
			for(Field f:type.getDeclaredFields()){
				f.setAccessible(true);
				Iterator<String> iterator = dataObject.keys();
				while(iterator.hasNext()){
					String key = iterator.next();
					if(key.equals(f.getName())){
						
						if(Collection.class.isAssignableFrom(f.getType()))
							f.set(object, parseCollection(dataObject.getJSONArray(key).toString(), f.getType()));
						else{
							if(dataObject.get(key) instanceof JSONObject)
								f.set(object,parseObject(dataObject.getJSONObject(key).toString(),f.getType()));
							else if(f.isAnnotationPresent(DateFormat.class))
								f.set(object, TypeUtils.getTypedValue(dataObject.getString(key), f.getType(),f.getAnnotation(DateFormat.class).value()));
							else
								f.set(object, TypeUtils.getTypedValue(dataObject.getString(key), f.getType()));
							
						}
						break;
					}
					
				}
			}
			return object;
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static <T> Collection<T> parseCollection(String jsonString, Class<T> type){
		Collection<T> collection = new ArrayList<T>();
		try{
			JSONArray dataArray = new JSONArray(jsonString);
			for(int i=0;i<dataArray.length();i++){
				if(TypeUtils.isPrimitiveOrWrapper(type))
					collection.add(TypeUtils.getTypedValue(dataArray.getString(i), type));
				else
					collection.add(parseObject(dataArray.getJSONObject(i).toString(),type));
			}
			
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
		return collection;
	}
	
	@SuppressWarnings("unchecked")
	public static String parseJson(Serializable object){
		if(TypeUtils.isPrimitiveOrWrapper(object.getClass()))
			return String.valueOf(object);
		
		String jsonString = "{";
		for(Field f:object.getClass().getDeclaredFields()){
			f.setAccessible(true);
			jsonString = jsonString.concat(f.getName()).concat(":");
			try{
				Object value = null;
				
			
				if(Collection.class.isAssignableFrom(f.getType()))
					value = parseJson((Collection<Serializable>) value);
				else{
					value = String.valueOf(f.get(object));
					if(TypeUtils.isTypeInRange(value, String.class,CharSequence.class))
						value = "\"".concat(String.valueOf(value)).concat("\"");
				}
				
				jsonString = jsonString.concat(String.valueOf(value)).concat(", ");
			}
			
			catch(Exception e){
				throw new RuntimeException(e);
			}
			
		}
		jsonString = StringUtils.rtrim(jsonString, ", ").concat("}");
		
		return jsonString;
	}
	
	public static String parseJson(Collection<Serializable> objects){
		String jsonString = "[";
		for(Object object:objects){
			if(TypeUtils.isPrimitiveOrWrapper(object.getClass()))
				jsonString = jsonString.concat(String.valueOf(object));
			else
				jsonString = jsonString.concat(parseJson((Serializable) object));
			jsonString = jsonString.concat(", ");
		}
		
		jsonString = StringUtils.rtrim(jsonString, ", ").concat("]");
		
		return jsonString;
	}
	
	public static String parseJson(Serializable[] objects){
		return parseJson(Arrays.asList(objects));
	}
	
	

}
