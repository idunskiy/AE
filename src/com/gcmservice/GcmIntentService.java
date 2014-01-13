package com.gcmservice;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.assignmentexpert.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.library.Constants;
import com.tabscreens.DashboardTabScreen;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "GCM Demo";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm;
        gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString(), null);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString(), null);
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
            	
//                for (int i = 0; i < 5; i++) {
//                    Log.i(TAG, "Working... " + (i + 1)
//                            + "/5 @ " + SystemClock.elapsedRealtime());
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                    }
//                }
//                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
            	
                // Post notification of received message.
            	
            	
            	Intent send = new Intent(Constants.ORDER_LIST_UPDATE);
            	String _id = null;
            	if (Integer.parseInt(extras.getString("status"))==0)
            	{
            		try {
						JSONObject id = new JSONObject(extras.getString("data"));
						 _id = id.getString("order_id");
						Log.i("parse id ", _id);
						send.putExtra("action", "order_payed");
						send.putExtra("order_id", _id);
						Log.i("received id", _id);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            		
            	}
            	else 
            	{
            		try {
            		send.putExtra("action", "order_changed");
            		JSONObject data = new JSONObject(extras.getString("data"));
            		Log.i("parse order ", data.toString());
            		 _id = data.getString("order_id");
						send.putExtra("order_changed", _id);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            	String ham = extras.getString("ham");
            	extras.get("data");
            	sendNotification(ham, _id);
            	sendBroadcast(send);
               
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg, String id) {
    	// check if order id was parsed successfully
    	if (id!=null)
    	{
	        mNotificationManager = (NotificationManager)
	                this.getSystemService(Context.NOTIFICATION_SERVICE);
	
	        
	        Intent notificationIntent = new Intent(this, DashboardTabScreen.class);
	        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
	                | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	     
	        if (id !=null){
	        	Bundle bundle = new Bundle();
	        	Log.i("parsed order id is not 0", id);
	        	bundle.putInt(Constants.ORDER_NOTIFICATED_UPDATE, Integer.parseInt(id));
	        	notificationIntent.putExtras(bundle);
	        	notificationIntent.setAction(Constants.ORDER_NOTIFICATED_UPDATE);
	        }
	        
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
	        		notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT  | PendingIntent.FLAG_ONE_SHOT);
	        
	        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	        
	        
	        NotificationCompat.Builder mBuilder =
	                new NotificationCompat.Builder(this)
	        .setSmallIcon(R.drawable.launcher)
	        .setContentTitle("AssignmentExpert Notification")
	        .setStyle(new NotificationCompat.BigTextStyle()
	        .bigText(msg))
	        	.setContentText(msg)
	         .setDefaults(Notification.DEFAULT_ALL)
	         .setSound(notification)
	         .setAutoCancel(true);
	        
	        mBuilder.setContentIntent(contentIntent);
	        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    	}
    }
    
}