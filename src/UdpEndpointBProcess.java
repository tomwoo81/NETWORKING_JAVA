
public class UdpEndpointBProcess implements Constants, Statuses{
	public static void main(String args[]) {
		int i = Integer.valueOf(args[0]);

		/* Child process for UDP Endpoint B */
		System.out.println("[Info] " + "Child process for UDP Endpoint B " + i + " - enter");

		UdpEndpointBThread udpEndpointBThread = null;

		try
		{
			udpEndpointBThread = new UdpEndpointBThread(IP_ADDR_VER_4, UDP_ENDPOINT_B_IP_ADDR, (short)(UDP_ENDPOINT_B_PORT_NUMBER_BASE + i),
					UDP_ENDPOINT_A_IP_ADDR, UDP_ENDPOINT_A_PORT_NUMBER);
		}
		catch (Exception e)
		{
//			ErrLog(<<"Fail to create an instance of UdpEndpointBThread!");
			System.out.println("[Err] " + "Fail to create an instance of UdpEndpointBThread!");
			System.out.println("[Info] " + "Child process for Udp Endpoint B " + i + " - exit");
			System.exit(STATUS_ERR);
		}

		udpEndpointBThread.start();

		try {
			udpEndpointBThread.join();
		}
		catch (InterruptedException e) {}

		udpEndpointBThread = null;

		System.out.println("[Info] " + "Child process for Udp Endpoint B " + i + " - exit");
		System.exit(STATUS_OK);
	}
}

/* End of File */
