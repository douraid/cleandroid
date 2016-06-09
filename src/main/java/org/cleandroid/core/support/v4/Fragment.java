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
package org.cleandroid.core.support.v4;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.View;

import org.cleandroid.core.di.ComponentHandler;
import org.cleandroid.core.di.DependencyManager;
import org.cleandroid.core.event.InEventsHandler;
import org.cleandroid.core.lifecycle.annotation.OnActivityCreated;
import org.cleandroid.core.lifecycle.annotation.OnAttach;
import org.cleandroid.core.lifecycle.annotation.OnCreate;
import org.cleandroid.core.lifecycle.annotation.OnDestroy;
import org.cleandroid.core.lifecycle.annotation.OnDestroyView;
import org.cleandroid.core.lifecycle.annotation.OnDetach;
import org.cleandroid.core.lifecycle.annotation.OnResume;
import org.cleandroid.core.lifecycle.annotation.OnSaveInstanceState;
import org.cleandroid.core.lifecycle.annotation.OnStart;
import org.cleandroid.core.lifecycle.annotation.OnStop;
import org.cleandroid.core.lifecycle.annotation.OnViewCreated;
import org.cleandroid.core.lifecycle.annotation.OnViewStateRestored;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Douraid on 23/03/2016.
 */
public class Fragment extends android.support.v4.app.Fragment{
    private Set<Method> onViewCreatedCallbacks = new HashSet<Method>();
    private Set<Method> onActivityCreatedCallbacks = new HashSet<Method>();
    private Set<Method> onViewStateRestoredCallbacks = new HashSet<Method>();
    private Set<Method> onStartCallbacks = new HashSet<Method>();
    private Set<Method> onResumeCallbacks = new HashSet<Method>();
    private Set<Method> onSaveInstanceStateCallbacks = new HashSet<Method>();
    private Set<Method> onStopCallbacks = new HashSet<Method>();
    private Set<Method> onDestroyViewCallbacks = new HashSet<Method>();
    private Set<Method> onAttachCallbacks = new HashSet<Method>();
    private Set<Method> onDetachCallbacks = new HashSet<Method>();
    private Set<Method> onDestroyCallbacks = new HashSet<Method>();

    @Override
    @CallSuper
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ComponentHandler.injectDependencies(this, getActivity());
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        InEventsHandler.handle(this);
        for(Method method: getClass().getDeclaredMethods()){
            if(method.isAnnotationPresent(OnCreate.class)){
                Object[] parameters = DependencyManager.getMethodDependencies(method, getActivity(), savedInstanceState);
                try {
                    method.invoke(this, parameters);
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
            else if(method.isAnnotationPresent(OnViewCreated.class)){
                onViewCreatedCallbacks.add(method);
            }
            else if(method.isAnnotationPresent(OnActivityCreated.class)){
                onActivityCreatedCallbacks.add(method);
            }
            else if(method.isAnnotationPresent(OnViewStateRestored.class)){
                onViewStateRestoredCallbacks.add(method);
            }
            else if(method.isAnnotationPresent(OnStart.class)){
                onStartCallbacks.add(method);
            }
            else if(method.isAnnotationPresent(OnResume.class)){
                onResumeCallbacks.add(method);
            }
            else if(method.isAnnotationPresent(OnSaveInstanceState.class)){
                onSaveInstanceStateCallbacks.add(method);
            }
            else if(method.isAnnotationPresent(OnStop.class)){
                onStopCallbacks.add(method);
            }
            else if(method.isAnnotationPresent(OnDestroyView.class)){
                onDestroyViewCallbacks.add(method);
            }
            else if(method.isAnnotationPresent(OnAttach.class)){
                onAttachCallbacks.add(method);
            }
            else if(method.isAnnotationPresent(OnDetach.class)){
                onDetachCallbacks.add(method);
            }
            else if(method.isAnnotationPresent(OnDestroy.class)){
                onDestroyCallbacks.add(method);
            }


        }
    }


    @Override
    @CallSuper
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for(Method method:onViewCreatedCallbacks){
            Object[] parameters = DependencyManager.getMethodDependencies(method,getActivity(), savedInstanceState);
            try {
                method.invoke(this, parameters);
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @CallSuper
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for(Method method:onActivityCreatedCallbacks){
            Object[] parameters = DependencyManager.getMethodDependencies(method,getActivity(), savedInstanceState);
            try {
                method.invoke(this, parameters);
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @CallSuper
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        for(Method method:onViewStateRestoredCallbacks){
            Object[] parameters = DependencyManager.getMethodDependencies(method,getActivity(), savedInstanceState);
            try {
                method.invoke(this, parameters);
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        for(Method method:onResumeCallbacks){
            Object[] parameters = DependencyManager.getMethodDependencies(method,getActivity());
            try {
                method.invoke(this, parameters);
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        for(Method method:onStopCallbacks){
            Object[] parameters = DependencyManager.getMethodDependencies(method,getActivity());
            try {
                method.invoke(this, parameters);
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @CallSuper
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for(Method method:onSaveInstanceStateCallbacks){
            Object[] parameters = DependencyManager.getMethodDependencies(method,getActivity(), outState);
            try {
                method.invoke(this, parameters);
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @CallSuper
    public void onStop() {
        super.onStop();
        for(Method method:onStopCallbacks){
            Object[] parameters = DependencyManager.getMethodDependencies(method,getActivity());
            try {
                method.invoke(this, parameters);
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        super.onDestroyView();
        for(Method method:onDestroyViewCallbacks){
            Object[] parameters = DependencyManager.getMethodDependencies(method,getActivity());
            try {
                method.invoke(this, parameters);
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @CallSuper
    public void onDestroy() {
        super.onDestroy();
        for(Method method:onDestroyCallbacks){
            Object[] parameters = DependencyManager.getMethodDependencies(method,getActivity());
            try {
                method.invoke(this, parameters);
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @CallSuper
    public void onDetach() {
        super.onDetach();
        for(Method method:onDetachCallbacks){
            Object[] parameters = DependencyManager.getMethodDependencies(method,getActivity());
            try {
                method.invoke(this, parameters);
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @CallSuper
    public void onAttach(Context context) {
        super.onAttach(context);
        for(Method method:onAttachCallbacks){
            Object[] parameters = DependencyManager.getMethodDependencies(method,getActivity(), context);
            try {
                method.invoke(this, parameters);
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }

}
