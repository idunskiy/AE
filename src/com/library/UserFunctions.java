package com.library;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.assignmentexpert.FileManagerActivity;



public class UserFunctions {
	private JSONParser jsonParser;
	 
    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String loginURL = "https://192.168.0.250/app_dev.php/api/login_check";
    private static String ordersURL = "https://192.168.0.250/app_dev.php/api/client/orderList/";
    private static String registerURL = "http://192.168.0.250/app_dev.php/api/client/register/";
    private static String captchaURL = "http://192.168.0.250/app_dev.php/captcha/regenerate/";
    private static String restoreURL = "http://192.168.0.250/app_dev.php/api/client/request/resetting/";
    private static String attachURL = "http://192.168.0.250/app_dev.php/api/client/request/resetting/";
   
    private static String sendOrderURL = "http://192.168.0.250/app_dev.php/api/client/createOrder/";
    private static String sendMessageURL = "http://192.168.0.250//app_dev.php/api/client/createMessage/";
    private static String login_tag = "login";
    private static String register_tag = "register";
    int flag =0;

	private JSONObject jObj;

	private String json;
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
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params, RequestMethod.POST);
        return json;
    }
    
    public JSONObject getOrders(String page, String perpage) throws Exception{
        // Building Parameters
    	
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String stStart = String.valueOf(page);
    	String stEnd = String.valueOf(perpage);
    	Log.i("getOrders start",stStart);
    	Log.i("getOrders end",stEnd);
        params.add(new BasicNameValuePair("page", stStart));
        params.add(new BasicNameValuePair("perpage", stEnd));
        JSONObject json = jsonParser.getJSONFromUrl(ordersURL,params,RequestMethod.POST);
      //  Log.i("orders",json.toString());
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
 
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params,RequestMethod.POST);
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
        JSONObject json = jsonParser.getJSONFromUrl(restoreURL, params,RequestMethod.POST);
        return json;
        
    }
    
    public void sendFiles(List<NameValuePair> params) throws Exception
	 {
		 if (!FileManagerActivity.getFinalAttachFiles().isEmpty())
		 {		
			 
			 RestClient client = new RestClient(attachURL);
//			 Part[] parts = new Part[FileManagerActivity.getFinalAttachFiles().size()]  ;
	    	  for (NameValuePair param: params)
	    	 {	
	    		  
	    		  client.AddParam(param.getName(), param.getValue());
	    		  
	    	 }
//			 
			MultipartEntity entity = new MultipartEntity();
			for (File file : FileManagerActivity.getFinalAttachFiles())
			{ 	
				
				 entity.addPart(file.getName(), new FileBody(file));
				
//				 PostMethod postMethod = new PostMethod(url);
//				 
//				 Part[] parts = {new FilePart(file.getName(), file)};
//				 postMethod.setParameter("name", "value"); // set parameters like this instead in separate call
//
//				 postMethod.setRequestEntity( new MultipartRequestEntity(parts, postMethod.getParams()));
			 }
			client.Execute(RequestMethod.POST);
		 }
		 
		 
	 }
    public JSONObject sendOrder(String title, String category, String level, String deadline, String info, String explanation,
    		String specInfo, String timezone, List<File> files) throws Exception
    { 
    	
   	
    	RestClient restClient = new RestClient(sendOrderURL);
    	
    	restClient.AddEntity("title", new StringBody(title));
    	restClient.AddEntity("category", new StringBody(category));
    	restClient.AddEntity("level", new StringBody(level));
    	restClient.AddEntity("deadline", new StringBody(deadline));
    	restClient.AddEntity("info", new StringBody(info));
    	restClient.AddEntity("dtl_expl",new StringBody(explanation));
    	restClient.AddEntity("specInfo", new StringBody(specInfo));
    	restClient.AddEntity("timezone", new StringBody(timezone));
    	for (File file : files)
    	{
    		Log.i("files to upload", file.toString());
    		ContentBody fbody = new FileBody(( File )file, "application/octet-stream", "UTF-8");
    		restClient.AddEntity("files[]", fbody);
    		
    	}
    	
    	 InputStream is = restClient.Execute(RequestMethod.POST_UPLOAD);
    	  try {
	        	
	        	BufferedReader reader = new BufferedReader(new InputStreamReader(is));

	            StringBuilder sb = new StringBuilder();
	            
	            String line = null;
	            try
	            {
		            while ((line = reader.readLine()) != null)
		            {
		                sb.append(line);// + "n");
		            }
		            
	            }
	            catch (IOException e)
	            {
	            	e.printStackTrace();
	            }
	            finally
	            {
	            	try
	            	{
	            		is.close();
	            	}
	            	catch (IOException e)
		            {
		            	e.printStackTrace();
		            }
	            }
	            
	          
	               json = sb.toString();
	            Log.i("new order result", json);
	            
	           
	        } 
	        catch (Exception e) {
	            Log.e("Buffer Error JSON Parsing at ORDER_CREATE", "Error converting result " + e.toString());
	            
	        }
	        try {
	        	
	            jObj = new JSONObject(json);
	        } catch (JSONException e) {
	        	
	            Log.e("JSON Parser in order creating", "Error parsing data " + e.toString());
	        }
	        
	        return jObj;
        
    }
    public JSONObject sendMessage(String category,  String deadline, String price, String body,
    		String order, List<File> files) throws Exception
    { 
    	
        RestClient restClient = new RestClient(sendMessageURL);
    	restClient.AddEntity("deadline", new StringBody("2012-11-14 14:00:00"));
    	restClient.AddEntity("category", new StringBody(category));
    	restClient.AddEntity("price", new StringBody(price));
    	restClient.AddEntity("body", new StringBody(body, Charset.forName("UTF-8")));
    	restClient.AddEntity("order",new StringBody(order));
    	
    	for (File file : files)
    	{
    		Log.i("files to upload", file.toString());
    		ContentBody fbody = new FileBody(( File )file, "application/octet-stream","UTF-8");
    		restClient.AddEntity("files[]", fbody);
    		
    	}
    	
    	 InputStream is = restClient.Execute(RequestMethod.POST_UPLOAD);
    	  try {
	        	
	        	BufferedReader reader = new BufferedReader(new InputStreamReader(is));

	            StringBuilder sb = new StringBuilder();
	            
	            String line = null;
	            try
	            {
		            while ((line = reader.readLine()) != null)
		            {
		                sb.append(line);// + "n");
		            }
		            
	            }
	            catch (IOException e)
	            {
	            	e.printStackTrace();
	            }
	            finally
	            {
	            	try
	            	{
	            		is.close();
	            	}
	            	catch (IOException e)
		            {
		            	e.printStackTrace();
		            }
	            }
	            
	          
	               json = sb.toString();
	            Log.i("new order result", json);
	            
	           
	        } 
	        catch (Exception e) {
	            Log.e("Buffer Error JSON Parsing at MESSAGE_CREATE", "Error converting result " + e.toString());
	            
	        }
	        
	 
	        try {
	        	
	            jObj = new JSONObject(json);
	        } catch (JSONException e) {
	        	
	            Log.e("JSON Parser in message creating", "Error parsing data " + e.toString());
	        }
	        
	        return jObj;

        
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
 
    
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

}
