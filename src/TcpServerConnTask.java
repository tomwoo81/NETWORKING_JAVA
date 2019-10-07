import java.io.*;
import java.net.*;

public class TcpServerConnTask extends PoolTaskIf implements Constants{
	private Socket mSocket;
	
	public TcpServerConnTask(final Socket socket) {
		mSocket = socket;
	}
	protected void finalize() throws Throwable{
		if (null != mSocket) {
			mSocket = null;
		}
	}
	
	@Override
	public void run() {
		System.out.println("[Info] " + "TcpServerConnTask - enter");

		InputStream is;
		OutputStream os;
		DataInputStream dis;
		DataOutputStream dos;
		String rxMsg, txMsg;

//		mSocket.setFlag(0); //blocking I/O

		try {
			is = mSocket.getInputStream();
			dis = new DataInputStream(is);
			os = mSocket.getOutputStream();
			dos = new DataOutputStream(os);
		}
		catch (IOException e) {
			System.out.println("[Err] " + "Fail to get input stream or output stream!");
			return;
		}
		
		while (true) {
			try {
//				is = mSocket.getInputStream();
//				dis = new DataInputStream(is);
				rxMsg = new String(dis.readUTF());
			}
			catch (IOException e) {
				/* No received message. */
				break;
			}
			
			/* A received message. */
			
			System.out.println("[Info] " + "TCP Server Rx [" + rxMsg.length() + " bytes]");
			System.out.println("[Info] " + "TCP Server Rx: " + rxMsg);
			
			try {
				/* Send a message. */
//				os = mSocket.getOutputStream();
//				dos = new DataOutputStream(os);
				txMsg = new String(TCP_SERVER_TX_MSG);
				dos.writeUTF(txMsg);
			}
			catch (IOException e) {
//				WarningLog(<<"Fail to send a message to a TCP client!");
				System.out.println("[Warn] " + "Fail to send a message to a TCP client!");
				continue;
			}
			
			System.out.println("[Info] " + "TCP Server Tx [" + txMsg.length() + " bytes]");
		}

		try {
			dis.close();
			is.close();
			dos.close();
			os.close();
		}
		catch (IOException e) {}

		System.out.println("[Info] " + "TcpServerConnTask - exit");
	}
}

/* End of File */
