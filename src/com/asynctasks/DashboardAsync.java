package com.asynctasks;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.R;
import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.crashlytics.android.Crashlytics;
import com.datamodel.Order;
import com.datamodel.ProcessStatus;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.library.Constants;
import com.library.DataParsing;
import com.library.DatabaseHandler;
import com.library.UserFunctions;
import com.library.singletones.SharedPrefs;
/** * AsyncTask для загрузки списка заказов*/
public class DashboardAsync extends AbstractTaskLoader{
	Context context;
	List<String[]> results = null;
	List<Order> forPrint = null;
	private DatabaseHandler db;
	JSONObject k;
	 boolean res = false;
	 DataParsing u = new DataParsing();
 	UserFunctions n = new UserFunctions();
 	DashboardActivityAlt dashboardAct = new DashboardActivityAlt();
		String result = "";
		List<Order> orders;
		/** *метод вызова выполнения запроса из активностей*/
		public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

			DashboardAsync loader = new DashboardAsync(fa);

			new TaskProgressDialogFragment.Builder(fa, loader, fa.getResources().getString(R.string.dialog_loading))
					.setCancelable(false)
					.setTaskLoaderListener(taskLoaderListener)
					.show();
		}
		
	public DashboardAsync(Context context) {
		super(context);
		this.context = context; 
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Bundle getArguments() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		
	}
	/** *метод выполнения запроса*/
	@Override
	public Object loadInBackground() {
		try{
		db = new DatabaseHandler(context.getApplicationContext());
		Dao<ProcessStatus,Integer> dao = db.getStatusDao();
		db.getLevelDao();
		db.getSubjectDao();
		db.getCategoryDao();
		
		Log.i("page in async", Integer.toString(DashboardActivityAlt.page));
		Log.i("per_page in async", Integer.toString(DashboardActivityAlt.perpage));
		k = n.getOrders(Integer.toString(DashboardActivityAlt.page),Integer.toString(DashboardActivityAlt.perpage));
		orders = u.wrapOrders(k);
    		if (orders == null)
    		{
    			SharedPrefs.getInstance().getSharedPrefs().edit().putBoolean(Constants.NO_MORE_ORDERS, true).commit();
    			DashboardActivityAlt.stopDownload = true;
    			result = "empty";
    			res = true;
    			onStopLoading();
    		}
    		else
    		{
		QueryBuilder<ProcessStatus,Integer> query = dao.queryBuilder();
		query.where();
	    	for(Order r : orders)
	    	{
		         GenericRawResults<String[]> rawResults = dao.queryRaw("select "+ ProcessStatus.STATUS_TITLE +" from process_status where "+ ProcessStatus.STATUS_ID + " = " + r.getProcess_status().getProccessStatusId());
		         results = rawResults.getResults();
		         String[] resultArray = results.get(0); 
		         result = resultArray[0];
		         if (!r.getIsActive().equalsIgnoreCase("active"))
		         {
		        	    r.getProcess_status().setProccessStatusId(9);
		        	 	r.getProcess_status().setProccessStatusTitle("Inactive");
		         }
		          else
		        	 r.getProcess_status().setProccessStatusTitle(result);
		         
		        DashboardActivityAlt.forPrint.add(r);
	         }
	    	result = "success";
		    DashboardActivityAlt.page+=1;
		    orders = null;
    		}
		}
	    catch (Exception e1) {
	   	 Crashlytics.logException(e1);
			e1.printStackTrace();
			result = "error";
			onStopLoading();
		}
		return result;
	}
	@Override 
	 protected void onStopLoading() {
	        Log.i("DashboardAsync", "onStopLoading method");
//	        this.setCanseled(true);
//	        TaskProgressDialogFragment.cancel();
//	        if (errorFlag)
//	        new FrequentlyUsedMethods(context).someMethod("Something went wrong. Please try later.");
//	        cancelLoad();
	    }

}
