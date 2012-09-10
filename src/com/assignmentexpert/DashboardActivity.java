package com.assignmentexpert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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

public class DashboardActivity extends Activity{
    UserFunctions userFunctions;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    protected MyGestureListener myGestureListener;
    Button btnLogout;
    private ListView ordersListView ;
	private SimpleAdapter mSchedule;
	private View Scroll;
	private GestureDetector gestureScanner;
	Button btnClose;
	Button btnNewOrder;
	private boolean mIsScrolling;
	static int start = 0;
	static int end = 10;
	
	int page = 1;
	Scroller scroller;
	static List<Order> orders;
	List<Order> forPrint = new ArrayList<Order>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        Log.i(" ",LoginActivity.orders.toString());
        
        scroller = new Scroller(getApplicationContext());
        TableRow.LayoutParams wrapWrapTableRowParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int[] fixedColumnWidths = new int[]{20, 20, 20, 20, 20};
        int[] scrollableColumnWidths = new int[]{20, 20, 20, 30, 30};
        int fixedRowHeight = 50;
        int fixedHeaderHeight = 60;
        btnClose  =(Button)findViewById(R.id.btnClose);
        btnNewOrder = (Button) findViewById(R.id.btnNewOrder);
        TableRow row = new TableRow(this);
        //header (fixed vertically)
//        TableLayout header = (TableLayout) findViewById(R.id.table_header);
//        row.setLayoutParams(wrapWrapTableRowParams);
//        row.setGravity(Gravity.CENTER);
//        row.setBackgroundColor(Color.YELLOW);
//        row.addView(makeTableRowWithText("col 1", fixedColumnWidths[0], fixedHeaderHeight));
//        row.addView(makeTableRowWithText("col 2", fixedColumnWidths[1], fixedHeaderHeight));
//        row.addView(makeTableRowWithText("col 3", fixedColumnWidths[2], fixedHeaderHeight));
//        row.addView(makeTableRowWithText("col 4", fixedColumnWidths[3], fixedHeaderHeight));
//       
//        header.addView(row);
        
        final TableLayout table = (TableLayout)findViewById(R.id.table);
       // TableRow row = (TableRow)findViewById(R.id.row);
       // Scroll = findViewById(R.id.scroll).setOnTouchListener(myGestureListener);
        gestureDetector = new GestureDetector(new MyGestureListener());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
            	
                if (gestureDetector.onTouchEvent(event)){
                	Log.i("qew","qwe");
                	return true;}
        
