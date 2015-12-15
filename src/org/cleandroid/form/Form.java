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
package org.cleandroid.form;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import org.cleandroid.http.OnHttpResponseListener;
import org.cleandroid.http.PostRequest;
import org.cleandroid.http.SimpleHttpClient;
import org.cleandroid.util.ViewUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

@SuppressLint("ViewConstructor")
public class Form<T> extends LinearLayout{

	private Map<String,View> views = new TreeMap<String,View>();
	private Context context;
	private T formModel;
	
	public Form(Context context, T formModel) {
		super(context);
		this.context = context;
		this.formModel = formModel;
	}
	
	public void addView(String key,View view){
		super.addView(view);
		views.put(key, view);
	}
	
	public View getView(String key){
		return views.get(key);
	}
	
	public T getView(String key, Class<T> type){
		return type.cast(getView(key));
	}
	
	public Map<String,View> getViews(){
		return views;
	}
	
	public T getModel(){
		return formModel;
	}
	
	
	public Form<T> addSubmitButton(String buttonLabel,final Class<?> destinationActivity){
		Button button = createButton(context,buttonLabel);
		addView(buttonLabel,button);
		
		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				
				Intent i = new Intent(context,destinationActivity);
				for(String key:views.keySet()){
					i.putExtra(key, (Serializable) ViewUtils.getViewInputValue(views.get(key)));
				}
				context.startActivity(i);
				
			}
			
		});
		return this;
	}
	
	public Form<T> addSubmitButton(String buttonLabel, Integer style, Integer background, final Class<?> destinationActivity){
		Button button = null;
		if(style!=null)
			button = createButton(new ContextThemeWrapper(context, style), buttonLabel);
		else button = createButton(context, buttonLabel);
		if(background!=null)
			button.setBackgroundResource(background);
		addView(buttonLabel,button);
		
		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				
				Intent i = new Intent(context,destinationActivity);
				for(String key:views.keySet()){
					i.putExtra(key, (Serializable) ViewUtils.getViewInputValue(views.get(key)));
				}
				context.startActivity(i);
				
			}
			
		});
		return this;
	}
	
	
	
	public Form<T> addHttpSubmitButton(String buttonLabel, final String targetUrl, final OnHttpResponseListener onHttpResponseListener){
		Button button = createButton(context,buttonLabel);
		addView(buttonLabel, button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				PostRequest request = SimpleHttpClient.POST(targetUrl);
				for(String key:views.keySet()){
					request.addParameter(key, String.valueOf(ViewUtils.getViewInputValue(views.get(key))));
				}
				request.process(onHttpResponseListener);
				
			}
		});
		return this;
	}
	
	public Form<T> addHttpSubmitButton(String buttonLabel, Integer style, Integer background, final String targetUrl, final OnHttpResponseListener onHttpResponseListener){
		Button button = null;
		if(style!=null)
			button = createButton(new ContextThemeWrapper(context, style), buttonLabel);
		else button = createButton(context, buttonLabel);
		if(background!=null)
			button.setBackgroundResource(background);
		addView(buttonLabel, button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				PostRequest request = SimpleHttpClient.POST(targetUrl);
				for(String key:views.keySet()){
					request.addParameter(key, String.valueOf(ViewUtils.getViewInputValue(views.get(key))));
				}
				request.process(onHttpResponseListener);
				
			}
		});
		return this;
	}
	
	public Form<T> addButton(String buttonLabel, OnClickListener onClickListener){
		Button button = createButton(context,buttonLabel);
		addView(buttonLabel,button);
		button.setOnClickListener(onClickListener);
		return this;
	}
	
	public Form<T> addButton(String buttonLabel, Integer style, Integer background, OnClickListener onClickListener){
		Button button = null;
		if(style!=null)
			button = createButton(new ContextThemeWrapper(context, style), buttonLabel);
		else button = createButton(context, buttonLabel);
		if(background!=null)
			button.setBackgroundResource(background);
		addView(buttonLabel,button);
		button.setOnClickListener(onClickListener);
		return this;
	}
	
	
	@SuppressLint("NewApi")
	private Button createButton(Context context, String buttonLabel){
		Button button = new Button(context);
		if(Build.VERSION.SDK_INT>=17)
			button.setId(View.generateViewId());
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		button.setLayoutParams(params);
		button.setText(buttonLabel);
		return button;
	}
	
	
	

		

	

}
