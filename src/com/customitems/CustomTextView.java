package com.customitems;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.assignmentexpert.R;
import com.library.singletones.TypeFaceSingletone;
/** *����������������� ������� TextView. ������������� ����������� ������������ ����� ��������� �����.  */
public class CustomTextView  extends TextView {
    private static final String TAG = "TextView";

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String customFont = a.getString(R.styleable.CustomTextView_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {
        tf = TypeFaceSingletone.getInstance().getCustomFont(ctx,asset);//Typeface.createFromAsset(ctx.getAssets(), asset);  
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: "+e.getMessage() + ctx.getClass().toString());
            return false;
        }

        setTypeface(tf);  
        return true;
    }

}