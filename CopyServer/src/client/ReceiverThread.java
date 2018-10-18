package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import data.Message;
import utils.ThreadListener;

public class ReceiverThread extends Thread {
	
	private Socket mSocket;
	private ThreadListener mListener;
	private boolean isRunning = true;

	public ReceiverThread(Socket socket) {
		this.mSocket = socket;
	}
	
	public void setListener(ThreadListener listener) {
		this.mListener = listener;
	}

	@Override
	public void run() {
		super.run();
		
		if(mListener != null) mListener.setOnThreadStart(mSocket.getRemoteSocketAddress().toString());
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
			
			while(isRunning) {
				String msg = reader.readLine();
				System.out.println(String.format("Message from %s : %s", mSocket.getRemoteSocketAddress().toString(), msg));
				if(msg == null || msg.equals(Message.EOC.toString())) break;
				if(msg.equals(Message.RQ_PW.toString())) {
					System.out.println("Need password to auth.");
				}
				else if(msg.equals(Message.ACK.toString())) {
					System.out.println("Auth finished.");
				}
				else if(msg.equals(Message.REJ.toString())) {
					System.out.println("Incorrect password.");
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(mListener != null) mListener.setOnThreadClosed(mSocket.getRemoteSocketAddress().toString());
	}
	
	public void finish(boolean isRunning) {
		isRunning = false;
	}
	
}
