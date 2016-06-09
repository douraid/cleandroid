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
import org.cleandroid.core.validation.annotation.Iban;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

public class IBANValidator implements CleandroidValidator {
	
	private static final Validator[] DEFAULT_FORMATS = {
        new Validator("AL", 28, "AL\\d{10}[A-Z0-9]{16}"                 ), // Albania
        new Validator("AD", 24, "AD\\d{10}[A-Z0-9]{12}"                 ), // Andorra
        new Validator("AT", 20, "AT\\d{18}"                             ), // Austria
        new Validator("AZ", 28, "AZ\\d{2}[A-Z]{4}[A-Z0-9]{20}"          ), // Republic of Azerbaijan
        new Validator("BH", 22, "BH\\d{2}[A-Z]{4}[A-Z0-9]{14}"          ), // Bahrain (Kingdom of)
        new Validator("BE", 16, "BE\\d{14}"                             ), // Belgium
        new Validator("BA", 20, "BA\\d{18}"                             ), // Bosnia and Herzegovina
        new Validator("BR", 29, "BR\\d{25}[A-Z]{1}[A-Z0-9]{1}"          ), // Brazil
        new Validator("BG", 22, "BG\\d{2}[A-Z]{4}\\d{6}[A-Z0-9]{8}"     ), // Bulgaria
        new Validator("CR", 21, "CR\\d{19}"                             ), // Costa Rica
        new Validator("HR", 21, "HR\\d{19}"                             ), // Croatia
        new Validator("CY", 28, "CY\\d{10}[A-Z0-9]{16}"                 ), // Cyprus
        new Validator("CZ", 24, "CZ\\d{22}"                             ), // Czech Republic
        new Validator("DK", 18, "DK\\d{16}"                             ), // Denmark
        new Validator("FO", 18, "FO\\d{16}"                             ), // Denmark (Faroes)
        new Validator("GL", 18, "GL\\d{16}"                             ), // Denmark (Greenland)
        new Validator("DO", 28, "DO\\d{2}[A-Z0-9]{4}\\d{20}"            ), // Dominican Republic
        new Validator("EE", 20, "EE\\d{18}"                             ), // Estonia
        new Validator("FI", 18, "FI\\d{16}"                             ), // Finland
        new Validator("FR", 27, "FR\\d{12}[A-Z0-9]{11}\\d{2}"           ), // France
        new Validator("GE", 22, "GE\\d{2}[A-Z]{2}\\d{16}"               ), // Georgia
        new Validator("DE", 22, "DE\\d{20}"                             ), // Germany
        new Validator("GI", 23, "GI\\d{2}[A-Z]{4}[A-Z0-9]{15}"          ), // Gibraltar
        new Validator("GR", 27, "GR\\d{9}[A-Z0-9]{16}"                  ), // Greece
        new Validator("GT", 28, "GT\\d{2}[A-Z0-9]{24}"                  ), // Guatemala
        new Validator("HU", 28, "HU\\d{26}"                             ), // Hungary
        new Validator("IS", 26, "IS\\d{24}"                             ), // Iceland
        new Validator("IE", 22, "IE\\d{2}[A-Z]{4}\\d{14}"               ), // Ireland
        new Validator("IL", 23, "IL\\d{21}"                             ), // Israel
        new Validator("IT", 27, "IT\\d{2}[A-Z]{1}\\d{10}[A-Z0-9]{12}"   ), // Italy
        new Validator("JO", 30, "JO\\d{2}[A-Z]{4}\\d{4}[A-Z0-9]{18}"    ), // Jordan
        new Validator("KZ", 20, "KZ\\d{5}[A-Z0-9]{13}"                  ), // Kazakhstan
        new Validator("XK", 20, "XK\\d{18}"                             ), // Republic of Kosovo
        new Validator("KW", 30, "KW\\d{2}[A-Z]{4}[A-Z0-9]{22}"          ), // Kuwait
        new Validator("LV", 21, "LV\\d{2}[A-Z]{4}[A-Z0-9]{13}"          ), // Latvia
        new Validator("LB", 28, "LB\\d{6}[A-Z0-9]{20}"                  ), // Lebanon
        new Validator("LI", 21, "LI\\d{7}[A-Z0-9]{12}"                  ), // Liechtenstein (Principality of)
        new Validator("LT", 20, "LT\\d{18}"                             ), // Lithuania
        new Validator("LU", 20, "LU\\d{5}[A-Z0-9]{13}"                  ), // Luxembourg
        new Validator("MK", 19, "MK\\d{5}[A-Z0-9]{10}\\d{2}"            ), // Macedonia, Former Yugoslav Republic of
        new Validator("MT", 31, "MT\\d{2}[A-Z]{4}\\d{5}[A-Z0-9]{18}"    ), // Malta
        new Validator("MR", 27, "MR13\\d{23}"                           ), // Mauritania
        new Validator("MU", 30, "MU\\d{2}[A-Z]{4}\\d{19}[A-Z]{3}"       ), // Mauritius
        new Validator("MD", 24, "MD\\d{2}[A-Z0-9]{20}"                  ), // Moldova
        new Validator("MC", 27, "MC\\d{12}[A-Z0-9]{11}\\d{2}"           ), // Monaco
        new Validator("ME", 22, "ME\\d{20}"                             ), // Montenegro
        new Validator("NL", 18, "NL\\d{2}[A-Z]{4}\\d{10}"               ), // The Netherlands
        new Validator("NO", 15, "NO\\d{13}"                             ), // Norway
        new Validator("PK", 24, "PK\\d{2}[A-Z]{4}[A-Z0-9]{16}"          ), // Pakistan
        new Validator("PS", 29, "PS\\d{2}[A-Z]{4}[A-Z0-9]{21}"          ), // Palestine, State of
        new Validator("PL", 28, "PL\\d{26}"                             ), // Poland
        new Validator("PT", 25, "PT\\d{23}"                             ), // Portugal
        new Validator("QA", 29, "QA\\d{2}[A-Z]{4}[A-Z0-9]{21}"          ), // Qatar
        new Validator("RO", 24, "RO\\d{2}[A-Z]{4}[A-Z0-9]{16}"          ), // Romania
        new Validator("LC", 32, "LC\\d{2}[A-Z]{4}\\d{24}"               ), // Saint Lucia
        new Validator("SM", 27, "SM\\d{2}[A-Z]{1}\\d{10}[A-Z0-9]{12}"   ), // San Marino
        new Validator("ST", 25, "ST\\d{23}"                             ), // Sao Tome And Principe
        new Validator("SA", 24, "SA\\d{4}[A-Z0-9]{18}"                  ), // Saudi Arabia
        new Validator("RS", 22, "RS\\d{20}"                             ), // Serbia
        new Validator("SK", 24, "SK\\d{22}"                             ), // Slovak Republic
        new Validator("SI", 19, "SI\\d{17}"                             ), // Slovenia
        new Validator("ES", 24, "ES\\d{22}"                             ), // Spain
        new Validator("SE", 24, "SE\\d{22}"                             ), // Sweden
        new Validator("CH", 21, "CH\\d{7}[A-Z0-9]{12}"                  ), // Switzerland
        new Validator("TL", 23, "TL\\d{21}"                             ), // Timor-Leste
        new Validator("TN", 24, "TN59\\d{20}"                           ), // Tunisia
        new Validator("TR", 26, "TR\\d{8}[A-Z0-9]{16}"                  ), // Turkey
        new Validator("AE", 23, "AE\\d{21}"                             ), // United Arab Emirates
        new Validator("GB", 22, "GB\\d{2}[A-Z]{4}\\d{14}"               ), // United Kingdom
        new Validator("VG", 24, "VG\\d{2}[A-Z]{4}\\d{16}"               ), // Virgin Islands, British
};

