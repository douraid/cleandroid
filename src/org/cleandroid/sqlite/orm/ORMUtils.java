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
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cleandroid.application.App;
import org.cleandroid.sqlite.orm.annotation.Column;
import org.cleandroid.sqlite.orm.annotation.Entity;
import org.cleandroid.sqlite.orm.annotation.FetchType;
import org.cleandroid.sqlite.orm.annotation.Id;
import org.cleandroid.sqlite.orm.annotation.JoinColumn;
import org.cleandroid.sqlite.orm.annotation.JoinTable;
import org.cleandroid.sqlite.orm.annotation.ManyToMany;
import org.cleandroid.sqlite.orm.annotation.ManyToOne;
import org.cleandroid.sqlite.orm.annotation.MappedSuperclass;
import org.cleandroid.sqlite.orm.annotation.OneToMany;
import org.cleandroid.sqlite.orm.annotation.OneToOne;
import org.cleandroid.sqlite.orm.annotation.Table;
import org.cleandroid.util.CollectionUtils;
import org.cleandroid.util.StringUtils;



import android.annotation.SuppressLint;
import dalvik.system.DexFile;

public class ORMUtils {

	private static final Map<Class<?>,String> dataTypesMap = new HashMap<Class<?>,String>();
	
	static{
		dataTypesMap.put(String.class, "TEXT");
		dataTypesMap.put(char.class, "TEXT");
		dataTypesMap.put(Character.class, "TEXT");
		dataTypesMap.put(short.class, "INTEGER");
		dataTypesMap.put(Short.class, "INTEGER");
		dataTypesMap.put(int.class, "INTEGER");
		dataTypesMap.put(Integer.class, "INTEGER");
		dataTypesMap.put(Long.class, "INTEGER");
		dataTypesMap.put(long.class, "INTEGER");
		dataTypesMap.put(float.class, "REAL");
		dataTypesMap.put(Float.class, "REAL");
		dataTypesMap.put(double.class, "REAL");
		dataTypesMap.put(Double.class, "REAL");
		dataTypesMap.put(byte[].class, "BLOB");
		dataTypesMap.put(Byte[].class, "BLOB");
		dataTypesMap.put(boolean.class, "NUMERIC");
		dataTypesMap.put(Boolean.class, "NUMERIC");
		dataTypesMap.put(Date.class, "NUMERIC");
		
	}
	
	public static String getRelationalType(Class<?> objectType){
		return dataTypesMap.get(objectType);
	}
	
	public static String getAcceptableFieldTypesAsString(){
		String types =  new String();
		for(Class<?> type:dataTypesMap.keySet())
			types = types.concat(type.getName()).concat(", ");
		return types.substring(0,types.length()-3);
	}
	
	@SuppressWarnings("unchecked")
	public static Set<Class<Entity>> scanPackagesForEntityClasses(String...packages) throws Exception{
		  Set<Class<Entity>> entityClasses = new HashSet<Class<Entity>>();
	      
	      DexFile dex = new DexFile(App.getAppContext().getApplicationInfo().sourceDir);
	      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	      Enumeration<String> entries = dex.entries();
	      while (entries.hasMoreElements()) {
	          String entry = entries.nextElement();
	          Class<?> entryClass = null;
	          try{
	        	 entryClass = classLoader.loadClass(entry);
	          }
	          catch(IncompatibleClassChangeError e){
	        	  continue;
	          }

    		  for(String packageName:packages){
    			  if(entryClass.getPackage().getName().equals(packageName.trim()) && entryClass.isAnnotationPresent(Entity.class))
    				  entityClasses.add((Class<Entity>) entryClass);
    		  }
	          }

	      
	      
	      return entityClasses;
	}
	
	
	
	
	public static void validateMappedBy(String mappedBy, Class<Entity> targetClass) throws MappingException{
		
		
		if(mappedBy.isEmpty() || getEntityPrimaryKeyField(targetClass).getName().equals(mappedBy))
			return;
		try{
			Field f = targetClass.getDeclaredField(mappedBy);
			Class<?> entityType = Collection.class.isAssignableFrom(f.getType()) ? CollectionUtils.getCollectionType(f):f.getType();
			if(!entityType.isAnnotationPresent(Entity.class)){
				throw MappingException.invalidMappedReference(f);
			}
			
		}
		catch(NoSuchFieldException e){
			throw MappingException.mappedReferenceNotExists(targetClass,mappedBy);
		}
	}
	
	
	

	
	public static String getTableName(Class<?> entityClass){
		String tableName = StringUtils.toUnderscored(entityClass.getSimpleName());
		if(entityClass.isAnnotationPresent(Table.class)){
			if(!entityClass.getAnnotation(Table.class).name().trim().isEmpty())
				tableName = entityClass.getAnnotation(Table.class).name();
		}
		return tableName;
	}

	
	public static Field getEntityPrimaryKeyField(Class<?> entityClass){
		List<Field> fields = getEntityFields(entityClass);
		for(Field f: fields){
			if(f.isAnnotationPresent(Id.class)){
				f.setAccessible(true);
				return f;
			}
		}
		return null;
	}
	
