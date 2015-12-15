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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.cleandroid.sqlite.orm.annotation.AutoIncrement;
import org.cleandroid.sqlite.orm.annotation.Column;
import org.cleandroid.sqlite.orm.annotation.Embedded;
import org.cleandroid.sqlite.orm.annotation.Id;
import org.cleandroid.sqlite.orm.annotation.ManyToMany;
import org.cleandroid.sqlite.orm.annotation.ManyToOne;
import org.cleandroid.sqlite.orm.annotation.OneToMany;
import org.cleandroid.sqlite.orm.annotation.OneToOne;
import org.cleandroid.util.StringUtils;
import org.cleandroid.util.TypeUtils;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

class CommandBuilder {
	
	static List<SQLiteStatement> createInsertQuery(Object entity, SQLiteDatabase db) throws IllegalAccessException, IllegalArgumentException{
		
		List<Object> values = new ArrayList<Object>();
		
		List<SQLiteStatement> statements = new ArrayList<SQLiteStatement>();
		
		String query = "INSERT INTO %TABLENAME% (%COLS%) VALUES (%VALS%); ";
		String columns = "";
		List<Field> fields = ORMUtils.getEntityFields(entity.getClass());
		for(Field f: fields){
			f.setAccessible(true);

			if(f.isAnnotationPresent(Id.class) || f.isAnnotationPresent(Column.class)){
				if(!f.isAnnotationPresent(AutoIncrement.class)){
					if(f.get(entity)!=null){
					columns = columns.concat(ORMUtils.getColumnName(f)).concat(", ");
				    if(f.getType().equals(Date.class)){
						Date date = (Date) f.get(entity);
						Long vdate = date.getTime();
						values.add(String.valueOf(vdate));
					}
					
					else if(f.getType().equals(byte[].class) || f.getType().equals(Byte[].class))
						values.add((byte[])f.get(entity));
				    
					else if(f.getType().equals(boolean.class) || f.getType().equals(Boolean.class))
						values.add(((Boolean) f.get(entity)) ? 1:0);
					else
						values.add(TypeUtils.getTypedValue(String.valueOf(f.get(entity)), f.getType()));

					}
				}
			}
			else if(f.isAnnotationPresent(Embedded.class)){
				for(Field f1:f.getType().getDeclaredFields()){
					f1.setAccessible(true);
					if(!f1.isAnnotationPresent(AutoIncrement.class) && f.isAnnotationPresent(Column.class)){
						columns = columns.concat(ORMUtils.getColumnName(f1)).concat(", ");
						if(f1.get(f.get(entity))!=null){

						if(f.getType().equals(Date.class)){
							Date date = (Date) f1.get(f.get(entity));
							Long vdate = date.getTime();
							values.add(vdate);
						}
						
						else if(f.getType().equals(boolean.class) || f.getType().equals(Boolean.class))
							values.add(((Boolean) f.get(entity)) ? 1:0);
						
						else if(f1.getType().equals(byte[].class) || f1.getType().equals(Byte[].class))
								values.add((byte[])f1.get(f.get(entity)));
						
						else
							values.add(f1.get(f.get(entity)));
						}
			


					}
				}
			}
			
			else if(f.isAnnotationPresent(OneToOne.class)){
					
					if(ORMUtils.isOwningSideRelation(f)){
							
							Field idField = ORMUtils.getEntityPrimaryKeyField(f.getType());
							Object idValue = null;
							if(f.get(entity)!=null){
								idValue = idField.get(f.get(entity));
								columns = columns.concat(ORMUtils.getJoinColumnName(entity.getClass(), f)).concat(", ");
								values.add(idValue);
							}
							
					}

				
			}

			else if(f.isAnnotationPresent(ManyToOne.class)){
				Field idField = ORMUtils.getEntityPrimaryKeyField(f.getType());
				if(f.get(entity)!=null){
				Object idValue = idField.get(f.get(entity));
				if(idValue!=null){
					columns = columns.concat(ORMUtils.getJoinColumnName(entity.getClass(), f)).concat(", ");
					values.add(idValue);
				}
				}
			}
			
			else if(f.isAnnotationPresent(ManyToMany.class) || f.isAnnotationPresent(OneToMany.class)){
				if(ORMUtils.isOwningSideRelation(f)){
					if(f.get(entity)!=null){
					String joinTableName = ORMUtils.getJoinTableName(f, entity.getClass());
					String joinColumn = ORMUtils.getJoinColumnName(entity.getClass(), f);
					String refJoinColumn = ORMUtils.getReferencedJoinColumnName(entity.getClass(), f);
					
					
					List<Object> attachedIds = new ArrayList<Object>();
					for(Object object:((Collection<?>)f.get(entity))){
						String joinInsertQuery = "INSERT INTO ".concat(joinTableName)
															   .concat("(")
															   .concat(joinColumn)
															   .concat(",")
															   .concat(refJoinColumn)
															   .concat(") VALUES(?,?)");
						SQLiteStatement joinInsertStatement = db.compileStatement(joinInsertQuery);
						Object attachedId = ORMUtils.getEntityPrimaryKeyField(object.getClass()).get(object);
						bindStatementValue(joinInsertStatement, attachedId, 2);
						statements.add(joinInsertStatement);
						attachedIds.add(attachedId);
						
					}
					
					
				}
				}
			}
			
		}
		String valParams = "";
		
		for(int i=0;i<values.size();i++){
			valParams = valParams.concat("?,");
		}
		
		columns = StringUtils.rtrim(columns, ", ");
		valParams = StringUtils.rtrim(valParams, ",");
		query = query.replace("%TABLENAME%", ORMUtils.getTableName(entity.getClass()))
		             .replace("%COLS%",columns)
		             .replace("%VALS%",valParams);
		SQLiteStatement statement = db.compileStatement(query);

		for(int i=0;i<values.size();i++){
			Object value = values.get(i);
			
			bindStatementValue(statement, value,i+1);
			

		}
		
		statements.add(statement);
		
		return statements;
	}
	
