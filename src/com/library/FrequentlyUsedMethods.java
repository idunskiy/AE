package com.library;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.ListPreference;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.R;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.InactivateAsync;
import com.customitems.CustomEditPreference;
import com.datamodel.Files;
import com.datamodel.Level;
import com.datamodel.Order;
import com.datamodel.ProcessStatus;
import com.j256.ormlite.dao.Dao;
import com.library.singletones.SharedPrefs;
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
          alt_bld.setMessage(getContext().getResources().getString(R.string.dialog_inactivate_message))
          .setCancelable(false)
          .setPositiveButton(getContext().getResources().getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
              // Action for 'Yes' Button
              	InactivateAsync.execute((FragmentActivity)getContext(), (ITaskLoaderListener)getContext());
              }
          })
          .setNegativeButton(getContext().getResources().getString(R.string.btn_no), new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
              //  Action for 'NO' Button

                  dialog.cancel();
              }
          });
          AlertDialog alert = alt_bld.create();
          // Title for AlertDialog
          alert.setTitle(getContext().getResources().getString(R.string.dialog_inactivate_title));
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
		      	newPayment.setRecipient("paygbpaltest@gmail.com");
		      	newPayment.setMerchantName("FreelanceSystems LTD");
		      	newPayment.setIpnUrl(Constants.prodHost+"/paypal/notifier");
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
	    	 
//	    	  File choosed = findFile(((TextView)v).getText().toString());
	    	Files file = findFile(((TextView)v).getTag().toString());
	    	new DownloadFileAsync(getContext()).execute(file);
