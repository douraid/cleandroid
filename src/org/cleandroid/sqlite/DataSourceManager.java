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
package org.cleandroid.sqlite;


import java.io.File;

import org.cleandroid.application.App;

import android.database.sqlite.SQLiteDatabase;

public class DataSourceManager {
	
	
	
	public static SQLiteDatabase openOrCreateDataSource(String dataSourceName){
		String filesDirPath = App.getAppContext().getFilesDir().getAbsolutePath();
		return SQLiteDatabase.openOrCreateDatabase(filesDirPath+ File.separator + "cleandroid"+File.separator+"databases"+File.separator+dataSourceName+".db",null);
	}

	

}
