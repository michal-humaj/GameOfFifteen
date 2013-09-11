package humaj.michal.util;

import android.content.Context;
import android.util.AttributeSet;

public class LandSquareImageView extends SquareImageView {

	public LandSquareImageView(Context context) {
		super(context);
	}

	public LandSquareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LandSquareImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(final int widthMeasureSpec,
			final int heightMeasureSpec) {
		int height = getDefaultSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		int dimension = height < width ? height : width;
		setMeasuredDimension(dimension, dimension);
	}

	@Override
	protected void onSizeChanged(final int w, final int h, final int oldw,
			final int oldh) {
		super.onSizeChanged(w, w, oldw, oldh);
	}

}
