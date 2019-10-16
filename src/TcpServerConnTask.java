import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;

public class TcpServerConnTask extends PoolTaskIf implements Constants{
	private SocketChannel mSocket;
	private final int TCP_SERVER_TASK_BUFFER_LENGTH = (512 * 1024); // 512 KB
	
	public TcpServerConnTask(final SocketChannel socket) {
		mSocket = socket;
	}
	protected void finalize() throws Throwable{
        if (null != mSocket) {
        	/* Close the TCP Server Socket for connection. */
        	mSocket.close();
//        	InfoLog(<<"Close the TCP Server Socket for connection.");
        	System.out.println("[Info] " + "Close the TCP Server Socket for connection.");
        	mSocket = null;
        }
	}
	
	@Override
	public void run() {
		System.out.println("[Info] " + "TcpServerConnTask - enter");
		
		try {
			/* Non-blocking I/O */
			mSocket.configureBlocking(false);
		}
		catch (IOException e) {
//			ErrLog(<<"Fail to configure a non-blocking I/O!");
			System.out.println("[Warn] " + "Fail to configure a non-blocking I/O!");
			return;
		}
		
		Selector selector;
		
		try {
			/* Open a selector. */
			selector = Selector.open();
			
			/* Register the ACCEPT event to the selector. */
			mSocket.register(selector, SelectionKey.OP_READ);
		}
		catch (IOException e) {
//			ErrLog(<<"Fail to open a selector or register the READ event to the selector!");
			System.out.println("[Err] " + "Fail to open a selector or register the READ event to the selector!");
			return;
		}
		
		/* Allocate a read buffer. */
        ByteBuffer readBuffer = ByteBuffer.allocate(TCP_SERVER_TASK_BUFFER_LENGTH);
        
		/* Allocate a write buffer. */
        ByteBuffer writeBuffer = ByteBuffer.allocate(TCP_SERVER_TASK_BUFFER_LENGTH);
        
        /* Configure Character Set. */
        Charset charset = Charset.forName("utf-8");
        
        String rxMsg, txMsg;
        
        boolean connected = true;
        
        while (!mThreadPool.isShutdown()) {
        	mSleep(10); // Wait for 10 ms.
        	
        	/* Check whether the TCP connection exists. */
        	if (!connected) {
//            	InfoLog(<<"A TCP connection is shutdown.");
            	System.out.println("[Info] " + "A TCP connection is shutdown.");
            	break;
        	}
        	
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
							((SocketChannel)sk.channel()).read(readBuffer);
							readBuffer.flip();
							rxMsg = charset.decode(readBuffer).toString();
							if (rxMsg.length() <= 0) {
								connected = false;
								break;
							}
							System.out.println("[Info] " + "TCP Server Rx [" + rxMsg.length() + " bytes]");
							System.out.println("[Info] " + "TCP Server Rx: " + rxMsg);
							readBuffer.clear();
						}
						catch (IOException e) {
							continue;
						}
						
						try {
							/* Send a message. */
							txMsg = new String(TCP_SERVER_TX_MSG);
							writeBuffer = charset.encode(txMsg);
							((SocketChannel)sk.channel()).write(writeBuffer);
						}
						catch (IOException e) {
//							WarningLog(<<"Fail to send a message to a TCP client!");
							System.out.println("[Warn] " + "Fail to send a message to a TCP client!");
							continue;
						}
						System.out.println("[Info] " + "TCP Server Tx [" + txMsg.length() + " bytes]");
					}
					
					selector.selectedKeys().remove(sk);
				}
            	
            	if (!connected) {
            		break;
            	}
			}
        }
        
        System.out.println("[Info] " + "TcpServerConnTask - exit");
	}
}

/* End of File */
