package com.assignmentexpert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
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
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.activitygroups.MainTabGroup;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.InactivateAsync;
import com.asynctasks.PayPalAsync;
import com.asynctasks.PaymentProceeding;
import com.customitems.CustomTextView;
import com.datamodel.Files;
import com.datamodel.Messages;
import com.datamodel.Order;
import com.library.Constants;
import com.library.FrequentlyUsedMethods;
import com.library.MyGLRenderer;
import com.library.ServiceIntentMessages;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.tabscreens.DashboardTabScreen;

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
	ArrayList<Files> messageFiles ;
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
	private ArrayAdapter<Files> fileAdapter;
	   private CustomTextView currMess1;
			private CustomTextView currMess3;
			private CustomTextView currMess2;
			private ListView interactionsFileList;
			private Button btnReply;
			private Button btnInactivate;
			private RelativeLayout messagesPanel;
			
     private long enqueue;
	 private DownloadManager dm;
	 
	 final String DOWNLOAD_FILE = "http://goo.gl/w3XV3";

	 final String strPref_Download_ID = "PREF_DOWNLOAD_ID";
	 
	 SharedPreferences preferenceManager;
	 DownloadManager downloadManager;
	 int currentMessPos;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		 InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
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
	    
	    btnReply = (Button)findViewById(R.id.btnReply);
	    btnInactivate = (Button)findViewById(R.id.btnInactivate);
	    
	    
	    messagesPanel = (RelativeLayout)findViewById(R.id.messagesPanel);
	    
	     currMess1 =  (CustomTextView) findViewById(R.id.cursorMess1);
      	 currMess2 =  (CustomTextView) findViewById(R.id.cursorMess2);
      	 currMess3 =  (CustomTextView) findViewById(R.id.cursorMess3);
	    
      	 if (!DashboardActivityAlt.messagesExport.isEmpty())
      	 {	    for (Messages b : DashboardActivityAlt.messagesExport)
			    {
			    	Log.i("messages", b.getMessageBody());
			    	
			    }
      	 }
      	 else 
      		 Log.i("messages exp[ort","it is empty");
      	 
      	 
      	btnReply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent frequentMessages = new Intent(getParent(), NewMessageActivity.class);
	             MainTabGroup parentActivity = (MainTabGroup)getParent();
	             parentActivity.startChildActivity("FrequentMessageActivity", frequentMessages);
            }
        });
	    btnInactivate.setOnClickListener(new View.OnClickListener() {
        	 
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
	        	setCurrMessPos(currentPage);
	        	Log.i("interactions","onPageSelected");
				     if (currentPage == 0)
				     {
				    	 currMess1.setText(" ");
				    	 currMess2.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()));
				    	 currMess3.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()-1  ));
				     }
				     else if(currentPage == DashboardActivityAlt.messagesExport.size()-1)
				     {
				    	 currMess1.setText(Integer.toString(2));
				    	 currMess2.setText(Integer.toString(1));
				    	 currMess3.setText("   ");
				     }
				     else
				     {
					     currMess1.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()-(currentPage-1)));
				    	 currMess2.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()-(currentPage)));
				    	 currMess3.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()-(currentPage +1) ));
				     }
	        }

	    });
	    
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

