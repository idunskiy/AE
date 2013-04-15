package com.library;

import java.util.ArrayList;

public class OrderPreferences {
	private String[] arrayList;
	
	private static OrderPreferences instance;

	private OrderPreferences()
	{
		
	    arrayList = new String[9];
	}

	public static OrderPreferences getInstance(){
	    if (instance == null)
	    {
	        instance = new OrderPreferences();
	    }
	    return instance;
	}

	public Object[] getArrayList()
	{
	    return arrayList;
	}
	
	public Object getOrderPrefItem(int i)
	{
		return arrayList[i];
	}
	
	@Override
	public String toString()
	{		
		return getArrayList().toString();	
	}
}
