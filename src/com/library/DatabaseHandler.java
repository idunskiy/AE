package com.library;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamodel.Category;
import com.datamodel.Level;
import com.datamodel.ProcessStatus;
import com.datamodel.Subject;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHandler extends  OrmLiteSqliteOpenHelper{
	 private static final String TAG = DatabaseHandler.class.getSimpleName();
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
 
    // Database Name
    private static final String DATABASE_NAME = "cashdata";
 
    private Dao<Category, Integer> simpleCategoryDao;
    private Dao<Level, Integer> simpleLevelDao;
    private Dao<ProcessStatus, Integer> simpleStatusDao;
    private Dao<Subject, Integer> simpleSubjectDao;
    // Login table name
    private static final String TABLE_LOGIN = "login";
    private static final AtomicInteger usageCounter = new AtomicInteger(0);
    
    private RuntimeExceptionDao<Category, Integer> categoryRuntimeDao = null;
    private RuntimeExceptionDao<Level, Integer> levelRuntimeDao = null;
    private RuntimeExceptionDao<ProcessStatus, Integer> statusRuntimeDao = null;
    private RuntimeExceptionDao<Subject, Integer> subjectRuntimeDao = null;
    //private Dao<Category, Integer> categoryDao;
    // Categories table name
   // private CategoryDAO categoryDao = null;
	private Context _context;
	
    private static final String TABLE_CATEGORIES = "categories";
     // Categories Table Columns names
    private static final String KEY_CATEGORIES_ID = "id";
    private static final String KEY_CATEGORIES_TITLE = "title";

    // Subjects table name
    private static final String TABLE_SUBJECTS = "subjects";
    // Subjects Table Columns names
    private static final String KEY_SUBJECTS_ID = "id";
    private static final String KEY_SUBJECTS_TITLE = "title";
    
    // Process statuses table name
    private static final String TABLE_PROCESS_STATUS = "process_statuses";
    // Process statuses Table Columns names
    private static final String KEY_STATUS_ID = "id";
    private static final String KEY_STATUS_TITLE = "title";
    private static final String KEY_STATUS_IDENTIFICATOR = "identificator";
    
    // Process statuses table name
    private static final String TABLE_LEVELS = "levels";
    // Process statuses Table Columns names
    private static final String KEY_LEVELS_ID = "id";
    private static final String KEY_LEVELS_TITLE = "title";
    
    
    private static DatabaseHandler helper;
	private static DatabaseHandler _helperInstance;
	private SQLiteDatabase myDataBase;
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        
    }
    // Creating Tables
    @Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) 
    {
    	 try
         {	
    		
            TableUtils.createTable(connectionSource, Category.class);
            TableUtils.createTable(connectionSource, Level.class);
            TableUtils.createTable(connectionSource, ProcessStatus.class);
            TableUtils.createTable(connectionSource, Subject.class);
 			Log.i(DatabaseHandler.class.getName(), "created new entries in onCreate: " );
         }
         catch (SQLException e){
             Log.e(TAG, "error creating DB " + DATABASE_NAME);
             throw new RuntimeException(e);
         }
	}

    // Upgrading database
	  @Override
	   public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer, int newVer)
	  {
	       try
	        {	      
	    	   
	           TableUtils.dropTable(connectionSource, Category.class, true);
	           TableUtils.dropTable(connectionSource, Level.class, true);
	           TableUtils.dropTable(connectionSource, ProcessStatus.class, true);
	           TableUtils.dropTable(connectionSource, Subject.class,true);
	           onCreate(db, connectionSource);
	       	}
	       catch (SQLException e){
	           Log.e(TAG,"error upgrading db "+DATABASE_NAME+"from ver "+oldVer);
	           throw new RuntimeException(e);
	       }
   
	   }
	

	  public Dao<Category, Integer> getCategoryDao() throws SQLException 
	    {
			if (simpleCategoryDao == null) {
				simpleCategoryDao = getDao(Category.class);
			}
			return simpleCategoryDao;
		}

	  public Dao<ProcessStatus, Integer> getStatusDao() throws SQLException 
	    {
			if (simpleStatusDao == null) {
				simpleStatusDao = getDao(ProcessStatus.class);
			}
			return simpleStatusDao;
		}
	  
	  public Dao<Level, Integer> getLevelDao() throws SQLException 
	    {
			if (simpleLevelDao == null) {
				simpleLevelDao = getDao(Level.class);
			}
			return simpleLevelDao;
		}
	  
	  public Dao<Subject, Integer> getSubjectDao() throws SQLException 
	    {
			if (simpleSubjectDao == null) {
				simpleSubjectDao = getDao(Subject.class);
			}
			return simpleSubjectDao;
		}
	  
