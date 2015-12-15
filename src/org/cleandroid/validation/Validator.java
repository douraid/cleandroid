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
package org.cleandroid.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.cleandroid.util.ViewUtils;
import org.cleandroid.validation.annotation.ABANumber;
import org.cleandroid.validation.annotation.Choice;
import org.cleandroid.validation.annotation.CreditCard;
import org.cleandroid.validation.annotation.Email;
import org.cleandroid.validation.annotation.GreaterThan;
import org.cleandroid.validation.annotation.GreaterThanOrEqual;
import org.cleandroid.validation.annotation.Iban;
import org.cleandroid.validation.annotation.Ip;
import org.cleandroid.validation.annotation.Length;
import org.cleandroid.validation.annotation.LessThan;
import org.cleandroid.validation.annotation.LessThanOrEqual;
import org.cleandroid.validation.annotation.Luhn;
import org.cleandroid.validation.annotation.NotBlank;
import org.cleandroid.validation.annotation.NotNull;
import org.cleandroid.validation.annotation.Range;
import org.cleandroid.validation.annotation.Regex;
import org.cleandroid.validation.annotation.Url;
import org.cleandroid.validation.validators.ABANumberValidator;
import org.cleandroid.validation.validators.ChoiceValidator;
import org.cleandroid.validation.validators.CreditCardValidator;
import org.cleandroid.validation.validators.EmailValidator;
import org.cleandroid.validation.validators.GreaterThanOrEqualValidator;
import org.cleandroid.validation.validators.GreaterThanValidator;
import org.cleandroid.validation.validators.IBANValidator;
import org.cleandroid.validation.validators.IpValidator;
import org.cleandroid.validation.validators.LengthValidator;
import org.cleandroid.validation.validators.LessThanOrEqualValidator;
import org.cleandroid.validation.validators.LessThanValidator;
import org.cleandroid.validation.validators.LuhnValidator;
import org.cleandroid.validation.validators.NotBlankValidator;
import org.cleandroid.validation.validators.NotNullValidator;
import org.cleandroid.validation.validators.RangeValidator;
import org.cleandroid.validation.validators.RegexValidator;
import org.cleandroid.validation.validators.UrlValidator;

import android.content.Context;
import android.util.Log;
import android.view.View;

public class Validator {
	
	private static Map<Class<?>,Class<?>> validators = new HashMap<Class<?>,Class<?>>();
	private Context context;
	
	static {
		registerValidator(ABANumber.class, ABANumberValidator.class);
		registerValidator(CreditCard.class, CreditCardValidator.class);
		registerValidator(Email.class, EmailValidator.class);
		registerValidator(GreaterThan.class, GreaterThanValidator.class);
		registerValidator(GreaterThanOrEqual.class, GreaterThanOrEqualValidator.class);
		registerValidator(Iban.class, IBANValidator.class);
		registerValidator(Ip.class, IpValidator.class);
		registerValidator(Length.class, LengthValidator.class);
		registerValidator(LessThan.class, LessThanValidator.class);
		registerValidator(LessThanOrEqual.class, LessThanOrEqualValidator.class);
		registerValidator(Luhn.class, LuhnValidator.class);
		registerValidator(NotBlank.class, NotBlankValidator.class);
		registerValidator(Range.class, RangeValidator.class);
		registerValidator(Regex.class, RegexValidator.class);
		registerValidator(Url.class, UrlValidator.class);
		registerValidator(NotNull.class, NotNullValidator.class);
		registerValidator(Choice.class, ChoiceValidator.class);
	}
	
	public Validator(Context context){
		this.context = context;
	}
	
	public ConstraintSet validateView(View view){
		ConstraintSet constraintSet = new ConstraintSet(view);
		Object value = String.valueOf(ViewUtils.getViewInputValue(view));

		if(value==null)
			throw new IllegalArgumentException("View of type " + view.getClass().getName() + " cannot be validated. Only EditText, CheckBox, RadioButton, ToggleButton, Spinner, DatePicker and TimePicker are accepted");
		
		Annotation[] annotations = null;
		for(Field f: context.getClass().getDeclaredFields()){
			
			f.setAccessible(true);
			try{

			if(f.get(context).equals(view)){
				annotations = f.getAnnotations();
				break;
			}
			}
			catch(IllegalAccessException e){
				Log.e("CleanDroid", e.getMessage(),e);
			}
	
		}
		
		for(Annotation a:annotations){
			if(validators.containsKey(a.annotationType())){
				try{
					CleandroidValidator validator = (CleandroidValidator) validators.get(a.annotationType()).newInstance();
					Constraint ruleValidation = new Constraint(a);

					if(!validator.isValid(String.valueOf(value),a)){
						ruleValidation.setMessage(validator.getErrorMessage());
					}
					constraintSet.addConstraint(a, ruleValidation);
					
				}
				catch(Exception e){
					throw new RuntimeException(e);
				}
			}
		}
				
		return constraintSet;
	}
	
	public ConstraintSetCollection validateViews(View...views){
		ConstraintSetCollection collection = new ConstraintSetCollection();
		for(View v:views){
			collection.addViewConstraintSet(v, validateView(v));
		}
		return collection;
	}
	
	
	public ConstraintSetCollection validateBean(Object bean){
		
		ConstraintSetCollection collection = new ConstraintSetCollection();
		ConstraintSet[] constraintSets = new ConstraintSet[bean.getClass().getDeclaredFields().length];
		int i = 0;
		for(Field f:bean.getClass().getDeclaredFields()){
			f.setAccessible(true);
			constraintSets[i] = new ConstraintSet(f.getName());
			try {
				Object value = f.get(bean);
				
				for(Annotation a:f.getAnnotations()){
					if(validators.containsKey(a.annotationType())){
						Constraint ruleValidation = new Constraint(a);

						try{
							CleandroidValidator validator = (CleandroidValidator) validators.get(a.annotationType()).newInstance();

							if(!validator.isValid(value,a)){
								ruleValidation.setMessage(validator.getErrorMessage());
							}
						constraintSets[i].addConstraint(a, ruleValidation);
						}
						catch(Exception e){
							throw new RuntimeException(e);
						}
					}
				}
				
			} catch (Exception e) {
				Log.e("CleanDroid",e.getMessage(),e);
			} 
			
			i++;
		}
		
		for(ConstraintSet cs:constraintSets){
			collection.addBeanConstraintSet(bean, cs);
		}

		
		return collection;
		
	}
	
	
	
	
	
	public static void registerValidator(Class<?> annotation, Class<?> validatorClass){
		if(!annotation.isAnnotation())
			throw new RuntimeException("Parameter 1 musty be an annotation type");
		if(!CleandroidValidator.class.isAssignableFrom(validatorClass))
			throw new RuntimeException("Parameter 2 must implements " + CleandroidValidator.class.getName());
		validators.put(annotation, validatorClass);
	}

}
