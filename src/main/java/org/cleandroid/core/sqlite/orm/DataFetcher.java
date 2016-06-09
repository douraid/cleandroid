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
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.cleandroid.core.sqlite.orm.annotation.Column;
import org.cleandroid.core.sqlite.orm.annotation.Embedded;
import org.cleandroid.core.sqlite.orm.annotation.Id;
import org.cleandroid.core.sqlite.orm.annotation.ManyToMany;
import org.cleandroid.core.sqlite.orm.annotation.ManyToOne;
import org.cleandroid.core.sqlite.orm.annotation.OneToMany;
import org.cleandroid.core.sqlite.orm.annotation.OneToOne;
import org.cleandroid.core.util.CollectionUtils;
import org.cleandroid.core.util.TypeUtils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataFetcher {
	
	@SuppressWarnings("unchecked")
	public static <T> T cursorToObject(Cursor cursor, Class<T> objectClass, SQLiteDatabase db, boolean logSQL) throws InstantiationException, IllegalAccessException{
		
		Object object = objectClass.newInstance();
			
			List<Field> fields = ORMUtils.getEntityFields(objectClass);
			
			for(Field f:fields){
				f.setAccessible(true);
				
				if(f.isAnnotationPresent(Id.class) || f.isAnnotationPresent(Column.class)){
					
					Object value = null;
					int colIndex = cursor.getColumnIndex(ORMUtils.getColumnName(f));
					
					if(f.getType().equals(Date.class)){
						long timestamp = cursor.getLong(colIndex);
						value = new Date(timestamp);
					}
					
					else if(f.getType().equals(boolean.class) || f.getType().equals(Boolean.class))
						value = cursor.getLong(colIndex)==1;
					
					else if(f.getType().equals(byte[].class) || f.getType().equals(Byte[].class))
						value = cursor.getBlob(colIndex);
					else
						value = TypeUtils.getTypedValue(cursor.getString(colIndex), f.getType());
					
					if(value!=null)
						f.set(object, value);
					
				}
				
				else if(f.isAnnotationPresent(Embedded.class)){
					Object embeddedObject = cursorToObject(cursor,f.getType(),db,logSQL);
					f.set(object, embeddedObject);
				}
				
				else if(f.isAnnotationPresent(OneToOne.class) || f.isAnnotationPresent(ManyToOne.class)){
					if(ORMUtils.isEagerFetched(f)){
						String tableName = ORMUtils.getTableName(f.getType());
						String idColumn = null;
						String idValue = null;
						if(ORMUtils.isOwningSideRelation(f)){
							idColumn = ORMUtils.getEntityPrimaryKeyColumnName(f.getType());
							idValue = cursor.getString(cursor.getColumnIndex(ORMUtils.getJoinColumnName(objectClass, f)));
													}
						else{
							idColumn = ORMUtils.getJoinColumnName(objectClass, ORMUtils.getInverseRelation(f));
							idValue = cursor.getString(cursor.getColumnIndex(ORMUtils.getEntityPrimaryKeyColumnName(object.getClass())));
							
						}
						
						if(idColumn!=null && idValue!=null){
							String query = "SELECT * FROM ".concat(tableName)
								   .concat(" WHERE ")
								   .concat(idColumn)
								   .concat(" =?");
							
							if(logSQL)
								Log.d("Cleandroid ORM", query);


						Cursor c = db.rawQuery(query, new String[]{idValue});
						if(c.moveToFirst()){
							Object o = cursorToObject(c, f.getType(), db,logSQL);
							f.set(object, o);
						}
						}

					}
				}
				
				else if(f.isAnnotationPresent(OneToMany.class) && !ORMUtils.isOwningSideRelation(f)){
					if(ORMUtils.isEagerFetched(f)){
						String tableName = ORMUtils.getTableName(CollectionUtils.getCollectionType(f));
						String joinColumn = ORMUtils.getJoinColumnName(ORMUtils.getInverseRelation(f).getType(), ORMUtils.getInverseRelation(f));
						String 	idValue = cursor.getString(cursor.getColumnIndex(ORMUtils.getEntityPrimaryKeyColumnName(object.getClass())));
													
						if(joinColumn!=null && idValue!=null){
						String query = "SELECT * FROM ".concat(tableName)
								   .concat(" WHERE ")
								   .concat(joinColumn)
								   .concat(" =?");
						
						if(logSQL)
							Log.d("Cleandroid ORM", query);


						Cursor c = db.rawQuery(query, new String[]{idValue});
						Collection<Object> entities = null;
						
						if(f.getType().isAssignableFrom(ArrayList.class))
							entities = new ArrayList<Object>();
						
						else if(f.getType().isAssignableFrom(HashSet.class))
							entities = new HashSet<Object>();
						
						while(c.moveToNext()){
							Object o = cursorToObject(c, CollectionUtils.getCollectionType(f), db,logSQL);
							entities.add(o);
						}
						f.set(object, entities);
						}

					}
				}
				
				else if(f.isAnnotationPresent(ManyToMany.class) || (f.isAnnotationPresent(OneToMany.class) && ORMUtils.isOwningSideRelation(f))){
					if(ORMUtils.isEagerFetched(f)){
						String tableName = ORMUtils.getTableName(CollectionUtils.getCollectionType(f));
						String joinTableName = ORMUtils.getJoinTableName(f, object.getClass());
						String pkColumn1 = ORMUtils.getEntityPrimaryKeyColumnName(object.getClass());
						String pkValue1 = cursor.getString(cursor.getColumnIndex(pkColumn1));
						String pkColumn2 = ORMUtils.getEntityPrimaryKeyColumnName(CollectionUtils.getCollectionType(f));
						String pkJoinColumn1 = ORMUtils.getJoinColumnName(object.getClass(), f);
						String pkJoinColumn2 = ORMUtils.getReferencedJoinColumnName(CollectionUtils.getCollectionType(f),f);
						
						String query = "SELECT * FROM ".concat(tableName).concat(" AS t1")
													   .concat(" JOIN ").concat(joinTableName).concat(" AS t2")
													   .concat(" ON t1.").concat(pkColumn2).concat("=t2.").concat(pkJoinColumn2)
													   .concat(" JOIN ").concat(ORMUtils.getTableName(object.getClass())).concat(" AS t3")
													   .concat(" ON t3.").concat(pkColumn1).concat("=t2.").concat(pkJoinColumn1)
													   .concat(" WHERE t3.").concat(pkColumn1).concat("=?");
						
						if(logSQL)
							Log.d("Cleandroid ORM", query);

						
						Cursor c = db.rawQuery(query, new String[]{pkValue1});
						Collection<Object> entities = null;
						
						if(f.getType().isAssignableFrom(ArrayList.class))
							entities = new ArrayList<Object>();
						
						else if(f.getType().isAssignableFrom(HashSet.class))
							entities = new HashSet<Object>();
						
						while(c.moveToNext()){
							Object o = cursorToObject(c, CollectionUtils.getCollectionType(f), db,logSQL);
							entities.add(o);
						}
						f.set(object, entities);
					}
				}
				
			}

		
		return (T) object;
		
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getTypedValueFromCursor(Cursor cursor, int columnIndex, Class<T> type){
		if(type.equals(Blob.class))
			return (T) cursor.getBlob(columnIndex);
		else return TypeUtils.getTypedValue(cursor.getString(columnIndex), type);
	}

}
