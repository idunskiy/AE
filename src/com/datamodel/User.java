package com.datamodel;

import java.util.Date;

public class User {
  int id;
  String first_name;
  String last_name;
  String email;
  String timezone;
  Date created_at;
 
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
	public Date getDate() 
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
	public void setUserDate(Date date)
	{
		 this.created_at = date;
	}
  
  
}
