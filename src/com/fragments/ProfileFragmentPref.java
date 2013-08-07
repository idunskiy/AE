package com.fragments;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.assignmentexpert.LoginActivity;
import com.assignmentexpert.R;
import com.customitems.CustomEditPreference;
import com.library.singletones.SharedPrefs;
/** * �������� ��� ����������� ���������� ������� ������������*/
public class ProfileFragmentPref  extends Fragment implements IClickListener {
	/** * CustomEditPreference ��� ����������� email'a ������������*/
	public static CustomEditPreference loginTextView;
	/** * CustomEditPreference ��� ����������� �������� ������������*/
	public static CustomEditPreference phoneTextView;
	View buttonFooter;
	/** * ������ ��� ��������� ������� ������������*/
	private Button profileEdit;
	/** * CustomEditPreference ��� ����������� ��������� ���� ������������*/
	public static CustomEditPreference timezonePref;
	/** * CustomEditPreference ��� ����������� ������ ������������*/
	public static CustomEditPreference countryPref;
	/** * CustomEditPreference ��� ������ ������������*/
	public static CustomEditPreference passwordPref;
	/** * CheckBox ��� ����� � ���������� ��� ����������� ����� login'a*/
	private CheckBox signMeCheck;
	/** * CustomEditPreference ��� ����� ������������*/
	public static CustomEditPreference firstNamePref;
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
	    return view;
	  }
	/** * ��������� ����������  IClickListener*/
	IClickListener listener;
    
    @Override
    public void onStart() {
        super.onStart();
        setFields();
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
	public void setFields()
	{
		firstNamePref.setSummary(LoginActivity.getUser.getUser().getUserFirstName());
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