//			if (!DashboardActivityAlt.messagesExport.isEmpty())
//			{
//				int g=0;
//				for (Messages s :  DashboardActivityAlt.messagesExport)
//				{
//					if (!s.getFiles().isEmpty())
//					{
//						for (Files b: s.getFiles())
//						{
//							try{
//							 Uri downloadUri = Uri.parse(b.getFileFullPath());
//			            	   DownloadManager.Request request = new DownloadManager.Request(downloadUri);
//			            	   long id = downloadManager.enqueue(request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
//			            			   "AssignmentExpert/"+b.getFileName()));
//			            	   Editor PrefEdit = preferenceManager.edit();
//			            	   PrefEdit.putLong(strPref_Download_ID, id);
//			            	   PrefEdit.commit();
//							}
//							catch(NullPointerException e)
//							{
//								e.printStackTrace();
//							}
//						}
//					}
//				}
//			}
			 preferenceManager
		        = PreferenceManager.getDefaultSharedPreferences(this);
		       downloadManager
		        = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
	}
	 public int getFilePosition(View v)
	 {
		 int pos = 0;
		for (int i = 0; i<fileAdapter.getCount();i++)
		{
			if (((CustomTextView)v.findViewById(android.R.id.title)).getText().equals((fileAdapter.getItem(i).getFileName())))
			 pos = i;
		}
		 return pos;
	 }
	 OnClickListener filesClicklistener = new OnClickListener() {
			public void onClick(View arg0) {
				final CharSequence[] items = {"Open", "Details"};
				final AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
			    builder.setTitle((((CustomTextView)arg0.findViewById(android.R.id.title))).getText().toString());
			     int pos = getFilePosition(arg0);
			    Files filePos = reverse(DashboardActivityAlt.messagesExport).get(getCurrMessPos()).getFiles().get(pos);
			    final File file = new FrequentlyUsedMethods(InteractionsActivityViewPager.this).findFile(filePos.getFileName());
			    if (file != null)
			    {
				    try{
					builder.setItems(items, new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog, int item) {
					    	if (item == 0)
					    	{
					    		
					    		Intent intent = new Intent();
				    			intent.setAction(android.content.Intent.ACTION_VIEW);
				    			intent.setDataAndType(Uri.fromFile(file), "text/plain");
				    			startActivity(intent);
								
					    	}
					    	 else if (item == 1)
					    	{  
						    		AlertDialog.Builder builder2 = new AlertDialog.Builder(getParent());
						  		    builder2.setTitle(file.getName());
						  		  FileInputStream fis;
								try {
									fis = new FileInputStream(file);
									builder2.setMessage("Size of file is: " + Long.toString(fis.getChannel().size()/1024)+ "  KB"+"\r\n"+
									"Path of file is: " +"\r\n" + file.getPath());
	
								} catch (FileNotFoundException e) 
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) 
								{
									e.printStackTrace();
								}
						  		  AlertDialog alert = builder2.create();
						  			alert.show();
					    	}
					    }
					});
					AlertDialog alert = builder.create();
					alert.show();
				    }
				    catch(Exception e)
				    {
				    	 Toast.makeText(getApplicationContext(), "current file couldn't be finded", Toast.LENGTH_SHORT).show();
				    }
			    }
			    
			}
		};

	public class PageSlider extends PagerAdapter{
		private LinearLayout linearLayout;
		int count=0;
		private LinearLayout interactionsFilesPanel;
		private ListView fileslist;
		
		@Override
	    public int getItemPosition(Object object) {
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
	    	 
	    	 linearLayout = (LinearLayout)page.findViewById(R.id.interactionsCursor);
	    	 interactionsFilesPanel = (LinearLayout)page.findViewById(R.id.interactionsFilesPanel);
	    	 fileslist = (ListView)page.findViewById(R.id.fileslist);
	    	 if (!DashboardActivityAlt.messagesExport.isEmpty())
		     {	
	    		 if (reverse(DashboardActivityAlt.messagesExport).get(position).getFiles().isEmpty())
	    			 interactionsFilesPanel.setVisibility(View.GONE);
	    		 else
	    		 {
	    			 interactionsFilesPanel.setVisibility(View.VISIBLE);
	    			 messageFiles =  reverse(DashboardActivityAlt.messagesExport).get(position).getFiles();
	    			 Log.i("files size",Integer.toString(messageFiles.size()));
	    			 fileAdapter = new ArrayAdapter<Files>(InteractionsActivityViewPager.this,
	 						R.layout.interactions_item, R.id.fileCheck,messageFiles ) 
	 						{
	 					@Override
	 					public View getView(int position2, View convertView, ViewGroup parent) 
	 					{
	 				        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 				        View view = inflater.inflate(R.layout.file, null);
	 			            view.setFocusable(false);
	 			            RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	 			            relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	 			            CustomTextView textView = (CustomTextView)view
	 								.findViewById(android.R.id.title); 
	 			            CustomTextView fileSize = (CustomTextView)view
	 								.findViewById(R.id.fileSize); 
	 			            final CheckBox checkBox = (CheckBox)view
	 								.findViewById(R.id.fileCheck); 
	 						textView.setClickable(true);
	 						checkBox.setVisibility(View.INVISIBLE);
	 						for (int i = 0; i< messageFiles.size();i++)
	 				         {
	 								textView.setTag(i);
	 				         }
	 						textView.setText(messageFiles.get(position2).getFileName());
		 					fileSize.setText(Integer.toString(messageFiles.get(position2).getFileSize()/1024)+ " KB");
	 						view.setOnClickListener(filesClicklistener);
	 						return view;
	 					}
	 					@Override
	 				     public int getCount(){
	 				           return messageFiles.size();
	 				     }
	 				};
	 				CustomTextView textView = (CustomTextView)page
								.findViewById(android.R.id.title); 
			        CustomTextView fileSize = (CustomTextView)page
								.findViewById(R.id.fileSize); 
			        try{
			        textView.setText(Integer.toString(messageFiles.size())+ " files attached");
                    int wholeSize = 0;
                    
                    for (Files file: messageFiles)
                    {
                    	wholeSize += file.getFileSize();
                    }
                    fileSize.setText(Integer.toString(wholeSize/1024)+ " KB");
			        }catch(Exception w)
			        {
			        	w.printStackTrace();
			        }
	 				
	    		 }
	    		 
	    		 interId.setText("Interaction "+Integer.toString(reverse(DashboardActivityAlt.messagesExport).get(position).getMessageId()));
			     interMess.setText(reverse(DashboardActivityAlt.messagesExport).get(position).getMessageBody());
			     textDate.setText(reverse(DashboardActivityAlt.messagesExport).get(position).getMessageDate().toString());
			   
		     }	
	    	
	    	 else if(DashboardActivityAlt.messagesExport.isEmpty())
	    	 {
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
	 private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	        	updateUI(intent);   
	        }
	    };
	    private BroadcastReceiver downloadFilesReceiver = new BroadcastReceiver() {

	    	  @Override
	    	  public void onReceive(Context arg0, Intent arg1) {
	    	   // TODO Auto-generated method stub
	    	   DownloadManager.Query query = new DownloadManager.Query();
	    	   query.setFilterById(preferenceManager.getLong(strPref_Download_ID, 0));
	    	   Cursor cursor = downloadManager.query(query);
	    	   if(cursor.moveToFirst()){
	    	    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
	    	    int status = cursor.getInt(columnIndex);
	    	    if(status == DownloadManager.STATUS_SUCCESSFUL){
	    	     long downloadID = preferenceManager.getLong(strPref_Download_ID, 0);
	    	     ParcelFileDescriptor file;
	    	     try {
	    	      file = downloadManager.openDownloadedFile(downloadID);
	    	     } catch (FileNotFoundException e) {
	    	      // TODO Auto-generated catch block
	    	      e.printStackTrace();
	    	     }
	    	     
	    	    }
	    	   }
	    	  } 
	    	 };
	    	 
	    	 
	    private BroadcastReceiver messageFilesReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            Intent frequentMessages = new Intent(getParent(), NewMessageActivity.class);
	             MainTabGroup parentActivity = (MainTabGroup)getParent();
	             parentActivity.startChildActivity("FrequentMessageActivity", frequentMessages);
	        }
	    };
	 @Override
	 public void onResume() 
	   {
			 InputMethodManager imm = (InputMethodManager)getSystemService(
				      Context.INPUT_METHOD_SERVICE);
		    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
			serviceCount ++;
		    startService(intent);
			registerReceiver(broadcastReceiver, new IntentFilter(ServiceIntentMessages.MESSAGES_IMPORT));
			registerReceiver(broadcastReceiver, new IntentFilter(ServiceIntentMessages.ORDERS_IMPORT));
			registerReceiver(messageFilesReceiver, new IntentFilter(Constants.MESSAGE_FILES));
			super.onResume();	
			 // registerReceiver(downloadFilesReceiver, intentFilter);
		}
		@Override
		public void onPause() {
			super.onPause();
			unregisterReceiver(broadcastReceiver);
			unregisterReceiver(messageFilesReceiver);
			stopService(intent);
			//unregisterReceiver(downloadFilesReceiver);
		}	

	    private void updateUI(Intent intent)
	    {
	    	 updateLayout();
	    	 pager.setAdapter(ps);   
	    }
	    
	    public void updateLayout()
	    {
	    	if (!DashboardActivityAlt.messagesExport.isEmpty())
	 	    {
	    		Log.i("interactions","regular selection");
	 		    if(DashboardActivityAlt.messagesExport.size() == 1)
	 		    {
	 	   		 	currMess2.setText(Integer.toString(1) ); 
	 	   	 	}
	 		     else
	 		     {
	 		    	 currMess2.setText(" "+Integer.toString(DashboardActivityAlt.messagesExport.size()));
	 		    	 currMess3.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()-1  ));
	 		     }
	 	    }
	    	if (DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId()==9)
	    	{
	    		btnInactivate.setEnabled(false);
	    	}
	    	if (DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId()==2 | DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId()==3)
	    	{
	    		 emptyList.setVisibility(View.VISIBLE);
	    		  messagesPanel.setVisibility(View.GONE);
	    		 priceLabel.setText("N/A");
	    	}
	    	else
	    	 { 
	    		
	    		emptyList.setVisibility(View.GONE);
	    		priceLabel.setTextColor(Color.GREEN);
	   		    priceLabel.setText("$"+Float.toString(DashboardActivityAlt.listItem.getPrice()));
	   		    
	    	 }
	    			if (DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId()==4 & DashboardActivityAlt.listItem.getPrice()>0)
				   	    {
				   	    	btnPay.setVisibility(View.GONE);
				   	    	PayPalAsync.execute(this, this);
				   	    }
				   	else
				   	    {
				   	    	btnPay.setVisibility(View.VISIBLE);
				   	    	btnPay.setClickable(false);
				   	    	btnPay.setEnabled(false);
				   	  }
   		      deadline.setText(DashboardActivityAlt.listItem.getDeadline().toString());
   		      pager.setAdapter(ps);   
   		      Log.i("interactions pager", Integer.toString(pager.getChildCount()));
   		      pager.setCurrentItem(pager.getChildCount());
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
	                      DashboardTabScreen.class);
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
		                       DashboardTabScreen.class);
		            startActivity(i);
				}
				
			}
			public void onCancelLoad() {
				  faq.showFailure(InteractionsActivityViewPager.this);
	        }
			
			
			public int getCurrMessPos()
			{
				return this.currentMessPos;
			}
			public void setCurrMessPos(int currPoss)
			{
				this.currentMessPos = currPoss;
			}
		
}



