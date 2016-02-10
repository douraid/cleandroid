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


import java.io.File;
import java.io.InputStream;

public class PostRequest extends Request{
	
	
	public PostRequest(String url){
		super(url,HttpMethod.POST);
	}
	
	public PostRequest addParameter(String name, String value){
		parameters.put(name, value);
		return this;
	}
	
	
	public PostRequest addFile(String name, File file){
		files.put(name,file);
		return this;
	}
	
	public PostRequest setRequestBody(String requestBody){
		return (PostRequest) super.setRequestBody(requestBody);
	}
	
	public PostRequest setRequestBody(File file, String contentType){
		return (PostRequest) super.setRequestBody(file);
	}
	
	public PostRequest setRequestBody(InputStream inputStream, long length){
		return (PostRequest) super.setRequestBody(inputStream);
	}
	
	public PostRequest setRequestBody(byte[] bytes){
		return (PostRequest) super.setRequestBody(bytes);
	}
	


}
