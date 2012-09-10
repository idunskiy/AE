package com.datamodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Message {
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
	List<File> files;
	
	// need for ORMlite
	public Message()
	{}
	public Message (int id, DateTime created_at , String body, DateTime deadline, float price, boolean is_cat_ch, boolean is_dl_ch, boolean is_pr_ch , ArrayList<File> files)
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
	
	public List<File> getFiles() 
	{
		 return this.files;
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
	
	public void setFiles(List<File> files)
	{
		 this.files = files;
	}
	
	
//	@Override
//	public String toString()
//	{		
//	return "{title="+title +" "+"id="+id+ " " + "name"+ name + " "+ "full_path" + full_path + " " + "mime path" + mime_path + " " + "extension" + extension +" "+ "size " + size + "}";	
//	}

}
