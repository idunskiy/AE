package com.library;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.datamodel.Category;
import com.datamodel.Level;
import com.datamodel.Order;
import com.datamodel.ProcessStatus;
import com.datamodel.Subject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
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
    private String KEY_SUBJECTS = "subjects";
    
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
		Log.i("customer json while login",customer.toString());
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
		{JSONArray data= k.getJSONArray(KEY_ORDERS);
		JsonParser parser = new JsonParser();
		JsonArray array = parser.parse(data.toString()).getAsJsonArray();
		Gson gson = new Gson();
		Type listType = new TypeToken<List<Order>>() {}.getType();
		
	    List<Order> orders = new ArrayList<Order>();
	    orders = gson.fromJson(array.toString(), listType);
	    Order a = new Order();
	    a.setOrders(orders);
		return orders;}
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
	 

}
