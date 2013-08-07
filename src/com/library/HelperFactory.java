package com.library;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**	 *	 ����� - ������ ��� �������� ������ ����������� �� ���������� ����� ����������. ���� ���������� �������� ������ �� ���� ������, ������������ ���� ������������. 
 *   	 */
public class HelperFactory {
	/**	 *	���� ������ com.library.DatabaseHandler	 *   	 */
	private static DatabaseHandler databaseHelper;
	/**	 *	�����, ������������ ������ ������ com.library.DatabaseHandler	 *   	 */
	 public static DatabaseHandler GetHelper(){
	       return databaseHelper;
	   }
	 /**	 *	�����, ��������������� ������ ������ com.library.DatabaseHandler 	 *   	 */
	public static void SetHelper(Context context){
	       databaseHelper = OpenHelperManager.getHelper(context,DatabaseHandler.class);
	   }
	/**	 *	����� ��� "������" �������	 *   	 */
	public static void ReleaseHelper(){
	       OpenHelperManager.releaseHelper();
	       databaseHelper = null;
	   }
}
