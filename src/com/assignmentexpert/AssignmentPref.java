package com.assignmentexpert;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.SQLException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.asynctasks.LoginAsync;
import com.customitems.CustomCheckBoxPref;
import com.customitems.CustomEditPreference;
import com.customitems.CustomEditText;
import com.customitems.CustomTextView;
import com.customitems.IconPreference;
import com.datamodel.Category;
import com.datamodel.EssayCreationStyle;
import com.datamodel.EssayType;
import com.datamodel.Level;
import com.datamodel.NumberOfReferences;
import com.datamodel.NumberPages;
import com.datamodel.Subject;
import com.j256.ormlite.dao.Dao;
import com.library.Constants;
import com.library.DatabaseHandler;
import com.library.FrequentlyUsedMethods;
import com.library.UserFunctions;
import com.library.singletones.OrderPreferences;

/** *Activity для отправки нового заказа. Так как, согласно дизайну, Activity похожа на Preference, наследуется от PreferenceActivity.*/
@SuppressLint("NewApi")
public class AssignmentPref extends PreferenceActivity {
	/** *кнопка выбора файлов для отправки.*/
	private Button btnSelectFiles;
	/** *sharedPreferences для сохранения настроек текущего заказа в случае, если пользователь решит покинуть текущую активность*/
	private SharedPreferences prefs;
	private Editor prefEditor;
	/** адаптер для списка файлов **/
	private ArrayAdapter<File> adapter;
	/** контейнер для отметок файлов на удаление**/
	ArrayList<Integer> checks=new ArrayList<Integer>();
	/** кнопка удаления файлов**/
	private Button btnFilesRemove;
	/**основной ListView активности**/
	private ListView mainList;
	View assignfooter;
	View editsfooter;
	
	/**Context текущей активности**/
	Context context;
	/**ListView списка файлов**/
	private View filesList;
	ListView customfileList;
	private Button btnSubmitOrder;
	/**IconPreference для тем заказа**/
	private IconPreference subjectPref;
	/**IconPreference для категорий**/
	private IconPreference categoryPref;
	/**IconPreference для уровней**/
	private IconPreference levelPref;
	/**IconPreference для временных зон**/
	private IconPreference timezonePref;
	
	/**id для диалога выбора даты**/
	 static final int DATE_DIALOG_ID = 1;
	 /**id для диалога выбора времени**/
	 static final int TIME_DIALOG_ID = 0;
	 	 
	 /**числовое значение выбранного дня**/
	 private int mDay;
	 /**числовое значение выбранного часа**/
	 private int mHour;
	 /**числовое значение выбранной минуты**/
	 private int mMinute;
	 /**числовое значение выбранного года**/
	 private int mYear;
	 /**числовое значение выбранного месяца**/
	 private int mMonth;
	 /**ListPreference числа страниц essay**/
	private ListPreference nmbPages;
	/**ListPreference типа essay**/
	private ListPreference typePref;
	/**ListPreference числа ccылок для essay**/
	private ListPreference nmbRefs;
	/**ListPreference типа создания essay**/
	private ListPreference crStyle;
	/**ImageView информации напротив EditText названия заказа**/
	private ImageView orderInfo;
	/**ImageView информации напротив EditText задания заказа**/
	private ImageView taskInfo;
	boolean trigger = false;
	/**CustomTextView для общего заголовка файлов**/
	 CustomTextView textHead;
	 /**CustomTextView для общего размера файлов**/
	 CustomTextView fileSizeHead;
	 /**CustomEditText названия заказа**/
	private CustomEditText orderTitle;
	 /**CustomEditText деталей заказа**/
	private CustomEditText taskText;
	UserFunctions launch = new UserFunctions();
	/**ListPreference продукта**/
	private ListPreference productPref;
	/**CustomEditPreference времени выполнения заказа**/
	CustomEditPreference deadlineCus;
	/**ArrayAdapter для тем заказов**/
	ArrayAdapter<Subject> subjectDataAdapter ;
	/**ArrayAdapter для категории заказов**/
	ArrayAdapter<Category> categoryDataAdapter;
	/**ArrayAdapter для уровня заказов**/
	ArrayAdapter<Level> levelDataAdapter;
	
	
	String[] prefValues;
	
	
	Object[] essayPref = new Object[4];
	/**CustomCheckBoxPref детальной информации**/
	CustomCheckBoxPref dtlInfo;
	/**CustomCheckBoxPref эсклюзивного видео**/
	CustomCheckBoxPref ExcVideo;
	/**CustomCheckBoxPref обычного видео**/
	CustomCheckBoxPref commVideo;
	
	/**ArrayAdapter количества страниц**/
	ArrayAdapter<NumberPages> nmbPageDataAdapter;
	/**ArrayAdapter количества ссылочных материалов**/
	ArrayAdapter<NumberOfReferences> nmbRefsDataAdapter ;
	/**ArrayAdapter типов essay**/
	ArrayAdapter<EssayType> essayTypeDataAdapter;
	/**ArrayAdapter типа создания essay**/
	ArrayAdapter<EssayCreationStyle> essayCrDataAdapter;
	
	/**int для дополнительной информации**/
	 private int explanationReqInt;
	 /**int для обычного видео**/
	 private int commVideoInt;
	 /**int для эксклюзивного видео**/
	 private int exclVideoInt;
	 /**CheckBox для отметки всех файлов в списке**/
	private CheckBox fileCheckHead;
	
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
            
          
            final ListPreference productList = (ListPreference) findPreference("productPref");
            
