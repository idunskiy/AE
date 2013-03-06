package com.assignmentexpert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.TimeZone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.activitygroups.ProfileGroup;
import com.customitems.CustomEditPreference;
import com.library.FrequentlyUsedMethods;

public class ProfileCompl extends FragmentActivity{
	private CustomEditPreference counrtyEditPref;
	private CustomEditPreference timeZonePref;
	Dialog dialog;
	ArrayAdapter<String> adapter; 
	ArrayAdapter<String> modeAdapter;
	private Button cancelProfile;
	private Button saveProfile;
	private Button orderInfo;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_compl);
        timeZonePref = (CustomEditPreference)findViewById(R.id.timeZonePref);
        timeZonePref.setTitle("Time zone");
        
        counrtyEditPref = (CustomEditPreference)findViewById(R.id.counrtyEditPref);
		counrtyEditPref.setTitle("Country");
        counrtyEditPref.setSummary(Locale.getDefault().getDisplayCountry());
        //android.R.layout.simple_list_item_1, android.R.id.text1, countries);
        cancelProfile = (Button)findViewById(R.id.cancelProfile);
		saveProfile = (Button)findViewById(R.id.cancelProfile);
		
		saveProfile = (Button)findViewById(R.id.cancelProfile);
		orderInfo = (Button)findViewById(R.id.orderInfo);
		
		orderInfo.setOnClickListener(new View.OnClickListener() {
      	   
         public void onClick(View view) {
        	 
        	 Toast.makeText(ProfileCompl.this, "Info", Toast.LENGTH_LONG).show();

         }
		});
		
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
        
        
        adapter = new ArrayAdapter<String>(
    			this, android.R.layout.simple_spinner_item);
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
				Log.i("current timezone in a loop", TZ1.get(i));
			}
            Log.i("subject items", entries[i].toString() +  entryValues[i].toString());
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
}
