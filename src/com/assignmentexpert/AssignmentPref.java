package com.assignmentexpert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
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
import android.content.DialogInterface.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.SQLException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

/**
 * *Activity для отправки нового заказа. Так как, согласно дизайну, Activity
 * похожа на Preference, наследуется от PreferenceActivity.
 */
@SuppressLint("NewApi")
public class AssignmentPref extends PreferenceActivity {
	/** * button for files selecting */
	private Button btnSelectFiles;
	/** *sharedPreferences for orders preferences saving */
	private SharedPreferences prefs;
	private Editor prefEditor;
	/** adapter for files **/
	private ArrayAdapter<File> adapter;
	/** containter for files to remove saving **/
	ArrayList<Integer> checks = new ArrayList<Integer>();
	/** button for files removing **/
	private Button btnFilesRemove;

	private ListView mainList;
	View assignfooter;
	View editsfooter;

	/** current Context **/
	Context context;
	/** ListView for files list **/
	private View filesList;
	ListView customfileList;
	private Button btnSubmitOrder;
	/** IconPreference for order subject **/
	private IconPreference subjectPref;
	/** IconPreference for categories **/
	private IconPreference categoryPref;
	/** IconPreference for levels **/
	private IconPreference levelPref;
	/** IconPreference for timezones **/
	private CustomEditPreference timezonePref;

	/** date dialog id **/
	static final int DATE_DIALOG_ID = 1;
	/** time dialog id **/
	static final int TIME_DIALOG_ID = 0;

	/** value for chosen day **/
	private int mDay;
	/** value for chosen hour ***/
	private int mHour;
	/** value for chosen minute ***/
	private int mMinute;
	/** value for chosen yeah ***/
	private int mYear;
	/** value for chosen month ***/
	private int mMonth;
	/** ListPreference for essay's number pages **/
	private ListPreference nmbPages;
	/** ListPreference for essay's type **/
	private ListPreference typePref;
	/** ListPreference for essay's number of references **/
	private ListPreference nmbRefs;
	/** ListPreference for essay's creation type **/
	private ListPreference crStyle;
	/** order title info **/
	private ImageView orderInfo;
	/** order task info **/
	private ImageView taskInfo;
	boolean trigger = false;
	/** CustomTextView for files header **/
	CustomTextView textHead;
	/** CustomTextView for all files size **/
	CustomTextView fileSizeHead;
	/** CustomEditText for order title **/
	private CustomEditText orderTitle;
	/** CustomEditText for order task info **/
	private CustomEditText taskText;
	UserFunctions launch = new UserFunctions();
	/** ListPreference of product **/
	private ListPreference productPref;
	/** CustomEditPreference for order deadline **/
	CustomEditPreference deadlineCus;
	ArrayAdapter<Subject> subjectDataAdapter;
	ArrayAdapter<Category> categoryDataAdapter;
	ArrayAdapter<Level> levelDataAdapter;

	String[] prefValues;

	Object[] essayPref = new Object[4];
	/** CustomCheckBoxPref for detailed info **/
	CustomCheckBoxPref dtlInfo;
	/** CustomCheckBoxPref for exclusive video **/
	CustomCheckBoxPref ExcVideo;
	/** CustomCheckBoxPref for common video **/
	CustomCheckBoxPref commVideo;

	/** ArrayAdapter for pages number **/
	ArrayAdapter<NumberPages> nmbPageDataAdapter;
	/** ArrayAdapter for references page number **/
	ArrayAdapter<NumberOfReferences> nmbRefsDataAdapter;
	/** ArrayAdapter of essay types **/
	ArrayAdapter<EssayType> essayTypeDataAdapter;
	/** ArrayAdapter creation style **/
	ArrayAdapter<EssayCreationStyle> essayCrDataAdapter;

	private int explanationReqInt;
	private int commVideoInt;
	private int exclVideoInt;
	private CheckBox fileCheckHead;

	int assignment_video = 0;
	private Button btnAddPhoto;
	private Uri mOriginalUri;

	GregorianCalendar c;
	private ArrayAdapter<String> timeZoneAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.assignment);
		 setContentView(R.layout.assign_header);
		context = this;

		

		final ListPreference productList = (ListPreference) findPreference("productPref");

		assignfooter = getLayoutInflater().inflate(R.layout.assign_footer2,
				null);
		editsfooter = getLayoutInflater()
				.inflate(R.layout.assign_footer1, null);
		deadlineCus = new CustomEditPreference(this);
		deadlineCus.setTitle("Deadline");
		deadlineCus.setSummary(getResources().getString(R.string.preference_not_selected));
		deadlineCus.setIcon(R.drawable.list_pointer);
		deadlineCus.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				showDialog(DATE_DIALOG_ID);
			}

		});

		filesList = getLayoutInflater().inflate(R.layout.file_list, null);

		orderInfo = (ImageView) editsfooter.findViewById(R.id.orderInfo);
		taskInfo = (ImageView) editsfooter.findViewById(R.id.taskInfo);

		customfileList = (ListView) filesList.findViewById(R.id.fileslist);
		btnSubmitOrder = (Button) assignfooter
				.findViewById(R.id.btnSubmitOrder);
		btnSubmitOrder.getBackground().setAlpha(120);
		subjectPref = (IconPreference) findPreference("subjectPref");
		categoryPref = (IconPreference) findPreference("categoryPref");
		levelPref = (IconPreference) findPreference("levelPref");
