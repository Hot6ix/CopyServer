package main;

public class Start {

	private static Server mServer;
	
	public static void main(String[] args) {
		mServer = new Server();
		mServer.start();
	}

}