			return false;

        }

        };
        
        btnClose.setOnClickListener(new View.OnClickListener() {
    	       
            public void onClick(View view) {
            	moveTaskToBack(true);
            } 
    	});
        
        btnNewOrder.setOnClickListener(new View.OnClickListener() {
        	 
            public void onClick(View view) {
            	
                Intent i = new Intent(getApplicationContext(),
                        NewOrderActivity.class);
                startActivity(i);
                
            }
        });
       
         Log.i("element",forPrint.toString());
         try {
			getOrderList("1","10");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    appendRows(0,10);
    
    }
    private TextView recyclableTextView;
    public TextView makeTableRowWithText(String text, int widthInPercentOfScreenWidth, int fixedHeightInPixels) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        recyclableTextView = new TextView(this);
        recyclableTextView.setText(text);
        recyclableTextView.setTextColor(Color.BLACK);
        recyclableTextView.setTextSize(20);
        recyclableTextView.setWidth(widthInPercentOfScreenWidth * screenWidth / 100);
        recyclableTextView.setHeight(fixedHeightInPixels);
        return recyclableTextView;
    }
    public void handleScrollFinished() {
		Log.i("scrol", "Scroll was finished");
		
	}
    
    private void getOrderList(String page, String perpage) throws Exception
    {

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        try {
        	DataParsing u = new DataParsing();
        	UserFunctions n = new UserFunctions();
    		JSONObject k= n.getOrders(page,perpage);
    		
    		orders = u.wrapOrders(k);
			Dao<ProcessStatus,Integer> dao = db.getStatusDao();
			Dao<Level,Integer> daoLevel = db.getLevelDao();
			Dao<Subject,Integer> daoSubject = db.getSubjectDao();
			Dao<Category,Integer> daoCategory = db.getCategoryDao();		
			
			Log.i("status",dao.queryForAll().toString());
			Log.i("level",daoLevel.queryForAll().toString());
			Log.i("subject",daoSubject.queryForAll().toString());
			Log.i("category",daoCategory.queryForAll().toString());
			
			
			
			QueryBuilder<ProcessStatus,Integer> query = dao.queryBuilder();
			Where where = query.where();
			String result = "";
			List<String[]> results = null;
			String res[][] = null;

		    try{
		    	for(Order r : orders)
		    	{
			         GenericRawResults<String[]> rawResults = dao.queryRaw("select "+ ProcessStatus.STATUS_TITLE +" from process_status where "+ ProcessStatus.STATUS_ID + " = " + r.getProcess_status().getProccessStatusId());
			         results = rawResults.getResults();
			         String[] resultArray = results.get(0); //This will select the first result (the first and maybe only row returned)
			         result = resultArray[0]; //This will select the first field in the result (That should be the ID you need)
			         r.getProcess_status().setProccessStatusTitle(result);
			       
			         forPrint.add(r);
			         
			         //res = Integer.toString(r.getOrderid()); 		       
		         }
		    
		    }

		    catch (Exception e) {
		        e.printStackTrace();
		    }

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
   
    private void appendRows(int start,int end) {
    	final TableLayout table = (TableLayout)findViewById(R.id.table);
    	if(forPrint.size() >= end)
    	{
			for (int i=start; i<end-1; i++)
			{ 
				
				TableRow row = new TableRow(this); 
				
				TextView idText = new TextView(this);
		
				idText.setText(Integer.toString(forPrint.get(i).getOrderid()));
				
				idText.setPadding(10, 0, 0, 20);
				idText.setTextColor(Color.BLACK);
				row.addView(idText);
		
				TextView textOne = new TextView(this);
				textOne.setText(forPrint.get(i).getTitle());
				textOne.setSingleLine(false);
				textOne.setPadding(15, 0, 0, 0);
				textOne.setTextColor(Color.BLACK);
				row.addView(textOne);
		
				TextView textTwo = new TextView(this);
				textTwo.setText(Float.toString(forPrint.get(i).getPrice()));
				textTwo.setPadding(15, 0, 0, 0);
				textTwo.setTextColor(Color.BLACK);
				row.addView(textTwo);
				
				 TextView textThree = new TextView(this);
				 textThree.setText(forPrint.get(i).getProcess_status().getProccessStatusTitle());
				 textThree.setPadding(15, 0, 0, 0);
				 textThree.setTextColor(Color.BLACK);
			    row.addView(textThree);
				
			   table.addView(row, new TableLayout.LayoutParams()); 
	 	     }
	   }
    }
    class MyGestureListener extends SimpleOnGestureListener implements OnTouchListener
    {
	
	public boolean onDown(MotionEvent arg0) {
		Log.i("asd","sdas");
		return false;
	}
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(Math.abs(e1.getY()-e2.getY()) > 250) 
            return false;               
        if(e1.getX() - e2.getX() > 120 && Math.abs(velocityX) > 200){
        	Toast mToast =  Toast.makeText(getApplicationContext(), "Move Next", Toast.LENGTH_LONG);
 	       mToast.show();
 	      Log.d("one", "Coords: x=" + e1.getX() + ",y=" + e2.getY());
            //do something...
        }
        else if(e2.getX() - e1.getX() > 120 && Math.abs(velocityX) > 200){
        	Toast mToast =  Toast.makeText(getApplicationContext(), "Move Previous", Toast.LENGTH_LONG);
  	       mToast.show();
  	       Log.d("two", "Coords: x=" + e1.getX() + ",y=" + e2.getY());
            //do something...
        }
		return true;
	}
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float arg2,
			float arg3) {
		mIsScrolling = true;   
		
		float pos = e2.getY() - e1.getY();
		if (pos< -120)
		{
			Log.i("sd","I'm here");
			try {
				
				page+=1;
				start +=10;
				end+=10;
				Log.i("position down",Integer.toString(page));
				getOrderList(Integer.toString(page),"10");
				appendRows(start,end);
				
			} 
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
//		else if (pos>120)
//		{
//			Log.i("sd","I'm there");
//			try {
//				page-=1;
//				start -=10;
//				end-=10;
//				Log.i("position down",Integer.toString(page));
//				getOrderList(Integer.toString(page),"10");
//				appendRows(start,end);
//				
			//}
//	        catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			//forPrint =null;
		
		 
//	  	if (scroller.isFinished())
//	   {
//	  		if (pos<0)
//			  {
//				  start +=10;
//				  end+=10;
//			  }
//			  else 
//			  {
//				  start -=10;
//				  end -=10;  
//				  
//			  }
//	  		appendRows();
//	  		Log.i("pos",Float.toString(e1.getY()));
	  		
	  		
	   //}
	  	return true;
	}
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
    
	 public boolean onTouchEvent(MotionEvent me)
	 {
		 Log.d(null,"Touch");
	 return gestureScanner.onTouchEvent(me);
	 }
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        super.dispatchTouchEvent(ev);
        return gestureDetector.onTouchEvent(ev);
    } 
    
}