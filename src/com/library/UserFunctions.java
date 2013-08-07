package com.library;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;


/**  * класс связующих функций клиент - серверного взаимодействия. В функциях происходит передача необходимых параметров по соответсвующим url'ам и получение JSON'a в качестве ответа.  */
public class UserFunctions {
	private JSONParser jsonParser;
	
//	private static String host =  StaticFields.testHost;
	/**  * Адрес основного host'a, к которому будут подставляться необходимые функции */
	private static String host =  Constants.finalHost;
	
//	private static String host =  StaticFields.IPHost;
	/**  * url для логина */
	private static String loginURL = host+"/app_dev.php/api/login_check";
	/**  * url для получения списка заказов */
    private static String ordersURL = host+"/app_dev.php/api/client/orderList/";
    /**  * url для регистрации */
    private static String registerURL = host+"/app_dev.php/api/client/register/";
    /**  * url для получения каптчи */
    private static String captchaURL = host+"/app_dev.php/captcha/regenerate/";
    /**  * url для восстановления пароля */
    private static String restoreURL = host+"/app_dev.php/api/client/request/resetting/";
    //private static String sendOrderURL = host+"/app_dev.php/api/client/createOrder/";
    /**  * url для отправки заказа */
    private static String sendOrderURL = host+"/app_dev.php/api/client/order/";
    /**  * url для отправки сообщения, касающегося текущего заказа */
    private static String sendMessageURL = host+"/app_dev.php/api/client/message/";
    /**  * url для деактивации заказа */
    private static String deleteOrderURL = host+"/app_dev.php/api/client/order/";
    /**  * url для обновления профиля пользователя */
    private static String updateProfileURL = host+"/app_dev.php/api/client/user/edit";
    /**  * url для логаута текущего пользователя */
    private static String logOutURL = host+"/app_dev.php/api/logout";
    /**  * url для обновления полей заказа после его оплаты */
    private static String sendPaymentURL = host+"/app_dev.php/payment/notifier/";
    
    int flag =0;

