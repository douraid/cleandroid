package org.cleandroid.application;

import java.util.HashMap;
import java.util.Map;

public class Container {

	private Map<String,Object> objectsMap = new HashMap<String,Object>();
	
	public void registerObject(String identifier, Object object){
		if(objectsMap.containsKey(identifier))
			throw new RuntimeException("There's already an object holding the <"+identifier+"> identifier inside the container");
		objectsMap.put(identifier, object);
	}
	
	public void removeObject(String identifier){
		objectsMap.remove(identifier);
	}
	
	public  void updateObject(String identifier, Object newObject){
		objectsMap.put(identifier, newObject);
	}
	
	public Object getObject(String identifier){
		return objectsMap.get(identifier);
	}
	
	public boolean isRegistered(String identifier){
		return objectsMap.containsKey(identifier);
	}
	

}
