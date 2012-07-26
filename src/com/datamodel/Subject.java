package com.datamodel;

public class Subject {

	int id;
	String title;
	public int getSubjectId() 
	{
		
		 return this.id;
    }
		 
	public String getSubjectTitle() 
	{
		 return this.title;
    }
	
	public void setSubjectId(int id)
	{
		 this.id = id;
	}

	public void setSubjectTitle(String title)
	{
		 this.title = title;
	}
}
