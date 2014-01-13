package com.fragmentactivities;

import java.io.ByteArrayOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.assignmentexpert.LoginActivity;
import com.assignmentexpert.R;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.CaptchaAsync;
import com.asynctasks.RegisterAsync;
import com.fragments.IClickListener;
import com.fragments.RegisterFragment;
import com.fragments.RegisterFragmentCompl;
import com.library.singletones.SharedPrefs;
import com.tabscreens.LoginTabScreen;
/** * FragmentActivity регистрации. Оперерует фрагментами RegisterFragment, RegisterFragmentCompl
 * @see RegisterFragment
 * @see RegisterFragmentCompl  */
public class RegisterFragmentActivity extends FragmentActivity implements IClickListener, ITaskLoaderListener{
	/** * экземпляр класса Fragment. Используется для RegisterFragment	  */
	Fragment registerFragment;
	/** * экземпляр класса FragmentTransaction для проведения оперций над обьектами Fragment	  */
	 FragmentTransaction fragmentTransaction;
	 /** * экземпляр класса FragmentManager для для работы с обьектами Fragment внутри Activity	  */
	 FragmentManager fragmentManager;
	 /** * Bitmap для сохранении каптчи, при её наличии	  */
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
	/** *метод для переключения фрагментов внутри активности и вызова операций в отдельном потоке(RegisterAsync).   */
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
	 /** *метод интерфейса ITaskLoaderListener. служит для обработки данных после успешной работы задачи в отдельном потоке, используемой в данной активности.  */
	public void onLoadFinished(Object data) {
		 Fragment currentFragment; 
			 if (data instanceof Bitmap & data != null)
	    	 {
				 currentFragment =fragmentManager.findFragmentById(R.id.myfragment); 
				if (currentFragment instanceof RegisterFragment)
				{
	    		 RegisterFragment.setImageBitmap((Bitmap)data);//.setImageBitmap((Bitmap)data);
	    		 ((RegisterFragment)currentFragment).fillCountryAdapter();
				}
	    		 
//	    		 captchaForSave = (Bitmap)data;
//	    		 setRegisterCaptha();
	    	 }
			 if (data instanceof String)
			 {
				 if (((String) data).equalsIgnoreCase("error"))
				 {
					 Toast.makeText(getApplicationContext(), RegisterAsync.RegisterAsyncError, Toast.LENGTH_SHORT ).show();
					 RegisterFragment.setImageBitmap(RegisterAsync.reCatcha);
					 
				 }
				 else if (((String) data).equalsIgnoreCase("success"))
				 {
					 
					 Toast.makeText(this, getResources().getString(R.string.toast_register_success)+"\r\n"+ RegisterFragment.userEmail
								, Toast.LENGTH_LONG).show();

					 
					 new Timer().schedule(new TimerTask() {
						    @Override
						    public void run() {
						        //This code is run all 60seconds
						        //If you want to operate UI modifications, you must run ui stuff on UiThread.
						        RegisterFragmentActivity.this.runOnUiThread(new Runnable() {
						            public void run() {
						            	
						            	 ((LoginTabScreen)getParent()).switchTab(0);
						            	 LoginActivity.inputEmail.setText(RegisterFragment.userEmail);
						            	 LoginActivity.inputPassword.getText().clear();
						            }
						        });
						    }
						}, 1000);
					
				 }
				 
			 }
			 // if error occurs
			 if (data==null)
			 {
				 Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_server_problem), Toast.LENGTH_SHORT).show();
			 }
			
		
		
	}
	/** *метод интерфейса ITaskLoaderListener. служит для обработки данных после неуспешной работы задачи в отдельном потоке, используемой в данной активности.  */
	public void onCancelLoad() {
		
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
	
	
	/** *вызывается в случае нажатии кнопки Back. Если пользователь находиться во фрагменте завершения регистрации, то после нажатия он попадает на RegisterFragment */
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
	/** *метод получения имени текущего используемого фрагмента
	 * @return имя текущего фрагмента*/
	public String getCurrentFragment()
	{
		 Fragment insFragment = getSupportFragmentManager().findFragmentById(R.id.myfragment);
		return insFragment.getClass().getName();
		
	}
	/** *метод возвращения на первый этап регистрации, в RegisterFragment	 */
	public void returnToFirstStep()
	{
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		 Fragment registerFragment2 = new RegisterFragment();
    	 fragmentTransaction.replace(R.id.myfragment, registerFragment2).commit();
    	 Log.i("RegisterFragmentActivity", " returnTo1Step");
//    	 getRegisterCaptcha();
	}
	/** *метод установки каптчи	 */
	private void setRegisterCaptha()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		captchaForSave.compress(Bitmap.CompressFormat.JPEG, 100, baos);   
		byte[] b = baos.toByteArray(); 

		String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

		SharedPrefs.getInstance().editSharePrefs().putString("captcha_decode", encodedImage).commit();
		
	}
	/** *метод получения сохраненной каптчи в SharedPreferences	 */
//	private void getRegisterCaptcha()
//	{
//		String previouslyEncodedImage =  SharedPrefs.getInstance().getSharedPrefs().getString("captcha_decode", "");
//		if( !previouslyEncodedImage.equalsIgnoreCase("") ){
//			Log.i("RegisterFragmentActivity","getRegisterCaptcha");
//		    byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
//		    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
//		    RegisterFragment.setImageBitmap(bitmap);
//		    Log.i("RegisterFragmentActivity , getRegisterCaptcha", Integer.toString(bitmap.getDensity()));
//		    //RegisterFragment.captcha.setImageBitmap(bitmap);
//		  
//		}
//	}
	@Override
	public void onBackPressed() {
    	
    	Log.i("RegisterFragmentActivity", "onBackPressed");

    }
}