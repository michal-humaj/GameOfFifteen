package humaj.michal.activity;

import humaj.michal.R;
import humaj.michal.util.ImageUtils;
import humaj.michal.util.SquareImageView;
import humaj.michal.util.TiledSquareImageView;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;

public class MainActivity extends Activity {

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

	private TiledSquareImageView mIV;
	private CheckBox checkBox3x3;
	private CheckBox checkBox4x4;
	private CheckBox checkBox5x5;
	private CheckBox checkBox6x6;

	Intent mIntent;

	private Bitmap mGameBitmap = null;
	private int mDifficulty;
	private int mWidth;
	private int mBorderWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mIV = (TiledSquareImageView) findViewById(R.id.iv);
		checkBox3x3 = (CheckBox) findViewById(R.id.checkBox3x3);
		checkBox4x4 = (CheckBox) findViewById(R.id.checkBox4x4);
		checkBox5x5 = (CheckBox) findViewById(R.id.checkBox5x5);
		checkBox6x6 = (CheckBox) findViewById(R.id.checkBox6x6);
		setPicWidthAndTileBorderWidth();
		Intent intent = getIntent();
		mDifficulty = intent.getIntExtra("DIFFICULTY", -1);
		mIntent = getIntent();
		mGameBitmap = getBitmapFromIntent(intent);
		if (mGameBitmap == null) {
			Random random = new Random(System.currentTimeMillis());
			mDifficulty = random.nextInt(4) + 3;
			int randomIndex = random.nextInt(mPictureIDs.length);
			mGameBitmap = ImageUtils.decodeSampledBitmapFromResource(
					getResources(), mPictureIDs[randomIndex], mWidth, mWidth);
		}
		checkRightCheckBox();
		mIV.setImageBitmap(mGameBitmap);
		mIV.setDifficulty(mDifficulty);
		mIV.setBorderWidth(mBorderWidth);
	}

	@Override
	protected void onDestroy() {
		mGameBitmap.recycle();
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		mIntent = intent;
		mGameBitmap.recycle();
		mGameBitmap = getBitmapFromIntent(intent);
		mIV.setImageBitmap(mGameBitmap);
		super.onNewIntent(intent);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.activity_main);
		mIV = (TiledSquareImageView) findViewById(R.id.iv);
		checkBox3x3 = (CheckBox) findViewById(R.id.checkBox3x3);
		checkBox4x4 = (CheckBox) findViewById(R.id.checkBox4x4);
		checkBox5x5 = (CheckBox) findViewById(R.id.checkBox5x5);
		checkBox6x6 = (CheckBox) findViewById(R.id.checkBox6x6);
		checkRightCheckBox();
		mIV.setImageBitmap(mGameBitmap);
		mIV.setDifficulty(mDifficulty);
		mIV.setBorderWidth(mBorderWidth);		
	}

	public void onChoosePicture(View v) {
		Intent intent = new Intent(this, ChoosePictureActivity.class);
		intent.putExtra("DIFFICULTY", mDifficulty);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	public void onDiffChanged(View v) {
		checkBox3x3.setChecked(false);
		checkBox4x4.setChecked(false);
		checkBox5x5.setChecked(false);
		checkBox6x6.setChecked(false);
		((CheckBox) v).setChecked(true);

		if (v == checkBox3x3) {
			mDifficulty = 3;
			mIV.setDifficulty(3);
		} else if (v == checkBox4x4) {
			mDifficulty = 4;
			mIV.setDifficulty(4);
		} else if (v == checkBox5x5) {
			mDifficulty = 5;
			mIV.setDifficulty(5);
		} else if (v == checkBox6x6) {
			mDifficulty = 6;
			mIV.setDifficulty(6);
		}

		int choice = mIntent.getIntExtra("CHOICE", -1);
		if (choice == SYMBOL) {
			int position = mIntent.getIntExtra("PICTURE", -1);
			mGameBitmap.recycle();
			mGameBitmap = ImageUtils.decodeSampledBitmapFromResource(
					getResources(), mSymbolsIDs[mDifficulty - 3][position],
					mWidth, mWidth);
			mIV.setImageBitmap(mGameBitmap);
		}
	}

	private void checkRightCheckBox() {
		switch (mDifficulty) {
		case 3:
			checkBox3x3.setChecked(true);
			break;
		case 4:
			checkBox4x4.setChecked(true);
			break;
		case 5:
			checkBox5x5.setChecked(true);
			break;
		case 6:
			checkBox6x6.setChecked(true);
			break;
		}
	}

	private void setPicWidthAndTileBorderWidth() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int height = metrics.heightPixels;
		int width = metrics.widthPixels;
		mWidth = height < width ? height : width;
		switch (metrics.densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			mBorderWidth = 2;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			mBorderWidth = 2;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			mBorderWidth = 3;
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			mBorderWidth = 4;
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			mBorderWidth = 6;
			break;
		}
	}

	private Bitmap getBitmapFromIntent(Intent intent) {
		int choice = intent.getIntExtra("CHOICE", -1);
		if (choice == DEFAULT_PICTURE) {
			int position = intent.getIntExtra("PICTURE", -1);
			return ImageUtils.decodeSampledBitmapFromResource(getResources(),
					mPictureIDs[position], mWidth, mWidth);
		} else if (choice == PHONE_GALLERY) {
			String fileName = intent.getStringExtra("PICTURE");
			return ImageUtils.decodeSampledBitmapFromFile(fileName, mWidth,
					mWidth);
		} else if (choice == SYMBOL) {
			int position = intent.getIntExtra("PICTURE", -1);
			return ImageUtils.decodeSampledBitmapFromResource(getResources(),
					mSymbolsIDs[mDifficulty - 3][position], mWidth, mWidth);
		} else {
			return null;
		}
	}
}
