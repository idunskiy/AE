package com.assignmentexpert;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.LoginAsync;
import com.asynctasks.RestoreAsync;
import com.customitems.CustomTextView;
import com.datamodel.Customer;
import com.library.Constants;
import com.library.FrequentlyUsedMethods;
import com.library.singletones.SharedPrefs;
import com.tabscreens.DashboardTabScreen;
/** *Activity логина пользователя */
public class LoginActivity extends FragmentActivity implements ITaskLoaderListener{
    private boolean dialogDismiss  = false;
    /** *кнопка логина пользователя */
    Button btnProceed;
    /** *TextView рестора пароля */
    TextView btnRestore;
    /** *EditText логина пользователя */
    public static AutoCompleteTextView inputEmail;
    /** *EditText пароля пользователя */
    public static EditText inputPassword;
    TextView loginErrorMsg;
   Dialog restoreDialog;
    
   
   /** *публичное статичное поле логина пользователя для использования в LoginAsync */
    public static String forFragmentLogin;
    /** *публичное статичное поле пароля пользователя для использования в LoginAsync */
    public static String forFragmentPassword;
    
    static boolean newUser = false;
    private static Context instance;
    public static String passUserId = null;
    public static Context context;
	
    /** * поле для диалога */
    private static final int DLG_EXAMPLE1 = 0;
    
    public static Customer getUser;
    /** *SharedPreferences для запоминания значений логина и пароля введенных пользователем */
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    /** *поле для запоминания значения емеила для восстановления пароля */
	public static String restorePass ="";
	public static String currentPass ="";
	FrequentlyUsedMethods faq;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
    	
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        new CustomTextView(this);
         // Importing all assets like buttons, text fields
        inputEmail = (AutoCompleteTextView ) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        
        
        inputEmail.setSelection(0);
        btnProceed= (Button) findViewById(R.id.btnProceed);
        btnRestore = (TextView) findViewById(R.id.btnRestore);

        getWindow().setFormat(PixelFormat.RGBA_8888);
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE );
        context = this;
        editor = sharedPreferences.edit();
        faq = new FrequentlyUsedMethods(this);
        printHashKey();

        File directory = new File(Environment.getExternalStorageDirectory()+ "/download/AssignmentExpert");
        if (!directory.exists())
        	directory.mkdirs();
       
    	Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        { 
          
        	if (getIntent().getExtras().getBoolean("relogin") == true)
            	reLogin();
          if(getIntent().getExtras().getString("restError") != null)
          {}
        }
        
        inputPassword.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				//inputPassword.setFocusable(true);
				inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				return false;
			}
    		
    	});
        
        inputPassword.setOnFocusChangeListener(new OnFocusChangeListener() {          
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                	if (inputPassword.getText().toString().length()==0)
                		inputPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });
        
    	inputEmail.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)) 
				.showSoftInput(inputEmail, 0); 
				inputEmail.setHint("E-mail");
				inputEmail.setTextColor(Color.BLACK);
				
			}
    		
    	});
    	
    	
    	 // for test values
