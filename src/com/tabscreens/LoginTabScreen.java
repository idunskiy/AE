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

import com.activitygroups.TabGroup1Activity;
import com.activitygroups.TabGroup2Activity;
import com.activitygroups.TabsManager;
import com.assignmentexpert.ExitActivity;
import com.assignmentexpert.R;
import com.assignmentexpert.R.drawable;
import com.assignmentexpert.R.layout;


public class LoginTabScreen extends TabActivity  //implements OnTouchListener, OnTabReselectListener
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
        intent = new Intent().setClass(this, TabGroup1Activity.class);
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("tab_1").setIndicator("Log in",getResources().getDrawable(R.drawable.login)).setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(spec);
        intent = new Intent().setClass(this, TabGroup2Activity.class);
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
            	   moveTaskToBack(true);            	   
               }

                    }



        });
        
        
//        getLocalActivityManager();
//        getTabHost().getTabWidget().getChildAt(1).setOnTouchListener(new OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_DOWN) {
//                	Log.i("onTouch","I was touched");
//                  getResources().getDrawable(R.drawable.login).setAlpha(80);
//                  getResources().getDrawable(R.drawable.signup).setAlpha(255);
//                  getResources().getDrawable(R.drawable.close).setAlpha(80);
//                }
//                else if (event.getAction() == MotionEvent.ACTION_UP) {
//                	
//                }
//				return false;
//            }
//            });
       
    }
    @Override 
    public void onResume()
    {
    	super.onResume();
    	 getTabHost().setCurrentTab(0);
    	
    	 InputMethodManager imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//    	Log.i("LoginabScreen","onResume method");
//    	getTabHost().setOnTabChangedListener(new OnTabChangeListener() {
//    		
//    		           	public void onTabChanged(String tabId) {
//    		           		Activity currentActivity = getCurrentActivity();
//    		                Log.i("choosed activity", currentActivity.toString());
//    		                if (currentActivity instanceof LoginActivity) {
//    		                	Log.i("current activity", "LoginActivity");
//    		                	getResources().getDrawable(R.drawable.login).setAlpha(255);
//    		                	getResources().getDrawable(R.drawable.signup).setAlpha(80);
//    		                	getResources().getDrawable(R.drawable.close).setAlpha(80);
//    		                }
//    		                if (currentActivity instanceof RegisterActivity) { 
//    		                	Log.i("current activity", "RegisterActivity");
//    		                	getResources().getDrawable(R.drawable.login).setAlpha(80);
//    		                	getResources().getDrawable(R.drawable.signup).setAlpha(255);
//    		                	getResources().getDrawable(R.drawable.close).setAlpha(80);
//    		                }
//
//    		
//    		           	  }
//    		           	});
    		

    }
    
//    private StateListDrawable getDrawable(int id)
//    {
//    	  StateListDrawable content = new StateListDrawable();
//          Drawable contentSelected = this.getResources().getDrawable(
//          R.drawable.login);
//          contentSelected.mutate().setAlpha(80);
//
//          Drawable contentNormal = this.getResources().getDrawable(id);
//
//          content.addState(new int[] { -android.R.attr.state_pressed }, contentSelected);
//          content.addState(new int[] { -android.R.attr.state_enabled }, contentNormal);
//    	return content;
//    }
//    private void iconsHighlight(int i)
//    {
//    		ViewGroup vg = (ViewGroup) getTabHost().getTabWidget().getChildTabViewAt(i);
//    	  ImageView tv = (ImageView)vg.getChildAt(0);
//    	  for (int a=0;a< getTabHost().getTabWidget().getChildCount();a++)
//  	      {
//	    	  if(a==i)
//	    	  {
//	    		  tv.getDrawable().setAlpha(255);
//	    	  }
//	    	  else
//	    		  tv.getDrawable().setAlpha(80);
//  	      }
//    }
  
//    private void setupTab(final View view, final String tag, Intent intent,  int  drawableId) {
//    	    View tabview = createTabView(tabHost.getContext(), tag);
//    	        TabSpec setContent = tabHost.newTabSpec(tag).setIndicator(tabview).setContent(new TabContentFactory() {
//    	        public View createTabContent(String tag) {return view;}
//    	    });
//    	        ImageView icon = (ImageView) tabview.findViewById(R.id.icon);
//    	        icon.setImageDrawable(getResources().getDrawable(drawableId));
////    	        Bitmap btmicon  = BitmapFactory.decodeResource(context.getResources(),
////                        drawableId);
////    	        icon.setImageBitmap(btmicon);
//    	    	setContent.setContent(intent);
////    	        spec.setIndicator(tabview).setContent(intent);
////    	        spec.setContent(intent);
//    	        tabHost.addTab(setContent);
//    	}
//    	 
//    	private static View createTabView(final Context context, final String text) {
//    	    View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
//    	    TextView tv = (TextView) view.findViewById(R.id.tabTextView);
//    	    tv.setText(text);
////    	    tv.setPadding(0, 0, 0, 2);
//            ImageView itv =  (ImageView) view.findViewById(R.id.icon);
//            itv.setPadding(0, 10, 0, 0);
//    	    return view;
//    	}
    	
//    	public boolean onTouch(View v, MotionEvent event) {
//
//    		boolean consumed = false;
//    	    // use getTabHost().getCurrentTabView to decide if the current tab is
//    	    // touched again
//    	    if (event.getAction() == MotionEvent.ACTION_DOWN
//    	            && v.equals(getTabHost().getCurrentTabView())) {
//    	        // use getTabHost().getCurrentView() to get a handle to the view
//    	        // which is displayed in the tab - and to get this views context
//    	        View currentView = getTabHost().getCurrentView();
//    	        Context currentViewContext = currentView.getContext();
//    	        Log.i("current activity",currentViewContext.toString());
//    	        if (currentViewContext instanceof OnTabReselectListener) {
//    	        	Log.i("current activity",currentViewContext.toString());
//    	            OnTabReselectListener listener = (OnTabReselectListener) currentViewContext;
//    	            listener.onTabReselect();
//    	            consumed = true;
//    	        }
//    	    }
//    	    return consumed;
//    	}
//		public void onTabReselect() {
//			 
//			
//			
//		}
}