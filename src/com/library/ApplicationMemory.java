package com.library;

import com.library.singletones.OrdersViewMapSingletone;
import com.library.singletones.SharedPrefs;
import com.library.singletones.TypeFaceSingletone;
import com.testflightapp.lib.TestFlight;

import android.app.Application;

/**
 *  
 * class for app's state service. Singletones and database initialization
 *   
 */
public class ApplicationMemory extends Application{
	static ApplicationMemory instance;
	   @Override
	   public void onCreate() {
	       super.onCreate();
	       instance = this;
	       TestFlight.takeOff(this, "f289d8b5-f8e6-4c26-bf12-bce0e4c73de7");
	       HelperFactory.SetHelper(getApplicationContext());
	       initSingletons();
	   }
	   @Override
	   public void onTerminate() {
	       HelperFactory.ReleaseHelper();
	       super.onTerminate();
	   }
	   public static ApplicationMemory getInstance(){
	        return instance;
	    }
	   /**
	    * Signletone initialization
	    */
	   protected void initSingletons()
	   {
	     TypeFaceSingletone.initInstance();
	     OrdersViewMapSingletone.initInstance();
	     SharedPrefs.getInstance().Initialize(getApplicationContext());
	   }
	}