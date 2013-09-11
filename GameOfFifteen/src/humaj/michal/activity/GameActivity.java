package humaj.michal.activity;

import humaj.michal.R;
import humaj.michal.gameoffifteen.SquareGameSurfaceView;
import humaj.michal.gameoffifteen.SurfaceRenderer;
import humaj.michal.uilogic.BackDialog;
import humaj.michal.uilogic.PauseDialog;
import humaj.michal.uilogic.BackDialog.BackDialogListener;
import humaj.michal.uilogic.WinDialog;
import humaj.michal.util.BitmapHolder;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends FragmentActivity implements OnTouchListener,
		BackDialogListener {

	private SquareGameSurfaceView mGameSurfaceView;
	private SurfaceRenderer mSurfaceRenderer;
	private boolean mDialogPaused = false;
	private boolean mQuitPressed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		Object config = getLastCustomNonConfigurationInstance();
		if (config != null) {
			mSurfaceRenderer = (SurfaceRenderer) config;
			mSurfaceRenderer.setTvMoves((TextView) findViewById(R.id.tvMoves));
			mSurfaceRenderer.setTimeHandler(new TimeHandler(Looper
					.getMainLooper(), (TextView) findViewById(R.id.tvTime)));
		} else {
			Intent intent = getIntent();
			int difficulty = intent.getIntExtra("DIFFICULTY", -1);
			int borderWidth = intent.getIntExtra("BORDER_WIDTH", -1);
			int surfaceWidth = intent.getIntExtra("WIDTH", -1);
			mSurfaceRenderer = new SurfaceRenderer(
					(TextView) findViewById(R.id.tvMoves), difficulty,
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
		/*
		 * AdView mAdView = (AdView) findViewById(R.id.ad); AdRequest adRequest
		 * = new AdRequest(); adRequest.addKeyword("ski");
		 * adRequest.setTesting(true); mAdView.loadAd(adRequest);
		 */
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
		/*
		 * if (mGameSurfaceView.isSurfaceCreated() && !mDialogPaused) {
		 * Log.d("dade", "DOPICEEEEEEEEEE"); mSurfaceRenderer.resume(); }
		 */
	}

	@Override
	public boolean onTouch(View v, MotionEvent me) {
		mSurfaceRenderer.onTouch(me);
		if (mSurfaceRenderer.isSolved()) {
			WinDialog dialog = new WinDialog();
			dialog.show(getSupportFragmentManager(), "WinDialogFragment");
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
}
