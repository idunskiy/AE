package com.assignmentexpert;

import java.io.File;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.datamodel.Message;
import com.datamodel.Order;
import com.library.UserFunctions;

public class NewMessageActivity extends Activity{
	private ViewPager pager;
	private Context cxt;
	//private CharSequence[] pages = {"stuff", "more stuff", "other stuff"};
	
	private Intent intent;
	List<Message> messagesList;
	List<Order> ordersFromService;	
	Button btnClose;
	TextView interId;
	TextView interMess;
	TextView textDate;
	private Button btnInfo;
	View page;
	private TextView emptyList;
	private Button btnPay;
	private Button btnInfoOrder;
	int lastOne;
	private Button btnAddFilesMessage;
	private Button btnCancel;
	private Button btnSendMessage;
	private EditText editMessage;
	private ProgressDialog progDailog;
	private TextView informationView;
	TextView tv[];
	 HorizontalScrollView scroll;
	 LinearLayout layout ;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
	    super.onCreate(savedInstanceState);
	    this.setContentView(R.layout.new_message);
	    
	    cxt = this;
	    scroll = (HorizontalScrollView) findViewById(R.id.scrollMessageFiles);
	    pager = (ViewPager) findViewById(R.id.conpageslider);
	     
//	    for (Message a: DashboardActivityAlt.messagesExport)
//	    {
//	    	Log.i("messages",a.toString());
//	    }
	  
	    TextView deadline = (TextView)findViewById(R.id.deadlineLabel);
	    TextView priceLabel = (TextView)findViewById(R.id.priceLabel);
	    LayoutInflater inflater = LayoutInflater.from(cxt);
	    	   
	    btnClose = (Button) findViewById(R.id.btnClose);
	    btnInfo = (Button) findViewById(R.id.btnInfoOrder);
	    btnPay  = (Button) findViewById(R.id.btnPay);
	    btnInfoOrder = (Button) findViewById(R.id.btnInfoOrder);
	    btnAddFilesMessage = (Button) findViewById(R.id.btnAddFilesMessage);
	    btnCancel =(Button) findViewById(R.id.btnCancelNewMessage);
	    btnSendMessage =(Button) findViewById(R.id.btnSendMessage);
	    editMessage =(EditText) findViewById(R.id.editMessage);
	    layout  = (LinearLayout) findViewById(R.id.panelMessageFiles);
	   
	     btnInfoOrder.setText("info"+"\r\n"+ Integer.toString(DashboardActivityAlt.listItem.getOrderid()));
	     page = inflater.inflate(R.layout.interactions_item, null);
	     interId = (TextView) page.findViewById(R.id.interactionId);
	     interMess = (TextView) page.findViewById(R.id.interactionMessage);
	     textDate = (TextView) page.findViewById(R.id.interactionDate);
	     emptyList = (TextView) findViewById(R.id.emptyResult);
	     addFiles();
	  
	    btnClose.setOnClickListener(new View.OnClickListener() {
	    	   
	           public void onClick(View view) {
	               Intent i = new Intent(getApplicationContext(),
	                       DashboardActivityAlt.class);
	               startActivity(i);
	               
	           }
	       });
	    btnInfo.setOnClickListener(new View.OnClickListener() {
	    	   
	           public void onClick(View view) {
	               Intent i = new Intent(getApplicationContext(),
	                       OrderInfoActivityAA.class);
	               startActivity(i);
	               
	           }
	       });
	    btnAddFilesMessage.setOnClickListener(new View.OnClickListener() {
	    	   
	           public void onClick(View view) {
	               Intent i = new Intent(getApplicationContext(),
	                       FileManagerActivity.class); 
	               Bundle mBundle = new Bundle();
	                mBundle.putString("FileManager", "NewMessage");
	                i.putExtras(mBundle);
	                startActivity(i);
	               
	           }
	       });
	    btnCancel.setOnClickListener(new View.OnClickListener() {
	    	   
	           public void onClick(View view) {
	               Intent i = new Intent(getApplicationContext(),
	                       InteractionsActivityViewPager.class);
	               startActivity(i);
	           }
	       });
	    
