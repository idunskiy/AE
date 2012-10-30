package com.assignmentexpert;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.datamodel.Message;
import com.datamodel.Order;
import com.library.ServiceMessages;

public class InteractionsActivityViewPager extends Activity{
	private ViewPager pager;
	private Context cxt;
	//private CharSequence[] pages = {"stuff", "more stuff", "other stuff"};
	private PageSlider ps;
	private Intent intent;
	List<Message> messagesList;
	List<Order> ordersFromService;	
	Button btnClose;
	TextView interId;
	TextView interMess;
	TextView textDate;
	private Button btnInfo;
	View page;
	static int serviceCount= 0;
	static int serviceStopCount= 0;
	static int serviceUpdateCount= 0;
	private TextView emptyList;
	private Button btnPay;
	private Button btnInfoOrder;
	List <Message> messageList ;
	int lastOne;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
	    super.onCreate(savedInstanceState);
	    this.setContentView(R.layout.interactions_view_pager);
	    intent = new Intent(this, ServiceMessages.class);
	    cxt = this;
	    ps = new PageSlider();
	    pager = (ViewPager) findViewById(R.id.conpageslider);
	     
	    TextView deadline = (TextView)findViewById(R.id.deadlineLabel);
	    TextView priceLabel = (TextView)findViewById(R.id.priceLabel);
	    LayoutInflater inflater = LayoutInflater.from(cxt);
	    	   
	    btnClose = (Button) findViewById(R.id.btnClose);
	    btnInfo = (Button) findViewById(R.id.btnInfoOrder);
	    btnPay  = (Button) findViewById(R.id.btnPay);
	    btnInfoOrder = (Button) findViewById(R.id.btnInfoOrder);
	    btnInfoOrder.setText("info"+"\r\n"+ Integer.toString(DashboardActivityAlt.listItem.getOrderid()));
	     page = inflater.inflate(R.layout.interactions_item, null);
	     interId = (TextView) page.findViewById(R.id.interactionId);
	     interMess = (TextView) page.findViewById(R.id.interactionMessage);
	     textDate = (TextView) page.findViewById(R.id.interactionDate);
	     emptyList = (TextView) findViewById(R.id.emptyResult);
   	  	messageList = DashboardActivityAlt.listItem.getCusThread().getMessages();
	    if (DashboardActivityAlt.messagesExport.isEmpty())
	    {
	    	  deadline.setText(DashboardActivityAlt.listItem.getDeadline().toString());
	    	  priceLabel.setText("N/A");
	    	  emptyList.setVisibility(View.VISIBLE);
	    	  
	    }
	    else
	    {   
	    	lastOne =DashboardActivityAlt.messagesExport.size()-1;
	    	priceLabel.setTextColor(Color.GREEN);
		    priceLabel.setText(Float.toString(reverse(DashboardActivityAlt.messagesExport).get(lastOne).getPrice()));
		    deadline.setText((reverse(DashboardActivityAlt.messagesExport).get(lastOne).getMessagedeadline().toString()));
		    btnPay.setBackgroundColor(Color.GREEN);
		    pager.setAdapter(ps);   
	    } 
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
	    
	   
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option, menu);
        return true;
    }

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.optReply:   
        	{
        		
        		Intent i = new Intent(getApplicationContext(),
	                       NewMessageActivity.class);
	               startActivity(i);
        		
        	}
        	
                            
        case R.id.optInAct:    
            {
        	    
            	 
        	
        	}
            break;
                            
        }
    
    
    return true;
    }
 

	public class PageSlider extends PagerAdapter{

	    @Override
	    public int getCount() {
	        return DashboardActivityAlt.messagesExport.size();
	    }

	    @Override
	    public Object instantiateItem(View collection, int position) {
	    	
	    	LayoutInflater inflater = LayoutInflater.from(cxt);
		     page = inflater.inflate(R.layout.interactions_item, null);
		     interId = (TextView) page.findViewById(R.id.interactionId);
	    	 interMess = (TextView) page.findViewById(R.id.interactionMessage);
	    	 textDate = (TextView) page.findViewById(R.id.interactionDate);
	    	 if (!DashboardActivityAlt.messagesExport.isEmpty())
		     {	
	    		 
	    		 interId.setText("Interaction "+Integer.toString(reverse(DashboardActivityAlt.messagesExport).get(position).getMessageId()));
			     interMess.setText(reverse(DashboardActivityAlt.messagesExport).get(position).getMessageBody());
			     textDate.setText(reverse(DashboardActivityAlt.messagesExport).get(position).getMessageDate().toString());
	    		 
		     }	
	    	 else if(DashboardActivityAlt.messagesExport.isEmpty())
	    	 {
	    		  Log.i("list is empty", "interactions");
	    		  
		    	  
		    	  //interMess.setTextSize(15);
		    	  
		    	  interMess.setTextColor(Color.BLACK);
		    	  interMess.setText("Currently no messages");
	    	}
	    	 
	    	
	        ((ViewPager) collection).addView(page);
	        return page;
	       
	        
	    }

	    @Override
	    public void destroyItem(View collection, int position, Object view) {
	        ((ViewPager) collection).removeView((View) view);
	    }

	    @Override
	    public boolean isViewFromObject(View view, Object object) {
	        return view==((View)object);
	    }

	}
	 private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	        	updateUI(intent);       
	        }
	    };
	 @Override
		public void onResume() {
			super.onResume();		
			Log.i("in interactons count start", Integer.toString(serviceCount));
			serviceCount ++;
		    startService(intent);
			registerReceiver(broadcastReceiver, new IntentFilter(ServiceMessages.MESSAGES_IMPORT));
		}

		@Override
		public void onPause() {
			super.onPause();
			unregisterReceiver(broadcastReceiver);
			Log.i("in interactons count stop service", Integer.toString(serviceStopCount));
			
			stopService(intent); 		
		}	

	    private void updateUI(Intent intent) {
	    	 pager.setAdapter(ps);   
	    }
	    public ArrayList<Message> reverse( List<Message> orig )
	    {
	       ArrayList<Message> reversed = new ArrayList<Message>() ;
	       for ( int i = orig.size() - 1 ; i >= 0 ; i-- )
	       {
	           Message obj = orig.get( i ) ;
	           reversed.add( obj ) ;
	       }
	       return reversed ;
	    }
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
		    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
		    	  Intent i = new Intent(getApplicationContext(),
	                      DashboardActivityAlt.class);
	              startActivity(i);
		    }
		    return super.onKeyDown(keyCode, event);
		}
}
