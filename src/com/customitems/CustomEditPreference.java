package com.customitems;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.assignmentexpert.R;

public class CustomEditPreference extends RelativeLayout{
	
	
	  private CustomTextView title;
	private CustomTextView summary;
	Context ctx;
	private ImageView icon;
	public CustomEditPreference(Context context) {
	        super(context);
	       init(context, null);
	    }
	 public CustomEditPreference(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        
	        init(context, attrs);

	    }
	 private void init(Context context, AttributeSet attrs)
	 {

		     LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	         inflater.inflate(R.layout.icon_pref, this);
	         
		     title = (CustomTextView) this.findViewById(android.R.id.title);
		     
	         summary = (CustomTextView) this.findViewById(android.R.id.summary);
	         icon  = (ImageView) this.findViewById(R.id.icon);
	         TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomEditPreference);
		        int i= a.getInt(R.styleable.CustomEditPreference_iconDisable,0);
		        String v= a.getString(R.styleable.CustomEditPreference_android_title);
		        if (i!=0)
		        {
		        	icon.setVisibility(View.GONE);
		        }
		        if (v!=null)
		        	title.setText(v);
		        a.recycle();
	 }
	    
	    public void setTitle(String text)
	    {
	    	title.setText(text);
	    }
	    public void setSummary(String text)
	    {
	    	summary.setText(text);
	    }
	    public String getSummary()
	    {
	    	return this.summary.getText().toString();
	    }
	    public void setIcon(int rez)
	    {
	    	Resources res = getResources();
            Drawable icon2 = res.getDrawable(rez);
	    	icon.setImageDrawable(icon2);
	    }
	    public void iconDisable()
	    {
	    	icon.setVisibility(View.GONE);
	    }
	    
	    
}
