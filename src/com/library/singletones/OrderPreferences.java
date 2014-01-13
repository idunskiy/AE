package com.library.singletones;

/**
 *  singletone for order's preferences saving
 */
public class OrderPreferences {
	private String[] arrayList;
	
	private static OrderPreferences instance;
	private OrderPreferences()
	{
	    arrayList = new String[16];
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
