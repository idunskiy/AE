package com.assignmentexpert;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.datamodel.Messages;
import com.datamodel.Order;
import com.library.FrequentlyUsedMethods;
import com.library.ResultPayPalDelegate;
import com.library.ServiceMessages;
import com.library.UserFunctions;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;

public class InteractionsActivityViewPager extends Activity{
	private ViewPager pager;
	private Context cxt;
	private PageSlider ps;
	private Intent intent;
	List<Messages> messagesList;
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
	static boolean deleteFlag = false;
	private Button btnInfoOrder;
	List <Messages> messageList ;
	int lastOne;
	
	private static final int request = 1;
	protected static final Integer INITIALIZE_SUCCESS = 1;
	protected static final Integer INITIALIZE_FAILURE = 2;
	PayPal mPayPal;
	String resultTitle;
	String resultInfo;
	String resultExtra;
	private ProgressDialog progDailog;
	private RelativeLayout panelInteractions;
	FrequentlyUsedMethods faq;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
	    super.onCreate(savedInstanceState);
	    this.setContentView(R.layout.interactions_view_pager);
	    
	    intent = new Intent(this, ServiceMessages.class);
	    cxt = this;
	    ps = new PageSlider();
	    pager = (ViewPager) findViewById(R.id.conpageslider);
	     faq = new FrequentlyUsedMethods();
	     faq.setActivity(InteractionsActivityViewPager.this);
   		 faq.setContext(InteractionsActivityViewPager.this);
	    TextView deadline = (TextView)findViewById(R.id.deadlineLabel);
	    TextView priceLabel = (TextView)findViewById(R.id.priceLabel);
	    LayoutInflater inflater = LayoutInflater.from(cxt);
	    Log.i("order process status", DashboardActivityAlt.listItem.getProcess_status().getProccessStatusTitle());
	    btnClose = (Button) findViewById(R.id.btnClose);
	    btnInfo = (Button) findViewById(R.id.btnInfoOrder);
	    btnPay  = (Button) findViewById(R.id.btnPay);
	    btnInfoOrder = (Button) findViewById(R.id.btnInfoOrder);
	    panelInteractions =  ((RelativeLayout)findViewById(R.id.panelInteractions));
	    
	    Log.i("Interactions class","onCreate");
	    try
	    {
	    	
	    	btnInfoOrder.setText("info"+"\r\n"+ Integer.toString(DashboardActivityAlt.listItem.getOrderid()));
	    	
	    }
	    catch (NullPointerException e)
	    {
	    	e.printStackTrace();
	    }
	     page = inflater.inflate(R.layout.interactions_item, null);
	     interId = (TextView) page.findViewById(R.id.interactionId);
	     interMess = (TextView) page.findViewById(R.id.interactionMessage);
	     textDate = (TextView) page.findViewById(R.id.interactionDate);
	     emptyList = (TextView) findViewById(R.id.emptyResult);
    	  	messageList = DashboardActivityAlt.listItem.getCusThread().getMessages();
   	     Bundle bundle = getIntent().getExtras();
		if (getIntent().getStringExtra("NewMessage") != null)
		{
			String value = bundle.getString("NewMessage");
			if (value.equals("wasAdded"))
			{
				ArrayList<java.io.File> files = new ArrayList<java.io.File>();
				FileManagerActivity.setFinalAttachFiles(files);
			}
		}
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
		    priceLabel.setText(Float.toString(DashboardActivityAlt.listItem.getPrice()));
		    deadline.setText(DashboardActivityAlt.listItem.getDeadline().toString());
		    pager.setAdapter(ps);   
	    } 
	    if (!faq.payPalActivate())
	    {
	    	btnPay.setVisibility(View.VISIBLE);
	    	btnPay.setClickable(false);
	    	btnPay.setEnabled(false);
	    	btnPay.setBackgroundColor(Color.GRAY);
	    	
	    }
	    else new PayPalInitialize().execute();
	    btnClose.setOnClickListener(new View.OnClickListener() {
	    	   
	           public void onClick(View view) {
	               Intent i = new Intent(getApplicationContext(),
	                       DashboardActivityAlt.class);
	               startActivity(i);
	               
	           }
	       });
	    
	    
	 
	    
	    btnInfo.setOnClickListener(new View.OnClickListener() {
	    	   
	           public void onClick(View view) {
	        	   if (DashboardActivityAlt.listItem.getProduct().getProductType().equals("assignment"))
	               {
	        		   Intent i = new Intent(getApplicationContext(),
	                       OrderInfoActivityAA.class);
	        		   startActivity(i);
	               }
	        	   else if (DashboardActivityAlt.listItem.getProduct().getProductType().equals("writing"))
	        	   {
	        		   Intent i = new Intent(getApplicationContext(),
	                       OrderInfoActivityEW.class);
	        		   startActivity(i);
	               }
	           }
	       });
	    
	   
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean res;
	//	if (!DashboardActivityAlt.listItem.getProcess_status().getProccessStatusTitle().equals("Inactive"))
      //  {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.menu_option, menu);
			 res = true;
        //}
