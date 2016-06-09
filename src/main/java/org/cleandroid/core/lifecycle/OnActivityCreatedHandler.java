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
package org.cleandroid.core.lifecycle;
import org.cleandroid.core.di.FieldsDependencyInjector;
import org.cleandroid.core.event.InEventsHandler;
import org.cleandroid.core.lifecycle.annotation.OnCreate;

import android.app.Activity;
import android.os.Bundle;

public class OnActivityCreatedHandler {
	
	public static void handle(Activity activity, Bundle savedInstanceState){
		FieldsDependencyInjector.getInstance().injectDependencies(activity);
		ActivityLifecycleMethodsHandler.handle(activity, savedInstanceState, OnCreate.class);
		InEventsHandler.handle(activity);
	}

}
