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
package org.cleandroid.core.config;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import org.cleandroid.core.util.CollectionUtils;
import org.cleandroid.core.util.TypeUtils;
import org.cleandroid.core.util.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



import android.content.Context;
import android.util.Log;

import org.cleandroid.core.application.App;

public class ConfigurationLoader {
	
	
	public static void loadConfigurations(Context context){
		    
		   InputStream is = null;
		   try {
			is = context.getAssets().open("cleandroid-config.xml");
		   }
		   catch (IOException e) {
				Log.w("CleanDroid","Configuration file assets/cleandroid-config.xml was not found");
				return;
		   }

			try {
				Document doc = new XMLUtils().parseFromInputStream(is);
				doc.normalize();
				NodeList configurations = doc.getElementsByTagName("configuration");
				for(int i=0;i<configurations.getLength();i++){
					Node configuration = configurations.item(i);
					Node classAttr = configuration.getAttributes().getNamedItem("class");
					Node idAttr = configuration.getAttributes().getNamedItem("id");
					
					
					String className = classAttr.getNodeValue();
					String configId = idAttr.getNodeValue();
					
					Class<?> confClass = Class.forName(className);
					Configuration instance = (Configuration) confClass.newInstance();
					instance.setId(configId);
					for(Field f:confClass.getDeclaredFields()){
						inject(f,instance,configuration);
					}
					
					App.addConfiguration(instance);
					try{
						instance.onConfigurationLoaded();
					}
					catch(Exception e){
						throw new RuntimeException(e);
					}
					
				}
			} catch (Exception e) {
				Log.w("CleanDroid","Cannot load configuration file cleandroid-config.xml");
				throw new RuntimeException(e);
			}
		
			
	}
	
	private static void inject(Field f,Object target,Node configuration) throws Exception{
		f.setAccessible(true);



		String fieldName = f.getName();
		String nodeName = null;
		for(Node node: XMLUtils.getDirectChilds(configuration)){
			nodeName = node.getNodeName();
			if(!fieldName.equalsIgnoreCase(nodeName.replace("-", "")))
				continue;
			
			if(TypeUtils.isPrimitiveOrWrapper(f.getType())){
				Object value = TypeUtils.getTypedValue(node.getTextContent(), f.getType());
				f.set(target, value);
			}
			
			else if(Collection.class.isAssignableFrom(f.getType())){
				Class<?> typeClass = CollectionUtils.getCollectionType(f);
				if(!typeClass.equals(Object.class)){
					
					Collection<Object> collection = new ArrayList<Object>();
					

					for(Node cnode:XMLUtils.getDirectChilds(node)){
						Object co = null;
						if(TypeUtils.isPrimitiveOrWrapper(typeClass)){
							co = TypeUtils.getTypedValue(cnode.getTextContent(), typeClass);
							
						}
						else{
						co = typeClass.newInstance();
						for(Field cf:typeClass.getDeclaredFields()){
							cf.setAccessible(true);
							if(cnode.hasAttributes()){
								for(int j=0;j<cnode.getAttributes().getLength();j++){
									Node attr = cnode.getAttributes().item(j);
									if(attr.getNodeName().replace("-", "").equalsIgnoreCase(cf.getName())){
										cf.set(co, TypeUtils.getTypedValue(attr.getNodeValue(), cf.getType()));
									}
								}
							}
							inject(cf,co,cnode);

						}
						}
						collection.add(co);
					}
					f.set(target, collection);
				}
				else{
					throw new Exception("No generic type was found for the Collection " + fieldName);
				}
			}
			
			else if(f.getType().isArray()){

				/*TO IMPLEMENT*/
			}
			
			
			
			
			else{
				for(Node n: XMLUtils.getDirectChilds(node)){
				for(Field of:f.getType().getDeclaredFields()){
					of.setAccessible(true);

					if(!n.getNodeName().replace("-", "").equalsIgnoreCase(of.getName()))
						continue;
					
					if(TypeUtils.isPrimitiveOrWrapper(f.getType())){
						of.set(target, TypeUtils.getTypedValue(n.getNodeValue(), of.getType()));
					}
					
					else{
					Object otarget = of.getType().newInstance();
					if(node.hasAttributes()){
						for(int j=0;j<node.getAttributes().getLength();j++){
							Node attr = node.getAttributes().item(j);
							if(attr.getNodeName().replace("-", "").equalsIgnoreCase(of.getName())){
								of.set(target, TypeUtils.getTypedValue(attr.getNodeValue(), of.getType()));
							}
						}
					}
					inject(of,otarget,n);
					}

				}
				}
			}
		}
		
	
	}
	

}
