package com.asynctasks;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.fragments.InteractionFragment;
import com.library.FrequentlyUsedMethods;
import com.paypal.android.MEP.PayPal;

public class PayPalAsync  extends AbstractTaskLoader {
	Context context;
	FrequentlyUsedMethods faq = new FrequentlyUsedMethods(context);
	private boolean errorFlag = false;
	protected PayPalAsync(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

		PayPalAsync loader = new PayPalAsync(fa);

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
		Integer result = 0;
			try{
				
			boolean res =faq.initLibrary(context);
			Log.i("PayPal init", Boolean.toString(res));
			// The library is initialized so let's create our CheckoutButton and update the UI.
			if (PayPal.getInstance().isLibraryInitialized()) {
				result = (1);
				Log.i("PayPalAsync curr Act", ((Activity)context).getClass().toString());
				
				//faq.setupButtons(context, InteractionFragment.panelInteractions);
			}
			else {
				
				this.setCanseled(true);
		        TaskProgressDialogFragment.cancel();
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
			return result;
		}

   }
