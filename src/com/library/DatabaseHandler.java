
package com.library;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamodel.Category;
import com.datamodel.EssayCreationStyle;
import com.datamodel.EssayType;
import com.datamodel.Level;
import com.datamodel.NumberOfReferences;
import com.datamodel.NumberPages;
import com.datamodel.Order;
import com.datamodel.ProcessStatus;
import com.datamodel.Subject;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
/**
 *	 класс для работы с базой данных. Реализуется ORM подход.
 */
public class DatabaseHandler extends  OrmLiteSqliteOpenHelper{
	 private static final String TAG = DatabaseHandler.class.getSimpleName();
    // All Static variables
    // Database Version
	 /**
	  *	 версия базы данных
	  */
    private static final int DATABASE_VERSION = 2;
 
    // Database Name
    private static final String DATABASE_NAME = "cashdata";
	 /**	  *	 DAO обьект для доступа к Category  */
    private Dao<Category, Integer> simpleCategoryDao;
    /**	  *	 DAO обьект для доступа к Level  */
    private Dao<Level, Integer> simpleLevelDao;
    /**	  *	 DAO обьект для доступа к ProcessStatus  */
    private Dao<ProcessStatus, Integer> simpleStatusDao;
    /**	  *	 DAO обьект для доступа к Subject  */
    private Dao<Subject, Integer> simpleSubjectDao;
    /**	  *	 DAO обьект для доступа к EssayType  */
    private Dao<EssayType, Integer> simpleEssayTypetDao;
    /**	  *	 DAO обьект для доступа к EssayCreationStyle  */
    private Dao<EssayCreationStyle, Integer> simpleEssayCreationStyleDao;
    /**	  *	 DAO обьект для доступа к NumberPages  */
    private Dao<NumberPages, Integer> simpleNumberPagesDao;
    /**	  *	 DAO обьект для доступа к NumberOfReferences  */
    private Dao<NumberOfReferences, Integer> simpleNumberReferencesDao;
    /**	  *	 DAO обьект для доступа к Order  */
    private Dao<Order, Integer> simpleOrderDao;
    // Login table name
    /**	  *	 DAO обьект для доступа к Order  */
    private static final String TABLE_LOGIN = "login";
    private static final AtomicInteger usageCounter = new AtomicInteger(0);
    /**	  *	 RuntimeExceptionDao обьект, перебрасыващий RuntimeException в случае возникновения какого-либо Exception   */
    private RuntimeExceptionDao<Category, Integer> categoryRuntimeDao = null;
    private RuntimeExceptionDao<Level, Integer> levelRuntimeDao = null;
    private RuntimeExceptionDao<ProcessStatus, Integer> statusRuntimeDao = null;
    private RuntimeExceptionDao<Subject, Integer> subjectRuntimeDao = null;
    private RuntimeExceptionDao<EssayType, Integer> essayTypetRuntimeDao = null;
    private RuntimeExceptionDao<EssayCreationStyle, Integer> essayCreationStyleRuntimeDao = null;
    private RuntimeExceptionDao<NumberPages, Integer> numberPagesRuntimeDao = null;
    private RuntimeExceptionDao<NumberOfReferences, Integer> numberReferencesRuntimeDao = null;
    private RuntimeExceptionDao<Order, Integer> simpleOrderRuntimeDao = null;
    // Categories Table Columns names

