package com.asynctasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.FileManagerActivity;
import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.fragments.NewMessageFragment;
import com.library.FrequentlyUsedMethods;
import com.library.UserFunctions;
/** * AsyncTask дл€ посылки сообщени€ по заказу.  */
public class SendMessageAsync extends AbstractTaskLoader{
	private static String KEY_STATUS = "status";
	public static String messageErrorMess;
	private Context context;
	private boolean errorFlag = false;
	protected SendMessageAsync(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public Bundle getArguments() {
		// TODO Auto-generated method stub
		return null;
	}
	/** *метод вызова выполнени€ запроса из активностей*/
	public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

		SendMessageAsync loader = new SendMessageAsync(fa);

	new TaskProgressDialogFragment.Builder(fa, loader, "LoadingЕ")
			.setCancelable(true)
			.setTaskLoaderListener(taskLoaderListener)
			.show();
	}

	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		
	}
	/** *метод выполнени€ запроса*/
	@Override
	public Object loadInBackground() {
			JSONObject response = null ;
			UserFunctions userFunc = new UserFunctions();
			String result = "";
       		try {
       			if (DashboardActivityAlt.listItem.getProduct().getProductType().equalsIgnoreCase("assignment"))
       	       		{
					response = userFunc.sendMessage(Integer.toString(DashboardActivityAlt.listItem.getCategory().getCategoryId()), 
						DashboardActivityAlt.listItem.getDeadline().toString(),
						Float.toString(DashboardActivityAlt.listItem.getPrice()), NewMessageFragment.newMessage, 
						Integer.toString(DashboardActivityAlt.listItem.getOrderid()), FileManagerActivity.getFinalMessageFiles());
				 
       		}
       		else if(DashboardActivityAlt.listItem.getProduct().getProductType().equalsIgnoreCase("writing"))
       		{
       			response = userFunc.sendMessage("0", 
           				DashboardActivityAlt.listItem.getDeadline().toString(),
           				Float.toString(DashboardActivityAlt.listItem.getPrice()), NewMessageFragment.newMessage, 
           				Integer.toString(DashboardActivityAlt.listItem.getOrderid()), FileManagerActivity.getFinalMessageFiles());
       		}
       		
       		try {
				if ((response.getString(KEY_STATUS) != null))
				{
					
				    String res = response.getString(KEY_STATUS);
				    if(Integer.parseInt(res) == 1)
				    {	
				    	result = "send_message_success";
				    	
				    }
				    else 
				    {
				    	Log.i("SendMessageAsync", "key status is 0");
				    	result = "send_message_error";
//				    	messageErrorMess ="Something went wrong. Please try later.";
//				    	errorFlag = true;
//				    	this.setCanseled(true);
//	             		TaskProgressDialogFragment.cancel();
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
       		catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorFlag = true;
				onStopLoading();
			}
       	 return result;
	}
	@Override 
	 protected void onStopLoading() {
	        Log.i("SendMessageAsync", "onStopLoading method");
	        this.setCanseled(true);
	        TaskProgressDialogFragment.cancel();
	        if(errorFlag)
	        new FrequentlyUsedMethods(context).someMethod("Something went wrong. Please try later.");
	    }

}