//		timezonePref = (IconPreference) findPreference("timezonePref");
		productPref = (ListPreference) findPreference("productPref");

		orderTitle = (CustomEditText) editsfooter.findViewById(R.id.orderTitle);
		taskText = (CustomEditText) editsfooter.findViewById(R.id.taskText);

		nmbPages = (ListPreference) findPreference("nmbPages");
		typePref = (ListPreference) findPreference("typePref");
		crStyle = (ListPreference) findPreference("crStyle");
		nmbRefs = (ListPreference) findPreference("nmbRefs");

		disableCategory();

		mainList = getListView();
		mainList.setCacheColorHint(Color.BLACK);
		getListView().setBackgroundColor(Color.BLACK);
		btnSelectFiles = (Button) editsfooter.findViewById(R.id.btnSelectFiles);
		btnAddPhoto = (Button) editsfooter.findViewById(R.id.btnAddPhoto);

		btnFilesRemove = (Button) filesList.findViewById(R.id.btnRemoveFiles);

		btnFilesRemove.setVisibility(View.INVISIBLE);
		textHead = (CustomTextView) filesList.findViewById(android.R.id.title);

		fileSizeHead = (CustomTextView) filesList.findViewById(R.id.fileSize);

		fileCheckHead = (CheckBox) filesList.findViewById(R.id.fileCheck);

		prefs = getSharedPreferences("newOrder", MODE_PRIVATE);
		prefEditor = prefs.edit();
		
		
		timezonePref = new CustomEditPreference(this);
		timezonePref.setTitle("Time zone");
		timezonePref.setSummary(getResources().getString(R.string.preference_not_selected));
		timezonePref.setIcon(R.drawable.list_pointer);

		
		mainList.addFooterView(deadlineCus);
		mainList.addFooterView(timezonePref);
		mainList.addFooterView(editsfooter, null, true);
		mainList.addFooterView(assignfooter);

		for (int b = 0; b < FileManagerActivity.getFinalAttachFiles().size(); b++) {
			checks.add(b, 0);
		}
		mainList.setAdapter(adapter);
		// Get the custom preference
		addSubjects();
		addLevels();
		productList
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						productList.setSummary((CharSequence) newValue);
						if (productList.getSummary().equals("Assignment")) {
							if (getPreferenceScreen().findPreference("dtlInfo") == null) {
								assignmentFieldsAdd();
								if (!subjectPref.getSummary().toString().equalsIgnoreCase(AssignmentPref.this.getResources().getString(R.string.preference_not_selected)))
									{
									Subject currSubj=null;
									for (Subject name : subjectsListSpinner)
									{
										if (subjectPref.getSummary().toString().equalsIgnoreCase(name.getSubjectTitle().toString()))
											currSubj = name;
											
									}
										addCategories(currSubj.getSubjectId());
										enableCategory();
									}
							}
							if (getPreferenceScreen()
									.findPreference("nmbPages") != null) {
								writingFieldsDelete();
							}
						} else if (((String) productList.getSummary())
								.equalsIgnoreCase("essay")) {

							if (getPreferenceScreen().findPreference("dtlInfo") != null) {
								assignmentFieldsDelete();
							}
							if (getPreferenceScreen()
									.findPreference("nmbPages") == null) {
								writingFieldsAdd();
								addEssayCreationStyles();
								addNumberPages();
								addNumberReferences();
								addEssayTypes();
							}
							categoryPref.setSummary(getResources().getString(R.string.preference_not_selected));
							disableCategory();
						}
						OrderPreferences.getInstance().getArrayList()[11] = productList
								.getSummary();
						return true;
					}

				});

		orderInfo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Dialog dialog = new Dialog(context, R.style.FullHeightDialog);
				TextView myMsg = new TextView(AssignmentPref.this);
				myMsg.setText(getResources().getString(R.string.toast_order_info));
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
				myMsg.setText(getResources().getString(R.string.toast_order_task));
				myMsg.setTextColor(Color.WHITE);
				myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
				dialog.setContentView(myMsg);
				dialog.show();
				dialog.setCanceledOnTouchOutside(true);
			}
		});

		subjectPref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						int index = subjectPref.findIndexOfValue(newValue
								.toString());
						subjectPref.setSummary(subjectPref.getEntries()[index]);
						
						Subject a = subjectDataAdapter.getItem(index);

						OrderPreferences.getInstance().getArrayList()[0] = Integer
								.toString(a.getSubjectId());
						if (productList.getSummary().equals("Assignment")) {
							addCategories(a.getSubjectId());
							enableCategory();
							categoryPref.setSummary(getResources().getString(R.string.preference_not_selected));
						}
						return true;
					}

				});

		try {
			categoryPref
					.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
						public boolean onPreferenceChange(
								Preference preference, Object newValue) {
							if (!subjectPref.getSummary().toString()
									.equalsIgnoreCase(getResources().getString(R.string.preference_not_selected))) {
								int index = categoryPref
										.findIndexOfValue(newValue.toString());
								categoryPref.setSummary(categoryPref
										.getEntries()[index]);
								Category a = categoryDataAdapter.getItem(index);
								if (categoryPref.isEnabled())
									OrderPreferences.getInstance()
											.getArrayList()[1] = Integer
											.toString(a.getCategoryId());
							} else {
								Toast.makeText(AssignmentPref.this,
										getResources().getString(R.string.toast_subject_choose),
										Toast.LENGTH_LONG).show();
							}
							return true;
						}
					});
		} catch (Exception e) {
			Toast.makeText(AssignmentPref.this,
					getResources().getString(R.string.toast_subject_choose),
					Toast.LENGTH_LONG).show();
		}
		levelPref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						int index = levelPref.findIndexOfValue(newValue
								.toString());
						levelPref.setSummary(levelPref.getEntries()[index]);
						Level a = levelDataAdapter.getItem(index);
						// prefValues[2] = Integer.toString(a.getLevelId());
						OrderPreferences.getInstance().getArrayList()[2] = Integer
								.toString(a.getLevelId());
						return true;
					}

				});
		timeZoneAdapter = new ArrayAdapter<String>(
		  			this,  R.layout.dialog_list_item, R.id.tv);
		  new FrequentlyUsedMethods(this).addTimeZones(timezonePref, timeZoneAdapter);
		timezonePref.setOnClickListener(new View.OnClickListener() {
	     	   
	    	  
	    	  
	          private AlertDialog dialog;

			public void onClick(View view) {
	          			
	          	AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentPref.this);
	          	builder.setTitle("Select your time zone");

	          	ListView modeList = new ListView(AssignmentPref.this);
	          	modeList.setAdapter(timeZoneAdapter);
	          	builder.setView(modeList);
	          	dialog = builder.create();
	          	dialog.show(); 
	          	modeList.setBackgroundColor(Color.WHITE);
	          	modeList.setCacheColorHint(Color.WHITE);
	          	modeList.setOnItemClickListener(new OnItemClickListener() {
	    	          public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	  	          {
	    	        	  
	    	        	dialog.dismiss();
	    	        	timezonePref.setSummary(timeZoneAdapter.getItem(position));
	    	        	  
	  	          }
	          	});
	          	
	          	
	          }
	      });
//		timezonePref
//				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//					public boolean onPreferenceChange(Preference preference,
//							Object newValue) {
//						int index = timezonePref.findIndexOfValue(newValue
//								.toString());
//						timezonePref.setSummary(timezonePref.getEntries()[index]);
//						return true;
//					}
//
//				});

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

		if (!FileManagerActivity.getFinalAttachFiles().isEmpty()) {
			addFiles();
			btnFilesRemove.setVisibility(View.VISIBLE);
		} else {

			mainList.removeFooterView(filesList);
		}
		btnFilesRemove.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				for (int i = 0; i < checks.size(); i++) {
					if (checks.get(i) == 1) {
						adapter.remove(adapter.getItem(i));
						checks.remove(i);
						textHead.setText(Integer.toString(FileManagerActivity
								.getFinalAttachFiles().size())
								+ " files attached");
						long wholeSize = 0;
						for (File file : FileManagerActivity
								.getFinalAttachFiles()) {
							wholeSize += file.length();
						}
						fileSizeHead.setText(Long.toString(wholeSize / 1024)
								+ " KB");
						i--;
					}

				}
				if (FileManagerActivity.getFinalAttachFiles().size() == 0) {
					fileCheckHead.setChecked(false);
					FileManagerActivity.getFinalAttachFiles().clear();
					adapter.clear();
					adapter.notifyDataSetChanged();
					mainList.removeFooterView(filesList);
					btnSelectFiles.setText(getResources().getString(R.string.btn_add_files));
				}
			}
		});

