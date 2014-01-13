package com.assignmentexpert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.arrayadapters.OrderAdapter;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.asynctasks.DashboardAsync;
import com.customitems.CustomTextView;
import com.datamodel.Messages;
import com.datamodel.Order;
import com.datamodel.ProcessStatus;
import com.datamodel.Product;
import com.datamodel.ProductAssignment;
import com.datamodel.ProductWriting;
import com.gcmservice.DemoActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.j256.ormlite.dao.Dao;
import com.library.Constants;
import com.library.DatabaseHandler;
import com.library.FrequentlyUsedMethods;
import com.library.JSONParser;
import com.library.RequestMethod;
import com.library.ServiceIntentMessages;
import com.library.UserFunctions;
import com.library.singletones.OrderPreferences;
import com.library.singletones.SharedPrefs;
import com.tabscreens.OrderInfoTabScreen;

/** *Activity для отображения списка заказов пользователя.*/
 public class DashboardActivityAlt extends FragmentActivity implements ITaskLoaderListener, OnScrollListener {
	 /** *перечисление для определения направления движения touch'a пользователя*/
	 public static enum Action {
	        LR, // Left to Right
	        RL, // Right to Left
	        TB, // Top to bottom
	        BT, // Bottom to Top
	        None // when no action was detected
	    }
	
	 /** *контейнер для списка заказов*/
	public static ArrayList<Order> forPrint = new ArrayList<Order>();
	 /** *ListView для отображения заказов*/
	private ListView listView1;
	 /** *OnTouchListener для определения движения пользователя*/
	View.OnTouchListener gestureListener;
	static Context ctx;
	/** *номер страницы заказов пользователя*/
	public static int page = 1;
	/** *количество отображаемых заказов на странице*/
    public static int perpage;
    /** *флаг остановки загрузки заказов*/
    public static boolean stopDownload = false;
    /** *контейнер списка сообщений текущего заказа*/
    public static List<Messages> messagesExport;
    /** *экземпляр класса DatabaseHandler*/
	DatabaseHandler db;
	/** *выбранный заказ*/
	static public Order listItem;
	  /** *экземпляр класса Intent для работы с сервисом загрузки заказов и сообщений*/
	private Intent intent;
	
	View orderSwipedView;
	/** *экземпляр класса Intent для работы с сервисом загрузки заказов и сообщений*/
	boolean isEmpty = false;
	LinearLayout dashboardHeader;
	int layoutHeight;
	/** *флаг для определения направления движения swip'a - влево*/
	boolean borderDetLeft = false;
	/** *флаг для определения направления движения swip'a - вправо*/
	boolean borderDetRight = false;
	/** *прекращение swipe'a*/
	 private Action mSwipeDetected = Action.None;
	 /** *экземпляр класса UserFunctions для выполнения операций посылки заказов*/
	 UserFunctions userFunc = new UserFunctions();
	 
	 /** *getter для определения swipe'a*/
	    public boolean swipeDetected() {
	        return mSwipeDetected != Action.None;
	    }
	    /** *getter текущего Action'a*/
	    public Action getAction() {
	        return mSwipeDetected;
	    }
	    /** *setter текущего Action'a*/
	    public void setAction(Action action) {
	    	mSwipeDetected = action;
	    }
	    
	    /** *поля для получения текущих координат touch'a*/
	    private float downX, downY, upX, upY;
	    /** *экземпляр OrderAdapter*/
	    OrderAdapter adapter;
	    /** *экземпляр SharedPreferences для запоминания позиции ListView*/
		private SharedPreferences sharedPreferences;
		private Editor editor;
		/** *экземпляр FrequentlyUsedMethods*/
		FrequentlyUsedMethods faq;
	    private static final String logTag = "SwipeDetector";
	    /** *флаг открытия окна информации заказа*/
	    private boolean openInfoFlag = false;
	    /** *флаг открытия окна списка заказов*/
        private boolean openInteractFlag = false;
        /** *поле для запоминания текущей позиции в listView*/
        private int savedPosition;
        /** *поле для запоминания верхнего элемента*/
	    private int savedListTop;
	    Parcelable state;
	    /** *флаг для определения клика на заказе*/
	    boolean clickDetected = false;
	    /** *BroadcastReceiver для определения и реакцию на новый заказ*/
		NewOrderListener newOrderListener;
		OrderListUpdate orderListUpdateListener;
		NotificatedOrderOpen notificatedOrderOpenListener;
		  /** *Boolean для опеределения регистрации BroadcastReceiver'a*/
		private Boolean MyListenerIsRegistered = false;
		private Boolean OrderListUpdateIsRegistered = false;
		private Boolean notificatedOrderOpenListenerRegistered = false;
		  /** *экземпляр Handler'a для добавления нового заказа в отдельном потоке*/
		private Handler mHandler;
		/** *ViewSwitcher for switching if order list is empty*/
		private ViewSwitcher switcher;
		
		Context context;
		
		/** static fields for GCM service */
		public static final String EXTRA_MESSAGE = "message";
		public static final String PROPERTY_REG_ID = "registration_id";
		private static final String PROPERTY_APP_VERSION = "appVersion";
		private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
		private GoogleCloudMessaging gcm;
		String regid;
		AtomicInteger msgId = new AtomicInteger();
		private static final String TAG="DashboardActivityAlt";
		/**
		 * Substitute you own sender ID here. This is the project number you got
		 * from the API Console, as described in "Getting Started."
		 */
		String SENDER_ID = "7999501650";
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new GestureOverlayView(this);
		newOrderListener = new NewOrderListener();
		orderListUpdateListener= new OrderListUpdate();
		notificatedOrderOpenListener = new NotificatedOrderOpen();
		mHandler = new Handler();
		setContentView(R.layout.dash_alt);
		
		switcher = (ViewSwitcher) findViewById(R.id.dashboardSwitcher);
		
	    dashboardHeader = (LinearLayout) findViewById(R.id.dashboardHeader);
	    faq = new FrequentlyUsedMethods(DashboardActivityAlt.this);
	    dashboardHeader.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	        public void onGlobalLayout() {
	        	layoutHeight = dashboardHeader.getMeasuredHeight();
	        }
	    });
	    context = this;
		listView1 = (ListView)findViewById(R.id.altOrderslist);
		
		listView1.setCacheColorHint(Color.TRANSPARENT);
		listView1.setCacheColorHint(Color.BLACK);
		listView1.setScrollingCacheEnabled(false); 
		
		new FrequentlyUsedMethods(DashboardActivityAlt.this);
		final ViewConfiguration vc = ViewConfiguration.get(this);
		vc.getScaledTouchSlop();
		vc.getScaledMinimumFlingVelocity();
		final float HORIZONTAL_MIN_DISTANCE = 80;//=met.convertPixelsToDp(200,act);
		final float VERTICAL_MIN_DISTANCE =8;//met.convertPixelsToDp(100,act);
		ctx = this;
		paginationGenerate();
	    adapter = new OrderAdapter(DashboardActivityAlt.this,
    			R.layout.dash_alt_item, forPrint);
	    sharedPreferences = getSharedPreferences("user", MODE_PRIVATE );
		editor = sharedPreferences.edit();
		

	    
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
		if (forPrint.isEmpty() & !stopDownload )
		{
			DashboardActivityAlt.page = 1;
			DashboardAsync.execute(DashboardActivityAlt.this, DashboardActivityAlt.this);
			adapter.notifyDataSetChanged();
			LoginActivity.newUser = false;
		}

		
		gestureListener = new View.OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
	            	boolean result = false;
	            	int id =0;
	            	switch (event.getAction()) {
	                case MotionEvent.ACTION_DOWN: {
	                    downX = event.getX();
	                    downY = event.getY();
	                    
	                    mSwipeDetected = Action.None;
	                    result = false; // allow other events like Click to be processed
	                }
	                case MotionEvent.ACTION_UP: {
	                	if (openInteractFlag)
	                        openCurrentOrderInteract();
	                	else if (openInfoFlag)
	                		openCurrentOrderInfo();
	                	else if (!openInteractFlag & openInfoFlag | openInteractFlag&!openInfoFlag)
	                	{
	                		id = listView1.pointToPosition((int) downX, (int) downY);
	                		orderDetectDismiss();
	                	}
	                
	                	result = false; // allow other events like Click to be processed
	                }
	                case MotionEvent.ACTION_MOVE: {
	                    upX = event.getX();
	                    upY = event.getY();
	                    float deltaX = downX - upX;
	                    float deltaY = downY - upY;
	                            if (Math.abs(deltaX) > HORIZONTAL_MIN_DISTANCE) {
	                                // left or right
	                            	Log.i("swipe detect", Float.toString(deltaX));
	                            	Log.i("swipe detect const", Float.toString(HORIZONTAL_MIN_DISTANCE));
	                                if (deltaX < -250) {
	                                	
	                                    mSwipeDetected = Action.LR;
	                                    if (!openInteractFlag)
	                                    {
		                                    id = listView1.pointToPosition((int) upX, (int) upY);
		                                    borderDetLeft =true;
		                                    openInteractFlag = true;
		                                    orderDetect(id);
	                                    }
//	                                 Order temp = (Order) adapter.getItem((id));
//	                   	             Log.i("swiping order", temp.toString());
//	                   	             
//	                   	              View w = listView1.getChildAt(id);
//	                   	              
//	                   	 	    	 w.setBackgroundColor(Color.rgb(66, 174, 255));
//	           	                     listItem = (Order) listView1.getItemAtPosition(position);
//	           	                      ((CustomTextView)v.findViewById(R.id.altOrderTitle)).setTextSize(28);
//	           	                      ((CustomTextView)v.findViewById(R.id.altOrderTitle)).setTextColor(Color.WHITE);
//	           	                      ((CustomTextView)v.findViewById(R.id.altOrderTitle)).setText("Opening...");
//	           	                      
//	           	                     try {
//	           	                    	 messagesExport = listItem.getCusThread().getMessages();
//	           	                 	  }
//	           	                 	  catch (NullPointerException npe) {
//	           	                 		 Toast toast = Toast.makeText(DashboardActivityAlt.this, 
//	           	                    			 "You should to swipe the item properly", Toast.LENGTH_SHORT);
//	           	                    	 toast.show();
//	           	                    	 Intent intent = getIntent();
//	           	                    	 finish();
//	           	                    	 startActivity(intent);
//	           	                 	  }
//	           	                     
//	           	                     Intent i = new Intent(getApplicationContext(),
//	           	                            OrderInfoTabScreen.class);
//	           	                     Bundle mBundle = new Bundle();
//	           	                     mBundle.putString("OrderSwiping", "LR");
//	           	                     i.putExtras(mBundle);
//	           	                     startActivity(i);
	                                    
	                                    return true;
	                                }
	                                if (deltaX > 250) {
	                                    mSwipeDetected = Action.RL;
	                                    if (!openInfoFlag )
	                                    {
		                                    id = listView1.pointToPosition((int) upX, (int) upY);
		                                    borderDetRight =true;
		                                    openInfoFlag = true;
		                                    orderDetect(id);
	                                    }
	                                    	
	                                    result = true;
	                                }
	                            } else 

	                            if (Math.abs(deltaY) > VERTICAL_MIN_DISTANCE) {
	                                // top or down
	                                if (deltaY < 0) {
	                                    mSwipeDetected = Action.TB;
	                                    Log.i(logTag,Boolean.toString(borderDetLeft));
	                                    if (borderDetLeft){
	                                    	id = listView1.pointToPosition((int) upX, (int) upY);
	                                    	orderDetectDismiss();
	                                    	openInteractFlag = false;
	                                    }
	                                    
	                                  //  return false;
	                                    result = false;
	                                }
	                                if (deltaY > 0) {
	                                    if (borderDetRight){
	                                    	id = listView1.pointToPosition((int) upX, (int) upY);
	                                    	orderDetectDismiss();
	                                    	openInfoFlag = false;
	                                    }
	                                    mSwipeDetected = Action.BT;
	                                    if (deltaY > 400)
	    	                            {
//	    	                            	 try {
//	    	                					boolean isOnline = faq.isOnline();
//	    	                					if (isOnline)
//	    	                					{
//	    	                							if (!TaskProgressDialogFragment.dialogIsProceeding)
//	    	                							{
//	    	                								DashboardAsync.execute(DashboardActivityAlt.this, DashboardActivityAlt.this);
//	    	                							    adapter.notifyDataSetChanged();
//	    	                							}
//	    	                							else 
//	    	                							{
//	    	                							}
//	    	                					}
//	    	                					else
//	    	                					{
//	    	                					   Toast mToast =  Toast.makeText(getApplicationContext(), "No network connection. Please try later.", Toast.LENGTH_LONG);
//	    	                			   	  	   mToast.show();
//	    	                					}
//	    	                                 }
//	    	                            	 catch(Exception e)
//	    	                            	 {
//	    	                            		e.printStackTrace(); 
//	    	                            	 }
	    	                            	 return false;
	    	                            }
	                                    //return false;
	                                    result = false;
	                                }
	                            }
	                          
	                  	}
	        }
	           return result;
        }};
        
        listView1.setOnScrollListener(this);
		listView1.setOnItemClickListener(new OnItemClickListener() {
	          public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	          {
	        	  clickDetected = true;
	        	  		orderDetect(position);
	        	  		openCurrentOrderInteract();
	          }
	      });
		  listView1.setOnTouchListener(gestureListener);
		listView1.setOnItemLongClickListener(new OnItemLongClickListener() {
	          public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
	        	 if (  mSwipeDetected == Action.None)
	        	 { 
	        		  
	        		  CharSequence[] items;
	        		  AlertDialog.Builder builder = null;
	   	            if (position<forPrint.size())
			         {
			        	  listItem =  forPrint.get(position);
			        	  Log.i("long click listItem", listItem.toString());
			        	  List<String> listItems = new ArrayList<String>();
		
			        	  listItems.add("Open");
			        	if (listItem.getProcess_status().getProccessStatusId()==10)
			        	{
			        		  listItems.add("Try again");
			        		  listItems.add("Remove order");
			        		  items = listItems.toArray(new CharSequence[listItems.size()]);
					  			builder = new AlertDialog.Builder(DashboardActivityAlt.this);
					  		    builder.setTitle(Integer.toString(forPrint.get(position).getOrderid())+ "  " +forPrint.get(position).getTitle());
					  			builder.setItems(items, new DialogInterface.OnClickListener() {
					  			    public void onClick(DialogInterface dialog, int item) {
					  			    	if (item == 0)
					  			    	{
					  			    		
						                     try {
						                    	 messagesExport = listItem.getMessages();
						                 	  }
						                 	  catch (NullPointerException npe) {
						                 		 Toast toast = Toast.makeText(DashboardActivityAlt.this, 
						                    			getResources().getString(R.string.error_swiping), Toast.LENGTH_SHORT);
						                    	 toast.show();
						                    	 Intent intent = getIntent();
						                    	 finish();
						                    	 startActivity(intent);
						                 	  }
						                     openCurrentOrderInteract();
						                     
					  			    	}
					  			    	else if (item == 1)
					  			    	{
					  			    		
					  			    	}
					  			    	else if (item == 2)
					  			    	{
					  			    		forPrint.remove(forPrint.get(position));
					  			    		adapter.notifyDataSetChanged();
					  			    	}
					  			    	 
					  			    }
					  			});
			        	}
			        	else
			        	{
				        	  if (forPrint.get(position).getProcess_status().getProccessStatusId() !=9 &
				        			  forPrint.get(position).getProcess_status().getProccessStatusId() !=4)
				        		  {
				        		  	listItems.add("Inactivate");
				        		  }
				        	  else 
				        		  listItems.remove("Inactivate");
				        	 items = listItems.toArray(new CharSequence[listItems.size()]);
				  			builder = new AlertDialog.Builder(DashboardActivityAlt.this);
				  		    builder.setTitle(Integer.toString(forPrint.get(position).getOrderid())+ "  " +forPrint.get(position).getTitle());
				  			builder.setItems(items, new DialogInterface.OnClickListener() {
				  			    public void onClick(DialogInterface dialog, int item) {
				  			    	if (item == 0)
				  			    	{
				  			    		
					                     try {
					                    	 messagesExport = listItem.getMessages();
					                 	  }
					                 	  catch (NullPointerException npe) {
					                 		 Toast toast = Toast.makeText(DashboardActivityAlt.this, 
					                 				getResources().getString(R.string.error_swiping), Toast.LENGTH_SHORT);
					                    	 toast.show();
					                    	 Intent intent = getIntent();
					                    	 finish();
					                    	 startActivity(intent);
					                 	  }
					                    
					                     openCurrentOrderInteract();
					                     
				  			    	}
				  			    	else if (item == 1)
				  			    	{
				  			    		
				  			    		
				  			    		faq.inactivateOrder();
				  			    		
				  			    	}
				  			    	 
				  			    }
				  			});
			        	}
			  			AlertDialog alert = builder.create();
			  			alert.show();
			        	  }
	        		 
	        	 }
	        	 mSwipeDetected = Action.None;
	        	  return true;
	         
	          }
	      });
		
		// Check device for Play Services APK. If check succeeds, proceed with
				// GCM registration.
				if (checkPlayServices()) {
					gcm = GoogleCloudMessaging.getInstance(this);
					regid = getRegistrationId(context);

					if (regid.isEmpty()) {
						registerInBackground();
					}
					new SendRegistrationIdTask(getRegistrationId(this)).execute();
				} else {
					
				}

	}
	/** *метод для определения элемента, который был выбран*/
	public void myOnItemActionUp(View view, int pos)
    {
		 	clickDetected = true;
	  		orderDetect(pos);
	  		openCurrentOrderInteract();
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

	    
	    /** *BroadcastReceiver for posting blank order with status "Uploading" and sending order to the server*/
	    protected class NewOrderListener extends BroadcastReceiver {

	        @Override
	        public void onReceive(Context context, Intent intent) {
	            // No need to check for the action unless the listener will
	            // will handle more than one - let's do it anyway
	            if (intent.getAction().equals(Constants.NEW_ORDER)) {
	            	Log.i("DashboardActAlt NewOrderLister", "I've got new order");
	            	switcher.setDisplayedChild(0);
	            	Order new2 = new Order(Constants.ORDER_NEW_UPLOADING);
	            	if (OrderPreferences.getInstance().getOrderPrefItem(11).toString().equalsIgnoreCase("assignment"))
	            	{
	            		new2.setProduct(new Product(1,"assignment",new ProductAssignment(OrderPreferences.getInstance().getOrderPrefItem(7).toString(),  OrderPreferences.getInstance().getArrayList()[9].toString(), 0, null, false, false)));
	            	}
	            	else if (OrderPreferences.getInstance().getOrderPrefItem(11).toString().equalsIgnoreCase("essay"))
	            	{

				            		//	            		new2.setProduct(new Product(1,"writing",new ProductWriting(OrderPreferences.getInstance().getOrderPrefItem(7).toString(),OrderPreferences.getInstance().getArrayList()[9].toString(),
			//	            				true,OrderPreferences.getInstance().getArrayList()[3].toString(), OrderPreferences.getInstance().getArrayList()[4].toString(),
			//	            				  OrderPreferences.getInstance().getArrayList()[5].toString(), OrderPreferences.getInstance().getArrayList()[6].toString())));
	            		
	            	}
	            	
	            	new2.setIsActive("active");
	            	
	            	ProcessStatus a  = new ProcessStatus();
	            	a.setProccessStatusId(10);
	            	a.setProccessStatusTitle("Uploading");
	            	a.setProccessStatusIdent(null);
	            	new2.setTitle(OrderPreferences.getInstance().getOrderPrefItem(7).toString());
	            	new2.setProcess_status(a);
	            	addMethod(new2);
	            	adapter.notifyDataSetChanged();
	            	
	            	if ( OrderPreferences.getInstance().getOrderPrefItem(11).toString().equalsIgnoreCase("assignment"))
	            		new SendAssignment().execute();
	            	else if (OrderPreferences.getInstance().getOrderPrefItem(11).toString().equalsIgnoreCase("essay"))
	            		new SendEssay().execute();
	            }
	        }
	    }
	    /** *BroadcastReceiver for list updating if changes from GCM received*/
	    protected class OrderListUpdate extends BroadcastReceiver {

	        @Override
	        public void onReceive(Context context, Intent intent) {
	            
	            if (intent.getAction().equals(Constants.ORDER_LIST_UPDATE)) {
	            	try{
	                if (intent.getExtras().getString("action").equals("order_payed"))
	            	{
	            	int i = Integer.parseInt(intent.getExtras().getString("order_id"));
	            	Order a= new Order(i);
	            	DatabaseHandler db = new DatabaseHandler(context.getApplicationContext());
	    			Dao<ProcessStatus, Integer> daoProcess;
	    			daoProcess = db.getStatusDao();
	            	 adapter.getItem(adapter.getPosition(a)).setProcess_status((daoProcess.queryForId(4)));
	            	 adapter.notifyDataSetChanged();
	            	//updateList();
	            	//updateUI(intent);
	            	}
	            	else if (intent.getExtras().getString("action").equals("order_changed"))
	            	{
	            		int i= Integer.parseInt(intent.getExtras().getString("order_changed"));
	            		new DownloadGCMOrder().execute(i);
	            		
	            	}
	            		adapter.notifyDataSetChanged();
	            	}
	            	catch(Exception e)
	            	{e.printStackTrace();}
	            }
	        }
	    }
	    /** *BroadcastReceiver opening updated order from notification*/
	    protected class NotificatedOrderOpen extends BroadcastReceiver {

	        @Override
	        public void onReceive(Context context, Intent intent) {
	            
	            	try{
	                if (intent.getExtras().getInt(Constants.ORDER_NOTIFICATED_UPDATE)!=0)
	            	{
	                	Log.i("NotificatedOrderOpen", Integer.toString(intent.getExtras().getInt(Constants.ORDER_NOTIFICATED_UPDATE)));
	                	int i = intent.getExtras().getInt(Constants.ORDER_NOTIFICATED_UPDATE);
	            		Order a= new Order(i);
	            		listItem = adapter.getItem(adapter.getPosition(a));
	            		messagesExport = listItem.getMessages();
	            		Intent intent2 = new Intent(getApplicationContext(),
	      	                  OrderInfoTabScreen.class);
	            		 Bundle mBundle = new Bundle();
	                     mBundle.putString("OrderSwiping", "lr");
	                     intent2.putExtras(mBundle);
	                   
	            		startActivity(intent2);
	            	}
	            	}
	                catch(Exception e)
	            	{e.printStackTrace();}
	        }
	    }
	    /** *Runnable для добавления нового заказа в список*/
	    private class sendUpdatings implements Runnable {
		    Order r;

		    public sendUpdatings(Order r) {
		        this.r = r;
		    }

		    public void run(){
		         DashboardActivityAlt.forPrint.add(0,r);
		    }
		}
	    /** *метод для добавления нового заказа в список*/
	    private void addMethod(Order r)
	 	{
	 		mHandler.post(new sendUpdatings(r));
	 	}
	   
	    @Override
		public void onResume() {
			super.onResume();	
			 openInteractFlag = false;
			 openInfoFlag = false;
			 clickDetected = false;
			listView1.setAdapter(adapter);
			if (savedPosition >= 0) {
			      listView1.setSelectionFromTop(savedPosition, savedListTop);
			    }
//			startService(intent);
//			registerReceiver(broadcastReceiver, new IntentFilter(ServiceIntentMessages.ORDERS_IMPORT));
			 if (!MyListenerIsRegistered) {
		            registerReceiver(newOrderListener, new IntentFilter(Constants.NEW_ORDER));
		            MyListenerIsRegistered = true;
		        }
			 if (!OrderListUpdateIsRegistered)
			 {
				  registerReceiver(orderListUpdateListener, new IntentFilter(Constants.ORDER_LIST_UPDATE));
				  OrderListUpdateIsRegistered = true;
			 }
			 if (!notificatedOrderOpenListenerRegistered)
			 {
				 registerReceiver(notificatedOrderOpenListener, new IntentFilter(Constants.ORDER_NOTIFICATED_UPDATE)); 
				 notificatedOrderOpenListenerRegistered=true;
			 }
		   checkPlayServices();
		}
	    
	    
	    @Override
		public void onRestart() {
			super.onRestart();	
		}
	    
		@Override
		public void onPause() {
			super.onPause();
		   savedPosition = listView1.getFirstVisiblePosition();
		   View firstVisibleView = listView1.getChildAt(0);
		   savedListTop = (firstVisibleView == null) ? 0 : firstVisibleView.getTop();
		   editor.putInt("savedPosition", savedPosition);
		   editor.putInt("savedListTop", savedListTop);
		   editor.commit();
		   if (MyListenerIsRegistered) {
	            unregisterReceiver(newOrderListener);
	            MyListenerIsRegistered = false;
	        }
		   if (OrderListUpdateIsRegistered) {
	            unregisterReceiver(orderListUpdateListener);
	            OrderListUpdateIsRegistered = false;
	        }
		   if (notificatedOrderOpenListenerRegistered) {
	            unregisterReceiver(notificatedOrderOpenListener);
	            notificatedOrderOpenListenerRegistered = false;
	        }
		}	
		 /** *сохранение текущей позиции listview для воспроизведения, если пользователь вернется на данную активность*/
	    @Override
	    public void onSaveInstanceState(Bundle savedInstanceState) {
	      super.onSaveInstanceState(savedInstanceState);
	       savedPosition = listView1.getFirstVisiblePosition();
	      state = listView1.onSaveInstanceState();
		  View firstVisibleView = listView1.getChildAt(0);
		  savedListTop = (firstVisibleView == null) ? 0 : firstVisibleView.getTop();
	      savedInstanceState.putInt("savedPosition", savedPosition);
	      savedInstanceState.putInt("savedListTop", savedListTop);
	    }
	    /** *воспроизведение сохраненной позиции listview при возврате пользователя*/
	    @Override
	    public void onRestoreInstanceState(Bundle savedInstanceState) {
	      super.onRestoreInstanceState(savedInstanceState);
	      
	      int savedPosition = savedInstanceState.getInt("savedPosition");
	      int savedListTop=  savedInstanceState.getInt("savedListTop");
	      if (savedPosition >= 0) {
		      listView1.setSelectionFromTop(savedPosition, savedListTop);
		    }
	    }
	    
	    @Override
	    public void onBackPressed() {
	    	
	        // Do Here what ever you want do on back press;
	    }
	    /** *метод для обработки результатов успешной работы DashboardAsync*/
		public void onLoadFinished(Object data) {
			Log.i("dashboardActivityAlt", "onLoadFinished mtd");
			if (data instanceof String)
			{
				if(((String)data).equalsIgnoreCase("success"))
						{
							if (DashboardActivityAlt.page == 2)
							{	
			        		listView1.setAdapter(adapter);
							}
							adapter.notifyDataSetChanged();
						}
				else if(((String)data).equalsIgnoreCase("empty") )
					{
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_order_list_empty), Toast.LENGTH_LONG).show();
					if (forPrint.isEmpty())
						switcher.showNext();  
					
					}
				else if(((String)data).equalsIgnoreCase("error"))
				{}
				else if(((String)data).equalsIgnoreCase("inactivate_success"))
					{
						adapter.notifyDataSetChanged();
						
					}
			}
		}
		/** *метод для обработки результатов неуспешной работы DashboardAsync или если её работа была прекращена*/
		public void onCancelLoad() {
			Log.i("dashboardActivityAlt", "onCancelLoad mtd");
			 Toast.makeText(DashboardActivityAlt.this, getResources().getString(R.string.error_server_problem), Toast.LENGTH_LONG).show();
		}
		/** *метод возвращающий список заказов*/
	    public ArrayList<Order> getOrderList()
	    {
	    	return forPrint;
	    }
	    /** *метод обновления списка заказов*/
	     public void updateList()
	     {
	    	 adapter.notifyDataSetChanged();
	     }
	     /** *метод для подсвечивания выбранного заказа*/
	     private void orderDetect(int id)
	     {
	    	 try{
	             Order temp = (Order) adapter.getItem((id));
	             
	             View w = adapter.getMapedView(temp);
	              w.setBackgroundColor(Color.rgb(50, 107, 161));
	              setSwipedId(w);
	              
	              
	             listItem = (Order) adapter.getItem((id));
	              
	             ((CustomTextView) w.findViewById(R.id.altOrderTitle)).setTextSize(32);
	             ((CustomTextView) w.findViewById(R.id.altOrderTitle)).setText("Opening...");
	             ((CustomTextView) w.findViewById(R.id.altOrderTitle)).setPadding(0, 1, 0, 0);
	             ((CustomTextView) w.findViewById(R.id.altOrderTitle)).setTextColor(Color.WHITE);
	             ((CustomTextView) w.findViewById(R.id.altOrderStatus)).setTextColor(Color.WHITE);
	             ((TextView) w.findViewById(R.id.menu_sep1)).setBackgroundColor(Color.WHITE);
	             ((TextView) w.findViewById(R.id.menu_sep2)).setVisibility(View.GONE);
	             ((CustomTextView) w.findViewById(R.id.altOrderPrice)).setVisibility(View.GONE);

	             messagesExport = listItem.getMessages();
	    	
	    	 }
	            catch(Exception e)
	            {
	            	e.printStackTrace();
	            	 Toast toast = Toast.makeText(DashboardActivityAlt.this, 
		           			 "You should to swipe the item properly", Toast.LENGTH_SHORT);
		           	 toast.show();
	            }
	     }
	     /** *метод для прекращения подсвечивания выбранного заказа*/
	     private void orderDetectDismiss()
	     {
	    	 try{
	    		 Log.i("dashboard", "orderDetDismiss");
	             View w = getSwipedId();//adapter.getView(getSwipedId());
	 	    	 w.setBackgroundColor(Color.rgb(0, 0, 0));
	             ((CustomTextView) w.findViewById(R.id.altOrderTitle)).setTextSize(17);
	             ((CustomTextView) w.findViewById(R.id.altOrderTitle)).setTextColor(Color.WHITE);
	             ((CustomTextView) w.findViewById(R.id.altOrderStatus)).setTextColor(adapter.getStatusColor(listItem));
	             ((TextView) w.findViewById(R.id.menu_sep2)).setVisibility(View.VISIBLE);
	             ((TextView) w.findViewById(R.id.menu_sep1)).setBackgroundColor(Color.rgb(86, 86, 86));
	             ((CustomTextView) w.findViewById(R.id.altOrderPrice)).setVisibility(View.VISIBLE);
	             ((CustomTextView) w.findViewById(R.id.altOrderTitle)).setText(listItem.getTitle());
	             adapter.notifyDataSetChanged();
	             
	    	 }
	            catch(Exception e)
	            {
	            	e.printStackTrace();
	            }
	     }
	     /** *метод открытия списка сообщений выбранного заказа*/
	    private void openCurrentOrderInteract()
	    {
	    	
           Intent i = new Intent(getApplicationContext(),
                  OrderInfoTabScreen.class);
           Bundle mBundle = new Bundle();
           mBundle.putString("OrderSwiping", "LR");
           i.putExtras(mBundle);
           startActivity(i);
           
	    }
	    /** *метод открытия информации выбранного заказа*/
	    private void openCurrentOrderInfo()
	    {
	    	
	           Intent i = new Intent(getApplicationContext(),
	                  OrderInfoTabScreen.class);
	           Bundle mBundle = new Bundle();
	           mBundle.putString("OrderSwiping", "RL");
	           i.putExtras(mBundle);
	           startActivity(i);
          
	    }
	    /** *getter для определения View для выбранного заказа*/
	    private View getSwipedId()
	    {
	    	return this.orderSwipedView;
	    }
	    /** *setter для определения View для выбранного заказа*/
	    private void setSwipedId(View id)
	    {
	    	 this.orderSwipedView = id;
	    }
	    private class DownloadNewOrder extends AsyncTask<Integer, Void, Order >
	    {
	    	Order added = null;
	    	int id;
			@Override
			protected Order doInBackground(Integer... params) {
				added = userFunc.getOrder(params[0]);
				added = faq.updateOrderFields(added);
				return added;
					
			}
			 protected void onPostExecute(Order added) {
			        if (added != null)
			        {
//			        
			        	Order a =new Order(Constants.ORDER_NEW_UPLOADING);
//			        	int row = listView1.getFirstVisiblePosition();
//			        	adapter.getItem(row).setOrderid(added.getOrderid());
			        	forPrint.set(forPrint.indexOf(a), added);
			        	
			        
//			        	Log.i("order ", adapter.getItem(row).toString());
			        	adapter.notifyDataSetInvalidated();
//			        	 ((TextView)row.findViewById(R.id.altOrderId)).setText(Integer.toString(added.getOrderid()));
			        	Log.i("order id", Integer.toString(forPrint.get(forPrint.indexOf(added)).getOrderid()));
			        	Log.i("order id", Integer.toString(forPrint.get(0).getOrderid()));
			        
			        }
			        else 
			        {
			        }
			        adapter.notifyDataSetChanged();
			    }
	    }
	    private class DownloadGCMOrder extends AsyncTask<Integer, Void, Order >
	    {
	    	Order added = null;
			@Override
			protected Order doInBackground(Integer... params) {
				added = userFunc.getOrder(params[0]);
				if (forPrint.contains(added))
					return added;
				else 
					{
						this.cancel(true);
						return null;
					}
			}
			 protected void onPostExecute(Order added) {
			        if (added != null)
			        {
			        	faq.updateOrderFields(added);
			        	forPrint.set(forPrint.indexOf(added), added);
			        }
			        else 
			        {
			        	
			        }
			        adapter.notifyDataSetChanged();
			    }
	    }
	    
	    
	    
	    /** *AsyncTask для посылки нового заказа, тип - Assignment*/
	    private class SendAssignment extends AsyncTask<Void, Void, JSONObject > {
		    
	    	JSONObject response ;
	    	protected void onPreExecute() {
	        }

	        protected JSONObject doInBackground(Void... args) {
            try {
 					response = userFunc.sendAssignment(OrderPreferences.getInstance().getOrderPrefItem(7).toString(),
 							 OrderPreferences.getInstance().getOrderPrefItem(1).toString(),
 							OrderPreferences.getInstance().getOrderPrefItem(2).toString(),
 							OrderPreferences.getInstance().getOrderPrefItem(8).toString(),   OrderPreferences.getInstance().getOrderPrefItem(9).toString(),
 							OrderPreferences.getInstance().getOrderPrefItem(12).toString(),
 							"",
 							OrderPreferences.getInstance().getOrderPrefItem(10).toString(),
 							FileManagerActivity.getFinalAttachFiles(),  OrderPreferences.getInstance().getOrderPrefItem(13).toString()
 							);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	       		
	       	return response;
	     }

	        protected void onPostExecute(JSONObject  json) {
	 	  	   
	        	try {
					if (Boolean.parseBoolean(json.getString(Constants.KEY_STATUS)))
					{
						int id =Integer.parseInt(json.getString(Constants.ORDER_DATA));
						FileManagerActivity.getFinalAttachFiles().clear();
						new DownloadNewOrder().execute(id);
					}
					else 
						FileManagerActivity.getFinalAttachFiles().clear();
							adapter.notifyDataSetChanged();
					
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {

					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	   }
	    /** *AsyncTask для посылки нового заказа, тип - Essay*/
	    private class SendEssay extends AsyncTask<Void, Void, JSONObject> {
	    			JSONObject response = null;
	    			protected void onPreExecute() {

	    			}
	    			protected JSONObject doInBackground(Void... args) {
	    				try {
	    	
	    					response = userFunc.sendWriting(OrderPreferences.getInstance().getOrderPrefItem(7).toString(),
	    							OrderPreferences.getInstance().getOrderPrefItem(0).toString()
	    								, OrderPreferences.getInstance().getOrderPrefItem(2).toString(), OrderPreferences.getInstance().getOrderPrefItem(8).toString()
	    											, OrderPreferences.getInstance().getOrderPrefItem(9).toString(),
	    							"1", "", OrderPreferences.getInstance().getOrderPrefItem(10).toString(),
	    							FileManagerActivity.getFinalAttachFiles(),OrderPreferences.getInstance().getOrderPrefItem(3).toString(), 
	    							OrderPreferences.getInstance().getOrderPrefItem(4).toString(), OrderPreferences.getInstance().getOrderPrefItem(5).toString(),
	    							OrderPreferences.getInstance().getOrderPrefItem(6).toString());
	    				} catch (Exception e) {
	    	
	    					e.printStackTrace();
	    				}
	    	
	    				return response;
	    			}
	    	
	    			protected void onPostExecute(JSONObject json) {
	    				
	    				try {
	    					Log.i("json after order posting", json.toString());
							if (json.getString(Constants.KEY_STATUS) != null)
							{
								
								if (Boolean.parseBoolean(json.getString(Constants.KEY_STATUS)))
								{
									int id =Integer.parseInt(json.getString(Constants.ORDER_DATA));
									FileManagerActivity.getFinalAttachFiles().clear();
									new DownloadNewOrder().execute(id);
								}
	  							else 
	  							{
	  								FileManagerActivity.getFinalAttachFiles().clear();
	  								adapter.notifyDataSetChanged();
	  								
	  							}
							
							}
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    	
	    	
	    			}
	    	
	    			
	    }
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			
		}
		/** *метод для загрузки новых заказов текущего пользователя.*/
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
	            // Sample calculation to determine if the last 
	            // item is fully visible.
	            final int lastItem = firstVisibleItem + visibleItemCount;
	            if(lastItem == totalItemCount) {
				          	 try {
								boolean isOnline = faq.isOnline();
								if (isOnline)
								{
										if (!TaskProgressDialogFragment.dialogIsProceeding)
										{
											// for hasNext order checking
											if (SharedPrefs.getInstance().getSharedPrefs().getBoolean(Constants.ORDER_HAS_NEXT, false)==false)
											{
											    DashboardAsync.execute(DashboardActivityAlt.this, DashboardActivityAlt.this);
												adapter.notifyDataSetChanged();
											}
											else 
											{
												
											}
										}
										else 
										{
										}
								}
								else
								{
								}
				            }
				       	 catch(Exception e)
				       	 {
				       		e.printStackTrace(); 
				       	 }
	            }
			
		}  
		// genarating perpage items corresponding to user's screen height
		public void paginationGenerate()
		{
			DisplayMetrics displaymetrics = new DisplayMetrics();
		    getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		    int height = (int)dpFromPx(displaymetrics.heightPixels);
		    height = height - (int)dpFromPx(90);
		    final int count =(int)dpFromPx(height)/(int)dpFromPx(50);
		    perpage = count;
		}
		
		 /**
	       *
	       *
	       *GCM service methods
	       *
	       *
	       *
	 
	 
	  */
	 
	 /**
		 * Check the device to make sure it has the Google Play Services APK. If it
		 * doesn't, display a dialog that allows users to download the APK from the
		 * Google Play Store or enable it in the device's system settings.
		 */
		private boolean checkPlayServices() {
			int resultCode = GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(this);
			if (resultCode != ConnectionResult.SUCCESS) {
				if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
					GooglePlayServicesUtil.getErrorDialog(resultCode, this,
							PLAY_SERVICES_RESOLUTION_REQUEST).show();
				} else {
					Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_device_support), Toast.LENGTH_SHORT).show();
				}
				return false;
			}
			return true;
		}

		/**
		 * Stores the registration ID and the app versionCode in the application's
		 * {@code SharedPreferences}.
		 * 
		 * @param context
		 *            application's context.
		 * @param regId
		 *            registration ID
		 */
		private void storeRegistrationId(Context context, String regId) {
			final SharedPreferences prefs = getGcmPreferences(context);
			int appVersion = getAppVersion(context);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(PROPERTY_REG_ID, regId);
			editor.putInt(PROPERTY_APP_VERSION, appVersion);
			editor.commit();
		}

		/**
		 * Gets the current registration ID for application on GCM service, if there
		 * is one.
		 * <p>
		 * If result is empty, the app needs to register.
		 * 
		 * @return registration ID, or empty string if there is no existing
		 *         registration ID.
		 */
		private String getRegistrationId(Context context) {
			final SharedPreferences prefs = getGcmPreferences(context);
			String registrationId = prefs.getString(PROPERTY_REG_ID, "");
			if (registrationId.isEmpty()) {
				return "";
			}
			// Check if app was updated; if so, it must clear the registration ID
			// since the existing regID is not guaranteed to work with the new
			// app version.
			int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
					Integer.MIN_VALUE);

			int currentVersion = getAppVersion(context);

			if (registeredVersion != currentVersion) {
				return "";
			}
			return registrationId;
		}

		/**
		 * Registers the application with GCM servers asynchronously.
		 * <p>
		 * Stores the registration ID and the app versionCode in the application's
		 * shared preferences.
		 */

		private void registerInBackground() {
			new AsyncTask<Void, Void, String>() {
				@Override
				protected String doInBackground(Void... params) {
					String msg = "";
					try {
						if (gcm == null) {
							gcm = GoogleCloudMessaging.getInstance(context);
						}
						regid = gcm.register(SENDER_ID);
						msg = "Device registered, registration ID=" + regid;

						// You should send the registration ID to your server over
						// HTTP, so it
						// can use GCM/HTTP or CCS to send messages to your app.
						sendRegistrationIdToBackend();

						// For this demo: we don't need to send it because the
						// device will send
						// upstream messages to a server that echo back the message
						// using the
						// 'from' address in the message.

						// Persist the regID - no need to register again.
						storeRegistrationId(context, regid);
					} catch (IOException ex) {
						msg = "Error :" + ex.getMessage();
						// If there is an error, don't just keep trying to register.
						// Require the user to click a button again, or perform
						// exponential back-off.
					}
					return msg;
				}

				@Override
				protected void onPostExecute(String msg) {
				}
			}.execute(null, null, null);
		}

