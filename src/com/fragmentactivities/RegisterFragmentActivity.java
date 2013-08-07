package com.fragmentactivities;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.assignmentexpert.R;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.CaptchaAsync;
import com.asynctasks.RegisterAsync;
import com.fragments.IClickListener;
import com.fragments.RegisterFragment;
import com.fragments.RegisterFragmentCompl;
import com.library.singletones.SharedPrefs;
/** * FragmentActivity �����������. ��������� ����������� RegisterFragment, RegisterFragmentCompl
 * @see RegisterFragment
 * @see RegisterFragmentCompl  */
public class RegisterFragmentActivity extends FragmentActivity implements IClickListener, ITaskLoaderListener{
	/** * ��������� ������ Fragment. ������������ ��� RegisterFragment	  */
	Fragment registerFragment;
	/** * ��������� ������ FragmentTransaction ��� ���������� ������� ��� ��������� Fragment	  */
	 FragmentTransaction fragmentTransaction;
	 /** * ��������� ������ FragmentManager ��� ��� ������ � ��������� Fragment ������ Activity	  */
	 FragmentManager fragmentManager;
	 /** * Bitmap ��� ���������� ������, ��� � �������	  */
	 public static Bitmap captchaForSave;
	@Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.profile_fragment);
      registerFragment = new RegisterFragment();
     
      fragmentManager = getSupportFragmentManager();
      fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.add(R.id.myfragment, registerFragment);
      fragmentTransaction.commit();
      CaptchaAsync.execute(this, this);
	}
	/** *����� ��� ������������ ���������� ������ ���������� � ������ �������� � ��������� ������(RegisterAsync).   */
	public void changeFragment(int flag) {
		if (flag==4)
		{
			Log.i("RegisterFragmentActivity", "add registerFragmentCompl Frag");
			 Fragment newFragment = new RegisterFragmentCompl();
			  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		      fragmentTransaction.replace(R.id.myfragment, newFragment);
		      //fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		      fragmentTransaction.commit();
		}
		if (flag==5)
		{
			RegisterAsync.execute(this, this);
		}
		
	}
	 /** *����� ���������� ITaskLoaderListener. ������ ��� ��������� ������ ����� �������� ������ ������ � ��������� ������, ������������ � ������ ����������.  */
	public void onLoadFinished(Object data) {
			 if (data instanceof Bitmap & data != null)
	    	 {
				 
	    		 RegisterFragment.setImageBitmap((Bitmap)data);//.setImageBitmap((Bitmap)data);
	    		 captchaForSave = (Bitmap)data;
	    		 setRegisterCaptha();
	    	 }
			 if (data instanceof String)
			 {
				 Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show();
			 }
			
		
		
	}
	/** *����� ���������� ITaskLoaderListener. ������ ��� ��������� ������ ����� ���������� ������ ������ � ��������� ������, ������������ � ������ ����������.  */
	public void onCancelLoad() {
		// TODO Auto-generated method stub
		
	}
//	@Override
//	public void onBackPressed() {
//		Log.i("registerAct", "onBackPressed");
//		   Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.myfragment);
//		   if (fragment instanceof RegisterFragmentCompl) {
//			   Log.i("registerFragment", "onBackPressed");
//			   Fragment newFragment = new RegisterFragment();
//			      FragmentManager fragmentManager = getSupportFragmentManager();
//			      fragmentTransaction = fragmentManager.beginTransaction();
//			      fragmentTransaction.replace(R.id.myfragment, newFragment);
//			      fragmentTransaction.commit();
//		          return;
//		   }
//		  
//		}
	
	
	/** *���������� � ������ ������� ������ Back. ���� ������������ ���������� �� ��������� ���������� �����������, �� ����� ������� �� �������� �� RegisterFragment */
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i("RegisterFragmentActivity", "onKeyDown mthd");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	
        	 Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.myfragment);
        	if (fragment instanceof RegisterFragmentCompl) {
        	 Log.i("registerFragment", "onBackPressed");
        	 FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        	 Fragment registerFragment2 = new RegisterFragment();
        	 fragmentTransaction.replace(R.id.myfragment, registerFragment2).commit();
        	 getSupportFragmentManager().executePendingTransactions();
        	  return true;
        	}
        	if (fragment instanceof RegisterFragment)
        	{
        		return true;
        	}
        }
        return false;//super.onKeyDown(keyCode, event);
    }
	/** *����� ��������� ����� �������� ������������� ���������
	 * @return ��� �������� ���������*/
	public String getCurrentFragment()
	{
		 Fragment insFragment = getSupportFragmentManager().findFragmentById(R.id.myfragment);
		return insFragment.getClass().getName();
		
	}
	/** *����� ����������� �� ������ ���� �����������, � RegisterFragment	 */
	public void returnToFirstStep()
	{
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		 Fragment registerFragment2 = new RegisterFragment();
    	 fragmentTransaction.replace(R.id.myfragment, registerFragment2).commit();
    	 Log.i("RegisterFragmentActivity", " returnTo1Step");
    	 getRegisterCaptcha();
	}
	/** *����� ��������� ������	 */
	private void setRegisterCaptha()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		captchaForSave.compress(Bitmap.CompressFormat.JPEG, 100, baos);   
		byte[] b = baos.toByteArray(); 

		String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

		SharedPrefs.getInstance().editSharePrefs().putString("captcha_decode", encodedImage).commit();
		
	}
	/** *����� ��������� ����������� ������ � SharedPreferences	 */
	private void getRegisterCaptcha()
	{
		String previouslyEncodedImage =  SharedPrefs.getInstance().getSharedPrefs().getString("captcha_decode", "");
		if( !previouslyEncodedImage.equalsIgnoreCase("") ){
			Log.i("RegisterFragmentActivity","getRegisterCaptcha");
		    byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
		    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
		    RegisterFragment.setImageBitmap(bitmap);
		    Log.i("RegisterFragmentActivity , getRegisterCaptcha", Integer.toString(bitmap.getDensity()));
		    //RegisterFragment.captcha.setImageBitmap(bitmap);
		  
		}
	}
	@Override
	public void onBackPressed() {
    	
    	Log.i("RegisterFragmentActivity", "onBackPressed");

    }
}