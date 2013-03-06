package com.assignmentexpert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.ProfileUpdateAsync;
import com.library.FrequentlyUsedMethods;
import com.library.UserFunctions;

public class ProfileActivity  extends FragmentActivity implements ITaskLoaderListener{
	private Button btnNewOrder;
	private Button btnClose;
	private Button btnOrderHistory;
	private SharedPreferences sharedPreferences;
	private Editor editor;
	private Spinner timezoneTextView;
	private EditText passTextView;
	private TextView loginTextView;
	private EditText phoneTextView;
	private EditText lastnameTextView;
	private EditText firstnameTextView;
	UserFunctions launch = new UserFunctions();
	private Button btnSaveProfileUpdate;
	private Button btnCancelProfileUpdate;
	private Button btnEditProfile;
	private LinearLayout savingLayout;
	
	public static String firstName;
	public static String lastName;
	public static String password;
	public static String timeZone;
	public static String phone;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
//        btnOrderHistory = (Button) findViewById(R.id.btnOrdersHistory);
//        btnClose = (Button) findViewById(R.id.btnClose);
//        btnNewOrder = (Button) findViewById(R.id.btnNewOrder);
        btnSaveProfileUpdate = (Button) findViewById(R.id.btnSaveProfileUpdate);
        btnCancelProfileUpdate = (Button) findViewById(R.id.btnCancelProfileUpdate);
        btnEditProfile = (Button) findViewById(R.id.btnEditProfile);
        savingLayout = (LinearLayout)findViewById(R.id.savingLayout);
        savingLayout.setVisibility(View.GONE);
        FrequentlyUsedMethods faq = new FrequentlyUsedMethods(this);
        
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE );
		editor = sharedPreferences.edit();
		
		firstnameTextView = (EditText) findViewById(R.id.firstnameTextView);
		lastnameTextView = (EditText) findViewById(R.id.lastnameTextView);
		loginTextView = (TextView) findViewById(R.id.loginTextView);
		passTextView = (EditText)findViewById(R.id.passTextView);
		phoneTextView = (EditText)findViewById(R.id.phoneTextView);
		timezoneTextView = (Spinner)findViewById(R.id.timezoneProfileSpin);
        
		setFields();
		disableEdits();
//		faq.addTimeZones(timezoneTextView);
		ArrayAdapter<CharSequence> myAdap = (ArrayAdapter<CharSequence>) timezoneTextView.getAdapter(); //cast to an ArrayAdapter
		
		int spinnerPosition = myAdap.getPosition(LoginActivity.getUser.getUser().getTimeZone());
		timezoneTextView.setSelection(spinnerPosition);
		
		//set the default according to value
//		btnClose.setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View view) 
//			{
//            	//editor.putBoolean("logout", true);
//            	editor.remove("username");
//            	editor.remove("password");
//            	editor.remove("isChecked");
//            	editor.commit();
//            	try {
//					launch.logOut();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            	
//            	Intent i = new Intent(getApplicationContext(),
//                        LoginActivity.class);
//                startActivity(i);
//            } 
//			
//    	});
//		
//		   btnNewOrder.setOnClickListener(new View.OnClickListener() {
//	        	 
//	            public void onClick(View view) {
//	            	
//	                Intent i = new Intent(getApplicationContext(),
//	                        PreOrderActivity.class);
//	                startActivity(i);
//	            }
//	        });
//		   
//		   btnOrderHistory.setOnClickListener(new View.OnClickListener() {
//	        	 
//	            public void onClick(View view) {
//	            	
//	                Intent i = new Intent(getApplicationContext(),
//	                       DashboardActivityAlt.class);
//	                startActivity(i);
//	                
//	            }
//	        });
		   
		   btnSaveProfileUpdate.setOnClickListener(new View.OnClickListener() {
	        	 
	            public void onClick(View view) {
	            	firstName = firstnameTextView.getText().toString();
	            	lastName = lastnameTextView.getText().toString();
	            	password = passTextView.getText().toString();
	            	timeZone = timezoneTextView.getSelectedItem().toString();
	            	phone = phoneTextView.getText().toString();
	            	boolean errorFlag = false;
	            	
	            	Log.i("passw", password);
	            	
	            	if (!errorFlag)
	            	ProfileUpdateAsync.execute(ProfileActivity.this, ProfileActivity.this);
	                
	            }
	        });
		   
		   btnEditProfile.setOnClickListener(new View.OnClickListener() {
	        	 
	            public void onClick(View view) {
	            	
	            	
	            	enableEdits();
	                
	            }
	        });
		   
		   btnCancelProfileUpdate.setOnClickListener(new View.OnClickListener() {
	        	 
	            public void onClick(View view) {
	            	
	            	
	            	setFields();
	            	disableEdits();
	            }
	        });
   	   
	}
	public void setFields()
	{
		firstnameTextView.setText(LoginActivity.getUser.getUser().getUserFirstName());
		lastnameTextView.setText(LoginActivity.getUser.getUser().getUserLastName());
		loginTextView.setText(LoginActivity.getUser.getUser().getUserEmail());
		phoneTextView.setText(LoginActivity.getUser.getPhone());
		//timezoneTextView.setText(LoginActivity.getUser.getUser().getTimeZone());
		//passTextView.setText(LoginActivity.getUser.get);
		
	}
	private void enableEdits()
	{   firstnameTextView.setEnabled(true);
		lastnameTextView.setEnabled(true);
		passTextView.setEnabled(true);
		timezoneTextView.setEnabled(true);
		phoneTextView.setEnabled(true);
		btnEditProfile.setVisibility(View.GONE);
    	savingLayout.setVisibility(View.VISIBLE);
	}
	private void disableEdits()
	{
		firstnameTextView.setEnabled(false);
		lastnameTextView.setEnabled(false);
		passTextView.setEnabled(false);
		timezoneTextView.setEnabled(false);
		phoneTextView.setEnabled(false);
		btnEditProfile.setVisibility(View.VISIBLE);
    	savingLayout.setVisibility(View.GONE);
	}
	
	 @Override
	    public void onBackPressed() {
	        // Do Here what ever you want do on back press;
	    }
	 public void onLoadFinished(Object data) {
		if (data instanceof String & ((String)data).equalsIgnoreCase("success"))
			{
				Toast.makeText(this, "Your profile was updated successfully", Toast.LENGTH_LONG).show();
				firstnameTextView.setText(firstnameTextView.getText().toString());
				lastnameTextView.setText(lastnameTextView.getText().toString());
				loginTextView.setText(loginTextView.getText().toString());
				phoneTextView.setText(phoneTextView.getText().toString());
				//timezoneTextView.setText(LoginActivity.getUser.getUser().getTimeZone());
				//passTextView.setText(phoneTextView.getText().toString());
				disableEdits();
			}
		else if(data instanceof String & ((String)data).equalsIgnoreCase("error"))
			Toast.makeText(this, "Your profile wasn't updated. "+ ProfileUpdateAsync.errorMessage, Toast.LENGTH_LONG).show();
		
	}
	 public void onCancelLoad() {
		// TODO Auto-generated method stub
		
	 }
}
