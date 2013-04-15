package com.assignmentexpert;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datamodel.Category;
import com.datamodel.Files;
import com.datamodel.Level;
import com.datamodel.ProductAssignment;
import com.datamodel.Subject;
import com.j256.ormlite.dao.Dao;
import com.library.DatabaseHandler;
import com.library.FrequentlyUsedMethods;

public class OrderInfoActivityAA extends Activity {
	private View btnClose;
	private Button btnInteractions;
	private Button btnInfoOrder;
	TextView productTextView;
    TextView priceTextView;
	TextView timezoneTextView;
	TextView subjTextView;
	private TextView postedTextView;
	private TextView categoryTextView;
	private TextView levelTextView;
	private TextView deadlineTextView;
	private TextView taskTextView;
	private TextView requireTextView;
	
	List<Subject> subjectsList;
	List<Category> categoryList;
	List<Level> levelList;
	private LinearLayout layout;
	SharedPreferences preferenceManager;
	DownloadManager downloadManager;
	//DownloadReceiver downloadFilesReceiver;
	final String strPref_Download_ID = "PREF_DOWNLOAD_ID";
	@SuppressWarnings("null")
	@Override
    public void onCreate(Bundle savedInstanceState) {
		 InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_info_aa);
        btnClose = (Button) findViewById(R.id.btnClose);
        
	    btnInteractions = (Button) findViewById(R.id.btnInteractions);
	    btnInfoOrder = (Button) findViewById(R.id.btnInfoOrder);
	   /// btnInfoOrder.setText("info"+ "\r\n"+Integer.toString(DashboardActivityAlt.listItem.getOrderid()));
	    
	    productTextView = (TextView)findViewById(R.id.productTextView);
	    priceTextView =   (TextView)findViewById(R.id.priceTextView);
	    timezoneTextView = (TextView)findViewById(R.id.timezoneTextView);
	    subjTextView = (TextView)findViewById(R.id.subjTextView);
	    postedTextView =  (TextView)findViewById(R.id.postedTextView);
	    categoryTextView =  (TextView)findViewById(R.id.categoryTextView);
	    levelTextView =  (TextView)findViewById(R.id.levelTextView);
	    deadlineTextView =  (TextView)findViewById(R.id.deadlineTextView);
	    taskTextView =  (TextView)findViewById(R.id.taskTextView);
	    requireTextView =  (TextView)findViewById(R.id.requireTextView);
	    
	    layout = (LinearLayout)findViewById(R.id.infoMessageListLayout);
	    
