package com.datamodel;

import java.util.ArrayList;
import java.util.List;

public class Categories {
	 ArrayList<Category> categories;
	 
	 public Categories() {
	 categories = new ArrayList<Category>();
	 }
	 
	  public ArrayList<Category> getCategories() { 
          return this.categories; 
     }

     public void setCategories(ArrayList<Category> categories) { 
          this.categories = (ArrayList<Category>)categories; 
     } 
     
     @Override
     public String toString()
     {
    	 return this.categories.toString();
    	 
     }
}
