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
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class ChoosePictureActivity extends FragmentActivity
	implements LoaderManager.LoaderCallbacks<Cursor>{	
	
	private Bitmap mPlaceHolderBitmap = null;
	private int mIdColumnIndex;
	private int mDataColumnIndex;
	private DefaultPicsAdapter mDefaultPicsAdapter;
	private PhoneGalleryAdapter mPhoneGalleryAdapter;
	private ContentResolver mContentResolver;
	private Cursor mCursor;
	private int mSampleSize = 1;
	private static final int URL_LOADER = 0;
	private Context mContext;
	
	public static final Integer[] mThumbIDs = new Integer[]{
		R.drawable.t001, R.drawable.t002, R.drawable.t003, R.drawable.t004, R.drawable.t020, 
		R.drawable.t005, R.drawable.t006, R.drawable.t007, R.drawable.t008, R.drawable.t009, 
		R.drawable.t010, R.drawable.t011, R.drawable.t012, R.drawable.t013, R.drawable.t014, 
		R.drawable.t015, R.drawable.t016, R.drawable.t017, R.drawable.t018, R.drawable.t019	
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
		Display display = getWindowManager().getDefaultDisplay();		
		mSampleSize = (int) Math.floor(384.0f/(display.getWidth()/3.0f));		
		
		
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
	        mOptions.inSampleSize = mSampleSize;
	    }
	    
	    @Override
	    protected Bitmap doInBackground(Integer... params) {
	        data = params[0];
	        return MediaStore.Images.Thumbnails.getThumbnail(
	        			mContentResolver,
	        			data,
	        			MediaStore.Images.Thumbnails.MINI_KIND,
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


































