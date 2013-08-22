package humaj.michal.activity;

import humaj.michal.R;
import humaj.michal.util.ImageUtils;
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
		mGameBitmap = ImageUtils.getBitmapFromIntent(intent, getResources(),
				mWidth, mDifficulty);
		if (mGameBitmap == null) {
			Random random = new Random(System.currentTimeMillis());
			mDifficulty = random.nextInt(4) + 3;
			int randomIndex = random.nextInt(ImageUtils.mPictureIDs.length);
			mIntent.putExtra("CHOICE", ImageUtils.DEFAULT_PICTURE);
			mIntent.putExtra("PICTURE", randomIndex);
			mIntent.putExtra("DIFFICULTY", mDifficulty);
			mGameBitmap = ImageUtils.decodeSampledBitmapFromResource(
					getResources(), ImageUtils.mPictureIDs[randomIndex],
					mWidth, mWidth);
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
		mGameBitmap = ImageUtils.getBitmapFromIntent(intent, getResources(),
				mWidth, mDifficulty);
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

	public void onPlay(View v) {
		Intent intent = new Intent(this, GameActivity.class);		
		int choice = mIntent.getIntExtra("CHOICE", -1);		
		intent.putExtra("CHOICE", choice);
		if (choice == ImageUtils.PHONE_GALLERY){
			intent.putExtra("PICTURE", mIntent.getStringExtra("PICTURE"));
		} else {
			intent.putExtra("PICTURE", mIntent.getIntExtra("PICTURE", -1));
		}		
		intent.putExtra("DIFFICULTY", mDifficulty);
		intent.putExtra("WIDTH", mWidth);		
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
		if (choice == ImageUtils.SYMBOL) {
			int position = mIntent.getIntExtra("PICTURE", -1);
			mGameBitmap.recycle();
			mGameBitmap = ImageUtils.decodeSampledBitmapFromResource(
					getResources(),
					ImageUtils.mSymbolsIDs[mDifficulty - 3][position], mWidth,
					mWidth);
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
}