    /**  * конструктор. инициализация обьекта JSONParser*/
    public UserFunctions(){
        
		jsonParser = new JSONParser();
        
    }
 
    
    /**  * метод логина пользователя
     * @param email - email пользователя
     * @param password - пароль пользователя
     * @return обьект JSON, после парсинга ответа от сервера*/
    public JSONObject loginUser(String email, String password) throws Exception{
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params, RequestMethod.POST);
        return json;
    }
    /**  * метод получения списка заказов
     * @param page - номер страницы заказов
     * @param perpage - количество заказов на каждой из страниц
     * @return обьект JSON, после парсинга ответа от сервера*/
	public JSONObject getOrders(String page, String perpage) throws Exception{
        // Building Parameters
    	
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String stStart = String.valueOf(page);
    	String stEnd = String.valueOf(perpage);
    	Log.i("getOrders start",stStart);
    	Log.i("getOrders end",stEnd);
        params.add(new BasicNameValuePair("page", stStart));
        params.add(new BasicNameValuePair("perpage", stEnd));
        JSONObject json = jsonParser.getJSONFromUrl(ordersURL,params,RequestMethod.GET);
      //  Log.i("orders",json.toString());
        return json;
    }
	/**  * метод логаута текущего пользователя
     * @return обьект JSON, после парсинга ответа от сервера*/
    public JSONObject logOut() throws Exception{
        // Building Parameters
    	
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        JSONObject json = jsonParser.getJSONFromUrl(logOutURL,params,RequestMethod.GET);
      //  Log.i("orders",json.toString());
        return json;
        
    }
 
    /**  * метод регистрации пользователя
     * @param name - имя нового пользователя
     * @param email - email нового пользователя
     * @param password - пароль нового пользователя
     * @param confpass - подтверждение пароля нового пользователя
     * @param captcha - строковое представление передаваемой каптчи
     * @return обьект JSON, после парсинга ответа от сервера*/
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
  
    /**  * метод получения каптчи от сервера
     * @return обьект Bitmap, после парсинга ответа от сервера*/
    public Bitmap getCaptcha() throws Exception
    { 
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
     	String obj  = jsonParser.getStringFromUrl(captchaURL, params);
        Log.i("captcha url", captchaURL);
        String b= obj.toString();
        Log.i("captcha", obj);
     	b = b.substring(23, b.length()-1);
    	byte [] encodeByte=Base64.decode(b,Base64.NO_PADDING);
        Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }
    /**  * метод восстановления пароля
     * @param newPass - новый пароль
     * @param captcha - строковое представление передаваемой каптчи
     * @return обьект JSON, после парсинга ответа от сервера*/
    public JSONObject restorePassword(String newPass, String captcha) throws Exception
    { 
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", newPass));
        Log.i("restore email", newPass);
        JSONObject json = jsonParser.getJSONFromUrl(restoreURL, params,RequestMethod.GET);
        return json;
        
    }
    
    /**  * метод отправки нового заказа (тип Assignment)
     * @param title - название заказа
     * @param category - категория заказа
     * @param level - степень выполнения заказа
     * @param deadline - срок выполнения заказа
     * @param info - доп. информация заказа
     * @param explanation - флаг наличия детального обьяснения
     * @param specInfo - доп. информация заказа
     * @param timezone - временная зона
     * @param files - коллекция файлов
     * @param exclusive_video - флаг наличия exclusive_video
     * @param common_video - флаг наличия common_video
     * @return обьект JSON, после парсинга ответа от сервера*/
    public JSONObject sendAssignment(String title, String category, String level, String deadline, String info, String explanation,
    		String specInfo, String timezone, List<File> files,  
    		String exclusive_video,String common_video ) throws Exception
    { 
    	RestClient restClient = new RestClient(sendOrderURL);
    	restClient.AddEntity("product[product_profile][title]", new StringBody(title,Charset.forName("UTF-8")));
    	restClient.AddEntity("category", new StringBody(category));
    	restClient.AddEntity("level", new StringBody(level));
    	restClient.AddEntity("deadline", new StringBody(deadline));
    	restClient.AddEntity("product[product_profile][info]", new StringBody(info,Charset.forName("UTF-8")));
    	restClient.AddEntity("product[product_profile][dtl_expl]",new StringBody(explanation));
    	restClient.AddEntity("product[product_profile][special_info]", new StringBody(specInfo,Charset.forName("UTF-8")));
    	restClient.AddEntity("timezone", new StringBody(timezone));
    	restClient.AddEntity("product[product_type]", new StringBody("assignment"));
    	restClient.AddEntity("product[product_profile][shoot_exclusive_video]", new StringBody(exclusive_video));
    	restClient.AddEntity("product[product_profile][shoot_common_video]", new StringBody(common_video));
    	for (File file : files)
    	{
    		Log.i("files to upload", file.toString());
    		ContentBody fbody = new FileBody(( File )file, "application/octet-stream", "UTF-8");
    		restClient.AddEntity("files[]", fbody);
    		
    	}
    	 InputStream is = restClient.Execute(RequestMethod.POST_UPLOAD);
    	JSONParser parser = new JSONParser();
    	return parser.getJSONfromInputStream(is);
    	
    }
    /**  * метод отправки нового заказа (тип Essay)
     * @param title - название заказа
     * @param subject - тема заказа
     * @param level - степень выполнения заказа
     * @param deadline - срок выполнения заказа
     * @param info - доп. информация заказа
     * @param explanation - флаг наличия детального обьяснения
     * @param specInfo - доп. информация заказа
     * @param timezone - временная зона
     * @param files - коллекция файлов
     * @param pages_number - количество страниц
     * @param number_of_references -  количество ссылаемого материала
     * @param essay_type -  тип essay
     * @param essay_creation_style -  пребуемый тип создания essay
     * @return обьект JSON, после парсинга ответа от сервера*/
    public JSONObject sendWriting(String title, String subject, String level, String deadline, String info, String explanation,
    		String specInfo, String timezone, List<File> files, 
    		String pages_number,String number_of_references ,String essay_type, String essay_creation_style) throws Exception
    { 
    	RestClient restClient = new RestClient(sendOrderURL);
    	restClient.AddEntity("product[product_profile][title]", new StringBody(title,Charset.forName("UTF-8")));
    	restClient.AddEntity("subject", new StringBody(subject));
    	restClient.AddEntity("level", new StringBody(level));
    	restClient.AddEntity("deadline", new StringBody(deadline));
    	restClient.AddEntity("timezone", new StringBody(timezone));
    	restClient.AddEntity("product[product_type]", new StringBody("writing"));
    	restClient.AddEntity("product[product_profile][info]", new StringBody(info,Charset.forName("UTF-8")));
    	restClient.AddEntity("product[product_profile][dtl_expl]",new StringBody(explanation));
    	restClient.AddEntity("product[product_profile][special_info]", new StringBody(specInfo,Charset.forName("UTF-8")));
    	restClient.AddEntity("product[product_profile][pages_number]", new StringBody(pages_number));
    	restClient.AddEntity("product[product_profile][number_of_references]", new StringBody(number_of_references));
    	restClient.AddEntity("product[product_profile][essay_type]", new StringBody(essay_type));
    	restClient.AddEntity("product[product_profile][essay_creation_style]", new StringBody(essay_creation_style));
    	for (File file : files)
    	{
    		Log.i("files to upload", file.toString());
    		ContentBody fbody = new FileBody(( File )file, "application/octet-stream", "UTF-8");
    		restClient.AddEntity("files[]", fbody);
    		
    	}
    	 InputStream is = restClient.Execute(RequestMethod.POST_UPLOAD);
    	JSONParser parser = new JSONParser();
    	return parser.getJSONfromInputStream(is);
    	
    }
    /**  * метод отправки сообщения заказа
     * @param category - категория сообщения заказа
     * @param deadline - срок выполнения заказа
     * @param price - цена заказа
     * @param body - тело сообщения
     * @param order - id заказа
     * @param files - коллекция файлов
     * @return обьект JSON, после парсинга ответа от сервера*/
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
    		Log.i("file body", fbody.getFilename().toString());
    		restClient.AddEntity("files[]", fbody);
    		
    	}
    	
    	 InputStream is = restClient.Execute(RequestMethod.POST_UPLOAD);
    	 JSONParser parser = new JSONParser();
     	 return parser.getJSONfromInputStream(is);
        
    }
    /**  * метод деактивации заказа
     * @param orderId - id заказа
     * @return обьект JSON, после парсинга ответа от сервера*/
    public JSONObject deleteOrder(String orderId) throws Exception
    { 
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
       // params.add(new BasicNameValuePair("id", orderId));
     //   params.add(new BasicNameValuePair("captcha", captcha));
    	Log.i("delete uri", deleteOrderURL+orderId+"/");
        JSONObject json = jsonParser.getJSONFromUrl(deleteOrderURL+orderId+"/", params,RequestMethod.DELETE);
        return json;
        
    }
    /**  * метод обновления профиля пользователя
     * @param orderId - id заказа
     * @return обьект JSON, после парсинга ответа от сервера*/
    public JSONObject profileUpdate(String timezone, String firstname, 
    		String lastname, String phone, String password) throws Exception
    { 
    	 RestClient restClient = new RestClient(updateProfileURL);
    	 Log.i("profile rest", firstname);
    	 Log.i("profile rest", phone);
    	 restClient.AddEntity("user[timezone]", new StringBody(timezone));
    	 restClient.AddEntity("user[first_name]", new StringBody(firstname,Charset.forName("UTF-8")));
    	 restClient.AddEntity("user[last_name]", new StringBody(lastname,Charset.forName("UTF-8")));
    	 restClient.AddEntity("phone", new StringBody(phone));
    	 restClient.AddEntity("user[password_plain]", new StringBody(password,Charset.forName("UTF-8")));
    	 InputStream is = restClient.Execute(RequestMethod.POST_UPLOAD);
    	 JSONParser parser = new JSONParser();
     	 return parser.getJSONfromInputStream(is);
        
    }
    /**  * метод оплаты заказа
     * @param item_number - id заказа
     * @param price - цена заказа
     * @return обьект JSON, после парсинга ответа от сервера*/
    public JSONObject sendPayment(String item_number, String price) throws Exception
    {

    	RestClient restClient = new RestClient(sendPaymentURL);
    	restClient.AddEntity("test_ipn", new StringBody("1"));
    	restClient.AddEntity("payment_type", new StringBody("echeck"));
    	restClient.AddEntity("payment_date", new StringBody("06:36:34 Nov 28, 2012 PST"));
    	restClient.AddEntity("payment_status", new StringBody("Completed"));
    	restClient.AddEntity("address_status", new StringBody("confirmed"));
    	restClient.AddEntity("payer_status", new StringBody("verified"));
    	restClient.AddEntity("first_name", new StringBody("John"));
    	restClient.AddEntity("last_name", new StringBody("Smith"));
    	restClient.AddEntity("payer_email", new StringBody("buyer@paypalsandbox.com"));
    	restClient.AddEntity("payer_id", new StringBody("TESTBUYERID01"));
    	restClient.AddEntity("address_name", new StringBody("John Smith"));
    	restClient.AddEntity("address_country", new StringBody("United States"));
    	restClient.AddEntity("address_country_code", new StringBody("US"));
    	restClient.AddEntity("address_zip", new StringBody("95131"));
    	restClient.AddEntity("address_state", new StringBody("CA"));
    	restClient.AddEntity("address_city", new StringBody("San Jose"));
    	restClient.AddEntity("address_street", new StringBody("123, any street"));
    	restClient.AddEntity("business", new StringBody("seller@paypalsandbox.com"));
    	restClient.AddEntity("receiver_email", new StringBody("seller@paypalsandbox.com"));
    	restClient.AddEntity("receiver_id", new StringBody("TESTSELLERID1"));
    	restClient.AddEntity("residence_country", new StringBody("US"));
    	restClient.AddEntity("item_name", new StringBody("something"));
    	restClient.AddEntity("item_number", new StringBody(item_number));
    	restClient.AddEntity("quantity", new StringBody("1"));
    	restClient.AddEntity("shipping", new StringBody("720.00"));
    	restClient.AddEntity("tax", new StringBody("2.02"));
    	restClient.AddEntity("mc_currency", new StringBody("USD"));
    	restClient.AddEntity("mc_fee", new StringBody("0.44"));
    	restClient.AddEntity("mc_gross", new StringBody(price));
    	restClient.AddEntity("txn_type", new StringBody("web_accept"));
    	restClient.AddEntity("txn_id", new StringBody("3411281436"));
    	restClient.AddEntity("notify_version", new StringBody("2.1"));
    	restClient.AddEntity("custom", new StringBody("xyz123"));
    	restClient.AddEntity("invoice", new StringBody("abc1234"));
    	restClient.AddEntity("charset", new StringBody("windows-1252"));
    	restClient.AddEntity("verify_sign", new StringBody("3411281436"));
    	restClient.AddEntity("txn_id", new StringBody("AFcWxV21C7fd0v3bYYYRCpSSRl31AFTE1Dsxm17sTf3EppTHBN-FALLS"));
    	InputStream is = restClient.Execute(RequestMethod.POST_UPLOAD);
    	 JSONParser parser = new JSONParser();
     	 return parser.getJSONfromInputStream(is);
    			
    }
 
    
//    /**
//     * Function get Login status
//     * */
//    public boolean isUserLoggedIn(Context context)
//    {
//        DatabaseHandler db = new DatabaseHandler(context);
//        int count = db.getRowCount();
//        if(count > 0){
//            // user logged in
//            return true;
//        }
//        return false;
//    }
// 
    

}
