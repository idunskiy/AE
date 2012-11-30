package com.assignmentexpert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

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
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.datamodel.Category;
import com.datamodel.Level;
import com.datamodel.Messages;
import com.datamodel.Order;
import com.datamodel.ProcessStatus;
import com.datamodel.Subject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.library.DataParsing;
import com.library.DatabaseHandler;
import com.library.FrequentlyUsedMethods;
import com.library.OrderAdapter;
import com.library.ServiceMessages;
import com.library.SwipeDetector;
import com.library.UserFunctions;

public class DashboardActivityAlt extends Activity{
	 public static enum Action {
	        LR, // Left to Right
	        RL, // Right to Left
	        TB, // Top to bottom
	        BT, // Bottom to Top
	        None // when no action was detected
	    }
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
	static int page = 1;
    static int perpage = 6;
    static boolean stopDownload = false;
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
	    private static final String logTag = "SwipeDetector";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dash_alt);
		
		btnClose  =(Button)findViewById(R.id.btnClose);
	    btnNewOrder = (Button) findViewById(R.id.btnNewOrder);
	    btnProfile = (Button) findViewById(R.id.btnProfile);
	    dashboardHeader = (LinearLayout) findViewById(R.id.dashboardHeader);
	    dashboardHeader.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	        public void onGlobalLayout() {
	        	layoutHeight = dashboardHeader.getMeasuredHeight();
	        }
	    });
	    final Activity act = DashboardActivityAlt.this;
	    View footer = getLayoutInflater().inflate(R.layout.dash_footer, null);
	    intent = new Intent(DashboardActivityAlt.this, ServiceMessages.class);
		listView1 = (ListView)findViewById(R.id.altOrderslist);
		listView1.setCacheColorHint(Color.WHITE);
		listView1.addFooterView(footer);
		final FrequentlyUsedMethods met = new FrequentlyUsedMethods();
		
		final float HORIZONTAL_MIN_DISTANCE =met.convertPixelsToDp(30,act);
		final float VERTICAL_MIN_DISTANCE = met.convertPixelsToDp(100,act);
		ctx = this;
	    DisplayMetrics displaymetrics = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	    int height = (int)dpFromPx(displaymetrics.heightPixels);
	    Log.i("height", Integer.toString(height));
	    height = height - (int)dpFromPx(90);
	    int count =(int)dpFromPx(height)/(int)dpFromPx(50);
	    adapter = new OrderAdapter(DashboardActivityAlt.this,
    			R.layout.dash_alt_item, forPrint);
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
				FileManagerActivity.setFinalAttachFiles(files);
			}
		}
		
		//listView1.addView( findViewById(R.id.empty_list_view1 ), new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		if (forPrint.isEmpty() & !stopDownload )
		{
			
			DashboardActivityAlt.page = 1;
			this.pd = ProgressDialog.show(this, "Please wait..", "Downloading Data..."
					, true, false); 
			
			new DownloadTask().execute(Integer.toString(DashboardActivityAlt.page), Integer.toString(DashboardActivityAlt.perpage));
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
			this.pd = ProgressDialog.show(this, "Please wait..", "Downloading Data..."
					, true, false); 
			new DownloadTask().execute(Integer.toString(DashboardActivityAlt.page), Integer.toString(DashboardActivityAlt.perpage));
			Log.i("new page count dashboard",Integer.toString(DashboardActivityAlt.page));
			LoginActivity.newUser = false;
		}
