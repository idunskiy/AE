package com.activitygroups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.view.inputmethod.InputMethodManager;

import com.assignmentexpert.RegisterActivity;

public class TabGroup2Activity extends MainTabGroup{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		startChildActivity("RegisterActivity", new Intent(getParent(),RegisterActivity.class));
		
		
	//ListPreference list = (ListPreference)findPreference("registerCountry");
		
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
