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
		R.drawable.t001, R.drawable.t002, R.drawable.t003, R.drawable.t004, R.drawable.t230, 
		R.drawable.t005, R.drawable.t006, R.drawable.t007, R.drawable.t008, R.drawable.t009, 
		R.drawable.t010, R.drawable.t011, R.drawable.t012, R.drawable.t013, R.drawable.t014, 
		R.drawable.t015, R.drawable.t016, R.drawable.t017, R.drawable.t018, R.drawable.t019, 
		R.drawable.t020, R.drawable.t021, R.drawable.t022, R.drawable.t023, R.drawable.t024, 
		R.drawable.t025, R.drawable.t026, R.drawable.t027, R.drawable.t028, R.drawable.t029, 
		R.drawable.t030, R.drawable.t031, R.drawable.t032, R.drawable.t033, R.drawable.t034, 
		R.drawable.t035, R.drawable.t036, R.drawable.t037, R.drawable.t038, R.drawable.t039, 
		R.drawable.t040, R.drawable.t041, R.drawable.t042, R.drawable.t043, R.drawable.t044, 
		R.drawable.t045, R.drawable.t046, R.drawable.t047, R.drawable.t048, R.drawable.t049, 
		R.drawable.t050, R.drawable.t051, R.drawable.t052, R.drawable.t053, R.drawable.t054, 
		R.drawable.t055, R.drawable.t056, R.drawable.t057, R.drawable.t058, R.drawable.t059, 
		R.drawable.t060, R.drawable.t061, R.drawable.t062, R.drawable.t063, R.drawable.t064, 
		R.drawable.t065, R.drawable.t066, R.drawable.t067, R.drawable.t068, R.drawable.t069, 
		R.drawable.t070, R.drawable.t071, R.drawable.t072, R.drawable.t073, R.drawable.t074, 
		R.drawable.t075, R.drawable.t076, R.drawable.t077, R.drawable.t078, R.drawable.t079, 
		R.drawable.t080, R.drawable.t081, R.drawable.t082, R.drawable.t083, R.drawable.t084, 
		R.drawable.t085, R.drawable.t086, R.drawable.t087, R.drawable.t088, R.drawable.t089, 
		R.drawable.t090, R.drawable.t091, R.drawable.t092, R.drawable.t093, R.drawable.t094, 
		R.drawable.t095, R.drawable.t096, R.drawable.t097, R.drawable.t098, R.drawable.t099, 
		R.drawable.t100, R.drawable.t101, R.drawable.t102, R.drawable.t103, R.drawable.t104, 
		R.drawable.t105, R.drawable.t106, R.drawable.t107, R.drawable.t108, R.drawable.t109, 
		R.drawable.t110, R.drawable.t111, R.drawable.t112, R.drawable.t113, R.drawable.t114, 
		R.drawable.t115, R.drawable.t116, R.drawable.t117, R.drawable.t118, R.drawable.t119, 
		R.drawable.t120, R.drawable.t121, R.drawable.t122, R.drawable.t123, R.drawable.t124, 
		R.drawable.t125, R.drawable.t126, R.drawable.t127, R.drawable.t128, R.drawable.t129, 
		R.drawable.t130, R.drawable.t131, R.drawable.t132, R.drawable.t133, R.drawable.t134, 
		R.drawable.t135, R.drawable.t136, R.drawable.t137, R.drawable.t138, R.drawable.t139, 
		R.drawable.t140, R.drawable.t141, R.drawable.t142, R.drawable.t143, R.drawable.t144, 
		R.drawable.t145, R.drawable.t146, R.drawable.t147, R.drawable.t148, R.drawable.t149, 
		R.drawable.t150, R.drawable.t151, R.drawable.t152, R.drawable.t153, R.drawable.t154, 
		R.drawable.t155, R.drawable.t156, R.drawable.t157, R.drawable.t158, R.drawable.t159, 
		R.drawable.t160, R.drawable.t161, R.drawable.t162, R.drawable.t163, R.drawable.t164, 
		R.drawable.t165, R.drawable.t166, R.drawable.t167, R.drawable.t168, R.drawable.t169, 
		R.drawable.t170, R.drawable.t171, R.drawable.t172, R.drawable.t173, R.drawable.t174, 
		R.drawable.t175, R.drawable.t176, R.drawable.t177, R.drawable.t178, R.drawable.t179, 
		R.drawable.t180, R.drawable.t181, R.drawable.t182, R.drawable.t183, R.drawable.t184, 
		R.drawable.t185, R.drawable.t186, R.drawable.t187, R.drawable.t188, R.drawable.t189, 
		R.drawable.t190, R.drawable.t191, R.drawable.t192, R.drawable.t193, R.drawable.t194, 
		R.drawable.t195, R.drawable.t196, R.drawable.t197, R.drawable.t198, R.drawable.t199, 
		R.drawable.t200, R.drawable.t201, R.drawable.t202, R.drawable.t203, R.drawable.t204, 
		R.drawable.t205, R.drawable.t206, R.drawable.t207, R.drawable.t208, R.drawable.t209, 
		R.drawable.t210, R.drawable.t211, R.drawable.t212, R.drawable.t213, R.drawable.t214, 
		R.drawable.t215, R.drawable.t216, R.drawable.t217, R.drawable.t218, R.drawable.t219, 
		R.drawable.t220, R.drawable.t221, R.drawable.t222, R.drawable.t223, R.drawable.t224, 
		R.drawable.t225, R.drawable.t226, R.drawable.t227, R.drawable.t228, R.drawable.t229		
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
		
		if (choice == DEFAULT_PICTURE){
			
			int position = intent.getIntExtra("PICTURE", 0);
			
			mGameBitmap = decodeSampledBitmapFromResource(
					getResources(),mPictureIDs[position],width, width);
			
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
