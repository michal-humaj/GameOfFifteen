package humaj.michal.gameoffifteen;

public class Timer implements Runnable {

	private boolean mIsPaused = true;	
	private Thread mThread;
	private long mStart;
	
	

	@Override
	public void run() {
		while(!mIsPaused){
			
		}
	}
	
	public void resume() {
		mIsPaused = false;
		mThread = new Thread(this);
		mThread.start();
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

}
