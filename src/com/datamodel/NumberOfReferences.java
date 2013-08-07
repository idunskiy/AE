package com.datamodel;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/** *класс обьекта количества ссылок заказа(essay). Используется добавление в базу данных.*/
@DatabaseTable(tableName = "number_of_references")
public class NumberOfReferences {
	/** *поле количества ссылок*/
	@DatabaseField(dataType = DataType.STRING)
	String item;
	/** *конструктор для ORMLite.*/
	public NumberOfReferences()
	{
		
	}
	/** *основной конструктор*/
	public NumberOfReferences(String item)
	{
		this.item = item;
	}
	/** *getter для поля количества ссылок заказа*/
	public String getNumberReference()
	{
		return this.item;
	}
	/** *setter для поля количества ссылок заказа*/
	public void setNumberReference(String item)
	{
		this.item = item;
	}
	@Override
	public String toString() {
		return this.item;
		
	}
}
