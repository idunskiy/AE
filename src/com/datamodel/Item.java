package com.datamodel;
/** * ����� �������� ����� ��� ���������� � ������, ������������ FileManager */
public class Item {
	
	/** * ���� � ���������� ����� */
		public String file;
		/** * ������ ����� */
		public int icon;
		/** * ����, ������������ ��� �� ������ ���� �� ������ �� �������� */
		public boolean check;
		/** * ����, ������������ ����� �� ��� ���� ��������� ������. ������ ��� ���������� ������ ������ � ������������� ����������� �����, � ����� ������ � ������. */
		public int sortId;
		/** * �������� ����������*/
		public Item(String file, Integer icon, boolean check, int sortId) {
			this.file = file;
			this.icon = icon;
			this.check = check;
			this.sortId = sortId;
		}
		/** * getter ���� �����*/
		 public String getItemFile()
		 {
		     return file;
		 }
		 /** * getter ������ �����*/
		 public int getItemIcon()
		 {
		     return icon;
		 }
		 /** * getter ��� �����, ������������� ��� �� ������ ���� �� ������ �� �������� */
		 public boolean getItemCheck()
		 {
		     return check;
		 }
		 /** * getter ��� ����� ����, ������������ ����� �� ��� ���� ��������� ������*/
		 public int getItemSortId()
		 {
		     return sortId;
		 }

		 /** * setter ��� �����, ������������� ��� �� ������ ���� �� ������ �� �������� */
		 public void setItemCheck(boolean check)
		 {
		     this.check = check;
		 }
		 /** * setter ���� ����� */
		 public void setItemFile(String file)
		 {
		     this.file = file;
		 }
		 /** * setter ������ ����� */
		 public void setItemIcon(int icon)
		 {
		     this.icon = icon;
		 }
		 /** * setter ��� ����, ������������� ����� �� ��� ���� ��������� ������ */
		 public void setItemSortId(int sortId)
		 {
			 this.sortId = sortId;
		 }
		 
		 
		 public void toggle()
		 {
			 check = !check;
		 }
		 
		 
		@Override
		public String toString() {
			return file + " "+ icon+ " "+ check;
		}
	
}
