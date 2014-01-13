package com.library;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.assignmentexpert.R;
import com.datamodel.Files;

public class DownloadFileAsync extends AsyncTask<Files, Integer, File> {

    private Context context;
	private ProgressDialog mProgressDialog;

    public DownloadFileAsync(Context context) {
    	mProgressDialog = new ProgressDialog(context);
//    	mProgressDialog.setTitle(R.string.dialog_proceeding);
    	mProgressDialog.setMessage(context.getResources().getString(R.string.dialog_proceeding));
    	this.context = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.show();
    }
    @Override
    protected File doInBackground(Files... params) {
    	int count;
    	OutputStream output;
        try {
            URL url = new URL(params[0].getFileFullPath());
            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();
 
            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
 
            // Output stream to write file
             output = new FileOutputStream("/sdcard/download/AssignmentExpert"+params[0].getFileName());
 
            byte data[] = new byte[1024];
 
            long total = 0;
 
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
 
                // writing data to file
                output.write(data, 0, count);
            }
 
            // flushing output
            output.flush();
 
            // closing streams
            output.close();
            input.close();
 
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
        
        return  new File("/sdcard/download/AssignmentExpert"+params[0].getFileName());
    }
    @Override
    protected void onPostExecute(File result) {
        mProgressDialog.dismiss();
        if (result != null)
        {
        	Intent intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(result), "text/plain");
			context.startActivity(intent);
        }
        else
        {
        	Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
        }
    }
}