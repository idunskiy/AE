package com.tabscreens;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.assignmentexpert.AssignmentPref;
import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.ExitActivity;
import com.assignmentexpert.R;
import com.fragmentactivities.ProfileFragmentActivity;

public class DashboardTabScreen extends TabActivity
{
    private SharedPreferences sharedPreferences;
	private Editor editor;
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dashboard_menu);

        final TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        intent = new Intent().setClass(this, AssignmentPref.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("tab_1").setIndicator("New Order",getResources().getDrawable(R.drawable.order)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, DashboardActivityAlt.class);
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("tab_2").setIndicator("Orders",getResources().getDrawable(R.drawable.history)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, ProfileFragmentActivity.class);
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("tab_3").setIndicator("Profile",getResources().getDrawable(R.drawable.profile)).setContent(intent);
        tabHost.addTab(spec);
        

        intent = new Intent().setClass(this, LoginTabScreen.class);
        spec = tabHost.newTabSpec("tab_4").setIndicator("Log out",getResources().getDrawable(R.drawable.logout)).setContent(new Intent().setClass(this,ExitActivity.class));
        tabHost.addTab(spec);
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE );
        editor = sharedPreferences.edit();
        
        tabHost.setOnTabChangedListener(new OnTabChangeListener(){
            public void onTabChanged(String tabId) {
            	//String current = tabHost.getCurrentTabTag();
            	 int i = getTabHost().getCurrentTab();
            	 Log.i("choosed dashboard", Integer.toString(i));
            	if (i==3)
            	{
            		Log.i("dashboardTab screen", "it was clicked");
            		editor.remove("isChecked").commit();
            		  finish();
            		  Intent intent = new Intent(DashboardTabScreen.this, LoginTabScreen.class);
            		  startActivity(intent);
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
     	   
        }
       
    }
    @Override 
    public void onResume()
    {
    	super.onResume();
//    	 getTabHost().setCurrentTab(1);
    }
}
