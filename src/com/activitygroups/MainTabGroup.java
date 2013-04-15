package com.activitygroups;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class MainTabGroup  extends ActivityGroup {

private ArrayList<String> mIdList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		final InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), imm.HIDE_NOT_ALWAYS);
		imm.hideSoftInputFromWindow(getWindow().getDecorView().getApplicationWindowToken(), imm.HIDE_NOT_ALWAYS);
		super.onCreate(savedInstanceState);
		if (mIdList == null) mIdList = new ArrayList<String>();
	}
	
	@Override
	public void onResume()
	{
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), imm.HIDE_NOT_ALWAYS);
		imm.hideSoftInputFromWindow(getWindow().getDecorView().getApplicationWindowToken(), imm.HIDE_NOT_ALWAYS);
		
		super.onResume();
	}

		/**
		* This is called when a child activity of this one calls its finish method.
		* This implementation calls {@link LocalActivityManager#destroyActivity} on the child activity
		* and starts the previous activity.
		* If the last child activity just called finish(),this activity (the parent),
		* calls finish to finish the entire group.
		*/
		@Override
		public void finishFromChild(Activity child) {
			LocalActivityManager manager = getLocalActivityManager();
			int index = mIdList.size()-1;
			
			if (index < 1) {
			finish();
			return;
			}
	
			manager.destroyActivity(mIdList.get(index), true);
			mIdList.remove(index);
			index--;
			String lastId = mIdList.get(index);
			Intent lastIntent = manager.getActivity(lastId).getIntent();
			
			InputMethodManager inputMethodManager = (InputMethodManager)  child.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(child.getCurrentFocus().getWindowToken(), 0);
			inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getApplicationWindowToken(), inputMethodManager.HIDE_NOT_ALWAYS);
			hideSoftKeyboard(child);
			
			Window newWindow = manager.startActivity(lastId, lastIntent);
			newWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			setContentView(newWindow.getDecorView());
			
		}
		
		public static void hideSoftKeyboard(Activity activity) {
		    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		}

/**
* Starts an Activity as a child Activity to this.
* @param Id Unique identifier of the activity to be started.
* @param intent The Intent describing the activity to be started.
* @throws android.content.ActivityNotFoundException.
*/
public void startChildActivity(String Id, Intent intent) 
{
	Activity  a =  getLocalActivityManager().getActivity(Id);
	
	Window window = getLocalActivityManager().startActivity(Id,intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	Log.i("ActivityGroup","startActivity method");
	
	if (window != null) {
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		mIdList.add(Id);
		Log.i("startActivity method","before setContent");
		setContentView(window.getDecorView());
		Log.i("startActivity method","after setContent");
	}
	
	
}

/**
* The primary purpose is to prevent systems before android.os.Build.VERSION_CODES.ECLAIR
* from calling their default KeyEvent.KEYCODE_BACK during onKeyDown.
*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		//preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
		return true;
		}
		return super.onKeyDown(keyCode, event);
	}

/**
* Overrides the default implementation for KeyEvent.KEYCODE_BACK
* so that all systems call onBackPressed().
*/
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		onBackPressed();
		return true;
		}
		return super.onKeyUp(keyCode, event);
	}

/**
* If a Child Activity handles KeyEvent.KEYCODE_BACK.
* Simply override and add this method.
*/
			@Override
			public void onBackPressed () {
			int length = mIdList.size();
			if ( length > 1) {
			Activity current = getLocalActivityManager().getActivity(mIdList.get(length-1));
			Log.i("MainTabGroup", "onBackPressed");
			current.finish();
			}
		}
}