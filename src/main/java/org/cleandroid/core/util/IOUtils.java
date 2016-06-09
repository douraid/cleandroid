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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

public class IOUtils {
	
	public static String readStreamAsString(InputStream is) throws IOException{
		StringBuilder sb = new StringBuilder();
		InputStreamReader isReader = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isReader);
		String line = null;
		while((line=reader.readLine())!=null)
			sb.append(line).append('\n');
		return StringUtils.rtrim(sb.toString(),"\n");
	}
	
	public static File readStreamAsFile(InputStream is, String filePath){
		File f = new File(filePath);
		FileOutputStream fos = null;
		try{
			fos = new FileOutputStream(f);
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = is.read(bytes)) != -1) {
				fos.write(bytes, 0, read);
			}

		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
		finally{
			try{
			if(is!=null)
				is.close();
			}
			catch(Exception e){
				Log.e("Cleandroid",e.getMessage());
			}
			try{
				if(fos!=null)
					fos.close();
				}
				catch(Exception e){
					Log.e("Cleandroid",e.getMessage());
				}
			
		}
		return f;
		
	}

}
