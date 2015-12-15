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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.cleandroid.util.IOUtils;

import android.os.AsyncTask;
import android.util.Log;

public abstract class Request {

	
	protected Map<String,String> headers = new HashMap<String,String>();
	protected HttpClient client  =new DefaultHttpClient();
	protected String url;
	protected Map<String,String> parameters = new HashMap<String,String>();
	protected HttpRequest request;
	
	private Exception clientException = null;
	
	public Request addHeader(String name, String value){
		headers.put(name,value);
		return this;
	}
	

	
	public void process(final OnHttpResponseListener onHttpResponseListener){
		for(String name:headers.keySet()){
			request.addHeader(name, headers.get(name));
		}
		
		List <NameValuePair> data = new ArrayList<NameValuePair>();
		for(String name:parameters.keySet()){
			data.add(new BasicNameValuePair(name, parameters.get(name)));
		}

		if(request instanceof HttpPost){
			try{
				((HttpPost) request).setEntity(new UrlEncodedFormEntity(data));
			}
			catch(Exception e){
				Log.w("CleanDroid", e.getMessage(),e);
			}
		}
		
		new AsyncTask<Void,Void,HttpResponse>(){
			
			@Override
			protected void onPreExecute(){
				super.onPreExecute();
				onHttpResponseListener.beforeRequest();
			}

			@Override
			protected HttpResponse doInBackground(Void... params) {
				try{
					return client.execute((HttpUriRequest) request);
									}
				catch (Exception e) {
					clientException = e;
					
				}

				return null;
			}
			
			@Override
			protected void onPostExecute(HttpResponse response){
				super.onPostExecute(response);
				if(onHttpResponseListener==null)
					return;
				
				if(clientException ==null)
					onHttpResponseListener.onException(clientException.getMessage(), clientException);
				if(response!=null){
					int statusCode = response.getStatusLine().getStatusCode();
					if(statusCode>=200 && statusCode<=206){
						try{
							String responseData = IOUtils.readStreamAsString(response.getEntity().getContent());
							onHttpResponseListener.onSuccess(statusCode, responseData);
						}
						catch(IOException e){
							Log.e("CleanDroid",e.getMessage(), e);
							onHttpResponseListener.onException(e.getMessage(), e);
						}
					}
					else{
						onHttpResponseListener.onFail(statusCode, response.getStatusLine().getReasonPhrase());
					}

				}
			}
			
		}.execute();

		
		
		
	}
	
	
	public void process(){
		process(null);
	}
	
}
