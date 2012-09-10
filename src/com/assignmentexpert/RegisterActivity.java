package com.assignmentexpert;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
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

public class RegisterActivity extends Activity {
    Button btnProceed;
    Button btnLinkToLogin;
    Button btnClose;
    EditText inputFullName;
    EditText inputEmail;
    EditText inputPassword;
    EditText confPassword;
    TextView registerErrorMsg;
    ImageView captcha;
    EditText captchaEdit;
	boolean errorFlag = false;
	
 
    // JSON Response node names
    private static String KEY_SUCCESS = "status";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    private static String KEY_STATUS = "status";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
 
        // Importing all assets like buttons, text fields
        inputFullName = (EditText) findViewById(R.id.registerName);
        inputEmail = (EditText) findViewById(R.id.registerEmail);
        inputPassword = (EditText) findViewById(R.id.registerPassword);
        confPassword = (EditText) findViewById(R.id.registerPasswordconf);
        captchaEdit = (EditText) findViewById(R.id.captcha);
        btnProceed = (Button) findViewById(R.id.btnProceed);
        btnLinkToLogin = (Button) findViewById(R.id.btnLogin);
        btnClose = (Button) findViewById(R.id.btnClose);
        ImageView captcha = (ImageView) findViewById(R.id.captchaView);
       
        // test editboxed
       
      
        UserFunctions a = new UserFunctions();
        String b = null;
        try {
			
			captcha.setImageBitmap(a.getCaptcha());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        inputPassword.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				//inputPassword.setFocusable(true);
				inputPassword.setText(" ");
				inputPassword.setHint("Password"); inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				inputPassword.setTextColor(Color.BLACK);
				return false;
			}
    		
    	});
        confPassword.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				//inputPassword.setFocusable(true);
				confPassword.setText(" ");
				confPassword.setHint("Password");confPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				confPassword.setTextColor(Color.BLACK);
				return false;
			}
    		
    	});
    	inputEmail.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				inputEmail.setText(" ");
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)) 
				.showSoftInput(inputEmail, 0); 
				inputEmail.setTextColor(Color.BLACK);
				return false;
			}
    	});
    	inputFullName.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				inputFullName.setText(" ");
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)) 
				.showSoftInput(inputFullName, 0); 
				inputFullName.setTextColor(Color.BLACK);
				return false;
			}
    	});
    	captchaEdit.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				captchaEdit.setText(" ");
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)) 
				.showSoftInput(captchaEdit, 0); 
				captchaEdit.setTextColor(Color.BLACK);
				return false;
			}
    	});
        // Register Button Click event
        btnProceed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               String name = inputFullName.getText().toString();
               String email = inputEmail.getText().toString();
               String password = inputPassword.getText().toString();
               String confpassword = confPassword.getText().toString();
               String captcha = captchaEdit.getText().toString();
               Context context = getApplicationContext();
              
               if(inputFullName.getText().toString().equals("FirstName") )
            	   {
            	   inputFullName.getText().clear();
            	   errorFlag = true;
            	   }
            	   
            		 if(email.equals("E-mail(Login in future)"))
            		 {
            			 inputEmail.getText().clear();
            			 errorFlag = true;
            		 }
            		  		if (password.equals("Password(min 6 symbols)"))
            		  		{
            		  			inputPassword.getText().clear();
            		  			errorFlag = true;
            		  		}
            		  			if(confpassword.equals("Password confirmation"))
            		  			{
            		  				confPassword.getText().clear();
            		  				errorFlag = true;
            		  			}
            	
                
                if (inputEmail.length()<2)
                {
                            	
                	inputEmail.setText(" ");
                	inputEmail.setTextColor(Color.RED);
                	inputEmail.setText("Must be valid");
                	
                	errorFlag = true;
               	}
               
                
                
                if (inputFullName.length()<2)
                	{
                	
                	inputFullName.setText(" ");
                	inputFullName.setTextColor(Color.RED);
                	inputFullName.setText("At least 2 charachters");
                	

                	errorFlag = true;
                 	}
                
                if (inputPassword.length()<5)
            	{
            	inputPassword.setText(" ");
            	inputPassword.setTextColor(Color.RED);
            	inputPassword.setText("At least 5 charachters");
            	
            	errorFlag = true;
             	}
                if (!confpassword.equals(password))
                {	 
                	inputPassword.setInputType(InputType.TYPE_CLASS_TEXT);
           	 		confPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                	inputPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                	inputPassword.setText(" ");
                	inputPassword.setTextColor(Color.RED);
                	confPassword.setText(" ");
                	confPassword.setTextColor(Color.RED);
                	inputPassword.setText("Should be equal");
                	confPassword.setText("Should be equal");
                	
                	errorFlag = true;
                }
                if (captchaEdit.length()<4)
                {
                	captchaEdit.setText(" ");
                	captchaEdit.setTextColor(Color.RED);
                	captchaEdit.setText("Incorrect");
                	errorFlag = true;
                }

                if (!EmailValidate(email))
                {
                	inputEmail.setText(" ");
                	inputEmail.setTextColor(Color.RED);
                	inputEmail.setText("You have to enter correct email");
                	errorFlag = true;
                	
                }
                // register data processing
                
                if (errorFlag == false)
                {	
                	UserFunctions reg = new UserFunctions();
                JSONObject a = null;
				try {
					a = reg.registerUser(name, email, password, confpassword, captcha);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                	Log.i("register_response", a.toString());
                	String b;
					try {
						b = a.get(KEY_STATUS).toString();
						if (Integer.parseInt(b)==1)
						{
							String message = "A message has been send to"+"\r\n"+ name+ 
									"\r\n"+"Follow the link within message to restore password";
							Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
							toast.show();
							
						}
						else 
						{
							String message = "Registration failed";
							Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
							toast.show();
							
						}
					} 
					catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					
                }
     
                
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
   	       
            public void onClick(View view) {
            	moveTaskToBack(true);
            } 
    	});
        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                // Close Registration View
                finish();
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