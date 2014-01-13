package com.library.singletones;

import java.util.HashMap;
import java.util.Map;

import android.view.View;

import com.datamodel.Order;
/**
 * singletone for order and view binding
 */
public class OrdersViewMapSingletone {
	 Map<Order, View>  arrayList;

	private static OrdersViewMapSingletone instance;

	private OrdersViewMapSingletone()
	{
	    arrayList = new  HashMap<Order,View>();
	}
	public static OrdersViewMapSingletone getInstance(){
		
	    return instance;
	}
	 public static void initInstance()
	  {
		 if (instance == null)
		 {
		        instance = new OrdersViewMapSingletone();
		 }
	  }

	public Map<Order, View> getOrderViewMap()
	{
	    return arrayList;
	}
	
	@Override
	public String toString()
	{		
	return getOrderViewMap().toString();	
	}
}
