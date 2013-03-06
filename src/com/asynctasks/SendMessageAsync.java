package com.asynctasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.FileManagerActivity;
import com.assignmentexpert.LoginActivity;
import com.assignmentexpert.NewMessageActivity;
import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.library.UserFunctions;

public class SendMessageAsync extends AbstractTaskLoader{
	private static String KEY_STATUS = "status";
	private static String KEY_MESSAGE = "message";
	private static String KEY_EXCEPTION= "exception";
	protected SendMessageAsync(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Bundle getArguments() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

		SendMessageAsync loader = new SendMessageAsync(fa);

	new TaskProgressDialogFragment.Builder(fa, loader, "Loading…")
			.setCancelable(true)
			.setTaskLoaderListener(taskLoaderListener)
			.show();
	}

	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object loadInBackground() {
			UserFunctions reg = new UserFunctions();
			JSONObject response = null ;
			UserFunctions userFunc = new UserFunctions();
       		try {
       			if (DashboardActivityAlt.listItem.getProduct().getProductType().equalsIgnoreCase("assignment"))
       	       		{
					response = userFunc.sendMessage(Integer.toString(DashboardActivityAlt.listItem.getCategory().getCategoryId()), 
						DashboardActivityAlt.listItem.getDeadline().toString(),
						Float.toString(DashboardActivityAlt.listItem.getPrice()), NewMessageActivity.newMessage, 
						Integer.toString(DashboardActivityAlt.listItem.getOrderid()), FileManagerActivity.getFinalMessageFiles());
				 
       		}
       		else if(DashboardActivityAlt.listItem.getProduct().getProductType().equalsIgnoreCase("writing"))
       		{
       			response = userFunc.sendMessage("0", 
           				DashboardActivityAlt.listItem.getDeadline().toString(),
           				Float.toString(DashboardActivityAlt.listItem.getPrice()), NewMessageActivity.newMessage, 
           				Integer.toString(DashboardActivityAlt.listItem.getOrderid()), FileManagerActivity.getFinalMessageFiles());
       		}
       		}
       		catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     
	
	    return response;
	}

}
