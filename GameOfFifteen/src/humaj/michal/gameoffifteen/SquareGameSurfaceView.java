package humaj.michal.gameoffifteen;

import humaj.michal.util.ImageUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class SquareGameSurfaceView extends SurfaceView implements Runnable,
		SurfaceHolder.Callback {

	private SurfaceHolder mHolder;
	private Thread mThread = null;

	private Bitmap mBitmap;
	private int mDifficulty;
	private double mSurfaceTileWidth;
	private Tile[][] tile;
	private int emptyTileX;
	private int emptyTileY;
	private boolean isPaused = true;

	public SquareGameSurfaceView(Context context) {
		super(context);
		mHolder = getHolder();
		mHolder.addCallback(this);
	}

	public SquareGameSurfaceView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mHolder = getHolder();
		mHolder.addCallback(this);
	}

	public SquareGameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHolder = getHolder();
		mHolder.addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		while (!isPaused) {
			long startTime = System.currentTimeMillis();
			if (!mHolder.getSurface().isValid())
				continue;
			Canvas c = mHolder.lockCanvas();
			drawTiles(c);
			mHolder.unlockCanvasAndPost(c);
			int toSleep = (int) (startTime + 20 - System.currentTimeMillis());
			if (toSleep > 0) {
				try {
					Thread.sleep(toSleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void pause() {
		isPaused = true;
		while (true) {
			try {
				mThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}
		mThread = null;
	}

	public void resume() {
		isPaused = false;
		mThread = new Thread(this);
		mThread.start();
	}

	public void init(Bitmap bitmap, int difficulty, int width, int borderWidth) {
		mBitmap = bitmap;
		mDifficulty = difficulty;
		tile = new Tile[difficulty][difficulty];
		emptyTileX = difficulty - 1;
		emptyTileY = difficulty - 1;
		Tile.setBorderWidth(borderWidth);
		double bitmapTileWidth = mBitmap.getWidth() / (double) mDifficulty;
		mSurfaceTileWidth = width / (double) mDifficulty;
		for (int j = 0; j < mDifficulty; j++) {
			for (int i = 0; i < mDifficulty; i++) {
				tile[i][j] = new Tile(
						ImageUtils.getRect(i, j, bitmapTileWidth),
						ImageUtils.getRect(i, j, mSurfaceTileWidth), i, j);
			}
		}

	}

	public void onTouch(MotionEvent me) {
		int x = (int) (me.getX() / mSurfaceTileWidth);
		int y = (int) (me.getY() / mSurfaceTileWidth);
		x = x < mDifficulty ? x : mDifficulty - 1;
		y = y < mDifficulty ? y : mDifficulty - 1;
		if (x != emptyTileX && y != emptyTileY)
			return;
		if (x == emptyTileX && y == emptyTileY)
			return;
		int i = emptyTileX;
		int j = emptyTileY;
		int dx = (int) Math.signum(x - emptyTileX);
		int dy = (int) Math.signum(y - emptyTileY);
		do {
			i += dx;
			j += dy;
			Rect to = ImageUtils.getRect(emptyTileX, emptyTileY,
					mSurfaceTileWidth);
			if (tile[i][j].slide(to)) {
				tile[emptyTileX][emptyTileY] = tile[i][j];
				emptyTileX = i;
				emptyTileY = j;
			}
		} while (i != x || j != y);
	}

	protected void drawTiles(Canvas c) {
		c.drawARGB(255, 255, 0, 255);
		for (int j = 0; j < mDifficulty; j++) {
			for (int i = 0; i < mDifficulty; i++) {
				if (i == emptyTileX && j == emptyTileY)
					continue;
				tile[i][j].draw(c, mBitmap);
			}
		}

	}

	@Override
	protected void onMeasure(final int widthMeasureSpec,
			final int heightMeasureSpec) {
		int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		setMeasuredDimension(width, width);
	}

	@Override
	protected void onSizeChanged(final int w, final int h, final int oldw,
			final int oldh) {
		super.onSizeChanged(w, w, oldw, oldh);
	}

}
