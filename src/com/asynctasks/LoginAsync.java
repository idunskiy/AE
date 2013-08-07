package com.asynctasks;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.assignmentexpert.LoginActivity;
import com.asynctaskbase.AbstractTaskLoader;
import com.asynctaskbase.ITaskLoaderListener;
import com.asynctaskbase.TaskProgressDialogFragment;
import com.datamodel.Category;
import com.datamodel.Customer;
import com.datamodel.EssayCreationStyle;
import com.datamodel.EssayType;
import com.datamodel.Level;
import com.datamodel.NumberOfReferences;
import com.datamodel.NumberPages;
import com.datamodel.Order;
import com.datamodel.ProcessStatus;
import com.datamodel.Subject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.library.ContentRepository;
import com.library.DataParsing;
import com.library.DatabaseHandler;
import com.library.FrequentlyUsedMethods;
import com.library.RestClient;
import com.library.UserFunctions;
/** * AsyncTask для логина пользователя. Получение массивов категорий, статусов, тем заказов, иформации о пользователе. */
public class LoginAsync  extends AbstractTaskLoader {
	private static String KEY_STATUS = "status";
    private static String KEY_MESSAGE = "message";
    private static String KEY_DATA = "data";
    public DatabaseHandler databaseHandler=null;
	public static String loginErrorMess;
	static Customer getUser;
	Context context;
	JSONObject json;
	public static String passUserId = null;
	
	public static  List<NumberPages> numberPagesList;
	public static  List<NumberOfReferences> numberReferencesList;
	
	
    SharedPreferences.Editor editor;
    static List<Order> orders;
    boolean signMe = false;
	private boolean errorFlag = false;
//    SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
//    editor = sharedPreferences.edit();
	/** *метод вызова выполнения запроса из активностей*/
	public static void execute(FragmentActivity fa,	ITaskLoaderListener taskLoaderListener) {

		LoginAsync loader = new LoginAsync(fa);

		new TaskProgressDialogFragment.Builder(fa, loader, "Loading…")
				.setCancelable(false)
				.setTaskLoaderListener(taskLoaderListener)
				.show();
	}
	
