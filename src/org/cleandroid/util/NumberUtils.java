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

public class NumberUtils {
	
	public static Integer parseInt(String value){
		try{
			return Integer.parseInt(value);
		}
		catch(NumberFormatException e){
			return 0;
		}
	}
	
	public static Short parseShort(String value){
		try{
			return Short.parseShort(value);
		}
		catch(NumberFormatException e){
			return 0;
		}
	}
	
	public static Double parseDouble(String value){
		try{
			return Double.parseDouble(value);
		}
		catch(NumberFormatException e){
			return 0D;
		}
	}
	
	public static Float parseFloat(String value){
		try{
			return Float.parseFloat(value);
		}
		catch(NumberFormatException e){
			return 0F;
		}
	}
	
	public static Long parseLong(String value){
		try{
			return Long.parseLong(value);
		}
		catch(NumberFormatException e){
			return 0L;
		}
	}
	

}
