package com.arrayadapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignmentexpert.R;
import com.datamodel.CountryInfo;

public class CountryAdapter extends ArrayAdapter<CountryInfo>{
	private static final String TAG = "CountryADapter";
	private Context context;
	 ArrayList<CountryInfo> data ;

	public CountryAdapter(Activity context, int resource, ArrayList<CountryInfo> data) {
		super(context, resource);
		this.context = context;
		this.data = data;
		Log.i(TAG + "adapter const", Integer.toString(this.data.size()));
		// TODO Auto-generated constructor stub
	}
	@Override
	public int getCount() {
		Log.i(TAG + "getCount", Integer.toString(this.data.size()));
	    // TODO Auto-generated method stub
	    return data.size();
	}
	  @Override
      public View getView(int position, View convertView, ViewGroup parent) 
      {   // Ordinary view in Spinner, we use android.R.layout.simple_spinner_item
		  Log.i(TAG, "getCustom view");
		  View row = convertView;
          if(row == null)
          {
        	  LayoutInflater inflater = LayoutInflater.from(context);
              row = inflater.inflate(R.layout.country_item, parent, false);
          }

          CountryInfo item = data.get(position);
          Log.i(TAG, item.toString());
          if(item != null)
          {   // Parse the data from each object and set it.
              ImageView myFlag = (ImageView) row.findViewById(R.id.imageIcon);
              TextView myCountry = (TextView) row.findViewById(R.id.countryName);
              if(myFlag != null)
              {
                  myFlag.setBackgroundDrawable(context.getResources().getDrawable(item.getCountryIcon()));
              }
              if(myCountry != null)
                  myCountry.setText("+"+item.getCountryCode());
          }
          return row; 
      }
	  @Override
      public View getDropDownView(int position, View convertView, ViewGroup parent)
      {      Log.i(TAG, "getCustom view");
		  View row = convertView;
	      if(row == null)
	      {
	    	  LayoutInflater inflater = LayoutInflater.from(context);
	          row = inflater.inflate(R.layout.country_item, parent, false);
	      }
	
	      CountryInfo item = data.get(position);
	      Log.i(TAG, item.toString());
	      if(item != null)
	      {   // Parse the data from each object and set it.
	          ImageView myFlag = (ImageView) row.findViewById(R.id.imageIcon);
	          TextView myCountry = (TextView) row.findViewById(R.id.countryName);
	          if(myFlag != null)
	          {
	              myFlag.setBackgroundDrawable(context.getResources().getDrawable(item.getCountryIcon()));
	          }
	          if(myCountry != null)
	              myCountry.setText(item.getCountryName() + " (+ "+ item.getCountryCode()+")");
	      }
	      return row; 
          
	  }
}
