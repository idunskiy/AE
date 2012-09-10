package com.assignmentexpert;

import java.io.File;
import java.security.Security;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.datamodel.Category;
import com.datamodel.Level;
import com.datamodel.Subject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.library.DatabaseHandler;

public class NewOrderActivity extends Activity{
	
	private Spinner catSpin;
	private Spinner levelSpin;
	private Spinner subjSpin;
	private CheckBox informationView;
	private Button btnOrderHistory;
	private Button btnClose;
	private Button selectFiles;
	private Button btnFilesRemove;
	private Button btnSubmitOrder;
	
	private EditText orderTitle;
	private EditText deadline;
//	private Button timezoneEdit;
	private int mYear;
    private int mMonth;
    int count  = 0;
    private int mDay;
    private int mHour;
    private int mMinute;
	private TextView mDateDisplay;
	private EditText taskReq;
	
	
	private SharedPreferences.Editor prefEditor;
	
	CheckBox commVideo;
	ArrayList<Integer> checks=new ArrayList<Integer>();
	
	private Spinner timezonSpin;
	  ArrayAdapter<File> adapter;
	private ListView fileList;
	boolean errorFlag = false;
	//private RelativeLayout mainLayout;
	 static final int DATE_DIALOG_ID = 1;
	 static final int TIME_DIALOG_ID = 0;
	 View header;
	 View footer;
	 SharedPreferences prefs;
	 
