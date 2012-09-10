package com.assignmentexpert;

import com.library.HelperFactory;

import android.app.Application;

public class ApplicationMemory extends Application{

	   @Override
	   public void onCreate() {
	       super.onCreate();
	       HelperFactory.SetHelper(getApplicationContext());
	   }
	   @Override
	   public void onTerminate() {
	       HelperFactory.ReleaseHelper();
	       super.onTerminate();
	   }
	}