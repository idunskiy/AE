package com.datamodel;
/** * класс обьекта типа продукта. явл€етс€ родителем дл€ классов ProductAssignment, ProductWriting. ѕринимает значени€ одного из классов в зависимости от передаваемой информации по заказу.*/
public class ProductType implements IProductType{
	/** *поле названи€ продукта*/
	String product;
	/** *тип продукта*/
	static ProductType productType;
	/** *тип продукта - assignment*/
	static ProductAssignment productAssignment;
	/** *тип продукта - writing*/
	static ProductWriting productWriting;
	/** *обьект интерфейса IProductType*/
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
