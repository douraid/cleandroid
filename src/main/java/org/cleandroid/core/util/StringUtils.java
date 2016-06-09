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

public class StringUtils {
	
	public static String capitalize(String s){
		return s.replace(String.valueOf(s.charAt(0)), String.valueOf(s.charAt(0)).toUpperCase());
	}
	
	private static String splitCamelCase(String s) {
		   return s.replaceAll(
		      String.format("%s|%s|%s",
		         "(?<=[A-Z])(?=[A-Z][a-z])",
		         "(?<=[^A-Z])(?=[A-Z])",
		         "(?<=[A-Za-z])(?=[^A-Za-z])"
		      ),
		      " "
		   );
		}
	
	public static String toLabel(String fieldName){
		return capitalize(fieldName.charAt(0) + splitCamelCase(fieldName.substring(1)));
	}
	
	public static String toUnderscored(String fieldName){
		return splitCamelCase(fieldName).replace(" ", "_").toLowerCase();
	}
	
	public static String rtrim(String s,String pattern){
		while(s.substring(s.length()-pattern.length()).equals(pattern))
			s = s.substring(0,s.length()-pattern.length());
		return s;
	}
	
	public static String repeat(String str, int n){
		for(int i=1;i<n;i++)
			str = str.concat(str);
		return str;
	}

	public static String maxSubstring(String str, int start, int length){
		if(str.substring(start).length()>length)
			return str.substring(start,length);
		return str.substring(start);
	}
	


}