//		// Send an upstream message.
//		public void onClick(final View view) {
//
//			if (view == findViewById(R.id.send)) {
//				new AsyncTask<Void, Void, String>() {
//					@Override
//					protected String doInBackground(Void... params) {
//						String msg = "";
//						try {
//							Bundle data = new Bundle();
//							data.putString("my_message", "Hello World");
//							data.putString("my_action",
//									"com.google.android.gcm.demo.app.ECHO_NOW");
//							String id = Integer.toString(msgId.incrementAndGet());
//							gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
//							msg = "Sent message";
//						} catch (IOException ex) {
//							msg = "Error :" + ex.getMessage();
//						}
//						return msg;
//					}
//
//					@Override
//					protected void onPostExecute(String msg) {
//					}
//				}.execute(null, null, null);
//			} else if (view == findViewById(R.id.clear)) {
//				storeRegistrationId(context, "");
//			} else if (view == findViewById(R.id.send_stored)) {
//				sendRegistrationIdToBackend();
//			}
//			
//			else if (view == findViewById(R.id.send_id)) {
//				new SendRegistrationIdTask(getRegistrationId(this)).execute();
//			}
//
//		}

		@Override
		protected void onDestroy() {
			super.onDestroy();
		}

		/**
		 * @return Application's version code from the {@code PackageManager}.
		 */
		private static int getAppVersion(Context context) {
			try {
				PackageInfo packageInfo = context.getPackageManager()
						.getPackageInfo(context.getPackageName(), 0);
				return packageInfo.versionCode;
			} catch (NameNotFoundException e) {
				// should never happen
				throw new RuntimeException("Could not get package name: " + e);
			}
		}

		/**
		 * @return Application's {@code SharedPreferences}.
		 */
		private SharedPreferences getGcmPreferences(Context context) {
			// This sample app persists the registration ID in shared preferences,
			// but
			// how you store the regID in your app is up to you.
			return getSharedPreferences(DemoActivity.class.getSimpleName(),
					Context.MODE_PRIVATE);
		}

		/**
		 * Sends the registration ID to your server over HTTP, so it can use
		 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
		 * since the device sends upstream messages to a server that echoes back the
		 * message using the 'from' address in the message.
		 */
		private void sendRegistrationIdToBackend() {
			if (getRegistrationId(this) != "") {
				Log.i("success", getRegistrationId(this));
			} else
				Log.i("success", "fuck it");
		}
		
		
		  private final class SendRegistrationIdTask extends  AsyncTask<String, Void, JSONObject> {
				    private String mRegId;
				
				    public SendRegistrationIdTask(String regId) {
				      mRegId = regId;
				    }
				    JSONObject json;
				    @Override
				    protected JSONObject doInBackground(String... regIds) {
				      String url = Constants.prodHost + "/users/register_gcm_device";
				
				      try {
				        Log.i("send REg id", mRegId);
				        List<NameValuePair> params = new ArrayList<NameValuePair>();
				        params.add(new BasicNameValuePair("registrationId", mRegId));
				    	 JSONParser parser = new JSONParser();
				    	 json= parser.getJSONFromUrl(url, params, RequestMethod.POST);
				      } catch (ClientProtocolException e) {
				        Log.e(Constants.SENDREGID, e.getMessage(), e);
				      } catch (IOException e) {
				        Log.e(Constants.SENDREGID, e.getMessage(), e);
				      } catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				      return json;
				    }
				
				    @Override
				    protected void onPostExecute(JSONObject response) {
				      if (response == null) {
				        Log.e(Constants.SENDREGID, "HttpResponse is null");
				        return;
				      }
				
				        return;
				      }
				
				    }
}
