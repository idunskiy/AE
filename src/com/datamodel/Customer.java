package com.datamodel;

import com.google.gson.annotations.SerializedName;

/** * Класс, описывающий обьект клиента. Используется сериализация. */
public class Customer  {
	/** * номер телефона клиента */
	
	/** * Обьект класса User 
	 * @see User*/
	 @SerializedName("id")
	  int id;
	 @SerializedName("last_name")
	  String last_name;
	  /** * имя клиента*/
	 @SerializedName("created_at")
	  long created_at;
	  @SerializedName("first_name")
	  String first_name;
	  @SerializedName("username")
	  String username;
	  @SerializedName("email")
	  String email;
	  @SerializedName("phone")
		String phone;
	public Customer(int id, String last_name, long created_at,
			String first_name, String username, String email, String phone) {
		super();
		this.id = id;
		this.last_name = last_name;
		this.created_at = created_at;
		this.first_name = first_name;
		this.username = username;
		this.email = email;
		this.phone = phone;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
