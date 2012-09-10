package com.library;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.util.Xml.Encoding;



public class UserFunctions {
	private JSONParser jsonParser;
	 
    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String loginURL = "https://192.168.0.250/app_dev.php/api/login_check";
    private static String ordersURL = "https://192.168.0.250/app_dev.php/api/client/orderList/";
    private static String registerURL = "http://192.168.0.250/app_dev.php/api/client/register/";
    private static String captchaURL = "http://192.168.0.250/app_dev.php/captcha/regenerate/";
    private static String restoreURL = "http://192.168.0.250/app_dev.php/api/client/request/resetting/";
    private static String login_tag = "login";
    private static String register_tag = "register";
    int flag =0;
    // constructor
    public UserFunctions(){
        
		jsonParser = new JSONParser();
        
    }
 
    /**
     * function make Login Request
     * @param email
     * @param password
     * @throws Exception 
     * */
    public JSONObject loginUser(String email, String password) throws Exception{
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }
    
    public JSONObject getOrders(String page, String perpage) throws Exception{
        // Building Parameters
    	
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        
        params.add(new BasicNameValuePair("page", page));
        params.add(new BasicNameValuePair("perpage", perpage));
        JSONObject json = jsonParser.getJSONFromUrl(ordersURL,params);
        Log.i("orders",json.toString());
        return json;
    }
    
 
  
    public JSONObject registerUser(String name, String email, String password, String confpass, String captcha) throws Exception
    {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("reg[name]", name));
        params.add(new BasicNameValuePair("reg[email]", email));
        params.add(new BasicNameValuePair("reg[password]", password));
        params.add(new BasicNameValuePair("reg[passwordConfirmed]", confpass));
        params.add(new BasicNameValuePair("reg[captcha]", captcha));
        params.add(new BasicNameValuePair("reg[agree]", "on"));
 
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        return json;
    }
  
    
    public Bitmap getCaptcha() throws Exception
    { 
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
     	String obj  = jsonParser.getStringCaptcha(captchaURL, params);
        String b= obj.toString();
     	b = b.substring(23, b.length()-1);
    	byte [] encodeByte=Base64.decode(b,Base64.NO_PADDING);
        Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }
    
    public JSONObject restorePassword(String newPass, String captcha) throws Exception
    { 
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", newPass));
        params.add(new BasicNameValuePair("captcha", captcha));
        JSONObject json = jsonParser.getJSONFromUrl(restoreURL, params);
        return json;
        
    }
    
    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context)
    {
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
