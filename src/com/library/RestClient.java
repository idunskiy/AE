package com.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
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
import org.apache.http.conn.ConnectTimeoutException;
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
import android.util.Base64;
import android.util.Log;

import com.assignmentexpert.LoginActivity;
import com.library.singletones.CookieStorage;
/**  *класс, реализующий REST архитектуру*/
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
    private static boolean restError = false;
    
    public static String restClientErrorMess;
    
    private String response;
    HttpUriRequest request;
    public String getResponse() {
        return response;
    }
    /**  	метод, возвращающий строку ошибки*/
    public String getErrorMessage() {
        return message;
    }
    /**  *	метод, возвращающий код ответа сервера*/
    public int getResponseCode() {
        return responseCode;
    }
    /**  	метод, устанавливающий обьект Context для использования обьекта Rest */
    public void setContextRest(Context ctx)
    {
    	this.context = ctx;
    }
    /**  	метод, возвращающий обьект Context для использования обьекта Rest*/
    public Context getContextRest()
    {
    	return  this.context;
    }
    /**  	конструктор класса*/
    public RestClient(String url)
    {
        this.url = url;
        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();
    }
    /**  	конструктор класса*/
    public RestClient(Context context)
    {
    	this.context = context;
    }
    /**  	метод, добавления параметра обьекта BasicNameValuePair*/
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
    /**  	метод, выполняющий подключение по передаваемому методу
     * @param method - один из CRUD методов
     * @return обьект InputStream*/
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
                break;
            }
            case PUT:
            {
                request = new HttpPut(url);
                Log.i("put request url", url);
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
//                    request.addHeader("Content-Transfer-Encoding", "8bit");
//                    request.addHeader("Content-Type", "text/plain; charset=US-ASCII");
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
        	Log.i("restClient exc", "ClientProtocolException");
        	RestError(true);
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } 
        catch (ConnectTimeoutException e)  {
        	Log.i("restClient exc", "ConnectTimeoutException");
        	RestError(true);
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } 
        catch (UnknownHostException e)  {
        	Log.i("restClient exc", "UnknownHostException");
        	RestError(true);
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } 
        
        catch (IOException e) {
        	Log.i("restClient exc", "IOException");
        	RestError(true);
        	if (e instanceof HttpHostConnectException)
            client.getConnectionManager().shutdown();
            
            e.printStackTrace();
        }
        
        return instream;
    }
    /** 	метод, создающий обьект DefaultHttpClient
     * @return обьект DefaultHttpClient*/
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

    /**  	метод, конвертации InputStream в обьект JSON
     * @param is - обьект InputStream
     * @return обьект JSONObject*/
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
    
    public static boolean RestError(boolean res)
    {
    	restError = res;
    	
    	return restError;
    }
    
    
}
