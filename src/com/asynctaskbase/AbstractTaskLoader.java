package com.asynctaskbase;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.AsyncTaskLoader;
/** *родительский класс для выполнения AsyncTask'ов .*/
public abstract class AbstractTaskLoader extends AsyncTaskLoader<Object> {
	
	//type of the published values
	public static int MSGCODE_PROGRESS = 1;
	public static int MSGCODE_MESSAGE = 2;
	
	private Handler handler;
	
	//custom canceled flag
	private static boolean canseled = false;
	
	public abstract Bundle getArguments();
	public abstract void setArguments(Bundle args);
	
	protected AbstractTaskLoader(Context context) {
		super(context);
		
	}
	protected void setHandler(Handler handler){
		this.handler = handler;
	}
	
	/**
	 * Helper получения integer значения из сообщения
	 * @param msg - сообщения прогресса
	 * @return 
	 */
	public static int getProgressValue(Message msg){
		return msg.arg1;
	}
	
	/**
	 * Helper получения string значения из сообщения
	 * @param msg - сообщение прогресса
	 * @return
	 */
	public static String getMessageValue(Message msg){
		Bundle data = msg.getData();
		if(data.containsKey("message")){
			return data.getString("message");
		}else{
			return null;
		}
	}
	
	/**
	 * Helper публикования string значений
	 * @param value - строка 
	 */
	protected void publishMessage(String value){
		
		if(handler!=null){
		
			Bundle data = new Bundle();
			data.putString("message", value);
			
			/* Creating a message */
			Message msg = new Message();
			msg.setData(data);
			msg.what = MSGCODE_MESSAGE; 
			
			/* Sending the message */
			handler.sendMessage(msg);
			
		}
		
	}
	
	/**
	 * Helper публикования string значений
	 * @param value
	 */
	protected void publishProgress(int value){
		
		if(handler!=null){
			
			/* Creating a message */
			Message msg = new Message();
			msg.what = MSGCODE_PROGRESS; 
			msg.arg1 = value;
			
			/* Sending the message */
			handler.sendMessage(msg);
			
		}
	
	}
	
	/**
	 * метод установки флага на cancel операции
	 * @param canselled - флаг, устанавливающий отменение выполнения операции
	 */
	public void setCanseled(boolean canselled){
		AbstractTaskLoader.canseled = canselled;
		if (canselled)
			this.cancelLoad();
	}
	/**
	 * метод проверки или операция была прервана
	 * @param флаг cancel'a операции
	 */
	public static boolean isCanselled() {
		return canseled;
	}

}
