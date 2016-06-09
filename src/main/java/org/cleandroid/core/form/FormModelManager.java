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
package org.cleandroid.core.form;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class FormModelManager {

	public static <T> org.cleandroid.core.form.Form<T> createForm(Context context,T formModel) {
		org.cleandroid.core.form.Form<T> form = new org.cleandroid.core.form.Form<T>(context,formModel);
		form.setOrientation(LinearLayout.VERTICAL);
		form.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		List<Field> fields = org.cleandroid.core.util.ReflectionUtils.getAnnotatedFields(formModel.getClass(), org.cleandroid.core.form.annotation.FormView.class);
		Collections.sort(fields, new Comparator<Field>(){

			@Override
			public int compare(Field field1, Field field2) {
				return field1.getAnnotation(org.cleandroid.core.form.annotation.FormView.class).appearanceIndex() - field2.getAnnotation(org.cleandroid.core.form.annotation.FormView.class).appearanceIndex();
			}
			
		});
		for (Field f : fields) {
				f.setAccessible(true);
				String labelText = f.getAnnotation(org.cleandroid.core.form.annotation.FormView.class).label();
				if(labelText.equals("@:_DEFAULT"))
					labelText = org.cleandroid.core.util.StringUtils.toLabel(f.getName());
				if(!labelText.isEmpty())
					form.addView(createLabel(labelText, context));
				int style = f.isAnnotationPresent(org.cleandroid.core.form.annotation.FormViewStyle.class) ? f.getAnnotation(org.cleandroid.core.form.annotation.FormViewStyle.class).style():0;
				int background = f.isAnnotationPresent(org.cleandroid.core.form.annotation.FormViewStyle.class) ? f.getAnnotation(org.cleandroid.core.form.annotation.FormViewStyle.class).background():0;

				form.addView(f.getName(),createView(f.getAnnotation(org.cleandroid.core.form.annotation.FormView.class).type(),context,style,background));
			
		}
		org.cleandroid.core.form.FormModelHandler.handle(form, formModel);
		return form;
	}

	@SuppressLint("NewApi")
	private static View createView(org.cleandroid.core.form.FormType type, Context context, int style, int background) {
		View view = null;
		Context mContext = null;
		if(style!=0)
			mContext = new ContextThemeWrapper(context, style);
		else mContext = context;
		
		switch (type) {
		case EditText:
			view = new EditText(mContext);
			break;
		case Spinner:
			view = new Spinner(mContext);
			break;
		case CheckBox:
			view = new CheckBox(mContext);
			break;
		case RadioButton:
			view = new RadioButton(mContext);
			break;
		case ToggleButton:
			view = new ToggleButton(mContext);
			break;
		case DatePicker:
			view = new DatePicker(mContext);
			((DatePicker) view).setCalendarViewShown(false);
			break;
		case TimePicker:
			view = new TimePicker(mContext);
			break;
		}

	
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
	
		if(Build.VERSION.SDK_INT>=17)
			view.setId(View.generateViewId());
		view.setLayoutParams(params);
		
		if(background!=0)
			view.setBackgroundResource(background);
		
		return view;
	}
	
	public static TextView createLabel(String text, Context context){
		TextView label = new TextView(context);
		label.setText(text);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		label.setLayoutParams(params);
		return label;
	}

}
