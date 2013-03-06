package com.assignmentexpert;

import java.io.File;
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
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import com.activitygroups.MainTabGroup;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.InactivateAsync;
import com.asynctasks.PayPalAsync;
import com.asynctasks.PaymentProceeding;
import com.customitems.CustomTextView;
import com.datamodel.Messages;
import com.datamodel.Order;
import com.library.Constants;
import com.library.FrequentlyUsedMethods;
import com.library.MyGLRenderer;
import com.library.ServiceIntentMessages;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;

public class InteractionsActivityViewPager extends FragmentActivity implements ITaskLoaderListener{
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
	protected static final String PAYMENT_NOTIFY = "new_payment";
	PayPal mPayPal;
	String resultTitle;
	String resultInfo;
	String resultExtra;
	private ProgressDialog progDailog;
	private RelativeLayout panelInteractions;
	FrequentlyUsedMethods faq;
	TextView deadline;
	TextView priceLabel;
	private ArrayAdapter<File> fileAdapter;
	   private CustomTextView currMess1;
			private CustomTextView currMess3;
			private CustomTextView currMess2;
			private ListView interactionsFileList;
			private Button saveProfile;
			private Button cancelProfile;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
	    super.onCreate(savedInstanceState);
	    this.setContentView(R.layout.interactions_view_pager);
	    
	    intent = new Intent(InteractionsActivityViewPager.this, ServiceIntentMessages.class);
	    cxt = this;
	    ps = new PageSlider();
	    
	    pager = (ViewPager) findViewById(R.id.conpageslider);
	    faq = new FrequentlyUsedMethods(InteractionsActivityViewPager.this);
	    faq.setActivity(InteractionsActivityViewPager.this);
   		faq.setContext(InteractionsActivityViewPager.this);
   		deadline = (TextView)findViewById(R.id.deadlineLabel);
   		priceLabel = (TextView)findViewById(R.id.priceLabel);
	    LayoutInflater inflater = LayoutInflater.from(cxt);
	    btnClose = (Button) findViewById(R.id.btnClose);
	    btnInfo = (Button) findViewById(R.id.btnInfoOrder);
	    btnPay  = (Button) findViewById(R.id.btnPay);
	    btnInfoOrder = (Button) findViewById(R.id.btnInfoOrder);
	    panelInteractions =  ((RelativeLayout)findViewById(R.id.panelInteractions));
	    interactionsFileList = (ListView)findViewById(R.id.interactionsFileList);
	    
	    saveProfile = (Button)findViewById(R.id.saveProfile);
	    cancelProfile = (Button)findViewById(R.id.cancelProfile);
	    
	    
	    saveProfile.setOnClickListener(new View.OnClickListener() {
        	 
            public void onClick(View view) {
            	 Intent frequentMessages = new Intent(getParent(), NewMessageActivity.class);
	             MainTabGroup parentActivity = (MainTabGroup)getParent();
	             parentActivity.startChildActivity("FrequentMessageActivity", frequentMessages);
                
            }
        });
	    cancelProfile.setOnClickListener(new View.OnClickListener() {
        	 
            public void onClick(View view) {
            	InactivateAsync.execute(InteractionsActivityViewPager.this, InteractionsActivityViewPager.this);
                
            }
        });
	    
	    
	    pager.setOnPageChangeListener(new OnPageChangeListener() {

	        public void onPageScrollStateChanged(int arg0) {
	        }

	        public void onPageScrolled(int arg0, float arg1, int arg2) {
	        	
	        	
	        }

	        public void onPageSelected(int currentPage) {
//	        	 currMess1.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()-p));
//		    	 currMess3.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()-2  ));
//		    	 currMess3.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()-3 ));
	        	Log.i("onPageScrolled method", Integer.toString(currentPage));
	        //	for (File f : DashboardActivityAlt.messagesExport.get(currentPage))
	        }

	    });
	    
	    RelativeLayout  v = (RelativeLayout) findViewById(R.layout.interactions_view_pager);
	    Log.i("Interactions class","onCreate");
	    
