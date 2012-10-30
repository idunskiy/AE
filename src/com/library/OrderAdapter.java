package com.library;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.assignmentexpert.R;
import com.datamodel.Order;

public class OrderAdapter extends ArrayAdapter<Order>{
	

	    Context context; 
	    int layoutResourceId;    
	    List<Order> orders = null;
	    private Activity activity;
	    public OrderAdapter(Context context,  int layoutResourceId,List<Order> orders) {
	        super(context, layoutResourceId, orders);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.orders = orders;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        OrderHolder holder = null;
	        
	        if(row == null)
	        {

	            
	        	LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            row = inflater.inflate(R.layout.dash_alt_item, parent, false);
	            row.setBackgroundColor(Color.WHITE);
	          
	            holder = new OrderHolder();
	            holder.orderId = (TextView)row.findViewById(R.id.altOrderId);
	            holder.orderTitle = (TextView)row.findViewById(R.id.altOrderTitle);
	            holder.orderStatus = (TextView)row.findViewById(R.id.altOrderStatus);
	            holder.orderPrice = (TextView)row.findViewById(R.id.altOrderPrice);
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (OrderHolder)row.getTag();
	        }
	        
	        Order order = orders.get(position);
	        holder.orderId.setText(Integer.toString(order.getOrderid()));
	        holder.orderTitle.setText(order.getTitle());
	        holder.orderStatus.setText(order.getProcess_status().getProccessStatusTitle());
	        holder.orderPrice.setText(Float.toString(order.getPrice()));


	        return row;
	    }
 	    
	    static class OrderHolder
	    {
	    	TextView orderId;
	    	TextView orderTitle;
	    	TextView orderStatus;
	        TextView orderPrice;
	    }
	}

