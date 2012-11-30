package com.datamodel;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CustomerThread implements Parcelable{
		@SerializedName("id")
		int id;
		@SerializedName("messages")
		ArrayList<Messages> messages;
		// need for ORMlite
		public CustomerThread()
		{}
		public CustomerThread (int id, ArrayList<Messages> messlist)
	    {
	        this.id = id;
	        this.messages = messlist;
	    }
		public CustomerThread(Parcel in) {
			readFromParcel(in);
		}
		public int getThreadId() 
		{
			
			 return this.id;
	    }
			 
		public ArrayList<Messages> getMessages() 
		{
			 return this.messages;
	    }
		
		public void setMessageId(int id)
		{
			 this.id = id;
		}

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
		public static final Parcelable.Creator CREATOR =
		    	new Parcelable.Creator() {
		            public CustomerThread createFromParcel(Parcel in) {
		                return new CustomerThread(in);
		            }
		 
		            public CustomerThread[] newArray(int size) {
		                return new CustomerThread[size];
		            }
		        };
}