	     page = inflater.inflate(R.layout.interactions_item, null);
	     interId = (TextView) page.findViewById(R.id.interactionId);
	     interMess = (TextView) page.findViewById(R.id.interactionMessage);
	     textDate = (TextView) page.findViewById(R.id.interactionDate);
	     emptyList = (TextView) findViewById(R.id.emptyResult);
    	  	messageList = DashboardActivityAlt.listItem.getCusThread().getMessages();
    	    updateLayout();
   	     Bundle bundle = getIntent().getExtras();
		if (getIntent().getStringExtra("NewMessage") != null)
		{
			String value = bundle.getString("NewMessage");
			if (value.equals("wasAdded"))
			{
				ArrayList<java.io.File> files = new ArrayList<java.io.File>();
				FileManagerActivity.setMessageAttachFiles(files);
			}
		}
		Log.i("Interactions activity item status", Integer.toString(DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId()));
//		if (DashboardActivityAlt.listItem.getProduct().getProductType().equalsIgnoreCase("assignment"))
//            {
//
//            Intent frequentMessages = new Intent(getParent(), OrderInfoActivityAA.class);
//             MainTabGroup parentActivity = (MainTabGroup)getParent();
//             parentActivity.startChildActivity("FrequentMessageActivity", frequentMessages);
//            }
//     	   else if (DashboardActivityAlt.listItem.getProduct().getProductType().equalsIgnoreCase("writing"))
//     	   {
//
//               Intent frequentMessages = new Intent(getParent(), OrderInfoActivityEW.class);
//	             MainTabGroup parentActivity = (MainTabGroup)getParent();
//	             parentActivity.startChildActivity("FrequentMessageActivity", frequentMessages);
//            }

	    
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		boolean res;
//		if (!DashboardActivityAlt.listItem.getProcess_status().getProccessStatusTitle().equals("Inactive"))
//        {
//			MenuInflater inflater = getMenuInflater();
//			inflater.inflate(R.menu.menu_option, menu);
//			 res = true;
//        }
//		else 
//		{
//			res  = false;
//		}
//		return res;
//    }
	@Override

