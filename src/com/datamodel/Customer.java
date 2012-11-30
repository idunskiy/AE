package com.datamodel;

import com.google.gson.annotations.SerializedName;


public class Customer extends User {
	@SerializedName("phone")
	String phone;
	@SerializedName("user")
	User user;
	
	public Customer()
	{}
	
	public Customer(User user , String phone)
	{
		this.phone= phone;
		this.user = user;
		
	}
	
	public User getUser()
	{
		return this.user;
		
	}
	public String getPhone()
	{
		return this.phone;
	}
	public void setUser(User user)
	{
		this.user = user;
		
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
		
	}
	
	@Override
	public String toString()
	{		
		return "id=" + id + " " + "user=" + user				
				 +  "}";
	}
}