//		else if (InteractionsActivityViewPager.deleteFlag)
//		{
//			DashboardActivityAlt.page = 1;
//			forPrint.clear();
//			this.pd = ProgressDialog.show(this, "Please wait..", "Downloading Data..."
//					, true, false); 
//			new DownloadTask().execute(Integer.toString(DashboardActivityAlt.page), Integer.toString(DashboardActivityAlt.perpage));
//			InteractionsActivityViewPager.deleteFlag = false;
//		}
		
		else
		{
			Log.i("old user","dashboard");
			 adapter = new OrderAdapter(DashboardActivityAlt.this,
        			R.layout.dash_alt_item, forPrint);
			 listView1.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Log.i(" old page count dashboard",Integer.toString(DashboardActivityAlt.page));
			
		}
		sharedPreferences = getSharedPreferences("user", MODE_PRIVATE );
		editor = sharedPreferences.edit();
		
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

	                    // horizontal swipe detection
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
	                  if (getAction() == getAction().LR)
	                  {
	                	 Log.i("start value", "it might be started");
	                	 view.setBackgroundColor(Color.GREEN);
	                     Log.i("Swipe Action", getAction().toString());
	                    
	                     
	                      listItem = (Order) listView1.getItemAtPosition(position);
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
	                            InteractionsActivityViewPager.class);
	                     
	                     
	                     //i.putExtra("messages", position);
	                    // i.putParcelableArrayListExtra ("messages", listItem.getCusThread().getMessages());
	                     startActivity(i);
	                     
	                    
	                  } 
	             
	                  else if(!swipeDetected())
	                  {
	                	  Log.i("stop value", "it was stoped");
	                     //view.setBackgroundColor(Color.WHITE);
	                  }
	                  
	               
	          }
	      });
		listView1.setOnTouchListener(gestureListener);
		listView1.setOnItemLongClickListener(new OnItemLongClickListener() {
	          public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
	        	  final CharSequence[] items = {"Open", "Inactivate"};
	  			final AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivityAlt.this);
	  		    builder.setTitle(Integer.toString(DashboardActivityAlt.forPrint.get(position).getOrderid())+ "  " +DashboardActivityAlt.forPrint.get(position).getTitle());
	  		    
	  			builder.setItems(items, new DialogInterface.OnClickListener() {
	  			    public void onClick(DialogInterface dialog, int item) {
	  			    	if (item == 0)
	  			    	{
	  			    		listItem =  DashboardActivityAlt.forPrint.get(position);
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
		                            InteractionsActivityViewPager.class);
		                     startActivity(i);
		                     
	  			    	}
	  			    	else if (item == 1)
	  			    	{
	  			    		new InactivateOrder().execute();
	  			    	}
	  			    	
	  			    	 
	  			    }
	  			});
	  			AlertDialog alert = builder.create();
	  			alert.show();
	        	  return false;
	          }
	      });

		
		listView1.setBackgroundColor(Color.WHITE);
        btnClose.setOnClickListener(new View.OnClickListener() {
    	       
            public void onClick(View view) {
            	//editor.putBoolean("logout", true);
            	editor.remove("username");
            	editor.remove("password");
            	editor.remove("isChecked");
            	editor.commit();
            	Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
            } 
    	});
        
        
        btnNewOrder.setOnClickListener(new View.OnClickListener() {
        	 
            public void onClick(View view) {
            	
                Intent i = new Intent(getApplicationContext(),
                        PreOrderActivity.class);
                startActivity(i);
                
            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
          	 
            public void onClick(View view) {
            	
                Intent i = new Intent(getApplicationContext(),
                       ProfileActivity.class);
                startActivity(i);
            }
        });
        
       
	}
	
	
	 class MyGestureListener extends SimpleOnGestureListener implements OnTouchListener
	    {
		 @Override
		public boolean onDown(MotionEvent arg0) {
			Log.i("asd","sdas");
			return true;
		}
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if(Math.abs(e1.getY()-e2.getY()) > 250) 
	            return false;               
	        if(e1.getX() - e2.getX() > 120// && Math.abs(velocityX) > 200
	        		){ 
	        	
	 	      //mSwipeDetected = Action.RL;
	 	      Log.d("one", "Coords: x=" + e1.getX() + ",y=" + e2.getY());
	            //do something...
	 	     return true;
	        }
	        else if(e2.getX() - e1.getX() > 70// && Math.abs(velocityX) > 200
	        		){
	        	
	        	setAction(Action.LR);
	  	       Log.d("two", "Coords: x=" + e1.getX() + ",y=" + e2.getY());
	            //do something...
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
//			
			float pos = dpFromPx(e2.getY() - e1.getY());
			if (pos < -280 & pos > -320)
			{
				mSwipeDetected = Action.BT;
				try {
					if (!DashboardActivityAlt.pd.isShowing() & !DashboardActivityAlt.stopDownload)
					{
					DashboardActivityAlt.pd = ProgressDialog.show(DashboardActivityAlt.this,
							"Please wait..", "Downloading Data...", true, true);
			           new DownloadTask().execute();
					}
//					else
//					{
//					   Toast mToast =  Toast.makeText(getApplicationContext(), "���� ������!!!", Toast.LENGTH_LONG);
//			   	  	   mToast.show();
//					}

				} 

			catch (Exception e) {
					// TODO Auto-generated catch block
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
				if (arg1.getAction()==MotionEvent.ACTION_UP)
					arg0.setBackgroundColor(Color.WHITE);
				return false;
			  }
	    }

	    @Override
	    public boolean dispatchTouchEvent(MotionEvent ev){
	        super.dispatchTouchEvent(ev);
	        //return gestureDetector.onTouchEvent(ev);
	        return true;
	    } 
	    
	    private class DownloadTask extends AsyncTask<String, Void, List<Order> >
	    {
	    	boolean res = false;
	    	String result = "";
			List<String[]> results = null;
			String Res[][] = null;
			Dao<ProcessStatus,Integer> dao;
			
			
	        protected List<Order>  doInBackground(String... args) {
	        
	        try {
	        	
	        	db = new DatabaseHandler(getApplicationContext());
				dao = db.getStatusDao();
				Dao<Level,Integer> daoLevel = db.getLevelDao();
				Dao<Subject,Integer> daoSubject = db.getSubjectDao();
				Dao<Category,Integer> daoCategory = db.getCategoryDao();
	        
	        	
	        	DataParsing u = new DataParsing();
	        	UserFunctions n = new UserFunctions();
	    		JSONObject k;
				
						k = n.getOrders(Integer.toString(DashboardActivityAlt.page),Integer.toString(DashboardActivityAlt.perpage));
						orders = u.wrapOrders(k);
					
		    		
		    		
		    		if (orders.isEmpty())
		    		{
		    			Log.i("empty orders","orders are empty");
		    			stopDownload = true;
		    			new DownloadTask().cancel(true);
		    			DashboardActivityAlt.pd.dismiss();
		    			
		    			res = true;
		    		}
		    		else
		    		{
						
					
					Log.i("status",dao.queryForAll().toString());
					Log.i("level",daoLevel.queryForAll().toString());
					Log.i("subject",daoSubject.queryForAll().toString());
					Log.i("category",daoCategory.queryForAll().toString());
		    		}
	    		
				QueryBuilder<ProcessStatus,Integer> query = dao.queryBuilder();
				Where where = query.where();
				
				}
	    		catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    try{
			    	
			    	for(Order r : orders)
			    	{
				         GenericRawResults<String[]> rawResults = dao.queryRaw("select "+ ProcessStatus.STATUS_TITLE +" from process_status where "+ ProcessStatus.STATUS_ID + " = " + r.getProcess_status().getProccessStatusId());
				         results = rawResults.getResults();
				         String[] resultArray = results.get(0); //This will select the first result (the first and maybe only row returned)
				         result = resultArray[0]; //This will select the first field in the result (That should be the ID you need)
				         if (!r.getIsActive())
				        	 r.getProcess_status().setProccessStatusTitle("Inactive");
				         else
				        	 r.getProcess_status().setProccessStatusTitle(result);
				         
				         forPrint.add(r);
				         Log.i("come in orders", r.toString());
				         
			         }
			    	
	    		}
			    
				 catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				 }
			    catch (Exception e) {
			        e.printStackTrace();
			    }
			    res = false;
			    DashboardActivityAlt.page+=1;
	       	
	       		return forPrint;

	        }

	        protected void onPostExecute(List<Order>  forPrint) {
	            // Pass the result data back to the main activity
	        	
				if (!res)
				{
					
					if (DashboardActivityAlt.page ==2)
		        	{	
		        		listView1.setAdapter(adapter);
		        	}
		        	adapter.notifyDataSetChanged();
	            }
				else 
				{
					  this.cancel(true);
					  Toast mToast =  Toast.makeText(getApplicationContext(), "���� ������!!!", Toast.LENGTH_LONG);
		   	  	      mToast.show();
					
				}
	            	DashboardActivityAlt.pd.dismiss();
	  
	            
	        }
	   }
	    private float dpFromPx(float px)
	    {
	        return px / ctx.getResources().getDisplayMetrics().density;
	    }

		  private class InactivateOrder extends AsyncTask<Void, Void, JSONObject > {
			    
			    JSONObject as;
				private ProgressDialog progDailog;
				protected void onPreExecute() {
		        	progDailog = ProgressDialog.show(DashboardActivityAlt.this,"Please wait...", "Proceed your order inactivation ...", true);
		        }

		        protected JSONObject doInBackground(Void... args) {
		        
		       	try 
		       	{
		       		UserFunctions func = new UserFunctions();
	            	
					try {
						as = func.deleteOrder(Integer.toString(DashboardActivityAlt.listItem.getOrderid()));
						Log.i("delete operation", as.toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//deleteFlag = true;
					DashboardActivityAlt.listItem.getProcess_status().setProccessStatusTitle("Inactive");
					Log.i("send message response",as.toString());
				} catch (Exception e) {
					
					e.printStackTrace();
				}
		       		return as;
		       		
		        }

		        protected void onPostExecute(JSONObject  forPrint) {
		 	  	   progDailog.dismiss();
		 	  	   Intent i = new Intent(getApplicationContext(),
	                       DashboardActivityAlt.class);
	               startActivity(i);
		 	  	
	
		            
		        }
				
		   }

	  
	    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	        	Log.i("Dashboard onreceive", "I've got receiving");
//	        	if (intent.getExtras().getParcelableArrayList("ordersService").isEmpty())
//	        	{
//	        	for (Parcelable e : getIntent().getExtras().getParcelableArrayList("ordersService"))
//	        	{
//	        		Log.i("from service", e.toString());
//	        	}
//	        	}
//	        	else
//	        		Log.i("orders from service", "e,pty;");
//	        	Bundle extras = intent.getExtras();
//				  
//			    for (Parcelable a : extras.getParcelableArrayList("ordersService"))
//			    { 
//			    	Log.i("intent from service", a.toString());
//			    	
//			    }
        	//if (!(ServiceMessages.orderExport.equals(DashboardActivityAlt.forPrint)))
	        	updateUI(intent);       
	        }
	    };
	    @Override
		public void onResume() {
			super.onResume();	
			startService(intent);
			registerReceiver(broadcastReceiver, new IntentFilter(ServiceMessages.ORDERS_IMPORT));
			
		}

		@Override
		public void onPause() {
			super.onPause();
			unregisterReceiver(broadcastReceiver);
		   stopService(intent); 		
		}	

	    private void updateUI(Intent intent) {
	    	
	    	try {
	    		
	    		db = new DatabaseHandler(getApplicationContext());
	    		Dao<ProcessStatus,Integer> daoProcess = db.getStatusDao();
				try 
				{
					DashboardActivityAlt.forPrint.get(0).getProcess_status().setProccessStatusTitle
					((daoProcess.queryForId(DashboardActivityAlt.forPrint.get(0).getProcess_status().getProccessStatusId()).getProccessStatusTitle()));
					Log.i("order dashboard", Integer.toString(DashboardActivityAlt.forPrint.get(0).getProcess_status().getProccessStatusId()));
					Log.i("order title process title",daoProcess.queryForId(DashboardActivityAlt.forPrint.get(0).getProcess_status().getProccessStatusId()).getProccessStatusTitle());
					
					OrderAdapter adapter = new OrderAdapter(DashboardActivityAlt.this,
		        			R.layout.dash_alt_item, ServiceMessages.orderExport);
		        	listView1.setAdapter(adapter);
		          
		            adapter.notifyDataSetChanged();
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	

	    }
	
	    
	    
	    
}
