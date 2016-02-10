package org.cleandroid.rest;

import java.lang.reflect.Proxy;


import android.app.Activity;

public class RestClientHandler {
	
	@SuppressWarnings("unchecked")
	public static <T> T handle(Class<T> varClass, Activity activity){
		RestInvocationHandler handler = new RestInvocationHandler(varClass);
		return (T) Proxy.newProxyInstance(varClass.getClassLoader(),
                new Class[] { varClass },
                handler);
	}

}
