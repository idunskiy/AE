package com.assignmentexpert;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.LoginAsync;
import com.asynctasks.RestoreAsync;
import com.customitems.CustomMenuButton;
import com.customitems.CustomTextView;
import com.datamodel.Customer;
import com.datamodel.Order;
import com.library.ContentRepository;
import com.library.DatabaseHandler;
import com.library.FrequentlyUsedMethods;
import com.library.SharedPrefs;
import com.tabscreens.DashboardTabScreen;
import com.tabscreens.LoginTabScreen;

public class LoginActivity extends FragmentActivity implements Runnable, ITaskLoaderListener{
    private static final int DIALOG2_KEY = 0;
	private static final int DIALOG1_KEY = 1;
	CustomMenuButton btnLogin;
    Button btnLinkToRegister;
    Button btnProceed;
    TextView btnRestore;
    Button btnClose;
    EditText inputEmail;
    EditText inputPassword;
    TextView loginErrorMsg;
    CheckBox checkboxSignMe;
   
    
    public static String forFragmentLogin;
    public static String forFragmentPassword;
    static boolean loginError = false;
    static boolean newUser = false;
    private static Context instance;
    public static String passUserId = null;
    static List<Order> orders;
    public static Context _context;
	
 
    // JSON Response node names
    private static String KEY_STATUS = "status";
    private static String KEY_ID = "id";
    private static String KEY_MESSAGE = "message";
    private static String KEY_CATEGORIES = "categories";
    private static String KEY_LEVELS = "levels";
    private static String KEY_DATA = "data";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    private static final int DLG_EXAMPLE1 = 0;
    public static Customer getUser;
    
    private Dialog progDailog;
 	public DatabaseHandler databaseHandler=null;
    JSONObject json;
    boolean signMe = false;
    boolean value = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
	private TextView imageHeader;
	public static String restorePass ="";
	public static String currentPass ="";
	final FrequentlyUsedMethods faq = new FrequentlyUsedMethods(LoginActivity.this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
//    	InputMethodManager imm = (InputMethodManager)getSystemService(
//			      Context.INPUT_METHOD_SERVICE);
//	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.login);
    	
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CustomTextView a = new CustomTextView(this);
         // Importing all assets like buttons, text fields
        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        inputEmail.setSelection(0);
        btnProceed= (Button) findViewById(R.id.btnProceed);
//        btnLogin = (CustomMenuButton) findViewById(R.id.btnLogin);
//     
//        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
//        btnClose = (Button) findViewById(R.id.btnClose);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);
        btnRestore = (TextView) findViewById(R.id.btnRestore);
       // checkboxSignMe = (CheckBox) findViewById(R.id.checkSigned);
        imageHeader =  (TextView) findViewById(R.id.imageView1);

        getWindow().setFormat(PixelFormat.RGBA_8888);
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE );
        _context = this;
        editor = sharedPreferences.edit();
        
        
        File directory = new File(Environment.getExternalStorageDirectory()+ "/download/AssignmentExpert");
        Log.i("downloads directory", directory.getPath());
        if (!directory.exists())
        	directory.mkdirs();
       
    	Log.d("user while login",sharedPreferences.getString("username", ""));
    	Log.d("password while login",sharedPreferences.getString("password", ""));
    	Log.d("password while login",Boolean.toString(sharedPreferences.contains("logout")));
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
				if (inputEmail.getText().toString().equals("Login (E-mail)") |inputEmail.getText().toString().equalsIgnoreCase("Wrong E-mail"))
				inputPassword.setText("");
				inputPassword.setHint("Password");
				
