package com.assignmentexpert;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datamodel.Category;
import com.datamodel.Level;
import com.datamodel.ProductWriting;
import com.datamodel.Subject;
import com.j256.ormlite.dao.Dao;
import com.library.DatabaseHandler;

public class OrderInfoActivityEW extends Activity{
	private Button btnClose;
	private Button btnInteractions;
	private TextView btnInfoOrder;
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
	private TextView typeTextView;
	private TextView citationTextView;
	private TextView referenceTextView;
	private TextView pagesTextView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_info_ew);
        btnClose = (Button) findViewById(R.id.btnClose);
        
	    btnInteractions = (Button) findViewById(R.id.btnInteractions);
	    btnInfoOrder = (Button) findViewById(R.id.btnInfoOrder);
	    
	    productTextView = (TextView)findViewById(R.id.productTextView);
	    priceTextView =   (TextView)findViewById(R.id.priceTextView);
	    timezoneTextView = (TextView)findViewById(R.id.timezoneTextView);
	    subjTextView = (TextView)findViewById(R.id.subjTextView);
	    postedTextView =  (TextView)findViewById(R.id.postedTextView);
	    categoryTextView =  (TextView)findViewById(R.id.categoryTextView);
	    levelTextView =  (TextView)findViewById(R.id.levelTextView);
	    deadlineTextView =  (TextView)findViewById(R.id.deadlineTextView);
	    taskTextView =  (TextView)findViewById(R.id.taskTextView);
	    requireTextView =  (TextView)findViewById(R.id.requireTextView);
	    typeTextView =  (TextView)findViewById(R.id.typeTextView);
	    layout = (LinearLayout)findViewById(R.id.infoMessageListLayout);
	    
	    citationTextView = (TextView)findViewById(R.id.citationTextView);
	    referenceTextView = (TextView)findViewById(R.id.referenceTextView);
	    pagesTextView = (TextView)findViewById(R.id.pagesTextView);
	    
	    
	    
	   
	    
	    
	    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
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
	    Log.i("listItem in orderInfoEW", DashboardActivityAlt.listItem.toString());
	    addFiles();
	    
//	    btnClose.setOnClickListener(new View.OnClickListener() {
//	           public void onClick(View view) {
//	               Intent i = new Intent(getApplicationContext(),
//	                       DashboardActivityAlt.class);
//	               startActivity(i);
//	               
//	           }
//	       });
//        btnInteractions.setOnClickListener(new View.OnClickListener() {
//	    	   
//	           public void onClick(View view) {
//	               Intent i = new Intent(getApplicationContext(),
//	                       InteractionsActivityViewPager.class);
//	               startActivity(i);
//	               
//	           }
//	       });
	}
	public void addFiles()
	 {
		try
		{
	    if(!DashboardActivityAlt.listItem.getOrderFiles().isEmpty())
	    {
	    	TextView tv[] = new TextView[DashboardActivityAlt.listItem.getOrderFiles().size()];
		         for (int i = 0; i< DashboardActivityAlt.listItem.getOrderFiles().size();i++)
		          {
		            
		            	View line = new View(this);
		                line.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
		                line.setBackgroundColor(0xAA345556);
		                tv[i] = new TextView(this);
		                tv[i].setId(i);
		                tv[i].setTag(i);
		                tv[i].setTextColor(Color.BLACK);
		                tv[i].setTextSize(12);
		                tv[i].setCompoundDrawablesWithIntrinsicBounds(
		                        0, R.drawable.file_icon, 0, 0);
		                tv[i].setText(DashboardActivityAlt.listItem.getOrderFiles().get(i).getFileName());
		                layout.addView(tv[i], 0);
		                layout.addView(line, 1);
		                
	
		          }
		       
	      }
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public void onResume()
	{
		super.onResume();
		 InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}
	
	@Override
    public void onBackPressed() {
		Intent i = new Intent(getApplicationContext(),
                DashboardActivityAlt.class);
        startActivity(i);
    }
	
}

