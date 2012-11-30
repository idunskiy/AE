package com.library;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.NewMessageActivity;
import com.assignmentexpert.R;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalPayment;

public class FrequentlyUsedMethods {
	private static final int server = PayPal.ENV_SANDBOX;
	private static final String appID = "APP-80W284485P519543T";
	private static final int request = 1;
	public static CheckoutButton launchSimplePayment;
	Context context;
	Activity activity;
	public void setContext(Context ctx)
	{
		this.context = ctx;
	}
	public Context getContext()
	{
		return this.context;
	}
	public void setActivity(Activity ctx)
	{
		this.activity = ctx;
	}
	public Activity getActivity()
	{
		return this.activity;
	}
	
	
	public boolean initLibrary(Context arg) {
		PayPal pp = PayPal.getInstance();
		// If the library is already initialized, then we don't need to initialize it again.
		if(pp == null) {
			// This is the main initialization call that takes in your Context, the Application ID, and the server you would like to connect to.
			pp = PayPal.initWithAppID(arg, appID, server);
   			
			// -- These are required settings.
        	pp.setLanguage("en_US"); // Sets the language for the library.
        	// --
        	
        	// -- These are a few of the optional settings.
        	// Sets the fees payer. If there are fees for the transaction, this person will pay for them. Possible values are FEEPAYER_SENDER,
        	// FEEPAYER_PRIMARYRECEIVER, FEEPAYER_EACHRECEIVER, and FEEPAYER_SECONDARYONLY.
        	
        	pp.setFeesPayer(PayPal.FEEPAYER_EACHRECEIVER);
        	
        	// Set to true if the transaction will require shipping.
        	
        	pp.setShippingEnabled(true);
        	
        	// Dynamic Amount Calculation allows you to set tax and shipping amounts based on the user's shipping address. Shipping must be
        	// enabled for Dynamic Amount Calculation. This also requires you to create a class that implements PaymentAdjuster and Serializable.
        	
        	pp.setDynamicAmountCalculationEnabled(false);
        	
        	// --
        	
		}
		return PayPal.getInstance().isLibraryInitialized();
	}
  public void setupButtons(Context arg, RelativeLayout panelInteractions) {
		PayPal pp = PayPal.getInstance();
		arg = getContext();
		// Get the CheckoutButton. There are five different sizes. The text on the button can either be of type TEXT_PAY or TEXT_DONATE.
		launchSimplePayment = pp.getCheckoutButton(arg, PayPal.BUTTON_194x37, CheckoutButton.TEXT_PAY);
		// You'll need to have an OnClickListener for the CheckoutButton. For this application, MPL_Example implements OnClickListener and we
		// have the onClick() method below.
		Log.i("now I'm in this Activity",  arg.getClass().getName());
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int) getContext().getResources().getDimension(R.dimen.paypal_width), LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lp.leftMargin =(int) getContext().getResources().getDimension(R.dimen.paypal_left_margin); //(int) dpFromPx(135);
		lp.topMargin = (int) getContext().getResources().getDimension(R.dimen.paypal_top_margin);//(int) dpFromPx(20);
		
		launchSimplePayment.setOnClickListener(mCorkyListener);
		// The CheckoutButton is an android LinearLayout so we can add it to our display like any other View.
		panelInteractions.addView(launchSimplePayment,lp);
		
		
		
		
		
	}
  public void showFailure(Context ctx) {
	  Toast.makeText(ctx, "Sorry, could not initialize the PayPal library. Try to make it later",  Toast.LENGTH_LONG).show();
		
	}
  private OnClickListener mCorkyListener = new OnClickListener() {
	    public void onClick(View v) {
	    	PayPalPayment newPayment = new PayPalPayment();
      	
      	Log.i("item current price" , Float.toString(DashboardActivityAlt.listItem.getPrice()));
      	//if (new BigDecimal(Float.toString(DashboardActivityAlt.listItem.getPrice()))!= null) 
      	newPayment.setSubtotal(new BigDecimal(Float.toString(DashboardActivityAlt.listItem.getPrice())));
      	
      	newPayment.setCurrencyType("USD");
      	newPayment.setRecipient("billing@globalwriters.co.uk");
      	newPayment.setMerchantName("BrainRouter LTD");
      	newPayment.setIpnUrl("http://www.assignmentexpert.com/assignments/index.php/tools/paypal_notifier");
      	Intent paypalIntent = PayPal.getInstance().checkout(newPayment, getContext(),new ResultPayPalDelegate());
	    getActivity().startActivityForResult(paypalIntent, request);
	    }
   };
   public boolean payPalActivate()
   {
	   boolean result;
	   if (DashboardActivityAlt.listItem.getProcess_status().getProccessStatusTitle().equals("Inactive") | DashboardActivityAlt.listItem.getCusThread().getMessages().isEmpty() )
		   result  = false;
	   else result = true;
	   return result;
   }
   public float convertPixelsToDp(float px, Activity ctx){
   	DisplayMetrics metrics = new DisplayMetrics();
   	ctx.getWindowManager().getDefaultDisplay().getMetrics(metrics);
   	float dp = metrics.density;
   	return dp;
   }
   private float dpFromPx(float px)
   {
       return px / getContext().getResources().getDisplayMetrics().density;
   }
//   public void showToast()
//   {
//	   Toast.makeText(getContext(), "", duration)
//	   
//   }
   
	
	
}
