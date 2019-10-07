import java.io.*;
import java.net.*;
import java.lang.Thread;

public class TcpServerListenThread extends Thread implements Constants{
	private int mIpVer;
	private String mLocalIpAddrStr;
	private short mLocalPortNumber;
	private ServerSocket mListenTcpServerSocket;
	private ThreadPool mThreadPool;
	
	public TcpServerListenThread(final int ipVer, final String localIpAddrStr, 
	final short localPortNumber) {
		mIpVer = ipVer;
		mLocalIpAddrStr = localIpAddrStr;
		mLocalPortNumber = localPortNumber;
		
		try {
			mThreadPool = new ThreadPool(THREAD_POOL_NUM_THREADS);
		}
		catch (Exception e) {
//			ErrLog(<<"Fail to create a Thread Pool!");
			System.out.println("[Err] " + e.toString() + " Fail to create a Thread Pool!");
			return;
		}
	}
	protected void finalize() throws Throwable{
		if (null != mListenTcpServerSocket) {
			mListenTcpServerSocket = null;
		}
		
		if (null != mThreadPool) {
			mThreadPool = null;
		}
	}
	
	@Override
	public void run() {
		System.out.println("[Info] " + "TcpServerListenThread - enter");

		Socket connTcpServerSocket;
		TcpServerConnTask task;
		
		/* Open a TCP Server Socket for listening with addr and port. */
		try {
			mListenTcpServerSocket = new ServerSocket(mLocalPortNumber, 
					TCP_CONNECTIONS_MAX_NUM, InetAddress.getByName(mLocalIpAddrStr));
		}
		catch (IOException e) {
//			ErrLog(<<"Fail to open a TCP Server Socket for listening with addr and port!");
			System.out.println("[Err] " + "Fail to open a TCP Server Socket for listening with addr and port!");
			return;
		}
		
		while (true) {
			try {
				/* Accept TCP client. */
				connTcpServerSocket = mListenTcpServerSocket.accept();
			}
			catch (IOException e) {
				/* No new TCP connection. */
				continue;
			}
			
			/* A new TCP connection. */
			try {
				/* Create a TCP Server Connection Task. */
				task = new TcpServerConnTask(connTcpServerSocket);
			}
			catch (Exception e) {
//				ErrLog(<<"Fail to create an HTTP Server Connection Task!");
				System.out.println("[Err] " + e.toString() + " Fail to create an HTTP Server Connection Task!");
				try {
					connTcpServerSocket.close();
				}
				catch (IOException e2) {
					continue;
				}
				continue;
			}
			
			/* Add a TCP Server Connection Task to the Thread Pool. */
			mThreadPool.addTask(task);
//			InfoLog(<<"Add a TCP Server Connection Task to the Thread Pool.");
			System.out.println("[Info] " + "Add a TCP Server Connection Task to the Thread Pool.");
		}

//		System.out.println("[Info] " + "TcpServerListenThread - exit");
	}
}

/* End of File */
