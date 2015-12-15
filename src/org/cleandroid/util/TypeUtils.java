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


public class TypeUtils {
	
	@SuppressWarnings({ "unchecked" })
	public static <T> T getTypedValue(String value, Class<T> type){
		if(value!=null){
		if (type.equals(int.class)
				|| type.equals(Integer.class))
			return (T) NumberUtils.parseInt(value.toString());
		else if (type.equals(short.class)
				|| type.equals(Short.class))
			return (T) NumberUtils.parseShort(value.toString());
		else if (type.equals(long.class)
				|| type.equals(Long.class))
			return (T) NumberUtils.parseLong(value.toString());
		else if (type.equals(double.class)
				|| type.equals(Double.class))
			return (T) NumberUtils.parseDouble(value.toString());
		else if (type.equals(float.class)
				|| type.equals(Float.class))
			return (T) NumberUtils.parseFloat(value.toString());
		else if (type.equals(String.class))
			return (T) value.toString();
		else if (type.equals(char.class)
				|| type.equals(Character.class))
			return (T) ((Character)value.toString().charAt(0));
		else if (type.equals(byte.class)
				|| type.equals(Byte.class))
			return (T) ((Byte)value.toString().getBytes()[0]);
		else if (type.equals(byte[].class) || type.equals(Byte[].class))
			return (T) value.toString().getBytes();
		}
		else if(type.equals(boolean.class) || type.equals(Boolean.class))
			return (T) ((Boolean) Boolean.parseBoolean(value));
		
		return null;
	}
	
	public static boolean isPrimitiveOrWrapper(Class<?> cls){
		if(cls.equals(String.class) ||
		   cls.equals(Double.class) ||
		   cls.equals(Integer.class) ||
		   cls.equals(Character.class) ||
		   cls.equals(Long.class) ||
		   cls.equals(Float.class) ||
		   cls.isPrimitive())
			return true;
		return false;
	}
	
	public static boolean isTypeInRange(Object object, Class<?>...types){
		
		for(Class<?> type:types){
			if(type.equals(object.getClass()))
				return true;
		}
		
		return false;
	}

}
