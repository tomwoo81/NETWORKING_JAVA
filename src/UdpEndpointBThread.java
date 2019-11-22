import java.net.*;
import java.io.*;
import java.lang.Thread;

public class UdpEndpointBThread extends Thread implements Constants{
	private int mIpVer;
	private String mLocalIpAddrStr;
	private short mLocalPortNumber;
	private String mRemoteIpAddrStr;
	private short mRemotePortNumber;
	private final int UDP_ENDPOINT_B_THREAD_BUFFER_LENGTH = (1 * 1024 * 1024); // 1 MB
	
	public UdpEndpointBThread(final int ipVer, final String localIpAddrStr, final short localPortNumber,
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
		System.out.println("[Info] " + "UdpEndpointBThread - enter");

		DatagramSocket socket;
		String rxMsg, txMsg;
		byte[] readBuffer = new byte[UDP_ENDPOINT_B_THREAD_BUFFER_LENGTH], writeBuffer;
		DatagramPacket rxPacket, txPacket;

		/* Open a UDP Endpoint Socket with addr and port. */
		try {
			socket = new DatagramSocket(mLocalPortNumber, InetAddress.getByName(mLocalIpAddrStr)); // blocking I/O
		}
		catch (IOException e) {
//			ErrLog(<<"Fail to open and configure a UDP Endpoint Socket with addr and port!");
			System.out.println("[Err] " + e.toString() + " Fail to open a UDP Endpoint Socket with addr and port!");
			return;
		}

		for (int i = 0; i < UDP_ENDPOINT_B_TX_MSG_NUM; i++) {
			try {
				/* Send a message. */
				txMsg = String.format(UDP_ENDPOINT_B_TX_MSG, i, mLocalPortNumber);
				writeBuffer = txMsg.getBytes();
				txPacket = new DatagramPacket(writeBuffer, writeBuffer.length, InetAddress.getByName(mRemoteIpAddrStr), mRemotePortNumber);
				socket.send(txPacket);
			}
			catch (IOException e) {
//				WarningLog(<<"Fail to send a message to the UDP Peer Endpoint!");
				System.out.println("[Warn] " + "Fail to send a message to the UDP Peer Endpoint!");
				continue;
			}
			System.out.println("[Info] " + "UDP Endpoint (port: " + mLocalPortNumber + ") Tx (" + i + ") [" + txMsg.length() + " bytes]");

			try {
				rxPacket = new DatagramPacket(readBuffer, readBuffer.length);
				socket.receive(rxPacket);
				rxMsg = new String(rxPacket.getData(), rxPacket.getOffset(), rxPacket.getLength(), "utf-8");
			}
			catch (IOException e) {
				/* No received message. */
				break;
			}
			
			/* A received message. */
			
			System.out.println("[Info] " + "UDP Endpoint (port: " + mLocalPortNumber + ") Rx (" + i + ") [" + rxMsg.length() + " bytes]");
			System.out.println("[Info] " + "UDP Endpoint (port: " + mLocalPortNumber + ") Rx (" + i + "): " + rxMsg);
			
			try {
				sleep(UDP_ENDPOINT_B_TX_MSG_INTERVAL);
			}
			catch (InterruptedException e) {}
		}

		/* Close the UDP Endpoint Socket. */
		socket.close();
//		InfoLog(<<"Close the UDP Endpoint Socket.");
		System.out.println("[Info] " + "Close the UDP Endpoint Socket (port: " + mLocalPortNumber + ").");

		System.out.println("[Info] " + "UdpEndpointBThread - exit");
	}
}

/* End of File */
