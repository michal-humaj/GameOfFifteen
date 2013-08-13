package humaj.michal.activity;

import humaj.michal.R;
import humaj.michal.uilogic.DefaultPicsAdapter;
import humaj.michal.uilogic.PhoneGalleryAdapter;
import humaj.michal.util.ImageUtils;
import humaj.michal.util.SquareImageView;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class ChoosePictureActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	public static final int CURSOR_LOADER = 0;

	private Context mContext;

	private Cursor mCursor;
	private int mDataColumnIndex;

	private DefaultPicsAdapter mDefaultPicsAdapter;
	private PhoneGalleryAdapter mPhoneGalleryAdapter;

	private Bitmap mPlaceHolderBitmap = null;
	private int mThumbWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpViews();
		mContext = this;
		mPlaceHolderBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.placeholder);
		Display display = getWindowManager().getDefaultDisplay();
		mThumbWidth = display.getWidth() / 4;
		getSupportLoaderManager().initLoader(CURSOR_LOADER, null, this);
	}

	public void loadBitmap(int resId, SquareImageView imageView) {
		if (cancelPotentialWork(resId, imageView)) {
			final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(
					getResources(), mPlaceHolderBitmap, task);
			imageView.setImageDrawable(asyncDrawable);
			task.execute(resId);
		}
	}

	public void loadGalleryBitmap(String fileName, SquareImageView imageView) {
		if (cancelPotentialGalleryWork(fileName, imageView)) {
			final GalleryWorkerTask task = new GalleryWorkerTask(imageView);
			final AsyncGalleryDrawable asyncGalleryDrawable = new AsyncGalleryDrawable(
					getResources(), mPlaceHolderBitmap, task);
			imageView.setImageDrawable(asyncGalleryDrawable);
			task.execute(fileName);
		}
	}

	static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap,
				BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(
					bitmapWorkerTask);
		}

		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

	static class AsyncGalleryDrawable extends BitmapDrawable {
		private final WeakReference<GalleryWorkerTask> galleryWorkerTaskReference;

		public AsyncGalleryDrawable(Resources res, Bitmap bitmap,
				GalleryWorkerTask galleryWorkerTask) {
			super(res, bitmap);
			galleryWorkerTaskReference = new WeakReference<GalleryWorkerTask>(
					galleryWorkerTask);
		}

		public GalleryWorkerTask getGalleryWorkerTask() {
			return galleryWorkerTaskReference.get();
		}
	}

	public static boolean cancelPotentialWork(int data,
			SquareImageView imageView) {
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
		// No task associated with the ImageView, or an existing task was
		// cancelled
		return true;
	}

	public static boolean cancelPotentialGalleryWork(String data,
			SquareImageView imageView) {
		final GalleryWorkerTask galleryWorkerTask = getGalleryWorkerTask(imageView);

		if (galleryWorkerTask != null) {
			final String bitmapData = galleryWorkerTask.data;
			if (!bitmapData.equals(data)) {
				// Cancel previous task
				galleryWorkerTask.cancel(true);
			} else {
				// The same work is already in progress
				return false;
			}
		}
		// No task associated with the ImageView, or an existing task was
		// cancelled
		return true;
	}

	private static BitmapWorkerTask getBitmapWorkerTask(
			SquareImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	private static GalleryWorkerTask getGalleryWorkerTask(
			SquareImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncGalleryDrawable) {
				final AsyncGalleryDrawable asyncGalleryDrawable = (AsyncGalleryDrawable) drawable;
				return asyncGalleryDrawable.getGalleryWorkerTask();
			}
		}
		return null;
	}

	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
		private final WeakReference<SquareImageView> imageViewReference;
		private int data = 0;

		public BitmapWorkerTask(SquareImageView imageView) {
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imageViewReference = new WeakReference<SquareImageView>(imageView);
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(Integer... params) {
			data = params[0];
			return ImageUtils.decodeSampledBitmapFromResource(getResources(),
					data, mThumbWidth, mThumbWidth);
		}

		// Once complete, see if ImageView is still around and set bitmap.
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}
			if (imageViewReference != null && bitmap != null) {
				final SquareImageView imageView = imageViewReference.get();
				final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
				if (this == bitmapWorkerTask && imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	}

	class GalleryWorkerTask extends AsyncTask<String, Void, Bitmap> {
		private final WeakReference<SquareImageView> imageViewReference;
		private String data = "";

		public GalleryWorkerTask(SquareImageView imageView) {
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imageViewReference = new WeakReference<SquareImageView>(imageView);
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(String... params) {
			data = params[0];
			return ImageUtils.decodeSampledBitmapFromFile(data, mThumbWidth,
					mThumbWidth);
		}

		// Once complete, see if ImageView is still around and set bitmap.
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}
			if (imageViewReference != null && bitmap != null) {
				final SquareImageView imageView = imageViewReference.get();
				final GalleryWorkerTask galleryWorkerTask = getGalleryWorkerTask(imageView);
				if (this == galleryWorkerTask && imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
		switch (loaderID) {
		case CURSOR_LOADER:
			String[] projection = { MediaStore.Images.Media._ID,
					MediaStore.Images.Media.DATA };
			return new CursorLoader(this,
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
					null, null, null);
		default:
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mDataColumnIndex = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		mPhoneGalleryAdapter.changeCursor(cursor);
		mCursor = cursor;
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mPhoneGalleryAdapter.changeCursor(null);
	}

	public int getDataColumnIndex() {
		return mDataColumnIndex;
	}

	private void setUpViews() {
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

		gvDefaultPictures.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = new Intent(mContext, MainActivity.class);
				intent.putExtra("CHOICE", MainActivity.DEFAULT_PICTURE);
				intent.putExtra("PICTURE", position);
				startActivity(intent);
			}
		});

		gvPhoneGallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = new Intent(mContext, MainActivity.class);
				intent.putExtra("CHOICE", MainActivity.PHONE_GALLERY);
				intent.putExtra("PICTURE", mCursor.getString(mDataColumnIndex));
				startActivity(intent);
			}
		});
		mDefaultPicsAdapter = new DefaultPicsAdapter(this);
		mPhoneGalleryAdapter = new PhoneGalleryAdapter(this, null, 0);
		gvDefaultPictures.setAdapter(mDefaultPicsAdapter);
		gvPhoneGallery.setAdapter(mPhoneGalleryAdapter);
	}

}
