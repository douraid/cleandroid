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
package org.cleandroid.sqlite.orm;

public class PersistenceException extends RuntimeException{

	
	private static final long serialVersionUID = 1L;
	
	
	private PersistenceException(String message){
		super(message);
	}
	
	public static PersistenceException invalidEntity(Class<?> entityClass){
		return new PersistenceException(entityClass.getName() + " is not an entity");
	}
	

}
