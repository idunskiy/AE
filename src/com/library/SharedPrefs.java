package com.library;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPrefs  extends Application {
    private  SharedPreferences settings;
    private static SharedPrefs instance;
    //private final SharedPreferences.Editor editor = settings.edit();
    private Context mContext;
    
    
	private SharedPrefs()
	{
	}

	public static SharedPrefs getInstance(){
	    if (instance == null)
	    {
	        instance = new SharedPrefs();
	    }
	    return instance;
	}
	  public void Initialize(Context ctxt){
	       mContext = ctxt;
	       settings = PreferenceManager.getDefaultSharedPreferences(mContext);
	   }
	  
	  public void writeBoolean(String key, boolean value ){
		     Editor e = settings.edit();
		     e.putBoolean(key, value);
		     e.commit();
		}
    public SharedPreferences.Editor editSharePrefs() {
        return settings.edit();
    }
    public SharedPreferences getSharedPrefs()
    {
    	return settings;
    }
}
