package com.assignmentexpert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.RegisterAsync;
import com.customitems.CountriesListPref;
import com.customitems.CustomEditPreference;
import com.customitems.CustomEditText;
import com.library.SharedPrefs;

public class RegisterActivityCompl extends FragmentActivity implements ITaskLoaderListener
{
	private Preference registerCountry;
	View register_compl_check ;
	private ImageView orderInfo;
	private CustomEditPreference counrtyEditPref;
	Dialog dialog;
	CheckBox signMeCheck;
	private SharedPreferences sharedPreferences;
	private Editor editor;
	private Button btnProceed;
	private CustomEditText registerPhone;
	private CustomEditPreference registerName;
	private CustomEditPreference registerEmail;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
			InputMethodManager imm = (InputMethodManager)getSystemService(
  		      Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.register_compl);
            btnProceed = (Button)findViewById(R.id.btnProceed);
            registerPhone  = (CustomEditText)findViewById(R.id.registerPhone);
            btnProceed.getBackground().setAlpha(120);
			registerName = (CustomEditPreference)findViewById(R.id.registerName);
			registerEmail = (CustomEditPreference)findViewById(R.id.registerEmail);
			registerName.iconDisable();
			registerEmail.iconDisable();
			
			btnProceed.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {

		        	btnProceed.getBackground().setAlpha(255);
		        	RegisterAsync.execute(RegisterActivityCompl.this, RegisterActivityCompl.this);
		        }
		    });
			counrtyEditPref = (CustomEditPreference)findViewById(R.id.counrtyEditPref);
			counrtyEditPref.setTitle("Country");
			
			 sharedPreferences = getSharedPreferences("user", MODE_PRIVATE );
		        editor = sharedPreferences.edit();
		        
			orderInfo = (ImageView)findViewById(R.id.orderInfo);
			orderInfo.setOnClickListener(new OnClickListener() {
	                public void onClick(View v) {
	                	Dialog dialog = new Dialog(getParent(), R.style.FullHeightDialog);
	                	TextView myMsg = new TextView(RegisterActivityCompl.this);
	                	myMsg.setText("We recommend you to leave a cell number so we could update you via SMS. We won’t call you or disclose this number.");
	                	myMsg.setTextColor(Color.WHITE);
	                	myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
	                	dialog.setContentView(myMsg);
	                	dialog.show();
	            		dialog.setCanceledOnTouchOutside(true);
	                }
	            });
			
			 signMeCheck = (CheckBox) findViewById(R.id.signMeCheck);
			 SharedPrefs.getInstance().Initialize(getApplicationContext());
			 if (!signMeCheck.isChecked())
				{
				 SharedPrefs.getInstance().writeBoolean("isChecked", false);
				}
				else
			 {
					SharedPrefs.getInstance().writeBoolean("isChecked", true);
		     }
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
	            }
	            });
			 
			 CountriesListPref registerCountry = new CountriesListPref(getDialogContext(RegisterActivityCompl.this));
			String currLocale = this.getResources().getConfiguration().locale.getCountry();
			
			registerName.setTitle("Name");
			registerEmail.setTitle("Email");
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
	 @Override 
	    public void onResume()
	    {
	    	InputMethodManager imm = (InputMethodManager)getSystemService(
	    		      Context.INPUT_METHOD_SERVICE);
	        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	    	super.onResume();
	    	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	    
	    }

	public void onLoadFinished(Object data) {
		if (data instanceof String)
		 {
			 Toast.makeText(RegisterActivityCompl.this, data.toString(), Toast.LENGTH_LONG).show();
		 }
		
	}

	public void onCancelLoad() {
		// TODO Auto-generated method stub
		
	}
}
