package com.datamodel;


import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/** * �����, ����������� ����� �������� essay. ������������ ������������ � �������� � ���� ������*/
@DatabaseTable(tableName = "EssayCreationStyle")
public class EssayCreationStyle {
	private static final String ESSAY_CREATION_ID = "essay_cr_id";
	private static final String ESSAY_CREATION_DESC = "essay_cr_desc";
	private static final String ESSAY_CREATION_TITLE = "essay_cr_title";
	/** * id ����� �������� essay.*/
	@SerializedName("id")
	@DatabaseField(columnName = ESSAY_CREATION_ID,id = true)
     int id;
	/** * �������� ����� �������� essay.*/
	@SerializedName("description")
	@DatabaseField(columnName = ESSAY_CREATION_DESC, dataType = DataType.STRING)
     String description;
	/** * �������� ����� �������� essay.*/
	@SerializedName("title")
	@DatabaseField(columnName = ESSAY_CREATION_TITLE, dataType = DataType.STRING)
     String title;
	/** *����������� ��� ORMLite*/
	public EssayCreationStyle()
	{}
	/** *�������� �����������*/
	public EssayCreationStyle(int id, String description, String title)
	{
		this.id = id;
		this.description = description;
		this.title = title;
		
	}
	/** * id  getter */
	public int getECSId()
	{
		return this.id;
	}
	/** *  getter �������� */
	public String getECSDesc()
	{
		return this.description;
	}
	/** *  getter ��������*/
	public String getECSTitle()
	{
		return this.title;
	}
	/** * id  setter */
	public void setStr(int id)
	{
		this.id = id;
	}
	/** *  setter ��������*/
	public void setECSDesc(String description)
	{
		this.description = description;
	}
	/** *  setter ��������*/
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
