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
package org.cleandroid.core.view;

import java.util.Collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.cleandroid.R;

public class AdapterViewManager {

	private static <T> BaseAdapter createAdapter(final Context context,final T[] items){
		return new BaseAdapter() {
			@Override
			public int getCount() {
				return items.length;
			}

			@Override
			public T getItem(int position) {
				return (T) items[position];
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null)
					convertView = LayoutInflater.from(context).inflate(R.layout.adapterview_simple_text_item, null, false);
				((TextView) convertView.findViewById(R.id.adapterview_simple_text_content)).setText(String.valueOf(getItem(position)));
				return convertView;
			}
		};
	}
	
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
		adapterView.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                adapterViewListener.onItemSelected((T) adapterView.getItemAtPosition(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterViewListener.onNothingSelected();
            }

        });
		adapterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterViewListener.onItemClicked((T) adapterView.getItemAtPosition(position));
            }
        });

        adapterView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return adapterViewListener.onItemLongClicked((T) parent.getItemAtPosition(position));
            }
        });
		
		
	}




	public static <T> void fill(Context context, AdapterView adapterView, final T[] items){

        adapterView.setAdapter(createAdapter(context,items));

	}

	public static <T> void fill(final Context context, AdapterView adapterView, final Collection<T> items){
		fill(context, adapterView, items.toArray());
	}



}
