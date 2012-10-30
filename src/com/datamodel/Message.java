package com.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Message implements Parcelable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("id")
	int id;
	@SerializedName("created_at")
	DateTime created_at;
	@SerializedName("body")
	String body;
	@SerializedName("deadline")
	DateTime deadline;
	@SerializedName("price")
	float price;
	@SerializedName("icch")
	boolean is_cat_ch;
	@SerializedName("idch")
	boolean is_dl_ch;
	@SerializedName("ipch")
	boolean is_pr_ch;
	@SerializedName("files")
	ArrayList<File> files;
	@SerializedName("tread")
	Threads thread;
	// need for ORMlite
	public Message()
	{}
	public Message (int id, DateTime created_at , String body, DateTime deadline, float price, 
			boolean is_cat_ch, boolean is_dl_ch, boolean is_pr_ch , ArrayList<File> files, Threads thread)
    {
        this.id = id;
        this.created_at = created_at;
        this.body = body;
        this.deadline = deadline;
        this.price = price;
        this.is_cat_ch = is_cat_ch;
        this.is_dl_ch = is_dl_ch;
        this.is_pr_ch = is_pr_ch;
        this.files = files;
        this.thread = thread;
    }
	public Message(Parcel in) {
		readFromParcel(in);
	}
	public int getMessageId() 
	{
		
		 return this.id;
    }
		 
	public DateTime getMessageDate() 
	{
		 return this.created_at;
    }
	
	public String getMessageBody() 
	{
		 return this.body;
    }
	
		
	public DateTime getMessagedeadline() 
	{
		 return this.deadline;
    }
	
	public float getPrice() 
	{
		 return this.price;
    }
	
	public boolean getIsCatCh() 
	{
		 return this.is_cat_ch;
    }
	public boolean getIsDlCh() 
	{
		 return this.is_dl_ch;
    }
	public boolean getIsPrCh() 
	{
		 return this.is_pr_ch;
    }
	
	public ArrayList<File> getFiles() 
	{
		 return this.files;
    }
	public Threads getThread() 
	{
		 return this.thread;
    }
		
	public void setMessageId(int id)
	{
		 this.id = id;
	}

	public void setMessageDate(DateTime date)
	{
		 this.created_at = date;
	}
	
	public void setMessageBody(String body)
	{
		 this.body = body;
	}
	
	public void setDeadLine(DateTime deadline)
	{
		 this.deadline = deadline;
	}
	
	public void setPrice(float price)
	{
		 this.price = price;
	}
	
	public void setIsCatCh(boolean IsCatCh)
	{
		 this.is_cat_ch = IsCatCh;
	}
	
	public void setIsDlCh(boolean IsDlCh)
	{
		 this.is_dl_ch = IsDlCh;
	}
	
	public void setIsPrCh(boolean IsPrCh)
	{
		 this.is_pr_ch = IsPrCh;
	}
	
	public void setFiles(ArrayList<File> files)
	{
		 this.files = files;
	}
	public void setThread(Threads thread) 
	{
		 this.thread = thread;
    }
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeInt(id);
		dest.writeParcelable(deadline, flags);
		dest.writeParcelable(created_at, flags);
		dest.writeBooleanArray(new boolean[] {is_pr_ch});
		dest.writeBooleanArray(new boolean[] {is_dl_ch});
		dest.writeBooleanArray(new boolean[] {is_cat_ch});
		dest.writeFloat(price);
		dest.writeString(body);
		dest.writeList(files);
		dest.writeParcelable(thread, flags);
		
	}
	private void readFromParcel(Parcel in) {
		boolean[] myBooleanArr = new boolean[2];
		
		id = in.readInt();
		deadline = in.readParcelable(DateTime.class.getClassLoader());
		created_at = in.readParcelable(DateTime.class.getClassLoader());
		is_pr_ch = myBooleanArr[0];
		is_dl_ch = myBooleanArr[1];
		is_cat_ch = myBooleanArr[2];
		price = in.readFloat();
		body = in.readString();
		files = in.readArrayList(File.class.getClassLoader());
		thread = in.readParcelable(Threads.class.getClassLoader());
		
		
	}
	public static final Parcelable.Creator CREATOR =
	    	new Parcelable.Creator() {
	            public Message createFromParcel(Parcel in) {
	                return new Message(in);
	            }
	 
	            public Message[] newArray(int size) {
	                return new Message[size];
	            }
	        };

	
	

	
	@Override
	public String toString()
	{		
	return "{title="+deadline.toString() +" "+
	"id="+id+ " " + "body = "+body + " "+"thread="+thread+"}";	
	}

}
