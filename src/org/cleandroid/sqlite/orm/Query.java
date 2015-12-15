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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.cleandroid.sqlite.orm.annotation.Entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Query<T> {
	
	String queryString;
	Class<?> resultType;
	SQLiteDatabase db;
	Map<Integer,String> queryParams = new TreeMap<Integer,String>();
	boolean logSQL;
	
	Query(String queryString, Class<T> resultType, SQLiteDatabase db, boolean logSQL) {
		super();
		this.queryString = queryString;
		this.resultType = resultType;
		this.db = db;
		this.logSQL = logSQL;
	}
	
	@SuppressWarnings("unchecked")
	private T getResult(Cursor cursor){
		if(resultType.isAnnotationPresent(Entity.class)){
			try{
				return (T) DataFetcher.cursorToObject(cursor, resultType, db, logSQL);
			}
			catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		else
			return (T) DataFetcher.getTypedValueFromCursor(cursor, 0, resultType);
	}
	
	public T getSingleResult(){
		Cursor cursor = getCursor();
		
		if(!cursor.moveToFirst())
			return null;
		return getResult(cursor);
	}
	
	public List<T> getResultList(){
		List<T> results = new ArrayList<T>();
		Cursor cursor = getCursor();
		while(cursor.moveToNext()){
			results.add(getResult(cursor));
		}
		
		return results;
	}
	
	public Query<T> setParameter(int index, Object value){
		queryParams.put(index, String.valueOf(value));
		return this;
	}
	
	private Cursor getCursor(){
		String[] selectionArgs = new String[queryParams.size()];
		int i = 0;
		for(int key:queryParams.keySet())
			selectionArgs[i++] = queryParams.get(key);
		return db.rawQuery(queryString, selectionArgs);
	}
	
	
	
	

}
