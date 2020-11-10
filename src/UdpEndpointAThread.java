import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.lang.Thread;

public class UdpEndpointAThread extends Thread implements Constants{
	private int mIpVer;
	private String mLocalIpAddrStr;
	private short mLocalPortNumber;
	private boolean mShutdown;
	private final int UDP_ENDPOINT_A_THREAD_BUFFER_LENGTH = (512 * 1024); // 512 KB
	
	public UdpEndpointAThread(final int ipVer, final String localIpAddrStr, 
	final short localPortNumber) {
		mIpVer = ipVer;
		mLocalIpAddrStr = localIpAddrStr;
		mLocalPortNumber = localPortNumber;
		mShutdown = false;
	}
	protected void finalize() throws Throwable{}
	
	@Override
	public void run() {
		System.out.println("[Info] " + "UdpEndpointAThread - enter");
		
		DatagramChannel channel;
		
		try {
			/* Open a UDP Endpoint Socket Channel. */
			channel = DatagramChannel.open();
			
			/* Non-blocking I/O */
			channel.configureBlocking(false);
			
			/* Make address reusable. */
			channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			
			/* Configure a UDP Endpoint Socket Channel with addr and port. */
			channel.socket().bind(new InetSocketAddress(mLocalIpAddrStr, mLocalPortNumber));
		}
		catch (IOException e) {
//			ErrLog(<<"Fail to open a UDP Endpoint Socket Channel with addr and port!");
			System.out.println("[Err] " + "Fail to open a UDP Endpoint Socket Channel with addr and port!");
			return;
		}
		
		Selector selector;
		
		try {
			/* Open a selector. */
			selector = Selector.open();
			
			/* Register the READ event to the selector. */
			channel.register(selector, SelectionKey.OP_READ);
		}
		catch (IOException e) {
//			ErrLog(<<"Fail to open a selector or register the READ event to the selector!");
			System.out.println("[Err] " + "Fail to open a selector or register the READ event to the selector!");
			return;
		}
		
		/* Allocate a read buffer. */
        ByteBuffer readBuffer = ByteBuffer.allocate(UDP_ENDPOINT_A_THREAD_BUFFER_LENGTH);
        
		/* Allocate a write buffer. */
        ByteBuffer writeBuffer = ByteBuffer.allocate(UDP_ENDPOINT_A_THREAD_BUFFER_LENGTH);
        
        /* Configure Character Set. */
        Charset charset = Charset.forName("utf-8");
        
        String rxMsg, txMsg;
        
        SocketAddress addr;
        
		while (!waitForShutdown(10)) { //Wait for 10 ms.
        	while (true) {
            	try {
            		if (selector.selectNow() <= 0) {
    					/* No received message. */
    					break;
            		}
            	}
				catch (IOException e) {
					/* No received message. */
					break;
				}
            	
            	/* Received message(s). */
            	
            	for (SelectionKey sk: selector.selectedKeys()) {
					if (sk.isReadable()) {
						try {
							addr = ((DatagramChannel)sk.channel()).receive(readBuffer);
							readBuffer.flip();
							rxMsg = charset.decode(readBuffer).toString();
							System.out.println("[Info] " + "UDP Endpoint Rx [" + rxMsg.length() + " bytes]");
							System.out.println("[Info] " + "UDP Endpoint Rx: " + rxMsg);
							readBuffer.clear();
						}
						catch (IOException e) {
							continue;
						}
						
						try {
							/* Send a message. */
							txMsg = new String(UDP_ENDPOINT_A_TX_MSG);
							writeBuffer = charset.encode(txMsg);
							((DatagramChannel)sk.channel()).send(writeBuffer, addr);
						}
						catch (IOException e) {
//							WarningLog(<<"Fail to send a message to a UDP Peer Endpoint!");
							System.out.println("[Warn] " + "Fail to send a message to a UDP Peer Endpoint!");
							continue;
						}
						System.out.println("[Info] " + "UDP Endpoint Tx [" + txMsg.length() + " bytes]");
					}
					
					selector.selectedKeys().remove(sk);
				}
			}
		}
		
        /* Close the UDP Endpoint Socket Channel. */
		try {
			channel.close();
		}
		catch (IOException e) {}
//		InfoLog(<<"Close the UDP Endpoint Socket Channel.");
		System.out.println("[Info] " + "Close the UDP Endpoint Socket Channel.");
		
        System.out.println("[Info] " + "UdpEndpointAThread - exit");
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
