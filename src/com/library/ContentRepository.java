package com.library;

import java.sql.SQLException;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import com.datamodel.Category;
import com.datamodel.Level;
import com.datamodel.ProcessStatus;
import com.datamodel.Subject;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

public class ContentRepository {
	  private ContentResolver _contentResolver;

	    private Context _context;
	    public ContentRepository(ContentResolver contentResolver, Context context) {
	        this._contentResolver = contentResolver;
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

	    public void saveCategories(List<Category> contacts) throws SQLException
	    {
	    	
	    	OrmLiteSqliteOpenHelper dbHelper=DatabaseHandler.getInstance(_context);
	    	
	    	Dao<Category, Integer> daoContact=dbHelper.getDao(Category.class);
	        for (Category contact : contacts) {
	        	 HelperFactory.GetHelper().getCategoryDao().createIfNotExists(contact);
	        }
	        dbHelper.close();
	    }
	    
	    public void saveStatuses(List<ProcessStatus> statuses) throws SQLException
	    {
	       	OrmLiteSqliteOpenHelper dbHelper=DatabaseHandler.getInstance(_context);
	    	Dao<ProcessStatus, Integer> daoContact=dbHelper.getDao(ProcessStatus.class);
	       	Log.i(" ", statuses.toString());
	    	Log.i("dao",HelperFactory.GetHelper().getStatusDao().toString());
	        for (ProcessStatus status : statuses) {
	        	
	            HelperFactory.GetHelper().getStatusDao().create(status);
	        }
	        dbHelper.close();
	    }
	    
	    public void saveSubjects(List<Subject> statuses) throws SQLException
	    {
	       	OrmLiteSqliteOpenHelper dbHelper=DatabaseHandler.getInstance(_context);
	    	Dao<Subject, Integer> daoContact=dbHelper.getDao(Subject.class);
	        for (Subject status : statuses) {
	        	
	            HelperFactory.GetHelper().getSubjectDao().create(status);
	        }
	        dbHelper.close();
	    }
	    
	    
	    public void saveLevels(List<Level> levels) throws SQLException
	    {
	       	OrmLiteSqliteOpenHelper dbHelper=DatabaseHandler.getInstance(_context);
	    	Dao<Level, Integer> daoContact=dbHelper.getDao(Level.class);
	      
	    	Log.i("dao",HelperFactory.GetHelper().getSubjectDao().toString());
	        for (Level level : levels) {
	        	
	            HelperFactory.GetHelper().getLevelDao().create(level);
	        }
	        dbHelper.close();
	    }
}
