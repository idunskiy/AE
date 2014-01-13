package com.datamodel;


import com.google.gson.annotations.SerializedName;
/** * класс обьекта клиента. Используется сериализация.*/
public class User {
	/** * id клиента*/
  @SerializedName("id")
  int id;
  /** * имя клиента*/
  @SerializedName("first_name")
  String first_name;
  /** * фамилия клиента*/
  @SerializedName("last_name")
  String last_name;
  /** * email клиента*/
  @SerializedName("email")
  String email;
  /** * временная зона клиента*/
  @SerializedName("timezone")
  String timezone;
  /** * время, когда был добавлен клиент*/
  @SerializedName("created_at")
  int created_at;
 
   public int getUserId() 
   {
		
    	 return this.id;
   }
  public String getUserFirstName() 
	{
		 return this.first_name;
    }
	
	public String getUserLastName() 
	{
		 return this.last_name;
    }
	public String getUserEmail() 
	{
		 return this.email;
    }
	public String getTimeZone() 
	{
		 return this.timezone;
   }
	public int getDate() 
	{
		 return this.created_at;
    }
	public void setUserId(int id)
	{
		 this.id = id;
	}
	public void setUserFirstName(String first_name)
	{
		 this.first_name = first_name;
	}
	public void setUserLastName(String last_name)
	{
		 this.last_name = last_name;
	}
	public void setUserEmail(String email)
	{
		 this.email = email;
	}
	public void setUserTimeZone(String timeZone)
	{
		 this.timezone = timeZone;
	}
	public void setUserDate(int date)
	{
		 this.created_at = date;
	}
	@Override
	public String toString()
	{		
		return "id=" + id + " " + "first name=" + first_name
				
				+ "timezone = "+ timezone +"}"  + "created_at" + created_at+ "}";
	}
  
}
