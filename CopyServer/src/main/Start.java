package main;

import java.awt.Desktop;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import data.ButtonId;
import utils.Notificator;
import utils.Setting;

public class Start {

	
	public static void main(String[] args) {
		new ServerHandler().start();
	}
	
	static class ServerHandler implements ActionListener {
		
		private Server mServer;
		private Setting mSetting = Setting.getInstance();
		private Notificator mNotifier;
		
		public ServerHandler() {
			// Handle popup 
			mNotifier = Notificator.getInstance();
			mNotifier.getmUrl().addActionListener(this);
			mNotifier.getmExit().addActionListener(this);
			mNotifier.getmRestart().addActionListener(this);
			mNotifier.getmSetting().addActionListener(this);
			mNotifier.getmKick().addActionListener(this);
		}
		
		public void start() {
			mNotifier.printNotification("CopyServer 실행 중", "시스템 트레이에서 확인할 수 있습니다.", MessageType.INFO);
			mServer = new Server();
			mServer.start();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals(ButtonId.BTN_INFO.getId())) {
				// Open github
				try {
					Desktop.getDesktop().browse(new URI("https://github.com/Hot6ix/CopyServer"));
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
			else if(e.getActionCommand().equals(ButtonId.BTN_SETTING.getId())) {
				// Open setting file
				try {
					Desktop.getDesktop().open(new File("./setting.ini"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else if(e.getActionCommand().equals(ButtonId.BTN_RESTART.getId())) {
				// Restart server
				System.out.println("Restarting server...");
				mServer.finish();
				mSetting.reloadProp();
				mServer = new Server();
				mServer.start();
			}
			else if(e.getActionCommand().equals(ButtonId.BTN_KICK.getId())) {
				// Kick current connection
				mServer.kick();
			}
			else if(e.getActionCommand().equals(ButtonId.BTN_EXIT.getId())) {
				// Finish
				System.out.println("Finishing server...");
				mServer.finish();
				System.exit(0);
			}
		}
	}

}
