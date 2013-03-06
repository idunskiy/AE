package com.datamodel;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderList extends ArrayList<Order> implements Parcelable{

	List<Order> orders;
	
	public OrderList()
	{}

	 public OrderList(Parcel in) {
	        this();
	        readFromParcel(in);
	    }
	private void readFromParcel(Parcel in) {
		this.clear();

        // First we have to read the list size
        int size = in.readInt();
        
        for (int i = 0; i < size; i++) {
            Order r = new Order(in);
            this.add(r);
        }
		
	}
	public List<Order> getOrders()
	{
		return this.orders;
	}
	public void setOrders(List<Order> orders)
	{
		this.orders  = orders;;
	}
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void writeToParcel(Parcel dest, int arg1) {
		int size = this.size();

        // We have to write the list size, we need him recreating the list
        dest.writeInt(size);

        for (int i = 0; i < size; i++) {
            Order r = this.get(i);

      //     dest.writeT
        }
		
	}
    public final Parcelable.Creator<OrderList> CREATOR = new Parcelable.Creator<OrderList>() {
        public OrderList createFromParcel(Parcel in) {
            return new OrderList(in);
        }

        public OrderList[] newArray(int size) {
            return new OrderList[size];
        }
    };
}
