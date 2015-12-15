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
package org.cleandroid.validation.validators;

import java.lang.annotation.Annotation;

import org.cleandroid.validation.CleandroidValidator;
import org.cleandroid.validation.annotation.ABANumber;


public class ABANumberValidator implements CleandroidValidator{

	ABANumber constraint;
	
	@Override
	public boolean isValid(Object value, Annotation constraint) {
		if(value==null || String.valueOf(value).isEmpty())
			return true;
		this.constraint = (ABANumber) constraint;
		try{
			String routing = String.valueOf(value);
			if(routing.length()==9){
				if(routing.charAt(0)!=5){
					int d1 = Integer.parseInt(String.valueOf(routing.charAt(0)));
					int d2 = Integer.parseInt(String.valueOf(routing.charAt(1)));
					int d3 = Integer.parseInt(String.valueOf(routing.charAt(2)));
					int d4 = Integer.parseInt(String.valueOf(routing.charAt(3)));
					int d5 = Integer.parseInt(String.valueOf(routing.charAt(4)));
					int d6 = Integer.parseInt(String.valueOf(routing.charAt(5)));
					int d7 = Integer.parseInt(String.valueOf(routing.charAt(6)));
					int d8 = Integer.parseInt(String.valueOf(routing.charAt(7)));
					int d9 = Integer.parseInt(String.valueOf(routing.charAt(8)));
					return (3*(d1+d4+d7) + 7*(d2*d5*d8) + (d3+d6+d9))%10==0;

				}
			}
			return false;

		}
		catch(Exception e){
			return false;
		}
	}

	@Override
	public String getErrorMessage() {
		return constraint.message();
	}

}
