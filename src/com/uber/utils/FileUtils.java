package com.uber.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class FileUtils {
	
	public static File retrieveRootFolder(String name) {
		final File extStorage = Environment.getExternalStorageDirectory();
		if (extStorage != null) {
			return retrieveFolder(extStorage, name);
		}
		return null;
	}
	
	public static File retrieveFolder(File parent, String name) {
		File folder = null;
		if (parent != null) {
			if (parent.isDirectory()) {
				if (parent.canRead()) {
					folder = new File(parent, name);
					if (!folder.exists()) {
						folder.mkdirs();
					}
				}
			}
		}
		return folder;
	}
	
	public static Bitmap loadImage(File folder, String name) {
		Bitmap bitmap = null;
		final File imageFile = new File(folder, name);
		if (imageFile != null && imageFile.exists() && imageFile.isFile()) {
			bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
		}
		return bitmap;
	}
	
	public static boolean saveImage(File folder, String name, Bitmap bitmap) {
		boolean ret = false;
		final File imageFile = new File(folder, name);
	    FileOutputStream fos;
		try {
			fos = new FileOutputStream(imageFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			ret = true;
		    fos.flush();
		    fos.close();
		} catch (Exception e) {
			ret = false;
		}
		return ret;
	}

}
