package com.arrayadapters;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ViewSwitcher;

import com.assignmentexpert.R;
import com.customitems.CustomTextView;
import com.datamodel.Order;
import com.datamodel.ProductAssignment;
import com.datamodel.ProductWriting;
import com.library.singletones.OrdersViewMapSingletone;
/**  *	adapter for orders */
public class OrderAdapter extends ArrayAdapter<Order>{
		View view;
		 OrderHolder holder = null;
		 int color = 0;
	    
	    Context context; 
	    int layoutResourceId;    
	    public static int rowHeight = 0;
	    List<Order> orders = null;
	    Map<Order, View> orderViewMap = OrdersViewMapSingletone.getInstance().getOrderViewMap();
		private ViewSwitcher switcher;
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
	        view = row;
	        if(row == null)
	        {
	        	LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            row = inflater.inflate(R.layout.dash_alt_item, parent, false);
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
	        try{
	        holder.orderId.setText(Integer.toString(order.getOrderid()));
	        holder.orderStatus.setText(order.getProcess_status().getProccessStatusTitle());
	        
	        holder.orderTitle.setText(order.getTitle());
//	        if (order.getProduct().getProductType().equals("assignment"))
//	        	holder.orderTitle.setText(((ProductAssignment)order.getProduct().getProduct()).getAssignTitle());
//	        else if (order.getProduct().getProductType().equals("writing"))
//	        	holder.orderTitle.setText(((ProductWriting)order.getProduct().getProduct()).getEssayTitle());
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        	
	        }
	        
	        
	        if (order.getPrice()==0)
	        {
	        	 holder.orderPrice.setText("N/A");
	        	 holder.orderPrice.setTextColor(Color.GRAY);
	        }
	        else
	        {
	        	holder.orderPrice.setText("$" + Integer.toString(order.getPrice()));
	        	if (order.getProcess_status().getProccessStatusId()==9)
	        		holder.orderPrice.setTextColor(Color.GRAY);
	        	else 
	        		holder.orderPrice.setTextColor(Color.rgb(77, 164, 0));
	        }
	        setStatusColor(order);
	       orderViewMap.put(order,row);
	        return row;
	    }
	    /**  *		order status color setting		     * */
	    public void setStatusColor(Order order)
	    {
	    	  if (!order.getIsActive().equalsIgnoreCase("active"))
		         {
		        	order.getProcess_status().setProccessStatusId(9);
		        	order.getProcess_status().setProccessStatusTitle("Inactive");
		        	holder.orderPrice.setTextColor(Color.GRAY);
		         }
	    	  holder.orderStatus.setTextColor(Color.rgb(255, 69, 0));
      		ViewGroup row = (ViewGroup) holder.orderPrice.getParent();
      		switcher = (ViewSwitcher) row.findViewById(R.id.profileSwitcher);
      		 if (order.getProcess_status().getProccessStatusId()==10)
	        	{
      			 holder.orderId.setVisibility(View.INVISIBLE);
	        		switcher.setDisplayedChild(1);
	        	}
      		 else 
      		 {
      			holder.orderId.setVisibility(View.VISIBLE);
      			switcher.setDisplayedChild(0);
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
      		 }
	    	  	
		        holder.orderStatus.setPadding(0, 2, 0, 0);
	    }
	    /**  *	order status color getting	     */
	    public int getStatusColor(Order order)
	    {
	    	if (order.getProcess_status().getProccessStatusId() == 10)
	    		color= (Color.rgb(255, 69, 0));
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
	    /**  * method for getting View of chosen order	   */
	    public View getMapedView(Order order)
	    {
	    	return this.orderViewMap.get(order);
	    }
	    /**  *	 class - holder*/
	    static class OrderHolder
	    {
	    	CustomTextView orderId;
	    	CustomTextView orderTitle;
	    	CustomTextView orderStatus;
	    	CustomTextView orderPrice;
	    }
	}

