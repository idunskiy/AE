package com.fragmentactivities;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.asynctasks.PaymentProceeding;
import com.asynctasks.SendMessageAsync;
import com.fragments.IClickListener;
import com.fragments.InteractionFragment;
import com.fragments.NewMessageFragment;
import com.fragments.OrderInfoFragmentAA;
import com.fragments.OrderInfoFragmentEW;
import com.library.Constants;
import com.library.FrequentlyUsedMethods;
import com.library.ResultPayPalDelegate;
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
			if (flag ==10)
				SendMessageAsync.execute(this, this);
			
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
			    DashboardActivityAlt.listItem.setPrice(5);
		      	Log.i("item current price" , Float.toString(DashboardActivityAlt.listItem.getPrice()));
		      	newPayment.setSubtotal(new BigDecimal(Float.toString(DashboardActivityAlt.listItem.getPrice())));
		      	newPayment.setCurrencyType("USD");
		      	newPayment.setRecipient("ivand_1356619309_biz@aeteam.org");
		      	newPayment.setMerchantName("BrainRouter LTD");
		      	newPayment.setIpnUrl(Constants.testHost+"/app_dev.php/payment/notifier/");
		      	newPayment.setPaymentType(PayPal.PAYMENT_TYPE_SERVICE);
		      	PayPalInvoiceData invoice = new PayPalInvoiceData();
		        	invoice.setTax(new BigDecimal("0"));
		      	  invoice.setShipping(new BigDecimal("0"));
		      	 PayPalInvoiceItem  item =new PayPalInvoiceItem();
		      	 item.setName("something");
		      	 item.setID(Integer.toString(DashboardActivityAlt.listItem.getOrderid()));
		      	 item.setQuantity(1);
		      	 invoice.getInvoiceItems().add(item);
		      	 newPayment.setInvoiceData(invoice);
		      	
		      	Intent paypalIntent = PayPal.getInstance().checkout(newPayment, InteractionsFragmentActivity.this ,new ResultPayPalDelegate());
			    InteractionsFragmentActivity.this.startActivityForResult(paypalIntent, request);
			    }
		   };

		   public void onActivityResult(int requestCode, int resultCode, Intent data) {
			   Log.i("newFragment ActRes", Integer.toString(resultCode));
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
			  	Log.i("interactions frag res", "result_ok");
			  	PaymentProceeding.execute(this, this);
			  	faq.updateOrderFields(DashboardActivityAlt.listItem);
				resultTitle = "SUCCESS";
				resultInfo = "You have successfully completed this payment.";
				Toast.makeText(InteractionsFragmentActivity.this, "Your payment was proceeded successfully", Toast.LENGTH_SHORT).show();
				 Intent i = new Intent(getApplicationContext(),
	 	                    DashboardTabScreen.class);
	 	     	  	  startActivity(i);
				break;
			case Activity.RESULT_CANCELED:
				Log.i("interactions frag res", "result_cancelled");
//				Log.i("getAct FAQ", faq.getActivity().getClass().toString());
				faq.setActivity(this);
				resultTitle = "CANCELED";
				resultInfo = "The transaction has been cancelled.";
				resultExtra = "";
				
				Toast.makeText(InteractionsFragmentActivity.this, "The transaction has been cancelled.", Toast.LENGTH_SHORT).show();
				
				//InteractionFragment.updatePayPalBtn();
				break;
			case PayPalActivity.RESULT_FAILURE:
				resultTitle = "FAILURE";
				resultInfo = data.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
				resultExtra = "Error ID: " + data.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
				Toast.makeText(InteractionsFragmentActivity.this, "Failure! "+ resultInfo, Toast.LENGTH_SHORT).show();
				
			case Constants.addFilesResult:
				FragmentManager fragmentManager = getSupportFragmentManager();
				Fragment currentFragment = fragmentManager.findFragmentById(R.id.myfragment);
				Log.i("interactions act", currentFragment.getClass().toString());
				if (currentFragment instanceof NewMessageFragment)
				{
					((NewMessageFragment)currentFragment).addMessageFiles();
				}
		    	}
		    
		    }
		   /** *метод интерфейса ITaskLoaderListener. служит дл€ обработки данных после успешной работы задачи в отдельном потоке, используемой в данной активности.  */   
	public void onLoadFinished(Object data) {
		if (data instanceof Integer)
			 {
//				faq.setActivity(this);
//				faq.setContext(this);
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
			Log.i("interactionsFragmentAct onLoadFinished", (String)data);
			if (((String) data).equalsIgnoreCase("send_message_success"))
			{
			  Fragment newFragment = new InteractionFragment();
			  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		      fragmentTransaction.replace(R.id.myfragment, newFragment);
		      fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		      fragmentTransaction.commit();
		      FileManagerActivity.getFinalMessageFiles().clear();
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
			      Toast.makeText(this, "Something went wrong. Please try later. ", Toast.LENGTH_SHORT).show();
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
	

}
