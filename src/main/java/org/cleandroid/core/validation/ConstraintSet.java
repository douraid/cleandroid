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
package org.cleandroid.core.validation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.view.View;

public class ConstraintSet {
	
	private Map<Annotation,Constraint> constraints = new HashMap<Annotation,Constraint>();
	private View view;
	private String property;
	
	ConstraintSet(View view){
		this.view = view;
	}
	
	ConstraintSet(String property){
		this.property = property;
	}
	
	void addConstraint(Annotation rule,Constraint constraint){
		constraints.put(rule, constraint);
	}
	
	public boolean isValid(){
		for(Annotation a:constraints.keySet()){
			if(!constraints.get(a).isValid())
				return false;
			
		}
		return true;
	}
	
	
	
	public List<String> getAllMessages(){
		List<String> messages = new ArrayList<String>();
		for(Annotation a:constraints.keySet()){
			if(!constraints.get(a).isValid())
				messages.add(constraints.get(a).getMessage());
		}
		return messages;
	}
	
	public Constraint getConstraint(Annotation constraint){
		return constraints.get(constraint);
	}
	
	public Set<Constraint> getInvalidatedConstraints(){
		Set<Constraint> constraintsSet = new HashSet<Constraint>();
		
		for(Annotation a:constraints.keySet()){
			if(!constraints.get(a).isValid())
				constraintsSet.add(constraints.get(a));
		}
		
		return constraintsSet;
	}
	
	public View getView(){
		return view;
	}
	
	public String getProperty(){
		return property;
	}

}
