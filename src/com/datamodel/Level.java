package com.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "levels")
public class Level implements Parcelable{
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
 public Level(Parcel in) {
	 readFromParcel(in);
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
	public static final Parcelable.Creator CREATOR =
	    	new Parcelable.Creator() {
	            public Level createFromParcel(Parcel in) {
	                return new Level(in);
	            }
	 
	            public Level[] newArray(int size) {
	                return new Level[size];
	            }
	        };
}
