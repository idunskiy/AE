package com.library;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.datamodel.Captcha;
import com.datamodel.Order;
import com.library.singletones.SharedPrefs;


/**  * class for client-server maintenance.   */
public class UserFunctions {
	private JSONParser jsonParser;
//	private static String host =  StaticFields.testHost;
	/**  * url for host */
//	private static String host =  Constants.finalHost;
	private static String host =  Constants.prodHost;
	
//	private static String host =  StaticFields.IPHost;
	/**  * url for login */
	private static String loginURL = host+"/login";
	
	private static String guiURL = host+"/gui";
	/**  * url for order list */
    public static String ordersURL = host+"/orders/";
    /**  * url for registration*/
    private static String registerURL = host+"/users";
    /**  * url for captcha getting */
    private static String captchaURL = host+"/captcha/";
    /**  * url for pass restoring */
    private static String restoreURL = host+"/restore_password";
    //private static String sendOrderURL = host+"/app_dev.php/api/client/createOrder/";
    /**  * url profile updating */
    private static String updateProfileURL = host+"/users/update";
    
    int flag =0;

    
    Captcha captcha;
	private FrequentlyUsedMethods faq;
    public void setCaptcha(Captcha captcha) {
		this.captcha = captcha;
	}
    public Captcha getCaptcha( ) {
		return this.captcha;
	}



	public UserFunctions(){
        
		jsonParser = new JSONParser();
        
    }
 
    
    /**  * user login
    */
    public JSONObject loginUser(String email, String password) throws Exception{
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
    	 JSONParser parser = new JSONParser();
        JSONObject json = parser.getJSONFromUrl(loginURL, params, RequestMethod.POST);
    	return json;
    }
    
    
    public JSONObject getGuiFields() throws Exception{
        // Building Parameters
    	JSONObject json;
    	 List<NameValuePair> params = new ArrayList<NameValuePair>();
    	 JSONParser parser = new JSONParser();
    	json = parser.getJSONFromUrl(guiURL, params, RequestMethod.GET);
        return json;
    }
    /**  *order list getting
     */
	public JSONObject getOrders(String page, String perpage) throws Exception{
        // Building Parameters
    	
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String stStart = String.valueOf(page);
    	String stEnd = String.valueOf(perpage);
    	Log.i("getOrders start",stStart);
    	Log.i("getOrders end",stEnd);
        params.add(new BasicNameValuePair("page", stStart));
        params.add(new BasicNameValuePair("per_page", stEnd));
        JSONObject json = jsonParser.getJSONFromUrl(ordersURL,params,RequestMethod.GET);
      //  Log.i("orders",json.toString());
        return json;
    }
	/**  * user logout
   */
    public JSONObject logOut() throws Exception{
        // Building Parameters
    	
        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        JSONObject json = jsonParser.getJSONFromUrl(logOutURL,params,RequestMethod.GET);
      //  Log.i("orders",json.toString());
//        return json;
        return null;
        
    }
 
    /**  * user registration
    */
    public JSONObject registerUser(String name, String email, String password, Captcha capthca, String phone) throws Exception
    {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("captcha_id", capthca.getId()));
       
        params.add(new BasicNameValuePair("captcha_word", capthca.getWord()));
        params.add(new BasicNameValuePair("phone", phone));
        Log.i("send captcha register", capthca.toString());
 
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params,RequestMethod.POST);
        return json;
    }
    
    
       public JSONObject sendCaptcha(String id, String value) throws Exception
    {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("id", name));