	    btnSendMessage.setOnClickListener(new View.OnClickListener() {
	    	   
	           public void onClick(View view) {
	              new SendMessageTask().execute();
	           }
	       });
	    
		
	}
	
	OnLongClickListener onclicklistener = new OnLongClickListener() {

	
		public boolean onLongClick(final View arg0) {
			final CharSequence[] items = {"Open", "Delete", "Details"};
			
			
			final AlertDialog.Builder builder = new AlertDialog.Builder(NewMessageActivity.this);
			
		    
		    builder.setTitle(((TextView)arg0).getText().toString());
			builder.setItems(items, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	if (item == 0)
			    	{
			    		
			    	}
			    	 else if (item == 1)
			    		{  
			    		   FileManagerActivity.getFinalAttachFiles().remove(item);
			    		   ((LinearLayout)arg0.getParent()).removeView(arg0);
			    		   layout.invalidate();
			    		}
			    	else if (item == 2)
			    	{
			    		
			    	}
			    	
			    	 
			    }
			});
			AlertDialog alert = builder.create();
			alert.show();
			return false;
		}
	};
	public void addFiles()
	 {

	    if(!FileManagerActivity.getFinalAttachFiles().isEmpty())
	    {
	    	TextView tv[] = new TextView[FileManagerActivity.getFinalAttachFiles().size()];
		         for (int i = 0; i< FileManagerActivity.getFinalAttachFiles().size();i++)
		          {
		            
		        	 
		            	View line = new View(this);
		                line.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
		                line.setBackgroundColor(0xAA345556);
		                tv[i] = new TextView(this);
		                tv[i].setId(i);
		                tv[i].setTextColor(Color.BLACK);
		                tv[i].setTextSize(12);
		                tv[i].setCompoundDrawablesWithIntrinsicBounds(
		                        0, R.drawable.file_icon, 0, 0);
		                tv[i].setText(FileManagerActivity.getFinalAttachFiles().get(i).getName().toString());
		                tv[i].setOnLongClickListener(onclicklistener);
		                layout.addView(tv[i], 0);
		                layout.addView(line, 1);
		                
	
		          }
		       
	      }
	  
	    

	}
    private class SendMessageTask extends AsyncTask<Void, Void, JSONObject > {
    
    	JSONObject response ;
		protected void onPreExecute() {
        	progDailog = ProgressDialog.show(NewMessageActivity.this,"Please wait...", "Sending message to the server ...", true);
        }

        protected JSONObject doInBackground(Void... args) {
        
       	try 
       	{
       		UserFunctions userFunc = new UserFunctions();
       		response = userFunc.sendMessage(Integer.toString(DashboardActivityAlt.listItem.getCategory().getCategoryId()), 
       				DashboardActivityAlt.listItem.getDeadline().toString(),
       				Float.toString(DashboardActivityAlt.listItem.getPrice()), editMessage.getText().toString(), 
       				Integer.toString(DashboardActivityAlt.listItem.getOrderid()), FileManagerActivity.getFinalAttachFiles());
			
			Log.i("send message response",response.toString());
		} catch (Exception e) {
			
			e.printStackTrace();
		}
       		return response;
       		
        }

        protected void onPostExecute(JSONObject  forPrint) {
//        	Toast mToast =  Toast.makeText(getApplicationContext(),"qwerty"+forPrint.toString(), Toast.LENGTH_LONG);
// 	  	      mToast.show();
 	  	   progDailog.dismiss();
 	  	   Intent i = new Intent(getApplicationContext(),
                InteractionsActivityViewPager.class);
 	  	   startActivity(i);
            // Pass the result data back to the main activity
        	
//			if (!res )
//			{
//	        	DashboardActivityAlt.this.listView1 = (ListView)findViewById(R.id.altOrderslist);
//	        	OrderAdapter adapter = new OrderAdapter(DashboardActivityAlt.this,
//	        			R.layout.dash_alt_item, forPrint);
//
//
//	        	listView1.setAdapter(adapter);
//	          
//	            adapter.notifyDataSetChanged();
//            }
//			else 
//			{
//				  this.cancel(true);
//				  Toast mToast =  Toast.makeText(getApplicationContext(), "Нету больше!!!", Toast.LENGTH_LONG);
//	   	  	      mToast.show();
//				
//			}
//
//            	DashboardActivityAlt.pd.dismiss();
  
            
        }

		
   }

 



}
