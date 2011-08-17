package com.uber.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.util.DisplayMetrics;
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
			if (contentResolver != null && imageUri != null) {
				BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri), null, options);
				if (!options.mCancel && options.outWidth != -1 && options.outHeight != -1) {
					options.inSampleSize = computeSampleSize(options.outWidth, options.outHeight, sizeTarget);
					options.inJustDecodeBounds = false;
					options.inDither = false;
					options.inPreferredConfig = Bitmap.Config.ARGB_8888;
					bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri), null, options);
				}
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

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float dpRadius, Context context) {
		if (bitmap != null) {
			float pxRadius = convertDpToPixels(dpRadius, context);
			
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(rect);

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, pxRadius, pxRadius, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);

			return output;
		}
		return null;
	}
	
	public static int convertDpToPixels(float dpValue ,Context context){
		final Double scale = context.getResources().getDisplayMetrics().density * 1.6;
		return (int) (scale * dpValue + 0.5f);
	}
}