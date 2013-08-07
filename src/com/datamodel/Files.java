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
	@SerializedName("extension")
	String extension;
	/** * полный путь файла*/
	@SerializedName("full_path")
	String full_path;
	
	@SerializedName("md5_checksum")
	String md5_checksum;
	@SerializedName("mime_path")
	String mime_path;
	/** * размер файла*/
	@SerializedName("size")
	int size;
	/** * имя файла*/
	@SerializedName("name")
	String name;
	File file;
	public Files()
	{}
	/** * основной конструктор.*/
	public Files (int id, String title , String name, String full_path , String mime_path, String extension , int size, File file, String md5_checksum )
    {
        this.id = id;
        this.name = name;
        this.full_path = full_path;
        this.mime_path = mime_path;
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
	public void  setFile(File file)
	{
		this.file = file;
	}
	
	@Override
	public String toString()
	{		
	return "{ "+"id="+id+ " " + "name"+ name + " "+ "full_path" + full_path + " " + "mime path" + mime_path + " " + "extension" + extension +" "+ "size " + size + "}";	
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
		dest.writeString(mime_path);
		dest.writeString(name);
		
	}
	private void readFromParcel(Parcel in) {
		id = in.readInt();
		name = in.readString();
		full_path = in.readString();
		mime_path = in.readString();
		extension = in.readString();
		size = in.readInt();
		md5_checksum = in.readString();
		
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
