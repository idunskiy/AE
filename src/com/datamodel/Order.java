package com.datamodel;

import java.util.ArrayList;
import java.util.List;

public class Order {
	List<Category> categories;
	List<ProcessStatus> process_statuses;
	Customer customer;
	List<Subject> subjects;
	List<Level> levels;
	
	public Order()
	{
		categories = new ArrayList<Category>();
		process_statuses = new ArrayList<ProcessStatus>();
		customer = new Customer();
		subjects = new ArrayList<Subject>();
		levels = new ArrayList<Level>();
	}
	
	public Order getOrder()
	{
		return null;
		
	}

	
	
	
	
	
}
