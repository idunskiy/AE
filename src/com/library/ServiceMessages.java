package com.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.*;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.LoginActivity;
import com.datamodel.Message;
import com.datamodel.Order;
import com.datamodel.Threads;
import com.google.gson.Gson;

public class ServiceMessages extends  Service {
	public static final String KEY_DATA = "data";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_ORDER = "order";
	public static final String KEY_THREAD = "thread";
	final String LOG_TAG = "myLogs";
	SharedPreferences sharedPreferences;
	static InputStream is = null;
	String json;
	Context mContext;
	public ArrayList<Order> serviceOrders = new ArrayList<Order>();
	public ArrayList<Message> serviceMessages = new ArrayList<Message>();
	 
	boolean isRunning = false;
	HttpResponse httpResponse;
	HttpResponse httpTestResponse;

 	
	public static final String ORDERS_IMPORT = "ORDERS_UPDATE";
	public static final String MESSAGES_IMPORT = "MESSAGES_UPDATE";
	private final Handler handler = new Handler();
	private JSONObject jObj;
	Intent intentOrder;
	Intent intentMessage;
	ComponentName componentInfo;
	 ActivityManager.RunningTaskInfo ar;
	 private Handler mHandler;
	 private JSONArray jArr;
     public static ArrayList<Order> orderExport;
	List< ActivityManager.RunningTaskInfo > taskInfo ;
	  public void onCreate() {
	    super.onCreate();
	    intentOrder = new Intent(ORDERS_IMPORT);
	    intentMessage = new Intent(MESSAGES_IMPORT);
	    mContext = getApplicationContext();
	    orderExport = DashboardActivityAlt.forPrint;
	    
	    Log.d(LOG_TAG, "onCreate");
	    
//	    DklabExecute performBackgroundTask = new DklabExecute();
//      	if (isRunning == false)
//	    performBackgroundTask.execute();
	    
	    
	  }
	  
	  
	  public int onStartCommand(Intent intent, int flags, int startId) {
		  
	    Log.d(LOG_TAG, "onStartCommand");
	    mHandler = new Handler();
	    Thread t = new Thread(run);
	    isRunning = true;
	    t.start();
 	    return super.onStartCommand(intent, flags, startId);
	  }

	  public void onDestroy() {
	    super.onDestroy();
	    isRunning = false;
	    Log.d(LOG_TAG, "onDestroy");
	  }

