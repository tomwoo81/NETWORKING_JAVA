import java.io.*;

public class Networking implements Constants, Statuses{
	public static void main(String args[]) {
		System.out.println("[Info] " + "Main process - enter");

		ProcessBuilder tcpServerProcessBuilder;
		Process tcpServerProcess = null;
		
		try {
			tcpServerProcessBuilder = new ProcessBuilder("java", "TcpServerProcess");
			tcpServerProcessBuilder.directory(new File(DIRECTORY));
			/* merge the error output with the standard output */
			tcpServerProcessBuilder.redirectErrorStream(true);
			tcpServerProcess = tcpServerProcessBuilder.start();
		}
		catch (Exception e) {
			/* Main process */
			System.out.println("[Err] " + "Fail to start a child process!");
			System.out.println("[Info] " + "Main process - exit");
			System.exit(STATUS_ERR);
		}
		
//		/* read the standard output */
//		BufferedReader stdout1 = new BufferedReader(new InputStreamReader(tcpServerProcess.getInputStream()));
//		String line1;
//		try {
//			while (null != (line1 = stdout1.readLine())) {
//			    System.out.println(line1);
//			}
//		}
//		catch (IOException e) {}
		
		/* Main process */
		ProcessBuilder tcpClientProcessBuilder[] = new ProcessBuilder[NUM_TCP_CLIENTS];
		Process tcpClientProcess[] = new Process[NUM_TCP_CLIENTS];
		BufferedReader stdout2[] = new BufferedReader[NUM_TCP_CLIENTS];
		String line2[] = new String[NUM_TCP_CLIENTS];
		
		for (int i = 0; i < NUM_TCP_CLIENTS; i++) {
			try {
				Thread.sleep(TCP_CLIENT_TX_MSG_INTERVAL);
			}
			catch (InterruptedException e) {}
			
			try {
				tcpClientProcessBuilder[i] = new ProcessBuilder("java", "TcpClientProcess", String.valueOf(i));
				tcpClientProcessBuilder[i].directory(new File(DIRECTORY));
				/* merge the error output with the standard output */
				tcpClientProcessBuilder[i].redirectErrorStream(true);
				tcpClientProcess[i] = tcpClientProcessBuilder[i].start();
			}
			catch (Exception e) {
				/* Main process */
				System.out.println("[Err] " + "Fail to start a child process!");
				System.out.println("[Info] " + "Main process - exit");
				System.exit(STATUS_ERR);
			}
			
//			/* read the standard output */
//			stdout2[i] = new BufferedReader(new InputStreamReader(tcpClientProcess[i].getInputStream()));
//			try {
//				while (null != (line2[i] = stdout2[i].readLine())) {
//				    System.out.println(line2[i]);
//				}
//			}
//			catch (IOException e) {}
		}
		
		for (int i = 0; i < NUM_TCP_CLIENTS; i++) {
			/* read the standard output */
			stdout2[i] = new BufferedReader(new InputStreamReader(tcpClientProcess[i].getInputStream()));
			try {
				while (null != (line2[i] = stdout2[i].readLine())) {
				    System.out.println(line2[i]);
				}
			}
			catch (IOException e) {}
		}
		
		/* read the standard output */
		BufferedReader stdout1 = new BufferedReader(new InputStreamReader(tcpServerProcess.getInputStream()));
		String line1;
		try {
			while (null != (line1 = stdout1.readLine())) {
			    System.out.println(line1);
			}
		}
		catch (IOException e) {}
		
		/* Wait for shutdown of all child processes. */
		try {
			tcpServerProcess.waitFor();
		}
		catch (InterruptedException e) {}
		for (int i = 0; i < NUM_TCP_CLIENTS; i++) {
			try {
				tcpClientProcess[i].waitFor();
			}
			catch (InterruptedException e) {}
		}
		
		try {
			stdout1.close();
		}
		catch (IOException e) {}
		for (int i = 0; i < NUM_TCP_CLIENTS; i++) {
			try {
				stdout2[i].close();
			}
			catch (IOException e) {}
		}

		System.out.println("[Info] " + "Main process - exit");
		System.exit(STATUS_OK);
	}
}

/* End of File */
