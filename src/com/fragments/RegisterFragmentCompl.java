package com.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

import com.assignmentexpert.R;
import com.customitems.CustomEditPreference;
import com.library.singletones.SharedPrefs;
/** * фрагмент дл€ завершающего этапа регистрации*/
public class RegisterFragmentCompl extends Fragment implements IClickListener{
	/** * кнопка дл€ завершени€ регистрации и отправки данных на сервер*/
	private Button btnProceed;
	 /** * экземпл€р интерфейса  IClickListener*/
	private IClickListener listener;
	/** * CustomEditPreference дл€ отображени€ списка стран*/
	private CustomEditPreference counrtyEditPref;
	private ImageView orderInfo;
	/** * CustomEditPreference дл€ отображени€ имени регистрирующегос€*/
	private CustomEditPreference registerName;
	/** * CustomEditPreference дл€ отображени€ email'a регистрирующегос€*/
	private CustomEditPreference registerEmail;
	/** * CheckBox дл€ входа в приложение без прохождени€ этапа login'a*/
	private CheckBox signMeCheck;

	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.register_compl,
	        container, false);
		registerName = (CustomEditPreference)view.findViewById(R.id.registerName);
		registerEmail = (CustomEditPreference)view.findViewById(R.id.registerEmail);
	    btnProceed = (Button)view.findViewById(R.id.btnProceed);
	    btnProceed.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
//	        	listener.changeFragment(5);
	        	
	        }
	    });
		counrtyEditPref = (CustomEditPreference)view.findViewById(R.id.counrtyEditPref);
		
	        
		orderInfo = (ImageView)view.findViewById(R.id.orderInfo);
		orderInfo.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                	Dialog dialog = new Dialog(getActivity(), R.style.FullHeightDialog);
                	TextView myMsg = new TextView(getActivity());
                	myMsg.setText("We recommend you to leave a cell number so we could update you via SMS. We wonТt call you or disclose this number.");
                	myMsg.setTextColor(Color.WHITE);
                	myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                	dialog.setContentView(myMsg);
                	dialog.show();
            		dialog.setCanceledOnTouchOutside(true);
                }
            });
		
		 signMeCheck = (CheckBox) view.findViewById(R.id.signMeCheck);
		 SharedPrefs.getInstance().Initialize(getActivity());
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
		 
		String currLocale = this.getResources().getConfiguration().locale.getCountry();
		
		registerName.setTitle("Name");
		registerEmail.setTitle("Email");
		registerName.setSummary(RegisterFragment.userName);
		registerEmail.setSummary(RegisterFragment.userEmail);
		
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
        counrtyEditPref.setSummary(Locale.getDefault().getDisplayCountry());
        counrtyEditPref.setOnClickListener(new View.OnClickListener() {
         	   
            private AlertDialog dialog;

			public void onClick(View view) {
            			
            	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            	builder.setTitle("Select your country");

            	ListView modeList = new ListView(getActivity());
            	Locale[] locales = Locale.getAvailableLocales();
    	        ArrayList<String> countries = new ArrayList<String>();
    	     
    	        for (Locale locale : locales) {
    	            String country = locale.getDisplayCountry();
    	            if (country.trim().length()>0 && !countries.contains(country)) {
    	                countries.add(country);
    	            }
    	        }
    	        Collections.sort(countries);
            	final ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.dialog_list_item, R.id.tv, countries);
            	modeList.setAdapter(modeAdapter);
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
            }
        });
      
	    return view;
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
	public void changeFragment(int flag) {
		
	}
	

}