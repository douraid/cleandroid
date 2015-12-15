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

import org.cleandroid.lifecycle.OnActivityCreatedHandler;
import org.cleandroid.lifecycle.OnActivityDestroyedHandler;
import org.cleandroid.lifecycle.OnActivityPausedHandler;
import org.cleandroid.lifecycle.OnActivityResumedHandler;
import org.cleandroid.lifecycle.OnActivitySaveInstanceStateHandler;
import org.cleandroid.lifecycle.OnActivityStartedHandler;
import org.cleandroid.lifecycle.OnActivityStoppedHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

@SuppressLint("NewApi")
public class ActivityLifecycleCallbacks implements App.ActivityLifecycleCallbacks{
	
	private Bundle savedInstanceState;
	


	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		try {
			OnActivityCreatedHandler.handle(activity,savedInstanceState);
			this.savedInstanceState = savedInstanceState;
		} catch (Exception e) {
			throw new RuntimeException(e);
			
		}
	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		try {
			OnActivityDestroyedHandler.handle(activity,savedInstanceState);
		} catch (Exception e) {
			throw new RuntimeException(e);
			
		}
	}

	@Override
	public void onActivityPaused(Activity activity) {
		try {
			OnActivityPausedHandler.handle(activity,savedInstanceState);
		} catch (Exception e) {
			throw new RuntimeException(e);
			
		}
	}

	@Override
	public void onActivityResumed(Activity activity) {
		try {
			OnActivityResumedHandler.handle(activity,savedInstanceState);
		} catch (Exception e) {
			throw new RuntimeException(e);
			
		}
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
		try {
			OnActivitySaveInstanceStateHandler.handle(activity,savedInstanceState);
		} catch (Exception e) {
			throw new RuntimeException(e);
			
		}
	}

	@Override
	public void onActivityStarted(Activity activity) {
		try {
			OnActivityStartedHandler.handle(activity,savedInstanceState);
		} catch (Exception e) {
			throw new RuntimeException(e);
			
		}
	}

	@Override
	public void onActivityStopped(Activity activity) {
		try {
			OnActivityStoppedHandler.handle(activity,savedInstanceState);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	


}
