package com.assignmentexpert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activitygroups.MainTabGroup;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.CaptchaAsync;
import com.customitems.CustomTextView;
import com.library.FrequentlyUsedMethods;

public class RegisterActivity extends FragmentActivity implements ITaskLoaderListener{
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

	private ProgressDialog pd = null;
	private Button btnLinkToRegisterScreen;
	private CustomTextView btnTermsService;
	private CustomTextView btnPrivatePolicy;
 
    // JSON Response node names
    private static String KEY_SUCCESS = "status";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    private static String KEY_STATUS = "status";
    
    public static String userName;
    public static String userEmail;
    public static String userPass;
    public static String userConf;
    public static String userCaptcha;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	InputMethodManager imm = (InputMethodManager)getSystemService(
    		      Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        final FrequentlyUsedMethods faq = new FrequentlyUsedMethods(RegisterActivity.this);
        // Importing all assets like buttons, text fields
        inputFullName = (EditText) findViewById(R.id.registerName);
        inputEmail = (EditText) findViewById(R.id.registerEmail);
        inputPassword = (EditText) findViewById(R.id.registerPassword);
        confPassword = (EditText) findViewById(R.id.registerPasswordconf);
        captchaEdit = (EditText) findViewById(R.id.captcha);
        btnProceed = (Button) findViewById(R.id.btnProceed);
        btnTermsService = (CustomTextView)findViewById(R.id.btnTermsService);
        btnPrivatePolicy = (CustomTextView)findViewById(R.id.btnPrivatePolicy);
        btnProceed.getBackground().setAlpha(120);
      

        inputPassword.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				//inputPassword.setFocusable(true);
				inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				if (inputPassword.getText().toString().equals("At least 5 charachters"))
					inputPassword.getText().clear();
				if(inputPassword.getText().toString().equalsIgnoreCase("Should be equal"))
				{
					inputPassword.getText().clear();
					confPassword.getText().clear();
				}
				
				inputPassword.setTextColor(Color.BLACK);
				return false;
			}
    		
    	});
        confPassword.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				//inputPassword.setFocusable(true);
				confPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				confPassword.setTextColor(Color.BLACK);
				return false;
			}
    		
    	});
    	inputEmail.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)) 
				.showSoftInput(inputEmail, 0); 
				if (inputEmail.getText().toString().equals("You have to enter correct email"))
					inputEmail.getText().clear();
				inputEmail.setTextColor(Color.BLACK);
				return false;
			}
    	});
    	inputFullName.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)) 
				.showSoftInput(inputFullName, 0); 
				if (inputFullName.getText().toString().equals("At least 2 charachters"))
					inputFullName.getText().clear();
				inputFullName.setTextColor(Color.BLACK);
				return false;
			}
    	});
    	captchaEdit.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)) 
				.showSoftInput(captchaEdit, 0); 
				if (captchaEdit.getText().toString().equals("Incorrect"))
					captchaEdit.getText().clear();
				captchaEdit.setTextColor(Color.BLACK);
				return false;
			}
    	});
    	btnTermsService.setOnClickListener(new View.OnClickListener() {

    	    public void onClick(View v) {
    	    	String url = "http://www.assignmentexpert.com/terms-and-conditions.html";
    	    	Intent i = new Intent(Intent.ACTION_VIEW);
    	    	i.setData(Uri.parse(url));
    	    	startActivity(i);
    	    }
    	});
    	btnPrivatePolicy.setOnClickListener(new View.OnClickListener() {

    	    public void onClick(View v) {
    	    	String url = "http://www.assignmentexpert.com/privacy-policy.html";
    	    	Intent i = new Intent(Intent.ACTION_VIEW);
    	    	i.setData(Uri.parse(url));
    	    	startActivity(i);
    	    }
    	});
    	
        // Register Button Click event
        btnProceed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               boolean errorFlag = false;
