
public abstract class PoolTaskIf{
	protected String mTaskNameStr;
	protected ThreadPool mThreadPool;
	
	public PoolTaskIf() {
		mThreadPool = null;
	}
	public PoolTaskIf(final String taskNameStr) {
		mTaskNameStr = taskNameStr;
		mThreadPool = null;
	}
	protected void finalize() throws Throwable{}
	
	public void setThreadPool(final ThreadPool threadPool) {
		mThreadPool = threadPool;
	}
	public abstract void run();
	
	protected void mSleep(final short ms) {
		Thread.sleep(ms);
	}
}

/* End of File */
