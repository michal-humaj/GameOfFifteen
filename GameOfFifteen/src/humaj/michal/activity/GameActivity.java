package humaj.michal.activity;

import humaj.michal.R;
import humaj.michal.gameoffifteen.SquareGameSurfaceView;
import humaj.michal.util.ImageUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

public class GameActivity extends Activity implements OnTouchListener {

	private SquareGameSurfaceView gameSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		Intent intent = getIntent();
		int difficulty = intent.getIntExtra("DIFFICULTY", -1);
		int width = intent.getIntExtra("WIDTH", -1);
		Bitmap bitmap = ImageUtils.getBitmapFromIntent(intent, getResources(),
				width, difficulty);
		ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
		ivPreview.setImageBitmap(bitmap);
		gameSurfaceView = (SquareGameSurfaceView) findViewById(R.id.gameSurfaceView);
		gameSurfaceView.setOnTouchListener(this);
		gameSurfaceView.init(bitmap, difficulty, width);		
	}

	@Override
	protected void onPause() {
		super.onPause();
		gameSurfaceView.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		gameSurfaceView.resume();
	}

	@Override
	public boolean onTouch(View v, MotionEvent me) {
		gameSurfaceView.onTouch(me);
		return true;
	}
}