    private static DatabaseHandler helper;
	private static DatabaseHandler _helperInstance;
	public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        
    }
	/**	  *	создание таблиц  */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) 
    {
    	 try
         {	
            TableUtils.createTable(connectionSource, Category.class);
            TableUtils.createTable(connectionSource, Level.class);
            TableUtils.createTable(connectionSource, ProcessStatus.class);
            TableUtils.createTable(connectionSource, Subject.class);
            TableUtils.createTable(connectionSource, EssayType.class);
            TableUtils.createTable(connectionSource, EssayCreationStyle.class);
            TableUtils.createTable(connectionSource, NumberPages.class);
            TableUtils.createTable(connectionSource, NumberOfReferences.class);
 			Log.i(DatabaseHandler.class.getName(), "created new entries in onCreate: " );
         }
         catch (SQLException e){
             Log.e(TAG, "error creating DB " + DATABASE_NAME);
             throw new RuntimeException(e);
         }
	}

	/**	  *	изменение таблиц  */
	  @Override
	   public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer, int newVer)
	  {
	       try
	        {	      
	    	   
	           TableUtils.dropTable(connectionSource, Category.class, true);
	           TableUtils.dropTable(connectionSource, Level.class, true);
	           TableUtils.dropTable(connectionSource, ProcessStatus.class, true);
	           TableUtils.dropTable(connectionSource, Subject.class,true);
	           TableUtils.dropTable(connectionSource, EssayType.class,true);
	           TableUtils.dropTable(connectionSource, EssayCreationStyle.class,true);
	           TableUtils.dropTable(connectionSource, NumberPages.class,true);
	           TableUtils.dropTable(connectionSource, NumberOfReferences.class,true);
	        //   TableUtils.dropTable(connectionSource, Order.class,true);
	           onCreate(db, connectionSource);
	       	}
	       catch (SQLException e){
	           Log.e(TAG,"error upgrading db "+DATABASE_NAME+"from ver "+oldVer);
	           throw new RuntimeException(e);
	       }
   
	   }
	
	  /**	  *	метод для создания и возврата обьекта DAO для Category
	   * 	@return возвращает обьект Dao<Category, Integer>	
	   *   */

	  public Dao<Category, Integer> getCategoryDao() throws SQLException 
	    {
			if (simpleCategoryDao == null) {
				simpleCategoryDao = getDao(Category.class);
			}
			return simpleCategoryDao;
		}
	  /**	  *	метод для создания и возврата обьекта DAO для ProcessStatus
	   * 	@return возвращает обьект Dao<ProcessStatus, Integer>	
	   *   */
	  public Dao<ProcessStatus, Integer> getStatusDao() throws SQLException 
	    {
			if (simpleStatusDao == null) {
				simpleStatusDao = getDao(ProcessStatus.class);
			}
			return simpleStatusDao;
		}
	  /**	  *	метод для создания и возврата обьекта DAO для Level
	   * 	@return возвращает обьект Dao<Level, Integer>	
	   *   */
	  public Dao<Level, Integer> getLevelDao() throws SQLException 
	    {
			if (simpleLevelDao == null) {
				simpleLevelDao = getDao(Level.class);
			}
			return simpleLevelDao;
		}
	  /**	  *	метод для создания и возврата обьекта DAO для Subject
	   * 	@return возвращает обьект Dao<Subject, Integer>	
	   *   */
	  public Dao<Subject, Integer> getSubjectDao() throws SQLException 
	    {
			if (simpleSubjectDao == null) {
				simpleSubjectDao = getDao(Subject.class);
			}
			return simpleSubjectDao;
		}
	  /**	  *	метод для создания и возврата обьекта DAO для EssayType
	   * 	@return возвращает обьект Dao<EssayType, Integer>	
	   *   */
	  public Dao<EssayType, Integer> getEssayTypeDao() throws SQLException 
	    {
			if (simpleEssayTypetDao == null) {
				simpleEssayTypetDao = getDao(EssayType.class);
			}
			return simpleEssayTypetDao;
		}
	  /**	  *	метод для создания и возврата обьекта DAO для EssayCreationStyle
	   * 	@return возвращает обьект Dao<EssayCreationStyle, Integer>	
	   *   */
	  public Dao<EssayCreationStyle, Integer> getEssayCreationStyleDao() throws SQLException 
	    {
			if (simpleEssayCreationStyleDao == null) {
				simpleEssayCreationStyleDao = getDao(EssayCreationStyle.class);
			}
			return simpleEssayCreationStyleDao;
		}
	  /**	  *	метод для создания и возврата обьекта DAO для NumberPages
	   * 	@return возвращает обьект Dao<NumberPages, Integer>	
	   *   */
	  public Dao<NumberPages, Integer> getNumberPagesDao() throws SQLException 
	    {
			if (simpleNumberPagesDao == null) {
				simpleNumberPagesDao = getDao(NumberPages.class);
			}
			return simpleNumberPagesDao;
		}
	  /**	  *	метод для создания и возврата обьекта DAO для NumberOfReferences
	   * 	@return возвращает обьект Dao<NumberOfReferences, Integer>	
	   *   */
	  public Dao<NumberOfReferences, Integer> getNumberReferencesDao() throws SQLException 
	    {
			if (simpleNumberReferencesDao == null) {
				simpleNumberReferencesDao = getDao(NumberOfReferences.class);
			}
			return simpleNumberReferencesDao;
		}
	  /**	  *	метод для создания и возврата обьекта DAO для Order
	   * 	@return возвращает обьект Dao<Order, Integer>	
	   *   */
	  public Dao<Order, Integer> getOrderDao() throws SQLException 
	    {
			if (simpleOrderDao == null) {
				simpleOrderDao = getDao(Order.class);
			}
			return simpleOrderDao;
		}
	  
	  

	  
	  	/**	  *	метод для создания и возврата обьекта DAO для Order
	   		  * @return возвращает обьект Dao<Order, Integer>	
	   *   */
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
		public RuntimeExceptionDao<EssayType, Integer> getSimpleEssayTypeDao() {
			if (essayTypetRuntimeDao == null) {
				essayTypetRuntimeDao = getRuntimeExceptionDao(EssayType.class);
			}
			return essayTypetRuntimeDao;
		}
		public RuntimeExceptionDao<EssayCreationStyle, Integer> getSimpleEssayCrStyleDao() {
			if (essayCreationStyleRuntimeDao == null) {
				essayCreationStyleRuntimeDao = getRuntimeExceptionDao(EssayCreationStyle.class);
			}
			return essayCreationStyleRuntimeDao;
		}
		public RuntimeExceptionDao<NumberPages, Integer> getSimpleNumberPagesDao() {
			if (numberPagesRuntimeDao == null) {
				numberPagesRuntimeDao = getRuntimeExceptionDao(NumberPages.class);
			}
			return numberPagesRuntimeDao;
		}
		public RuntimeExceptionDao<NumberOfReferences, Integer> getSimpleNumberReferencesDao() {
			if (numberReferencesRuntimeDao == null) {
				numberReferencesRuntimeDao = getRuntimeExceptionDao(NumberOfReferences.class);
			}
			return numberReferencesRuntimeDao;
		}
		  public RuntimeExceptionDao<Order, Integer> getSimpleOrderRuntimeDao() throws SQLException 
		    {
				if (simpleOrderRuntimeDao == null) {
					simpleOrderRuntimeDao = getDao(NumberOfReferences.class);
				}
				return simpleOrderRuntimeDao;
			}
			/**	  *	метод, предполагающий одновременное обращение, для создания и возврата обьекта DatabaseHandler
	   		  * @return возвращает DatabaseHandler
	   *   */
	  public static synchronized DatabaseHandler getHelper(Context context) 
	   {
			if (helper == null) 
			{
				helper = new DatabaseHandler(context);
			}
			usageCounter.incrementAndGet();
			return helper;
		}
	  
		/**	  * метод, позволяющий открыть соединение с базой данных
		 *   */
	  public void open() throws SQLException {
		  helper.getReadableDatabase();
		  helper.getWritableDatabase();
		  Log.i("opening", "It's might to be open");
		  
	  }
	  
	  /**	  * метод, создающий и возвращающий обьект DatabaseHandler
		 *   */
	  public static DatabaseHandler getInstance(Context context)
	    {
		  
	        if(_helperInstance==null)
	            _helperInstance=new DatabaseHandler(context);
	        
	        return _helperInstance;
	    }
	  
    /**
     * Getting user data from database
     * */
 
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
     * Пересоздание базы данных - удаление и создание всех таблиц.
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }
    /**
     * Закрытие базы данных. Обнуление всех обьетов DAO. 
     * */
    @Override
    public void close(){
        super.close();
        SQLiteDatabase db = this.getWritableDatabase();
        db.close();
      //  helper.close();
        Log.i("database closing", "All DAO's might be destroyed");
        simpleCategoryDao = null;
        simpleLevelDao = null;
        simpleStatusDao = null;
        simpleSubjectDao = null;
        simpleEssayTypetDao= null;
        simpleEssayCreationStyleDao = null;
        
        categoryRuntimeDao = null;
        levelRuntimeDao = null;
        statusRuntimeDao = null;
        subjectRuntimeDao  = null;
        essayTypetRuntimeDao = null;
        essayCreationStyleRuntimeDao = null;
    }
	
 
}