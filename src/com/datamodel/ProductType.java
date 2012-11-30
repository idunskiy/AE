package com.datamodel;

public class ProductType implements IProductType{
	String product;
	
	static ProductType productType;
	static ProductAssignment productAssignment;
	static ProductWriting productWriting;
	IProductType component;
	public ProductType()
	{		
    }
	public ProductType(IProductType c)
	{
             component = c;
    }

//	@Override
//	public String toString() {
//		return productAssignment.toString() + productWriting.toString();
//	}
	public ProductType returnObject() {
		return this.returnObject();
	}
}
