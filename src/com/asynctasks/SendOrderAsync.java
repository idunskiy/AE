package com.asynctasks;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.R;
import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.crashlytics.android.Crashlytics;
import com.library.FrequentlyUsedMethods;
/** * AsyncTask для посылки заказа.  */
public class SendOrderAsync extends AbstractTaskLoader{
	private Context context;
	private boolean errorFlag = false;
	protected SendOrderAsync(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public Bundle getArguments() {
		// TODO Auto-generated method stub
		return null;
	}
	/** *метод вызова выполнения запроса из активностей*/
	public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

		SendOrderAsync loader = new SendOrderAsync(fa);

	new TaskProgressDialogFragment.Builder(fa, loader, fa.getResources().getString(R.string.dialog_loading))
			.setCancelable(true)
			.setTaskLoaderListener(taskLoaderListener)
			.show();
	}

	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		
	}
	/** *метод выполнения запроса*/
	@Override
	public Object loadInBackground() {
			JSONObject response = null ;
	       		try {
	       			if (DashboardActivityAlt.listItem.getProduct().getProductType().equalsIgnoreCase("assignment"))
	       	       		{
//						response = userFunc.sendMessage(Integer.toString(DashboardActivityAlt.listItem.getCategory().getCategoryId()), 
//							DashboardActivityAlt.listItem.getDeadline().toString(),
//							Float.toString(DashboardActivityAlt.listItem.getPrice()), NewMessageActivity.newMessage, 
//							Integer.toString(DashboardActivityAlt.listItem.getOrderid()), FileManagerActivity.getFinalMessageFiles());
					 
	       		}
	       		else if(DashboardActivityAlt.listItem.getProduct().getProductType().equalsIgnoreCase("writing"))
	       		{
//	       			response = userFunc.sendMessage("0", 
//	           				DashboardActivityAlt.listItem.getDeadline().toString(),
//	           				Float.toString(DashboardActivityAlt.listItem.getPrice()), NewMessageActivity.newMessage, 
//	           				Integer.toString(DashboardActivityAlt.listItem.getOrderid()), FileManagerActivity.getFinalMessageFiles());
	       		}
	       		}
       		catch (Exception e) {
       		 Crashlytics.logException(e);
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorFlag  = true;
				onStopLoading();
			}
	     
	
	    return response;
	}
	@Override 
	 protected void onStopLoading() {
	        Log.i("LoginAsync", "onStopLoading method");
	        this.setCanseled(true);
	        TaskProgressDialogFragment.cancel();
	        if (errorFlag)
	        new FrequentlyUsedMethods(context).someMethod("Something went wrong. Please try later.");
	        cancelLoad();
	    }

}