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


public class GetRequest extends Request {
	
	public GetRequest(String url){
		super(url,HttpMethod.GET);
	}

	@Override
	public org.cleandroid.core.http.GetRequest addHeader(String name, String value) {
		return (org.cleandroid.core.http.GetRequest) super.addHeader(name, value);
	}

	@Override
	public org.cleandroid.core.http.GetRequest setFollowRedirects(boolean followRedirects) {
		return (org.cleandroid.core.http.GetRequest) super.setFollowRedirects(followRedirects);
	}

    @Override
    public org.cleandroid.core.http.GetRequest setBasicAuthCredentials(String username, String password){
        return (org.cleandroid.core.http.GetRequest) super.setBasicAuthCredentials(username,password);
    }

}
