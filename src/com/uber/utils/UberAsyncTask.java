package com.uber.utils;

import java.util.Vector;

import android.os.Handler;

public abstract class UberAsyncTask implements Runnable {
	
	private Thread mThread;
	private boolean mIsRunning = false;
	private final Handler mHandler = new Handler();
	private final Vector<Task> mQueue = new Vector<Task>();
	
	public void addTask(long id, Object tag) {
		final Task task = new Task();
		task.id = id;
		task.tag = tag;
		mQueue.add(task);
		start();
	}
	
	public synchronized void start() {
		if (!mIsRunning) {
			mIsRunning = true;
			mThread = new Thread(this);
			mThread.start();
		}
	}

	public synchronized void stop() {
		mIsRunning = false;
	}
	
	public void removeAllTasks() {
		mQueue.clear();
		mIsRunning = false;
	}
	
	public void run() {
		mHandler.post(new Runnable() {
			public void run() {
				onPreExecute();
			}
		});
		while (mIsRunning) {
			if (!mQueue.isEmpty()) {
				final Task task = mQueue.remove(0);
				doExecute(task.id, task.tag);
			} else {
				mIsRunning = false;
			}
		}
		mHandler.post(new Runnable() {
			public void run() {
				onPostExecute();
			}
		});
	}
	
	protected final void publishProgress(final long id, final Object result) {
		mHandler.post(new Runnable() {
			public void run() {
				onProgressUpdate(id, result);
			}
		});
	}
	
	protected final void publishError(final long id, final Object error) {
		mHandler.post(new Runnable() {
			public void run() {
				onError(id, error);
			}
		});
	}
	
	abstract protected void doExecute(long id, Object tag);
	
	protected void onProgressUpdate(long id, Object result) {
		// To override
	}
	
	protected void onError(long id, Object error) {
		// To override
	}
	
	protected void onPreExecute() {
		// To override
	}
	
	protected void onPostExecute() {
		// To override
	}
	
	private static class Task {
		long id;
		Object tag;
	}

}