            assignfooter = getLayoutInflater().inflate(R.layout.assign_footer2, null);
            editsfooter = getLayoutInflater().inflate(R.layout.assign_footer1, null);
            deadlineCus = new CustomEditPreference(this);
            deadlineCus.setTitle("Deadline");
            deadlineCus.setSummary("Not selected");
            deadlineCus.setIcon(R.drawable.list_pointer);
            deadlineCus.setOnClickListener(new OnClickListener()
            {
				public void onClick(View arg0) {
					showDialog(DATE_DIALOG_ID);
				}
  			
            });
            
            filesList = getLayoutInflater().inflate(R.layout.file_list, null);

            orderInfo = (ImageView)editsfooter.findViewById(R.id.orderInfo);
            taskInfo = (ImageView)editsfooter.findViewById(R.id.taskInfo);
            
            customfileList = (ListView) filesList.findViewById(R.id.fileslist);
            btnSubmitOrder = (Button)assignfooter.findViewById(R.id.btnSubmitOrder);
            btnSubmitOrder.getBackground().setAlpha(120);
            subjectPref =  (IconPreference)findPreference("subjectPref");
            categoryPref = (IconPreference)findPreference("categoryPref");
            levelPref =    (IconPreference)findPreference("levelPref");
            timezonePref = (IconPreference)findPreference("timezonePref");
            productPref = (ListPreference)findPreference("productPref");

            orderTitle = (CustomEditText)editsfooter.findViewById(R.id.orderTitle);
            taskText = (CustomEditText)editsfooter.findViewById(R.id.taskText);
            
            nmbPages  = (ListPreference)findPreference("nmbPages");
            typePref = (ListPreference)findPreference("typePref");
            crStyle = (ListPreference)findPreference("crStyle");
            nmbRefs = (ListPreference)findPreference("nmbRefs");
            
            categoryPref.setEnabled(false);
            
            
            mainList = getListView();
            mainList.setCacheColorHint(Color.BLACK);
            getListView().setBackgroundColor(Color.BLACK);
            btnSelectFiles = (Button) editsfooter.findViewById(R.id.btnSelectFiles);
            btnFilesRemove = (Button) filesList.findViewById(R.id.btnRemoveFiles);
            
            
            btnFilesRemove.setVisibility(View.INVISIBLE);
            textHead  = (CustomTextView)filesList
    				.findViewById(android.R.id.title); 
            
            fileSizeHead = (CustomTextView)filesList
    				.findViewById(R.id.fileSize); 
            
            fileCheckHead = (CheckBox)filesList
    				.findViewById(R.id.fileCheck); 
            
            prefs = getSharedPreferences("newOrder", MODE_PRIVATE);
    		prefEditor = prefs.edit();
    		mainList.addFooterView(deadlineCus);
    	    mainList.addFooterView(editsfooter,null,true);
    	    mainList.addFooterView(assignfooter);
    	    
