package com.assignmentexpert;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;




import com.datamodel.Message;
import com.library.SwipeDetector;

public class InteractionsActivity extends Activity {
	 public static enum Action {
	        LR, // Left to Right
	        RL, // Right to Left
	        TB, // Top to bottom
	        BT, // Bottom to Top
	        None // when no action was detected
	    }
	 private Action mSwipeDetected = Action.None;

	    public boolean swipeDetected() {
	        return mSwipeDetected != Action.None;
	    }
	    public Action getAction() {
	        return mSwipeDetected;
	    }
	    int messageCount = 0;
	    
	private Button btnClose;
	private Button btnOrderInfo;
	private TextView interactionMessage;
	private TextView interactionBelow;
	private TextView interactionAbove;
	private TextView interactionDate;
	private TextView interactionId;
	private TextView priceLabel;
	private TextView deadlineLabel;
	private RelativeLayout messagesPanel;
	private GestureDetector gestureDetector;
	private OnTouchListener gestureListener;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interactions);
		btnClose  =(Button)findViewById(R.id.btnClose);
		btnOrderInfo = (Button)findViewById(R.id.btnInfoOrder);
		interactionMessage = (TextView)findViewById(R.id.interactionMessage);
		interactionBelow = (TextView)findViewById(R.id.interactionBelow);
		interactionAbove = (TextView)findViewById(R.id.interactionAbove);
		interactionDate = (TextView)findViewById(R.id.interactionDate);
		interactionId = (TextView)findViewById(R.id.interactionId);
		priceLabel = (TextView)findViewById(R.id.priceLabel);
		deadlineLabel = (TextView)findViewById(R.id.deadlineLabel);
		
		 messagesPanel =  (RelativeLayout) findViewById(R.id.messagesInteractions);
		 final SwipeDetector swipeDetector = new SwipeDetector();
		 
		 
		 messagesPanel.setOnTouchListener(new View.OnTouchListener() 
		 {
		        public boolean onTouch(View view, MotionEvent event) {
		            Log.d("test", "clicked!");
		            Log.d("test", swipeDetector.getAction().toString());
		            if(swipeDetector.getAction()== swipeDetector.getAction().TB)  {
		                Log.d("TB", "TopBack");
		                return true;
		            }
		            if(swipeDetector.getAction()== swipeDetector.getAction().BT)  {
		                Log.d("BT", "Back Top");
		                return true;
		            }

		            return false;
		        }
		    });
		 gestureDetector = new GestureDetector(new MyGestureListener());
	        gestureListener = new View.OnTouchListener() {
		            public boolean onTouch(View v, MotionEvent event) {
		                if (gestureDetector.onTouchEvent(event)){
		                	Log.i("qew","qwe");
		                	return true;}
		        
					return false;
		
		        }

	        };
	        messagesPanel.setOnTouchListener(gestureListener);
	        messageCount = DashboardActivityAlt.messagesExport.size();
	        this.udpateMessage();
