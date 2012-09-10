package com.datamodel;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "levels")
public class Level {
@SerializedName("id")
@DatabaseField(id = true)
 int id;
@SerializedName("title")
@DatabaseField(dataType = DataType.STRING)
 String title;
 
 public Level()
 {
	 
 }
 public Level(int id, String title)
 {
	 this.id = id;
	 this.title = title;
 }
 public int getLevelId()
 {
     return id;
 }

 public String getLevelTitle()
 {
     return title;
 }

 public void setLevelId(int id)
 {
     this.id = id;
 }

 public void setLevelTitle(String title)
 {
     this.title = title;
 }
 
 @Override
public String toString()
	{		
	return title;	
	}
}
