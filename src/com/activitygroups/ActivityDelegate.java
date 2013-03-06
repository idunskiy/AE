package com.activitygroups;

import android.app.Activity;
import android.os.Bundle;

public class ActivityDelegate{
	private Activity mActivity;

public ActivityDelegate(final Activity activity) {
    mActivity = activity;
}
public void onCreate(final Bundle savedInstanceState) {
    // Do stuff.
}
}