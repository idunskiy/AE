package com.assignmentexpert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.customitems.CountriesListPref;
import com.customitems.CustomEditPreference;

public class RegisterActivityCompl extends PreferenceActivity
{
	private Preference registerCountry;
	private Preference registerEmail;
	private Preference registerName;
	private View listFHeader;
	private ListView listView;
	private View listFooter;
	private View register_compl_footer;
	View register_compl_check ;
	private ImageView orderInfo;
	private CustomEditPreference counrtyEditPref;
	Dialog dialog;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.register_coml);
            
            listFHeader = getLayoutInflater().inflate(R.layout.register_compl_header, null);
            listFooter = getLayoutInflater().inflate(R.layout.register_compl_footer, null);
			listView  = getListView();
			listView.addHeaderView(listFHeader);
			listView.addFooterView(listFooter);
			ListPreference registerName= (ListPreference)findPreference("registerName");
			ListPreference registerEmail= (ListPreference)findPreference("registerEmail");
			View preferenceView = registerEmail.getView( null, getListView() ); 
			preferenceView.setVisibility( View.GONE ); 
			counrtyEditPref = (CustomEditPreference)listFooter.findViewById(R.id.counrtyEditPref);
			counrtyEditPref.setTitle("Country");
			Log.i("before call","it was before");
			orderInfo = (ImageView)listFooter.findViewById(R.id.orderInfo);
			orderInfo.setOnClickListener(new OnClickListener() {
	                public void onClick(View v) {
	                    Toast.makeText(RegisterActivityCompl.this,
	                            "The favorite list would appear on clicking this icon",
	                            Toast.LENGTH_LONG).show();
	                }
	            });
//			 CountriesListPref registerCountry = new CountriesListPref(getDialogContext(RegisterActivityCompl.this));
			 CountriesListPref registerCountry = new CountriesListPref(getDialogContext(RegisterActivityCompl.this));
			
			 
			String currLocale = this.getResources().getConfiguration().locale.getCountry();
			
			registerName.setSummary(RegisterActivity.userName);
			registerEmail.setSummary(RegisterActivity.userEmail);
			registerCountry.setSummary(currLocale);
			registerCountry.setTitle("Country");
			registerCountry.setKey("registerCountry");
			
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
	        counrtyEditPref.setSummary(Locale.getDefault().getDisplayCountry());
	        counrtyEditPref.setOnClickListener(new View.OnClickListener() {
	         	   
                public void onClick(View view) {
                			
                	AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
                	builder.setTitle("Select Color Mode");

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
                	final ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(getParent(), android.R.layout.simple_list_item_1, android.R.id.text1, countries);
                	modeList.setAdapter(modeAdapter);

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
//                	dialog.setItems(array_of_items, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                          //which is the item number in the list which you can use  
//                          //to do things accordingly
//                        }
//                      });
                }
            });
	        
	}
	
	public Context getDialogContext(Activity act) {
	    Context context;
	    if (act.getParent() != null) 
	        context = act.getParent();
	    else context = act;
	    Log.i("RegisterAct curr context", context.getClass().toString());
	        return context;
	}
}
