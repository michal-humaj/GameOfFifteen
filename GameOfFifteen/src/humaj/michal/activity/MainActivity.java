package humaj.michal.activity;

import humaj.michal.R;
import humaj.michal.util.ImageUtils;
import humaj.michal.util.SquareImageView;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	public static final Integer[] mPictureIDs = { R.drawable.p001,
			R.drawable.p002, R.drawable.p003, R.drawable.p004, R.drawable.p005,
			R.drawable.p006, R.drawable.p007, R.drawable.p008, R.drawable.p009,
			R.drawable.p010, R.drawable.p011, R.drawable.p012, R.drawable.p013,
			R.drawable.p014, R.drawable.p015, R.drawable.p016, R.drawable.p017,
			R.drawable.p018, R.drawable.p019, R.drawable.p020 };

	public static final int DEFAULT_PICTURE = 0;
	public static final int PHONE_GALLERY = 1;

	public Bitmap mGameBitmap;
	private SquareImageView iv;
	private int mDifficulty;
	private Random random = new Random(System.currentTimeMillis());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		iv = (SquareImageView) findViewById(R.id.iv);
		int randomIndex = random.nextInt(mPictureIDs.length);
		mGameBitmap = BitmapFactory.decodeResource(getResources(),
				mPictureIDs[randomIndex]);
		iv.setImageBitmap(mGameBitmap);
		mDifficulty = random.nextInt(4) + 3;
	}

	public void onChoosePicture(View v) {
		Intent intent = new Intent(this, ChoosePictureActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		int width = iv.getWidth();
		int choice = intent.getIntExtra("CHOICE", 0);
		mGameBitmap.recycle();
		if (choice == DEFAULT_PICTURE) {
			int position = intent.getIntExtra("PICTURE", 0);
			mGameBitmap = ImageUtils.decodeSampledBitmapFromResource(
					getResources(), mPictureIDs[position], width, width);
		} else if (choice == PHONE_GALLERY) {
			String fileName = intent.getStringExtra("PICTURE");
			mGameBitmap = ImageUtils.decodeSampledBitmapFromFile(fileName,
					width, width);
		}
		iv.setImageBitmap(mGameBitmap);
	}
}
