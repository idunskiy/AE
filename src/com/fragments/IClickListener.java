package com.fragments;
/** * интерфейс для переключения вложенных фрагментов */
public interface IClickListener {
	/** * метод для переключения фрагмента
	 * @param flag - параметр для смены фрагмента*/
	public void changeFragment(int flag);
}
