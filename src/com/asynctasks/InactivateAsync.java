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

public class InactivateAsync extends AbstractTaskLoader{
	Context context ; 
	protected InactivateAsync(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

		InactivateAsync loader = new InactivateAsync(fa);

		new TaskProgressDialogFragment.Builder(fa, loader, "Loading�")
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
		 JSONObject as = null;
		try {
			as = func.deleteOrder(Integer.toString(DashboardActivityAlt.listItem.getOrderid()));
			Log.i("delete operation", as.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//deleteFlag = true;
		DashboardActivityAlt.listItem.getProcess_status().setProccessStatusTitle("Inactive");
		DashboardActivityAlt.listItem.getProcess_status().setProccessStatusId(9);
		return as;
	}

}