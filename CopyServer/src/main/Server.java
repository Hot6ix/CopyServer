package main;
import java.awt.TrayIcon;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import data.Message;
import utils.MessageSender;
import utils.Notificator;
import utils.ReceiverThread;
import utils.Setting;
import utils.ThreadListener;

public class Server extends Thread {
	
	public static final int PORT = 31331;
	
	private ServerSocket mServer;
	private Properties mProp;
	private Setting mSetting = Setting.getInstance();
	private MessageSender mSender = MessageSender.getInstance();
	private Notificator mNotifier = Notificator.getInstance();
	
	private Socket mConnected;
	private ReceiverThread mReceiver;
	
	public static boolean isPasswordMode = false;
	
	@Override
	public void run() {
		int port = PORT;
		try {
			mProp = mSetting.getProp();
			port = Integer.valueOf(mProp.getProperty("port"));
			
			mServer = new ServerSocket(port);
			System.out.println("Initiate socket server...");
			System.out.println("=========================");
			mProp.list(System.out);
			System.out.println("=========================");
			
			// Only a connection allowed
			while(true) {
				Socket mSocket = mServer.accept();
				
				if(mConnected != null) {
					mNotifier.printNotification("연결 거부", mSocket.getRemoteSocketAddress().toString(), TrayIcon.MessageType.INFO);
					
					mSender.setmSocket(mSocket);
					mSender.sendMessage(Message.EOC);
					
					mSocket.close();
				}
				else {
					mConnected = mSocket;
					
					mSender.setmSocket(mConnected);
					mSender.sendMessage(Message.CONNECTED);

					String password = mProp.getProperty("password");
					if(password != null && !password.isEmpty()) {
						mSender.sendMessage(Message.RQ_PW);
						isPasswordMode = true;
					}
					else {
						mNotifier.printNotification("연결됨", mSocket.getRemoteSocketAddress().toString(), TrayIcon.MessageType.INFO);
					}
					
					mReceiver = new ReceiverThread(mConnected);
					mReceiver.setListener(new ThreadListener() {
						
						@Override
						public void setOnThreadStart(String ip) {
							System.out.println("Receiver thread started");
						}
						
						@Override
						public void setOnThreadClosed(String ip) {
							if(mConnected != null && mConnected.isConnected()) {
								try {
									mConnected.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
								mConnected = null;
							}
							System.out.println("Receiver thread finished");
						}
					});
					mReceiver.start();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println(String.format("Unavailable to get port. Use default port : %s", port));
		}
	}
	
	public void finish() {
		try {
			if(mConnected != null && mConnected.isConnected()) {
				mSender.sendMessage(Message.EOC);
				mConnected.close();
			}
			
			mServer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void kick() {
		if(mConnected != null && mConnected.isConnected()) {
			System.out.println("Disconnecting...");
			mNotifier.printNotification("연결 종료", mConnected.getRemoteSocketAddress().toString(), TrayIcon.MessageType.INFO);
			try {
				mSender.sendMessage(Message.EOC);
				mConnected.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			mConnected = null;
		}
		else {
			System.out.println("Nothing connected.");
		}
	}
	
}
