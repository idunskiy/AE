package com.customitems;

import android.content.Context;
import android.graphics.Color;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/** *кастомизированный CheckBoxPreference. используетс€ дл€ отображени€ CheckBox'ов в настройках отправки нового заказа. */
public class CustomCheckBoxPref extends CheckBoxPreference implements OnClickListener, OnTouchListener{
	private String fileTitle;
	TextView titleView ;
	 CheckBox checkBox;
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
    public void setTextSize(float size)
    {
    	titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }
    

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView titleView = (TextView) view.findViewById(android.R.id.title);
        //checkBox.setChecked(true);
        titleView.setTextColor(Color.WHITE);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)titleView.getLayoutParams();
        params.setMargins(15, 0, 0, 0); //substitute parameters for left, top, right, bottom
        titleView.setLayoutParams(params);
        titleView.setText(getTitle());
        
    }

	public void onClick(View v) {
		
	}

	public boolean onTouch(View v, MotionEvent event) {
		 Log.i("checkBox title", "asdasdas");
		return false;
	}
    

}