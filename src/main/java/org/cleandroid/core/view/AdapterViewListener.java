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
package org.cleandroid.core.view;

import android.view.View;

public interface AdapterViewListener<T> {
	
	void onCreateView(View view, T item);
	void onItemSelected(T item);
	void onNothingSelected();
	void onItemClicked(T item);
	boolean onItemLongClicked(T item);

}
