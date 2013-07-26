package humaj.michal;

import java.lang.ref.WeakReference;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class ChoosePictureActivity extends FragmentActivity
	implements LoaderManager.LoaderCallbacks<Cursor>{	
	
	private Bitmap mPlaceHolderBitmap = null;
	private int mIdColumnIndex;
	private int mDataColumnIndex;
	private DefaultPicsAdapter mDefaultPicsAdapter;
	private PhoneGalleryAdapter mPhoneGalleryAdapter;
	private ContentResolver mContentResolver;
	private Cursor mCursor;
	private static final int URL_LOADER = 0;
	private Context mContext;
	
	public static final Integer[] mThumbIDs = new Integer[]{
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
		setContentView(R.layout.activity_choose_picture);		
		
		TabHost tabs = (TabHost) findViewById(R.id.tabhost);
		tabs.setup();
		
		TabSpec spec = tabs.newTabSpec("tag1");
		spec.setContent(R.id.gvDefaultPictures);
		spec.setIndicator(getString(R.string.tabDefaultPictures));
		tabs.addTab(spec);
		
		spec = tabs.newTabSpec("tag2");
		spec.setContent(R.id.gvPhoneGallery);
		spec.setIndicator(getString(R.string.tabPhoneGallery));
		tabs.addTab(spec);		
		
		spec = tabs.newTabSpec("tag3");
		spec.setContent(R.id.gvSymbols);
		spec.setIndicator(getString(R.string.tabSymbols));
		tabs.addTab(spec);
		
		GridView gvDefaultPictures = (GridView) findViewById(R.id.gvDefaultPictures);
		GridView gvPhoneGallery = (GridView) findViewById(R.id.gvPhoneGallery);
		GridView gvSymbols = (GridView) findViewById(R.id.gvSymbols);
		
		gvDefaultPictures.setOnItemClickListener(new OnItemClickListener(){
			
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("CHOICE", MainActivity.DEFAULT_PICTURE);
                intent.putExtra("PICTURE", position);             
                startActivity(intent);  
            }
		});
		
		gvPhoneGallery.setOnItemClickListener(new OnItemClickListener(){
			
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				
                Intent intent = new Intent(mContext, MainActivity.class);     
                intent.putExtra("CHOICE", MainActivity.PHONE_GALLERY);
                intent.putExtra("PICTURE", mCursor.getString(mDataColumnIndex));                
                startActivity(intent);                
            }
		});
		
		mPlaceHolderBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder);
		mContentResolver = getContentResolver();
		mDefaultPicsAdapter = new DefaultPicsAdapter(this);
		mPhoneGalleryAdapter = new PhoneGalleryAdapter(this, null, 0);
		mContext = this;
		
		gvDefaultPictures.setAdapter(mDefaultPicsAdapter);
		gvPhoneGallery.setAdapter(mPhoneGalleryAdapter);
		
		getSupportLoaderManager().initLoader(URL_LOADER, null, this);		
	}	
	
	public void loadBitmap(int resId, SquareImageView imageView) {
		
		if (cancelPotentialWork(resId, imageView)) {
	        final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
	        final AsyncDrawable asyncDrawable =
	                new AsyncDrawable(getResources(), mPlaceHolderBitmap, task);
	        imageView.setImageDrawable(asyncDrawable);
	        task.execute(resId);
	    }
	}
	
	static class AsyncDrawable extends BitmapDrawable {
		
	    private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

	    public AsyncDrawable(Resources res, Bitmap bitmap,
	            BitmapWorkerTask bitmapWorkerTask) {
	        super(res, bitmap);
	        bitmapWorkerTaskReference =
	            new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
	    }

	    public BitmapWorkerTask getBitmapWorkerTask() {
	        return bitmapWorkerTaskReference.get();
	    }    
	}	
	
	public static boolean cancelPotentialWork(int data, SquareImageView imageView) {
		
	    final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
	    if (bitmapWorkerTask != null) {
	        final int bitmapData = bitmapWorkerTask.data;
	        if (bitmapData != data) {
	            // Cancel previous task
	            bitmapWorkerTask.cancel(true);
	        } else {
	            // The same work is already in progress
	            return false;
	        }
	    }
	    // No task associated with the ImageView, or an existing task was cancelled
	    return true;
	}
	
	private static BitmapWorkerTask getBitmapWorkerTask(SquareImageView imageView) {
		
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
		    if (drawable instanceof AsyncDrawable) {
		    	final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
		        return asyncDrawable.getBitmapWorkerTask();
		    }
		}
		return null;
	}
	
	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
		
	    private final WeakReference<SquareImageView> mImageViewReference;
	    private int data = 0;

	    public BitmapWorkerTask(SquareImageView imageView) {
	        
	        mImageViewReference = new WeakReference<SquareImageView>(imageView);
	    }
	   
	    @Override
	    protected Bitmap doInBackground(Integer... params) {
	    	
	        data = params[0];
	        return BitmapFactory.decodeResource(getResources(), data);
	    }
	    
	    @Override
	    protected void onPostExecute(Bitmap bitmap) {	    	
	    	
	    	 if (isCancelled()) {
	             bitmap = null;
	         }
	         if (mImageViewReference != null && bitmap != null) {
	             final SquareImageView imageView = mImageViewReference.get();
	             final BitmapWorkerTask bitmapWorkerTask =
	                     getBitmapWorkerTask(imageView);
	             if (this == bitmapWorkerTask && imageView != null) {
	                 imageView.setImageBitmap(bitmap);
	             }
	         }	        
	    }
	}
	
	private class DefaultPicsAdapter extends BaseAdapter {

		private Context context;		
		
		public DefaultPicsAdapter(Context c) {
			super();
			this.context = c;
		}

		@Override
		public int getCount() {
			
			return mThumbIDs.length;
		}

		@Override
		public Object getItem(int position) {
			
			return mThumbIDs[position];
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}	

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			SquareImageView imageView;
			if (convertView == null){
				imageView = new SquareImageView(context);           
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			}else{
				imageView = (SquareImageView) convertView;
			}
			loadBitmap(mThumbIDs[position], imageView);					
	        return imageView;
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle arg1) {
		
		switch (loaderID) {
        case URL_LOADER:
        	
        	String[] projection = {MediaStore.Images.Media._ID,
        			MediaStore.Images.Media.DATA};
        	
            return new CursorLoader(
            		this,   
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,     
                    null,            
                    null,            
                    null);
            
        default:            
            return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		
		mIdColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
		mDataColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		mPhoneGalleryAdapter.changeCursor(cursor);
		mCursor = cursor;
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		
		mPhoneGalleryAdapter.changeCursor(null);
	}
	
	class GalleryWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
		
	    private final WeakReference<SquareImageView> imageViewReference;
	    private final Options mOptions;
	    private ContentResolver mContentResolver;
	    private int data = 0;

	    public GalleryWorkerTask(SquareImageView imageView, ContentResolver cr) {
	        
	        imageViewReference = new WeakReference<SquareImageView>(imageView);
	        mContentResolver = cr;
	        mOptions = new Options();
	    }
	    
	    @Override
	    protected Bitmap doInBackground(Integer... params) {
	        data = params[0];
	        return MediaStore.Images.Thumbnails.getThumbnail(
	        			mContentResolver,
	        			data,
	        			MediaStore.Images.Thumbnails.MICRO_KIND,
	        			mOptions);
	    }
	    
	    @Override
	    protected void onPostExecute(Bitmap bitmap) {
	    	
	        if (isCancelled()) {
	            bitmap = null;
	        }

	        if (imageViewReference != null && bitmap != null) {
	            final SquareImageView imageView = imageViewReference.get();
	            final GalleryWorkerTask galleryWorkerTask =
	                    getGalleryWorkerTask(imageView);
	            if (this == galleryWorkerTask && imageView != null) {
	                imageView.setImageBitmap(bitmap);	                
	            }
	        }
	    }
	}
	
	static class AsyncGalleryDrawable extends BitmapDrawable {
		
	    private final WeakReference<GalleryWorkerTask> galleryWorkerTaskReference;

	    public AsyncGalleryDrawable(Resources res, Bitmap bitmap,
	            GalleryWorkerTask galleryWorkerTask) {
	        super(res, bitmap);
	        galleryWorkerTaskReference =
	            new WeakReference<GalleryWorkerTask>(galleryWorkerTask);
	    }

	    public GalleryWorkerTask getGalleryWorkerTask() {
	        return galleryWorkerTaskReference.get();
	    }    
	}
	
	public void loadGalleryBitmap(int imageID, SquareImageView imageView) {
		
		if (cancelPotentialGalleryWork(imageID, imageView)) {
			final GalleryWorkerTask task = new GalleryWorkerTask(imageView, mContentResolver);        
	        final AsyncGalleryDrawable asyncDrawable =
	                new AsyncGalleryDrawable(getResources(), mPlaceHolderBitmap, task);
	        imageView.setImageDrawable(asyncDrawable);
	        task.execute(imageID);
	    }
	}
	
	public static boolean cancelPotentialGalleryWork(int data, SquareImageView imageView) {
	    final GalleryWorkerTask galleryWorkerTask = getGalleryWorkerTask(imageView);

	    if (galleryWorkerTask != null) {
	        final int bitmapData = galleryWorkerTask.data;
	        if (bitmapData != data) {
	            // Cancel previous task
	            galleryWorkerTask.cancel(true);
	        } else {
	            // The same work is already in progress
	            return false;
	        }
	    }
	    // No task associated with the ImageView, or an existing task was cancelled
	    return true;
	}
	
	private static GalleryWorkerTask getGalleryWorkerTask(SquareImageView imageView) {
		   if (imageView != null) {
		       final Drawable drawable = imageView.getDrawable();
		       if (drawable instanceof AsyncGalleryDrawable) {
		           final AsyncGalleryDrawable asyncDrawable = (AsyncGalleryDrawable) drawable;
		           return asyncDrawable.getGalleryWorkerTask();
		       }
		    }
		    return null;
		}
	
	private class PhoneGalleryAdapter extends CursorAdapter{

		public PhoneGalleryAdapter(Context context, Cursor c, int flags) {
			
			super(context, c, flags);
			
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			
			SquareImageView imageView = (SquareImageView) view;	
			int imageID = cursor.getInt(mIdColumnIndex);
			loadGalleryBitmap(imageID, imageView);					
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup arg2) {
			
			SquareImageView imageView;	        
	        imageView = new SquareImageView(context);	            
	        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);		                
	        return imageView;
		}
			
	}
}


































