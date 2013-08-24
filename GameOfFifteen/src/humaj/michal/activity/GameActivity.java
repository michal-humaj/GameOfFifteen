package humaj.michal.activity;

import humaj.michal.R;
import humaj.michal.gameoffifteen.SquareGameSurfaceView;
import humaj.michal.util.ImageUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends Activity implements OnTouchListener {

	private SquareGameSurfaceView mGameSurfaceView;
	private MovesHandler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		Intent intent = getIntent();
		int difficulty = intent.getIntExtra("DIFFICULTY", -1);
		int width = intent.getIntExtra("WIDTH", -1);
		int borderWidth = intent.getIntExtra("BORDER_WIDTH", -1);
		mGameSurfaceView = (SquareGameSurfaceView) findViewById(R.id.gameSurfaceView);
		Bitmap bitmap = ImageUtils.getBitmapFromIntent(intent, getResources(),
				width, difficulty);
		mGameSurfaceView.init(bitmap, difficulty, width, borderWidth,
				(TextView) findViewById(R.id.tvMoves));
		mGameSurfaceView.setOnTouchListener(this);
		ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
		ivPreview.setImageBitmap(bitmap);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGameSurfaceView.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGameSurfaceView.resume();
	}

	@Override
	public boolean onTouch(View v, MotionEvent me) {
		mGameSurfaceView.onTouch(me);
		return true;
	}

	public static class MovesHandler extends Handler {

		TextView tvMoves;

		public MovesHandler(Looper mainLooper, TextView tv) {
			super(mainLooper);
			tvMoves = tv;
		}

		@Override
		public void handleMessage(Message message) {
			Integer moves = (Integer) message.obj;
			tvMoves.setText(moves);
		}
	}
}
