import java.util.Iterator;
import java.util.Vector;
import java.lang.Thread;

public class ThreadPool implements Statuses{
	private class PoolThread extends Thread{
		private ThreadPool mThreadPool;
		private PoolTaskIf mTask;
		
		public PoolThread(final ThreadPool threadPool) {
			mThreadPool = threadPool;
			mTask = null;
		}
		protected void finalize() throws Throwable{
			if (null != mTask) {
				mTask = null;
			}
		}
		
		@Override
		public void run() {
			System.out.println("[Info] " + "PoolThread - enter");
			
			long tid = getId();
			
			while (true) {
				if (mThreadPool.getTask(this)) {
//					InfoLog(<<"Thread "<<tid<<" will be busy.");
					System.out.println("[Info] " + "Thread " + tid + " will be busy.");
					mTask.run();
					mTask = null;
//					InfoLog(<<"Thread "<<tid<<" will be idle.");
					System.out.println("[Info] " + "Thread " + tid + " will be idle.");
				}
				else {
//					InfoLog(<<"Thread "<<tid<<" will exit.");
					System.out.println("[Info] " + "Thread " + tid + " will exit.");
					break;
				}
			}
			
			System.out.println("[Info] " + "PoolThread - exit");
		}
	}
	
	private Vector<PoolThread> mThreadVec;
	private Vector<PoolTaskIf> mTaskVec;
	
	public ThreadPool(final int numThreads) {
		mThreadVec = new Vector<PoolThread>();
		mTaskVec = new Vector<PoolTaskIf>();
		
		if (STATUS_ERR == createAll(numThreads)) {
			return;
		}
		
//		InfoLog(<<numThreads<<" threads are created in this Thread Pool.");
		System.out.println("[Info] " + numThreads + " threads are created in this Thread Pool.");
	}
	protected void finalize() throws Throwable{
		shutdownAll();
		
		mTaskVec = null;
		mThreadVec = null;
	}
	
	public synchronized void addTask(final PoolTaskIf task) {
		task.setThreadPool(this);
		mTaskVec.addElement(task);
		notify();
	}
	public synchronized Boolean getTask(final PoolThread thread) {
		while ((0 == mTaskVec.size())) {
			try {
				wait();
			}
			catch (InterruptedException e) {}
		}

		Iterator<PoolTaskIf> iter = mTaskVec.iterator();
		if (iter.hasNext()) {
			thread.mTask = iter.next();
			mTaskVec.removeElement(thread.mTask);
		}

		return true;
	}
	public synchronized void shutdownAll() {
		for (Object o: mThreadVec) {
			o.stop();
			o.join();
			o = null;
		}
		mThreadVec.clear();

		for (Object o: mTaskVec) {
			o = null;
		}
		mTaskVec.clear();
	}
	public synchronized int getPendingTaskNum() {
		return mTaskVec.size();
	}
	
	private int createAll(final int numThreads) {
		PoolThread thread;

		for (int i = 0; i < numThreads; i++) {
			try {
				thread = new PoolThread(this);
			}
			catch (Exception e) {
//				ErrLog(<<"Fail to create a thread!");
				System.out.println("[Err] " + e.toString() + " Fail to create a thread!");
				return STATUS_ERR;
			}

			thread.start();
			mThreadVec.addElement(thread);
		}

		return STATUS_OK;
	}
}

/* End of File */
