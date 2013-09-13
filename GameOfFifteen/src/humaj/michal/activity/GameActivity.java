package humaj.michal.activity;

import humaj.michal.R;
import humaj.michal.gameoffifteen.HighscoreContract.HighscoreEntry;
import humaj.michal.gameoffifteen.HighscoreDbHelper;
import humaj.michal.gameoffifteen.SurfaceRenderer;
import humaj.michal.uilogic.BackDialog;
import humaj.michal.uilogic.BackDialog.BackDialogListener;
import humaj.michal.uilogic.PauseDialog;
import humaj.michal.uilogic.WinDialog;
import humaj.michal.util.BitmapHolder;
import humaj.michal.util.ImageUtils;
import humaj.michal.util.SquareGameSurfaceView;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends FragmentActivity implements OnTouchListener,
		BackDialogListener, LoaderManager.LoaderCallbacks<Cursor> {

	private SquareGameSurfaceView mGameSurfaceView;
	private SurfaceRenderer mSurfaceRenderer;
	private boolean mDialogPaused = false;
	private boolean mQuitPressed = false;
	private int mDifficulty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		Intent intent = getIntent();
		mDifficulty = intent.getIntExtra("DIFFICULTY", -1);
		Object config = getLastCustomNonConfigurationInstance();
		if (config != null) {
			mSurfaceRenderer = (SurfaceRenderer) config;
			mSurfaceRenderer.setTvMoves((TextView) findViewById(R.id.tvMoves));
			mSurfaceRenderer.setTimeHandler(new TimeHandler(Looper
					.getMainLooper(), (TextView) findViewById(R.id.tvTime)));
		} else {
			int borderWidth = intent.getIntExtra("BORDER_WIDTH", -1);
			int surfaceWidth = intent.getIntExtra("WIDTH", -1);
			mSurfaceRenderer = new SurfaceRenderer(
					(TextView) findViewById(R.id.tvMoves), mDifficulty,
					surfaceWidth, borderWidth, new TimeHandler(
							Looper.getMainLooper(),
							(TextView) findViewById(R.id.tvTime)));
		}
		Bitmap bitmap = BitmapHolder.getInstance().getBitmap();
		mGameSurfaceView = (SquareGameSurfaceView) findViewById(R.id.gameSurfaceView);
		mGameSurfaceView.setSurfaceRenderer(mSurfaceRenderer);
		mGameSurfaceView.setOnTouchListener(this);
		ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
		ivPreview.setImageBitmap(bitmap);
		PreviewListener previewListener = new PreviewListener();
		ivPreview.setOnTouchListener(previewListener);
		Button btnPreview = (Button) findViewById(R.id.btnPreview);
		btnPreview.setOnTouchListener(previewListener);
		if (config == null)
			mSurfaceRenderer.shuffleTiles();
	}

	class PreviewListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				mSurfaceRenderer.previewOn();
				break;
			case MotionEvent.ACTION_UP:
				mSurfaceRenderer.previewOff();
				break;
			}
			return true;
		}
	}

	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		return mSurfaceRenderer;
	}

	@Override
	protected void onPause() {
		mSurfaceRenderer.pause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!mDialogPaused) {
			mSurfaceRenderer.resume();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent me) {
		if (mSurfaceRenderer.isSolved())
			return true;
		mSurfaceRenderer.onTouch(me);
		if (mSurfaceRenderer.isSolved()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			WinDialog dialog = new WinDialog();
			dialog.show(getSupportFragmentManager(), "WinDialogFragment");
			writeToDatabase(mSurfaceRenderer.getSolveTime(),
					mSurfaceRenderer.getMovesCount());
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		if (!mSurfaceRenderer.isSolved()) {
			mDialogPaused = true;
			mSurfaceRenderer.pause();
			BackDialog dialog = new BackDialog();
			dialog.show(getSupportFragmentManager(), "BackDialogFragment");
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		mQuitPressed = true;
		super.onBackPressed();
	}

	public void onDialogDismiss() {
		if (!mQuitPressed)
			mSurfaceRenderer.resume();
		Log.d("ad", "onDialog dissmiss resume");
		mDialogPaused = false;
	}

	public void onToggleEmptySeen(View v) {
		mSurfaceRenderer.toggleEmptySeen();
	}

	public void onPausePressed(View v) {
		mDialogPaused = true;
		mSurfaceRenderer.pause();
		PauseDialog dialog = new PauseDialog();
		dialog.show(getSupportFragmentManager(), "PauseDialogFragment");
	}

	public static class TimeHandler extends Handler {

		TextView tvTime;

		public TimeHandler(Looper mainLooper, TextView tv) {
			super(mainLooper);
			tvTime = tv;
		}

		@Override
		public void handleMessage(Message message) {
			String time = (String) message.obj;
			tvTime.setText(time);
		}
	}

	private void writeToDatabase(String solveTime, int movesCount) {
		ContentValues values = new ContentValues();
		values.put("moves_" + mDifficulty, movesCount);
		values.put("time_" + mDifficulty, solveTime);
		HighscoreDbHelper dbHelper = new HighscoreDbHelper(this);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String[] projection = { "moves_" + mDifficulty, "time_" + mDifficulty };
		Cursor c;
		String selection;
		String[] selectionArgs = new String[1];
		Intent intent = getIntent();
		int choice = intent.getIntExtra("CHOICE", -1);
		if (choice == ImageUtils.PHONE_GALLERY) {
			String imagePath = intent.getStringExtra("PICTURE");
			selection = HighscoreEntry.COLUMN_NAME_PIC_FILENAME + " LIKE ?";
			selectionArgs[0] = imagePath;
		} else {
			int thumbID = intent.getIntExtra("THUMBNAIL_ID", -1);
			selection = HighscoreEntry.COLUMN_NAME_PIC_RES_ID + " LIKE ?";
			selectionArgs[0] = String.valueOf(thumbID);
		}
		c = db.query(HighscoreEntry.TABLE_NAME, projection, selection,
				selectionArgs, null, null, null);
		if (choice == ImageUtils.PHONE_GALLERY) {
			values.put(HighscoreEntry.COLUMN_NAME_IS_GALLERY_PIC, 1);
			values.put(HighscoreEntry.COLUMN_NAME_PIC_FILENAME,
					intent.getStringExtra("PICTURE"));
		} else {
			values.put(HighscoreEntry.COLUMN_NAME_IS_GALLERY_PIC, 0);
			values.put(HighscoreEntry.COLUMN_NAME_PIC_RES_ID,
					intent.getIntExtra("THUMBNAIL_ID", -1));
		}
		if (c.getCount() == 0) {
			SQLiteDatabase writeableDB = dbHelper.getWritableDatabase();
			writeableDB.insert(HighscoreEntry.TABLE_NAME, null, values);
			writeableDB.close();
		} else {
			c.moveToFirst();
			int colIdex = c.getColumnIndex("moves_" + mDifficulty);
			int highscoreMoves = c.getInt(colIdex);
			if (movesCount < highscoreMoves || highscoreMoves == 0) {
				db.update(HighscoreEntry.TABLE_NAME, values, selection,
						selectionArgs);
				db.close();
			}
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

}
