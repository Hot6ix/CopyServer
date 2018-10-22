package utils;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.Message;
import main.Server;

public class ReceiverThread extends Thread {
	
	private Socket mSocket;
	private ThreadListener mListener;
	private Clipboard mClipper;
	private Properties prop;
	private MessageSender mSender;
	private Notificator mNotifier = Notificator.getInstance();

	public ReceiverThread(Socket socket) {
		this.mSocket = socket;
		this.mClipper = Toolkit.getDefaultToolkit().getSystemClipboard();
		this.prop = Setting.getInstance().getProp();
		this.mSender = MessageSender.getInstance();
	}
	
	public void setListener(ThreadListener listener) {
		this.mListener = listener;
	}

	@Override
	public void run() {
		super.run();
		
		if(mListener != null) mListener.setOnThreadStart(mSocket.getRemoteSocketAddress().toString());
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "UTF-8"));
			String msg;
			while((msg = reader.readLine()) != null) {
				System.out.println(String.format("Message from %s : %s", mSocket.getRemoteSocketAddress().toString(), msg));

				if(Server.isPasswordMode) {
					// Read next message to verify password
					if(msg.equals(prop.getProperty("password"))) {
						Server.isPasswordMode = false;
						mSender.setmSocket(mSocket);
						mSender.sendMessage(Message.ACK);
						System.out.println("Authentication success!");
						mNotifier.printNotification("Connected", mSocket.getRemoteSocketAddress().toString(), TrayIcon.MessageType.INFO);
					}
					else {
						mSender.setmSocket(mSocket);
						mSender.sendMessage(Message.REJ);
						System.out.println("Authentication failed!");
						break;
					}
				}
				else {
					boolean allowCharacter = Boolean.valueOf(prop.getProperty("allowCharacter"));
					if(allowCharacter) {
						mClipper.setContents(new StringSelection(msg), null);
						Notificator.getInstance().printNotification("메세지 복사됨", String.format("Ctrl + V를 눌러 붙여넣기 : %s", msg), MessageType.INFO);
					}
					else {
						Pattern pattern = Pattern.compile("\\d+");
						Matcher matcher = pattern.matcher(msg);
						if(matcher.matches()) {
							mClipper.setContents(new StringSelection(msg), null);
							Notificator.getInstance().printNotification("메세지 복사됨", String.format("Ctrl + V를 눌러 붙여넣기 : %s", msg), MessageType.INFO);
						}
						else {
							System.out.println(String.format("Message must not include character : %s", msg));
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(mListener != null) mListener.setOnThreadClosed(mSocket.getRemoteSocketAddress().toString());
	}
	
}
