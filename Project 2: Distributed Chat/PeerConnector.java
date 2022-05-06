import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class PeerConnector extends Thread {

	private ServerSocket serverSocket;
	private Ring peersInRing = new Ring();
	
	public PeerConnector(int portnum) throws IOException{
		
		serverSocket = new ServerSocket((portnum));
		
	}
	
	public void run() {
		
		try {
			
			while(true) {
				
				ThreadPC serverThreadThread = new ThreadPC(serverSocket.accept(), this);
				peersInRing.add(serverThreadThread);
				serverThreadThread.start();
				
			}
			
		}
		catch (Exception e) {
			
			e.printStackTrace();
			
		}		
		
	}
	
	void sendMessage(String message) {
		
		try {
						
			//peersInRing.forEach(t -> t.getPrintWriter().println(message));
			peersInRing.broadcastMsg(message);
			
		}
		catch (Exception e) {
			
			e.printStackTrace();
			
		}
	}
	
	public Ring getRing(){
		
		return peersInRing;
		
	}
	
}
