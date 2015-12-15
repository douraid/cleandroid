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
import org.cleandroid.validation.Comparable;
import org.cleandroid.validation.annotation.GreaterThanOrEqual;

import android.annotation.SuppressLint;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GreaterThanOrEqualValidator implements CleandroidValidator{

	GreaterThanOrEqual constraint;
	private static  SimpleDateFormat dateFormat;


	
	@SuppressLint("SimpleDateFormat")
	@Override
	public boolean isValid(Object value, Annotation constraint) {
		if(value==null || String.valueOf(value).isEmpty())
			return true;
		this.constraint = (GreaterThanOrEqual) constraint;
		dateFormat = new SimpleDateFormat(this.constraint.dateFormat());
		try{
		if(this.constraint.dataType().equals(Comparable.DATE))
			return ((Date)value).compareTo(dateFormat.parse(this.constraint.value()))>=0;
		else
			return Double.parseDouble(String.valueOf(value)) >= Double.parseDouble(this.constraint.value());
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getErrorMessage() {
		return constraint.message().replace("{value}", String.valueOf(constraint.value()));
	}


}