	public boolean onPrepareOptionsMenu(Menu menu) {

		menu.clear();
		if(DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId() == 2|
				DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId() == 4 | 
				DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId() == 5 |
				DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId() == 6 |
				DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId() == 7) 
		{
	
			menu.add(0, 1, 0, "Reply");
	
		} 
		if (DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId() == 2 | 
				  DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId() == 3 | 
	  			 DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId() == 4 | 
	  			  DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId() == 5)
		{
	
				menu.add(0, 2, 1, "Inactivate");
	
		}

		return super.onPrepareOptionsMenu(menu);

	}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	
		switch (item.getItemId()) {
        case 1:   
        	{

        		Intent i = new Intent(getApplicationContext(),
	                       NewMessageActivity.class);
	               startActivity(i);
	               
        	}
        	
        	break;             
        case 2:    
            {
            	InactivateAsync.execute(this, this);
        	} 
            break;
         
	   }
return true;
    }
 

	public class PageSlider extends PagerAdapter{

	 
		private LinearLayout linearLayout;
		int count=0;
		private LinearLayout interactionsFilesPanel;
		private ListView fileslist;
		
		@Override
	    public int getItemPosition(Object object) {
	        // TODO Auto-generated method stub
			Log.i("page adapter",Integer.toString(POSITION_NONE));
	        return POSITION_NONE;
	    }
		@Override
	    public int getCount() {
	        return DashboardActivityAlt.messagesExport.size();
	    }

	    @Override
	    public Object instantiateItem(View collection, final int position) {
	    	
	    	LayoutInflater inflater = LayoutInflater.from(cxt);
		     page = inflater.inflate(R.layout.interactions_item, null);
		     interId = (TextView) page.findViewById(R.id.interactionId);
	    	 interMess = (TextView) page.findViewById(R.id.interactionMessage);
	    	 textDate = (TextView) page.findViewById(R.id.interactionDate);
	    	 currMess1 =  (CustomTextView) page.findViewById(R.id.cursorMess1);
	       	 currMess2 =  (CustomTextView) page.findViewById(R.id.cursorMess2);
	       	 currMess3 =  (CustomTextView) page.findViewById(R.id.cursorMess3);
	    	 linearLayout = (LinearLayout)page.findViewById(R.id.interactionsCursor);
	    	 interactionsFilesPanel = (LinearLayout)page.findViewById(R.id.interactionsFilesPanel);
	    	 fileslist = (ListView)page.findViewById(R.id.fileslist);
	    	 if (!DashboardActivityAlt.messagesExport.isEmpty())
		     {	
	    		 if (reverse(DashboardActivityAlt.messagesExport).get(position).getFiles().isEmpty())
	    			 interactionsFilesPanel.setVisibility(View.GONE);
	    		 else
	    		 {
	    			 final ArrayList<File> messageFiles =  reverse(DashboardActivityAlt.messagesExport).get(position).getFiles();
	    			 Log.i("files size",Integer.toString(messageFiles.size()));
	    			 
//	    			 trtfor(File w: messageFiles)
//	    			 {
//	    				 Log.i("files message", w.getName());
//	    			 }
	    			 fileAdapter = new ArrayAdapter<File>(InteractionsActivityViewPager.this,
	 						R.layout.interactions_item, R.id.fileCheck,messageFiles ) 
	 						{
	 					@Override
	 					public View getView(final int position2, View convertView, ViewGroup parent) 
	 					{
	 				        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 				        View view = inflater.inflate(R.layout.file, null);
	 			            view.setFocusable(false);
	 			            RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	 			            relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//	 						CheckBox textView = (CheckBox) view
//	 								.findViewById(R.id.fileCheck);
	 			            CustomTextView textView = (CustomTextView)view
	 								.findViewById(android.R.id.title); 
	 			            CustomTextView fileSize = (CustomTextView)view
	 								.findViewById(R.id.fileSize); 
	 			            final CheckBox checkBox = (CheckBox)view
	 								.findViewById(R.id.fileCheck); 
	 						textView.setClickable(true);
	 						checkBox.setVisibility(View.INVISIBLE);
	 						
//	 						textView.setText(messageFiles.get(position2).getName());
	 					//	fileSize.setText(Long.toString(reverse(DashboardActivityAlt.messagesExport).get(position).getFiles().get(position).length())+ " Mb");
	 						
	 						
	 						return view;
	 					}
	 				};
	 				CustomTextView textView = (CustomTextView)page
								.findViewById(android.R.id.title); 
			        CustomTextView fileSize = (CustomTextView)page
								.findViewById(R.id.fileSize); 
			        try{
			        textView.setText(Integer.toString(messageFiles.size())+ " files attached");
			        Log.i("files size2",Integer.toString(messageFiles.size()));
                    long wholeSize = 0;
                    
                    for (File file: messageFiles)
                    {
                    	Log.i("files name", file.getName());
                    	wholeSize += file.length();
                    }
                    fileSize.setText(Long.toString(wholeSize)+ " Mb");
			        }catch(Exception w)
			        {
			        	w.printStackTrace();
			        }
	 				
	    		 }
	    		 
	    		 interId.setText("Interaction "+Integer.toString(reverse(DashboardActivityAlt.messagesExport).get(position).getMessageId()));
			     interMess.setText(reverse(DashboardActivityAlt.messagesExport).get(position).getMessageBody());
			     textDate.setText(reverse(DashboardActivityAlt.messagesExport).get(position).getMessageDate().toString());
			     if(DashboardActivityAlt.messagesExport.size() == 1)
		    	 {
		    		 	currMess2.setText(Integer.toString(1) ); 
		    	 }
			     else
			     {
				     if (position == 0)
				     {
				    	 currMess2.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()));
				    	 currMess3.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()-1  ));
				     }
				     else if(position == DashboardActivityAlt.messagesExport.size()-1)
				     {
				    	 currMess1.setText(Integer.toString(2));
				    	 currMess2.setText(Integer.toString(1));
				     }
				     else
				     {
					     currMess1.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()-(position-1)));
				    	 currMess2.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()-(position)  ));
				    	 currMess3.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()-(position +1) ));
				     }
			     }
		     }	
	    	
	    	 else if(DashboardActivityAlt.messagesExport.isEmpty())
	    	 {
	    		  Log.i("list is empty", "interactions");
		    	  interMess.setTextColor(Color.BLACK);
		    	  interMess.setText("Currently no messages");
	    	 }
	    	 
	    	 fileslist.setAdapter(fileAdapter);
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
	public void addFiles(int position)
	{
		
	}
	
	 private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	        	Log.i("Messages from service", "on receive method");
	        	updateUI(intent);   
