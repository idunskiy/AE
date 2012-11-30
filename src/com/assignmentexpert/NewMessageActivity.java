package com.assignmentexpert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.datamodel.Messages;
import com.datamodel.Order;
import com.library.FrequentlyUsedMethods;
import com.library.ResultPayPalDelegate;
import com.library.UserFunctions;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalPayment;
public class NewMessageActivity extends Activity{
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
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
	    super.onCreate(savedInstanceState);
	    this.setContentView(R.layout.new_message2);
	    
	    cxt = this;
	    scroll = (HorizontalScrollView) findViewById(R.id.scrollMessageFiles);
	    pager = (ViewPager) findViewById(R.id.conpageslider);
	  
	    
	    TextView deadline = (TextView)findViewById(R.id.deadlineLabel);
	    TextView priceLabel = (TextView)findViewById(R.id.priceLabel);
	    LayoutInflater inflater = LayoutInflater.from(cxt);
		faq = new FrequentlyUsedMethods();
	    btnCancelNewMessage = (Button) findViewById(R.id.btnCancelNewMessage);
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
	     
	     panelInteractions =  ((RelativeLayout)findViewById(R.id.panelInteractions));
	     addFiles();
	     
	     prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	     prefEditor = prefs.edit();
	     if (!faq.payPalActivate())
		    {
		    	btnPay.setVisibility(View.VISIBLE);
		    	btnPay.setClickable(false);
		    	btnPay.setEnabled(false);
		    	btnPay.setBackgroundColor(Color.GRAY);
		    	
		    }
		    else new PayPalInitialize().execute();
	     
			
	     editMessage.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                int heightDiff = editMessage.getRootView().getHeight() - editMessage.getHeight();
	                if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
	                	if (!(editMessage.getText().toString().equals("")))
	                	{
	                		prefEditor.putString("messageBody",editMessage.getText().toString());
	                		prefEditor.commit();
	    		        	
	                		
	                	}
	                }
	             }
	        });
	     if (DashboardActivityAlt.messagesExport.isEmpty())
		    {
		    	  deadline.setText(DashboardActivityAlt.listItem.getDeadline().toString());
		    	  
		    }
		    else
		    {   
		    	priceLabel.setTextColor(Color.GREEN);
			    priceLabel.setText(Float.toString(DashboardActivityAlt.listItem.getPrice()));
			    deadline.setText(DashboardActivityAlt.listItem.getDeadline().toString());
		    } 
	    btnClose.setOnClickListener(new View.OnClickListener() {
	    	   
	           public void onClick(View view) {
	               Intent i = new Intent(getApplicationContext(),
	                       DashboardActivityAlt.class);
	               startActivity(i);
	               
	           }
	       });
	    
	    btnCancelNewMessage.setOnClickListener(new View.OnClickListener() {
	    	   
	           public void onClick(View view) {
	               Intent i = new Intent(getApplicationContext(),
	                       InteractionsActivityViewPager.class);
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
	        	   if (editMessage.getText().toString().length()<7)
	        	   {
	        		   Toast.makeText(NewMessageActivity.this, "Your message should be longer than 7 symbols", Toast.LENGTH_LONG).show();
	        	   }
	        	   else
	        	   {
		              new SendMessageTask().execute();
		              prefEditor.remove("messageBody");
		              prefEditor.commit();
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
	    
	}
	
	
	OnLongClickListener onclicklistener = new OnLongClickListener() {

	
		public boolean onLongClick(final View arg0) {
			final CharSequence[] items = {"Open", "Delete", "Details"};
			final AlertDialog.Builder builder = new AlertDialog.Builder(NewMessageActivity.this);
		    builder.setTitle(((TextView)arg0).getText().toString());
		     //Integer position  = (Integer) ((TextView)arg0).getTag();
			builder.setItems(items, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	 Integer position  = (Integer) ((TextView)arg0).getTag();
			    	if (item == 0)
			    	{
			    		File file = FileManagerActivity.getFinalAttachFiles().get(position.intValue());
			    		
			    			//Scanner input = new Scanner(new File(filesDir, file.getName()));
			    			//Environement.getExternalStorageDirectory().getAbsolutePath() + "/" + file;
			    			Intent intent = new Intent();
			    			intent.setAction(android.content.Intent.ACTION_VIEW);
			    			
			    			intent.setDataAndType(Uri.fromFile(file), "text/plain");
			    			startActivity(intent); 
						
			    	}
			    	 else if (item == 1)
			    		{  
			    		 
			    		   if (FileManagerActivity.getFinalAttachFiles().size()==1)
			    		   { 
			    			   Log.i("the size is 1", "is going to clear");
			    			   FileManagerActivity.getFinalAttachFiles().clear();
			    			   layout.removeAllViewsInLayout();
			    			   layout.invalidate();
			    		   }
			    		   else
			    		   { 
			    				   try
			    				   {
			    					 
			    						 FileManagerActivity.getFinalAttachFiles().remove(position.intValue());
			    					    ((LinearLayout)arg0.getParent()).removeView(arg0);
				    				     layout.invalidate();
						    		     updateFileList( FileManagerActivity.getFinalAttachFiles());
						    		     
						    		     Log.i("count",Integer.toString(FileManagerActivity.getFinalAttachFiles().size()));
					    		     
			    				   }
			    				   catch(IndexOutOfBoundsException e)
			    				   {
			    					 //  FileManagerActivity.getFinalAttachFiles().remove(position.intValue());
			    					 //  Log.i("exception deleted position",FileManagerActivity.getFinalAttachFiles().get(position.intValue()).getName() );
			    					   e.printStackTrace();
			    				   }
			    				  
			    				   for (File f :  FileManagerActivity.getFinalAttachFiles())
					    		     {
					    		    	 Log.i("file container", f.getName());
					    		     } 
			    				   Log.i("file manager size", Integer.toString( FileManagerActivity.getFinalAttachFiles().size()));
			    			   }
			    		}
			    	else if (item == 2)
			    	{
			    		File file = FileManagerActivity.getFinalAttachFiles().get(position.intValue());
			    		final AlertDialog.Builder builder = new AlertDialog.Builder(NewMessageActivity.this);
			  		    builder.setTitle(file.getName());
			  		  FileInputStream fis;
					try {
						fis = new FileInputStream(file);
						builder.setMessage("Size of file is: " + Long.toString(fis.getChannel().size())+ "  KB"+"\r\n"+
						"Path of file is: " +"\r\n" + file.getPath());
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    			
			  		    
			  		  AlertDialog alert = builder.create();
			  			alert.show();
			    	}
			    	
			    	 
			    }
			});
			AlertDialog alert = builder.create();
			alert.show();
			return false;
		}
	};
	public void updateFileList(ArrayList<File> fileList)
	{
		Log.i("linear layout count", Integer.toString(layout.getChildCount()));
		for (int i = 0;i<fileList.size();i++)
		{
			View child = layout.getChildAt(i);
			if (child instanceof TextView)
			{
				child.setTag(i);
				child.setId(i);
				Log.i("child class", child.getClass().getName());
			}
		}
	}
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
		                tv[i].setTag(i);
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
 	  	   	Bundle mBundle = new Bundle();
 	  		mBundle.putString("NewMessage", "wasAdded");
        	i.putExtras(mBundle);
 	  	   startActivity(i);
            // Pass the result data back to the main activity

  
            
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
    	}
    
    }
	private class PayPalInitialize extends AsyncTask<Void, Void, Integer> {
	    
	
		protected void onPreExecute() {
        	progDailog = ProgressDialog.show(NewMessageActivity.this,"Please wait...", "PayPal library initializing ...", true);
        }

        protected Integer doInBackground(Void... args) {
        Integer result = 0;
       	try 
       	{
       		
       		 faq.setActivity(NewMessageActivity.this);
       		 faq.setContext(NewMessageActivity.this);
       		boolean res =faq.initLibrary(NewMessageActivity.this);
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
        	 faq.setupButtons(NewMessageActivity.this, panelInteractions);
         else if (result.equals(INITIALIZE_FAILURE))
        	 faq.showFailure(NewMessageActivity.this);
        }
		
   }


}
