package com.asynctasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.assignmentexpert.ProfileActivity;
import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.library.FrequentlyUsedMethods;
import com.library.UserFunctions;
import com.paypal.android.MEP.PayPal;

public class ProfileUpdateAsync extends AbstractTaskLoader {
	Context context;
	private static String KEY_STATUS = "status";
	private static String KEY_MESSAGE = "message";
	public static String errorMessage;
	FrequentlyUsedMethods faq = new FrequentlyUsedMethods(context);
	protected ProfileUpdateAsync(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

		ProfileUpdateAsync loader = new ProfileUpdateAsync(fa);

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
		
		
		
		UserFunctions reg = new UserFunctions();
		String res = "";
     	JSONObject a = null;
		try {
			a = reg.profileUpdate(ProfileActivity.timeZone, ProfileActivity.firstName,ProfileActivity.lastName,ProfileActivity.phone,
					ProfileActivity.password);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			String b = a.get(KEY_STATUS).toString();
			
			if(Integer.parseInt(b)==1)
				{
					res = "success";
				}
			else 
			{
				errorMessage = a.getString(KEY_MESSAGE);
				 res = "error";
			}
		} catch (JSONException e) 
		{
			e.printStackTrace();
		}
     

    return res;
		
	}
}

