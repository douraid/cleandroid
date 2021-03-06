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
package org.cleandroid.core.di;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.cleandroid.core.application.App;
import org.cleandroid.core.application.Fragment;
import org.cleandroid.core.di.annotation.ActivityContext;
import org.cleandroid.core.di.annotation.AppContext;
import org.cleandroid.core.di.annotation.CFragment;
import org.cleandroid.core.di.annotation.Inject;
import org.cleandroid.core.di.annotation.IntentExtra;
import org.cleandroid.core.di.annotation.SystemService;
import org.cleandroid.core.sqlite.DatasourcesConfig;
import org.cleandroid.core.sqlite.orm.Persistence;
import org.cleandroid.core.sqlite.orm.annotation.PersistenceContext;
import org.cleandroid.core.util.TypeUtils;
import org.cleandroid.core.util.ViewUtils;
import org.cleandroid.core.view.annotation.InputValue;
import org.cleandroid.core.view.annotation.CView;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.Dialog;
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
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.TextServicesManager;

public class DependencyManager {
	
	private static Map<Class<?>,String> systemServicesMap = new HashMap<Class<?>,String>();
	private static Map<Method,Object[]> methodParamsMap = new HashMap<>();
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
	
	
	
	
	public static Object getDependencyFromAnnotation(Annotation annotation,Object context,Class<?> varClass){


		View contentView = null;
		Context elementContext = null;

		if(Activity.class.isAssignableFrom(context.getClass())) {
			contentView = ((Activity) context).findViewById(android.R.id.content);
			elementContext = (Context) context;
		}


		else if(Fragment.class.isAssignableFrom(context.getClass())) {
			contentView = ((Fragment) context).getView();
			elementContext = ((Fragment) context).getActivity();
		}
		else if(org.cleandroid.core.support.v4.Fragment.class.isAssignableFrom(context.getClass())){
			contentView = ((org.cleandroid.core.support.v4.Fragment) context).getView();
			elementContext = ((org.cleandroid.core.support.v4.Fragment) context).getActivity();
		}

		else if(Dialog.class.isAssignableFrom(context.getClass())) {
			contentView = ((Dialog) context).findViewById(android.R.id.content);
			elementContext = ((Dialog) context).getContext();
			if(elementContext==null)
				elementContext = ((Dialog) context).getOwnerActivity();
		}


		if(CView.class.isInstance(annotation) && contentView!=null) {
			int vid = ((CView) annotation).value();
			return contentView.findViewById(vid);
		}
		else if(CFragment.class.isInstance(annotation)){
			CFragment cfa = (CFragment) annotation;
			if(Fragment.class.isAssignableFrom(varClass)){
				if(cfa.id()!=Integer.MIN_VALUE)
					return ((Activity) context).getFragmentManager().findFragmentById(cfa.id());
				else if(!cfa.tag().isEmpty())
					return ((Activity) context).getFragmentManager().findFragmentByTag(cfa.tag());
			}
			else if(org.cleandroid.core.support.v4.Fragment.class.isAssignableFrom(varClass)
                    && FragmentActivity.class.isAssignableFrom(context.getClass())){
                if(cfa.id()!=Integer.MIN_VALUE)
                    return ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(cfa.id());
                else if(!cfa.tag().isEmpty())
                    return ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag(cfa.tag());

			}


		}
		
		else if(InputValue.class.isInstance(annotation) && contentView!=null){

			Object rawValue = ViewUtils.getViewInputValue(contentView.findViewById(((InputValue) annotation).view()));
			if(TypeUtils.isPrimitiveOrWrapper(varClass))
				return TypeUtils.getTypedValue(String.valueOf(rawValue), varClass);
			return rawValue;
		}
			
		else if(SystemService.class.isInstance(annotation) && elementContext!=null) {
			return elementContext.getSystemService(systemServicesMap.get(varClass));

		}

		else if(Inject.class.isInstance(annotation)){
			for(Annotation annotation1:varClass.getAnnotations()){
				if(App.isDependencyProviderRegistered(annotation1.annotationType())) {
					return App.getDependencyProvider(annotation1.annotationType()).handle(varClass, context);
				}
			}

		}
		
		else if(ActivityContext.class.isInstance(annotation))
			return elementContext;
		else if(AppContext.class.isInstance(annotation))
			return elementContext.getApplicationContext();
		
		else if(IntentExtra.class.isInstance(annotation)){
			if(context instanceof Activity) {
				if (((Activity) context).getIntent().getExtras() != null)
					return ((Activity) context).getIntent().getExtras().get(((IntentExtra) annotation).value());
			}
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
	
	public static Object[] getMethodDependencies(Method m, Object context,Object...extras){

		if(methodParamsMap.containsKey(m))
			return methodParamsMap.get(m);

		Class<?>[] paramsTypes = m.getParameterTypes();
		Annotation[][] paramsAnnotations = m.getParameterAnnotations();
		Object[] params = new Object[paramsTypes.length];

		for(int i=0;i<paramsTypes.length;i++){
			Class<?> cl = paramsTypes[i];
			Annotation[] annotations = paramsAnnotations[i];
			
			for(Object extra:extras){
				if(annotations.length>i){
					if(annotations[i].equals(CView.class))
						continue;
				}
				if(cl.isInstance(extra)){
					params[i] = extra;
					break;
				}
			}
			
			for(Annotation a:annotations){
				Object dep = getDependencyFromAnnotation(a, context, cl);
				if(dep!=null)
					params[i] = dep;
			}
			
			
		}
		methodParamsMap.put(m,params);
		return params;
	}
	
	
	

}
