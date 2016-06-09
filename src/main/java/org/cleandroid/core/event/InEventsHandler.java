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
package org.cleandroid.core.event;

import java.lang.reflect.Method;

import org.cleandroid.core.di.DependencyManager;
import org.cleandroid.core.event.annotation.OnClick;
import org.cleandroid.core.event.annotation.OnFocusChange;
import org.cleandroid.core.event.annotation.OnItemClick;
import org.cleandroid.core.event.annotation.OnItemLongClick;
import org.cleandroid.core.event.annotation.OnItemSelected;
import org.cleandroid.core.event.annotation.OnKey;
import org.cleandroid.core.event.annotation.OnLongClick;
import org.cleandroid.core.event.annotation.OnTouch;
import org.cleandroid.core.util.TypeUtils;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;

public class InEventsHandler {
	
	
	public static void handle(final Object context){

        View rootView = null;
        if(context instanceof Activity)
            rootView = ((Activity) context).findViewById(android.R.id.content);
        else if(context instanceof Fragment)
            rootView = ((Fragment) context).getView();
        else if(context instanceof Dialog)
            rootView = ((Dialog) context).findViewById(android.R.id.content);

		for(final Method m:context.getClass().getMethods()){
			if(m.isAnnotationPresent(OnClick.class)){
				rootView.findViewById(m.getAnnotation(OnClick.class).value()).setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View view) {
						Object[] parameters = DependencyManager.getMethodDependencies(m, context, view);
						invokeMethod(m, context,parameters);
					}
					
				});
			}
			
			if(m.isAnnotationPresent(OnLongClick.class)){
				rootView.findViewById(m.getAnnotation(OnLongClick.class).value()).setOnLongClickListener(new OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View view) {
                        Object[] parameters = DependencyManager.getMethodDependencies(m, context, view);
                        invokeMethod(m, context, parameters);
                        return false;
                    }

                });
			}
			
			if(m.isAnnotationPresent(OnTouch.class)){
				rootView.findViewById(m.getAnnotation(OnTouch.class).value()).setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        Object[] parameters = DependencyManager.getMethodDependencies(m, context, view, motionEvent);
                        invokeMethod(m, context, parameters);
                        return false;
                    }

                });
			}
			
			if(m.isAnnotationPresent(OnFocusChange.class)){
				rootView.findViewById(m.getAnnotation(OnFocusChange.class).value()).setOnFocusChangeListener(new OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {

                        try {
                            Object[] args = DependencyManager.getMethodDependencies(m, context, view, hasFocus);
                            invokeMethod(m, context, args);
                        } catch (Exception e) {
                            Log.e("CleanDroid", e.getMessage(), e);
                        }


                    }
                });
			}
			
			
			if(m.isAnnotationPresent(OnKey.class)){
				rootView.findViewById(m.getAnnotation(OnKey.class).value()).setOnKeyListener(new OnKeyListener() {

                    @Override
                    public boolean onKey(View view, int keyCode, KeyEvent e) {
                        Object[] parameters = DependencyManager.getMethodDependencies(m, context, view, keyCode, e);
                        invokeMethod(m, context, parameters);
                        return false;
                    }

                });
			}

			if(m.isAnnotationPresent(OnItemSelected.class)){
				View view = rootView.findViewById(m.getAnnotation(OnItemSelected.class).value());
				if(view instanceof AdapterView) {
					((AdapterView) view).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
							Object[] parameters = DependencyManager.getMethodDependencies(m, context, view, position,parent.getItemAtPosition(position));
							invokeMethod(m, context,parameters);
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {}
					});
				}
				else{
					throw new RuntimeException("Cannot attach OnItemSelectedListener to " + view.getClass().getName());
				}
			}

            if(m.isAnnotationPresent(OnItemClick.class)){
                View view = rootView.findViewById(m.getAnnotation(OnItemClick.class).value());
                if(view instanceof AdapterView) {
                    ((AdapterView) view).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Object[] parameters = DependencyManager.getMethodDependencies(m, context, view, parent.getItemAtPosition(position));
                            invokeMethod(m, context, parameters, position);
                        }
                    });
                }
                else{
                    throw new RuntimeException("Cannot attach OnItemClickListener to " + view.getClass().getName());
                }
            }

            if(m.isAnnotationPresent(OnItemLongClick.class)){
                if(!TypeUtils.isTypeInRange(m.getReturnType(),boolean.class,Boolean.class)){
                    throw new RuntimeException("Method " + m.getName() +" declared in "+context.getClass().getName() + " and annotated with OnItemLongClickListener should return a boolean");
                }
                View view = rootView.findViewById(m.getAnnotation(OnItemLongClick.class).value());
                if(view instanceof AdapterView) {
                    ((AdapterView) view).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            Object[] parameters = DependencyManager.getMethodDependencies(m, context, view,parent.getItemAtPosition(position));
                            m.setAccessible(true);
                            try {
                                return (boolean) m.invoke(context, parameters, parent.getItemAtPosition(position));
                            }
                            catch (Exception e){
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
                else{
                    throw new RuntimeException("Cannot attach OnItemLongClickListener to " + view.getClass().getName());
                }
            }
			
		}
	}
	
	
	public static void invokeMethod(Method m,Object receiver,Object...args){
		try{
			m.setAccessible(true);
			m.invoke(receiver, args);
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	

}
