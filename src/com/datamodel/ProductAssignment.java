package com.datamodel;

import com.google.gson.annotations.SerializedName;

public class ProductAssignment extends ProductType{
	ProductAssignment thisObj;
	@SerializedName("title")
	String title;
	@SerializedName("info")
	String info;
	@SerializedName("dtl_expl")
	boolean dtl_expl;
	@SerializedName("special_info")
	String special_info;
	@SerializedName("shoot_exclusive_video")
	boolean shoot_exclusive_video;
	@SerializedName("shoot_common_video")
	boolean shoot_common_video;
	public ProductAssignment()
	{}
	public ProductAssignment(String title, String info, boolean dtl_expl, String special_info,
			boolean shoot_common_video, boolean shoot_exclusive_video)
	{
		
		this.title = title;
		this.info = info ;
		this.dtl_expl = dtl_expl;
		this.special_info = special_info;
		this.shoot_common_video = shoot_common_video;
		this.shoot_exclusive_video =shoot_exclusive_video;
	}
	public String getAssignTitle() 
	{
		 return this.title;
    }
	public String getAssignInfo() 
	{
		 return this.info;
    }
	public boolean getAssignDtl_expl() 
	{
		 return this.dtl_expl;
    }
	public String getAssignSpecInfo() 
	{
		 return this.special_info;
    }
	public boolean getAssignCommVideo() 
	{
		 return this.shoot_common_video;
    }
	public boolean getAssignExcVideo() 
	{
		 return this.shoot_exclusive_video;
    }
	
	public void setAssignTitle(String title)
	{
		
		this.title = title;
		
	}
	public void setAssignInfo(String info)
	{
		this.info = info;
		
	}
	public void setAssignTitle(boolean dtl_expl)
	{
		this.dtl_expl = dtl_expl;
		
	}
	public void setAssignSpecInfo(String special_info)
	{
		this.special_info = special_info;
	}
	public void setAssignExcVideo(boolean shoot_exclusive_video)
	{
		this.shoot_exclusive_video = shoot_exclusive_video;
		
	}
	public void setAssignCommVideo(boolean shoot_common_video)
	{
		this.shoot_common_video = shoot_common_video;
		
	}
	 @Override
     public ProductType returnObject() {
             return new ProductAssignment();
     }
	@Override
	public String toString() {
		return "title=" + title + "info " + "=" + info
				+ " " + "profile=" + dtl_expl +  "}";
	}
	
	
}
