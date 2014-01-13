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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.LoginActivity;
import com.assignmentexpert.R;
import com.datamodel.Files;
import com.datamodel.Messages;
import com.datamodel.Order;
import com.datamodel.ProcessStatus;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.library.singletones.CookieStorage;
import com.tabscreens.OrderInfoTabScreen;
/**  * IntentService for background order and messages updating*/
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
	public ArrayList<Messages> serviceMessages = new ArrayList<Messages>();
	int count = 0;
	boolean isRunning ;
	 private DownloadManager dm;
	HttpResponse lastResponse;
	HttpResponse httpTestResponse;
	 static boolean res ;
	 private long enqueue;
	public static final String ORDERS_IMPORT = "ORDERS_UPDATE";
	public static final String MESSAGES_IMPORT = "MESSAGES_UPDATE";
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
	DefaultHttpClient rplClient;
	FrequentlyUsedMethods faq = new FrequentlyUsedMethods(this);
	JSONObject data;
	private DatabaseHandler db;
	DashboardActivityAlt dashboardAct = new DashboardActivityAlt();
	public ServiceIntentMessages() {
		super("ServiceIntentMessages");
	}
	
	public void onCreate() {
	    super.onCreate();
	    mHandler = new Handler();
	    serviceList = dashboardAct.getOrderList();
	    Log.d(LOG_TAG, "onCreate");
	  }
	  
	@Override
	protected void onHandleIntent(Intent intent) {
		
		 Calendar calendar = Calendar.getInstance();
	         java.util.Date now = calendar.getTime();
	         java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
	         String url = Constants.finalRplHost+"/?identifier=nspid_"+md5(LoginActivity.passUserId)+
	        		  ",nspc&ncrnd="+Long.toString(currentTimestamp.getTime());
	        HttpGet rplPost = new HttpGet(url);
		    rplPost.setHeader("Cookie", CookieStorage.getInstance().getArrayList().get(0).toString());
			 HttpResponse httpResponse;
					try 
					{
						rplClient = new DefaultHttpClient();
					httpResponse = rplClient.execute(rplPost);	
					lastResponse  = httpResponse;
			        HttpEntity httpEntity = httpResponse.getEntity();
			        is = httpEntity.getContent();
			        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		            StringBuilder sb = new StringBuilder();
		            String line = null;
			            while ((line = reader.readLine()) != null)
			            {
			                sb.append(line);
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
		            if (new JSONArray(json) != null) 
		                jArr = new JSONArray(json);
		            JSONObject toObj;
				try {
					toObj = jArr.getJSONObject(0);
					data = toObj.getJSONObject(KEY_DATA);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
		             
					}
					 catch (ClientProtocolException e1) {
						 	rplClient.getConnectionManager().shutdown();
 					} catch (IOException e1) {
 						rplClient.getConnectionManager().shutdown();
 					} catch (JSONException e) {
						// TODO Auto-generated catch block
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					try
					{
						// если в полученном json'e содержиться обьект order, то с помощью gson преобразуем его в обьект
						if (data.has(KEY_ORDER))
     					{
     						JSONObject jsonOrder=null;
     						try {
     							jsonOrder = data.getJSONObject(KEY_ORDER);
     						} catch (JSONException e1) {
     							e1.printStackTrace();
     						}
     						DataParsing parse = new DataParsing();
     						Order orderObj = parse.wrapOrder(jsonOrder);
     						Order newOrder = new Order();
     						//так как для обьекта заказа есть поле статуса, а в json мы получаем только идентификатор статуса, то
     						//обращаемся к базе данных и имея идентификатор, получаем названия соответствующих статусов. 
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
     							try{
     							
     								//если заказ новый, то есть текущий идентификатор больше последнего имеющегося в имеющимуся ArrayList заказов
     							if (orderObj.getOrderid() > DashboardActivityAlt.forPrint.get(0).getOrderid() )
     							{
     								if (orderObj.getOrderid() != newOrder.getOrderid())
     								{
     									//добавление заказа к имеющимуся ArrayList, который служит для их отображения в ListView
	     								addMethod (orderObj);
	     								intent = new Intent(ORDERS_IMPORT);
	     	     						sendBroadcast(intent);
	     	     						dashboardAct.updateList();
	     	     						newOrder.setOrderid(orderObj.getOrderid());
     								}
     							}
     							//если произошло обновление одного из полей существующего заказа.
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
     	     						sendBroadcast(intent);
     								someMethod("Your order  "+ orderObj.getTitle() +" " + Integer.toString(orderObj.getOrderid()) + "  changed");
     								
     							}
     							}
     							catch (Exception e)
     							{
     								e.printStackTrace();
     							}
     						
     					}
						//если получаем обьект сообщения
     					else if (data.has(KEY_MESSAGE))
     					{
     						JSONObject jsonMessage = null;
     						Messages message = null ;
     						Gson gson = new Gson();
							try{
     							jsonMessage = data.getJSONObject(KEY_MESSAGE);
     							Log.i("received mess", jsonMessage.toString());
     							//преобразовываес в обьект Message с помощью gson
     							message =  gson.fromJson(jsonMessage.toString(),Messages.class);
     							Log.i("message file list", Integer.toString(message.getFiles().size()));
     							for (Files s: message.getFiles())
     							{
     								registerReceiver(downloadFilesReceiver, new IntentFilter(
     						                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
     								 dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
     						        Request request = new Request(
     						                Uri.parse(s.getFileFullPath()));
     						        enqueue = dm.enqueue(request);
     							}
	     					  	JSONObject jsonThread = jsonMessage.getJSONObject(KEY_THREAD);
	     						JSONObject jsonOrder = jsonThread.getJSONObject(KEY_ORDER);
	     						Order orderMess =  gson.fromJson(jsonOrder.toString(),Order.class);
	     						for (ListIterator<Order> itr = serviceList.listIterator(); itr.hasNext();)
     							{
	     							
     								if (itr.hasNext())
     								{
	     								Order a =  itr.next();
	     								if (a.equals(orderMess))
	     									{
	     									
	     									if (a.getMessages().isEmpty())
	     									{
	     										a.addMessage(message);
	     									}
	     									
	     									else
	     									{
	     										if(message.getMessageId()>a.getMessages().get(a.getMessages().size()-1).getMessageId() &
	     											 (!message.getMessageBody().equals(a.getMessages().get(a.getMessages().size()-1).getMessageBody())))
	     									   {
	     										  a.addMessage(message);
	     									   }
	     									}
	     									//нотификация пользователя о получении нового сообщения
	     									someMethod("Your order  "+ a.getTitle() + "  was changed. Message was added");
	     									a.setCategory(orderMess.getCategory());
	     								//	a.setPrice(orderMess.getPrice());
	     									a.setProcess_status(orderMess.getProcess_status());
	     									a.setLevel(orderMess.getLevel());
	     									a.setSubject(orderMess.getSubject());
	     									faq.updateOrderFields(a);
    			                            intent = new Intent(MESSAGES_IMPORT);
    			                            sendBroadcast(intent);
    			                            sendNotification(message.getMessageBody());
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
	 /**  * метод преобразования в md5 пароля пользователя 
	  * @param string - строка для преобразования
	  * @return строковое представление md5 для входящей строки */
	public String md5(String string) {
	    byte[] hash = null;
	    StringBuilder hex = null;
	    try {
	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
	    hex = new StringBuilder(hash.length * 2);

	    for (byte b : hash) {
	        int i = (b & 0xFF);
	        if (i < 0x10) hex.append('0');
	        hex.append(Integer.toHexString(i));
	    }
	    } catch (NoSuchAlgorithmException e) 
	    {
	        throw new RuntimeException("Huh, MD5 should be supported?", e);
	    } catch (UnsupportedEncodingException e) 
	    {
	        throw new RuntimeException("Huh, UTF-8 should be supported?", e);
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    return hex.toString();
	}
	 /**  * класс для вывода строки в отдельном потоке во всех частях приложения	   */
	 private class ToastRunnable implements Runnable {
		    String mText;

		    public ToastRunnable(String text) {
		        mText = text;
		    }

		    public void run(){
		         Toast.makeText(getApplicationContext(), mText, Toast.LENGTH_SHORT).show();
		    }
		}
	 /**  * класс для добавления заказа в статический контейнер в отдельном потоке*/
	 private class sendUpdatings implements Runnable {
		    Order r;

		    public sendUpdatings(Order r) {
		        this.r = r;
		    }

		    public void run(){
		         DashboardActivityAlt.forPrint.add(0,r);
		    }
		}
	 /**  * метод для добавления заказа в статический контейнер*/
	 	private void addMethod(Order r)
	 	{
	 		mHandler.post(new sendUpdatings(r));
	 		someMethod("Your order  "+ r.getTitle() +" " + Integer.toString(r.getOrderid()) + "  was added");
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
		 /**  * BroadcastReceiver для закачки файлов*/
		 BroadcastReceiver downloadFilesReceiver = new BroadcastReceiver() {
	            @Override
	            public void onReceive(Context context, Intent intent) {
	                String action = intent.getAction();
	                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
	                    intent.getLongExtra(
	                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
	                    Query query = new Query();
	                    query.setFilterById(enqueue);
	                    Cursor c = dm.query(query);
	                    if (c.moveToFirst()) {
	                        int columnIndex = c
	                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
	                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
	                        		
	                            c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
	                            
	                        }
	                    }
	                    //cursor closing (experiment feature)
	                    c.close();
	                }
	            }
	        };
	        /**  * метод для послания уведомления*/
		 private void sendNotification(String strNotification)
		 {
			 
			 String ns = Context.NOTIFICATION_SERVICE;
			 NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

			 int icon = R.drawable.launcher;        
			 CharSequence tickerText =Constants.NOTIFICATION_MESS; // ticker-text
			 long when = System.currentTimeMillis();         
			 Context context = getApplicationContext();     
			 CharSequence contentTitle = Constants.NOTIFICATION_MESS;  
			 CharSequence contentText = strNotification;      
			 Intent notificationIntent = new Intent(this, OrderInfoTabScreen.class);
			 notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			 notificationIntent.setFlags(Notification.FLAG_AUTO_CANCEL);
			 if (checkAppForeground())
				 notificationIntent.setAction("notification_foreground");
			 else
				 notificationIntent.setAction("notification_background");
			 PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
			 Notification notification = new Notification(icon, tickerText, when);
			 notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
			 
			 Log.i("check App Foreground", Boolean.toString(checkAppForeground()));
			 
			 mNotificationManager.notify(Constants.NOTIFICATION_ID, notification);
			 startForeground(Constants.NOTIFICATION_ID, notification);
		 }
	        /**  * метод для проверки на вопрос наличие приложения в foreground'e*/
		 private boolean checkAppForeground()
		 {
			 boolean inForeground = false;
			 ActivityManager actMngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	
			 List<RunningAppProcessInfo> runningAppProcesses = actMngr.getRunningAppProcesses();
			 for (RunningAppProcessInfo pi : runningAppProcesses) {
			     if (getPackageName().equals(pi.processName)) {
	
			         inForeground = pi.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
			     }
			 }
			 return inForeground;
		 }
} 
