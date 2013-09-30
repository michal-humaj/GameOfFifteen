package humaj.michal.util;

import humaj.michal.R;

import java.io.IOException;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

public class ImageUtils {

	public static final Integer[] mPictureIDs = { R.drawable.p001,
			R.drawable.p002, R.drawable.p003, R.drawable.p004, R.drawable.p005,
			R.drawable.p006, R.drawable.p007, R.drawable.p008, R.drawable.p009,
			R.drawable.p010, R.drawable.p011, R.drawable.p012, R.drawable.p013,
			R.drawable.p014, R.drawable.p015, R.drawable.p016, R.drawable.p017,
			R.drawable.p018, R.drawable.p019, R.drawable.p020, R.drawable.p021,
			R.drawable.p022, R.drawable.p023, R.drawable.p024, R.drawable.p025,
			R.drawable.p026, R.drawable.p027, R.drawable.p028, R.drawable.p029,
			R.drawable.p030, R.drawable.p031, R.drawable.p032, R.drawable.p033,
			R.drawable.p034, R.drawable.p035, R.drawable.p036, R.drawable.p037,
			R.drawable.p038, R.drawable.p039, R.drawable.p040, R.drawable.p041,
			R.drawable.p042, R.drawable.p043, R.drawable.p044, R.drawable.p045,
			R.drawable.p046, R.drawable.p047, R.drawable.p048, R.drawable.p049,
			R.drawable.p050, R.drawable.p051, R.drawable.p052, R.drawable.p053,
			R.drawable.p054, R.drawable.p055, R.drawable.p056, R.drawable.p057,
			R.drawable.p058, R.drawable.p059, R.drawable.p060, R.drawable.p061,
			R.drawable.p062, R.drawable.p063, R.drawable.p064, R.drawable.p065,
			R.drawable.p066, R.drawable.p067, R.drawable.p068, R.drawable.p069,
			R.drawable.p070, R.drawable.p071, R.drawable.p072, R.drawable.p073,
			R.drawable.p074, R.drawable.p075, R.drawable.p076, R.drawable.p077,
			R.drawable.p078, R.drawable.p079, R.drawable.p080, R.drawable.p081,
			R.drawable.p082, R.drawable.p083, R.drawable.p084, R.drawable.p085,
			R.drawable.p086, R.drawable.p087, R.drawable.p088, R.drawable.p089,
			R.drawable.p090, R.drawable.p091, R.drawable.p092, R.drawable.p093,
			R.drawable.p094, R.drawable.p095, R.drawable.p096, R.drawable.p097,
			R.drawable.p098, R.drawable.p099, R.drawable.p100, R.drawable.p101,
			R.drawable.p102, R.drawable.p103, R.drawable.p104 };

	public static final Integer[][] mSymbolsIDs = {
			{ R.drawable.s01d3, R.drawable.s02d3, R.drawable.s03d3 },
			{ R.drawable.s01d4, R.drawable.s02d4, R.drawable.s03d4 },
			{ R.drawable.s01d5, R.drawable.s02d5, R.drawable.s03d5 },
			{ R.drawable.s01d6, R.drawable.s02d6, R.drawable.s03d6 } };

	public static final Integer[] pictureThumbIDs = { R.drawable.t001,
			R.drawable.t002, R.drawable.t003, R.drawable.t004, R.drawable.t005,
			R.drawable.t006, R.drawable.t007, R.drawable.t008, R.drawable.t009,
			R.drawable.t010, R.drawable.t011, R.drawable.t012, R.drawable.t013,
			R.drawable.t014, R.drawable.t015, R.drawable.t016, R.drawable.t017,
			R.drawable.t018, R.drawable.t019, R.drawable.t020, R.drawable.t021,
			R.drawable.t022, R.drawable.t023, R.drawable.t024, R.drawable.t025,
			R.drawable.t026, R.drawable.t027, R.drawable.t028, R.drawable.t029,
			R.drawable.t030, R.drawable.t031, R.drawable.t032, R.drawable.t033,
			R.drawable.t034, R.drawable.t035, R.drawable.t036, R.drawable.t037,
			R.drawable.t038, R.drawable.t039, R.drawable.t040, R.drawable.t041,
			R.drawable.t042, R.drawable.t043, R.drawable.t044, R.drawable.t045,
			R.drawable.t046, R.drawable.t047, R.drawable.t048, R.drawable.t049,
			R.drawable.t050, R.drawable.t051, R.drawable.t052, R.drawable.t053,
			R.drawable.t054, R.drawable.t055, R.drawable.t056, R.drawable.t057,
			R.drawable.t058, R.drawable.t059, R.drawable.t060, R.drawable.t061,
			R.drawable.t062, R.drawable.t063, R.drawable.t064, R.drawable.t065,
			R.drawable.t066, R.drawable.t067, R.drawable.t068, R.drawable.t069,
			R.drawable.t070, R.drawable.t071, R.drawable.t072, R.drawable.t073,
			R.drawable.t074, R.drawable.t075, R.drawable.t076, R.drawable.t077,
			R.drawable.t078, R.drawable.t079, R.drawable.t080, R.drawable.t081,
			R.drawable.t082, R.drawable.t083, R.drawable.t084, R.drawable.t085,
			R.drawable.t086, R.drawable.t087, R.drawable.t088, R.drawable.t089,
			R.drawable.t090, R.drawable.t091, R.drawable.t092, R.drawable.t093,
			R.drawable.t094, R.drawable.t095, R.drawable.t096, R.drawable.t097,
			R.drawable.t098, R.drawable.t099, R.drawable.t100, R.drawable.t101,
			R.drawable.t102, R.drawable.t103, R.drawable.t104 };

