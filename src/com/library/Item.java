package com.library;

public class Item {
	

		public String file;
		public int icon;
		public boolean check;

		public Item(String file, Integer icon, boolean check) {
			this.file = file;
			this.icon = icon;
			this.check = check;
		}
		 public String getItemFile()
		 {
		     return file;
		 }

		 public int getItemIcon()
		 {
		     return icon;
		 }
		 public boolean getItemCheck()
		 {
		     return check;
		 }


		 public void setItemCheck(boolean check)
		 {
		     this.check = check;
		 }

		 public void setItemFile(String file)
		 {
		     this.file = file;
		 }
		 public void setItemIcon(int icon)
		 {
		     this.icon = icon;
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
