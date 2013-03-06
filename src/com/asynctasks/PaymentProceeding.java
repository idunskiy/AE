package com.asynctasks;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.assignmentexpert.DashboardActivityAlt;
import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.library.UserFunctions;

public class PaymentProceeding  extends AbstractTaskLoader{
	Context context;
	protected PaymentProceeding(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

		PaymentProceeding loader = new PaymentProceeding(fa);

		new TaskProgressDialogFragment.Builder(fa, loader, "Loading…")
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

	@Override
	public Object loadInBackground() {
		 UserFunctions func = new UserFunctions();
		 JSONObject res =  null;
         try {
        	 res = 	func.sendPayment(Integer.toString(DashboardActivityAlt.listItem.getOrderid()),Float.toString(DashboardActivityAlt.listItem.getPrice()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         Log.i("payment was proceeded", res.toString());
		return null;
	}

}
