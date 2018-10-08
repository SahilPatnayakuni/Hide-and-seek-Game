package sp50_egd2.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import gov.nasa.worldwind.globes.Earth;
import map.MapPanel;
import java.awt.FlowLayout;

public class GameView extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6347151905287063755L;
	
	/**
	 * the frame this Game Displays in
	 */
	private JFrame frameGame;
	/**
	 * text field for inputting IP
	 */
	private JTextField txtFieldIPAddress;
	/**
	 * button for connecting to input IP
	 */
	private JButton btnConnect;
	/**
	 * panel which hosts chatroom interface
	 */
	private JPanel pnlGameCmds;
	/**
	 * button to create chatroom
	 */
	private JButton btnCreateChatroom;
	/**
	 * panel on which user info is dislpayed
	 */
	private JPanel pnlUser;
	/**
	 * text field to change username
	 */
	private JTextField txtUsername;
	/**
	 * button to update username
	 */
	private JButton btnUpdateUsername;
	/**
	 * panel on which messaging interfaces are displayed
	 */
	private JPanel pnlChatroomTabs;
	/**
	 * button to join a chatroom
	 */
	private JButton btnHidersWin;
	
	private JButton btnSeekersWin;
	/**
	 * the adapter to the model
	 */
	private IView2ModelAdapter view2ModelAdpt;
	/**
	 * label which displays current username
	 */
	private JLabel lblUsername;
	/**
	 * lavel which displays IP
	 */
	private JLabel lblIPAddress;
	/**
	 * button to leave a chatroom 
	 */
	private JButton btnLeaveGame;
	private JPanel pnlConnect;
	private JPanel pnlEndGame;
	
	private MapPanel mapPanel;


	/**
	 * Create the application.
	 * @param view2ModelAdpt	The adapter that connects the view to the model.
	 */
	public GameView(IView2ModelAdapter view2ModelAdpt) {
		this.view2ModelAdpt = view2ModelAdpt;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frameGame = new JFrame();
		frameGame.setTitle("GameClient");
		frameGame.setBounds(100, 100, 700, 550);
		frameGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frameGame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e){
		    		view2ModelAdpt.closeApp(); 
		    	}
		});
		
		
		JPanel pnlLeft = new JPanel();
		pnlLeft.setBackground(new Color(255, 250, 240));
		pnlLeft.setToolTipText("Panel containing user and chatroom info");
		frameGame.getContentPane().add(pnlLeft, BorderLayout.WEST);
		pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
		
		pnlConnect = new JPanel();
		pnlConnect.setBackground(new Color(152,251,152));
		pnlConnect.setToolTipText("Panel for adding a new contact.");
		pnlLeft.add(pnlConnect);
		pnlConnect.setBorder(new TitledBorder(null, "Connect", TitledBorder.CENTER,
				TitledBorder.TOP, null, null));
		pnlConnect.setLayout(new GridLayout(2, 0, 0, 0));
		
		txtFieldIPAddress = new JTextField();
		pnlConnect.add(txtFieldIPAddress);
		txtFieldIPAddress.setText("localhost");
		txtFieldIPAddress.setToolTipText("Text field to enter ip address");
		txtFieldIPAddress.setColumns(10);
		
		btnConnect = new JButton("Connect");
		pnlConnect.add(btnConnect);
		btnConnect.setToolTipText("Button to connect to ip addresses");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ipAddress = txtFieldIPAddress.getText();	
				if (ipAddress.length() > 0) {
					int returnStatus = view2ModelAdpt.connect(ipAddress);
					if (returnStatus < 0) {
						JOptionPane.showMessageDialog(frameGame, "Could not connect to " + ipAddress + ".");
					}
					
				} else {
					JOptionPane.showMessageDialog(frameGame, "Enter an ip address!");
				}
				
			
			}
		});
		
		pnlEndGame = new JPanel();
		pnlEndGame.setToolTipText("Panel for chatrooms");
		pnlEndGame.setBackground(new Color(152,251,152));
		pnlLeft.add(pnlEndGame);
		pnlEndGame.setBorder(new TitledBorder(null, "End Game", TitledBorder.CENTER,
				TitledBorder.TOP, null, null));
		pnlEndGame.setLayout(new GridLayout(3, 0, 0, 0));
		
		
		btnHidersWin = new JButton("Hiders Win");
		pnlEndGame.add(btnHidersWin);
		btnHidersWin.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.endGameHidersWin();
			}
		});
		btnHidersWin.setToolTipText("Button to add yourself to Chatroom");
		
		btnSeekersWin = new JButton("Seekers Win");
		pnlEndGame.add(btnSeekersWin);
		btnSeekersWin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.endGameSeekersWin();
				
			}
			
		});
		
		btnLeaveGame = new JButton("Leave Chatroom");
		btnLeaveGame.setToolTipText("Button to leave the room");
		btnLeaveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.closeApp();
			}
		});
		
		pnlGameCmds = new JPanel();
		pnlGameCmds.setBackground(new Color(152,251,152));
		pnlGameCmds.setToolTipText("Panel for creating new chatroom");
		pnlLeft.add(pnlGameCmds);
		pnlGameCmds.setBorder(new TitledBorder(null, "Game Cmds", TitledBorder.CENTER,

						TitledBorder.TOP, null, null));
		pnlGameCmds.setLayout(new GridLayout(2, 1, 0, 0));
		pnlGameCmds.add(btnLeaveGame);
		
		btnCreateChatroom = new JButton("Next Round!");
		btnCreateChatroom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.nextRound();
			}
		});
		btnCreateChatroom.setToolTipText("Button for creating a new chatroom");
		pnlGameCmds.add(btnCreateChatroom);
		
		pnlUser = new JPanel();
		pnlUser.setBackground(new Color(152,251,152));
		pnlLeft.add(pnlUser);
		pnlUser.setToolTipText("Panel for user information.");
		pnlUser.setBorder(new TitledBorder(null, "User Info", TitledBorder.CENTER,
						TitledBorder.TOP, null, null));
		pnlUser.setLayout(new GridLayout(4, 1, 0, 0));
		
		lblUsername = new JLabel();
		pnlUser.add(lblUsername);
		lblUsername.setToolTipText("Identifier that will appear in chats");
		lblUsername.setText("Username: edi_sahil");
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblIPAddress = new JLabel();
		pnlUser.add(lblIPAddress);
		lblIPAddress.setToolTipText("Local ip address");
		lblIPAddress.setText("IP: ");
		lblIPAddress.setHorizontalAlignment(SwingConstants.CENTER);
		
		txtUsername = new JTextField();
		pnlUser.add(txtUsername);
		txtUsername.setToolTipText("Text field to enter user name.");
		txtUsername.setText("edi_sahil");
		txtUsername.setColumns(10);
		
		btnUpdateUsername = new JButton("Update Username");
		pnlUser.add(btnUpdateUsername);
		btnUpdateUsername.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newUserName = txtUsername.getText();
				
				if (newUserName.length() > 0) {
					view2ModelAdpt.updateUsername(txtUsername.getText());
					lblUsername.setText("Username: " + txtUsername.getText());
				} else {
					JOptionPane.showMessageDialog(frameGame, "Don't leave your user name blank!");
				}
				
			}
		});
		btnUpdateUsername.setToolTipText("Button to connect to ip addresses");
	
		pnlChatroomTabs = new JPanel();
		pnlChatroomTabs.setToolTipText("Panel for chatrooms to appear");
		pnlChatroomTabs.setBackground(new Color(255, 255, 255));
		frameGame.getContentPane().add(pnlChatroomTabs, BorderLayout.CENTER);
		pnlChatroomTabs.setLayout(new BoxLayout(pnlChatroomTabs, BoxLayout.Y_AXIS));

