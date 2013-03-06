package com.asynctasks;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.assignmentexpert.DashboardActivityAlt;
import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.datamodel.Category;
import com.datamodel.Level;
import com.datamodel.Order;
import com.datamodel.ProcessStatus;
import com.datamodel.Subject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.library.DataParsing;
import com.library.DatabaseHandler;
import com.library.UserFunctions;

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
		
		public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

			DashboardAsync loader = new DashboardAsync(fa);

			new TaskProgressDialogFragment.Builder(fa, loader, "Loading…")
					.setCancelable(true)
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
	@Override
	public Object loadInBackground() {
		try{
		db = new DatabaseHandler(context.getApplicationContext());
		Dao<ProcessStatus,Integer> dao = db.getStatusDao();
		Dao<Level,Integer> daoLevel = db.getLevelDao();
		Dao<Subject,Integer> daoSubject = db.getSubjectDao();
		Dao<Category,Integer> daoCategory = db.getCategoryDao();
		
		Log.i("page in async", Integer.toString(DashboardActivityAlt.page));
		Log.i("per_page in async", Integer.toString(DashboardActivityAlt.perpage));
		k = n.getOrders(Integer.toString(DashboardActivityAlt.page),Integer.toString(DashboardActivityAlt.perpage));
		orders = u.wrapOrders(k);
    		if (orders.isEmpty())
    		{
    			Log.i("empty orders","orders are empty");
    			DashboardActivityAlt.stopDownload = true;
    			cancelLoad();
    			res = true;
    		}
    		else
    		{
			
    		}
		
		QueryBuilder<ProcessStatus,Integer> query = dao.queryBuilder();
		Where where = query.where();
	    	for(Order r : orders)
	    	{
		         GenericRawResults<String[]> rawResults = dao.queryRaw("select "+ ProcessStatus.STATUS_TITLE +" from process_status where "+ ProcessStatus.STATUS_ID + " = " + r.getProcess_status().getProccessStatusId());
		         results = rawResults.getResults();
		         String[] resultArray = results.get(0); //This will select the first result (the first and maybe only row returned)
		         result = resultArray[0]; //This will select the first field in the result (That should be the ID you need)
		         if (!r.getIsActive())
		         {
		        	    r.getProcess_status().setProccessStatusId(9);
		        	 	r.getProcess_status().setProccessStatusTitle("Inactive");
		         }
		          else
		        	 r.getProcess_status().setProccessStatusTitle(result);
		         
		        DashboardActivityAlt.forPrint.add(r);
	         }
	    	
		}
	    catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    res = false;
	    DashboardActivityAlt.page+=1;
		return null;
	}

}
