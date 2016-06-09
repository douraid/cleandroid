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
package org.cleandroid.core.validation.validators;

import org.cleandroid.core.validation.CleandroidValidator;
import org.cleandroid.core.validation.annotation.CreditCard;

import java.lang.annotation.Annotation;

public class CreditCardValidator implements CleandroidValidator {

	CreditCard constraint;

	@Override
	public boolean isValid(Object value, Annotation constraint) {
		if(value==null || String.valueOf(value).isEmpty())
			return true;
		this.constraint = (CreditCard) constraint;
		String cardNumber = String.valueOf(value);
		if (cardNumber.length() < 13 || cardNumber.length() > 19)
			return false;
		else if (!LuhnValidator.luhnCheck(cardNumber))
			return false;

		else {
			switch(this.constraint.type()){
			case AMEX: return new Amex().matches(cardNumber);
			case DISCOVER: return new Discover().matches(cardNumber);
			case MASTERCARD: return new Mastercard().matches(cardNumber);
			case VISA: return new Visa().matches(cardNumber);
			case DINERS: return new Diners().matches(cardNumber);
			case ANY: return true;
			
			}
		}
		return false;

	}

	@Override
	public String getErrorMessage() {
		return constraint.message();
	}

	public interface CreditCardInterface {

		boolean matches(String card);

	}


	private static class Visa implements CreditCardInterface {
		private static final String PREFIX = "4";

		public boolean matches(String card) {
			return (card.substring(0, 1).equals(PREFIX) && (card.length() == 13 || card
					.length() == 16));
		}
	}

	private static class Amex implements CreditCardInterface {
		private static final String PREFIX = "34,37,";

		public boolean matches(String card) {
			String prefix2 = card.substring(0, 2) + ",";
			return ((PREFIX.contains(prefix2)) && (card.length() == 15));
		}
	}

	private static class Discover implements CreditCardInterface {
		private static final String PREFIX = "6011";

		public boolean matches(String card) {
			return (card.substring(0, 4).equals(PREFIX) && (card.length() == 16));
		}
	}

	private static class Mastercard implements CreditCardInterface {
		private static final String PREFIX = "51,52,53,54,55,";

		public boolean matches(String card) {
			String prefix2 = card.substring(0, 2) + ",";
			return ((PREFIX.contains(prefix2)) && (card.length() == 16));
		}
	}
	
	private static class Diners implements CreditCardInterface {
		private static final String PREFIX = "300,301,302,303,304,305,";

		public boolean matches(String card) {
			String prefix2 = card.substring(0, 3) + ",";
			return ((PREFIX.contains(prefix2)) && (card.length() == 14));
		}
	}
}