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
package org.cleandroid.inevent;

import java.lang.reflect.Method;

import org.cleandroid.di.DependencyManager;
import org.cleandroid.inevent.annotation.OnClick;
import org.cleandroid.inevent.annotation.OnFocusChange;
import org.cleandroid.inevent.annotation.OnKey;
import org.cleandroid.inevent.annotation.OnLongClick;
import org.cleandroid.inevent.annotation.OnTouch;

import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;

public class InEventsHandler {
	
	
	public static void handle(final Activity activity){
		for(final Method m:activity.getClass().getMethods()){
			if(m.isAnnotationPresent(OnClick.class)){
				activity.findViewById(m.getAnnotation(OnClick.class).view()).setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View view) {
						Object[] parameters = DependencyManager.getMethodDependencies(m, activity, view);
						invokeMethod(m, activity,parameters);
					}
					
				});
			}
			
			if(m.isAnnotationPresent(OnLongClick.class)){
				activity.findViewById(m.getAnnotation(OnLongClick.class).view()).setOnLongClickListener(new OnLongClickListener(){

					@Override
					public boolean onLongClick(View view) {
						Object[] parameters = DependencyManager.getMethodDependencies(m, activity, view);
						invokeMethod(m, activity,parameters);
						return false;
					}
					
				});
			}
			
			if(m.isAnnotationPresent(OnTouch.class)){
				activity.findViewById(m.getAnnotation(OnTouch.class).view()).setOnTouchListener(new OnTouchListener(){

					@Override
					public boolean onTouch(View view, MotionEvent motionEvent) {
						Object[] parameters = DependencyManager.getMethodDependencies(m, activity, view, motionEvent);
						invokeMethod(m, activity,parameters);
						return false;
					}
					
				});
			}
			
			if(m.isAnnotationPresent(OnFocusChange.class)){
				activity.findViewById(m.getAnnotation(OnFocusChange.class).view()).setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View view, boolean hasFocus) {
							
							try{
								Object[] args = DependencyManager.getMethodDependencies(m, activity,view,hasFocus);
								invokeMethod(m, activity,args);
							}
							catch(Exception e){
								Log.e("CleanDroid", e.getMessage(), e);
							}
	
						
					}
				});
			}
			
			
			if(m.isAnnotationPresent(OnKey.class)){
				activity.findViewById(m.getAnnotation(OnKey.class).view()).setOnKeyListener(new OnKeyListener(){

					@Override
					public boolean onKey(View view, int keyCode, KeyEvent e) {
						Object[] parameters = DependencyManager.getMethodDependencies(m, activity, view, keyCode, e);
						invokeMethod(m, activity,parameters);
						return false;
					}
					
				});
			}
			
			
		}
	}
	
	
	public static void invokeMethod(Method m,Object receiver,Object...args){
		try{
			m.setAccessible(true);
			m.invoke(receiver, args);
		}
		catch(Exception e){
			Log.e("CleanDroid",e.getMessage(),e);
		}
	}

	

}
