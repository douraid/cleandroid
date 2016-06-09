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


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.cleandroid.core.di.DependencyManager;

import android.app.Activity;
import android.os.Bundle;

public class ActivityLifecycleMethodsHandler {
	


    public static void handle(Activity activity, Bundle savedInstanceState, Class<? extends Annotation> cycleStage){
        for(Method m:activity.getClass().getDeclaredMethods()){
            if(m.isAnnotationPresent(cycleStage)){
                m.setAccessible(true);
                Object[] parameters = DependencyManager.getMethodDependencies(m, activity, savedInstanceState);
                try{
                    m.invoke(activity, parameters);
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
    }
	
}
