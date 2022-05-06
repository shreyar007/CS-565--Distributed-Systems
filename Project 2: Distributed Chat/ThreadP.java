import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ThreadP extends Thread {

	private BufferedReader bufferedReader;
	private Socket socket;
	
	public ThreadP(Socket socket) throws IOException{
	
		this.socket = socket;
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
	}
	
	public void run() {
		
		boolean flag = true;
		while(flag) {
			
			try {
				
				if(socket.isClosed()) {
					
					return;
					
				}
				
				String received = bufferedReader.readLine();
				//bufferedReader.close();
				//bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				System.out.println(received);
				if(received.contains("shutdown") ||received.contains("SHUTDOWN")){
					
					System.out.println("Exiting..");
					System.exit(0);
					
				}
				
				
			}
			catch(Exception e) {
				
				flag = false;
				interrupt();
				
			}
			
		}
		
	}
}
