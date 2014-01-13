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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.FileManagerActivity;
import com.assignmentexpert.R;
import com.customitems.CustomTextView;
import com.library.Constants;
import com.library.FrequentlyUsedMethods;
import com.paypal.android.MEP.PayPalActivity;
/** * фрагмент для посылки нового сообщения*/
public class NewMessageFragment extends Fragment implements IClickListener{
	/** * экземпляр интерфейса  IClickListener*/
	IClickListener listener;
	/** * PayPal Button, если PayPal библиотека не активирована(статус заказа не discussion).*/
	private Button btnPay;
	/** * кнопка для добавления файлов*/
	private Button btnAddFilesMessage;
	/** * кнопка для отправки нового сообщения*/
	private Button btnSendMessage;
	/** * EditText для ввода текста нового сообщения*/
	private EditText editMessage;
	/** * View для отображения списка файлов*/
	private View messageFileList;
	/** * ListView для отображения списка файлов*/
	private ListView fileslist;
	/** * кнопка для удаления файлов*/
	private Button btnFilesRemove;
	/** * CustomTextView для отображения общего количества файлов в списке*/
	private CustomTextView textHead;
	/** * CustomTextView для отображения общего размера файлов в списке*/
	private CustomTextView fileSizeHead;
	/** * TextView для отображения срока выполнения заказа*/
	private TextView deadline;
	/** * TextView для отображения цены заказа*/
	private TextView priceLabel;
	/** * RelativeLayout для отображения списка файлов*/
	private LinearLayout newMessagePanel;
	/** * кнопка Cancel*/
	private View btnCancelNewMessage;
	/** * экземпляр класса часто используемых функций*/
	private FrequentlyUsedMethods faq;
	private SharedPreferences.Editor prefEditor;
	private SharedPreferences prefs;
	/** * поле для сохранения строки нового сообщения*/
	public static String newMessage;
	/** * коллекция для сохранения состояний checkbox'ов файлов*/
	private ArrayList<Integer> checks = new ArrayList<Integer>();
	/** * флаг проверки наличия списка файлов. Если контейнер файлов не пуст, то к newMessagePanel добавляется messageFileList, в котором отображается список файлов.*/
	boolean fileListExist  = true;
	/** * обьект ScrollView, который является главным View и используется для определения наличия клавиатуры*/
	private ScrollView scrollView;
	private CheckBox fileCheckHead;
	private Button btnAddPhoto;
	/** * RelativeLayout панели отображения информации по текущему заказу. Используется для setup'a PayPal кнопки. */
	public static RelativeLayout panelInteractions;
	private static final int request = 1;
	/** * строка результа PayPal транзакции */
	public static String resultTitle;
	/** * строка информации PayPal транзакции */
	public static String resultInfo;
	/** * строка дополнительной информации PayPal транзакции */
	public static String resultExtra;
	
	public static Uri mOriginalUri;

	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.new_message2,
	        container, false);
	     btnPay  = (Button) view.findViewById(R.id.btnPay);
	     scrollView =(ScrollView)view.findViewById(R.id.scrollNewMessage);
	     btnAddFilesMessage = (Button) view.findViewById(R.id.btnAddFilesMessage);
	     
//	     btnAddPhoto= (Button) view.findViewById(R.id.btnAddPhoto);
	     
	     btnSendMessage =(Button) view.findViewById(R.id.btnSendMessage);
	     editMessage =(EditText) view.findViewById(R.id.editMessage);
	     messageFileList = view.findViewById(R.id.messageFileList);
	     panelInteractions = (RelativeLayout) view.findViewById(R.id.panelInteractions);
	     fileslist = (ListView) messageFileList.findViewById(R.id.fileslist);
	     btnFilesRemove = (Button)messageFileList.findViewById(R.id.btnRemoveFiles);
	     textHead  = (CustomTextView)messageFileList
 				.findViewById(android.R.id.title); 
	     fileSizeHead = (CustomTextView) messageFileList.findViewById(R.id.fileSize);
	     fileCheckHead = (CheckBox)messageFileList
 				.findViewById(R.id.fileCheck); 
	     
	     
	     deadline = (TextView)view.findViewById(R.id.deadlineLabel);
	   	 priceLabel = (TextView)view.findViewById(R.id.priceLabel);
	   	 
	   	 btnCancelNewMessage = (Button)view.findViewById(R.id.btnCancelNewMessage);

		 faq = new FrequentlyUsedMethods(getActivity());
		 prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		 prefEditor = prefs.edit();
	     newMessagePanel = (LinearLayout)view.findViewById(R.id.newMessagePanel);
