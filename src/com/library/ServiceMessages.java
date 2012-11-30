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

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.LoginActivity;
import com.datamodel.Messages;
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
	public ArrayList<Messages> serviceMessages = new ArrayList<Messages>();
	int count = 0;
	boolean isRunning ;
	
	HttpResponse lastResponse;
	HttpResponse httpTestResponse;

 	
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
	private Editor editor;
	DefaultHttpClient rplClient;
	
	  public void onCreate() {
	    super.onCreate();

	    mContext = getApplicationContext();
	    orderExport = DashboardActivityAlt.forPrint;
	    sharedPreferences = getSharedPreferences("user", MODE_PRIVATE );
	    editor = sharedPreferences.edit();
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
		            	  Calendar calendar = Calendar.getInstance();
		     	         java.util.Date now = calendar.getTime();
		     	         java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
		     	         
		     	         String url = "http://192.168.0.250:81/?identifier=nspid_"+md5(LoginActivity.passUserId)+
		     	        		  ",nspc&ncrnd="+Long.toString(currentTimestamp.getTime());
		     			 
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
			     						e1.printStackTrace();
			     					} catch (IOException e1) {
			     						rplClient.getConnectionManager().shutdown();
			     						e1.printStackTrace();
			     					} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
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
		     						Gson gson = new Gson();
		     						Order orderObj= gson.fromJson(jsonOrder.toString(), Order.class);
		     						
		     						if (orderObj.getOrderid()>DashboardActivityAlt.forPrint.get(0).getOrderid() )
		     						{
		     							Log.i("last Order ID", Integer.toString(DashboardActivityAlt.forPrint.get(0).getOrderid() ));
		     							DashboardActivityAlt.forPrint.add(0,orderObj);
		     							someMethod("Your order  "+ orderObj.getTitle() +" " + Integer.toString(orderObj.getOrderid()) + "  was added");
		     							intent = new Intent(ORDERS_IMPORT);
			     						sendBroadcast(intent);
		     						}
		     						else
		     						{
		     							for (ListIterator<Order> itr = DashboardActivityAlt.forPrint.listIterator(); itr.hasNext();)
			     							{
			     								if (itr.hasNext())
			     								{
				     								Order a = new Order(); 
				     										a = itr.next();
				     								if (a.equals(orderObj))
				     									a = orderObj;
				     										
			     								}
			     							}
		     							someMethod("Your order  "+ orderObj.getTitle() +" " + Integer.toString(orderObj.getOrderid()) + "  changed");
		     							intent = new Intent(ORDERS_IMPORT);
			     						sendBroadcast(intent);
		     						}
//		     						intent = new Intent(ORDERS_IMPORT);
//		     						sendBroadcast(intent);
		     							
