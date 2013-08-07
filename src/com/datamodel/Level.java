package com.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/** * класс обьекта уровня заказа. Используется сериализация и внесение обьекта в базу данных. */
@DatabaseTable(tableName = "levels")
public class Level implements Parcelable{
/** * id уровня заказа */
@SerializedName("id")
@DatabaseField(id = true)
 int id;
/** * название уровня заказа */
@SerializedName("title")
@DatabaseField(dataType = DataType.STRING)
 String title;
/** * конструктор для ORMLite */
 public Level()
 {
	 
 }
 /** * основной конструктор */
 public Level(int id, String title)
 {
	 this.id = id;
	 this.title = title;
 }
 
 public Level(Parcel in) {
	 readFromParcel(in);
}
 /** * id getter*/
public int getLevelId()
 {
     return id;
 }
/** * title getter*/
 public String getLevelTitle()
 {
     return title;
 }
 /** * id setter*/
 public void setLevelId(int id)
 {
     this.id = id;
 }
 /** * title setter*/
 public void setLevelTitle(String title)
 {
     this.title = title;
 }
 
 @Override
public String toString()
	{		
	  return title;	
	}
 public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
		
	}
	private void readFromParcel(Parcel in) {
		id = in.readInt();
		title = in.readString();
		
	}
	public static final Parcelable.Creator <Level>CREATOR =
	    	new Parcelable.Creator<Level>() {
	            public Level createFromParcel(Parcel in) {
	                return new Level(in);
	            }
	 
	            public Level[] newArray(int size) {
	                return new Level[size];
	            }
	        };
}
