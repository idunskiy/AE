package com.asynctasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.assignmentexpert.RegisterActivity;
import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.library.UserFunctions;

public class RegisterAsync  extends AbstractTaskLoader{
	Context context;
	private static String KEY_STATUS = "status";
	private static String KEY_MESSAGE = "message";
	private static String KEY_EXCEPTION= "exception";
	protected RegisterAsync(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

		RegisterAsync loader = new RegisterAsync(fa);

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
		String res = "";
		UserFunctions reg = new UserFunctions();
        JSONObject a = null;
		try {
			a = reg.registerUser(RegisterActivity.userName,RegisterActivity.userEmail,RegisterActivity.userPass,RegisterActivity.userConf,RegisterActivity.userCaptcha);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        	String b;
			try
			{
				b = a.get(KEY_STATUS).toString();
				if (Integer.parseInt(b)==1)
				{
					res = "Registration confirmation message was send to"+"\r\n"+ RegisterActivity.userEmail+ 
							"\r\n"+"Congratulations!";
					
				}
				else 
				{
					//String errorMess = a.getString(KEY_MESSAGE).toString();
					String exceptionMess = a.getString(KEY_EXCEPTION).toString();
					String mainMess = "Registration failed. ";
					if (exceptionMess.equalsIgnoreCase("PDOException"))
					  res = mainMess + "Duplicate entry "+ RegisterActivity.userEmail ;
					else
					  res = mainMess;
				}
			} 
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
         

        return res;
	}

}
