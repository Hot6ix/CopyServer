package main;
import java.awt.Desktop;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import data.ButtonId;
import data.Message;
import utils.MessageSender;
import utils.Notificator;
import utils.ReceiverThread;
import utils.Setting;
import utils.ThreadListener;

public class Server implements ActionListener {
	
	public static final int PORT = 31331;
	
	private ServerSocket mServer;
	private Notificator mNotifier;
	private Properties mProp;
	private Setting mSetting = Setting.getInstance();
	private MessageSender mSender = MessageSender.getInstance();
	
	private Socket mConnected;
	private ReceiverThread mReceiver;
	
	public static boolean isPasswordMode = false;
	
	public Server() {
		mNotifier = Notificator.getInstance();
		mNotifier.getmUrl().addActionListener(this);
		mNotifier.getmExit().addActionListener(this);
		mNotifier.getmSetting().addActionListener(this);
		mNotifier.getmKick().addActionListener(this);
		mProp = mSetting.getProp();
	}
	
	public void start()	{
		try {
			int port = PORT;
			try {
				port = Integer.valueOf(mProp.getProperty("port"));
			} catch (NumberFormatException e) {
				System.out.println(String.format("Unavailable to get port. Use default port : %s", port));
			}
			
			mServer = new ServerSocket(port);
			System.out.println("Initiate socket server...");
			System.out.println("=========================");
			mProp.list(System.out);
			System.out.println("=========================");
			
			// Only a connection allowed
			while(true) {
				Socket mSocket = mServer.accept();
				
				if(mConnected != null) {
					// Reject other connections
					mNotifier.printNotification("Connection refused!", mSocket.getRemoteSocketAddress().toString(), TrayIcon.MessageType.INFO);
					
					// Send end of connection
					mSender.setmSocket(mSocket);
					mSender.sendMessage(Message.EOC);
					
					mSocket.close();
				}
				else {
					// Allow first connection
					mConnected = mSocket;
					mNotifier.printNotification("New device connected!", mSocket.getRemoteSocketAddress().toString(), TrayIcon.MessageType.INFO);
					
					// Send connected
					mSender.setmSocket(mConnected);
					mSender.sendMessage(Message.CONNECTED);

					// Request password if set or not
					String password = mProp.getProperty("password");
					if(password != null && !password.isEmpty()) {
						mSender.sendMessage(Message.RQ_PW);
						isPasswordMode = true;
					}
					
					// Ready to get message
					mReceiver = new ReceiverThread(mConnected);
					mReceiver.setListener(new ThreadListener() {
						
						@Override
						public void setOnThreadStart(String ip) {
							System.out.println(String.format("Receiver thread started : %s", ip));
						}
						
						@Override
						public void setOnThreadClosed(String ip) {
							mConnected = null;
							System.out.println(String.format("Receiver thread finished : %s", ip));
						}
					});
					mReceiver.start();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Close server
	public void finish() {
		try {
			if(mConnected != null) {
				mConnected.close();
			}
			
			mServer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Print connected every 30 sec
	private void printConnection() {
		new Thread(() -> {
			while(true) {
				try {
					if(mConnected != null) {
						System.out.println(String.format("%s is connected : %b", mConnected.getRemoteSocketAddress().toString(), !mConnected.isClosed()));
						mSender.sendMessage(Message.CONNECTED);
					}
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	// Event handler for sysetm tray
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(ButtonId.BTN_INFO.getId())) {
			try {
				Desktop.getDesktop().browse(new URI("https://github.com/Hot6ix/CopyServer"));
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getActionCommand().equals(ButtonId.BTN_EXIT.getId())) {
			System.out.println("Finishing server...");
			finish();
			System.exit(0);
		}
		else if(e.getActionCommand().equals(ButtonId.BTN_SETTING.getId())) {
			try {
				Desktop.getDesktop().open(new File("./setting.ini"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getActionCommand().equals(ButtonId.BTN_KICK.getId())) {
			if(mConnected != null) {
				System.out.println("Disconnecting...");
				try {
					mConnected.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else {
				System.out.println("Nothing connected.");
			}
		}
	}
	
}