//	        	if (intent.getAction().equalsIgnoreCase(ServiceIntentMessages.ORDERS_IMPORT))
//	        	{
//	        		 Intent i = new Intent(getApplicationContext(),
//	 	                    DashboardActivityAlt.class);
//	 	     	  	  startActivity(i);
//	        	}
	        }
	    };
	    private BroadcastReceiver messageFilesReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	        	Log.i("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!","broadcast was received");
	            Intent frequentMessages = new Intent(getParent(), NewMessageActivity.class);
	             MainTabGroup parentActivity = (MainTabGroup)getParent();
	             parentActivity.startChildActivity("FrequentMessageActivity", frequentMessages);
	        }
	    };

	 @Override
	 public void onResume() 
	   {
			
			Log.i("Interactions class","onResume");
			Log.i("Interactions activity order status",DashboardActivityAlt.listItem.getProcess_status().getProccessStatusTitle());
			serviceCount ++;
		    startService(intent);
			registerReceiver(broadcastReceiver, new IntentFilter(ServiceIntentMessages.MESSAGES_IMPORT));
			registerReceiver(broadcastReceiver, new IntentFilter(ServiceIntentMessages.ORDERS_IMPORT));
			registerReceiver(messageFilesReceiver, new IntentFilter(Constants.MESSAGE_FILES));
			super.onResume();	
		}

		@Override
		public void onPause() {
			
			unregisterReceiver(broadcastReceiver);
			unregisterReceiver(messageFilesReceiver);
			stopService(intent); 		
			super.onPause();
		}	

	    private void updateUI(Intent intent)
	    {
	    	 updateLayout();
	    	 pager.setAdapter(ps);   
	    }
	    
	    public void updateLayout()
	    {
	    	if (DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId()==2 | DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId()==3)
	    	{
	    		emptyList.setVisibility(View.VISIBLE);
	    		 priceLabel.setText("N/A");
	    	}
	    	else
	    	 { 
	    		emptyList.setVisibility(View.GONE);
	    		priceLabel.setTextColor(Color.GREEN);
	   		    priceLabel.setText("$"+Float.toString(DashboardActivityAlt.listItem.getPrice()));
	    	 }
	    	if (DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId()==4)
				   	    {
				   	    	Log.i("interations status", "process status is 4");
				   	    	btnPay.setVisibility(View.GONE);
				   	    	PayPalAsync.execute(this, this);
				   	    	
				   	    }
				   	else
				   	    {
				   	    	Log.i("process status is not 4", DashboardActivityAlt.listItem.getProcess_status().getProccessStatusTitle());
				   	    	btnPay.setVisibility(View.VISIBLE);
				   	    	btnPay.setClickable(false);
				   	    	btnPay.setEnabled(false);
				   	    	Log.i("Interactions activity item status", DashboardActivityAlt.listItem.getProcess_status().getProccessStatusTitle());
				   	    	
				   	  }
   		      deadline.setText(DashboardActivityAlt.listItem.getDeadline().toString());
   		      pager.setAdapter(ps);   
		   	    
		   	   
	    	
	    }
	    
	    
	    
	    public ArrayList<Messages> reverse( List<Messages> orig )
	    {
	       ArrayList<Messages> reversed = new ArrayList<Messages>() ;
	       for ( int i = orig.size() - 1 ; i >=0 ; i-- )
	       {
	           Messages obj = orig.get( i ) ;
	           reversed.add( obj ) ;
	           obj.setPosition(i+1);
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
		  public void onActivityResult(int requestCode, int resultCode, Intent data) {
		    	if(requestCode != request)
		    		return;
		    	/**
		    	 * If you choose not to implement the PayPalResultDelegate, then you will receive the transaction results here.
		    	 * Below is a section of code that is commented out. This is an example of how to get result information for
		    	 * the transaction. The resultCode will tell you how the transaction ended and other information can be pulled
		    	 * from the Intent using getStringExtra.
		    	 */
		    	switch(resultCode)
		    	{
		  case Activity.RESULT_OK:
			  	PaymentProceeding.execute(this, this);
			  	faq.updateOrderFields(DashboardActivityAlt.listItem);
				resultTitle = "SUCCESS";
				resultInfo = "You have successfully completed this payment.";
				Toast.makeText(InteractionsActivityViewPager.this, "Your payment was proceeded successfully", Toast.LENGTH_SHORT).show();
				 Intent i = new Intent(getApplicationContext(),
	 	                    DashboardActivityAlt.class);
	 	     	  	  startActivity(i);
//        	    UserFunctions func = new UserFunctions();
//	              try {
//					func.sendPayment(Integer.toString(DashboardActivityAlt.listItem.getOrderid()),Float.toString(DashboardActivityAlt.listItem.getPrice()));
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
	   
			
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
			@Override
		    public void onBackPressed() {
				Intent i = new Intent(getApplicationContext(),
		                DashboardActivityAlt.class);
		        startActivity(i);
		    }
			public void onLoadFinished(Object data) {
				if (data instanceof Integer)
				 faq.setupButtons(InteractionsActivityViewPager.this, panelInteractions);
				else if(data instanceof JSONObject)
				{
					Intent i = new Intent(getApplicationContext(),
		                       DashboardActivityAlt.class);
		            startActivity(i);
				}
				
			}
			public void onCancelLoad() {
				  faq.showFailure(InteractionsActivityViewPager.this);
	        }
			
//			public void addFiles()
//		    {
//				
//				
//
//				adapter = new ArrayAdapter<File>(this,
//						R.layout.interactions_view_pager, R.id.fileCheck,
//						DashboardActivityAlt.messagesExport.g) 
//						{
//					@Override
//					public View getView(final int position, View convertView, ViewGroup parent) 
//					{
//				        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				        View view = inflater.inflate(R.layout.file, null);
//			            view.setFocusable(false);
//			            RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
//			            relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
////						CheckBox textView = (CheckBox) view
////								.findViewById(R.id.fileCheck);
//			            CustomTextView textView = (CustomTextView)view
//								.findViewById(android.R.id.title); 
//			            CustomTextView fileSize = (CustomTextView)view
//								.findViewById(R.id.fileSize); 
//			            final CheckBox checkBox = (CheckBox)view
//								.findViewById(R.id.fileCheck); 
//						textView.setClickable(true);
//						
//						textView.setText(FileManagerActivity.getFinalAttachFiles().get(position).getName().toString());
//						//textView.setPadding(10, 0, 0, 0);
//						//fileSize.setPadding(10, 0, 0, 0);
//						
//						fileSize.setText(Long.toString(FileManagerActivity.getFinalAttachFiles().get(position).length())+ " Mb");
//						for (int i = 0; i< FileManagerActivity.getFinalAttachFiles().size();i++)
//				         {
//								textView.setTag(i);
//				         }
//						final boolean trigger = false;
//						view.setOnClickListener(new OnClickListener() {
//							public void onClick(View v) {
//								
////								if (((CheckBox)((((ViewGroup)v)).getChildAt(position))).isChecked())
////								((CheckBox)((ViewGroup)v).getChildAt(position))))
////				                if((CheckBox(ViewGroup)v.getChildAt(position))).isChecked()){
////				                  checks.set(position, 1);
////				                }
////				                else{
////				                 checks.set(position, 0);
////				                }
////								if (!trigger)
////								   trigger = true;
////								else
////							       trigger = false;
////								
////								checkBox.setChecked(trigger);
//								if (((CheckBox)((((ViewGroup)v)).getChildAt(position))).isChecked())
//									  checks.set(position, 1);
//								else
//									  checks.set(position, 0);
//				            }
//				        });
//						textView.setOnLongClickListener(onclicklistener);
//						return view;
//					}
//				};
//				customfileList.setAdapter(adapter);
//				mainList.addFooterView(filesList);
//				mainList.addFooterView(assignfooter);
//		    }		 
			
		
		
}

class MyGLSurfaceView extends GLSurfaceView {

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(new MyGLRenderer());

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}

