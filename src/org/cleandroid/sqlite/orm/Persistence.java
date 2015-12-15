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

import java.util.HashMap;
import java.util.Map;

import org.cleandroid.sqlite.DataSource;

public class Persistence {
	
	private static Map<String,EntityManager> entityManagers = new HashMap<String, EntityManager>();
	
	public static void registerEntityManager(DataSource dataSource){
		entityManagers.put(dataSource.getName(), new EntityManager(dataSource));
	}

	public static EntityManager getEntityManager(String datasourceName){
		return entityManagers.get(datasourceName);
	}
}
