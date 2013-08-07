package com.datamodel;

import com.google.gson.annotations.SerializedName;
/** *класс обьекта продукта заказа. »спользуетс€ сериализаци€.*/
public class Product implements IProductType{
	ProductAssignment prodAss;
	ProductWriting prodWr;
	/** *id продукта.*/
	@SerializedName("id")
	int id;
	/** *название имени продукта.*/
	@SerializedName("product_type")
	String product_type;
	/** * поле профил€ продукта.*/
	@SerializedName("product_profile")
	ProductType product_profile;
	
	public Product()
	{}
	/** * основной конструктор*/
	public Product(int id, String product_type, ProductType product_profile)
	{
		this.id = id;
		this.product_type = product_type;
		this.product_profile = product_profile;
		
	}
	
	public int getProductId() 
	{
		
		 return this.id;
    }
	public String getProductType() 
	{
		 return this.product_type;
    }
	public ProductType getProduct() 
	{
		
		 return this.product_profile;
    }
	public void setProductId(int id) 
	{
		this.id = id;
    }
	public void setProductTitle(String product_type) 
	{
		this.product_type = product_type;
    }
	public void setProduct(ProductType product_profile) 
	{
		this.product_profile = product_profile;
    }
	@Override
	public String toString() {
		return "id=" + id + " " + "title=" + product_type
				+ " " + "profile=" + product_profile +  "}";
	}
	public ProductType returnObject() {
		
		return null;
	}
	
	
	
	
}
