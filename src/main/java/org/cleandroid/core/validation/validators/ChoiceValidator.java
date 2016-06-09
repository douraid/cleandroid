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

import java.lang.annotation.Annotation;
import java.util.Arrays;

import org.cleandroid.core.util.ArrayUtils;
import org.cleandroid.core.validation.CleandroidValidator;
import org.cleandroid.core.validation.annotation.Choice;

public class ChoiceValidator implements CleandroidValidator{

	Choice constraint;
	@Override
	public boolean isValid(Object value, Annotation constraint) {
		if(value==null || String.valueOf(value).isEmpty())
			return true;
		this.constraint = (Choice) constraint;
		return Arrays.asList(this.constraint.choices()).contains(String.valueOf(value));
	}

	@Override
	public String getErrorMessage() {
		return constraint.message().replace("{choices}", ArrayUtils.implode(", ", constraint.choices()));
	}

}
