package com.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "process_status")
public class ProcessStatus implements Parcelable{
	public static final String STATUS_ID = "id";
	public static final String STATUS_TITLE = "title";
	@SerializedName("id")
	@DatabaseField(id = true,generatedId = false,dataType = DataType.INTEGER, columnName = STATUS_ID)
	int id;
	@SerializedName("identificator")
	@DatabaseField(dataType = DataType.STRING, columnName = "identificator")
	String identificator;
	@SerializedName("title")
	@DatabaseField(dataType = DataType.STRING, columnName = STATUS_TITLE)
	String title;
	
	public ProcessStatus()
	{}
	public ProcessStatus (int id,String identificator, String title)
    {
        this.id = id;
        this.identificator = identificator;
        this.title = title;
        
    }
	public ProcessStatus(Parcel in) {
		readFromParcel(in);
	}
	public int getProccessStatusId() 
	{
		
		 return this.id;
    }
	public String getProccessStatusTitle() 
	{
		 return this.title;
    }
	public String getProccessStatusIdent() 
	{
		 return this.identificator;
    }
	
	public void setProccessStatusId(int id)
	{
		 this.id = id;
	}
	public void setProccessStatusTitle(String title)
	{
		 this.title = title;
	}
	public void setProccessStatusIdent(String id)
	{
		 this.identificator = id;
	}
	@Override
	public String toString()
	{		
	return "{title="+title +" "+"id="+id+" "+"identificator="+" " +identificator+"}";	
	}
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeString(identificator);
		
	}
	private void readFromParcel(Parcel in) {
		id = in.readInt();
		title = in.readString();
		identificator = in.readString();
		
	}
	public static final Parcelable.Creator CREATOR =
	    	new Parcelable.Creator() {
	            public ProcessStatus createFromParcel(Parcel in) {
	                return new ProcessStatus(in);
	            }
	 
	            public ProcessStatus[] newArray(int size) {
	                return new ProcessStatus[size];
	            }
	        };
}
