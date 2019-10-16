
public class TcpServerProcess implements Constants, Statuses{
	public static void main(String args[]) {
		/* Child process for TCP server */
		System.out.println("[Info] " + "Child process for TCP server - enter");

		TcpServerListenThread tcpServerListenThread = null;

		try {
			tcpServerListenThread = new TcpServerListenThread
					(IP_ADDR_VER_4, TCP_SERVER_IP_ADDR, TCP_SERVER_PORT_NUMBER);
		}
		catch (Exception e) {
//			ErrLog(<<"Fail to create an instance of TcpServerListenThread!");
			System.out.println("[Err] " + "Fail to create an instance of TcpServerListenThread!");
			System.out.println("[Info] " + "Child process for TCP server - exit");
			System.exit(STATUS_ERR);
		}

		tcpServerListenThread.start();

		try {
			Thread.sleep(TCP_SERVER_RUNNING_DURATION);
		}
		catch (InterruptedException e) {}

		try {
			tcpServerListenThread.shutdown();
			tcpServerListenThread.join();
		}
		catch (InterruptedException e) {}

		tcpServerListenThread = null;

		System.out.println("[Info] " + "Child process for TCP server - exit");
		System.exit(STATUS_OK);
	}
}

/* End of File */
