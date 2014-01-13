package com.datamodel;

import com.google.gson.annotations.SerializedName;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/** * �����, ����������� ��� essay. ������������ ������������ � �������� � ���� ������*/
@DatabaseTable(tableName = "EssayType")
public class EssayType {
	public static final String ESSAY_TYPE_ID = "essay_type_id";
	public static final String ESSAY_TYPE_DESC = "essay_type_desc";
	public static final String ESSAY_TYPE_TITLE = "essay_type_title";
	/** * id ���� essay.*/
	@SerializedName("id")
	@DatabaseField(columnName = ESSAY_TYPE_ID,id = true)
	int id;
	/** * �������� ���� essay.*/
	@SerializedName("title")
	@DatabaseField(columnName = ESSAY_TYPE_TITLE, dataType = DataType.STRING)
	String title;
	/** * �������� ���� essay.*/
	@SerializedName("description")
	@DatabaseField(columnName = ESSAY_TYPE_DESC, dataType = DataType.STRING)
	String description;
	/** * ����������� ��� ORMLite.*/
	public EssayType()
	{}
	/** *�������� �����������e.*/
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
	/** *  getter �������� */
	public String getEssayTypeTitle() 
	{
		 return this.title;
    }
	/** *  getter �������� */
	public String getEssayTypeDesc() 
	{
		 return this.description;
    }
	/** * id  setter */
	public void setEssayTypeId(int id) 
	{
		  this.id  = id;
    }
	/** *  setter ��������*/
	public void setEssayTypeTitle(String title) 
	{
		  this.title  = title;
    }
	/** *  setter ��������*/
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
