package com.assignmentexpert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.asynctasks.LoginAsync;
import com.customitems.CustomTextView;
import com.customitems.DatePreference;
import com.datamodel.Category;
import com.datamodel.EssayCreationStyle;
import com.datamodel.EssayType;
import com.datamodel.Level;
import com.datamodel.NumberOfReferences;
import com.datamodel.NumberPages;
import com.datamodel.Subject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.library.DatabaseHandler;
import com.library.FrequentlyUsedMethods;

public class AssignmentPref extends PreferenceActivity {
    private ListView fileList;
	private Button btnSelectFiles;
	private SharedPreferences prefs;
	private Editor prefEditor;
	private ArrayAdapter<File> adapter;
	ArrayList<Integer> checks=new ArrayList<Integer>();
	private int currFilePos;
	private Button btnFilesRemove;
	private ListView mainList;
	View assignfooter;
	View editsfooter;
	Context context;
	private PreferenceScreen mFilterShow;
	private View filesList;
	private View newAssign;
	
	 ListView customfileList;
	private Button btnSubmitOrder;
	private ListPreference subjectPref;
	private ListPreference categoryPref;
	private ListPreference levelPref;
	private DatePreference deadlinePref;
	private ListPreference timezonePref;
	
	 static final int DATE_DIALOG_ID = 1;
	 static final int TIME_DIALOG_ID = 0;
	 
	 
	 private int mDay;
	 private int mHour;
	 private int mMinute;
	 private int mYear;
	 private int mMonth;
	private ListPreference nmbPages;
	private ListPreference typePref;
	private ListPreference nmbRefs;
	private ListPreference crStyle;
	private ImageView orderInfo;
	private ImageView taskInfo;
	boolean trigger = false;
	 CustomTextView textHead;
	 CustomTextView fileSizeHead;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.assignment);
           // setContentView(R.layout.footer_listview);
            context  = this;
            
        	
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            mHour = c.get(Calendar.HOUR);
            mMinute = c.get(Calendar.MINUTE);

            
            mFilterShow =
                    (PreferenceScreen)getPreferenceScreen().findPreference("orderScreen");
            final ListPreference productList = (ListPreference) findPreference("productPref");
            
            assignfooter = getLayoutInflater().inflate(R.layout.assign_footer2, null);
            editsfooter = getLayoutInflater().inflate(R.layout.assign_footer1, null);
            filesList = getLayoutInflater().inflate(R.layout.file_list, null);
            
          //  newAssign = getLayoutInflater().inflate(R.xml.new_assign, null);
            
            orderInfo = (ImageView)editsfooter.findViewById(R.id.orderInfo);
            taskInfo = (ImageView)editsfooter.findViewById(R.id.taskInfo);
            
            customfileList = (ListView) filesList.findViewById(R.id.fileslist);
            btnSubmitOrder = (Button)assignfooter.findViewById(R.id.btnSubmitOrder);
            
            subjectPref =  (ListPreference)findPreference("subjectPref");
            categoryPref = (ListPreference)findPreference("categoryPref");
            levelPref =    (ListPreference)findPreference("levelPref");
            deadlinePref = (DatePreference)findPreference("deadlinePref");
            timezonePref = (ListPreference)findPreference("timezonePref");
            
            nmbPages  = (ListPreference)findPreference("nmbPages");
            typePref = (ListPreference)findPreference("typePref");
            crStyle = (ListPreference)findPreference("crStyle");
            nmbRefs = (ListPreference)findPreference("nmbRefs");
            
            categoryPref.setEnabled(false);
            
            mainList = getListView();
            mainList.setCacheColorHint(Color.BLACK);
            getListView().setBackgroundColor(Color.BLACK);
          //  mainList.addFooterView(footer,null,true);
            fileList = getListView();
            btnSelectFiles = (Button) editsfooter.findViewById(R.id.btnSelectFiles);
            btnFilesRemove = (Button) filesList.findViewById(R.id.btnRemoveFiles);
            
            
            btnFilesRemove.setVisibility(View.INVISIBLE);
            textHead  = (CustomTextView)filesList
    				.findViewById(android.R.id.title); 
            fileSizeHead = (CustomTextView)filesList
    				.findViewById(R.id.fileSize); 
            prefs = getSharedPreferences("newOrder", MODE_PRIVATE);
    		prefEditor = prefs.edit();
    	    mainList.addFooterView(editsfooter,null,true);
    	    mainList.addFooterView(assignfooter);
    		//mainList.addFooterView(essayfooter,null,true);
    		for(int b=0;b<FileManagerActivity.getFinalAttachFiles().size();b++)
    		{ 
    			Log.i("files to add assign prefs", FileManagerActivity.getFinalAttachFiles().get(b).toString());
    			checks.add(b,0);
    	    } 
    		mainList.setAdapter(adapter);
            // Get the custom preference
    		addSubjects();
    		addLevels();
