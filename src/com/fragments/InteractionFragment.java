package com.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.R;
import com.customitems.CustomTextView;
import com.datamodel.Files;
import com.datamodel.Messages;
import com.datamodel.Order;
import com.library.FrequentlyUsedMethods;
import com.library.ServiceIntentMessages;
import com.paypal.android.MEP.CheckoutButton;
import com.tabscreens.DashboardTabScreen;
/** * фрагмент для отображения сообщений выбранного заказа */
public class InteractionFragment extends Fragment implements IClickListener{
	/** * экземпляр класса PageSlider*/
	private PageSlider ps;
	private Intent intent;
	/** * контейнер сообщений*/
	List<Messages> messagesList;
	/** * контейнер заказов*/
	List<Order> ordersFromService;
	/** * кнопка "Cancel"*/
	Button btnClose;
	/** * id сообщения"*/
	TextView interId;
	/** * текст сообщения"*/
	TextView interMess;
	/** * дата сообщения*/
	TextView textDate;
	/** * ArrayAdapter файлов */
	private ArrayAdapter<Files> fileAdapter;
	/** * ViewPager для списка сообщений */
	private ViewPager pager;
	/** * CustomTextView для отображения предыдущего номера сообщения в списке*/
	private CustomTextView currMess1;
	/** * CustomTextView для отображения текущего номера сообщения в списке*/
	private CustomTextView currMess2;
	/** * CustomTextView для отображения следующего номера сообщения в списке*/
	private CustomTextView currMess3;
	/** * Button для посылки сообщения на сервер*/
	private Button btnReply;
	/** * Button для деактивации заказа*/
	private Button btnInactivate;
	/** * экземпляр интерфейса  IClickListener*/
	private IClickListener listener;
	/** * PayPal Button, если PayPal библиотека не активирована(статус заказа не discussion).*/
	private Button btnPay;
	/** * TextView для отображения срока выполнения заказа */
	private TextView deadline;
	/** * TextView для отображения цены заказа */
	private TextView priceLabel;
	/** * RelativeLayout - панель порядковых номеров заказа*/
	private RelativeLayout messagesPanel;
	/** * TextView - для отображения сообщения при пустом контейнере заказов*/
	private TextView emptyList;
	public static RelativeLayout panelInteractions;
	/** * PayPal Button, если PayPal библиотека активирована(статус заказа discussion).*/
	private static CheckoutButton launchSimplePayment;
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.interactions_view_pager,
	        container, false);
	    ps = new PageSlider();
	    
	    pager = (ViewPager) view.findViewById(R.id.conpageslider);
	    
	     currMess1 =  (CustomTextView) view.findViewById(R.id.cursorMess1);
     	 currMess2 =  (CustomTextView) view.findViewById(R.id.cursorMess2);
     	 currMess3 =  (CustomTextView) view.findViewById(R.id.cursorMess3);
     	 
     	btnReply = (Button)view.findViewById(R.id.btnReply);
   	    btnInactivate = (Button)view.findViewById(R.id.btnInactivate);
   	    
   	    btnPay  = (Button) view.findViewById(R.id.btnPay);
   	    
   		deadline = (TextView)view.findViewById(R.id.deadlineLabel);
   		priceLabel = (CustomTextView)view.findViewById(R.id.priceLabel);
   		panelInteractions =  ((RelativeLayout)view.findViewById(R.id.panelInteractions));
   		messagesPanel = (RelativeLayout)view.findViewById(R.id.messagesPanel);
   		
   		emptyList = (TextView) view.findViewById(R.id.emptyResult);
   		
		// Get the CheckoutButton. There are five different sizes. The text on the button can either be of type TEXT_PAY or TEXT_DONATE.
	//	launchSimplePayment = pp.getCheckoutButton(getActivity(), PayPal.BUTTON_194x37, CheckoutButton.TEXT_PAY);
   		
   		
   		btnReply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	if (!DashboardActivityAlt.listItem.getProcess_status().getProccessStatusTitle().equalsIgnoreCase("uploading"))
            		listener.changeFragment(6);
            	else
            	{
            		
            	}
            }
        });
	    btnInactivate.setOnClickListener(new View.OnClickListener() {
        	 
            public void onClick(View view) {
            	if (!DashboardActivityAlt.listItem.getProcess_status().getProccessStatusTitle().equalsIgnoreCase("uploading"))
            		listener.changeFragment(7);
            	else 
            	{
            		DashboardActivityAlt.forPrint.remove(DashboardActivityAlt.listItem);
            		getActivity().finish();
            		Intent i =new Intent(getActivity(), DashboardTabScreen.class);
            		getActivity().startActivity(i);
            		
            		
            	}
            	//InactivateAsync.execute(InteractionsActivityViewPager.this, InteractionsActivityViewPager.this);
                
            }
        });
	    updateLayout();
	    
	    intent = new Intent(getActivity(), ServiceIntentMessages.class);
	    pager.setOnPageChangeListener(new OnPageChangeListener() {

	        public void onPageScrollStateChanged(int arg0) {
	        }

	        public void onPageScrolled(int arg0, float arg1, int arg2) {
	        	
	        }

	        public void onPageSelected(int currentPage) {
	        	setCurrMessPos(currentPage);
	        	Log.i("interactions","onPageSelected");
	        	Log.i("messages size",Integer.toString(DashboardActivityAlt.messagesExport.size()));
	        	
	        	int counter = DashboardActivityAlt.messagesExport.size() - (currentPage+1);
	        	Log.i("messages counter",Integer.toString(counter));
				     if (currentPage == 0)
				     {
				    	 currMess1.setText("   ");
				    	 currMess2.setText(Integer.toString(1));
				    	 currMess3.setText(Integer.toString(2));
				    	 
				     }
				     else if(currentPage == DashboardActivityAlt.messagesExport.size()-1)
				     {
				    	 currMess1.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()-1));
				    	 currMess2.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()));
				    	 currMess3.setText(" ");
				     }
				     else
				     {
					     currMess1.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()-(counter+1)));
				    	 currMess2.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()-counter));
				    	 currMess3.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()-(counter-1)));
				     }
				     Log.i("pager ", Integer.toString(currentPage));
	        }

	    });
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
	/** * PagerAdapter для отображения сообщений заказа */
	public class PageSlider extends PagerAdapter{
		int count=0;
		private LinearLayout interactionsFilesPanel;
		private ListView fileslist;
		private View page;
		private ArrayAdapter<Files> fileAdapter;
		private ArrayList<Files> messageFiles;
		
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
	    	LayoutInflater inflater = LayoutInflater.from(getActivity());
		     page = inflater.inflate(R.layout.interactions_item, null);
		     
		     interId = (TextView) page.findViewById(R.id.interactionId);
	    	 interMess = (TextView) page.findViewById(R.id.interactionMessage);
	    	 textDate = (TextView) page.findViewById(R.id.interactionDate);
	    	 messageFiles =  (DashboardActivityAlt.messagesExport).get(position).getFiles();
	    	 interactionsFilesPanel = (LinearLayout)page.findViewById(R.id.interactionsFilesPanel);
	    	 fileslist = (ListView)page.findViewById(R.id.fileslist);
	    	 if (!DashboardActivityAlt.messagesExport.isEmpty())
		     {	
	    		 if ((DashboardActivityAlt.messagesExport).get(position).getFiles().isEmpty())
	    			 interactionsFilesPanel.setVisibility(View.GONE);
	    		 else
	    		 {
	    			 interactionsFilesPanel.setVisibility(View.VISIBLE);
	    			 messageFiles =  DashboardActivityAlt.messagesExport.get(position).getFiles();
	    			 Log.i("files size",Integer.toString(messageFiles.size()));
	    			 fileAdapter = new ArrayAdapter<Files>(getActivity(),
	 						R.layout.interactions_item, R.id.fileCheck,messageFiles ) 
	 						{
						@Override
	 					public View getView(int position2, View convertView, ViewGroup parent) 
	 					{
	 				        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
	    		 
	    		 interId.setText("Interaction "+Integer.toString((DashboardActivityAlt.messagesExport).get(position).getMessageId()));
			     interMess.setText((DashboardActivityAlt.messagesExport).get(position).getMessageBody());
			     textDate.setText((DashboardActivityAlt.messagesExport).get(position).getMessageDate().toString());
			   
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
	/** *BroadcastReceiver для обновления списка новыми сообщениями */
	 private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	        	updateUI(intent);   
	        }
	    };
	    @Override
		public void onResume()
		{
		    super.onResume();
		    getActivity().startService(intent);
		    getActivity().registerReceiver(broadcastReceiver, new IntentFilter(ServiceIntentMessages.MESSAGES_IMPORT));
		    getActivity().registerReceiver(broadcastReceiver, new IntentFilter(ServiceIntentMessages.ORDERS_IMPORT));
		}
		@Override
		public void onPause() {
			super.onPause();
			getActivity().unregisterReceiver(broadcastReceiver);
			getActivity().stopService(intent);
			//unregisterReceiver(downloadFilesReceiver);
		}	
		/** *метод обновления layout при получении новых сообщений */
	    private void updateUI(Intent intent)
	    {
	    	Log.i("interactions fragment", "updateUI method");
	    	
	    	 //pager.setAdapter(ps);   
	    	ps.notifyDataSetChanged();
	    	 updateLayout();
	    	 
	    	
	    }
	    
	    public static void updatePayPalBtn()
		 {
			 launchSimplePayment.updateButton();
		 }
	    /** *метод для инверсии порядка размещению сообщений в контейнере. (Для отображения начиная с последнего в списке)
	     * @param orig - список сообщений, принадлежащих текущему заказу
	     * @return контейнер сообщений в обратном порядке.*/
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
	  /** *OnClickListener для списка прикрепленных файлов к сообщению */
	  OnClickListener filesClicklistener = new OnClickListener() {
			public void onClick(View arg0) {
				final CharSequence[] items = {"Open", "Details"};
				final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			    builder.setTitle((((CustomTextView)arg0.findViewById(android.R.id.title))).getText().toString());
			     int pos = getFilePosition(arg0);
			    Files filePos = reverse(DashboardActivityAlt.messagesExport).get(getCurrMessPos()).getFiles().get(pos);
			    final File file = new FrequentlyUsedMethods(getActivity()).findFile(filePos.getFileName());
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
				    	 Toast.makeText(getActivity(), "current file couldn't be finded", Toast.LENGTH_SHORT).show();
				    }
			    }
			    
			}
		};
	

		/** *метод обновления layout */
		    public void updateLayout()
		    {
		    	try{
		    	if(DashboardActivityAlt.listItem.getProcess_status().getProccessStatusTitle().equalsIgnoreCase("uploading"))
		    	{
		    		btnInactivate.setText("Remove order");
		    		btnReply.setText("Try again");
		    	}
		    	if (!DashboardActivityAlt.messagesExport.isEmpty())
		 	    {
		    		 emptyList.setVisibility(View.INVISIBLE);
		    		 messagesPanel.setVisibility(View.VISIBLE);
		    		Log.i("interactions","regular selection");
		 		    if(DashboardActivityAlt.messagesExport.size() == 1)
		 		    {
		 	   		 	currMess2.setText(Integer.toString(1)); 
		 	   	 	}
		 		     else
		 		     {
		 		    	 currMess3.setText("   ");
		 		    	 currMess2.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()));
		 		    	 currMess1.setText(Integer.toString(DashboardActivityAlt.messagesExport.size()-1  ));
		 		     }
		 	    }
		    	else
		    	{
		    		 emptyList.setVisibility(View.VISIBLE);
		    		 messagesPanel.setVisibility(View.GONE);
		    	}
		    	if (DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId()==9)
		    	{
		    		btnReply.setVisibility(View.INVISIBLE);
		    		btnInactivate.setVisibility(View.INVISIBLE);
		    	}
		    	if (DashboardActivityAlt.listItem.getPrice() ==0)
		    		 priceLabel.setText("N/A");
		    	else
		    		priceLabel.setText(DashboardActivityAlt.listItem.getPrice() + "$");
//		    	if (DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId()==2 | DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId()==3)
//		    	{
//			    		 emptyList.setVisibility(View.VISIBLE);
//			    		 messagesPanel.setVisibility(View.GONE);
//			    		 priceLabel.setText("N/A");
//		    	}
//		    	else
//		    	 { 
//			    		emptyList.setVisibility(View.GONE);
//			    		priceLabel.setTextColor(Color.GREEN);
//			   		    priceLabel.setText("$"+Float.toString(DashboardActivityAlt.listItem.getPrice()));
//		    	 }
		    			if (DashboardActivityAlt.listItem.getProcess_status().getProccessStatusId()==2)// & DashboardActivityAlt.listItem.getPrice()>0)
					   	    {
					   	    
					   	    	listener.changeFragment(8);
					   	    	//setupButtons((Context)getActivity(), panelInteractions);
					   	    	btnPay.setVisibility(View.GONE);
					   	    
					   	    }
					   	else
					   	  {
					   	    	btnPay.setVisibility(View.VISIBLE);
					   	    	btnPay.setClickable(false);
					   	    	btnPay.setEnabled(false);
					   	  }
	   		   deadline.setText(DashboardActivityAlt.listItem.getDeadline().toString());
	   		   pager.setAdapter(ps);
	   		   pager.setCurrentItem(DashboardActivityAlt.messagesExport.size());
	   		 Log.i("interactions pager", Integer.toString(DashboardActivityAlt.messagesExport.size()));
		    	}
		    	catch(Exception  e)
		    	{
		    		e.printStackTrace();
		    	}
		    }
	
		    private int currentMessPos;
		    /** *метод получения позиции файла в списке, принадлежащего конкретному сообщению */
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
		 /** *метод получения позиции текущего сообщения */
		 public int getCurrMessPos()
			{
				return this.currentMessPos;
			}
		 /** *метод установки позиции текущего сообщения */
			public void setCurrMessPos(int currPoss)
			{
				this.currentMessPos = currPoss;
			}

			public void changeFragment(int flag) {
				// TODO Auto-generated method stub
				
			}
			   
}
