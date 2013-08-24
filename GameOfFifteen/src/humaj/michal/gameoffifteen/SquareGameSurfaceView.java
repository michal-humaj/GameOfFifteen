package humaj.michal.gameoffifteen;

import humaj.michal.util.ImageUtils;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class SquareGameSurfaceView extends SurfaceView implements Runnable,
		SurfaceHolder.Callback {

	private SurfaceHolder mHolder;
	private Thread mThread = null;

	private Bitmap mBitmap;
	private int mDifficulty;
	private double mSurfaceTileWidth;
	private Tile[][] mTile;
	private int mEmptyTileX;
	private int mEmptyTileY;
	private boolean mIsPaused = true;
	private boolean mIsShuffling = false;
	private boolean mIsSolved = false;

	private Rect mBitmapRect;
	private Rect mSurfaceRect;
	private Paint mAlphaPaint;

	private int mMoves = 0;
	private TextView mTvMoves;

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
		shuffleTiles();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		while (!mIsPaused) {
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
		mIsPaused = true;
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
		mIsPaused = false;
		mThread = new Thread(this);
		mThread.start();
	}

	public void init(Bitmap bitmap, int difficulty, int surfaceWidth,
			int borderWidth, TextView tv) {
		mTvMoves = tv;
		mBitmap = bitmap;
		mDifficulty = difficulty;
		mTile = new Tile[difficulty][difficulty];
		mEmptyTileX = difficulty - 1;
		mEmptyTileY = difficulty - 1;
		Tile.setBorderWidth(borderWidth);
		double bitmapTileWidth = mBitmap.getWidth() / (double) mDifficulty;
		mSurfaceTileWidth = surfaceWidth / (double) mDifficulty;
		for (int j = 0; j < mDifficulty; j++) {
			for (int i = 0; i < mDifficulty; i++) {
				mTile[i][j] = new Tile(
						ImageUtils.getRect(i, j, bitmapTileWidth),
						ImageUtils.getRect(i, j, mSurfaceTileWidth), i, j);
			}
		}
		mBitmapRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
		mSurfaceRect = new Rect(0, 0, surfaceWidth, surfaceWidth);
		mAlphaPaint = new Paint();
		mAlphaPaint.setAlpha(5);
	}

	public void onTouch(MotionEvent me) {
		if (mIsShuffling)
			return;
		if (mIsSolved)
			return;
		int x = (int) (me.getX() / mSurfaceTileWidth);
		int y = (int) (me.getY() / mSurfaceTileWidth);
		x = x < mDifficulty ? x : mDifficulty - 1;
		y = y < mDifficulty ? y : mDifficulty - 1;
		if (x != mEmptyTileX && y != mEmptyTileY)
			return;
		if (x == mEmptyTileX && y == mEmptyTileY)
			return;
		mMoves++;
		mTvMoves.setText(mMoves + "");
		int i = mEmptyTileX;
		int j = mEmptyTileY;
		int dx = (int) Math.signum(x - mEmptyTileX);
		int dy = (int) Math.signum(y - mEmptyTileY);
		do {
			i += dx;
			j += dy;
			Rect to = ImageUtils.getRect(mEmptyTileX, mEmptyTileY,
					mSurfaceTileWidth);
			if (mTile[i][j].slide(to)) {
				mTile[mEmptyTileX][mEmptyTileY] = mTile[i][j];
				mEmptyTileX = i;
				mEmptyTileY = j;
			}
		} while (i != x || j != y);
		if (isSolved()) {
			mIsSolved = true;
		}
	}

	protected void drawTiles(Canvas c) {
		c.drawARGB(255, 255, 0, 255);
		for (int j = 0; j < mDifficulty; j++) {
			for (int i = 0; i < mDifficulty; i++) {
				if (i == mEmptyTileX && j == mEmptyTileY)
					continue;
				mTile[i][j].draw(c, mBitmap);
			}
		}
		if (mIsSolved) {
			c.drawBitmap(mBitmap, mBitmapRect, mSurfaceRect, mAlphaPaint);
			int alpha = mAlphaPaint.getAlpha();
			if (alpha < 255) {
				mAlphaPaint.setAlpha(alpha + 10);
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

	private boolean isSolved() {
		for (int j = 0; j < mDifficulty; j++) {
			for (int i = 0; i < mDifficulty; i++) {
				if (i == mEmptyTileX && j == mEmptyTileY)
					continue;
				if (!mTile[i][j].isSolved(i, j))
					return false;
			}
		}
		return true;
	}

	private void shuffleTiles() {
		Thread t = new Thread() {
			private static final byte UP = 0;
			private static final byte DOWN = 1;
			private static final byte LEFT = 2;
			private static final byte RIGHT = 3;

			@Override
			public void run() {
				mIsShuffling = true;
				Random random = new Random(System.currentTimeMillis());
				byte previousDirection = -1;
				int x = -1;
				int y = -1;
				int shuffleCount = (int) Math.pow(mDifficulty, 4) / 7 + 30;
				Tile.setNumFrames(8 - mDifficulty);
				for (int i = 0; i < shuffleCount; i++) {
					Rect to = ImageUtils.getRect(mEmptyTileX, mEmptyTileY,
							mSurfaceTileWidth);
					boolean foundGood = false;
					do {
						int direction = random.nextInt(4);
						if (mEmptyTileX == 0 && direction == RIGHT)
							continue;
						if (mEmptyTileY == 0 && direction == DOWN)
							continue;
						if (mEmptyTileX == mDifficulty - 1 && direction == LEFT)
							continue;
						if (mEmptyTileY == mDifficulty - 1 && direction == UP)
							continue;
						if (direction == oppositeDirection(previousDirection))
							continue;
						switch (direction) {
						case UP:
							x = mEmptyTileX;
							y = mEmptyTileY + 1;
							previousDirection = UP;
							break;
						case DOWN:
							x = mEmptyTileX;
							y = mEmptyTileY - 1;
							previousDirection = DOWN;
							break;
						case LEFT:
							x = mEmptyTileX + 1;
							y = mEmptyTileY;
							previousDirection = LEFT;
							break;
						case RIGHT:
							x = mEmptyTileX - 1;
							y = mEmptyTileY;
							previousDirection = RIGHT;
							break;
						}
						foundGood = true;
					} while (!foundGood);
					if (mTile[x][y].slide(to)) {
						mTile[mEmptyTileX][mEmptyTileY] = mTile[x][y];
						mEmptyTileX = x;
						mEmptyTileY = y;
						try {
							mTile[x][y].getThread().join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				Tile.setNumFrames(7);
				mIsShuffling = false;
			}

			private byte oppositeDirection(byte dir) {
				switch (dir) {
				case UP:
					return DOWN;
				case DOWN:
					return UP;
				case RIGHT:
					return LEFT;
				case LEFT:
					return RIGHT;
				}
				return LEFT;
			}
		};
		t.start();
	}
}
