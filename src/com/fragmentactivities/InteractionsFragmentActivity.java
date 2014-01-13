package com.fragmentactivities;

import java.io.File;
import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.FileManagerActivity;
import com.assignmentexpert.R;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.PayPalAsync;
import com.asynctasks.SendMessageAsync;
import com.datamodel.Messages;
import com.fragments.IClickListener;
import com.fragments.InteractionFragment;
import com.fragments.NewMessageFragment;
import com.library.Constants;
import com.library.FrequentlyUsedMethods;
import com.library.ResultPayPalDelegate;
import com.library.UserFunctions;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalPayment;
import com.tabscreens.DashboardTabScreen;
import com.tabscreens.OrderInfoTabScreen;
/** * FragmentActivity списка сообщений заказа и отправки нового сообщени€ по заказу. ќперерует фрагментами InteractionFragment, NewMessageFragment
 * @see InteractionFragment
 * @see NewMessageFragment  */
public class InteractionsFragmentActivity extends FragmentActivity implements ITaskLoaderListener, IClickListener{
	/** * экземпл€р класса FragmentTransaction дл€ проведени€ оперций над обьектами Fragment	  */
	private FragmentTransaction fragmentTransaction;
	/** * экземпл€р класса часто используемых функций FrequentlyUsedMethods. »спользуетс€ дл€ деактивации заказа и инициализации PayPal библиотеки.	  */
	private FrequentlyUsedMethods faq;
	/** * PayPal кнопка  */
	private CheckoutButton launchSimplePayment;
	private static final int request = 1;
	
	String resultTitle;
	String resultInfo;
	String resultExtra;
	
	
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	     
	      getWindow().setSoftInputMode(
	    	      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	      setContentView(R.layout.profile_fragment);
	      Fragment newFragment = new InteractionFragment();
	      FragmentManager fragmentManager = getSupportFragmentManager();
	      fragmentTransaction = fragmentManager.beginTransaction();
	      fragmentTransaction.add(R.id.myfragment, newFragment);
	      fragmentTransaction.commit();
	      faq = new FrequentlyUsedMethods(this);
	      
		}
	/** *метод дл€ переключени€ фрагментов внутри активности и вызова операций в отдельном потоке(SendMessageAsync,InactivateAsync ).   */
		public void changeFragment(int flag) {
			if (flag==6)
			{
				  Fragment newFragment = new NewMessageFragment();
				  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			      fragmentTransaction.replace(R.id.myfragment, newFragment);
			      fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			      fragmentTransaction.commit();
			}
			if (flag==7)
			{
				Log.i("profile Act", "change");
				faq.inactivateOrder();
				 
			}
			if (flag==8)
			{
				PayPalAsync.execute(this, this);
			
			}
			
			if (flag==9)
			{
				  Fragment newFragment = new InteractionFragment();
				  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			      fragmentTransaction.replace(R.id.myfragment, newFragment);
			      fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			      fragmentTransaction.commit();
			}
			
			// new message sending
			if (flag ==10)
			{
				 Fragment newFragment = new InteractionFragment();
				  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			      fragmentTransaction.replace(R.id.myfragment, newFragment);
			      fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			      fragmentTransaction.commit();
//			      FileManagerActivity.getFinalMessageFiles().clear();
			      Messages message = new Messages();
			      message.setMessageBody(NewMessageFragment.newMessage);
			      message.setMessageDate(DashboardActivityAlt.listItem.getDeadline());
			      
			      DashboardActivityAlt.messagesExport.add(message);
					
			      new SendMessage().execute();
//	              SendMessageAsync.execute(this, this);
			}
			
		}
		
