package com.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/** *класс обьекта статуса заказа. Используется сериализация и добавление в базу данных.*/
@DatabaseTable(tableName = "process_status")
public class ProcessStatus implements Parcelable{
	public static final String STATUS_ID = "id";
	public static final String STATUS_TITLE = "title";
	/** *id статуса заказа.*/
	@SerializedName("id")
	@DatabaseField(id = true,generatedId = false,dataType = DataType.INTEGER, columnName = STATUS_ID)
	int id;
	/** *идентификатор статуса заказа.*/
	@SerializedName("identificator")
	@DatabaseField(dataType = DataType.STRING, columnName = "identificator")
	String identificator;
	/** *название статуса заказа.*/
	@SerializedName("title")
	@DatabaseField(dataType = DataType.STRING, columnName = STATUS_TITLE)
	String title;
	/** *конструктор для ORMLite*/
	public ProcessStatus()
	{}
	/** *основной конструктор*/
	public ProcessStatus (int id,String identificator, String title)
    {
        this.id = id;
        this.identificator = identificator;
        this.title = title;
        
    }
	public ProcessStatus(Parcel in) {
		readFromParcel(in);
	}
	public ProcessStatus(String string) {
		this.title = string;
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
	public static final Parcelable.Creator<ProcessStatus> CREATOR =
	    	new Parcelable.Creator<ProcessStatus>() {
	            public ProcessStatus createFromParcel(Parcel in) {
	                return new ProcessStatus(in);
	            }
	 
	            public ProcessStatus[] newArray(int size) {
	                return new ProcessStatus[size];
	            }
	        };
}
