package humaj.michal.util;

import android.content.Context;
import android.util.AttributeSet;
import humaj.michal.gameoffifteen.SquareGameSurfaceView;

public class LandSquareGameSurfaceView extends SquareGameSurfaceView {

	public LandSquareGameSurfaceView(Context context) {
		super(context);
	}

	public LandSquareGameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LandSquareGameSurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(final int widthMeasureSpec,
			final int heightMeasureSpec) {
		int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		setMeasuredDimension(height, height);
	}

	@Override
	protected void onSizeChanged(final int w, final int h, final int oldw,
			final int oldh) {
		super.onSizeChanged(w, w, oldw, oldh);
	}

}
