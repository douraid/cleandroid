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


import org.cleandroid.core.application.App;
import org.cleandroid.core.sqlite.DataSource;

public class Persistence {
	
	
	public static void registerEntityManager(DataSource dataSource){
		App.getContainer().registerObject(dataSource.getName()+"_em", new org.cleandroid.core.sqlite.orm.EntityManager(dataSource));
	}

	public static org.cleandroid.core.sqlite.orm.EntityManager getEntityManager(String datasourceName){
		return App.getContainer().getObject(EntityManager.class,datasourceName+"_em");
	}
}
