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
import org.cleandroid.validation.annotation.NotBlank;

import java.lang.annotation.Annotation;

public class NotBlankValidator implements CleandroidValidator {

	private NotBlank constraint;
	
	@Override
	public boolean isValid(Object value, Annotation constraint) {
		if(value==null)
			return true;
		this.constraint = (NotBlank) constraint;
		return !String.valueOf(value).trim().isEmpty();
	}

	@Override
	public String getErrorMessage() {
		return this.constraint.message();
	}

}
