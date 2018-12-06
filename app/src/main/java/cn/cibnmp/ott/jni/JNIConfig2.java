package cn.cibnmp.ott.jni;

import java.io.Serializable;


public class JNIConfig2 implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String vercode;
    private String cachepath;
    private String sldbpath;
    private long availsize;
    private String chnId;
    private String devId;
    private String projId;
    private String appId;
    private String mac2;
    private String mac;  //有线mac
    private String callback_class_path;
    private String chatmsg_method_name; // 消息回调方法名称


    public JNIConfig2() {
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

    public String getChnId() {
        return chnId;
    }

    public void setChnId(String chnId) {
        this.chnId = chnId;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMac2() {
        return mac2;
    }

    public void setMac2(String mac2) {
        this.mac2 = mac2;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getCallback_class_path() {
        return callback_class_path;
    }

    public void setCallback_class_path(String callback_class_path) {
        this.callback_class_path = callback_class_path;
    }

    public String getChatmsg_method_name() {
        return chatmsg_method_name;
    }

    public void setChatmsg_method_name(String chatmsg_method_name) {
        this.chatmsg_method_name = chatmsg_method_name;
    }

    @Override
    public String toString() {
        return "JNIConfig2{" +
                "vercode='" + vercode + '\'' +
                ", cachepath='" + cachepath + '\'' +
                ", sldbpath='" + sldbpath + '\'' +
                ", availsize=" + availsize +
                ", chnId='" + chnId + '\'' +
                ", devId='" + devId + '\'' +
                ", projId='" + projId + '\'' +
                ", appId='" + appId + '\'' +
                ", mac2='" + mac2 + '\'' +
                ", mac='" + mac + '\'' +
                ", callback_class_path='" + callback_class_path + '\'' +
                ", chatmsg_method_name='" + chatmsg_method_name + '\'' +
                '}';
    }
}
