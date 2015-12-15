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
package org.cleandroid.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.view.View;

public class ConstraintSetCollection {
	
	
	private Map<View,ConstraintSet> viewConstraints = new HashMap<View,ConstraintSet>();
	private Set<ConstraintSet> beanConstraints = new HashSet<ConstraintSet>();

	
	void addViewConstraintSet(View view, ConstraintSet constraintSet){
		viewConstraints.put(view,constraintSet);
	}
	
	void addBeanConstraintSet(Object bean, ConstraintSet constraintSet){
		beanConstraints.add(constraintSet);
	}
	
	public boolean isValid(){
		boolean valid = true;
		for(View v:viewConstraints.keySet()){
			if(!viewConstraints.get(v).isValid()){
				valid = false;
				break;
			}
		}
		
		for(ConstraintSet cs:beanConstraints)
			if(!cs.isValid()){
				valid = false;
				break;
			}
		
		
		return valid;
	}
	
	public List<String> getAllMessages(){
		List<String> messages = new ArrayList<String>();
		for(View v:viewConstraints.keySet()){
			messages.addAll(viewConstraints.get(v).getAllMessages());
		}
		
		for(ConstraintSet cs:beanConstraints){
			messages.addAll(cs.getAllMessages());
		}
			
		return messages;
	}

	public ConstraintSet getViewConstraintSet(View view){
		return viewConstraints.get(view);
	}
	
	public ConstraintSet getPropertyConstraintSet(String propertyName){
		for(ConstraintSet cs:beanConstraints){
			if(cs.getProperty().equals(propertyName))
				return cs;
		}
		return null;
	}
}
