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
import com.library.UserFunctions;
/** * AsyncTask для деактивации заказа*/
public class InactivateAsync extends AbstractTaskLoader{
	Context context ; 
	private boolean errorFlag = false;
	private static String KEY_STATUS = "status";
	protected InactivateAsync(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	/** *метод вызова выполнения запроса из активностей*/
	public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

		InactivateAsync loader = new InactivateAsync(fa);

		new TaskProgressDialogFragment.Builder(fa, loader, fa.getResources().getString(R.string.dialog_loading))
				.setCancelable(true)
				.setTaskLoaderListener(taskLoaderListener)
				.show();
	}
	@Override
	public Bundle getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		
	}
	/** *метод выполнения запроса*/
	@Override
	public Object loadInBackground() {
		UserFunctions func = new UserFunctions();
		 JSONObject as = null;
		 String result = "";
		try {
			as = func.deleteOrder(Integer.toString(DashboardActivityAlt.listItem.getOrderid()));
			Log.i("delete operation", as.toString());
		
		//deleteFlag = true;
		DashboardActivityAlt.listItem.getProcess_status().setProccessStatusTitle("Inactive");
		DashboardActivityAlt.listItem.getProcess_status().setProccessStatusId(9);
		String res = as.getString(KEY_STATUS);
	        if(Boolean.parseBoolean(res))
	        {
	        	result = "inactivate_success";
	        }
	        else
	        {
	        	result = "inactivate_error";
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 Crashlytics.logException(e);
			e.printStackTrace();
			errorFlag = true;
			onStopLoading();
		}
		
		return result;
	}
	@Override 
	 protected void onStopLoading() {
	        Log.i("InactivateAsync", "onStopLoading method");
	        this.setCanseled(true);
	        TaskProgressDialogFragment.cancel();
	        if(errorFlag)
	        new FrequentlyUsedMethods(context).someMethod("Something went wrong. Please try later.");
	        cancelLoad();
	    }

}
