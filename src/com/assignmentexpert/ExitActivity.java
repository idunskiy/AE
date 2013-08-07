package com.assignmentexpert;

import android.app.Activity;
import android.os.Bundle;
/** *Activity, вызывающаяся при выходе пользователя из приложения. Просто черный экран.*/
public class ExitActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exit);}

}
