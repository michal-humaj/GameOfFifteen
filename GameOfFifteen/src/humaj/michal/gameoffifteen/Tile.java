package humaj.michal.gameoffifteen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Tile {

	private Rect src;
	private Rect dst;
	private int supposedX;
	private int supposedY;

	public Tile(Rect src, Rect dst, int x, int y) {
		this.src = src;
		this.dst = dst;
		supposedX = x;
		supposedY = y;
	}

	public synchronized void draw(Canvas c, Bitmap b) {
		c.drawBitmap(b, src, dst, null);
	}

	public synchronized boolean isSolved(int x, int y) {
		if (x != supposedX)
			return false;
		if (y != supposedY)
			return false;
		return true;
	}

	public synchronized void setDst(Rect dst) {
		this.dst = dst;
	}

	public synchronized Rect getDst() {
		Rect ret = new Rect(dst);
		return ret;
	}
}
