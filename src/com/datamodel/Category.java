package com.datamodel;


import android.os.Parcel;
import android.os.Parcelable;

import com.fragments.InteractionFragment;
import com.fragments.NewMessageFragment;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/** *  ласс, описывающий обьект категории. »спользуетс€ сериализаци€ и внесение обьекта в базу данных */
@DatabaseTable(tableName = "categories")
public class Category  implements Parcelable{
	public final static String CATEGORY_TITLE_FIELD_NAME = "title";
	public static final String SUBJECT_ID = "subject_id";
	/** * id категории. ѕоле сериализуетс€(id) и вноситьс€ в базу */
	@SerializedName("id")
	@DatabaseField(id = true, generatedId = false)
	int id;
	/** * название категории. ѕоле сериализуетс€(title) и вноситьс€ в базу */
	@SerializedName("title")
	@DatabaseField(dataType = DataType.STRING, columnName = CATEGORY_TITLE_FIELD_NAME)
	String title;
	/** * тема дл€ текущей категории.  аждой категории соответствует тема. ќтношение - много к одному.  */
	@SerializedName("subject")
	@DatabaseField(columnName = SUBJECT_ID,foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
	Subject subject;

	/** * конструктор нужен дл€ использовани€ ORMLite */
	public Category() {
	}
	/** * основной контсруктор */
	public Category(int id, String title, Subject subject) {
		this.id = id;
		this.title = title;
		this.subject = subject;
	}
	/** * id  getter */
	public int getCategoryId() {

		return this.id;
	}
	/** * title getter */
	public String getCategoryTitle() {
		return this.title;
	}
	/** * Subject  getter дл€ данной категории */
	public Subject getCategorySubject() {
		return this.subject;
	}
	/** * id setter */
	public void setCategoryId(int id) {
		this.id = id;
	}
	/** * title setter */
	public void setCategoryTitle(String title) {
		this.title = title;
	}
	/** * Subject setter */
	public void setCategorySubject(Subject subject) {
		this.subject = subject;
	}

	@Override
	public String toString() {
		return "{title=" + title + " " + "id=" + id + "subject=" + subject.toString()+ "}";
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
	public static final Parcelable.Creator <Category>CREATOR =
	    	new Parcelable.Creator<Category>() {
	            public Category createFromParcel(Parcel in) {
	                return new Category(in);
	            }
	 
	            public Category[] newArray(int size) {
	                return new Category[size];
	            }
	        };

}
