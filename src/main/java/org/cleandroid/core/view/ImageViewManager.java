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

import android.Manifest;
import android.graphics.BitmapFactory;
import android.support.annotation.RequiresPermission;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Douraid on 08/03/2016.
 */
public class ImageViewManager {

    @RequiresPermission(Manifest.permission.INTERNET)
    public static void loadFromUrl(final ImageView imageView, final String url){
        org.cleandroid.core.http.SimpleHttpClient.GET(url)
                        .process(new org.cleandroid.core.http.OnHttpResponseListener() {
                            @Override
                            public void onSuccess(int statusCode, org.cleandroid.core.http.HttpResponse response) {
                                imageView.setImageBitmap(BitmapFactory.decodeStream(response.getResponseBodyAsInputStream()));
                            }

                            @Override
                            public void onFail(int statusCode, String message, org.cleandroid.core.http.HttpResponse response) {
                                Log.e("Cleandroid","Failed to load bitmap into ImageView " + imageView.getId() + " from the following URL: " + url + " ::: HTTP " + statusCode + "/" + message);
                            }

                            @Override
                            public void onException(String message, Throwable exception) {
                                Log.e("Cleandroid","Failed to load bitmap into ImageView " + imageView.getId() + " from the following URL: " + url + " ::: Exception: " + message);
                            }
                        });
    }
}
