package cn.cibnmp.ott.bean;

public class UserStateEvent {
	private boolean isLogined;

	public UserStateEvent(boolean isLogined) {
		this.isLogined = isLogined;
	}

	public boolean isLogined() {
		return isLogined;
	}

	public void setLogined(boolean isLogined) {
		this.isLogined = isLogined;
	}
}