//		/**
//		 * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
//		 * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
//		 */
	  
		public RuntimeExceptionDao<Category, Integer> getSimpleCategoryDao() {
			if (categoryRuntimeDao == null) {
				categoryRuntimeDao = getRuntimeExceptionDao(Category.class);
			}
			return categoryRuntimeDao;
		}
		
		public RuntimeExceptionDao<Subject, Integer> getSimpleSubjectDao() {
			if (subjectRuntimeDao == null) {
				subjectRuntimeDao = getRuntimeExceptionDao(Subject.class);
			}
			return subjectRuntimeDao;
		}
  
		public RuntimeExceptionDao<Level, Integer> getSimpleLevelDao() {
			if (levelRuntimeDao == null) {
				levelRuntimeDao = getRuntimeExceptionDao(Level.class);
			}
			return levelRuntimeDao;
		}
		
		public RuntimeExceptionDao<ProcessStatus, Integer> getSimpleStatusDao() {
			if (statusRuntimeDao == null) {
				statusRuntimeDao = getRuntimeExceptionDao(ProcessStatus.class);
			}
			return statusRuntimeDao;
		}
		
		
	  public static synchronized DatabaseHandler getHelper(Context context) 
	   {
			if (helper == null) 
			{
				helper = new DatabaseHandler(context);
			}
			usageCounter.incrementAndGet();
			return helper;
		}
	  

	  public void open() throws SQLException {
		  helper.getReadableDatabase();
		  helper.getWritableDatabase();
		  Log.i("opening", "It's might to be open");
		  
	  }
	  
//	  private ConnectionSource connectionSource = null;
//	  @Override
//	  public ConnectionSource getConnectionSource() {
//	      if (connectionSource == null) {
//	          connectionSource = super.getConnectionSource();
//	      }
//	      return connectionSource;
//	  }
	  private ConnectionSource connectionSource = null;
	
	  @Override
	  public ConnectionSource getConnectionSource() {
	      if (connectionSource == null) {
	          connectionSource = super.getConnectionSource();
	      }
	      return connectionSource;
	  }
	  public static DatabaseHandler getInstance(Context context)
	    {
		  
	        if(_helperInstance==null)
	            _helperInstance=new DatabaseHandler(context);
	        
	        return _helperInstance;
	    }
	  
	   	  
    public void addUser(String name, String email, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

 
        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }
    
    public void addCategories(Category id,Category title)
    {
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(KEY_CATEGORIES_ID, id.toString()); // ID
        values.put(KEY_CATEGORIES_TITLE, title.toString()); // TITLE
     // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
        
    }
    
 
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }
 
    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
 
        // return row count
        return rowCount;
    }
 
    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }

    @Override
    public void close(){
        super.close();
        Log.i("database closing", "All DAO's might be destroyed");
        simpleCategoryDao = null;
        simpleLevelDao = null;
        simpleStatusDao = null;
        simpleSubjectDao = null;
        
        categoryRuntimeDao = null;
        levelRuntimeDao = null;
        statusRuntimeDao = null;
        subjectRuntimeDao  = null;
    }
	
 
}