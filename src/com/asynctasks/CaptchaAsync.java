package com.asynctasks;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.assignmentexpert.R;
import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.crashlytics.android.Crashlytics;
import com.datamodel.Captcha;
import com.google.gson.Gson;
import com.library.UserFunctions;
import com.library.singletones.SharedPrefs;
/** * AsyncTask для загрузки каптчи*/
public class CaptchaAsync extends AbstractTaskLoader{
	Context context;
	private static Bitmap bp;
	private boolean errorFlag  = false;
	public CaptchaAsync(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	/** *метод вызова выполнения запроса из активностей*/
	public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

		CaptchaAsync loader = new CaptchaAsync(fa);
		
		new TaskProgressDialogFragment.Builder(fa, loader, fa.getResources().getString(R.string.dialog_loading))
				.setCancelable(false)
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
			 UserFunctions a = new UserFunctions();
	    	 Bitmap output = null;
	    	 Captcha captcha  = null;
	    	 try {
	    		 
	    		 a.getCaptchaFromServer();
	    		 
	    		 captcha = a.getCaptcha();
	    		
	    		 JSONObject manJson = new JSONObject();
	    		 manJson.put("id", captcha.getId());
	    		 manJson.put("image",captcha.getImage());
	    		 Log.i("manJson.toString()",manJson.toString());
	    		 SharedPrefs.getInstance().getSharedPrefs().edit().putString("current_captcha",manJson.toString()).commit();
				  URL url = new URL(captcha.getImage());
			         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			         connection.addRequestProperty("Authorization", "Basic MWE6MmI=");
			         connection.setDoInput(true);
			         connection.connect();
			         InputStream input = connection.getInputStream();
			      Bitmap bitmap = BitmapFactory.decodeStream(input);
				  output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
			                .getHeight(), Config.ARGB_8888);
				  Log.i("bitmap size", Integer.toString(output.getWidth()));
			        Canvas canvas = new Canvas(output);
			       
			        final int color = 0xff424242;
			        final Paint paint = new Paint();
			        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			        final RectF rectF = new RectF(rect);
			        final float roundPx = 3;
			        paint.setAntiAlias(true);
			        canvas.drawARGB(0, 0, 0, 0);
			        paint.setColor(color);
			        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			        canvas.drawBitmap(bitmap, rect, rect, paint);
			        output = Bitmap.createScaledBitmap(output, output.getWidth(), 45, true );
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				 Crashlytics.logException(e1);
				e1.printStackTrace();
				errorFlag = true;
			}
	    	 bp = output;
	    	 return output;
	 }
	public static Bitmap returnBitmap()
	{
		return bp;
	}
	@Override 
	 protected void onStopLoading() {
	        Log.i("CaptchaAsync", "onStopLoading method");
//	        this.setCanseled(true);
//	        TaskProgressDialogFragment.cancel();
//	        if (errorFlag)
//	        	new FrequentlyUsedMethods(context).someMethod("Captcha downloading failed. Please try later.");
//	        cancelLoad();
	    }
}
