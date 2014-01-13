package com.library;


import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.datamodel.Captcha;
import com.datamodel.Category;
import com.datamodel.Customer;
import com.datamodel.EssayCreationStyle;
import com.datamodel.EssayType;
import com.datamodel.Level;
import com.datamodel.NumberOfReferences;
import com.datamodel.NumberPages;
import com.datamodel.Order;
import com.datamodel.ProcessStatus;
import com.datamodel.ProductAssignment;
import com.datamodel.ProductWriting;
import com.datamodel.Subject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.library.singletones.SharedPrefs;
/**
 *	 класс для парсинга данных с получаемого json и преобразование их в соответствующие обьекты.
 */
public class DataParsing {
	/**	 *	 строка для парсинга категорий.	 */
	private static String KEY_CATEGORIES = "categories";
	/**	 *	 строка для парсинга данных клиента.	 */
    private static String KEY_CUSTOMER = "customer";
    /**	 *	 строка для парсинга id	 */
    private static String KEY_ID = "id";
    /**	 *	 строка для парсинга данных пользователя	 */
    private static String KEY_USER = "user";
    private static String KEY_DATA = "data";
    
    private static String KEY_ERROR = "error";
    
    private static String KEY_META = "_meta";
    
    /**	 *	 строка для парсинга заказов	 */
    private static String KEY_ORDERS= "orders";
    /**	 *	 строка для парсинга level'ов	 */
    private static String KEY_LEVELS= "levels";
    /**	 *	 строка для парсинга number pages	 */
    private static String KEY_NUMBER_PAGES ="pages_number_list";
    /**	 *	 строка для парсинга number of references	 */
    private static String KEY_NUMBER_REFERENCES ="number_of_references_list";
    /**	 *	 строка для парсинга subjects	 */
    private String KEY_SUBJECTS = "subjects";
    /**	 *	 строка для парсинга essay types	 */
    private static String KEY_ESSAY_TYPE= "essay_types";
    /**	 *	 строка для парсинга essay creation styles	 */
    private static String KEY_CREATION_STYLES= "essay_creation_styles";
    /**	 *	 строка для парсинга process statuses	 */
    private String KEY_STATUSES = "process_statuses";
    /**
     *	 метод для парсинга и преобразования json в обьекты process statuses
     *@param json - данные для парсинга
     *@return возвращает List ProcessStatus - ов
     */
    
    FrequentlyUsedMethods faq;
    
