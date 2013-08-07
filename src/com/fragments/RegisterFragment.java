package com.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignmentexpert.R;
import com.customitems.CustomTextView;
import com.fragmentactivities.RegisterFragmentActivity;
import com.library.singletones.SharedPrefs;
/** * фрагмент для регистрации нового пользователя*/
public class RegisterFragment extends Fragment implements IClickListener{
	/** * кнопка для обработки введенных пользователем данных*/
	 Button btnProceed;
	 /** * EditText для ввода имени пользователя*/
	    EditText inputFullName;
	    /** * EditText для ввода email'a пользователя*/
	    EditText inputEmail;
	    /** * EditText для ввода пароля пользователя*/
	    EditText inputPassword;
	    /** * EditText для подтверждения ввода пароля пользователя*/
	    EditText confPassword;
	    /** * EditText для ввода катчи*/
	    EditText captchaEdit;

		private CustomTextView btnTermsService;
		private CustomTextView btnPrivatePolicy;
		/** *статическое поле userName для использования в FragmentActivity для отправки на сервер*/
	    public static String userName;
	    /** *статическое поле userEmail для использования в FragmentActivity для отправки на сервер*/
	    public static String userEmail;
	    /** *статическое поле пароля пользователя для использования в FragmentActivity для отправки на сервер*/
	    public static String userPass;
	    /** *статическое поле подтверждения ввода пароля для использования в FragmentActivity для отправки на сервер*/
	    public static String userConf;
	    /** *статическое поле строки каптчи для использования в FragmentActivity для отправки на сервер*/
	    public static String userCaptcha;
	    
	    /** *ImageView для отображения каптчи*/
	    public static ImageView captcha;
	    /** * экземпляр интерфейса  IClickListener*/
	    IClickListener listener;
	    
	    String name;
	    String email;
	    String password;
	    String confpassword;
	    String captchaString;
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
               name = inputFullName.getText().toString();
               email = inputEmail.getText().toString();
               password = inputPassword.getText().toString();
               confpassword = confPassword.getText().toString();
               captchaString = captchaEdit.getText().toString();
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
               
               	 userName = name;
	    		 userEmail  = email;
	    		 userPass = password;
	    		 userConf = confpassword;
	    		 userCaptcha = captchaString;
               setSharedPreferences();
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
	/** * метод для сохранения данных c помощью sharedPreferences*/
	private void setSharedPreferences()
	{
		 SharedPrefs.getInstance().Initialize(getActivity().getApplicationContext());
	     SharedPrefs.getInstance().writeString("registerName", name);
         SharedPrefs.getInstance().writeString("registerEmail", email);
         SharedPrefs.getInstance().writeString("registerPassword", password);
         SharedPrefs.getInstance().writeString("registerConfPass", confpassword);
         SharedPrefs.getInstance().writeString("registerCaptcha", captchaString);
        
	}
	/** * метод для получения данных c помощью sharedPreferences*/
	private void getSharedPreferences()
	{
		Log.i("registerFragment","getSharedPreferences mtdh");
         
		String registerName =   SharedPrefs.getInstance().getSharedPrefs().getString("registerName", null);
		String registerEmail =    SharedPrefs.getInstance().getSharedPrefs().getString("registerEmail", null);
		String registerPassword  =   SharedPrefs.getInstance().getSharedPrefs().getString("registerPassword", null);
		String registerConfPass  =   SharedPrefs.getInstance().getSharedPrefs().getString("registerConfPass", null);
		String registerCaptcha  =   SharedPrefs.getInstance().getSharedPrefs().getString("registerCaptcha", null);

		try{

		if (registerName != null)
			inputFullName.setText(SharedPrefs.getInstance().getSharedPrefs().getString("registerName", null));
		if(registerEmail != null)
			inputEmail.setText(SharedPrefs.getInstance().getSharedPrefs().getString("registerEmail", null));
		if(registerPassword != null)
			inputPassword.setText(SharedPrefs.getInstance().getSharedPrefs().getString("registerPassword", null));
		if(registerConfPass != null)
			confPassword.setText(SharedPrefs.getInstance().getSharedPrefs().getString("registerConfPass", null));
		if(registerCaptcha != null)
			captchaEdit.setText(SharedPrefs.getInstance().getSharedPrefs().getString("registerCaptcha", null));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/** * метод для установки обькта Bitmap, полученного с сервера в ImageView каптчи*/
	public static void setImageBitmap(Bitmap data)
	{
		captcha.setImageBitmap(data);
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
	/** * используется, если каптча уже скачана, её установка в ImageView*/
	@Override
	  public void onResume() {
	    
	     super.onResume();
	     if (RegisterFragmentActivity.captchaForSave != null)
	    	 captcha.setImageBitmap(RegisterFragmentActivity.captchaForSave);
	     getSharedPreferences();
	     
	  }
}
