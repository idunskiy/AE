package com.datamodel;

public class ProcessStatus {

	int id;
	String title;
	String identificator;
	public int getProccessStatusId() 
	{
		
		 return this.id;
    }
	public String getProccessStatusTitle() 
	{
		 return this.title;
    }
	public String getProccessStatusIdent() 
	{
		 return this.identificator;
    }
	
	public void setProccessStatusId(int id)
	{
		 this.id = id;
	}
	public void setProccessStatusTitle(String title)
	{
		 this.title = title;
	}
	public void setProccessStatusIdent(String id)
	{
		 this.identificator = id;
	}
}
