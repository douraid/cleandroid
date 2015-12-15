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

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cleandroid.sqlite.orm.annotation.Column;
import org.cleandroid.sqlite.orm.annotation.Embeddable;
import org.cleandroid.sqlite.orm.annotation.Embedded;
import org.cleandroid.sqlite.orm.annotation.Entity;
import org.cleandroid.sqlite.orm.annotation.AutoIncrement;
import org.cleandroid.sqlite.orm.annotation.Id;
import org.cleandroid.sqlite.orm.annotation.ManyToMany;
import org.cleandroid.sqlite.orm.annotation.ManyToOne;
import org.cleandroid.sqlite.orm.annotation.OneToMany;
import org.cleandroid.sqlite.orm.annotation.OneToOne;
import org.cleandroid.util.CollectionUtils;
import org.cleandroid.util.StringUtils;

public class SchemaGenerator {
	
	static Set<String> generationStrategies  = new HashSet<String>();
	
	static{
		generationStrategies.add("create");
		generationStrategies.add("drop-and-create");
	}
	
	@SuppressLint("DefaultLocale")
	public static void generateSchema(boolean logSQL,SQLiteDatabase db,String generationStrategy,Class<Entity>... entityClasses) throws MappingException{
		
		if(!generationStrategies.contains(generationStrategy))
			throw new RuntimeException("Unknown generation strategy ".concat(generationStrategy));
		
		if(generationStrategy.equals("drop-and-create"))
			dropTables(logSQL, db, entityClasses);
		
		Set<String> schemaQueries = new HashSet<String>();
		Set<String> foreignKeyConstraints;
		
		for(Class<Entity> entityClass:entityClasses){
			foreignKeyConstraints = new HashSet<String>();
			
			String tableName = ORMUtils.getTableName(entityClass);
			StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");
			
			validateEntity(entityClass);
			
			List<Field> fields = ORMUtils.getEntityFields(entityClass);
				
			for(Field f:fields){
				f.setAccessible(true);
				
				if(f.isAnnotationPresent(Id.class)){
					query = query.append(getColumnCreateString(f, true));
				}
				
				
				else if(f.isAnnotationPresent(Column.class)){
					query = query.append(getColumnCreateString(f, false));

				}
				
				
				else if(f.isAnnotationPresent(OneToMany.class)){
					if(ORMUtils.isOwningSideRelation(f)){
						schemaQueries.add(createJoinTableQuery(entityClass,f));

					}
				}
				
				else if(f.isAnnotationPresent(ManyToOne.class) || (f.isAnnotationPresent(OneToOne.class) && ORMUtils.isOwningSideRelation(f))){
					String joinColumn = ORMUtils.getJoinColumnName(entityClass, f);
					query = query.append(joinColumn + " " + ORMUtils.getRelationalType(ORMUtils.getEntityPrimaryKeyType(f.getType())) + ", ");
					
					String foreignKeyQuery = "FOREIGN KEY ("+joinColumn+") REFERENCES " + ORMUtils.getTableName(f.getType()) + "(" + ORMUtils.getEntityPrimaryKeyColumnName(f.getType()) + "), ";
					foreignKeyConstraints.add(foreignKeyQuery);
				}
				
				else if(f.isAnnotationPresent(Embedded.class)){
										
					for(Field f1:f.getType().getDeclaredFields()){
						query = query.append(getColumnCreateString(f1,false));

					}
					
				}
				
				else if(f.isAnnotationPresent(ManyToMany.class)){
					if(ORMUtils.isOwningSideRelation(f)){
						schemaQueries.add(createJoinTableQuery(entityClass,f));
					}
				}
				
				
				
			}
			
			for(String fkQ:foreignKeyConstraints)
				query = query.append(fkQ);
			
			
			String queryStr = StringUtils.rtrim(query.toString(),", ").concat(");");
			schemaQueries.add(queryStr);

		}
		
		
		for(String query:schemaQueries){
			if(logSQL)
				Log.d("Cleandroid ORM",query);
			db.execSQL(query);
		}
		db.close();
	}
	
	
	@SuppressWarnings("unchecked")
	private static void validateEntity(Class<?> entityClass) throws MappingException{
	
		int primaryKeyCount = 0;
		List<Field> fields = ORMUtils.getEntityFields(entityClass);

		for(Field f: fields){
			

			
			if(f.isAnnotationPresent(Id.class)){
				primaryKeyCount++;
			}
			
			if(f.isAnnotationPresent(AutoIncrement.class) && !(f.getType().equals(int.class) || f.getType().equals(Integer.class) || f.getType().equals(long.class) || f.getType().equals(Long.class) || f.getType().equals(short.class) || f.getType().equals(Short.class) )){
				throw MappingException.invalidTypeForAutoIncrement(f);
			}
			
			if(ORMUtils.getRelationalType(f.getType())==null 
					&& !Collection.class.isAssignableFrom(f.getType()) 
					&& !f.getType().isAnnotationPresent(Entity.class)
					&& !f.getType().isAnnotationPresent(Embeddable.class)){
				throw MappingException.invalidType(f);
			}
			
		
			if(f.isAnnotationPresent(OneToOne.class)){
				if(f.isAnnotationPresent(OneToMany.class) || f.isAnnotationPresent(ManyToOne.class) || f.isAnnotationPresent(ManyToMany.class) || f.isAnnotationPresent(Column.class) || f.isAnnotationPresent(Embedded.class)){
				}
				ORMUtils.validateMappedBy(f.getAnnotation(OneToOne.class).mappedBy(),(Class<Entity>) f.getType());

			}
			
			if(f.isAnnotationPresent(OneToMany.class)){
				if(f.isAnnotationPresent(OneToOne.class) || f.isAnnotationPresent(ManyToOne.class) || f.isAnnotationPresent(ManyToMany.class) || f.isAnnotationPresent(Column.class) || f.isAnnotationPresent(Embedded.class)){
					throw MappingException.multiplePersistenceTypes(f);
				}
				ORMUtils.validateMappedBy(f.getAnnotation(OneToMany.class).mappedBy(),(Class<Entity>)CollectionUtils.getCollectionType(f));

			}
			
			if(f.isAnnotationPresent(ManyToOne.class)){
				if(f.isAnnotationPresent(OneToMany.class) || f.isAnnotationPresent(OneToOne.class) || f.isAnnotationPresent(ManyToMany.class) || f.isAnnotationPresent(Column.class) || f.isAnnotationPresent(Embedded.class)){
					throw MappingException.multiplePersistenceTypes(f);
				}

			}
			
			if(f.isAnnotationPresent(ManyToMany.class)){
				if(f.isAnnotationPresent(OneToMany.class) || f.isAnnotationPresent(ManyToOne.class) || f.isAnnotationPresent(OneToOne.class) || f.isAnnotationPresent(Column.class) || f.isAnnotationPresent(Embedded.class)){
					throw MappingException.multiplePersistenceTypes(f);
				}
				ORMUtils.validateMappedBy(f.getAnnotation(ManyToMany.class).mappedBy(),(Class<Entity>) CollectionUtils.getCollectionType(f));

			}
			
			if(f.isAnnotationPresent(Column.class)){
				if(f.isAnnotationPresent(OneToMany.class) || f.isAnnotationPresent(ManyToOne.class) || f.isAnnotationPresent(OneToOne.class) || f.isAnnotationPresent(ManyToMany.class) || f.isAnnotationPresent(Embedded.class)){
					throw MappingException.multiplePersistenceTypes(f);
				}
			}
			
			if(f.isAnnotationPresent(Embedded.class)){
				if(f.isAnnotationPresent(OneToMany.class) || f.isAnnotationPresent(ManyToOne.class) || f.isAnnotationPresent(OneToOne.class) || f.isAnnotationPresent(Column.class) || f.isAnnotationPresent(ManyToMany.class)){
					throw MappingException.multiplePersistenceTypes(f);
				}
			}
		}
		
		
		
		if(primaryKeyCount==0)
			throw MappingException.noPrimaryKey(entityClass);
		else if(primaryKeyCount>1)
			throw MappingException.multiplePrimaryKeys(entityClass);
	}
	
		
	
