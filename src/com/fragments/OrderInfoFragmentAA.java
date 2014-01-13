package com.fragments;

import java.sql.SQLException;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.assignmentexpert.DashboardActivityAlt;
import com.assignmentexpert.R;
import com.customitems.CustomTextView;
import com.datamodel.Category;
import com.datamodel.Level;
import com.datamodel.ProductAssignment;
import com.datamodel.Subject;
import com.j256.ormlite.dao.Dao;
import com.library.DatabaseHandler;
import com.library.FrequentlyUsedMethods;
/** * фрагмент для отображения информации по заказу (Assignment)*/
public class OrderInfoFragmentAA extends Fragment{
	/** * CustomTextView для отображения продукта заказа*/
	private CustomTextView productTextView;
	/** * CustomTextView для отображения цены*/
	private CustomTextView priceTextView;
	/** * CustomTextView для отображения временной зоны*/
	private CustomTextView timezoneTextView;
	/** * CustomTextView для отображения темы заказа*/
	private CustomTextView subjTextView;
	/** * CustomTextView для отображения темы заказа*/
	private CustomTextView postedTextView;
	/** * CustomTextView для отображения категории заказа*/
	private CustomTextView categoryTextView;
	/** * CustomTextView для отображения уровня заказа*/
	private CustomTextView levelTextView;
	/** * CustomTextView для отображения срока выполнения заказа*/
	private CustomTextView deadlineTextView;
	/** * CustomTextView для отображения задачи заказа*/
	private CustomTextView taskTextView;
	private CustomTextView requireTextView;
	/** * коллекция для списка тем заказа*/
	List<Subject> subjectsList;
	/** * коллекция для списка категорий*/
	List<Category> categoryList;
	/** * коллекция для списка уровней заказов*/
	List<Level> levelList;

	
	FrequentlyUsedMethods faq;
	private LinearLayout fileListLayout;
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.order_info_aa,
	        container, false);
	    faq = new FrequentlyUsedMethods(getActivity());
	    productTextView = (CustomTextView)view.findViewById(R.id.productTextView);
	    priceTextView =   (CustomTextView)view.findViewById(R.id.priceTextView);
	    timezoneTextView = (CustomTextView)view.findViewById(R.id.timezoneTextView);
	    subjTextView = (CustomTextView)view.findViewById(R.id.subjTextView);
	    postedTextView =  (CustomTextView)view.findViewById(R.id.postedTextView);
	    categoryTextView =  (CustomTextView)view.findViewById(R.id.categoryTextView);
	    levelTextView =  (CustomTextView)view.findViewById(R.id.levelTextView);
	    deadlineTextView =  (CustomTextView)view.findViewById(R.id.deadlineTextView);
	    taskTextView =  (CustomTextView)view.findViewById(R.id.taskTextView);
	    requireTextView =  (CustomTextView)view.findViewById(R.id.requireTextView);
	    
	    fileListLayout = (LinearLayout)view.findViewById(R.id.infoMessageListLayout);
	    Log.i("orderInfoAA", "onCreate methd");
	    fillFields();
	    faq.addOrderFiles(getActivity(), fileListLayout);
	    
	    return view;
	  }
	/** * метод заполнения значениями полей информации*/
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
				
				
			
				 Log.i("orderInfoAA fillFields",DashboardActivityAlt.listItem.toString());
		    if (DashboardActivityAlt.listItem.getPrice()==0)
		    priceTextView.setText("N/A");
		    	else
		    priceTextView.setText(Float.toString(DashboardActivityAlt.listItem.getPrice())+"$");
		    timezoneTextView.setText("GMT " + DashboardActivityAlt.listItem.getTimezone());
		    subjTextView.setText(DashboardActivityAlt.listItem.getCategory().getCategorySubject().getSubjectTitle());
		    postedTextView.setText(DashboardActivityAlt.listItem.getCreated_at().toString());
		    
		    categoryTextView.setText(DashboardActivityAlt.listItem.getCategory().getCategoryTitle());
		    
		    if (DashboardActivityAlt.listItem.getLevel() != null)
		       levelTextView.setText(DashboardActivityAlt.listItem.getLevel().getLevelTitle());
		    else
		       levelTextView.setText("N/A");
		    deadlineTextView.setText(DashboardActivityAlt.listItem.getDeadline().toString());
		    
		    taskTextView.setText(((ProductAssignment)DashboardActivityAlt.listItem.getProduct().getProduct()).getAssignInfo());
		    
		    if (((ProductAssignment)DashboardActivityAlt.listItem.getProduct().getProduct()).getAssignDtl_expl() == 1)
		    	requireTextView.setText("Detailed explanation"+"\r\n" );
		    if(((ProductAssignment)DashboardActivityAlt.listItem.getProduct().getProduct()).getAssignExcVideo())
		    	requireTextView.append("Exclusive video"+"\r\n" );
		    if(((ProductAssignment)DashboardActivityAlt.listItem.getProduct().getProduct()).getAssignCommVideo())
		    	requireTextView.append("Common video" );
		    	
		    productTextView.setText(DashboardActivityAlt.listItem.getProduct().getProductType());
		    Log.i("listItem in orderInfoAA", DashboardActivityAlt.listItem.toString());
		    
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(NullPointerException e)
			{
				DashboardActivityAlt.listItem.setCategory(new Category());
				DashboardActivityAlt.listItem.getCategory().setCategorySubject(new Subject());
				DashboardActivityAlt.listItem.setLevel(new Level());
			}
	}
}
