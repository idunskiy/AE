package com.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {
	   static InputStream is = null;
	    static JSONObject jObj = null;
	    static String json = "";
	    String jsonData = null;
	    // constructor
	    public JSONParser() {
	 
	    }
	 
	    public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {
	 
	        // Making HTTP request
	        try {
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Android-AEApp,ID=2435743");
	            httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);
	            HttpPost httpPost = new HttpPost(url);
	            httpPost.setEntity(new UrlEncodedFormEntity(params));
	            httpPost.setHeader("User-Agent","Android-AEApp,ID=2435743");
	            httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	            httpPost.setHeader("Cookie", "PHPSESSID=lc89a2uu0rj6t2p219gc2cq4i2; path=/");
	            HttpResponse httpResponse = httpClient.execute(httpPost);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            Log.i("connect info",httpEntity.toString());
	            is = httpEntity.getContent();
	            Log.i("connect info",is.toString());
	        	
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        //Log.d("connect info",is.toString());
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
	           
	        } 
	        catch (Exception e) {
	            Log.e("Buffer Error", "Error converting result " + e.toString());
	        }
	 
	        // try parse the string to a JSON object
	        try {
	            jObj = new JSONObject(json);
	        } catch (JSONException e) {
	            Log.e("JSON Parser", "Error parsing data " + e.toString());
	        }
	 
	        // return JSON String
	        return jObj;
	    }
}
