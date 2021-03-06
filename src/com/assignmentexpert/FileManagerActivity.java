package com.assignmentexpert;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.library.Item;

@SuppressLint("ParserError")
public class FileManagerActivity extends Activity {

	// Stores names of traversed directories
	ArrayList<String> str = new ArrayList<String>();
	ArrayList<File> resFiles = new ArrayList<File>();
	static ArrayList<File> finalAttachFiles = new ArrayList<File>();
	// Check if the first level of the directory structure is the one showing
	private Boolean firstLvl = true;
	Dialog dialog = null;
	private static final String TAG = "F_PATH";
	View file_list;
	public static Item[] fileList;
	private File path = new File(Environment.getExternalStorageDirectory() + "");
	private String chosenFile;
	private static final int DIALOG_LOAD_FILE = 1000;
	AlertDialog.Builder builder;
	ArrayAdapter adapter;
	TextView textView;
	Button btnAttach;
	Button btnCancel;
	ListView lv ;
	AlertDialog alertDialog;
	boolean hasFile = true;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_manager);
		btnAttach = (Button) findViewById(R.id.btnAttach);
		btnCancel = (Button) findViewById(R.id.btnCancelAttach);
		
			
		lv = (ListView)findViewById(R.id.fileManagerList);
		loadFileList();
		

		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setFocusable(false);
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
		      public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) 
		      {
		      
		    	myView.setFocusable(false);
		    	chosenFile = fileList[myItemInt].file;
				File sel = new File(path + "/" + chosenFile);
				Log.i("path",sel.toString());
		        if (sel.isDirectory()) {
		        	
					firstLvl = false;

					str.add(chosenFile);
					
					fileList = null;
					path = new File(sel + "");
					
					loadFileList();
					
					lv.setAdapter(adapter);
	
				}
		        else if (chosenFile.equalsIgnoreCase("up") && !sel.exists()) {

					// present directory removed from list
					String s = str.remove(str.size() - 1);

					// path modified to exclude present directory
					path = new File(path.toString().substring(0,
							path.toString().lastIndexOf(s)));
					fileList = null;
					// if there are no more directories in the list, then
					// its the first level
					
					if (str.isEmpty()) {
						firstLvl = true;
					}
					loadFileList();
					lv.setAdapter(adapter);
		        }
		        else
		        {
		        	
				    fileList[myItemInt].toggle(); 
				    if(fileList[myItemInt].file.equalsIgnoreCase("select all") & (fileList[myItemInt].check == true))
	        		{
				    	String selectAllFile = fileList[myItemInt].file;
						File all = new File(path + "/" + selectAllFile);
				    	 for(int i =0;i<fileList.length;i++)
							{
				    		 	chosenFile = fileList[i].file;
								File select = new File(path + "/" + chosenFile);
						    	 fileList[i].setItemCheck(true);
				        		
				        		 
				        		 if (select.isFile())
				        			 {		 	
					        			 resFiles.add(select);
					        			 resFiles.remove(all);
					        			 
				        			 }

				             }

				    	 adapter.notifyDataSetChanged();
					}			
				    else if(fileList[myItemInt].file.equalsIgnoreCase("select all") & (fileList[myItemInt].check == false))
				    {
				    	for(int i =0;i<fileList.length;i++)
						{
				    		chosenFile = fileList[i].file;
							File select = new File(path + "/" + chosenFile);
				    		fileList[i].setItemCheck(false);
				    		if (select.isFile())
		        			 {		 	
			        			 resFiles.remove(select);
			        			 //resFiles.remove(all);
			        			 
		        			 }
				    		
						}
				    	 adapter.notifyDataSetChanged();
				    }
				    if (fileList[myItemInt].check == true)
				    	{
				    	resFiles.add(sel);
				    	 if(fileList[myItemInt].file.equalsIgnoreCase("select all"))
				    			 {
				    		    	String selectAllFile = fileList[myItemInt].file;
							    	File all = new File(path + "/" + selectAllFile);
								    resFiles.remove(all);
				    			 }
				    	}
				    else
				    resFiles.remove(sel);
					adapter.notifyDataSetChanged();
					Log.i(fileList[myItemInt].file,Boolean.toString(fileList[myItemInt].check));
		        }   
		}

	});
		
		 btnAttach.setOnClickListener(new View.OnClickListener() {
	           public void onClick(View view) {
	        	
	        	   if (finalAttachFiles.isEmpty())
	        		   {
	        		   		finalAttachFiles = resFiles;
	        		   }
	        	   else 
	        	   {
	        		   for (File it: resFiles)
	        		   finalAttachFiles.add(it);
	        	   }
	        	   if (!resFiles.isEmpty())
	        	   {  Intent i = new Intent(getApplicationContext(),
	                       NewOrderActivity.class);
	               		startActivity(i);
	               }
	        	   else
	        	   {
	        		   Toast toast = Toast.makeText(getApplicationContext(), "You should choose at least one file", Toast.LENGTH_SHORT);
	        		   toast.show();
	        	   }
	               
	           }
	       });
		 btnCancel.setOnClickListener(new View.OnClickListener() {
	    	   
	           public void onClick(View view) {
	           	
	               Intent i = new Intent(getApplicationContext(),
	                       NewOrderActivity.class);
	               startActivity(i);
	               
	           }
	       });
	

	
	}

	private void loadFileList() {
		
		try {
			path.mkdirs();
		} catch (SecurityException e) {
			Log.e(TAG, "unable to write on the sd card ");
		}

		// Checks whether path exists
		if (path.exists()) {
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String filename) {
					File sel = new File(dir, filename);
					// Filters based on whether the file is hidden or not
					return (sel.isFile() || sel.isDirectory())
							&& !sel.isHidden();
				}
			};

			String[] fList = path.list(filter);
			fileList = new Item[fList.length];
			for (int i = 0; i < fList.length; i++) {
				 	
				fileList[i] = new Item(fList[i], R.drawable.file_icon, false);
				// Convert into file path
				
				File sel = new File(path, fList[i]);
				hasFile = true;
			
				// Set drawables
				if (sel.isDirectory()) {
					fileList[i].icon = R.drawable.directory_icon;
					hasFile = false;
				} 
				else 
				{

				}
				if (resFiles.contains(sel))
					fileList[i].setItemCheck(true);		
			}
			if (hasFile)
			{
				Item temp[] = new Item[fileList.length + 1];
				for (int i = 0; i < fileList.length; i++) {
					temp[i + 1] = fileList[i];
				}
				temp[0] = new Item("Select all", R.drawable.stand_logo,false);
				fileList = temp;
				
			}

			if (!firstLvl) {
				Log.i("I'm not at first level","not first level");
				Item temp[] = new Item[fileList.length + 1];
				for (int i = 0; i < fileList.length; i++) {
					temp[i + 1] = fileList[i];
				}
				temp[0] = new Item("Up", R.drawable.directory_up,false);
				fileList = temp;
			}
			else if(firstLvl)
			{
				Item temp[] = new Item[fileList.length + 1];
				for (int i = 0; i < fileList.length; i++) {
					temp[i + 1] = fileList[i];
				}
				temp[0] = new Item("Select all", R.drawable.stand_logo,false);
				fileList = temp;
			}
		} else {
	
				
		}
			

		adapter = new ArrayAdapter<Item>(this,
				R.layout.file_manager, R.id.checkedTextItem,
				fileList) 
				{
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// creates view
		        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            View view = inflater.inflate(R.layout.item, null);
	            view.setFocusable(false);
				CheckedTextView textView = (CheckedTextView) view
						.findViewById(R.id.checkedTextItem);
				// put the image on the text view
				textView.setCompoundDrawablesWithIntrinsicBounds(
						fileList[position].icon, 0, 0, 0);				
				textView.setTextColor(Color.WHITE);
				textView.setText(fileList[position].file);
				if(fileList[position].icon == R.drawable.directory_icon)
					textView.setCheckMarkDrawable(null);
				else if(fileList[position].icon == R.drawable.directory_up)
					textView.setCheckMarkDrawable(null);
				if(fileList[position].check == true)
					textView.setChecked(true);
				else
					textView.setChecked(false);
				
		    	// add margin between image and text (support various screen
				// densities)
				int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
				textView.setCompoundDrawablePadding(dp5);
				
				textView.setFocusable(false);
				
				return view;
				
			}
		};
				

	}

	
//	@Override
//	public 	boolean onKeyDown(int keyCode, KeyEvent event) 
//	{
//	   if (keyCode == KeyEvent.KEYCODE_BACK && dialog.isShowing())
//	   {
//		   Intent i = new Intent(getApplicationContext(),
//                   NewOrderActivity.class);
//           startActivity(i);
//	   }
//
//	   // Call super code so we dont limit default interaction
//	   super.onKeyDown(keyCode, event);
//
//	   return true;
//	}
}