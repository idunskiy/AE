package com.assignmentexpert;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.DashboardAsync;
import com.asynctasks.InactivateAsync;
import com.customitems.CustomTextView;
import com.datamodel.Messages;
import com.datamodel.Order;
import com.library.DatabaseHandler;
import com.library.FrequentlyUsedMethods;
import com.library.OrderAdapter;
import com.library.ServiceIntentMessages;
import com.library.UserFunctions;
import com.tabscreens.OrderInfoTabScreen;

 
 public class DashboardActivityAlt extends FragmentActivity implements ITaskLoaderListener{
	 public static enum Action {
	        LR, // Left to Right
	        RL, // Right to Left
	        TB, // Top to bottom
	        BT, // Bottom to Top
	        None // when no action was detected
	    }
	 UserFunctions func = new UserFunctions();
	public static List<Order> orders;
	public static ArrayList<Order> forPrint = new ArrayList<Order>();
	public static  List<Order> ordersExport;
	private ListView listView1;
	private Button btnClose;
	private Button btnNewOrder;
	private GestureDetector gestureDetector;
	private boolean mIsScrolling;
	private GestureDetector gestureScanner;
	View.OnTouchListener gestureListener;
	static Context ctx;
	public static int page = 1;
    public static int perpage;
    public static boolean stopDownload = false;
    public static List<Messages> messagesExport;
	static private ProgressDialog pd;
	DatabaseHandler db;
	static public Order listItem;
	private Intent intent;
	boolean isEmpty = false;
	LinearLayout dashboardHeader;
	int layoutHeight;
	
	 private Action mSwipeDetected = Action.None;

	    public boolean swipeDetected() {
	        return mSwipeDetected != Action.None;
	    }
	    public Action getAction() {
	        return mSwipeDetected;
	    }
	    public void setAction(Action action) {
	    	mSwipeDetected = action;
	    }
	    
	    private static final int MIN_DISTANCE = 100;
		
	    private float downX, downY, upX, upY ,  stopX, stopY;
	    OrderAdapter adapter;
		private SharedPreferences sharedPreferences;
		private Editor editor;
		private View btnProfile;
		FrequentlyUsedMethods faq;
	    private static final String logTag = "SwipeDetector";
	    
	    private int savedPosition;
	    private int savedListTop;
	    Parcelable state;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dash_alt);
		
	    dashboardHeader = (LinearLayout) findViewById(R.id.dashboardHeader);
	    faq = new FrequentlyUsedMethods(DashboardActivityAlt.this);
	    dashboardHeader.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	        public void onGlobalLayout() {
	        	layoutHeight = dashboardHeader.getMeasuredHeight();
	        }
	    });
	    final Activity act = DashboardActivityAlt.this;
	    intent = new Intent(DashboardActivityAlt.this, ServiceIntentMessages.class);
		listView1 = (ListView)findViewById(R.id.altOrderslist);
		
		listView1.setCacheColorHint(Color.TRANSPARENT);
		listView1.setCacheColorHint(Color.BLACK);
		listView1.setScrollingCacheEnabled(false); 
		final FrequentlyUsedMethods met = new FrequentlyUsedMethods(DashboardActivityAlt.this);
		
		final float HORIZONTAL_MIN_DISTANCE =met.convertPixelsToDp(30,act);
		final float VERTICAL_MIN_DISTANCE = met.convertPixelsToDp(100,act);
		ctx = this;
	    DisplayMetrics displaymetrics = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	    int height = (int)dpFromPx(displaymetrics.heightPixels);
	    Log.i("height", Integer.toString(height));
	    height = height - (int)dpFromPx(90);
	    final int count =(int)dpFromPx(height)/(int)dpFromPx(50);
	    adapter = new OrderAdapter(DashboardActivityAlt.this,
    			R.layout.dash_alt_item, forPrint);
	    sharedPreferences = getSharedPreferences("user", MODE_PRIVATE );
		editor = sharedPreferences.edit();
	    Log.i("dp height", Integer.toString(height));
	    Log.i("count", Integer.toString(count));
	    
	    perpage = count;

	    
        Bundle bundle = getIntent().getExtras();
		if (getIntent().getStringExtra("NewOrder") != null)
		{
			String value = bundle.getString("NewOrder");
			if (value.equals("wasAdded"))
			{
				ArrayList<java.io.File> files = new ArrayList<java.io.File>();
				FileManagerActivity.setOrderAttachFiles(files);
			}
		}

		//listView1.addView( findViewById(R.id.empty_list_view1 ), new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		if (forPrint.isEmpty() & !stopDownload )
		{
			
			DashboardActivityAlt.page = 1;
			DashboardAsync.execute(DashboardActivityAlt.this, DashboardActivityAlt.this);
			adapter.notifyDataSetChanged();
//				this.pd = ProgressDialog.show(this, "Please wait..", "Downloading Data..."
//					, true, false); 
//				new DownloadTask().execute(Integer.toString(DashboardActivityAlt.page), Integer.toString(DashboardActivityAlt.perpage));
			LoginActivity.newUser = false;
			Log.i("download page count dashboard",Integer.toString(DashboardActivityAlt.page));
			
		}
		else if (LoginActivity.newUser)
		{
			Log.i("new user","dashboard");
			Log.i("if new user in new user", Boolean.toString(LoginActivity.newUser));
			forPrint.clear();
			stopDownload = false;
			DashboardActivityAlt.page = 1;
//			this.pd = ProgressDialog.show(this, "Please wait..", "Downloading Data..."
//					, true, false); 
//			new DownloadTask().execute(Integer.toString(DashboardActivityAlt.page), Integer.toString(DashboardActivityAlt.perpage));
			DashboardAsync.execute(DashboardActivityAlt.this, DashboardActivityAlt.this);
			adapter.notifyDataSetChanged();
			Log.i("new page count dashboard",Integer.toString(DashboardActivityAlt.page));
			LoginActivity.newUser = false;
		}
		else
		{
			Log.i("old user","dashboard");
			adapter = new OrderAdapter(DashboardActivityAlt.this,
        			R.layout.dash_alt_item, forPrint);
			listView1.setAdapter(adapter);
            adapter.notifyDataSetChanged();
      
            if (sharedPreferences != null)
            {
            	savedPosition = sharedPreferences.getInt("savedPosition", 0);
            	savedListTop=  sharedPreferences.getInt("savedListTop", 0 );
            }
            if (savedPosition >= 0) {
  		      listView1.setSelectionFromTop(savedPosition, savedListTop);
  		    }
		}
		
		gestureDetector = new GestureDetector(new MyGestureListener());
