package com.fragmentactivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.assignmentexpert.R;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctasks.ProfileUpdateAsync;
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
		if (flag==3)
			ProfileUpdateAsync.execute(this, this);
		
	}
	 /** *метод интерфейса ITaskLoaderListener. служит дл€ обработки данных после успешной работы задачи в отдельном потоке, используемой в данной активности.  */
	public void onLoadFinished(Object data) {
		if (data instanceof String & ((String)data).equalsIgnoreCase("success"))
		{
			Toast.makeText(this, "Your profile was updated successfully. Please relogin to see updatings.", Toast.LENGTH_LONG).show();
			ProfileFragmentPref.firstNamePref.setSummary(ProfileFragmentCompl.firstName);
			ProfileFragmentPref.countryPref.setSummary(ProfileFragmentCompl.counrtyEditPref.getSummary());
			ProfileFragmentPref.phoneTextView.setSummary(ProfileFragmentCompl.phone);
			ProfileFragmentPref.timezonePref.setSummary(ProfileFragmentCompl.timeZone);
			//lastnameTextView.setText(lastnameTextView.getText().toString());
			//loginTextView.setText(loginTextView.getText().toString());
			//phoneTextView.setText(phoneTextView.getText().toString());
			//timezoneTextView.setText(LoginActivity.getUser.getUser().getTimeZone());
			//passTextView.setText(phoneTextView.getText().toString());
			//disableEdits();
		}
	else if(data instanceof String & ((String)data).equalsIgnoreCase("error"))
		Toast.makeText(this, "Your profile wasn't updated. "+ ProfileUpdateAsync.errorMessage, Toast.LENGTH_LONG).show();
		
	}
	/** *метод интерфейса ITaskLoaderListener. служит дл€ обработки данных после неуспешной работы задачи в отдельном потоке, используемой в данной активности.  */
	public void onCancelLoad() {
		// TODO Auto-generated method stub
		
	}
}
