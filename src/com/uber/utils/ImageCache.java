package com.uber.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;

public class ImageCache {
	
	private final HashMap<Long, SoftReference<Bitmap>> mCache = new HashMap<Long, SoftReference<Bitmap>>();
	
	public Bitmap getImage(long id) {
		final SoftReference<Bitmap> ref = mCache.get(id);
		if (ref != null) {
			return (Bitmap) ref.get();
		}
		return null;
	}
	
	public void putImage(long id, Bitmap bitmap) {
		mCache.put(id, new SoftReference<Bitmap>(bitmap));
	}

}