//               String name = inputFullName.getText().toString();
//               String email = inputEmail.getText().toString();
//               String password = inputPassword.getText().toString();
//               String confpassword = confPassword.getText().toString();
//               String captcha = captchaEdit.getText().toString();
//                
//                if (inputEmail.length()<2)
//                {
//                            	
//                	inputEmail.setText(" ");
//                	inputEmail.setTextColor(Color.RED);
//                	inputEmail.setText("Must be valid");
//                	
//                	errorFlag = true;
//               	}
//               
//                
//                
//                if (inputFullName.length()<2)
//                	{
//                	
//                	inputFullName.setText(" ");
//                	inputFullName.setTextColor(Color.RED);
//                	inputFullName.setText("At least 2 charachters");
//                	
//
//                	errorFlag = true;
//                 	}
//                
//                if (inputPassword.length()<5)
//            	{
//            	inputPassword.setText("");
//            	inputPassword.setTextColor(Color.RED);
//            	inputPassword.setInputType(InputType.TYPE_CLASS_TEXT);
//            	inputPassword.setText("At least 5 charachters");
//            	
//            	errorFlag = true;
//             	}
//                if (!confpassword.equals(password))
//                {	 
//                	inputPassword.setInputType(InputType.TYPE_CLASS_TEXT);
//           	 		confPassword.setInputType(InputType.TYPE_CLASS_TEXT);
//                	inputPassword.setInputType(InputType.TYPE_CLASS_TEXT);
//                	inputPassword.setText("");
//                	inputPassword.setTextColor(Color.RED);
//                	confPassword.setText("");
//                	confPassword.setTextColor(Color.RED);
//                	inputPassword.setText("Should be equal");
//                	confPassword.setText("Should be equal");
//                	
//                	errorFlag = true;
//                }
//                if (captchaEdit.length()!=4)
//                {
//                	captchaEdit.setText(" ");
//                	captchaEdit.setTextColor(Color.RED);
//                	captchaEdit.setText("Incorrect");
//                	errorFlag = true;
//                }
//
//                if (!EmailValidate(email))
//                {
//                	inputEmail.setText(" ");
//                	inputEmail.setTextColor(Color.RED);
//                	inputEmail.setText("You have to enter correct email");
//                	errorFlag = true;
//                	
//                }
//                if (errorFlag == false)
//                {	
//                	btnProceed.getBackground().setAlpha(255);
//			    		 userName = name;
//			    		 userEmail  = email;
//			    		 userPass = password;
//			    		 userConf = confpassword;
//			    		 userCaptcha = captcha;
//			    		 Intent frequentMessages = new Intent(getParent(), RegisterActivityCompl.class);
//				         MainTabGroup parentActivity = (MainTabGroup)getParent();
//				         parentActivity.startChildActivity("FrequentMessageActivity", frequentMessages);
//                }
               Intent frequentMessages = new Intent(getParent(), RegisterActivityCompl.class);
		         MainTabGroup parentActivity = (MainTabGroup)getParent();
		         parentActivity.startChildActivity("FrequentMessageActivity", frequentMessages);
            
            }
        });
       
    }
    @Override 
    public void onResume()
    {
    	InputMethodManager imm = (InputMethodManager)getSystemService(
    		      Context.INPUT_METHOD_SERVICE);
        	imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    	super.onResume();
    	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    	  CaptchaAsync.execute(this, this);
    
    }
    boolean EmailValidate(String email)
    {
        
    	Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
			Matcher matcher = pattern.matcher(email);
			boolean matchFound = matcher.matches();
    	return matchFound;
    }
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
	public void onLoadFinished(Object data) {
		 if (data instanceof Bitmap & data != null)
    	 {
    		 RegisterActivity.this.captcha  = (ImageView) findViewById(R.id.captchaView);
    		 captcha.setImageBitmap((Bitmap)data);
    	 }
		 if (data instanceof String)
		 {
			 Toast.makeText(RegisterActivity.this, data.toString(), Toast.LENGTH_LONG).show();
		 }
		
	}
	public void onCancelLoad() {
		 Toast.makeText(RegisterActivity.this, "Some error occurs. Please try later", Toast.LENGTH_LONG).show();
		
	}
    
}