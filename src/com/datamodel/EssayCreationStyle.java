package com.datamodel;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable(tableName = "EssayCreationStyle")
public class EssayCreationStyle {
	private static final String ESSAY_CREATION_ID = "essay_cr_id";
	private static final String ESSAY_CREATION_DESC = "essay_cr_desc";
	private static final String ESSAY_CREATION_TITLE = "essay_cr_title";
	@SerializedName("id")
	@DatabaseField(columnName = ESSAY_CREATION_ID,id = true)
     int id;
	@SerializedName("description")
	@DatabaseField(columnName = ESSAY_CREATION_DESC, dataType = DataType.STRING)
     String description;
	@SerializedName("title")
	@DatabaseField(columnName = ESSAY_CREATION_TITLE, dataType = DataType.STRING)
     String title;
	public EssayCreationStyle()
	{}
	public EssayCreationStyle(int id, String description, String title)
	{
		this.id = id;
		this.description = description;
		this.title = title;
		
	}
	public int getECSId()
	{
		return this.id;
	}
	public String getECSDesc()
	{
		return this.description;
	}
	public String getECSTitle()
	{
		return this.title;
	}
	public void setStr(int id)
	{
		this.id = id;
	}
	public void setECSDesc(String description)
	{
		this.description = description;
	}
	public void setStr(String title)
	{
		this.title = title;
	}
	@Override
	public String toString()
	{		
	return title;	
	}
	
	
	
}
