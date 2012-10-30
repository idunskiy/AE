package com.datamodel;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "categories")
public class Category  implements Parcelable{
	public final static String CATEGORY_TITLE_FIELD_NAME = "title";
	public static final String SUBJECT_ID = "subject_id";
	@SerializedName("id")
	@DatabaseField(id = true, generatedId = false)
	int id;
	@SerializedName("title")
	@DatabaseField(dataType = DataType.STRING, columnName = CATEGORY_TITLE_FIELD_NAME)
	String title;
	@SerializedName("subject")
	@DatabaseField(columnName = SUBJECT_ID,foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
	Subject subject;

	// need for ORMlite
	public Category() {
	}

	public Category(int id, String title, Subject subject) {
		this.id = id;
		this.title = title;
		this.subject = subject;
	}

	public int getCategoryId() {

		return this.id;
	}

	public String getCategoryTitle() {
		return this.title;
	}
//
	public Subject getCategorySubject() {
		return this.subject;
	}

	public void setCategoryId(int id) {
		this.id = id;
	}

	public void setCategoryTitle(String title) {
		this.title = title;
	}

	public void setCategorySubject(Subject subject) {
		this.subject = subject;
	}

	@Override
	public String toString() {
		return title;//"{title=" + title + " " + "id=" + id + "subject=" + subject+ "}";
	}
	
	public String display() {
		return title;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeParcelable(subject, flags);
		
		
	}
	 private Category(Parcel in) {
		 readFromParcel(in);
     }

	public Category(String string ,int parseInt) {
		this.id = parseInt;
		this.title = string;
	}

	private void readFromParcel(Parcel in) {
		
		id = in.readInt();
		title = in.readString();
		subject = in.readParcelable(Subject.class.getClassLoader());
		
	}
	public static final Parcelable.Creator CREATOR =
	    	new Parcelable.Creator() {
	            public Category createFromParcel(Parcel in) {
	                return new Category(in);
	            }
	 
	            public Category[] newArray(int size) {
	                return new Category[size];
	            }
	        };

}
