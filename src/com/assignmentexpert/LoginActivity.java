package com.assignmentexpert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.library.UserFunctions;

public class LoginActivity extends Activity {
    Button btnLogin;
    Button btnLinkToRegister;
    EditText inputEmail;
    EditText inputPassword;
    TextView loginErrorMsg;
 
    // JSON Response node names
    private static String KEY_STATUS = "status";
    private static String KEY_MESSAGE = "message";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
 
        // Importing all assets like buttons, text fields
        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);
        inputEmail.setText("shurko@ukr.net", TextView.BufferType.EDITABLE);
    	inputPassword.setText("123456", TextView.BufferType.EDITABLE);
        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {
//                String email = inputEmail.getText().toString();
//                String password = inputPassword.getText().toString();
            	
                UserFunctions userFunction = new UserFunctions();
                JSONObject json = userFunction.loginUser("shurko@ukr.net", "123456");
                String strOut =json.toString();
                appendLog(strOut);
                Log.i("output",json.toString());
                Gson gson = new Gson();
//                Toast toast = Toast.makeText(getApplicationContext(), json.toString(), Toast.LENGTH_LONG);
//            	toast.show();
                // check for login response
//                try {
//                    if (json.getString(KEY_STATUS) != null) {
//                        loginErrorMsg.setText("");
//                        String res = json.getString(KEY_STATUS);
//                        if(Integer.parseInt(res) == 1)
//                        {	
//                        	Toast toast = Toast.makeText(getApplicationContext(), json.toString(), Toast.LENGTH_LONG);
//                        	toast.show();
//                        	Log.d("result_tag",json.toString());
//                            // user successfully logged in
//                            // Store user details in SQLite Database
//                         //   DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//                            //JSONObject json_user = json.getJSONObject("user");
// 
//                            // Clear all previous data in database
//                           // userFunction.logoutUser(getApplicationContext());
//                           // db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));                        
// 
//                            // Launch Dashboard Screen
//                            //Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
// 
//                            // Close all views before launching Dashboard
//                           // dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            // startActivity(dashboard);
// 
//                            // Close Login Screen
//                            finish();
//                        }else{
//                            // Error in login
//                            loginErrorMsg.setText("Incorrect username/password");
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
 
        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
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
}