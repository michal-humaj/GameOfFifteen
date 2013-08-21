package humaj.michal.util;

import android.content.Context;
import android.util.AttributeSet;

public class LandTiledSquareImageView extends TiledSquareImageView {

	public LandTiledSquareImageView(Context context) {
		super(context);
	}

	public LandTiledSquareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LandTiledSquareImageView(Context context, AttributeSet attrs, int defStyle) {
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
