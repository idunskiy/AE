package com.activitygroups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.assignmentexpert.InteractionsActivityViewPager;
import com.assignmentexpert.NewMessageActivity;

public class InteractionTabGroup extends MainTabGroup {
	Context ctx;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
		super.onCreate(savedInstanceState);
		startChildActivity("InteractionsActivity", new Intent(getParent(),InteractionsActivityViewPager.class));
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
	public void setContext(Context context)
	{
		this.ctx = context;
	}
	
	public Context getContext()
	{return this.ctx;}
}