package com.datamodel;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable(tableName = "EssayType")
public class EssayType {
	private static final String ESSAY_TYPE_ID = "essay_type_id";
	private static final String ESSAY_TYPE_DESC = "essay_type_desc";
	private static final String ESSAY_TYPE_TITLE = "essay_type_title";
	@SerializedName("id")
	@DatabaseField(columnName = ESSAY_TYPE_ID,id = true)
	int id;
	@SerializedName("title")
	@DatabaseField(columnName = ESSAY_TYPE_TITLE, dataType = DataType.STRING)
	String title;
	@SerializedName("description")
	@DatabaseField(columnName = ESSAY_TYPE_DESC, dataType = DataType.STRING)
	String description;
	
	public EssayType()
	{}
	public EssayType(int id, String title, String description)
	{
		this.id = id;
		this.title = title;
		this.description = description;
	}
	public int getEssayTypeId() 
	{
		 return this.id;
    }
	public String getEssayTypeTitle() 
	{
		 return this.title;
    }
	public String getEssayTypeDesc() 
	{
		 return this.description;
    }
	public void setEssayTypeId(int id) 
	{
		  this.id  = id;
    }
	public void setEssayTypeTitle(String title) 
	{
		  this.title  = title;
    }
	public void setEssayTypeDesc(String description) 
	{
		  this.description = description;
    }
	@Override
	public String toString()
	{		
	  return title;	
	}
}
