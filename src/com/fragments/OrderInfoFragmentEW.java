package com.fragments;

import java.sql.SQLException;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.R;
import com.datamodel.Category;
import com.datamodel.Level;
import com.datamodel.ProductWriting;
import com.datamodel.Subject;
import com.j256.ormlite.dao.Dao;
import com.library.DatabaseHandler;

public class OrderInfoFragmentEW extends Fragment{
	private TextView productTextView;
	private TextView priceTextView;
	private TextView subjTextView;
	private TextView timezoneTextView;
	private TextView postedTextView;
	private TextView categoryTextView;
	private TextView levelTextView;
	private TextView deadlineTextView;
	private TextView taskTextView;
	private TextView requireTextView;
	private TextView typeTextView;
	private LinearLayout layout;
	private TextView citationTextView;
	private TextView referenceTextView;
	private TextView pagesTextView;

	
	List<Subject> subjectsList;
	List<Category> categoryList;
	List<Level> levelList;
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.order_info_ew,
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
	    typeTextView =  (TextView)view.findViewById(R.id.typeTextView);
	    layout = (LinearLayout)view.findViewById(R.id.infoMessageListLayout);
	    
	    citationTextView = (TextView)view.findViewById(R.id.citationTextView);
	    referenceTextView = (TextView)view.findViewById(R.id.referenceTextView);
	    pagesTextView = (TextView)view.findViewById(R.id.pagesTextView);
	    
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
				
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				
				//if (DashboardActivityAlt.listItem.getCategory().getCategorySubject().getSubjectId() != 0)
				//DashboardActivityAlt.listItem.getCategory().getCategorySubject().setSubjectTitle((
				//		daoSubject.queryForId(DashboardActivityAlt.listItem.getCategory().getCategorySubject().getSubjectId())).getSubjectTitle());
				if (DashboardActivityAlt.listItem.getLevel() != null)
				DashboardActivityAlt.listItem.getLevel().setLevelTitle((
						daoLevel.queryForId(DashboardActivityAlt.listItem.getLevel().getLevelId()).getLevelTitle()));
				if (DashboardActivityAlt.listItem.getSubject() != null)
					DashboardActivityAlt.listItem.getSubject().setSubjectTitle((
							daoSubject.queryForId(DashboardActivityAlt.listItem.getSubject().getSubjectId()).getSubjectTitle()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
			
		    
		    try{
		    priceTextView.setText(Float.toString(DashboardActivityAlt.listItem.getPrice()));
		    timezoneTextView.setText(DashboardActivityAlt.listItem.getTimezone());
		    subjTextView.setText(DashboardActivityAlt.listItem.getSubject().getSubjectTitle());
		    postedTextView.setText(DashboardActivityAlt.listItem.getCreated_at().toString());
		    
		    if (DashboardActivityAlt.listItem.getLevel() != null)
		       levelTextView.setText(DashboardActivityAlt.listItem.getLevel().getLevelTitle());
		    else
		       levelTextView.setText("N/A");
		    deadlineTextView.setText(DashboardActivityAlt.listItem.getDeadline().toString());
		    taskTextView.setText(((ProductWriting)DashboardActivityAlt.listItem.getProduct().getProduct()).getEssayInfo());
		    productTextView.setText(DashboardActivityAlt.listItem.getProduct().getProductType());
		    }
		    catch(Exception e)
		    {
		    	e.printStackTrace();
		    }
		    Log.i("dash board writing", DashboardActivityAlt.listItem.toString());
		    try{
		    //if (((ProductWriting)DashboardActivityAlt.listItem.getProduct().getProduct()).getEssayType().getEssayTypeTitle()!=null)
		    	typeTextView.setText(((ProductWriting)DashboardActivityAlt.listItem.getProduct().getProduct()).getEssayType().getEssayTypeTitle());
		    //if(((ProductWriting)DashboardActivityAlt.listItem.getProduct().getProduct()).getEssayStyle().getECSTitle()!=null)
		    	citationTextView.setText(((ProductWriting)DashboardActivityAlt.listItem.getProduct().getProduct()).getEssayStyle().getECSTitle());
		    }
		    catch(NullPointerException e)
		    {
		    	typeTextView.setText("");citationTextView.setText("");
		    }
		    referenceTextView.setText(((ProductWriting)DashboardActivityAlt.listItem.getProduct().getProduct()).getEssayNumbRef());
		    pagesTextView.setText((((ProductWriting)DashboardActivityAlt.listItem.getProduct().getProduct()).getEssayPagesNumb()));
		    }
}