	public static String getEntityPrimaryKeyColumnName(Class<?> entityClass){
		Field f = getEntityPrimaryKeyField(entityClass);
		return getColumnName(f);
		
	}
	
	public static String getColumnName(Field f){
		if(f!=null){
			String columnName = "";
			if(f.isAnnotationPresent(Column.class)){
				columnName = f.getAnnotation(Column.class).name().trim();
			}
			if(columnName.isEmpty())
				columnName = StringUtils.toUnderscored(f.getName());
			return columnName;
		}
		return null;
		
	}
	
	public static String getColumnName(Class<?> entityClass, String property){
		Field f = getEntityFieldByPropertyName(entityClass, property);
		return getColumnName(f);
			
	}

	
	public static  Class<?> getEntityPrimaryKeyType(Class<?> entityClass){
		Field f = getEntityPrimaryKeyField(entityClass);
		if(f!=null)
			return f.getType();
		return null;
	
	}
	
	public static Field getInverseRelation(Field field){
		Class<?> fieldType = field.getType();
		if(Collection.class.isAssignableFrom(field.getType()))
			fieldType = CollectionUtils.getCollectionType(field);
		
		for(Field f:getEntityFields(fieldType)){
			
			if(field.isAnnotationPresent(ManyToOne.class)){
				if(f.isAnnotationPresent(OneToMany.class)){
					if(field.getName().equals(f.getAnnotation(OneToMany.class).mappedBy()))
						return f;
				}
			}
			
			else if(field.isAnnotationPresent(OneToOne.class)){
				if(f.isAnnotationPresent(OneToOne.class)){
					if(f.getName().equals(field.getAnnotation(OneToOne.class).mappedBy()) || field.getName().equals(f.getAnnotation(OneToOne.class).mappedBy()))
						return f;
				}
			}
			
			else if(field.isAnnotationPresent(OneToMany.class)){
				if(f.isAnnotationPresent(ManyToOne.class)){
					if(field.getAnnotation(OneToMany.class).mappedBy().equals(f.getName()))
						return f;
				}
			}
			
			else if(field.isAnnotationPresent(ManyToMany.class)){
				if(f.isAnnotationPresent(ManyToMany.class)){
					if(f.getName().equals(field.getAnnotation(ManyToMany.class).mappedBy()) || field.getName().equals(f.getAnnotation(ManyToMany.class).mappedBy()))
						return f;
				}
			}
				
		}
		return null;
	}
	


	public static Field getEntityFieldByPropertyName(Class<?> entityClass, String property){
		for(Field f:getEntityFields(entityClass)){
			if(f.getName().equals(property))
				return f;
		}
		return null;
	}
	
	public static List<Field> getEntityFields(Class<?> entityClass){
		List<Field> fields = new ArrayList<Field>();
		if(entityClass.getSuperclass().isAnnotationPresent(MappedSuperclass.class))
			fields.addAll(Arrays.asList(entityClass.getSuperclass().getDeclaredFields()));
		fields.addAll(Arrays.asList(entityClass.getDeclaredFields()));
		
		return fields;
	}
	
