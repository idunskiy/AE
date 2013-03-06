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

public class DataParsing {
	private static String KEY_STATUS = "status";
    private static String KEY_MESSAGE = "message";
    private static String KEY_CATEGORIES = "categories";
    private static String KEY_CUSTOMER = "customer";
    private static String KEY_ID = "id";
    private static String KEY_USER = "user";
    private static String KEY_DATA = "data";
    private static String KEY_ORDERS= "orders";
    private static String KEY_LEVELS= "levels";
    private static String KEY_NUMBER_PAGES ="pages_number_list";
    private static String KEY_NUMBER_REFERENCES ="number_of_references_list";
    private String KEY_SUBJECTS = "subjects";
    private static String KEY_ESSAY_TYPE= "essay_types";
    private static String KEY_CREATION_STYLES= "essay_creation_styles";
   
    
    private DatabaseHandler databaseHandler;
    private Context _context;
	private String KEY_STATUSES = "process_statuses";
	
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
	public String wrapUserId(JSONObject json) throws JSONException
	{
		String id=null;
		JSONObject data = json.getJSONObject(KEY_DATA);
    	JSONObject customer = new JSONObject();
    	JSONObject user = new JSONObject();
    	customer = data.getJSONObject(KEY_CUSTOMER);
    	user = customer.getJSONObject(KEY_USER);
    	id = user.getString(KEY_ID);
    	
    	 Log.i("id", id);
		Log.i(" user json while login",customer.toString());
    	return id;
	}
	
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
	
	public List<Order> wrapOrders(JSONObject k) throws JSONException
	{
		if (k != null)
		{
			JSONArray data= k.getJSONArray(KEY_ORDERS);
			JsonParser parser = new JsonParser();
			JsonArray array = parser.parse(data.toString()).getAsJsonArray();
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
		    	else if (orders.get(i).getProduct().getProductType().equalsIgnoreCase("writing"))
		    	{
		    		com.datamodel.ProductType prod  = gson.fromJson(jobject, com.datamodel.ProductWriting.class);
		    		orders.get(i).getProduct().setProduct(prod);
		    		orders.get(i).setTitle(((ProductWriting)orders.get(i).getProduct().getProduct()).getEssayTitle());
		    	}
		    }
		    return orders;
		}
		else 
			return null;
	
	}
	public Order wrapOrder(JSONObject k) throws JSONException
	{
		if (k != null)
		{
			JSONObject  jobject = k.getJSONObject("product");
			jobject = jobject.getJSONObject("product_profile");
			Gson gson = new Gson();
		    Order orderObj= gson.fromJson(k.toString(), Order.class);
		  
		    	if (orderObj.getProduct().getProductType().equalsIgnoreCase("assignment"))
		    	{
		    		com.datamodel.ProductType prod  = gson.fromJson(jobject.toString(), com.datamodel.ProductAssignment.class);
		    		orderObj.getProduct().setProduct(prod);
		    		orderObj.setTitle(((ProductAssignment)orderObj.getProduct().getProduct()).getAssignTitle());
		    	}
		    	else if (orderObj.getProduct().getProductType().equalsIgnoreCase("writing"))
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
            Log.i("elements NumberPages", elem.toString());
            	numberList.add(elem);
        }

    	return numberList;
	}
	
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
         Log.i("elements NumberOfReferences", elem.toString());
         numberList.add(elem);
            
        }

    	return numberList;
	}
	
	public Customer wrapUser (JSONObject json) throws JSONException, JsonSyntaxException 
   {
		
		JSONObject data = json.getJSONObject(KEY_DATA);
    	Gson gson = new Gson();
    	JSONObject customer = new JSONObject();
    	JSONObject jsonUser = new JSONObject();
    	customer = data.getJSONObject(KEY_CUSTOMER);
    	jsonUser = customer.getJSONObject(KEY_USER);
    	JsonParser parser = new JsonParser();
    	
		Customer user =  gson.fromJson(customer.toString(),Customer.class);
    	return user;
	}
	 

}
