package com.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.assignmentexpert.R;
import com.customitems.CustomTextView;
import com.datamodel.Order;
import com.datamodel.ProductAssignment;
import com.datamodel.ProductWriting;

public class OrderAdapter extends ArrayAdapter<Order>{
	

	    Context context; 
	    int layoutResourceId;    
	    public static int rowHeight = 0;
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
	            row.setBackgroundColor(Color.BLACK);
	            //row.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,30));
	            holder = new OrderHolder();
	            holder.orderId = (CustomTextView)row.findViewById(R.id.altOrderId);
	            holder.orderTitle = (CustomTextView)row.findViewById(R.id.altOrderTitle);
	            holder.orderStatus = (CustomTextView)row.findViewById(R.id.altOrderStatus);
	            holder.orderPrice = (CustomTextView)row.findViewById(R.id.altOrderPrice);
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (OrderHolder)row.getTag();
	        }
	        
	        Order order = orders.get(position);
	        holder.orderId.setText(Integer.toString(order.getOrderid()));
	        if (order.getProduct().getProductType().equals("assignment"))
	        	holder.orderTitle.setText(((ProductAssignment)order.getProduct().getProduct()).getAssignTitle());
	        else if (order.getProduct().getProductType().equals("writing"))
	        	holder.orderTitle.setText(((ProductWriting)order.getProduct().getProduct()).getEssayTitle());
	    	holder.orderTitle.setText(order.getTitle());
	        holder.orderStatus.setText(order.getProcess_status().getProccessStatusTitle());
	        if (order.getProcess_status().getProccessStatusId() == 8)
	        	holder.orderStatus.setTextColor(Color.rgb(161, 161, 161));
	        if (order.getProcess_status().getProccessStatusId() == 7)
	        	holder.orderStatus.setTextColor(Color.rgb(190, 0, 207));
	        if (order.getProcess_status().getProccessStatusId() == 6)
	        	holder.orderStatus.setTextColor(Color.rgb(98, 166, 0));
	        if (order.getProcess_status().getProccessStatusId() == 5)
	        	holder.orderStatus.setTextColor(Color.rgb(255, 126, 0));
	        if (order.getProcess_status().getProccessStatusId() == 4)
	        	holder.orderStatus.setTextColor(Color.rgb(78, 163, 245));
	        if (order.getProcess_status().getProccessStatusId() == 3)
	        	holder.orderStatus.setTextColor(Color.rgb(255, 210, 0));
	        if (order.getProcess_status().getProccessStatusId() == 2)
	        	holder.orderStatus.setTextColor(Color.rgb(98, 166, 0));
	        holder.orderStatus.setPadding(0, 8, 0, 0);
//	        if (order.getProcess_status().getProccessStatusId() == 1)
//	        	holder.orderStatus.setTextColor(Color.rgb(98, 166, 0));
	        holder.orderPrice.setText("$" + Float.toString(order.getPrice()));
//	        rowHeight = row.getHeight();
	        
	        return row;
	    }
 	    
	    static class OrderHolder
	    {
	    	CustomTextView orderId;
	    	CustomTextView orderTitle;
	    	CustomTextView orderStatus;
	    	CustomTextView orderPrice;
	    }
	}

