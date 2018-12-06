package cn.cibnmp.ott.bean;

import java.io.Serializable;

/**
 * Created by wanqi on 2017/3/25.
 */

public class AboutUsDataBean implements Serializable {

    private String companyName;
    private String companyEmail;  //公司邮箱
    private String serviceTel;   //客服电话
    private String qrCodeFid;   //关于我们二维码图片
    private String feedbackQrCodeFid;   //问题返回二维码图片
    private String softDecodeDevice;  //需要使用软解的设备
    private String qqCodeFid;//问题反馈的qq二维码图片
    private String companyQQ;//问题反馈的qq群号
    private String webkitErrorDevices;//详情页webview字段
    private String cdnHostName; // cdn对应域名编号
    private String advertVid;//定投广告的字段
    private String advertMediatype;//栏目广告的字段

    public String getAdvertMediatype() {
        return advertMediatype;
    }

    public void setAdvertMediatype(String advertMediatype) {
        this.advertMediatype = advertMediatype;
    }

    public String getAdvertVid() {
        return advertVid;
    }

    public void setAdvertVid(String advertVid) {
        this.advertVid = advertVid;
    }

    public String getWebkitErrorDevices() {
        return webkitErrorDevices;
    }

    public void setWebkitErrorDevices(String webkitErrorDevices) {
        this.webkitErrorDevices = webkitErrorDevices;
    }

    public String getCompanyQQ() {
        return companyQQ;
    }

    public void setCompanyQQ(String companyQQ) {
        this.companyQQ = companyQQ;
    }

    public String getQqCodeFid() {
        return qqCodeFid;
    }

    public void setQqCodeFid(String qqCodeFid) {
        this.qqCodeFid = qqCodeFid;
    }

    public AboutUsDataBean() {
    }

    public String getFeedbackQrCodeFid() {
        return feedbackQrCodeFid;
    }

    public void setFeedbackQrCodeFid(String feedbackQrCodeFid) {
        this.feedbackQrCodeFid = feedbackQrCodeFid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getServiceTel() {
        return serviceTel;
    }

    public void setServiceTel(String serviceTel) {
        this.serviceTel = serviceTel;
    }

    public String getQrCodeFid() {
        return qrCodeFid;
    }

    public void setQrCodeFid(String qrCodeFid) {
        this.qrCodeFid = qrCodeFid;
    }

    public String getSoftDecodeDevice() {
        return softDecodeDevice;
    }

    public void setSoftDecodeDevice(String softDecodeDevice) {
        this.softDecodeDevice = softDecodeDevice;
    }

    public String getCdnHostName() {
        return cdnHostName;
    }

    public void setCdnHostName(String cdnHostName) {
        this.cdnHostName = cdnHostName;
    }
}
