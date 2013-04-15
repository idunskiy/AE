package com.fragments;

import java.sql.SQLException;
import java.util.List;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.R;
import com.datamodel.Category;
import com.datamodel.Level;
import com.datamodel.ProductAssignment;
import com.datamodel.Subject;
import com.j256.ormlite.dao.Dao;
import com.library.DatabaseHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderInfoFragmentAA extends Fragment{
	private TextView productTextView;
	private TextView priceTextView;
	private TextView timezoneTextView;
	private TextView subjTextView;
	private TextView postedTextView;
	private TextView categoryTextView;
	private TextView levelTextView;
	private TextView deadlineTextView;
	private TextView taskTextView;
	private TextView requireTextView;
	private LinearLayout layout;
	
	List<Subject> subjectsList;
	List<Category> categoryList;
	List<Level> levelList;

	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.order_info_aa,
	        container, false);
	    productTextView = (TextView)view.findViewById(R.id.productTextView);
	    priceTextView =   (TextView)view.findViewById(R.id.priceTextView);
	    timezoneTextView = (TextView)view.findViewById(R.id.timezoneTextView);
	    subjTextView = (TextView)view.findViewById(R.id.subjTextView);
	    postedTextView =  (TextView)view.findViewById(R.id.postedTextView);
	    categoryTextView =  (TextView)view.findViewById(R.id.categoryTextView);
	    levelTextView =  (TextView)view.findViewById(R.id.levelTextView);
	    deadlineTextView =  (TextView)view.findViewById(R.id.deadlineTextView);
	    taskTextView =  (TextView)view.findViewById(R.id.taskTextView);
	    requireTextView =  (TextView)view.findViewById(R.id.requireTextView);
	    
	    layout = (LinearLayout)view.findViewById(R.id.infoMessageListLayout);
	    fillFields();
	    return view;
	  }
	
	private void fillFields()
	{
		  DatabaseHandler db = new DatabaseHandler(getActivity());
		    Dao<Subject, Integer> daoSubject = null;
		    Dao<Category, Integer> daoCategory = null;
		    Dao<Level, Integer> daoLevel = null;
			try {
				
				daoSubject = db.getSubjectDao();
				subjectsList = daoSubject.queryForAll();
				
				daoLevel = db.getLevelDao();
				levelList = daoLevel.queryForAll();
				
				daoCategory = db.getCategoryDao();
				categoryList = daoCategory.queryForAll();
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				if (DashboardActivityAlt.listItem.getCategory()  != null)
				DashboardActivityAlt.listItem.getCategory().setCategoryTitle((
						daoCategory.queryForId(DashboardActivityAlt.listItem.getCategory().getCategoryId())).getCategoryTitle());
				if (DashboardActivityAlt.listItem.getCategory().getCategorySubject() != null)
				DashboardActivityAlt.listItem.getCategory().getCategorySubject().setSubjectTitle((
						daoSubject.queryForId(DashboardActivityAlt.listItem.getCategory().getCategorySubject().getSubjectId())).getSubjectTitle());
				if (DashboardActivityAlt.listItem.getLevel() != null)
				DashboardActivityAlt.listItem.getLevel().setLevelTitle((
						daoLevel.queryForId(DashboardActivityAlt.listItem.getLevel().getLevelId()).getLevelTitle()));
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(NullPointerException e)
			{
				DashboardActivityAlt.listItem.getCategory().setCategoryTitle("");
				DashboardActivityAlt.listItem.getCategory().getCategorySubject().setSubjectTitle("");
				DashboardActivityAlt.listItem.getLevel().setLevelTitle("");
			}
					
			
		    
		    
		    priceTextView.setText(Float.toString(DashboardActivityAlt.listItem.getPrice()));
		    timezoneTextView.setText(DashboardActivityAlt.listItem.getTimezone());
		    subjTextView.setText(DashboardActivityAlt.listItem.getCategory().getCategorySubject().getSubjectTitle());
		    postedTextView.setText(DashboardActivityAlt.listItem.getCreated_at().toString());
		    
		    categoryTextView.setText(DashboardActivityAlt.listItem.getCategory().getCategoryTitle());
		    
		    if (DashboardActivityAlt.listItem.getLevel() != null)
		       levelTextView.setText(DashboardActivityAlt.listItem.getLevel().getLevelTitle());
		    else
		       levelTextView.setText("N/A");
		    deadlineTextView.setText(DashboardActivityAlt.listItem.getDeadline().toString());
		    taskTextView.setText(((ProductAssignment)DashboardActivityAlt.listItem.getProduct().getProduct()).getAssignInfo());
		    if (((ProductAssignment)DashboardActivityAlt.listItem.getProduct().getProduct()).getAssignDtl_expl())
		    	requireTextView.setText("Detailed explanation"+"\r\n" );
		    if(((ProductAssignment)DashboardActivityAlt.listItem.getProduct().getProduct()).getAssignExcVideo())
		    	requireTextView.append("Exclusive video"+"\r\n" );
		    if(((ProductAssignment)DashboardActivityAlt.listItem.getProduct().getProduct()).getAssignCommVideo())
		    	requireTextView.append("Common video" );
		    	
		    productTextView.setText(DashboardActivityAlt.listItem.getProduct().getProductType());
		    Log.i("listItem in orderInfoAA", DashboardActivityAlt.listItem.toString());
	}
}
