package com.tabscreens;


import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.activitygroups.TabsManager;
import com.assignmentexpert.ExitActivity;
import com.assignmentexpert.LoginActivity;
import com.assignmentexpert.R;
import com.fragmentactivities.RegisterFragmentActivity;


public class LoginTabScreen extends TabActivity  
{
	 TabHost tabHost;
	 TabHost.TabSpec spec; 
	 Intent intent;
	 Context context;
	 TabsManager mTabManager;
	private View view;
	public static Context loginTabContext;
	 public static String registerCompl = "";
	 boolean flag=  false;
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.root_menu);
        InputMethodManager imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	    
        view = getLayoutInflater().inflate(R.layout.root_menu, null);
        tabHost = getTabHost();//(TabHost)view.findViewById(android.R.id.tabhost); 
        context = this;
        intent = new Intent().setClass(this, LoginActivity.class);
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("tab_1").setIndicator("Log in",getResources().getDrawable(R.drawable.login)).setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(spec);
        intent = new Intent().setClass(this, RegisterFragmentActivity.class);
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("tab_2").setIndicator("Sign up",getResources().getDrawable(R.drawable.signup)).setContent(intent);
        tabHost.addTab(spec);
        loginTabContext = this;
        this.setDefaultTab(0);
        getTabHost().setCurrentTab(0);
        spec = tabHost.newTabSpec("tab_3").setIndicator("Close",getResources().getDrawable(R.drawable.close)).setContent(new Intent().setClass(this,ExitActivity.class));
        tabHost.addTab(spec);
        Typeface localTypeface1 = Typeface.createFromAsset(getAssets(),
                "Roboto-Medium.ttf");
        for (int i=0;i< tabHost.getTabWidget().getChildCount();i++)
        	{
        	
	        	  ViewGroup vg = (ViewGroup) tabHost.getTabWidget().getChildTabViewAt(i);
	        	  TextView tv = (TextView)vg.getChildAt(1);
	        	  tv.setTextSize(13);
	              tv.setTypeface(localTypeface1);
	              tv.setPadding(0, 0, 0, 2);
	              ImageView itv = (ImageView)vg.getChildAt(0);
	              itv.setPadding(0, 4, 0, 0);
        	}
      
        getTabHost().setOnTabChangedListener(new OnTabChangeListener() {
        public void onTabChanged(String tabId) {
        int i = getTabHost().getCurrentTab();
         
         		if (i == 0) {
                            Log.i("@@@@@@@@@@ Inside onClick tab 0", "onClick tab");

                            }
               else if (i ==1) {
                            Log.i("@@@@@@@@@@ Inside onClick tab 1", "onClick tab");
                 }
               else if(i==2)
               {
            	   System.exit(0);
            	   
               }

                    }

        });
        
       
    }
    
}