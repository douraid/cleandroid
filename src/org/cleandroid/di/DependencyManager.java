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
package org.cleandroid.di;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.cleandroid.application.App;
import org.cleandroid.form.FormModelHandler;
import org.cleandroid.form.annotation.FormModel;
import org.cleandroid.rest.RestClientHandler;
import org.cleandroid.rest.annotation.RestClient;
import org.cleandroid.service.ServiceHandler;
import org.cleandroid.service.annotation.Service;
import org.cleandroid.service.annotation.SystemService;
import org.cleandroid.sqlite.DatasourcesConfig;
import org.cleandroid.sqlite.orm.EntityManager;
import org.cleandroid.sqlite.orm.Persistence;
import org.cleandroid.sqlite.orm.annotation.PersistenceContext;
import org.cleandroid.util.TypeUtils;
import org.cleandroid.util.ViewUtils;
import org.cleandroid.view.annotation.InputControlValue;
import org.cleandroid.view.annotation.View;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManager;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.net.ConnectivityManager;
import android.net.nsd.NsdManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.DropBoxManager;
import android.os.PowerManager;
import android.os.UserManager;
import android.os.Vibrator;
import android.os.storage.StorageManager;
import android.print.PrintManager;
import android.service.wallpaper.WallpaperService;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.TextServicesManager;

@SuppressLint("NewApi")
public class DependencyManager {
	
