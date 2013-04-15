package com.fragmentactivities;

import java.math.BigDecimal;

import org.json.JSONObject;

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
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.InteractionsActivityViewPager;
import com.assignmentexpert.R;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.InactivateAsync;
import com.asynctasks.PayPalAsync;
import com.asynctasks.PaymentProceeding;
import com.asynctasks.SendMessageAsync;
import com.fragments.IClickListener;
import com.fragments.InteractionFragment;
import com.fragments.NewMessageFragment;
import com.library.FrequentlyUsedMethods;
import com.library.ResultPayPalDelegate;
import com.library.StaticFields;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalPayment;
import com.tabscreens.DashboardTabScreen;

public class InteractionsFragmentActivity extends FragmentActivity implements ITaskLoaderListener, IClickListener{
	private FragmentTransaction fragmentTransaction;
	private FrequentlyUsedMethods faq;
	private CheckoutButton launchSimplePayment;
	private static final int request = 1;
	
	String resultTitle;
	String resultInfo;
	String resultExtra;
	
	
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.profile_fragment);
	      Fragment newFragment = new InteractionFragment();
	      FragmentManager fragmentManager = getSupportFragmentManager();
	      fragmentTransaction = fragmentManager.beginTransaction();
	      fragmentTransaction.add(R.id.myfragment, newFragment);
	      fragmentTransaction.commit();
	      faq = new FrequentlyUsedMethods(this);
	      
		}
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
				InactivateAsync.execute(this,this);
				 
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
		    private OnClickListener mCorkyListener = new OnClickListener() {
			    public void onClick(View v) {
			    PayPalPayment newPayment = new PayPalPayment();
			    DashboardActivityAlt.listItem.setPrice(5);
		      	Log.i("item current price" , Float.toString(DashboardActivityAlt.listItem.getPrice()));
		      	newPayment.setSubtotal(new BigDecimal(Float.toString(DashboardActivityAlt.listItem.getPrice())));
		      	newPayment.setCurrencyType("USD");
		      	newPayment.setRecipient("ivand_1356619309_biz@aeteam.org");
		      	newPayment.setMerchantName("BrainRouter LTD");
		      	newPayment.setIpnUrl(StaticFields.testHost+"/app_dev.php/payment/notifier/");
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
				resultTitle = "CANCELED";
				resultInfo = "The transaction has been cancelled.";
				resultExtra = "";
				Toast.makeText(InteractionsFragmentActivity.this, "The transaction has been cancelled.", Toast.LENGTH_SHORT).show();
				break;
			case PayPalActivity.RESULT_FAILURE:
				resultTitle = "FAILURE";
				resultInfo = data.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
				resultExtra = "Error ID: " + data.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
				Toast.makeText(InteractionsFragmentActivity.this, "Failure! "+ resultInfo, Toast.LENGTH_SHORT).show();
		    	}
		    
		    }
		   
	public void onLoadFinished(Object data) {
		if (data instanceof Integer)
			 {
				setupButtons(this, InteractionFragment.panelInteractions);
			 }
		else if(data instanceof String )
		{
			if (((String) data).equalsIgnoreCase("success"))
			{
			 Fragment newFragment = new InteractionFragment();
			  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		      fragmentTransaction.replace(R.id.myfragment, newFragment);
		      fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		      fragmentTransaction.commit();
			}
		}
		
	}

	public void onCancelLoad() {
		// TODO Auto-generated method stub
		
	}

}
