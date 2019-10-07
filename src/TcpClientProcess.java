
public class TcpClientProcess implements Constants, Statuses{
	public static void main(String args[]) {
		int i = Integer.valueOf(args[0]);

		/* Child process for TCP client */
		System.out.println("[Info] " + "Child process for TCP client " + i + " - enter");

		TcpClientThread tcpClientThread = null;

		try
		{
			tcpClientThread = new TcpClientThread(IP_ADDR_VER_4, TCP_CLIENT_IP_ADDR, (short)(TCP_CLIENT_PORT_NUMBER_BASE + i),
					TCP_SERVER_IP_ADDR, TCP_SERVER_PORT_NUMBER);
		}
		catch (Exception e)
		{
//			ErrLog(<<"Fail to create an instance of TcpClientThread!");
			System.out.println("[Err] " + "Fail to create an instance of TcpClientThread!");
			System.out.println("[Info] " + "Child process for TCP client " + i + " - exit");
			System.exit(STATUS_ERR);
		}

		tcpClientThread.start();

		try {
			tcpClientThread.join();
		}
		catch (InterruptedException e) {}

		tcpClientThread = null;

		System.out.println("[Info] " + "Child process for TCP client " + i + " - exit");
		System.exit(STATUS_OK);
	}
}

/* End of File */
