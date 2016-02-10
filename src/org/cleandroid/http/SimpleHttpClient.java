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

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;

import android.os.Build;


public class SimpleHttpClient {
	
	
	private final static CookieManager cookieManager = new CookieManager();
	
	static{
		
		CookieHandler.setDefault(cookieManager);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO)
		     System.setProperty("http.keepAlive", "false");
	}
	
	public static PostRequest POST(String url){
		return new PostRequest(url);
	}
	
	public static GetRequest GET(String url){
		return new GetRequest(url);
	}
	
	public static PutRequest PUT(String url){
		return new PutRequest(url);
	}
	
	public static DeleteRequest DELETE(String url){
		return new DeleteRequest(url);
	}
	
	

	
	public static void addCookie(HttpCookie cookie, String uri){
		try{
			cookieManager.getCookieStore().add(new URI(uri), cookie);
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static void addCookie(String name, String value, String uri){
		HttpCookie cookie = new HttpCookie(name, value);
		addCookie(cookie, uri);
	}
}
