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

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.cleandroid.core.util.StringUtils;

import android.Manifest;
import android.os.AsyncTask;
import android.support.annotation.RequiresPermission;
import android.util.Base64;
import android.util.Log;

public abstract class Request {
	
	private org.cleandroid.core.http.HttpMethod method;
	protected String url;
	private Map<String,String> headers = new HashMap<String,String>();
	protected Map<String,String> parameters = new HashMap<String,String>();
	protected Map<String,File> files = new HashMap<String,File>();
	private boolean followRedirects = false;
	private String authCredentials = null;
	private Object requestBody;

	private Exception clientException;

	private HttpURLConnection connection;

	public Request(String url, org.cleandroid.core.http.HttpMethod method){
		this.url = url;
		this.method = method;
	}




	private void setHeaders(HttpURLConnection connection){
		HttpURLConnection.setFollowRedirects(followRedirects);
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("Cache-Control", "no-cache");
		connection.setRequestProperty("User-agent", "Cleandroid HTTP Client");

		for(String name:headers.keySet()){
			connection.setRequestProperty(name, headers.get(name));
		}
		if(method.equals(org.cleandroid.core.http.HttpMethod.POST) || method.equals(org.cleandroid.core.http.HttpMethod.PUT)){
			if(files.isEmpty())
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			else
				connection.setRequestProperty("Content-type", "multipart/form-data; boundary=*****");


		}
	}

