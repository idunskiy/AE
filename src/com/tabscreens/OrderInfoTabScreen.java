package com.tabscreens;

import android.app.NotificationManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.ExitActivity;
import com.assignmentexpert.R;
import com.fragmentactivities.InteractionsFragmentActivity;
import com.fragmentactivities.OrderInfoFragmentActivity;
import com.library.Constants;
import com.library.singletones.TypeFaceSingletone;
/**
 *  
 * Класс табов информации о заказе и списка сообщений заказа
 *   
 */
public class OrderInfoTabScreen extends TabActivity  //implements OnTouchListener, OnTabReselectListener
{
	 TabHost tabHost;
	 TabHost.TabSpec spec; 
	 Intent intent;
	 Context context;
   public void onCreate(Bundle savedInstanceState) 
   {
	   
       super.onCreate(savedInstanceState);
       InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
       setContentView(R.layout.dashboard_menu);
      
       tabHost = getTabHost();//(TabHost)view.findViewById(android.R.id.tabhost); 
       context = this;
      
	       intent = new Intent().setClass(this, OrderInfoFragmentActivity.class);
	       spec = tabHost.newTabSpec("tab_1").setIndicator(Integer.toString(DashboardActivityAlt.listItem.getOrderid()),getResources().getDrawable(R.drawable.tab_info)).setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	       tabHost.addTab(spec);
	       intent = new Intent().setClass(this, InteractionsFragmentActivity.class);
	       spec = tabHost.newTabSpec("tab_2").setIndicator(DashboardActivityAlt.listItem.getTitle()).setContent(intent);
	       tabHost.addTab(spec);
	       setFontsImages();
	       spec = tabHost.newTabSpec("tab_3").setIndicator("Close",getResources().getDrawable(R.drawable.tab_close)).setContent(new Intent().setClass(this,ExitActivity.class));
	       tabHost.addTab(spec);
      
       if (this.getIntent().getExtras()!=null)
       {
    	   if (getIntent().getExtras().getString("OrderSwiping").equalsIgnoreCase("lr"))
  				getTabHost().setCurrentTab(1);
  			else if(getIntent().getExtras().getString("OrderSwiping").equalsIgnoreCase("rl"))
  				getTabHost().setCurrentTab(0);
    	   
       }
       if (this.getIntent().getAction()!=null)
       {
    	   if (this.getIntent().getAction().equalsIgnoreCase("notification_background") |this.getIntent().getAction().equalsIgnoreCase("notification_foreground") )
    	   {
    		   Log.i("OrderInfoTabScreen", "notification opened");
    		   getTabHost().setCurrentTab(1);
    		   NotificationManager mNotificationManager = (NotificationManager) getSystemService
    				   (Context.NOTIFICATION_SERVICE);
    		   mNotificationManager.cancel(Constants.NOTIFICATION_ID);
    		 
    	   }
       }
    		
       changeTabsListener();
       textCentering();
         
   }
   
   /**
    *  
    * Метод для изменения табов.
    *   
    */
   private void changeTabsListener()
   {
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
			            	  // 
			            	  if (OrderInfoTabScreen.this.getIntent().getAction()!=null)
			            	  {
				            	  if (OrderInfoTabScreen.this.getIntent().getAction().equalsIgnoreCase("notification_background"))
				            	  {
				            		  Intent b = new Intent(OrderInfoTabScreen.this, DashboardTabScreen.class);
				            		  b.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				            		  startActivity(b);
				            		  finish();
				            	  }	
				            	  else if(OrderInfoTabScreen.this.getIntent().getAction().equalsIgnoreCase("notification_foreground"))
				            	  {
				            		  finish();
				            	  }
			            	  }
			            	  else
			            	  finish();
			              }

	                   }
	       });
   }
   /**
    *  
    * Метод для центрирования текста в центральной вкладке списка сообщений выбранного заказа.
    *   
    */
   private void textCentering()
   {
	   final View view = tabHost.getTabWidget().getChildTabViewAt(1);
       if ( view != null ) {
         //  view.getLayoutParams().height *= 0.66;
    	   InputMethodManager imm2 = (InputMethodManager)getSystemService(
 			      Context.INPUT_METHOD_SERVICE);
    	   imm2.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
           //  get title text view
           final View textView = view.findViewById(android.R.id.title);
           if ( textView instanceof TextView ) {
               // just in case check the type

               ((TextView) textView).setGravity(Gravity.CENTER);
               ((TextView) textView).setSingleLine(false);
               textView.getLayoutParams().height = ViewGroup.LayoutParams.FILL_PARENT;
               textView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
           }
       }
       tabHost.getTabWidget().getChildAt(1).getLayoutParams().width = 100;
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
   @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	  Intent i = new Intent(getApplicationContext(),
                     DashboardTabScreen.class);
             startActivity(i);
	    }
	    return super.onKeyDown(keyCode, event);
	}

}