package com.datamodel;
/** * класс описани€ файла дл€ добавлени€ в список, возвращаемом FileManager */
public class Item {
	
	/** * путь к выбранному файлу */
		public String file;
		/** * иконка файла */
		public int icon;
		/** * флаг, определ€ющий был ли выбран файл из списка на удаление */
		public boolean check;
		/** * поле, определ€ющее папка ли или файл выбранный обьект. —лужит дл€ сортировки списка файлов и приоритетному отображению папок, а затем файлов в списке. */
		public int sortId;
		/** * основной конструтор*/
		public Item(String file, Integer icon, boolean check, int sortId) {
			this.file = file;
			this.icon = icon;
			this.check = check;
			this.sortId = sortId;
		}
		/** * getter пути файла*/
		 public String getItemFile()
		 {
		     return file;
		 }
		 /** * getter иконки файла*/
		 public int getItemIcon()
		 {
		     return icon;
		 }
		 /** * getter дл€ флага, определ€ющего был ли выбран файл из списка на удаление */
		 public boolean getItemCheck()
		 {
		     return check;
		 }
		 /** * getter дл€ флага поле, определ€ющее папка ли или файл выбранный обьект*/
		 public int getItemSortId()
		 {
		     return sortId;
		 }

		 /** * setter дл€ флага, определ€ющего был ли выбран файл из списка на удаление */
		 public void setItemCheck(boolean check)
		 {
		     this.check = check;
		 }
		 /** * setter пути файла */
		 public void setItemFile(String file)
		 {
		     this.file = file;
		 }
		 /** * setter иконки файла */
		 public void setItemIcon(int icon)
		 {
		     this.icon = icon;
		 }
		 /** * setter дл€ пол€, определ€ющего папка ли или файл выбранный обьект */
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
