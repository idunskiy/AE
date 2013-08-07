package com.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
/** * класс обьекта потока иформации по заказу в присылаемом сообщении.*/
public class Threads implements Parcelable{
	/** * обьект заказа*/
	@SerializedName("order")
	private Order order;
	/** * id заказа*/
	@SerializedName("id")
	private int id;
	public Threads()
	{}
	public Threads(Parcel in) {
		readFromParcel(in);
	}
	public int getTreadId() 
	{
		 return this.id;
    } 
	public Order getTreadOrder() 
	{
		 return this.order;
    }
	public void setTreadId(int id)
	{
		 this.id = id;
	}
	public void setTreadOrder(Order order)
	{
		 this.order = order;
	}
	
	@Override
	public String toString()
	{		
		return "id=" + id + " " + "order=" + order
				
				+ "}";
	}
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeParcelable(order, arg1);
		dest.writeInt(id);
		
	}
	private void readFromParcel(Parcel in) {
		id = in.readInt();
		order = in.readParcelable(Order.class.getClassLoader());
		
	}
	public static final Parcelable.Creator<Threads> CREATOR =
	    	new Parcelable.Creator<Threads>() {
	            public Threads createFromParcel(Parcel in) {
	                return new Threads(in);
	            }
	 
	            public Threads[] newArray(int size) {
	                return new Threads[size];
	            }
	        };

}
