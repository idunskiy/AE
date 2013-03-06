package com.customitems;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.assignmentexpert.R;

public class ObscuredImageView extends ImageView {
    protected static final String TAG = "ObscuredImageView";
    private Drawable _innerShading;
    private Drawable _obscured;
    int id;
    
    public ObscuredImageView(Context context) {
        super(context);
    }
    public ObscuredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _obscured = getContext().getResources().getDrawable(R.drawable.login);
    }
    public ObscuredImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public void setId(int id)
    {
    	this.id = id;
    	
    }
    
    public int getId()
    {
    	return this.id;
    }
   
    
    @Override
    public void setImageBitmap(Bitmap bitmap) {
        BitmapDrawable image = new BitmapDrawable(getContext().getResources(), bitmap);
        Drawable pressed = new LayerDrawable(new Drawable[] { image, _innerShading });
        Drawable normal = new LayerDrawable(new Drawable[] { image, _innerShading, _obscured });
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[] { android.R.attr.state_pressed }, pressed);
        states.addState(new int[] { android.R.attr.state_focused }, pressed);
        states.addState(new int[] {}, normal);
        setImageDrawable(states);
    }
}