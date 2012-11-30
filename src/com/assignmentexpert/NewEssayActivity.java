package com.assignmentexpert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Security;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.EditorInfo;
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
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.datamodel.Category;
import com.datamodel.EssayCreationStyle;
import com.datamodel.EssayType;
import com.datamodel.Level;
import com.datamodel.Subject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.library.DatabaseHandler;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View header = getLayoutInflater().inflate(
				R.layout.header_listview, null, false);
		final View footer = getLayoutInflater().inflate(
				R.layout.footer_listview, null, true);
		final View essayheader = getLayoutInflater().inflate(
				R.layout.essay_header, null, false);
		setContentView(R.layout.new_order);
		fileList = (ListView) findViewById(R.id.list);
		btnOrderHistory = (Button) findViewById(R.id.btnOrdersHistory);
		btnClose = (Button) findViewById(R.id.btnClose);
		btnProfile = (Button) findViewById(R.id.btnProfile);
		orderTitle = (EditText) essayheader.findViewById(R.id.orderTitle);
		subjSpin = (Spinner) essayheader.findViewById(R.id.subjectSpin);
		catSpin = (Spinner) essayheader.findViewById(R.id.categorySpin);
		levelSpin = (Spinner) essayheader.findViewById(R.id.levelSpin);
		essayTypeSpin = (Spinner) essayheader.findViewById(R.id.essayTypeSpin);
		citationStyleSpin =(Spinner) essayheader.findViewById(R.id.citationStyleSpin); 
				
		Security.addProvider(new BouncyCastleProvider());

		deadline = (EditText) essayheader.findViewById(R.id.deadlineSpin);
		mDateDisplay = (TextView) findViewById(R.id.login_error);
		selectFiles = (Button) essayheader.findViewById(R.id.btnSelectFiles);
		taskReq = (EditText) essayheader.findViewById(R.id.taskRequirements);


		btnSubmitOrder = (Button) footer.findViewById(R.id.btnSubmitOrder);
		btnFilesRemove = (Button) footer.findViewById(R.id.btnRemoveFiles);

		btnFilesRemove.setVisibility(View.INVISIBLE);

		fileList.setCacheColorHint(Color.WHITE);


		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR);
		mMinute = c.get(Calendar.MINUTE);

		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		prefEditor = prefs.edit();
		sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
		editor = sharedPreferences.edit();

		productSpin = (Spinner) essayheader.findViewById(R.id.productSpin);
		ArrayList<String> products = new ArrayList<String>();
		products.add("Essay");
		products.add("Order");

		final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, products);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		productSpin.setAdapter(dataAdapter);
		
		fileList.addHeaderView(essayheader, null, false);
		fileList.addFooterView(footer, null, true);
		productSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

			private ViewStub stub;

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				if (productSpin.getSelectedItem().toString().equals("Order")) {
					{
						Intent i = new Intent(getApplicationContext(),
			                       NewOrderActivity.class);
			               startActivity(i);
					}

				}
				
				}
			

			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}

		});
		addSubjects();
		addLevels();
		addTimeZones();
		addFiles();
		addEssayTypes();
		addEssayCreationStyles();
		fileList.setAdapter(adapter);
		 
		//
		if (!FileManagerActivity.getFinalAttachFiles().isEmpty())
			btnFilesRemove.setVisibility(View.VISIBLE);
		else
			btnFilesRemove.setVisibility(View.INVISIBLE);

		for (int b = 0; b < FileManagerActivity.getFinalAttachFiles().size(); b++) {
			checks.add(b, 0);
		}

		//
		// footer.setOnTouchListener(new View.OnTouchListener() {
		// public boolean onTouch(View arg0, MotionEvent arg1) {
		// header.clearFocus();
		// footer.setFocusable(true);
		// return true;
		// }
		// });

		btnProfile.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				Intent i = new Intent(getApplicationContext(),
						ProfileActivity.class);
				startActivity(i);

			}
		});
		btnOrderHistory.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				Intent i = new Intent(getApplicationContext(),
						DashboardActivityAlt.class);
				startActivity(i);

			}
		});

		btnClose.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				editor.remove("username");
				editor.remove("password");
				editor.remove("isChecked");
				editor.commit();
				Intent i = new Intent(getApplicationContext(),
						LoginActivity.class);
				startActivity(i);
			}
		});

		btnFilesRemove.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				for (int i = 0; i < checks.size(); i++) {

					if (checks.get(i) == 1) {
						adapter.remove(adapter.getItem(i));
						checks.remove(i);
						i--;
					}
					if (!FileManagerActivity.getFinalAttachFiles().isEmpty())
						btnFilesRemove.setVisibility(View.VISIBLE);
					else
						btnFilesRemove.setVisibility(View.INVISIBLE);

				}

			}
		});

		btnOrderHistory.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				Intent i = new Intent(getApplicationContext(),
						DashboardActivityAlt.class);
				startActivity(i);

			}
		});

		selectFiles.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				Intent i = new Intent(getApplicationContext(),
						FileManagerActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putString("FileManager", "NewEssay");
				i.putExtras(mBundle);
				startActivity(i);

			}
		});

		orderTitle.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {

				if (orderTitle.getText().toString()
						.equals("Order title(specify)")
						| orderTitle
								.getText()
								.toString()
								.equals("The title have to be longer than 5 symbols")
						| orderTitle.getText().toString()
								.equals("You have to choose order name")) {
					orderTitle.setText("");
				}
				orderTitle.setHint("Order title");
				orderTitle.setTextColor(Color.BLACK);
				return false;
			}
		});

		orderTitle.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					public void onGlobalLayout() {
						int heightDiff = orderTitle.getRootView().getHeight()
								- orderTitle.getHeight();
						if (heightDiff > 100) { // if more than 100 pixels, its
												// probably a keyboard...
							if (!(orderTitle.getText().toString()
									.equals("Order title(specify)") & orderTitle
									.getText().toString().equals(""))) {
								prefEditor.putString("orderTitle", orderTitle
										.getText().toString());
								prefEditor.commit();
								// prefEditor.commit();

							}
						}
					}
				});
		taskReq.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					public void onGlobalLayout() {
						int heightDiff = taskReq.getRootView().getHeight()
								- taskReq.getHeight();
						if (heightDiff > 100) { // if more than 100 pixels, its
												// probably a keyboard...
							if (!(taskReq.getText().toString()
									.equals("Task/Specific requirements"))) {
								prefEditor.putString("taskReq", taskReq
										.getText().toString());
								prefEditor.commit();
							}

						}
					}
				});
		deadline.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					public void onGlobalLayout() {
						int heightDiff = deadline.getRootView().getHeight()
								- deadline.getHeight();
						if (heightDiff > 100) { // if more than 100 pixels, its
												// probably a keyboard...

							if (!(deadline.getText().toString()
									.equals("Deadline"))) {
								prefEditor.putString("deadline", deadline
										.getText().toString());
								prefEditor.commit();
							}
						}
					}
				});

		taskReq.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// inputPassword.setFocusable(true);

				if (taskReq.getText().toString()
						.equals("Task/Specific requirements")
						| taskReq
								.getText()
								.toString()
								.equals("Choose the task specific requirements")) {
					taskReq.setText("");
				}
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
				btnSubmitOrder.setFocusable(true);
				return false;
			}

		});

		deadline.setOnEditorActionListener(new OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					deadline.setFocusable(false);

				}
				return false;
			}
		});
		// final View activityRootView = findViewById(R.id.new_order);
		// activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new
		// OnGlobalLayoutListener() {
		// public void onGlobalLayout() {
		// int heightDiff = activityRootView.getRootView().getHeight() -
		// activityRootView.getHeight();
		// if (heightDiff > 100)
		// {
		// // if more than 100 pixels, its probably a keyboard...
		// Log.i("I'm in the observer", "soft keyboard mama");
		// // header.setFocusable(true);
		// }
		// else
		// {
		// Log.i("I'm in the observer", "soft keyboard closed");
		// footer.setFocusable(true);
		// }
		// }
		// });

		
		btnSubmitOrder.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					essayheader.clearFocus();
					boolean errorFlag = false;
					if (deadline.getText().toString()
							.equalsIgnoreCase("Deadline")) {
						Toast toast = Toast.makeText(getApplicationContext(),
								"You have to choose deadline",
								Toast.LENGTH_SHORT);
						toast.show();
						deadline.setText("");
						deadline.setTextColor(Color.RED);
						deadline.setText("You have to choose deadline");
						errorFlag = true;

					}
					if (orderTitle.getText().toString()
							.equalsIgnoreCase("Order title(specify)")) {
						Toast toast = Toast.makeText(getApplicationContext(),
								"You have to choose order name",
								Toast.LENGTH_SHORT);
						toast.show();
						orderTitle.setText("");
						orderTitle.setTextColor(Color.RED);
						orderTitle.setText("You have to choose order name");
						errorFlag = true;

					} else if (orderTitle.getText().length() < 5) {
						Toast toast = Toast.makeText(getApplicationContext(),
								"The title have to be longer than 5 symbols",
								Toast.LENGTH_SHORT);
						toast.show();
						orderTitle.setText("");
						orderTitle.setTextColor(Color.RED);
						orderTitle
								.setText("The title have to be longer then 5 symbols");
						errorFlag = true;
					} else if (taskReq.getText().toString()
							.equalsIgnoreCase("Task/Specific requirements")) {
						Toast toast = Toast.makeText(getApplicationContext(),
								"Choose the task specific requirements",
								Toast.LENGTH_SHORT);
						toast.show();
						taskReq.setText("");
						taskReq.setTextColor(Color.RED);
						taskReq.setText("Choose the task specific requirements");
						errorFlag = true;
					}
					if (subjSpin.getSelectedItem().toString()
							.equals("Subjects"))

					{

						Toast toast = Toast.makeText(getApplicationContext(),
								"You should to choose the subject",
								Toast.LENGTH_SHORT);
						toast.show();
						errorFlag = true;
					}
					if (levelSpin.getSelectedItem().toString().equals("Levels")) {

						Toast toast = Toast.makeText(getApplicationContext(),
								"You should to choose the level",
								Toast.LENGTH_SHORT);
						toast.show();
						errorFlag = true;
					}

					if (errorFlag == false) {

						ArrayList<File> files = new ArrayList<File>();

						new SendOrderTask().execute();
						prefEditor.remove("orderTitle");
						prefEditor.remove("taskReq");
						prefEditor.remove("deadline");
						prefEditor.remove("LevelsValue");
						prefEditor.remove("SubjectsValue");
						prefEditor.putString("newOrder", "newOne");
						prefEditor.commit();
						// if
						// (!FileManagerActivity.getFinalAttachFiles().isEmpty())
						// FileManagerActivity.getFinalAttachFiles().clear();

					}

				}

				return false;
			}

		});

		// start to comment

		// if (!prefs.getAll().isEmpty())
		// {
		// String levelsValue=prefs.getString("LevelsValue", null);
		// String subjectsValue=prefs.getString("SubjectsValue", null);
		// CharSequence charTitle = prefs.getString("orderTitle", null);
		// CharSequence charTask = prefs.getString("taskReq", null);
		// CharSequence charDeadline = prefs.getString("deadline", null);
		//
		// if (charTitle != null)
		// {
		// orderTitle.setText(charTitle);
		// }
		// else
		// orderTitle.setText("Order title(specify)");
		// if (charTask != null)
		// {
		// taskReq.setText(charTask);
		//
		// }
		// else
		// taskReq.setText("Task/Specific requirements");
		// if (charDeadline != null)
		// {
		// deadline.setText(charDeadline);
		// }
		// else
		// deadline.setText("Deadline");
		// if (levelsValue != null)
		// {
		// for(int i=0;i<levelsListSpinner.size();i++)
		// {
		//
		// if(levelsValue.equals(levelsListSpinner.get(i).toString()))
		// {
		// levelSpin.setSelection(i);
		// break;
		// }
		// }
		// }
		// else
		// addLevels();
		// if (subjectsValue!=null)
		// {
		// Log.i("subjSize", Integer.toString(subjectsListSpinner.size()));
		// ArrayAdapter<Subject> myAdap = (ArrayAdapter<Subject>)
		// subjSpin.getAdapter();
		// for(int i=0;i<myAdap.getCount();i++)
		// {
		//
		// if(subjectsValue.equals(myAdap.getItem(i).getSubjectTitle()))
		// {
		// Log.i("subjTitle",subjectsValue);
		// Log.i("subjTitle from spin",subjectsListSpinner.get(i).toString());
		// Log.i("subjPosition", Integer.toString(i));
		// //subjSpin.setSelection(i);
		// //cast to an ArrayAdapter
		//
		// int spinnerPosition = myAdap.getPosition(subjectsListSpinner.get(i));
		//
		// Log.i("selected item", Integer.toString(spinnerPosition));
		// Log.i("selected item spin", Integer.toString(
		// subjSpin.getSelectedItemPosition()));
		// //set the default according to value
		// subjSpin.setSelection(i);
		//
		// break;
		// }
		// }
		// }
		// else
		// addSubjects();
		//
		//
		//
		// }

		// end comment

	}

	DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
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
	public void updateFileList(ArrayList<File> fileArrayList)
	{
		Log.i("linear layout count", Integer.toString(fileList.getChildCount()));
		for (int i = 0;i<fileArrayList.size();i++)
		{
			View child = fileList.getChildAt(i);
			if (child instanceof TextView)
			{
				child.setTag(i);
				child.setId(i);
				Log.i("child class", child.getClass().getName());
			}
		}
	}

	public void addFiles() {
		adapter = new ArrayAdapter<File>(this, R.layout.new_order,
				R.id.fileCheck, FileManagerActivity.getFinalAttachFiles()) {
			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.file, null);
				view.setFocusable(false);
				CheckBox textView = (CheckBox) view
						.findViewById(R.id.fileCheck);
				textView.setClickable(true);
				textView.setText(FileManagerActivity.getFinalAttachFiles()
						.get(position).getName().toString());
				textView.setTextColor(Color.BLACK);
				textView.setPadding(55, 0, 0, 0);
				textView.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.file_icon, 0, 0, 0);
				textView.setTextSize(16);
				textView.setGravity(Gravity.CENTER_VERTICAL
						| Gravity.CENTER_HORIZONTAL);
				int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
				textView.setCompoundDrawablePadding(dp5);
				textView.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {

						if (((CheckBox) v).isChecked()) {
							checks.set(position, 1);
						} else {
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
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);

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

			break;
		}
	}

	private void updateDisplay() {
		String minutes = null;
		if (mMinute > 9)
			minutes = Integer.toString(mMinute);
		else
			minutes = "0" + Integer.toString(mMinute);
		deadline.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mYear).append("-").append(mMonth + 1).append("-")
				.append(mDay).append(" ").append(mHour).append(":")
				.append(minutes).append(" "));
		showDialog(TIME_DIALOG_ID);
	}

	public void addSubjects() {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());

		try {

			Dao<Subject, Integer> daoSubject = db.getSubjectDao();
			subjectsListSpinner = daoSubject.queryForAll();
			final Subject head = new Subject();
			head.setSubjectTitle("Subjects");
			// if (prefs.getString("newOrder", null).equals("newOne"))
			subjectsListSpinner.add(0, head);
			final ArrayAdapter<Subject> dataAdapter = new ArrayAdapter<Subject>(
					this, android.R.layout.simple_spinner_item,
					subjectsListSpinner);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			subjSpin.setAdapter(dataAdapter);
			subjSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					prefEditor.putString("SubjectsValue", subjSpin
							.getSelectedItem().toString());
					prefEditor.remove("newOrder");
					prefEditor.commit();

					// if (subjectsListSpinner.contains(head));
					// {
					// subjectsListSpinner.remove(head);
					// dataAdapter.remove(head);
					// }

					Subject item = (Subject) parent.getItemAtPosition(position);
					addCategories(item);

				}

				public void onNothingSelected(AdapterView<?> arg0) {
					if (subjectsListSpinner.contains(head))
						subjectsListSpinner.remove(head);

				}
			});
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public void addCategories(Subject subj) {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		try {

			Dao<Category, Integer> daoCategory = db.getCategoryDao();
			// final List<Category> catTitles = (List<Category>)
			// daoCategory.q.queryForId(Integer.valueOf(id));

			QueryBuilder<Category, Integer> qb = daoCategory.queryBuilder();
			//
			List<Category> categories = daoCategory.queryBuilder().where()
					.eq("subject_id", subj.getSubjectId()).query();
			final Category head = new Category();
			head.setCategoryTitle("Categories");
			// if (prefs.getString("newOrder", null).equals("newOne"))
			// categories.add(0,head);
			ArrayAdapter<Category> dataAdapter = new ArrayAdapter<Category>(
					this, android.R.layout.simple_spinner_item, categories);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			catSpin.setAdapter(dataAdapter);
			catSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					prefEditor.putString("CategoriesValue", catSpin
							.getSelectedItem().toString());
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

	boolean timezoneValidate(String email) {

		Pattern pattern = Pattern.compile(".+\\/+[A-z]+");
		Matcher matcher = pattern.matcher(email);
		boolean matchFound = matcher.matches();
		return matchFound;
	}

	public void addLevels() {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		try {
			Dao<Level, Integer> daoSubject = db.getLevelDao();
			levelsListSpinner = daoSubject.queryForAll();
			final Level head = new Level();
			head.setLevelTitle("Levels");
			levelsListSpinner.add(0, head);
			ArrayAdapter<Level> dataAdapter = new ArrayAdapter<Level>(this,
					android.R.layout.simple_spinner_item, levelsListSpinner);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			levelSpin.setAdapter(dataAdapter);
			levelSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					prefEditor.putString("LevelsValue", levelSpin
							.getSelectedItem().toString());
					prefEditor.commit();

				}

				public void onNothingSelected(AdapterView<?> parent) {
					// do nothing
				}

			});

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void addEssayTypes() {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		try {
			Dao<EssayType, Integer> daoSubject = db.getEssayTypeDao();
			essayTypeListSpinner = daoSubject.queryForAll();
			final EssayType head = new EssayType();
			head.setEssayTypeTitle("Essay Types");
			essayTypeListSpinner.add(0, head);
			ArrayAdapter<EssayType> dataAdapter = new ArrayAdapter<EssayType>(this,
					android.R.layout.simple_spinner_item, essayTypeListSpinner);
			dataAdapter	.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			essayTypeSpin.setAdapter(dataAdapter);
			essayTypeSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					prefEditor.putString("EssayTypeValue", essayTypeSpin
							.getSelectedItem().toString());
					prefEditor.commit();

				}

				public void onNothingSelected(AdapterView<?> parent) {
					// do nothing
				}

			});

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void addEssayCreationStyles() {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		try {
			Dao<EssayCreationStyle, Integer> daoSubject = db.getEssayCreationStyleDao();
			essayCreationStyleListSpinner = daoSubject.queryForAll();
			final EssayCreationStyle head = new EssayCreationStyle();
			head.setStr("Essay Creation Styles");
			essayCreationStyleListSpinner.add(0, head);
			ArrayAdapter<EssayCreationStyle> dataAdapter = new ArrayAdapter<EssayCreationStyle>(this,
					android.R.layout.simple_spinner_item, essayCreationStyleListSpinner);
			dataAdapter	.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			citationStyleSpin.setAdapter(dataAdapter);
			citationStyleSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					prefEditor.putString("EssayCreationStyle", citationStyleSpin
							.getSelectedItem().toString());
					prefEditor.commit();

				}

				public void onNothingSelected(AdapterView<?> parent) {
					// do nothing
				}

			});

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addTimeZones() {
		timezonSpin = (Spinner) findViewById(R.id.timezoneSpin);
		//
		final String[] TZ = TimeZone.getAvailableIDs();

		Log.i("count", Integer.toString(TZ.length));
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		final ArrayList<String> TZ1 = new ArrayList<String>();
		for (int i = 0; i < TZ.length; i++) {
			if (!(TZ1.contains(TimeZone.getTimeZone(TZ[i]).getDisplayName()))) {
				if (timezoneValidate(TZ[i]))
					TZ1.add(TZ[i]);
			}
		}
		for (int i = 0; i < TZ1.size(); i++) {
			adapter.add(TZ1.get(i));
		}

		timezonSpin.setAdapter(adapter);
		for (int i = 0; i < TZ1.size(); i++) {
			if (TimeZone.getTimeZone(TZ[i]).getDisplayName()
					.equals(TimeZone.getDefault().getDisplayName())) {
				timezonSpin.setSelection(i);
			}
		}

		timezonSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
			boolean count = false;

			public void onItemSelected(AdapterView<?> parent, View view,
					final int position, long id) {
				// for (int i = 0; i < TZ.length; i++)
				// {
				// Log.i("timezone",TZ[i]);
				// }
				Log.i("list count", Integer.toString(TZ1.size()));
				Log.i("list count", Integer.toString(TZ.length));
				// for (String a : TZ1)
				// {
				// Log.i("qwerty",a);
				// }
				if (count) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							NewEssayActivity.this);
					builder.setMessage(
							"Are you sure to choose timezone  "
									+ TZ1.get(position) + "  ?")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {

											Log.i("timezone spin", timezonSpin
													.getSelectedItem()
													.toString());
											Log.i("timezone spin",
													Integer.toString(position));

											// Log.i("timezone from spin",
											// TimeZone.getTimeZone(TZ1.get(id)).toString());
											if (timezonSpin
													.getSelectedItem()
													.toString()
													.equals(TimeZone
															.getTimeZone(
																	timezonSpin
																			.getSelectedItem()
																			.toString())
															.getDisplayName())) {
												{
													TimeZone zone = TimeZone
															.getTimeZone(timezonSpin
																	.getSelectedItem()
																	.toString());
													Log.i("chosen timezone",
															zone.getDisplayName());
												}

											}

										}
									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											for (int i = 0; i < TZ1.size(); i++) {
												if (TimeZone
														.getTimeZone(TZ[i])
														.getDisplayName()
														.equals(TimeZone
																.getDefault()
																.getDisplayName())) {
													timezonSpin.setSelection(i);
												}
											}
											count = false;
											dialog.cancel();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
					TimeZone timeZone = TimeZone.getTimeZone(TZ1.get(position));
					Log.i("final countdown", timeZone.toString());
				}
				count = true;

			}

			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}

		});
	}

	private class SpinnersUpload extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			progDailog = ProgressDialog.show(NewEssayActivity.this,
					"Please wait...", "Retrieving data ...", true, true);
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			return null;
		}

		protected void onPostExecute(Void unused) {
			addSubjects();
			addLevels();
			addTimeZones();
			addFiles();
			progDailog.dismiss();

		}

	}

	private class SendOrderTask extends AsyncTask<Void, Void, JSONObject> {

		JSONObject response;

		protected void onPreExecute() {
			progDailog = ProgressDialog.show(NewEssayActivity.this,
					"Please wait...", "Sending order to the server ...", true,
					true);
		}

		protected JSONObject doInBackground(Void... args) {

			UserFunctions launch = new UserFunctions();
			try {

				// Category curCat = (Category) catSpin.getSelectedItem();
				Level curLev = (Level) levelSpin.getSelectedItem();
				// Log.i("cat ",(curCat).getCategoryTitle());
				Log.i("level ", (curLev).getLevelTitle());
				TimeZone timezone = TimeZone.getTimeZone(timezonSpin
						.getSelectedItem().toString());
				Log.i("selected timezone", timezone.toString());

				response = launch.sendOrder(orderTitle.getText().toString(),
						Integer.toString(((Category) catSpin.getSelectedItem())
								.getCategoryId()), Integer
								.toString(((Level) levelSpin.getSelectedItem())
										.getLevelId()), deadline.getText()
								.toString(), taskReq.getText().toString(),
						Integer.toString(explanationReqInt), "", timezonSpin
								.getSelectedItem().toString(),
						FileManagerActivity.getFinalAttachFiles());

				Log.i("new order creation response", response.toString());
			} catch (Exception e) {

				e.printStackTrace();
			}

			return response;
		}

		protected void onPostExecute(JSONObject forPrint) {
			progDailog.dismiss();
			Intent i = new Intent(NewEssayActivity.this,
					DashboardActivityAlt.class);
			Bundle mBundle = new Bundle();
			mBundle.putString("NewOrder", "wasAdded");
			i.putExtras(mBundle);
			startActivity(i);

			// Pass the result data back to the main activity

			// if (!res )
			// {
			// DashboardActivityAlt.this.listView1 =
			// (ListView)findViewById(R.id.altOrderslist);
			// OrderAdapter adapter = new
			// OrderAdapter(DashboardActivityAlt.this,
			// R.layout.dash_alt_item, forPrint);
			//
			//
			// listView1.setAdapter(adapter);
			//
			// adapter.notifyDataSetChanged();
			// }
			// else
			// {
			// this.cancel(true);
			// Toast mToast = Toast.makeText(getApplicationContext(),
			// "���� ������!!!", Toast.LENGTH_LONG);
			// mToast.show();
			//
			// }
			//
			// DashboardActivityAlt.pd.dismiss();

		}

	}

	 public int getFilePosition(View v)
	 {
		 int pos = 0;
		for (int i = 0; i<adapter.getCount();i++)
		{
			Log.i("fileList item", adapter.getItem(i).getName());
			if (((CheckBox)v).getText().equals((adapter.getItem(i).getName())))
			 pos = i;
		}
		 return pos;
	 }
	  OnLongClickListener onclicklistener = new OnLongClickListener() {
			public boolean onLongClick(View arg0) {
				final CharSequence[] items = {"Open", "Delete", "Details"};
				final AlertDialog.Builder builder = new AlertDialog.Builder(NewEssayActivity.this);
			    builder.setTitle(((TextView)arg0).getText().toString());
			    Log.i("view class", arg0.getClass().getName());
			    final int pos = getFilePosition(arg0);
			    Log.i("position ", Integer.toString(pos));
			    Log.i("view name", ((CheckBox)arg0).getText().toString());
			    Log.i("view tag", (((CheckBox)arg0).getTag()).toString());
			    Integer position  = (Integer) ((CheckBox)arg0).getTag();
			    
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
				    		AlertDialog.Builder builder2 = new AlertDialog.Builder(NewEssayActivity.this);
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

}
