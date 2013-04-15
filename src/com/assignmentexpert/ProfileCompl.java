package com.assignmentexpert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.TimeZone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.activitygroups.ProfileGroup;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.LoginAsync;
import com.asynctasks.ProfileUpdateAsync;
import com.customitems.CustomEditPreference;
import com.customitems.CustomEditText;
import com.library.FrequentlyUsedMethods;
import com.library.SharedPrefs;

public class ProfileCompl extends FragmentActivity implements ITaskLoaderListener{
	private CustomEditPreference counrtyEditPref;
	private CustomEditPreference timeZonePref;
	Dialog dialog;
	ArrayAdapter<String> adapter; 
	ArrayAdapter<String> modeAdapter;
	private Button cancelProfile;
	private Button saveProfile;
	private ImageView orderInfo;
	private CustomEditText firstnameTextView;
	private CustomEditText passTextView;
	private CustomEditText phoneTextView;
	private CheckBox signMeCheck;
	private SharedPreferences sharedPreferences;
	private Editor editor;
	private CustomEditText confPassTextView;
	
	
	public static String firstName;
	public static String lastName;
	public static String password;
	public static String timeZone;
	public static String phone;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_compl);
        timeZonePref = (CustomEditPreference)findViewById(R.id.timeZonePref);
        timeZonePref.setTitle("Time zone");
        
        counrtyEditPref = (CustomEditPreference)findViewById(R.id.counrtyEditPref);
		counrtyEditPref.setTitle("Country");
        counrtyEditPref.setSummary(Locale.getDefault().getDisplayCountry());
        cancelProfile = (Button)findViewById(R.id.cancelProfile);
		saveProfile = (Button)findViewById(R.id.saveProfile);
		saveProfile.getBackground().setAlpha(120);
		
		firstnameTextView = (CustomEditText)findViewById(R.id.firstnameTextView);
		passTextView = (CustomEditText)findViewById(R.id.passTextView);
		phoneTextView= (CustomEditText)findViewById(R.id.phoneTextView);
		confPassTextView = (CustomEditText)findViewById(R.id.confPassTextView);

		
		
		firstnameTextView.setText(LoginActivity.getUser.getUser().getUserFirstName());
		phoneTextView.setText(LoginActivity.getUser.getPhone());
		passTextView.setText(LoginActivity.forFragmentPassword);
		confPassTextView.setText(LoginActivity.forFragmentPassword);
		
		
		
		orderInfo = (ImageView)findViewById(R.id.orderInfo);
		
		orderInfo.setOnClickListener(new View.OnClickListener() {
      	   
         public void onClick(View view) {
        	 Dialog dialog = new Dialog(getParent(), R.style.FullHeightDialog);
         	TextView myMsg = new TextView(ProfileCompl.this);
         	myMsg.setText("We recommend you to leave a cell number so we could update you via SMS. We won’t call you or disclose this number.");
         	myMsg.setTextColor(Color.WHITE);
         	myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
         	dialog.setContentView(myMsg);
         	dialog.show();
     		dialog.setCanceledOnTouchOutside(true);
         }
		});
		
		 sharedPreferences = getSharedPreferences("user", MODE_PRIVATE );
	     editor = sharedPreferences.edit();
	     SharedPrefs.getInstance().Initialize(getApplicationContext());
	     signMeCheck = (CheckBox) findViewById(R.id.signMeCheck);
	     if ( SharedPrefs.getInstance().getSharedPrefs().getBoolean("isChecked", false) ==true)
	    	  signMeCheck.setChecked(true);
	     
	     
	     
	    
	     
		 signMeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
            {           
            	if (isChecked == true)
            	{
            		SharedPrefs.getInstance().writeBoolean("isChecked", true);
            	}
            	else
            	{
            		SharedPrefs.getInstance().writeBoolean("isChecked", false);
            	}
            	Log.i("isCHecked value", Boolean.toString(isChecked));
            }
            });
		
		if (!signMeCheck.isChecked())
		{
			SharedPrefs.getInstance().writeBoolean("isChecked", false);
		}
		else
		{
			SharedPrefs.getInstance().writeBoolean("isChecked", true);
		}
        counrtyEditPref.setOnClickListener(new View.OnClickListener() {
         	   
            public void onClick(View view) {
            			
            	AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
            	builder.setTitle("Select country");

            	ListView modeList = new ListView(getParent());
            	
            	Locale[] locales = Locale.getAvailableLocales();
    	        ArrayList<String> countries = new ArrayList<String>();
    	     
    	        for (Locale locale : locales) {
    	            String country = locale.getDisplayCountry();
    	            if (country.trim().length()>0 && !countries.contains(country)) {
    	                countries.add(country);
    	            }
    	        }
    	        Collections.sort(countries);
    	        modeAdapter= new ArrayAdapter<String>(getParent(), android.R.layout.simple_spinner_item,countries);
    	        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            	modeList.setAdapter(modeAdapter);
            	modeList.setBackgroundColor(Color.WHITE);
            	modeList.setCacheColorHint(Color.WHITE);
            	builder.setView(modeList);
            	dialog = builder.create();
            	dialog.show(); 
            	modeList.setOnItemClickListener(new OnItemClickListener() {
      	          public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    	          {
      	        	  
      	        	dialog.dismiss();
      	        	counrtyEditPref.setSummary(modeAdapter.getItem(position));
      	        	  
    	          }
            	});
//            	
            }
        });
        
        cancelProfile.setOnClickListener(new View.OnClickListener() {
	           public void onClick(View view) {
	        	   ProfileGroup parentActivity = (ProfileGroup)getParent();
	        	   parentActivity.onBackPressed();
	           }
	       });
        saveProfile.setOnClickListener(new View.OnClickListener() {
	           public void onClick(View view) {
	        	   firstName = firstnameTextView.getText().toString();
	            	lastName = "";
	            	password = passTextView.getText().toString();
	            	timeZone = timeZonePref.getSummary().toString();
	            	phone = phoneTextView.getText().toString();
	            	String confPass  = confPassTextView.getText().toString();
	            	boolean errorFlag = false;
	            	Log.i("passw", password);
	            	//if (!errorFlag)
	            	if (firstName.isEmpty() | firstName.length()<4)
	            	{
	            		errorFlag = true;
	            		firstnameTextView.getText().clear();
	            		Toast.makeText(ProfileCompl.this, "Put first name", Toast.LENGTH_LONG).show();
	            	}
	            	if (!password.equals(confPass))
	            	{
	            		errorFlag = true;
	            		confPassTextView.getText().clear();
	            		passTextView.getText().clear();
	            		Toast.makeText(ProfileCompl.this, "Passwords do not match", Toast.LENGTH_LONG).show();
	            	}
	            	if (password.length()<4)
	            	{
	            		errorFlag = true;
	            		confPassTextView.getText().clear();
	            		passTextView.getText().clear();
	            		Toast.makeText(ProfileCompl.this, "Password should be longer", Toast.LENGTH_LONG).show();
	            	}
	            	if (!errorFlag)
	            	{
	            		saveProfile.getBackground().setAlpha(255);
	            		ProfileUpdateAsync.execute(ProfileCompl.this, ProfileCompl.this);
	            	}
	           }
	       });
        
        adapter = new ArrayAdapter<String>(
    			this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addTimeZones();
        timeZonePref.setOnClickListener(new View.OnClickListener() {
       	   
            public void onClick(View view) {
            			
            	AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
            	builder.setTitle("Select time zone");

            	ListView modeList = new ListView(getParent());
            	modeList.setAdapter(adapter);
            	builder.setView(modeList);
            	dialog = builder.create();
            	dialog.show(); 
            	modeList.setBackgroundColor(Color.WHITE);
            	modeList.setCacheColorHint(Color.WHITE);
            	modeList.setOnItemClickListener(new OnItemClickListener() {
      	          public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    	          {
      	        	  
      	        	dialog.dismiss();
      	        	timeZonePref.setSummary(adapter.getItem(position));
      	        	  
    	          }
            	});
            	
            	
            }
        });
	}
	
	public void addTimeZones()
	{
		final String[] TZ = TimeZone.getAvailableIDs();
		final ArrayList<String> TZ1 = new ArrayList<String>();
		for (int i = 0; i < TZ.length; i++) {
			if (!(TZ1.contains(TimeZone.getTimeZone(TZ[i]).getID()))) {
				if (new FrequentlyUsedMethods(ProfileCompl.this).timezoneValidate(TZ[i]))
					TZ1.add(TZ[i]);
			}
		}
		for (int i = 0; i < TZ1.size(); i++) {
			
			
			adapter.add(TZ1.get(i));
		}
		CharSequence[] entries = new CharSequence[adapter.getCount()];
	    CharSequence[] entryValues = new CharSequence[adapter.getCount()];
	    int i = 0;
	    for (String dev : TZ1)
	    {
	    	entries[i] = dev;
            entryValues[i] = dev;
            if (TimeZone.getTimeZone(TZ1.get(i)).getID()
					.equals(TimeZone.getDefault().getID())) {
            	timeZonePref.setSummary((TimeZone.getDefault().getID()));
			}
            i++;
	    }
	    Locale locale = new Locale("en", "IN");
	    String curr = TimeZone.getTimeZone(TimeZone.getDefault().getID()).getDisplayName(false,TimeZone.SHORT, locale);
	    String currTimeZone = "";
	    for (int q = 0; q<TZ1.size();q++)
	    {
	    	if (TimeZone.getTimeZone(TZ1.get(q)).getDisplayName(false,TimeZone.SHORT, locale).equals(curr))
	    	currTimeZone = TZ1.get(q);
	    }
	    timeZonePref.setSummary(currTimeZone );
	    
	 
	}

	public void onLoadFinished(Object data) {
		if (data instanceof String & ((String)data).equalsIgnoreCase("success"))
			{
				Toast.makeText(this, "Your profile was updated successfully. Please relogin to see updatings.", Toast.LENGTH_LONG).show();
				firstnameTextView.setText(firstnameTextView.getText().toString());
				//lastnameTextView.setText(lastnameTextView.getText().toString());
				//loginTextView.setText(loginTextView.getText().toString());
				phoneTextView.setText(phoneTextView.getText().toString());
				//timezoneTextView.setText(LoginActivity.getUser.getUser().getTimeZone());
				//passTextView.setText(phoneTextView.getText().toString());
				//disableEdits();
			}
		else if(data instanceof String & ((String)data).equalsIgnoreCase("error"))
			Toast.makeText(this, "Your profile wasn't updated. "+ ProfileUpdateAsync.errorMessage, Toast.LENGTH_LONG).show();
		
	}

	public void onCancelLoad() {
		// TODO Auto-generated method stub
		
	}
	@Override
    public void onBackPressed() {
		
		  ProfileGroup parentActivity = (ProfileGroup)getParent();
		  parentActivity.onBackPressed();
    }
}
