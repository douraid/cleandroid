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


public class PutRequest extends Request{
	
	public PutRequest(String url){
		super(url,HttpMethod.PUT);
	}

	public PutRequest addParameter(String name, String value){
		parameters.put(name, value);
		return this;
	}
	
	
	public PutRequest addFile(String name, File file){
		files.put(name,file);
		return this;
	}
	
	public PutRequest setRequestBody(String requestBody){
		return (PutRequest) super.setRequestBody(requestBody);
	}
	
	public PutRequest setRequestBody(File file, String contentType){
		return (PutRequest) super.setRequestBody(file);
	}
	
	public PutRequest setRequestBody(InputStream inputStream, long length){
		return (PutRequest) super.setRequestBody(inputStream);
	}
	
	public PutRequest setRequestBody(byte[] bytes){
		return (PutRequest) super.setRequestBody(bytes);
	}

}
