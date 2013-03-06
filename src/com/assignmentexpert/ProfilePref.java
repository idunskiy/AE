package com.assignmentexpert;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;

import com.activitygroups.MainTabGroup;

public class ProfilePref  extends PreferenceActivity {
	private ListPreference firstnameTextView;
	private ListPreference lastnameTextView;
	private ListPreference loginTextView;
	private ListPreference phoneTextView;
	View buttonFooter;
	private ListView listView;
	private View listFHeader;
	private Button profileEdit;
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
		
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.profile);
            //setContentView(R.layout.profile_footer);
            firstnameTextView = (ListPreference) findPreference("firstNamePref");
			loginTextView = (ListPreference) findPreference("loginPref");
			phoneTextView = (ListPreference) findPreference("phonePref");
			InputMethodManager imm = (InputMethodManager)getSystemService(
				      Context.INPUT_METHOD_SERVICE);
		    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
			
			buttonFooter = getLayoutInflater().inflate(R.layout.profile_footer, null);
			listFHeader = getLayoutInflater().inflate(R.layout.profile_header, null);
//			LinearLayout.LayoutParams footParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//					100);
//			buttonFooter.setLayoutParams(footParam);
			listView  = getListView();
			listView.addHeaderView(listFHeader);
			listView.addFooterView(buttonFooter);
			profileEdit = (Button)buttonFooter.findViewById(R.id.profileEdit);
			setFields();
		   disableEdits();
		   
		   profileEdit.setOnClickListener(new View.OnClickListener() {
	    	   
	           public void onClick(View view) {
	        	  // Intent frequentMessages = new Intent(getParent(), ProfilePrefCompl.class);
	        	     Intent frequentMessages = new Intent(getParent(), ProfileCompl.class);
		             MainTabGroup parentActivity = (MainTabGroup)getParent();
		             parentActivity.startChildActivity("FrequentMessageActivity", frequentMessages);

	               
	           }
	       });
	}
	public void setFields()
	{
		firstnameTextView.setSummary(LoginActivity.getUser.getUser().getUserFirstName());
		loginTextView.setSummary(LoginActivity.getUser.getUser().getUserEmail());
		phoneTextView.setSummary(LoginActivity.getUser.getPhone());
		//timezoneTextView.setText(LoginActivity.getUser.getUser().getTimeZone());
		//passTextView.setText(LoginActivity.getUser.get);
		
	}
	private void enableEdits()
	{  
		firstnameTextView.setEnabled(true);
		lastnameTextView.setEnabled(true);
		phoneTextView.setEnabled(true);
//		passTextView.setEnabled(true);
//		timezoneTextView.setEnabled(true);
//		btnEditProfile.setVisibility(View.GONE);
//    	savingLayout.setVisibility(View.VISIBLE);
	}
	private void disableEdits()
	{
//		firstnameTextView.setEnabled(false);
//		phoneTextView.setEnabled(false);
//		phoneTextView.setEnabled(false);
//		passTextView.setEnabled(false);
//		timezoneTextView.setEnabled(false);
//		btnEditProfile.setVisibility(View.VISIBLE);
//    	savingLayout.setVisibility(View.GONE);
	}
	 @Override
	    public void onResume() {
		 
		 super.onResume();
		 InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	        // Do Here what ever you want do on back press;
	    }
	
	 @Override
	    public void onBackPressed() {
	        // Do Here what ever you want do on back press;
	    }
}
