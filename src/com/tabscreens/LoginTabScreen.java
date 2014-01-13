package com.tabscreens;


import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.assignmentexpert.ExitActivity;
import com.assignmentexpert.LoginActivity;
import com.assignmentexpert.R;
import com.crashlytics.android.Crashlytics;
import com.fragmentactivities.RegisterFragmentActivity;
import com.library.singletones.TypeFaceSingletone;


public class LoginTabScreen extends TabActivity  
{

	 TabHost tabHost;
	 TabHost.TabSpec spec; 
	 Intent intent;
	 Context context;
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        Crashlytics.start(this);
		setContentView(R.layout.root_menu);
        InputMethodManager imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	    
        tabHost = getTabHost(); 
        context = this;
        intent = new Intent().setClass(this, LoginActivity.class);
        spec = tabHost.newTabSpec("tab_1").setIndicator(getResources().getString(R.string.tab_sign_in),getResources().getDrawable(R.drawable.tab_login)).setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(spec);
        intent = new Intent().setClass(this, RegisterFragmentActivity.class);
        spec = tabHost.newTabSpec("tab_2").setIndicator(getResources().getString(R.string.tab_sign_up),getResources().getDrawable(R.drawable.tab_sign_in)).setContent(intent);
        tabHost.addTab(spec);
        this.setDefaultTab(0);
        getTabHost().setCurrentTab(0);
        spec = tabHost.newTabSpec("tab_3").setIndicator(getResources().getString(R.string.tab_close),getResources().getDrawable(R.drawable.tab_close)).setContent(new Intent().setClass(this,ExitActivity.class));
        tabHost.addTab(spec);
        
        setFontsImages();
      
        getTabHost().setOnTabChangedListener(new OnTabChangeListener() {
        public void onTabChanged(String tabId) {
        int i = getTabHost().getCurrentTab();
         
         		if (i == 0) {

                            }
               else if (i ==1) {
                 }
               else if(i==2)
               {
		            	  finish();
               }

                    }

        });
        
       
    }
    
    
    private void setFontsImages()
    {
    	 Typeface localTypeface1 = TypeFaceSingletone.getInstance().getCustomFont(context,  "Roboto-Medium.ttf");
         for (int i=0;i< tabHost.getTabWidget().getChildCount();i++)
         	{
         	
 	        	  ViewGroup vg = (ViewGroup) tabHost.getTabWidget().getChildTabViewAt(i);
 	        	  TextView tv = (TextView)vg.getChildAt(1);
 	        	  tv.setTextSize(13);
 	              tv.setTypeface(localTypeface1);
 	              tv.setPadding(0, 0, 0, 2);
 	              tv.setTextColor(Color.argb(255, 255, 255, 255));
 	              ImageView itv = (ImageView)vg.getChildAt(0);
 	              itv.setPadding(0, 4, 0, 0);
         	}
    }
    public void onBackPressed() {
    	
    	String tabTag = getTabHost().getCurrentTabTag(); 
    	Activity activity = getLocalActivityManager().getActivity(tabTag); 

        Log.d("topActivity", "CURRENT Activity ::"
                + activity.getClass().toString());
        if (activity instanceof RegisterFragmentActivity)
        {
        	Log.i("current fragment",((RegisterFragmentActivity)activity).getCurrentFragment());
        	if (((RegisterFragmentActivity)activity).getCurrentFragment().equalsIgnoreCase("com.fragments.registerfragmentcompl"))
        	{
        		((RegisterFragmentActivity)activity).returnToFirstStep();
        	}
        }

    }
    public void switchTab(int tab)
    {
    	 getTabHost().setCurrentTab(tab);
    }
    
    
}