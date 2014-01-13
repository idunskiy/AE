package com.datamodel;

import com.google.gson.annotations.SerializedName;
/** * класс обьекта заказа, тип - writing. Используется сериализация. */
public class ProductWriting extends ProductType{
		/** * id заказа */
		@SerializedName("id")
		int id;
		/** * название заказа */
		@SerializedName("title")
		String title;
		/** * количество страниц заказа */
		@SerializedName("pages_number")
		String pages_number;
		/** * количество ссылочных материалов по заказу */
		@SerializedName("pages_of_references")
		String number_of_references;
		/** * флаг детальной информации по заказу */
		@SerializedName("dtl_expl")
		boolean dtl_expl;
		/** * информация по заказу */
		@SerializedName("info")
		String info;
		/** * тип essay*/
		@SerializedName("essay_type_id")
		int essayType;
		/** * стиль essay*/
		@SerializedName("essay_creation_style_id")
		int essayStyle;
		
		public ProductWriting()
		{}
		public ProductWriting(String title, String info, boolean dtl_expl, 
				String pages_number , int essayType, int essayStyle,
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
		public String getEssayPagesNumb() 
		{
			 return this.pages_number;
	    }
		public int getEssayId() 
		{
			 return this.id;
	    }
		public int getEssayTypeId() 
		{
			 return this.essayType;
	    }
		public int getEssayStyleId() 
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
		public void setEssayPagesNumb(String pages_number) 
		{
			this.pages_number = pages_number ;
	    }
		public void getEssayId(int id) 
		{
			  this.id = id;
	    }
		public void setEssayTypeId(int essayType) 
		{
			 this.essayType = essayType;
	    }
		public void setEssayStyleId(int essayStyle) 
		{
			 this.essayStyle = essayStyle;
	    }
		 @Override
	     public ProductType returnObject() {
	             return new ProductWriting();
	     }
		 @Override
			public String toString() {
				return "title=" + title + "info " + "=" + info
						+ " " + "profile=" + dtl_expl+  " essay type = "+essayType +" essay style"+essayStyle
						+" number references " + number_of_references +" number pages "+pages_number+"}";
			
			}
		
	

}
