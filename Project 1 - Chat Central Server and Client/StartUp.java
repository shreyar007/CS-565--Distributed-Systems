import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTextField;

//acts as a GUI for our application for the Join functionality
public class StartUp {
	public static void main(String [] args){

	//initializing the Java swing modules
	//setting the color of the Java Frame, Text, and Button Background Color

	final JFrame finalFrame = new JFrame("Enter Your Name..");
	JPanel panel = new JPanel();
	final JTextField name = new JTextField(30);
	JButton click = new JButton("JOIN");
	click.setBackground(Color.BLUE);
	click.setForeground(Color.WHITE);
	click.setOpaque(true);
	panel.add(name);
	panel.add(click);
	panel.setBackground(java.awt.Color.gray);
	finalFrame.add(panel);
	finalFrame.setSize(300, 300);
	finalFrame.pack();
	finalFrame.setVisible(true);
	finalFrame.setLocationRelativeTo(null);
	finalFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	//The listener interface for receiving keyboard events
	name.addKeyListener(new KeyListener() {
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				try {
					DsClient client = new DsClient(name.getText(),true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				finalFrame.setVisible(false);
				finalFrame.dispose();
			}
		}
		@Override
		public void keyReleased(KeyEvent e) { }
		@Override
		public void keyTyped(KeyEvent e) { }
	});

	//This class is responsible for handling all action events such as when the user clicks on a component.
	click.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				DsClient client = new DsClient(name.getText(),true);
				finalFrame.setVisible(false);
				finalFrame.dispose();
			} catch (IOException e1) {

				e1.printStackTrace();
			}

		}
	});
}
}
