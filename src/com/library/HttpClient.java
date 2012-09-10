package com.library;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.CoreProtocolPNames;

import android.util.Log;

public class HttpClient {
	static InputStream is = null;
	private static final DefaultHttpClient httpClient = new DefaultHttpClient() ;
	public static DefaultHttpClient getInstance() { return httpClient; }
	 
	 public static InputStream getResponse(String url, List<NameValuePair> params)
	 {
		try {
    	CookieStore cookieStore = new BasicCookieStore();
        DefaultHttpClient httpClient = JSONParser.getInstance();        
        httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Android-AEApp,ID=2435743");
        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);
        BasicClientCookie cookie = new BasicClientCookie("PHPSESSID", "1");  
        httpClient.setCookieStore(cookieStore);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        httpPost.setHeader("User-Agent","Android-AEApp,ID=2435743");
        httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        System.out.println(CookieStorage.getInstance().toString());
        if (CookieStorage.getInstance().getArrayList().isEmpty())
        	CookieStorage.getInstance().getArrayList().add("PHPSESSID=lc89a2uu0rj6t2p219gc2cq4i2");
        httpPost.setHeader("Cookie", CookieStorage.getInstance().getArrayList().get(0).toString());
        HttpResponse httpResponse = httpClient.execute(httpPost);
        Header[] head = httpResponse.getAllHeaders();
        System.out.println(cookie);

        if (httpResponse.getLastHeader("Set-Cookie")!=null)
        {
        	CookieStorage.getInstance().getArrayList().remove(0);
        	CookieStorage.getInstance().getArrayList().add(httpResponse.getLastHeader("Set-Cookie").getValue());
        }
      	
       
        HttpEntity httpEntity = httpResponse.getEntity();
        is = httpEntity.getContent();
        //Log.i("response",toString());
	    	
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		 
		 return is;
	 }
    

}