//	     if (!FileManagerActivity.getFinalMessageFiles().isEmpty())
//	     {
//	    	 addFiles();
//	     }
	     
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
	                startActivityForResult(i,Constants.addFilesResult);
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
	        		   Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.toast_message_length), Toast.LENGTH_LONG).show();
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
            	if( FileManagerActivity.getFinalMessageFiles().isEmpty())
            	  {
         	  		newMessagePanel.removeView(messageFileList);
         	  		fileCheckHead.setChecked(false);
            	  }
            	else 
            	{
            		Log.i("checks size", Integer.toString(checks.size()));
            		Log.i("files size", Integer.toString(FileManagerActivity.getFinalMessageFiles().size()));
            		Log.i("adapter size", Integer.toString(adapter.getCount()));
			         for(int i=0;i<checks.size();i++){
			          if(checks.get(i)==1){
			        	   adapter.remove(adapter.getItem(i));
			                checks.remove(i);
			                textHead.setText(Integer.toString(FileManagerActivity.getFinalMessageFiles().size())+ " files attached");
			                long wholeSize = 0;
			                for (File file: FileManagerActivity.getFinalMessageFiles())
			                {
			                	wholeSize += file.length();
			                }
			                fileSizeHead.setText(Long.toString(wholeSize/1024)+ " KB");
			                
			                 i--;
			              }
			          
			          		btnAddFilesMessage.setText("Add files");
			              fileListExist  = false;
			            }
            	}

          }
       });
	     
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

	    	fileCheckHead.setOnCheckedChangeListener(new OnCheckedChangeListener()
        	{

    			public void onCheckedChanged(CompoundButton buttonView,
    					boolean isChecked) {
    				if (isChecked)
    				{
    					Log.i(" check id's", Integer.toString(checks.size()));
    					Log.i(" file id's", Integer.toString(FileManagerActivity.getFinalMessageFiles().size()));
    					for (Integer i : checks) {
    						Log.i(" check id's", Integer.toString(i));
    						checks.set(checks.indexOf(i), 1);
    					}
    					for (int i = 0;i<(fileslist).getCount();i++)
    					{
    					
    						View a = (fileslist).getChildAt(i);
    						CheckBox b = (CheckBox) a.findViewById(R.id.fileCheck);
    						b.setChecked(true);
    					}
    					btnFilesRemove.setEnabled(true);
    				}
    				else 
    				{
    					for (Integer i : checks)
    					{
    						checks.set(checks.indexOf(i), 0);
    					}
    					for (int i = 0;i<(fileslist).getCount();i++)
    					{
    					
    						View a = (fileslist).getChildAt(i);
    						CheckBox b = (CheckBox) a.findViewById(R.id.fileCheck);
    						b.setChecked(false);
    					}
    					btnFilesRemove.setEnabled(false);
    					
    				}
    			}
        		
        	});
