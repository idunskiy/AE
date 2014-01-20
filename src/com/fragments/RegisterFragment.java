package com.fragments;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.arrayadapters.CountryAdapter;
import com.assignmentexpert.R;
import com.customitems.CustomTextView;
import com.datamodel.CountryInfo;
import com.fragmentactivities.RegisterFragmentActivity;
import com.library.FrequentlyUsedMethods;
import com.library.UserFunctions;
import com.library.singletones.SharedPrefs;

/** * фрагмент для регистрации нового пользователя*/
/** * фрагмент для регистрации нового пользователя */
public class RegisterFragment extends Fragment implements IClickListener {
	private static final String TAG = "RegisterFragment";
	/** * кнопка для обработки введенных пользователем данных */
	Button btnProceed;
	/** * EditText для ввода имени пользователя */
	EditText inputFullName;
	/** * EditText для ввода email"a пользователя */
	EditText inputEmail;
	/** * EditText для ввода пароля пользователя */
	EditText inputPassword;
	/** * EditText для подтверждения ввода пароля пользователя */
	EditText confPassword;
	/** * EditText для ввода катчи */
	EditText captchaEdit;

	private CustomTextView btnTermsService;
	private CustomTextView btnPrivatePolicy;
	/**
	 * *статическое поле userName для использования в FragmentActivity для
	 * отправки на сервер
	 */
	public static String userName;
	/**
	 * *статическое поле userEmail для использования в FragmentActivity для
	 * отправки на сервер
	 */
	public static String userEmail;
	/**
	 * *статическое поле пароля пользователя для использования в
	 * FragmentActivity для отправки на сервер
	 */
	public static String userPass;
	/**
	 * *статическое поле подтверждения ввода пароля для использования в
	 * FragmentActivity для отправки на сервер
	 */
	public static String userConf;

	/**
	 * *статическое поле строки каптчи для использования в FragmentActivity для
	 * отправки на сервер
	 */
	public static String userCaptcha;

	public static String userPhone;

	/** *ImageView для отображения каптчи */
	public static ImageView captcha;
	/** * экземпляр интерфейса IClickListener */
	IClickListener listener;

	String name;
	String email;
	String password;
	String confpassword;
	String captchaString;

	String phone;
	String codePhone;

	UserFunctions userFunc = new UserFunctions();

	FrequentlyUsedMethods faq;

