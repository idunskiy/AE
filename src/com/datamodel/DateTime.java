package com.datamodel;

import com.google.gson.annotations.SerializedName;

public class DateTime {
	@SerializedName("str")
	String str;
	@SerializedName("ts")
	String ts;
	
	public DateTime()
	{}
	public DateTime(String str, String ts)
	{
		this.str = str;
		this.ts = ts;
		
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
	return "{str="+str +" "+"ts="+ts+"}";	
	}
	
	
}
