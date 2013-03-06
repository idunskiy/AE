package com.activitygroups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.assignmentexpert.InteractionsActivityViewPager;
import com.assignmentexpert.NewMessageActivity;

public class InteractionTabGroup extends MainTabGroup {
	Context ctx;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContext(InteractionTabGroup.this);
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		startChildActivity("InteractionsActivity", new Intent(getParent(),InteractionsActivityViewPager.class));
}
	public void setContext(Context context)
	{
		this.ctx = context;
	}
	
	public Context getContext()
	{return this.ctx;}
	public void replaceContentView() {
//	    View view = ((ActivityGroup) context)
//	            .getLocalActivityManager()
//	            .startActivity(id,
//	                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//	            .getDecorView();
//	    ((Activity) context).setContentView(view);
		startChildActivity("InteractionsActivity", new Intent(getParent(),NewMessageActivity.class));

	}
}