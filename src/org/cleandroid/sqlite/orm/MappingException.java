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

import java.lang.reflect.Field;

import org.cleandroid.sqlite.orm.annotation.Entity;

public class MappingException extends RuntimeException{
	

	private static final long serialVersionUID = 1L;

	private MappingException(String message){
		super(message);
	}
	
	public static MappingException invalidType(Field field){
		return new MappingException("Invalid type was found for the field: " + field.getDeclaringClass().getName() + ":" + field.getName() + ". Acceptable types are "+Entity.class.getName()+" for association fields and " + ORMUtils.getAcceptableFieldTypesAsString() + " for non-association fields");
	}

	public static MappingException noPrimaryKey(Class<?> entityClass){
		return new MappingException("No primary key was defined for the entity " + entityClass);
	}
	
	public static MappingException multiplePrimaryKeys(Class<?> entityClass){
		return new MappingException("Multiple primary keys were defined for the entity " + entityClass.getName() + ". Only one should be defined");

	}
	
	public static MappingException multiplePersistenceTypes(Field field){
		return new MappingException("Multiple persistent type annotations found for the field: " + field.getDeclaringClass().getName() + ":"+ field.getName());

	}
	
	public static MappingException invalidTypeForAutoIncrement(Field field){
		return new MappingException("Type " + field.getType().getName() + " for the field " + field.getDeclaringClass().getName() + ":" + field.getName() + " cannot be autoincremented");
	}
	
	public static MappingException invalidMappedReference(Field field){
		return new MappingException("Mapped reference " + field.getDeclaringClass().getName() + ":" + field.getName() + " is not an entity type");
	}
	
	public static MappingException mappedReferenceNotExists(Class<?> targetClass, String mappedBy){
		return new MappingException("Mapped reference " + targetClass.getName() + ":" + mappedBy + " does not exist");
	}
	
	public static MappingException propertyNotExists(Class<?> entityClass, String propertyName){
		return new MappingException("Property " + propertyName + " was not found for the entity " + entityClass.getName());
	}
}
