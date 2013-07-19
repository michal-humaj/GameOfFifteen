package humaj.michal;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ChoosePictureActivity extends Activity{
	
	private Cursor mCursor;
	private int mColumnIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_picture);
		
		String[] projection = {MediaStore.Images.Thumbnails._ID};	
		mCursor = getContentResolver().query(
				  	MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
				  	projection,
				  	null,
				  	null,
				  	null);		
		mColumnIndex = mCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
		GridView gvPhoneGallery = (GridView) findViewById(R.id.gvPhoneGallery);
		gvPhoneGallery.setAdapter(new PhoneGalleryAdapter(this));		
	}
	
	private class PhoneGalleryAdapter extends BaseAdapter{
		
		private Context mContext;
		
		public PhoneGalleryAdapter(Context c){
			
			mContext = c;
		}

		@Override
		public int getCount() {
			
			return mCursor.getCount();
		}

		@Override
		public Object getItem(int position) {
			
			return position;
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			SquareImageView imageView;
			if (convertView == null){
				imageView = new SquareImageView(mContext);
				mCursor.moveToPosition(position);
				int imageId = mCursor.getInt(mColumnIndex);
				Uri uri = Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
											   "" + imageId);
				imageView.setImageURI(uri);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
								
			} else {
				imageView = (SquareImageView) convertView;
			}
			
			return imageView;
		}	
	}
}


































