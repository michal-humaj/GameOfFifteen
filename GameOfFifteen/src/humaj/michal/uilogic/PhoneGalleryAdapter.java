package humaj.michal.uilogic;

import humaj.michal.activity.ChoosePictureActivity;
import humaj.michal.util.SquareImageView;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PhoneGalleryAdapter extends CursorAdapter {

	public PhoneGalleryAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		SquareImageView imageView = (SquareImageView) view;
		int dataColumnIndex = ((ChoosePictureActivity) context).getDataColumnIndex();
		String fileName = cursor.getString(dataColumnIndex);
		((ChoosePictureActivity) context).loadGalleryBitmap(fileName, imageView);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		SquareImageView imageView = new SquareImageView(context);		
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		return imageView;
	}
}