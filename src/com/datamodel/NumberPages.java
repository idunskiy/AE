package com.datamodel;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/** *класс обьекта количества страниц заказа(essay). Используется добавление в базу данных.*/
@DatabaseTable(tableName = "pages_number_list")
public class NumberPages {
	/** *поле количества страниц заказа.*/
	@DatabaseField(dataType = DataType.STRING)
	String item;
	/** *конструктор для ORMLite.*/
	public NumberPages()
	{
		
	}
	/** *основной конструктор*/
	public NumberPages(String item)
	{
		this.item = item;
	}
	/** *getter для поля количества заказа*/
	public String getNumberPage()
	{
		return this.item;
	}
	/** *setter для поля количества заказа*/
	public void setNumberPage(String item)
	{
		this.item = item;
	}
	
	@Override
	public String toString() {
		return this.item;
		
	}
}
