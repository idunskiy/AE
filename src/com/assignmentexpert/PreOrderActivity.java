package com.assignmentexpert;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class PreOrderActivity  extends Activity{
	protected static final String TAG = null;
	private Spinner productSpin;
	private View btnProfile;
	private View btnOrderHistory;
	private SharedPreferences sharedPreferences;
	private Editor editor;
	private View btnClose;
	public  Spinner typeSpinner;
	LayoutInflater mInflator;
    boolean selected;
    ArrayAdapter<String> dataAdapter;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_order);
    	productSpin = (Spinner)findViewById(R.id.productSpin);
    	btnOrderHistory = (Button) findViewById(R.id.btnOrdersHistory);
        btnClose = (Button) findViewById(R.id.btnClose);
        btnProfile = (Button) findViewById(R.id.btnProfile);
        mInflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        
    	final ArrayList<String> products = new ArrayList<String>();
		products.add("Choose order type");
		products.add("no selection");
		products.add("Order");
		products.add("Essay");
		sharedPreferences = getSharedPreferences("user", MODE_PRIVATE );
		editor = sharedPreferences.edit();
		 dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, products);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		productSpin.setAdapter(dataAdapter);
		productSpin.setOnItemSelectedListener(typeSelectedListener);
		productSpin.setOnTouchListener(typeSpinnerTouchListener);
		
		
		btnProfile.setOnClickListener(new View.OnClickListener() {
			  	 
			    public void onClick(View view) {
			    	
			        Intent i = new Intent(getApplicationContext(),
			               ProfileActivity.class);
			        startActivity(i);
			        
			    }
			});
			 btnOrderHistory.setOnClickListener(new View.OnClickListener() {
				 
			        public void onClick(View view) {
			        	
			            Intent i = new Intent(getApplicationContext(),
			                   DashboardActivityAlt.class);
			            startActivity(i);
			            
			        }
			    });
			 btnClose.setOnClickListener(new View.OnClickListener() {
		   	       
		            public void onClick(View view) {
		            	editor.remove("username");
		            	editor.remove("password");
		            	editor.remove("isChecked");
		            	editor.commit();
		            	Intent i = new Intent(getApplicationContext(),
		                        LoginActivity.class);
		                startActivity(i);
		            } 
		    	});
	}
	private SpinnerAdapter typeSpinnerAdapter = new BaseAdapter() {

		private TextView text;
		private String[] data = { "Choose order type", "Order", "Essay" };
		private int count = 3;

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflator.inflate(R.layout.spinner_hint, null);
			}
			text = (TextView) convertView.findViewById(R.id.spinnerTarget);
			if (!selected) {
				text.setText("Choose order type");
			} else {
				text.setText(data[position]);
			}
			return convertView;
		}

		public long getItemId(int position) {
			return position;
		}

		public Object getItem(int position) {
			return data[position];
		}

		public int getCount() {
			return count;
		}

		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflator.inflate(
						android.R.layout.simple_spinner_dropdown_item, null);
			}
			text = (TextView) convertView.findViewById(android.R.id.text1);
			text.setText(data[position]);
			return convertView;
		};
	};

	private OnItemSelectedListener typeSelectedListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			 if(productSpin.getSelectedItem().toString().equals("Order")) 
        	 {
		        	{
		        		
		        		Intent i = new Intent(getApplicationContext(),
		                        NewOrderActivity.class);
		                        startActivity(i);
		        	}
        	
            }
        	 if(productSpin.getSelectedItem().toString().equals("Essay")) 
        	 {
		        	{
		        		Intent i = new Intent(getApplicationContext(),
		                        NewEssayActivity.class);
		                        startActivity(i);
		        	}
        	
            }

		}

		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	private OnTouchListener typeSpinnerTouchListener = new OnTouchListener() {

		

		public boolean onTouch(View v, MotionEvent event) {
			selected = true;
			dataAdapter.remove("Choose order type");
			dataAdapter.notifyDataSetChanged();
			return false;
		}
	};

}
