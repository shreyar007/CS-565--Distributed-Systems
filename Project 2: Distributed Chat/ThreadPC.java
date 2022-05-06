import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadPC extends Thread{

	private PeerConnector serverThread;
	private Socket socket;
	private PrintWriter printWriter;
	
	public ThreadPC(Socket socket, PeerConnector serverThread) {
		
		this.serverThread = serverThread;
		this.socket = socket;
		
	}
	
	public void run() {
		
		try {
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.printWriter = new PrintWriter(socket.getOutputStream(), true);
			while(true) {
				
				serverThread.sendMessage(bufferedReader.readLine());
				
			}
		}
		catch (Exception e) {
			
			serverThread.getRing().remove(this);
			
		}	
	}
	
	public PrintWriter getPrintWriter() {
		
		return printWriter;
		
	}
	
	
}
