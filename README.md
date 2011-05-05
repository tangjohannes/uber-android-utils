Uber Android Utils
==================

Quick description
-----------------
This repository offers a set of random useful classes to deal with asynchronous tasks and images. For example, if you have a list of pictures whose bitmaps are stored online you should really consider using this library. This is really the first draft and I'm planning on adding a lot of other generic utilities. I'm also counting on you to extend it :)

Usage
-----
Why having a UberAsyncTask when we can use the AsyncTask class from the Android SDK? Two reasons:
 - UberAsyncTask makes it possible to queue requests
 - UberAsyncTask does not require to create a new instance after the execution has finished

The ImageLoader is a good example of how UberAsyncTask should be used. You extend it, add your custom addTask method and bind a listener to it.

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