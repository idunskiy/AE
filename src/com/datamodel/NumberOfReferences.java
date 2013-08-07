package com.datamodel;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/** *����� ������� ���������� ������ ������(essay). ������������ ���������� � ���� ������.*/
@DatabaseTable(tableName = "number_of_references")
public class NumberOfReferences {
	/** *���� ���������� ������*/
	@DatabaseField(dataType = DataType.STRING)
	String item;
	/** *����������� ��� ORMLite.*/
	public NumberOfReferences()
	{
		
	}
	/** *�������� �����������*/
	public NumberOfReferences(String item)
	{
		this.item = item;
	}
	/** *getter ��� ���� ���������� ������ ������*/
	public String getNumberReference()
	{
		return this.item;
	}
	/** *setter ��� ���� ���������� ������ ������*/
	public void setNumberReference(String item)
	{
		this.item = item;
	}
	@Override
	public String toString() {
		return this.item;
		
	}
}
