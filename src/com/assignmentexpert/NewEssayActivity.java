package com.assignmentexpert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Security;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.asynctasks.LoginAsync;
import com.datamodel.EssayCreationStyle;
import com.datamodel.EssayType;
import com.datamodel.Level;
import com.datamodel.NumberOfReferences;
import com.datamodel.NumberPages;
import com.datamodel.Subject;
import com.j256.ormlite.dao.Dao;
import com.library.DatabaseHandler;
import com.library.FrequentlyUsedMethods;
import com.library.UserFunctions;

public class NewEssayActivity extends Activity {

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
	// private Button timezoneEdit;
	private int mYear;
	private int mMonth;
	int count = 0;
	private int mDay;
	private int mHour;
	private int mMinute;
	private TextView mDateDisplay;
	private EditText taskReq;
	 Location location;
	private SharedPreferences.Editor prefEditor;

	ArrayList<Integer> checks = new ArrayList<Integer>();

	private Spinner timezonSpin;
	ArrayAdapter<File> adapter;
	private ListView fileList;

	// private RelativeLayout mainLayout;
	static final int DATE_DIALOG_ID = 1;
	static final int TIME_DIALOG_ID = 0;
	View header;
	View footer;
	SharedPreferences prefs;

	List<Level> levelsListSpinner;
	List<Subject> subjectsListSpinner;
	List<EssayType> essayTypeListSpinner;
	List<EssayCreationStyle> essayCreationStyleListSpinner;
	
	private SharedPreferences sharedPreferences;
	private Editor editor;
	private CheckBox explanationReq;
	private CheckBox exclVideo;
	private CheckBox commVideo;

