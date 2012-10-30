package com.assignmentexpert;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.datamodel.Category;
import com.datamodel.Level;
import com.datamodel.Subject;
import com.j256.ormlite.dao.Dao;
import com.library.DatabaseHandler;

public class OrderInfoActivityAA extends Activity {
	private View btnClose;
	private Button btnInteractions;
	private Button btnInfoOrder;
	TextView productTextView;
    TextView priceTextView;
	 TextView timezoneTextView;
	 TextView subjTextView;
	private TextView postedTextView;
	private TextView categoryTextView;
	private TextView levelTextView;
	private TextView deadlineTextView;
	private TextView taskTextView;
	private TextView requireTextView;
	
	List<Subject> subjectsList;
	List<Category> categoryList;
	List<Level> levelList;
	
	@SuppressWarnings("null")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_info_aa);
        btnClose = (Button) findViewById(R.id.btnClose);
	    btnInteractions = (Button) findViewById(R.id.btnInteractions);
	    btnInfoOrder = (Button) findViewById(R.id.btnInfoOrder);
	    btnInfoOrder.setText("info"+ "\r\n"+Integer.toString(DashboardActivityAlt.listItem.getOrderid()));
	    
	    productTextView = (TextView)findViewById(R.id.productTextView);
	    priceTextView = (TextView)findViewById(R.id.priceTextView);
	    timezoneTextView = (TextView)findViewById(R.id.timezoneTextView);
	    subjTextView = (TextView)findViewById(R.id.subjTextView);
	    postedTextView =  (TextView)findViewById(R.id.postedTextView);
	    categoryTextView =  (TextView)findViewById(R.id.categoryTextView);
	    levelTextView =  (TextView)findViewById(R.id.levelTextView);
	    deadlineTextView =  (TextView)findViewById(R.id.deadlineTextView);
	    taskTextView =  (TextView)findViewById(R.id.taskTextView);
	    requireTextView =  (TextView)findViewById(R.id.requireTextView);
	    
	    Log.i("chosed listItem", DashboardActivityAlt.listItem.toString());
	    
	    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
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
			if (DashboardActivityAlt.listItem.getCategory().getCategoryId() != 0)
			DashboardActivityAlt.listItem.getCategory().setCategoryTitle((
					daoCategory.queryForId(DashboardActivityAlt.listItem.getCategory().getCategoryId())).getCategoryTitle());
			if (DashboardActivityAlt.listItem.getCategory().getCategorySubject().getSubjectId() != 0)
			DashboardActivityAlt.listItem.getCategory().getCategorySubject().setSubjectTitle((
					daoSubject.queryForId(DashboardActivityAlt.listItem.getCategory().getCategorySubject().getSubjectId())).getSubjectTitle());
			if (DashboardActivityAlt.listItem.getLevel() != null)
			DashboardActivityAlt.listItem.getLevel().setLevelTitle((
					daoLevel.queryForId(DashboardActivityAlt.listItem.getLevel().getLevelId()).getLevelTitle()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	    taskTextView.setText(DashboardActivityAlt.listItem.getOrderinfo());
	    requireTextView.setText(DashboardActivityAlt.listItem.getSpecInfo());
	    
	    
	    
//	    Log.i("category order",DashboardActivityAlt.listItem.getCategory().getCategoryTitle());
//	    Log.i("subject order",DashboardActivityAlt.listItem.getCategory().getCategorySubject().getSubjectTitle());
//	    Log.i("level order",DashboardActivityAlt.listItem.getLevel().getLevelTitle());
	    
	    
	    
	    btnClose.setOnClickListener(new View.OnClickListener() {
	           public void onClick(View view) {
	               Intent i = new Intent(getApplicationContext(),
	                       DashboardActivityAlt.class);
	               startActivity(i);
	               
	           }
	       });
        btnInteractions.setOnClickListener(new View.OnClickListener() {
	    	   
	           public void onClick(View view) {
	               Intent i = new Intent(getApplicationContext(),
	                       InteractionsActivityViewPager.class);
	               startActivity(i);
	               
	           }
	       });
	}
}
