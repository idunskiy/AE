package com.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.LoginActivity;
import com.datamodel.Messages;
import com.datamodel.Order;
import com.datamodel.ProcessStatus;
import com.datamodel.ProductAssignment;
import com.datamodel.ProductWriting;
import com.datamodel.Threads;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class ServiceIntentMessages  extends IntentService{
	public static final String KEY_DATA = "data";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_ORDER = "order";
	public static final String KEY_THREAD = "thread";
	final String LOG_TAG = "myLogs";
	SharedPreferences sharedPreferences;
	static InputStream is = null;
	String json;
	Context mContext;
	private ArrayList<Order> serviceOrders = new ArrayList<Order>();
	public ArrayList<Messages> serviceMessages = new ArrayList<Messages>();
	int count = 0;
	boolean isRunning ;
	
	HttpResponse lastResponse;
	HttpResponse httpTestResponse;
	 static boolean res ;
 	
	public static final String ORDERS_IMPORT = "ORDERS_UPDATE";
	public static final String MESSAGES_IMPORT = "MESSAGES_UPDATE";
	private final Handler handler = new Handler();
	private JSONObject jObj;
	Intent intent;
	 int remID ;
	ComponentName componentInfo;
	ActivityManager.RunningTaskInfo ar;
	private Handler mHandler;
	private JSONArray jArr;
    public static ArrayList<Order> orderExport;
	List< ActivityManager.RunningTaskInfo > taskInfo ;
	 Thread t;
	 private static ArrayList<Order> serviceList;
	private Editor editor;
	DefaultHttpClient rplClient;
	FrequentlyUsedMethods faq = new FrequentlyUsedMethods(this);
	JSONObject data;
	private DatabaseHandler db;
	DashboardActivityAlt dashboardAct = new DashboardActivityAlt();
	public ServiceIntentMessages() {
		super("ServiceIntentMessages");
		// TODO Auto-generated constructor stub
	}
	public void onCreate() {
	    super.onCreate();
	    mHandler = new Handler();
//	    Thread t = new Thread(run);
//	    isRunning = true;
//	    t.start();
	    serviceList = dashboardAct.getOrderList();
//		serviceOrders =  DashboardActivityAlt.forPrint;
	    Log.d(LOG_TAG, "onCreate");
	  }
	  


	@Override
	protected void onHandleIntent(Intent intent) {
		
		 Calendar calendar = Calendar.getInstance();
	         java.util.Date now = calendar.getTime();
	         java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
	         
	         String url = "http://192.168.0.250:81/?identifier=nspid_"+md5(LoginActivity.passUserId)+
	        		  ",nspc&ncrnd="+Long.toString(currentTimestamp.getTime());
	         
	         
//	         String url = "http://rpl.test.assignmentexpert.com/?identifier=nspid_"+md5(LoginActivity.passUserId)+
//	        		  ",nspc&ncrnd="+Long.toString(currentTimestamp.getTime());
	         
			 Log.i(" rpl url",url);
	        HttpGet rplPost = new HttpGet(url);
		    rplPost.setHeader("Cookie", CookieStorage.getInstance().getArrayList().get(0).toString());
			 HttpResponse httpResponse;
					try 
					{
						
						rplClient = new DefaultHttpClient();
					httpResponse = rplClient.execute(rplPost);	
					lastResponse  = httpResponse;
					
					Log.i("I'm in service on start", "Fuck, I'm here again!");
					Header[] head = httpResponse.getAllHeaders();
			        Log.i("http Response",httpResponse.toString());
			        for (Header one:head)
			        {
			        	Log.i("headers",one.toString());
			        }
			        Log.i("response code", Integer.toString(httpResponse.getStatusLine().getStatusCode()));
			        HttpEntity httpEntity = httpResponse.getEntity();
			        is = httpEntity.getContent();
			        Log.i("httpResponse entity",is.toString());
			        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		            StringBuilder sb = new StringBuilder();
		            String line = null;
			            while ((line = reader.readLine()) != null)
			            {
			                sb.append(line);// + "n");
			            }
		            	try
		            	{
		            		is.close();
		            	}
		            	catch (IOException e)
			            {
			            	e.printStackTrace();
			            }
		            	
		            json = sb.toString();
		            Log.i("rpl response",json);
		            if (new JSONArray(json) != null) 
		                jArr = new JSONArray(json);
					 
		            JSONObject toObj;
				try {
					toObj = jArr.getJSONObject(0);
					data = toObj.getJSONObject(KEY_DATA);
				} 
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		             
					}
					 catch (ClientProtocolException e1) {
 						// TODO Auto-generated catch block
						 	rplClient.getConnectionManager().shutdown();
 						//e1.printStackTrace();
 					} catch (IOException e1) {
 						rplClient.getConnectionManager().shutdown();
 						//e1.printStackTrace();
 					} catch (JSONException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
					
					try
					{
						if (data.has(KEY_ORDER))
     					{
     						JSONObject jsonOrder=null;
     						try {
     							jsonOrder = data.getJSONObject(KEY_ORDER);
     						} catch (JSONException e1) {
     							// TODO Auto-generated catch block
     							e1.printStackTrace();
     						}
     							
     						DataParsing parse = new DataParsing();
     						Order orderObj = parse.wrapOrder(jsonOrder);
     						db = new DatabaseHandler(getApplicationContext());
     			    		
     						try 
     						{
     							Dao<ProcessStatus,Integer> daoProcess = db.getStatusDao();
     							orderObj.getProcess_status().setProccessStatusTitle
     							((daoProcess.queryForId(orderObj.getProcess_status().getProccessStatusId()).getProccessStatusTitle()));
     							faq.updateOrderFields(orderObj);
     							Log.i("service message", "after order fields updating");
     						}
     						catch (SQLException e) 
     				    	{
     							e.printStackTrace();
     						}
     							Log.i("get order from rpl", orderObj.toString());
     							try{
     							if (orderObj.getOrderid() > serviceList.get(0).getOrderid() )
     							{
     								//serviceList.add(0,orderObj);
     								addMethod (orderObj);
     								someMethod("Your order  "+ orderObj.getTitle() +" " + Integer.toString(orderObj.getOrderid()) + "  was added");
     								intent = new Intent(ORDERS_IMPORT);
     								//intent.putParcelableArrayListExtra("orders",  serviceOrders);
     								
     	     						sendBroadcast(intent);
     	     						dashboardAct.updateList();
     							}
     							else
     							{
     								for (ListIterator<Order> itr =serviceList.listIterator(); itr.hasNext();)
     	 							{
     	 								if (itr.hasNext())
     	 								{
     	     								Order a = new Order(); 
     	     									a = itr.next();
     	     								if (a.equals(orderObj))
     	     									serviceList.set(serviceList.indexOf(a), orderObj);
     	 								}
     	 							}
     								intent = new Intent(ORDERS_IMPORT);
//     								intent.putParcelableArrayListExtra("orders",  serviceOrders);
     	     						sendBroadcast(intent);
     								someMethod("Your order  "+ orderObj.getTitle() +" " + Integer.toString(orderObj.getOrderid()) + "  changed");
     								
     							}
     							}
     							catch (Exception e)
     							{
     								e.printStackTrace();
     							}
     						
     					}
     					else if (data.has(KEY_MESSAGE))
     					{
     						JSONObject jsonMessage = null;
     						Messages message = null ;
     						Threads thread = null ;
     						Gson gson = new Gson();
							try{
     							jsonMessage = data.getJSONObject(KEY_MESSAGE);
     							message =  gson.fromJson(jsonMessage.toString(),Messages.class);
     							Log.i("disparse message",jsonMessage.toString());
	     					  	JSONObject jsonThread = jsonMessage.getJSONObject(KEY_THREAD);
	     						Log.i("jsonServiceThread regular", jsonThread.toString());
	     						//thread =  gson.fromJson(jsonThread.toString(),Threads.class);
	     					//	Log.i("jsonServiceThread after parse", thread.toString());
	     						//Log.i("jsonServiceThread after parse", (gson.fromJson(jsonThread.toString(),Threads.class)).toString());
	     						JSONObject jsonOrder = jsonThread.getJSONObject(KEY_ORDER);
	     						Log.i("jsonServiceOrder after parse", jsonOrder.toString());
	     						Order orderMess =  gson.fromJson(jsonOrder.toString(),Order.class);
	     						Log.i("incomming thread",orderMess.toString());
	     						Log.i("asdqweasd", "broadcast mess");
	     						for (ListIterator<Order> itr = serviceList.listIterator(); itr.hasNext();)
     							{
	     							Log.i("service", "Im in addimg mes loop");
     								if (itr.hasNext())
     								{
	     								Order a =  itr.next();
	     								if (a.equals(orderMess))
	     									{
	     									
	     									if (a.getCusThread().getMessages().isEmpty())
	     									{
	     										a.getCusThread().addMessage(message);
	     										 someMethod("Your order  "+ a.getTitle() + "  was changed. Message was added");
	     									}
	     									
	     									else
	     									{
	     										if(message.getMessageId()>a.getCusThread().getMessages().get(a.getCusThread().getMessages().size()-1).getMessageId() &
	     											 (!message.getMessageBody().equals(a.getCusThread().getMessages().get(a.getCusThread().getMessages().size()-1).getMessageBody())))
	     									   {
	     										  a.getCusThread().addMessage(message);
	     										 someMethod("Your order  "+ a.getTitle() + "  was changed. Message was added");
	     									     // a = orderMess;
	     									     
	     									   }
	     									}
	     									
	     									a.setCategory(orderMess.getCategory());
	     									a.setPrice(orderMess.getPrice());
	     									a.setProcess_status(orderMess.getProcess_status());
	     									a.setLevel(orderMess.getLevel());
	     									a.setSubject(orderMess.getSubject());
	     									faq.updateOrderFields(a);
	     									Log.i("order message", "before sending");
    			                            intent = new Intent(MESSAGES_IMPORT);
    			                            sendBroadcast(intent);
	     									Log.i("order message", a.toString());
	     									}
 										
     								}
     								
     							}
							}
	     						catch(Exception e)
	     						{
	     							e.printStackTrace();
	     						}
					}
						else
						{
							isRunning=false;
							
						}
					}
					catch(NullPointerException e)
					{
					} 
					catch (JSONException e) 
					{
				    }
		
	}
	public String md5(String string) {
	    byte[] hash;

	    try {
	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
	    } catch (NoSuchAlgorithmException e) 
	    {
	        throw new RuntimeException("Huh, MD5 should be supported?", e);
	    } catch (UnsupportedEncodingException e) 
	    {
	    	
	        throw new RuntimeException("Huh, UTF-8 should be supported?", e);
	    }

	    StringBuilder hex = new StringBuilder(hash.length * 2);

	    for (byte b : hash) {
	        int i = (b & 0xFF);
	        if (i < 0x10) hex.append('0');
	        hex.append(Integer.toHexString(i));
	    }
	    return hex.toString();
	}
	 private class ToastRunnable implements Runnable {
		    String mText;

		    public ToastRunnable(String text) {
		        mText = text;
		    }

		    public void run(){
		         Toast.makeText(getApplicationContext(), mText, Toast.LENGTH_SHORT).show();
		    }
		}
	 private class sendUpdatings implements Runnable {
		    Order r;

		    public sendUpdatings(Order r) {
		        this.r = r;
		    }

		    public void run(){
		         DashboardActivityAlt.forPrint.add(0,r);
		    }
		}
	 	private void addMethod(Order r)
	 	{
	 		mHandler.post(new sendUpdatings(r));
	 	}
	 	


		private void someMethod(String message) {
		    mHandler.post(new ToastRunnable(message));
		}
		 public void onDestroy() {
			    super.onDestroy();
			  }

		 public ArrayList<Order> getServiceList()
		 {
			 return serviceList;
		 }
} 