    Context context;
	public DataParsing(Context context) {
		super();
		this.context= context;
		faq=  new FrequentlyUsedMethods(context);
	}
	public List<ProcessStatus> wrapStatuses(JSONObject json) throws JSONException, SQLException
	{
		
		JSONObject data = json.getJSONObject(KEY_DATA);
     	// getting statuses
    	JSONArray statuses = new JSONArray();
    	
    	statuses = data.getJSONArray(KEY_STATUSES);
    	Gson gson = new Gson();
    	JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(statuses.toString()).getAsJsonArray();
        Type listType = new TypeToken<List<ProcessStatus>>() {}.getType();
        List<ProcessStatus> obj_statuses = new ArrayList<ProcessStatus>();
        obj_statuses = gson.fromJson(array.toString(), listType);
        
		
		return obj_statuses;
	}
	 /**
     *	 метод для парсинга и преобразования json в обьекты Level
     *@param json - данные для парсинга
     *@return возвращает List Level - ов
     */
	public List<Level> wrapLevels(JSONObject json) throws JSONException, SQLException
	{
		
		JSONObject data = json.getJSONObject(KEY_DATA);
     	// getting statuses
    	JSONArray levels = new JSONArray();
    	levels = data.getJSONArray(KEY_LEVELS);
    	Gson gson = new Gson();
    	JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(levels.toString()).getAsJsonArray();
        Type listType = new TypeToken<List<Level>>() {}.getType();
        List<Level> obj_statuses = new ArrayList<Level>();
        obj_statuses = gson.fromJson(array.toString(), listType);
        
		
		return obj_statuses;
	}	
	/**
     *	 метод для парсинга id пользователя
     *@param json - данные для парсинга
     *@return возвращает id юзера
     */
	public String wrapUserId(JSONObject json) throws JSONException
	{
		String id=null;
		JSONObject data = json.getJSONObject(KEY_DATA);
    	JSONObject customer = new JSONObject();
    	JSONObject user = new JSONObject();
    	customer = data.getJSONObject(KEY_CUSTOMER);
    	user = customer.getJSONObject(KEY_USER);
    	id = user.getString(KEY_ID);
    	return id;
	}
	 /**
     *	 метод для парсинга и преобразования json в обьекты Level
     *@param json - данные для парсинга
     *@return возвращает List Category
     */
	public List<Category> wrapCategories(JSONObject json) throws JSONException, SQLException
	{
		
		JSONObject data = json.getJSONObject(KEY_DATA);
    	JSONArray categories = new JSONArray();
    	categories = data.getJSONArray(KEY_CATEGORIES );
    	
    	Gson gson = new Gson();
    	JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(categories.toString()).getAsJsonArray();
        Type listType = new TypeToken<List<Category>>() {}.getType();
        List<Category> tasks = new ArrayList<Category>();
        tasks = gson.fromJson(array.toString(), listType);
    	return tasks;
	}
	 /**
     *	 метод для парсинга и преобразования json в обьекты Order
     *@param k - данные для парсинга
     *@return возвращает List Order
     */
	public List<Order> wrapOrders(JSONObject k) throws JSONException
	{
		if (k != null)
		{
			if (k.has(KEY_DATA))
			{
				JSONObject data= k.getJSONObject(KEY_DATA);
				if (data.getJSONObject(KEY_META)!=null)
				{
					if (data.getJSONObject(KEY_META).get("has_next").toString().equalsIgnoreCase("false"))
					{
						SharedPrefs.getInstance().getSharedPrefs().edit().putBoolean(Constants.ORDER_HAS_NEXT,true).commit();
					}
					else SharedPrefs.getInstance().getSharedPrefs().edit().putBoolean(Constants.ORDER_HAS_NEXT,false).commit();
				}
				JSONArray arr_orders= data.getJSONArray(KEY_ORDERS);
				JsonParser parser = new JsonParser();
				JsonArray array = parser.parse(arr_orders.toString()).getAsJsonArray();
				Log.i("received order before parse",array.toString());
				Gson gson = new Gson();
				Type listType = new TypeToken<List<Order>>() {}.getType();
			    List<Order> orders = new ArrayList<Order>();
			    orders = gson.fromJson(array.toString(), listType);
			    for (int i = 0; i < orders.size();i++)
			    {
			    	JsonObject jobject = array.get(i).getAsJsonObject();
			    	jobject = jobject.getAsJsonObject("product");
			    	jobject = jobject.getAsJsonObject("product_profile");
			    	//Log.i("order product", jobject.toString());
			    	if (orders.get(i).getProduct().getProductType().equalsIgnoreCase("assignment"))
			    	{
			    		com.datamodel.ProductType prod  = gson.fromJson(jobject, com.datamodel.ProductAssignment.class);
			    		orders.get(i).getProduct().setProduct(prod);
			    		orders.get(i).setTitle(((ProductAssignment)orders.get(i).getProduct().getProduct()).getAssignTitle());
			    	}
			    	else if (orders.get(i).getProduct().getProductType().equalsIgnoreCase("essay"))
			    	{
			    		com.datamodel.ProductType prod  = gson.fromJson(jobject, com.datamodel.ProductWriting.class);
			    		orders.get(i).getProduct().setProduct(prod);
			    		orders.get(i).setTitle(((ProductWriting)orders.get(i).getProduct().getProduct()).getEssayTitle());
			    	}
			    	Log.i(" parsed orders", orders.get(i).toString());
			    }
			    return orders;
			}
			else 
			{
				return null;
			}
		}
		else 
			return null;
	
	}
	
	 /**
     *	 метод для парсинга  и преобразования json в обьект Order
     *@param k - данные для парсинга
     *@return возвращает обьект Order
     */
	public Order wrapOrder(JSONObject k) throws JSONException
	{
		if (k != null)
		{
			
			//
			JSONObject data= k.getJSONObject(KEY_DATA);
			Gson gson = new Gson();
			
	    	JSONObject jobject = data.getJSONObject("product");
	    	jobject = jobject.getJSONObject("product_profile");
		    Order orderObj = gson.fromJson(data.toString(), Order.class);
		    if (orderObj.getProduct().getProductType().equalsIgnoreCase("assignment"))
	    	{
	    		com.datamodel.ProductType prod  = gson.fromJson(jobject.toString(), com.datamodel.ProductAssignment.class);
	    		orderObj.getProduct().setProduct(prod);
	    		orderObj.setTitle(((ProductAssignment)orderObj.getProduct().getProduct()).getAssignTitle());
	    	}
	    	else if (orderObj.getProduct().getProductType().equalsIgnoreCase("essay"))
	    	{
	    		com.datamodel.ProductType prod  = gson.fromJson(jobject.toString(), com.datamodel.ProductWriting.class);
	    		orderObj.getProduct().setProduct(prod);
	    		orderObj.setTitle(((ProductWriting)orderObj.getProduct().getProduct()).getEssayTitle());
	    	}
		    
		    return orderObj;    
			}
		else 
			return null;
	
	}
	public DataParsing() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
     *	 метод для парсинга и преобразования json в обьекты Subject
     *@param json - данные для парсинга
     *@return возвращает List Subject
     */

