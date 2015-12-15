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
package org.cleandroid.view;

import java.util.Collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;

public class AdapterViewManager {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> void fill(final Context context,
							final AdapterView adapterView,
							final int itemsView,
							final Collection<T> items,
							final AdapterViewListener<T> adapterViewListener){
		
		BaseAdapter adapter = new BaseAdapter(){

			@Override
			public int getCount() {
				return items.size();
			}

			@Override
			public T getItem(int position) {
				return (T) items.toArray()[position];
			}

			@Override
			public long getItemId(int position) {
				return (long) position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup rootView) {
				if(convertView==null)
					convertView = LayoutInflater.from(context).inflate(itemsView, rootView,false);
				
				adapterViewListener.onCreateView(convertView, getItem(position));
				
				return convertView;
			}
			
		};
		
		adapterView.setAdapter(adapter);
		adapterView.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view,int position, long id) {
				
				adapterViewListener.onItemSelected((T) adapterView.getItemAtPosition(position));
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
				
			}
			
		});
		
		
	}

}