//		else 
//		{
//			res  = false;
//		}
		return res;
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
        	
        	break;             
        case R.id.optInAct:    
            {
            	new InactivateOrder().execute();
//        	    UserFunctions func = new UserFunctions();
//            	JSONObject as;
//				try {
//					as = func.deleteOrder(Integer.toString(DashboardActivityAlt.listItem.getOrderid()));
//					Log.i("delete operation", as.toString());
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				//deleteFlag = true;
//				DashboardActivityAlt.listItem.getProcess_status().setProccessStatusTitle("Inactive");
//				Intent i = new Intent(getApplicationContext(),
//	                       DashboardActivityAlt.class);
//	               startActivity(i);
            	 
            
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
	        	Log.i("Messages from service", "on receive method");
	        	updateUI(intent);       
	        }
	    };
	 @Override
		public void onResume() 
	   {
			super.onResume();	
			Log.i("in interactons count start", Integer.toString(serviceCount));
			Log.i("Interactions class","onResume");
			serviceCount ++;
		    startService(intent);
			registerReceiver(broadcastReceiver, new IntentFilter(ServiceMessages.MESSAGES_IMPORT));
		}

		@Override
		public void onPause() {
			super.onPause();
			unregisterReceiver(broadcastReceiver);
			stopService(intent); 		
		}	

	    private void updateUI(Intent intent) {
	    	 pager.setAdapter(ps);   
	    }
	    public ArrayList<Messages> reverse( List<Messages> orig )
	    {
	       ArrayList<Messages> reversed = new ArrayList<Messages>() ;
	       for ( int i = orig.size() - 1 ; i >= 0 ; i-- )
	       {
	           Messages obj = orig.get( i ) ;
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
		  private class InactivateOrder extends AsyncTask<Void, Void, JSONObject > {
			    
			    JSONObject as;
				private ProgressDialog progDailog;
				protected void onPreExecute() {
		        	progDailog = ProgressDialog.show(InteractionsActivityViewPager.this,"Please wait...", "Proceed your order inactivation ...", true);
		        }

		        protected JSONObject doInBackground(Void... args) {
		        
		       	try 
		       	{
		       		UserFunctions func = new UserFunctions();
	            	
					try {
						as = func.deleteOrder(Integer.toString(DashboardActivityAlt.listItem.getOrderid()));
						Log.i("delete operation", as.toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//deleteFlag = true;
					DashboardActivityAlt.listItem.getProcess_status().setProccessStatusTitle("Inactive");
					
					
					Log.i("send message response",as.toString());
				} catch (Exception e) {
					
					e.printStackTrace();
				}
		       		return as;
		       		
		        }

		        protected void onPostExecute(JSONObject  forPrint) {
		 	  	   progDailog.dismiss();
		 	  	   Intent i = new Intent(getApplicationContext(),
	                       DashboardActivityAlt.class);
	               startActivity(i);
		 	  	
	
		            
		        }
				
		   }
		  public void onActivityResult(int requestCode, int resultCode, Intent data) {
		    	if(requestCode != request)
		    		return;
		    	/**
		    	 * If you choose not to implement the PayPalResultDelegate, then you will receive the transaction results here.
		    	 * Below is a section of code that is commented out. This is an example of how to get result information for
		    	 * the transaction. The resultCode will tell you how the transaction ended and other information can be pulled
		    	 * from the Intent using getStringExtra.
		    	 */
		    	switch(resultCode){
		  case Activity.RESULT_OK:
				resultTitle = "SUCCESS";
				resultInfo = "You have successfully completed this payment.";
				Toast.makeText(InteractionsActivityViewPager.this, "Your payment was proceeded successfully", Toast.LENGTH_SHORT).show();
	    		Intent i = new Intent(getApplicationContext(),
	                    DashboardActivityAlt.class);
	     	  	   startActivity(i);
			
				break;
			case Activity.RESULT_CANCELED:
				resultTitle = "CANCELED";
				resultInfo = "The transaction has been cancelled.";
				resultExtra = "";
				Toast.makeText(InteractionsActivityViewPager.this, "The transaction has been cancelled.", Toast.LENGTH_SHORT).show();
				break;
			case PayPalActivity.RESULT_FAILURE:
				resultTitle = "FAILURE";
				resultInfo = data.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
				resultExtra = "Error ID: " + data.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
				Toast.makeText(InteractionsActivityViewPager.this, "Failure! "+ resultInfo, Toast.LENGTH_SHORT).show();
		    	}
		    
		    }
			private class PayPalInitialize extends AsyncTask<Void, Void, Integer> {
			    
				
				protected void onPreExecute() {
					
		        	progDailog = ProgressDialog.show(InteractionsActivityViewPager.this,"Please wait...", "PayPal library initializing ...", true);
		        }

		        protected Integer doInBackground(Void... args) {
		        Integer result = 0;
		       	try 
		       	{
		       		
		       		
		       		boolean res =faq.initLibrary(InteractionsActivityViewPager.this);
		       		Log.i("PayPal init res", Boolean.toString(res));
					
					// The library is initialized so let's create our CheckoutButton and update the UI.
					if (PayPal.getInstance().isLibraryInitialized()) {
						result = (INITIALIZE_SUCCESS);
					}
					else {
						result = (INITIALIZE_FAILURE);
						
					}
				} catch (Exception e) {
					
					e.printStackTrace();
				}
		       	Log.i("result result", result.toString());
				return result;
		       		
		        }
		        protected void onPostExecute(Integer result) {

		        	progDailog.dismiss();
		        	Log.i("result onPost result", result.toString());
		         if (result.equals(INITIALIZE_SUCCESS))
		        	 faq.setupButtons(InteractionsActivityViewPager.this, panelInteractions);
		         else if (result.equals(INITIALIZE_FAILURE))
		        	 faq.showFailure(InteractionsActivityViewPager.this);
		        }
		   }

		  
		
}
