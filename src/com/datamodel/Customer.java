package com.datamodel;

import com.google.gson.annotations.SerializedName;

/** * Класс, описывающий обьект клиента. Используется сериализация. */
public class Customer extends User {
	/** * номер телефона клиента */
	@SerializedName("phone")
	String phone;
	/** * Обьект класса User 
	 * @see User*/
	@SerializedName("user")
	User user;
	
	public Customer()
	{}
	/** * основной конструктор*/
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
