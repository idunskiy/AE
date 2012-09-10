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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.datamodel.Category;
import com.datamodel.Level;
import com.datamodel.Order;
import com.datamodel.OrderList;
import com.datamodel.ProcessStatus;
import com.datamodel.Subject;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.DatabaseConnection;
import com.library.ContentRepository;
import com.library.DataParsing;
import com.library.DatabaseHandler;
import com.library.JSONParser;
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
    CheckBox checkbox;
    
    private Dialog progDailog;
    
	public DatabaseHandler databaseHandler=null;
	 private Context _context;
	
 
    // JSON Response node names
    private static String KEY_STATUS = "status";
    private static String KEY_MESSAGE = "message";
    private static String KEY_CATEGORIES = "categories";
    private static String KEY_LEVELS = "levels";
    private static String KEY_DATA = "data";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    static List<Order> orders;
    JSONObject json;
    
    boolean signMe = false;
	

   
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
        checkbox = (CheckBox) findViewById(R.id.checkSigned);
        
     
    	inputPassword.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				//inputPassword.setFocusable(true);
				inputPassword.setText(" ");
				inputPassword.setHint("Password"); inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				inputPassword.setTextColor(Color.BLACK);
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
    	btnClose.setOnClickListener(new View.OnClickListener() {
  	       
            public void onClick(View view) {
            	moveTaskToBack(true);
            } 
    	});
    	
    	checkbox.setOnClickListener(new OnClickListener() {
    		 
    		  public void onClick(View v) {
    	             
    			if (((CheckBox) v).isChecked()) {
    				if (signMe == true)
    				{
    					
    				}
    				
//    				Toast.makeText(LoginActivity.this,
//    			 	   "Bro, try Android :)", Toast.LENGTH_LONG).show();
    			}
    	 
    		  }
    		});
    	
    	
    	 
        inputEmail.setText("shurko@ukr.net");
        inputPassword.setText("123456");

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
	                	inputEmail.setText("Wrong email");
	                	errorFlag = true;
			    	 
			     }
			     
			     if(errorFlag == false)
				  {	signMe = true;
			    	 new LoginTask().execute(email,password);
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
   
    class LoginTask extends AsyncTask<String, String, Void> {

        private DatabaseConnection connect;

		protected void onPreExecute() {
        	progDailog = ProgressDialog.show(LoginActivity.this,"Please wait...", "Retrieving data ...", true);
        }

        protected Void doInBackground(String... param) {
        	 ContentRepository _contactRepo;
        	 ContentRepository _contactRepo2;
        
        	UserFunctions userFunction = new UserFunctions();
        	try {
				json = userFunction.loginUser(param[0], param[1]);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
            // check for login response
            try {
            	if ((json.getString(KEY_STATUS) != null) & json.getString(KEY_MESSAGE).equals((String)"Success"))
            	{
                    
                    String res = json.getString(KEY_STATUS);
                    if(Integer.parseInt(res) == 1)
                    {	
                       	appendLog(json.toString());
                    	DataParsing u = new DataParsing();
                		List<Category> catlist = u.wrapCategories(json);
                		List<ProcessStatus> status = u.wrapStatuses(json);
                		List<Level> level = u.wrapLevels(json);
                		List<Subject> subject = u.wrapSubjects(json);
                		
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
                		try{
                			if (levelquery.query().isEmpty())
                				_contactRepo.saveLevels(level);
                		}
                		catch (IllegalStateException e)
                		{
                			getHelper1().open();			
                			e.printStackTrace();
                			
                		}

                		JSONObject k= userFunction.getOrders("1" , "10");
                		String order_res = k.getString(KEY_STATUS);
                		orders = u.wrapOrders(k);
                		if (Integer.parseInt(order_res)==1)	
                		{	
             
                			OrderList b= new OrderList();
                			b.setOrders(orders);
               
                		}
                     }
            
          	}
            	else
            	{
            		final String wrong = "Wrong password"; 
            		publishProgress(wrong);
            	}
         }
          catch (JSONException e) {
            e.printStackTrace();
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
            return(null);
        }
        protected void onProgressUpdate(String ... progress) {
        	inputEmail.setText(progress[0]);
        	inputPassword.setText(progress[0]);
		}

        protected void onPostExecute(Void unused) {
        	
           Intent i = new Intent(getApplicationContext(),
                   DashboardActivity.class);
           
                   startActivity(i);
                   progDailog.dismiss();
          
        }
      }
    
    	
    
    public void appendLog(String text)
    {       
       File logFile = new File("sdcard/log.file");
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