package com.assignmentexpert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.activitygroups.ProfileGroup;
import com.library.FrequentlyUsedMethods;

public class ProfilePrefCompl extends PreferenceActivity {
	private EditTextPreference firstnameTextView;
	private EditTextPreference lastnameTextView;
	private EditTextPreference loginTextView;
	private EditTextPreference phoneTextView;
	View buttonFooter;
	private ListView listView;
	private View listFHeader;
	private ListPreference profileCountry;
	private ListPreference profileTimezone;
	private Button cancelProfile;
	private Button saveProfile;
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
		
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.profile_compl);
            //setContentView(R.layout.profile_footer);
          //  firstnameTextView = (EditTextPreference) findPreference("firstNamePref");
		//	loginTextView = (EditTextPreference) findPreference("loginPref");
		//	phoneTextView = (EditTextPreference) findPreference("phonePref");
			
			
			buttonFooter = getLayoutInflater().inflate(R.layout.profile_compl_footer, null);
			listFHeader = getLayoutInflater().inflate(R.layout.profile_compl_header, null);
			listView  = getListView();
			listView.addHeaderView(listFHeader);
			listView.addFooterView(buttonFooter);
			
			profileCountry = (ListPreference)findPreference("profileCountry");
			profileTimezone = (ListPreference)findPreference("profileTimezone");
			
			cancelProfile = (Button)buttonFooter.findViewById(R.id.cancelProfile);
			saveProfile = (Button)buttonFooter.findViewById(R.id.cancelProfile);
			
			
			//new FrequentlyUsedMethods(this).addTimeZones(profileTimezone);
			
			//setFields();
		  // disableEdits();
			
			 final ListPreference registerCountry = new ListPreference(new FrequentlyUsedMethods(this).getDialogContext(ProfilePrefCompl.this));
				
			 
			String currLocale = this.getResources().getConfiguration().locale.getCountry();
			
			registerCountry.setSummary(currLocale);
			registerCountry.setTitle("Country");
			
			
			Locale[] locales = Locale.getAvailableLocales();
	        ArrayList<String> countries = new ArrayList<String>();
	       
	        for (Locale locale : locales) {
	            String country = locale.getDisplayCountry();
	            if (country.trim().length()>0 && !countries.contains(country)) {
	                countries.add(country);
	            }
	        }
	        Collections.sort(countries);
	        CharSequence[] chars = countries.toArray(new CharSequence[countries.size()]);
	        System.out.println( "# countries found: " + chars.length);
	        for (CharSequence a : chars)
	        {
	        	Log.i("got countries", a.toString());
	        }
	        registerCountry.setEntries(chars);
	        registerCountry.setEntryValues(chars);

	        
	        getPreferenceScreen().addPreference(registerCountry);
	        
			profileTimezone.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	                public boolean onPreferenceChange(Preference preference, Object newValue) {
	                	int index = profileTimezone.findIndexOfValue(newValue.toString()) ;
	                	profileTimezone.setSummary(profileTimezone.getEntries()[index]);
	                	return true;
	                }

	            });
			
			cancelProfile.setOnClickListener(new View.OnClickListener() {
		    	   
		           public void onClick(View view) {
		        	   
		        	   ProfileGroup parentActivity = (ProfileGroup)getParent();
		        	   parentActivity.onBackPressed();
			      

		               
		           }
		       });
			
			CharSequence[] ens = profileTimezone.getEntries();
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
	{   firstnameTextView.setEnabled(true);
		lastnameTextView.setEnabled(true);
		phoneTextView.setEnabled(true);
//		passTextView.setEnabled(true);
//		timezoneTextView.setEnabled(true);
//		btnEditProfile.setVisibility(View.GONE);
//    	savingLayout.setVisibility(View.VISIBLE);
	}
	private void disableEdits()
	{
		firstnameTextView.setEnabled(false);
		phoneTextView.setEnabled(false);
		phoneTextView.setEnabled(false);
//		passTextView.setEnabled(false);
//		timezoneTextView.setEnabled(false);
//		btnEditProfile.setVisibility(View.VISIBLE);
//    	savingLayout.setVisibility(View.GONE);
	}
	
	 @Override
	    public void onBackPressed() {
	        // Do Here what ever you want do on back press;
	    }
	 
	 public void addTimeZones() {
			//
			final String[] TZ = TimeZone.getAvailableIDs();

			Log.i("count", Integer.toString(TZ.length));
			ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
					getParent(), android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			final ArrayList<String> TZ1 = new ArrayList<String>();
			for (int i = 0; i < TZ.length; i++) {
				if (!(TZ1.contains(TimeZone.getTimeZone(TZ[i]).getID()))) {
					if (timezoneValidate(TZ[i]))
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
	            	profileTimezone.setSummary((TimeZone.getDefault().getID()));
				}
	            i++;
	            
		    }
		   
		    profileTimezone.setEntries(entries);
		    profileTimezone.setEntryValues(entryValues);
		    Locale locale = new Locale("en", "IN");
		    String curr = TimeZone.getTimeZone(TimeZone.getDefault().getID()).getDisplayName(false,TimeZone.SHORT, locale);
		    String currTimeZone = "";
		    for (int q = 0; q<TZ1.size();q++)
		    {
		    	if (TimeZone.getTimeZone(TZ1.get(q)).getDisplayName(false,TimeZone.SHORT, locale).equals(curr))
		    	currTimeZone = TZ1.get(q);
		    }
		    profileTimezone.setSummary(currTimeZone );
			
		}
	 boolean timezoneValidate(String email)
	    {
	        
	    	Pattern pattern = Pattern.compile(".+\\/+[A-z]+");
				Matcher matcher = pattern.matcher(email);
				boolean matchFound = matcher.matches();
	    	return matchFound;
	    }
	 
}
