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
package org.cleandroid.core.http;


public class DeleteRequest extends Request {
	
	public DeleteRequest(String url){
		super(url,HttpMethod.DELETE);
	}

	@Override
	public org.cleandroid.core.http.DeleteRequest addHeader(String name, String value) {
		return (org.cleandroid.core.http.DeleteRequest) super.addHeader(name, value);
	}

	@Override
	public org.cleandroid.core.http.DeleteRequest setFollowRedirects(boolean followRedirects) {
		return (org.cleandroid.core.http.DeleteRequest) super.setFollowRedirects(followRedirects);
	}

	@Override
	public org.cleandroid.core.http.DeleteRequest setBasicAuthCredentials(String username, String password) {
		return (org.cleandroid.core.http.DeleteRequest) super.setBasicAuthCredentials(username, password);
	}
}
