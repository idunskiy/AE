package com.datamodel;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CustomerThread {
		@SerializedName("id")
		int id;
		@SerializedName("messages")
		List<Message> messages;
		// need for ORMlite
		public CustomerThread()
		{}
		public CustomerThread (int id, List<Message> messlist)
	    {
	        this.id = id;
	        this.messages = messlist;
	    }
		public int getThreadId() 
		{
			
			 return this.id;
	    }
			 
		public List<Message> getMessages() 
		{
			 return this.messages;
	    }
		
		public void setMessageId(int id)
		{
			 this.id = id;
		}

		public void setMessages(List<Message> messages)
		{
			 this.messages = messages;
		}
		
		@Override
		public String toString()
		{		
		return "{messages="+messages +" "+"id="+id+"}";	
		}
}
