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

public class PostRequest extends Request{


	public PostRequest(String url){
		super(url, HttpMethod.POST);
	}
	
	public org.cleandroid.core.http.PostRequest addParameter(String name, String value){
		parameters.put(name, value);
		return this;
	}
	

	public org.cleandroid.core.http.PostRequest addFile(String name, File file){
		files.put(name,file);
		return this;
	}

	@Override
	public org.cleandroid.core.http.PostRequest setRequestBody(String requestBody){
		return (org.cleandroid.core.http.PostRequest) super.setRequestBody(requestBody);
	}

	@Override
	public org.cleandroid.core.http.PostRequest setRequestBody(File file){
		return (org.cleandroid.core.http.PostRequest) super.setRequestBody(file);
	}

	@Override
	public org.cleandroid.core.http.PostRequest setRequestBody(InputStream inputStream){
		return (org.cleandroid.core.http.PostRequest) super.setRequestBody(inputStream);
	}

	@Override
	public org.cleandroid.core.http.PostRequest setRequestBody(byte[] bytes){
		return (org.cleandroid.core.http.PostRequest) super.setRequestBody(bytes);
	}

	@Override
	public org.cleandroid.core.http.PostRequest setBasicAuthCredentials(String username, String password) {
		return (org.cleandroid.core.http.PostRequest) super.setBasicAuthCredentials(username, password);
	}

	@Override
	public org.cleandroid.core.http.PostRequest addHeader(String name, String value) {
		return (org.cleandroid.core.http.PostRequest) super.addHeader(name, value);
	}

	@Override
	public Request setFollowRedirects(boolean followRedirects) {
		return (org.cleandroid.core.http.PostRequest) super.setFollowRedirects(followRedirects);
	}
}
