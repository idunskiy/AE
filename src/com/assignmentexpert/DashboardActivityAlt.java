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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.datamodel.Category;
import com.datamodel.Level;
import com.datamodel.Message;
import com.datamodel.Order;
import com.datamodel.ProcessStatus;
import com.datamodel.Subject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.library.DataParsing;
import com.library.DatabaseHandler;
import com.library.OrderAdapter;
import com.library.RestClient;
import com.library.ServiceMessages;
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
	boolean res = false;
	static int page = 1;
    static int perpage = 6;
    static boolean stopDownload = false;
    public static List<Message> messagesExport;
	static private ProgressDialog pd;
	DatabaseHandler db;
	static public Order listItem;
	private Intent intent;
	boolean isEmpty = false;
	 private Action mSwipeDetected = Action.None;

	    public boolean swipeDetected() {
	        return mSwipeDetected != Action.None;
	    }
	    public Action getAction() {
	        return mSwipeDetected;
	    }
	    
	    private static final int MIN_DISTANCE = 100;
		private static final float HORIZONTAL_MIN_DISTANCE =30;
		private static final float VERTICAL_MIN_DISTANCE = 100;
	    private float downX, downY, upX, upY ,  stopX, stopY;
	    OrderAdapter adapter;
		private SharedPreferences sharedPreferences;
		private Editor editor;
	    private static final String logTag = "SwipeDetector";
	    
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.dash_alt);
		
		btnClose  =(Button)findViewById(R.id.btnClose);
	    btnNewOrder = (Button) findViewById(R.id.btnNewOrder);
	    View footer = getLayoutInflater().inflate(R.layout.dash_footer, null);
	    intent = new Intent(this, ServiceMessages.class);
		listView1 = (ListView)findViewById(R.id.altOrderslist);
		listView1.setCacheColorHint(Color.WHITE);
		listView1.addFooterView(footer);
		RestClient helper = new RestClient(getBaseContext());
		//listView1.addView( findViewById(R.id.empty_list_view1 ), new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		if (forPrint.isEmpty() & !stopDownload )
		{
			Log.i("download new orders","dashboard");
			DashboardActivityAlt.page = 1;
			this.pd = ProgressDialog.show(this, "Please wait..", "Downloading Data..."
					, true, false); 
			new DownloadTask().execute();
			Log.i("download page count dashboard",Integer.toString(DashboardActivityAlt.page));
			
		}
		else if (LoginActivity.newUser)
		{
			Log.i("new user","dashboard");
			forPrint.clear();
			stopDownload = false;
			DashboardActivityAlt.page = 1;
			this.pd = ProgressDialog.show(this, "Please wait..", "Downloading Data..."
					, true, false); 
			new DownloadTask().execute();
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
            Log.i(" old page count dashboard",Integer.toString(DashboardActivityAlt.page));
			
		}
		sharedPreferences = getSharedPreferences("user", MODE_PRIVATE );
		editor = sharedPreferences.edit();
    
	    gestureDetector = new GestureDetector(new MyGestureListener());
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
	                                     if (deltaX <= 0) {
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
	                                 if (Math.abs(deltaY) > VERTICAL_MIN_DISTANCE) 
	                                 {
	                                     // top or down
	                                     if (deltaY < 0) {
	                                         Log.i(logTag, "Swipe Top to Bottom");
	                                         mSwipeDetected = Action.TB;
	                                         return false;
	                                     }
	                                     if (deltaY > 0) 
	                                     {
	                                         Log.i(logTag, "Swipe Bottom to Top");
	                                         mSwipeDetected = Action.BT;
	                                         return false;
	                                     }
	                                 } 
	                                 Log.i("delta X", Float.toString(deltaX));
	                                 return true;
	                     }
	                     case MotionEvent.ACTION_UP:
	                     {
	                     	
	                          stopX = event.getX();
	                          stopY = event.getY();
	                          return false;
	                     }
	                     
	                     
	                     }
	                     
	                	return true;}
	        
				return false;
	
	        }

        };

       
       // final SwipeDetector swipeDetector = new SwipeDetector();
	//	listView1.setOnTouchListener(swipeDetector);
		listView1.setOnItemClickListener(new OnItemClickListener() {
	          public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	          {
	                  if (getAction()== getAction().LR)
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
	          public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
	              if (swipeDetected()){
//	            	  Toast mToast =  Toast.makeText(getApplicationContext(), "Ебись как хочешь", Toast.LENGTH_LONG);
//	   	  	       mToast.show();
	              } else 
	              {
	                  // do the onItemLongClick action
	              }
				return mIsScrolling;
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
                        NewOrderActivity.class);
                startActivity(i);
                
            }
        });
        
       
	}
	
	 private boolean getOrderList(String start, String end) throws Exception
	    {

	        db = new DatabaseHandler(getApplicationContext());
	        
	        
	        	
	        	DataParsing u = new DataParsing();
	        	UserFunctions n = new UserFunctions();
	    		JSONObject k= n.getOrders(start,end);
	    		
	    		orders = u.wrapOrders(k);
	    		if (orders.isEmpty())
	    		{
	    			Log.i("empty orders","orders are empty");
	    			stopDownload = true;
	    			new DownloadTask().cancel(true);
	    			DashboardActivityAlt.pd.dismiss();
	    			
	    			isEmpty = true;
	    		}
	    		else
	    		{
				Dao<ProcessStatus,Integer> dao = db.getStatusDao();
				Dao<Level,Integer> daoLevel = db.getLevelDao();
				Dao<Subject,Integer> daoSubject = db.getSubjectDao();
				Dao<Category,Integer> daoCategory = db.getCategoryDao();		
				
				Log.i("status",dao.queryForAll().toString());
				Log.i("level",daoLevel.queryForAll().toString());
				Log.i("subject",daoSubject.queryForAll().toString());
				Log.i("category",daoCategory.queryForAll().toString());

				QueryBuilder<ProcessStatus,Integer> query = dao.queryBuilder();
				Where where = query.where();
				String result = "";
				List<String[]> results = null;
				String res[][] = null;

			    try{
			    	
			    	for(Order r : orders)
			    	{
				         GenericRawResults<String[]> rawResults = dao.queryRaw("select "+ ProcessStatus.STATUS_TITLE +" from process_status where "+ ProcessStatus.STATUS_ID + " = " + r.getProcess_status().getProccessStatusId());
				         results = rawResults.getResults();
				         String[] resultArray = results.get(0); //This will select the first result (the first and maybe only row returned)
				         result = resultArray[0]; //This will select the first field in the result (That should be the ID you need)
				         r.getProcess_status().setProccessStatusTitle(result);
				         forPrint.add(r);
				         
			         }
			    	
			    	
	    		}
			    

			  

				 catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				 }
			    catch (Exception e) {
			        e.printStackTrace();
			    }
			    isEmpty = false;
			    DashboardActivityAlt.page+=1;
			}
	    		 
	    	return isEmpty;
	    
	    }
	 class MyGestureListener extends SimpleOnGestureListener implements OnTouchListener
	    {
		
		public boolean onDown(MotionEvent arg0) {
			Log.i("asd","sdas");
			return true;
		}
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
	        else if(e2.getX() - e1.getX() > 120// && Math.abs(velocityX) > 200
	        		){
	        	
	  	    // mSwipeDetected = Action.LR;
	  	       Log.d("two", "Coords: x=" + e1.getX() + ",y=" + e2.getY());
	            //do something...
	  	     return true;
	        }
			return false;
		}
		public void onLongPress(MotionEvent arg0) {
			 AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivityAlt.this);
        	builder.setMessage("Are you sure to choose timezone  "+"  ?")
     	   .setCancelable(false)
     	   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
     	       public void onClick(DialogInterface dialog, int id) {
     	           
     	       }
     	   })
     	   .setNegativeButton("No", new DialogInterface.OnClickListener() {
     	       public void onClick(DialogInterface dialog, int id) {
     	    	   
     	            dialog.cancel();
     	       }
     	   });
     	AlertDialog alert = builder.create();
     	alert.show();
			
		}
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float arg2,
				float arg3) {
			mIsScrolling = true;   
			
			float pos = e2.getY() - e1.getY();
			if (pos < -170 & pos > -200)
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
//					   Toast mToast =  Toast.makeText(getApplicationContext(), "Нету больше!!!", Toast.LENGTH_LONG);
//			   	  	   mToast.show();
//					}

				} 

			catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

		  	return true;
		}
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			Log.i("SingleTap Dashboard",Float.toString(e.getY()));
			return false;
		}
		@Override
        public boolean onDoubleTap(MotionEvent e) {
			Log.i("DoubleTap Dashboard",Float.toString(e.getY()));
			return false;
		}
		
	    
		 public boolean onTouchEvent(MotionEvent me)
		 {
			 Log.d(null,"Touch");
		 return gestureScanner.onTouchEvent(me);
		 }
		public boolean onTouch(View arg0, MotionEvent arg1) {
			if (arg1.getAction()==MotionEvent.ACTION_UP)
				arg0.setBackgroundColor(Color.WHITE);
			return false;
		}
	    }

	    @Override
	    public boolean dispatchTouchEvent(MotionEvent ev){
	        super.dispatchTouchEvent(ev);
	        return gestureDetector.onTouchEvent(ev);
	    } 
	    
	    private class DownloadTask extends AsyncTask<String, Void, List<Order> > {
	        protected List<Order>  doInBackground(String... args) {
	        
	       	try {
	       		
				res = DashboardActivityAlt.this.getOrderList(Integer.toString(DashboardActivityAlt.page), Integer.toString(DashboardActivityAlt.perpage));
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	       	
	       	
	       		return forPrint;

	        }

	        protected void onPostExecute(List<Order>  forPrint) {
	            // Pass the result data back to the main activity
	        	
				if (!res )
				{
		        	DashboardActivityAlt.this.listView1 = (ListView)findViewById(R.id.altOrderslist);
		        	OrderAdapter adapter = new OrderAdapter(DashboardActivityAlt.this,
		        			R.layout.dash_alt_item, forPrint);

	
		        	listView1.setAdapter(adapter);
		          
		            adapter.notifyDataSetChanged();
	            }
				else 
				{
					  this.cancel(true);
					  Toast mToast =  Toast.makeText(getApplicationContext(), "Нету больше!!!", Toast.LENGTH_LONG);
		   	  	      mToast.show();
					
				}

	            	DashboardActivityAlt.pd.dismiss();
	  
	            
	        }
	   }
	    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
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
	    		for (Order a : ServiceMessages.orderExport)
				  {
					  
					 Log.i("dashboard act from service", a.toString());
				  }
	    		 Log.i(" ", "  ");
				  for (Order a : DashboardActivityAlt.forPrint)
				  {
					  
					 Log.i("dashboard act from dashboard", a.toString());
				  }
	    		db = new DatabaseHandler(getApplicationContext());
	    		Dao<ProcessStatus,Integer> daoProcess = db.getStatusDao();
				try 
				{
					Log.i("order new", DashboardActivityAlt.forPrint.get(0).toString());
					DashboardActivityAlt.forPrint.get(0).getProcess_status().setProccessStatusTitle
					((daoProcess.queryForId(DashboardActivityAlt.forPrint.get(0).getProcess_status().getProccessStatusId()).getProccessStatusTitle()));
					Log.i("order dashboard", Integer.toString(DashboardActivityAlt.forPrint.get(0).getProcess_status().getProccessStatusId()));
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	OrderAdapter adapter = new OrderAdapter(DashboardActivityAlt.this,
        			R.layout.dash_alt_item, ServiceMessages.orderExport);
        	listView1.setAdapter(adapter);
          
            adapter.notifyDataSetChanged();

	    }
	    
}
