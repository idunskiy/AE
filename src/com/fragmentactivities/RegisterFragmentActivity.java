package com.fragmentactivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Toast;

import com.assignmentexpert.R;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.CaptchaAsync;
import com.asynctasks.RegisterAsync;
import com.fragments.IClickListener;
import com.fragments.RegisterFragment;
import com.fragments.RegisterFragmentCompl;

public class RegisterFragmentActivity extends FragmentActivity implements IClickListener, ITaskLoaderListener{
	
	private Button btnOne;
	Fragment registerFragment;
	 FragmentTransaction fragmentTransaction;
	 FragmentManager fragmentManager;
	@Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.profile_fragment);
      registerFragment = new RegisterFragment();
     
      fragmentManager = getSupportFragmentManager();
      fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.add(R.id.myfragment, registerFragment);
      fragmentTransaction.commit();
      CaptchaAsync.execute(this, this);
	}
	public void changeFragment(int flag) {
		if (flag==4)
		{
			 Fragment newFragment = new RegisterFragmentCompl();
			  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		      fragmentTransaction.replace(R.id.myfragment, newFragment);
		      //fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		      fragmentTransaction.commit();
		}
		if (flag==5)
		{
			RegisterAsync.execute(this, this);
		}
		
	}
	public void onLoadFinished(Object data) {
			 if (data instanceof Bitmap & data != null)
	    	 {
	    		 RegisterFragment.captcha.setImageBitmap((Bitmap)data);
	    	 }
			 if (data instanceof String)
			 {
				 Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show();
			 }
			
		
		
	}
	public void onCancelLoad() {
		// TODO Auto-generated method stub
		
	}
//	@Override
//	public void onBackPressed() {
//		Log.i("registerAct", "onBackPressed");
//		   Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.myfragment);
//		   if (fragment instanceof RegisterFragmentCompl) {
//			   Log.i("registerFragment", "onBackPressed");
//			   Fragment newFragment = new RegisterFragment();
//			      FragmentManager fragmentManager = getSupportFragmentManager();
//			      fragmentTransaction = fragmentManager.beginTransaction();
//			      fragmentTransaction.replace(R.id.myfragment, newFragment);
//			      fragmentTransaction.commit();
//		          return;
//		   }
//		  
//		}
	
	
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	 Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.myfragment);
        	if (fragment instanceof RegisterFragmentCompl) {
        	 Log.i("registerFragment", "onBackPressed");
        	 Fragment registerFragment2 = new RegisterFragment();
        	 Log.i("registerFragment", fragment.getClass().toString());
        	 fragmentTransaction.replace(R.id.myfragment, registerFragment2).commit();
        	 getSupportFragmentManager().executePendingTransactions();
        	  return true;
        	}
        	if (fragment instanceof RegisterFragment)
        	{
        		return true;
        	}
        }
        return false;//super.onKeyDown(keyCode, event);
    }
}