	public static String getJoinTableName(Field relation, Class<?> entityClass){
		
		if(relation.isAnnotationPresent(JoinTable.class))
				return relation.getAnnotation(JoinTable.class).name();
		
		String joinTableName = null;
		
		if(isOwningSideRelation(relation))
			joinTableName = ORMUtils.getTableName(entityClass) + "_" + ORMUtils.getTableName(CollectionUtils.getCollectionType(relation));

		else{
			joinTableName = ORMUtils.getTableName(CollectionUtils.getCollectionType(relation)) + "_" +  ORMUtils.getTableName(entityClass);

		}
		return joinTableName;	
	}
	
	@SuppressLint("DefaultLocale")
	public static String getJoinColumnName(Class<?> entityClass, Field f){

		if(f.isAnnotationPresent(OneToMany.class) || f.isAnnotationPresent(ManyToMany.class)){
			if(f.isAnnotationPresent(JoinTable.class)){
				if(f.getAnnotation(JoinTable.class).joinColumn()!=null)
					return f.getAnnotation(JoinTable.class).joinColumn().name();
			}
			return ORMUtils.getTableName(entityClass).toLowerCase().concat("_id");

		}
		
		else if(f.isAnnotationPresent(ManyToOne.class) || f.isAnnotationPresent(OneToOne.class)){
			if(f.isAnnotationPresent(JoinColumn.class))
				return f.getAnnotation(JoinColumn.class).name();
			return f.getName().toLowerCase().concat("_id");
		}
		return null;
	}
	
	
	
	@SuppressLint("DefaultLocale")
	public static String getReferencedJoinColumnName(Class<?> entityClass, Field f){

			if(f.isAnnotationPresent(JoinTable.class)){
				if(f.getAnnotation(JoinTable.class).referencedJoinColumn()!=null)
					return f.getAnnotation(JoinTable.class).referencedJoinColumn().name();
			}

		return ORMUtils.getTableName(CollectionUtils.getCollectionType(f)).toLowerCase() + "_id";
	}
	
	
	
	
	public static boolean isOwningSideRelation(Field relation){
		if(relation.isAnnotationPresent(ManyToOne.class))
			return true;
		else if(relation.isAnnotationPresent(OneToOne.class)){
			if(relation.getAnnotation(OneToOne.class).mappedBy().trim().isEmpty()  
					|| relation.getAnnotation(OneToOne.class).mappedBy().equals(ORMUtils.getEntityPrimaryKeyField(relation.getType()).getName()))
				return true;
			return false;
		}
		
		else if(relation.isAnnotationPresent(OneToMany.class)){
			if(relation.getAnnotation(OneToMany.class).mappedBy().trim().isEmpty()  
					|| relation.getAnnotation(OneToMany.class).mappedBy().equals(ORMUtils.getEntityPrimaryKeyField(CollectionUtils.getCollectionType(relation)).getName()))
				return true;
			return false;
		}
		
		else if(relation.isAnnotationPresent(ManyToMany.class)){
			if(relation.getAnnotation(ManyToMany.class).mappedBy().trim().isEmpty()  
					|| relation.getAnnotation(ManyToMany.class).mappedBy().equals(ORMUtils.getEntityPrimaryKeyField(CollectionUtils.getCollectionType(relation)).getName()))
				return true;
			return false;
		}
		
		else
			throw new RuntimeException("The field " + relation.getName() + " is not annotated with any mapping relationship.");
		
	}
	
	public static boolean isEagerFetched(Field relation){
		FetchType type = null;
		if(relation.isAnnotationPresent(OneToOne.class))
			type = relation.getAnnotation(OneToOne.class).fetch();
		else if(relation.isAnnotationPresent(OneToMany.class))
			type = relation.getAnnotation(OneToMany.class).fetch();
		else if(relation.isAnnotationPresent(ManyToOne.class))
			type = relation.getAnnotation(ManyToOne.class).fetch();
		else if(relation.isAnnotationPresent(ManyToMany.class))
			type = relation.getAnnotation(ManyToMany.class).fetch();
		return FetchType.EAGER.equals(type);
	}
}