//    	 
//    	inputEmail.setText("test@mail.com");
//    	inputPassword.setText("dnipro");
    	
    	
    	
    	if (SharedPrefs.getInstance().getSharedPrefs()!=null)
	    {
    		if (SharedPrefs.getInstance().getSharedPrefs().getBoolean("isChecked", false)==true)
	        {
	        	
	        	forFragmentLogin = sharedPreferences.getString("username", "");
	   		 	forFragmentPassword = sharedPreferences.getString("password", "");
	   		 	LoginAsync.execute(this, this);
	        }
    	}
        btnProceed.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				 boolean errorFlag = false;
				 String email = inputEmail.getText().toString();
			     String password= inputPassword.getText().toString();
			     if(!faq.EmailValidate(email))
			     {
			    	    inputEmail.setText("");
			    	    
//			    	    faq.createToast( getResources().getString(R.string.toast_login_wrong_email), LoginActivity.this);
			    	    Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_login_wrong_email),Toast.LENGTH_LONG).show();
	                	errorFlag = true;
			     }
			     
			     if(errorFlag == false)
				  {	
//			    	 if (!sharedPreferences.getString("username", "").equals(email))
//			    	 {
//			    	   newUser = true;
//			    	   editor.remove("username");
//			    	   editor.commit();
//			    	   Log.i("new User action","LoginActivity");
//			    	 }
//			    	 else 
//			    	 {
//			    			 newUser = false;
//			    			 Log.i("old User action","LoginActivity");
//			    	 }
			    	 boolean isOnline = faq.isOnline();
			    	 if (isOnline)
			    		 
				     {
			    		 forFragmentLogin = email;
			    		 forFragmentPassword = password;
			    		 LoginAsync.execute(LoginActivity.this,LoginActivity.this);
//				    	 editor.putString("username", email);
//				    	 editor.putString("password", password);
//				    	 editor.commit();
			    	 }
				  }
			}
			
        });
       
       btnRestore.setOnClickListener(new View.OnClickListener() {
    	   
           public void onClick(View view) {
        	  
        	    showDialog(DLG_EXAMPLE1);
           }
       });
       final View activityRootView = findViewById(R.id.scrollViewLogin);
       activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
           public void onGlobalLayout() {
               int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
               if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
               	if (inputPassword.getText().toString().length()!=0 & inputEmail.getText().toString().length()!=0)
                  	 btnProceed.getBackground().setAlpha(255);
               	else 
               		btnProceed.getBackground().setAlpha(120);
               		
               }
            }
       });
    }
    
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
      if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
        if (inputPassword.getText().toString().length()!=0 & inputEmail.getText().toString().length()!=0)
        	 btnProceed.getBackground().setAlpha(255);
        return true;  // So it is not propagated.
      }
      return super.dispatchKeyEvent(event);
    }
    /** *метод ре-логина пользователя */
    public  void reLogin()
    {
    	
    	inputEmail.setText(sharedPreferences.getString("username", ""));
    	inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    	inputPassword.setText(sharedPreferences.getString("password", ""));
    	
    }
    /** *метод записи полученных данных от сервера в файл. используется для дебага */
    public static void appendLog(String text)
    {       
       File logFile = new File("sdcard/log.txt");
       if (!logFile.exists())
       {
          try
          {
             logFile.createNewFile();
          } 
          catch (IOException e)
          {
             // TODO Auto-generated catch block
             e.printStackTrace();
          }
       }
       try
       {
          BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
          buf.append(text);
          buf.newLine();
          buf.close();
          
       }
       catch (IOException e)
       {
          // TODO Auto-generated catch block
          e.printStackTrace();
       }
    }

    /** *метод, проверяющий, что введенная строка  соответствует формату email'a*/
	

	  @Override
	    public void onBackPressed() {
	    }
	  @Override
	  public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	  }
	  public static Context getInstance(){
		    if (instance == null)
		    {
		        instance = LoginActivity.context;
		    }
		    return instance;
		}
	  
	  /** *метод, обработки результатов успешной работы LoginAsync*/
	public void onLoadFinished(Object data) {
		if (data instanceof String)
		{
			
			// successful login 
			if (((String)data).equalsIgnoreCase("success"))
			{
				Log.i("LoginAsync in LoginActivity result", "success");
				currentPass = inputPassword.getText().toString();
				
				
				 SharedPrefs.getInstance().getSharedPrefs().edit().putString(Constants.ENTERED_PASS, forFragmentPassword).commit();
	    		 SharedPrefs.getInstance().getSharedPrefs().edit().putString(Constants.ENTERED_EMAIL, forFragmentLogin).commit();
				Intent i = new Intent(getApplicationContext(),
		                DashboardTabScreen.class);
					//i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		                startActivity(i);
	                }
			else if(data.toString().equalsIgnoreCase("error"))
			{
				 btnProceed.getBackground().setAlpha(120);
			     Toast.makeText(LoginActivity.this, LoginAsync.loginErrorMess, Toast.LENGTH_LONG).show();
		    	 
			}
			else if(data.toString().equalsIgnoreCase("restoreSuccess"))
			{
				if (restoreDialog.isShowing())
					restoreDialog.dismiss();
			     Toast.makeText(LoginActivity.this, RestoreAsync.restoreRes, Toast.LENGTH_LONG).show();
			}
			else if (data.toString().equalsIgnoreCase("restoreError"))
			{
				if (restoreDialog.isShowing())
					restoreDialog.dismiss();
			     Toast.makeText(LoginActivity.this, RestoreAsync.restoreRes, Toast.LENGTH_LONG).show();
			}
			Log.i("LoginActivity data received", data.toString());
		}
	}
	/** *метод, обработки результатов неуспешной работы LoginAsync или прекращения его работы*/
	public void onCancelLoad() {
		Log.i("LoginaAct", "onCancelled");
		 Toast.makeText(LoginActivity.this,getResources().getString(R.string.error_interrupted), Toast.LENGTH_LONG).show();
	}
	@Override
	    protected Dialog onCreateDialog(int id) {
	 
	        switch (id) {
	            case DLG_EXAMPLE1:
	                return createExampleDialog();
	            default:
	                return null;
	        }
	    }
	/** *работа диалога для рестора пароля*/
	    @Override
	    protected void onPrepareDialog(int id, final Dialog dialog) {
	 
	        switch (id) {
	            case DLG_EXAMPLE1:
	                // Clear the input box.
	                final EditText text = (EditText) dialog.findViewById(1);
	                text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
	                text.setText("");
	                restoreDialog = (AlertDialog) dialog;
	                ((AlertDialog) dialog).getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
	                
	                text.addTextChangedListener(new TextWatcher() {

	                    public void onTextChanged(CharSequence s, int start, int before, int count) {
	                    }

	                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	                    }

	                    public void afterTextChanged(Editable s) {
	                    	if (s.length() > 5) {
	                    		Log.i("edit restore", "it was changed");
	                    		((AlertDialog) dialog).getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
	                         } else {
	                        	 ((AlertDialog) dialog).getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
	                         }
	                    }
	                });
	                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
	                {            
	                    public void onClick(View v)
	                    {
	                    	if (faq.EmailValidate(text.getText().toString()))
	       			    	 {
	       			    		 boolean isOnline = faq.isOnline();
	       			    		 if (isOnline) 
	       			    		 {
	       				    		 restorePass = text.getText().toString();
	       				    		 RestoreAsync.execute(LoginActivity.this, LoginActivity.this);
	       				    		 if (dialogDismiss)
	       				    			 dialog.dismiss();
	       			    		 }
	       			    	 }
	       			    	 else 
	       			    	 {
	       			    		 Toast.makeText(LoginActivity.this, getResources().getString(R.string.error_email_format), Toast.LENGTH_SHORT).show();
	       			    	 }
	                    }
	                });
	                break;
	        }
	    }
	    /** *метод создания диалога рестора пароля*/
	  private Dialog createExampleDialog() {
		  
	        AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
	        LayoutInflater inflater = this.getLayoutInflater();

	        // Inflate and set the layout for the dialog
	        // Pass null as the parent view because its going in the dialog layout
	        builder.setView(inflater.inflate(R.layout.restore_dialog, null));
	        builder.setTitle("Forgot password?");
	        builder.setMessage("To reset your password, please enter your email address");
	        LinearLayout layout = new LinearLayout(this);
	        layout.setOrientation(LinearLayout.VERTICAL);
	        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	             LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	        params.setMargins(15, 0, 15, 0);
	         // Use an EditText view to get user input.
	         final EditText input = new EditText(this);
	         input.setId(1);
	         input.setSingleLine();
	         input.setHint("Email Address");
	         layout.addView(input, params);
	         builder.setView(layout);
	         input.getText().toString();
	        builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
//	            	
//			    	 if (EmailValidate(input.getText().toString()))
//			    	 {
//			    		 boolean isOnline = faq.isOnline();
//			    		 if (isOnline) 
//			    		 {
//				    		 Log.i("restore password", pass);
//				    		 restorePass = input.getText().toString();
//				    		 RestoreAsync.execute(LoginActivity.this, LoginActivity.this);
//			    		 }
//			    	 }
//			    	 else 
//			    	 {
//			    		 Toast.makeText(LoginActivity.this, "Format of email is wrong.", Toast.LENGTH_SHORT).show();
//			    		
//			    	 }
	               // return;
	            }
	        });
	        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	 
	            public void onClick(DialogInterface dialog, int which) {
	                return;
	            }
	        });
	        return builder.create();
	    }
		 public void printHashKey() {

		        try {
		            PackageInfo info = getPackageManager().getPackageInfo("com.assignmentexpert",
		                    PackageManager.GET_SIGNATURES);
		            for (Signature signature : info.signatures) {
		                MessageDigest md = MessageDigest.getInstance("SHA");
		                md.update(signature.toByteArray());
		            }
		        } catch (NameNotFoundException e) {

		        } catch (NoSuchAlgorithmException e) {

		        }

		    }
		 private String[] getLoggedIn()
		 {
			 String[] res = null;
			 int size = SharedPrefs.getInstance().getSharedPrefs().getInt(Constants.LOGGED_IN_SIZE, 0);
			 Log.i("logged in size", Integer.toString(size));
			 if (size != 0)
			 {
				 res= new String[size];
				 for (int i=0;i<size;i++)
				 {
					 Log.i("count loop", Integer.toString(i));
					 res[i] = SharedPrefs.getInstance().getSharedPrefs().getString(Constants.LOGGED_IN + Integer.toString(i), "");
					 Log.i("logged in users",  res[i].toString());
				 }
			 }
			
			 return  res;
		 }
		 @Override
		 protected void onResume() {
		     super.onResume();
		     if ( getLoggedIn()!=null){
		    		
		    	 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		                 android.R.layout.simple_dropdown_item_1line, getLoggedIn());
		    	
		    	 inputEmail.setAdapter(adapter);
		    	}
		     // Normal case behavior follows
		 }
		
	
}