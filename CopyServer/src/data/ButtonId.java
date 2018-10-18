package data;

public enum ButtonId {
	BTN_EXIT("BTN_EXIT"),
	BTN_KICK("BTN_KICK"),
	BTN_INFO("BTN_INFO"),
	BTN_SETTING("BTN_SETTING");
	
	private String id;
	
	ButtonId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
}
