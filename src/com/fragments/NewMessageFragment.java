package com.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.FileManagerActivity;
import com.assignmentexpert.R;
import com.customitems.CustomTextView;
import com.library.FrequentlyUsedMethods;
import com.paypal.android.MEP.PayPalActivity;

public class NewMessageFragment extends Fragment implements IClickListener{
	IClickListener listener;
	private View page;
	private Button btnPay;
	private Button btnAddFilesMessage;
	private Button btnSendMessage;
	private EditText editMessage;
	private TextView interId;
	private TextView interMess;
	private TextView textDate;
	private TextView emptyList;
	private View messageFileList;
	private ListView fileslist;
	private Button btnFilesRemove;
	private CustomTextView textHead;
	private CustomTextView test1TextView;
	private CustomTextView fileSizeHead;
	private RelativeLayout panelInteractions;
	private TextView deadline;
	private TextView priceLabel;
	private RelativeLayout newMessagePanel;
	private View btnCancelNewMessage;
	private FrequentlyUsedMethods faq;
	private SharedPreferences.Editor prefEditor;
	private SharedPreferences prefs;
	public static String newMessage;
	private ArrayList<Integer> checks = new ArrayList<Integer>();
	boolean fileListExist  = true;
	private ScrollView scrollView;
	private static final int request = 1;
	public static String resultTitle;
	public static String resultInfo;
	public static String resultExtra;
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.new_message2,
	        container, false);
	     btnPay  = (Button) view.findViewById(R.id.btnPay);
	     scrollView =(ScrollView)view.findViewById(R.id.scrollNewMessage);
	     btnAddFilesMessage = (Button) view.findViewById(R.id.btnAddFilesMessage);
	     btnSendMessage =(Button) view.findViewById(R.id.btnSendMessage);
	     editMessage =(EditText) view.findViewById(R.id.editMessage);
	     page = inflater.inflate(R.layout.interactions_item, null);
	     interId = (TextView) page.findViewById(R.id.interactionId);
	     interMess = (TextView) page.findViewById(R.id.interactionMessage);
	     textDate = (TextView) page.findViewById(R.id.interactionDate);
	     emptyList = (TextView) view.findViewById(R.id.emptyResult);
	     messageFileList = view.findViewById(R.id.messageFileList);
	     fileslist = (ListView) messageFileList.findViewById(R.id.fileslist);
	     btnFilesRemove = (Button)messageFileList.findViewById(R.id.btnRemoveFiles);
	     textHead  = (CustomTextView)fileslist
 				.findViewById(android.R.id.title); 
	     test1TextView = (CustomTextView) messageFileList.findViewById(android.R.id.title);
	     fileSizeHead = (CustomTextView) messageFileList.findViewById(R.id.fileSize);
	     
	     panelInteractions =  ((RelativeLayout)view.findViewById(R.id.panelInteractions));
	     deadline = (TextView)view.findViewById(R.id.deadlineLabel);
	   	 priceLabel = (TextView)view.findViewById(R.id.priceLabel);
	   	 
	   	 btnCancelNewMessage = (Button)view.findViewById(R.id.btnCancelNewMessage);

		 faq = new FrequentlyUsedMethods(getActivity());
		 prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		 prefEditor = prefs.edit();
	     newMessagePanel = (RelativeLayout)view.findViewById(R.id.newMessagePanel);
	     btnCancelNewMessage.setOnClickListener(new View.OnClickListener() {
	           public void onClick(View view) {
	        	   
	        	 listener.changeFragment(9);
	               
	           }
	       });

	     btnAddFilesMessage.setOnClickListener(new View.OnClickListener() {
	    	   
	           public void onClick(View view) {
	                Intent i = new Intent(getActivity(),
	                       FileManagerActivity.class); 
	                Bundle mBundle = new Bundle();
	                mBundle.putString("FileManager", "NewMessage");
	                i.putExtras(mBundle);
	                startActivityForResult(i,5);
	               
	           }
	       });
	   
	     
	     editMessage.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Log.i("scrollView ", "scrollView was touched");
	            	Log.i("p[osition", Integer.toString(getEditTextY()));
	        	    scrollView.scrollTo(0, -132);
					
				}
	        }); 
	     editMessage.setOnKeyListener(new EditText.OnKeyListener() {
	         public boolean onKey(View v, int keyCode, KeyEvent event) {
	             if(keyCode==66){
	            	
		            	
	            	 InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
	            		      Context.INPUT_METHOD_SERVICE);
	            		imm.hideSoftInputFromWindow(editMessage.getWindowToken(), 0);
		            	Editable etext = editMessage.getText();
		            	int position = editMessage.getText().toString().length();
		            	Selection.setSelection(etext, position);
	             }
	             return false;
	         }

	 });
