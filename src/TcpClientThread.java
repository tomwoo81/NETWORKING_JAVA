import java.net.*;
import java.io.*;
import java.lang.Thread;

public class TcpClientThread extends Thread implements Constants{
	private int mIpVer;
	private String mLocalIpAddrStr;
	private short mLocalPortNumber;
	private String mRemoteIpAddrStr;
	private short mRemotePortNumber;
	private final int TCP_CLIENT_THREAD_BUFFER_LENGTH = (1 * 1024 * 1024); // 1 MB
	
	public TcpClientThread(final int ipVer, final String localIpAddrStr, final short localPortNumber,
			final String remoteIpAddrStr, final short remotePortNumber) {
		mIpVer = ipVer;
		mLocalIpAddrStr = localIpAddrStr;
		mLocalPortNumber = localPortNumber;
		mRemoteIpAddrStr = remoteIpAddrStr;
		mRemotePortNumber = remotePortNumber;
	}
	protected void finalize() throws Throwable{}
	
	@Override
	public void run() {
		System.out.println("[Info] " + "TcpClientThread - enter");

		Socket socket;
		InputStream is;
		OutputStream os;
		DataInputStream dis;
		DataOutputStream dos;
		String rxMsg, txMsg;
		byte[] readBuffer = new byte[TCP_CLIENT_THREAD_BUFFER_LENGTH];

		/* Open a TCP Client Socket with addr and port. */
		try {
			socket = new Socket(InetAddress.getByName(mRemoteIpAddrStr), mRemotePortNumber, 
					InetAddress.getByName(mLocalIpAddrStr), mLocalPortNumber); // blocking I/O
		}
		catch (IOException e) {
//			ErrLog(<<"Fail to open and configure a TCP Client Socket with addr and port!");
			System.out.println("[Err] " + e.toString() + " Fail to open a TCP Client Socket with addr and port or fail to connect the TCP server!");
			return;
		}

		try {
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
		}
		catch (IOException e) {
			System.out.println("[Err] " + "Fail to get input stream or output stream!");
			return;
		}
		
		for (int i = 0; i < TCP_CLIENT_TX_MSG_NUM; i++) {
			try {
				/* Send a message. */
				txMsg = String.format(TCP_CLIENT_TX_MSG, i, mLocalPortNumber);
				dos.write(txMsg.getBytes());
			}
			catch (IOException e) {
//				WarningLog(<<"Fail to send a message to the TCP server!");
				System.out.println("[Warn] " + "Fail to send a message to the TCP server!");
				continue;
			}
			System.out.println("[Info] " + "TCP Client (port: " + mLocalPortNumber + ") Tx (" + i + ") [" + txMsg.length() + " bytes]");

			try {
				int ret = dis.read(readBuffer);
				rxMsg = new String(readBuffer, 0, ret, "utf-8");
			}
			catch (IOException e) {
				/* No received message. */
				break;
			}
			
			/* A received message. */
			
			System.out.println("[Info] " + "TCP Client (port: " + mLocalPortNumber + ") Rx (" + i + ") [" + rxMsg.length() + " bytes]");
			System.out.println("[Info] " + "TCP Client (port: " + mLocalPortNumber + ") Rx (" + i + "): " + rxMsg);
			
			try {
				sleep(TCP_CLIENT_TX_MSG_INTERVAL);
			}
			catch (InterruptedException e) {}
		}

		try {
			dis.close();
			is.close();
			dos.close();
			os.close();
			
			/* Close the TCP Client Socket. */
			socket.close();
		}
		catch (IOException e) {}
//		InfoLog(<<"Close the TCP Client Socket.");
		System.out.println("[Info] " + "Close the TCP Client Socket (port: " + mLocalPortNumber + ").");

		System.out.println("[Info] " + "TcpClientThread - exit");
	}
}

/* End of File */
