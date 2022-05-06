import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.Color;

//This code is for the Chat Clients to perform operations
public class DsClient extends JFrame implements Runnable {

	public void run(){
		while(true){
			try {
				String msgFromClient = din.readUTF();
				StringTokenizer st = new StringTokenizer(msgFromClient);

				String msg="";
				int lo = -1;

				while(st.hasMoreTokens()){
					String a=st.nextToken();
					if(a.trim().equals("SHUTTINGALL")){
						//System.out.println("alldie");
						socket.close();
						System.exit(1);
						//break;
					}
					msg= msg + " " + a;
				}
				area.append("\n"+ msg);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void update(){
		setSize(600,550);
		JPanel panel = new JPanel();
		//panel.pack();
		panel.setBackground(java.awt.Color.gray);
		panel.add(new JScrollPane(area));
		panel.add(textField);
		panel.add(join);
		panel.add(leave);
		panel.add(note);
		panel.add(shutDown);
		panel.add(shutDownAll);
		add(panel);
		setVisible(true);

	}

	//define the Java swing classes for the functionalities
	Socket socket;
	JTextArea area;
	JButton note,shutDown,shutDownAll,join,leave;
	JTextField textField;
	Thread thread;
	public Boolean already_joined=false;
	DataInputStream din;
	DataOutputStream dout;
	String name;
	
	public DsClient (String chatLogin,Boolean joined) throws UnknownHostException, IOException {
		super(chatLogin);
		name = chatLogin;
		this.already_joined=joined;

		//this class is notified whenever we change the state of window
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) { }
			
			@Override
			public void windowIconified(WindowEvent e) { }
			
			@Override
			public void windowDeiconified(WindowEvent e) { }
			
			@Override
			public void windowDeactivated(WindowEvent e) { }
			
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					dout.writeUTF(name + " " + "SHUTDOWN");
					System.exit(1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) { }
			
			@Override
			public void windowActivated(WindowEvent e) { }
		});
		area = new JTextArea(25,50);
		textField = new JTextField(50);
		textField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) { }
			
			@Override
			public void keyReleased(KeyEvent e) { }
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() ==  KeyEvent.VK_ENTER){
					if(already_joined){
						try {
							if(textField.getText().length()>0)
							dout.writeUTF(name + " " + "DATA " + textField.getText().toString());
							textField.setText("");
						} catch (IOException e1) {
							e1.printStackTrace();
						}	
					}else{
						try{
							textField.setText("JOIN FIRST!!");
							// textField.setText("");
						}catch(Exception e1){
							e1.printStackTrace();
						}
						
					}				
			}
			}
		});


		join = new JButton("Chat Join");
		join.setBackground(Color.BLUE);
		join.setForeground(Color.WHITE);
		leave = new JButton("Chat Leave");
		leave.setBackground(Color.BLUE);
		leave.setForeground(Color.WHITE);
		note = new JButton("Chat Message");
		note.setBackground(Color.BLUE);
		note.setForeground(Color.WHITE);
		shutDown = new JButton("Chat ShutDown");
		shutDown.setBackground(Color.BLUE);
		shutDown.setForeground(Color.WHITE);
		shutDownAll=new JButton("Chat ShutDownAll");
		shutDownAll.setBackground(Color.BLUE);
		shutDownAll.setForeground(Color.WHITE);
		note.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(already_joined){
					try {
						if(textField.getText().length()>0)
						dout.writeUTF(name + " " + "DATA " + textField.getText().toString());
						textField.setText("");
					} catch (IOException e1) {
						e1.printStackTrace();
					}	
				}else{
					try{
						// dout.writeUTF(name + " " + "DATA " + "JOIN FIRST");
					textField.setText("JOIN FIRST!!");
					}catch(Exception e1){
						e1.printStackTrace();
					}
					
				}
			}
		});
		shutDownAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
					try {
						dout.writeUTF(name + " " +"SHUTDOWNALL");
						// area.append("\n"+"shutting down!");
						//dout.writeUTF(name + " " + "SHUTDOWN");
						textField.setText("");
						System.exit(1);
						try{
								Thread.sleep(2000);
							}catch(InterruptedException ex){
								Thread.currentThread().interrupt();
							}

					} catch (IOException e1) {
						e1.printStackTrace();
					}	
				}
		});
		join.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!already_joined){
					try {
						dout.writeUTF(name + " " + "JOIN");
						already_joined=true;
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else{
					try {
						// dout.writeUTF(name + " " + "DATA " + "You Need to leave before joining!");
						textField.setText("--- HIT LEAVE BEFORE JOIN ---");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				
			}
		});
		leave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(already_joined){
					try {
						
						dout.writeUTF(name + " " + "LEAVE");
						already_joined=false;
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else{
					try {
						// dout.writeUTF(name + " " + "DATA " + "You Need to join first before leaving!");
						textField.setText("--- HIT JOIN BEFORE LEAVE ---");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		shutDown.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dout.writeUTF(name + " " + "SHUTDOWN");
					System.exit(1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		socket = new Socket("localhost",5217);
		din = new DataInputStream(socket.getInputStream());
		dout = new DataOutputStream(socket.getOutputStream());
		
		dout.writeUTF(name);
		dout.writeUTF(name +" " +"JOIN");
		
		thread = new Thread(this);
		thread.start();
		update();
	}
}