//		     					 if (orderObj.getOrderid() != sharedPreferences.getInt("previousID", 0))
//						     	 {
//			     						editor.putInt("previousOrdID", orderObj.getOrderid());
//				                    	editor.commit();
//			     						try
//			     						{
//			     							
//			     							for (ListIterator<Order> itr = DashboardActivityAlt.forPrint.listIterator(); itr.hasNext();)
//			     							{
//			     								if (itr.hasNext())
//			     								{
//				     								Order a = new Order(); 
//				     										a = itr.next();
//				     								
//				     								Log.i("order count", a.toString());
//				     								
//				     								if(DashboardActivityAlt.forPrint.contains(a))
//				     								{
//				     									Log.i("Service","order was changed");
//				     									a = orderObj;
//				     									someMethod("Your order  "+ orderObj.getTitle() +" " + Integer.toString(orderObj.getOrderid()) + "  was changed");
//				     								}
//				     								else
//				     								{
//				     									Log.i("Service","order"+ orderObj.getTitle()+" was added");
//				     									Log.i("get 0 order", DashboardActivityAlt.forPrint.get(0).toString());
//				     									Log.i("new order", orderObj.toString());
//				     									DashboardActivityAlt.forPrint.add(0,orderObj);
//				     									//itr.add(orderObj);
//				     									someMethod("Your order  "+ orderObj.getTitle() +" " + Integer.toString(orderObj.getOrderid()) + "  was added");
//				     									Log.i("status",Integer.toString(orderObj.getProcess_status().getProccessStatusId()));
//			     									
//			     								}
//		     								}
//		     								
//		     							}
//		     						}
//		     						
//		     						catch (Exception e)
//		     						{
//		     							e.printStackTrace();
//		     						}
//		     						intent = new Intent(ORDERS_IMPORT);
//		     						sendBroadcast(intent);
//		     						
//		     						Log.i("after parse order",orderObj.toString());
//		     						Log.i("orders after updating",DashboardActivityAlt.orders.toString() );
//						     	  }
		     					}
		     					else if (data.has(KEY_MESSAGE))
		     					{
		     						JSONObject jsonMessage = null;
		     						Messages message = null ;
		     						Threads thread = null ;
		     						
									
									jsonMessage = data.getJSONObject(KEY_MESSAGE);
		     						Gson gson = new Gson();
		     						Log.i("disparse message",jsonMessage.toString());
		     						message =  gson.fromJson(jsonMessage.toString(),Messages.class);
		     						   
			     					  	JSONObject jsonThread = jsonMessage.getJSONObject(KEY_THREAD);
			     						Log.i("jsonServiceThread", jsonThread.toString());
			     						JSONObject jsonOrder = jsonThread.getJSONObject(KEY_ORDER);
			     						thread =  gson.fromJson(jsonThread.toString(),Threads.class);
			     						Order orderMess =  gson.fromJson(jsonOrder.toString(),Order.class);
			     						Log.i("incomming order from message",thread.toString());
			     						Log.i("incomming thread",orderMess.toString());
			     						for (ListIterator<Order> itr = DashboardActivityAlt.forPrint.listIterator(); itr.hasNext();)
		     							{
		     								if (itr.hasNext())
		     								{
			     								Order a = new Order(); 
			     										a = itr.next();
			     								if (a.equals(orderMess))
			     									{
			     									Log.i("new message ID", Integer.toString(message.getMessageId()));
			     									//Log.i("last message ID", Integer.toString(a.getCusThread().getMessages().get(a.getCusThread().getMessages().size()-1).getMessageId()));
			     									Log.i("order message", a.toString());
			     									if (a.getCusThread().getMessages().isEmpty())
			     									{
			     										a.getCusThread().addMessage(message);
			     										someMethod("Your order  "+ orderMess.getTitle() + "  was changed. Message was added");
			    			                            intent = new Intent(MESSAGES_IMPORT);
			    			                            sendBroadcast(intent);
			     									}
			     									
			     									else
			     									{
			     										if(message.getMessageId()>a.getCusThread().getMessages().get(a.getCusThread().getMessages().size()-1).getMessageId() &
			     											 (!message.getMessageBody().equals(a.getCusThread().getMessages().get(a.getCusThread().getMessages().size()-1).getMessageBody())))
			     									   {
			     										  a.getCusThread().addMessage(message);
			     									      a = orderMess;
			     									      someMethod("Your order  "+ orderMess.getTitle() + "  was changed. Message was added");
			    			                              intent = new Intent(MESSAGES_IMPORT);
			    			                              sendBroadcast(intent);
			     									   
			     									   }
			     									  }
			     									}
	     										
			     										
		     								}
		     								
		     							}
			     						
//			     						 Order orderChanged = new Order();
			                             String orderName = null;
			                           
//			                             for(Order as : DashboardActivityAlt.forPrint)
//			                             {
//	
//			                                 if (as.getOrderid() == orderMess.getOrderid())
//			                                 {
//			                                     orderName = as.getTitle();
//			                                     as.setTitle(orderMess.getTitle());
//				     						     as.setPrice(orderMess.getPrice());
//				     						     as.setCategory(orderMess.getCategory());
//			                                     orderChanged = as;
//			                                     Log.i("messages count after", Integer.toString(as.getOrderid()));
//			                                 }
//	
//			                             }
//			                             Log.i("orderchanged",orderChanged.toString());
//			                       if (message.getMessageId() != sharedPreferences.getInt("previousMessID", 0))
//					     			 {  
//			                    	     editor.putInt("previousMessID", message.getMessageId());
//			                    	     editor.commit();
//			                             someMethod("Your order  "+ orderMess.getTitle() + "  was changed. Message was added");
//			                             orderChanged.getCusThread().addMessage(message);
//			                             intent = new Intent(MESSAGES_IMPORT);
//			                             sendBroadcast(intent);
//		     						 }
		     					}
		     					else
		     					{
		     						isRunning=false;
		     						
		     					}
		     					}
		     					catch(NullPointerException e)
		     					{
		     						e.printStackTrace();
		     					} catch (JSONException e) {
									// TODO Auto-generated catch block
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