//	    View.OnTouchListener gestureListener = new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                return gestureDetector.onTouchEvent(event); 
//            }};
        gestureListener = new View.OnTouchListener() {
	            public boolean onTouch(View v, MotionEvent event) {
	            	if (gestureDetector.onTouchEvent(event)){
	            	switch (event.getAction()) {
	                case MotionEvent.ACTION_DOWN: {
	                    downX = event.getX();
	                    downY = event.getY();
	                    mSwipeDetected = Action.None;
	                    return false; // allow other events like Click to be processed
	                }
	                case MotionEvent.ACTION_MOVE: {
	                    upX = event.getX();
	                    upY = event.getY();
	                    float deltaX = downX - upX;
	                    float deltaY = downY - upY;
	                            if (Math.abs(deltaX) > HORIZONTAL_MIN_DISTANCE) {
	                                // left or right
	                                if (deltaX < 0) {
	                                    Log.i(logTag, "Swipe Left to Right");
	                                    mSwipeDetected = Action.LR;
	                                    return true;
	                                }
	                                if (deltaX > 0) {
	                                    Log.i(logTag, "Swipe Right to Left");
	                                    mSwipeDetected = Action.RL;
	                                    return true;
	                                }
	                            } else 

	                            // vertical swipe detection
	                            if (Math.abs(deltaY) > VERTICAL_MIN_DISTANCE) {
	                                // top or down
	                                if (deltaY < 0) {
	                                    Log.i(logTag, "Swipe Top to Bottom");
	                                    mSwipeDetected = Action.TB;
	                                    return false;
	                                }
	                                if (deltaY > 0) {
	                                    Log.i(logTag, "Swipe Bottom to Top");
	                                    mSwipeDetected = Action.BT;
	                                    return false;
	                                }
	                            } 
	                            return true;
	                	}
	                  }
	            	}
				return false;
	        }
        };
		listView1.setOnItemClickListener(new OnItemClickListener() {
	          public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	          {
	                  if (getAction() == getAction().LR | getAction() == getAction().RL)
	                  {
	                	 
	                	  
	                	  
	                     view.setBackgroundColor(Color.rgb(66, 174, 255));
	                     
	                      listItem = (Order) listView1.getItemAtPosition(position);
	                      ((CustomTextView)view.findViewById(R.id.altOrderTitle)).setTextSize(28);
	                      ((CustomTextView)view.findViewById(R.id.altOrderTitle)).setTextColor(Color.WHITE);
	                      ((CustomTextView)view.findViewById(R.id.altOrderTitle)).setText("Opening...");
	                      
	                     try {
	                    	 messagesExport = listItem.getCusThread().getMessages();
	                 	  }
	                 	  catch (NullPointerException npe) {
	                 		 Toast toast = Toast.makeText(DashboardActivityAlt.this, 
	                    			 "You should to swipe the item properly", Toast.LENGTH_SHORT);
	                    	 toast.show();
	                    	 Intent intent = getIntent();
	                    	 finish();
	                    	 startActivity(intent);
	                 	  }
	                     
	                     Intent i = new Intent(getApplicationContext(),
	                            OrderInfoTabScreen.class);
	                     startActivity(i);
	                     
	                    
	                  } 
	             
	                  else if(!swipeDetected())
	                  {
	                	  Log.i("stop value", "it was stoped");
	                     //view.setBackgroundColor(Color.WHITE);
	                  }
	                  else 
	                  {
	                	 
	                  }
	                  listItem = (Order) listView1.getItemAtPosition(position);
                      ((CustomTextView)view.findViewById(R.id.altOrderTitle)).setTextSize(28);
                      ((CustomTextView)view.findViewById(R.id.altOrderTitle)).setTextColor(Color.WHITE);
                      ((CustomTextView)view.findViewById(R.id.altOrderTitle)).setText("Opening...");
                      view.setBackgroundColor(Color.rgb(66, 174, 255));
                      Log.i("selected item product", listItem.getProduct().getProductType());
                     try {
                    	 messagesExport = listItem.getCusThread().getMessages();
                 	  }
                 	  catch (NullPointerException npe) {
                 		 Toast toast = Toast.makeText(DashboardActivityAlt.this, 
                    			 "You should to swipe the item properly", Toast.LENGTH_SHORT);
                    	 toast.show();
                    	 Intent intent = getIntent();
                    	 finish();
                    	 startActivity(intent);
                 	  }
                     
                     Intent i = new Intent(getApplicationContext(),
                            OrderInfoTabScreen.class);
                     startActivity(i);
	                  
	               
	          }
	      });
		listView1.setOnTouchListener(gestureListener);
		listView1.setOnItemLongClickListener(new OnItemLongClickListener() {
	          public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
	        	  if (position<forPrint.size())
	        	  {
	        	  listItem =  forPrint.get(position);
	        	  List<String> listItems = new ArrayList<String>();

	        	  listItems.add("Open");

	        	  if (DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId() == 2 | 
	        			  DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId() == 3 | 
		        			 DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId() == 4 | 
		        			  DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId() == 5)
	        		  listItems.add("Inactivate");
	        	final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
	  			final AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivityAlt.this);
	  		    builder.setTitle(Integer.toString(forPrint.get(position).getOrderid())+ "  " +forPrint.get(position).getTitle());
	  			builder.setItems(items, new DialogInterface.OnClickListener() {
	  			    public void onClick(DialogInterface dialog, int item) {
	  			    	if (item == 0)
	  			    	{
	  			    		
		                     try {
		                    	 messagesExport = listItem.getCusThread().getMessages();
		                 	  }
		                 	  catch (NullPointerException npe) {
		                 		 Toast toast = Toast.makeText(DashboardActivityAlt.this, 
		                    			 "You should to swipe the item properly", Toast.LENGTH_SHORT);
		                    	 toast.show();
		                    	 Intent intent = getIntent();
		                    	 finish();
		                    	 startActivity(intent);
		                 	  }
		                    
		                     Intent i = new Intent(getApplicationContext(),
			                            OrderInfoTabScreen.class);
			                     startActivity(i);
		                     
	  			    	}
	  			    	else if (item == 1)
	  			    	{
	  			    		//new InactivateOrder().execute();
	  			    		InactivateAsync.execute(DashboardActivityAlt.this, DashboardActivityAlt.this);
	  			    	}
	  			    	
	  			    	 
	  			    }
	  			});
	  			AlertDialog alert = builder.create();
	  			alert.show();
	        	  }
	        	  return false;
	         
	          }
	      });

       
	}
	
	
	 class MyGestureListener extends SimpleOnGestureListener implements OnTouchListener
	    {
		 @Override
		public boolean onDown(MotionEvent arg0) {
			 Log.i("MyGesture", "onDown");  
			return true;
		}
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if(Math.abs(e1.getY()-e2.getY()) > 250) 
	            return false;               
	        if(e1.getX() - e2.getX() > 70// && Math.abs(velocityX) > 200
	        		){ 
	        	setAction(Action.RL);
	 	      Log.d("one", "Coords: x=" + e1.getX() + ",y=" + e2.getY());
	 	     return true;
	        }
	        else if(e2.getX() - e1.getX() > 70// && Math.abs(velocityX) > 200
	        		){
	        	
	        	setAction(Action.LR);
	  	       Log.d("two", "Coords: x=" + e1.getX() + ",y=" + e2.getY());
	  	     return true;
	        }
			return false;
		}
		
		@Override
		public void onLongPress(MotionEvent arg0) {
//			final CharSequence[] items = {"Open", "Delete", "Details"};
//			final AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivityAlt.this);
//		    builder.setTitle("");
//		    final Integer position  = (Integer) ((TextView)arg0).getTag();
//		    Log.i("current list pos", position.toString());
//			builder.setItems(items, new DialogInterface.OnClickListener() {
//			    public void onClick(DialogInterface dialog, int item) {
//			    	if (item == 0)
//			    	{
//			    		
//			    	}
//			    	 else if (item == 1)
//			    		{  
//			    		 
//			    		   if (FileManagerActivity.getFinalAttachFiles().size()==1)
//			    			   FileManagerActivity.getFinalAttachFiles().clear();
//			    		   else
//			    		 FileManagerActivity.getFinalAttachFiles().remove(position.intValue());
//			    		   
//			    		   ((LinearLayout)arg0.getParent()).removeView(arg0);
//			    		   layout.invalidate();
//			    		   Log.i("count",Integer.toString(FileManagerActivity.getFinalAttachFiles().size()));
//			    		}
//			    	else if (item == 2)
//			    	{
//			    		
//			    	}
//			    	
//			    	 
//			    }
//			});
//			AlertDialog alert = builder.create();
//			alert.show();
			
			
		}
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float arg2,
				float arg3) {
			mIsScrolling = true;   
			float pos = dpFromPx(e2.getY() - e1.getY());
			if (pos < -280 & pos > -320)
			{
				Log.i("stopDownload flag", Boolean.toString(stopDownload));
				mSwipeDetected = Action.BT;
				try {
					if (!AbstractTaskLoader.isCanselled())
					{	
						boolean isOnline = faq.isOnline();
						if (isOnline)
						{
							DashboardAsync.execute(DashboardActivityAlt.this, DashboardActivityAlt.this);
							adapter.notifyDataSetChanged();
						}
					}
					else
					{
					   Toast mToast =  Toast.makeText(getApplicationContext(), "Downloading was interrupted. Please try later", Toast.LENGTH_LONG);
			   	  	   mToast.show();
					}

				} 

			catch (Exception e) {
					e.printStackTrace();
				}
				
			}

		  	return false;
		}
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			
			 Log.i("MyGesture", "onSingleTapUp");  
			return false;
			
			
		}
		@Override
        public boolean onDoubleTap(MotionEvent e) {
//			try {
//				
//					if (!DashboardActivityAlt.pd.isShowing() & !DashboardActivityAlt.stopDownload)
//					{
//						DashboardActivityAlt.pd = ProgressDialog.show(DashboardActivityAlt.this,
//							"Please wait..", "Downloading Data...", true, true);
//							new DownloadTask().execute();
//					}
//			
//				
//			}
//			catch (Exception exp) {
//				// TODO Auto-generated catch block
//				exp.printStackTrace();
//			}
			return true;
		}
		
		public boolean onTouchEvent(MotionEvent me)
		 {
			return gestureDetector.onTouchEvent(me);
		 }
		
			public boolean onTouch(View arg0, MotionEvent arg1) 
			{

				return false;
			 
			}
			
	    }

	    @Override
	    public boolean dispatchTouchEvent(MotionEvent ev){
	        super.dispatchTouchEvent(ev);
	        //return gestureDetector.onTouchEvent(ev);
	        return true;
	    } 
	    
	    private float dpFromPx(float px)
	    {
	        return px / ctx.getResources().getDisplayMetrics().density;
	    }

		  private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	        	Log.i("Dashboard onreceive", "I've got receiving");
	        	adapter.notifyDataSetChanged();
	        	updateUI(intent);       
	        }
	    };
		
	    @Override
		public void onResume() {
			super.onResume();	
			Log.i("dashboard activity", "it was resumed");
			listView1.setAdapter(adapter);
			if (savedPosition >= 0) {
			      listView1.setSelectionFromTop(savedPosition, savedListTop);
			    }
			startService(intent);
			registerReceiver(broadcastReceiver, new IntentFilter(ServiceIntentMessages.ORDERS_IMPORT));
			
		}
	    
	    
	    @Override
		public void onRestart() {
			super.onRestart();	
			Log.i("dashboard activity", "it was restarted");
		}
	    
		@Override
		public void onPause() {
			super.onPause();
			Log.i("dashboard activity", "it was stoped");
	
			unregisterReceiver(broadcastReceiver);
		   stopService(intent);
		   savedPosition = listView1.getFirstVisiblePosition();
		   View firstVisibleView = listView1.getChildAt(0);
		   savedListTop = (firstVisibleView == null) ? 0 : firstVisibleView.getTop();
		   editor.putInt("savedPosition", savedPosition);
		      editor.putInt("savedListTop", savedListTop);
		      editor.commit();
		}	

		
	    private void updateUI(Intent intent) {
	    	
	    	try 
			{	
				//forPrint = new ArrayList<Order>(ServiceIntentMessages.serviceList);//ServiceIntentMessages.serviceList;
	    		DashboardActivityAlt.this.runOnUiThread(new Runnable ()
	    		{
					public void run() {
//						 Bundle b = getIntent().getExtras();
//						 if (b!=null)
//							 {  ArrayList<Order> fromService = b.getParcelableArrayList("orders");
//						     forPrint = fromService;
//						     Log.i("from service list", Integer.toString(fromService.size()));
//							for(Order a: fromService)
//							{
//								Log.i("dashboard update list", a.toString());
//							}
			//				OrderAdapter adapter = new OrderAdapter(DashboardActivityAlt.this,
			//						R.layout.dash_alt_item, ServiceIntentMessages.serviceList);
			//				listView1.setAdapter(adapter);
						for (Order a: forPrint)
						{
						Log.i("forPrint befor equate updateUI", a.toString());
						}
						ServiceIntentMessages b = new ServiceIntentMessages();
						forPrint = b.getServiceList();
						for (Order a: b.getServiceList())
						{
							Log.i("serviceList befor equate updateUI", a.toString());
						}
					//	 forPrint = ServiceIntentMessages.serviceList;
						 adapter.notifyDataSetChanged();
//						 }
						
					}
	    		});
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
	    }
	    
	    
	    @Override
	    public void onSaveInstanceState(Bundle savedInstanceState) {
	      super.onSaveInstanceState(savedInstanceState);
	      // Save UI state changes to the savedInstanceState.
	      // This bundle will be passed to onCreate if the process is
	      // killed and restarted.
	      Log.i("dashboard onSaveInstance","Im here fuck");
	        savedPosition = listView1.getFirstVisiblePosition();
	        state = listView1.onSaveInstanceState();
			View firstVisibleView = listView1.getChildAt(0);
			savedListTop = (firstVisibleView == null) ? 0 : firstVisibleView.getTop();
			Log.i("listview pos on save", Integer.toString(savedPosition));
	        Log.i("listview pos on save", Integer.toString(savedListTop));
	      savedInstanceState.putInt("savedPosition", savedPosition);
	      savedInstanceState.putInt("savedListTop", savedListTop);
	    }
	    
	    
	    @Override
	    public void onRestoreInstanceState(Bundle savedInstanceState) {
	      super.onRestoreInstanceState(savedInstanceState);
	      
	      int savedPosition = savedInstanceState.getInt("savedPosition");
	      int savedListTop=  savedInstanceState.getInt("savedListTop");
	      Log.i("Restore listview pos", Integer.toString(savedPosition));
	      Log.i("Restore listview pos", Integer.toString(savedListTop));
	      if (savedPosition >= 0) {
		      listView1.setSelectionFromTop(savedPosition, savedListTop);
		    }
	    }
	    
	    @Override
	    public void onBackPressed() {
	        // Do Here what ever you want do on back press;
	    }
	    @Override
		  public void onConfigurationChanged(Configuration newConfig) {
		    super.onConfigurationChanged(newConfig);
		  }
		public void onLoadFinished(Object data) {
			
				
				if (DashboardActivityAlt.page == 2)
	        	{	
	        		listView1.setAdapter(adapter);
	        	}
	        	adapter.notifyDataSetChanged();
			
		}
		public void onCancelLoad() {
			 Toast.makeText(DashboardActivityAlt.this, "Some problems at server occurs. Please try later ", Toast.LENGTH_LONG).show();
			
		}
		
	    public ArrayList<Order> getOrderList()
	    {
	    	return forPrint;
	    }
	     public void updateList()
	     {
	    	 adapter.notifyDataSetChanged();
	     }
	    
}
