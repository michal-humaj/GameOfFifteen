package humaj.michal.activity;

import humaj.michal.R;
import humaj.michal.util.ImageUtils;
import humaj.michal.util.SquareImageView;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final Integer[] mPictureIDs = { R.drawable.p001,
			R.drawable.p002, R.drawable.p003, R.drawable.p004, R.drawable.p005,
			R.drawable.p006, R.drawable.p007, R.drawable.p008, R.drawable.p009,
			R.drawable.p010, R.drawable.p011, R.drawable.p012, R.drawable.p013,
			R.drawable.p014, R.drawable.p015, R.drawable.p016, R.drawable.p017,
			R.drawable.p018, R.drawable.p019, R.drawable.p020 };

	public static final Integer[] mSymbolsIDs3x3 = { R.drawable.s01d3,
			R.drawable.s02d3 };
	public static final Integer[] mSymbolsIDs4x4 = { R.drawable.s01d4,
			R.drawable.s02d4 };
	public static final Integer[] mSymbolsIDs5x5 = { R.drawable.s01d5,
			R.drawable.s02d5 };
	public static final Integer[] mSymbolsIDs6x6 = { R.drawable.s01d6,
			R.drawable.s02d6 };

	public static final int DEFAULT_PICTURE = 1111;
	public static final int PHONE_GALLERY = 2222;
	public static final int SYMBOL = 3333;

	private CheckBox checkBox3x3;
	private CheckBox checkBox4x4;
	private CheckBox checkBox5x5;
	private CheckBox checkBox6x6;
	
	Intent mIntent;

	private Bitmap mGameBitmap;
	private int mDifficulty;
	private int mWidth;
	private Random random = new Random(System.currentTimeMillis());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SquareImageView iv = (SquareImageView) findViewById(R.id.iv);
		checkBox3x3 = (CheckBox) findViewById(R.id.checkBox3x3);
		checkBox4x4 = (CheckBox) findViewById(R.id.checkBox4x4);
		checkBox5x5 = (CheckBox) findViewById(R.id.checkBox5x5);
		checkBox6x6 = (CheckBox) findViewById(R.id.checkBox6x6);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int height = metrics.heightPixels;
		int width = metrics.widthPixels;
		mWidth = height < width ? height : width;
		mDifficulty = random.nextInt(4) + 3;
		if (mDifficulty == 3) {
			checkBox3x3.setChecked(true);
		} else if (mDifficulty == 4) {
			checkBox4x4.setChecked(true);
		} else if (mDifficulty == 5) {
			checkBox5x5.setChecked(true);
		} else {
			checkBox6x6.setChecked(true);
		}
		Intent intent = getIntent();
		int choice = intent.getIntExtra("CHOICE", 0);
		if (choice == DEFAULT_PICTURE) {
			int position = intent.getIntExtra("PICTURE", 0);
			mGameBitmap = ImageUtils.decodeSampledBitmapFromResource(
					getResources(), mPictureIDs[position], mWidth, mWidth);
			iv.setImageBitmap(mGameBitmap);
		} else if (choice == PHONE_GALLERY) {
			String fileName = intent.getStringExtra("PICTURE");
			mGameBitmap = ImageUtils.decodeSampledBitmapFromFile(fileName,
					mWidth, mWidth);
			iv.setImageBitmap(mGameBitmap);
		} else if (choice == SYMBOL) {
			mGameBitmap.recycle();
			int position = intent.getIntExtra("PICTURE", 0);
			int id = 0;
			switch (mDifficulty) {
			case 3:
				id = mSymbolsIDs3x3[position];
				break;
			case 4:
				id = mSymbolsIDs4x4[position];
				break;
			case 5:
				id = mSymbolsIDs5x5[position];
				break;
			case 6:
				id = mSymbolsIDs6x6[position];
				break;
			}
			mGameBitmap = ImageUtils.decodeSampledBitmapFromResource(
					getResources(), id, mWidth, mWidth);
			iv.setImageBitmap(mGameBitmap);
		} else {
			int randomIndex = random.nextInt(mPictureIDs.length);
			mGameBitmap = ImageUtils.decodeSampledBitmapFromResource(
					getResources(), mPictureIDs[randomIndex], mWidth, mWidth);
			iv.setImageBitmap(mGameBitmap);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.activity_main);
		SquareImageView iv = (SquareImageView) findViewById(R.id.iv);
		checkBox3x3 = (CheckBox) findViewById(R.id.checkBox3x3);
		checkBox4x4 = (CheckBox) findViewById(R.id.checkBox4x4);
		checkBox5x5 = (CheckBox) findViewById(R.id.checkBox5x5);
		checkBox6x6 = (CheckBox) findViewById(R.id.checkBox6x6);
		if (mDifficulty == 3) {
			checkBox3x3.setChecked(true);
		} else if (mDifficulty == 4) {
			checkBox4x4.setChecked(true);
		} else if (mDifficulty == 5) {
			checkBox5x5.setChecked(true);
		} else {
			checkBox6x6.setChecked(true);
		}
		iv.setImageBitmap(mGameBitmap);
	}

	@Override
	protected void onDestroy() {
		mGameBitmap.recycle();
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		mIntent = intent;
		int choice = intent.getIntExtra("CHOICE", 0);
		SquareImageView iv = (SquareImageView) findViewById(R.id.iv);
		if (choice == DEFAULT_PICTURE) {
			mGameBitmap.recycle();
			int position = intent.getIntExtra("PICTURE", 0);
			mGameBitmap = ImageUtils.decodeSampledBitmapFromResource(
					getResources(), mPictureIDs[position], mWidth, mWidth);
			iv.setImageBitmap(mGameBitmap);
		} else if (choice == PHONE_GALLERY) {
			mGameBitmap.recycle();
			String fileName = intent.getStringExtra("PICTURE");
			mGameBitmap = ImageUtils.decodeSampledBitmapFromFile(fileName,
					mWidth, mWidth);
			iv.setImageBitmap(mGameBitmap);
		} else if (choice == SYMBOL) {
			mGameBitmap.recycle();
			int position = intent.getIntExtra("PICTURE", 0);
			int id = 0;
			switch (mDifficulty) {
			case 3:
				id = mSymbolsIDs3x3[position];
				break;
			case 4:
				id = mSymbolsIDs4x4[position];
				break;
			case 5:
				id = mSymbolsIDs5x5[position];
				break;
			case 6:
				id = mSymbolsIDs6x6[position];
				break;
			}
			mGameBitmap = ImageUtils.decodeSampledBitmapFromResource(
					getResources(), id, mWidth, mWidth);
			iv.setImageBitmap(mGameBitmap);
		}
		super.onNewIntent(intent);
	}

	public void onChoosePicture(View v) {
		Intent intent = new Intent(this, ChoosePictureActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	public void onDiffChanged(View v) {
		checkBox3x3.setChecked(false);
		checkBox4x4.setChecked(false);
		checkBox5x5.setChecked(false);
		checkBox6x6.setChecked(false);
		((CheckBox) v).setChecked(true);
		int id = 0;		
		int choice = mIntent.getIntExtra("CHOICE", 0);
		int position = mIntent.getIntExtra("PICTURE", 0);
		if (v == checkBox3x3) {
			mDifficulty = 3;
			if (choice == SYMBOL) {
				id = mSymbolsIDs3x3[position];
			}
		} else if (v == checkBox4x4) {
			mDifficulty = 4;
			if (choice == SYMBOL) {
				id = mSymbolsIDs4x4[position];
			}
		} else if (v == checkBox5x5) {
			mDifficulty = 5;
			if (choice == SYMBOL) {
				id = mSymbolsIDs5x5[position];
			}
		} else {
			mDifficulty = 6;
			if (choice == SYMBOL) {
				id = mSymbolsIDs6x6[position];
			}
		}
		if (choice == SYMBOL) {			
			mGameBitmap.recycle();
			mGameBitmap = ImageUtils.decodeSampledBitmapFromResource(
					getResources(), id, mWidth, mWidth);
			SquareImageView iv = (SquareImageView) findViewById(R.id.iv);
			iv.setImageBitmap(mGameBitmap);
		}
	}
}
