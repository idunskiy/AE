package com.assignmentexpert;

import java.util.ArrayList;
import java.util.List;
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

import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.asynctasks.DashboardAsync;
import com.customitems.CustomTextView;
import com.datamodel.Messages;
import com.datamodel.Order;
import com.datamodel.ProcessStatus;
import com.datamodel.Product;
import com.datamodel.ProductAssignment;
import com.library.Constants;
import com.library.DatabaseHandler;
import com.library.FrequentlyUsedMethods;
import com.library.OrderAdapter;
import com.library.ServiceIntentMessages;
import com.library.UserFunctions;
import com.library.singletones.OrderPreferences;
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
	 UserFunctions launch = new UserFunctions();
	 
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
		  /** *Boolean для опеределения регистрации BroadcastReceiver'a*/
		private Boolean MyListenerIsRegistered = false;
		private Boolean OrderListUpdateIsRegistered = false;
		  /** *экземпляр Handler'a для добавления нового заказа в отдельном потоке*/
		private Handler mHandler;
		/** *ViewSwitcher для переключения крутящегося диалога в случае загразки нового заказа и цены, если он был загружен*/
		private ViewSwitcher switcher;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new GestureOverlayView(this);
		newOrderListener = new NewOrderListener();
		orderListUpdateListener= new OrderListUpdate();
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
	    intent = new Intent(DashboardActivityAlt.this, ServiceIntentMessages.class);
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
		if (forPrint.isEmpty() & !stopDownload )
		{
			
			DashboardActivityAlt.page = 1;
			DashboardAsync.execute(DashboardActivityAlt.this, DashboardActivityAlt.this);
			adapter.notifyDataSetChanged();
			LoginActivity.newUser = false;
		}
		else if (LoginActivity.newUser)
		{
			Log.i("new user","dashboard");
			Log.i("if new user in new user", Boolean.toString(LoginActivity.newUser));
			forPrint.clear();
			stopDownload = false;
			DashboardActivityAlt.page = 1;
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
		
        gestureListener = new View.OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
	            	boolean result = false;
	            	int id =0;
//	            	if (gestureDetector.onTouchEvent(event)){
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
	                                	
	                                    Log.i(logTag, "Swipe Left to Right");
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
	                                    Log.i(logTag, "Swipe Right to Left");
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
	                                    Log.i(logTag, "Swipe Top to Bottom");
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
	                                    Log.i(logTag, "Swipe Bottom to Top");
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
				        	  if (DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId() == 2 | 
				        			  DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId() == 3 | 
					        			 DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId() == 4 | 
					        			  DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId() == 5)
				        		  listItems.add("Inactivate");
				        	 items = listItems.toArray(new CharSequence[listItems.size()]);
				  			builder = new AlertDialog.Builder(DashboardActivityAlt.this);
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
	        	  return false;
	         
	          }
	      });

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

		  private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	        	updateUI(intent);      
	        }
	    };
	    /** *BroadcastReceiver для получения нового заказа*/
	    protected class NewOrderListener extends BroadcastReceiver {

	        @Override
	        public void onReceive(Context context, Intent intent) {
	            // No need to check for the action unless the listener will
	            // will handle more than one - let's do it anyway
	            if (intent.getAction().equals(Constants.NEW_ORDER)) {
	            	switcher.setDisplayedChild(0);
	            	Log.i("newOrderList", "might be added");
	            	Log.i("dashboardActivityAlt onReceive",Integer.toString(forPrint.size()));
	            	Order new2 = new Order();
	            	if (OrderPreferences.getInstance().getOrderPrefItem(11).toString().equalsIgnoreCase("assignment"))
	            	{
	            		new2.setProduct(new Product(1,"assignment",new ProductAssignment(OrderPreferences.getInstance().getOrderPrefItem(7).toString(),  OrderPreferences.getInstance().getArrayList()[9].toString(), false, null, false, false)));
	            	}
	            	else if (OrderPreferences.getInstance().getOrderPrefItem(11).toString().equalsIgnoreCase("writing"))
	            	{
//	            		new2.setProduct(new Product(1,"writing",new ProductWriting(OrderPreferences.getInstance().getOrderPrefItem(7).toString(),OrderPreferences.getInstance().getArrayList()[9].toString(),
//	            				true,OrderPreferences.getInstance().getArrayList()[3].toString(), OrderPreferences.getInstance().getArrayList()[4].toString(),
//	            				  OrderPreferences.getInstance().getArrayList()[5].toString(), OrderPreferences.getInstance().getArrayList()[6].toString())));
	            		
	            	}
	            	
	            	new2.setIsActive(true);
	            	try{
	            	if ((forPrint.get(0).getOrderid()+1)!=0)
	            		new2.setOrderid(forPrint.get(0).getOrderid()+1);
	            	}
	            	catch(Exception e)
	            	{
	            		e.printStackTrace();
	            		new2.setOrderid(1);
	            	}
	            	
	            	ProcessStatus a  = new ProcessStatus();
	            	a.setProccessStatusId(10);
	            	a.setProccessStatusTitle("Uploading");
	            	a.setProccessStatusIdent(null);
	            	new2.setProcess_status(a);
	            	addMethod(new2);
	            	adapter.notifyDataSetChanged();
//	            	OrderPreferences.getInstance().getOrderPrefItem(7).toString(),
//					 OrderPreferences.getInstance().getOrderPrefItem(1).toString(),
//					OrderPreferences.getInstance().getOrderPrefItem(2).toString(),
//					OrderPreferences.getInstance().getOrderPrefItem(8).toString(),   OrderPreferences.getInstance().getOrderPrefItem(9).toString(),"1", "",
//					OrderPreferences.getInstance().getOrderPrefItem(10).toString()
	            	
	            	if ( OrderPreferences.getInstance().getOrderPrefItem(11).toString().equalsIgnoreCase("assignment"))
	            		new SendAssignment().execute();
	            	else if (OrderPreferences.getInstance().getOrderPrefItem(11).toString().equalsIgnoreCase("writing"))
	            		new SendEssay().execute();
	            }
	        }
	    }
	    /** *BroadcastReceiver для update'a списка заказов*/
	    protected class OrderListUpdate extends BroadcastReceiver {

	        @Override
	        public void onReceive(Context context, Intent intent) {
	            
	            if (intent.getAction().equals(Constants.ORDER_LIST_UPDATE)) {
	            	//updateList();
	            	updateUI(intent);
	            }
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
			startService(intent);
			registerReceiver(broadcastReceiver, new IntentFilter(ServiceIntentMessages.ORDERS_IMPORT));
			 if (!MyListenerIsRegistered) {
		            registerReceiver(newOrderListener, new IntentFilter(Constants.NEW_ORDER));
		            MyListenerIsRegistered = true;
		        }
			 if (!OrderListUpdateIsRegistered)
			 {
				  registerReceiver(orderListUpdateListener, new IntentFilter(Constants.ORDER_LIST_UPDATE));
				  OrderListUpdateIsRegistered = true;
			 }
		}
	    
	    
	    @Override
		public void onRestart() {
			super.onRestart();	
		}
	    
		@Override
		public void onPause() {
			super.onPause();
			unregisterReceiver(broadcastReceiver);
		   stopService(intent);
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
		}	
		  /** *метод для обновления UI. вызывается в случае получения уведомления от OrderListUpdate*/
		private void updateUI(Intent intent) {
	    	try 
			{	
	    		DashboardActivityAlt.this.runOnUiThread(new Runnable ()
	    		{
	    			
					public void run() {
						ServiceIntentMessages b = new ServiceIntentMessages();
						for (Order d : forPrint)
						{
							if (d.getProcess_status().getProccessStatusTitle().equalsIgnoreCase("uploading"))
							{
								forPrint.remove(d);
							}
						}
						forPrint = b.getServiceList();
						 adapter.notifyDataSetChanged();
					}
	    		});
	    		 adapter.notifyDataSetChanged();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
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
					Toast.makeText(getApplicationContext(), "Order list is empty", Toast.LENGTH_LONG).show();
					if (forPrint.isEmpty())
						switcher.showNext();  
					
					}
				else if(((String)data).equalsIgnoreCase("error"))
					Log.i("dashboardActivityAlt", "error");
				else if(((String)data).equalsIgnoreCase("inactivate_success"))
					{
						Log.i("dashboardActivityAlt", "order inactivation");
						adapter.notifyDataSetChanged();
						
					}
			}
		}
		/** *метод для обработки результатов неуспешной работы DashboardAsync или если её работа была прекращена*/
		public void onCancelLoad() {
			Log.i("dashboardActivityAlt", "onCancelLoad mtd");
			 Toast.makeText(DashboardActivityAlt.this, "Some problems at server occurs. Please try later ", Toast.LENGTH_LONG).show();
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
	             Log.i("swiping order", temp.toString());
	             
	             View w = adapter.getMapedView(temp);
	              w.setBackgroundColor(Color.rgb(50, 107, 161));
	              Log.i("got view tag detect",  ((CustomTextView) w.findViewById(R.id.altOrderId)).getText().toString());
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

	             messagesExport = listItem.getCusThread().getMessages();
	    	
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
	    
	    /** *AsyncTask для посылки нового заказа, тип - Assignment*/
	    private class SendAssignment extends AsyncTask<Void, Void, JSONObject > {
		    
	    	JSONObject response ;
	    	protected void onPreExecute() {
	        //	progDailog = ProgressDialog.show(AssignmentPref.this,"Please wait...", "Loading...", true,true);
	        }
			
			
			
//			restClient.AddEntity("product[product_profile][title]", new StringBody(title,Charset.forName("UTF-8")));
//	    	restClient.AddEntity("category", new StringBody(category));
//	    	restClient.AddEntity("level", new StringBody(level));
//	    	restClient.AddEntity("deadline", new StringBody(deadline));
//	    	restClient.AddEntity("product[product_profile][info]", new StringBody(info,Charset.forName("UTF-8")));
//	    	restClient.AddEntity("product[product_profile][dtl_expl]",new StringBody(explanation));
//	    	restClient.AddEntity("product[product_profile][special_info]", new StringBody(specInfo,Charset.forName("UTF-8")));
//	    	restClient.AddEntity("timezone", new StringBody(timezone));
//	    	restClient.AddEntity("product[product_type]", new StringBody("assignment"));
//	    	restClient.AddEntity("product[product_profile][shoot_exclusive_video]", new StringBody(exclusive_video));
//	    	restClient.AddEntity("product[product_profile][shoot_common_video]", new StringBody(common_video));			
			

	        protected JSONObject doInBackground(Void... args) {
            try {
            	Log.i("SendAssignment files count", Integer.toString(FileManagerActivity.getFinalAttachFiles().size()));
 					response = launch.sendAssignment(OrderPreferences.getInstance().getOrderPrefItem(7).toString(),
 							 OrderPreferences.getInstance().getOrderPrefItem(1).toString(),
 							OrderPreferences.getInstance().getOrderPrefItem(2).toString(),
 							OrderPreferences.getInstance().getOrderPrefItem(8).toString(),   OrderPreferences.getInstance().getOrderPrefItem(9).toString(),
 							OrderPreferences.getInstance().getOrderPrefItem(12).toString(),
 							"",
 							OrderPreferences.getInstance().getOrderPrefItem(10).toString(),
 							FileManagerActivity.getFinalAttachFiles(),  OrderPreferences.getInstance().getOrderPrefItem(13).toString(), 
 							OrderPreferences.getInstance().getOrderPrefItem(14).toString());
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	       		
	        
	       	return response;
	     }

	        protected void onPostExecute(JSONObject  forPrint) {
	 	  	   
	        	try {
					
					if (Integer.parseInt(forPrint.getString(Constants.KEY_STATUS))==1)
					{
						FileManagerActivity.getFinalAttachFiles().clear();
					
					}
					else 
						FileManagerActivity.getFinalAttachFiles().clear();
					Toast.makeText(DashboardActivityAlt.this, "Something went wrong. Please try later.", Toast.LENGTH_LONG).show();
					
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {

					e.printStackTrace();
				}
	        }
	   }
	    /** *AsyncTask для посылки нового заказа, тип - Writing*/
	    private class SendEssay extends AsyncTask<Void, Void, JSONObject> {
	    			JSONObject response = null;
	    			protected void onPreExecute() {
//	    				progDailog = ProgressDialog.show(AssignmentPref.this,
//	    						"Please wait...", "Loading...", true,
//	    						true);
	    			}
	    			protected JSONObject doInBackground(Void... args) {
	    				try {
	    	
	    					// ??? Category curCat = (Category) catSpin.getSelectedItem();
//	    					(String title, String subject, String level, String deadline, String info, String explanation,
//	    				    		String specInfo, String timezone, List<File> files, 
//	    				    		String pages_number,String number_of_references ,String essay_type, String essay_creation_style)
	    					
	    					Log.i("SendEssay files count", Integer.toString(FileManagerActivity.getFinalAttachFiles().size()));
	    					response = launch.sendWriting(OrderPreferences.getInstance().getOrderPrefItem(7).toString(),
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
	    	
	    			protected void onPostExecute(JSONObject forPrint) {
	    				
	    				try {
							if (forPrint.getString(Constants.KEY_STATUS) != null)
							{
								if (Integer.parseInt(forPrint.getString(Constants.KEY_STATUS))==1)
								{
									FileManagerActivity.getFinalAttachFiles().clear();
//									Intent i = new Intent(AssignmentPref.this,
//							       DashboardTabScreen.class);
//											 	   Bundle mBundle = new Bundle();
//											 	   //		               mBundle.putString("NewOrder", "wasAdded");
//											 	   //		               i.putExtras(mBundle);
//											 	   startActivity(i);
								}
  							else 
  							{
  								FileManagerActivity.getFinalAttachFiles().clear();
  								Toast.makeText(DashboardActivityAlt.this, "Something went wrong. Please try later.", Toast.LENGTH_LONG).show();	
  								
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
			Log.i("onscroll method", "onscroll");
	            // Sample calculation to determine if the last 
	            // item is fully visible.
	            final int lastItem = firstVisibleItem + visibleItemCount;
	            if(lastItem == totalItemCount) {
	            	Log.i("onscroll method", "last item");
				          	 try {
								boolean isOnline = faq.isOnline();
								if (isOnline)
								{
										if (!TaskProgressDialogFragment.dialogIsProceeding)
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
								   Toast mToast =  Toast.makeText(getApplicationContext(), "No network connection. Please try later.", Toast.LENGTH_LONG);
						   	  	   mToast.show();
								}
				            }
				       	 catch(Exception e)
				       	 {
				       		e.printStackTrace(); 
				       	 }
	            }
			
		}  
}
