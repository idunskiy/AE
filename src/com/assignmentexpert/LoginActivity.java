package com.assignmentexpert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.datamodel.Category;
import com.datamodel.Customer;
import com.datamodel.EssayCreationStyle;
import com.datamodel.EssayType;
import com.datamodel.Level;
import com.datamodel.Order;
import com.datamodel.ProcessStatus;
import com.datamodel.ProductType;
import com.datamodel.Subject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.DatabaseConnection;
import com.library.ContentRepository;
import com.library.DataParsing;
import com.library.DatabaseHandler;
import com.library.RestClient;
import com.library.UserFunctions;

public class LoginActivity extends Activity implements Runnable {
    private static final int DIALOG2_KEY = 0;
	private static final int DIALOG1_KEY = 1;
	Button btnLogin;
    Button btnLinkToRegister;
    Button btnProceed;
    Button btnRestore;
    Button btnClose;
    EditText inputEmail;
    EditText inputPassword;
    TextView loginErrorMsg;
    CheckBox checkboxSignMe;
    
    static boolean newUser = false;
    
   
    static boolean loginError = false;
    
    private Dialog progDailog;
    
	public DatabaseHandler databaseHandler=null;
	 private static Context _context;
	
 
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
    static Customer getUser;
    public static String passUserId = null;
    SharedPreferences.Editor editor;
    static List<Order> orders;
    JSONObject json;
    
