package com.activitygroups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.assignmentexpert.ProfilePref;

public class ProfileGroup extends MainTabGroup {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		super.onCreate(savedInstanceState);
		startChildActivity("LoginActivity", new Intent(getParent(),ProfilePref.class));
	}
	@Override
	public void onResume()
	{
	
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		super.onResume();
		
	}
}
