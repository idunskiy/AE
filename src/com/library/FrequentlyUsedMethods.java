package com.library;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.preference.ListPreference;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.R;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.InactivateAsync;
import com.datamodel.Level;
import com.datamodel.Order;
import com.datamodel.ProcessStatus;
import com.j256.ormlite.dao.Dao;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalPayment;
/**  *	 класс для часто используемых функций в разных местах приложения */
public class FrequentlyUsedMethods {
	private static final int server = PayPal.ENV_SANDBOX;
	  /**	 *	  id для PayPal библиотеки	 */
	private static final String appID = "APP-80W284485P519543T";
//	private static final String appID = "APP-8HA162973U847442G";
	private static final int request = 1;
	public static CheckoutButton launchSimplePayment;
	Context context;
	Activity activity;
	private Handler mHandler;
	public FrequentlyUsedMethods(Context ctx)
	{ 
		this.context = ctx;
		mHandler = new Handler();
	}
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
		Log.i("freq method", activity.getClass().toString());
		return this.activity;
	}
	  /**	 *	  метод инициализации PayPal библиотеки
	       * @param arg  - обьект Context для инициализации	 */
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
	 /**	 *	  метод setup кнопки PayPal
     * @param arg  - обьект Context для инициализации
     * @param panelInteractions  - RelativeLayout для setup'a кнопки на нем	 */
  public void setupButtons(Context arg, RelativeLayout panelInteractions) {
		PayPal pp = PayPal.getInstance();
		//arg = getContext();
		Log.i("FAQ context", arg.getClass().toString());
		// Get the CheckoutButton. There are five different sizes. The text on the button can either be of type TEXT_PAY or TEXT_DONATE.
		launchSimplePayment = pp.getCheckoutButton(arg, PayPal.BUTTON_194x37, CheckoutButton.TEXT_PAY);
		// You'll need to have an OnClickListener for the CheckoutButton. For this application, MPL_Example implements OnClickListener and we
		// have the onClick() method below.
		Log.i("now I'm in this Activity",  arg.getClass().getName());
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int) getContext().getResources().getDimension(R.dimen.paypal_width), LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lp.leftMargin =(int) getContext().getResources().getDimension(R.dimen.paypal_left_margin); 
		lp.topMargin = (int) getContext().getResources().getDimension(R.dimen.paypal_top_margin);
		setActivity((Activity)arg);
		Log.i("getAct FAQ", getActivity().getClass().toString());
		launchSimplePayment.setOnClickListener(mCorkyListener);
		// The CheckoutButton is an android LinearLayout so we can add it to our display like any other View.
		panelInteractions.addView(launchSimplePayment,lp);
		
	}
  /**	 *	  метод вывода ошибки при попытке инициализации PayPal библиотеки  */
  public void showFailure(Context ctx) {
	  Toast.makeText(ctx, "Sorry, could not initialize the PayPal library. Try to make it later",  Toast.LENGTH_LONG).show();
	}
  /**	 *	  метод деактивации заказов   	 */
  	public void inactivateOrder()
  	{
  		AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());
          alt_bld.setMessage("Are you sure you want to inactive the order?")
          .setCancelable(false)
          .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
              // Action for 'Yes' Button
              	InactivateAsync.execute((FragmentActivity)getContext(), (ITaskLoaderListener)getContext());
              }
          })
          .setNegativeButton("No", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
              //  Action for 'NO' Button

                  dialog.cancel();
              }
          });
          AlertDialog alert = alt_bld.create();
          // Title for AlertDialog
          alert.setTitle("Order inactivation");
          // Icon for AlertDialog
          alert.show();
  	}
  	 /**	 *	 OnClickListener для PayPal кнопки  	 */
  private OnClickListener mCorkyListener = new OnClickListener() {
	    public void onClick(View v) {
		    	PayPalPayment newPayment = new PayPalPayment();
		    	 //launchSimplePayment.updateButton();
		      	Log.i("item current price" , Float.toString(DashboardActivityAlt.listItem.getPrice()));
		      	//if (new BigDecimal(Float.toString(DashboardActivityAlt.listItem.getPrice()))!= null) 
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
		      	Intent paypalIntent = PayPal.getInstance().checkout(newPayment, getActivity(),new ResultPayPalDelegate());
			    getActivity().startActivityForResult(paypalIntent, request);
	    }
   };
	 /**	 *	 OnClickListener для открытия файла  	 */
   public OnClickListener fileInfoListenter = new OnClickListener() {
	    public void onClick(View v) {
	    	  File choosed = findFile(((TextView)v).getText().toString());
              Intent intent = new Intent();
  			intent.setAction(android.content.Intent.ACTION_VIEW);
  			intent.setDataAndType(Uri.fromFile(choosed), "text/plain");
  			getContext().startActivity(intent);
     	
   }};
	 /**	 *	 метод для поиска файла на карте памяти по имени файла  	 */
   public File findFile(String name)
   {
	   File directory = new File("/mnt/sdcard/download/AssignmentExpert");
	   File choosedFile = null;
	   File[] files = directory.listFiles();
	   try
	   {
	   for (int i = 0; i < files.length; ++i) {
	      if (files[i].getName().equals(name))
	    	  choosedFile = files[i];
	   }
	   }
	   catch(NullPointerException e)
	   {
		   someMethod("current file can't be find");
		   e.printStackTrace();
		   
		   
	   }
	   return choosedFile;
   }
   /**	 *	 метод конвертации пикселей в dp 
     * @param px - пиксели для конвертации
    *  @param ctx - Activity для которой будет осуществляться конвертация  	 */
   public float convertPixelsToDp(float px, Activity ctx){
   	DisplayMetrics metrics = new DisplayMetrics();
   	ctx.getWindowManager().getDefaultDisplay().getMetrics(metrics);
   	float dp = metrics.density;
   	return dp;
   }
   /**	 *	 метод проверки на налачие подключения к интернету    	 */
   public boolean isOnline() {
	   boolean res = false;
       ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo netInfo = cm.getActiveNetworkInfo();
       if (netInfo != null && netInfo.isConnectedOrConnecting()) {
    	   res= true;
       }
       else
       {
    	   Toast.makeText(context, "Connection is not available. Please try later.", Toast.LENGTH_LONG).show();
    	   res = false;
        }
       return res;
   }
   
   public boolean isOnlineOrderSend() {
	   boolean res = false;
       ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo netInfo = cm.getActiveNetworkInfo();
       if (netInfo != null && netInfo.isConnectedOrConnecting()) {
    	   res= true;
       }
       else
    	   res= false;
       return res;
   }
   /**	 *	 метод обновления всех полей текущего заказа
    * @param ord - заказ для обновления    	 */
   public Order updateOrderFields(Order ord)
   {
	   		
	   		DatabaseHandler db = new DatabaseHandler(context.getApplicationContext());
			Dao<ProcessStatus, Integer> daoProcess;
			Dao<Level, Integer> daoLevel;
			try {
				daoProcess = db.getStatusDao();
				daoLevel = db.getLevelDao();
				ord.getProcess_status().setProccessStatusTitle
				((daoProcess.queryForId(ord.getProcess_status().getProccessStatusId()).getProccessStatusTitle()));
				if (ord.getLevel() != null)
				ord.getLevel().setLevelTitle((daoLevel.queryForId(ord.getLevel().getLevelId()).getLevelTitle()));
				Log.i("order dashboard in updateOrderFields FAQ class", Integer.toString(ord.getProcess_status().getProccessStatusId()));
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			return ord;
   }
	
   /**	 *	 метод добавления временных зон в ListPreference
    * @param   timezonePref - ListPreference в который будут добавляться временные зоны  	 */
	 public void addTimeZones(ListPreference timezonePref) {
			final String[] TZ = TimeZone.getAvailableIDs();

			ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
					getContext(), android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			final ArrayList<String> TZ1 = new ArrayList<String>();
			for (int i = 0; i < TZ.length; i++) {
				if (!(TZ1.contains(TimeZone.getTimeZone(TZ[i]).getID()))) {
					if (timezoneValidate(TZ[i]))
						TZ1.add(TZ[i]);
				}
			}
			for (int i = 0; i < TZ1.size(); i++) {
				
				
				adapter.add(TZ1.get(i));
			}
			
			CharSequence[] entries = new CharSequence[adapter.getCount()];
		    CharSequence[] entryValues = new CharSequence[adapter.getCount()];
		    int i = 0;
		    for (String dev : TZ1)
		    {
		    	entries[i] = dev;
	            entryValues[i] = dev;
	            if (TimeZone.getTimeZone(TZ1.get(i)).getID()
						.equals(TimeZone.getDefault().getID())) {
	            	 timezonePref.setSummary((TimeZone.getDefault().getID()));
				}
	            i++;
	            
		    }
		   
		    timezonePref.setEntries(entries);
		    timezonePref.setEntryValues(entryValues);
		    Locale locale = new Locale("en", "IN");
		    String curr = TimeZone.getTimeZone(TimeZone.getDefault().getID()).getDisplayName(false,TimeZone.SHORT, locale);
		    String currTimeZone = "";
		    for (int q = 0; q<TZ1.size();q++)
		    {
		    	if (TimeZone.getTimeZone(TZ1.get(q)).getDisplayName(false,TimeZone.SHORT, locale).equals(curr))
		    	currTimeZone = TZ1.get(q);
		    }
		    timezonePref.setSummary(currTimeZone );
		}
	 
	 /**	 *	 метод валидации временных зон, которые соответствуют необходимому формату
	    * @param   email - строка для  валидации	 */
	 public boolean timezoneValidate(String email)
	    {
	        
	    	Pattern pattern = Pattern.compile(".+\\/+[A-z]+");
				Matcher matcher = pattern.matcher(email);
				boolean matchFound = matcher.matches();
	    	return matchFound;
	    }
	 public Context getDialogContext(Activity act) {
		    Context context;
		    if (act.getParent() != null) 
		        context = act.getParent();
		    else context = act;
		    Log.i("RegisterAct curr context", context.getClass().toString());
		        return context;
		}
	 /**	 *	создание отдельного потока для вывода строки во всех Activities приложения	   	 */
	 private class ToastRunnable implements Runnable {
		    String mText;

		    public ToastRunnable(String text) {
		        mText = text;
		    }

		    public void run(){
		         Toast.makeText(getContext(), mText, Toast.LENGTH_SHORT).show();
		    }
		}
	 /**	 *	метод вывода строки во всех Activities приложения   
	  * @param message - сообщение для вывода 	 */
	 public void someMethod(String message) {
		    mHandler.post(new ToastRunnable(message));
		}
	
}