	static List<SQLiteStatement> createUpdateQuery(Object entity, SQLiteDatabase db) throws IllegalAccessException, IllegalArgumentException{
		
		List<Object> values = new ArrayList<Object>();
		
		List<SQLiteStatement> statements = new ArrayList<SQLiteStatement>();
		
		String query = "UPDATE %TABLENAME% SET %COLS% WHERE %PKCOLUMN%=?;";
		String columns = "";
		List<Field> fields = ORMUtils.getEntityFields(entity.getClass());
		for(Field f: fields){
			f.setAccessible(true);

			if(f.isAnnotationPresent(Id.class) || f.isAnnotationPresent(Column.class)){
				if(!f.isAnnotationPresent(AutoIncrement.class)){
					if(f.get(entity)!=null){
					columns = columns.concat(ORMUtils.getColumnName(f)).concat("=?, ");
				    if(f.getType().equals(Date.class)){
						Date date = (Date) f.get(entity);
						Long vdate = date.getTime();
						values.add(String.valueOf(vdate));
					}
					
					else if(f.getType().equals(byte[].class) || f.getType().equals(Byte[].class))
						values.add((byte[])f.get(entity));
				    
					else if(f.getType().equals(boolean.class) || f.getType().equals(Boolean.class))
						values.add(((Boolean) f.get(entity)) ? 1:0);
					else
						values.add(TypeUtils.getTypedValue(String.valueOf(f.get(entity)), f.getType()));

					}
				}
			}
			else if(f.isAnnotationPresent(Embedded.class)){
				for(Field f1:f.getType().getDeclaredFields()){
					f1.setAccessible(true);
					if(!f1.isAnnotationPresent(AutoIncrement.class) && f.isAnnotationPresent(Column.class)){
						columns = columns.concat(ORMUtils.getColumnName(f1)).concat("=?, ");
						if(f1.get(f.get(entity))!=null){

						if(f.getType().equals(Date.class)){
							Date date = (Date) f1.get(f.get(entity));
							Long vdate = date.getTime();
							values.add(vdate);
						}
						
						else if(f.getType().equals(boolean.class) || f.getType().equals(Boolean.class))
							values.add(((Boolean) f.get(entity)) ? 1:0);
						
						else if(f1.getType().equals(byte[].class) || f1.getType().equals(Byte[].class))
								values.add((byte[])f1.get(f.get(entity)));
						
						else
							values.add(f1.get(f.get(entity)));
						}
			


					}
				}
			}
			
			else if(f.isAnnotationPresent(OneToOne.class)){
					
					if(ORMUtils.isOwningSideRelation(f)){
							
							Field idField = ORMUtils.getEntityPrimaryKeyField(f.getType());
							Object idValue = null;
							if(f.get(entity)!=null){
								idValue = idField.get(f.get(entity));
								columns = columns.concat(ORMUtils.getJoinColumnName(entity.getClass(), f)).concat("=?, ");
								values.add(idValue);
							}
							
					}

				
			}

			else if(f.isAnnotationPresent(ManyToOne.class)){
				Field idField = ORMUtils.getEntityPrimaryKeyField(f.getType());
				if(f.get(entity)!=null){
				Object idValue = idField.get(f.get(entity));
				if(idValue!=null){
					columns = columns.concat(ORMUtils.getJoinColumnName(entity.getClass(), f)).concat("=?, ");
					values.add(idValue);
				}
				}
			}
			
			else if(f.isAnnotationPresent(ManyToMany.class) || f.isAnnotationPresent(OneToMany.class)){
				if(ORMUtils.isOwningSideRelation(f)){
					if(f.get(entity)!=null){
					String joinTableName = ORMUtils.getJoinTableName(f, entity.getClass());
					String joinColumn = ORMUtils.getJoinColumnName(entity.getClass(), f);
					String refJoinColumn = ORMUtils.getReferencedJoinColumnName(entity.getClass(), f);
					
					
					List<Object> attachedIds = new ArrayList<Object>();
					for(Object object:((Collection<?>)f.get(entity))){
						String joinInsertQuery = "INSERT INTO ".concat(joinTableName)
															   .concat("(")
															   .concat(joinColumn)
															   .concat(",")
															   .concat(refJoinColumn)
															   .concat(") VALUES(?,?)");
						SQLiteStatement joinInsertStatement = db.compileStatement(joinInsertQuery);
						Object attachedId = ORMUtils.getEntityPrimaryKeyField(object.getClass()).get(object);
						bindStatementValue(joinInsertStatement, attachedId, 2);
						statements.add(joinInsertStatement);
						attachedIds.add(attachedId);
						
					}
					
					String removeDetachedQuery = "DELETE FROM ".concat(joinTableName)
															   .concat(" WHERE ")
															   .concat(joinColumn)
															   .concat("=? AND ")
															   .concat(refJoinColumn)
															   .concat(" NOT IN(")
															   .concat(StringUtils.rtrim(StringUtils.repeat("?,", attachedIds.size()),","))
															   .concat(")");
					SQLiteStatement removeDetachedStatement = db.compileStatement(removeDetachedQuery);										  
					for(int i=0;i<attachedIds.size();i++){
						bindStatementValue(removeDetachedStatement, attachedIds.get(i),i+2);
					}
					statements.add(removeDetachedStatement);
				}
				}
			}
			
		}

		columns = StringUtils.rtrim(columns, ", ");

		query = query.replace("%TABLENAME%", ORMUtils.getTableName(entity.getClass()))
		             .replace("%COLS%",columns)
		             .replace("%PKCOLUMN%", ORMUtils.getEntityPrimaryKeyColumnName(entity.getClass()));
		
		SQLiteStatement statement = db.compileStatement(query);

		int i=0;
		for(i=0;i<values.size();i++){
			Object value = values.get(i);
			
			bindStatementValue(statement, value,i+1);
			

		}
		bindStatementValue(statement, ORMUtils.getEntityPrimaryKeyField(entity.getClass()).get(entity), i+1);
		
		statements.add(statement);
		
		return statements;
	}
	
	static String createDeleteQuery(){
		
		return null;
	}
	
	
	
	public static void bindStatementValue(SQLiteStatement statement, Object value, int index){
		if(TypeUtils.isTypeInRange(value, String.class,char.class,Character.class))
			statement.bindString(index, String.valueOf(value));
		
		else if(TypeUtils.isTypeInRange(value, short.class,Short.class,int.class,Integer.class,long.class,Long.class))
			statement.bindLong(index, TypeUtils.getTypedValue(String.valueOf(value), long.class));
		
		else if(TypeUtils.isTypeInRange(value, float.class, Float.class, double.class, Double.class))
			statement.bindDouble(index, (Double) value);
		
		else if(TypeUtils.isTypeInRange(value, boolean.class, Boolean.class))
			statement.bindLong(index, ((Boolean) value) ? 1:0);
		
		else if(TypeUtils.isTypeInRange(value, byte[].class,Byte[].class))
			statement.bindBlob(index, (byte[]) value);
	}

}
