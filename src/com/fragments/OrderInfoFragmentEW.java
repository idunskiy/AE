package com.fragments;

import java.sql.SQLException;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.R;
import com.customitems.CustomTextView;
import com.datamodel.Category;
import com.datamodel.Level;
import com.datamodel.ProductWriting;
import com.datamodel.Subject;
import com.j256.ormlite.dao.Dao;
import com.library.DatabaseHandler;
/** * �������� ��� ����������� ���������� �� ������ (Essay)*/
public class OrderInfoFragmentEW extends Fragment{
	/** * CustomTextView ��� ����������� �������� ������*/
	private CustomTextView productTextView;
	/** * CustomTextView ��� ����������� ���� ������*/
	private CustomTextView priceTextView;
	/** * CustomTextView ��� ����������� ���� ������*/
	private CustomTextView subjTextView;
	/** * CustomTextView ��� ����������� ��������� ����*/
	private CustomTextView timezoneTextView;
	/** * CustomTextView ��� ����������� ����, ����� ����� ��� ������*/
	private CustomTextView postedTextView;
	/** * CustomTextView ��� ����������� ������ ������*/
	private CustomTextView levelTextView;
	/** * CustomTextView ��� ����������� ����� ���������� ������*/
	private CustomTextView deadlineTextView;
	/** * CustomTextView ��� ����������� ������ ������*/
	private CustomTextView taskTextView;
	/** * CustomTextView ��� ����������� ���� essay*/
	private CustomTextView typeTextView;
	private CustomTextView citationTextView;
	private CustomTextView referenceTextView;
	/** * CustomTextView ��� ����������� ���������� �������*/
	private CustomTextView pagesTextView;

	/** * ��������� ��� ������ ��� ������*/
	List<Subject> subjectsList;
	/** * ��������� ��� ������ ���������*/
	List<Category> categoryList;
	/** * ��������� ��� ������ ������� �������*/
	List<Level> levelList;
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.order_info_ew,
	        container, false);
	    
	    
	    productTextView = (CustomTextView)view.findViewById(R.id.productTextView);
	    priceTextView =   (CustomTextView)view.findViewById(R.id.priceTextView);
	    timezoneTextView = (CustomTextView)view.findViewById(R.id.timezoneTextView);
	    subjTextView = (CustomTextView)view.findViewById(R.id.subjTextView);
	    postedTextView =  (CustomTextView)view.findViewById(R.id.postedTextView);
	    levelTextView =  (CustomTextView)view.findViewById(R.id.levelTextView);
	    deadlineTextView =  (CustomTextView)view.findViewById(R.id.deadlineTextView);
	    taskTextView =  (CustomTextView)view.findViewById(R.id.taskTextView);
	    typeTextView =  (CustomTextView)view.findViewById(R.id.typeTextView);
	    
	    citationTextView = (CustomTextView)view.findViewById(R.id.citationTextView);
	    referenceTextView = (CustomTextView)view.findViewById(R.id.referenceTextView);
	    pagesTextView = (CustomTextView)view.findViewById(R.id.pagesTextView);
	    
	    fillFields();
	    return view;
	  }
	/** * ����� ���������� ���������� ����� ����������*/
	private void fillFields()
	{
		  DatabaseHandler db = new DatabaseHandler(getActivity());
		    Dao<Subject, Integer> daoSubject = null;
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
		    if (DashboardActivityAlt.listItem.getPrice()==0)
		 		priceTextView.setText("N/A");
		 	else
		 		priceTextView.setText(Float.toString(DashboardActivityAlt.listItem.getPrice())+"$");
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
