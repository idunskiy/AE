package com.datamodel;

import java.util.List;

public class OrderList {

	List<Order> orders;
	
	public OrderList()
	{}
	public OrderList(List<Order> order)
	{
		this.orders = orders;
	}
	public List<Order> getOrders()
	{
		return this.orders;
	}
	public void setOrders(List<Order> orders)
	{
		this.orders  = orders;;
	}
}
