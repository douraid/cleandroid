package org.cleandroid.core.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.cleandroid.core.util.IOUtils;

public class HttpResponse {

	private HttpURLConnection connection;

	private int statusCode;
	
	private String responseMessage;
	
	private String contentType;
	
	private Map<String,String> headers = new HashMap<String,String>();
	
	private InputStream responseBody;
	
	private int contentLength;
	
	private String contentEncoding;

	public int getStatusCode() {
		return statusCode;
	}

	void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
	
	public String getHeader(String name){
		return headers.get(name);
	}
	
	void addHeader(String name, String value){
		headers.put(name, value);
	}


	public InputStream getResponseBodyAsInputStream() {
		return responseBody;
	}
	
	public String getResponseBodyAsString(){
		try {
			return IOUtils.readStreamAsString(responseBody);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			connection.disconnect();
		}
	}
	
	public File getResponseBodyAsFile(String filePath){

		File f =  IOUtils.readStreamAsFile(responseBody,filePath);
		connection.disconnect();
		return f;
	}
	

	void setResponseBody(InputStream responseBody) {
		this.responseBody = responseBody;
	}

	public String getContentType() {
		return contentType;
	}

	void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public int getContentLength() {
		return contentLength;
	}

	void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public String getContentEncoding() {
		return contentEncoding;
	}

	void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public void setConnection(HttpURLConnection connection) {
		this.connection = connection;
	}
}
