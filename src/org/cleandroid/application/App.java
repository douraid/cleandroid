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
package org.cleandroid.application;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cleandroid.config.Configuration;
import org.cleandroid.config.ConfigurationLoader;

import android.content.Context;
import android.util.Log;

public class App extends android.app.Application{
	
	private static Context appContext = null;
	

	private static final Set<Configuration> configurations = new HashSet<Configuration>();
	private static final Map<String, Object> appVars = new HashMap<String, Object>();
	
	private final ActivityLifecycleCallbacks lifeCycleCallbacks = new org.cleandroid.application.ActivityLifecycleCallbacks();

	@Override
	public void onCreate() {
		super.onCreate();
		appContext = this.getApplicationContext();
		if(isFirstRun()){
			try {
				AppEnvironment.createAppDirectories();
			} catch (IOException e) {
				Log.e("CleanDroid", "Unable to create application directories",e);
			}
		}
		ConfigurationLoader.loadConfigurations(this.getApplicationContext());
		registerActivityLifecycleCallbacks(lifeCycleCallbacks);
	
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		unregisterActivityLifecycleCallbacks(lifeCycleCallbacks);
	}
	
	public static void addConfiguration(Configuration config){
		configurations.add(config);
	}
	
	public static Configuration getConfiguration(String id){
		for(Configuration conf:configurations){
			if(conf.getId().equals(id))
				return conf;
		}
		return null;
	}
	
	public static Context getAppContext(){
		return appContext;
	}
	
	
	public static boolean isFirstRun(){
		boolean isFirstRun = appContext.getSharedPreferences("CLEANDROID-PREFS", MODE_PRIVATE).getBoolean("isFirstRun", true);
	    if (isFirstRun)
	        appContext.getSharedPreferences("CLEANDROID-PREFS", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();
	   
	    return isFirstRun;
	}
	
	public static void setVar(String name, Object value){
		appVars.put(name, value);
	}
	
	public static Object getVar(String name, Object value){
		return appVars.get(name);
	}
	
	

}
