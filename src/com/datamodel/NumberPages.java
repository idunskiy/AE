package com.datamodel;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/** *����� ������� ���������� ������� ������(essay). ������������ ���������� � ���� ������.*/
@DatabaseTable(tableName = "pages_number_list")
public class NumberPages {
	/** *���� ���������� ������� ������.*/
	@DatabaseField(dataType = DataType.STRING)
	String item;
	/** *����������� ��� ORMLite.*/
	public NumberPages()
	{
		
	}
	/** *�������� �����������*/
	public NumberPages(String item)
	{
		this.item = item;
	}
	/** *getter ��� ���� ���������� ������*/
	public String getNumberPage()
	{
		return this.item;
	}
	/** *setter ��� ���� ���������� ������*/
	public void setNumberPage(String item)
	{
		this.item = item;
	}
	
	@Override
	public String toString() {
		return this.item;
		
	}
}
