package com.datamodel;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "subjects")
public class Subject {
	private static final String SUBJECT_ID = "subject_id";
	@SerializedName("id")
	@DatabaseField(columnName = SUBJECT_ID,id = true,generatedId = false)
	int id;
	@SerializedName("title")
	@DatabaseField(dataType = DataType.STRING)
	String title;
	public Subject()
	{}
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
	
	@Override
	public String toString()
	{		
	return title;	
	}
	
	
}