	public List<Subject> wrapSubjects(JSONObject json) throws JSONException, SQLException
	{
		
		JSONObject data = json.getJSONObject(KEY_DATA);
    	JSONArray categories = new JSONArray();
    	categories = data.getJSONArray(KEY_SUBJECTS );
    	Gson gson = new Gson();
    	JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(categories.toString()).getAsJsonArray();
        Type listType = new TypeToken<List<Subject>>() {}.getType();
        List<Subject> tasks = new ArrayList<Subject>();
        tasks = gson.fromJson(array.toString(), listType);
    	return tasks;
	}
	/**
     *	 метод для парсинга и преобразования json в обьекты EssayType
     *@param json - данные для парсинга
     *@return возвращает List EssayType
     */
	public List<EssayType> wrapEssayType(JSONObject json) throws JSONException, SQLException
	{
		
		JSONObject data = json.getJSONObject(KEY_DATA);
    	JSONArray categories = new JSONArray();
    	categories = data.getJSONArray(KEY_ESSAY_TYPE );
    	Gson gson = new Gson();
    	JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(categories.toString()).getAsJsonArray();
        Type listType = new TypeToken<List<EssayType>>() {}.getType();
        List<EssayType> tasks = new ArrayList<EssayType>();
        tasks = gson.fromJson(array.toString(), listType);
    	return tasks;
	}
	/**
     *	 метод для парсинга и преобразования json в обьекты EssayCreationStyle
     *@param json - данные для парсинга
     *@return возвращает List EssayCreationStyle
     */
	public List<EssayCreationStyle> wrapEssayCreationStyle(JSONObject json) throws JSONException, SQLException
	{
		
		JSONObject data = json.getJSONObject(KEY_DATA);
    	JSONArray categories = new JSONArray();
    	categories = data.getJSONArray(KEY_CREATION_STYLES );
    	Gson gson = new Gson();
    	JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(categories.toString()).getAsJsonArray();
        Type listType = new TypeToken<List<EssayCreationStyle>>() {}.getType();
        List<EssayCreationStyle> tasks = new ArrayList<EssayCreationStyle>();
        tasks = gson.fromJson(array.toString(), listType);
    	return tasks;
	}
	/**
     *	 метод для парсинга и преобразования json в обьекты NumberPages
     *@param json - данные для парсинга
     *@return возвращает List NumberPages
     */
	public final List<NumberPages> wrapNumberPages(JSONObject json) throws JSONException, SQLException
	{
		
		JSONObject data = json.getJSONObject(KEY_DATA);
		JSONObject categories = new JSONObject();
    	categories = data.getJSONObject(KEY_NUMBER_PAGES);
    	List<NumberPages> numberList =  new ArrayList<NumberPages>();
        Iterator<?> keys = categories.keys();
        while( keys.hasNext() ){
            String key = (String)keys.next();
            NumberPages elem = new NumberPages(key);
            	numberList.add(elem);
        }

    	return numberList;
	}
	/**
     *	 метод для парсинга и преобразования json в обьекты NumberOfReferences
     *@param json - данные для парсинга
     *@return возвращает List NumberOfReferences
     */
	public final List<NumberOfReferences> wrapNumberReferences(JSONObject json) throws JSONException, SQLException
	{
		
		JSONObject data = json.getJSONObject(KEY_DATA);
		JSONObject categories = new JSONObject();
    	categories = data.getJSONObject(KEY_NUMBER_REFERENCES);
    	List<NumberOfReferences> numberList =  new ArrayList<NumberOfReferences>();
        Iterator<?> keys = categories.keys();
        while( keys.hasNext() ){
            String key = (String)keys.next();
         NumberOfReferences elem = new NumberOfReferences(key);
         numberList.add(elem);
            
        }

    	return numberList;
	}
	/**
     *	 метод для парсинга и преобразования json в обьект Customer
     *@param json - данные для парсинга
     *@return возвращает обьект Customer
     */
	public Customer wrapCustomer (JSONObject json) throws JSONException, JsonSyntaxException 
   {
		
		JSONObject data = json.getJSONObject(KEY_DATA);
    	Gson gson = new Gson();
    	JSONObject customer = new JSONObject();
    	customer = data.getJSONObject(KEY_CUSTOMER);
    	Log.i("user parse", customer.toString());
    	Customer user =  gson.fromJson(customer.toString(),Customer.class);
    	return user;
	}
	
	public Captcha wrapCapthca(JSONObject json) throws JSONException, JsonSyntaxException 
	   {
			
			JSONObject data = json.getJSONObject(KEY_DATA);
	    	Gson gson = new Gson();
	    	new JsonParser();
			Captcha user =  gson.fromJson(data.toString(),Captcha.class);
	    	return user;
		}
	 

}