	private static Map<Class<?>,String> systemServicesMap = new HashMap<Class<?>,String>();
	static{
		
		systemServicesMap.put(AccessibilityManager.class, Context.ACCESSIBILITY_SERVICE);
		systemServicesMap.put(ActivityManager.class, Context.ACTIVITY_SERVICE);
		systemServicesMap.put(AlarmManager.class, Context.ALARM_SERVICE);
		systemServicesMap.put(AudioManager.class, Context.AUDIO_SERVICE);
		systemServicesMap.put(AccountManager.class, Context.ACCOUNT_SERVICE);
		

		if(Build.VERSION.SDK_INT>=11){
			systemServicesMap.put(ClipboardManager.class, Context.CLIPBOARD_SERVICE);
		}
		
		if(Build.VERSION.SDK_INT>=12){
			systemServicesMap.put(UsbManager.class, Context.USB_SERVICE);

		}
		
		if(Build.VERSION.SDK_INT>=14){
			systemServicesMap.put(TextServicesManager.class, Context.TEXT_SERVICES_MANAGER_SERVICE);
			systemServicesMap.put(WifiP2pManager.class, Context.WIFI_P2P_SERVICE);

		}
		
		if(Build.VERSION.SDK_INT>=16){
			systemServicesMap.put(InputManager.class, Context.INPUT_SERVICE);
			systemServicesMap.put(MediaRouter.class, Context.MEDIA_ROUTER_SERVICE);
			systemServicesMap.put(NsdManager.class, Context.NSD_SERVICE);

		}
		
		if(Build.VERSION.SDK_INT>=17){
			systemServicesMap.put(DisplayManager.class, Context.DISPLAY_SERVICE);
			systemServicesMap.put(UserManager.class, Context.USER_SERVICE);

		}
		

		if(Build.VERSION.SDK_INT>=18){
			systemServicesMap.put(BluetoothManager.class, Context.BLUETOOTH_SERVICE);
		}
		
		if(Build.VERSION.SDK_INT>=19){
			systemServicesMap.put(AppOpsManager.class, Context.APP_OPS_SERVICE);
			systemServicesMap.put(CaptioningManager.class, Context.CAPTIONING_SERVICE);		
			systemServicesMap.put(ConsumerIrManager.class, Context.CONSUMER_IR_SERVICE);
			systemServicesMap.put(PrintManager.class, Context.PRINT_SERVICE);

		}
		
		
		systemServicesMap.put(ConnectivityManager.class, Context.CONNECTIVITY_SERVICE);
		systemServicesMap.put(DevicePolicyManager.class, Context.DEVICE_POLICY_SERVICE);
		systemServicesMap.put(DropBoxManager.class, Context.DROPBOX_SERVICE);
		systemServicesMap.put(DownloadManager.class, Context.DOWNLOAD_SERVICE);
		systemServicesMap.put(InputMethodManager.class, Context.INPUT_METHOD_SERVICE);
		systemServicesMap.put(KeyguardManager.class, Context.KEYGUARD_SERVICE);
		systemServicesMap.put(LayoutInflater.class, Context.LAYOUT_INFLATER_SERVICE);
		systemServicesMap.put(LocationManager.class, Context.LOCATION_SERVICE);
		systemServicesMap.put(NfcManager.class, Context.NFC_SERVICE);
		systemServicesMap.put(NotificationManager.class, Context.NOTIFICATION_SERVICE);
		systemServicesMap.put(PowerManager.class, Context.POWER_SERVICE);
		systemServicesMap.put(SearchManager.class, Context.SEARCH_SERVICE);
		systemServicesMap.put(SensorManager.class, Context.SENSOR_SERVICE);
		systemServicesMap.put(StorageManager.class, Context.STORAGE_SERVICE);
		systemServicesMap.put(TelephonyManager.class, Context.TELEPHONY_SERVICE);
		systemServicesMap.put(UiModeManager.class, Context.UI_MODE_SERVICE);
		systemServicesMap.put(Vibrator.class, Context.VIBRATOR_SERVICE);
		systemServicesMap.put(WallpaperService.class, Context.WALLPAPER_SERVICE);
		systemServicesMap.put(WifiManager.class, Context.WIFI_SERVICE);
		systemServicesMap.put(WindowManager.class, Context.WINDOW_SERVICE);
		
	}
	
	
	
	
	public static Object getDependencyFromAnnotation(Annotation annotation,Activity activity,Class<?> varClass){
		

		if(View.class.isInstance(annotation))
			return activity.findViewById(((View)annotation).value());
		
		else if(InputControlValue.class.isInstance(annotation)){
			Object rawValue =  ViewUtils.getViewInputValue(activity.findViewById(((InputControlValue) annotation).view()));
			if(TypeUtils.isPrimitiveOrWrapper(varClass))
				return TypeUtils.getTypedValue(String.valueOf(rawValue), varClass);
			return rawValue;
		}
			
		else if(SystemService.class.isInstance(annotation))
			return activity.getSystemService(systemServicesMap.get(varClass));
		
		else if(Inject.class.isInstance(annotation)){
			if(varClass.isAnnotationPresent(FormModel.class)){
				if(!App.getContainer().isRegistered(FormModel.class.getName()+"."+varClass.getName()))
					App.getContainer().registerObject(FormModel.class.getName()+"."+varClass.getName(), FormModelHandler.handle(varClass,activity));
				return App.getContainer().getObject(FormModel.class.getName()+"."+varClass.getName());
			}
			
			else if(varClass.isAnnotationPresent(Service.class)){
				return ServiceHandler.handle(varClass, activity);
			}
			
			else if(varClass.isAnnotationPresent(RestClient.class))
				return RestClientHandler.handle(varClass, activity);

		}
		
		else if(Context.class.isInstance(annotation))
			return (Context) activity;
		
		else if(IntentExtra.class.isInstance(annotation)){
			if(activity.getIntent().getExtras()!=null)
				return activity.getIntent().getExtras().get(((IntentExtra)annotation).value());
			return null;
		}
		
		else if(PersistenceContext.class.isInstance(annotation)){
			DatasourcesConfig config = (DatasourcesConfig) App.getConfiguration("dbConfig");
			if(config==null)
				throw new RuntimeException("Configuration dbConfig was not found in cleandroid-config.xml");
			return Persistence.getEntityManager(((PersistenceContext) annotation).datasource());
		}
		

		
		return null;
	}
	
	public static Object[] getMethodDependencies(Method m, Activity activity,Object...extras){
		Class<?>[] paramsTypes = m.getParameterTypes();
		Annotation[][] paramsAnnotations = m.getParameterAnnotations();
		Object[] params = new Object[paramsTypes.length];

		for(int i=0;i<paramsTypes.length;i++){
			Class<?> cl = paramsTypes[i];
			Annotation[] annotations = paramsAnnotations[i];
			
			for(Object extra:extras){
				if(annotations.length>i){
					if(annotations[i].equals(View.class))
						continue;
				}
				if(cl.isInstance(extra)){
					params[i] = extra;
					break;
				}
			}
			
			for(Annotation a:annotations){
				Object dep = getDependencyFromAnnotation(a, activity, cl);
				if(dep!=null)
					params[i] = dep;
			}
			
			
		}
		
		return params;
	}
	
	
	

}