    boolean signMe = false;
    SharedPreferences sharedPreferences;
    boolean value = false;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
         // Importing all assets like buttons, text fields
        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        btnProceed= (Button) findViewById(R.id.btnProceed);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        
       
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        btnClose = (Button) findViewById(R.id.btnClose);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);
        btnRestore = (Button) findViewById(R.id.btnRestore);
        checkboxSignMe = (CheckBox) findViewById(R.id.checkSigned);
        RestClient helper = new RestClient(getBaseContext());
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE );
        _context = this;
        editor = sharedPreferences.edit();
    	Log.d("user while login",sharedPreferences.getString("username", ""));
    	Log.d("password while login",sharedPreferences.getString("password", ""));
    	Log.d("password while login",Boolean.toString(sharedPreferences.contains("logout")));
    	Bundle bundle = getIntent().getExtras();
        if (bundle != null)
          if (getIntent().getExtras().getBoolean("relogin") == true)
            	reLogin();
    	inputPassword.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				//inputPassword.setFocusable(true);
				inputPassword.setText("");
				inputPassword.setHint("Password");
				inputPassword.setHint("Password"); 
				inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				inputPassword.setTextColor(Color.BLACK);
				return false;
			}
    		
    	});
    	inputEmail.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (inputEmail.getText().toString().equals("Login (E-mail)") |inputEmail.getText().toString().equals("Wrong E-mail"))
				inputEmail.setText("");
				inputEmail.setHint("E-mail");
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)) 
				.showSoftInput(inputEmail, 0); 
				inputEmail.setTextColor(Color.BLACK);
				return false;
			}
			
    		
    	});
    	btnClose.setOnClickListener(new View.OnClickListener() {
  	       
            public void onClick(View view) {
            	moveTaskToBack(true);
            } 
    	});
    	
    	checkboxSignMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
            {           
            	if (isChecked == true)
            	{
	            	editor.putString("username",inputEmail.getText().toString());
			        editor.putString("password",inputPassword.getText().toString());
			        editor.putBoolean("isChecked", checkboxSignMe.isChecked());
			        editor.commit();
            	}
            }
            });
    	
    	inputEmail.setText("shurko@ukr.net");
    	inputPassword.setText("123456");

        if(sharedPreferences.getBoolean("isChecked", signMe)) 
        {
        	new LoginTask().execute(sharedPreferences.getString("username", ""),sharedPreferences.getString("password", ""));
        }

    	// Login button Click Event
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
			    	 Log.i("new user boolean", Boolean.toString(newUser));
			    	 
			    	 Log.i("email String", email);
			    	 new LoginTask().execute(email,password);
			    	 Log.i("shared Prefs before", sharedPreferences.getString("username", ""));
			    	 editor.putString("username", email);
			    	 editor.putString("password", password);
			    	 //editor.putString("oldUser", email);
			    	 editor.commit();
			    	 Log.i("shared Prefs afeter", sharedPreferences.getString("username", ""));
				  }

			}
			
        });
       
	
        // Link to Register Screen
       btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {
            	
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                
            }
        });
   
       btnRestore.setOnClickListener(new View.OnClickListener() {
    	   
           public void onClick(View view) {
               Intent i = new Intent(getApplicationContext(),
                       RestoreActivity.class);
               startActivity(i);
               
           }
       });
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
    	checkboxSignMe.setChecked(false);
    	inputEmail.setText(sharedPreferences.getString("username", ""));
    	inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    	inputPassword.setText(sharedPreferences.getString("password", ""));
    	Toast toast = Toast.makeText(_context, "Sorry but we have some problems at our server." +  "\r\n" +
     "You should to re-login", Toast.LENGTH_LONG);
    	toast.show();
    	
    }
   
    class LoginTask extends AsyncTask<String, String, Void> {

        private DatabaseConnection connect;

		protected void onPreExecute() {
        	progDailog = ProgressDialog.show(LoginActivity.this,"Please wait...", "Retrieving data ...", true);
        }

        protected Void doInBackground(String... param)  {
        	 ContentRepository _contactRepo;
        	 ContentRepository _contactRepo2;
        
        	UserFunctions userFunction = new UserFunctions();
        	try {
				json = userFunction.loginUser(param[0], param[1]);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
			}
        	
            // check for login response
            try {
//            	if (RestClient.inetError == true)
//            	{throw new NoInternetException();}
            	if ((json.getString(KEY_STATUS) != null) & json.getString(KEY_MESSAGE).equals((String)"Success"))
            	{
            		
                    String res = json.getString(KEY_STATUS);
                    if(Integer.parseInt(res) == 1)
                    {	
                       	appendLog(json.toString());
                    	DataParsing u = new DataParsing();
                    	editor.putString("user_id",u.wrapUserId(json));
                		editor.commit();
                		passUserId = u.wrapUserId(json);
                		List<Category> catlist = u.wrapCategories(json);
                		List<ProcessStatus> status = u.wrapStatuses(json);
                		List<Level> level = u.wrapLevels(json);
                		List<Subject> subject = u.wrapSubjects(json);
                		List<EssayType> essayTypes = u.wrapEssayType(json);
                		List<EssayCreationStyle> essayCreatStyle = u.wrapEssayCreationStyle(json);
                		Log.i("essayTypes", essayTypes.toString());
                		Log.i("essayCreationStyles", essayCreatStyle.toString());
                		getUser = u.wrapUser(json);
                		Log.i("get user from", getUser.toString());
                		
                		_contactRepo=new ContentRepository(getContentResolver(),getApplicationContext());

                		DatabaseHandler dbHelper=getHelper1();
                		
                		dbHelper.open();
                		dbHelper.getWritableDatabase();
                		Log.i("isOpened",Boolean.toString(dbHelper.isOpen()));
                		
                		Dao<Subject, Integer> daoSubject=dbHelper.getDao(Subject.class);
                		QueryBuilder<Subject,Integer> Subjectquery = daoSubject.queryBuilder();

                		try
                		{
                			
                		   	if (Subjectquery.query().isEmpty())
                		   		_contactRepo.saveSubjects(subject);
                		}
                		catch (IllegalStateException e)
                		{
                			dbHelper.open();			
                			e.printStackTrace();
                			
                		}
                		Log.i("subjects",daoSubject.queryForAll().toString());
                		
                		Dao<Category, Integer> daoCat=dbHelper.getDao(Category.class);
                		QueryBuilder<Category,Integer> catquery = daoCat.queryBuilder();
                		try{
                			dbHelper.open();	
                			if (catquery.query().isEmpty())
                			{
                				_contactRepo.saveCategories(catlist);
                			}
                		}
                		catch (IllegalStateException e)
                		{
                			dbHelper.open();			
                			e.printStackTrace();
                			
                		}
                		Log.i("Rest client error flag", Boolean.toString(RestClient.inetError));
                		Dao<ProcessStatus, Integer> daoStatus=dbHelper.getDao(ProcessStatus.class);
                		QueryBuilder<ProcessStatus,Integer> Statusquery = daoStatus.queryBuilder();
                		try{
                			if (Statusquery.query().isEmpty())
                			 _contactRepo.saveStatuses(status);
                		}
                		catch (IllegalStateException e)
                		{
                			dbHelper.open();			
                			e.printStackTrace();
                			
                		}

                		Dao<Level, Integer> daoLevel=dbHelper.getDao(Level.class);
                		QueryBuilder<Level,Integer> levelquery = daoLevel.queryBuilder();
                		try
                		{
                			
                			if (levelquery.query().isEmpty())
                				_contactRepo.saveLevels(level);
                		}
                		catch (IllegalStateException e)
                		{
                			getHelper1().open();			
                			e.printStackTrace();
                			
                		}
                		
                		Dao<EssayType, Integer> daoEssayType=dbHelper.getDao(EssayType.class);
                		QueryBuilder<EssayType,Integer> essayTypequery = daoEssayType.queryBuilder();
                		try
                		{
                			
                			if (essayTypequery.query().isEmpty())
                				_contactRepo.saveEssayTypes(essayTypes);
                		}
                		catch (IllegalStateException e)
                		{
                			getHelper1().open();			
                			e.printStackTrace();
                			
                		}
                		Log.i("essayType in database",daoEssayType.queryForAll().toString());
                		
                		Dao<EssayType, Integer> daoEssayCreationStyle=dbHelper.getDao(EssayCreationStyle.class);
                		QueryBuilder<EssayType,Integer> essayCrStyleQuery= daoEssayCreationStyle.queryBuilder();
                		try
                		{
                			
                			if (essayCrStyleQuery.query().isEmpty())
                				_contactRepo.saveEssayCreationStyles(essayCreatStyle);
                		}
                		catch (IllegalStateException e)
                		{
                			getHelper1().open();			
                			e.printStackTrace();
                			
                		}
                		Log.i("essayCreationStyles in database",daoEssayCreationStyle.queryForAll().toString());
                		
                     }
            
          	}
            	else
            	{
            		final String wrong = "Incorrect login or password"; 
            		
            		publishProgress(wrong);
            	}
         }
          catch (JSONException e) {
            e.printStackTrace();
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//           catch(NoInternetException  e)
//           {
//            	this.cancel(true);
//    			editor.clear();
//    			editor.commit();
//    			((LoginActivity)_context).finish();
//    			_context.startActivity(getIntent());
//    			 Toast.makeText(LoginActivity.this, "You've lost internet connection. You should try later.",Toast.LENGTH_LONG)
//        		   .show();
//    			e.printStackTrace();
//            }
//        catch (HttpHostConnectException e) {
//        	
//		}
             
            return(null);
        }
        protected void onProgressUpdate(String ... progress) {
//        	inputEmail.setText(progress[0]);
//        	inputPassword.setText(progress[0]);
        	this.cancel(true);
        	Toast toast = Toast.makeText(LoginActivity.this, progress[0], Toast.LENGTH_LONG);
        	toast.show();
        	
        	Intent intent = getIntent();
        	finish();
        	startActivity(intent);
        	
        	
		}

        protected void onPostExecute(Void unused) {
        	
           Intent i = new Intent(getApplicationContext(),
                   DashboardActivityAlt.class);
                   startActivity(i);
                   progDailog.dismiss();
          
        }
      }
    
    	
    
    public void appendLog(String text)
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
				Log.i("destroy","destroy");
			}
			
		}
	
}