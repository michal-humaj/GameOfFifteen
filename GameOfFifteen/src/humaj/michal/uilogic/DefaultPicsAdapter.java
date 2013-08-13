package humaj.michal.uilogic;

import humaj.michal.R;
import humaj.michal.activity.ChoosePictureActivity;
import humaj.michal.util.SquareImageView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class DefaultPicsAdapter extends BaseAdapter {

	public static final Integer[] mThumbIDs = { R.drawable.t001,
			R.drawable.t002, R.drawable.t003, R.drawable.t004, R.drawable.t005,
			R.drawable.t006, R.drawable.t007, R.drawable.t008, R.drawable.t009,
			R.drawable.t010, R.drawable.t011, R.drawable.t012, R.drawable.t013,
			R.drawable.t014, R.drawable.t015, R.drawable.t016, R.drawable.t017,
			R.drawable.t018, R.drawable.t019, R.drawable.t020 };

	private Context mContext;

	public DefaultPicsAdapter(Context c) {
		mContext = c;
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
		if (convertView == null) {
			imageView = new SquareImageView(mContext);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		} else {
			imageView = (SquareImageView) convertView;
		}
		((ChoosePictureActivity) mContext).loadBitmap(mThumbIDs[position],
				imageView);
		return imageView;
	}
}
