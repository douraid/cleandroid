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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.cleandroid.form.annotation.FormView;
import org.cleandroid.form.annotation.FormViewStyle;
import org.cleandroid.util.ReflectionUtils;
import org.cleandroid.util.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
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

	public static <T> Form<T> createForm(Context context,T formModel) {
		Form<T> form = new Form<T>(context,formModel);
		form.setOrientation(LinearLayout.VERTICAL);
		form.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		List<Field> fields = ReflectionUtils.getAnnotatedFields(formModel.getClass(), FormView.class);
		Collections.sort(fields, new Comparator<Field>(){

			@Override
			public int compare(Field field1, Field field2) {
				return field1.getAnnotation(FormView.class).appearanceIndex() - field2.getAnnotation(FormView.class).appearanceIndex();
			}
			
		});
		for (Field f : fields) {
				f.setAccessible(true);
				String labelText = f.getAnnotation(FormView.class).label();
				if(labelText.equals("@:_DEFAULT"))
					labelText = StringUtils.toLabel(f.getName());
			
				form.addView(createLabel(labelText, context));
				int style = f.isAnnotationPresent(FormViewStyle.class) ? f.getAnnotation(FormViewStyle.class).style():0;
				int background = f.isAnnotationPresent(FormViewStyle.class) ? f.getAnnotation(FormViewStyle.class).background():0;

				form.addView(f.getName(),createView(f.getAnnotation(FormView.class).type(),context,style,background));
			
		}
		FormModelHandler.handle(form, formModel, (Activity) context);
		return form;
	}

	@SuppressLint("NewApi")
	private static View createView(FormType type, Context context, int style, int background) {
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
