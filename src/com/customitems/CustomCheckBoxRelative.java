package com.customitems;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.assignmentexpert.R;

public class CustomCheckBoxRelative extends RelativeLayout implements OnTouchListener{
	

	private CheckBox checkBox;
	CustomTextView title;
	public CustomCheckBoxRelative(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}
	
	public CustomCheckBoxRelative(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		
		// TODO Auto-generated constructor stub
	}
	
	 private void init(Context context)
	 {
		     LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	         inflater.inflate(R.layout.custom_checkbox_preference, this);
		     title = (CustomTextView) this.findViewById(android.R.id.title);
		     title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
	         checkBox = (CheckBox) this.findViewById(R.id.custom_checkbox);
	         checkBox.setOnTouchListener(this);
	        
	         
	 }
	 public boolean isCustomChecked()
	 {
		 return checkBox.isChecked();
	 }
	 public void setChecked(boolean flag)
	 {
		 checkBox.setChecked(flag);
	 }

	 public void setTitle(String str)
	 {
		 title.setText(str);
	 }
	 public void setTextSize(float size)
	 {
		 title.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
	 }


	public boolean onTouch(View v, MotionEvent event) {
		init(v.getContext());
		Log.i("CustomCheckBoxRelative", Boolean.toString(checkBox.isChecked()));
		if (!checkBox.isChecked())
			checkBox.setChecked(true);
		else 
			checkBox.setChecked(false);
//		if (((CustomTextView)v).getText().toString().equals(object))
//		     checkBox.setChecked(true);
		return false;
	}
	 

}
