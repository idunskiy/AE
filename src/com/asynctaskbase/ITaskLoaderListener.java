package com.asynctaskbase;
/** *��������� ��� ���������� ������� � �����������, ����������� ���������� ������ ����� ��������/���������� ������ AsyncTask'��.*/
public interface ITaskLoaderListener {
	void onLoadFinished(Object data);
	void onCancelLoad();
}
