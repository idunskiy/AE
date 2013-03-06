package com.datamodel;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "number_of_references")
public class NumberOfReferences {
	@DatabaseField(dataType = DataType.STRING)
	String item;
	public NumberOfReferences()
	{
		
	}
	
	public NumberOfReferences(String item)
	{
		this.item = item;
	}
	
	public String getNumberReference()
	{
		return this.item;
	}
	public void setNumberReference(String item)
	{
		this.item = item;
	}
	@Override
	public String toString() {
		return this.item;
		
	}
}
