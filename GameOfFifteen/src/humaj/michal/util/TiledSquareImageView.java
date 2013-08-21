package humaj.michal.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

public class TiledSquareImageView extends SquareImageView {

	private int mDifficulty = -1;
	private int mBorderWidth = -1;

	public TiledSquareImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TiledSquareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TiledSquareImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {		
		Paint up = new Paint();
		up.setStrokeWidth(0);
		up.setARGB(153, 255, 255, 255);
		
		Paint left = new Paint();
		left.setStrokeWidth(0);
		left.setARGB(102, 255, 255, 255);
		
		Paint down = new Paint();
		down.setStrokeWidth(0);
		down.setARGB(153, 0, 0, 0);
		
		Paint right = new Paint();
		right.setStrokeWidth(0);
		right.setARGB(102, 0, 0, 0);	
		
		float width = (float) getWidth() / mDifficulty;
		for (int j = 0; j < mDifficulty; j++){
			for (int i = 0; i < mDifficulty; i++){
				int x1 = (int) Math.ceil(i * width);
				int x2 = (int) Math.floor((i + 1) * width);
				int y1 = (int) Math.ceil(j * width);
				int y2 = (int) Math.floor((j + 1) * width);
				
				for (int k = 0; k < mBorderWidth; k++){
					canvas.drawLine(x1+k, y1+k, x2-1-k, y1+k, up);
				}
				for (int k = 0; k < mBorderWidth; k++){
					canvas.drawLine(x1+1+k, y2-k, x2-k, y2-k, down);
				}
				for (int k = 0; k < mBorderWidth; k++){
					canvas.drawLine(x1+k, y1+1+k, x1+k, y2-k, left);
				}
				for (int k = 0; k < mBorderWidth; k++){
					canvas.drawLine(x2-k, y2+k, x2-k, y2-1-k, right);
				}
			}
		}
	}
	
	public void setDifficulty(int difficulty){
		mDifficulty = difficulty;
		invalidate();
	}
	
	public void setBorderWidth(int borderWidth){
		mBorderWidth = borderWidth;
		invalidate();
	}
}
