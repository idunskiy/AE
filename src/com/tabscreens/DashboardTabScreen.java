package com.tabscreens;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.assignmentexpert.AssignmentPref;
import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.ExitActivity;
import com.assignmentexpert.FileManagerActivity;
import com.assignmentexpert.R;
import com.fragmentactivities.ProfileFragmentActivity;
import com.library.Constants;
import com.library.singletones.SharedPrefs;
/**
 *  
 * Класс табов отправки заказов, списка заказов, профиля и логаута
 *   
 */
public class DashboardTabScreen extends TabActivity
{	
	/**
	 *  
	 * BroadcastReceiver для переключения на таб списка заказов после успешного добавления нового заказа
	 *   
	 */
	TabChangeListener tabChangeListener;
	private Boolean MyListenerIsRegistered = false;
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dashboard_menu);
        tabChangeListener = new TabChangeListener();
        final TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        intent = new Intent().setClass(this, AssignmentPref.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("tab_1").setIndicator("New Order",getResources().getDrawable(R.drawable.tab_order)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, DashboardActivityAlt.class);
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("tab_2").setIndicator("Orders",getResources().getDrawable(R.drawable.tab_history)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, ProfileFragmentActivity.class);
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("tab_3").setIndicator("Profile",getResources().getDrawable(R.drawable.tab_profile)).setContent(intent);
        tabHost.addTab(spec);
        

        intent = new Intent().setClass(this, LoginTabScreen.class);
        spec = tabHost.newTabSpec("tab_4").setIndicator("Log out",getResources().getDrawable(R.drawable.tab_logout)).setContent(new Intent().setClass(this,ExitActivity.class));
        tabHost.addTab(spec);
        for (int i=0;i< tabHost.getTabWidget().getChildCount();i++)
    	{
    	
        	  ViewGroup vg = (ViewGroup) tabHost.getTabWidget().getChildTabViewAt(i);
        	  TextView tv = (TextView)vg.getChildAt(1);
              tv.setTextColor(Color.argb(255, 255, 255, 255));
    	}
  
        tabHost.setOnTabChangedListener(new OnTabChangeListener(){
            public void onTabChanged(String tabId) {
            	//String current = tabHost.getCurrentTabTag();
            	 int i = getTabHost().getCurrentTab();
            	 Log.i("choosed dashboard", Integer.toString(i));
            	if (i==3)
            	{
            		Log.i("dashboardTab screen", "it was clicked");
            		
            		AlertDialog.Builder alt_bld = new AlertDialog.Builder(DashboardTabScreen.this);
                    alt_bld.setMessage("Are you sure you want to log out?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button
                        	SharedPrefs.getInstance().writeBoolean("isChecked", false);
                        	finish();
                  		  Intent intent = new Intent(DashboardTabScreen.this, LoginTabScreen.class);
                  		  startActivity(intent);
                  		  }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button

                            dialog.cancel();
                            getTabHost().setCurrentTab(1);
                        }
                    });
                    AlertDialog alert = alt_bld.create();
                    // Title for AlertDialog
                    alert.setTitle("Log out");
                    // Icon for AlertDialog
                    alert.show();
            		  
            	}
            }
            });
        
        getTabHost().setCurrentTab(1);
        if (getIntent().getExtras()!=null)
        {
     	   if (getIntent().getExtras().getString("DashboardTabScreen").equalsIgnoreCase("FilesOrder"))
     	   {
     		  getTabHost().setCurrentTab(0);
     	   }
     	   else if (getIntent().getExtras().getString("DashboardTabScreen").equalsIgnoreCase("NewOrder"))
     	   {
     		  getTabHost().setCurrentTab(1);
     		  FileManagerActivity.getFinalAttachFiles().clear();
     	   }
     	   
        }
       
    }
	/**
	 *  
	 * Метод для переключения текущего таба.
	 *  @param tab порядковый номер таба, на который необходимо переключиться
	 */
	public void switchTab(int tab){
        getTabHost().setCurrentTab(tab);
	}
	
	/**
	 *  
	 * BroadcastReceiver для переключения на таб списка заказов после успешного добавления нового заказа
	 *   
	 */
	 protected class TabChangeListener extends BroadcastReceiver {

	        @Override
	        public void onReceive(Context context, Intent intent) {

	            if (intent.getAction().equals(Constants.TAB_CHANGE_ORDER)) 
	            {	            	  
	            	getTabHost().setCurrentTab(1);
			            if (intent.getExtras() != null)
			            {
				            if (intent.getExtras().getString("newOrder").equalsIgnoreCase("success"))
				            {
				            		Intent new_order = new Intent(Constants.NEW_ORDER);
				            		sendBroadcast(new_order);
				            }
			            }
	            }
	        }
	    }
	 
	 	/**
		 *  
		 * Используется для регистрации BroadcastReceiver смены таба
		 *   
		 */
    @Override 
    public void onResume()
    {
    	super.onResume();
    	  if (!MyListenerIsRegistered) {
	            registerReceiver(tabChangeListener, new IntentFilter(Constants.TAB_CHANGE_ORDER));
	            MyListenerIsRegistered = true;
	        }
    }
    /**
	 *  
	 * Используется для де-регистрации BroadcastReceiver смены таба
	 *   
	 */
    @Override
    protected void onPause() {
        super.onPause();

        if (MyListenerIsRegistered) {
            unregisterReceiver(tabChangeListener);
            MyListenerIsRegistered = false;
        }
    }
}
