
public interface Constants{
	int IP_ADDR_VER_4 =  4; /* IPv4 (4-octet address) */
	int IP_ADDR_VER_6 = 16; /* IPv6 (16-octet address) */

	String TCP_SERVER_IP_ADDR = "127.0.0.1"; // Loopback Address
	short TCP_SERVER_PORT_NUMBER = 8888;
	int TCP_CONNECTIONS_MAX_NUM = 5;
	int THREAD_POOL_NUM_THREADS = 3;
	String TCP_SERVER_TX_MSG = "Response from TCP Server to TCP Client";
	int TCP_SERVER_RUNNING_DURATION = 40 * 1000; // Unit: millisecond

	String TCP_CLIENT_IP_ADDR = "127.0.0.1"; // Loopback Address
	short TCP_CLIENT_PORT_NUMBER_BASE = 23500;
	int TCP_CLIENT_TX_MSG_NUM = 5;
	int TCP_CLIENT_TX_MSG_INTERVAL = 3 * 1000; // Unit: millisecond
	String TCP_CLIENT_TX_MSG = "Request (%d) from TCP Client (port: %d) to TCP Server";
	int NUM_TCP_CLIENTS = 5;

	String UDP_ENDPOINT_A_IP_ADDR = "127.0.0.1"; // Loopback Address
	short UDP_ENDPOINT_A_PORT_NUMBER = 8888;
	String UDP_ENDPOINT_A_TX_MSG = "Response from UDP Endpoint A to UDP Endpoint B";
	int UDP_ENDPOINT_A_RUNNING_DURATION = 40 * 1000; // Unit: millisecond

	String UDP_ENDPOINT_B_IP_ADDR = "127.0.0.1"; // Loopback Address
	short UDP_ENDPOINT_B_PORT_NUMBER_BASE = 23500;
	int UDP_ENDPOINT_B_TX_MSG_NUM = 5;
	int UDP_ENDPOINT_B_TX_MSG_INTERVAL = 3 * 1000; // Unit: millisecond
	String UDP_ENDPOINT_B_TX_MSG = "Request (%d) from UDP Endpoint B (port: %d) to UDP Endpoint A";
	int NUM_UDP_ENDPOINTS_B = 5;

	String DIRECTORY = "/home/tarena/workspaces/learning/NETWORKING_JAVA/bin";
	//String SOFTWARE_VERSION = "V10.01.00.00";
}

/* End of File */
