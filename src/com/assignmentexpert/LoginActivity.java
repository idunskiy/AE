package com.assignmentexpert;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

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
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.LoginAsync;
import com.asynctasks.RestoreAsync;
import com.customitems.CustomTextView;
import com.datamodel.Customer;
import com.datamodel.Order;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.library.DatabaseHandler;
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
    EditText inputEmail;
    /** *EditText пароля пользователя */
    EditText inputPassword;
    TextView loginErrorMsg;
   Dialog restoreDialog;
    
   
   /** *публичное статичное поле логина пользователя для использования в LoginAsync */
    public static String forFragmentLogin;
    /** *публичное статичное поле пароля пользователя для использования в LoginAsync */
    public static String forFragmentPassword;
    
    static boolean newUser = false;
    private static Context instance;
    public static String passUserId = null;
    public static Context _context;
	
    /** * поле для диалога */
    private static final int DLG_EXAMPLE1 = 0;
    
    public static Customer getUser;
    /** *SharedPreferences для запоминания значений логина и пароля введенных пользователем */
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    /** *поле для запоминания значения емеила для восстановления пароля */
	public static String restorePass ="";
	public static String currentPass ="";
	final FrequentlyUsedMethods faq = new FrequentlyUsedMethods(LoginActivity.this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
    	
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        new CustomTextView(this);
         // Importing all assets like buttons, text fields
        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        inputEmail.setSelection(0);
        btnProceed= (Button) findViewById(R.id.btnProceed);
//     
        loginErrorMsg = (TextView) findViewById(R.id.login_error);
        btnRestore = (TextView) findViewById(R.id.btnRestore);

        getWindow().setFormat(PixelFormat.RGBA_8888);
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE );
        _context = this;
        editor = sharedPreferences.edit();
        
        printHashKey();

        File directory = new File(Environment.getExternalStorageDirectory()+ "/download/AssignmentExpert");
        Log.i("downloads directory", directory.getPath());
        if (!directory.exists())
        	directory.mkdirs();
       
    	Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        { 
        	if (getIntent().getExtras().getBoolean("relogin") == true)
            	reLogin();
          if(getIntent().getExtras().getString("restError") != null)
        	  Toast.makeText(this, getIntent().getExtras().getString("restError"), Toast.LENGTH_LONG).show();
        	  
        }
        
    	inputPassword.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)) 
				.showSoftInput(inputPassword, 0); 
				inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				//if (inputEmail.getText().toString().equals("Login (E-mail)") |inputEmail.getText().toString().equalsIgnoreCase("Wrong E-mail"))
				inputPassword.setText("");
				inputPassword.setHint("Password");
				
				inputPassword.setTextColor(Color.BLACK);
				
			}
    		
    	});
    	inputEmail.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)) 
				.showSoftInput(inputEmail, 0); 
				if (inputEmail.getText().toString().equalsIgnoreCase("wrong e-mail"))
					inputEmail.getText().clear();
				inputEmail.setHint("E-mail");
				inputEmail.setTextColor(Color.BLACK);
				
			}
    		
    	});
    	inputEmail.setText("shurko@ukr.net");
    	inputPassword.setText("11111");
    	SharedPrefs.getInstance().Initialize(getApplicationContext());
    	if (SharedPrefs.getInstance().getSharedPrefs()!=null)
	    {
    		if (SharedPrefs.getInstance().getSharedPrefs().getBoolean("isChecked", false)==true)
	        {
	        	
	        	forFragmentLogin = sharedPreferences.getString("username", "");
	   		 	forFragmentPassword = sharedPreferences.getString("password", "");
	   		 	LoginAsync.execute(this, this);
	        }
    	}
        Log.i("isChecked in LoginAct", Boolean.toString(SharedPrefs.getInstance().getSharedPrefs().getBoolean("isChecked", false)));
        btnProceed.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				 boolean errorFlag = false;
				 String email = inputEmail.getText().toString();
			     String password= inputPassword.getText().toString();
			     if(email.equals("Login"))
        		 {
        			 inputEmail.getText().clear();
        			 errorFlag = true;
        		 }
			     if(password.equals("Password"))
        		 {
			    	 inputPassword.setText(" ");
			    	 inputPassword.setInputType(InputType.TYPE_CLASS_TEXT);
			    	 inputPassword.setTextColor(Color.RED);
			    	 inputPassword.setText("Should be not empty");
        			 errorFlag = true;
        		 }
			     if(inputPassword.length()<5)
        		 {
			    	 inputPassword.setText(" ");
			    	 inputPassword.setTextColor(Color.RED);
			    	 inputPassword.setInputType(InputType.TYPE_CLASS_TEXT);
			    	 inputPassword.setText("Wrong password");
        			 errorFlag = true;
        		 }
			     
			     if(!EmailValidate(email))
			     {
			    	    inputEmail.setText(" ");
	                	inputEmail.setTextColor(Color.RED);
	                	inputEmail.setText("Wrong E-mail");
	                	errorFlag = true;
			     }
			     
			     if(errorFlag == false)
				  {	
			    	 if (!sharedPreferences.getString("username", "").equals(email))
			    	 {
			    	   newUser = true;
			    	   editor.remove("username");
			    	   editor.commit();
			    	   Log.i("new User action","LoginActivity");
			    	 }
			    	 else 
			    	 {
			    			 newUser = false;
			    			 Log.i("old User action","LoginActivity");
			    	 }
			    	 boolean isOnline = faq.isOnline();
			    	 if (isOnline)
			    		 
				     {
			    		 forFragmentLogin = email;
			    		 forFragmentPassword = password;
			    		 LoginAsync.execute(LoginActivity.this,LoginActivity.this);
				    	 editor.putString("username", email);
				    	 editor.putString("password", password);
				    	 editor.commit();
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
    	Toast toast = Toast.makeText(_context, "Sorry but we have some problems at our server." +  "\r\n" +
     "You should to re-login", Toast.LENGTH_LONG);
    	toast.show();
    	
    }
    /** *метод записи полученных данных от сервера в файл. используется для дебага */
//    public static void appendLog(String text)
//    {       
//       File logFile = new File("sdcard/log.txt");
//       if (!logFile.exists())
//       {
//          try
//          {
//             logFile.createNewFile();
//          } 
//          catch (IOException e)
//          {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//          }
//       }
//       try
//       {
//          BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
//          buf.append(text);
//          buf.newLine();
//          buf.close();
//          
//       }
//       catch (IOException e)
//       {
//          // TODO Auto-generated catch block
//          e.printStackTrace();
//       }
//    }

    /** *метод, проверяющий, что введенная строка  соответствует формату email'a*/
	 boolean EmailValidate(String email)
	    {
	        
	    	Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
				Matcher matcher = pattern.matcher(email);
				boolean matchFound = matcher.matches();
	    	return matchFound;
	    }

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
		        instance = LoginActivity._context;
		    }
		    return instance;
		}
	  
	  /** *метод, обработки результатов успешной работы LoginAsync*/
	public void onLoadFinished(Object data) {
		if (data instanceof String)
		{
			if (((String)data).equalsIgnoreCase("success"))
			{
				Log.i("LoginAsync in LoginActivity result", "success");
				currentPass = inputPassword.getText().toString();
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
		 Toast.makeText(LoginActivity.this, "Interrupted.", Toast.LENGTH_LONG).show();
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
	                    	if (EmailValidate(text.getText().toString()))
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
	       			    		 Toast.makeText(LoginActivity.this, "Format of email is wrong.", Toast.LENGTH_SHORT).show();
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
	  private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		    if (state.isOpened()) {
		    } else if (state.isClosed()) {
		    }
		}
	  private Session.StatusCallback callback = new Session.StatusCallback() {
		    public void call(Session session, SessionState state, Exception exception) {
		        onSessionStateChange(session, state, exception);
		    }
		};
		@Override
		 public void onActivityResult(int requestCode, int resultCode, Intent data) {
		     super.onActivityResult(requestCode, resultCode, data);
		     Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		 }
		 public void printHashKey() {

		        try {
		            PackageInfo info = getPackageManager().getPackageInfo("com.assignmentexpert",
		                    PackageManager.GET_SIGNATURES);
		            for (Signature signature : info.signatures) {
		                MessageDigest md = MessageDigest.getInstance("SHA");
		                md.update(signature.toByteArray());
		                Log.d("TEMPTAGHASH KEY:",
		                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
		            }
		        } catch (NameNotFoundException e) {

		        } catch (NoSuchAlgorithmException e) {

		        }

		    }
		
	
}