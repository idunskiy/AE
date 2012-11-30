package com.assignmentexpert;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.datamodel.Customer;

public class ProfileActivity extends Activity{
	private Button btnNewOrder;
	private Button btnClose;
	private Button btnOrderHistory;
	private SharedPreferences sharedPreferences;
	private Editor editor;
	private TextView timezoneTextView;
	private TextView passTextView;
	private TextView loginTextView;
	private TextView phoneTextView;
	private TextView lastnameTextView;
	private TextView firstnameTextView;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        btnOrderHistory = (Button) findViewById(R.id.btnOrdersHistory);
        btnClose = (Button) findViewById(R.id.btnClose);
        btnNewOrder = (Button) findViewById(R.id.btnNewOrder);
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE );
		editor = sharedPreferences.edit();
		
		firstnameTextView = (TextView) findViewById(R.id.firstnameTextView);
		lastnameTextView = (TextView) findViewById(R.id.lastnameTextView);
		loginTextView = (TextView) findViewById(R.id.loginTextView);
		passTextView = (TextView)findViewById(R.id.passTextView);
		phoneTextView = (TextView)findViewById(R.id.phoneTextView);
		timezoneTextView = (TextView)findViewById(R.id.timezoneTextView);
        
		firstnameTextView.setText(LoginActivity.getUser.getUser().getUserFirstName());
		lastnameTextView.setText(LoginActivity.getUser.getUser().getUserLastName());
		loginTextView.setText(LoginActivity.getUser.getUser().getUserEmail());
		phoneTextView.setText(LoginActivity.getUser.getPhone());
		timezoneTextView.setText(LoginActivity.getUser.getUser().getTimeZone());
		passTextView.setText(sharedPreferences.getString("password", null));
		
		btnClose.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
            	//editor.putBoolean("logout", true);
            	editor.remove("username");
            	editor.remove("password");
            	editor.remove("isChecked");
            	editor.commit();
            	Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
            } 
    	});
		
		   btnNewOrder.setOnClickListener(new View.OnClickListener() {
	        	 
	            public void onClick(View view) {
	            	
	                Intent i = new Intent(getApplicationContext(),
	                        PreOrderActivity.class);
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
   	   
	}
}
