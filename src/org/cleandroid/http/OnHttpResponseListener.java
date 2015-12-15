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
package org.cleandroid.http;

public interface OnHttpResponseListener {
	
	void beforeRequest();
	
	void onSuccess(int statusCode, String responseData);
	
	void onFail(int statusCode, String message);
	
	void onException(String message, Throwable exception);
	
	void afterRequest();
	

}