	 List<Level> levelsListSpinner;
	 List<Subject> subjectsListSpinner;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_order);
        View header = getLayoutInflater().inflate(R.layout.header_listview, null);
	    View footer = getLayoutInflater().inflate(R.layout.footer_listview, null);

        btnOrderHistory = (Button) findViewById(R.id.btnOrdersHistory);
        btnClose = (Button) findViewById(R.id.btnClose);
        orderTitle = (EditText) header.findViewById(R.id.orderTitle);
        subjSpin = (Spinner) header.findViewById(R.id.subjectSpin);
   	 	catSpin = (Spinner) header.findViewById(R.id.categorySpin);
   	    levelSpin = (Spinner) header.findViewById(R.id.levelSpin);
   	    
   	    Security.addProvider(new BouncyCastleProvider());
   	    
   	    btnSubmitOrder = (Button) footer.findViewById(R.id.btnSubmitOrder);
   	    
        deadline = (EditText) header.findViewById(R.id.deadlineSpin);
        mDateDisplay = (TextView) findViewById(R.id.login_error);
        selectFiles = (Button) header.findViewById(R.id.btnSelectFiles);
        taskReq = (EditText) header.findViewById(R.id.taskRequirements);
        commVideo = (CheckBox) header.findViewById(R.id.commVideo);
        btnFilesRemove = (Button) footer.findViewById(R.id.btnRemoveFiles);
        btnFilesRemove.setVisibility(View.INVISIBLE);
       
        fileList = (ListView) findViewById(R.id.list);
		fileList.addHeaderView(header);
		fileList.addFooterView(footer);
		fileList.setCacheColorHint(Color.WHITE);
		addSubjects();
	    addLevels();
		addTimeZones();
	    addFiles();
		fileList.setAdapter(adapter);
		
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR);
        mMinute = c.get(Calendar.MINUTE);

		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		prefEditor = prefs.edit();
	
		
		
		 if( !FileManagerActivity.finalAttachFiles.isEmpty())
       	  btnFilesRemove.setVisibility(View.VISIBLE);
         else 
       	  btnFilesRemove.setVisibility(View.INVISIBLE);
		
		for(int b=0;b<FileManagerActivity.finalAttachFiles.size();b++){ checks.add(b,0);} 
        btnClose.setOnClickListener(new View.OnClickListener() {
   	       
            public void onClick(View view) {
            	moveTaskToBack(true);
            } 
    	});


        btnFilesRemove.setOnClickListener(new View.OnClickListener() {
       	 
            public void onClick(View view) {

         for(int i=0;i<checks.size();i++){

          if(checks.get(i)==1){
        	  
        	   adapter.remove(adapter.getItem(i));
                checks.remove(i);
                 i--;
              }
          if( !FileManagerActivity.finalAttachFiles.isEmpty())
           	  btnFilesRemove.setVisibility(View.VISIBLE);
             else 
           	  btnFilesRemove.setVisibility(View.INVISIBLE);
  
           }

            	
            }
        });
        
        btnOrderHistory.setOnClickListener(new View.OnClickListener() {
       	 
            public void onClick(View view) {
            	
                Intent i = new Intent(getApplicationContext(),
                        DashboardActivity.class);
                startActivity(i);
                
            }
        });
        
        
        selectFiles.setOnClickListener(new View.OnClickListener() {
          	 
            public void onClick(View view) {
            	
                Intent i = new Intent(getApplicationContext(),
                        FileManagerActivity.class);
                startActivity(i);
                
            }
        });
        
        orderTitle.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				//inputPassword.setFocusable(true);
				 orderTitle.setText(" ");
				 orderTitle.setHint("Order title");
				 orderTitle.setTextColor(Color.BLACK);
				return false;
			}
        });
        
        taskReq.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				//inputPassword.setFocusable(true);
				taskReq.setText(" ");
				taskReq.setHint("Task requirements");
				taskReq.setTextColor(Color.BLACK);
					
				return false;
			}
        });
       
        deadline.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent arg1) 
			
			{
				  deadline.setTextColor(Color.BLACK);
				  showDialog(DATE_DIALOG_ID);
				
				  return false;
			}
			
        });
    	
     
        
        
        btnSubmitOrder.setOnClickListener(new View.OnClickListener() {
         	 
           public void onClick(View view) {
           	
        	   if(deadline.getText().toString().equalsIgnoreCase("Deadline"))
        	   {
        		   deadline.setText(" ");
        		   deadline.setTextColor(Color.RED);
        		   deadline.setText("You have to choose deadline");
        		   errorFlag = true;
 
        	   }
        	   if  (orderTitle.getText().toString().equalsIgnoreCase("Order title(specify)"))
        	   {
        		   orderTitle.setText(" ");
        		   orderTitle.setTextColor(Color.RED);
        		   orderTitle.setText("You have to choose order name");
        		   errorFlag = true;
        			  
        	   }
        	   else if(orderTitle.getText().length()<8)
        	   {
        		   orderTitle.setText(" ");
        		   orderTitle.setTextColor(Color.RED);
        		   orderTitle.setText("The title have to be longer then 8 symbols");
        		   errorFlag = true;
        	   }
        	   else if(taskReq.getText().toString().equalsIgnoreCase("Task/Specific requirements"))
        	   {
        		   
        		   taskReq.setText(" ");
        		   taskReq.setTextColor(Color.RED);
        		   taskReq.setText("Choose the task specific requirements");
        		   errorFlag = true;
        	   }
        	   if (subjSpin.getSelectedItem().toString().equals("Subjects"))
        	   {
        		   
        		   Toast toast = Toast.makeText(getApplicationContext(), "You should to choose the subject", Toast.LENGTH_SHORT);
        		   toast.show();
        		   errorFlag = true;
        	   }
        	   if (levelSpin.getSelectedItem().toString().equals("Levels"))
        	   {
        		   
        		   Toast toast = Toast.makeText(getApplicationContext(), "You should to choose the level", Toast.LENGTH_SHORT);
        		   toast.show();
        		   errorFlag = true;
        	   }
        	   
        	   if (errorFlag == false)
        	   {
        		   
        	   }
        	   
        	   
        	   
        	   
        	   
               
           }
       });

        if (!prefs.getAll().isEmpty())
        {
			String levelsValue=prefs.getString("LevelsValue", null);
			String subjectsValue=prefs.getString("SubjectsValue", null);

			for(int i=0;i<levelsListSpinner.size();i++)
			{
				
				if(levelsValue.equals(levelsListSpinner.get(i).toString()))
				{
					
					levelSpin.setSelection(i);
				    break;
				}
			}
			
			for(int i=0;i<subjectsListSpinner.size();i++)
			{
				
				if(subjectsValue.equals(subjectsListSpinner.get(i).toString()))
				{
					
					subjSpin.setSelection(i);
				    break;
				}
			}

        }

	}
	
	
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
     private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
    	    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
    	      mHour = hourOfDay;
    	      mMinute = minuteOfHour;
    	      updateDisplay();

    	    }
    	  };
	
	
    	  
    	  public void addFiles()
    	    {
    	
    			adapter = new ArrayAdapter<File>(this,
    					R.layout.new_order, R.id.fileCheck,
    					FileManagerActivity.finalAttachFiles) 
    					{
    				@Override
    				public View getView(final int position, View convertView, ViewGroup parent) {
    					// creates view
    					
    			        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    			        View view = inflater.inflate(R.layout.file, null);
    		            view.setFocusable(false);
    					CheckBox textView = (CheckBox) view
    							.findViewById(R.id.fileCheck);
    					textView.setClickable(true);
    					textView.setText(FileManagerActivity.finalAttachFiles.get(position).getName().toString());
    					textView.setTextColor(Color.BLACK);
    					textView.setPadding(55, 0, 0, 0);
    					textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.file_icon, 0, 0, 0);
    					textView.setTextSize(16);
    					textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
    					int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
    					textView.setCompoundDrawablePadding(dp5);
    					textView.setOnClickListener(new OnClickListener() {

    			            public void onClick(View v) {
    			               

    			                if(((CheckBox)v).isChecked()){
    			                  checks.set(position, 1);
    			                }
    			                else{
    			                 checks.set(position, 0);
    			                }

    			            }
    			        });
    					return view;
    					
    				}
    			};
    	    	
    	    }
    	    
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
		deadline.setText(
                new StringBuilder()
                // Month is 0 based so add 1
                .append(mYear).append("-")
                .append(mMonth + 1).append("-")
                .append(mDay)
                .append(" ").append(mHour).append("-").append(mMinute)
                .append(" "));
		showDialog(TIME_DIALOG_ID);
}
	
	 public void addSubjects() {
		 DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		 try {
			Dao<Subject,Integer> daoSubject = db.getSubjectDao();
			subjectsListSpinner = daoSubject.queryForAll();
			final Subject head = new Subject();
		    head.setSubjectTitle("Subjects");
		    subjectsListSpinner.add(0,head);
			ArrayAdapter<Subject> dataAdapter = new ArrayAdapter<Subject>(this,
			android.R.layout.simple_spinner_item, subjectsListSpinner);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			subjSpin.setAdapter(dataAdapter);
			subjSpin.setOnItemSelectedListener( new OnItemSelectedListener() {		
			        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			        {
			        	prefEditor.putString("SubjectsValue", subjSpin.getSelectedItem().toString());
			        	prefEditor.commit();
			        	if (subjectsListSpinner.contains(head));
			        	subjectsListSpinner.remove(head);
			        	
			        	Subject item = (Subject) parent.getItemAtPosition(position);
			        	addCategories(item.getSubjectId());
			        	
			        }

					public void onNothingSelected(AdapterView<?> arg0) {
						if (subjectsListSpinner.contains(head))
							subjectsListSpinner.remove(head);
						
					}
			   });
			}
		  catch (SQLException e) {
		
			e.printStackTrace();
		}
		 
	 }
		 
		
		public void addCategories(int id) {
			 DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			 try {

				Dao<Category,Integer> daoCategory = db.getCategoryDao();
			//	 final List<Category> catTitles = (List<Category>) daoCategory.q.queryForId(Integer.valueOf(id));
					
				 QueryBuilder<Category, Integer> qb = daoCategory.queryBuilder();
				 GenericRawResults<String[]> rawResults =
						 daoCategory.queryRaw(
						    "select title from categories where subject_id="+Integer.toString(id));
				List<String> categories = new ArrayList<String>();
				for (String[] resultArray : rawResults) {
					categories.add(resultArray[0]);
					}
				
//			    final String head = "Categories";
//			    categories.add(0,head);
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, categories);
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				catSpin.setAdapter(dataAdapter);
				catSpin.setOnItemSelectedListener( new OnItemSelectedListener() {
					
			        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			        	
			        	prefEditor.putString("CategoriesValue", catSpin.getSelectedItem().toString());
			        	prefEditor.commit();
			        	
			        }

			        public void onNothingSelected(AdapterView<?> parent) {
			            // do nothing
			        }

			    });
				
			 }
				 
			  catch (SQLException e) {
				// TODO Auto-generated catch block
	
				e.printStackTrace();
			}
	
		 }
		
		 public void addLevels() {
			 DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			 try {
				
			       
				Dao<Level,Integer> daoSubject = db.getLevelDao();
				levelsListSpinner = daoSubject.queryForAll();
				final Level head = new Level();
			    head.setLevelTitle("Levels");
			    levelsListSpinner.add(0,head);
				ArrayAdapter<Level> dataAdapter = new ArrayAdapter<Level>(this,
				android.R.layout.simple_spinner_item, levelsListSpinner);
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				levelSpin.setAdapter(dataAdapter);
				levelSpin.setOnItemSelectedListener( new OnItemSelectedListener() {
					
			        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			        	
			        	prefEditor.putString("LevelsValue", levelSpin.getSelectedItem().toString());
			        	prefEditor.commit();
			        	
			        }

			        public void onNothingSelected(AdapterView<?> parent) {
			            // do nothing
			        }

			    });
				
				}
			  catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		 }
		 
		 public void addTimeZones() {
			timezonSpin = (Spinner) findViewById(R.id.timezoneSpin);
			HashMap<Integer,String> zones = new HashMap<Integer, String>();
			zones.put(-11, "UTC-11 Pago Pago, United States");
			zones.put(-10, "UTC-10 Honolulu, United States");
			zones.put(-9, "UTC-9 Anchorage, United States");
			zones.put(-8, "UTC-8 Los Angeles, United States");
			zones.put(-7, "UTC-7 Denver, United States");
			zones.put(-6, "UTC-6 Chicago, United States");
			zones.put(-5, "UTC-5 New York, United States");
			zones.put(-4, "UTC-4 La Paz, Bolivia");
			zones.put(-3, "UTC-3 Rio de Janeiro, Brazil");
			zones.put(-2, "UTC-2 Fernando de Noronha");
			zones.put(-1, "UTC-1 Azores, Portugal");
			zones.put(0, "UTC 0 London, UK");
			zones.put(1, "UTC 1 Amsterdam, Netherlands");
			zones.put(2, "UTC 2 Athens, Greece");
			zones.put(3, "UTC 3 Kuwait City, Kuwait");
			zones.put(4, "UTC 4 Dubai, United Arab Emirates");
			zones.put(5, "UTC 5 Islamabad, Pakistan");
			zones.put(6, "UTC 6 Thimphu, Bhutan");
			zones.put(7, "UTC 7 Jakarta, Indonesia");
			zones.put(8, "UTC 8 Hong Kong, China");
			zones.put(9, "UTC 9 Tokyo, Japan");
			zones.put(10, "UTC 10 Sydney, Australia");
			zones.put(11, "UTC 11 Port Vila, Vanuatu");
			zones.put(12,"UTC 12 Wellington, New Zealand");
			final ArrayList <String> timezonelist = new ArrayList<String>();
			
			TimeZone timezone = TimeZone.getDefault();
			int TimeZoneOffset = timezone.getRawOffset()/(60 * 60 * 1000);
			
			for (int key : zones.keySet()) {
				if (TimeZoneOffset == key)
					timezonelist.add(0,zones.get(key));
			}
				timezonelist.add("UTC-11 Pago Pago, United States");
				timezonelist.add("UTC-10 Honolulu, United States");
				timezonelist.add("UTC-9 Anchorage, United States");
				timezonelist.add("UTC-8 Los Angeles, United States");
				timezonelist.add("UTC-7 Denver, United States");
				timezonelist.add("UTC-6 Chicago, United States");
				timezonelist.add("UTC-5 New York, United States");
				timezonelist.add("UTC-4 La Paz, Bolivia");
				timezonelist.add("UTC-3 Rio de Janeiro, Brazil");
				timezonelist.add("UTC-2 Fernando de Noronha");
				timezonelist.add("UTC-1 Azores, Portugal");
				timezonelist.add("UTC 0 London, UK");
				timezonelist.add("UTC 1 Amsterdam, Netherlands");
				timezonelist.add("UTC 2 Athens, Greece");
				timezonelist.add("UTC 3 Kuwait City, Kuwait");
				timezonelist.add("UTC 4 Dubai, United Arab Emirates");
				timezonelist.add("UTC 5 Islamabad, Pakistan");
				timezonelist.add("UTC 6 Thimphu, Bhutan");
				timezonelist.add("UTC 7 Jakarta, Indonesia");
				timezonelist.add("UTC 8 Hong Kong, China");
				timezonelist.add("UTC 9 Tokyo, Japan");
				timezonelist.add("UTC 10 Sydney, Australia");
				timezonelist.add("UTC 11 Port Vila, Vanuatu");
				timezonelist.add("UTC 12 Wellington, New Zealand");	
				
			

//				
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, timezonelist);
						dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				
				timezonSpin.setAdapter(dataAdapter);
				timezonSpin.setOnItemSelectedListener( new OnItemSelectedListener() {
					boolean count = false ;
			        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			        	
			        	if (count)
			        	{
			        	AlertDialog.Builder builder = new AlertDialog.Builder(NewOrderActivity.this);
			        	builder.setMessage("Are you sure to choose timezone  "+ timezonelist.get(position)+"  ?")
			        	   .setCancelable(false)
			        	   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			        	       public void onClick(DialogInterface dialog, int id) {
			        	           
			        	       }
			        	   })
			        	   .setNegativeButton("No", new DialogInterface.OnClickListener() {
			        	       public void onClick(DialogInterface dialog, int id) {
			        	    	   timezonSpin.setSelection(0);
			        	    	   count = false;
			        	            dialog.cancel();
			        	       }
			        	   });
			        	AlertDialog alert = builder.create();
			        	alert.show();
			        	}
			        	count = true;
			        	
			        	
			        }

			        public void onNothingSelected(AdapterView<?> parent) {
			            // do nothing
			        }

			    });
		 }
	 
}
