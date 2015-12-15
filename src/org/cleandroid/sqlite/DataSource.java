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
package org.cleandroid.sqlite;

import java.util.List;



public class DataSource {
	
	private String name;
	private String entityPackages;
	private List<DataSourceProperty> properties;

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEntityPackages() {
		return entityPackages;
	}
	public void setEntityPackages(String entityPackages) {
		this.entityPackages = entityPackages;
	}
	public List<DataSourceProperty> getProperties() {
		return properties;
	}
	public void setProperties(List<DataSourceProperty> properties) {
		this.properties = properties;
	}

	public String getProperty(String property){
		for(DataSourceProperty p:properties){
			if(p.getKey().equals(property))
				return p.getValue();
		}
		return null;
	}
	
	
	

}