//package com.assignmentexpert;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.GestureDetector;
//import android.view.GestureDetector.SimpleOnGestureListener;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnTouchListener;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.library.InteractionsAdapter;
//import com.library.SwipeDetector.Action;
//
//public class InteractionsActivityAlt  extends Activity{
//	private Button btnClose;
//	private Button btnOrderInfo;
//	private TextView interactionMessage;
//	private TextView interactionBelow;
//	private TextView interactionAbove;
//	private TextView interactionDate;
//	private TextView interactionId;
//	private TextView priceLabel;
//	private TextView deadlineLabel;
//	private ListView listMessages;
//	private GestureDetector gestureDetector;
//	private boolean mIsScrolling;
//	private GestureDetector gestureScanner;
//	View.OnTouchListener gestureListener;
//	 private Action mSwipeDetected = Action.None;
//
//	    public boolean swipeDetected() {
//	        return mSwipeDetected != Action.None;
//	    }
//	    public Action getAction() {
//	        return mSwipeDetected;
//	    }
//	    static int position;
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		//setContentView(R.layout.interactions_list);
//		btnClose  =(Button)findViewById(R.id.btnClose);
//		btnOrderInfo = (Button)findViewById(R.id.btnInfoOrder);
//		interactionMessage = (TextView)findViewById(R.id.interactionMessage);
//		interactionBelow = (TextView)findViewById(R.id.interactionBelow);
//		interactionAbove = (TextView)findViewById(R.id.interactionAbove);
//		interactionDate = (TextView)findViewById(R.id.interactionDate);
//		interactionId = (TextView)findViewById(R.id.interactionId);
//		priceLabel = (TextView)findViewById(R.id.priceLabel);
//		deadlineLabel = (TextView)findViewById(R.id.deadlineLabel);
//		listMessages = (ListView)findViewById(R.id.interactrionList);
//		
//		position = DashboardActivityAlt.messagesExport.size();
//		InteractionsAdapter adapter = new InteractionsAdapter(InteractionsActivityAlt.this,
//    			R.layout.interactions_item, DashboardActivityAlt.messagesExport,position - 1);
//		listMessages.setAdapter(adapter);
//		gestureDetector = new GestureDetector(new MyGestureListener());
//        gestureListener = new View.OnTouchListener() {
//	            public boolean onTouch(View v, MotionEvent event) {
//	                if (gestureDetector.onTouchEvent(event)){
//	                	
//	                	return true;}
//	        
//				return false;
//	
//	        }
//
//        };
//        listMessages.setOnTouchListener(gestureListener);
//	}
//
//       
//		 class MyGestureListener extends SimpleOnGestureListener implements OnTouchListener
//		    {
//			 private float initialX = 0;  
//			 private float initialY = 0;  
//			 @Override
//		        public boolean onDoubleTap(MotionEvent e) {
////				  if (e.getY()> 70 & e.getY()<120)
////				  {
////					  Log.i("gesture moving",">70 <120");
////					  while (position >0)
////						{
////							Log.i("Move ","up moving");
////							position -= 1;
////							InteractionsAdapter adapter = new InteractionsAdapter(InteractionsActivityAlt.this,
////					    			R.layout.interactions_item, DashboardActivityAlt.messagesExport,position - 1);
////							//listMessages.setAdapter(adapter);
////							adapter.notifyDataSetChanged();
////							
////						}
////				  }
////					  else if (e.getY()>210)
////						  
////						{	
////						  Log.i("gesture moving",">210");
////							while (position < DashboardActivityAlt.messagesExport.size())
////							{
////								
////								Log.i("Down ","down moving");
////								position += 1;
////								InteractionsAdapter adapter = new InteractionsAdapter(InteractionsActivityAlt.this,
////						    			R.layout.interactions_item, DashboardActivityAlt.messagesExport,position - 1);
////								//listMessages.setAdapter(adapter);
////								adapter.notifyDataSetChanged();
////								
////							}
////						}
////				 
////		           Log.i("double Tap method","I'm in double Tap");
////		           Log.i("double Tap method",Float.toString(e.getY()));
//		            return true;
//			 }
//		        
//			public boolean onDown(MotionEvent arg0) {
//						
//		
//				
//				return true;
//			}
//			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//					float velocityY) {
//				if(Math.abs(e1.getY()-e2.getY()) > 250) 
//		            return false;               
//		        if(e1.getX() - e2.getX() > 120// && Math.abs(velocityX) > 200
//		        		){
//		        	
//		 	      //mSwipeDetected = Action.RL;
//		 	      Log.d("one", "Coords: x=" + e1.getX() + ",y=" + e2.getY());
//		            //do something...
//		 	     return true;
//		        }
//		        else if(e2.getX() - e1.getX() > 120// && Math.abs(velocityX) > 200
//		        		){
//		        	
//		  	    // mSwipeDetected = Action.LR;
//		  	       Log.d("two", "Coords: x=" + e1.getX() + ",y=" + e2.getY());
//		            //do something...
//		  	     return true;
//		        }
//				return false;
//			}
//			public void onLongPress(MotionEvent arg0) {
//				 AlertDialog.Builder builder = new AlertDialog.Builder(InteractionsActivityAlt.this);
//	        	builder.setMessage("Are you sure to choose timezone  "+"  ?")
//	     	   .setCancelable(false)
//	     	   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//	     	       public void onClick(DialogInterface dialog, int id) {
//	     	           
//	     	       }
//	     	   })
//	     	   .setNegativeButton("No", new DialogInterface.OnClickListener() {
//	     	       public void onClick(DialogInterface dialog, int id) {
//	     	    	   
//	     	            dialog.cancel();
//	     	       }
//	     	   });
//	     	AlertDialog alert = builder.create();
//	     	alert.show();
//				
//			}
//			public boolean onScroll(MotionEvent e1, MotionEvent e2, float arg2,
//					float arg3) {
//				
//				
//				float pos = e2.getY() - e1.getY();
//				if (pos < -220 & pos > -250)
//				{
//					 
//					
//					mSwipeDetected = Action.BT;
//					
//					try {
//						
//						//Log.i("gesturelistener", "Move down");
//					} 
//
//				catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//				}
//				if (pos > 170 & pos < 200)
//				{
//					 
//					mSwipeDetected = Action.BT;
//					
//					try {
//						
//						//Log.i("gesturelistener", "Move up");
//					} 
//
//				catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//				}
//
//			  	return true;
//			}
//			public void onShowPress(MotionEvent arg0) {
//				// TODO Auto-generated method stub
//				 
//			}
//			public boolean onSingleTapUp(MotionEvent e) {
//				if (e.getY()> 30 & e.getY()<210)
//				  {
//					  Log.i("gesture moving",">70 <120");
//					  if (position < DashboardActivityAlt.messagesExport.size())
//						{
//							Log.i("Move ","up moving");
//							position -= 1;
//							InteractionsAdapter adapter = new InteractionsAdapter(InteractionsActivityAlt.this,
//					    			R.layout.interactions_item, DashboardActivityAlt.messagesExport,position - 1);
//							//listMessages.setAdapter(adapter);
//							adapter.notifyDataSetChanged();
//							
//						}
//				  }
//					  else if (e.getY()>210)
//						  
//						{	
//						  Log.i("gesture moving",">210");
//						  Log.i("position",Integer.toString(InteractionsActivityAlt.position));
//							if (InteractionsActivityAlt.position >0)
//							{
//								
//								Log.i("Down ","down moving");
//								InteractionsActivityAlt.position -= 1;
//								InteractionsAdapter adapter = new InteractionsAdapter(InteractionsActivityAlt.this,
//						    			R.layout.interactions_item, DashboardActivityAlt.messagesExport,InteractionsActivityAlt.position - 1);
//								listMessages.setAdapter(adapter);
//								//adapter.notifyDataSetChanged();
//								
//							}
//						}
//				return false;
//			}
//		    
//			 public boolean onTouchEvent(MotionEvent me)
//			 {
//				 
//			 return gestureScanner.onTouchEvent(me);
//			 }
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				if (arg1.getAction()==MotionEvent.ACTION_UP)
//					arg0.setBackgroundColor(Color.WHITE);
//				return false;
//			} 
//		    }
//
//	 @Override
//	    public boolean dispatchTouchEvent(MotionEvent ev){
//	        super.dispatchTouchEvent(ev);
//	        return gestureDetector.onTouchEvent(ev);
//	    } 
//}
