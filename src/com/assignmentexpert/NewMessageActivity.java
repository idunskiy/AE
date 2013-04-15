package com.assignmentexpert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.activitygroups.MainTabGroup;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.asynctasks.LoginAsync;
import com.asynctasks.PayPalAsync;
import com.asynctasks.SendMessageAsync;
import com.customitems.CustomTextView;
import com.datamodel.Messages;
import com.datamodel.Order;
import com.library.FrequentlyUsedMethods;
import com.library.UserFunctions;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.tabscreens.DashboardTabScreen;
import com.tabscreens.LoginTabScreen;
public class NewMessageActivity extends FragmentActivity implements ITaskLoaderListener{
	private ViewPager pager;
	public Context cxt;
	//private CharSequence[] pages = {"stuff", "more stuff", "other stuff"};
	
	private Intent intent;
	List<Messages> messagesList;
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
	private SharedPreferences prefs;
	private Editor prefEditor;
	private Button btnCancelNewMessage;
	private static final String PAYPAL_APP_ID = "APP-80W284485P519543T";
	private static final int request = 1;
	PayPal mPayPal;
	public static String resultTitle;
	public static String resultInfo;
	public static String resultExtra;
	
	private static final int server = PayPal.ENV_SANDBOX;
	private static final String appID = "APP-80W284485P519543T";
	CheckoutButton launchSimplePayment;
	protected static final Integer INITIALIZE_SUCCESS = 1;
	protected static final Integer INITIALIZE_FAILURE = 2;
	FrequentlyUsedMethods faq;
	private RelativeLayout panelInteractions;
	public static String newMessage = "";
	   View messageFileList;
	private ArrayList<Integer> checks = new ArrayList<Integer>();
	private ListView fileslist;
	private Button btnFilesRemove;
	CustomTextView test1TextView;
	CustomTextView fileSizeHead;
	 RelativeLayout newMessagePanel ;
	 TextView deadline;
	 TextView priceLabel;
	private CustomTextView textHead;
	boolean fileListExist  = false;
	 private static String KEY_STATUS = "status";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    
	    this.setContentView(R.layout.new_message2);
	    
	    cxt = this;
	    pager = (ViewPager) findViewById(R.id.conpageslider);
	  
	    LayoutInflater inflater = LayoutInflater.from(cxt);

		faq = new FrequentlyUsedMethods(NewMessageActivity.this);
		faq.setActivity(NewMessageActivity.this);
		faq.setContext(NewMessageActivity.this);
	    btnCancelNewMessage = (Button) findViewById(R.id.btnCancelNewMessage);
	    btnPay  = (Button) findViewById(R.id.btnPay);
	    btnAddFilesMessage = (Button) findViewById(R.id.btnAddFilesMessage);
	    btnSendMessage =(Button) findViewById(R.id.btnSendMessage);
	    editMessage =(EditText) findViewById(R.id.editMessage);
	     page = inflater.inflate(R.layout.interactions_item, null);
	     interId = (TextView) page.findViewById(R.id.interactionId);
	     interMess = (TextView) page.findViewById(R.id.interactionMessage);
	     textDate = (TextView) page.findViewById(R.id.interactionDate);
	     emptyList = (TextView) findViewById(R.id.emptyResult);
	     messageFileList = findViewById(R.id.messageFileList);
	     fileslist = (ListView) messageFileList.findViewById(R.id.fileslist);
	     btnFilesRemove = (Button)messageFileList.findViewById(R.id.btnRemoveFiles);
	     textHead  = (CustomTextView)fileslist
 				.findViewById(android.R.id.title); 
	     test1TextView = (CustomTextView) messageFileList.findViewById(android.R.id.title);
	     fileSizeHead = (CustomTextView) messageFileList.findViewById(R.id.fileSize);
	     
	     panelInteractions =  ((RelativeLayout)findViewById(R.id.panelInteractions));
	     deadline = (TextView)findViewById(R.id.deadlineLabel);
	   	 priceLabel = (TextView)findViewById(R.id.priceLabel);
	     
	     newMessagePanel = (RelativeLayout)findViewById(R.id.newMessagePanel);
	     if (!FileManagerActivity.getFinalMessageFiles().isEmpty())
	     {
	    	 addFiles();
	     }
	     
