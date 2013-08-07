package com.datamodel;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
/** *  ласс, описывающий обьект потока сообщений конкретного заказа. »спользуетс€ сериализаци€ */
public class CustomerThread implements Parcelable{
	/** * id заказа */
		@SerializedName("id")
		int id;
		/** * список сообщений */
		@SerializedName("messages")
		ArrayList<Messages> messages;
		/** * основной конструктор */
		public CustomerThread (int id, ArrayList<Messages> messlist)
	    {
	        this.id = id;
	        this.messages = messlist;
	    }
		public CustomerThread(Parcel in) {
			readFromParcel(in);
		}
		/** * order id getter*/
		public int getThreadId() 
		{
			
			 return this.id;
	    }
		/** * List of messages getter*/
		public ArrayList<Messages> getMessages() 
		{
			 return this.messages;
	    }
		/** * order id setter*/
		public void setMessageId(int id)
		{
			 this.id = id;
		}
		/** * Messages list getter*/
		public void setMessages(ArrayList<Messages> messages)
		{
			 this.messages = messages;
		}
		public void addMessage(Messages message)
		{
			messages.add(message);
		}
		
		@Override
		public String toString()
		{		
		return "{messages="+messages +" "+"id="+id+"}";	
		}
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(id);
			dest.writeList(messages);
			
		}
		private void readFromParcel(Parcel in) {
			id = in.readInt();
			messages = in.readArrayList(Messages.class.getClassLoader());
			
		}
		public static final Parcelable.Creator <CustomerThread> CREATOR =
		    	new Parcelable.Creator<CustomerThread>() {
		            public CustomerThread createFromParcel(Parcel in) {
		                return new CustomerThread(in);
		            }
		 
		            public CustomerThread[] newArray(int size) {
		                return new CustomerThread[size];
		            }
		        };
}
