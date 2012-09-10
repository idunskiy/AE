package com.assignmentexpert;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.library.UserFunctions;

public class RestoreActivity  extends Activity{
	EditText restoreEmail;
	Button btnProceed;
	Button btnClose;
	boolean errorFlag  = false;
	EditText textCaptcha;
	
	private static String KEY_STATUS = "status";
	
	@Override
	    public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
	        setContentView(R.layout.restore);
	        
	        ImageView captcha = (ImageView) findViewById(R.id.captchaView);
	        
	         
	        
	        restoreEmail = (EditText)findViewById(R.id.restoreEmail);
	        btnProceed = (Button) findViewById(R.id.btnProceed);
	        textCaptcha = (EditText) findViewById(R.id.captcha);
	        btnClose = (Button) findViewById(R.id.btnClose);
	       
	        
	        UserFunctions a = new UserFunctions();
	        String b = null;
	        try {
				
				captcha.setImageBitmap(a.getCaptcha());
			} catch (IOException e) {
				
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        restoreEmail.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View arg0, MotionEvent arg1) {
					restoreEmail.setText(" ");
					((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)) 
					.showSoftInput(restoreEmail, 0); 
					restoreEmail.setTextColor(Color.BLACK);
					return false;
				}
	        });
	    	btnClose.setOnClickListener(new View.OnClickListener() {
	   	       
	            public void onClick(View view) {
	            	moveTaskToBack(true);
	            } 
	    	});
	        
	        btnProceed.setOnClickListener(new View.OnClickListener() {
	          

				public void onClick(View view) {

					String newPass = restoreEmail.getText().toString();
					String captcha = textCaptcha.getText().toString();
					if(newPass.equals("E-mail"))
	           		 {
						restoreEmail.getText().clear();
						restoreEmail.setTextColor(Color.RED);
						restoreEmail.setText("Should be filled");
						errorFlag = true;
	           		 }
					
					if (!EmailValidate(newPass))
	                {
						restoreEmail.setText(" ");
						restoreEmail.setTextColor(Color.RED);
						restoreEmail.setText("Try another address");
	                	errorFlag = true;
	                	
	                }
					if (captcha.length()<4)
	                {
						textCaptcha.setText(" ");
						textCaptcha.setTextColor(Color.RED);
						textCaptcha.setText("Incorrect");
	                	errorFlag = true;
	                }


	            	 if (errorFlag == false)
	                 {	
	                 	UserFunctions reg = new UserFunctions();
	                 	
	                 	JSONObject a = null;
						try {
							a = reg.restorePassword(newPass, captcha);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						try {
							
							String b = a.get(KEY_STATUS).toString();
							
							if(Integer.parseInt(b)==1)
								{
									String message = "A message has been send to"+"\r\n"+ newPass+ 
											"\r\n"+"Follow the link within message to restore password";
									Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
									toast.show();
									
								}
							else 
							{
								String message = "Restoring failed";
								Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
								toast.show();
								
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                 	Log.i("register_response", a.toString());
	                 }
	            }
	            
	        });
	
	}
	boolean EmailValidate(String email)
    {
        
    	Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
			Matcher matcher = pattern.matcher(email);
			boolean matchFound = matcher.matches();
    	return matchFound;
    }
}
