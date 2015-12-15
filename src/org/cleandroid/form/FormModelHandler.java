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

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.cleandroid.form.annotation.FormView;
import org.cleandroid.util.TypeUtils;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.ToggleButton;

public class FormModelHandler {

	public static <T> T handle(Class<T> varClass,Activity activity) {
		T returnObject = null;

				try{
				returnObject = varClass.newInstance();
				}
				catch(Exception e){
					throw new RuntimeException(e);
				}
				for (final Field f2 : varClass.getDeclaredFields()) {
					if (f2.isAnnotationPresent(FormView.class)) {
						f2.setAccessible(true);
						
						if(f2.getType().equals(String.class)){
							try{
								f2.set(returnObject, TypeUtils.getTypedValue(new String(), f2.getType()));
							}
							catch(Exception e){}
						}
						
						if(f2.getAnnotation(FormView.class).id()==0)
							continue;
						
						View view = activity.findViewById(f2.getAnnotation(
								FormView.class).id());
						if (view instanceof EditText)
							bindEditTextValue(view, returnObject, f2, activity);

						else if (view instanceof Spinner)
							bindSpinnerValue(view, returnObject, f2, activity);

						else if (view instanceof CheckBox || view instanceof RadioButton || view instanceof ToggleButton)
							bindCompoundButtonValue(view, returnObject, f2, activity);
						else if(view instanceof DatePicker)
							bindDatePickerValue(view, returnObject, f2, activity);
						else if(view instanceof TimePicker)
							bindTimePickerValue(view, returnObject, f2, activity);
							
				}
		}
			return returnObject;
	}
	
	public static <T> void handle(Form<T> form, T formModel, Activity activity){
		
		for(String key:form.getViews().keySet()){
			View view = form.getViews().get(key);
			for(Field f:formModel.getClass().getDeclaredFields()){
				
				if(!f.isAnnotationPresent(FormView.class))
					continue;
				
				f.setAccessible(true);
				
				if(f.getType().equals(String.class)){
					try{
						f.set(formModel, TypeUtils.getTypedValue(new String(), f.getType()));
					}
					catch(Exception e){}
				}
				
				if(!key.equals(f.getName()))
					continue;
				
				if (view instanceof EditText)
					bindEditTextValue(view, formModel, f, activity);

				else if (view instanceof Spinner)
					bindSpinnerValue(view, formModel, f, activity);

				else if (view instanceof CheckBox || view instanceof RadioButton || view instanceof ToggleButton)
					bindCompoundButtonValue(view, formModel, f, activity);
				else if(view instanceof DatePicker)
					bindDatePickerValue(view, formModel, f, activity);
				else if(view instanceof TimePicker)
					bindTimePickerValue(view, formModel, f, activity);
			}
		}
		
	}

	private static void bindEditTextValue(final View view, final Object target,
			final Field f2, final Activity activity) {
		f2.setAccessible(true);

		((EditText) view).addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				Object value = TypeUtils.getTypedValue(s.toString(), f2.getType());
				try {
					if(value!=null)
						f2.set(target, value);
				}
				catch(Exception e){
					Log.e("CleanDroid", e.getMessage(), e);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
					return;
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
					return;
			}

		});
	}

	private static void bindSpinnerValue(final View view, final Object target,
			final Field f2, final Activity activity) {
		f2.setAccessible(true);
		try{
			Object selectedItem = ((Spinner) view).getSelectedItem();
			if(selectedItem!=null)
				f2.set(target, selectedItem);
		}
		catch(Exception e){
			Log.e("CleanDroid", e.getMessage(), e);
		}
		
		((Spinner) view)
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						try {
							if(parent.getSelectedItem()!=null)
								f2.set(target, parent.getSelectedItem());
						}
						catch(Exception e){
							Log.e("CleanDroid", e.getMessage(), e);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						return;
					}

				});

	}

	private static void bindCompoundButtonValue(final View view, final Object target,
			final Field f2, final Activity activity) {
		f2.setAccessible(true);

		Object value = null;
		if (f2.getType().equals(boolean.class)
				|| f2.getType().equals(Boolean.class)) {
			value = ((CompoundButton) view).isChecked();

		}

		else if (f2.getType().equals(int.class)
				|| f2.getType().equals(Integer.class)) {
			value = ((CompoundButton) view).isChecked() ? 1 : 0;
		}
		if (value != null) {
			try {
				f2.set(target, value);
			} catch (Exception e) {
				Log.e("CleanDroid", e.getMessage(), e);
			}
		}

		((CompoundButton) view)
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Object value = null;
						if (f2.getType().equals(boolean.class)
								|| f2.getType().equals(Boolean.class))

							value = isChecked;

						else if (f2.getType().equals(int.class)
								|| f2.getType().equals(Integer.class))
							value = isChecked ? 1 : 0;

						if (value != null) {
							try {
								f2.set(target, value);
							} catch (Exception e) {
								Log.e("CleanDroid", e.getMessage(), e);
							}
						}
					}

				});

	}
	
	private static void bindDatePickerValue(final View view, final Object target,
			final Field f2, final Activity activity){
		f2.setAccessible(true);
		final GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
		
			DatePicker dp = (DatePicker) view;
			cal.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());
			cal.set(Calendar.MONTH, dp.getMonth());
			cal.set(Calendar.YEAR, dp.getYear());
			try{
				f2.set(target, cal.getTime());
			}
			catch(Exception e){
				Log.e("CleanDroid", e.getMessage());
			}
		
		((DatePicker) view).init(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), new OnDateChangedListener(){

			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				cal.set(Calendar.MONTH, monthOfYear);
				cal.set(Calendar.YEAR, year);
				try{
				f2.set(target, cal.getTime());
				}
				catch(Exception e){
					Log.e("CleanDroid", e.getMessage());
				}
			}
			
		});

		
		
	}
	
	private static void bindTimePickerValue(final View view, final Object target,
			final Field f2, final Activity activity){
		
		f2.setAccessible(true);
		final GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
		
			DatePicker dp = (DatePicker) view;
			cal.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());
			cal.set(Calendar.MONTH, dp.getMonth());
			cal.set(Calendar.YEAR, dp.getYear());
			try{
				f2.set(target, cal.getTime());
			}
			catch(Exception e){
				Log.e("CleanDroid", e.getMessage(), e);
			}
		
		((TimePicker) view).setOnTimeChangedListener(new OnTimeChangedListener(){

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
				cal.set(Calendar.HOUR, hourOfDay);
				cal.set(Calendar.MINUTE, minute);
				try{
					f2.set(target, cal.getTime());
				}
				catch(Exception e){
					Log.e("CleanDroid", e.getMessage(), e);
				}
			}
			
			
		});
			
	}


}
