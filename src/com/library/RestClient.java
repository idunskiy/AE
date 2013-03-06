package com.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.assignmentexpert.LoginActivity;

public class RestClient {
	private ArrayList <NameValuePair> params;
    private ArrayList <NameValuePair> headers;
    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null,Charset.forName("UTF-8"));
//    MultipartEntity entity = new MultipartEntity();
    static InputStream instream = null;
    private Context context;
    private String url;
    int count= 0;
    private int responseCode;
    private String message;
    public static boolean inetError = false;
    
    public static String restClientErrorMess;
    
    private String response;
    HttpUriRequest request;
    public String getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }
    public void setContextRest(Context ctx)
    {
    	this.context = ctx;
    }
    public Context getContextRest()
    {
    	return  this.context;
    }
    public RestClient(String url)
    {
        this.url = url;
        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();
    }
    public RestClient(Context context)
    {
    	this.context = context;
    }

    public void AddParam(String name, String value)
    {
        params.add(new BasicNameValuePair(name, value));
    }

    public void AddHeader(String name, String value)
    {
        headers.add(new BasicNameValuePair(name, value));
    }
    public void AddEntity(String name, ContentBody data)
    {
        entity.addPart(name, data);
    }

    public InputStream Execute(RequestMethod method) throws Exception
    {   
    	HttpResponse httpResponse;
    	DefaultHttpClient client = getNewHttpClient();
    	InputStream instream = null;
    	RestClient.this.AddHeader(CoreProtocolPNames.USER_AGENT, "Android-AEApp,ID=2435743");
    	RestClient.this.AddHeader(ClientPNames.COOKIE_POLICY,  CookiePolicy.RFC_2109);
    	RestClient.this.AddHeader("User-Agent",  "Android-AEApp,ID=IoaNN");
    	RestClient.this.AddHeader("Accept",  "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
    	RestClient.this.AddHeader("Authorization",  "Basic " + Base64.encodeToString("1a:1a".getBytes(), Base64.NO_WRAP));
    	RestClient.this.AddHeader("Content-Transfer-Encoding",  "8bit");
    	if (CookieStorage.getInstance().getArrayList().isEmpty())
         	CookieStorage.getInstance().getArrayList().add("PHPSESSID=lc89a2uu0rj6t2p219gc2cq4i2");
    	RestClient.this.AddHeader("Cookie",  CookieStorage.getInstance().getArrayList().get(0).toString()); 
        switch(method) {
            case GET:
            {
                //add parameters
                String combinedParams = "";
                if(!params.isEmpty()){
                    combinedParams += "?";
                    for(NameValuePair p : params)
                    {
                        String paramString = null;
						try {
							paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        if(combinedParams.length() > 1)
                        {
                            combinedParams  +=  "&" + paramString;
                        }
                        else
                        {
                            combinedParams += paramString;
                        }
                    }
                }

                 request = new HttpGet(url + combinedParams);

                //add headers
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }

                break;
            }

            case POST:
            {
                request = new HttpPost(url);
                
                //add headers
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }

                if(!params.isEmpty()){
                    try {
						((HttpEntityEnclosingRequestBase) request).setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
//                if(entity.getContentLength()!=0)
//                {
//                	((HttpPost)request).setEntity(entity);
//                	
//                }

                //executeRequest(request, url);
                break;
            }
            case POST_UPLOAD:
            {
                request = new HttpPost(url);
              
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }
              
				Log.i("POST_UPLOAD",entity.toString());
               
                if(entity.getContentLength()!=0)
                {
                	Log.i("rest client entity check",
                				"is not null and is going to launch post method");
                	((HttpPost)request).setEntity(entity);
                	
                }

                //executeRequest(request, url);
                break;
            }
            case PUT:
            {
                request = new HttpPut(url);
                Log.i("put request url", url);
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                    request.addHeader("Content-Transfer-Encoding", "8bit");
                }
                Log.i("http put", request.getClass().toString());
                if(entity.getContentLength()!=0)
                {
                	Log.i("rest client entity check",
                				"is not null and is going to launch put method");
                	((HttpPut)request).setEntity(entity);
                	
                }
                
                
                
                break;
            }
            case DELETE:
            {
                request = new HttpDelete(url);

                //add headers
                for(NameValuePair h : headers)
                { 
                    request.addHeader(h.getName(), h.getValue());
                }
//                if(!params.isEmpty()){
//                    ((HttpPost)request).setEntity(entity);
//                }

                //executeRequest(request, url);
                break;
            }
            default:
            	request = null;
        }
        
        try {
            httpResponse = client.execute(request);
            if (httpResponse.getLastHeader("Set-Cookie")!=null)
            {
            	CookieStorage.getInstance().getArrayList().remove(0);
            	CookieStorage.getInstance().getArrayList().add(httpResponse.getLastHeader("Set-Cookie").getValue());
            }
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();
            Header[] headers = httpResponse.getAllHeaders();
            for(Header head : headers)
            {
            	Log.i("RestClient headers", head.toString());
            }
            Log.i("RestClient response status code", Integer.toString(responseCode));
            if (responseCode == 401)
            {
            	
                Intent i = new Intent(LoginActivity.getInstance(), LoginActivity.class);
                restClientErrorMess = "Some problem at server occurs. Please try later";
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("restError", "Authorization failed");
				LoginActivity.getInstance().startActivity(i);
            	
            }
            
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {

                instream = entity.getContent();
            }
            

        } catch (ClientProtocolException e)  {
        	
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } 
        
        catch (IOException e) {
        	if (e instanceof HttpHostConnectException)
            client.getConnectionManager().shutdown();
            
            e.printStackTrace();
        }
        
        return instream;
    }

    public DefaultHttpClient getNewHttpClient() {
        try {
        	KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            final HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public InputStream executeRequest(HttpUriRequest request, String url)
    {

    	
        DefaultHttpClient client = getNewHttpClient();

        HttpResponse httpResponse;
        InputStream instream = null;

        try {
            httpResponse = client.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {

                instream = entity.getContent();
               // if instream.getClass()
                response = convertStreamToString(instream);
                // Closing the input stream will trigger connection release
                instream.close();
            }
            

        } catch (ClientProtocolException e)  {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } catch (IOException e) {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        }
        return instream;
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) 
            {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    public JSONObject convertStreamToJSON(InputStream is) {
    	  BufferedReader reader = new BufferedReader(new InputStreamReader(is));
          StringBuilder sb = new StringBuilder();
          String json = null;
		try {
          String line = null;
          
          while ((line = reader.readLine()) != null) {
              sb.append(line + "n");
          }
          is.close();
          json  = sb.toString();
          Log.i("response",json);

      } 
      catch (Exception e) {
          Log.e("Buffer Error Rest Client", "Error converting result " + e.toString());
      }

      JSONObject jObj = null;
	try {
          jObj = new JSONObject(json);
      } catch (JSONException e) {
      	
          Log.e("JSON Parser", "Error parsing data " + e.toString());
      }
      
      return jObj;
  }
    
    
}