	private static String createJoinTableQuery(Class<?> entityClass,Field f){
		String joinTableName = ORMUtils.getJoinTableName(f, entityClass);
		String joinColumn = ORMUtils.getJoinColumnName(entityClass, f);
		String refJoinColumn = ORMUtils.getReferencedJoinColumnName(entityClass, f);
		StringBuilder joinTableQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(joinTableName)
																  .append(" (")
																  .append(joinColumn + " ")
																  .append(ORMUtils.getRelationalType(ORMUtils.getEntityPrimaryKeyType(entityClass)) + ",")
																  .append(refJoinColumn + " ")
																  .append(ORMUtils.getRelationalType(ORMUtils.getEntityPrimaryKeyType(CollectionUtils.getCollectionType(f))) + ", ")
																  .append("PRIMARY KEY (" + joinColumn + "," + refJoinColumn+")")
																  .append(");");
		return joinTableQuery.toString();
	}
	
	private  static String getColumnCreateString(Field f, boolean isId){
		StringBuilder query = new StringBuilder("");
		String columnName=ORMUtils.getColumnName(f);
		boolean unique=false;
		boolean nullable = false;
		if(f.isAnnotationPresent(Column.class)){
		if(!f.getAnnotation(Column.class).name().trim().isEmpty())
			columnName = f.getAnnotation(Column.class).name();

		nullable = f.getAnnotation(Column.class).nullable();
		unique = f.getAnnotation(Column.class).unique();
		}
		query = query.append(columnName)
			     .append(" ")
			     .append(ORMUtils.getRelationalType(f.getType()));
		if(isId)
			query = query.append(" PRIMARY KEY");
		
		if(unique && !isId)
			query = query.append(" UNIQUE");
		
		if(f.isAnnotationPresent(AutoIncrement.class))
			query = query.append(" AUTOINCREMENT");
		if(!nullable && !isId)
			query = query.append(" NOT NULL");
	
		return query.append(", ").toString();
	}
	
	
	public static void dropTables(boolean logSQL, SQLiteDatabase db, Class<Entity>...entities){
		String command = null;
		Set<String> joinTables = new HashSet<String>();
		for(Class<Entity> entityClass:entities){
			command = "DROP TABLE IF EXISTS ".concat(ORMUtils.getTableName(entityClass));
			if(logSQL)
				Log.d("Cleandroid ORM", command);
			db.execSQL(command);
			for(Field f:ORMUtils.getEntityFields(entityClass)){
				if(f.isAnnotationPresent(OneToMany.class) || f.isAnnotationPresent(ManyToMany.class)){
					String joinTable = ORMUtils.getJoinTableName(f, entityClass);
					if(joinTable!=null)
						joinTables.add(joinTable);
				}
			}
		}
		for(String joinTable:joinTables){
			command = "DROP TABLE IF EXISTS ".concat(joinTable);
			if(logSQL)
				Log.d("Cleandroid ORM", command);
			db.execSQL(command);
		}
	}

	
}
