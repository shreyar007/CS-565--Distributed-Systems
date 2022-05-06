import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

//class that handles all the functionalities of the chat client
public class DsServer {
	ServerSocket serverSocket;
	static Vector ClientSockets;
	static Vector ClientLogin;

	public static void main(String[] args) throws IOException{
		DsServer serverSocket = new DsServer();
	}
	class TakeInput extends Thread {
		Socket ClientSocket;
		DataInputStream din;
		DataOutputStream dout;

		public TakeInput(Socket client) throws IOException{
			ClientSocket = client;
			din = new DataInputStream(ClientSocket.getInputStream());
			dout = new DataOutputStream(ClientSocket.getOutputStream());
			String chatName = din.readUTF();
			ClientLogin.add(chatName);
			ClientSockets.add(ClientSocket);
			start();
		}
		public void run(){
			while(true){
				String clientMessage;
				try {
					clientMessage = din.readUTF();
					StringTokenizer st = new StringTokenizer(clientMessage);
					String chatName = st.nextToken();
					String message = st.nextToken();
					String M = "";
					int lo = -1;

					while(st.hasMoreTokens()){
						M= M + " " + st.nextToken();
					}
					if(message.equals("LEAVE")) {
						for (int i = 0; i < ClientLogin.size(); i++) {
							if (chatName.equals(ClientLogin.elementAt(i)))
								lo = i;
							Socket pSocket = (Socket) ClientSockets.elementAt(i);
							DataOutputStream pOut;
							try {
								pOut = new DataOutputStream(pSocket.getOutputStream());
								pOut.writeUTF(chatName + " LEFT --");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					else if(message.equals("JOIN")){
						for(int i=0;i<ClientLogin.size();++i){
							Socket pSocket = (Socket) ClientSockets.elementAt(i);
							DataOutputStream pOut;
							try {
								pOut = new DataOutputStream(pSocket.getOutputStream());

								pOut.writeUTF(chatName + " JOINED --");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					else if(message.equals("LEAVE")){
						for(int i=0;i<ClientLogin.size();i++){
							if(chatName.equals(ClientLogin.elementAt(i)))
								lo = i;
								Socket pSocket = (Socket) ClientSockets.elementAt(i);
								DataOutputStream pOut;
							try {
								pOut = new DataOutputStream(pSocket.getOutputStream());
								pOut.writeUTF(chatName+ " LEFT--");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if(lo>=0){
							ClientLogin.removeElement(lo);
							ClientSockets.removeElement(lo);
						}
					}
					else if(message.equals("SHUTDOWNALL")){
						System.out.println("-- ALL --");
						for(int i=0;i<ClientLogin.size();i++){
							Socket pSocket = (Socket) ClientSockets.elementAt(i);
							DataOutputStream pOut;
							try {
								pOut = new DataOutputStream(pSocket.getOutputStream());
								pOut.writeUTF("SHUTTINGALL");
								ClientLogin.removeElement(i);
								ClientSockets.removeElement(i);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						serverSocket.close();
					}
					else if(message.equals("SHUTDOWN")){
						for(int i=0;i<ClientLogin.size();i++){
							if(chatName.equals(ClientLogin.elementAt(i)))
								lo = i;
							Socket pSocket = (Socket) ClientSockets.elementAt(i);
							DataOutputStream pOut;
							try {
								pOut = new DataOutputStream(pSocket.getOutputStream());

								pOut.writeUTF(chatName+ " HAS SHUTDOWN --");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if(lo>=0){
							ClientLogin.removeElement(lo);
							ClientSockets.removeElement(lo);
						}
					}
					else{
						System.out.println("all");
						for(int i=0;i<ClientLogin.size();i++){
							Socket pSocket = (Socket) ClientSockets.elementAt(i);
							DataOutputStream pOut;
							try {
								pOut = new DataOutputStream(pSocket.getOutputStream());

								pOut.writeUTF(chatName+ ":" + M);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					if(message.equals("SHUTDOWN"))
						break;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public DsServer() throws IOException{
		serverSocket = new ServerSocket(5217);
		ClientSockets = new Vector<>();
		ClientLogin = new Vector<>();
		
		while(true){
			Socket client = serverSocket.accept();
			TakeInput acceptClient = new TakeInput(client);
		}
	}
	}
	
