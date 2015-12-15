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
import org.cleandroid.validation.annotation.NotNull;

public class NotNullValidator implements CleandroidValidator{

	NotNull constraint;
	
	@Override
	public boolean isValid(Object value, Annotation constraint) {
		this.constraint = (NotNull) constraint;
		return value!=null;
	}

	@Override
	public String getErrorMessage() {
		return constraint.message();
	}

}
