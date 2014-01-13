package com.library.singletones;

import android.content.Context;
import android.graphics.Typeface;
/**
 * singletone for custom fonts
 */
public class TypeFaceSingletone {
	private static TypeFaceSingletone instance = new TypeFaceSingletone();

	private TypeFaceSingletone() {}
	public static TypeFaceSingletone getInstance() {
		return instance;
	}
	public Typeface getCustomFont(Context ctx,String asset) {
		return Typeface.createFromAsset(ctx.getResources().getAssets(), asset);
	}
	 public static void initInstance()
	  {
	    if (instance == null)
	    {
	      // Create the instance
	      instance = new TypeFaceSingletone();
	    }
	  }

}