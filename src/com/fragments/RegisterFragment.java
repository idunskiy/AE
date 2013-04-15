package com.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignmentexpert.R;
import com.assignmentexpert.RegisterActivity;
import com.customitems.CustomTextView;

public class RegisterFragment extends Fragment implements IClickListener{
	 Button btnProceed;
	    Button btnLinkToLogin;
	    Button btnClose;
	    EditText inputFullName;
	    EditText inputEmail;
	    EditText inputPassword;
	    EditText confPassword;
	    TextView registerErrorMsg;
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
	    
	    
	    public static ImageView captcha;
	    IClickListener listener;
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.register,
	        container, false);
	    inputFullName = (EditText) view.findViewById(R.id.registerName);
        inputEmail = (EditText) view.findViewById(R.id.registerEmail);
        inputPassword = (EditText) view.findViewById(R.id.registerPassword);
        confPassword = (EditText) view.findViewById(R.id.registerPasswordconf);
        captchaEdit = (EditText) view.findViewById(R.id.captcha);
        btnProceed = (Button) view.findViewById(R.id.btnProceed);
        btnTermsService = (CustomTextView)view.findViewById(R.id.btnTermsService);
        btnPrivatePolicy = (CustomTextView)view.findViewById(R.id.btnPrivatePolicy);
        captcha  = (ImageView) view.findViewById(R.id.captchaView);

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
				
				if (inputEmail.getText().toString().equals("You have to enter correct email"))
					inputEmail.getText().clear();
				inputEmail.setTextColor(Color.BLACK);
				return false;
			}
    	});
    	inputFullName.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				
				if (inputFullName.getText().toString().equals("At least 2 charachters"))
					inputFullName.getText().clear();
				inputFullName.setTextColor(Color.BLACK);
				return false;
			}
    	});
    	captchaEdit.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
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
               listener.changeFragment(4);
            
            }
        });
        final View activityRootView = view.findViewById(R.id.scrollViewRegister);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                	if (inputPassword.getText().toString().length()!=0 & inputEmail.getText().toString().length()!=0 &
                			captchaEdit.getText().toString().length()!=0 & confPassword.getText().toString().length()!=0 &
                			inputFullName.getText().toString().length()!=0 )
                		btnProceed.getBackground().setAlpha(255);
                	else 
                		btnProceed.getBackground().setAlpha(120);
                }
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
		// TODO Auto-generated method stub
		
	}
}
