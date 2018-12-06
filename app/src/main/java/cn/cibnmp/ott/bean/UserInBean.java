package cn.cibnmp.ott.bean;

import java.io.Serializable;

public class UserInBean implements Serializable {

	private static final long serialVersionUID = 3366L;

	private int code;
	private String msg;
	private UserBean data;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public UserBean getData() {
		return data;
	}

	public void setData(UserBean data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "UserInBean{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", userBean=" + data +
				'}';
	}
}