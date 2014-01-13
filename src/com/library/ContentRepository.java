package com.library;

import java.sql.SQLException;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
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
/**
 *	 класс для записи/чтения данных в/из базы данных. Используется библиотека ORMLite для сохранения обьектов, а не значений. 
 */
public class ContentRepository {
	  private Context _context;
	    /**
	     * конструктор класса. 
	     */
	    public ContentRepository(ContentResolver contentResolver, Context context) {
	        this._context=context;
	    }

	    public List<Category> getContacts() {
//	        ContactList contactList = new ContactList();
//	        Uri uri = ContactsContract.Contacts.CONTENT_URI;
//	        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
//	                + " COLLATE LOCALIZED ASC";
//	        Cursor cur = _contentResolver.query(uri, null, null, null, sortOrder);
//	        if (cur.getCount() > 0) {
//	            String id;
//	            String name;
//	            while (cur.moveToNext()) {
//	                
//	                id = cur.getString(cur
//	                        .getColumnIndex(ContactsContract.Contacts._ID));
//	                name = cur
//	                        .getString(cur
//	                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//	                Contact c = new Contact(id,name);
//	                contactList.addContact(c);
//	            }
//	        }
//	        cur.close();
//	        return contactList.getContacts();
	    	return null;
	    }
	    /**
	     *	 метод сохранения списка категорий в базу данных
	     *	@param categories - List обьектов Category
	     */
	    public void saveCategories(List<Category> categories) throws SQLException
	    {
	    	
	    	OrmLiteSqliteOpenHelper dbHelper=DatabaseHandler.getInstance(_context);
	    	dbHelper.getDao(Category.class);
	        for (Category contact : categories) {
	        	 HelperFactory.GetHelper().getCategoryDao().createIfNotExists(contact);
	        }
	    }
	    /**
	     *	 метод сохранения списка статусов заказа в базу данных
	     *	@param statuses - List статусов заказа
	     */
	    public void saveStatuses(List<ProcessStatus> statuses) throws SQLException
	    {
	       	OrmLiteSqliteOpenHelper dbHelper=DatabaseHandler.getInstance(_context);
	    	dbHelper.getDao(ProcessStatus.class);
	        for (ProcessStatus status : statuses) {
	        	
	            HelperFactory.GetHelper().getStatusDao().create(status);
	        }
	    }
	    /**
	     *	 метод сохранения списка Subjects в базу данных
	     *	@param subjects - List обьектов Subject
	     */
	    public void saveSubjects(List<Subject> subjects) throws SQLException
	    {
	       	OrmLiteSqliteOpenHelper dbHelper=DatabaseHandler.getInstance(_context);
	    	dbHelper.getDao(Subject.class);
	        for (Subject status : subjects) {
	        	
	            HelperFactory.GetHelper().getSubjectDao().create(status);
	        }
	    }
	    
	    /**
	     *	 метод сохранения списка Level в базу данных
	     *	@param levels - List обьектов Level
	     */
	    public void saveLevels(List<Level> levels) throws SQLException
	    {
	       	OrmLiteSqliteOpenHelper dbHelper=DatabaseHandler.getInstance(_context);
	    	dbHelper.getDao(Level.class);
	      
	    	Log.i("dao",HelperFactory.GetHelper().getSubjectDao().toString());
	        for (Level level : levels) {
	        	
	            HelperFactory.GetHelper().getLevelDao().create(level);
	        }
	    }
	    /**
	     *	 метод сохранения списка EssayType в базу данных
	     *	@param esssayTypes - List обьектов EssayType
	     */
	    public void saveEssayTypes(List<EssayType> esssayTypes) throws SQLException
	    {
	       	OrmLiteSqliteOpenHelper dbHelper=DatabaseHandler.getInstance(_context);
	    	dbHelper.getDao(EssayType.class);
	      
	    	Log.i("dao",HelperFactory.GetHelper().getSubjectDao().toString());
	        for (EssayType essayType : esssayTypes) {
	        	
	            HelperFactory.GetHelper().getEssayTypeDao().create(essayType);
	        }
	    }
	    /**
	     *	 метод сохранения списка EssayCreationStyle в базу данных
	     *	@param esssayTypes - List обьектов EssayCreationStyle
	     */
	    public void saveEssayCreationStyles(List<EssayCreationStyle> esssayCreationStyles) throws SQLException
	    {
	       	OrmLiteSqliteOpenHelper dbHelper=DatabaseHandler.getInstance(_context);
	    	dbHelper.getDao(EssayCreationStyle.class);
	      
	        for (EssayCreationStyle essayType : esssayCreationStyles) {
	        	
	            HelperFactory.GetHelper().getEssayCreationStyleDao().create(essayType);
	        }
	    }
	    /**
	     *	 метод сохранения списка NumberPages в базу данных
	     *	@param numberPages - List обьектов NumberPages
	     */
	    public void saveNumberPages(List<NumberPages> numberPages) throws SQLException
	    {
	       	OrmLiteSqliteOpenHelper dbHelper=DatabaseHandler.getInstance(_context);
	    	dbHelper.getDao(NumberPages.class);
	      
	    	Log.i("dao",HelperFactory.GetHelper().getSubjectDao().toString());
	        for (NumberPages essayType : numberPages) {
	        	
	            HelperFactory.GetHelper().getNumberPagesDao().create(essayType);
	        }
	    }
	    /**
	     *	 метод сохранения списка NumberOfReferences в базу данных
	     *	@param numbersOfRefs - List обьектов NumberOfReferences
	     */
	    public void saveNumberReferences(List<NumberOfReferences> numbersOfRefs) throws SQLException
	    {
	       	OrmLiteSqliteOpenHelper dbHelper=DatabaseHandler.getInstance(_context);
	    	dbHelper.getDao(NumberOfReferences.class);
	      
	    	Log.i("dao",HelperFactory.GetHelper().getSubjectDao().toString());
	        for (NumberOfReferences essayType : numbersOfRefs) {
	        	
	            HelperFactory.GetHelper().getNumberReferencesDao().create(essayType);
	        }
	    }
	    /**
	     *	 метод сохранения заказа в базу данных
	     *	@param order - заказ для сохранения
	     */
	    public void saveOrder(Order order) throws SQLException
	    {
	       	OrmLiteSqliteOpenHelper dbHelper=DatabaseHandler.getInstance(_context);
	    	dbHelper.getDao(Order.class);
	        HelperFactory.GetHelper().getOrderDao().create(order);
	        dbHelper.close();
	    }
}