	public LoginAsync(Context context) {
		super(context);
		this.context = context;
	}
	/** *метод выполнения запроса*/
	@Override
	public Object loadInBackground() {
		
		ContentRepository _contactRepo;
		RestClient helper = new RestClient(context);
	    helper.setContextRest(context);
	    String result = null;
    	UserFunctions userFunction = new UserFunctions();
    	try {
    		 
			json = userFunction.loginUser(LoginActivity.forFragmentLogin, LoginActivity.forFragmentPassword);

        	if ((json.getString(KEY_STATUS) != null))
        	{
        		
                String res = json.getString(KEY_STATUS);
                if(Integer.parseInt(res) == 1)
                {	
                	//LoginActivity.appendLog(json.getString(KEY_DATA));
                	DataParsing u = new DataParsing();
                	LoginActivity.passUserId = u.wrapUserId(json);
            		List<Category> catlist = u.wrapCategories(json);
            		List<ProcessStatus> status = u.wrapStatuses(json);
            		List<Level> level = u.wrapLevels(json);
            		List<Subject> subject = u.wrapSubjects(json);
            		List<EssayType> essayTypes = u.wrapEssayType(json);
            		List<EssayCreationStyle> essayCreatStyle = u.wrapEssayCreationStyle(json);
            		numberPagesList = u.wrapNumberPages(json);
            		numberReferencesList = u.wrapNumberReferences(json);
            		
            		
            		LoginActivity.getUser = u.wrapUser(json);
            		try{
            		_contactRepo=new ContentRepository(context.getContentResolver(),context);

            		DatabaseHandler dbHelper=getHelper1();
            		
            		dbHelper.open();
            		dbHelper.getWritableDatabase();
            		Dao<Subject, Integer> daoSubject=dbHelper.getDao(Subject.class);
            		QueryBuilder<Subject,Integer> Subjectquery = daoSubject.queryBuilder();
            		try
            		{
            			
            		   	if (Subjectquery.query().isEmpty())
            		   		_contactRepo.saveSubjects(subject);
            		   	else Log.i("subjects",daoSubject.queryForAll().toString());
            		}
            		catch (IllegalStateException e)
            		{
            			dbHelper.open();			
            			e.printStackTrace();
            			
            		}
            		
            		
            		Dao<Category, Integer> daoCat=dbHelper.getDao(Category.class);
            		QueryBuilder<Category,Integer> catquery = daoCat.queryBuilder();
            		try{
            			dbHelper.open();	
            			if (catquery.query().isEmpty())
            			{
            				_contactRepo.saveCategories(catlist);
            			}
            		}
            		catch (IllegalStateException e)
            		{
            			dbHelper.open();			
            			e.printStackTrace();
            		}
            		Dao<ProcessStatus, Integer> daoStatus=dbHelper.getDao(ProcessStatus.class);
            		QueryBuilder<ProcessStatus,Integer> Statusquery = daoStatus.queryBuilder();
            		try{
            			if (Statusquery.query().isEmpty())
            			 _contactRepo.saveStatuses(status);
            			else Log.i("statuses in database",daoStatus.queryForAll().toString());
            		}
            		catch (IllegalStateException e)
            		{
            			dbHelper.open();			
            			e.printStackTrace();
            			
            		}
            		

            		Dao<Level, Integer> daoLevel=dbHelper.getDao(Level.class);
            		QueryBuilder<Level,Integer> levelquery = daoLevel.queryBuilder();
            		try
            		{
            			
            			if (levelquery.query().isEmpty())
            				_contactRepo.saveLevels(level);
            		}
            		catch (IllegalStateException e)
            		{
            			getHelper1().open();			
            			e.printStackTrace();
            			
            		}
            		
            		
            		Dao<EssayType, Integer> daoEssayType=dbHelper.getDao(EssayType.class);
            		QueryBuilder<EssayType,Integer> essayTypequery = daoEssayType.queryBuilder();
            		try
            		{
            			
            			if (essayTypequery.query().isEmpty())
            				_contactRepo.saveEssayTypes(essayTypes);
            		}
            		catch (IllegalStateException e)
            		{
            			getHelper1().open();			
            			e.printStackTrace();
            			
            		}
            		
            		Dao<EssayType, Integer> daoEssayCreationStyle=dbHelper.getDao(EssayCreationStyle.class);
            		QueryBuilder<EssayType,Integer> essayCrStyleQuery= daoEssayCreationStyle.queryBuilder();
            		try
            		{
            			
            			if (essayCrStyleQuery.query().isEmpty())
            				_contactRepo.saveEssayCreationStyles(essayCreatStyle);
            		}
            		catch (IllegalStateException e)
            		{
            			getHelper1().open();			
            			e.printStackTrace();
            			
            		}
            		}
            		catch(IllegalStateException e)
            		{
            			e.printStackTrace();
            		}
//            		Dao<NumberPages, Integer> daoNumberPages=dbHelper.getDao(NumberPages.class);
//            		QueryBuilder<NumberPages,Integer> numberPage= daoNumberPages.queryBuilder();
//            		try
//            		{
//            			
//            			if (numberPage.query().isEmpty())
//            				_contactRepo.saveNumberPages(numberPages);
//            		}
//            		catch (IllegalStateException e)
//            		{
//            			getHelper1().open();			
//            			e.printStackTrace();
//            			
//            		}
//            		Log.i("number Pages",daoNumberPages.queryForAll().toString());
//            		
//            		Dao<NumberPages, Integer> daoNumberReferences=dbHelper.getDao(NumberOfReferences.class);
//            		QueryBuilder<NumberPages,Integer> numberReference= daoNumberReferences.queryBuilder();
//            		try
//            		{
//            			
//            			if (numberReference.query().isEmpty())
//            				_contactRepo.saveNumberReferences(numberReferences);
//            		}
//            		catch (IllegalStateException e)
//            		{
//            			getHelper1().open();			
//            			e.printStackTrace();
//            			
//            		}
//            		Log.i("number References",daoNumberPages.queryForAll().toString());
            		result = "success";
            		publishMessage("Done successfully");
                 }
                else
             	
                {
                	
                	Log.i("LoginAsync", "it might be canceled");
//                	new FrequentlyUsedMethods(LoginActivity._context).someMethod("Error occurs");
                	result = "error";
                	if (json.getString(KEY_MESSAGE).equalsIgnoreCase("Incorrect login or password"))
                		loginErrorMess =json.getString(KEY_MESSAGE);
                	else
             		loginErrorMess ="Something went wrong. Please try later.";// json.getString(KEY_MESSAGE);
             		//Toast.makeText(context, loginErrorMess, Toast.LENGTH_LONG).show();
             		//this.setCanseled(true);
             		//TaskProgressDialogFragment.cancel();
             		
             	}
      	}
        	
     }
      catch (JSONException e) {
        e.printStackTrace();
    } catch (SQLException e) {
		e.printStackTrace();
	} catch (Exception e) {
		
		e.printStackTrace();
		errorFlag  = true;
		onStopLoading();
	} 

         Log.i("LoginAsync result", result);
        return result;
	}
	

	@Override
	public Bundle getArguments() {
		return null;
	}

	@Override
	public void setArguments(Bundle args) {
		
	}
	
	 private DatabaseHandler getHelper1() {
			if (databaseHandler == null) {
				databaseHandler = DatabaseHandler.getHelper(context.getApplicationContext());
			}
			return (DatabaseHandler) databaseHandler;
		}
	 @Override 
	 protected void onStopLoading() {
	         this.setCanseled(true);
	         TaskProgressDialogFragment.cancel();
	         if (errorFlag)
	        new FrequentlyUsedMethods(context).someMethod("Something went wrong. Please try later.");
	        cancelLoad();
	    }
	

}