//	     editMessage.setOnTouchListener(new View.OnClickListener() {
//	           public void onClick(View view) {
//	        	   Log.i("newMessageFrag", "i'm in onClick");
//	        	   editMessage.setImeOptions(EditorInfo.IME_ACTION_DONE);
//	        	   scrollView.post(new Runnable() { 
//	        	        public void run() { 
//	        	        	scrollView.scrollTo(0, scrollView.getBottom());
//	        	        } 
//	        	});
//	           }
//	       });
	    
	    btnSendMessage.setOnClickListener(new View.OnClickListener() {
	    	   
	           public void onClick(View view) {
	        	   if (editMessage.getText().toString().length()<7)
	        	   {
	        		   Toast.makeText(getActivity(), "Your message should be longer than 7 symbols", Toast.LENGTH_LONG).show();
	        	   }
	        	   else
	        	   {
		             boolean isOnline = faq.isOnline();
		             if (isOnline)
		             {	 
		            	 
		            	 newMessage =  editMessage.getText().toString();
		            	 listener.changeFragment(10);
	        		 // SendMessageAsync.execute(NewMessageActivity.this, NewMessageActivity.this);
		              prefEditor.remove("messageBody");
		              prefEditor.commit();
		             }
	        	   }
	           }
	       });
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
	     updateLayout();
	     if (!FileManagerActivity.getFinalMessageFiles().isEmpty())
	     {
	    	 addFiles();
	     }
	     scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	                int heightDiff = scrollView.getRootView().getHeight() - scrollView.getHeight();
	                if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
	                	if (editMessage.getText().toString().length()!=0)
	                		btnSendMessage.getBackground().setAlpha(255);
	                	else 
	                		btnSendMessage.getBackground().setAlpha(120);
	                }
	             }
	        });

	    return view;
	  }
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        	
            listener = (IClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
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
				   	    	listener.changeFragment(8);
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
				final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
				    		AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
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
		adapter = new ArrayAdapter<File>(getActivity(),
				R.layout.new_message2, R.id.fileCheck,
				FileManagerActivity.getFinalMessageFiles()) 
				{
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) 
			{
				LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
	public void changeFragment(int flag) {
		// TODO Auto-generated method stub
	}
	
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	if(requestCode != request)
	    		return;
	    	switch(resultCode){
	    case Activity.RESULT_OK:
	    	
			resultTitle = "SUCCESS";
			resultInfo = "You have successfully completed this payment.";
			Toast.makeText(getActivity(), "Your payment was proceeded successfully", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(getActivity(),
	                DashboardActivityAlt.class);
	 	  	   startActivity(i);
			break;
		case Activity.RESULT_CANCELED:
			resultTitle = "CANCELED";
			resultInfo = "The transaction has been cancelled.";
			resultExtra = "";
			Toast.makeText(getActivity(), "The transaction has been cancelled.", Toast.LENGTH_SHORT).show();
			break;
		case PayPalActivity.RESULT_FAILURE:
			resultTitle = "FAILURE";
			resultInfo = data.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
			resultExtra = "Error ID: " + data.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
			Toast.makeText(getActivity(), "Failure! "+ resultInfo, Toast.LENGTH_SHORT).show();
		case 4:
			//addFiles();
			Log.i("messages files length", Integer.toString(FileManagerActivity.getFinalMessageFiles().size()));
			break;
		case 5:
			addFiles();
			
			
	    	}
	    
	    }
	 	private int getEditTextY()
	 	{
	 		View globalView = scrollView; // the main view of my activity/application

	 		DisplayMetrics dm = new DisplayMetrics();
	 		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
	 		int topOffset = dm.heightPixels - globalView.getMeasuredHeight();

	 		View tempView = editMessage; // the view you'd like to locate
	 		int[] loc = new int[2]; 
	 		tempView.getLocationOnScreen(loc);

	 		final int y = loc[1] - topOffset;
	 		return y;
	 	}
		@Override
		public void onResume()
		{
		    super.onResume();
		    if (editMessage.getText().toString().length()==0)
		    	btnSendMessage.getBackground().setAlpha(120);
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
