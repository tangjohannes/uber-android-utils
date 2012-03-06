package com.uber.utils;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;

public class ImageCache {
	
	private static final String UBER_FOLDER = ".uber";
	
	private final HashMap<String, SoftReference<Bitmap>> mCache = new HashMap<String, SoftReference<Bitmap>>();
	private File mStorageFolder;
	private boolean mPersistentStorage = false;
	
	public ImageCache() {
		mPersistentStorage = false;
	}
	
	public ImageCache(boolean persistentStorage) {
		mPersistentStorage = persistentStorage;
	}
	
	public Bitmap getImage(String id) {
		id = id.replaceAll("/", "_");
		Bitmap bitmap = null;
		final SoftReference<Bitmap> ref = mCache.get(id);
		if (ref != null) {
			bitmap = (Bitmap) ref.get();
		}
		if (mPersistentStorage && bitmap == null) {
			final File cacheFolder = retrieveCacheFolder();
			if (cacheFolder != null) {
				bitmap = FileUtils.loadImage(cacheFolder, id);
			}
		}
		return bitmap;
	}
	
	public Bitmap getImage(long id) {
		return getImage(String.valueOf(id));
	}
	
	public void putImage(String id, Bitmap bitmap) {
		id = id.replaceAll("/", "_");
		mCache.put(id, new SoftReference<Bitmap>(bitmap));
		if (mPersistentStorage) {
			final File cacheFolder = retrieveCacheFolder();
			if (cacheFolder != null) {
				FileUtils.saveImage(cacheFolder, id, bitmap);
			}
		}
	}
	
	public void putImage(long id, Bitmap bitmap) {
		putImage(String.valueOf(id), bitmap);
	}
	
	private File retrieveCacheFolder() {
		if (mStorageFolder == null) {
			mStorageFolder = FileUtils.retrieveRootFolder(UBER_FOLDER);
		}
		return mStorageFolder;
	}

}
