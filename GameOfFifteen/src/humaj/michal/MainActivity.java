package humaj.michal;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	
	public static final int DEFAULT_PICTURE = 0;
	public static final int PHONE_GALLERY = 1;	
	public Bitmap mGameBitmap;
	private SquareImageView iv;
	private Random random = new Random(System.currentTimeMillis());
	public static final Integer[] mPictureIDs = new Integer[]{
		R.drawable.p001, R.drawable.p002, R.drawable.p003, R.drawable.p004, R.drawable.p020, 
		R.drawable.p005, R.drawable.p006, R.drawable.p007, R.drawable.p008, R.drawable.p009, 
		R.drawable.p010, R.drawable.p011, R.drawable.p012, R.drawable.p013, R.drawable.p014, 
		R.drawable.p015, R.drawable.p016, R.drawable.p017, R.drawable.p018, R.drawable.p019	
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		iv = (SquareImageView) findViewById(R.id.iv);
		int index = random.nextInt(mPictureIDs.length);
		mGameBitmap = BitmapFactory.decodeResource(getResources(), mPictureIDs[index]);
		iv.setImageBitmap(mGameBitmap);		
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
		
		if (choice == DEFAULT_PICTURE){
			
			int position = intent.getIntExtra("PICTURE", 0);
			
			mGameBitmap = decodeSampledBitmapFromResource(
					getResources(), mPictureIDs[position], width, width);
			
		}else if (choice == PHONE_GALLERY){
			
			String fileName = intent.getStringExtra("PICTURE");		
	        mGameBitmap = decodeSampledBitmapFromFileName(fileName, width, width);
		}		
        iv.setImageBitmap(mGameBitmap);        
	}
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	public static Bitmap decodeSampledBitmapFromFileName(String fileName,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(fileName, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(fileName, options);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight){
		
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {	
	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);
	
	        // Choose the smallest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }	
	    return inSampleSize;
	}
}
