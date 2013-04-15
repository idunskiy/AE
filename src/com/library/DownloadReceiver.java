package com.library;

import java.io.FileNotFoundException;

import com.datamodel.Files;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;

public class DownloadReceiver extends BroadcastReceiver{
	SharedPreferences preferenceManager;
	DownloadManager downloadManager;
	final String strPref_Download_ID = "PREF_DOWNLOAD_ID";
	Context ctx;
//	public DownloadReceiver(Context context)
//	{
//		 preferenceManager
//	        = PreferenceManager.getDefaultSharedPreferences(context);
//	       downloadManager
//	        = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
//	}
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		  

//	   DownloadManager.Query query = new DownloadManager.Query();
//  	   query.setFilterById(preferenceManager.getLong(strPref_Download_ID, 0));
//  	   Cursor cursor = downloadManager.query(query);
//  	   if(cursor.moveToFirst()){
//  	    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
//  	    int status = cursor.getInt(columnIndex);
//  	    if(status == DownloadManager.STATUS_SUCCESSFUL){
//  	     
//  	     //Retrieve the saved request id
//  	     long downloadID = preferenceManager.getLong(strPref_Download_ID, 0);
//  	     
//  	     ParcelFileDescriptor file;
//  	     try {
//  	      file = downloadManager.openDownloadedFile(downloadID);
////  	      FileInputStream fileInputStream
////  	       = new ParcelFileDescriptor.AutoCloseInputStream(file);
////  	      Bitmap bm = BitmapFactory.decodeStream(fileInputStream);
////  	      Drawable d =new BitmapDrawable(bm);
////  	      cancelProfile.setBackgroundDrawable(d);
//  	     } catch (FileNotFoundException e) {
//  	      // TODO Auto-generated catch block
//  	      e.printStackTrace();
//  	      }
//  	     
//  	     }
//  	   }
		
	}
	
	public void downloadFile(Files file)
	{
		try{
			 Uri downloadUri = Uri.parse(file.getFileFullPath());
      	   DownloadManager.Request request = new DownloadManager.Request(downloadUri);
      	   long id = downloadManager.enqueue(request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
      			   "AssignmentExpert/"+file.getFileName()));
      	   Editor PrefEdit = preferenceManager.edit();
      	   PrefEdit.putLong(strPref_Download_ID, id);
      	   PrefEdit.commit();
			}
			catch(NullPointerException e)
			{
				e.printStackTrace();
			}
	}
	
	public void setContext(Context ctx)
	{
		 preferenceManager
	        = PreferenceManager.getDefaultSharedPreferences(ctx);
	       downloadManager
	        = (DownloadManager)ctx.getSystemService(Context.DOWNLOAD_SERVICE);
	}

}