	  public IBinder onBind(Intent intent) {
	    Log.d(LOG_TAG, "onBind");
	    return null;
	  }
	 
		 
		 private Runnable run = new Runnable() {
			 JSONObject data;
		      public void run() {
		    	  
		              while(isRunning){
		            	  int count = 0;
		            	  Calendar calendar = Calendar.getInstance();
		     	         java.util.Date now = calendar.getTime();
		     	         java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
		     	        String url = "http://192.168.0.250:81/?identifier=nspid_"+md5(LoginActivity.passUserId)+
		     	        		  ",nspc&ncrnd="+Long.toString(currentTimestamp.getTime());
		     	        HttpGet rplPost = new HttpGet(url);
		     				isRunning = true;
		     				Log.i("service count", Integer.toString(count));
		     				count ++;
		     	         Log.i("md5 func", md5(LoginActivity.passUserId)); 
		     	        	String testData = "http://192.168.0.250/app_dev.php/api/comet/testOrder/";
		     	        	 JSONParser  parser = new JSONParser();
		     	        	 DefaultHttpClient testClient = new DefaultHttpClient();
		     	         	 DefaultHttpClient rplClient = new DefaultHttpClient();
		     	        	List<NameValuePair> params = new ArrayList<NameValuePair>();
		     	             params.add(new BasicNameValuePair("", ""));
		     				HttpGet httpTest = new HttpGet(testData);
		     					 httpTest.setHeader("Cookie", CookieStorage.getInstance().getArrayList().get(0).toString());
		     					 rplPost.setHeader("Cookie", CookieStorage.getInstance().getArrayList().get(0).toString());
		     					try {
		     						//rplClient.wait(3000);
		     						httpResponse = rplClient.execute(rplPost);
//		     						catch (InterruptedException e) {
//		     							// TODO Auto-generated catch block
//		     							e.printStackTrace();
//		     						}
		     			        Header[] head = httpResponse.getAllHeaders();
		     			        Log.i("http Response",httpResponse.toString());
		     			        for (Header one:head)
		     			        {
		     			        	Log.i("headers",one.toString());
		     			        }
		     			        Log.i("response code", Integer.toString(httpResponse.getStatusLine().getStatusCode()));
		     			        HttpEntity httpEntity = httpResponse.getEntity();
		     			        is = httpEntity.getContent();
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
		     		            else
		     		            	isRunning=false;
		     					 JSONObject toObj;
								try {
									toObj = jArr.getJSONObject(0);
									data = toObj.getJSONObject(KEY_DATA);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		     		             
		     					}
		     					 catch (ClientProtocolException e1) {
			     						// TODO Auto-generated catch block
			     						e1.printStackTrace();
			     					} catch (IOException e1) {
			     						// TODO Auto-generated catch block
			     						e1.printStackTrace();
			     					} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
		     					try
		     					{
		     					if (data.has(KEY_ORDER))
		     					{
		     						for (Order a : ServiceMessages.orderExport)
		     						  {
		     							 Log.i("service before list", a.toString());
		     						  }
		     			    		 Log.i(" ", "  ");
		     						  for (Order a : DashboardActivityAlt.forPrint)
		     						  {
		     							  
		     							 Log.i("before dashboard list", a.toString());
		     						  }
		     						JSONObject jsonOrder=null;
									try {
										jsonOrder = data.getJSONObject(KEY_ORDER);
									} catch (JSONException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
		     						Gson gson = new Gson();
		     						Order orderObj= gson.fromJson(jsonOrder.toString(), Order.class);
		     						try
		     						{
		     							for (ListIterator<Order> itr = DashboardActivityAlt.forPrint.listIterator(); itr.hasNext();)
		     							{
		     								Order a = itr.next();
		     								
		     								Log.i("order count", a.toString());
		     								
		     								if(orderObj.getOrderid()==a.getOrderid())
		     								{
		     									Log.i("Service","order was changed");
		     									a = orderObj;
		     									
		     									
		     								}
		     								else
		     								{
		     									Log.i("Service","order"+ orderObj.getTitle()+" was added");
		     									Log.i("get 0 order", DashboardActivityAlt.forPrint.get(0).toString());
		     									Log.i("new order", orderObj.toString());
		     									DashboardActivityAlt.forPrint.add(0,orderObj);
//		     									if (DashboardActivityAlt.forPrint.get(0).getOrderid()==orderObj.getOrderid())
//		     									{
//		     										Log.i("service adding order", "the same order as it was");
//		     									}
//		     									else
//		     									{
//		     										DashboardActivityAlt.forPrint.add(0,orderObj);
//		     										Log.i("service adding order", "different order and now it'll be reexecuted");
//		     										someMethod("Your order  "+ orderObj.getTitle() + "  was added");
//		     									}	
		     									Log.i("status",Integer.toString(orderObj.getProcess_status().getProccessStatusId()));
		     									
		     								}
		     								
		     							}
		     						}
		     						
		     						catch (Exception e)
		     						{
		     							e.printStackTrace();
		     						}
		     						
//		     						
		     						 someMethod("Your order  "+ orderObj.getTitle() + "  was changed");
		     						sendBroadcast(intentOrder);
		     						rplClient.getConnectionManager().shutdown();
		     						Log.i("after parse order",orderObj.toString());
		     						Log.i("orders after updating",DashboardActivityAlt.orders.toString() );
		     					}
		     					else if (data.has(KEY_MESSAGE))
		     					{
		     						
		     						JSONObject jsonMessage;
									try {
										jsonMessage = data.getJSONObject(KEY_MESSAGE);
									
		     						Gson gson = new Gson();
		     						Log.i("messages before parse", jsonMessage.toString());
		     						for (Order a: DashboardActivityAlt.forPrint)
		     						{
		     							Log.i("messages count", Integer.toString(a.getCusThread().getMessages().size()));
		     						}
		     						Log.i("disparse message",jsonMessage.toString());
		     						Message message =  gson.fromJson(jsonMessage.toString(),Message.class);
		     						Log.i("incomming message",message.toString());
		     						JSONObject jsonThread = jsonMessage.getJSONObject(KEY_THREAD);
		     						Threads thread =  gson.fromJson(jsonThread.toString(),Threads.class);
		     						Log.i("incomming thread",thread.toString());
		     						Order orderChanged = new Order();
		     						String orderName = null;
		     						for(Order as : DashboardActivityAlt.forPrint)
		     						{
		     							
		     							if (as.getOrderid() == thread.getTreadOrder().getOrderid())
		     							{
		     								orderName = as.getTitle();
		     								orderChanged = as;
		     								Log.i("messages count after", Integer.toString(as.getCusThread().getMessages().size()));
		     							}
		     							else 
		     							{
		     								
		     							}
		     							
		     							
		     						}
		     						Log.i("orderchanged",orderChanged.toString());
		     						someMethod("Your order  "+ thread.getTreadOrder().getTitle() + "  was changed. Message was added");
		     						orderChanged.getCusThread().addMessage(message);
		     						sendBroadcast(intentMessage);
		     					
		     						rplClient.getConnectionManager().shutdown();
		     						Log.i("messages service", "after sleep");
									} catch (JSONException e) {
										// TODO Auto-generated catch block
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
		     						e.printStackTrace();
		     					}
		     	        	
		              
		       }
		   }
		      
		 };
		 public void toCallAsynchronous() {
			    TimerTask doAsynchronousTask;
			    final Handler handler = new Handler();
			    Timer timer = new Timer();
			            doAsynchronousTask = new TimerTask() {

			                @Override
			                public void run() {
			                    // TODO Auto-generated method stub
			                    handler.post(new Runnable() {
			                        public void run() {
			                            try {
			                            
//			                            	DklabExecute performBackgroundTask = new DklabExecute();
//			                                performBackgroundTask.execute();

			                            } 
			                            
			                            catch (Exception e) {
			                                // TODO Auto-generated catch block
			                            }
			                        }
			                    });

			                }

			            };

			            timer.schedule(doAsynchronousTask, 0,3000);//execute in every 50000 ms

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


			private void someMethod(String message) {
			    mHandler.post(new ToastRunnable(message));
			}

}