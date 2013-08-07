package com.asynctaskbase;
/** *интерфейс дл€ реализации методов в активност€х, позвол€ющих обработать данные после успешной/неуспешной работы AsyncTask'ов.*/
public interface ITaskLoaderListener {
	void onLoadFinished(Object data);
	void onCancelLoad();
}
