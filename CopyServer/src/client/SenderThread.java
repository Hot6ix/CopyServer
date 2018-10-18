package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import utils.ThreadListener;

// This thread is using for only TestClient
public class SenderThread extends Thread {

	private Socket mSocket;
	private ThreadListener mListener;

	public SenderThread(Socket socket) {
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
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter writer = new PrintWriter(mSocket.getOutputStream());
			while(true) {
				String msg = reader.readLine();
				writer.println(msg);
				writer.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(mListener != null) mListener.setOnThreadClosed(mSocket.getRemoteSocketAddress().toString());
	}
	
}
