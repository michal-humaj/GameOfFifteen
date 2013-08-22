package humaj.michal.gameoffifteen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class SquareGameSurfaceView extends SurfaceView implements Runnable {

	private SurfaceHolder mHolder;
	private Thread mThread = null;

	private Bitmap mBitmap;
	private int mViewWidth;
	private int mDifficulty;
	private Tile[][] tile;
	private int emptyTileX;
	private int emptyTileY;
	private boolean isPaused = true;
	

	public SquareGameSurfaceView(Context context) {
		super(context);
		mHolder = getHolder();
	}

	public SquareGameSurfaceView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mHolder = getHolder();
	}

	public SquareGameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHolder = getHolder();
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
			long toSleep = startTime + 20 - System.currentTimeMillis();
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

	public void onTouch(MotionEvent me) {
		int tileWidth = Math.round(mViewWidth / (float) mDifficulty);
		int x = (int) me.getX() / tileWidth ;
		int y = (int) me.getY() / tileWidth;
		
		Rect temp = tile[x][y].getDst();
		tile[x][y].setDst(tile[emptyTileX][emptyTileY].getDst());
		tile[emptyTileX][emptyTileY].setDst(temp);
		
		Tile tempTile = tile[x][y];
		tile[x][y] = tile[emptyTileX][emptyTileY];		
		tile[emptyTileX][emptyTileY] = tempTile;
		emptyTileX = x;
		emptyTileY = y;
		
	}

	public void init(Bitmap bitmap, int difficulty, int viewWidth) {
		mBitmap = bitmap;
		mDifficulty = difficulty;
		mViewWidth = viewWidth;
		tile = new Tile[difficulty][difficulty];
		emptyTileX = difficulty - 1;
		emptyTileY = difficulty - 1;

		for (int j = 0; j < difficulty; j++) {
			for (int i = 0; i < difficulty; i++) {				
				double width = mBitmap.getWidth() / (double) mDifficulty;
				Rect src = new Rect((int) Math.round(i * width),
						(int) Math.round(j * width),
						(int) Math.round((i + 1) * width),
						(int) Math.round((j + 1) * width));
				width = mViewWidth / (double) mDifficulty;
				Rect dst = new Rect((int) Math.round(i * width),
						(int) Math.round(j * width),
						(int) Math.round((i + 1) * width),
						(int) Math.round((j + 1) * width));
				tile[i][j] = new Tile(src, dst, i, j);			
			}
		}
	}

	protected void drawTiles(Canvas c) {
		c.drawARGB(255, 212, 232, 0);		
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