//    		addTimeZones();
    		new FrequentlyUsedMethods(this).addTimeZones(timezonePref);
            productList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                	productList.setSummary((CharSequence) newValue);
                	if (productList.getSummary().equals("Assignment"))
                		{
                		 assignmentFieldsAdd();
                		 categoryPref.setEnabled(true);
                	
	                 		if (getPreferenceScreen().findPreference("nmbPages")!=null)
	                 		 {
	                 		    	writingFieldsDelete();
	                 		 }
                		}
                	else 
	                	{
                		 
	                	if ( getPreferenceScreen().findPreference("dtlInfo")!=null)
                		 {
                			 assignmentFieldsDelete();
                		 }
                		writingFieldsAdd();
                		categoryPref.setEnabled(false);
                   		addEssayCreationStyles();
                   		addNumberPages();
                   		addNumberReferences();
                   		addEssayTypes();
	                	}
                		
                	return true;
                }

            });
            orderInfo.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(AssignmentPref.this,
                            "The favorite list would appear on clicking this icon",
                            Toast.LENGTH_LONG).show();
                }
            });
            taskInfo.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(AssignmentPref.this,
                            "The favorite list would appear on clicking this icon",
                            Toast.LENGTH_LONG).show();
                }
            });
            subjectPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                	int index = subjectPref.findIndexOfValue(newValue.toString()) ;
                	subjectPref.setSummary(subjectPref.getEntries()[index]);
                	if (productList.getSummary().equals("Assignment"))
                	 addCategories(index) ;
                	return true;
                }

            });
            
            
            categoryPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                	int index = categoryPref.findIndexOfValue(newValue.toString()) ;
                	categoryPref.setSummary(categoryPref.getEntries()[index]);
                	return true;
                }
            });
            
            levelPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                	int index = levelPref.findIndexOfValue(newValue.toString()) ;
                	levelPref.setSummary(levelPref.getEntries()[index]);
                	return true;
                }

            });
            
            timezonePref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                	int index = timezonePref.findIndexOfValue(newValue.toString()) ;
                	timezonePref.setSummary(timezonePref.getEntries()[index]);
                	return true;
                }

            });
