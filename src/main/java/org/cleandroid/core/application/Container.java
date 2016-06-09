package org.cleandroid.core.application;

import java.util.HashMap;
import java.util.Map;

public class Container {

	private Map<Class<?>,Map<String,Object>> objectsMap = new HashMap<>();
	
	public void registerObject(String identifier, Object object){
		if(objectsMap.get(object.getClass())==null)
			objectsMap.put(object.getClass(),new HashMap<String, Object>());
		if(objectsMap.get(object.getClass()).containsKey(identifier))
			throw new RuntimeException("There's already an object holding the <"+identifier+"> identifier inside the container");
		objectsMap.get(object.getClass()).put(identifier, object);
	}
	
	public void removeObject(String identifier){
		objectsMap.remove(identifier);
	}
	
	public  void updateObject(String identifier, Object newObject){
		objectsMap.get(newObject.getClass()).put(identifier, newObject);
	}
	
	public <T> T getObject(Class<T> objectClass,String identifier){
		return (T) objectsMap.get(objectClass).get(identifier);
	}
	
	public <T> boolean isRegistered(Class<T> objectClass,String identifier){
		if(objectsMap.get(objectClass)==null)
			objectsMap.put(objectClass, new HashMap<String, Object>());
		return objectsMap.get(objectClass).containsKey(identifier);
	}


}
