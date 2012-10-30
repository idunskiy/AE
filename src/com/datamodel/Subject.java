package com.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "subjects")
public class Subject implements Parcelable{
	private static final String SUBJECT_ID = "subject_id";
	@SerializedName("id")
	@DatabaseField(columnName = SUBJECT_ID,id = true,generatedId = false)
	int id;
	@SerializedName("title")
	@DatabaseField(dataType = DataType.STRING)
	String title;
	public Subject()
	{}
	public Subject(Parcel in) {
		readFromParcel(in);
	}
	
	public int getSubjectId() 
	{
		 return this.id;
    } 
	public String getSubjectTitle() 
	{
		 return this.title;
    }
	public void setSubjectId(int id)
	{
		 this.id = id;
	}
	public void setSubjectTitle(String title)
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
	            public Subject createFromParcel(Parcel in) {
	                return new Subject(in);
	            }
	 
	            public Subject[] newArray(int size) {
	                return new Subject[size];
	            }
	        };
	
	
}
