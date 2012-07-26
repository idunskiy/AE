package com.library;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.datamodel.Order;
import com.google.gson.Gson;

import android.content.Context;


public class UserFunctions {
	private JSONParser jsonParser;
	 
    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String loginURL = "http://192.168.0.250/app_dev.php/api/login_check";
    private static String registerURL = "http://10.0.2.2/ah_login_api/";
 
    private static String login_tag = "login";
    private static String register_tag = "register";
 
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
        
    }
 
    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        String jsonStr = json.toString();
        Gson gson=new Gson();
        Order a = gson.fromJson(jsonStr,Order.class);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }
 
    /**
     * function make Login Request
     * @param name
     * @param email
     * @param password
     * */
    public JSONObject registerUser(String name, String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
 
        // getting JSON Object
       // JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return null;
    }
 
    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }
 
    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

}
