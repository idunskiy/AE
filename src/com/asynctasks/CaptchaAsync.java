package com.asynctasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.library.FrequentlyUsedMethods;
import com.library.UserFunctions;
/** * AsyncTask дл€ загрузки каптчи*/
public class CaptchaAsync extends AbstractTaskLoader{
	Context context;
	private boolean errorFlag  = false;
	protected CaptchaAsync(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	/** *метод вызова выполнени€ запроса из активностей*/
	public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

		CaptchaAsync loader = new CaptchaAsync(fa);

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
			 UserFunctions a = new UserFunctions();
	    	 Bitmap bitmap = null;
	    	 Bitmap output = null;
	    	 try {
				  bitmap = a.getCaptcha();
				  output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
			                .getHeight(), Config.ARGB_8888);
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
				
				e1.printStackTrace();
				errorFlag = true;
				onStopLoading();
			}
	    	 
	    	 return output;
	 }
	@Override 
	 protected void onStopLoading() {
	        Log.i("CaptchaAsync", "onStopLoading method");
	        this.setCanseled(true);
	        TaskProgressDialogFragment.cancel();
	        if (errorFlag)
	        new FrequentlyUsedMethods(context).someMethod("Captcha downloading failed. Please try later.");
	        cancelLoad();
	    }
}
