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
package org.cleandroid.core.util;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class TypeUtils {
	
	@SuppressWarnings({ "unchecked" })
	public static <T> T getTypedValue(String value, Class<T> type,Object...extraArgs){
		if(value!=null){
		if (type.equals(int.class)
				|| type.equals(Integer.class))
			return (T) NumberUtils.parseInt(value.toString(),0);
		else if (type.equals(short.class)
				|| type.equals(Short.class))
			return (T) NumberUtils.parseShort(value.toString(), (short) 0);
		else if (type.equals(long.class)
				|| type.equals(Long.class))
			return (T) NumberUtils.parseLong(value.toString(),0L);
		else if (type.equals(double.class)
				|| type.equals(Double.class))
			return (T) NumberUtils.parseDouble(value.toString(),0D);
		else if (type.equals(float.class)
				|| type.equals(Float.class))
			return (T) NumberUtils.parseFloat(value.toString(),0F);
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
		
		else if(type.equals(boolean.class) || type.equals(Boolean.class))
			return (T) ((Boolean) Boolean.parseBoolean(value));
		else if(type.equals(Date.class)){
			String dateFormat = "dd-MM-yyyy HH:mm:ss";
			if(extraArgs.length>0)
				dateFormat = (String) extraArgs[0];
			try {
				return (T) new SimpleDateFormat(dateFormat, Locale.FRANCE).parse(value);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
			
		}
		
		}

		
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
		return isTypeInRange(object.getClass(),types);
	}

	public static boolean isTypeInRange(Class<?> type, Class<?>...types){
		for(Class<?> t:types){
			if(type.equals(t))
				return true;
		}

		return false;
	}

}