//		orderTitle.setOnTouchListener(new OnTouchListener() {
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//
//				if (orderTitle.getText().toString()
//						.equalsIgnoreCase("Put more than 5 symbols")) {
//					orderTitle.getText().clear();
//					orderTitle.setHint("Order title");
//					orderTitle.setTextColor(Color.BLACK);
//				return false;
//			}
//		});

		// new DatePickerDialog.OnDateSetListener() {
		// public void onDateSet(DatePicker view, int year, int monthOfYear,
		// int dayOfMonth) {
		// mYear = year;
		// mMonth = monthOfYear;
		// mDay = dayOfMonth;
		// calendar.set(year, monthOfYear, dayOfMonth);
		// updateDisplay();
		// }
		// };
		// new TimePickerDialog.OnTimeSetListener() {
		// public void onTimeSet(TimePicker view, int hourOfDay, int
		// minuteOfHour) {
		// mHour = hourOfDay;
		// mMinute = minuteOfHour;
		// calendar.se
		// updateDisplay();
		//
		// }
		// };

		btnSubmitOrder.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				boolean errorFlag = false;
				if (deadlineCus.getSummary().toString()
						.equalsIgnoreCase(getResources().getString(R.string.preference_not_selected))) {
					Toast toast = Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.toast_deadline_choose), Toast.LENGTH_SHORT);
					toast.show();
					errorFlag = true;
				}
				if (orderTitle.getText().toString().equalsIgnoreCase("")) {
					Toast toast = Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.toast_order_title_length),
							Toast.LENGTH_SHORT);
					toast.show();
					errorFlag = true;
				}
				if (productPref.getSummary().toString()
						.equalsIgnoreCase(getResources().getString(R.string.preference_not_selected))) {
					
					Toast toast = Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.toast_product_choose),
							Toast.LENGTH_SHORT);
					toast.show();
					errorFlag = true;
				}
				if (subjectPref.getSummary().toString()
						.equalsIgnoreCase(getResources().getString(R.string.preference_not_selected))) {
					Toast toast = Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.toast_subject_choose),
							Toast.LENGTH_SHORT);
					toast.show();
					errorFlag = true;
				}
				if (productPref.getSummary().toString()
						.equalsIgnoreCase(getResources().getString(R.string.textview_assignment))) {
					if (categoryPref.getSummary().toString()
							.equalsIgnoreCase(getResources().getString(R.string.preference_not_selected))) {
						
						Toast toast = Toast.makeText(getApplicationContext(),
								getResources().getString(R.string.toast_category_choose),
								Toast.LENGTH_SHORT);
						toast.show();
						errorFlag = true;
					}
				}
				if (productPref.getSummary().toString()
						.equalsIgnoreCase(getResources().getString(R.string.textview_essay))) {

					if (nmbPages.getSummary().toString()
							.equalsIgnoreCase(getResources().getString(R.string.preference_not_selected))) {
						Toast toast = Toast.makeText(getApplicationContext(),
								getResources().getString(R.string.toast_number_pages_choose),
								Toast.LENGTH_SHORT);
						toast.show();
						errorFlag = true;
					}
					if (typePref.getSummary().toString()
							.equalsIgnoreCase(getResources().getString(R.string.preference_not_selected))) {
						Toast toast = Toast.makeText(getApplicationContext(),
								getResources().getString(R.string.toast_essay_type_choose),
								Toast.LENGTH_SHORT);
						toast.show();
						errorFlag = true;
					}
					if (nmbRefs.getSummary().toString()
							.equalsIgnoreCase(getResources().getString(R.string.preference_not_selected))) {
						Toast toast = Toast.makeText(getApplicationContext(),
								getResources().getString(R.string.toast_number_refs_choose),
								Toast.LENGTH_SHORT);
						toast.show();
						errorFlag = true;
					}
					if (crStyle.getSummary().toString()
							.equalsIgnoreCase(getResources().getString(R.string.preference_not_selected))) {
						Toast toast = Toast.makeText(getApplicationContext(),
								getResources().getString(R.string.toast_cr_style_choose),	
								Toast.LENGTH_SHORT);
						toast.show();
						errorFlag = true;
					}

				}
				if (levelPref.getSummary().toString()
						.equalsIgnoreCase(getResources().getString(R.string.preference_not_selected))) {

					Toast toast = Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.toast_level_choose),
							Toast.LENGTH_SHORT);
					toast.show();
					errorFlag = true;
				}

				if (taskText.getText().toString().equalsIgnoreCase("")) {
					Toast toast = Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.toast_task_choose),
							Toast.LENGTH_SHORT);
					toast.show();
					errorFlag = true;
				}

				if (errorFlag == false) {
					boolean isOnline = new FrequentlyUsedMethods(
							AssignmentPref.this).isOnlineOrderSend();
					if (isOnline) {
						OrderPreferences.getInstance().getArrayList()[7] = orderTitle
								.getText().toString();
						OrderPreferences.getInstance().getArrayList()[8] = deadlineCus
								.getSummary();
						OrderPreferences.getInstance().getArrayList()[9] = taskText
								.getText().toString();
						OrderPreferences.getInstance().getArrayList()[10] = timezonePref
								.getSummary();
						OrderPreferences.getInstance().getArrayList()[12] = Integer
								.toString(explanationReqInt);
						if (exclVideoInt == 1)
							assignment_video = 1;
						else if (commVideoInt == 1)
							assignment_video = 2;
						else
							assignment_video = 0;
						OrderPreferences.getInstance().getArrayList()[13] = Integer
								.toString(assignment_video);
						Log.i("checked video", Integer.toString(assignment_video));
						Intent intent = new Intent(Constants.TAB_CHANGE_ORDER);
						Bundle bundle = new Bundle();
						bundle.putString("newOrder", "success");
						intent.putExtras(bundle);
						sendBroadcast(intent);
						preferencesValuesDelete();

					} else {
						AlertDialog.Builder alt_bld = new AlertDialog.Builder(
								AssignmentPref.this);
						alt_bld.setMessage(
								getResources().getString(R.string.error_connection_lost))
								.setCancelable(false)
								.setPositiveButton("Try again",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												btnSubmitOrder.performClick();
											}
										})
								.setNegativeButton("Quit",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// Action for 'NO' Button

												preferencesValuesDelete();
												Intent intent = new Intent(
														Constants.TAB_CHANGE_ORDER);
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
		fileCheckHead.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					for (Integer i : checks) {
						checks.set(checks.indexOf(i), 1);
					}
					for (int i = 0; i < customfileList.getCount(); i++) {

						View a = customfileList.getChildAt(i);
						CheckBox b = (CheckBox) a.findViewById(R.id.fileCheck);
						b.setChecked(true);
					}
					btnFilesRemove.setEnabled(true);
				} else {
					for (Integer i : checks) {
						checks.set(checks.indexOf(i), 0);
					}
					for (int i = 0; i < customfileList.getCount(); i++) {

						View a = customfileList.getChildAt(i);
						CheckBox b = (CheckBox) a.findViewById(R.id.fileCheck);
						b.setChecked(false);
					}
					btnFilesRemove.setEnabled(false);

				}

			}

		});

		btnAddPhoto.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent cameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				mOriginalUri = Uri.fromFile(new File(Environment
						.getExternalStorageDirectory(), "original"
						+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
				cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
						mOriginalUri);
				cameraIntent.putExtra("return-data", true);
				startActivityForResult(cameraIntent, 3);
			}
		});

	}

	/** очистка списков настроек заказа **/
	private void preferencesValuesDelete() {

		orderTitle.getText().clear();
		taskText.getText().clear();
		if (productPref.getSummary().toString().equalsIgnoreCase(getResources().getString(R.string.textview_assignment)))
			assignmentFieldsDelete();
		else if (productPref.getSummary().toString().equalsIgnoreCase(getResources().getString(R.string.textview_essay)))
			writingFieldsDelete();
		productPref.setSummary(getResources().getString(R.string.preference_not_selected));
		levelPref.setSummary(getResources().getString(R.string.preference_not_selected));
		subjectPref.setSummary(getResources().getString(R.string.preference_not_selected));
		categoryPref.setSummary(getResources().getString(R.string.preference_not_selected));
		deadlineCus.setSummary(getResources().getString(R.string.preference_not_selected));

		prefEditor.putBoolean("orderPrefDismiss", true);
		mainList.removeFooterView(filesList);

	}

	/** добавление полей в случае выбора типа продукта assignment **/
	public void assignmentFieldsAdd() {
		addPreferencesFromResource(R.xml.new_assign);
		dtlInfo = (CustomCheckBoxPref) getPreferenceScreen().findPreference(
				"dtlInfo");
		ExcVideo = (CustomCheckBoxPref) getPreferenceScreen().findPreference(
				"ExcVideo");
		commVideo = (CustomCheckBoxPref) getPreferenceScreen().findPreference(
				"commVideo");
		dtlInfo.setTitle("Detailed explanation required");
		// dtlInfo.setTextSize(9);
		ExcVideo.setTitle("Shoot exclusive video");
		// ExcVideo.setTextSize(9);
		commVideo.setTitle("Shoot common video");
		// commVideo.setTextSize(9);
		dtlInfo.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				boolean checked = Boolean.valueOf(newValue.toString());
				Log.i("dtlInfo value", Boolean.toString(checked));
				if (checked)
					explanationReqInt = 1;
				else
					explanationReqInt = 0;

				Log.i("dtlInfo value", Integer.toString(explanationReqInt));
				return true;

			}
		});
		ExcVideo.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				boolean checked = Boolean.valueOf(newValue.toString());
				if (checked)
					{
						exclVideoInt = 1;
						commVideoInt = 0;
						commVideo.setChecked(false);
					}
				else
					{
						exclVideoInt = 0;
					}
				Log.i("checked exc video", Integer.toString(exclVideoInt));

				return true;
			}
		});
		commVideo
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						boolean checked = Boolean.valueOf(newValue.toString());
						Log.i("checked state", Boolean.toString(checked));
						if (checked)
							{
								commVideoInt = 1;
								exclVideoInt = 0;
								ExcVideo.setChecked(false);
							}
						else
							{
								commVideoInt = 0;
							}
						Log.i("checked comm video", Integer.toString(commVideoInt));
						return true;
					}
				});

	}

	/** удаление полей в случае выбора типа продукта assignment **/
	public void assignmentFieldsDelete() {

		CheckBoxPreference a = (CheckBoxPreference) getPreferenceScreen()
				.findPreference("dtlInfo");
		CheckBoxPreference b = (CheckBoxPreference) getPreferenceScreen()
				.findPreference("ExcVideo");
		CheckBoxPreference c = (CheckBoxPreference) getPreferenceScreen()
				.findPreference("commVideo");

		a.setChecked(false);
		b.setChecked(false);
		c.setChecked(false);
		explanationReqInt = 0;
		exclVideoInt = 0;
		commVideoInt = 0;

		getPreferenceScreen().removePreference(
				getPreferenceScreen().findPreference("dtlInfo"));
		getPreferenceScreen().removePreference(
				getPreferenceScreen().findPreference("ExcVideo"));
		getPreferenceScreen().removePreference(
				getPreferenceScreen().findPreference("commVideo"));

	}

	/** добавление полей в случае выбора типа продукта writing **/
	public void writingFieldsAdd() {
		addPreferencesFromResource(R.xml.new_writing);
	}

	public void writingFieldsDelete() {

		getPreferenceScreen().removePreference(
				getPreferenceScreen().findPreference("nmbPages"));
		getPreferenceScreen().removePreference(
				getPreferenceScreen().findPreference("typePref"));
		getPreferenceScreen().removePreference(
				getPreferenceScreen().findPreference("crStyle"));
		getPreferenceScreen().removePreference(
				getPreferenceScreen().findPreference("nmbRefs"));

	}

	public boolean getTrigger() {
		return this.trigger;
	}

	public void setTrigger(boolean trigger) {
		this.trigger = trigger;
	}

	/** добавление списка файлов **/
	public void addFiles() {

		if (mainList.getAdapter() != null) {
			mainList.removeFooterView(assignfooter);
		}

		adapter = new ArrayAdapter<File>(this, R.layout.assign_footer2,
				R.id.fileCheck, FileManagerActivity.getFinalAttachFiles()) {
			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.file, null);
				view.setFocusable(false);
				RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				CustomTextView textView = (CustomTextView) view
						.findViewById(android.R.id.title);
				CustomTextView fileSize = (CustomTextView) view
						.findViewById(R.id.fileSize);
				final CheckBox checkBox = (CheckBox) view
						.findViewById(R.id.fileCheck);
				textView.setClickable(true);

				textView.setText(FileManagerActivity.getFinalAttachFiles()
						.get(position).getName().toString());

				fileSize.setText(Long.toString(FileManagerActivity
						.getFinalAttachFiles().get(position).length() / 1024)
						+ " KB");
				for (int i = 0; i < FileManagerActivity.getFinalAttachFiles()
						.size(); i++) {
					textView.setTag(i);
				}
				view.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (!getTrigger())
							setTrigger(true);
						else
							setTrigger(false);
						checkBox.setChecked(getTrigger());
						Log.i("checkable state",
								Boolean.toString(checkBox.isChecked()));
						if (checkBox.isChecked()) {
							checks.set(position, 1);
							btnFilesRemove.setEnabled(true);
						} else {
							checks.set(position, 0);
							int sum = 0;
							for (int k = 0; k < checks.size(); k++) {
								sum += checks.get(k).intValue();
							}
							if (sum == 0)
								btnFilesRemove.setEnabled(false);
						}
					}
				});
				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							checks.set(position, 1);
							btnFilesRemove.setEnabled(true);
						} else {
							checks.set(position, 0);
							int sum = 0;
							for (int k = 0; k < checks.size(); k++) {
								sum += checks.get(k).intValue();
							}
							if (sum == 0)
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
		if (!prefs.getAll().isEmpty()) {
			Log.i("AssPref", "im in pref reload");
			String LevelsValue = prefs.getString("LevelsValue", null);
			String CategoriesValue = prefs.getString("CategoriesValue", null);
			String SubjectsValue = prefs.getString("SubjectsValue", null);
			String ProductsValue = prefs.getString("ProductValue", null);
			prefs.getString("prefValues", null);

			CharSequence charTitle = prefs.getString("orderTitle", null);
			CharSequence charTask = prefs.getString("taskReq", null);
			CharSequence charDeadline = prefs.getString("deadline", null);

			if (charTask != null)
				taskText.setText(charTask);

			if (charTitle != null) {
				orderTitle.setText(charTitle);
			} else
				orderTitle.setHint("Order title(specify)");

			if (ProductsValue != null)
				productPref.setSummary(ProductsValue.toString());

			if (charDeadline != null) {
				deadlineCus.setSummary(charDeadline.toString());
			} else
				deadlineCus.setSummary(getResources().getString(R.string.preference_not_selected));

			if (LevelsValue != null)
				levelPref.setSummary(LevelsValue.toString());
			if (CategoriesValue != null) {
				categoryPref.setSummary(CategoriesValue.toString());
				categoryPref.setEnabled(true);
			}
			if (SubjectsValue != null)
				subjectPref.setSummary(SubjectsValue.toString());
		} else {
			Log.i("asss", "prefs is empty");
		}

	}

	/** сохранение выбранных настроек **/
	public void savePreferences() {
		Iterator it = prefs.getAll().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());
			it.remove(); // avoids a ConcurrentModificationException
		}
		Log.i("AssignmentPRef", "im in savePrefs");
		prefEditor.putString("LevelsValue", levelPref.getSummary().toString());
		prefEditor.putString("CategoriesValue", categoryPref.getSummary()
				.toString());
		prefEditor.putString("SubjectsValue", subjectPref.getSummary()
				.toString());
		prefEditor.putString("ProductValue", productPref.getSummary()
				.toString());

		prefEditor.putString("taskReq", taskText.getText().toString());
		prefEditor.putString("orderTitle", orderTitle.getText().toString());
		prefEditor.putString("deadline", deadlineCus.getSummary().toString());

		prefEditor.putInt("detailedExp", explanationReqInt);
		prefEditor.putInt("exclVideo", exclVideoInt);
		prefEditor.putInt("commonVideo", commVideoInt);

		JSONObject user = new JSONObject();
		for (int i = 0; i < OrderPreferences.getInstance().getArrayList().length; i++) {
			try {
				if (OrderPreferences.getInstance().getArrayList()[i] != null)
					user.put(Integer.toString(i), OrderPreferences
							.getInstance().getArrayList()[i].toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		prefEditor.putString("prefValues", user.toString());
		prefEditor.commit();

	}

	/** OnLongClickListener для списка файлов **/
	OnLongClickListener filesClicklistener = new OnLongClickListener() {
		public boolean onLongClick(View arg0) {
			final CharSequence[] items = { "Open", "Delete", "Details" };
			final AlertDialog.Builder builder = new AlertDialog.Builder(
					AssignmentPref.this);
			builder.setTitle((((CustomTextView) arg0
					.findViewById(android.R.id.title))).getText().toString());
			final int pos = getFilePosition(arg0);
			((CustomTextView) arg0.findViewById(android.R.id.title)).getTag();
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					if (item == 0) {
						File file = FileManagerActivity.getFinalAttachFiles()
								.get(pos);
						Intent intent = new Intent();
						intent.setAction(android.content.Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.fromFile(file), "text/plain");
						startActivity(intent);

					} else if (item == 1) {

						if (FileManagerActivity.getFinalAttachFiles().size() == 1) {
							FileManagerActivity.getFinalAttachFiles().clear();
							adapter.clear();
							adapter.notifyDataSetChanged();
							mainList.removeFooterView(filesList);
							btnSelectFiles.setText(getResources().getString(R.string.btn_add_files));
						} else {
							try {

								adapter.remove(FileManagerActivity
										.getFinalAttachFiles().get(pos));
								textHead.setText(Integer
										.toString(FileManagerActivity
												.getFinalAttachFiles().size())
										+ " files attached");
								long wholeSize = 0;
								for (File file : FileManagerActivity
										.getFinalAttachFiles()) {
									wholeSize += file.length();
								}
								fileSizeHead.setText(Long
										.toString(wholeSize / 1024) + " KB");
								adapter.notifyDataSetChanged();
							} catch (IndexOutOfBoundsException e) {
								e.printStackTrace();
							}

						}
					} else if (item == 2) {
						File file = FileManagerActivity.getFinalAttachFiles()
								.get(pos);
						Log.i("file position in new Order details",
								Integer.toString(pos));
						AlertDialog.Builder builder2 = new AlertDialog.Builder(
								AssignmentPref.this);
						builder2.setTitle(file.getName());
						FileInputStream fis;
						try {
							fis = new FileInputStream(file);
							builder2.setMessage("Size of file is: "
									+ Long.toString(fis.getChannel().size() / 1024)
									+ "  KB" + "\r\n" + "Path of file is: "
									+ "\r\n" + file.getPath());

						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
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

	/** метод для получения позиции файла в списке по нажимаемому View **/
	public int getFilePosition(View v) {
		int pos = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			Log.i("fileList item", adapter.getItem(i).getName());
			if (((CustomTextView) v.findViewById(android.R.id.title)).getText()
					.equals((adapter.getItem(i).getName())))
				pos = i;
		}
		return pos;
	}

	/** TimePickerDialog для времени **/
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
			Toast.makeText(getApplicationContext(), "current time" + Integer.toString(hourOfDay) +  " " +Integer.toString(minuteOfHour), Toast.LENGTH_SHORT ).show();
			// the new set time can't be lower than current
			Calendar newDate = Calendar.getInstance();
			newDate.set(mYear, mMonth, mDay, hourOfDay, minuteOfHour);
			if (newDate.after(c)) {
				mHour = hourOfDay;
				mMinute = minuteOfHour;
				updateDisplay();
			} else {
				showDialog(DATE_DIALOG_ID);
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.toast_deadline_time_current),
						Toast.LENGTH_LONG).show();
			}

		}
	};
	
	/** OnDateSetListener для даты **/
	DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// the new set date can't be lower than current
			Calendar newDate = Calendar.getInstance();
			newDate.set(year, monthOfYear, dayOfMonth);
			
			if (newDate.after(c)) {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				showDialog(TIME_DIALOG_ID);
//				updateDisplay();
			} else {
				
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.toast_deadline_date_current),
						Toast.LENGTH_LONG).show();
				if (! deadlineCus.getSummary().equalsIgnoreCase(getResources().getString(R.string.preference_not_selected)))
					deadlineCus.setSummary(getResources().getString(R.string.preference_not_selected));
			}
			
		}
		
	};

	private List<Subject> subjectsListSpinner;
	private List<Level> levelsListSpinner;
	private List<EssayCreationStyle> essayCreationStyleListSpinner;
	private List<EssayType> essayTypeListSpinner;
	private boolean datePickerDialog_visible;

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			
			final DatePickerDialog datePicker;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				    // (picker is a DatePicker- set current time as min time.
					Toast.makeText(getApplicationContext(), "im higher than HONEYCOMB", Toast.LENGTH_SHORT).show();
					datePicker = new DatePickerDialog(this, null, mYear, mMonth,
							mDay);
					datePicker.setCancelable(true);
					datePicker.setCanceledOnTouchOutside(true);
					datePicker.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
					        new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									Toast.makeText(getApplicationContext(), "im before chackdate" , Toast.LENGTH_SHORT).show();
									checkChoosedDate(datePicker.getDatePicker().getYear(), datePicker.getDatePicker().getMonth(), datePicker.getDatePicker().getDayOfMonth());
								}
					        });
					datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", 
					        new DialogInterface.OnClickListener() {
					            public void onClick(DialogInterface dialog, int which) {
					            	
					            }
					        });
				   datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
				}
				else 
				{	
					Toast.makeText(getApplicationContext(), "im lower than HONEYCOMB", Toast.LENGTH_SHORT).show();
					datePicker = new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
							mDay);
				}
				
			return datePicker;

		case TIME_DIALOG_ID:
			c = (GregorianCalendar) Calendar.getInstance();
			mHour = c.get(Calendar.HOUR_OF_DAY);
			mMinute = c.get(Calendar.MINUTE);
			Toast.makeText(getApplicationContext(), "current time" + Integer.toString(mHour) +  " " +Integer.toString(mMinute), Toast.LENGTH_SHORT ).show();
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					true);
		}
		return null;
	}
	
	
	// update timePicker date during the casting )
	protected void onPrepareDialog(int id, Dialog dialog) {
	      super.onPrepareDialog(id, dialog);
	      if (id == TIME_DIALOG_ID) {
	    	  c = (GregorianCalendar) Calendar.getInstance();
				mHour = c.get(Calendar.HOUR_OF_DAY);
				mMinute = c.get(Calendar.MINUTE);
	        ((TimePickerDialog)dialog).updateTime(mHour, mMinute);
	      }
	    }

	private void updateDisplay() {
		String minutes = null;
		String month = null;
		String hour = null;
		String day = null;

		if (mMinute > 9)
			minutes = Integer.toString(mMinute);
		else
			minutes = "0" + Integer.toString(mMinute);

		if (mHour > 9)
			hour = Integer.toString(mHour);
		else
			hour = "0" + Integer.toString(mHour);

		if (mMonth >= 9)
			month = Integer.toString(mMonth + 1);
		else
			month = "0" + Integer.toString(mMonth + 1);

		if (mDay > 9)
			day = Integer.toString(mDay);
		else
			day = "0" + Integer.toString(mDay);

		deadlineCus.setSummary((new StringBuilder()
				// Month is 0 based so add 1
				.append(mYear).append("-").append(month).append("-")
				.append(day).append(" ").append(hour).append(":")
				.append(minutes).append(":00")).toString());

		
//		showDialog(TIME_DIALOG_ID);
	}

	/** метод добавления тем заказа **/
	public void addSubjects() {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		try {
			Dao<Subject, Integer> daoSubject = db.getSubjectDao();
			subjectsListSpinner = daoSubject.queryForAll();
			subjectDataAdapter = new ArrayAdapter<Subject>(this,
					android.R.layout.simple_spinner_item, subjectsListSpinner);
			subjectDataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			CharSequence[] entries = new CharSequence[subjectDataAdapter
					.getCount()];
			CharSequence[] entryValues = new CharSequence[subjectDataAdapter
					.getCount()];
			int i = 0;
			for (Subject dev : subjectsListSpinner) {
				entries[i] = dev.getSubjectTitle();
				entryValues[i] = Integer.toString(dev.getSubjectId());
				i++;
			}

			subjectPref.setEntries(entries);
			subjectPref.setEntryValues(entryValues);

		} catch (SQLException e) {

			e.printStackTrace();
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/** метод добавления категорий заказа **/
	public void addCategories(int index) {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		int _dataBaseindex = 0;
		try {

			Dao<Category, Integer> daoCategory = db.getCategoryDao();
			daoCategory.queryBuilder();
			// try {
			//
			// Dao<Subject,Integer> daoSubject = db.getSubjectDao();
			// daoCategory.q
			// }
			// catch(Exception e){e.printStackTrace();}

			final List<Category> categories = daoCategory.queryBuilder()
					.where().eq("subject_id", index).query();// Integer.parseInt(subjectPref.getEntry().toString())).query();

			categoryDataAdapter = new ArrayAdapter<Category>(this,
					android.R.layout.simple_spinner_item, categories);
			categoryDataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			CharSequence[] entries = new CharSequence[categoryDataAdapter
					.getCount()];
			CharSequence[] entryValues = new CharSequence[categoryDataAdapter
					.getCount()];
			int i = 0;
			for (Category dev : categories) {
				entries[i] = dev.getCategoryTitle();
				entryValues[i] = Integer.toString(dev.getCategoryId());
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

	/** метод добавления уровней заказа **/
	public void addLevels() {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		try {
			Dao<Level, Integer> daoSubject = db.getLevelDao();
			levelsListSpinner = daoSubject.queryForAll();
			levelDataAdapter = new ArrayAdapter<Level>(this,
					android.R.layout.simple_spinner_item, levelsListSpinner);
			levelDataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			CharSequence[] entries = new CharSequence[levelDataAdapter
					.getCount()];
			CharSequence[] entryValues = new CharSequence[levelDataAdapter
					.getCount()];
			int i = 0;
			for (Level dev : levelsListSpinner) {
				entries[i] = dev.getLevelTitle();
				entryValues[i] = Integer.toString(dev.getLevelId());
				Log.i("subject items",
						entries[i].toString() + entryValues[i].toString());
				i++;

			}
			levelPref.setEntries(entries);
			levelPref.setEntryValues(entryValues);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/** метод добавления временных зон **/
	public void addTimeZones() {
		//
		final String[] TZ = TimeZone.getAvailableIDs();

		Log.i("count", Integer.toString(TZ.length));
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, R.layout.dialog_list_item, R.id.tv);
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		final ArrayList<String> TZ1 = new ArrayList<String>();
		for (int i = 0; i < TZ.length; i++) {
			if (!(TZ1.contains(TimeZone.getTimeZone(TZ[i]).getID()))) {
				if (new FrequentlyUsedMethods(AssignmentPref.this)
						.timezoneValidate(TZ[i]))
					TZ1.add(TZ[i]);
			}
		}
		for (int i = 0; i < TZ1.size(); i++) {

			adapter.add(TZ1.get(i));
		}

		CharSequence[] entries = new CharSequence[adapter.getCount()];
		CharSequence[] entryValues = new CharSequence[adapter.getCount()];
		int i = 0;
		for (String dev : TZ1) {
			entries[i] = dev;
			entryValues[i] = dev;
			if (TimeZone.getTimeZone(TZ1.get(i)).getID()
					.equals(TimeZone.getDefault().getID())) {
				timezonePref.setSummary((TimeZone.getDefault().getID()));
				Log.i("current timezone in a loop", TZ1.get(i));
			}
			i++;

		}

		Locale locale = new Locale("en", "IN");
		String curr = TimeZone.getTimeZone(TimeZone.getDefault().getID())
				.getDisplayName(false, TimeZone.SHORT, locale);
		String currTimeZone = "";
		for (int q = 0; q < TZ1.size(); q++) {
			if (TimeZone.getTimeZone(TZ1.get(q))
					.getDisplayName(false, TimeZone.SHORT, locale).equals(curr))
				currTimeZone = TZ1.get(q);
		}
		timezonePref.setSummary(currTimeZone);

	}

	/** метод добавления списка стилей создания essay **/
	public void addEssayCreationStyles() {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		try {
			Dao<EssayCreationStyle, Integer> daoSubject = db
					.getEssayCreationStyleDao();
			essayCreationStyleListSpinner = daoSubject.queryForAll();
			essayCrDataAdapter = new ArrayAdapter<EssayCreationStyle>(this,
					android.R.layout.simple_spinner_item,
					essayCreationStyleListSpinner);
			essayCrDataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			CharSequence[] entries = new CharSequence[essayCrDataAdapter
					.getCount()];
			CharSequence[] entryValues = new CharSequence[essayCrDataAdapter
					.getCount()];
			int i = 0;
			for (EssayCreationStyle dev : essayCreationStyleListSpinner) {
				entries[i] = dev.getECSTitle();
				entryValues[i] = Integer.toString(dev.getECSId());
				i++;

			}
			crStyle = (IconPreference) findPreference("crStyle");
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

	/** метод добавления списка количества страниц **/
	public void addNumberPages() {
		try {

			nmbPages = (IconPreference) findPreference("nmbPages");
			Collections.sort(LoginAsync.numberPagesList,
					new Comparator<NumberPages>() {
						private int fixString(NumberPages in) {
							int res = 0;
							if (in.getNumberPage().length() > 2) {
								if (numbersCheck(in.getNumberPage()))
									res = Integer.parseInt((in.getNumberPage())
											.substring(0, in.getNumberPage()
													.indexOf('_')));
							} else {
								if (numbersCheck(in.getNumberPage()))
									res = Integer.parseInt((in.getNumberPage()));
							}
							return res;
						}

						public int compare(NumberPages lhs, NumberPages rhs) {

							int res = fixString(lhs) - fixString(rhs);
							if (res == 0) {
								if (lhs.getNumberPage().length() < rhs
										.getNumberPage().length())
									return -1;
								else
									return 0;
							} else if (res > 0)
								return 1;
							else
								return -1;
						}
					});
		} catch (StringIndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		nmbPageDataAdapter = new ArrayAdapter<NumberPages>(this,
				android.R.layout.simple_spinner_item,
				LoginAsync.numberPagesList);
		nmbPageDataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		CharSequence[] entries = new CharSequence[nmbPageDataAdapter.getCount()];
		CharSequence[] entryValues = new CharSequence[nmbPageDataAdapter
				.getCount()];
		int i = 0;
		for (NumberPages dev : LoginAsync.numberPagesList) {
			entries[i] = dev.getNumberPage();
			entryValues[i] = dev.getNumberPage();
			Log.i("subject items",
					entries[i].toString() + entryValues[i].toString());
			i++;

		}
		nmbPages.setEntries(entries);
		nmbPages.setEntryValues(entryValues);
		nmbPages.setOnPreferenceChangeListener(nmbPagesListener);
	}

	/** метод добавления списка количества ссылочных материалов **/
	public void addNumberReferences() {
		nmbRefs = (IconPreference) findPreference("nmbRefs");
		Collections.sort(LoginAsync.numberReferencesList,
				new Comparator<NumberOfReferences>() {
					private int fixString(NumberOfReferences in) {
						int res = 0;

						if (in.getNumberReference().length() > 2) {
							if (numbersCheck(in.getNumberReference()))
								res = Integer.parseInt((in.getNumberReference())
										.substring(0, in.getNumberReference()
												.indexOf('_')));
						} else {
							if (numbersCheck(in.getNumberReference()))
								res = Integer.parseInt((in.getNumberReference()));
						}

						return res;
					}

					public int compare(NumberOfReferences lhs,
							NumberOfReferences rhs) {

						int res = fixString(lhs) - fixString(rhs);
						if (res == 0) {
							if (lhs.getNumberReference().length() < rhs
									.getNumberReference().length())
								return -1;
							else
								return 0;
						} else if (res > 0)
							return 1;
						else
							return -1;
					}

				});
		nmbRefsDataAdapter = new ArrayAdapter<NumberOfReferences>(this,
				android.R.layout.simple_spinner_item,
				LoginAsync.numberReferencesList);
		nmbRefsDataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		CharSequence[] entries = new CharSequence[nmbRefsDataAdapter.getCount()];
		CharSequence[] entryValues = new CharSequence[nmbRefsDataAdapter
				.getCount()];
		int i = 0;
		for (NumberOfReferences dev : LoginAsync.numberReferencesList) {
			entries[i] = dev.getNumberReference();
			entryValues[i] = dev.getNumberReference();
			i++;

		}
		nmbRefs.setEntries(entries);
		nmbRefs.setEntryValues(entryValues);
		nmbRefs.setOnPreferenceChangeListener(nmbRefsListener);

	}

	/** метод добавления списка типов essay **/
	public void addEssayTypes() {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		try {
			typePref = (IconPreference) findPreference("typePref");
			Dao<EssayType, Integer> daoSubject = db.getEssayTypeDao();
			essayTypeListSpinner = daoSubject.queryForAll();
			essayTypeDataAdapter = new ArrayAdapter<EssayType>(this,
					android.R.layout.simple_spinner_item, essayTypeListSpinner);
			essayTypeDataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			CharSequence[] entries = new CharSequence[essayTypeDataAdapter
					.getCount()];
			CharSequence[] entryValues = new CharSequence[essayTypeDataAdapter
					.getCount()];
			int i = 0;
			for (EssayType dev : essayTypeListSpinner) {
				entries[i] = dev.getEssayTypeTitle();
				entryValues[i] = Integer.toString(dev.getEssayTypeId());
				Log.i("subject items",
						entries[i].toString() + entryValues[i].toString());
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

	/** метод проверки или строка состоит исключетельно из цифр **/
	boolean numbersCheck(String email) {
		Pattern pattern = Pattern.compile("^[0-9]{1,2}.*");
		Matcher matcher = pattern.matcher(email);
		boolean matchFound = matcher.matches();
		return matchFound;
	}

	/** OnPreferenceChangeListener для стилей создания essay **/
	public OnPreferenceChangeListener crStyleListener = new OnPreferenceChangeListener() {

		public boolean onPreferenceChange(Preference preference, Object newValue) {
			int index = crStyle.findIndexOfValue(newValue.toString());
			crStyle.setSummary(crStyle.getEntries()[index]);
			EssayCreationStyle a = essayCrDataAdapter.getItem(index);
			OrderPreferences.getInstance().getArrayList()[6] = Integer
					.toString(a.getECSId());
			return false;
		}
	};
	/** OnPreferenceChangeListener для количества страниц **/
	public OnPreferenceChangeListener nmbPagesListener = new OnPreferenceChangeListener() {

		public boolean onPreferenceChange(Preference preference, Object newValue) {
			int index = nmbPages.findIndexOfValue(newValue.toString());
			nmbPages.setSummary(nmbPages.getEntries()[index]);
			NumberPages a = nmbPageDataAdapter.getItem(index);
			OrderPreferences.getInstance().getArrayList()[3] = a
					.getNumberPage();
			return false;
		}
	};

	/** OnPreferenceChangeListener для количества страниц essay **/
	public OnPreferenceChangeListener nmbRefsListener = new OnPreferenceChangeListener() {

		public boolean onPreferenceChange(Preference preference, Object newValue) {
			int index = nmbRefs.findIndexOfValue(newValue.toString());
			nmbRefs.setSummary(nmbRefs.getEntries()[index]);
			NumberOfReferences a = nmbRefsDataAdapter.getItem(index);
			OrderPreferences.getInstance().getArrayList()[4] = a
					.getNumberReference();
			return false;
		}
	};
	/** OnPreferenceChangeListener для типов essay **/
	public OnPreferenceChangeListener typePrefListener = new OnPreferenceChangeListener() {

		public boolean onPreferenceChange(Preference preference, Object newValue) {
			int index = typePref.findIndexOfValue(newValue.toString());
			typePref.setSummary(typePref.getEntries()[index]);
			EssayType a = essayTypeDataAdapter.getItem(index);
			OrderPreferences.getInstance().getArrayList()[5] = Integer
					.toString(a.getEssayTypeId());
			return false;
		}
	};

	/**
	 * метод изменения заголовка списка файлов при удалении/добавлении файлов из
	 * списка
	 **/
	private void changeFileHeafer() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.file, null);
		view.setFocusable(false);
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

		textHead.setTextSize(17);
		textHead.setText(Integer.toString(FileManagerActivity
				.getFinalAttachFiles().size()) + " files attached");
		long wholeSize = 0;
		for (File file : FileManagerActivity.getFinalAttachFiles()) {
			wholeSize += file.length();
		}
		fileSizeHead.setText(Long.toString(wholeSize / 1024) + " KB");
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("AssignmentPref requestCode", Integer.toString(requestCode));
		Log.i("AssignmentPref resultCode", Integer.toString(resultCode));
		switch (resultCode) {
		case Constants.addFilesResult:

			if (adapter == null)
				addFiles();
			else if (adapter.getCount() == 0)
				addFiles();
			else {
				adapter.notifyDataSetChanged();
				changeFileHeafer();
			}
			btnFilesRemove.setVisibility(View.VISIBLE);
			for (int b = 0; b < FileManagerActivity.getFinalAttachFiles()
					.size(); b++) {
				checks.add(b, 0);
			}
		}

		if (requestCode == 3) {
			Log.d("path result", Integer.toString(resultCode));
			if (resultCode != RESULT_OK)
				return;
			Log.d("path", mOriginalUri.getPath());
			File imgFile = new File(mOriginalUri.getPath());
			Log.i("file size", imgFile.getAbsolutePath());
			if (imgFile.exists()) {

				FileManagerActivity.getFinalAttachFiles().add(imgFile);
				addFiles();
				// adapter.notifyDataSetChanged();

			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		c = (GregorianCalendar) Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		categoryPref.setEnabled(false);
		if (prefs.getBoolean("orderPrefDismiss", false) == true)
			getListView().setSelection(0);
		if (FileManagerActivity.getFinalAttachFiles().isEmpty()) {
			mainList.removeFooterView(filesList);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (FileManagerActivity.getFinalAttachFiles().isEmpty()) {
			if (adapter != null)
				Log.i("adapter size", Integer.toString(adapter.getCount()));
		}

	}
	private void disableCategory()
	{
		categoryPref.setEnabled(false);
		categoryPref.setIcon(this.getResources().getDrawable(R.drawable.list_pointer_unselected));
	}
	private void enableCategory()
	{
		categoryPref.setEnabled(true);
		categoryPref.setIcon(this.getResources().getDrawable(R.drawable.list_pointer));
	}
	
	
	private void checkChoosedDate(int year, int monthOfYear, int dayOfMonth)
	{
		Toast.makeText(getApplicationContext(), "im in chackdate" + Integer.toString(year) + " " +  Integer.toString(monthOfYear) + " "+ 
				 Integer.toString(dayOfMonth) + " ", Toast.LENGTH_SHORT).show();
		// the new set date can't be lower than current
					Calendar newDate = Calendar.getInstance();
					newDate.set(year, monthOfYear, dayOfMonth);
					
					if (newDate.after(c)) {
						mYear = year;
						mMonth = monthOfYear;
						mDay = dayOfMonth;
						showDialog(TIME_DIALOG_ID);
//						updateDisplay();
					} else {
						
						Toast.makeText(getApplicationContext(),
								getResources().getString(R.string.toast_deadline_date_current),
								Toast.LENGTH_LONG).show();
						if (! deadlineCus.getSummary().equalsIgnoreCase(getResources().getString(R.string.preference_not_selected)))
							deadlineCus.setSummary(getResources().getString(R.string.preference_not_selected));
					}
	}
}