//		 messagesPanel.setOnItemClickListener(new OnItemClickListener() {
//		          public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//		          {
//		                  if (swipeDetector.getAction()== swipeDetector.getAction().LR)
//		                  {
//		                	  Log.i("start value", "it might be started");
//		                	  view.setBackgroundColor(Color.GREEN);
//		                     Log.i("Swipe Action", getAction().toString());
//		                     TextView textOne = (TextView)findViewById(R.id.altOrderTitle);
//		                     textOne.setTextSize(25);
//		                     textOne.setText("Openening");
//		                     textOne.setGravity(Gravity.CENTER);
//		                     Order listItem = (Order) listView1.getItemAtPosition(position);
//		                     for (Message mes:listItem.getCusThread().getMessages())
//		                    	 {Log.i("get order item",mes.getMessageBody());}
//		                     messagesExport = listItem.getCusThread().getMessages();
//		                     Intent i = new Intent(getApplicationContext(),
//		                             InteractionsActivity.class);
//		                     //i.putExtra("messages", position);
//		                    // i.putParcelableArrayListExtra ("messages", listItem.getCusThread().getMessages());
//		                     startActivity(i);
//		                     
//		                    
//		                  } 
//		                  else if(!swipeDetector.swipeDetected())
//		                  {
//		                	  Log.i("stop value", "it was stoped");
//		                     //view.setBackgroundColor(Color.WHITE);
//		                  }
//		          }
//		      });
		
		btnClose.setOnClickListener(new View.OnClickListener() {
 	  
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        DashboardActivityAlt.class);
                startActivity(i);
            } 
    	});
		btnOrderInfo.setOnClickListener(new View.OnClickListener() {
		 	  
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        OrderInfoActivityAA.class);
                startActivity(i);
            } 
    	});
		for (Message mes :DashboardActivityAlt.messagesExport)
		{
			Log.i("message in this Activity",mes.getMessageBody());
			
		}
		

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	  Intent i = new Intent(getApplicationContext(),
                      DashboardActivityAlt.class);
              startActivity(i);
	    }
	    return super.onKeyDown(keyCode, event);
	}
	 class MyGestureListener extends SimpleOnGestureListener implements OnTouchListener
	    {
		
		private Activity gestureScanner;
		public boolean onDown(MotionEvent arg0) {
			Log.i("asd","sdas");
			return false;
		}
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if(Math.abs(e1.getY()-e2.getY()) > 250) 
	            return false;               
	        if(e1.getX() - e2.getX() > 120// && Math.abs(velocityX) > 200
	        		){
	        	
	 	      //mSwipeDetected = Action.RL;
	 	      Log.d("one", "Coords: x=" + e1.getX() + ",y=" + e2.getY());
	            //do something...
	 	     return true;
	        }
	        else if(e2.getX() - e1.getX() > 120// && Math.abs(velocityX) > 200
	        		){
	        	
	  	    // mSwipeDetected = Action.LR;
	  	       Log.d("two", "Coords: x=" + e1.getX() + ",y=" + e2.getY());
	            //do something...
	  	     return true;
	        }
			return false;
		}
		public void onLongPress(MotionEvent arg0) {
			 AlertDialog.Builder builder = new AlertDialog.Builder(InteractionsActivity.this);
     	builder.setMessage("Are you sure to choose timezone  "+"  ?")
  	   .setCancelable(false)
  	   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
  	       public void onClick(DialogInterface dialog, int id) {
  	           
  	       }
  	   })
  	   .setNegativeButton("No", new DialogInterface.OnClickListener() {
  	       public void onClick(DialogInterface dialog, int id) {
  	    	   
  	            dialog.cancel();
  	       }
  	   });
  	AlertDialog alert = builder.create();
  	alert.show();
			
		}
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float arg2,
				float arg3) {
		
			float pos = e2.getY() - e1.getY();
			if (pos < -220 & pos > -250)
			{
				 
				
				mSwipeDetected = Action.BT;
				
				try {
					if (messageCount > 0)
					{
						messageCount -= 1;
						udpateMessage();
						
					}
					Log.i("Interactions", "Bottom to Top moving");
				} 

			catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else if(pos > 220 & pos < 250)
				{
					 
					
					mSwipeDetected = Action.BT;
					
					try {
						if (messageCount <  DashboardActivityAlt.messagesExport.size())
						{
							messageCount += 1;
							udpateMessage();
							
						}
						Log.i("Interactions", "Top to Bottom moving");
					} 

				catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}

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
			if (arg1.getAction()==MotionEvent.ACTION_UP)
				arg0.setBackgroundColor(Color.WHITE);
			return false;
		}
	    }

	 public void udpateMessage()
	 {
		 if (!DashboardActivityAlt.messagesExport.isEmpty())
			{
				
		      Log.i("coumter", Integer.toString(messageCount));
			  interactionMessage.setText(DashboardActivityAlt.messagesExport.get(messageCount-1).getMessageBody());
			  priceLabel.setText(Float.toString(DashboardActivityAlt.messagesExport.get(messageCount-1).getPrice()));
			  interactionId.setText("Interaction  "+ Integer.toString(DashboardActivityAlt.messagesExport.get(messageCount-1).getMessageId()));
			  deadlineLabel.setText(DashboardActivityAlt.messagesExport.get(messageCount-1).getMessagedeadline().toString());
			  interactionDate.setText(DashboardActivityAlt.messagesExport.get(messageCount-1).getMessageDate().toString());
			  interactionAbove.setText(Integer.toString(messageCount-1));
			}
			else 
			{
				interactionMessage.setText("There's no messages");
			} 
		 
	 }
	    @Override
	    public boolean dispatchTouchEvent(MotionEvent ev){
	        super.dispatchTouchEvent(ev);
	        return gestureDetector.onTouchEvent(ev);
	    } 
}