				inputPassword.setTextColor(Color.BLACK);
				
			}
    		
    	});
    	inputEmail.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)) 
				.showSoftInput(inputEmail, 0); 
				inputEmail.setText("");
				inputEmail.setHint("E-mail");
				inputEmail.setTextColor(Color.BLACK);
				
			}
    		
    	});
    	inputEmail.setText("shurko@ukr.net");
    	inputPassword.setText("11111");
    	SharedPrefs.getInstance().Initialize(getApplicationContext());
    	if (SharedPrefs.getInstance().getSharedPrefs().getBoolean("isChecked", false)==true)
        {
        	
        	forFragmentLogin = sharedPreferences.getString("username", "");
   		 	forFragmentPassword = sharedPreferences.getString("password", "");
   		 	LoginAsync.execute(this, this);
        }
        Log.i("isChecked in LoginAct", Boolean.toString(SharedPrefs.getInstance().getSharedPrefs().getBoolean("isChecked", false)));
        btnProceed.setOnClickListener(new View.OnClickListener() {
 	       
            private ContentRepository _contactRepo;

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
			    	 signMe = true;
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
    private DatabaseHandler getHelper1() {
		if (databaseHandler == null) {
			
			databaseHandler = DatabaseHandler.getHelper(getApplicationContext());
			
		}
		return (DatabaseHandler) databaseHandler;
	}
    
    public  void reLogin()
    {
    	
//    	editor.remove("isChecked");
//    	editor.commit();
    	
    	signMe = false;
    	inputEmail.setText(sharedPreferences.getString("username", ""));
    	inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    	inputPassword.setText(sharedPreferences.getString("password", ""));
    	Toast toast = Toast.makeText(_context, "Sorry but we have some problems at our server." +  "\r\n" +
     "You should to re-login", Toast.LENGTH_LONG);
    	toast.show();
    	
    }
    
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
    	   
          //BufferedWriter for performance, true to set append to file flag
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

	public void run() {
		// TODO Auto-generated method stub
		
	}
	 boolean EmailValidate(String email)
	    {
	        
	    	Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
				Matcher matcher = pattern.matcher(email);
				boolean matchFound = matcher.matches();
	    	return matchFound;
	    }

	  @Override
		protected void onDestroy() {
			super.onDestroy();

			if (getHelper1() != null) {
				getHelper1().close();
				databaseHandler = null;
			}
			
		}
	  @Override
	    public void onBackPressed() {
	        // Do Here what ever you want do on back press;
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
	  

	public void onLoadFinished(Object data) {
		if (data instanceof String)
		{
			if (((String)data).equalsIgnoreCase("success"))
			{
				Log.i("LoginAsync in LoginActivity result", "success");
	//			Intent i = new Intent(getApplicationContext(),
	//                DashboardActivityAlt.class);
	//                startActivity(i);
				currentPass = inputPassword.getText().toString();
				Intent i = new Intent(getApplicationContext(),
		                DashboardTabScreen.class);
		                startActivity(i);
	                }
			else if(data.toString().equalsIgnoreCase("error"))
			{
				 btnProceed.getBackground().setAlpha(120);
			     Toast.makeText(LoginActivity.this, LoginAsync.loginErrorMess, Toast.LENGTH_LONG).show();
		    	 
			}
			else if(data.toString().equalsIgnoreCase("restoreSuccess"))
			{
			     Toast.makeText(LoginActivity.this, RestoreAsync.restoreRes, Toast.LENGTH_LONG).show();
			}
			Log.i("LoginActivity data received", data.toString());
		}
	}
	@Override
	public void onResume()
	{
	    super.onResume();
	    
	}

	public void onCancelLoad() {
		Log.i("LoginaAct", "onCancelled");
		 Toast.makeText(LoginActivity.this, "Interrupted.", Toast.LENGTH_LONG).show();
	     finish();
    	 Intent i = new Intent(getApplicationContext(),
	                LoginTabScreen.class); 
    	 startActivity(i);
    	 
	}
	private void init(AttributeSet attrs) { 
	    TypedArray a=this.obtainStyledAttributes(
	         attrs,
	         R.styleable.MyCustomView);

	    //Use a
	    Log.i("test",a.getString(
	         R.styleable.MyCustomView_android_text));
	    Log.i("test",""+a.getColor(
	         R.styleable.MyCustomView_android_textColor, Color.BLACK));
	    Log.i("test",a.getString(
	         R.styleable.MyCustomView_extraInformation));

	    //Don't forget this
	    a.recycle();
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
	 
	    @Override
	    protected void onPrepareDialog(int id, Dialog dialog) {
	 
	        switch (id) {
	            case DLG_EXAMPLE1:
	                // Clear the input box.
	                EditText text = (EditText) dialog.findViewById(1);
	                text.setText("");
	                break;
	        }
	    }
	  private Dialog createExampleDialog() {
		  
	        AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
	        builder.setTitle("Forgot password?");
	        builder.setMessage("To reset your password, please enter your email address");
	 
	         // Use an EditText view to get user input.
	         final EditText input = new EditText(this);
	         input.setId(1);
	         input.setHint("Email Address");
	         builder.setView(input);
	         final String pass = input.getText().toString();
	        builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
	 
	            public void onClick(DialogInterface dialog, int whichButton) {
	            	
			    	 if (EmailValidate(input.getText().toString()))
			    	 {
			    		 boolean isOnline = faq.isOnline();
			    		 if (isOnline) 
			    		 {
				    		 Log.i("restore password", pass);
				    		 restorePass = input.getText().toString();
				    		 RestoreAsync.execute(LoginActivity.this, LoginActivity.this);
			    		 }
			    	 }
			    	 else 
			    	 {
			    		 Toast.makeText(LoginActivity.this, "Format of email is wrong.", Toast.LENGTH_SHORT).show();
			    		 
			    	 }
	                return;
	            }
	        });
	 
	        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	 
	            public void onClick(DialogInterface dialog, int which) {
	                return;
	            }
	        });
	 
	        return builder.create();
	    }
	
}