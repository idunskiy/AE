package com.activitygroups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.OrderInfoActivityAA;
import com.assignmentexpert.OrderInfoActivityEW;

public class OrderInfoTabGroup  extends MainTabGroup {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		super.onCreate(savedInstanceState);
	    if (DashboardActivityAlt.listItem.getProduct().getProductType().equalsIgnoreCase("assignment"))
        {
	    	startChildActivity("LoginActivity", new Intent(getParent(),OrderInfoActivityAA.class));
        }
 	   else if (DashboardActivityAlt.listItem.getProduct().getProductType().equalsIgnoreCase("writing"))
 	   {

 			startChildActivity("LoginActivity", new Intent(getParent(),OrderInfoActivityEW.class));
       }

	
	}
	@Override
	public void onResume()
	{
	
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
		super.onResume();
		
	}
}