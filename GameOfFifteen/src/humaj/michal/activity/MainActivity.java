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
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final Integer[] mPictureIDs = { R.drawable.p001,
			R.drawable.p002, R.drawable.p003, R.drawable.p004, R.drawable.p005,
			R.drawable.p006, R.drawable.p007, R.drawable.p008, R.drawable.p009,
			R.drawable.p010, R.drawable.p011, R.drawable.p012, R.drawable.p013,
			R.drawable.p014, R.drawable.p015, R.drawable.p016, R.drawable.p017,
			R.drawable.p018, R.drawable.p019, R.drawable.p020 };

	public static final int DEFAULT_PICTURE = 1234;
	public static final int PHONE_GALLERY = 335;

	private Bitmap mGameBitmap;
	private SquareImageView mIV;
	private int mDifficulty;
	private int mWidth;
	private Random random = new Random(System.currentTimeMillis());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Context context = getApplicationContext();
		CharSequence text = "OnCreate MAIN";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

		mIV = (SquareImageView) findViewById(R.id.iv);
		int randomIndex = random.nextInt(mPictureIDs.length);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int height = metrics.heightPixels;
		int width = metrics.widthPixels;
		mWidth = height < width ? height : width;
		mDifficulty = random.nextInt(4) + 3;
		Intent intent = getIntent();
		int choice = intent.getIntExtra("CHOICE", 0);
		if (choice == DEFAULT_PICTURE) {
			int position = intent.getIntExtra("PICTURE", 0);
			mGameBitmap = ImageUtils.decodeSampledBitmapFromResource(
					getResources(), mPictureIDs[position], mWidth, mWidth);
			mIV.setImageBitmap(mGameBitmap);
		} else if (choice == PHONE_GALLERY) {
			String fileName = intent.getStringExtra("PICTURE");
			mGameBitmap = ImageUtils.decodeSampledBitmapFromFile(fileName,
					mWidth, mWidth);
			mIV.setImageBitmap(mGameBitmap);
		} else {
			mGameBitmap = ImageUtils.decodeSampledBitmapFromResource(
					getResources(), mPictureIDs[randomIndex], mWidth, mWidth);
			mIV.setImageBitmap(mGameBitmap);
		}

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.activity_main);
		mIV = (SquareImageView) findViewById(R.id.iv);
		mIV.setImageBitmap(mGameBitmap);

		Context context = getApplicationContext();
		CharSequence text = "OnConfigurationChanged MAIN";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	@Override
	protected void onDestroy() {
		Context context = getApplicationContext();
		CharSequence text = "OnDestroy MAIN";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
		mGameBitmap.recycle();
		super.onDestroy();
	}

	public void onChoosePicture(View v) {
		Intent intent = new Intent(this, ChoosePictureActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	@Override
	protected void onNewIntent(Intent intent) {

		Context context = getApplicationContext();
		CharSequence text = "OnNewIntent MAIN";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

		int choice = intent.getIntExtra("CHOICE", 0);

		if (choice == DEFAULT_PICTURE) {

			context = getApplicationContext();
			text = "DEFAULT_PICURE";
			duration = Toast.LENGTH_SHORT;
			toast = Toast.makeText(context, text, duration);
			toast.show();

			mGameBitmap.recycle();
			int position = intent.getIntExtra("PICTURE", 0);
			mGameBitmap = ImageUtils.decodeSampledBitmapFromResource(
					getResources(), mPictureIDs[position], mWidth, mWidth);
			mIV.setImageBitmap(mGameBitmap);
		} else if (choice == PHONE_GALLERY) {

			context = getApplicationContext();
			text = "PHONE_GALLERY";
			duration = Toast.LENGTH_SHORT;
			toast = Toast.makeText(context, text, duration);
			toast.show();

			mGameBitmap.recycle();
			String fileName = intent.getStringExtra("PICTURE");
			mGameBitmap = ImageUtils.decodeSampledBitmapFromFile(fileName,
					mWidth, mWidth);
			mIV.setImageBitmap(mGameBitmap);
		}

		super.onNewIntent(intent);
	}

	@Override
	protected void onRestart() {
		Context context = getApplicationContext();
		CharSequence text = "OnRestart MAIN";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		super.onRestart();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		Context context = getApplicationContext();
		CharSequence text = "OnRetainNonConfigurationInstance MAIN";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		return super.onRetainNonConfigurationInstance();
	}

	@Override
	protected void onStart() {
		Context context = getApplicationContext();
		CharSequence text = "OnStart MAIN";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		super.onStart();
	}

	@Override
	protected void onStop() {
		Context context = getApplicationContext();
		CharSequence text = "OnStop MAIN";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		super.onStop();
	}

}
