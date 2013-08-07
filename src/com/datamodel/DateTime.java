package com.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
/** * Класс, описывающий обьект получаемого формата даты. Используется сериализация */
public class DateTime implements Parcelable{
	/** * строка даты */
	@SerializedName("str")
	String str;
	/** * строка даты в ts формате */
	@SerializedName("ts")
	String ts;
	
	public DateTime()
	{}
	public DateTime(String str, String ts)
	{
		this.str = str;
		this.ts = ts;
		
	}
	public DateTime(Parcel in) {
		readFromParcel(in);
	}
	public String getStr()
	{
		return this.str;
	}
	public String getTs()
	{
		return this.ts;
	}
	public void setStr(String str)
	{
		this.str = str;
	}
	public void setTs(String ts)
	{
		this.ts = ts;
	}
	@Override
	public String toString()
	{		
	return str;	
	}
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(ts);
		dest.writeString(str);
		
	}
	private void readFromParcel(Parcel in) {
		
		str = in.readString();
		ts = in.readString();
	}
	public static final Parcelable.Creator <DateTime>CREATOR =
	    	new Parcelable.Creator<DateTime>() {
	            public DateTime createFromParcel(Parcel in) {
	                return new DateTime(in);
	            }
	 
	            public DateTime[] newArray(int size) 
	            {
	                return new DateTime[size];
	            }
	        };
	
	
}
