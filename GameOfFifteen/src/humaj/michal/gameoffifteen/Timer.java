package humaj.michal.gameoffifteen;

import humaj.michal.activity.GameActivity.TimeHandler;
import android.os.Message;
import android.util.Log;

public class Timer implements Runnable {

	private static final String mFormat = "%02d:%02d";

	private boolean mIsPaused = true;
	private Thread mThread = null;
	private long mStart = 0;
	private long mPause = 0;
	private TimeHandler mHandler;

	public Timer(TimeHandler h) {
		mHandler = h;
	}		

	public void setHandler(TimeHandler mHandler) {
		this.mHandler = mHandler;
	}

	@Override
	public void run() {
		int time;
		while (!mIsPaused) {
			time = (int) (System.currentTimeMillis() - mStart);
			Message msg = mHandler.obtainMessage();
			msg.obj = getFormattedTime(time);
			mHandler.sendMessage(msg);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void pause() {
		if (!mIsPaused) {
			mPause = System.currentTimeMillis();
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
	}

	public void resume() {
		Log.d("s", "Timer RESUME");
		if (mIsPaused) {
			mStart += System.currentTimeMillis() - mPause;
			mIsPaused = false;
			mThread = new Thread(this);
			mThread.start();
		}
	}

	private String getFormattedTime(int time) {
		time = time / 1000;
		int seconds = time % 60;
		int minutes = time / 60;
		return String.format(mFormat, minutes, seconds);
	}
}
