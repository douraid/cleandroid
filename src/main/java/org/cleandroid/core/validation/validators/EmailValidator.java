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
package org.cleandroid.core.validation.validators;

import org.cleandroid.core.validation.CleandroidValidator;
import org.cleandroid.core.validation.annotation.Email;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

public class EmailValidator implements CleandroidValidator {

	private Email constraint;
	private static final String EMAIL_REGEX = "^\\s*?(.+)@(.+?)\\s*$";
	
	@Override
	public boolean isValid(Object value, Annotation constraint) {
		if(value==null || String.valueOf(value).isEmpty())
			return true;
		this.constraint = (Email) constraint;
		return Pattern.compile(EMAIL_REGEX).matcher(EMAIL_REGEX).matches();
	}

	@Override
	public String getErrorMessage() {
		return constraint.message();
	}

}
