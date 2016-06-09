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

import java.io.File;
import java.io.InputStream;


public class PutRequest extends Request{
	
	public PutRequest(String url){
		super(url, HttpMethod.PUT);
	}

	public org.cleandroid.core.http.PutRequest addParameter(String name, String value){
		parameters.put(name, value);
		return this;
	}
	
	
	public org.cleandroid.core.http.PutRequest addFile(String name, File file){
		files.put(name, file);
		return this;
	}

	@Override
	public org.cleandroid.core.http.PutRequest setRequestBody(String requestBody){
		return (org.cleandroid.core.http.PutRequest) super.setRequestBody(requestBody);
	}

	@Override
	public org.cleandroid.core.http.PutRequest setRequestBody(File file){
		return (org.cleandroid.core.http.PutRequest) super.setRequestBody(file);
	}

	@Override
	public org.cleandroid.core.http.PutRequest setRequestBody(InputStream inputStream){
		return (org.cleandroid.core.http.PutRequest) super.setRequestBody(inputStream);
	}

	@Override
	public org.cleandroid.core.http.PutRequest setRequestBody(byte[] bytes){
		return (org.cleandroid.core.http.PutRequest) super.setRequestBody(bytes);
	}

    @Override
    public org.cleandroid.core.http.PutRequest addHeader(String name, String value) {
        return (org.cleandroid.core.http.PutRequest) super.addHeader(name, value);
    }

    @Override
    public org.cleandroid.core.http.PutRequest setBasicAuthCredentials(String username, String password) {
        return (org.cleandroid.core.http.PutRequest) super.setBasicAuthCredentials(username, password);
    }

    @Override
    public org.cleandroid.core.http.PutRequest setFollowRedirects(boolean followRedirects) {
        return (org.cleandroid.core.http.PutRequest) super.setFollowRedirects(followRedirects);
    }

}
