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
import com.fragmentactivities.RegisterFragmentActivity;
import com.library.singletones.TypeFaceSingletone;

/**
 *  
 * Класс табов логина, регистрации
 *   
 */

public class LoginTabScreen extends TabActivity  
{

	/** Обьект TabHost */
	 TabHost tabHost;
	 TabHost.TabSpec spec; 
	 /** Обьект Intent для  использования активнотей в табах*/
	 Intent intent;
	 Context context;
	 /** Инициализация и добавление используемых активностей в табы */
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.root_menu);
        InputMethodManager imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	    
        tabHost = getTabHost(); 
        context = this;
        //
        intent = new Intent().setClass(this, LoginActivity.class);
        // Инициализация TabSpec для каждого таба TabHost. Добавление класса LoginActivity
        spec = tabHost.newTabSpec("tab_1").setIndicator("Sign in",getResources().getDrawable(R.drawable.tab_login)).setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(spec);
        intent = new Intent().setClass(this, RegisterFragmentActivity.class);
     // Инициализация TabSpec для каждого таба TabHost. Добавление класса RegisterFragmentActivity(используется FragmentActivity, так как используются вложенные фрагменты - регистрация проходит в 2 этапа)
        spec = tabHost.newTabSpec("tab_2").setIndicator("Sign up",getResources().getDrawable(R.drawable.tab_sign_in)).setContent(intent);
        tabHost.addTab(spec);
        this.setDefaultTab(0);
        getTabHost().setCurrentTab(0);
        spec = tabHost.newTabSpec("tab_3").setIndicator("Close",getResources().getDrawable(R.drawable.tab_close)).setContent(new Intent().setClass(this,ExitActivity.class));
        tabHost.addTab(spec);
        
        setFontsImages();
      
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
		            	   Log.i("@@@@@@@@@@ Inside onClick tab 1", "finish");
		            	  finish();
               }

                    }

        });
        
       
    }
    /**
     *  
     * Метод для установки Roboto шрифта и позиционирования иконок в табах
     *   
     */
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
    
}