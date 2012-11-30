package com.datamodel;

import com.google.gson.annotations.SerializedName;

public class ProductWriting extends ProductType{
		
		@SerializedName("id")
		int id;
		@SerializedName("title")
		String title;
		@SerializedName("pages_number")
		int pages_number;
		@SerializedName("number_of_references")
		String number_of_references;
		@SerializedName("dtl_expl")
		boolean dtl_expl;
		@SerializedName("info")
		String info;
		@SerializedName("essay_type")
		EssayType essayType;
		@SerializedName("essay_creation_style")
		EssayCreationStyle essayStyle;
		
		public ProductWriting()
		{}
		public ProductWriting(String title, String info, boolean dtl_expl, 
				int pages_number , EssayType essayType, EssayCreationStyle essayStyle,
				int id,String number_of_references)
		{
			this.title = title;
			this.info = info ;
			this.dtl_expl = dtl_expl;
			this.id = id;
			this.pages_number = pages_number;
			this.number_of_references = number_of_references;
			this.essayType = essayType;
			this.essayStyle = essayStyle;
			
		}
		public ProductWriting(IProductType c){
            super(c);
		}
		public String getEssayTitle() 
		{
			 return this.title;
	    }
		public String getEssayInfo() 
		{
			 return this.info;
	    }
		public String getEssayNumbRef() 
		{
			 return this.number_of_references;
	    }
		public boolean getEssayDtl_expl() 
		{
			 return this.dtl_expl;
	    }
		public int getEssayPagesNumb() 
		{
			 return this.pages_number;
	    }
		public int getEssayId() 
		{
			 return this.id;
	    }
		public EssayType getEssayType() 
		{
			 return this.essayType;
	    }
		public EssayCreationStyle getEssayStyle() 
		{
			 return this.essayStyle;
	    }
		public void setEssayTitle(  String  title) 
		{
			  this.title = title;
	    }
		public void setEssayInfo(String info) 
		{
			 this.info = info;
	    }
		public void setEssayNumbRef(String number_of_references) 
		{
			  this.number_of_references = number_of_references;
	    }
		public void getEssayDtl_expl(boolean dtl_expl) 
		{
			 this.dtl_expl = dtl_expl;
	    }
		public void setEssayPagesNumb(int pages_number) 
		{
			this.pages_number = pages_number ;
	    }
		public void getEssayId(int id) 
		{
			  this.id = id;
	    }
		public void setEssayType(EssayType essayType) 
		{
			 this.essayType = essayType;
	    }
		public void getEssayStyle(EssayCreationStyle essayStyle) 
		{
			 this.essayStyle = essayStyle;
	    }
		 @Override
	     public ProductType returnObject() {
	             return new ProductWriting();
	     }
		
	

}