//        params.add(new BasicNameValuePair("reg[email]", email));
//        params.add(new BasicNameValuePair("reg[password]", password));
//        params.add(new BasicNameValuePair("reg[passwordConfirmed]", confpass));
//        params.add(new BasicNameValuePair("reg[captcha]", captcha));
//        params.add(new BasicNameValuePair("reg[agree]", "on"));
 
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params,RequestMethod.POST);
        return json;
    }
  
    /**  * метод получения каптчи от сервера
     * @return обьект Bitmap, после парсинга ответа от сервера*/
    public void  getCaptchaFromServer() throws Exception
    { 
    	
     	Captcha captcha  = new Captcha();
     			List<NameValuePair> params = new ArrayList<NameValuePair>();
     			JSONObject obj  = jsonParser.getJSONFromUrl(captchaURL, params, RequestMethod.GET);
         	DataParsing dataParse = new DataParsing();
     			captcha = dataParse.wrapCapthca(obj);
     		
     	
//     	 URL url = new URL(captcha.getImage());
//         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//         connection.setDoInput(true);
//         connection.connect();
//         InputStream input = connection.getInputStream();
//         Bitmap bitmap = BitmapFactory.decodeStream(input);
     	setCaptcha(captcha);
    }
    
    public Bitmap downloadCaptcha(Captcha captcha)
    {
    	Bitmap bitmap = null;
    	
	try {
		 URL url = new URL(captcha.getImage());
	       HttpURLConnection connection;
		connection = (HttpURLConnection) url.openConnection();
	
       connection.setDoInput(true);
       connection.connect();
       InputStream input = connection.getInputStream();
        bitmap = BitmapFactory.decodeStream(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
    }
    
    /**  * password restore*/
    public JSONObject restorePassword(String newPass) throws Exception
    { 
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", newPass));
        Log.i("restore email", newPass);
        JSONObject json = jsonParser.getJSONFromUrl(restoreURL, params,RequestMethod.POST);
        return json;
        
    }
    
    /**  * new order (assignment) posting*/
    public JSONObject sendAssignment(String title, String category, String level, String deadline, String info, String explanation,
    		String specInfo, String timezone, List<File> files,  
    		String video ) throws Exception
    { 
    	RestClient restClient = new RestClient(ordersURL);
    	Log.i("video to upload", video);
    	 
    	restClient.AddEntity("order_title", new StringBody(title,Charset.forName("UTF-8")));
    	restClient.AddEntity("cat_id", new StringBody(category));
    	restClient.AddEntity("level_id", new StringBody(level));
    	restClient.AddEntity("deadline", new StringBody(deadline));
    	restClient.AddEntity("task_info", new StringBody(info,Charset.forName("UTF-8")));
    	restClient.AddEntity("chb_detailed",new StringBody(explanation));
    	restClient.AddEntity("gnt", new StringBody(timezone));
    	restClient.AddEntity("type", new StringBody("assignment"));
    	restClient.AddEntity("video_flag", new StringBody(video));
    	int counter = 0;
    	for (File file : files)
    	{
    		++counter;
    		ContentBody fbody = new FileBody(( File )file, "application/octet-stream", "UTF-8");
    		restClient.AddEntity("files" + Integer.toString(counter), fbody);
    		
    	}
    	 InputStream is = restClient.Execute(RequestMethod.POST_UPLOAD);
    	JSONParser parser = new JSONParser();
    	return parser.getJSONfromInputStream(is);
    	
    }
    /**  * new order (essay) posting
     * */
    public JSONObject sendWriting(String title, String subject, String level, String deadline, String info, String explanation,
    		String specInfo, String timezone, List<File> files, 
    		String pages_number,String number_of_references ,String essay_type, String essay_creation_style) throws Exception
    { 
    	RestClient restClient = new RestClient(ordersURL);
    	restClient.AddEntity("order_title", new StringBody(title,Charset.forName("UTF-8")));
    	restClient.AddEntity("cat_id", new StringBody(subject));
    	restClient.AddEntity("level_id", new StringBody(level));
    	restClient.AddEntity("deadline", new StringBody(deadline));
    	restClient.AddEntity("gnt", new StringBody(timezone));
    	restClient.AddEntity("type", new StringBody("essay"));
    	restClient.AddEntity("task_info", new StringBody(info,Charset.forName("UTF-8")));
    	restClient.AddEntity("pages_number", new StringBody(pages_number));
    	restClient.AddEntity("pages_of_references", new StringBody(number_of_references));
    	restClient.AddEntity("essay_type_id", new StringBody(essay_type));
    	restClient.AddEntity("essay_creation_style_id", new StringBody(essay_creation_style));
    	int counter = 0;
    	for (File file : files)
    	{	++counter;
    		Log.i("files to upload", file.toString());
    		ContentBody fbody = new FileBody(( File )file, "application/octet-stream", "UTF-8");
    		restClient.AddEntity("files" + Integer.toString(counter), fbody);
    		
    	}
    	 InputStream is = restClient.Execute(RequestMethod.POST_UPLOAD);
    	JSONParser parser = new JSONParser();
    	return parser.getJSONfromInputStream(is);
    	
    }
    /**  * new message posting*/
    public JSONObject sendMessage(String category,  String deadline, String price, String body,
    		String order, List<File> files) throws Exception
    { 
    	
        RestClient restClient = new RestClient(ordersURL+"/" + order +"/messages");
    	restClient.AddEntity("message", new StringBody(body, Charset.forName("UTF-8")));
    	int counter = 0;
    	for (File file : files)
    	{
    		++counter;
    		Log.i("files to upload", file.toString());
    		ContentBody fbody = new FileBody(( File )file, "application/octet-stream","UTF-8");
    		restClient.AddEntity("files" + Integer.toString(counter), fbody);
    		
    	}
    	
    	 InputStream is = restClient.Execute(RequestMethod.POST_UPLOAD);
    	 JSONParser parser = new JSONParser();
     	 return parser.getJSONfromInputStream(is);
        
    }
    /**  * order deactivation*/
    public JSONObject deleteOrder(String orderId) throws Exception
    { 
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
       // params.add(new BasicNameValuePair("id", orderId));
     //   params.add(new BasicNameValuePair("captcha", captcha));
    	Log.i("delete uri", ordersURL+orderId+"/");
        JSONObject json = jsonParser.getJSONFromUrl(ordersURL+orderId+"/", params,RequestMethod.DELETE);
        return json;
        
    }
    /**  * user profile updating*/
    public JSONObject profileUpdate(String timezone, String firstname, 
    		String lastname, String phone, String password) throws Exception
    { 
    	 RestClient restClient = new RestClient(updateProfileURL);
    	 restClient.AddEntity("name", new StringBody(firstname,Charset.forName("UTF-8")));
    	 restClient.AddEntity("phone", new StringBody(phone));
    	 
    	 restClient.AddEntity("email", new StringBody( SharedPrefs.getInstance().getSharedPrefs().getString(Constants.ENTERED_EMAIL, ""),Charset.forName("UTF-8")));
    	 restClient.AddEntity("old_password", new StringBody( SharedPrefs.getInstance().getSharedPrefs().getString(Constants.ENTERED_PASS, ""),Charset.forName("UTF-8")));
    	 
    	 restClient.AddEntity("new_password", new StringBody(password,Charset.forName("UTF-8")));
    	 restClient.AddEntity("confirm_password", new StringBody(password,Charset.forName("UTF-8")));
    	 
    	 
    	 InputStream is = restClient.Execute(RequestMethod.POST_UPLOAD);
    	 JSONParser parser = new JSONParser();
     	 return parser.getJSONfromInputStream(is);
        
    }
 
    
    public Order getOrder(int id)
    {
    	
    	Order order = null;
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	DataParsing dataParse = new DataParsing();
        try {
			JSONObject json = jsonParser.getJSONFromUrl(ordersURL + Integer.toString(id),params,RequestMethod.GET);
			order = dataParse.wrapOrder(json);
			
			Log.i("got new order from server", order.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return order;
    }
    

}
