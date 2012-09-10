package com.datamodel;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


public class File {
	@SerializedName("id")
	int id;
	@SerializedName("extension")
	String extension;
	@SerializedName("full_path")
	String full_path;
	@SerializedName("md5_checksum")
	String md5_checksum;
	@SerializedName("mime_path")
	String mime_path;
	@SerializedName("size")
	int size;
	@SerializedName("name")
	String name;
	// need for ORMlite
	public File()
	{}
	public File (int id, String title , String name, String full_path , String mime_path, String extension , int size)
    {
        this.id = id;
        this.name = name;
        this.full_path = full_path;
        this.mime_path = mime_path;
        this.extension = extension;
        this.size = size;
        this.md5_checksum = md5_checksum;
    }
	public int getFileId() 
	{
		
		 return this.id;
    }
		 
	public String getFileName() 
	{
		 return this.name;
    }
	
		
	public String getFileFullPath() 
	{
		 return this.full_path;
    }
	
	public String getFileMimePath() 
	{
		 return this.mime_path;
    }
	
	public String getFileExtension() 
	{
		 return this.extension;
    }
	
	public int getFileSize() 
	{
		 return this.size;
    }
	
	public String getMd5() 
	{
		 return this.md5_checksum;
    }
	
	
	public void setFileId(int id)
	{
		 this.id = id;
	}

		
	public void setFileName(String name)
	{
		 this.name = name;
	}
	
	public void setFileFullPath(String full_path)
	{
		 this.full_path = full_path;
	}
	
	public void setFileMimePath(String mime_path)
	{
		 this.mime_path = mime_path;
	}
	
	public void setFileExtension(String extension)
	{
		 this.extension = extension;
	}
	
	public void setFileSize(int size)
	{
		 this.size = size;
	}
	
	public void setMd5(String md5)
	{
		 this.md5_checksum = md5;
	}
	
	@Override
	public String toString()
	{		
	return "{ "+"id="+id+ " " + "name"+ name + " "+ "full_path" + full_path + " " + "mime path" + mime_path + " " + "extension" + extension +" "+ "size " + size + "}";	
	}
}
