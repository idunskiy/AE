package com.library;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class HelperFactory {
	 private static DatabaseHandler databaseHelper;
	   
	   public static DatabaseHandler GetHelper(){
	       return databaseHelper;
	   }
	 
	public static void SetHelper(Context context){
	       databaseHelper = OpenHelperManager.getHelper(context,DatabaseHandler.class);
	   }
	 
	public static void ReleaseHelper(){
	       OpenHelperManager.releaseHelper();
	       databaseHelper = null;
	   }
}