	public static final Integer[] symbolThumbIDs = { R.drawable.st01,
			R.drawable.st02, R.drawable.st03 };

	public static final String PIC_TYPE = "PIC_TYPE";
	public static final String PICTURE = "PICTURE";
	public static final String THUMBNAIL_ID = "THUMBNAIL_ID";
	public static final String DIFFICULTY = "DIFFICULTY";
	public static final String BORDER_WIDTH = "BORDER_WIDTH";
	public static final String WIDTH = "WIDTH";

	public static final int DEFAULT_PICTURE = 1111;
	public static final int PHONE_GALLERY = 2222;
	public static final int SYMBOL = 3333;

	private static Paint up;
	private static Paint down;
	private static Paint right;
	private static Paint left;

	static {
		up = new Paint();
		up.setStrokeWidth(0);
		up.setARGB(128, 255, 255, 255);

		left = new Paint();
		left.setStrokeWidth(0);
		left.setARGB(76, 255, 255, 255);

		down = new Paint();
		down.setStrokeWidth(0);
		down.setARGB(128, 0, 0, 0);

		right = new Paint();
		right.setStrokeWidth(0);
		right.setARGB(76, 0, 0, 0);
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		int bitmapDimension = options.outHeight < options.outWidth ? options.outHeight
				: options.outWidth;
		options.inSampleSize = Math.round((float) bitmapDimension / reqWidth);
		options.inJustDecodeBounds = false;
		Bitmap b = BitmapFactory.decodeResource(res, resId, options);
		int width = b.getWidth();
		int height = b.getHeight();
		float scaleWidth;
		float scaleHeight;
		if (width > height) {
			scaleWidth = ((float) reqWidth) / width;
			scaleHeight = scaleWidth;
		} else {
			scaleHeight = ((float) reqWidth) / height;
			scaleWidth = scaleHeight;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(b, 0, 0, width, height, matrix, false);
	}

	public static Bitmap decodeSampledBitmapFromFile(String fileName,
			int reqWidth) {
		BitmapRegionDecoder decoder;
		try {
			decoder = BitmapRegionDecoder.newInstance(fileName, false);
		} catch (IOException e) {
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
		Bitmap b = decoder.decodeRegion(rect, options);
		width = b.getWidth();
		height = b.getHeight();
		float scaleWidth = ((float) reqWidth) / width;
		float scaleHeight = scaleWidth;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(b, 0, 0, width, height, matrix, false);
	}

	public static Bitmap getBitmapFromIntent(Intent intent, Resources res,
			int width, int difficulty) {
		int choice = intent.getIntExtra(PIC_TYPE, -1);
		if (choice == DEFAULT_PICTURE) {
			int position = intent.getIntExtra(PICTURE, -1);
			return ImageUtils.decodeSampledBitmapFromResource(res,
					mPictureIDs[position], width);
		} else if (choice == PHONE_GALLERY) {
			String fileName = intent.getStringExtra(PICTURE);
			return ImageUtils.decodeSampledBitmapFromFile(fileName, width);
		} else if (choice == SYMBOL) {
			int position = intent.getIntExtra(PICTURE, -1);
			return ImageUtils.decodeSampledBitmapFromResource(res,
					mSymbolsIDs[difficulty - 3][position], width);
		} else {
			return null;
		}
	}

	public static Rect getRectForTile(Rect rect, int x, int y, double tileWidth) {
		rect.left = (int) Math.round(x * tileWidth);
		rect.top = (int) Math.round(y * tileWidth);
		rect.right = (int) Math.round((x + 1) * tileWidth);
		rect.bottom = (int) Math.round((y + 1) * tileWidth);
		return rect;
	}

	public static void drawBorder(Canvas canvas, int x1, int y1, int x2,
			int y2, int borderWidth) {

		for (int k = 0; k < borderWidth; k++) {
			canvas.drawLine(x1 + k, y1 + k, x2 - 1 - k, y1 + k, up);
		}
		for (int k = 0; k < borderWidth; k++) {
			canvas.drawLine(x1 + 1 + k, y2 - k - 1, x2 - k, y2 - k - 1, down);
		}
		for (int k = 0; k < borderWidth; k++) {
			canvas.drawLine(x1 + k, y1 + 1 + k, x1 + k, y2 - k, left);
		}
		for (int k = 0; k < borderWidth; k++) {
			canvas.drawLine(x2 - k - 1, y1 + k, x2 - k - 1, y2 - 1 - k, right);
		}
	}
}
