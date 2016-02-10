package org.cleandroid.rest;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cleandroid.http.HttpMethod;
import org.cleandroid.http.HttpResponse;
import org.cleandroid.http.PostRequest;
import org.cleandroid.http.PutRequest;
import org.cleandroid.http.Request;
import org.cleandroid.http.SimpleHttpClient;
import org.cleandroid.rest.annotation.BasicHttpAuth;
import org.cleandroid.rest.annotation.JsonEncoded;
import org.cleandroid.rest.annotation.PathParam;
import org.cleandroid.rest.annotation.RequestBody;
import org.cleandroid.rest.annotation.RequestParam;
import org.cleandroid.rest.annotation.RestClient;
import org.cleandroid.rest.annotation.RestService;
import org.cleandroid.util.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class RestInvocationHandler implements InvocationHandler{
	
	private Class<?> restClientClass;
	private static Map<String, Request> requests = new HashMap<String,Request>();
	
	public RestInvocationHandler(Class<?> restClientClass){
		this.restClientClass = restClientClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(Object proxy, Method method, Object[] parameters)
			throws Throwable {
		if(method.isAnnotationPresent(RestService.class)){
			
		Request request = null;
		if(!requests.containsKey(method.getName())){
			RestService rsAnnotation = method.getAnnotation(RestService.class);
			String serviceUrl = restClientClass.getAnnotation(RestClient.class)
					                        .baseUrl();
			if(!serviceUrl.isEmpty())
				serviceUrl = serviceUrl.concat("/");
			
			serviceUrl = serviceUrl.concat(rsAnnotation.path());
											
			
			int paramIndex = 0;
			Object requestBody = null;
			Map<String,String> requestParams = new HashMap<String,String>();
			Map<String,File> requestFiles = new HashMap<String,File>();
			
			for(Annotation[] annotations:method.getParameterAnnotations()){
				for(Annotation annotation:annotations){
					
					if(annotation.annotationType().equals(JsonEncoded.class)){
						if(Collection.class.isAssignableFrom(parameters[paramIndex].getClass()))
							parameters[paramIndex] = JSONMapper.parseJson((Collection<Serializable>) parameters[paramIndex]);
						else if(parameters[paramIndex].getClass().isArray())
							parameters[paramIndex] = JSONMapper.parseJson((Serializable[]) parameters[paramIndex]);
						else if(Serializable.class.isAssignableFrom(parameters[paramIndex].getClass()))
							parameters[paramIndex] = JSONMapper.parseJson((Serializable) parameters[paramIndex]);
						else throw new RuntimeException("Cannot serialize parameter "+paramIndex+" to a JSON string, it must be a Serializable type");
					}
					
					if(annotation.annotationType().equals(PathParam.class)){
						String pathParamName = ((PathParam) annotation).value();
						if(!serviceUrl.contains("{"+pathParamName+"}"))
							throw new RuntimeException("No Path parameter named " + pathParamName + " was found");
						
						serviceUrl = serviceUrl.replace("{"+pathParamName+"}", String.valueOf(parameters[paramIndex]));
					}
					
					else if(annotation.annotationType().equals(RequestParam.class)){
						if(requestBody!=null)
							throw new RuntimeException("Cannot add a request parameter when the request body is provided");
						if(parameters[paramIndex] instanceof File)
							requestFiles.put(((RequestParam)annotation).name(), (File) parameters[paramIndex]);
						else
							requestParams.put(((RequestParam)annotation).name(), String.valueOf(parameters[paramIndex]));
					}
					else if(annotation.annotationType().equals(RequestBody.class)){						
						requestBody = parameters[paramIndex];
					}
					

					
					
				}
				paramIndex++;
			}
			
			
			if(rsAnnotation.method().equals(HttpMethod.GET)){
				request = SimpleHttpClient.GET(serviceUrl);
			}
			
			else if(rsAnnotation.method().equals(HttpMethod.POST)){
				request = SimpleHttpClient.POST(serviceUrl);
				
				if(requestBody!=null){
					if(requestBody instanceof String)
						((PostRequest) request).setRequestBody((String) requestBody);
					else if(requestBody instanceof byte[] || requestBody instanceof Byte[])
						((PostRequest) request).setRequestBody((byte[]) requestBody);
				}
				
				else{
				
					for(String paramKey:requestParams.keySet())
						((PostRequest) request).addParameter(paramKey, requestParams.get(paramKey));
					for(String fileName:requestFiles.keySet())
						((PostRequest) request).addFile(fileName, requestFiles.get(fileName));
				}
				
				
			}
			
			else if(rsAnnotation.method().equals(HttpMethod.PUT)){
				request = SimpleHttpClient.PUT(serviceUrl);
				
				if(requestBody!=null){
					if(requestBody instanceof String)
						((PutRequest) request).setRequestBody((String) requestBody);
					else if(requestBody instanceof byte[] || requestBody instanceof Byte[])
						((PutRequest) request).setRequestBody((byte[]) requestBody);
				}
				else{
					for(String paramKey:requestParams.keySet())
						((PutRequest) request).addParameter(paramKey, requestParams.get(paramKey));
				
					for(String fileName:requestFiles.keySet())
						((PutRequest) request).addFile(fileName, requestFiles.get(fileName));
				}
				
			}
			
			else if(rsAnnotation.method().equals(HttpMethod.DELETE)){
				request = SimpleHttpClient.DELETE(serviceUrl);
			}
			
			requests.put(method.getName(), request);
		}
		else
			request = requests.get(method.getName());
		
		BasicHttpAuth httpAuthAnnotation = null;
		
		if(method.getDeclaringClass().isAnnotationPresent(BasicHttpAuth.class))
			httpAuthAnnotation = method.getDeclaringClass().getAnnotation(BasicHttpAuth.class);
		
		if(method.isAnnotationPresent(BasicHttpAuth.class))
			httpAuthAnnotation = method.getAnnotation(BasicHttpAuth.class);
		
		if(httpAuthAnnotation!=null)
			request.setBasicAuthCredentials(httpAuthAnnotation.username(), httpAuthAnnotation.password());
			
			Class<?> returnType = method.getReturnType();
			
			HttpResponse response = request.process();
			
			if(!returnType.equals(void.class) && !returnType.equals(Void.class)){
				if(returnType.equals(InputStream.class))
					return response.getResponseBodyAsInputStream();
				else if(returnType.equals(String.class)){
					return response.getResponseBodyAsString();
				}
				else if(returnType.equals(File.class)){
					return response.getResponseBodyAsFile();
				}
				else if(returnType.equals(JSONObject.class))
					return new JSONObject(response.getResponseBodyAsString());
				else if(returnType.equals(JSONArray.class))
					return new JSONArray(response.getResponseBodyAsString());
			
				else if(Collection.class.isAssignableFrom(returnType)){
					return JSONMapper.parseCollection(response.getResponseBodyAsString(), CollectionUtils.getCollectionType(method));
				}
				
				else 
					return JSONMapper.parseObject(response.getResponseBodyAsString(), returnType);
				
			}
			
			
		}
		
		return null;
	}

}
