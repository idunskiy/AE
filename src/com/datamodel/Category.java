package com.datamodel;

public class Category {
	int id;
	String title;
	
	public int getCategoryId() 
	{
		
		 return this.id;
    }
		 
	public String getCategoryTitle() 
	{
		 return this.title;
    }
	
	public void setCategoryId(int id)
	{
		 this.id = id;
	}

	public void setCategoryTitle(String title)
	{
		 this.title = title;
	}

}