	    Log.i("chosed listItem", DashboardActivityAlt.listItem.toString());
	    
	    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
	    Dao<Subject, Integer> daoSubject = null;
	    Dao<Category, Integer> daoCategory = null;
	    Dao<Level, Integer> daoLevel = null;
		try {
			
			daoSubject = db.getSubjectDao();
			subjectsList = daoSubject.queryForAll();
			
			daoLevel = db.getLevelDao();
			levelList = daoLevel.queryForAll();
			
			daoCategory = db.getCategoryDao();
			categoryList = daoCategory.queryForAll();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if (DashboardActivityAlt.listItem.getCategory()  != null)
			DashboardActivityAlt.listItem.getCategory().setCategoryTitle((
					daoCategory.queryForId(DashboardActivityAlt.listItem.getCategory().getCategoryId())).getCategoryTitle());
			if (DashboardActivityAlt.listItem.getCategory().getCategorySubject() != null)
			DashboardActivityAlt.listItem.getCategory().getCategorySubject().setSubjectTitle((
					daoSubject.queryForId(DashboardActivityAlt.listItem.getCategory().getCategorySubject().getSubjectId())).getSubjectTitle());
			if (DashboardActivityAlt.listItem.getLevel() != null)
			DashboardActivityAlt.listItem.getLevel().setLevelTitle((
					daoLevel.queryForId(DashboardActivityAlt.listItem.getLevel().getLevelId()).getLevelTitle()));
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(NullPointerException e)
		{
			DashboardActivityAlt.listItem.getCategory().setCategoryTitle("");
			DashboardActivityAlt.listItem.getCategory().getCategorySubject().setSubjectTitle("");
			DashboardActivityAlt.listItem.getLevel().setLevelTitle("");
		}
				
		
	    
	    
	    priceTextView.setText(Float.toString(DashboardActivityAlt.listItem.getPrice()));
	    timezoneTextView.setText(DashboardActivityAlt.listItem.getTimezone());
	    subjTextView.setText(DashboardActivityAlt.listItem.getCategory().getCategorySubject().getSubjectTitle());
	    postedTextView.setText(DashboardActivityAlt.listItem.getCreated_at().toString());
	    
	    categoryTextView.setText(DashboardActivityAlt.listItem.getCategory().getCategoryTitle());
	    
	    if (DashboardActivityAlt.listItem.getLevel() != null)
	       levelTextView.setText(DashboardActivityAlt.listItem.getLevel().getLevelTitle());
	    else
	       levelTextView.setText("N/A");
	    deadlineTextView.setText(DashboardActivityAlt.listItem.getDeadline().toString());
	    taskTextView.setText(((ProductAssignment)DashboardActivityAlt.listItem.getProduct().getProduct()).getAssignInfo());
	    if (((ProductAssignment)DashboardActivityAlt.listItem.getProduct().getProduct()).getAssignDtl_expl())
	    	requireTextView.setText("Detailed explanation"+"\r\n" );
	    if(((ProductAssignment)DashboardActivityAlt.listItem.getProduct().getProduct()).getAssignExcVideo())
	    	requireTextView.append("Exclusive video"+"\r\n" );
	    if(((ProductAssignment)DashboardActivityAlt.listItem.getProduct().getProduct()).getAssignCommVideo())
	    	requireTextView.append("Common video" );
	    	
	    productTextView.setText(DashboardActivityAlt.listItem.getProduct().getProductType());
	    Log.i("listItem in orderInfoAA", DashboardActivityAlt.listItem.toString());
	    addFiles();
	    
	    
//	    Log.i("category order",DashboardActivityAlt.listItem.getCategory().getCategoryTitle());
//	    Log.i("subject order",DashboardActivityAlt.listItem.getCategory().getCategorySubject().getSubjectTitle());
//	    Log.i("level order",DashboardActivityAlt.listItem.getLevel().getLevelTitle());
	    
	    
	    
//	    btnClose.setOnClickListener(new View.OnClickListener() {
//	           public void onClick(View view) {
//	               Intent i = new Intent(getApplicationContext(),
//	                       DashboardActivityAlt.class);
//	               startActivity(i);
//	               
//	           }
//	       });
//        btnInteractions.setOnClickListener(new View.OnClickListener() {
//	    	   
//	           public void onClick(View view) {
//	               Intent i = new Intent(getApplicationContext(),
//	                       InteractionsActivityViewPager.class);
//	               startActivity(i);
//	               
//	           }
//	       });
	    preferenceManager
        = PreferenceManager.getDefaultSharedPreferences(this);
       downloadManager
        = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
//       downloadFilesReceiver = new DownloadReceiver();
       try{
       if (!DashboardActivityAlt.listItem.getOrderFiles().isEmpty())
       {
    	   for (Files b: DashboardActivityAlt.listItem.getOrderFiles())
			{
//    		   downloadFilesReceiver.setContext(this);
//    		   downloadFilesReceiver.downloadFile(b);
				try{
				 Uri downloadUri = Uri.parse(b.getFileFullPath());
           	   DownloadManager.Request request = new DownloadManager.Request(downloadUri);
           	   long id = downloadManager.enqueue(request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
           			   "AssignmentExpert/" + b.getFileName()));
           	   Editor PrefEdit = preferenceManager.edit();
           	   PrefEdit.putLong(strPref_Download_ID, id);
           	   PrefEdit.commit();
				}
				catch(NullPointerException e)
				{
					e.printStackTrace();
				}
			}
       }
       }
       catch(Exception e)
       {
    	   e.printStackTrace();
       }
	}
	public void addFiles()
	 {
		try
		{
	    if(!DashboardActivityAlt.listItem.getOrderFiles().isEmpty())
	    {
	    	final TextView tv[] = new TextView[DashboardActivityAlt.listItem.getOrderFiles().size()];
		         for (int i = 0; i< DashboardActivityAlt.listItem.getOrderFiles().size();i++)
		          {
		            
		            	View line = new View(this);
		                line.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
		                line.setBackgroundColor(0xAA345556);
		                tv[i] = new TextView(this);
		                tv[i].setId(i);
		                tv[i].setTag(i);
		                tv[i].setTextColor(Color.WHITE);
		                tv[i].setTextSize(12);
		                tv[i].setCompoundDrawablesWithIntrinsicBounds(
		                        0, R.drawable.file, 0, 0);
		                tv[i].setText(DashboardActivityAlt.listItem.getOrderFiles().get(i).getFileName());
		                
		                tv[i].setOnClickListener(new FrequentlyUsedMethods(OrderInfoActivityAA.this).fileInfoListenter);
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
	  private BroadcastReceiver downloadFilesReceiver = new BroadcastReceiver() {

    	  @Override
    	  public void onReceive(Context arg0, Intent arg1) {
    	   // TODO Auto-generated method stub
    	   DownloadManager.Query query = new DownloadManager.Query();
    	   query.setFilterById(preferenceManager.getLong(strPref_Download_ID, 0));
    	   Cursor cursor = downloadManager.query(query);
    	   if(cursor.moveToFirst()){
    	    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
    	    int status = cursor.getInt(columnIndex);
    	    if(status == DownloadManager.STATUS_SUCCESSFUL){
    	     
    	     //Retrieve the saved request id
    	     long downloadID = preferenceManager.getLong(strPref_Download_ID, 0);
    	     
    	     ParcelFileDescriptor file;
    	     try {
    	      file = downloadManager.openDownloadedFile(downloadID);
//    	      FileInputStream fileInputStream
//    	       = new ParcelFileDescriptor.AutoCloseInputStream(file);
//    	      Bitmap bm = BitmapFactory.decodeStream(fileInputStream);
//    	      Drawable d =new BitmapDrawable(bm);
//    	      cancelProfile.setBackgroundDrawable(d);
    	     } catch (FileNotFoundException e) {
    	      // TODO Auto-generated catch block
    	      e.printStackTrace();
    	     }
    	     
    	    }
    	   }
    	  } 
    	 };
	@Override
	public void onResume()
	{
		 InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		super.onResume();
		IntentFilter intentFilter
		   = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		  registerReceiver(downloadFilesReceiver, intentFilter);
		
	}
	@Override
	public void onPause()
	{
		super.onPause();
		unregisterReceiver(downloadFilesReceiver);
	}
	@Override
    public void onBackPressed() {
		Intent i = new Intent(getApplicationContext(),
                DashboardActivityAlt.class);
        startActivity(i);
    }
}
