package com.datamodel;


public class Customer extends User {
	int user_id;
	User user;
	
	public Customer()
	{
		user = new User();
		
	}
	
	public User getUser()
	{
		return user;
		
	}
	public void setUser(User user)
	{
		this.user = user;
		
	}
}
