package utils;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import data.Message;

public class MessageSender {
	
	private Socket mSocket;
	
	private MessageSender() {}
	
	private static class Singleton {
		private static final MessageSender instance = new MessageSender();
	}
	
	public static MessageSender getInstance() {
		return Singleton.instance;
	}
	
	public Socket getmSocket() {
		return mSocket;
	}


	public void setmSocket(Socket mSocket) {
		this.mSocket = mSocket;
	}


	public void sendMessage(Message msg) {
		try {
			PrintWriter writer = new PrintWriter(mSocket.getOutputStream());
			writer.println(msg);
			writer.checkError();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
