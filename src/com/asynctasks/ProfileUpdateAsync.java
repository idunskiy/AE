package com.asynctasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.assignmentexpert.R;
import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.crashlytics.android.Crashlytics;
import com.fragments.ProfileFragmentCompl;
import com.library.FrequentlyUsedMethods;
import com.library.UserFunctions;
/** * AsyncTask для обновления полей профиля пользователя. */
public class ProfileUpdateAsync extends AbstractTaskLoader {
	Context context;
	private static String KEY_STATUS = "status";
	private static String KEY_MESSAGE = "error";
	public static String errorMessage;
	private boolean errorFlag = false;
	FrequentlyUsedMethods faq = new FrequentlyUsedMethods(context);
	protected ProfileUpdateAsync(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	/** *метод вызова выполнения запроса из активностей*/
	public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

		ProfileUpdateAsync loader = new ProfileUpdateAsync(fa);

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
		
		
		
		UserFunctions reg = new UserFunctions();
		String res = "";
     	JSONObject a = null;
		try {
			a = reg.profileUpdate(ProfileFragmentCompl.timeZone, ProfileFragmentCompl.firstName,ProfileFragmentCompl.lastName,ProfileFragmentCompl.phone,
					ProfileFragmentCompl.password);
		
			try {
				
				String b = a.get(KEY_STATUS).toString();
				
				if(Boolean.parseBoolean(b))
					{
						res = "success";
					}
				else 
				{
					errorMessage = a.getString(KEY_MESSAGE);
					 res = "error";
				}
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
     
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 Crashlytics.logException(e);
			e.printStackTrace();
			errorFlag = true;
			onStopLoading();
		}
    return res;
		
	}
	
	@Override 
	 protected void onStopLoading() {
	        this.setCanseled(true);
	        TaskProgressDialogFragment.cancel();
	        if(errorFlag)
	        new FrequentlyUsedMethods(context).someMethod("Something went wrong. Please try later.");
	        cancelLoad();
	    }
}

