
public class UdpEndpointAProcess implements Constants, Statuses{
	public static void main(String args[]) {
		/* Child process for UDP Endpoint A */
		System.out.println("[Info] " + "Child process for UDP endpoint A - enter");

		UdpEndpointAThread udpEndpointAThread = null;

		try {
			udpEndpointAThread = new UdpEndpointAThread
					(IP_ADDR_VER_4, UDP_ENDPOINT_A_IP_ADDR, UDP_ENDPOINT_A_PORT_NUMBER);
		}
		catch (Exception e) {
//			ErrLog(<<"Fail to create an instance of UdpEndpointAThread!");
			System.out.println("[Err] " + "Fail to create an instance of UdpEndpointAThread!");
			System.out.println("[Info] " + "Child process for UDP endpoint A - exit");
			System.exit(STATUS_ERR);
		}

		udpEndpointAThread.start();

		try {
			Thread.sleep(UDP_ENDPOINT_A_RUNNING_DURATION);
		}
		catch (InterruptedException e) {}

		try {
			udpEndpointAThread.shutdown();
			udpEndpointAThread.join();
		}
		catch (InterruptedException e) {}

		udpEndpointAThread = null;

		System.out.println("[Info] " + "Child process for UDP endpoint A - exit");
		System.exit(STATUS_OK);
	}
}

/* End of File */
