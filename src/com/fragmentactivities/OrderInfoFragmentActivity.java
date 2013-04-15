package com.fragmentactivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.OrderInfoActivityAA;
import com.assignmentexpert.OrderInfoActivityEW;
import com.assignmentexpert.R;
import com.asynctaskbase.ITaskLoaderListener;
import com.fragments.IClickListener;
import com.fragments.OrderInfoFragmentAA;
import com.fragments.OrderInfoFragmentEW;
import com.fragments.ProfileFragmentPref;

public class OrderInfoFragmentActivity extends FragmentActivity implements ITaskLoaderListener, IClickListener{
	  private FragmentTransaction fragmentTransaction;

	public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.profile_fragment);
	      if (DashboardActivityAlt.listItem.getProduct().getProductType().equalsIgnoreCase("assignment"))
	        {
	    	  Fragment newFragment = new OrderInfoFragmentAA();
		      FragmentManager fragmentManager = getSupportFragmentManager();
		      fragmentTransaction = fragmentManager.beginTransaction();
		      fragmentTransaction.add(R.id.myfragment, newFragment);
		      fragmentTransaction.commit();
	        }
	 	   else if (DashboardActivityAlt.listItem.getProduct().getProductType().equalsIgnoreCase("writing"))
	 	   {

	 		  Fragment newFragment = new OrderInfoFragmentEW();
		      FragmentManager fragmentManager = getSupportFragmentManager();
		      fragmentTransaction = fragmentManager.beginTransaction();
		      fragmentTransaction.add(R.id.myfragment, newFragment);
		      fragmentTransaction.commit();
	       }

	     
	      
		}
	public void changeFragment(int flag) {
		// TODO Auto-generated method stub
		
	}

	public void onLoadFinished(Object data) {
		// TODO Auto-generated method stub
		
	}

	public void onCancelLoad() {
		// TODO Auto-generated method stub
		
	}

}
