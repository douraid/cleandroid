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

import org.cleandroid.validation.CleandroidValidator;
import org.cleandroid.validation.annotation.Length;

import java.lang.annotation.Annotation;

public class LengthValidator implements CleandroidValidator {

	private Length constraint;
	
	@Override
	public boolean isValid(Object value, Annotation constraint) {
		if(value==null || String.valueOf(value).isEmpty())
			return true;
		this.constraint = (Length) constraint;
		String str = String.valueOf(value);
		return str.length() >= this.constraint.min() && str.length() <= this.constraint.max();
	}

	@Override
	public String getErrorMessage() {
		return constraint.message().replace("{min}", String.valueOf(constraint.min()))
				   				   .replace("{max}", String.valueOf(constraint.max()));
	}

}
