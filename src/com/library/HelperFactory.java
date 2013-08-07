package com.library;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**	 *	 класс - хелпер для создания одного подключения на протяжении всего приложения. Если необходимо получить данные из базы данных, используется одно подколючение. 
 *   	 */
public class HelperFactory {
	/**	 *	поле класса com.library.DatabaseHandler	 *   	 */
	private static DatabaseHandler databaseHelper;
	/**	 *	метод, вовзращающий обьект класса com.library.DatabaseHandler	 *   	 */
	 public static DatabaseHandler GetHelper(){
	       return databaseHelper;
	   }
	 /**	 *	метод, устанавливающий хеппер классу com.library.DatabaseHandler 	 *   	 */
	public static void SetHelper(Context context){
	       databaseHelper = OpenHelperManager.getHelper(context,DatabaseHandler.class);
	   }
	/**	 *	метод для "релиза" хелпера	 *   	 */
	public static void ReleaseHelper(){
	       OpenHelperManager.releaseHelper();
	       databaseHelper = null;
	   }
}