//              Intent intent = new Intent();
//  			intent.setAction(android.content.Intent.ACTION_VIEW);
//  			intent.setDataAndType(Uri.fromFile(choosed), "text/plain");
//  			getContext().startActivity(intent);
     	
   }};
   
   
	 /**	 *	 метод для поиска файла на карте памяти по имени файла  	 */
	public Files findFile(String name)
   {
		 return DashboardActivityAlt.listItem.getOrderFiles().get(DashboardActivityAlt.listItem.getOrderFiles().indexOf(new Files(Integer.parseInt(name))));
		 
//	   File directory = new File("/mnt/sdcard/download/AssignmentExpert");
//	   File choosedFile = null;
//	   File[] files = directory.listFiles();
//	   try
//	   {
//	   for (int i = 0; i < files.length; ++i) {
//	      if (files[i].getName().equals(name))
//	    	  choosedFile = files[i];
//	   }
//	   }
//	   catch(NullPointerException e)
//	   {
//		   someMethod("current file can't be find");
//		   e.printStackTrace();
//		   
//		   
//	   }
//	   return choosedFile;
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
	 public void addTimeZones(Object timezonePref, ArrayAdapter<String> _adapter) {
			
		 	ArrayAdapter<String> adapter = null;
		 	final String[] TZ = TimeZone.getAvailableIDs();
		 	if (_adapter==null)
		 		adapter = new ArrayAdapter<String>(
					getContext(), android.R.layout.simple_spinner_item);
		 	else 
		 		adapter = _adapter;
		 	
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			final ArrayList<String> TZ1 = new ArrayList<String>();
			for (int i = 0; i < TZ.length; i++) {
				if (timezoneValidate(TZ[i]))
				{
						TZ1.add(TZ[i].substring(7) + ":00");
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
	            i++;
	            
		    }
		    Calendar cal = Calendar.getInstance();
		    TimeZone tz = TimeZone.getDefault();  
		    int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
		    String offset = String.format(checkIntSign(offsetInMillis) +"%1d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
		    
		    
		   if (timezonePref instanceof ListPreference){
		    ((ListPreference)timezonePref).setEntries(entries);
		    ((ListPreference)timezonePref).setEntryValues(entryValues);
		   
		    ((ListPreference)timezonePref).setSummary(offset );
		   }
		   else if (timezonePref instanceof CustomEditPreference)
		   {
			   ((CustomEditPreference)timezonePref).setSummary(offset);
		   }
		   
		}
	 
	 
	 public String checkIntSign(int value)
	 {
		 if(value  < 0)
			 return "-";
		 else 
			 return "+";
			 
	 }
	 
	 /**	 *	 метод валидации временных зон, которые соответствуют необходимому формату
	    * @param   email - строка для  валидации	 */
	 public boolean timezoneValidate(String email)
	    {
	        
	    	Pattern pattern = Pattern.compile("^(Etc/GMT)+[+|-]+[0-9]");
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
	public void logOut()
	{
		DashboardActivityAlt.forPrint.clear();
		DashboardActivityAlt.page = 1;
		if (SharedPrefs.getInstance().getSharedPrefs().getBoolean(Constants.NO_MORE_ORDERS, false)==true)
			SharedPrefs.getInstance().getSharedPrefs().edit().putBoolean(Constants.NO_MORE_ORDERS, false).commit();
		SharedPrefs.getInstance().getSharedPrefs().edit().putBoolean(Constants.IS_LOGGED, false).commit();
		//SharedPrefs.getInstance().getSharedPrefs().edit().clear().commit();
	}
	public void addOrderFiles(Context context, LinearLayout layout )
	 {
		try
		{
	    if(!DashboardActivityAlt.listItem.getOrderFiles().isEmpty())
	    {
	    	final TextView tv[] = new TextView[DashboardActivityAlt.listItem.getOrderFiles().size()];
		         for (int i = 0; i< DashboardActivityAlt.listItem.getOrderFiles().size();i++)
		          {
		            
		            	View line = new View(context);
		                line.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
		                line.setBackgroundColor(0xAA345556);
		                tv[i] = new TextView(context);
		                tv[i].setId(DashboardActivityAlt.listItem.getOrderFiles().get(i).getFileId());
		                tv[i].setTag(DashboardActivityAlt.listItem.getOrderFiles().get(i).getFileId());
		                tv[i].setTextColor(Color.WHITE);
		                tv[i].setTextSize(12);
		                tv[i].setCompoundDrawablesWithIntrinsicBounds(
		                        0, R.drawable.file, 0, 0);
		                tv[i].setText(DashboardActivityAlt.listItem.getOrderFiles().get(i).getFileName());
		                tv[i].setOnClickListener(this.fileInfoListenter);
		                
		                layout.addView(tv[i], 0);
		                layout.addView(line, 1);
		               
	
		          }
		       
	      }
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}

	}
	
	
	private class DownloadFileTask extends AsyncTask<Files, Integer, String> {

	    private Context context;
		private Dialog mProgressDialog;

	    public DownloadFileTask(Context context) {
	    	mProgressDialog = new Dialog(context);
	    	this.context = context;
	    }
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        mProgressDialog.show();
	    }
	    @Override
	    protected String doInBackground(Files... sUrl) {
	        // take CPU lock to prevent CPU from going off if the user 
	        // presses the power button during download
	        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
	             getClass().getName());
	        wl.acquire();

	        try {
	            InputStream input = null;
	            OutputStream output = null;
	            HttpURLConnection connection = null;
	            try {
	                URL url = new URL(sUrl[0].getFileFullPath());
	                connection = (HttpURLConnection) url.openConnection();
	                connection.connect();

	                // expect HTTP 200 OK, so we don't mistakenly save error report 
	                // instead of the file
	                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
	                     return "Server returned HTTP " + connection.getResponseCode() 
	                         + " " + connection.getResponseMessage();

	                // this will be useful to display download percentage
	                // might be -1: server did not report the length
	                int fileLength = connection.getContentLength();

	                // download the file
	                input = connection.getInputStream();
	                output = new FileOutputStream("/mnt/sdcard/download/" +  sUrl[0].getFileName());

	                byte data[] = new byte[4096];
	                long total = 0;
	                int count;
	                while ((count = input.read(data)) != -1) {
	                    // allow canceling with back button
	                    if (isCancelled())
	                        return null;
	                    total += count;
	                    // publishing the progress....
	                    if (fileLength > 0) // only if total length is known
	                        publishProgress((int) (total * 100 / fileLength));
	                    output.write(data, 0, count);
	                }
	            } catch (Exception e) {
	                return e.toString();
	            } finally {
	                try {
	                    if (output != null)
	                        output.close();
	                    if (input != null)
	                        input.close();
	                } 
	                catch (IOException ignored) { }

	                if (connection != null)
	                    connection.disconnect();
	            }
	        } finally {
	            wl.release();
	        }
	        return null;
	    }
	    @Override
	    protected void onPostExecute(String result) {
	        mProgressDialog.dismiss();
	        if (result != null)
	            Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
	        else
	            Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
	    }
	}
	 public boolean EmailValidate(String email)
	    {
	        
	    	Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
				Matcher matcher = pattern.matcher(email);
				boolean matchFound = matcher.matches();
	    	return matchFound;
	    }
	public void createToast(String text_, Activity ctx)
	{
		LayoutInflater inflater = ctx.getLayoutInflater();
				 View layout = inflater.inflate(R.layout.custom_toast,
		                 (ViewGroup) ctx.findViewById(R.id.toast_layout_root));
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(text_);
		Toast toast = new Toast(ctx.getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}
	
	public static String GetCountryZipCode(Context context)  
	{
	    // Get the country ISO
	    TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	    String countryIso = manager.getSimCountryIso().toUpperCase();

	    // Load resources containing the country codes list
	    String[] countryCodes = context.getResources().getStringArray(R.array.CountryCodes);

	    // Try to find what we need
	    for(int i=0; i<countryCodes.length; i++)
	    {
	        String[] line = countryCodes[i].split(",");
	        if (line.length != 2)
				try {
					throw new Exception("Resource file looks like invalid.");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	        // We have found something interesting
	        if (line[1].trim().equals(countryIso.trim()))
	        return line[0];
	    }
	    // Nothing was found
	    return null;
	}
}