//		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
//		tabbedPane.setBackground(new Color(240, 248, 255));
//		tabbedPane.setToolTipText("Tabs for chatrooms.");
//		pnlChatroomTabs.add(tabbedPane, BorderLayout.CENTER);
		
//		new JTextPane();
	}
	
//	/**
//	 * displays a pop-out component
//	 * @param component component to display
//	 */
//	public void displayPopOutComponent(Component component) {
//		
//		JFrame popout = new JFrame();
//		popout.getContentPane().add(component);
//		popout.setMinimumSize(new Dimension(300, 300));
//		popout.setVisible(true);
//		
//		popout.pack();
//		popout.setLocationRelativeTo(null);
//		popout.setVisible(true);
//	
//		frameGame.revalidate();
//		frameGame.repaint();
//	}
	
	/**
	 * Starts the view.
	 */
	public void start() {
		
		setLocationRelativeTo(null);
		frameGame.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void setIP(String ip) {
		lblIPAddress.setText("IP: " + ip);
	}
	
	/**
	 * adds a scrolling component to the mini view.
	 * @param c - the component we want to add
	 */
	public void addScrollingComponent(Supplier<Component> c) {
		System.out.println("ADDING COMPONENT " + c.get().getClass());
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				pnlChatroomTabs.add(c.get());
				pnlChatroomTabs.revalidate();
				pnlChatroomTabs.repaint();
			}
			
		});
		
	}
	
	/**
	 * adds a pop up static component.
	 * @param c - the component we want to add
	 */
	public void addStaticComponent(Supplier<Component> c) {
		System.out.println("ADDING COMPONENT ");
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				System.out.println("TRYING TO RUN");
				
				JFrame frame = new JFrame();
				frame.getContentPane().add(c.get());
				frame.pack();
				frame.setVisible(true);
				 
				/*createFrame(c.get());
				pnlChatroomTabs.revalidate();
				pnlChatroomTabs.repaint();*/
			}
			
		});
	}
	
	private void createFrame(Component c) {
		EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFrame frame = new JFrame("StaticMessage");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                try 
                {
                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                   e.printStackTrace();
                }
                JPanel panel = new JPanel();
                panel.add(c);
                frame.getContentPane().add(BorderLayout.CENTER, panel);
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
                frame.setResizable(false);
                c.requestFocus();
            }
        });
	}
}