package com.datamodel;
/** * ����� ������� ���� ��������. �������� ��������� ��� ������� ProductAssignment, ProductWriting. ��������� �������� ������ �� ������� � ����������� �� ������������ ���������� �� ������.*/
public class ProductType implements IProductType{
	/** *���� �������� ��������*/
	String product;
	/** *��� ��������*/
	static ProductType productType;
	/** *��� �������� - assignment*/
	static ProductAssignment productAssignment;
	/** *��� �������� - writing*/
	static ProductWriting productWriting;
	/** *������ ���������� IProductType*/
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
