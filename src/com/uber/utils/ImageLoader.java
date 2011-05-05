package com.uber.utils;

import android.graphics.Bitmap;

public abstract class ImageLoader extends UberAsyncTask {
	
	private OnImageLoadListener mListener;
	
	public void setOnImageLoadListener(OnImageLoadListener listener) {
		mListener = listener;
	}
	
	public void addImage(long id) {
		this.addTask(id, null);
	}
	
	public void destroy() {
		mListener = null;
		removeAllTasks();
	}

	@Override
	protected void doExecute(long id, Object tag) {
		final Bitmap bitmap = getBitmap(id);
		if (bitmap != null) {
			publishProgress(id, (Object) bitmap);
		}
	}
	
	@Override
	protected void onProgressUpdate(long id, Object result) {
		if (mListener != null) {
			mListener.onImageLoaded(id, (Bitmap) result);
		}
	}
	
	protected abstract Bitmap getBitmap(long id);

}