	private Spinner registerCodePhone;
	private EditText registerPhone;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.register, container, false);
		inputFullName = (EditText) view.findViewById(R.id.registerName);
		inputEmail = (EditText) view.findViewById(R.id.registerEmail);
		inputPassword = (EditText) view.findViewById(R.id.registerPassword);
		confPassword = (EditText) view.findViewById(R.id.registerPasswordconf);
		captchaEdit = (EditText) view.findViewById(R.id.captcha);

		registerCodePhone = (Spinner) view.findViewById(R.id.registerCodePhone);
		registerPhone = (EditText) view.findViewById(R.id.registerPhone);

		btnProceed = (Button) view.findViewById(R.id.btnProceed);
		btnTermsService = (CustomTextView) view
				.findViewById(R.id.btnTermsService);
		btnPrivatePolicy = (CustomTextView) view
				.findViewById(R.id.btnPrivatePolicy);
		captcha = (ImageView) view.findViewById(R.id.captchaView);
		faq = new FrequentlyUsedMethods(getActivity());

		inputPassword.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// inputPassword.setFocusable(true);
				inputPassword.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				inputPassword.setTextColor(Color.BLACK);
				return false;
			}

		});
		confPassword.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// inputPassword.setFocusable(true);
				confPassword.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				confPassword.setTextColor(Color.BLACK);
				return false;
			}

		});

		inputPassword.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus)
					if (inputPassword.getText().toString().length() == 0)
						inputPassword.setInputType(InputType.TYPE_CLASS_TEXT);
			}
		});

		confPassword.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus)
					if (confPassword.getText().toString().length() == 0)
						confPassword.setInputType(InputType.TYPE_CLASS_TEXT);
			}
		});

		inputEmail.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {

				if (inputEmail.getText().toString()
						.equals("You have to enter correct email"))
					inputEmail.getText().clear();
				inputEmail.setTextColor(Color.BLACK);
				return false;
			}
		});
		inputFullName.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {

				if (inputFullName.getText().toString()
						.equals("At least 2 charachters"))
					inputFullName.getText().clear();
				inputFullName.setTextColor(Color.BLACK);
				return false;
			}
		});

		captchaEdit.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (captchaEdit.getText().toString().equals("Incorrect"))
					captchaEdit.getText().clear();
				captchaEdit.setTextColor(Color.BLACK);
				return false;
			}
		});

		btnTermsService.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String url = "http://www.assignmentexpert.com/terms-and-conditions.html";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
		btnPrivatePolicy.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String url = "http://www.assignmentexpert.com/privacy-policy.html";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		// fillCountryAdapter();
		registerCodePhone
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						Log.i("un choosed item", registerCodePhone
								.getSelectedItem().toString());

					}

				});

		btnProceed.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				name = inputFullName.getText().toString();
				email = inputEmail.getText().toString();
				password = inputPassword.getText().toString();
				confpassword = confPassword.getText().toString();
				captchaString = captchaEdit.getText().toString();

				boolean errorFlag = false;
				if (!faq.EmailValidate(email)) {

					inputEmail.setText("");
					Toast.makeText(
							getActivity(),
							getActivity().getResources().getString(
									R.string.toast_register_wrong_email),
							Toast.LENGTH_SHORT).show();
					errorFlag = true;
				}
				if (inputFullName.length() == 0) {

					inputFullName.setText(" ");
					Toast.makeText(
							getActivity(),
							getActivity().getResources().getString(
									R.string.toast_register_firstname_length),
							Toast.LENGTH_SHORT).show();
					errorFlag = true;
				}
				if (inputPassword.length() < 5) {
					inputPassword.setText("");
					Toast.makeText(
							getActivity(),
							getActivity().getResources().getString(
									R.string.toast_register_password_length),
							Toast.LENGTH_SHORT).show();
					errorFlag = true;
				}
				if (!confpassword.equals(password)) {
					inputPassword.setText("");
					confPassword.setText("");
					Toast.makeText(
							getActivity(),
							getActivity().getResources().getString(
									R.string.toast_register_passwords_equal),
							Toast.LENGTH_SHORT).show();
					errorFlag = true;
				}
				if (captchaEdit.length() != 4) {
					captchaEdit.setText("");
					Toast.makeText(
							getActivity(),
							getActivity().getResources().getString(
									R.string.toast_register_captcha),
							Toast.LENGTH_SHORT).show();
					errorFlag = true;
				}
				if (errorFlag == false) {
					btnProceed.getBackground().setAlpha(255);
					userName = name;
					userEmail = email;
					userPass = password;
					userConf = confpassword;
					userCaptcha = captchaString;

					userPhone = ((CountryInfo) registerCodePhone
							.getSelectedItem()).getCountryCode()
							+ registerPhone.getText().toString();

					clearFields();

					// mehod for saving register data if nested fragments using
					// setSharedPreferences();
					// registration proceeding
					listener.changeFragment(5);
				}

			}

		});
		final View activityRootView = view
				.findViewById(R.id.scrollViewRegister);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					public void onGlobalLayout() {
						int heightDiff = activityRootView.getRootView()
								.getHeight() - activityRootView.getHeight();
						if (heightDiff > 100) { // if more than 100 pixels, its
												// probably a keyboard...
							if (inputPassword.getText().toString().length() != 0
									& inputEmail.getText().toString().length() != 0
									& captchaEdit.getText().toString().length() != 0
									& confPassword.getText().toString()
											.length() != 0
									& inputFullName.getText().toString()
											.length() != 0)
								btnProceed.getBackground().setAlpha(255);
							else
								btnProceed.getBackground().setAlpha(120);
						}
					}
				});

		return view;
	}

	/** * метод для сохранения данных c помощью sharedPreferences */
	private void setSharedPreferences() {
		SharedPrefs.getInstance().Initialize(
				getActivity().getApplicationContext());
		SharedPrefs.getInstance().writeString("registerName", name);
		SharedPrefs.getInstance().writeString("registerEmail", email);
		SharedPrefs.getInstance().writeString("registerPassword", password);
		SharedPrefs.getInstance().writeString("registerConfPass", confpassword);
		SharedPrefs.getInstance().writeString("registerCaptcha", captchaString);

	}

	/** * метод для получения данных c помощью sharedPreferences */
	private void getSharedPreferences() {

		String registerName = SharedPrefs.getInstance().getSharedPrefs()
				.getString("registerName", null);
		String registerEmail = SharedPrefs.getInstance().getSharedPrefs()
				.getString("registerEmail", null);
		String registerPassword = SharedPrefs.getInstance().getSharedPrefs()
				.getString("registerPassword", null);
		String registerConfPass = SharedPrefs.getInstance().getSharedPrefs()
				.getString("registerConfPass", null);
		String registerCaptcha = SharedPrefs.getInstance().getSharedPrefs()
				.getString("registerCaptcha", null);

		try {

			if (registerName != null)
				inputFullName.setText(SharedPrefs.getInstance()
						.getSharedPrefs().getString("registerName", null));
			if (registerEmail != null)
				inputEmail.setText(SharedPrefs.getInstance().getSharedPrefs()
						.getString("registerEmail", null));
			if (registerPassword != null)
				inputPassword.setText(SharedPrefs.getInstance()
						.getSharedPrefs().getString("registerPassword", null));
			if (registerConfPass != null)
				confPassword.setText(SharedPrefs.getInstance().getSharedPrefs()
						.getString("registerConfPass", null));
			if (registerCaptcha != null)
				captchaEdit.setText(SharedPrefs.getInstance().getSharedPrefs()
						.getString("registerCaptcha", null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * * метод для установки обькта Bitmap, полученного с сервера в ImageView
	 * каптчи
	 */
	public static void setImageBitmap(Bitmap data) {
		captcha.setImageBitmap(data);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {

			listener = (IClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	public void changeFragment(int flag) {
		// TODO Auto-generated method stub

	}

	/** * используется, если каптча уже скачана, её установка в ImageView */
	@Override
	public void onResume() {

		super.onResume();
		if (RegisterFragmentActivity.captchaForSave != null)
			captcha.setImageBitmap(RegisterFragmentActivity.captchaForSave);
		getSharedPreferences();

	}

	// method for clearing all the edittexts after registration
	private void clearFields() {
		inputFullName.getText().clear();
		inputEmail.getText().clear();
		inputPassword.getText().clear();
		confPassword.getText().clear();
		captchaEdit.getText().clear();

	}

	private String wrapPhoneCode() {
		Pattern pattern = Pattern.compile("(^[0-9]{0,3})");
		Matcher matcher = pattern.matcher(registerCodePhone.getSelectedItem()
				.toString());
		if (matcher.find()) {
			return matcher.group(1);
		} else
			return null;
	}

	public void fillCountryAdapter()
	{
		Log.i(TAG + "fillCountryAdapter","");
		ArrayList<CountryInfo> countriesList  = new ArrayList<CountryInfo>();
		    			countriesList.add(new CountryInfo("afghanistan", "AF ", "93", R.drawable.ad));
		    			countriesList.add(new CountryInfo("albania", "AL ", "355",R.drawable.af));
		    			 countriesList.add(new CountryInfo("algeria", "DZ ", "213",R.drawable.dz));
		    			 countriesList.add(new CountryInfo("american samoa", "AS ", "e",R.drawable.as));
		    			 countriesList.add(new CountryInfo("andorra", "AD ", "376",R.drawable.ad)); 
		    			 countriesList.add(new CountryInfo("angola", "AO ", "244",R.drawable.ao));
		    			 countriesList.add(new CountryInfo("anguilla", "AI ", "1 264",R.drawable.ai));
		    			 countriesList.add(new CountryInfo("antarctica", "AQ ", "672",R.drawable.aq));
		    			 countriesList.add(new CountryInfo("antigua and barbuda", "AG ", "1 268",R.drawable.ag));
		    			 countriesList.add(new CountryInfo("argentina", "AR ", "54",R.drawable.ar)); 
		    			 countriesList.add(new CountryInfo("armenia", "AM ", "374",R.drawable.am));
		    			 countriesList.add(new CountryInfo("aruba", "AW ", "297",R.drawable.aw)); 
		    			 countriesList.add(new CountryInfo("australia", "AU ", "61",R.drawable.au));
		    			 countriesList.add(new CountryInfo("austria", "AT ", "43",R.drawable.at)); 
		    			 countriesList.add(new CountryInfo("azerbaijan", "AZ ", "994",R.drawable.az));
		    			 countriesList.add(new CountryInfo("bahamas", "BS ", "1 242",R.drawable.bs));
		    			 countriesList.add(new CountryInfo("bahrain", "BH ", "973",R.drawable.bh)); 
		    			 countriesList.add(new CountryInfo("bangladesh", "BD ", "880",R.drawable.bd));
		    			 countriesList.add(new CountryInfo("barbados", "BB ", "1 246",R.drawable.bb)); 
		    			 countriesList.add(new CountryInfo("belarus", "BY ", "375",R.drawable.by));
		    			 countriesList.add(new CountryInfo("belgium", "BE ", "32",R.drawable.be)); 
		    			 countriesList.add(new CountryInfo("belize", "BZ ", "501",R.drawable.bz)); 
		    			 countriesList.add(new CountryInfo("benin", "BJ ", "229",R.drawable.bj)); 
		    			 countriesList.add(new CountryInfo("bermuda", "BM ", "1 441",R.drawable.bm));
		    			 countriesList.add(new CountryInfo("bhutan", "BT ", "975",R.drawable.bt));
		    			 countriesList.add(new CountryInfo("bolivia", "BO ", "591",R.drawable.bo));
		    			 countriesList.add(new CountryInfo("bosnia and herzegovina", "BA ", "387",R.drawable.ba));
		    			 countriesList.add(new CountryInfo("botswana", "BW ", "267",R.drawable.bw)); 
		    			 countriesList.add(new CountryInfo("brazil", "BR ", "55",R.drawable.br)); 
		    			 countriesList.add(new CountryInfo("british indian ocean territory", "IO ", "",R.drawable.io));
		    			 countriesList.add(new CountryInfo("british virgin islands", "VG ", "1 284",R.drawable.vg)); 
		    			 countriesList.add(new CountryInfo("brunei", "BN ", "673",R.drawable.bn));
		    			 countriesList.add(new CountryInfo("bulgaria", "BG ", "359",R.drawable.bg));
		    			 countriesList.add(new CountryInfo("burkina faso", "BF ", "226",R.drawable.bf));
		    			 countriesList.add(new CountryInfo("burma (myanmar)", "MM ", "95",R.drawable.mm)); 
		    			 countriesList.add(new CountryInfo("burundi", "BI ", "257",R.drawable.bi));
		    			 countriesList.add(new CountryInfo("cambodia", "KH ", "855",R.drawable.kh)); 
		    			 countriesList.add(new CountryInfo("cameroon", "CM ", "237",R.drawable.cm)); 
		    			 countriesList.add(new CountryInfo("canada", "CA ", "1",R.drawable.ca)); 
		    			 countriesList.add(new CountryInfo("cape verde", "CV ", "238",R.drawable.cv));
		    			 countriesList.add(new CountryInfo("cayman islands", "KY ", "1 345",R.drawable.ky)); 
		    			 countriesList.add(new CountryInfo("central african republic", "CF ", "236",R.drawable.cf)); 
		    			 countriesList.add(new CountryInfo("chad", "TD ", "235",R.drawable.td));
		    			 countriesList.add(new CountryInfo("chile", "CL ", "56",R.drawable.cl)); 
		    			 countriesList.add(new CountryInfo("china", "CN ", "86",R.drawable.cn)); 
		    			 countriesList.add(new CountryInfo("christmas island", "CX ", "61",R.drawable.cx)); 
		    			 countriesList.add(new CountryInfo("cocos (keeling) islands", "CC ", "61",R.drawable.cc)); 
		    			 countriesList.add(new CountryInfo("colombia", "CO ", "57",R.drawable.co));
		    			 countriesList.add(new CountryInfo("comoros", "KM ", "269",R.drawable.km)); 
		    			 countriesList.add(new CountryInfo("cook islands", "CK ", "682",R.drawable.ck)); 
		    			 countriesList.add(new CountryInfo("costa rica", "CR ", "506",R.drawable.cr));
		    			 countriesList.add(new CountryInfo("croatia", "HR ", "385",R.drawable.hr)); 
		    			 countriesList.add(new CountryInfo("cuba", "CU ", "53",R.drawable.cu)); 
		    			 countriesList.add(new CountryInfo("cyprus", "CY ", "357",R.drawable.cy));
		    			 countriesList.add(new CountryInfo("czech republic", "CZ ", "420",R.drawable.cz));
		    			 countriesList.add(new CountryInfo("democratic republic of the congo", "CD ", "243",R.drawable.cd));
		    			 countriesList.add(new CountryInfo("denmark", "DK ", "45",R.drawable.dk)); 
		    			 countriesList.add(new CountryInfo("djibouti", "DJ ", "253",R.drawable.dj)); 
		    			 countriesList.add(new CountryInfo("dominica", "DM ", "1 767",R.drawable.dm));
		    			 countriesList.add(new CountryInfo("dominican republic", "DO ", "1 809",R.drawable.do1)); 
		    			 countriesList.add(new CountryInfo("ecuador", "EC ", "593",R.drawable.ec)); 
		    			 countriesList.add(new CountryInfo("egypt", "EG ", "20",R.drawable.eg)); 
		    			 countriesList.add(new CountryInfo("el salvador", "SV ", "503",R.drawable.sv)); 
		    			 countriesList.add(new CountryInfo("equatorial guinea", "GQ ", "240",R.drawable.gq));
		    			 countriesList.add(new CountryInfo("eritrea", "ER ", "291",R.drawable.er));
		    			 countriesList.add(new CountryInfo("estonia", "EE ", "372",R.drawable.ee));
		    			 countriesList.add(new CountryInfo("ethiopia", "ET ", "251",R.drawable.et)); 
		    			 countriesList.add(new CountryInfo("falkland islands", "FK ", "500",R.drawable.fk));
		    			 countriesList.add(new CountryInfo("faroe islands", "FO ", "298",R.drawable.fo));
		    			 countriesList.add(new CountryInfo("fiji", "FJ ", "679",R.drawable.fj)); 
		    			 countriesList.add(new CountryInfo("finland", "FI ", "358",R.drawable.fi)); 
		    			 countriesList.add(new CountryInfo("france", "FR ", "33",R.drawable.fr));
		    			 countriesList.add(new CountryInfo("french polynesia", "PF ", "689",R.drawable.pf));
		    			 countriesList.add(new CountryInfo("gabon", "GA ", "241",R.drawable.ga)); 
		    			 countriesList.add(new CountryInfo("gambia", "GM ", "220",R.drawable.gm));
		    			 countriesList.add(new CountryInfo("georgia", "GE ", "995",R.drawable.ge));
		    			 countriesList.add(new CountryInfo("germany", "DE ", "49",R.drawable.de)); 
		    			 countriesList.add(new CountryInfo("ghana", "GH ", "233",R.drawable.gh));
		    			 countriesList.add(new CountryInfo("gibraltar", "GI ", "350",R.drawable.gi));
		    			 countriesList.add(new CountryInfo("greece", "GR ", "30",R.drawable.gr));
		    			 countriesList.add(new CountryInfo("greenland", "GL ", "299",R.drawable.gl));
		    			 countriesList.add(new CountryInfo("grenada", "GD ", "1 473",R.drawable.gd));
		    			 countriesList.add(new CountryInfo("guam", "GU ", "1 671",R.drawable.gu)); 
		    			 countriesList.add(new CountryInfo("guatemala", "GT ", "502",R.drawable.gt));
		    			 countriesList.add(new CountryInfo("guinea", "GN ", "224",R.drawable.gn)); 
		    			 countriesList.add(new CountryInfo("guinea-bissau", "GW ", "245",R.drawable.gw));
		    			 countriesList.add(new CountryInfo("guyana", "GY ", "592",R.drawable.gy));
		    			 countriesList.add(new CountryInfo("haiti", "HT ", "509",R.drawable.ht)); 
		    			 countriesList.add(new CountryInfo("holy see (vatican city)", "VA ", "39",R.drawable.va)); 
		    		     countriesList.add(new CountryInfo("honduras", "HN ", "504",R.drawable.hn)); 
		    			 countriesList.add(new CountryInfo("hong kong", "HK ", "852",R.drawable.hk));
		    			 countriesList.add(new CountryInfo("hungary", "HU ", "36",R.drawable.hu)); 
		    			 countriesList.add(new CountryInfo("iceland", "IS ", "354",R.drawable.is));
		    			 countriesList.add(new CountryInfo("indonesia", "ID ", "62",R.drawable.id));
		    			 countriesList.add(new CountryInfo("iran", "IR ", "98",R.drawable.ir));
		    			 countriesList.add(new CountryInfo("iraq", "IQ ", "964",R.drawable.iq)); 
		    			countriesList.add(new CountryInfo("ireland", "IE ", "353",R.drawable.ie));
		    			countriesList.add(new CountryInfo("isle of man", "IM ", "44",R.drawable.im)); 
		    			countriesList.add(new CountryInfo("israel", "IL ", "972",R.drawable.il)); 
		    			countriesList.add(new CountryInfo("italy", "IT ", "39",R.drawable.it)); 
		                countriesList.add(new CountryInfo("ivory coast", "CI ", "225",R.drawable.ci));
		    			countriesList.add(new CountryInfo("jamaica", "JM ", "1 876",R.drawable.jm)); 
		    			countriesList.add(new CountryInfo("japan", "JP ", "81",R.drawable.jp));
		    			countriesList.add(new CountryInfo("jersey", "JE ", "",R.drawable.je)); 
		    			 countriesList.add(new CountryInfo("jordan", "JO ", "962",R.drawable.jo)); 
		    				countriesList.add(new CountryInfo("kazakhstan", "KZ ", "7",R.drawable.kz));
		    				countriesList.add(new CountryInfo("kenya", "KE ", "254",R.drawable.ke)); 
		    				 countriesList.add(new CountryInfo("kiribati", "KI ", "686",R.drawable.ki)); 
		  			    countriesList.add(new CountryInfo("kuwait", "KW ", "965",R.drawable.kw)); 
		    			    countriesList.add(new CountryInfo("kyrgyzstan", "KG ", "996",R.drawable.kg)); 
		    			 countriesList.add(new CountryInfo("laos", "LA ", "856",R.drawable.la)); 
		    			countriesList.add(new CountryInfo("latvia", "LV ", "371",R.drawable.lv));
		    			countriesList.add(new CountryInfo("lebanon", "LB ", "961",R.drawable.lb)); 
		    		 countriesList.add(new CountryInfo("lesotho", "LS ", "266",R.drawable.ls)); 
		    		 countriesList.add(new CountryInfo("liberia", "LR ", "231",R.drawable.lr));
		    		 countriesList.add(new CountryInfo("libya", "LY ", "218",R.drawable.ly));
		    		 countriesList.add(new CountryInfo("liechtenstein", "LI ", "423",R.drawable.li)); 
		    		 countriesList.add(new CountryInfo("lithuania", "LT ", "370",R.drawable.lt));
		    		 countriesList.add(new CountryInfo("luxembourg", "LU ", "352",R.drawable.lu));
		    		 countriesList.add(new CountryInfo("macau", "MO ", "853",R.drawable.mo));
		    		 countriesList.add(new CountryInfo("macedonia", "MK ", "389",R.drawable.mk));
		    		 countriesList.add(new CountryInfo("madagascar", "MG ", "261",R.drawable.mg));
		    		 countriesList.add(new CountryInfo("malawi", "MW ", "265",R.drawable.mw));
				    	 countriesList.add(new CountryInfo("malaysia", "MY ", "60",R.drawable.my));
				  	 countriesList.add(new CountryInfo("maldives", "MV ", "960",R.drawable.mv)); 
				    	 countriesList.add(new CountryInfo("mali", "ML ", "223",R.drawable.ml));
				    	 countriesList.add(new CountryInfo("malta", "MT ", "356",R.drawable.mt)); 
				    	countriesList.add(new CountryInfo("marshall islands", "MH ", "692",R.drawable.mh)); 
				    	countriesList.add(new CountryInfo("mauritania", "MR ", "222",R.drawable.mu));
				    	countriesList.add(new CountryInfo("mauritius", "MU ", "230",R.drawable.mu));
				    	countriesList.add(new CountryInfo("mayotte", "YT ", "262",R.drawable.yt)); 
				    countriesList.add(new CountryInfo("mexico", "MX ", "52",R.drawable.mx));
				    countriesList.add(new CountryInfo("micronesia", "FM ", "691",R.drawable.fm));
				    countriesList.add(new CountryInfo("moldova", "MD ", "373",R.drawable.md)); 
				    countriesList.add(new CountryInfo("monaco", "MC ", "377",R.drawable.mc)); 
				    countriesList.add(new CountryInfo("mongolia", "MN ", "976",R.drawable.mn)); 
				   countriesList.add(new CountryInfo("montenegro", "ME ", "382",R.drawable.me));
				  countriesList.add(new CountryInfo("montserrat", "MS ", "1 664",R.drawable.ms)); 
				  countriesList.add(new CountryInfo("morocco", "MA ", "212",R.drawable.ma));
				  countriesList.add(new CountryInfo("mozambique", "MZ ", "258",R.drawable.mz));
				  countriesList.add(new CountryInfo("namibia", "NA ", "264",R.drawable.na)); 
				  countriesList.add(new CountryInfo("nauru", "NR ", "674",R.drawable.nr));
				  countriesList.add(new CountryInfo("nepal", "NP ", "977",R.drawable.np));
				  countriesList.add(new CountryInfo("netherlands", "NL ", "31",R.drawable.nl));
				  countriesList.add(new CountryInfo("netherlands antilles", "AN ", "599",R.drawable.an)); 
				  countriesList.add(new CountryInfo("new caledonia", "NC ", "687",R.drawable.nc));
				  countriesList.add(new CountryInfo("new zealand", "NZ ", "64",R.drawable.nz)); 
				  countriesList.add(new CountryInfo("nicaragua", "NI ", "505",R.drawable.ni));
				  countriesList.add(new CountryInfo("niger", "NE ", "227",R.drawable.ne));
				  countriesList.add(new CountryInfo("nigeria", "NG ", "234",R.drawable.ng));
				  countriesList.add(new CountryInfo("niue", "NU ", "683",R.drawable.nu));
				  countriesList.add(new CountryInfo("north korea", "KP ", "850",R.drawable.kp));
				  countriesList.add(new CountryInfo("northern mariana islands", "MP ", "1 670",R.drawable.mp));
				  countriesList.add(new CountryInfo("norway", "NO ", "47",R.drawable.no)); 
				  countriesList.add(new CountryInfo("oman", "OM ", "968",R.drawable.om)); 
				  countriesList.add(new CountryInfo("pakistan", "PK ", "92",R.drawable.pk));
				  countriesList.add(new CountryInfo("palau", "PW ", "680",R.drawable.pw));
				  countriesList.add(new CountryInfo("panama", "PA ", "507",R.drawable.pa));
				  countriesList.add(new CountryInfo("papua new guinea", "PG ", "675",R.drawable.pg));
				  countriesList.add(new CountryInfo("paraguay", "PY ", "595",R.drawable.py)); 
				  countriesList.add(new CountryInfo("peru", "PE ", "51",R.drawable.pe));
				  countriesList.add(new CountryInfo("philippines", "PH ", "63",R.drawable.ph)); 
				  countriesList.add(new CountryInfo("pitcairn islands", "PN ", "870",R.drawable.pn)); 
				  countriesList.add(new CountryInfo("poland", "PL ", "48",R.drawable.pl)); 
				  countriesList.add(new CountryInfo("portugal", "PT ", "351",R.drawable.pt));
				  countriesList.add(new CountryInfo("puerto rico", "PR ", "1",R.drawable.pr));
				  countriesList.add(new CountryInfo("qatar", "QA ", "974",R.drawable.qa));
				  countriesList.add(new CountryInfo("republic of the congo", "CG ", "242",R.drawable.cg)); 
				  countriesList.add(new CountryInfo("romania", "RO ", "40",R.drawable.ro)); 
				  countriesList.add(new CountryInfo("russia", "RU ", "7",R.drawable.ru));
				  countriesList.add(new CountryInfo("rwanda", "RW ", "250",R.drawable.rw)); 
				  countriesList.add(new CountryInfo("saint barthelemy", "BL ", "590",R.drawable.bl));
				  countriesList.add(new CountryInfo("saint helena", "SH ", "290",R.drawable.sh));
				  countriesList.add(new CountryInfo("saint kitts and nevis", "KN ", "1 869",R.drawable.kn));
				  countriesList.add(new CountryInfo("saint lucia", "LC ", "1 758",R.drawable.lc)); 
				  countriesList.add(new CountryInfo("saint martin", "MF ", "1 599",R.drawable.mf));
				  countriesList.add(new CountryInfo("saint pierre and miquelon", "PM ", "508",R.drawable.pm)); 
				  countriesList.add(new CountryInfo("saint vincent and the grenadines", "VC ", "1 784",R.drawable.vc));
				  countriesList.add(new CountryInfo("samoa", "WS ", "685",R.drawable.ws)); 
				  countriesList.add(new CountryInfo("san marino", "SM ", "378",R.drawable.sm)); 
				  countriesList.add(new CountryInfo("sao tome and principe", "ST ", "239",R.drawable.st));
				  countriesList.add(new CountryInfo("saudi arabia", "SA ", "966",R.drawable.sa)); 
				  countriesList.add(new CountryInfo("senegal", "SN ", "221",R.drawable.sn)); 
				  countriesList.add(new CountryInfo("serbia", "RS ", "381",R.drawable.rs));
				  countriesList.add(new CountryInfo("seychelles", "SC ", "248",R.drawable.sc));
				  countriesList.add(new CountryInfo("sierra leone", "SL ", "232",R.drawable.sl));
				  countriesList.add(new CountryInfo("singapore", "SG ", "65",R.drawable.sg)); 
			      countriesList.add(new CountryInfo("slovakia", "SK ", "421",R.drawable.sk));
			     countriesList.add(new CountryInfo("slovenia", "SI ", "386",R.drawable.si)); 
			    countriesList.add(new CountryInfo("solomon islands", "SB ", "677",R.drawable.sb)); 
			    countriesList.add(new CountryInfo("somalia", "SO ", "252",R.drawable.so)); 
			     countriesList.add(new CountryInfo("south africa", "ZA ", "27",R.drawable.za));
			     countriesList.add(new CountryInfo("south korea", "KR ", "82",R.drawable.kr));
			   	 countriesList.add(new CountryInfo("spain", "ES ", "34",R.drawable.es));
    			 countriesList.add(new CountryInfo("sri lanka", "LK ", "94",R.drawable.lk));
    			 countriesList.add(new CountryInfo("sudan", "SD ", "249",R.drawable.sd));
    			 countriesList.add(new CountryInfo("suriname", "SR ", "597",R.drawable.sr));
    			 countriesList.add(new CountryInfo("svalbard", "SJ ", "",R.drawable.sj));
    			 countriesList.add(new CountryInfo("swaziland", "SZ ", "268",R.drawable.sz)); 
    			 countriesList.add(new CountryInfo("sweden", "SE ", "46",R.drawable.se)); 
    			 countriesList.add(new CountryInfo("switzerland", "CH ", "41",R.drawable.ch)); 
    			 countriesList.add(new CountryInfo("syria", "SY ", "963",R.drawable.sy)); 
    			 countriesList.add(new CountryInfo("taiwan", "TW ", "886",R.drawable.tw));
    			 countriesList.add(new CountryInfo("tajikistan", "TJ ", "992",R.drawable.tj)); 
    			 countriesList.add(new CountryInfo("tanzania", "TZ ", "255",R.drawable.tz));
    			 countriesList.add(new CountryInfo("thailand", "TH ", "66",R.drawable.th)); 
    			 countriesList.add(new CountryInfo("timor-leste", "TL ", "670",R.drawable.tl));
    			 countriesList.add(new CountryInfo("togo", "TG ", "228",R.drawable.tg)); 
    			 countriesList.add(new CountryInfo("tokelau", "TK ", "690",R.drawable.tk));
    			 countriesList.add(new CountryInfo("tonga", "TO ", "676",R.drawable.to)); 
    			 countriesList.add(new CountryInfo("trinidad and tobago", "TT ", "1 868",R.drawable.tt));
    			 countriesList.add(new CountryInfo("tunisia", "TN ", "216",R.drawable.tn));
    			 countriesList.add(new CountryInfo("turkey", "TR ", "90",R.drawable.tr)); 
    			 countriesList.add(new CountryInfo("turkmenistan", "TM ", "993",R.drawable.tm)); 
    			 countriesList.add(new CountryInfo("turks and caicos islands", "TC ", "1 649",R.drawable.tc));
    			 countriesList.add(new CountryInfo("tuvalu", "TV ", "688",R.drawable.tv)); 
    			 countriesList.add(new CountryInfo("uganda", "UG ", "256",R.drawable.ug));
    			 countriesList.add(new CountryInfo("ukraine", "UA ", "380",R.drawable.ua));
    			 countriesList.add(new CountryInfo("united arab emirates", "AE ", "971",R.drawable.ae));
    			 countriesList.add(new CountryInfo("united kingdom", "GB ", "44",R.drawable.gb)); 
    			 countriesList.add(new CountryInfo("united states", "US ", "1",R.drawable.us));
    			 countriesList.add(new CountryInfo("uruguay", "UY ", "598",R.drawable.uy)); 
    			 countriesList.add(new CountryInfo("us virgin islands", "VI ", "1 340",R.drawable.vi)); 
    			 countriesList.add(new CountryInfo("uzbekistan", "UZ ", "998",R.drawable.uz));
    			 countriesList.add(new CountryInfo("vanuatu", "VU ", "678",R.drawable.vu));
    			 countriesList.add(new CountryInfo("venezuela", "VE ", "58",R.drawable.ve));
    			 countriesList.add(new CountryInfo("vietnam", "VN ", "84",R.drawable.vn));
				countriesList.add(new CountryInfo("wallis and futuna", "WF ", "681",R.drawable.wf)); 
				countriesList.add(new CountryInfo("western sahara", "EH ", "",R.drawable.eh));
				countriesList.add(new CountryInfo("yemen", "YE ", "967",R.drawable.ye)); 
				countriesList.add(new CountryInfo("zambia", "ZM ", "260",R.drawable.zm));
				countriesList.add(new CountryInfo("zimbabwe", "ZW ", "263",R.drawable.zw));
				
    	CountryAdapter countriesArrayAdapter = new CountryAdapter(getActivity(),
    	         android.R.layout.simple_list_item_1,countriesList);
    	countriesArrayAdapter.setDropDownViewResource(R.layout.country_item);
    	
    	registerCodePhone.setAdapter(countriesArrayAdapter);
	}
}
