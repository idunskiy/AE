package com.asynctasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.assignmentexpert.LoginActivity;
import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.library.UserFunctions;

public class RestoreAsync  extends AbstractTaskLoader{
	private static String KEY_STATUS = "status";
	private static String KEY_MESSAGE = "message";
	private static String KEY_EXCEPTION= "exception";
	protected RestoreAsync(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Bundle getArguments() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

		RestoreAsync loader = new RestoreAsync(fa);

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
			String res = "";
	     	JSONObject a = null;
			try {
				a = reg.restorePassword(LoginActivity.restorePass, null);
				Log.i("restore email", LoginActivity.restorePass);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				
				String b = a.get(KEY_STATUS).toString();
				
				if(Integer.parseInt(b)==1)
					{
						res = "A message has been send to"+"\r\n"+ LoginActivity.restorePass+ 
								"\r\n"+"Follow the link within message to restore password";
					}
				else 
				{
					String mainMess = "Restoring failed. Please try later";
					  res = mainMess;
				}
			} catch (JSONException e) 
			{
				e.printStackTrace();
			}
	     
	
	    return res;
	}

}
