package cn.cibnmp.ott.bean;

import java.io.Serializable;

/** 
 * @description 
 * @author xhn
 * @date 2016年1月21日
 */
public class UserBean implements Serializable{

	/**
	 * msgtype : 2004
	 * sessid : 711acf7d08dbeeac21f061fb09d01075
	 * uid : 50031
	 * mobile : 13146008521
	 * nickname :
	 * portraiturl :
	 * portraitfid :
	 * isvip : 0
	 * vipstarttime : 0
	 * vipendtime : 0
	 */

	private String msgtype;
	private String sessid;
	private int uid;
	private String mobile;
	private String nickname;
	private String portraiturl;
	private String portraitfid;
	private int isvip;
	private int vipstarttime;
	private int vipendtime;

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getSessid() {
		return sessid;
	}

	public void setSessid(String sessid) {
		this.sessid = sessid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPortraiturl() {
		return portraiturl;
	}

	public void setPortraiturl(String portraiturl) {
		this.portraiturl = portraiturl;
	}

	public String getPortraitfid() {
		return portraitfid;
	}

	public void setPortraitfid(String portraitfid) {
		this.portraitfid = portraitfid;
	}

	public int getIsvip() {
		return isvip;
	}

	public void setIsvip(int isvip) {
		this.isvip = isvip;
	}

	public int getVipstarttime() {
		return vipstarttime;
	}

	public void setVipstarttime(int vipstarttime) {
		this.vipstarttime = vipstarttime;
	}

	public int getVipendtime() {
		return vipendtime;
	}

	public void setVipendtime(int vipendtime) {
		this.vipendtime = vipendtime;
	}
}
