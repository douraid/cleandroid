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


import org.cleandroid.core.view.LayoutHandler;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import java.util.HashMap;
import java.util.Map;

public class ActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

	private Map<Activity,Bundle> savedInstancesStates = new HashMap<Activity,Bundle>();




	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		try {
            LayoutHandler.getInstance().setLayout(activity);
            OnActivityCreatedHandler.handle(activity, savedInstanceState);
            savedInstancesStates.put(activity, savedInstanceState);

		} catch (Exception e) {
			throw new RuntimeException(e);

		}
	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		try {
			OnActivityDestroyedHandler.handle(activity,savedInstancesStates.get(activity));
            savedInstancesStates.remove(activity);

		} catch (Exception e) {
			throw new RuntimeException(e);

		}
	}

	@Override
	public void onActivityPaused(Activity activity) {
		try {
			OnActivityPausedHandler.handle(activity,savedInstancesStates.get(activity));

		} catch (Exception e) {
			throw new RuntimeException(e);

		}
	}

	@Override
	public void onActivityResumed(Activity activity) {
		try {
			OnActivityResumedHandler.handle(activity,savedInstancesStates.get(activity));

		} catch (Exception e) {
			throw new RuntimeException(e);

		}
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
		try {
            savedInstancesStates.put(activity, outState);
			OnActivitySaveInstanceStateHandler.handle(activity, outState);
		} catch (Exception e) {
			throw new RuntimeException(e);

		}
	}

	@Override
	public void onActivityStarted(Activity activity) {
		try {
			OnActivityStartedHandler.handle(activity,savedInstancesStates.get(activity));

		} catch (Exception e) {
			throw new RuntimeException(e);

		}
	}

	@Override
	public void onActivityStopped(Activity activity) {
		try {
			OnActivityStoppedHandler.handle(activity,savedInstancesStates.get(activity));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}




}
