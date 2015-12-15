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
import org.cleandroid.validation.annotation.Range;

import java.lang.annotation.Annotation;

public class RangeValidator implements CleandroidValidator {
	
	private Range constraint;

	@Override
	public boolean isValid(Object value, Annotation constraint) {
		if(value==null || String.valueOf(value).isEmpty())
			return true;
		this.constraint = (Range) constraint;
		double val = Double.parseDouble(String.valueOf(value));
		return val >= this.constraint.min() && val <= this.constraint.max();
	}

	@Override
	public String getErrorMessage() {
		return constraint.message().replace("{min}", String.valueOf(constraint.min()))
								   .replace("{max}", String.valueOf(constraint.max()));
	}

}
