package humaj.michal.activity;

import humaj.michal.R;
import humaj.michal.gameoffifteen.HighscoreContract;
import humaj.michal.uilogic.HighscoreRowWrapper;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HighscoreActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	public static final int HIGHSCORE_LOADER = 55;

	private HighscoreAdapter mAdapter;
	private Cursor mCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_highscore);

		// getSupportLoaderManager().initLoader(HIGHSCORE_LOADER, null, this);

	}

	class HighscoreAdapter extends CursorAdapter {

		public HighscoreAdapter(Context context, Cursor c, int flags) {
			super(context, c, flags);
		}

		@Override
		public void bindView(View row, Context context, Cursor cursor) {
			HighscoreRowWrapper wrapper = (HighscoreRowWrapper) row.getTag();
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View row = inflater.inflate(R.layout.highscore_row, null);
			HighscoreRowWrapper wrapper = new HighscoreRowWrapper(row);
			row.setTag(wrapper);
			return row;
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle arg1) {
		switch (loaderID) {
		case HIGHSCORE_LOADER:
			String[] projection = { MediaStore.Images.Media._ID,
					MediaStore.Images.Media.DATA };
			return new CursorLoader(this,
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
					null, null, null);
			// TODO
		default:
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		mAdapter.changeCursor(cursor);
		mCursor = cursor;
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.changeCursor(null);
	}
}
