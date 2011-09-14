package com.uber.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class ExifUtil {

	private static File mOutputDir = null;

	public static void init(Context context) {
		mOutputDir = context.getCacheDir();
	}

	public static int getImageRotation(InputStream input) throws ImageReadException, IOException {
		if (input != null) {

			// Get meta data from image and find Exif tag orientation.
			final JpegImageMetadata metaData = (JpegImageMetadata) Sanselan.getMetadata(input, "image");
			if (metaData != null) {
				final TiffField field = metaData.findEXIFValue(TiffConstants.EXIF_TAG_ORIENTATION);
				int orientation = field == null ? 1 : field.getIntValue();

				// Transform tag orientation in degree values.
				switch (orientation) {
				case TiffConstants.ORIENTATION_VALUE_ROTATE_90_CW:
					return 90;

				case TiffConstants.ORIENTATION_VALUE_ROTATE_180:
					return 180;

				case TiffConstants.ORIENTATION_VALUE_ROTATE_270_CW:
					return 270;

				}

				// Dump meta data information.
				metaData.dump();
			}
		}
		return 0;
	}

	public static Bitmap fixImageOrientation(InputStream input) {
		Bitmap returnImage = null;

		if (input != null) {
			try {

				// Create temp file to store data.
				final File tempFile;
				if (mOutputDir != null) {
					tempFile = File.createTempFile("temp-" + System.currentTimeMillis(), ".jpeg", mOutputDir);
				} else {
					tempFile = File.createTempFile("temp-" + System.currentTimeMillis(), ".jpeg");
				}
				final OutputStream outputStream = new FileOutputStream(tempFile);
				final ExifRewriter writer = new ExifRewriter();
				writer.copyStreamToStream(input, outputStream);

				// Decode image received from server.
				byte[] fileBytes = new byte[(int) tempFile.length()];
				final FileInputStream fileInputStream = new FileInputStream(tempFile);
				fileInputStream.read(fileBytes);
				final Bitmap image = BitmapFactory.decodeByteArray(fileBytes, 0, fileBytes.length);

				// FYI: If anything goes wrong it'll just return the normal
				// image.
				returnImage = image;

				// Get image rotation with inputStream from file.
				int rotation = getImageRotation(new FileInputStream(tempFile));
				if (rotation != 0) {
					final Matrix matrix = new Matrix();
					matrix.preRotate(rotation);

					// Create image with orientation fixed.
					int width = (rotation == 90 || rotation == 270) ? image.getHeight() : image.getWidth();
					int height = (rotation == 90 || rotation == 270) ? image.getWidth() : image.getHeight();
					returnImage = Bitmap.createBitmap(image, 0, 0, width, height, matrix, true);
				}
				// Delete temp file.
				if (!tempFile.delete()) {
					tempFile.deleteOnExit();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnImage;
	}
}