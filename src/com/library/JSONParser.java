package com.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {
	
	   
	    static JSONObject jObj = null;
	    static JSONArray jArr = null;
	    static String json = "";
	    private static final DefaultHttpClient httpClient = new DefaultHttpClient() ;
	    String jsonData = null;
		List<Cookie> cookies;
	    private static String cookie;
	    
	     // constructor
	    public JSONParser() {
	    	
	    }
	    public static DefaultHttpClient getInstance() { return httpClient; }
	    
	    public JSONObject getJSONFromUrl(String url, List<NameValuePair> params, RequestMethod method) throws Exception 
	    {
	    	
	    	  RestClient client = new RestClient(url);
	    	  for (NameValuePair param: params)
	    	  {	
	    		  Log.i("params",param.getName()+param.getValue());
	    		  client.AddParam(param.getName(), param.getValue());  
	    	  }

	    	  
	    	 InputStream is = client.Execute(method);
	        // Log.i("params",client.getResponse().toString());
	          
	    	//InputStream is = HttpClient.getResponse(url, params);
	         // is = client.
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
	            Log.i("sb string in JSONObject", sb.toString());
	            if (sb != null)
	               {json = sb.toString();
	            
	            }
	            Log.i("final response",json);
	        } 
	        catch (Exception e) {
	            Log.e("Buffer Error JSON Parsing", "Error converting result " + e.toString());
	            
	        }
	        
	 
	        try {
	        	
	            jObj = new JSONObject(json);
	        } catch (JSONException e) {
	        	
	            Log.e("JSON Parser ", "Error parsing data " + e.toString());
	        }
	        
	        return jObj;
	    }
	    
	    
	    
	    
	    
	    public JSONArray getJSONArrayFromUrl(String url, List<NameValuePair> params, RequestMethod method) throws Exception 
	    {
	    	
	    	  RestClient client = new RestClient(url);
	    	  for (NameValuePair param: params)
	    	  {	
	    		  Log.i("params",param.getName()+param.getValue());
	    		  client.AddParam(param.getName(), param.getValue());  
	    	  }

	    	  
	    	 InputStream is = client.Execute(method);
	        // Log.i("params",client.getResponse().toString());
	          
	    	//InputStream is = HttpClient.getResponse(url, params);
	         // is = client.
	        try {
	        	
	        	BufferedReader reader = new BufferedReader(new InputStreamReader(is));

	            StringBuilder sb = new StringBuilder();
	            
	            String line = null;
	            try
	            {
		            while ((line = reader.readLine()) != null)
		            {
		                sb.append(line); //+ "n");
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
	            if (json==null)
	            	Log.i("JSONParser JSONAray","json response is null");
	            else
	                Log.i("final response",json);
	            
	        } 
	        catch (Exception e) {
	            Log.e("Buffer Error", "Error converting result " + e.toString());
	        }
	 
	        try {
	        	
	        	jArr = new JSONArray(json);
	        	  Log.i("JSONArray response",jArr.toString());
	        } catch (JSONException e) {
	        	
	            Log.e("JSON Parser", "Error parsing data " + e.toString());
	        }
	        
	        return jArr;
	    }
	    
	    public String getStringCaptcha (String url, List<NameValuePair> params) throws Exception
	    {
	    	
	    	  RestClient client = new RestClient(url);
	    	 
	    	  for (NameValuePair param: params)
	    	  {
	    		  client.AddParam(param.getName(), param.getValue());  
	    	  }

	          try
	          {
	              client.Execute(RequestMethod.POST);
	          }
	          catch (Exception e)
	          {
	            
	          }
	          
		    InputStream is = client.Execute(RequestMethod.POST);
//	    	InputStream is = HttpClient.getResponse(url, params);
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(
	                    is, "iso-8859-1"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "n");
	            }
	            is.close();
	            json = sb.toString();
	            
	          // json = client.getResponse();
	           
	        } 
	        catch (Exception e) {
	            Log.e("JsonParser Buffer Error", "Error converting result " + e.toString());
	        }
	        return json;

	    }
	    

	    
}
