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
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.activitygroups.InteractionTabGroup;
import com.activitygroups.OrderInfoTabGroup;
import com.activitygroups.TabsManager;
import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.ExitActivity;
import com.assignmentexpert.R;

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
       intent = new Intent().setClass(this, OrderInfoTabGroup.class);
       // Initialize a TabSpec for each tab and add it to the TabHost
       spec = tabHost.newTabSpec("tab_1").setIndicator(Integer.toString(DashboardActivityAlt.listItem.getOrderid()),getResources().getDrawable(R.drawable.info)).setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
       tabHost.addTab(spec);
       intent = new Intent().setClass(this, InteractionTabGroup.class);
       // Initialize a TabSpec for each tab and add it to the TabHost
       spec = tabHost.newTabSpec("tab_2").setIndicator(DashboardActivityAlt.listItem.getTitle()).setContent(intent);
       tabHost.addTab(spec);

//       intent = new Intent().setClass(this, TabGroup2Activity.class);;
       // Initialize a TabSpec for each tab and add it to the TabHost
       spec = tabHost.newTabSpec("tab_3").setIndicator("Close",getResources().getDrawable(R.drawable.close)).setContent(new Intent().setClass(this,ExitActivity.class));
       tabHost.addTab(spec);
//       mTabManager.addTab(tabHost.newTabSpec("tab_3").setIndicator("Close",getResources().getDrawable(R.drawable.close)),
//       		LoginActivity.class, null);
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
    	   if (this.getIntent().getExtras().getString("OrderInfoTabGroup").equals("FilesMessages"))
    	   {
	            //new InteractionTabGroup().replaceContentView(); 
    	   }
    	   
       }
       getTabHost().setOnTabChangedListener(new OnTabChangeListener() {
       public void onTabChanged(String tabId) {

       int i = getTabHost().getCurrentTab();
        
        		if (i == 0) {
//        			if (DashboardActivityAlt.listItem.getProduct().getProductType().equalsIgnoreCase("assignment"))
//        	            {
//        	
//        	            Intent frequentMessages = new Intent(getParent(), OrderInfoActivityAA.class);
//        	             MainTabGroup parentActivity = (MainTabGroup)getParent();
//        	             parentActivity.startChildActivity("FrequentMessageActivity", frequentMessages);
//        	            }
//        	     	    else if (DashboardActivityAlt.listItem.getProduct().getProductType().equalsIgnoreCase("writing"))
//        	     	    {
//        	
//        	               Intent frequentMessages = new Intent(getParent(), OrderInfoActivityEW.class);
//        		             MainTabGroup parentActivity = (MainTabGroup)getParent();
//        		             parentActivity.startChildActivity("FrequentMessageActivity", frequentMessages);
//        	            }

                           }
              else if (i ==1) {
                           Log.i("@@@@@@@@@@ Inside onClick tab 1", "onClick tab");
                }
              else if(i==2)
              {
            	  finish();
        		  Intent intent = new Intent(OrderInfoTabScreen.this, DashboardTabScreen.class);
        		  startActivity(intent);   	   
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
   public void onResume()
   {
   	super.onResume();
   	 getTabHost().setCurrentTab(1);
   }
}