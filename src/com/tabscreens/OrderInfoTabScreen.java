package com.tabscreens;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.activitygroups.TabsManager;
import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.ExitActivity;
import com.assignmentexpert.R;
import com.fragmentactivities.InteractionsFragmentActivity;
import com.fragmentactivities.OrderInfoFragmentActivity;

public class OrderInfoTabScreen extends TabActivity  //implements OnTouchListener, OnTabReselectListener
{
	 TabHost tabHost;
	 TabHost.TabSpec spec; 
	 Intent intent;
	 Context context;
	 TabsManager mTabManager;
	private View view;
	 public static String registerCompl = "";
	 boolean flag=  false;
   public void onCreate(Bundle savedInstanceState) 
   {
	   
       super.onCreate(savedInstanceState);
       setContentView(R.layout.dashboard_menu);
       InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
       view = getLayoutInflater().inflate(R.layout.root_menu, null);
       tabHost = getTabHost();//(TabHost)view.findViewById(android.R.id.tabhost); 
       context = this;
       
       Log.i("info tabHost count", Integer.toString(tabHost.getChildCount()));
	       intent = new Intent().setClass(this, OrderInfoFragmentActivity.class);
	       spec = tabHost.newTabSpec("tab_1").setIndicator(Integer.toString(DashboardActivityAlt.listItem.getOrderid()),getResources().getDrawable(R.drawable.info)).setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	       tabHost.addTab(spec);
	       intent = new Intent().setClass(this, InteractionsFragmentActivity.class);
	       spec = tabHost.newTabSpec("tab_2").setIndicator(DashboardActivityAlt.listItem.getTitle()).setContent(intent);
	       tabHost.addTab(spec);
	
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
       if (this.getIntent().getExtras()!=null)
       {
    	   
    	   if (getIntent().getExtras().getString("OrderSwiping").equalsIgnoreCase("lr"))
  				getTabHost().setCurrentTab(1);
  			else if(getIntent().getExtras().getString("OrderSwiping").equalsIgnoreCase("rl"))
  				getTabHost().setCurrentTab(0);
    	   
       }
       getTabHost().setOnTabChangedListener(new OnTabChangeListener() {
       public void onTabChanged(String tabId) {

       int i = getTabHost().getCurrentTab();
        
		        		if (i == 0) {
		
		                           }
		              else if (i ==1) 
		                {
		            	  
		                }
		              else if(i==2)
		              {
		            	  finish();
		              }

                   }



       });
       

       
           final View view = tabHost.getTabWidget().getChildTabViewAt(1);
           if ( view != null ) {
             //  view.getLayoutParams().height *= 0.66;

               //  get title text view
               final View textView = view.findViewById(android.R.id.title);
               if ( textView instanceof TextView ) {
                   // just in case check the type

                   // center text
                   ((TextView) textView).setGravity(Gravity.CENTER);
                   // wrap text
                   ((TextView) textView).setSingleLine(false);

                   // explicitly set layout parameters
                   textView.getLayoutParams().height = ViewGroup.LayoutParams.FILL_PARENT;
                   textView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
               }
           }
       
   }
   
//   private void setIndecator(TabHost.TabSpec tabSpec, String label) {
//	    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	    LinearLayout tabView = (LinearLayout) vi.inflate(R.layout.tab_view, null);
//	    ((TextView)tabView.findViewById(R.id.tabCaption)).setText(label);
//	    try {
//	        Method m = tabSpec.getClass().getMethod("setIndicator", View.class);
//	        m.invoke(tabSpec, tabView);
//	    } catch (Exception e) {
//	        //in case if platform 1.5 or via other problems indicator cannot be set as view
//	        //we have to set as just simple label.
//	        tabSpec.setIndicator(label, getResources().getDrawable(R.layout.tab_selector));
//	    }
//	}
   
   
   public void replaceContentView(String id, Intent newIntent) {
	    View view = ((ActivityGroup) context)
	            .getLocalActivityManager()
	            .startActivity(id,
	                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
	            .getDecorView();
	    ((Activity) context).setContentView(view);

	}
   @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	  Intent i = new Intent(getApplicationContext(),
                     DashboardTabScreen.class);
             startActivity(i);
	    }
	    return super.onKeyDown(keyCode, event);
	}

   @Override 
   public void onResume()
   {
   		super.onResume();
//   		if (getIntent().getExtras()!=null)
//   		{
//   			
//   		}	
   	 
   }
}