		/** *метод дл€ setup'a PayPal кнопки  */
	    public void setupButtons(Context arg, RelativeLayout panelInteractions) {
				PayPal pp = PayPal.getInstance();
				
				if( pp.isLibraryInitialized())
					Log.i("interaction frag", "library is initialized");
				arg = (this);
				if(arg != null)
					launchSimplePayment = pp.getCheckoutButton(arg, PayPal.BUTTON_194x37, CheckoutButton.TEXT_PAY);
				// You'll need to have an OnClickListener for the CheckoutButton. For this application, MPL_Example implements OnClickListener and we
				// have the onClick() method below.
				Log.i("now I'm in this Activity",  arg.getClass().getName());
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int) this.getResources().getDimension(R.dimen.paypal_width), LayoutParams.WRAP_CONTENT);
				lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				lp.leftMargin =(int) this.getResources().getDimension(R.dimen.paypal_left_margin); 
				lp.topMargin = (int) this.getResources().getDimension(R.dimen.paypal_top_margin);
				
				launchSimplePayment.setOnClickListener(mCorkyListener);
				// The CheckoutButton is an android LinearLayout so we can add it to our display like any other View.
				panelInteractions.addView(launchSimplePayment,lp);
				
			}
	    /** *OnClickListener дл€ обработки нажатий на кнопку PayPal   */
		    private OnClickListener mCorkyListener = new OnClickListener() {
			    public void onClick(View v) {
			    	Log.i("interact fragment paypal", "I was clicked");
			    	
			    	 launchSimplePayment.updateButton();
			    PayPalPayment newPayment = new PayPalPayment();
		      	Log.i("item current price" , Float.toString(DashboardActivityAlt.listItem.getPrice()));
		      	newPayment.setSubtotal(new BigDecimal(Float.toString(DashboardActivityAlt.listItem.getPrice())));
		      	newPayment.setCurrencyType("USD");
		      	newPayment.setRecipient("paygbpalbiz@gmail.com");
		      	newPayment.setMerchantName("FreelanceSystems LTD");
		      	newPayment.setPaymentType(PayPal.PAYMENT_TYPE_SERVICE);
		      	PayPalInvoiceData invoice = new PayPalInvoiceData();
		        	invoice.setTax(new BigDecimal("0"));
		      	  invoice.setShipping(new BigDecimal("0"));
		      	 newPayment.setInvoiceData(invoice);
		      	newPayment.setCustomID(Integer.toString(DashboardActivityAlt.listItem.getOrderid()));
		      	Intent paypalIntent = PayPal.getInstance().checkout(newPayment, InteractionsFragmentActivity.this ,new ResultPayPalDelegate());
			    InteractionsFragmentActivity.this.startActivityForResult(paypalIntent, request);
			    }
		   };

		   public void onActivityResult(int requestCode, int resultCode, Intent data) {
			   FragmentManager fragmentManager = getSupportFragmentManager();
//		    	if(requestCode != request)
//		    		return;
				/**
		    	 * If you choose not to implement the PayPalResultDelegate, then you will receive the transaction results here.
		    	 * Below is a section of code that is commented out. This is an example of how to get result information for
		    	 * the transaction. The resultCode will tell you how the transaction ended and other information can be pulled
		    	 * from the Intent using getStringExtra.
		    	 */
		    	switch(resultCode)
		    	{
		  case Activity.RESULT_OK:
			  	faq.updateOrderFields(DashboardActivityAlt.listItem);
				resultTitle = "SUCCESS";
				resultInfo = "You have successfully completed this payment.";
				 Intent i = new Intent(getApplicationContext(),
	 	                    DashboardTabScreen.class);
	 	     	  	  startActivity(i);
				break;
			case Activity.RESULT_CANCELED:
//				Log.i("getAct FAQ", faq.getActivity().getClass().toString());
				faq.setActivity(this);
				resultTitle = "CANCELED";
				resultInfo = "The transaction has been cancelled.";
				resultExtra = "";
				Toast.makeText(InteractionsFragmentActivity.this, getResources().getString(R.string.error_interrupted), Toast.LENGTH_SHORT).show();
				
				//InteractionFragment.updatePayPalBtn();
				break;
			case PayPalActivity.RESULT_FAILURE:
				resultTitle = "FAILURE";
				resultInfo = data.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
				resultExtra = "Error ID: " + data.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
				Toast.makeText(InteractionsFragmentActivity.this, resultInfo, Toast.LENGTH_SHORT).show();
				
			case Constants.addFilesResult:
				
				Fragment currentFragment = fragmentManager.findFragmentById(R.id.myfragment);
				Log.i("interactions act", currentFragment.getClass().toString());
				if (currentFragment instanceof NewMessageFragment)
				{
					((NewMessageFragment)currentFragment).addMessageFiles();
				}
				
				// files through camera adding
			case 3:
			{
				
				Fragment currentFragment2 = fragmentManager.findFragmentById(R.id.myfragment);
				if (resultCode != RESULT_OK)
					return;
				File imgFile = new File(NewMessageFragment.mOriginalUri.getPath());
				if (imgFile.exists()) {

					FileManagerActivity.getFinalMessageFiles().add(imgFile);
					((NewMessageFragment)currentFragment2).addMessageFiles();
					// adapter.notifyDataSetChanged();

				}
			}
			break;
			}
			
				
		   }
		    
		   /** *метод интерфейса ITaskLoaderListener. служит дл€ обработки данных после успешной работы задачи в отдельном потоке, используемой в данной активности.  */   
	public void onLoadFinished(Object data) {
		if (data instanceof Integer)
			 {
			FragmentManager fragmentManager = getSupportFragmentManager();
			Fragment currentFragment = fragmentManager.findFragmentById(R.id.myfragment);
			Log.i("interactions act", currentFragment.getClass().toString());
				if (currentFragment instanceof InteractionFragment)
					{
						setupButtons(this, InteractionFragment.panelInteractions);
					}
				else if(currentFragment instanceof NewMessageFragment)
					{
						setupButtons(this, NewMessageFragment.panelInteractions);
					}
			 }
		else if(data instanceof String )
		{
			
			// successful message receiving 
			if (((String) data).equalsIgnoreCase("send_message_success"))
			{
			  Fragment newFragment = new InteractionFragment();
			  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		      fragmentTransaction.replace(R.id.myfragment, newFragment);
		      fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		      fragmentTransaction.commit();
		      FileManagerActivity.getFinalMessageFiles().clear();
		      Messages message = new Messages();
		      message.setMessageBody(SendMessageAsync.getMessageBody());
		      message.setMessageDate(DashboardActivityAlt.listItem.getDeadline());
		      DashboardActivityAlt.messagesExport.add(message);
		      
			}
			if (((String) data).equalsIgnoreCase("inactivate_success"))
			{
				  OrderInfoTabScreen parentActivity;
		            parentActivity = (OrderInfoTabScreen) this.getParent();
		            parentActivity.finish();
		            //parentActivity.switchTab(1);
			}
			if(((String) data).equalsIgnoreCase("send_message_error"))
			{
				  Fragment newFragment = new InteractionFragment();
				  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			      fragmentTransaction.replace(R.id.myfragment, newFragment);
			      fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			      fragmentTransaction.commit();
			      FileManagerActivity.getFinalMessageFiles().clear();
			      Toast.makeText(this, getResources().getString(R.string.error_server_problem), Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	/** *метод интерфейса ITaskLoaderListener. служит дл€ обработки данных после неуспешной работы задачи в отдельном потоке, используемой в данной активности.  */
	public void onCancelLoad() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment currentFragment = fragmentManager.findFragmentById(R.id.myfragment);
		if (currentFragment instanceof NewMessageFragment)
		{
			Fragment newFragment = new InteractionFragment();
			  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		      fragmentTransaction.replace(R.id.myfragment, newFragment);
		      fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		      fragmentTransaction.commit();
		      FileManagerActivity.getFinalMessageFiles().clear();
		}
		
	}
	
	  private class SendMessage extends AsyncTask<Void, Void, JSONObject > {
		    
	    	JSONObject response ;
			private UserFunctions userFunc = new UserFunctions();
	    	protected void onPreExecute() {
	        }

	        protected JSONObject doInBackground(Void... args) {
          try {
	         		
        	  Log.i(" message files size", Integer.toString( FileManagerActivity.getFinalMessageFiles().size()));
        	       if (DashboardActivityAlt.listItem.getProduct().getProductType().equalsIgnoreCase("assignment"))
	         	       		{
	  					response = userFunc.sendMessage(Integer.toString(DashboardActivityAlt.listItem.getCategory().getCategoryId()), 
	  						DashboardActivityAlt.listItem.getDeadline().toString(),
	  						Float.toString(DashboardActivityAlt.listItem.getPrice()), NewMessageFragment.newMessage, 
	  						Integer.toString(DashboardActivityAlt.listItem.getOrderid()), FileManagerActivity.getFinalMessageFiles());
	  				 
	         		}
	         		else if(DashboardActivityAlt.listItem.getProduct().getProductType().equalsIgnoreCase("essay"))
	         		{
	         			response = userFunc.sendMessage("0", 
	             				DashboardActivityAlt.listItem.getDeadline().toString(),
	             				Float.toString(DashboardActivityAlt.listItem.getPrice()), NewMessageFragment.newMessage, 
	             				Integer.toString(DashboardActivityAlt.listItem.getOrderid()), FileManagerActivity.getFinalMessageFiles());
	         		
	         		}
         		
  				}
			 catch (Exception e) {
				
				e.printStackTrace();
			}
	       		
	       	return response;
	     }

	        protected void onPostExecute(JSONObject  json) {
	 	  	   
	        	try {
	  				if ((response.getString(Constants.KEY_STATUS) != null))
	  				{
	  					
	  				    String res = response.getString(Constants.KEY_STATUS);
	  				    if(Boolean.parseBoolean(res) )
	  				    {	
	  				    		
	  				    }
	  				    else 
	  				    {
	  				    	Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.error_server_problem), Toast.LENGTH_SHORT).show();
	  				    }
	      
	  				}	
	        	}
				 catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {

					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	   }
	

}