//	    	btnAddPhoto.setOnClickListener(new OnClickListener() {
//
//				
//				public void onClick(View v) {
//					Intent cameraIntent = new Intent(
//							MediaStore.ACTION_IMAGE_CAPTURE);
//					mOriginalUri = Uri.fromFile(new File(Environment
//							.getExternalStorageDirectory(), "original"
//							+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
//					cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
//							mOriginalUri);
//					cameraIntent.putExtra("return-data", true);
//					startActivityForResult(cameraIntent, 3);
//				}
//			});
	    	updateLayout();
	    return view;
	  }
	/** * инициализация экземпляра IClickListener*/
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
	/** *обновление layout'a*/
	 public void updateLayout()
	    {
	    	if (DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId()==2 | DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId()==3)
	    	{
	    		 priceLabel.setText("N/A");
	    	}
	    	else
	    	 { 
	   		    priceLabel.setText(Float.toString(DashboardActivityAlt.listItem.getPrice())+"$");
	    	 }
	    	//if (DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId()==4 & DashboardActivityAlt.listItem.getPrice()>0)
	    	if (DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId()==2)
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
	 /** *метод получения порядкового номера файла в списке 
	  * @param v  - View, на который было нажатие.
	  * @return номер файла в списке*/
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
			    ((CustomTextView)arg0.findViewById(android.R.id.title)).getTag();
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
				    			   btnAddFilesMessage.setText("Add files");
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
		try{
			for(int b=0;b<FileManagerActivity.getFinalMessageFiles().size();b++)
	 		{ 
	 			checks.add(0);
	 	    } 
			ViewGroup row = (ViewGroup) newMessagePanel;
			for (int itemPos = 0; itemPos < row.getChildCount(); itemPos++) {
			    View view = row.getChildAt(itemPos);
			    if (view.getId()==2131427432)
			    	{
			    	   fileListExist = true;
			    	}
			    
			}
			
		if (!fileListExist)
			 {
				newMessagePanel.addView(messageFileList);
			 }
		messageFileList.setVisibility(View.VISIBLE);
		
		textHead.setTextSize(17);
		textHead.setText(Integer.toString(FileManagerActivity.getFinalMessageFiles().size())+ " files attached");
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
						{
							checks.set(position, 1);
							btnFilesRemove.setEnabled(true);
						}
						else
						{
							checks.set(position, 0);
							int sum =0;
							for (int k=0;k<checks.size();k++)
							{
								sum += checks.get(k).intValue();
							}
							if (sum==0)
								btnFilesRemove.setEnabled(false);
						}
		            }
		        });
				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked){
							checks.set(position, 1);
							btnFilesRemove.setEnabled(true);
			            }
						else 
						{
							checks.set(position, 0);
							int sum =0;
							for (int k=0;k<checks.size();k++)
							{
								sum += checks.get(k).intValue();
							}
							if (sum==0)
								btnFilesRemove.setEnabled(false);
						}
					}
			    });
				textView.setOnLongClickListener(filesClicklistener);
				return view;
			}
		};
		
		fileslist.setAdapter(adapter);
		Log.i("NewMessAct",Integer.toString(adapter.getCount()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if (!FileManagerActivity.getFinalMessageFiles().isEmpty())
			btnAddFilesMessage.setText("Add more files");
    }
	public void changeFragment(int flag) {
		// TODO Auto-generated method stub
	}
	
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 	Log.i("newFragment ActRes", Integer.toString(resultCode));
	    	if(requestCode != request)
	    		return;
	    	switch(resultCode){
	    case Activity.RESULT_OK:
	    	
			resultTitle = "SUCCESS";
			resultInfo = "You have successfully completed this payment.";
			Intent i = new Intent(getActivity(),
	                DashboardActivityAlt.class);
	 	  	   startActivity(i);
			break;
		case Activity.RESULT_CANCELED:
			resultTitle = "CANCELED";
			resultInfo = "The transaction has been cancelled.";
			resultExtra = "";
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_interrupted), Toast.LENGTH_SHORT).show();
			break;
		case PayPalActivity.RESULT_FAILURE:
			resultTitle = "FAILURE";
			resultInfo = data.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
			resultExtra = "Error ID: " + data.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
			Toast.makeText(getActivity(),resultInfo, Toast.LENGTH_SHORT).show();
		case 4:
			//addFiles();
			Log.i("messages files length", Integer.toString(FileManagerActivity.getFinalMessageFiles().size()));
			break;
	    	}
	    
	    }
	 public void addMessageFiles()
	 {
		addFiles();
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
//		    if (!FileManagerActivity.getFinalMessageFiles().isEmpty())
//		     {
//		    	 addFiles();
//		     }
		    else   {
		    	
			}
		}

}
