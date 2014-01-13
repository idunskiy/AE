package com.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.assignmentexpert.LoginActivity;
import com.assignmentexpert.R;
import com.asynctasks.LoginAsync;
import com.customitems.CustomEditPreference;
import com.library.singletones.SharedPrefs;
/** * �������� ��� ����������� ���������� ������� ������������*/
public class ProfileFragmentPref  extends Fragment implements IClickListener {
	/** * CustomEditPreference ��� ����������� email'a ������������*/
	  CustomEditPreference loginTextView;
	/** * CustomEditPreference ��� ����������� �������� ������������*/
	  CustomEditPreference phoneTextView;
	View buttonFooter;
	/** * ������ ��� ��������� ������� ������������*/
	private Button profileEdit;
	/** * CustomEditPreference ��� ����������� ��������� ���� ������������*/
	  CustomEditPreference timezonePref;
	/** * CustomEditPreference ��� ����������� ������ ������������*/
	  CustomEditPreference countryPref;
	/** * CustomEditPreference ��� ������ ������������*/
	  CustomEditPreference passwordPref;
	/** * CheckBox ��� ����� � ���������� ��� ����������� ����� login'a*/
	private CheckBox signMeCheck;
	/** * CustomEditPreference ��� ����� ������������*/
	  CustomEditPreference firstNamePref;
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.profile_footer,
	        container, false);
	    profileEdit = (Button) view.findViewById(R.id.profileEdit);
	    loginTextView = (CustomEditPreference) view.findViewById(R.id.loginPref);
	    firstNamePref = (CustomEditPreference) view.findViewById(R.id.firstNamePref);
		phoneTextView = (CustomEditPreference) view.findViewById(R.id.phonePref);
		timezonePref = (CustomEditPreference) view.findViewById(R.id.timezonePref);
		countryPref  = (CustomEditPreference) view.findViewById(R.id.countryPref);
		passwordPref= (CustomEditPreference)  view.findViewById(R.id.passwordPref);
		
	    profileEdit.setOnClickListener(new View.OnClickListener() {
	           public void onClick(View view) {
	        	    listener.changeFragment(1);
	           }
	       });
	    signMeCheck = (CheckBox)view.findViewById(R.id.signMeCheck);
		 if ( SharedPrefs.getInstance().getSharedPrefs().getBoolean("isChecked", false) ==true)
	    	  signMeCheck.setChecked(true);
		 if (getArguments()!=null)
		 {
		  String profile_name = getArguments().getString("profile_name");    
		  String profile_phone = getArguments().getString("profile_phone");      
		  String profile_pass = getArguments().getString("profile_pass");  
		  if (profile_name!=null & profile_phone!=null& profile_pass!=null)
			  setFields(profile_name,profile_phone,profile_pass);
		 }
		  else
			  initializeFields();
		 
	    return view;
	  }
	/** * ��������� ����������  IClickListener*/
	IClickListener listener;
    
    @Override
    public void onStart() {
        super.onStart();
       
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        	
            listener = (IClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    /** *����� ��������� �������� � ��������������� ����*/
	public void initializeFields()
	{
		
		firstNamePref.setSummary(LoginAsync.user.getFirst_name());
		loginTextView.setSummary(LoginAsync.user.getEmail());
		phoneTextView.setSummary(LoginAsync.user.getPhone());
		String s = "";
		for (int i=0;i<LoginActivity.currentPass.length();i++)
		{
			s += "*";
		}
		passwordPref.setSummary(s);
	}
	public void setFields(String name, String phone, String pass)
	{
		
		firstNamePref.setSummary(name);
		phoneTextView.setSummary(phone);

		String s = "";
		for (int i=0;i<pass.length();i++)
		{
			s += "*";
		}
		passwordPref.setSummary(s);
	}
	
//	
//	private void disableEdits()
//	{
////		firstnameTextView.setEnabled(false);
////		phoneTextView.setEnabled(false);
////		phoneTextView.setEnabled(false);
////		passTextView.setEnabled(false);
////		timezoneTextView.setEnabled(false);
////		btnEditProfile.setVisibility(View.VISIBLE);
////    	savingLayout.setVisibility(View.GONE);
//	}
//	 @Override
//	    public void onResume() {
//		 InputMethodManager imm = (InputMethodManager)getSystemService(
//			      Context.INPUT_METHOD_SERVICE);
//	     imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//		 super.onResume();
//	    }
//	
//	 @Override
//	    public void onBackPressed() {
//	        // Do Here what ever you want do on back press;
//	    }
	
	
	public void changeFragment(int flag) {
		
	}
}
