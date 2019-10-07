
public interface Constants{
	int IP_ADDR_VER_4 =  4; /* IPv4 (4-octet address) */
	int IP_ADDR_VER_6 = 16; /* IPv6 (16-octet address) */

	String TCP_SERVER_IP_ADDR = "127.0.0.1"; //Loopback Address
	short TCP_SERVER_PORT_NUMBER = 80;
	int TCP_CONNECTIONS_MAX_NUM = 5;
	int THREAD_POOL_NUM_THREADS = 3;
	String TCP_SERVER_TX_MSG = "Response from TCP Server to TCP Client";
	int TCP_SERVER_RUNNING_DURATION = 40 * 1000; //Unit: millisecond

	String TCP_CLIENT_IP_ADDR = "127.0.0.1"; //Loopback Address
	short TCP_CLIENT_PORT_NUMBER_BASE = 23500;
	int TCP_CLIENT_TX_MSG_NUM = 5;
	int TCP_CLIENT_TX_MSG_INTERVAL = 3 * 1000; //Unit: millisecond
	String TCP_CLIENT_TX_MSG = "Request (%d) from TCP Client (port: %d) to TCP Server";
	int NUM_TCP_CLIENTS = 5;

	String DIRECTORY = "D:\\workspaces\\learning\\NETWORKING_JAVA\\bin";
	//String SOFTWARE_VERSION = "V10.01.00.00";
}

/* End of File */
