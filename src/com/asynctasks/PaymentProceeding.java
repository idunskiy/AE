package com.asynctasks;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.assignmentexpert.DashboardActivityAlt;
import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.library.FrequentlyUsedMethods;
import com.library.UserFunctions;
/** * AsyncTask дл€ осуществлени€ платежа. */
public class PaymentProceeding  extends AbstractTaskLoader{
	Context context;
	private boolean errorFlag = false;
	protected PaymentProceeding(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	/** *метод вызова выполнени€ запроса из активностей*/
	public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

		PaymentProceeding loader = new PaymentProceeding(fa);

		new TaskProgressDialogFragment.Builder(fa, loader, "LoadingЕ")
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
	/** *метод выполнени€ запроса*/
	@Override
	public Object loadInBackground() {
		 UserFunctions func = new UserFunctions();
         try {
        	 func.sendPayment(Integer.toString(DashboardActivityAlt.listItem.getOrderid()),Float.toString(DashboardActivityAlt.listItem.getPrice()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorFlag = true;
			onStopLoading();
		}
		return null;
	}
	 @Override 
	 protected void onStopLoading() {
	        Log.i("LoginAsync", "onStopLoading method");
	        this.setCanseled(true);
	        TaskProgressDialogFragment.cancel();
	        if(errorFlag)
	        new FrequentlyUsedMethods(context).someMethod("Something went wrong. Please try later.");
	        cancelLoad();
	    }

}
