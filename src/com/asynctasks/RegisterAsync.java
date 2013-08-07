package com.asynctasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.fragments.RegisterFragment;
import com.library.FrequentlyUsedMethods;
import com.library.UserFunctions;
/** * AsyncTask дл€ регистрации пользовател€. */
public class RegisterAsync  extends AbstractTaskLoader{
	Context context;
	private static String KEY_STATUS = "status";
	private static String KEY_EXCEPTION= "exception";
	private boolean errorFlag = false;
	protected RegisterAsync(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	/** *метод вызова выполнени€ запроса из активностей*/
	public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

		RegisterAsync loader = new RegisterAsync(fa);
		
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
		String res = "";
		UserFunctions reg = new UserFunctions();
        JSONObject a = null;
		try {
			a = reg.registerUser(RegisterFragment.userName,RegisterFragment.userEmail,RegisterFragment.userPass,
					RegisterFragment.userConf,RegisterFragment.userCaptcha);
		
        	String b;
			try
			{
				b = a.get(KEY_STATUS).toString();
				if (Integer.parseInt(b)==1)
				{
					res = "Registration confirmation message was send to"+"\r\n"+ RegisterFragment.userEmail+ 
							"\r\n"+"Congratulations!";
					
				}
				else 
				{
					//String errorMess = a.getString(KEY_MESSAGE).toString();
					String exceptionMess = a.getString(KEY_EXCEPTION).toString();
					String mainMess = "Registration failed. ";
					if (exceptionMess.equalsIgnoreCase("PDOException"))
					  res = mainMess + "Duplicate entry "+ RegisterFragment.userEmail ;
					else
					  res = mainMess;
				}
			} 
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			errorFlag =  true;
			onStopLoading();
		}

        return res;
	}
	@Override 
	 protected void onStopLoading() {
	        Log.i("RegisterAsync", "onStopLoading method");
	        this.setCanseled(true);
	        TaskProgressDialogFragment.cancel();
	        if(errorFlag)
	        new FrequentlyUsedMethods(context).someMethod("Something went wrong. Please try later.");
	        cancelLoad();
	    }

}
