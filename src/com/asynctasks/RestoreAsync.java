package com.asynctasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.assignmentexpert.LoginActivity;
import com.assignmentexpert.R;
import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.crashlytics.android.Crashlytics;
import com.library.FrequentlyUsedMethods;
import com.library.UserFunctions;
/** * AsyncTask восстановления пароля пользователя */
public class RestoreAsync  extends AbstractTaskLoader{
	private static String KEY_STATUS = "status";
	
	
	private static String KEY_MESSAGE = "message";
	private static String KEY_ERROR = "error";
	private Context context;
	private boolean errorFlag = false;
	public static String restoreRes =  "";
	protected RestoreAsync(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Bundle getArguments() {
		// TODO Auto-generated method stub
		return null;
	}
	/** *метод вызова выполнения запроса из активностей*/
	public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

		RestoreAsync loader = new RestoreAsync(fa);

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
			UserFunctions reg = new UserFunctions();
			String res = "";
	     	JSONObject a = null;
			try {
				a = reg.restorePassword(LoginActivity.restorePass);
				Log.i("restore email", LoginActivity.restorePass);
			
			
				try {
					String b = a.get(KEY_STATUS).toString();
					if(Boolean.parseBoolean(b))
						{
							restoreRes = "Restored successfully. A message has been send to"+"\r\n"+ LoginActivity.restorePass	;
							res = "restoreSuccess";
						}
					else 
					{
							 restoreRes = a.getString(KEY_ERROR);
							 res = "restoreError";
					}
				} catch (JSONException e) 
				{
					e.printStackTrace();
				}
			} catch (Exception e) {
				 Crashlytics.logException(e);
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorFlag = true;
				onStopLoading();
			}
	
	    return res;
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
