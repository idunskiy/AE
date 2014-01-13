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
import com.datamodel.EssayCreationStyle;
import com.datamodel.EssayType;
import com.datamodel.Level;
import com.datamodel.ProcessStatus;
import com.datamodel.ProductWriting;
import com.datamodel.Subject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.library.DatabaseHandler;
import com.library.FrequentlyUsedMethods;
/** * фрагмент для отображения информации по заказу (Essay)*/
public class OrderInfoFragmentEW extends Fragment{
	/** * CustomTextView для отображения продукта заказа*/
	private CustomTextView productTextView;
	/** * CustomTextView для отображения цены заказа*/
	private CustomTextView priceTextView;
	/** * CustomTextView для отображения темы заказа*/
	private CustomTextView subjTextView;
	/** * CustomTextView для отображения временной зоны*/
	private CustomTextView timezoneTextView;
	/** * CustomTextView для отображения даты, когда заказ был создан*/
	private CustomTextView postedTextView;
	/** * CustomTextView для отображения уровня заказа*/
	private CustomTextView levelTextView;
	/** * CustomTextView для отображения срока выполнения заказа*/
	private CustomTextView deadlineTextView;
	/** * CustomTextView для отображения задачи заказа*/
	private CustomTextView taskTextView;
	/** * CustomTextView для отображения типа essay*/
	private CustomTextView typeTextView;
	private CustomTextView citationTextView;
	private CustomTextView referenceTextView;
	/** * CustomTextView для отображения количества страниц*/
	private CustomTextView pagesTextView;

	/** * коллекция для списка тем заказа*/
	List<Subject> subjectsList;
	/** * коллекция для списка категорий*/
	List<Category> categoryList;
	/** * коллекция для списка уровней заказов*/
	List<Level> levelList;
	private LinearLayout fileListLayout;
	private FrequentlyUsedMethods faq;
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.order_info_ew,
	        container, false);
	    
	    faq = new FrequentlyUsedMethods(getActivity());
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
	    fileListLayout = (LinearLayout)view.findViewById(R.id.infoMessageListLayout);
	    
	    
	    fillFields();
	    faq.addOrderFiles(getActivity(), fileListLayout);
	    return view;
	  }
	/** * метод заполнения значениями полей информации*/
	private void fillFields()
	{
		  DatabaseHandler db = new DatabaseHandler(getActivity());
		    Dao<Subject, Integer> daoSubject = null;
		    Dao<Level, Integer> daoLevel = null;
		    
		    Dao<EssayCreationStyle, Integer> daoEsayCrSt = null;
		    Dao<EssayType, Integer> daoEssayType = null;
			try {
				daoSubject = db.getSubjectDao();
				subjectsList = daoSubject.queryForAll();
				
				daoLevel = db.getLevelDao();
				levelList = daoLevel.queryForAll();
				
				daoEsayCrSt= db.getEssayCreationStyleDao();
				daoEssayType = db.getEssayTypeDao();
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				
				if (DashboardActivityAlt.listItem.getLevel() != null)
				DashboardActivityAlt.listItem.getLevel().setLevelTitle((
						daoLevel.queryForId(DashboardActivityAlt.listItem.getLevel().getLevelId()).getLevelTitle()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
			
		    
		    try{
		    if (DashboardActivityAlt.listItem.getPrice()==0)
		 		priceTextView.setText("N/A");
		 	else
		 		priceTextView.setText(Float.toString(DashboardActivityAlt.listItem.getPrice())+"$");
		    timezoneTextView.setText("GMT " + DashboardActivityAlt.listItem.getTimezone());
		    subjTextView.setText(daoSubject.queryForId(DashboardActivityAlt.listItem.getCategory().getCategoryId()).getSubjectTitle());
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

		    	
		    	EssayCreationStyle m = null;
		    	EssayType n = null;
		    	try {
		    	QueryBuilder<EssayCreationStyle, Integer> qb = daoEsayCrSt.queryBuilder();
		    	Where where = qb.where();
		    	where.in(EssayCreationStyle.ESSAY_CREATION_ID, ((ProductWriting)DashboardActivityAlt.listItem.getProduct().getProduct()).getEssayTypeId());
		    	qb.selectColumns(EssayCreationStyle.ESSAY_CREATION_TITLE);
		    	
					m = daoEsayCrSt.queryForFirst(qb.prepare());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    	try {
			    	QueryBuilder<EssayType, Integer> qb = daoEssayType.queryBuilder();
			    	Where where = qb.where();
			    	where.in(EssayType.ESSAY_TYPE_ID, (((ProductWriting)DashboardActivityAlt.listItem.getProduct().getProduct()).getEssayStyleId()));
			    	qb.selectColumns(EssayType.ESSAY_TYPE_TITLE);
			    	
						n = daoEssayType.queryForFirst(qb.prepare());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		   
		    
		    if (m!=null)
		    	typeTextView.setText(m.getECSTitle());
		    if(n!=null)
		    	citationTextView.setText(n.getEssayTypeTitle());
		    }
		    catch(NullPointerException e)
		    {
		    	e.printStackTrace();
		    	typeTextView.setText("");
		    	citationTextView.setText("");
		    }
		    referenceTextView.setText(((ProductWriting)DashboardActivityAlt.listItem.getProduct().getProduct()).getEssayNumbRef());
		    pagesTextView.setText((((ProductWriting)DashboardActivityAlt.listItem.getProduct().getProduct()).getEssayPagesNumb()));
		    }
}
