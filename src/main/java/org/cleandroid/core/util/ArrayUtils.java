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

public class ArrayUtils {
	
	
	public static String implode(String delimiter, Object[] values){
		String result = "";
		for(Object val:values){
			result = result.concat(String.valueOf(val)).concat(delimiter);
		}
		return org.cleandroid.core.util.StringUtils.rtrim(result, delimiter);
	}


}
