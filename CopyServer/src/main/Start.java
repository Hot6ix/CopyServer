package main;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import data.ButtonId;
import utils.Notificator;

public class Start {

	
	public static void main(String[] args) {
		new ServerHandler().start();
	}
	
	static class ServerHandler implements ActionListener {
		
		private Server mServer;
		
		public ServerHandler() {
			// Handle popup 
			Notificator mNotifier = Notificator.getInstance();
			mNotifier.getmUrl().addActionListener(this);
			mNotifier.getmExit().addActionListener(this);
			mNotifier.getmRestart().addActionListener(this);
			mNotifier.getmSetting().addActionListener(this);
			mNotifier.getmKick().addActionListener(this);
		}
		
		public void start() {
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
