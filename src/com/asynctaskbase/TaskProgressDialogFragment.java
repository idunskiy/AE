package com.asynctaskbase;

import java.util.UUID;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
/** *класс, реализующий диалог, отображаемый при выполнении AsyncTask'a .*/
public class TaskProgressDialogFragment extends
		AbstractTaskProgressDialogFragment {
		
	private ITaskLoaderListener taskLoaderListener;
	private final Handler handler = new Handler();
	 static TaskProgressDialogFragment fragment;
	 
	 public static boolean dialogIsProceeding = false;
	/**
	 * статический хелпер для создания и запуска загрузчика. 
	 *
	 */
	public static class Builder{
		FragmentActivity fa;
		AbstractTaskLoader loader;
		ITaskLoaderListener taskLoaderListener;
		Boolean cancelable;
		String progressMsg;
		
		public Builder(FragmentActivity fa, AbstractTaskLoader loader,String progressMsg){
			this.fa = fa;
			this.loader = loader;
			this.progressMsg = progressMsg;
		}
		
		public Builder setTaskLoaderListener(ITaskLoaderListener taskLoaderListener){
			this.taskLoaderListener = taskLoaderListener;
			return this;
		}
		
		public Builder setCancelable(Boolean cancelable){
			this.cancelable = cancelable;
			return this;
		}
		
		public void show(){
		
			String TAG_FRAGMENT = UUID.randomUUID().toString();
			
			//remove prev if exists
			FragmentManager fm = fa.getSupportFragmentManager();
			
			FragmentTransaction ft = fm.beginTransaction();
	        Fragment prev = fm.findFragmentByTag(TAG_FRAGMENT);
	        if (prev != null) {
	            ft.remove(prev);
	        }
	        //create dlg fragment
	        fragment = new TaskProgressDialogFragment(loader,progressMsg);
	        fragment.setTaskLoaderListener(taskLoaderListener);
	        fragment.setCancelable(cancelable);
	        
	        Bundle args = new Bundle();
	        args.putString("message", progressMsg);
	        fragment.setArguments(args);
	        fragment.show(ft, TAG_FRAGMENT);
	        dialogIsProceeding = true;
	        
		}
		
		
	}
	public static void cancel()
	{
		fragment.dismiss();
		dialogIsProceeding = false;
	}
		
	protected TaskProgressDialogFragment(AbstractTaskLoader loader,	String message) {
		super(loader, message);
	}

	protected void setTaskLoaderListener(ITaskLoaderListener taskLoaderListener){
		this.taskLoaderListener = taskLoaderListener;
	}
	
	@Override
	protected void onLoadComplete(final Object data) {
		
		if(taskLoaderListener!=null){
			handler.post(new Runnable() {
				
				public void run() {
					taskLoaderListener.onLoadFinished(data);
					dialogIsProceeding = false;
				}
			});
		}
	}
	@Override
	protected void onCancelLoad() {
		if (taskLoaderListener != null) {
			handler.post(new Runnable() {
				public void run() {
					taskLoaderListener.onCancelLoad();
				}
			});

		}

	}

}
