package com.uber.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.util.Log;

public class ImageUtil {

	public static final int PIXEL_MEDIUM_QUALITY = 180;
	
	public static Bitmap loadImage(String url) {
		Bitmap bm = null;
		try {
			URL aURL = new URL(url);
			URLConnection conn = aURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
		} catch (IOException e) {
			Log.e("photo.get", e.getMessage());
		}
		return bm;
	}

	public static Bitmap resizeImageFromUri(Uri imageUri, int sizeTarget, ContentResolver contentResolver) {
		Bitmap bitmap = null;

		Options options = new BitmapFactory.Options();
		options.inSampleSize = 1;
		options.inJustDecodeBounds = true;

		try {
			BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri), null, options);
			if (!options.mCancel && options.outWidth != -1 && options.outHeight != -1) {
				options.inSampleSize = computeSampleSize(options.outWidth, options.outHeight, sizeTarget);
				options.inJustDecodeBounds = false;
				options.inDither = false;
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri), null, options);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (OutOfMemoryError error) {
			System.gc();
		}
		return bitmap;
	}

	private static int computeSampleSize(float width, float height, float sizeTarget) {
		return (width > height) ? Math.round(height / sizeTarget) : Math.round(width / sizeTarget);
	}
}