	private HttpResponse executeRequest() throws MalformedURLException, IOException{
		connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		setHeaders(connection);

		if(authCredentials!=null)
			connection.setRequestProperty("Authorization",authCredentials);


		switch(method){
			case POST: connection.setRequestMethod("POST"); break;
			case GET: connection.setRequestMethod("GET"); break;
			case PUT: connection.setRequestMethod("PUT"); break;
			case DELETE: connection.setRequestMethod("DELETE"); break;
		}


		OutputStream connectionOS = connection.getOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connectionOS, "UTF-8"));

		if(requestBody!=null){

			if(requestBody instanceof String)
				writer.write(String.valueOf(requestBody));

			else if(requestBody instanceof File || requestBody instanceof InputStream || requestBody instanceof byte[]){

				 InputStream inputStream =null;
				 if(requestBody instanceof File)
					 inputStream = new FileInputStream((File) requestBody);
				 else if(requestBody instanceof byte[])
					 inputStream = new ByteArrayInputStream((byte[]) requestBody);
				 else
					 inputStream = (InputStream) requestBody;

			     byte[] buffer = new byte[4096];
			     int bytesRead = -1;
			     while ((bytesRead = inputStream.read(buffer)) != -1) {
			            connectionOS.write(buffer, 0, bytesRead);
			     }
			     connectionOS.flush();
			     inputStream.close();
			}

			else if(requestBody instanceof byte[]){
				for(byte b:((byte[]) requestBody))
					writer.write(b);

			}

			writer.flush();



		}

		else{



		if(method.equals(org.cleandroid.core.http.HttpMethod.POST) || method.equals(HttpMethod.PUT)){
			try{
				if(files.isEmpty()){
					String dataQuery = "";
					for(String name:parameters.keySet()){
						dataQuery = dataQuery.concat(URLEncoder.encode(name, "UTF-8"))
											 .concat("=")
											 .concat(URLEncoder.encode(parameters.get(name),"UTF-8"))
											 .concat("&");
					}
					if(!dataQuery.isEmpty())
						dataQuery = StringUtils.rtrim(dataQuery, "&");
					writer.write(dataQuery);
					writer.flush();
				}
				else{
					
					for(String name:parameters.keySet()){
						 writer.append("--*****\r\n");
					     writer.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
					        writer.append("Content-Type: text/plain; charset=UTF-8\r\n\r\n");
					        writer.append(parameters.get(name).concat("\r\n"));
					        writer.flush();
					}
					for(String fileKey:files.keySet()){
                        if(files.get(fileKey)==null) {
                            Log.w("SimpleHttpClient","File " + fileKey + " is null");
                            continue;
                        }
						 writer.append("--*****\r\n");
					     writer.append("Content-Disposition: form-data; name=\"" + fileKey+ "\"; filename=\"" + files.get(fileKey).getName() + "\"\r\n");
					     writer.append("Content-Type: "+ HttpURLConnection.guessContentTypeFromName(files.get(fileKey).getName())+"\r\n");
					     writer.append("Content-Transfer-Encoding: binary\r\n\r\n");
					     writer.flush();
					     
					     FileInputStream inputStream = new FileInputStream(files.get(fileKey));
					     byte[] buffer = new byte[4096];
					     int bytesRead = -1;
					     while ((bytesRead = inputStream.read(buffer)) != -1) {
					            connectionOS.write(buffer, 0, bytesRead);
					     }
					     connectionOS.flush();
					     inputStream.close();   
					     writer.append("\r\n");
					     writer.flush();
					     
					}
					
					
					
				}
			}
			catch(Exception e){
				Log.w("CleanDroid", e.getMessage(),e);
			}
		}
		
		}
		
		writer.append("\r\n");
		writer.flush();
		writer.append("--*****--\r\n");
		writer.close();
	
		
		connectionOS.close();
		
		HttpResponse response = new HttpResponse();
		response.setStatusCode(connection.getResponseCode());
		response.setResponseMessage(connection.getResponseMessage());
        if(connection.getResponseCode()>=400)
            response.setResponseBody(connection.getErrorStream());
        else
		    response.setResponseBody(connection.getInputStream());
		for(String headerName:connection.getHeaderFields().keySet())
			response.addHeader(headerName, connection.getHeaderField(headerName));
		
		response.setContentType(connection.getContentType());
		response.setContentEncoding(connection.getContentEncoding());
		response.setContentLength(connection.getContentLength());
		response.setConnection(connection);
		return response;
		}

	@RequiresPermission(Manifest.permission.INTERNET)
	public void  process(final OnHttpResponseListener onHttpResponseListener){

		
		new AsyncTask<Void,Void,HttpResponse>(){

			@Override
			protected HttpResponse doInBackground(Void... params) {
				try{
					return executeRequest();
				}
				catch(Exception e){
					clientException = e;
				}

				return null;
			}
			
			@Override
			protected void onPostExecute(HttpResponse response){
				super.onPostExecute(response);
				if(onHttpResponseListener==null)
					return;
				
				if(clientException !=null){
					onHttpResponseListener.onException(clientException.getMessage(), clientException);
					return;
				}
				if(response!=null){
					int statusCode = response.getStatusCode();
					if(statusCode>=200 && statusCode<=206)
							onHttpResponseListener.onSuccess(response.getStatusCode(), response);
					else
						onHttpResponseListener.onFail(statusCode, response.getResponseMessage(), response);
					

				}
								
			}
			
		}.execute();

	
		
		
	}
	@RequiresPermission(Manifest.permission.INTERNET)
	public HttpResponse process() throws MalformedURLException, IOException{
		return  executeRequest();
	}
	


	protected org.cleandroid.core.http.Request setRequestBody(String requestBody){
		this.requestBody = requestBody;
		return this;
	}
	
	protected org.cleandroid.core.http.Request setRequestBody(File file){
		this.requestBody = file;
		return this;
	}
	
	protected org.cleandroid.core.http.Request setRequestBody(InputStream inputStream){
		this.requestBody = inputStream;
		return this;
	}
	
	protected org.cleandroid.core.http.Request setRequestBody(byte[] bytes){
		this.requestBody = bytes;
		return this;
	}
	
	public org.cleandroid.core.http.Request setBasicAuthCredentials(String username, String password){
		authCredentials = "Basic " + new String(Base64.encode((username+":"+password).getBytes(),Base64.DEFAULT));
		return this;
		
	}

	public org.cleandroid.core.http.Request addHeader(String name, String value){
		headers.put(name,value);
		return this;
	}
	
	protected org.cleandroid.core.http.Request setFollowRedirects(boolean followRedirects){
		this.followRedirects = followRedirects;
		return this;
	}


	
}