//            deadlinePref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//                public boolean onPreferenceChange(Preference preference, Object newValue) {
//                	int index = deadlinePref.findIndexOfValue(newValue.toString()) ;
//                	deadlinePref.setSummary(deadlinePref.getEntries()[index]);
//                	if (productList.getSummary().equals("Assignment"))
//                	 addCategories(index) ;
//                	return true;
//                }
//
//            });
           
            btnSelectFiles.setOnClickListener(new View.OnClickListener() {
             	 
                public void onClick(View view) {
                	savePreferences();
                    Intent i = new Intent(getApplicationContext(),
                            FileManagerActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("FileManager", "NewOrder");
                    i.putExtras(mBundle);
                    startActivity(i);
                    
                }
            });
            if( !FileManagerActivity.getFinalAttachFiles().isEmpty())
             	  {
            			addFiles();
            			btnFilesRemove.setVisibility(View.VISIBLE);
             	  }
               else 
             	  {
                 
            	     mainList.removeFooterView(filesList);
             	  }
            btnFilesRemove.setOnClickListener(new View.OnClickListener() {
              	 
                public void onClick(View view) {
//                	 LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//     		        View view3 = inflater.inflate(R.layout.file, null);
//     		       final CheckBox checkBox = (CheckBox)view3
//   						.findViewById(R.id.fileCheck); 
//                	if (checkBox.isChecked())
//						  checks.set(position, 1);
//					else
//						  checks.set(position, 0);
             for(int i=0;i<checks.size();i++){
            	 
            	Log.i("cheks state", Integer.toString(checks.get(i)));
            	
            	 
              if(checks.get(i)==1){
            	   adapter.remove(adapter.getItem(i));
                    checks.remove(i);
                    textHead.setText(Integer.toString(FileManagerActivity.getFinalAttachFiles().size())+ " files attached");
                    long wholeSize = 0;
                    for (File file: FileManagerActivity.getFinalAttachFiles())
                    {
                    	wholeSize += file.length();
                    }
                    fileSizeHead.setText(Long.toString(wholeSize)+ " Mb");
                    
                     i--;
                  }
              if( !FileManagerActivity.getFinalAttachFiles().isEmpty())
               	  {
            	  btnFilesRemove.setVisibility(View.VISIBLE);
            	 
               	  }
              
                 else 
                 {
               	  //btnFilesRemove.setVisibility(View.INVISIBLE);
               	 mainList.removeFooterView(filesList);
                 }
              	
                  }
             //adapter.notifyDataSetChanged();
                }
            });
            
            btnSubmitOrder.setOnClickListener(new View.OnClickListener() {
             	 
               public void onClick(View view) {
            	   Resources resources = AssignmentPref.this.getResources();
            	    XmlPullParser parser = resources.getXml(R.layout.assign_footer2);
            	    AttributeSet attributes = Xml.asAttributeSet(parser);
            	   new DatePreference(AssignmentPref.this, attributes).getDialog().show() ;
            	   
            	   
               }
               
           });
          
//            deadlinePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//            	 public boolean onPreferenceClick(Preference preference) {
//			            		 	DeadLinePicker pick = new DeadLinePicker(AssignmentPref.this);
//			            		 	pick.updateDisplay();
//            	    				  return false;
//            	    			}
//            });
//   
    			
           
            
//            deadlinePref.setOnEditorActionListener(new OnEditorActionListener() {
//               
//                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                    if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    	deadline.setFocusable(false);
//                    	
//                    }
//                    return false;
//                }
//            });
     
    }
	public void assignmentFieldsAdd()
	{
		addPreferencesFromResource(R.xml.new_assign);
		
	}
	public void assignmentFieldsDelete()
	{
		getPreferenceScreen().removePreference(getPreferenceScreen().findPreference("dtlInfo"));
		getPreferenceScreen().removePreference(getPreferenceScreen().findPreference("ExcVideo"));
		getPreferenceScreen().removePreference(getPreferenceScreen().findPreference("commVideo"));
		
	}
	public void writingFieldsAdd()
	{
		addPreferencesFromResource(R.xml.new_writing);
	}
	public void writingFieldsDelete()
	{
		
		getPreferenceScreen().removePreference(getPreferenceScreen().findPreference("nmbPages"));
		getPreferenceScreen().removePreference(getPreferenceScreen().findPreference("typePref"));
		getPreferenceScreen().removePreference(getPreferenceScreen().findPreference("crStyle"));
		getPreferenceScreen().removePreference(getPreferenceScreen().findPreference("nmbRefs"));
		
	}
	public boolean getTrigger()
	{return this.trigger;}
	public void setTrigger(boolean trigger)
	{this.trigger = trigger;}
	public void addFiles()
    {
		
		if (mainList.getAdapter()!=null) 
		{
			Log.i("listView is empty", "preferences");
			mainList.removeFooterView(assignfooter);
		}

		adapter = new ArrayAdapter<File>(this,
				R.layout.assign_footer2, R.id.fileCheck,
				FileManagerActivity.getFinalAttachFiles()) 
				{
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) 
			{
		        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        View view = inflater.inflate(R.layout.file, null);
	            view.setFocusable(false);
	            RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	            relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	            CustomTextView textView = (CustomTextView)view
						.findViewById(android.R.id.title); 
	            CustomTextView fileSize = (CustomTextView)view
						.findViewById(R.id.fileSize); 
	            final CheckBox checkBox = (CheckBox)view
						.findViewById(R.id.fileCheck); 
				textView.setClickable(true);
				
				textView.setText(FileManagerActivity.getFinalAttachFiles().get(position).getName().toString());
				
				fileSize.setText(Long.toString(FileManagerActivity.getFinalAttachFiles().get(position).length())+ " Mb");
				for (int i = 0; i< FileManagerActivity.getFinalAttachFiles().size();i++)
		         {
						textView.setTag(i);
		         }
				
				view.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						
//					
						
						if (!getTrigger())
							setTrigger(true);
						else 
							setTrigger(false);
						checkBox.setChecked(getTrigger());
						Log.i("checkable state", Boolean.toString(checkBox.isChecked()));
						if (checkBox.isChecked())
							  checks.set(position, 1);
						else
							  checks.set(position, 0);
		            }
		        });
				
				textView.setOnLongClickListener(onclicklistener);
				return view;
			}
		};
		
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.file, null);
        view.setFocusable(false);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      
       
      
        
        textHead.setTextSize(17);
        textHead.setText(Integer.toString(FileManagerActivity.getFinalAttachFiles().size())+ " files attached");
        long wholeSize = 0;
        for (File file: FileManagerActivity.getFinalAttachFiles())
        {
        	wholeSize += file.length();
        }
        
        fileSizeHead.setText(Long.toString(wholeSize)+ " Mb");
		customfileList.setAdapter(adapter);
		mainList.addFooterView(filesList);
		mainList.addFooterView(assignfooter);
    }
	 public void savePreferences()
	 {
//		 	prefEditor.putString("taskReq",taskReq.getText().toString());
//			prefEditor.putString("orderTitle",orderTitle.getText().toString());
//			prefEditor.putString("deadline",deadline.getText().toString());
//
//			prefEditor.putInt("LevelsValue", levelSpin.getSelectedItemPosition());
//			prefEditor.putInt("CategoriesValue", catSpin.getSelectedItemPosition());
//			prefEditor.putInt("SubjectsValue", subjSpin.getSelectedItemPosition());
//			
//			prefEditor.putInt("detailedExp", explanationReqInt);
//			prefEditor.putInt("exclVideo", exclVideoInt);
//			prefEditor.putInt("commonVideo", commVideoInt);
//			
//			prefEditor.commit();
		 
	 }
	 OnLongClickListener onclicklistener = new OnLongClickListener() {
			public boolean onLongClick(View arg0) {
				final CharSequence[] items = {"Open", "Delete", "Details"};
				final AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentPref.this);
			    builder.setTitle(((TextView)arg0).getText().toString());
			    Log.i("view class", arg0.getClass().getName());
			    final int pos = getFilePosition(arg0);
			    Log.i("position ", Integer.toString(pos));
			    //Log.i("view name", ((CheckBox)arg0).getText().toString());
			 //   Log.i("view tag", (((CheckBox)arg0).getTag()).toString());
			    Integer position  = (Integer) ((CustomTextView)arg0).getTag();
			    setFilePosition(position.intValue());
			    
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	if (item == 0)
				    	{
				    		File file = FileManagerActivity.getFinalAttachFiles().get(pos);
				    		
				    			Intent intent = new Intent();
				    			intent.setAction(android.content.Intent.ACTION_VIEW);
				    			intent.setDataAndType(Uri.fromFile(file), "text/plain");
				    			startActivity(intent);
				    			Log.i("file position in new Order open", Integer.toString(pos));
							
				    	}
				    	 else if (item == 1)
				    		{  
				    		 
				    		   if (FileManagerActivity.getFinalAttachFiles().size()==1)
				    		   {  
				    			   FileManagerActivity.getFinalAttachFiles().clear();
				    			   adapter.clear();
				    			   adapter.notifyDataSetChanged();
				    			   btnFilesRemove.setVisibility(View.INVISIBLE);
				    		   }
				    		   else
				    			   { 
				    				   try
				    				   {
				    					   
					    				     adapter.remove(FileManagerActivity.getFinalAttachFiles().get(pos));
							    		     Log.i("count",Integer.toString(FileManagerActivity.getFinalAttachFiles().size()));
							    		     adapter.notifyDataSetChanged();
				    				   }
				    				   catch(IndexOutOfBoundsException e)
				    				   {
				    					 //  FileManagerActivity.getFinalAttachFiles().remove(position.intValue());
				    					 //  Log.i("exception deleted position",FileManagerActivity.getFinalAttachFiles().get(position.intValue()).getName() );
				    					   e.printStackTrace();
				    				   }
				    				  
				    				   for (File f :  FileManagerActivity.getFinalAttachFiles())
						    		     {
						    		    	 Log.i("file container", f.getName());
						    		     } 
				    				   Log.i("file manager size", Integer.toString( FileManagerActivity.getFinalAttachFiles().size()));
				    			   }
				    		}
				    	else if (item == 2)
				    	{
				    		File file = FileManagerActivity.getFinalAttachFiles().get(pos);
				    		Log.i("file position in new Order details", Integer.toString(pos));
				    		AlertDialog.Builder builder2 = new AlertDialog.Builder(AssignmentPref.this);
				  		    builder2.setTitle(file.getName());
				  		  FileInputStream fis;
						try {
							fis = new FileInputStream(file);
							builder2.setMessage("Size of file is: " + Long.toString(fis.getChannel().size())+ "  KB"+"\r\n"+
							"Path of file is: " +"\r\n" + file.getPath());

						} catch (FileNotFoundException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    			
				  		    
				  		  AlertDialog alert = builder2.create();
				  			alert.show();
				    	}
				    	
				    	 
				    }
				});
				AlertDialog alert = builder.create();
				alert.show();
				return true;
			}
		};
	
		 public int getFilePosition(View v)
		 {
			 int pos = 0;
			for (int i = 0; i<adapter.getCount();i++)
			{
				Log.i("fileList item", adapter.getItem(i).getName());
				if (((CustomTextView)v).getText().equals((adapter.getItem(i).getName())))
				 pos = i;
			}
			 return pos;
		 }
		 public void setFilePosition(int position)
		 {
			 this.currFilePos = position;
		 }
		  private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
	    	    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
	    	      mHour = hourOfDay;
	    	      mMinute = minuteOfHour;
	    	      updateDisplay();

	    	    }
	    	  };
	    	  
	    		 DatePickerDialog.OnDateSetListener mDateSetListener =
	    	             new DatePickerDialog.OnDateSetListener() {
	    	             public void onDateSet(DatePicker view, int year, int monthOfYear,
	    	                             int dayOfMonth) {
	    	                     mYear = year;
	    	                     mMonth = monthOfYear;
	    	                     mDay = dayOfMonth;
	    	                    
	    	                     updateDisplay();
	    	             }

	    					
	    	     };
				private List<Subject> subjectsListSpinner;
				private List<Level> levelsListSpinner;
				private List<EssayCreationStyle> essayCreationStyleListSpinner;
				private List<EssayType> essayTypeListSpinner;
			@Override
		    protected Dialog onCreateDialog(int id) {
		            switch (id) {
		            case DATE_DIALOG_ID:
		                    return new DatePickerDialog(this,
		                            mDateSetListener,
		                            mYear, mMonth, mDay);
		                   
		            case TIME_DIALOG_ID:
		                return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
		                    false);
		            }
		            return null;
		    }
		    protected void onPrepareDialog(int id, Dialog dialog) {
		            switch (id) {
		            case DATE_DIALOG_ID:
		                    ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
		                    break;}
		            }
			private void updateDisplay() {
				String minutes = null;
				if (mMinute>9)
					minutes = Integer.toString(mMinute);
				else 
					minutes = "0"+Integer.toString(mMinute);
				deadlinePref.setSummary(
		                new StringBuilder()
		                // Month is 0 based so add 1
		                .append(mYear).append("-")
		                .append(mMonth + 1).append("-")
		                .append(mDay)
		                .append(" ").append(mHour).append(":").append(minutes)
		                );
				showDialog(TIME_DIALOG_ID);
		}
			
			 public void addSubjects() {
				 DatabaseHandler db = new DatabaseHandler(getApplicationContext());
				 
				 try {
					
					Dao<Subject,Integer> daoSubject = db.getSubjectDao();
					subjectsListSpinner = daoSubject.queryForAll();
					final ArrayAdapter<Subject> dataAdapter = new ArrayAdapter<Subject>(this,
					android.R.layout.simple_spinner_item, subjectsListSpinner);
					dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					CharSequence[] entries = new CharSequence[dataAdapter.getCount()];
				    CharSequence[] entryValues = new CharSequence[dataAdapter.getCount()];
				    int i = 0;
				    for (Subject dev : subjectsListSpinner)
				    {
				    	entries[i] = dev.getSubjectTitle();
			            entryValues[i] = Integer.toString(dev.getSubjectId());
			            Log.i("subject items", entries[i].toString() +  entryValues[i].toString());
			            i++;
			            
				    }
				    subjectPref.setEntries(entries);
				    subjectPref.setEntryValues(entryValues);
				    
					}
				  catch (SQLException e) {
				
					e.printStackTrace();
				} catch (java.sql.SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			 }
			
		
				public void addCategories(int index) {
					 DatabaseHandler db = new DatabaseHandler(getApplicationContext());
					 try {

						 Dao<Category,Integer> daoCategory = db.getCategoryDao();
						 QueryBuilder<Category, Integer> qb = daoCategory.queryBuilder();
						 final List<Category> categories =
								 daoCategory.queryBuilder().where().
								    eq("subject_id", index+1).query();//Integer.parseInt(subjectPref.getEntry().toString())).query();
						ArrayAdapter<Category> dataAdapter = new ArrayAdapter<Category>(this,
								android.R.layout.simple_spinner_item, categories);
						dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						CharSequence[] entries = new CharSequence[dataAdapter.getCount()];
					    CharSequence[] entryValues = new CharSequence[dataAdapter.getCount()];
					    int i = 0;
					    for (Category dev : categories)
					    {
					    	entries[i] = dev.getCategoryTitle();
				            entryValues[i] = Integer.toString(dev.getCategoryId());
				            Log.i("subject items", entries[i].toString() +  entryValues[i].toString());
				            i++;
				            
					    }
					    categoryPref.setEntries(entries);
					    categoryPref.setEntryValues(entryValues);
					 }
						 
					  catch (SQLException e) {
						// TODO Auto-generated catch block
			
						e.printStackTrace();
					} catch (java.sql.SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
				 }
				
				 public void addLevels() {
					 DatabaseHandler db = new DatabaseHandler(getApplicationContext());
					 try {
						Dao<Level,Integer> daoSubject = db.getLevelDao();
						levelsListSpinner = daoSubject.queryForAll();
						ArrayAdapter<Level> dataAdapter = new ArrayAdapter<Level>(this,
						android.R.layout.simple_spinner_item, levelsListSpinner);
						dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						CharSequence[] entries = new CharSequence[dataAdapter.getCount()];
					    CharSequence[] entryValues = new CharSequence[dataAdapter.getCount()];
					    int i = 0;
					    for (Level dev : levelsListSpinner)
					    {
					    	entries[i] = dev.getLevelTitle();
				            entryValues[i] = Integer.toString(dev.getLevelId());
				            Log.i("subject items", entries[i].toString() +  entryValues[i].toString());
				            i++;
				            
					    }
					    levelPref.setEntries(entries);
					    levelPref.setEntryValues(entryValues);
						}
					  catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (java.sql.SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
				 }
				 
				 public void addTimeZones() {
						//
						final String[] TZ = TimeZone.getAvailableIDs();

						Log.i("count", Integer.toString(TZ.length));
						ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
								this, android.R.layout.simple_spinner_item);
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

						final ArrayList<String> TZ1 = new ArrayList<String>();
						for (int i = 0; i < TZ.length; i++) {
							if (!(TZ1.contains(TimeZone.getTimeZone(TZ[i]).getID()))) {
								if (new FrequentlyUsedMethods(AssignmentPref.this).timezoneValidate(TZ[i]))
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
								Log.i("current timezone in a loop", TZ1.get(i));
							}
				            Log.i("subject items", entries[i].toString() +  entryValues[i].toString());
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
					    Log.i("choosed timezone2",currTimeZone);
					    timezonePref.setSummary(currTimeZone );
						
					}
				 
					public void addEssayCreationStyles() {
						DatabaseHandler db = new DatabaseHandler(getApplicationContext());
						try {
							Dao<EssayCreationStyle, Integer> daoSubject = db.getEssayCreationStyleDao();
							essayCreationStyleListSpinner = daoSubject.queryForAll();
							ArrayAdapter<EssayCreationStyle> dataAdapter = new ArrayAdapter<EssayCreationStyle>(this,
									android.R.layout.simple_spinner_item, essayCreationStyleListSpinner);
							dataAdapter	.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							
							CharSequence[] entries = new CharSequence[dataAdapter.getCount()];
						    CharSequence[] entryValues = new CharSequence[dataAdapter.getCount()];
						    int i = 0;
						    for (EssayCreationStyle dev : essayCreationStyleListSpinner)
						    {
						    	entries[i] = dev.getECSTitle();
					            entryValues[i] = Integer.toString(dev.getECSId());
					            Log.i("subject items", entries[i].toString() +  entryValues[i].toString());
					            i++;
					            
						    }
						    crStyle = (ListPreference)findPreference("crStyle");
						    crStyle.setEntries(entries);
						    crStyle.setEntryValues(entryValues);
						    crStyle.setOnPreferenceChangeListener(crStyleListener);
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (java.sql.SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				
					}
					public void addNumberPages() {
						try
						{
							
							nmbPages = (ListPreference) findPreference("nmbPages");
							Collections.sort(LoginAsync.numberPagesList, new Comparator<NumberPages>() {
							    private int fixString(NumberPages in) {
							    	int res = 0;
							    		if (in.getNumberPage().length()>2)
							             {
							    			if (numbersCheck(in.getNumberPage()))
							    			res= Integer.parseInt((in.getNumberPage()).substring(0, in.getNumberPage().indexOf('_')));
							             }
							    		else 
							    		 {
							    			if (numbersCheck(in.getNumberPage()))
							    				res= Integer.parseInt((in.getNumberPage()));
							    		 }
									return res;
							    }
								public int compare(NumberPages lhs, NumberPages rhs) 
								{
									
									int res = fixString(lhs) - fixString(rhs);
					        		if (res == 0)
					        		{
					        			if (lhs.getNumberPage().length() < rhs.getNumberPage().length())
					        				return -1;
					        			else
					        			    return 0;
					        		}
					        		else if (res > 0)
					        			return 1;
					        		else 
					        			return -1;   
								}
							});
						}
						catch(StringIndexOutOfBoundsException e)
						{
							e.printStackTrace();
						}
							ArrayAdapter<NumberPages> dataAdapter = new ArrayAdapter<NumberPages>(this,
									android.R.layout.simple_spinner_item, LoginAsync.numberPagesList);
							dataAdapter	.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							CharSequence[] entries = new CharSequence[dataAdapter.getCount()];
						    CharSequence[] entryValues = new CharSequence[dataAdapter.getCount()];
						    int i = 0;
						    for (NumberPages dev : LoginAsync.numberPagesList)
						    {
						    	entries[i] = dev.getNumberPage();
					            entryValues[i] = dev.getNumberPage();
					            Log.i("subject items", entries[i].toString() +  entryValues[i].toString());
					            i++;
					            
						    }
						    nmbPages.setEntries(entries);
						    nmbPages.setEntryValues(entryValues);
						    nmbPages.setOnPreferenceChangeListener(nmbPagesListener);
					}
						public void addNumberReferences() {
							nmbRefs = (ListPreference)findPreference("nmbRefs");
							Collections.sort(LoginAsync.numberReferencesList, new Comparator<NumberOfReferences>() {
							    private int fixString(NumberOfReferences in) {
							    	int res = 0;
							    	
							    		if (in.getNumberReference().length()>2)
							             {
							    			if (numbersCheck(in.getNumberReference()))
							    			res =  Integer.parseInt((in.getNumberReference()).substring(0, in.getNumberReference().indexOf('_')));
							             }
							    		else 
							    		 {
							    			if (numbersCheck(in.getNumberReference()))
							    			res= Integer.parseInt((in.getNumberReference()));
							    		 }
							    			    	
							    	return res;
							    }
								public int compare(NumberOfReferences lhs, NumberOfReferences rhs) {
								
									int res = fixString(lhs) - fixString(rhs);
					        		if (res == 0)
					        		{
					        			if (lhs.getNumberReference().length() < rhs.getNumberReference().length())
					        				return -1;
					        			else
					        			    return 0;
					        		}
					        		else if (res > 0)
					        			return 1;
					        		else 
					        			return -1;   
								}
								
							});
							ArrayAdapter<NumberOfReferences> dataAdapter = new ArrayAdapter<NumberOfReferences>(this,
									android.R.layout.simple_spinner_item, LoginAsync.numberReferencesList);
							dataAdapter	.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							CharSequence[] entries = new CharSequence[dataAdapter.getCount()];
						    CharSequence[] entryValues = new CharSequence[dataAdapter.getCount()];
						    int i = 0;
						    for (NumberOfReferences dev : LoginAsync.numberReferencesList)
						    {
						    	entries[i] = dev.getNumberReference();
					            entryValues[i] =dev.getNumberReference();
					            Log.i("subject items", entries[i].toString() +  entryValues[i].toString());
					            i++;
					            
						    }
						    nmbRefs.setEntries(entries);
						    nmbRefs.setEntryValues(entryValues);
						    nmbRefs.setOnPreferenceChangeListener(nmbRefsListener);
					
					}
						
						public void addEssayTypes() {
							DatabaseHandler db = new DatabaseHandler(getApplicationContext());
							try {
								 typePref = (ListPreference)findPreference("typePref");
								Dao<EssayType, Integer> daoSubject = db.getEssayTypeDao();
								essayTypeListSpinner = daoSubject.queryForAll();
								ArrayAdapter<EssayType> dataAdapter = new ArrayAdapter<EssayType>(this,
										android.R.layout.simple_spinner_item, essayTypeListSpinner);
								dataAdapter	.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								CharSequence[] entries = new CharSequence[dataAdapter.getCount()];
							    CharSequence[] entryValues = new CharSequence[dataAdapter.getCount()];
							    int i = 0;
							    for (EssayType dev : essayTypeListSpinner)
							    {
							    	entries[i] = dev.getEssayTypeTitle();
						            entryValues[i] =Integer.toString(dev.getEssayTypeId());
						            Log.i("subject items", entries[i].toString() +  entryValues[i].toString());
						            i++;
						            
							    }
							    typePref.setEntries(entries);
							    typePref.setEntryValues(entryValues);
							    typePref.setOnPreferenceChangeListener(typePrefListener);
					
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (java.sql.SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					
						}
					 boolean numbersCheck(String email)
					    {
					    	Pattern pattern = Pattern.compile("^[0-9]{1,2}.*");
								Matcher matcher = pattern.matcher(email);
								boolean matchFound = matcher.matches();
					    	return matchFound;
					    }
					public OnPreferenceChangeListener crStyleListener = new OnPreferenceChangeListener() {        

						public boolean onPreferenceChange(
								Preference preference, Object newValue) {
							int index = crStyle.findIndexOfValue(newValue.toString()) ;
		                 	crStyle.setSummary(crStyle.getEntries()[index]);
							return false;
						}
				    };
				    
				    public OnPreferenceChangeListener nmbPagesListener = new OnPreferenceChangeListener() {        

						public boolean onPreferenceChange(
								Preference preference, Object newValue) {
							int index = nmbPages.findIndexOfValue(newValue.toString()) ;
							nmbPages.setSummary(nmbPages.getEntries()[index]);
							return false;
						}
				    };
				    public OnPreferenceChangeListener nmbRefsListener = new OnPreferenceChangeListener() {        

						public boolean onPreferenceChange(
								Preference preference, Object newValue) {
							int index = nmbRefs.findIndexOfValue(newValue.toString()) ;
							nmbRefs.setSummary(nmbRefs.getEntries()[index]);
							return false;
						}
				    };
				    public OnPreferenceChangeListener typePrefListener = new OnPreferenceChangeListener() {        

						public boolean onPreferenceChange(
								Preference preference, Object newValue) {
							int index = typePref.findIndexOfValue(newValue.toString()) ;
							typePref.setSummary(typePref.getEntries()[index]);
							return false;
						}
				    };
	}
