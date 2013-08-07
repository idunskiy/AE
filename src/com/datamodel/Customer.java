package com.datamodel;

import com.google.gson.annotations.SerializedName;

/** * �����, ����������� ������ �������. ������������ ������������. */
public class Customer extends User {
	/** * ����� �������� ������� */
	@SerializedName("phone")
	String phone;
	/** * ������ ������ User 
	 * @see User*/
	@SerializedName("user")
	User user;
	
	public Customer()
	{}
	/** * �������� �����������*/
	public Customer(User user , String phone)
	{
		this.phone= phone;
		this.user = user;
		
	}
	/** * User getter*/
	public User getUser()
	{
		return this.user;
		
	}
	/** * phone getter*/
	public String getPhone()
	{
		return this.phone;
	}
	/** * user setter*/
	public void setUser(User user)
	{
		this.user = user;
		
	}
	/** * phone setter*/
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
