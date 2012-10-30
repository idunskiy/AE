package com.assignmentexpert;

import android.app.Application;
import android.content.Context;

import com.library.HelperFactory;

public class ApplicationMemory extends Application{
	static ApplicationMemory instance;
	   @Override
	   public void onCreate() {
	       super.onCreate();
	       instance = this;
	       HelperFactory.SetHelper(getApplicationContext());
	   }
	   @Override
	   public void onTerminate() {
	       HelperFactory.ReleaseHelper();
	       super.onTerminate();
	   }
	   public static ApplicationMemory getInstance(){
	        return instance;
	    }
	}