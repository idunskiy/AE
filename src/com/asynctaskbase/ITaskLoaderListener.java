package com.asynctaskbase;

public interface ITaskLoaderListener {
	void onLoadFinished(Object data);
	void onCancelLoad();
}
