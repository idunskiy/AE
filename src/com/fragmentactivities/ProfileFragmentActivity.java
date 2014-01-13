package com.fragmentactivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.assignmentexpert.LoginActivity;
import com.assignmentexpert.R;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.LoginAsync;
import com.asynctasks.ProfileUpdateAsync;
import com.customitems.CustomEditPreference;
import com.fragments.IClickListener;
import com.fragments.ProfileFragmentCompl;
import com.fragments.ProfileFragmentPref;
import com.fragments.RegisterFragment;
import com.fragments.RegisterFragmentCompl;
/** * FragmentActivity информации профил€ и его изменени€. ќперерует фрагментами ProfileFragmentPref, ProfileFragmentCompl
 * @see ProfileFragmentPref
 * @see ProfileFragmentCompl  */
public class ProfileFragmentActivity extends FragmentActivity  implements IClickListener, ITaskLoaderListener{
	/** * экземпл€р класса FragmentTransaction дл€ проведени€ оперций над обьектами Fragment	  */
	FragmentTransaction fragmentTransaction;
	@Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.profile_fragment);
      Fragment newFragment = new ProfileFragmentPref();
      FragmentManager fragmentManager = getSupportFragmentManager();
      fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.add(R.id.myfragment, newFragment);
      fragmentTransaction.commit();
      
	}
	/** *метод дл€ переключени€ фрагментов внутри активности и вызова операций в отдельном потоке(ProfileUpdateAsync).   */
	public void changeFragment(int flag) {
		if (flag==1)
		{
			  Fragment newFragment = new ProfileFragmentCompl();
			  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		      fragmentTransaction.replace(R.id.myfragment, newFragment);
		      fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		      fragmentTransaction.commit();
		}
		if (flag==2)
		{
			Log.i("profile Act", "change");
			  Fragment newFragment = new ProfileFragmentPref();
			  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		      fragmentTransaction.replace(R.id.myfragment, newFragment);
		      fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		      fragmentTransaction.commit();
		}
		// profile updating
		if (flag==3)
		{
			ProfileUpdateAsync.execute(this, this);
		}
			
		
	}
	 /** *метод интерфейса ITaskLoaderListener. служит дл€ обработки данных после успешной работы задачи в отдельном потоке, используемой в данной активности.  */
	public void onLoadFinished(Object data) {
		if (data instanceof String & ((String)data).equalsIgnoreCase("success"))
		{
			Fragment myFragment = (Fragment)getSupportFragmentManager().findFragmentById(R.id.myfragment);
			if (myFragment instanceof ProfileFragmentCompl)
			{
			
				Bundle bundle = new Bundle();
				bundle.putString("profile_name",ProfileFragmentCompl.firstName);
				bundle.putString("profile_phone", ProfileFragmentCompl.phone);
				bundle.putString("profile_pass", ProfileFragmentCompl.password);
				LoginAsync.user.setFirst_name(ProfileFragmentCompl.firstName);
				LoginAsync.user.setPhone(ProfileFragmentCompl.phone);
				LoginActivity.forFragmentPassword = ProfileFragmentCompl.password;
				
				Fragment newFragment = new ProfileFragmentPref();
		
			   ((ProfileFragmentPref)newFragment).setArguments(bundle);
			  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		      fragmentTransaction.replace(R.id.myfragment, newFragment);
		      fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		      fragmentTransaction.commit();
		  	  
			
			}
		}
	else if(data instanceof String & ((String)data).equalsIgnoreCase("error"))
		Toast.makeText(this, ProfileUpdateAsync.errorMessage, Toast.LENGTH_LONG).show();
		
	}
	/** *метод интерфейса ITaskLoaderListener. служит дл€ обработки данных после неуспешной работы задачи в отдельном потоке, используемой в данной активности.  */
	public void onCancelLoad() {
		// TODO Auto-generated method stub
		
	}
}
