package com.datamodel;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable(tableName = "pages_number_list")
public class NumberPages {
	@DatabaseField(dataType = DataType.STRING)
	String item;
	public NumberPages()
	{
		
	}
	public NumberPages(String item)
	{
		this.item = item;
	}
	
	public String getNumberPage()
	{
		return this.item;
	}
	public void setNumberPage(String item)
	{
		this.item = item;
	}
	
	@Override
	public String toString() {
		return this.item;
		
	}
}
