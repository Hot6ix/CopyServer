package data;

public enum Message {
	EMPTY(0), CONNECTED(1), EOC(2), ACK(3), REJ(4), RQ_PW(5);
	
	private int code;
	
	private Message(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
}
