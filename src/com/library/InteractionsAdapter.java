package com.library;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.assignmentexpert.R;
import com.customitems.CustomTextView;
import com.datamodel.Messages;


public class InteractionsAdapter extends  ArrayAdapter<Messages>{


    Context context; 
    int layoutResourceId;
    int position;
    //List<Message> messages = null;
    List<Messages> messages;
    LinearLayout linearLayout;
    public InteractionsAdapter(Context context,  int layoutResourceId,List<Messages> messages, int position) {
        super(context, layoutResourceId, messages);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.messages = messages;
        this.position  = position;
    }
//    @Override
//    public int getCount() {
//        return 1;
//    }
    public void setPosition(int position)
    {
    	this.position = position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        InteractionHolder holder = null;
        position = this.position;
        if(row == null)
        {

            //LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        	//LayoutInflater inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.interactions_item, parent, false);
          
            holder = new InteractionHolder();
            
            
            holder.interactionId= (TextView)row.findViewById(R.id.interactionId);
            holder.interactionAbove = (TextView)row.findViewById(R.id.interactionAbove);
            holder.interactionBelow = (TextView)row.findViewById(R.id.interactionBelow);
            holder.interactionMessage = (TextView)row.findViewById(R.id.interactionMessage);
            holder.interactionDate = (TextView)row.findViewById(R.id.interactionDate);
            linearLayout = (LinearLayout)row.findViewById(R.id.interactionsCursor);
//            holder.prev =  (TextView)row.findViewById(R.id.cursorMess1);
//            holder.curr =  (TextView)row.findViewById(R.id.cursorMess2);
//            holder.next =  (TextView)row.findViewById(R.id.cursorMess3);
            row.setTag(holder);
        }
        else
        {
            holder = (InteractionHolder)row.getTag();
        }
        
            Messages message = messages.get(position);
        	Log.i("position",Integer.toString(position));
        	
	        holder.interactionId.setText(Integer.toString(message.getMessageId()));
	        holder.interactionAbove.setText(Integer.toString(position));

	        holder.interactionMessage.setText(message.getMessageBody());
	        holder.interactionDate.setText(message.getMessageDate().toString());
	    	
	        CustomTextView mess = new CustomTextView(context);
	        mess.setText("huy");
	        mess.setTextSize(20);
	        mess.setLayoutParams(new LayoutParams(
	                LayoutParams.WRAP_CONTENT,
	                LayoutParams.WRAP_CONTENT));
	        mess.setGravity(Gravity.CENTER);
	        linearLayout.addView(mess);
//	        holder.prev.setText(Integer.toString(position-1));
//        	holder.curr.setText(Integer.toString(position));
//        	holder.next.setText(Integer.toString(position+1));
        	
//        	  holder.prev.setText("hyu");
//          	holder.curr.setText("qwe");
//          	holder.next.setText("adsa");
	        	
	        
	        
        	Log.i("end size",Integer.toString(position));
       

        return row;
    }
	    
    static class InteractionHolder
    {
    	TextView interactionId;
    	TextView interactionDate;
    	TextView interactionAbove;
        TextView interactionBelow;
        TextView interactionMessage;
        
//        TextView prev;
//        TextView curr;
//        TextView next;
        
    }
    
}
