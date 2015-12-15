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
import java.util.Set;

import org.cleandroid.application.App;
import org.cleandroid.config.Configuration;
import org.cleandroid.sqlite.orm.ORMUtils;
import org.cleandroid.sqlite.orm.Persistence;
import org.cleandroid.sqlite.orm.SchemaGenerator;
import org.cleandroid.sqlite.orm.annotation.Entity;
import org.cleandroid.util.CollectionUtils;

import android.database.sqlite.SQLiteDatabase;



public class DatasourcesConfig extends Configuration{

	List<DataSource> dataSources;


	public List<DataSource> getDataSources() {
		return dataSources;
	}

	public void setDataSources(List<DataSource> dataSources) {
		this.dataSources = dataSources;
	}


	public DataSource getDataSource(String name){
		for(DataSource ds:dataSources){
			if(ds.getName().equalsIgnoreCase(name))
				return ds;
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	@Override
	protected void onConfigurationLoaded() {
		
		for(DataSource ds:dataSources){
			SQLiteDatabase db = DataSourceManager.openOrCreateDataSource(ds.getName());
			
			try{
			Set<Class<Entity>> entityClasses = ORMUtils.scanPackagesForEntityClasses(ds.getEntityPackages().split(";"));
			
				boolean logSQL  = (ds.getProperty("log-sql")!=null) ? ds.getProperty("log-sql").equals("true"):false;
				String generationStrategy = ds.getProperty("generation.strategy")!=null ? ds.getProperty("generation.strategy"):"create";
				SchemaGenerator.generateSchema(logSQL, db,generationStrategy,CollectionUtils.toTypedArray(entityClasses,Class.class));
				Persistence.registerEntityManager(ds);
			
			}
			catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		
	
	}



}
