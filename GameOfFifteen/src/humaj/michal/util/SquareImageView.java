package humaj.michal.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class SquareImageView extends ImageView {

	public SquareImageView(final Context context) {
		super(context);
	}

	public SquareImageView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareImageView(final Context context, final AttributeSet attrs,
			final int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(final int widthMeasureSpec,
			final int heightMeasureSpec) {
		int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		int height = getDefaultSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		width = height < width && height != 0 ? height : width;
		setMeasuredDimension(width, width);
	}

	@Override
	protected void onSizeChanged(final int w, final int h, final int oldw,
			final int oldh) {
		super.onSizeChanged(w, w, oldw, oldh);
	}
}