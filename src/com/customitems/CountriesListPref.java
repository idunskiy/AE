package com.customitems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import com.tabscreens.LoginTabScreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

public class CountriesListPref extends ListPreference {

	  static Context context;

    public CountriesListPref(Context context, AttributeSet attrs) {
    	 super(context, attrs);
    	 Log.i("context in customListPref", context.getClass().toString());
        setEntries(new CharSequence[] {"one", "two"});
        setEntryValues(new CharSequence[] {"item1", "item2"});
//        setEntries(getCountries());
//        setEntryValues(getCountries());
    }

    public CountriesListPref(Context context) {
        super(context);
        Log.i("context in customListPref", context.getClass().toString());
        setEntries(new CharSequence[] {"one", "two"});
        setEntryValues(new CharSequence[] {"item1", "item2"});
//        setEntries(getCountries());
//        setEntryValues(getCountries());
    }

//    @Override
//    protected View onCreateDialogView() {
//        ListView view = new ListView(getContext());
//        view.setAdapter(adapter());
//        setEntries(entries());
//        setEntryValues(entryValues());
//        return view;
//    }
    protected void onDialogClosed(boolean positiveResult) {
        Toast.makeText(context, "item_selected",
                           Toast.LENGTH_SHORT).show();
        }
    protected Dialog onCreateDialog(int id) {
        
          AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
         
        return adb.create();
      }
    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
    	builder = new AlertDialog.Builder(getContext());
    	builder.setTitle(getTitle());
        builder.setMessage(getDialogMessage());
        builder.setSingleChoiceItems(entries(), -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        super.onPrepareDialogBuilder(builder);
    }

    private ListAdapter adapter() {
        return new ArrayAdapter(getContext(), android.R.layout.select_dialog_singlechoice);
    }
    private CharSequence[] getCountries()
    {
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
        return chars;
    	
    }
    private CharSequence[] entries() {
    	return getCountries();
    }

    private CharSequence[] entryValues() {
    	return getCountries();
    }
 
    public void setContext(Context context)
    {
    	this.context = context;
    }
    public Context getContext()
    {
    	return context;
    }
}