package com.datamodel;

import com.google.gson.annotations.SerializedName;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/** * класс, описывающий тип essay. Используется сериализация и внесение в базу данных*/
@DatabaseTable(tableName = "EssayType")
public class EssayType {
	public static final String ESSAY_TYPE_ID = "essay_type_id";
	public static final String ESSAY_TYPE_DESC = "essay_type_desc";
	public static final String ESSAY_TYPE_TITLE = "essay_type_title";
	/** * id типа essay.*/
	@SerializedName("id")
	@DatabaseField(columnName = ESSAY_TYPE_ID,id = true)
	int id;
	/** * название типа essay.*/
	@SerializedName("title")
	@DatabaseField(columnName = ESSAY_TYPE_TITLE, dataType = DataType.STRING)
	String title;
	/** * описание типа essay.*/
	@SerializedName("description")
	@DatabaseField(columnName = ESSAY_TYPE_DESC, dataType = DataType.STRING)
	String description;
	/** * конструктор для ORMLite.*/
	public EssayType()
	{}
	/** *основной конструкторe.*/
	public EssayType(int id, String title, String description)
	{
		this.id = id;
		this.title = title;
		this.description = description;
	}
	/** * id  getter */
	public int getEssayTypeId() 
	{
		 return this.id;
    }
	/** *  getter названия */
	public String getEssayTypeTitle() 
	{
		 return this.title;
    }
	/** *  getter описания */
	public String getEssayTypeDesc() 
	{
		 return this.description;
    }
	/** * id  setter */
	public void setEssayTypeId(int id) 
	{
		  this.id  = id;
    }
	/** *  setter названия*/
	public void setEssayTypeTitle(String title) 
	{
		  this.title  = title;
    }
	/** *  setter описания*/
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
