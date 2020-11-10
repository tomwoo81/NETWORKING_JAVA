import java.net.*;
import java.io.*;
import java.nio.channels.*;
import java.lang.Thread;

public class TcpServerListenThread extends Thread implements Constants{
	private int mIpVer;
	private String mLocalIpAddrStr;
	private short mLocalPortNumber;
	private boolean mShutdown;
	
	public TcpServerListenThread(final int ipVer, final String localIpAddrStr, 
	final short localPortNumber) {
		mIpVer = ipVer;
		mLocalIpAddrStr = localIpAddrStr;
		mLocalPortNumber = localPortNumber;
		mShutdown = false;
	}
	protected void finalize() throws Throwable{}
	
	@Override
	public void run() {
		System.out.println("[Info] " + "TcpServerListenThread - enter");
		
		ThreadPool threadPool;
		try {
			threadPool = new ThreadPool(THREAD_POOL_NUM_THREADS);
		}
		catch (Exception e) {
//			ErrLog(<<"Fail to create a Thread Pool!");
			System.out.println("[Err] " + e.toString() + " Fail to create a Thread Pool!");
			return;
		}
		
		ServerSocketChannel listenTcpServerSocketChannel;
		
		try {
			/* Open a TCP Server Socket for listening. */
			listenTcpServerSocketChannel = ServerSocketChannel.open();
			
			/* Non-blocking I/O */
			listenTcpServerSocketChannel.configureBlocking(false);
			
			/* Make address reusable. */
			listenTcpServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			
			/* Configure a TCP Server Socket for listening with addr and port. */
			listenTcpServerSocketChannel.socket().bind(new InetSocketAddress(mLocalIpAddrStr, mLocalPortNumber));
		}
		catch (IOException e) {
//			ErrLog(<<"Fail to open a TCP Server Socket Channel for listening with addr and port!");
			System.out.println("[Err] " + "Fail to open a TCP Server Socket Channel for listening with addr and port!");
			return;
		}
		
		Selector selector;
		
		try {
			/* Open a selector. */
			selector = Selector.open();
			
			/* Register the ACCEPT event to the selector. */
			listenTcpServerSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		}
		catch (IOException e) {
//			ErrLog(<<"Fail to open a selector or register the ACCEPT event to the selector!");
			System.out.println("[Err] " + "Fail to open a selector or register the ACCEPT event to the selector!");
			return;
		}
		
		while (!waitForShutdown(10)) { //Wait for 10 ms.
			while (true) {
				try {
					/* Accept TCP client(s). */
					if (selector.selectNow() <= 0) {
						/* No new TCP connection. */
						break;
					}
				}
				catch (IOException e) {
					/* No new TCP connection. */
					break;
				}
				
				/* New TCP connection(s). */
				for (SelectionKey sk: selector.selectedKeys()) {
					if (sk.isAcceptable()) {
						SocketChannel connTcpServerSocketChannel;
						
						try {
							connTcpServerSocketChannel = ((ServerSocketChannel)sk.channel()).accept();
						}
						catch (IOException e) {
							continue;
						}
						
						TcpServerConnTask task;
						try {
							/* Create a TCP Server Connection Task. */
							task = new TcpServerConnTask(connTcpServerSocketChannel);
						}
						catch (Exception e) {
//							ErrLog(<<"Fail to create a TCP Server Connection Task!");
							System.out.println("[Err] " + e.toString() + " Fail to create a TCP Server Connection Task!");
							try {
								connTcpServerSocketChannel.close();
							}
							catch (IOException e2) {
								continue;
							}
							continue;
						}
						
						/* Add a TCP Server Connection Task to the Thread Pool. */
						threadPool.addTask(task);
//						InfoLog(<<"Add a TCP Server Connection Task to the Thread Pool.");
						System.out.println("[Info] " + "Add a TCP Server Connection Task to the Thread Pool.");
						
						/* Remove the binding between the variable task and the object of TcpServerConnTask. */
						task = null;
					}
					
					selector.selectedKeys().remove(sk);
				}
			}
		}
		
        /* Close the TCP Server Socket Channel for listening. */
		try {
			listenTcpServerSocketChannel.close();
		}
		catch (IOException e) {}
//		InfoLog(<<"Close the TCP Server Socket Channel for listening.");
		System.out.println("[Info] " + "Close the TCP Server Socket Channel for listening.");
		
        /* Shutdown all threads in the Thread Pool. */
        threadPool.shutdownAll();
        threadPool.joinAll();
        
        System.out.println("[Info] " + "TcpServerListenThread - exit");
	}
	public synchronized void shutdown() {
        if (!mShutdown) {
            mShutdown = true;
            notify();
        }
	}
	public synchronized boolean isShutdown() {
		return mShutdown;
	}
	
	private synchronized boolean waitForShutdown(final long ms) {
        if (!mShutdown) {
        	try {
        		wait(ms);
        	}
        	catch (InterruptedException e) {}
        }
        
        return mShutdown;
	}
}

/* End of File */
