package com.customitems;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.assignmentexpert.R;

public class CustomEditPreference extends RelativeLayout{
	
	
	  private CustomTextView title;
	private CustomTextView summary;
	Context ctx;
	public CustomEditPreference(Context context) {
	        super(context);
	       init(context);
	    }
	 public CustomEditPreference(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        init(context);

	    }
	 private void init(Context context)
	 {

		 LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        inflater.inflate(R.layout.custom_listpref, this);
		     title = (CustomTextView) this.findViewById(android.R.id.title);
	        summary = (CustomTextView) this.findViewById(android.R.id.summary);
	 }
	    
	    public void setTitle(String text)
	    {
	    	title.setText(text);
	    }
	    public void setSummary(String text)
	    {
	    	summary.setText(text);
	    }
	    
	    
}
