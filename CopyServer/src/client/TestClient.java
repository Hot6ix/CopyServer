package client;

import java.io.IOException;
import java.net.Socket;

import main.Server;
import utils.ThreadListener;

public class TestClient {

	private static Socket mSocket;
	
	public static void main(String[] args) {

		try {
			mSocket = new Socket("192.168.1.23", Server.PORT);
			
			SenderThread st = new SenderThread(mSocket);
			st.setListener(new ThreadListener() {
				
				@Override
				public void setOnThreadStart(String ip) {
					System.out.println(String.format("Sender thread started : %s", ip));
				}
				
				@Override
				public void setOnThreadClosed(String ip) {
					System.out.println(String.format("Sender thread finished : %s", ip));
				}
			});
			st.start();
			
			ReceiverThread rt = new ReceiverThread(mSocket);
			rt.setListener(new ThreadListener() {
				
				@Override
				public void setOnThreadStart(String ip) {
					System.out.println(String.format("Receiver thread started : %s", ip));
				}
				
				@Override
				public void setOnThreadClosed(String ip) {
					System.out.println(String.format("Receiver thread finished : %s", ip));
					System.exit(0);
				}
			});
			rt.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
