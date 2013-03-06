package com.customitems;

import android.content.Context;
import android.graphics.Color;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;


public class CustomCheckBoxPref extends CheckBoxPreference {
	private String fileTitle;
	
    public CustomCheckBoxPref(Context context) {
        super(context);
    }

    public CustomCheckBoxPref(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCheckBoxPref(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public String getTitle()
    {
    	return this.fileTitle;
    }
    public void setTitle(String title)
    {
    	this.fileTitle = title;
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView titleView = (TextView) view.findViewById(android.R.id.title);
        CheckBox checkBox = (CheckBox) view.findViewById(android.R.id.checkbox);
        checkBox.setChecked(true);
        view.setBackgroundColor(Color.rgb(38, 38, 38));
        titleView.setTextColor(Color.WHITE);
        titleView.setText(getTitle());
        
        
    }
  

}