	     prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	     prefEditor = prefs.edit();

	     
	     btnCancelNewMessage.setOnClickListener(new View.OnClickListener() {
	           public void onClick(View view) {
	        	   
	        	   Intent frequentMessages = new Intent(getParent(), InteractionsActivityViewPager.class);
		             MainTabGroup parentActivity = (MainTabGroup)getParent();
		             parentActivity.startChildActivity("FrequentMessageActivity", frequentMessages);
	               
	           }
	       });

	     btnAddFilesMessage.setOnClickListener(new View.OnClickListener() {
	    	   
	           public void onClick(View view) {
	                Intent i = new Intent(getApplicationContext(),
	                       FileManagerActivity.class); 
	                Bundle mBundle = new Bundle();
	                mBundle.putString("FileManager", "NewMessage");
	                i.putExtras(mBundle);
	                startActivityForResult(i,5);
	               
	           }
	       });
	   
	    btnSendMessage.setOnClickListener(new View.OnClickListener() {
	    	   
	           public void onClick(View view) {
	        	   if (editMessage.getText().toString().length()<7)
	        	   {
	        		   Toast.makeText(NewMessageActivity.this, "Your message should be longer than 7 symbols", Toast.LENGTH_LONG).show();
	        	   }
	        	   else
	        	   {
		             boolean isOnline = faq.isOnline();
		             if (isOnline)
		             {	 
		            	 newMessage =  editMessage.getText().toString();
	        		  SendMessageAsync.execute(NewMessageActivity.this, NewMessageActivity.this);
		              prefEditor.remove("messageBody");
		              prefEditor.commit();
		             }
	        	   }
	           }
	       });
	    if (!prefs.getAll().isEmpty())
        {
			CharSequence messageBody=prefs.getString("messageBody", null);
			
			
			if (messageBody != null)
			{
				editMessage.setText(messageBody);
			}
			else
				editMessage.setText("");
        }
	    
	 
	    btnFilesRemove.setOnClickListener(new View.OnClickListener() {
         	 
            public void onClick(View view) {
         for(int i=0;i<checks.size();i++){
        	 
          if(checks.get(i)==1){
        	   adapter.remove(adapter.getItem(i));
                checks.remove(i);
                test1TextView.setText(Integer.toString(FileManagerActivity.getFinalMessageFiles().size())+ " files attached");
                long wholeSize = 0;
                for (File file: FileManagerActivity.getFinalMessageFiles())
                {
                	wholeSize += file.length();
                }
                fileSizeHead.setText(Long.toString(wholeSize/1024)+ " KB");
                
                 i--;
              }
          if( FileManagerActivity.getFinalMessageFiles().isEmpty())
           	  newMessagePanel.removeView(messageFileList);
              fileListExist  = false;
            }

          }
       });
	
	}
	
	 public void updateLayout()
	    {
	    	if (DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId()==2 | DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId()==3)
	    	{
	    		 priceLabel.setText("N/A");
	    	}
	    	else
	    	 { 
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
				   	    	Log.i("process status is not 4", DashboardActivityAlt.listItem.getProcess_status().getProccessStatusTitle());
				   	    	btnPay.setVisibility(View.VISIBLE);
				   	    	btnPay.setClickable(false);
				   	    	btnPay.setEnabled(false);
				   	    	Log.i("Interactions activity item status", DashboardActivityAlt.listItem.getProcess_status().getProccessStatusTitle());
				   	    	
				   	  }
		      deadline.setText(DashboardActivityAlt.listItem.getDeadline().toString());
	    	
	    }
	 public int getFilePosition(View v)
	 {
		 int pos = 0;
		for (int i = 0; i<adapter.getCount();i++)
		{
			if (((CustomTextView)v.findViewById(android.R.id.title)).getText().equals((adapter.getItem(i).getName())))
			 pos = i;
		}
		 return pos;
	 }
	 OnLongClickListener filesClicklistener = new OnLongClickListener() {
			public boolean onLongClick(View arg0) {
				final CharSequence[] items = {"Open", "Delete", "Details"};
				final AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
			    builder.setTitle((((CustomTextView)arg0.findViewById(android.R.id.title))).getText().toString());
			    Log.i("view class", arg0.getClass().getName());
			    final int pos = getFilePosition(arg0);
			    Log.i("position ", Integer.toString(pos));
			    Integer position  = (Integer) ((CustomTextView)arg0.findViewById(android.R.id.title)).getTag();
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	if (item == 0)
				    	{
				    		File file = FileManagerActivity.getFinalMessageFiles().get(pos);
				    			Intent intent = new Intent();
				    			intent.setAction(android.content.Intent.ACTION_VIEW);
				    			intent.setDataAndType(Uri.fromFile(file), "text/plain");
				    			startActivity(intent);
				    			Log.i("file position in new Order open", Integer.toString(pos));
							
				    	}
				    	 else if (item == 1)
				    		{  
				    		 
				    		   if (FileManagerActivity.getFinalMessageFiles().size()==1)
				    		   {  
				    			   FileManagerActivity.getFinalMessageFiles().clear();
				    			   adapter.clear();
				    			   adapter.notifyDataSetChanged();
				    			   newMessagePanel.removeView(messageFileList);
				    		   }
				    		   else
				    			   { 
				    				   try
				    				   {
				    					   
					    				     adapter.remove(FileManagerActivity.getFinalMessageFiles().get(pos));
					    				     textHead.setText(Integer.toString(FileManagerActivity.getFinalMessageFiles().size())+ " files attached");
					    	                    long wholeSize = 0;
					    	                    for (File file: FileManagerActivity.getFinalMessageFiles())
					    	                    {
					    	                    	wholeSize += file.length();
					    	                    }
					    	                    fileSizeHead.setText(Long.toString(wholeSize/1024)+ " KB");
							    		     adapter.notifyDataSetChanged();
				    				   }
				    				   catch(IndexOutOfBoundsException e)
				    				   {
				    					 //  FileManagerActivity.getFinalAttachFiles().remove(position.intValue());
				    					 //  Log.i("exception deleted position",FileManagerActivity.getFinalAttachFiles().get(position.intValue()).getName() );
				    					   e.printStackTrace();
				    				   }
				    				  
				    			   }
				    		}
				    	else if (item == 2)
				    	{
				    		File file = FileManagerActivity.getFinalMessageFiles().get(pos);
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
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    			
				  		  AlertDialog alert = builder2.create();
				  			alert.show();
				    	}
				    	
				    }
				});
				AlertDialog alert = builder.create();
				alert.show();
				return true;
			}
		};
	private ArrayAdapter<File> adapter;
	private boolean trigger =false;
	public void updateFileList(ArrayList<File> fileList)
	{
		for (int i = 0;i<fileList.size();i++)
		{
			View child = layout.getChildAt(i);
			if (child instanceof TextView)
			{
				child.setTag(i);
				child.setId(i);
			}
		}
	}
	public boolean getTrigger()
	{return this.trigger;}
	public void setTrigger(boolean trigger)
	{this.trigger = trigger;}
	public void addFiles()
    {
		Log.i("NewMessAct","in addFiles method");
		try{
			ViewGroup row = (ViewGroup) newMessagePanel;
			Log.i("asdasda",Integer.toString(row.getChildCount()));
			for (int itemPos = 0; itemPos < row.getChildCount(); itemPos++) {
			    View view = row.getChildAt(itemPos);
			    Log.i("newMessAct", Integer.toString(view.getId()));
			    if (view.getId()==2131427432)
			    	{
			    		Log.i("NewMess","fileListAct exist");
			    	   fileListExist = true;
			    	}
			    
			}
			
		if (!fileListExist)
			 {
				Log.i("NewMessAct", "it might be added");
				newMessagePanel.addView(messageFileList);
			 }
		Log.i("file exist flag",Boolean.toString(fileListExist));
		messageFileList.setVisibility(View.VISIBLE);
		
		test1TextView.setTextSize(17);
		test1TextView.setText(Integer.toString(FileManagerActivity.getFinalMessageFiles().size())+ " files attached");
       long wholeSize = 0;
       for (File file: FileManagerActivity.getFinalMessageFiles())
       {
    	   wholeSize += file.length();
       }
       fileSizeHead.setText(Long.toString(wholeSize/1024)+ " KB");
		adapter = new ArrayAdapter<File>(this,
				R.layout.new_message2, R.id.fileCheck,
				FileManagerActivity.getFinalMessageFiles()) 
				{
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) 
			{
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        View view = inflater.inflate(R.layout.file, null);
	            CustomTextView textView = (CustomTextView)view
						.findViewById(android.R.id.title); 
	            CustomTextView fileSize = (CustomTextView)view
						.findViewById(R.id.fileSize); 
	            final CheckBox checkBox = (CheckBox)view
						.findViewById(R.id.fileCheck);
				textView.setClickable(true);
				
				textView.setText(FileManagerActivity.getFinalMessageFiles().get(position).getName().toString());
				
				fileSize.setText(Long.toString(FileManagerActivity.getFinalMessageFiles().get(position).length()/1024)+ " KB");
				for (int i = 0; i< FileManagerActivity.getFinalMessageFiles().size();i++)
		         {
						textView.setTag(i);
		         }
				
				view.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						
						if (!getTrigger())
							setTrigger(true);
						else 
							setTrigger(false);
						checkBox.setChecked(getTrigger());
						Log.i("checkable state", Boolean.toString(checkBox.isChecked()));
						if (checkBox.isChecked())
							  checks.set(position, 1);
						else
							  checks.set(position, 0);
		            }
		        });
				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked){
							checks.set(position, 1);
			            }
					}
			    });
				textView.setOnLongClickListener(filesClicklistener);
				return view;
			}
		};
		fileslist.setAdapter(adapter);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode != request)
    		return;
    	switch(resultCode){
    case Activity.RESULT_OK:
    	
		resultTitle = "SUCCESS";
		resultInfo = "You have successfully completed this payment.";
		Toast.makeText(NewMessageActivity.this, "Your payment was proceeded successfully", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(getApplicationContext(),
                DashboardActivityAlt.class);
 	  	   startActivity(i);
		break;
	case Activity.RESULT_CANCELED:
		resultTitle = "CANCELED";
		resultInfo = "The transaction has been cancelled.";
		resultExtra = "";
		Toast.makeText(NewMessageActivity.this, "The transaction has been cancelled.", Toast.LENGTH_SHORT).show();
		break;
	case PayPalActivity.RESULT_FAILURE:
		resultTitle = "FAILURE";
		resultInfo = data.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
		resultExtra = "Error ID: " + data.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
		Toast.makeText(NewMessageActivity.this, "Failure! "+ resultInfo, Toast.LENGTH_SHORT).show();
	case 4:
		//addFiles();
		Log.i("messages files length", Integer.toString(FileManagerActivity.getFinalMessageFiles().size()));
		break;
	case 5:
		Intent intent = getIntent();
		finish();
		startActivity(intent);
		addFiles();
		
		
    	}
    
    }
	@Override
	public void onPause() {
		super.onPause();
	}
	@Override
    public void onBackPressed() {
		Intent i = new Intent(getApplicationContext(),
                InteractionsActivityViewPager.class);
        startActivity(i);
    }
	public void onLoadFinished(Object data) {
		
		if (data instanceof String)
		{
			if (((String)data).equalsIgnoreCase("success"))
			{
	             MainTabGroup parentActivity = (MainTabGroup)getParent();
	             TaskProgressDialogFragment.cancel();
	             parentActivity.onBackPressed();
	        }
			else if(((String)data).equalsIgnoreCase("error"))
			{
				  Log.i("new message error"," it occurs");
				 finish();
			     Toast.makeText(NewMessageActivity.this, SendMessageAsync.messageErrorMess, Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			faq.setupButtons(NewMessageActivity.this, panelInteractions);
		}
		
	}
	public void onCancelLoad() {
		 	faq.showFailure(NewMessageActivity.this);
	}
	@Override
	public void onResume()
	{
		 InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		super.onResume();
		Log.i("newMessAct","in onResume method");
	    updateLayout();
	    if (!FileManagerActivity.getFinalMessageFiles().isEmpty())
	     {
	    	 for(int b=0;b<FileManagerActivity.getFinalMessageFiles().size();b++)
		 		{ 
		 			checks.add(b,0);
		 	    } 
	    	 addFiles();
	     }
	}


}
