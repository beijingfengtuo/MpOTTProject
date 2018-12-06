package cn.cibnmp.ott.bean;

import java.io.Serializable;

public class DeviceAuthenResultBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int code;
	private String msg;
	private DeviceAuthenDataBean data;


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

	public DeviceAuthenDataBean getData() {
		return data;
	}

	public void setData(DeviceAuthenDataBean data) {
		this.data = data;
	}
}