	int explanationReqInt = 0;
	int exclVideoInt = 0;
	int commVideoInt = 0;
	private ProgressDialog progDailog;
	private Button btnProfile;
	private Spinner productSpin;
	private Spinner essayProductSpin;
	ViewFlipper vf;
	private Spinner essayTypeSpin;
	private Spinner citationStyleSpin;
	private Spinner numberPagesEssay;
	private Spinner numberReferences;
	UserFunctions launch = new UserFunctions();
	private ScrollView essayScroll;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		final View footer = getLayoutInflater().inflate(
//				R.layout.footer_listview, null, true);
//		final View essayheader = getLayoutInflater().inflate(
//				R.layout.essay_header, null, false);
//		setContentView(R.layout.new_order);
//		
//		//fileList = (ListView) findViewById(R.);
//		final FrequentlyUsedMethods faq = new FrequentlyUsedMethods(NewEssayActivity.this);
//		btnOrderHistory = (Button) findViewById(R.id.btnOrdersHistory);
//		btnClose = (Button) findViewById(R.id.btnClose);
//		btnProfile = (Button) findViewById(R.id.btnProfile);
//		orderTitle = (EditText) essayheader.findViewById(R.id.orderTitle);
//		subjSpin = (Spinner) essayheader.findViewById(R.id.subjectSpin);
//		catSpin = (Spinner) essayheader.findViewById(R.id.categorySpin);
//		levelSpin = (Spinner) essayheader.findViewById(R.id.levelSpin);
//		essayTypeSpin = (Spinner) essayheader.findViewById(R.id.essayTypeSpin);
//		essayScroll = (ScrollView) essayheader.findViewById(R.id.essayScroll);
//		citationStyleSpin =(Spinner) essayheader.findViewById(R.id.citationStyleSpin); 
//		numberPagesEssay = (Spinner) essayheader.findViewById(R.id.numberPagesEssay);
//		numberReferences = (Spinner) essayheader.findViewById(R.id.numberReferences);
//		Log.i("default timezone",TimeZone.getDefault().getDisplayName());
//		Security.addProvider(new BouncyCastleProvider());
//		deadline = (EditText) essayheader.findViewById(R.id.deadlineSpin);
//		mDateDisplay = (TextView) findViewById(R.id.login_error);
//		selectFiles = (Button) essayheader.findViewById(R.id.btnSelectFiles);
//		taskReq = (EditText) essayheader.findViewById(R.id.taskRequirements);
//
//
//		btnSubmitOrder = (Button) footer.findViewById(R.id.btnSubmitOrder);
//		btnFilesRemove = (Button) footer.findViewById(R.id.btnRemoveFiles);
//
//		btnFilesRemove.setVisibility(View.INVISIBLE);
//
//		//fileList.setCacheColorHint(Color.WHITE);
//
//
//		final Calendar c = Calendar.getInstance();
//		mYear = c.get(Calendar.YEAR);
//		mMonth = c.get(Calendar.MONTH);
//		mDay = c.get(Calendar.DAY_OF_MONTH);
//		mHour = c.get(Calendar.HOUR);
//		mMinute = c.get(Calendar.MINUTE);
//
//		prefs = getSharedPreferences("newOrder",MODE_PRIVATE);
//		prefEditor = prefs.edit();
//		sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
//		editor = sharedPreferences.edit();
//
//		productSpin = (Spinner) essayheader.findViewById(R.id.productSpin);
//		ArrayList<String> products = new ArrayList<String>();
//		products.add("Essay");
//		products.add("Order");
//
//		final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_spinner_item, products);
//		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		productSpin.setAdapter(dataAdapter);
//		
////		fileList.addHeaderView(essayheader, null, false);
////		fileList.addFooterView(footer, null, true);
//		productSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			private ViewStub stub;
//
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//
//				if (productSpin.getSelectedItem().toString().equalsIgnoreCase("Order")) {
//					 {
//						FileManagerActivity.getFinalAttachFiles().clear();
//						prefEditor.clear().commit();
//						prefs.edit().clear().commit();
//						Intent i = new Intent(getApplicationContext(),
//			                       NewOrderActivity.class);
//			               startActivity(i);
//					 }
//
//			      }
//				
//				}
//			
//
//			public void onNothingSelected(AdapterView<?> parent) {
//				// do nothing
//			}
//
//		});
//		timezonSpin = (Spinner) findViewById(R.id.timezoneSpin);
//		addSubjects();
//		addLevels();
//		faq.addTimeZones(timezonSpin);
//		addFiles();
//		addEssayTypes();
//		addEssayCreationStyles();
//		addNumberPages();
//		addNumberReferences();
//		//fileList.setAdapter(adapter);
//		 
//		//
//		if (!FileManagerActivity.getFinalAttachFiles().isEmpty())
//			btnFilesRemove.setVisibility(View.VISIBLE);
//		else
//			btnFilesRemove.setVisibility(View.INVISIBLE);
//
//		for (int b = 0; b < FileManagerActivity.getFinalAttachFiles().size(); b++) {
//			checks.add(b, 0);
//		}
//
//		btnClose.setOnClickListener(new View.OnClickListener() {
//
//			public void onClick(View view) {
//				editor.remove("username");
//				editor.remove("password");
//				editor.remove("isChecked");
//				editor.commit();
//				try {
//					launch.logOut();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				Intent i = new Intent(getApplicationContext(),
//						LoginActivity.class);
//				startActivity(i);
//			}
//		});
//		
//		
//		selectFiles.setOnClickListener(new View.OnClickListener() {
//
//			public void onClick(View view) {
//				 savePreferences();
//				Intent i = new Intent(getApplicationContext(),
//						FileManagerActivity.class);
//				Bundle mBundle = new Bundle();
//				mBundle.putString("FileManager", "NewEssay");
//				editor.putString("FileManager", "NewEssay");
//				editor.commit();
//				i.putExtras(mBundle);
//				startActivity(i);
//
//			}
//		});
//
//		btnFilesRemove.setOnClickListener(new View.OnClickListener() {
//
//			public void onClick(View view) {
//
//				for (int i = 0; i < checks.size(); i++) {
//
//					if (checks.get(i) == 1) {
//						adapter.remove(adapter.getItem(i));
//						checks.remove(i);
//						i--;
//					}
//					if (!FileManagerActivity.getFinalAttachFiles().isEmpty())
//						btnFilesRemove.setVisibility(View.VISIBLE);
//					else
//						btnFilesRemove.setVisibility(View.INVISIBLE);
//
//				}
//
//			}
//		});
//
//		btnOrderHistory.setOnClickListener(new View.OnClickListener() {
//
//			public void onClick(View view) {
//				 savePreferences();
//				Intent i = new Intent(getApplicationContext(),
//						DashboardActivityAlt.class);
//				startActivity(i);
//
//			}
//		});
//		
//		btnProfile.setOnClickListener(new View.OnClickListener() {
//
//			public void onClick(View view) {
//				 savePreferences();
//				Intent i = new Intent(getApplicationContext(),
//						ProfileActivity.class);
//				startActivity(i);
//
//			}
//		});
//
//		
//
//		orderTitle.setOnTouchListener(new OnTouchListener() {
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//
//				if (orderTitle.getText().toString()
//						.equalsIgnoreCase("The title have to be longer than 5 symbols")
//						| orderTitle.getText().toString()
//								.equals("You have to choose order title")) {
//					orderTitle.setText("");
//				}
//				orderTitle.setHint("Order title(specify)");
//				orderTitle.setTextColor(Color.BLACK);
//				return false;
//			}
//		});
//
//
//		deadline.setOnTouchListener(new OnTouchListener() {
//
//			public boolean onTouch(View arg0, MotionEvent arg1)
//
//			{
//				deadline.setTextColor(Color.BLACK);
//				showDialog(DATE_DIALOG_ID);
//				btnSubmitOrder.setFocusable(true);
//				return false;
//			}
//
//		});
//
//		deadline.setOnEditorActionListener(new OnEditorActionListener() {
//
//			public boolean onEditorAction(TextView v, int actionId,
//					KeyEvent event) {
//				if (actionId == EditorInfo.IME_ACTION_DONE) {
//					deadline.setFocusable(false);
//
//				}
//				return false;
//			}
//		});
//
//		
//		btnSubmitOrder.setOnTouchListener(new OnTouchListener() {
//
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					essayheader.clearFocus();
//					boolean errorFlag = false;
//					if (deadline.getText().length()<1
//							| deadline.getText().toString().equalsIgnoreCase("You have to choose deadline")) {
//						Toast toast = Toast.makeText(getApplicationContext(),
//								"You have to choose deadline",
//								Toast.LENGTH_SHORT);
//						toast.show();
//						deadline.setText("");
//						deadline.setTextColor(Color.RED);
//						deadline.setText("You have to choose deadline");
//						errorFlag = true;
//
//					}
//					if (orderTitle.getText().length() < 5) {
//						Toast toast = Toast.makeText(getApplicationContext(),
//								"The title have to be longer than 5 symbols",
//								Toast.LENGTH_SHORT);
//						toast.show();
//						orderTitle.setText("");
//						orderTitle.setTextColor(Color.RED);
//						orderTitle
//								.setText("The title have to be longer than 5 symbols");
//						errorFlag = true;
//					}
//					if(orderTitle.getText().toString().equalsIgnoreCase("You have to choose order title") ||
//							orderTitle.getText().toString().equalsIgnoreCase("The title have to be longer than 5 symbols")) {
//						Toast toast = Toast.makeText(getApplicationContext(),
//								"You have to choose order title",
//								Toast.LENGTH_SHORT);
//						toast.show();
//						orderTitle.setText("");
//						orderTitle.setTextColor(Color.RED);
//						orderTitle
//								.setText("You have to choose order title");
//						errorFlag = true;
//					}
//					if (subjSpin.getSelectedItem().toString()
//							.equals("Subjects"))
//					{
//
//						Toast toast = Toast.makeText(getApplicationContext(),
//								"You should to choose the subject",
//								Toast.LENGTH_SHORT);
//						toast.show();
//						errorFlag = true;
//					}
//					if (levelSpin.getSelectedItem().toString().equals("Levels")) {
//
//						Toast toast = Toast.makeText(getApplicationContext(),
//								"You should to choose the level",
//								Toast.LENGTH_SHORT);
//						toast.show();
//						errorFlag = true;
//					}
//					if (numberPagesEssay.getSelectedItem().toString().equalsIgnoreCase("Number pages"))
//					{
//						Toast toast = Toast.makeText(getApplicationContext(),
//								"You should to choose pages number",
//								Toast.LENGTH_SHORT);
//						toast.show();
//						errorFlag = true;
//					}
//					if (essayTypeSpin.getSelectedItem().toString().equalsIgnoreCase("Essay Types"))
//					{
//						Toast toast = Toast.makeText(getApplicationContext(),
//								"You should to choose essay type",
//								Toast.LENGTH_SHORT);
//						toast.show();
//						errorFlag = true;
//					}
//					if (citationStyleSpin.getSelectedItem().toString().equalsIgnoreCase("Essay creation styles"))
//					{
//						Toast toast = Toast.makeText(getApplicationContext(),
//								"You should to choose essay creation style",
//								Toast.LENGTH_SHORT);
//						toast.show();
//						errorFlag = true;
//					}
//					if (numberReferences.getSelectedItem().toString().equalsIgnoreCase("Number of references"))
//					{
//						Toast toast = Toast.makeText(getApplicationContext(),
//								"You should to choose number of references",
//								Toast.LENGTH_SHORT);
//						toast.show();
//						errorFlag = true;
//					}
//					if (errorFlag == false) {
//						prefEditor.clear().commit();
//						prefs.edit().clear().commit();
//						new SendOrderTask().execute();
//						Log.d("test size" , prefs.getAll().toString());
//						
//  		         	   
//
//					}
//
//				}
//
//				return false;
//			}
//
//		});
//
//		// start to comment
//
//		 if (!prefs.getAll().isEmpty())
//		 {
//		 int levelsValue=prefs.getInt("LevelsValue", 0);
//		 int EssayCreationStyle = prefs.getInt("EssayCreationStyle", 0);
//		 int NumberReferences = prefs.getInt("NumberReferences", 0);
//		 int EssayTypeValue = prefs.getInt("EssayTypeValue", 0);
//		 int SubjectsValue =  prefs.getInt("SubjectsValue", 0);
//		 int NumberPages =   prefs.getInt("NumberPages", 0);
//		 int sViewX = prefs.getInt("sViewX",0);   
//	     int sViewY = prefs.getInt("sViewY",0); 
//		 CharSequence charTitle = prefs.getString("orderTitle", null);
//		 CharSequence charTask = prefs.getString("taskReq", null);
//		 CharSequence charDeadline = prefs.getString("deadline", null);
//		 if (charTitle != null)
//		 {
//		 orderTitle.setText(charTitle);
//		 }
//		 else
//		 orderTitle.setHint("Order title(specify)");
//		 if (charTask != null)
//		 {
//		 taskReq.setText(charTask);
//		
//		 }
//		 else
//		 taskReq.setHint("Task/Specific requirements");
//		 if (charDeadline != null)
//		 {
//		 deadline.setText(charDeadline);
//		 }
//		 else
//		 deadline.setHint("Deadline");
//		 if (prefs.getInt("sViewX", 0)!=0 & prefs.getInt("sViewY", 0)!=0)
//		 {
//			 essayScroll.scrollTo(prefs.getInt("sViewX", 0), prefs.getInt("sViewX", 0));
//		 }
//		 if (levelsValue != 0)
//			 levelSpin.setSelection(levelsValue);
//		 if (EssayCreationStyle!=0)
//			 citationStyleSpin.setSelection(EssayCreationStyle);
//		 if (NumberReferences!=0)
//			 numberReferences.setSelection(NumberReferences);
//		 if (EssayTypeValue!=0)
//			 essayTypeSpin.setSelection(EssayCreationStyle);
//		 if (SubjectsValue!=0)
//			 subjSpin.setSelection(SubjectsValue);
//		 if (NumberPages!=0)
//			 numberPagesEssay.setSelection(NumberPages);
//		 if (sViewX!=0&sViewY!=0)
//			 essayScroll.scrollTo(sViewX, sViewY);
//		 }
	}

//	DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
//		public void onDateSet(DatePicker view, int year, int monthOfYear,
//				int dayOfMonth) {
//			mYear = year;
//			mMonth = monthOfYear;
//			mDay = dayOfMonth;
//
//			updateDisplay();
//		}
//
//	};
//	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
//		public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
//			mHour = hourOfDay;
//			mMinute = minuteOfHour;
//			updateDisplay();
//
//		}
//	};
//	public void updateFileList(ArrayList<File> fileArrayList)
//	{
//		Log.i("linear layout count", Integer.toString(fileList.getChildCount()));
//		for (int i = 0;i<fileArrayList.size();i++)
//		{
//			View child = fileList.getChildAt(i);
//			if (child instanceof TextView)
//			{
//				child.setTag(i);
//				child.setId(i);
//				Log.i("child class", child.getClass().getName());
//			}
//		}
//	}
//	public void addFiles() {
//		adapter = new ArrayAdapter<File>(this, R.layout.new_order,
//				R.id.fileCheck, FileManagerActivity.getFinalAttachFiles()) {
//			@Override
//			public View getView(final int position, View convertView,
//					ViewGroup parent) {
//				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				View view = inflater.inflate(R.layout.file, null);
//				view.setFocusable(false);
//				CheckBox textView = (CheckBox) view
//						.findViewById(R.id.fileCheck);
//				textView.setClickable(true);
//				textView.setText(FileManagerActivity.getFinalAttachFiles()
//						.get(position).getName().toString());
//				textView.setTextColor(Color.BLACK);
//				textView.setPadding(55, 0, 0, 0);
//				textView.setCompoundDrawablesWithIntrinsicBounds(
//						R.drawable.file_icon, 0, 0, 0);
//				textView.setTextSize(16);
//				textView.setGravity(Gravity.CENTER_VERTICAL
//						| Gravity.CENTER_HORIZONTAL);
//				int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
//				textView.setCompoundDrawablePadding(dp5);
//				textView.setOnClickListener(new OnClickListener() {
//					public void onClick(View v) {
//
//						if (((CheckBox) v).isChecked()) {
//							checks.set(position, 1);
//						} else {
//							checks.set(position, 0);
//						}
//
//					}
//				});
//				return view;
//
//			}
//		};
//
//	}
//
//	@Override
//	protected Dialog onCreateDialog(int id) {
//		switch (id) {
//		case DATE_DIALOG_ID:
//			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
//					mDay);
//
//		case TIME_DIALOG_ID:
//			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
//					false);
//
//		}
//
//		return null;
//	}
//
//	protected void onPrepareDialog(int id, Dialog dialog) {
//		switch (id) {
//		case DATE_DIALOG_ID:
//			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
//
//			break;
//		}
//	}
//
//	private void updateDisplay() {
//		String minutes = null;
//		if (mMinute > 9)
//			minutes = Integer.toString(mMinute);
//		else
//			minutes = "0" + Integer.toString(mMinute);
//		deadline.setText(new StringBuilder()
//				// Month is 0 based so add 1
//				.append(mYear).append("-").append(mMonth + 1).append("-")
//				.append(mDay).append(" ").append(mHour).append(":")
//				.append(minutes));
//		showDialog(TIME_DIALOG_ID);
//	}
//
//	public void addSubjects() {
//		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//
//		try {
//			Dao<Subject, Integer> daoSubject = db.getSubjectDao();
//			subjectsListSpinner = daoSubject.queryForAll();
//			final Subject head = new Subject();
//			head.setSubjectTitle("Subjects");
//			// if (prefs.getString("newOrder", null).equals("newOne"))
//			subjectsListSpinner.add(0, head);
//			final ArrayAdapter<Subject> dataAdapter = new ArrayAdapter<Subject>(
//					this, android.R.layout.simple_spinner_item,
//					subjectsListSpinner);
//			dataAdapter
//					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//			subjSpin.setAdapter(dataAdapter);
//			subjSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
//				public void onItemSelected(AdapterView<?> parent, View view,
//						int position, long id) {
//
//				}
//
//				public void onNothingSelected(AdapterView<?> arg0) {
//
//				}
//			});
//		} catch (SQLException e) {
//
//			e.printStackTrace();
//		}
//
//	}
//
//
//	boolean timezoneValidate(String email) {
//
//		Pattern pattern = Pattern.compile(".+\\/+[A-z]+");
//		Matcher matcher = pattern.matcher(email);
//		boolean matchFound = matcher.matches();
//		return matchFound;
//	}
//
//	public void addLevels() {
//		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//		try {
//			Dao<Level, Integer> daoSubject = db.getLevelDao();
//			levelsListSpinner = daoSubject.queryForAll();
//			final Level head = new Level();
//			head.setLevelTitle("Levels");
//			levelsListSpinner.add(0, head);
//			ArrayAdapter<Level> dataAdapter = new ArrayAdapter<Level>(this,
//					android.R.layout.simple_spinner_item, levelsListSpinner);
//			dataAdapter
//					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//			levelSpin.setAdapter(dataAdapter);
//			levelSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
//				public void onItemSelected(AdapterView<?> parent, View view,
//						int position, long id) {
//
//				}
//
//				public void onNothingSelected(AdapterView<?> parent) {
//					// do nothing
//				}
//
//			});
//
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//	public void addEssayTypes() {
//		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//		try {
//			Dao<EssayType, Integer> daoSubject = db.getEssayTypeDao();
//			essayTypeListSpinner = daoSubject.queryForAll();
//			final EssayType head = new EssayType();
//			head.setEssayTypeTitle("Essay Types");
//			essayTypeListSpinner.add(0, head);
//			ArrayAdapter<EssayType> dataAdapter = new ArrayAdapter<EssayType>(this,
//					android.R.layout.simple_spinner_item, essayTypeListSpinner);
//			dataAdapter	.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//			essayTypeSpin.setAdapter(dataAdapter);
//			essayTypeSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//				public void onItemSelected(AdapterView<?> parent, View view,
//						int position, long id) {
//
//				}
//
//				public void onNothingSelected(AdapterView<?> parent) {
//					// do nothing
//				}
//
//			});
//
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//	public void addEssayCreationStyles() {
//		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//		try {
//			Dao<EssayCreationStyle, Integer> daoSubject = db.getEssayCreationStyleDao();
//			essayCreationStyleListSpinner = daoSubject.queryForAll();
//			final EssayCreationStyle head = new EssayCreationStyle();
//			head.setStr("Essay Creation Styles");
//			essayCreationStyleListSpinner.add(0, head);
//			ArrayAdapter<EssayCreationStyle> dataAdapter = new ArrayAdapter<EssayCreationStyle>(this,
//					android.R.layout.simple_spinner_item, essayCreationStyleListSpinner);
//			dataAdapter	.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//			citationStyleSpin.setAdapter(dataAdapter);
//			citationStyleSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
//				public void onItemSelected(AdapterView<?> parent, View view,
//						int position, long id) {
//					
//
//				}
//
//				public void onNothingSelected(AdapterView<?> parent) {
//					// do nothing
//				}
//
//			});
//
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//	public void addNumberPages() {
//		try
//		{
//			Collections.sort(LoginAsync.numberPagesList, new Comparator<NumberPages>() {
//			    private int fixString(NumberPages in) {
//			    	int res = 0;
//			    		if (in.getNumberPage().length()>2)
//			             {
//			    			if (numbersCheck(in.getNumberPage()))
//			    			res= Integer.parseInt((in.getNumberPage()).substring(0, in.getNumberPage().indexOf('_')));
//			             }
//			    		else 
//			    		 {
//			    			if (numbersCheck(in.getNumberPage()))
//			    				res= Integer.parseInt((in.getNumberPage()));
//			    		 }
//					return res;
//			    }
//				public int compare(NumberPages lhs, NumberPages rhs) 
//				{
//					
//					int res = fixString(lhs) - fixString(rhs);
//	        		if (res == 0)
//	        		{
//	        			if (lhs.getNumberPage().length() < rhs.getNumberPage().length())
//	        				return -1;
//	        			else
//	        			    return 0;
//	        		}
//	        		else if (res > 0)
//	        			return 1;
//	        		else 
//	        			return -1;   
//				}
//			});
//		}
//		catch(StringIndexOutOfBoundsException e)
//		{
//			e.printStackTrace();
//		}
//			NumberPages head = new NumberPages();
//			head.setNumberPage("Number of pages");
//			if (!LoginAsync.numberPagesList.get(0).getNumberPage().equalsIgnoreCase("Number of pages"))
//				LoginAsync.numberPagesList.add(0, head);
//			ArrayAdapter<NumberPages> dataAdapter = new ArrayAdapter<NumberPages>(this,
//					android.R.layout.simple_spinner_item, LoginAsync.numberPagesList);
//			dataAdapter	.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//			numberPagesEssay.setAdapter(dataAdapter);
//			numberPagesEssay.setOnItemSelectedListener(new OnItemSelectedListener() {
//				public void onItemSelected(AdapterView<?> parent, View view,
//						int position, long id) {
//					
//				}
//				public void onNothingSelected(AdapterView<?> parent) {
//					
//					// do nothing
//				}
//			});
//
//	}
//		public void addNumberReferences() {
//			Collections.sort(LoginAsync.numberReferencesList, new Comparator<NumberOfReferences>() {
//			    private int fixString(NumberOfReferences in) {
//			    	int res = 0;
//			    	
//			    		if (in.getNumberReference().length()>2)
//			             {
//			    			if (numbersCheck(in.getNumberReference()))
//			    			res =  Integer.parseInt((in.getNumberReference()).substring(0, in.getNumberReference().indexOf('_')));
//			             }
//			    		else 
//			    		 {
//			    			if (numbersCheck(in.getNumberReference()))
//			    			res= Integer.parseInt((in.getNumberReference()));
//			    		 }
//			    			    	
//			    	return res;
//			    }
//				public int compare(NumberOfReferences lhs, NumberOfReferences rhs) {
//				
//					int res = fixString(lhs) - fixString(rhs);
//	        		if (res == 0)
//	        		{
//	        			if (lhs.getNumberReference().length() < rhs.getNumberReference().length())
//	        				return -1;
//	        			else
//	        			    return 0;
//	        		}
//	        		else if (res > 0)
//	        			return 1;
//	        		else 
//	        			return -1;   
//				}
//				
//			});
//			
//			ArrayAdapter<NumberOfReferences> dataAdapter = new ArrayAdapter<NumberOfReferences>(this,
//					android.R.layout.simple_spinner_item, LoginAsync.numberReferencesList);
//			dataAdapter	.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//			NumberOfReferences numberReferenceHead = new NumberOfReferences();
//			numberReferenceHead.setNumberReference("Number of references");
//			if (!LoginAsync.numberReferencesList.get(0).getNumberReference().equalsIgnoreCase("Number of references"))
//				LoginAsync.numberReferencesList.add(0, numberReferenceHead);
//			
//			numberReferences.setAdapter(dataAdapter);
//			numberReferences.setOnItemSelectedListener(new OnItemSelectedListener() {
//	
//				public void onItemSelected(AdapterView<?> parent, View view,
//						int position, long id) {
//					
//					
//	
//				}
//				public void onNothingSelected(AdapterView<?> parent) {
//				}
//			});
//	
//	}
//		
//	private class SendOrderTask extends AsyncTask<Void, Void, JSONObject> {
//
//		JSONObject response;
//
//		protected void onPreExecute() {
//			progDailog = ProgressDialog.show(NewEssayActivity.this,
//					"Please wait...", "Sending order to the server ...", true,
//					true);
//		}
//
//		protected JSONObject doInBackground(Void... args) {
//
//		
//			try {
//
//				// Category curCat = (Category) catSpin.getSelectedItem();
//				Level curLev = (Level) levelSpin.getSelectedItem();
//				// Log.i("cat ",(curCat).getCategoryTitle());
//				TimeZone timezone = TimeZone.getTimeZone(timezonSpin
//						.getSelectedItem().toString());
//				Log.i("selected timezone", timezone.toString());
//				
//				response = launch.sendWriting(orderTitle.getText().toString(),
//						Integer.toString(((Subject) subjSpin.getSelectedItem())
//								.getSubjectId()), Integer
//								.toString(((Level) levelSpin.getSelectedItem())
//										.getLevelId()), deadline.getText()
//								.toString(), taskReq.getText().toString(),
//						Integer.toString(explanationReqInt), "", timezonSpin
//								.getSelectedItem().toString(),
//						FileManagerActivity.getFinalAttachFiles(),numberPagesEssay.getSelectedItem().toString(), 
//						numberReferences.getSelectedItem().toString(), Integer.toString(((EssayType)essayTypeSpin.getSelectedItem()).getEssayTypeId()),
//						Integer.toString(((EssayCreationStyle)citationStyleSpin.getSelectedItem()).getECSId()));
//
//				Log.i("new order creation response", response.toString());
//			} catch (Exception e) {
//
//				e.printStackTrace();
//			}
//
//			return response;
//		}
//
//		protected void onPostExecute(JSONObject forPrint) {
//			progDailog.dismiss();
//			Intent i = new Intent(NewEssayActivity.this,
//					DashboardActivityAlt.class);
//			Bundle mBundle = new Bundle();
//			mBundle.putString("NewOrder", "wasAdded");
//			i.putExtras(mBundle);
//			startActivity(i);
//
//
//		}
//
//	}
//
//	 public int getFilePosition(View v)
//	 {
//		 int pos = 0;
//		for (int i = 0; i<adapter.getCount();i++)
//		{
//			Log.i("fileList item", adapter.getItem(i).getName());
//			if (((CheckBox)v).getText().equals((adapter.getItem(i).getName())))
//			 pos = i;
//		}
//		 return pos;
//	 }
//	  OnLongClickListener onclicklistener = new OnLongClickListener() {
//			public boolean onLongClick(View arg0) {
//				final CharSequence[] items = {"Open", "Delete", "Details"};
//				final AlertDialog.Builder builder = new AlertDialog.Builder(NewEssayActivity.this);
//			    builder.setTitle(((TextView)arg0).getText().toString());
//			    Log.i("view class", arg0.getClass().getName());
//			    final int pos = getFilePosition(arg0);
//			    Log.i("position ", Integer.toString(pos));
//			    Log.i("view name", ((CheckBox)arg0).getText().toString());
//			    Log.i("view tag", (((CheckBox)arg0).getTag()).toString());
//			    Integer position  = (Integer) ((CheckBox)arg0).getTag();
//			    
//				builder.setItems(items, new DialogInterface.OnClickListener() {
//				    public void onClick(DialogInterface dialog, int item) {
//				    	if (item == 0)
//				    	{
//				    		File file = FileManagerActivity.getFinalAttachFiles().get(pos);
//				    		
//				    			Intent intent = new Intent();
//				    			intent.setAction(android.content.Intent.ACTION_VIEW);
//				    			intent.setDataAndType(Uri.fromFile(file), "text/plain");
//				    			startActivity(intent);
//				    			Log.i("file position in new Order open", Integer.toString(pos));
//							
//				    	}
//				    	 else if (item == 1)
//				    		{  
//				    		 
//				    		   if (FileManagerActivity.getFinalAttachFiles().size()==1)
//				    		   {  
//				    			   FileManagerActivity.getFinalAttachFiles().clear();
//				    			   adapter.clear();
//				    			   adapter.notifyDataSetChanged();
//				    			   btnFilesRemove.setVisibility(View.INVISIBLE);
//				    		   }
//				    		   else
//				    			   { 
//				    				   try
//				    				   {
//				    					   
//					    				     adapter.remove(FileManagerActivity.getFinalAttachFiles().get(pos));
//							    		     Log.i("count",Integer.toString(FileManagerActivity.getFinalAttachFiles().size()));
//							    		     adapter.notifyDataSetChanged();
//				    				   }
//				    				   catch(IndexOutOfBoundsException e)
//				    				   {
//				    					   e.printStackTrace();
//				    				   }
//				    				  
//				    				   for (File f :  FileManagerActivity.getFinalAttachFiles())
//						    		     {
//						    		    	 Log.i("file container", f.getName());
//						    		     } 
//				    				   Log.i("file manager size", Integer.toString( FileManagerActivity.getFinalAttachFiles().size()));
//				    			   }
//				    		}
//				    	else if (item == 2)
//				    	{
//				    		File file = FileManagerActivity.getFinalAttachFiles().get(pos);
//				    		Log.i("file position in new Order details", Integer.toString(pos));
//				    		AlertDialog.Builder builder2 = new AlertDialog.Builder(NewEssayActivity.this);
//				  		    builder2.setTitle(file.getName());
//				  		  FileInputStream fis;
//						try {
//							fis = new FileInputStream(file);
//							builder2.setMessage("Size of file is: " + Long.toString(fis.getChannel().size())+ "  KB"+"\r\n"+
//							"Path of file is: " +"\r\n" + file.getPath());
//
//						} catch (FileNotFoundException e) 
//						{
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (IOException e) 
//						{
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//			    			
//				  		    
//				  		  AlertDialog alert = builder2.create();
//				  			alert.show();
//				    	}
//				    	
//				    	 
//				    }
//				});
//				AlertDialog alert = builder.create();
//				alert.show();
//				return true;
//			}
//		};
//		
//		@Override
//		public void onPause() {
//			super.onPause();
//			prefEditor.putInt("sViewX",essayScroll.getScrollX());
//			prefEditor.putInt("sViewY",essayScroll.getScrollY());
//			prefEditor.commit();
//		}
//		
//		@Override
//		public void onResume() {
//			super.onResume();
//			 int sViewX = prefs.getInt("sViewX",0);   
//		        int sViewY = prefs.getInt("sViewY",0); 
//		        Log.i("scrollX",Integer.toString(sViewX));
//		        Log.i("scrollY",Integer.toString(sViewY));
//		        essayScroll.scrollTo(sViewX, sViewY);
//		}
//		 @Override
//		    public void onBackPressed() {
//		        // Do Here what ever you want do on back press;
//		    }
//		    @Override 
//		    public void onSaveInstanceState(Bundle outState) 
//		    {
//		        //---save whatever you need to persist—
//
//		        outState.putInt("sViewX",essayScroll.getScrollX());
//		        outState.putInt("sViewY",essayScroll.getScrollY());
//		        editor.putInt("sViewX", essayScroll.getScrollX());
//		        editor.putInt("sViewY", essayScroll.getScrollY());
//		        editor.commit();
//		    super.onSaveInstanceState(outState);
//
//		    }
//
//		    @Override
//		    public void onRestoreInstanceState(Bundle savedInstanceState) 
//		    {
//		        super.onRestoreInstanceState(savedInstanceState);
//
//		        int sViewX = savedInstanceState.getInt("sViewX");   
//		        int sViewY = savedInstanceState.getInt("sViewY");
//
//		        essayScroll.scrollTo(sViewX, sViewY);
//
//		    }
//		    
//			 boolean numbersCheck(String email)
//			    {
//			    	Pattern pattern = Pattern.compile("^[0-9]{1,2}.*");
//						Matcher matcher = pattern.matcher(email);
//						boolean matchFound = matcher.matches();
//			    	return matchFound;
//			    }
//			 
//			 public void savePreferences()
//			 {
//				 	prefEditor.putString("taskReq",taskReq.getText().toString());
//					prefEditor.putString("orderTitle",orderTitle.getText().toString());
//					prefEditor.putString("deadline",deadline.getText().toString());
//					prefEditor.putInt("NumberReferences", numberReferences.getSelectedItemPosition());
//					prefEditor.putInt("LevelsValue", levelSpin.getSelectedItemPosition());
//					prefEditor.putInt("EssayCreationStyle", citationStyleSpin.getSelectedItemPosition());
//					prefEditor.putInt("NumberReferences", numberReferences.getSelectedItemPosition());
//					prefEditor.putInt("EssayTypeValue", essayTypeSpin.getSelectedItemPosition());
//					prefEditor.putInt("SubjectsValue", subjSpin.getSelectedItemPosition());
//					prefEditor.putInt("NumberPages", numberPagesEssay.getSelectedItemPosition());
//					
//					prefEditor.putInt("sViewX",essayScroll.getScrollX());
//					prefEditor.putInt("sViewY",essayScroll.getScrollY());
//					
//					prefEditor.commit();
//				 
//			 }

}
