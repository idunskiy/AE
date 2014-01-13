package com.datamodel;

import java.io.File;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/** *  класс, описывающий класс файлов. Используется сериализация.*/
public class Files implements Parcelable{
	/** * id файла*/
	@SerializedName("id")
	int id;
	/** * extension файла*/
	@SerializedName("ext")
	String extension;
	/** * полный путь файла*/
	@SerializedName("href")
	String full_path;
	
	@SerializedName("md5")
	String md5_checksum;
	/** * размер файла*/
	@SerializedName("size")
	int size;
	/** * имя файла*/
	@SerializedName("name")
	String name;
	File file;
	public Files()
	{}
	public Files(int id) {
		super();
		this.id = id;
	}
	/** * основной конструктор.*/
	public Files (int id, String title , String name, String full_path , String extension , int size, File file, String md5_checksum )
    {
        this.id = id;
        this.name = name;
        this.full_path = full_path;
        this.extension = extension;
        this.size = size;
        this.md5_checksum = md5_checksum;
        this.file  = file;
    }
	public Files(Parcel in) {
		readFromParcel(in);// TODO Auto-generated constructor stub
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
	
	public File getFile()
	{
		return this.file;
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
	public void  setFile(File file)
	{
		this.file = file;
	}
	
	@Override
	public String toString()
	{		
	return "{ "+"id="+id+ " " + "name"+ name + " "+ "full_path" + full_path + " "  + " " + "extension" + extension +" "+ "size " + size + "}";	
	}
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeInt(id);
		dest.writeInt(size);
		dest.writeString(extension);
		dest.writeString(full_path);
		dest.writeString(md5_checksum);
		dest.writeString(name);
		
	}
	private void readFromParcel(Parcel in) {
		id = in.readInt();
		name = in.readString();
		full_path = in.readString();
		extension = in.readString();
		size = in.readInt();
		md5_checksum = in.readString();
		
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Files))
			return false;
		Files other = (Files) obj;
		if (id != other.id)
			return false;
		return true;
	}
	public static final Parcelable.Creator<Files> CREATOR =
	    	new Parcelable.Creator<Files>() {
	            public Files createFromParcel(Parcel in) {
	                return new Files(in);
	            }
	 
	            public Files[] newArray(int size) {
	                return new Files[size];
	            }
	        };
}
