import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;
import java.util.LinkedList;

public class Peer {

	private LinkedList<ThreadP> sockets;
	
	public static void main(String[] args) throws IOException {
		
		String username; 
		String port;
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter username: ");
		username = bufferedReader.readLine();
		
		System.out.print("Enter port to use: ");
		port = bufferedReader.readLine();
		
		
		PeerConnector serverThread = new PeerConnector(Integer.valueOf(port));
		serverThread.start();

		new Peer().updatePeers(bufferedReader, username, serverThread);
		
	}
	
	public void updatePeers(BufferedReader bufferedReader, String username, PeerConnector serverThread) throws IOException{
		
		if(this.sockets == null || this.sockets.isEmpty()) {
			
			sockets = new LinkedList<ThreadP>();
			
		}
		
		String host = "localhost";
		String portnum;
		
		System.out.println("For multiple connections, put space between ports");	
		System.out.println("Enter Port# to connect to peer (join to continue): ");
		portnum = bufferedReader.readLine();
				
		String[] splitted = portnum.split(" ");
		
		if(!portnum.equals("c")) {
			
			for(int i = 0; i < splitted.length; i++) {
				
				Socket socket = null;
				
				try {
					socket = new Socket(host, Integer.valueOf(splitted[i]));
					ThreadP TP = new ThreadP(socket);
					TP.start();
					sockets.add(TP);
					
				}
				catch(Exception e) {
					
					if (socket != null) {
						
						socket.close();
						
					}
					else {
						
						System.out.print("Invalid input. Moving on..");
						
					}
					
				}
				
			}
			
		}
		
		broadcast(bufferedReader, username, serverThread);
		
	}
	
	public void broadcast(BufferedReader bufferedReader, String username, PeerConnector serverThread) {
		
		try {
			
			System.out.println("You can now communicate: (commands: leave, shutdown, shutdown all): ");
			boolean flag = true;
			
			while(flag) {
				
				System.out.print("==>");
				
				String message = bufferedReader.readLine();
				if(message.equals("shutdown")) {
					
					flag = false;
					break;
					
				}
				else if(message.equals("leave")) {
					
					for(int i = 0; i < sockets.size(); i++) {
						
						sockets.get(i).stop();
						sockets.remove(i);
						
					}
					updatePeers(bufferedReader, username, serverThread);
					
				}
				else {
					
					StringWriter stringWriter = new StringWriter();
					
					stringWriter.append("[" + username + "] " + message);
					
					serverThread.sendMessage(stringWriter.toString());
					
					if(message.contains("shutdown all")){
						
						System.out.println("Shutting down..");
						flag = false;
						
					}
					
				}
				
			}
			
			System.out.println("Exiting..");
			System.exit(0);
			
		}
		catch(Exception e) {
			
		}
		
	}

}
