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
package org.cleandroid.core.sqlite.orm;

import java.lang.reflect.Field;

import org.cleandroid.core.sqlite.orm.annotation.Entity;

public class MappingException extends RuntimeException{
	

	private static final long serialVersionUID = 1L;

	private MappingException(String message){
		super(message);
	}
	
	public static org.cleandroid.core.sqlite.orm.MappingException invalidType(Field field){
		return new org.cleandroid.core.sqlite.orm.MappingException("Invalid type was found for the field: " + field.getDeclaringClass().getName() + ":" + field.getName() + ". Acceptable types are "+Entity.class.getName()+" for association fields and " + ORMUtils.getAcceptableFieldTypesAsString() + " for non-association fields");
	}

	public static org.cleandroid.core.sqlite.orm.MappingException noPrimaryKey(Class<?> entityClass){
		return new org.cleandroid.core.sqlite.orm.MappingException("No primary key was defined for the entity " + entityClass);
	}
	
	public static org.cleandroid.core.sqlite.orm.MappingException multiplePrimaryKeys(Class<?> entityClass){
		return new org.cleandroid.core.sqlite.orm.MappingException("Multiple primary keys were defined for the entity " + entityClass.getName() + ". Only one should be defined");

	}
	
	public static org.cleandroid.core.sqlite.orm.MappingException multiplePersistenceTypes(Field field){
		return new org.cleandroid.core.sqlite.orm.MappingException("Multiple persistent type annotations found for the field: " + field.getDeclaringClass().getName() + ":"+ field.getName());

	}
	
	public static org.cleandroid.core.sqlite.orm.MappingException invalidTypeForAutoIncrement(Field field){
		return new org.cleandroid.core.sqlite.orm.MappingException("Type " + field.getType().getName() + " for the field " + field.getDeclaringClass().getName() + ":" + field.getName() + " cannot be autoincremented");
	}
	
	public static org.cleandroid.core.sqlite.orm.MappingException invalidMappedReference(Field field){
		return new org.cleandroid.core.sqlite.orm.MappingException("Mapped reference " + field.getDeclaringClass().getName() + ":" + field.getName() + " is not an entity type");
	}
	
	public static org.cleandroid.core.sqlite.orm.MappingException mappedReferenceNotExists(Class<?> targetClass, String mappedBy){
		return new org.cleandroid.core.sqlite.orm.MappingException("Mapped reference " + targetClass.getName() + ":" + mappedBy + " does not exist");
	}
	
	public static org.cleandroid.core.sqlite.orm.MappingException propertyNotExists(Class<?> entityClass, String propertyName){
		return new org.cleandroid.core.sqlite.orm.MappingException("Property " + propertyName + " was not found for the entity " + entityClass.getName());
	}
}
