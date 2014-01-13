package com.datamodel;

import com.google.gson.annotations.SerializedName;

public class Captcha {
	@SerializedName("id")
	String id;
	@SerializedName("image")
	String image;
	String word;
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImage() {
		return image;
	}
	@Override
	public String toString() {
		return "id:" + id + ", image:" + image + ", word:" + word
				+ "}";
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Captcha(String id, String image) {
		super();
		this.id = id;
		this.image = image;
	}
	public Captcha() {
		// TODO Auto-generated constructor stub
	}
}
