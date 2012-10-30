package com.assignmentexpert;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.library.UserFunctions;

public class RestoreActivity  extends Activity{
	EditText restoreEmail;
	Button btnProceed;
	Button btnClose;
	boolean errorFlag  = false;
	EditText textCaptcha;
	ImageView captcha;
	private static String KEY_STATUS = "status";
	private ProgressDialog pd = null;
	private Button btnLinkToRegisterScreen;
	private Button btnLogin;
	@Override
	    public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
	        setContentView(R.layout.restore);
	        
	        
	        restoreEmail = (EditText)findViewById(R.id.restoreEmail);
	        btnProceed = (Button) findViewById(R.id.btnProceed);
	        textCaptcha = (EditText) findViewById(R.id.captcha);
	        btnClose = (Button) findViewById(R.id.btnClose);
	        btnLinkToRegisterScreen = (Button) findViewById(R.id.btnLinkToRegisterScreen);
	        btnLogin = (Button) findViewById(R.id.btnLogin);
	       // downloading the image btnLogin
	        this.pd = ProgressDialog.show(this, "Please wait..", "Downloading Data...", true, false); 
	        new DownloadTask().execute();
	        
	        restoreEmail.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if (restoreEmail.getText().toString().equals("E-mail"))
					{
					restoreEmail.setText("");
					
					((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)) 
					.showSoftInput(restoreEmail, 0); 
					restoreEmail.setTextColor(Color.BLACK);
					}
					restoreEmail.setHint("E-mail");
					return false;
				}
	        });
	        
	        btnLinkToRegisterScreen.setOnClickListener(new View.OnClickListener() {
	        	 
	            public void onClick(View view) {
	            	
	                Intent i = new Intent(getApplicationContext(),
	                        RegisterActivity.class);
	                startActivity(i);
	                
	            }
	        });
	        btnLogin.setOnClickListener(new View.OnClickListener() {
	        	 
	            public void onClick(View view) {
	            	
	                Intent i = new Intent(getApplicationContext(),
	                        LoginActivity.class);
	                startActivity(i);
	                
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

    private class DownloadTask extends AsyncTask<String, Void, Bitmap> {
         protected Bitmap doInBackground(String... args) {
            
        	 UserFunctions a = new UserFunctions();
        	 Bitmap bitmap = null;
        	 try {
				  bitmap = a.getCaptcha();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
 			
			return bitmap;

            
         }

         protected void onPostExecute(Bitmap bitmap) {
             // Pass the result data back to the main activity
             //RestoreActivity.this.data = result;
        	 RestoreActivity.this.captcha  = (ImageView) findViewById(R.id.captchaView);
        	 captcha.setImageBitmap(bitmap);
             if (RestoreActivity.this.pd != null) {
            	 RestoreActivity.this.pd.dismiss();
             }
             
         }
    }    
	boolean EmailValidate(String email)
    {
        
    	Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
			Matcher matcher = pattern.matcher(email);
			boolean matchFound = matcher.matches();
    	return matchFound;
    }
}
