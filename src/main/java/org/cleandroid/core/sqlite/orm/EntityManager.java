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
import java.util.ArrayList;
import java.util.List;

import org.cleandroid.core.sqlite.DataSource;
import org.cleandroid.core.sqlite.DataSourceManager;
import org.cleandroid.core.sqlite.orm.annotation.AutoIncrement;
import org.cleandroid.core.sqlite.orm.annotation.Entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class EntityManager {
	
	private SQLiteDatabase database;
	private boolean logSQL = false;
	
	public EntityManager(DataSource datasource){
		this.database = DataSourceManager.openOrCreateDataSource(datasource.getName());
		if(datasource.getProperty("log-sql")!=null)
			logSQL = datasource.getProperty("log-sql").equals("true");
	}
	
	public void persist(Object entity){
		checkEntityArg(entity.getClass());
		
		try{
			List<SQLiteStatement> statements = null;
			
			Object idValue = ORMUtils.getEntityPrimaryKeyField(entity.getClass()).get(entity);
			Object tmpEntity = find(entity.getClass(), idValue);
			if(tmpEntity==null)
				statements = CommandBuilder.createInsertQuery(entity,database);
			else
				statements = CommandBuilder.createUpdateQuery(entity,database);
			
				SQLiteStatement stmt = statements.get(statements.size()-1);
				if(tmpEntity==null){
					if(logSQL)
						Log.d("Cleandroid ORM", stmt.toString());
					long rowid = stmt.executeInsert(); 
					setEntityAIPK(entity, rowid);
				}
				else{
					stmt.execute();
				}

				for(int i=0;i<statements.size()-1;i++){
					stmt = statements.get(i);
					CommandBuilder.bindStatementValue(stmt, ORMUtils.getEntityPrimaryKeyField(entity.getClass()).get(entity), 1);
					stmt.executeInsert();
					
					if(logSQL)
						Log.d("Cleandroid ORM",stmt.toString());

				}
			
			
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	
	
	public <T> T find(Class<T> entityClass, Object id){
		checkEntityArg(entityClass);
		T entity = null;
		String pkColumn = ORMUtils.getColumnName(ORMUtils.getEntityPrimaryKeyField(entityClass));
		Cursor cursor = database.rawQuery("SELECT * FROM ".concat(ORMUtils.getTableName(entityClass).concat(" WHERE ").concat(pkColumn).concat("=").concat(String.valueOf(id))), null);
		if(cursor.moveToFirst()){
			try {
				entity = DataFetcher.cursorToObject(cursor, entityClass,database,logSQL);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return entity;
	}

	public <T> List<T> findAll(Class<T> entityClass){
		checkEntityArg(entityClass);
		List<T> entities = new ArrayList<T>();
		String query = "SELECT * FROM ".concat(ORMUtils.getTableName(entityClass));

		if(logSQL)
			Log.d("Cleandroid ORM", query);

		Cursor cursor = database.rawQuery(query, null);
		while(cursor.moveToNext()){
			try{
				entities.add(DataFetcher.cursorToObject(cursor, entityClass,database,logSQL));
			}
			catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		return entities;
	}

	public <T> T findOneBy(Class<T> entityClass, String property, Object value){
		checkEntityArg(entityClass);
		String columnName = ORMUtils.getColumnName(entityClass, property);
		if(columnName==null)
			throw org.cleandroid.core.sqlite.orm.MappingException.propertyNotExists(entityClass, property);
		String query = "SELECT * FROM ".concat(ORMUtils.getTableName(entityClass)).concat(" WHERE ").concat(property).concat("=?");

		if(logSQL)
			Log.d("Cleandroid ORM", query);

		Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(value)});
		try{
		cursor.moveToFirst();
		T entity = DataFetcher.cursorToObject(cursor, entityClass,database,logSQL);
		return entity;
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}

	}

	public <T> List<T> findBy(Class<T> entityClass, String property, Object value){
		checkEntityArg(entityClass);
		String columnName = ORMUtils.getColumnName(entityClass, property);
		if(columnName==null)
			throw MappingException.propertyNotExists(entityClass, property);

		String query = "SELECT * FROM ".concat(ORMUtils.getTableName(entityClass)).concat(" WHERE ").concat(property).concat("=?");

		if(logSQL)
			Log.d("Cleandroid ORM", query);

		Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(value)});
		List<T> entities = new ArrayList<T>();
		try{
				while(cursor.moveToNext()){
					cursor.moveToFirst();
					T entity = DataFetcher.cursorToObject(cursor, entityClass,database,logSQL);
					entities.add(entity);
				}
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
		return entities;
	}


	public void remove(Class<?> entityClass, Object id){
		checkEntityArg(entityClass);
		String pkColumn = ORMUtils.getEntityPrimaryKeyColumnName(entityClass);
		String command = "DELETE FROM ".concat(ORMUtils.getTableName(entityClass).concat(" WHERE ").concat(pkColumn).concat("=?"));
		if(logSQL)
			Log.d("Cleandroid ORM",command);
		database.execSQL(command, new String[]{String.valueOf(id)});
	}

	public void removeAll(Class<?> entityClass){
		checkEntityArg(entityClass);
		String command = "DELETE FROM ".concat(ORMUtils.getTableName(entityClass));
		if(logSQL)
			Log.d("Cleandroid ORM",command);
		database.execSQL(command);
	}

	public <T> Query<T> createQuery(String query, Class<T> resultType){
		return new Query<T>(query, resultType, database, logSQL);
	}
	
	public Cursor createQuery(String query){
		return createQuery(query, new String[]{});
	}
	
	public Cursor createQuery(String query, String[] selectionArgs){
		return database.rawQuery(query, selectionArgs);
	}
	
	public void beginTransaction(){
		database.beginTransaction();
	}
	
	public void commitTransaction(){
		database.setTransactionSuccessful();
		database.endTransaction();
	}
	
	public void rollBackTransaction(){
		database.endTransaction();
	}
	

	private void setEntityAIPK(Object entity, long pkvalue) throws IllegalAccessException, IllegalArgumentException{
		Field pkf = ORMUtils.getEntityPrimaryKeyField(entity.getClass());
		if(pkf.isAnnotationPresent(AutoIncrement.class)){
			if(pkf.getType().equals(int.class) || pkf.getType().equals(Integer.class))
				pkf.set(entity, (int)pkvalue);
			else
				pkf.set(entity, pkvalue);
		}
	}
	

	
	private void checkEntityArg(Class<?> entityClass){
		if(!entityClass.isAnnotationPresent(Entity.class))
			throw PersistenceException.invalidEntity(entityClass);
		
			
	}
	
	
	
	
	

}
