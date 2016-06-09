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
package org.cleandroid.core.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

public class ViewUtils {

	public static Object getViewInputValue(View view){
		if(view instanceof EditText)
			return ((EditText) view).getText().toString();
		if(view instanceof CompoundButton)
			return ((CompoundButton) view).isChecked();
		if(view instanceof Spinner)
			return ((Spinner) view).getSelectedItem();
		if(view instanceof DatePicker){
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, ((DatePicker) view).getDayOfMonth());
			cal.set(Calendar.MONTH, ((DatePicker) view).getMonth());
			cal.set(Calendar.YEAR, ((DatePicker) view).getYear());
			return cal.getTime();
		}
		if(view instanceof TimePicker){
			GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
			cal.set(Calendar.HOUR, ((TimePicker) view).getCurrentHour());
			cal.set(Calendar.HOUR_OF_DAY, ((TimePicker) view).getCurrentHour());
			cal.set(Calendar.MINUTE, ((TimePicker) view).getCurrentMinute());
			return cal.getTime();
		}
		return null;
	}
	
}
