package cn.cibnmp.ott.jni;

import java.io.Serializable;


public class JNIConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String vercode;
	private String cachepath;
	private String sldbpath;
	private long availsize;
	private String devId;
	private String channel;
	private String saleId;
	private String wifiMac;
	private String lanMac;
	private String seriesnumber;
	private String cupInfo;
	private String devicename;
	private String srv_domain;
	private String srv_port;
	private String callback_class_path;

	/*------------回调方法名--------------*/
	private String netstat_method_name;
	private String get_sdcard_method_name;
	private String chatmsg_method_name; // 消息回调方法名称
	private String fileput_method_name;
	private String fileget_method_name;
	private String version_method_name;
	private String passwd_cb_method;

	/*------------回调方法名--------------*/

	public JNIConfig() {
//		setVercode(String.valueOf(AppInfo.getVersionName()));
//		setCachepath(Constant.JNICACHEPATH);
//		setSldbpath(Constant.DATABASEPATH);
//		setAvailsize(ApolloUtils.getSDFreeSize());
//		setDevId(DeviceUtils.getLanMac() + DeviceUtils.getWlanMac()); // hid
//		setChannel(App.oemid); // 渠道id
//		setSaleId(App.saleId);
//		setWifiMac(DeviceUtils.getWlanMac());
//		setLanMac(DeviceUtils.getLanMac());
//		setSeriesnumber(DeviceUtils.getSerialNumber());
//		setCupInfo(DeviceUtils.getCpuName());
//		setDevicename(DeviceUtils.getDeviceName());
//		setSrv_domain(ConfigProperty.getServerDomain());
//		setSrv_port(ConfigProperty.getServerPort());
//		setCallback_class_path(Constant.JNICLASSPATH);
//		setNetstat_method_name(JNIRequest.CALLBACKMETHODNAME);
//		setGet_sdcard_method_name(JNIRequest.CALLBACKMETHODNAME);
//		setChatmsg_method_name(JNIRequest.MESSAGECALLBACK);
//		setFileget_method_name(JNIRequest.CALLBACKMETHODNAME);
//		setFileput_method_name(JNIRequest.CALLBACKMETHODNAME);
//		setVersion_method_name(JNIRequest.CALLBACKMETHODNAME);
//		setPasswd_cb_method(JNIRequest.CALLBACKMETHODNAME);
	}

	public String getVercode() {
		return vercode;
	}

	public void setVercode(String vercode) {
		this.vercode = vercode;
	}

	public String getCachepath() {
		return cachepath;
	}

	public void setCachepath(String cachepath) {
		this.cachepath = cachepath;
	}

	public String getSldbpath() {
		return sldbpath;
	}

	public void setSldbpath(String sldbpath) {
		this.sldbpath = sldbpath;
	}

	public long getAvailsize() {
		return availsize;
	}

	public void setAvailsize(long availsize) {
		this.availsize = availsize;
	}

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getWifiMac() {
		return wifiMac;
	}

	public void setWifiMac(String wifiMac) {
		this.wifiMac = wifiMac;
	}

	public String getLanMac() {
		return lanMac;
	}

	public void setLanMac(String lanMac) {
		this.lanMac = lanMac;
	}

	public String getSeriesnumber() {
		return seriesnumber;
	}

	public void setSeriesnumber(String seriesnumber) {
		this.seriesnumber = seriesnumber;
	}

	public String getCupInfo() {
		return cupInfo;
	}

	public void setCupInfo(String cupInfo) {
		this.cupInfo = cupInfo;
	}

	public String getDevicename() {
		return devicename;
	}

	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}

	public String getSrv_domain() {
		return srv_domain;
	}

	public void setSrv_domain(String srv_domain) {
		this.srv_domain = srv_domain;
	}

	public String getSrv_port() {
		return srv_port;
	}

	public void setSrv_port(String srv_port) {
		this.srv_port = srv_port;
	}

	public String getCallback_class_path() {
		return callback_class_path;
	}

	public void setCallback_class_path(String callback_class_path) {
		this.callback_class_path = callback_class_path;
	}

	public String getNetstat_method_name() {
		return netstat_method_name;
	}

	public void setNetstat_method_name(String netstat_method_name) {
		this.netstat_method_name = netstat_method_name;
	}

	public String getGet_sdcard_method_name() {
		return get_sdcard_method_name;
	}

	public void setGet_sdcard_method_name(String get_sdcard_method_name) {
		this.get_sdcard_method_name = get_sdcard_method_name;
	}

	public String getChatmsg_method_name() {
		return chatmsg_method_name;
	}

	public void setChatmsg_method_name(String chatmsg_method_name) {
		this.chatmsg_method_name = chatmsg_method_name;
	}

	public String getFileput_method_name() {
		return fileput_method_name;
	}

	public void setFileput_method_name(String fileput_method_name) {
		this.fileput_method_name = fileput_method_name;
	}

	public String getFileget_method_name() {
		return fileget_method_name;
	}

	public void setFileget_method_name(String fileget_method_name) {
		this.fileget_method_name = fileget_method_name;
	}

	public String getVersion_method_name() {
		return version_method_name;
	}

	public void setVersion_method_name(String version_method_name) {
		this.version_method_name = version_method_name;
	}

	public String getPasswd_cb_method() {
		return passwd_cb_method;
	}

	public void setPasswd_cb_method(String passwd_cb_method) {
		this.passwd_cb_method = passwd_cb_method;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSaleId() {
		return saleId;
	}

	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}

}
