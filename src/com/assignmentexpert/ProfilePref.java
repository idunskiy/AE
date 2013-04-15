package com.assignmentexpert;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.activitygroups.MainTabGroup;
import com.library.SharedPrefs;

public class ProfilePref  extends PreferenceActivity {
	private ListPreference firstnameTextView;
	private ListPreference lastnameTextView;
	private ListPreference loginTextView;
	private ListPreference phoneTextView;
	View buttonFooter;
	private ListView listView;
	private View listFHeader;
	private Button profileEdit;
	private ListPreference timezonePref;
	private ListPreference countryPref;
	private ListPreference passwordPref;
	private CheckBox signMeCheck;
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
			InputMethodManager imm = (InputMethodManager)getSystemService(
				      Context.INPUT_METHOD_SERVICE);
		    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.profile);
            //setContentView(R.layout.profile_footer);
            firstnameTextView = (ListPreference) findPreference("firstNamePref");
			loginTextView = (ListPreference) findPreference("loginPref");
			phoneTextView = (ListPreference) findPreference("phonePref");
			timezonePref = (ListPreference) findPreference("timezonePref");
			countryPref  = (ListPreference) findPreference("countryPref");
			passwordPref= (ListPreference) findPreference("passwordPref");
			
			
			buttonFooter = getLayoutInflater().inflate(R.layout.profile_footer, null);
			listFHeader = getLayoutInflater().inflate(R.layout.profile_header, null);

			signMeCheck = (CheckBox)buttonFooter.findViewById(R.id.signMeCheck);
			 if ( SharedPrefs.getInstance().getSharedPrefs().getBoolean("isChecked", false) ==true)
		    	  signMeCheck.setChecked(true);
			
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
		timezonePref.setSummary(LoginActivity.getUser.getUser().getTimeZone());
		countryPref.setSummary(Locale.getDefault().getDisplayCountry());
		String s = "";
		for (int i=0;i<LoginActivity.currentPass.length();i++)
		{
			s += "*";
		}
		passwordPref.setSummary(s);
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
		 InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
	     imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		 super.onResume();
	    }
	
	 @Override
	    public void onBackPressed() {
	        // Do Here what ever you want do on back press;
	    }
}
