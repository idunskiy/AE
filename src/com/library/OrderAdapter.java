package com.library;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.R;
import com.assignmentexpert.DashboardActivityAlt.Action;
import com.customitems.CustomTextView;
import com.datamodel.Order;
import com.datamodel.ProductAssignment;
import com.datamodel.ProductWriting;

public class OrderAdapter extends ArrayAdapter<Order>{
		View view;
		 OrderHolder holder = null;
		 int color = 0;
	 public static enum Action {
	        LR, // Left to Right
	        RL, // Right to Left
	        TB, // Top to bottom
	        BT, // Bottom to Top
	        None // when no action was detected
	    }
	 private Action mSwipeDetected = Action.None;
	 	public boolean swipeDetected() {
	        return mSwipeDetected != Action.None;
	    }
	    public Action getAction() {
	        return mSwipeDetected;
	    }
	    public void setAction(Action action) {
	    	mSwipeDetected = action;
	    }
	    
	    private static final int MIN_DISTANCE = 100;
		
	    private float downX, downY, upX, upY ,  stopX, stopY;

	    Context context; 
	    int layoutResourceId;    
	    public static int rowHeight = 0;
	    List<Order> orders = null;
	    List<View> views = new ArrayList<View>();
	    public OrderAdapter(Context context,  int layoutResourceId,List<Order> orders) {
	        super(context, layoutResourceId, orders);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.orders = orders;
	    }
	    @Override
	    public Order getItem(int arg0) {
	        return orders.get(arg0);
	    }

	    @Override
	    public long getItemId(int arg0) {
	        return arg0;
	    }
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	       
	        
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
	        
//	        if (!order.getIsActive())
//	         {
//	        	order.getProcess_status().setProccessStatusId(9);
//	        	order.getProcess_status().setProccessStatusTitle("Inactive");
//	         }
//	        if (order.getProcess_status().getProccessStatusId() == 9)
//	        	holder.orderStatus.setTextColor(Color.rgb(96, 96, 96));
//	        if (order.getProcess_status().getProccessStatusId() == 8)
//	        	holder.orderStatus.setTextColor(Color.rgb(161, 161, 161));
//	        if (order.getProcess_status().getProccessStatusId() == 7)
//	        	holder.orderStatus.setTextColor(Color.rgb(190, 0, 207));
//	        if (order.getProcess_status().getProccessStatusId() == 6)
//	        	holder.orderStatus.setTextColor(Color.rgb(98, 166, 0));
//	        if (order.getProcess_status().getProccessStatusId() == 5)
//	        	holder.orderStatus.setTextColor(Color.rgb(255, 126, 0));
//	        if (order.getProcess_status().getProccessStatusId() == 4)
//	        	holder.orderStatus.setTextColor(Color.rgb(78, 163, 245));
//	        if (order.getProcess_status().getProccessStatusId() == 3)
//	        	holder.orderStatus.setTextColor(Color.rgb(255, 210, 0));
//	        if (order.getProcess_status().getProccessStatusId() == 2)
//	        	holder.orderStatus.setTextColor(Color.rgb(98, 166, 0));
//	        holder.orderStatus.setPadding(0, 8, 0, 0);
	        
	        setStatusColor(order);
//	        if (order.getProcess_status().getProccessStatusId() == 1)
//	        	holder.orderStatus.setTextColor(Color.rgb(98, 166, 0));
	        if (order.getPrice()==0)
	        {
	        	 holder.orderPrice.setText("N/A");
	        	 holder.orderPrice.setTextColor(Color.GRAY);
	        }
	        else
	        {
	        	holder.orderPrice.setText("$" + Float.toString(order.getPrice()));
	       	 holder.orderPrice.setTextColor(Color.rgb(77, 164, 0));
	        }
//	        rowHeight = row.getHeight();
	       if (position<orders.size())
	        views.add(position, row);
	        return row;
	    }
	    public void setStatusColor(Order order)
	    {
	    	  if (!order.getIsActive())
		         {
		        	order.getProcess_status().setProccessStatusId(9);
		        	order.getProcess_status().setProccessStatusTitle("Inactive");
		         }
		        if (order.getProcess_status().getProccessStatusId() == 9)
		        	holder.orderStatus.setTextColor(Color.rgb(96, 96, 96));
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
	    }
	    public int getStatusColor(Order order)
	    {
	    	if (order.getProcess_status().getProccessStatusId() == 9)
	    		color= (Color.rgb(96, 96, 96));
	        if (order.getProcess_status().getProccessStatusId() == 8)
	        	color = (Color.rgb(161, 161, 161));
	        if (order.getProcess_status().getProccessStatusId() == 7)
	        	color = (Color.rgb(190, 0, 207));
	        if (order.getProcess_status().getProccessStatusId() == 6)
	        	color = (Color.rgb(98, 166, 0));
	        if (order.getProcess_status().getProccessStatusId() == 5)
	        	color = (Color.rgb(255, 126, 0));
	        if (order.getProcess_status().getProccessStatusId() == 4)
	        	color = (Color.rgb(78, 163, 245));
	        if (order.getProcess_status().getProccessStatusId() == 3)
	        	color = (Color.rgb(255, 210, 0));
	        if (order.getProcess_status().getProccessStatusId() == 2)
	        	color = (Color.rgb(98, 166, 0));
	    	return color;
	    }
	    
	    public View getView(int position)
	    {return this.views.get(position);}
 	    
	    static class OrderHolder
	    {
	    	CustomTextView orderId;
	    	CustomTextView orderTitle;
	    	CustomTextView orderStatus;
	    	CustomTextView orderPrice;
	    }
	}

