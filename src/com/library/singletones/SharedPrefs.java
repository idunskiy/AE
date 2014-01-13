package com.library.singletones;

import java.util.Arrays;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
/**
 * singletone for global app's sharedprefs
 */
public class SharedPrefs  extends Application {
    private  SharedPreferences settings;
    private static SharedPrefs instance;
    //private final SharedPreferences.Editor editor = settings.edit();
    private Context mContext;
    static final String UNIQUE_SPLIT_VALUE = "<|>";
    
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
	  public void writeString(String key, String value ){
		     Editor e = settings.edit();
		     e.putString(key, value);
		     e.commit();
		}
    public SharedPreferences.Editor editSharePrefs() {
        return settings.edit();
    }
    public SharedPreferences getSharedPrefs()
    {
    	return settings;
    }
    String arrayListToString(List<String> list) {
        StringBuilder result = new StringBuilder();
        for (String value : list) {
            result.append(value);
            result.append(UNIQUE_SPLIT_VALUE);
        }
        return result.toString();
    }

    List<String> stringToArrayList(String prefString) {
        return Arrays.asList(prefString.split(UNIQUE_SPLIT_VALUE));
    }
}