    		for(int b=0;b<FileManagerActivity.getFinalAttachFiles().size();b++)
    		{ 
    			checks.add(b,0);
    	    } 
    		mainList.setAdapter(adapter);
            // Get the custom preference
    		addSubjects();
    		addLevels();
    		new FrequentlyUsedMethods(this).addTimeZones(timezonePref);
            productList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                	productList.setSummary((CharSequence) newValue);
                	if (productList.getSummary().equals("Assignment"))
                		{
                		  if ( getPreferenceScreen().findPreference("dtlInfo") ==null)
                			  {
                			  			assignmentFieldsAdd();
                			  }
	                 		if (getPreferenceScreen().findPreference("nmbPages")!=null)
	                 		 {
	                 		    	writingFieldsDelete();
	                 		 }
                		}
                	else if (productList.getSummary().equals("Writing"))
	                	{
                		 
		                	if ( getPreferenceScreen().findPreference("dtlInfo")!=null)
	                		 {
	                			 assignmentFieldsDelete();
	                		 }
	                	 	if ( getPreferenceScreen().findPreference("nmbPages") ==null)
	                	 	 { 
		                		 writingFieldsAdd();
		                		 addEssayCreationStyles();
		                		 addNumberPages();
		                		 addNumberReferences();
		                		 addEssayTypes();
	                		 }
	                	 	categoryPref.setSummary("Not selected");
	                	 	categoryPref.setEnabled(false);
	                	}
                	 OrderPreferences.getInstance().getArrayList()[11]=productList.getSummary();
                	return true;
                }

            });
            
            orderInfo.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                	Dialog dialog = new Dialog(context, R.style.FullHeightDialog);
                	TextView myMsg = new TextView(AssignmentPref.this);
                	myMsg.setText("Specify suitable title to identify your order in the future.");
                	myMsg.setTextColor(Color.WHITE);
                	myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                	dialog.setContentView(myMsg);
                	dialog.show();
            		dialog.setCanceledOnTouchOutside(true);
                    
                }
            });
            
            
            taskInfo.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                	Dialog dialog = new Dialog(context, R.style.FullHeightDialog);
                	TextView myMsg = new TextView(AssignmentPref.this);
                	myMsg.setText("Specify suitable task to identify your order in the future.");
                	myMsg.setTextColor(Color.WHITE);
                	myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                	dialog.setContentView(myMsg);
                	dialog.show();
            		dialog.setCanceledOnTouchOutside(true);
                }
            });
            
            
            categoryPref.setEnabled(false);
            subjectPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                	int index = subjectPref.findIndexOfValue(newValue.toString()) ;
                	subjectPref.setSummary(subjectPref.getEntries()[index]);
                	Subject a = subjectDataAdapter.getItem(index);
                	Log.i("choosed subject item",a.getSubjectTitle());
                	 OrderPreferences.getInstance().getArrayList()[0]=Integer.toString(a.getSubjectId());
                	if (productList.getSummary().equals("Assignment"))
                	 {
                		addCategories(index+1) ;
                		categoryPref.setEnabled(true);
                		categoryPref.setSummary("Not selected");
                	 }
                	return true;
                }

            });
            
            try{
	            categoryPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	                public boolean onPreferenceChange(Preference preference, Object newValue) {
	                	if (!subjectPref.getSummary().toString().equalsIgnoreCase("Not selected") )
	                	{
	                		int index = categoryPref.findIndexOfValue(newValue.toString()) ;
	                	    categoryPref.setSummary(categoryPref.getEntries()[index]);
	                	    Category a = categoryDataAdapter.getItem(index);
	                	    if (categoryPref.isEnabled())
	                	    	 OrderPreferences.getInstance().getArrayList()[1]=Integer.toString(a.getCategoryId());
	                	}
	                	else 
	                	{
	                		Toast.makeText(AssignmentPref.this, "Choose the subject before", Toast.LENGTH_LONG).show();
	                	}
	                	return true;
	                }
	            });
            }
            catch(Exception e)
            {
            	Toast.makeText(AssignmentPref.this, "Choose the subject before", Toast.LENGTH_LONG).show();
            }
            levelPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                	int index = levelPref.findIndexOfValue(newValue.toString()) ;
                	levelPref.setSummary(levelPref.getEntries()[index]);
                	Level a = levelDataAdapter.getItem(index);
                	Log.i("choosed level Pref", a.getLevelTitle());
                		//prefValues[2] = Integer.toString(a.getLevelId());
                	  OrderPreferences.getInstance().getArrayList()[2]= Integer.toString(a.getLevelId());
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
           
            btnSelectFiles.setOnClickListener(new View.OnClickListener() {
             	 
                public void onClick(View view) {
                	savePreferences();
                    Intent i = new Intent(getApplicationContext(),
                            FileManagerActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("FileManager", "NewOrder");
                    i.putExtras(mBundle);
                    startActivityForResult(i, Constants.addFilesResult);
                    
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
	            	
	             for(int i=0;i<checks.size();i++){
				              if(checks.get(i)==1)
				              {
				            	   adapter.remove(adapter.getItem(i));
				                    checks.remove(i);
				                    for (File s: FileManagerActivity.getFinalAttachFiles())
				                    {
				                    		Log.i("remained files", s.toString());
				                    }
				                    Log.i("FileManagerActivity.getFinalAttachFiles()", Integer.toString(FileManagerActivity.getFinalAttachFiles().size()));
				                    textHead.setText(Integer.toString(FileManagerActivity.getFinalAttachFiles().size())+ " files attached");
				                    long wholeSize = 0;
				                    for (File file: FileManagerActivity.getFinalAttachFiles())
				                    {
				                    	wholeSize += file.length();
				                    }
				                    fileSizeHead.setText(Long.toString(wholeSize/1024)+ " KB");
				                     i--;
				              }
	              
	              	
	                  }
	             if (FileManagerActivity.getFinalAttachFiles().size()==0)
	    		   {  
	    			 fileCheckHead.setChecked(false);
	            	 FileManagerActivity.getFinalAttachFiles().clear();
	    			   adapter.clear();
	    			   adapter.notifyDataSetChanged();
	                   mainList.removeFooterView(filesList);
	                   btnSelectFiles.setText("Add files");
	    		   }
	                }
            });
            
            
          orderTitle.setOnTouchListener(new OnTouchListener() 
          {
  			public boolean onTouch(View arg0, MotionEvent arg1) {
  				
  				if (orderTitle.getText().toString().equalsIgnoreCase("Put more than 5 symbols") )
  				{
  				 orderTitle.getText().clear();
  				 orderTitle.setHint("Order title");
  				 orderTitle.setTextColor(Color.BLACK);
  				}
  				return false;
  			}
          });
            

       	 new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
		                int dayOfMonth) {
		        mYear = year;
		        mMonth = monthOfYear;
		        mDay = dayOfMonth;
		       
		        updateDisplay();
		}
         };
            new TimePickerDialog.OnTimeSetListener() {
           	    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
           	      mHour = hourOfDay;
           	      mMinute = minuteOfHour;
           	      updateDisplay();
       
           	    }
           	  };
          
           	 btnSubmitOrder.setOnClickListener(new OnClickListener() {
         		

					public void onClick(View v) {
						 boolean errorFlag=false;
  		         	   if(deadlineCus.getSummary().toString().equalsIgnoreCase("Not selected"))
  		         	   {
  		         		   Toast toast = Toast.makeText(getApplicationContext(),"You have to choose deadline", Toast.LENGTH_SHORT);
  		         		   toast.show();
  		         		   errorFlag = true;
  		         	   }
  		         	   if(orderTitle.getText().length()<5)
  		         	   {
  		         		   Toast toast = Toast.makeText(getApplicationContext(), "Title have to be longer than 5 symbols", Toast.LENGTH_SHORT);
  		         		   toast.show();
  		         		   orderTitle.setText("");
  		         		   orderTitle.setTextColor(Color.RED);
  		         		   orderTitle.setText("Put more than 5 symbols");
  		         		   errorFlag = true;
  		         	   }
	    		           if (productPref.getSummary().toString().equalsIgnoreCase("Not selected"))
  		         	   {
  		         		   Toast toast = Toast.makeText(getApplicationContext(), "You should to choose the product", Toast.LENGTH_SHORT);
  		         		   toast.show();
  		         		   errorFlag = true;
  		         	   }
  		         	   if (subjectPref.getSummary().toString().equalsIgnoreCase("Not selected"))
  		         	   {
  		         		   Toast toast = Toast.makeText(getApplicationContext(), "You should to choose the subject", Toast.LENGTH_SHORT);
  		         		   toast.show();
  		         		   errorFlag = true;
  		         	   }
  		         	  if (productPref.getSummary().toString().equalsIgnoreCase("Assignment")) 
  		         	  {
							  if (categoryPref.getSummary().toString().equalsIgnoreCase("Not selected"))
		   		         	   {
		   		         		   Toast toast = Toast.makeText(getApplicationContext(), "You should to choose the category", Toast.LENGTH_SHORT);
		   		         		   toast.show();
		   		         		   errorFlag = true;
		   		         	   }
  		         	  }
  		         	 if (productPref.getSummary().toString().equalsIgnoreCase("Writing")) 
  		         	 {
  		         		 
							  if (nmbPages.getSummary().toString().equalsIgnoreCase("Not selected"))
		   		         	   {
		   		         		   Toast toast = Toast.makeText(getApplicationContext(), "You should to choose number of pages", Toast.LENGTH_SHORT);
		   		         		   toast.show();
		   		         		   errorFlag = true;
		   		         	   }
							  if (typePref.getSummary().toString().equalsIgnoreCase("Not selected"))
		   		         	   {
		   		         		   Toast toast = Toast.makeText(getApplicationContext(), "You should to choose type of essay", Toast.LENGTH_SHORT);
		   		         		   toast.show();
		   		         		   errorFlag = true;
		   		         	   }
							  if (nmbRefs.getSummary().toString().equalsIgnoreCase("Not selected"))
		   		         	   {
		   		         		   Toast toast = Toast.makeText(getApplicationContext(), "You should to choose number of references", Toast.LENGTH_SHORT);
		   		         		   toast.show();
		   		         		   errorFlag = true;
		   		         	   }
							  if (crStyle.getSummary().toString().equalsIgnoreCase("Not selected"))
		   		         	   {
		   		         		   Toast toast = Toast.makeText(getApplicationContext(), "You should to choose creation style", Toast.LENGTH_SHORT);
		   		         		   toast.show();
		   		         		   errorFlag = true;
		   		         	   }
							  
  		         	 }
  		         	   if (levelPref.getSummary().toString().equalsIgnoreCase("Not selected"))
  		         	   {
  		         		   
  		         		   Toast toast = Toast.makeText(getApplicationContext(), "You should to choose the level", Toast.LENGTH_SHORT);
  		         		   toast.show();
  		         		   errorFlag = true;
  		         	   }
  		         	 
  		         	   if (errorFlag == false)
  		         	   {
  		         		 boolean isOnline = new FrequentlyUsedMethods(AssignmentPref.this).isOnlineOrderSend();
  		         		 if (isOnline)
  		         		 {    
  		         			 OrderPreferences.getInstance().getArrayList()[7]= orderTitle.getText().toString();
	    		    		 OrderPreferences.getInstance().getArrayList()[8]= deadlineCus.getSummary();
	    		    		 OrderPreferences.getInstance().getArrayList()[9]= taskText.getText().toString();
	    		    		 OrderPreferences.getInstance().getArrayList()[10]= timezonePref.getSummary();
	    		    		 OrderPreferences.getInstance().getArrayList()[12] = Integer.toString(explanationReqInt);
	    		    		 OrderPreferences.getInstance().getArrayList()[13] = Integer.toString(exclVideoInt);
	    		    		 OrderPreferences.getInstance().getArrayList()[14] = Integer.toString(commVideoInt);
	    		    		 Intent intent = new Intent(Constants.TAB_CHANGE_ORDER);
	    		    		 Bundle bundle = new Bundle();
	    		    		 bundle.putString("newOrder", "success");
	    		    		 intent.putExtras(bundle);
	    		    		 sendBroadcast(intent);
	    		    		 Log.i("AssignmentPref filesToSend", Integer.toString(FileManagerActivity.getFinalAttachFiles().size()));
	    		    		 preferencesValuesDelete();
  		         			
  		         		 }
  		         		 else
  		         		 {
  		         			AlertDialog.Builder alt_bld = new AlertDialog.Builder(AssignmentPref.this);
  		                    alt_bld.setMessage("Connection is lost. Would you to try again or quit?")
  		                    .setCancelable(false)
  		                    .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
  		                        public void onClick(DialogInterface dialog, int id) {
  		                        	btnSubmitOrder.performClick();
  		                  		  }
  		                    })
  		                    .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
  		                        public void onClick(DialogInterface dialog, int id) {
  		                        //  Action for 'NO' Button
  		                        	
  		                        	
  		                        	preferencesValuesDelete();
  		                        	Intent intent = new Intent(Constants.TAB_CHANGE_ORDER);
  			    		    		 sendBroadcast(intent);
  			    		    		prefEditor.clear().commit();
  		                        	
  		                        	dialog.cancel();
  			    		    		
  		                            
  		                        }
  		                    });
  		                    AlertDialog alert = alt_bld.create();
  		                    // Title for AlertDialog
  		                    alert.setTitle("Connection is lost.");
  		                    // Icon for AlertDialog
  		                    alert.show();
  		         		 }
  		 					
  		 				} 
  		    		
  		         	   }
						
	    		    
	    		    });
           	fileCheckHead.setOnCheckedChangeListener(new OnCheckedChangeListener()
        	{

    			public void onCheckedChanged(CompoundButton buttonView,
    					boolean isChecked) {
    				if (isChecked)
    				{
    					for (Integer i : checks)
    					{
    						checks.set(checks.indexOf(i), 1);
    					}
    					for (int i = 0;i<customfileList.getCount();i++)
    					{
    					
    						View a = customfileList.getChildAt(i);
    						CheckBox b = (CheckBox) a.findViewById(R.id.fileCheck);
    						b.setChecked(true);
    					}
    					btnFilesRemove.setEnabled(true);
    				}
    				else 
    				{
    					for (Integer i : checks)
    					{
    						checks.set(checks.indexOf(i), 0);
    					}
    					for (int i = 0;i<customfileList.getCount();i++)
    					{
    					
    						View a = customfileList.getChildAt(i);
    						CheckBox b = (CheckBox) a.findViewById(R.id.fileCheck);
    						b.setChecked(false);
    					}
    					btnFilesRemove.setEnabled(false);
    					
    				}
    				
    			}
        		
        	});
           	 
    }
	 /**очистка списков настроек заказа**/
	private void preferencesValuesDelete()
	{
		
		orderTitle.getText().clear();
      	taskText.getText().clear();
      	if (productPref.getSummary().toString().equalsIgnoreCase("assignment"))
      		assignmentFieldsDelete();
      	else if (productPref.getSummary().toString().equalsIgnoreCase("writing"))
      	    writingFieldsDelete();
      	productPref.setSummary("Not selected");
      	levelPref.setSummary("Not selected");
      	subjectPref.setSummary("Not selected");
      	categoryPref.setSummary("Not selected");
      	deadlineCus.setSummary("Not selected");


      	prefEditor.putBoolean("orderPrefDismiss", true);
      	mainList.removeFooterView(filesList);
      	
		
	}
	/**добавление полей в случае выбора типа продукта assignment **/
	public void assignmentFieldsAdd()
	{
		addPreferencesFromResource(R.xml.new_assign);
		  dtlInfo = (CustomCheckBoxPref)  getPreferenceScreen().findPreference("dtlInfo");
		ExcVideo = (CustomCheckBoxPref)  getPreferenceScreen().findPreference("ExcVideo");
		commVideo = (CustomCheckBoxPref)  getPreferenceScreen().findPreference("commVideo");
    		dtlInfo.setTitle("Detailed explanation required");
    	//	dtlInfo.setTextSize(9);
    		ExcVideo.setTitle("Shoot exclusive video");
//    		ExcVideo.setTextSize(9);
    		commVideo.setTitle("Shoot common video");
//    		commVideo.setTextSize(9);
		dtlInfo.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                boolean checked = Boolean.valueOf(newValue.toString());
                Log.i("dtlInfo value",Boolean.toString(checked));
                if (checked)
                	explanationReqInt = 1;
                else 
                	explanationReqInt = 0;
               
                Log.i("dtlInfo value",Integer.toString(explanationReqInt));
                return true;
                
            }
        });
    	ExcVideo.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                boolean checked = Boolean.valueOf(newValue.toString());
                if (checked)
                	exclVideoInt = 1;
                else 
                	exclVideoInt = 0;
                
                return true;
            }
        });
    	commVideo.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                boolean checked = Boolean.valueOf(newValue.toString());
                Log.i("checked state",Boolean.toString(checked));
                if (checked)
                	commVideoInt = 1;
                else 
                	commVideoInt = 0;
               
                return true;
            }
        });
    	
		
	}
	/**удаление полей в случае выбора типа продукта assignment **/
	public void assignmentFieldsDelete()
	{
		
		
		CheckBoxPreference a = (CheckBoxPreference) getPreferenceScreen().findPreference("dtlInfo");
		CheckBoxPreference b = (CheckBoxPreference) getPreferenceScreen().findPreference("ExcVideo");
		CheckBoxPreference c = (CheckBoxPreference) getPreferenceScreen().findPreference("commVideo");
		
		
		a.setChecked(false);
		b.setChecked(false);
		c.setChecked(false);
		explanationReqInt = 0;
		exclVideoInt= 0;
		commVideoInt= 0;
		
		getPreferenceScreen().removePreference(getPreferenceScreen().findPreference("dtlInfo"));
		getPreferenceScreen().removePreference(getPreferenceScreen().findPreference("ExcVideo"));
		getPreferenceScreen().removePreference(getPreferenceScreen().findPreference("commVideo"));
		
	}
	/**добавление полей в случае выбора типа продукта writing **/
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
	/**добавление списка файлов**/
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
				
				fileSize.setText(Long.toString(FileManagerActivity.getFinalAttachFiles().get(position).length()/1024)+ " KB");
				for (int i = 0; i< FileManagerActivity.getFinalAttachFiles().size();i++)
		        {
						textView.setTag(i);
		        }
				view.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (!getTrigger())
							setTrigger(true);
						else 
							setTrigger(false);
						checkBox.setChecked(getTrigger());
						Log.i("checkable state", Boolean.toString(checkBox.isChecked()));
						if (checkBox.isChecked())
						{
							checks.set(position, 1);
							btnFilesRemove.setEnabled(true);
						}
						else
						{
							checks.set(position, 0);
							int sum =0;
							for (int k=0;k<checks.size();k++)
							{
								sum += checks.get(k).intValue();
							}
							if (sum==0)
								btnFilesRemove.setEnabled(false);
						}
		            }
		        });
				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked)
							{
								checks.set(position, 1);
								btnFilesRemove.setEnabled(true);
							}
						else
							{
								checks.set(position, 0);
								int sum =0;
								for (int k=0;k<checks.size();k++)
								{
									sum += checks.get(k).intValue();
								}
								if (sum==0)
									btnFilesRemove.setEnabled(false);
							}
					}
			    });
				
				view.setOnLongClickListener(filesClicklistener);
				return view;
			}
		};
		changeFileHeafer();
		customfileList.setAdapter(adapter);
		mainList.addFooterView(filesList);
		mainList.addFooterView(assignfooter);
		if (!FileManagerActivity.getFinalAttachFiles().isEmpty())
			btnSelectFiles.setText("Add more files");
      if (!prefs.getAll().isEmpty())
      {
    	  Log.i("AssPref", "im in pref reload");
    	  	String LevelsValue =  prefs.getString("LevelsValue", null);
			String CategoriesValue =  prefs.getString("CategoriesValue", null);
			String SubjectsValue  =  prefs.getString("SubjectsValue", null);
			String ProductsValue  =  prefs.getString("ProductValue", null);
			prefs.getString("prefValues", null);
			
			
			CharSequence charTitle = prefs.getString("orderTitle", null);
			CharSequence charTask = prefs.getString("taskReq", null);
		 	CharSequence charDeadline = prefs.getString("deadline", null);
		 	
			if (charTask!=null)
				taskText.setText(charTask);
				
			if (charTitle != null)
			{
				orderTitle.setText(charTitle);
			}
			else 
				orderTitle.setHint("Order title(specify)");
			
			if (ProductsValue != null)
				productPref.setSummary(ProductsValue.toString());
				
			if (charDeadline != null)
			{
				deadlineCus.setSummary(charDeadline.toString());
			}
			else
				deadlineCus.setSummary("Not selected");
			
			if (LevelsValue!=null)
				levelPref.setSummary(LevelsValue.toString());
			if (CategoriesValue!=null)
				{
					categoryPref.setSummary(CategoriesValue.toString());
					categoryPref.setEnabled(true);
				}
			if (SubjectsValue != null)
				subjectPref.setSummary(SubjectsValue.toString());
      }
      else 
      {
    	  Log.i("asss", "prefs is empty");
      }
		
    }
		
	/**сохранение выбранных настроек**/
	 public void savePreferences() 
	 {
		 Iterator it =  prefs.getAll().entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        System.out.println(pairs.getKey() + " = " + pairs.getValue());
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		 Log.i("AssignmentPRef","im in savePrefs");
		 	prefEditor.putString("LevelsValue", levelPref.getSummary().toString());
			prefEditor.putString("CategoriesValue", categoryPref.getSummary().toString());
			prefEditor.putString("SubjectsValue", subjectPref.getSummary().toString());
			prefEditor.putString("ProductValue", productPref.getSummary().toString());
			
		 	prefEditor.putString("taskReq",taskText.getText().toString());
			prefEditor.putString("orderTitle",orderTitle.getText().toString());
			prefEditor.putString("deadline",deadlineCus.getSummary().toString());

			prefEditor.putInt("detailedExp", explanationReqInt);
			prefEditor.putInt("exclVideo", exclVideoInt);
			prefEditor.putInt("commonVideo", commVideoInt);
			
			JSONObject user = new JSONObject();
			for (int i = 0; i < OrderPreferences.getInstance().getArrayList().length; i++)
			{
				try {
					if (OrderPreferences.getInstance().getArrayList()[i]!=null)
					user.put(Integer.toString(i), OrderPreferences.getInstance().getArrayList()[i].toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			prefEditor.putString("prefValues", user.toString());
			prefEditor.commit();
		 
	 }
	 /**OnLongClickListener для списка файлов**/
	 OnLongClickListener filesClicklistener = new OnLongClickListener() {
			public boolean onLongClick(View arg0) {
				final CharSequence[] items = {"Open", "Delete", "Details"};
				final AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentPref.this);
			    builder.setTitle((((CustomTextView)arg0.findViewById(android.R.id.title))).getText().toString());
			    Log.i("view class", arg0.getClass().getName());
			    final int pos = getFilePosition(arg0);
			    Log.i("position ", Integer.toString(pos));
			    ((CustomTextView)arg0.findViewById(android.R.id.title)).getTag();
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
				                   mainList.removeFooterView(filesList);
				                   btnSelectFiles.setText("Add files");
				    		   }
				    		   else
				    			   { 
				    				   try
				    				   {
				    					   
					    				     adapter.remove(FileManagerActivity.getFinalAttachFiles().get(pos));
					    				     textHead.setText(Integer.toString(FileManagerActivity.getFinalAttachFiles().size())+ " files attached");
					    	                    long wholeSize = 0;
					    	                    for (File file: FileManagerActivity.getFinalAttachFiles())
					    	                    {
					    	                    	wholeSize += file.length();
					    	                    }
					    	                    fileSizeHead.setText(Long.toString(wholeSize/1024)+ " KB");
							    		     adapter.notifyDataSetChanged();
				    				   }
				    				   catch(IndexOutOfBoundsException e)
				    				   {
				    					   e.printStackTrace();
				    				   }
				    				  
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
							builder2.setMessage("Size of file is: " + Long.toString(fis.getChannel().size()/1024)+ "  KB"+"\r\n"+
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
		/**метод для получения позиции файла в списке по нажимаемому View**/
		 public int getFilePosition(View v)
		 {
			 int pos = 0;
			for (int i = 0; i<adapter.getCount();i++)
			{
				Log.i("fileList item", adapter.getItem(i).getName());
				if (((CustomTextView)v.findViewById(android.R.id.title)).getText().equals((adapter.getItem(i).getName())))
				 pos = i;
			}
			 return pos;
		 }
			/**TimePickerDialog для времени**/
		  private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
	    	    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
	    	      mHour = hourOfDay;
	    	      mMinute = minuteOfHour;
	    	      updateDisplay();

	    	    }
	    	  };
	    	  /**OnDateSetListener для даты**/
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
		                    true);
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
				if (mMonth<10)
				deadlineCus.setSummary(
		                (new StringBuilder()
		                // Month is 0 based so add 1
		                .append(mYear).append("-").append("0")
		                .append(mMonth + 1).append("-")
		                .append(mDay)
		                .append(" ").append(mHour).append(":").append(minutes)).toString()
		                );
				else 
					deadlineCus.setSummary(
			                (new StringBuilder()
			                // Month is 0 based so add 1
			                .append(mYear).append("-")
			                .append(mMonth + 1).append("-")
			                .append(mDay)
			                .append(" ").append(mHour).append(":").append(minutes)).toString()
			                );
				showDialog(TIME_DIALOG_ID);
		}
			 /**метод добавления тем заказа**/
			 public void addSubjects() {
				 DatabaseHandler db = new DatabaseHandler(getApplicationContext());
				 try {
					Dao<Subject,Integer> daoSubject = db.getSubjectDao();
					subjectsListSpinner = daoSubject.queryForAll();
					try{
						Iterator<Subject> it = subjectsListSpinner.iterator();
						while (it.hasNext()) {
						    if (it.next().getSubjectTitle().equalsIgnoreCase("non-categorized")) {
						        it.remove();
						        // If you know it's unique, you could `break;` here
						    }
						}
					}
					catch(Exception e) 
					{
						e.printStackTrace();
					}
					subjectDataAdapter= new ArrayAdapter<Subject>(this,
					android.R.layout.simple_spinner_item, subjectsListSpinner);
					subjectDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					CharSequence[] entries = new CharSequence[subjectDataAdapter.getCount()];
				    CharSequence[] entryValues = new CharSequence[subjectDataAdapter.getCount()];
				    int i = 0;
				    for (Subject dev : subjectsListSpinner)
				    {
				    		entries[i] = dev.getSubjectTitle();
				    		entryValues[i] = Integer.toString(dev.getSubjectId());
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
			
			 /**метод добавления категорий заказа**/
				public void addCategories(int index) {
					 DatabaseHandler db = new DatabaseHandler(getApplicationContext());
					 try {

						 Dao<Category,Integer> daoCategory = db.getCategoryDao();
						 daoCategory.queryBuilder();
						 final List<Category> categories =
								 daoCategory.queryBuilder().where().
								    eq("subject_id", index+1).query();//Integer.parseInt(subjectPref.getEntry().toString())).query();
						try{
							Iterator<Category> it = categories.iterator();
							while (it.hasNext()) {
							    if (it.next().getCategoryTitle().equalsIgnoreCase("non-categorized")) {
							        it.remove();
							        // If you know it's unique, you could `break;` here
							    }
							}
						}
						catch(Exception e) 
						{
							e.printStackTrace();
						}

						categoryDataAdapter = new ArrayAdapter<Category>(this,
								android.R.layout.simple_spinner_item, categories);
						categoryDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						CharSequence[] entries = new CharSequence[categoryDataAdapter.getCount()];
					    CharSequence[] entryValues = new CharSequence[categoryDataAdapter.getCount()];
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
				/**метод добавления уровней заказа**/
				 public void addLevels() {
					 DatabaseHandler db = new DatabaseHandler(getApplicationContext());
					 try {
						Dao<Level,Integer> daoSubject = db.getLevelDao();
						levelsListSpinner = daoSubject.queryForAll();
						try{
						Iterator<Level> it = levelsListSpinner.iterator();
						while (it.hasNext()) {
						    if (it.next().getLevelTitle().equalsIgnoreCase("non-categorized")) {
						        it.remove();
						        // If you know it's unique, you could `break;` here
						    }
						}
					}
					catch(Exception e) 
					{
						e.printStackTrace();
					}
						levelDataAdapter = new ArrayAdapter<Level>(this,
						android.R.layout.simple_spinner_item, levelsListSpinner);
						levelDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						CharSequence[] entries = new CharSequence[levelDataAdapter.getCount()];
					    CharSequence[] entryValues = new CharSequence[levelDataAdapter.getCount()];
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
				 /**метод добавления временных зон**/
				 public void addTimeZones() {
						//
						final String[] TZ = TimeZone.getAvailableIDs();

						Log.i("count", Integer.toString(TZ.length));
						ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
								this,  R.layout.dialog_list_item, R.id.tv);
//						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
				 /**метод добавления списка стилей создания essay**/
					public void addEssayCreationStyles() {
						DatabaseHandler db = new DatabaseHandler(getApplicationContext());
						try {
							Dao<EssayCreationStyle, Integer> daoSubject = db.getEssayCreationStyleDao();
							essayCreationStyleListSpinner = daoSubject.queryForAll();
							essayCrDataAdapter = new ArrayAdapter<EssayCreationStyle>(this,
									android.R.layout.simple_spinner_item, essayCreationStyleListSpinner);
							essayCrDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							
							CharSequence[] entries = new CharSequence[essayCrDataAdapter.getCount()];
						    CharSequence[] entryValues = new CharSequence[essayCrDataAdapter.getCount()];
						    int i = 0;
						    for (EssayCreationStyle dev : essayCreationStyleListSpinner)
						    {
						    	entries[i] = dev.getECSTitle();
					            entryValues[i] = Integer.toString(dev.getECSId());
					            Log.i("subject items", entries[i].toString() +  entryValues[i].toString());
					            i++;
					            
						    }
						    crStyle = (IconPreference)findPreference("crStyle");
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
					/**метод добавления списка количества страниц**/
					public void addNumberPages() {
						try
						{
							
							nmbPages = (IconPreference) findPreference("nmbPages");
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
						catch(Exception e)
						{
							e.printStackTrace();
						}
						
							 nmbPageDataAdapter = new ArrayAdapter<NumberPages>(this,
									android.R.layout.simple_spinner_item, LoginAsync.numberPagesList);
							nmbPageDataAdapter	.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							CharSequence[] entries = new CharSequence[nmbPageDataAdapter.getCount()];
						    CharSequence[] entryValues = new CharSequence[nmbPageDataAdapter.getCount()];
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
					/**метод добавления списка количества ссылочных материалов**/
						public void addNumberReferences() {
							nmbRefs = (IconPreference)findPreference("nmbRefs");
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
							 nmbRefsDataAdapter = new ArrayAdapter<NumberOfReferences>(this,
									android.R.layout.simple_spinner_item, LoginAsync.numberReferencesList);
							nmbRefsDataAdapter	.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							CharSequence[] entries = new CharSequence[nmbRefsDataAdapter.getCount()];
						    CharSequence[] entryValues = new CharSequence[nmbRefsDataAdapter.getCount()];
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
						/**метод добавления списка типов essay**/
						public void addEssayTypes() {
							DatabaseHandler db = new DatabaseHandler(getApplicationContext());
							try {
								 typePref = (IconPreference)findPreference("typePref");
								Dao<EssayType, Integer> daoSubject = db.getEssayTypeDao();
								essayTypeListSpinner = daoSubject.queryForAll();
								essayTypeDataAdapter = new ArrayAdapter<EssayType>(this,
										android.R.layout.simple_spinner_item, essayTypeListSpinner);
								essayTypeDataAdapter	.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								CharSequence[] entries = new CharSequence[essayTypeDataAdapter.getCount()];
							    CharSequence[] entryValues = new CharSequence[essayTypeDataAdapter.getCount()];
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
						/**метод проверки или строка состоит исключетельно из цифр**/
					 boolean numbersCheck(String email)
					    {
					    	Pattern pattern = Pattern.compile("^[0-9]{1,2}.*");
								Matcher matcher = pattern.matcher(email);
								boolean matchFound = matcher.matches();
					    	return matchFound;
					    }
					 /**OnPreferenceChangeListener для стилей создания essay**/
					public OnPreferenceChangeListener crStyleListener = new OnPreferenceChangeListener() {        

						public boolean onPreferenceChange(
								Preference preference, Object newValue) {
							int index = crStyle.findIndexOfValue(newValue.toString()) ;
		                 	crStyle.setSummary(crStyle.getEntries()[index]);
		                 	EssayCreationStyle a = essayCrDataAdapter.getItem(index);
		                 	 OrderPreferences.getInstance().getArrayList()[6] = Integer.toString(a.getECSId());
							return false;
						}
				    };
				    /**OnPreferenceChangeListener для количества страниц**/
				    public OnPreferenceChangeListener nmbPagesListener = new OnPreferenceChangeListener() {        

						public boolean onPreferenceChange(
								Preference preference, Object newValue) {
							int index = nmbPages.findIndexOfValue(newValue.toString()) ;
							nmbPages.setSummary(nmbPages.getEntries()[index]);
							NumberPages  a = nmbPageDataAdapter.getItem(index);
							 OrderPreferences.getInstance().getArrayList()[3] = a.getNumberPage();
							return false;
						}
				    };
				    
				    /**OnPreferenceChangeListener для количества страниц essay**/
				    public OnPreferenceChangeListener nmbRefsListener = new OnPreferenceChangeListener() {        

						public boolean onPreferenceChange(
								Preference preference, Object newValue) {
							int index = nmbRefs.findIndexOfValue(newValue.toString()) ;
							nmbRefs.setSummary(nmbRefs.getEntries()[index]);
							NumberOfReferences a = nmbRefsDataAdapter.getItem(index);
							 OrderPreferences.getInstance().getArrayList()[4] = a.getNumberReference();
							return false;
						}
				    };
				    /**OnPreferenceChangeListener для типов essay**/
				    public OnPreferenceChangeListener typePrefListener = new OnPreferenceChangeListener() {        

						public boolean onPreferenceChange(
								Preference preference, Object newValue) {
							int index = typePref.findIndexOfValue(newValue.toString()) ;
							typePref.setSummary(typePref.getEntries()[index]);
							EssayType a = essayTypeDataAdapter.getItem(index);
							 OrderPreferences.getInstance().getArrayList()[5] = Integer.toString(a.getEssayTypeId());
							return false;
						}
				    };
				    /**метод изменения заголовка списка файлов при удалении/добавлении файлов из списка**/
				    private void changeFileHeafer()
				    {
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
				        fileSizeHead.setText(Long.toString(wholeSize/1024)+ " KB");
				    }
				    public void onActivityResult(int requestCode, int resultCode, Intent data) {
				    	switch(resultCode){
				    	case Constants.addFilesResult:
				    	
				    	if (adapter==null)
				    		addFiles();
				    	else if (adapter.getCount()==0)
				    		addFiles();
				    		else 
				    			{
				    				adapter.notifyDataSetChanged();
				    				changeFileHeafer();
				    			}
						btnFilesRemove.setVisibility(View.VISIBLE);
							for(int b=0;b<FileManagerActivity.getFinalAttachFiles().size();b++)
				    		{ 
				    			checks.add(b,0);
				    	    } 
				    	}
				    }
				    
					@Override
					public void onResume()
					{
					    super.onResume();
					    categoryPref.setEnabled(false);
					  if (	prefs.getBoolean("orderPrefDismiss", false) == true)
						  getListView().setSelection(0);
					  if( FileManagerActivity.getFinalAttachFiles().isEmpty())
	             	  {
						  mainList.removeFooterView(filesList);
					  }
					}
					
					@Override
					public void onStop()
					{
					    super.onStop();
					    if(FileManagerActivity.getFinalAttachFiles().isEmpty())
					    {
					    	if (adapter != null)
					    		Log.i("adapter size", Integer.toString(adapter.getCount()));
					    }
		            
					}
	  
				    
	}
