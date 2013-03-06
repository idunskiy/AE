package com.customitems;

import com.assignmentexpert.R;

import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class CustomPreferenceCategory extends PreferenceCategory {
	private boolean flag = false;
    public CustomPreferenceCategory(Context context) {
        super(context);
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }
    public boolean getFlag()
    {return this.flag;}
    public void setFlag(boolean flag)
    {this.flag = flag;}
    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        //if (!flag)
        view.setVisibility(View.INVISIBLE);
//        else 
//        	view.setVisibility(View.VISIBLE);
    }
}