	private Iban constraint;

	@Override
	public boolean isValid(Object value, Annotation constraint) {
		if(value==null || String.valueOf(value).isEmpty())
			return true;
		this.constraint = (Iban) constraint;
		String iban = String.valueOf(value);
		Validator validator = getValidator(iban);
		if(validator==null)
			return false;
		if(validator.lengthOfIBAN!=iban.length())
			return false;
		return validator.isValidIban(iban);
		
	}

	@Override
	public String getErrorMessage() {
		return constraint.message();
	}
	
	public Validator getValidator(String code){
		for(Validator v:DEFAULT_FORMATS){
			if(v.countryCode.substring(0,2).equals(code))
				return v;
		}
		return null;
	}
	
	
	
	 public static class Validator {

	        private static final int MIN_LEN = 8;
	        private static final int MAX_LEN = 34; 
	        final String countryCode;
	        private String format;
	        final int lengthOfIBAN;
	        public Validator(String cc, int len, String format) {
	            if (!(cc.length() == 2 && Character.isUpperCase(cc.charAt(0)) && Character.isUpperCase(cc.charAt(1)))) {
	                throw new IllegalArgumentException("Invalid country Code; must be exactly 2 upper-case characters");
	            }
	            if (len > MAX_LEN || len < MIN_LEN) {
	                throw new IllegalArgumentException("Invalid length parameter, must be in range "+MIN_LEN+" to "+MAX_LEN+" inclusive: " +len);
	            }
	            if (!format.startsWith(cc)) {
	                throw new IllegalArgumentException("countryCode '"+cc+"' does not agree with format: " + format);
	            }
	            this.countryCode = cc;
	            this.lengthOfIBAN = len;
	            this.format = format;
	            
	         
	        }
	        
	        public boolean isValidIban(String iban){
	           	return Pattern.compile(format).matcher(iban).matches();
	        }
	    }
	 
	

}
