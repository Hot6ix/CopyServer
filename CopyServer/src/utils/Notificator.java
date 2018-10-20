package utils;
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.net.MalformedURLException;
import java.net.URL;

import data.ButtonId;

public class Notificator {

	private PopupMenu mPopup;
	private SystemTray mTray;
	private MenuItem mExit;
	private MenuItem mUrl;
	private MenuItem mSetting;
	private MenuItem mRestart;
	private MenuItem mKick;
	private TrayIcon mIcon;
	
	private Notificator() {
		mTray = SystemTray.getSystemTray();
		mPopup = new PopupMenu();

		mIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("resources/bmo.png"));
		mIcon.setImageAutoSize(true);
		
		mUrl = new MenuItem("정보");
		mUrl.setActionCommand(ButtonId.BTN_INFO.getId());
		mSetting = new MenuItem("설정 파일 열기");
		mSetting.setActionCommand(ButtonId.BTN_SETTING.getId());
		mRestart = new MenuItem("서버 재시작");
		mRestart.setActionCommand(ButtonId.BTN_RESTART.getId());
		mKick = new MenuItem("연결 끊기");
		mKick.setActionCommand(ButtonId.BTN_KICK.getId());
		mExit = new MenuItem("종료");
		mExit.setActionCommand(ButtonId.BTN_EXIT.getId());

		mPopup.add(mUrl);
		mPopup.addSeparator();
		mPopup.add(mSetting);
		mPopup.add(mRestart);
		mPopup.add(mKick);
		mPopup.addSeparator();
		mPopup.add(mExit);
		mIcon.setPopupMenu(mPopup);
		
		try {
			mTray.add(mIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	private static class Singleton {
		private static final Notificator instance = new Notificator();
	}
	
	public static Notificator getInstance() {
		return Singleton.instance;
	}

	public MenuItem getmExit() {
		return mExit;
	}

	public MenuItem getmSetting() {
		return mSetting;
	}

	public MenuItem getmKick() {
		return mKick;
	}

	public MenuItem getmUrl() {
		return mUrl;
	}

	public MenuItem getmRestart() {
		return mRestart;
	}

	public void printNotification(String title, String message, MessageType type) {
		mIcon.displayMessage(title, message, type);
	}
	
}
