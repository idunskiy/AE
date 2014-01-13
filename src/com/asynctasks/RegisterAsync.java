package com.asynctasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.assignmentexpert.R;
import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.crashlytics.android.Crashlytics;
import com.datamodel.Captcha;
import com.fragments.RegisterFragment;
import com.google.gson.Gson;
import com.library.DataParsing;
import com.library.UserFunctions;
import com.library.singletones.SharedPrefs;
/** * AsyncTask для регистрации пользователя. */
public class RegisterAsync  extends AbstractTaskLoader{
	Context context;
	private static String KEY_STATUS = "status";
	private static String KEY_EXCEPTION= "error";
	private static String KEY_DATA= "data";
	private boolean errorFlag = false;
	
	
	FragmentActivity _fa;
	ITaskLoaderListener _taskLoaderListener;
	public static Bitmap reCatcha;
	UserFunctions userFunc = new UserFunctions ();
	public static String RegisterAsyncError;
	protected RegisterAsync(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	/** *метод вызова выполнения запроса из активностей*/
	public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

		RegisterAsync loader = new RegisterAsync(fa);
		
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
		String res = "";
		UserFunctions reg = new UserFunctions();
        JSONObject a = null;
        Captcha captcha;
		try {
			
			
			
//			reg.getCaptcha().setWord(RegisterFragment.userCaptcha);
			
			Gson gson  = new Gson();
			   String json = SharedPrefs.getInstance().getSharedPrefs().getString("current_captcha", "");
			   Log.i("json", json);
			   captcha = gson.fromJson(json, Captcha.class);
			   captcha.setWord(RegisterFragment.userCaptcha);
			a = reg.registerUser(RegisterFragment.userName,RegisterFragment.userEmail,RegisterFragment.userPass,
					captcha,	RegisterFragment.userPhone);
			
		
        	String b;
			try
			{
				b = a.get(KEY_STATUS).toString();
				Log.i("registration result", b);
				if (Boolean.parseBoolean(b))
				{
					res = "success";
				}
				else 
				{
					//String errorMess = a.getString(KEY_MESSAGE).toString();
					String exceptionMess = a.getString(KEY_EXCEPTION).toString();
					String mainMess = "Registration failed. ";
					errorFlag =  true;
					if (a.getJSONObject(KEY_DATA)!=null)
					{
							res = "error";
							DataParsing dataParse = new DataParsing();
							RegisterAsyncError = a.getString(KEY_EXCEPTION).toString();
							reCatcha = userFunc.downloadCaptcha(dataParse.wrapCapthca(a));
					}
						
						
				}
			} 
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		} catch (Exception e1) {
			 Crashlytics.logException(e1);
			// TODO Auto-generated catch block
			e1.printStackTrace();
			errorFlag =  true;
		}

        return res;
	}
	@Override 
	 protected void onStopLoading() {
	        this.setCanseled(true);
	        TaskProgressDialogFragment.cancel();
	        cancelLoad();
	    }

}
