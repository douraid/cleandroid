package org.cleandroid.di;

import android.app.Activity;

public interface DependencyInjectionHandler {
	
	public Object handle(Class<?> varClass, Activity activity);

}
