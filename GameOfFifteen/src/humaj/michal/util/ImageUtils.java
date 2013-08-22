package humaj.michal.util;

import humaj.michal.R;

import java.io.IOException;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;

public class ImageUtils {

	public static final Integer[] mPictureIDs = { R.drawable.p001,
			R.drawable.p002, R.drawable.p003, R.drawable.p004, R.drawable.p005,
			R.drawable.p006, R.drawable.p007, R.drawable.p008, R.drawable.p009,
			R.drawable.p010, R.drawable.p011, R.drawable.p012, R.drawable.p013,
			R.drawable.p014, R.drawable.p015, R.drawable.p016, R.drawable.p017,
			R.drawable.p018, R.drawable.p019, R.drawable.p020 };

	public static final Integer[][] mSymbolsIDs = {
			{ R.drawable.s01d3, R.drawable.s02d3 },
			{ R.drawable.s01d4, R.drawable.s02d4 },
			{ R.drawable.s01d5, R.drawable.s02d5 },
			{ R.drawable.s01d6, R.drawable.s02d6 } };

	public static final int DEFAULT_PICTURE = 1111;
	public static final int PHONE_GALLERY = 2222;
	public static final int SYMBOL = 3333;

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {

		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static Bitmap decodeSampledBitmapFromFile(String fileName,
			int reqWidth, int reqHeight) {
		BitmapRegionDecoder decoder;
		try {
			decoder = BitmapRegionDecoder.newInstance(fileName, false);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		int width = decoder.getWidth();
		int height = decoder.getHeight();
		int regionWidth = width < height ? width : height;
		int x1 = (width - regionWidth) / 2;
		int y1 = (height - regionWidth) / 2;
		final Rect rect = new Rect(x1, y1, x1 + regionWidth, y1 + regionWidth);

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = Math.round((float) regionWidth / reqWidth);
		return decoder.decodeRegion(rect, options);
	}

	/*
	 * public static Bitmap decodeSampledBitmapFromFile(String fileName, int
	 * reqWidth, int reqHeight) {
	 * 
	 * // First decode with inJustDecodeBounds=true to check dimensions final
	 * BitmapFactory.Options options = new BitmapFactory.Options();
	 * options.inJustDecodeBounds = true; BitmapFactory.decodeFile(fileName,
	 * options);
	 * 
	 * // Calculate inSampleSize options.inSampleSize =
	 * calculateInSampleSize(options, reqWidth, reqHeight);
	 * 
	 * // Decode bitmap with inSampleSize set options.inJustDecodeBounds =
	 * false; return BitmapFactory.decodeFile(fileName, options); }
	 */

	public static Bitmap getBitmapFromIntent(Intent intent, Resources res, int width,
			int difficulty) {
		int choice = intent.getIntExtra("CHOICE", -1);
		if (choice == DEFAULT_PICTURE) {
			int position = intent.getIntExtra("PICTURE", -1);
			return ImageUtils.decodeSampledBitmapFromResource(res,
					mPictureIDs[position], width, width);
		} else if (choice == PHONE_GALLERY) {
			String fileName = intent.getStringExtra("PICTURE");
			return ImageUtils.decodeSampledBitmapFromFile(fileName, width,
					width);
		} else if (choice == SYMBOL) {
			int position = intent.getIntExtra("PICTURE", -1);
			return ImageUtils.decodeSampledBitmapFromResource(res,
					mSymbolsIDs[difficulty - 3][position], width, width);
		} else {
			return null;
		}
	}
}
