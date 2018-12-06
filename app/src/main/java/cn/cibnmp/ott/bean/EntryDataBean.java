package cn.cibnmp.ott.bean;

import java.io.Serializable;

/**
 * Created by wanqi on 2017/3/24.
 */

public class EntryDataBean implements Serializable {


    /**
     * Epgurl : http://114.247.94.86:8010
     * javaEpgurl : http://appstore.api.cibn.cc/
     * Logurl : http://114.247.94.204:8087/report
     * WxPayUrl : http://dajuyuan.wx.cibn.cc/pay
     * WxBind : http://114.247.94.93:8030/Login/Index/index
     * msBindUrl : http://gaoqingtest.wx.cibn.cc/Control/tvbind
     * ShareDetail : http://haokan.wx.cibn.cc/detail
     * CarouselUrl : http://lbint.live.cibn.cc:8045/live/api/
     * Cdn : http://42.62.117.38:80/
     * Msg : 114.247.94.58:8087
     * Cdnpic : http://114.247.94.87:8080/
     * Udp : 114.247.94.204:5485
     * Areaurl : http://114.247.94.58:8188/areainfo
     * Bmsurl : http://weixtest2pay.hdv.ott.cibntv.net/cibn3api_02
     * CpCode : YOUKU
     * detailWebViewUrl : http://114.247.94.93:8216
     * navMobileEpg : 1034public final static String CHANNEL_ID = "channelid";
     * epgid : 1031
     */

    private String Epgurl;
    private String javaEpgurl;
    private String Logurl;
    private String WxPayUrl;
    private String WxBind;
    private String msBindUrl;
    private String ShareDetail;
    private String CarouselUrl;
    private String Cdn;
    private String Msg;
    private String Cdnpic;
    private String Udp;
    private String Areaurl;
    private String Bmsurl;
    private String CpCode;
    private String detailWebViewUrl;
    private String navMobileEpg;

    public String getNavMobileEpg2() {
        return navMobileEpg2;
    }

    public void setNavMobileEpg2(String navMobileEpg2) {
        this.navMobileEpg2 = navMobileEpg2;
    }

    private String navMobileEpg2;
    private int epgid;

    @Override
    public String toString() {
        return "EntryDataBean{" +
                "Epgurl='" + Epgurl + '\'' +
                ", javaEpgurl='" + javaEpgurl + '\'' +
                ", Logurl='" + Logurl + '\'' +
                ", WxPayUrl='" + WxPayUrl + '\'' +
                ", WxBind='" + WxBind + '\'' +
                ", msBindUrl='" + msBindUrl + '\'' +
                ", ShareDetail='" + ShareDetail + '\'' +
                ", CarouselUrl='" + CarouselUrl + '\'' +
                ", Cdn='" + Cdn + '\'' +
                ", Msg='" + Msg + '\'' +
                ", Cdnpic='" + Cdnpic + '\'' +
                ", Udp='" + Udp + '\'' +
                ", Areaurl='" + Areaurl + '\'' +
                ", Bmsurl='" + Bmsurl + '\'' +
                ", CpCode='" + CpCode + '\'' +
                ", detailWebViewUrl='" + detailWebViewUrl + '\'' +
                ", navMobileEpg='" + navMobileEpg + '\'' +
                ", epgid=" + epgid +
                '}';
    }

    public String getEpgurl() {
        return Epgurl;
    }

    public void setEpgurl(String Epgurl) {
        this.Epgurl = Epgurl;
    }

    public String getJavaEpgurl() {
        return javaEpgurl;
    }

    public void setJavaEpgurl(String javaEpgurl) {
        this.javaEpgurl = javaEpgurl;
    }

    public String getLogurl() {
        return Logurl;
    }

    public void setLogurl(String Logurl) {
        this.Logurl = Logurl;
    }

    public String getWxPayUrl() {
        return WxPayUrl;
    }

    public void setWxPayUrl(String WxPayUrl) {
        this.WxPayUrl = WxPayUrl;
    }

    public String getWxBind() {
        return WxBind;
    }

    public void setWxBind(String WxBind) {
        this.WxBind = WxBind;
    }

    public String getMsBindUrl() {
        return msBindUrl;
    }

    public void setMsBindUrl(String msBindUrl) {
        this.msBindUrl = msBindUrl;
    }

    public String getShareDetail() {
        return ShareDetail;
    }

    public void setShareDetail(String ShareDetail) {
        this.ShareDetail = ShareDetail;
    }

    public String getCarouselUrl() {
        return CarouselUrl;
    }

    public void setCarouselUrl(String CarouselUrl) {
        this.CarouselUrl = CarouselUrl;
    }

    public String getCdn() {
        return Cdn;
    }

    public void setCdn(String Cdn) {
        this.Cdn = Cdn;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public String getCdnpic() {
        return Cdnpic;
    }

    public void setCdnpic(String Cdnpic) {
        this.Cdnpic = Cdnpic;
    }

    public String getUdp() {
        return Udp;
    }

    public void setUdp(String Udp) {
        this.Udp = Udp;
    }

    public String getAreaurl() {
        return Areaurl;
    }

    public void setAreaurl(String Areaurl) {
        this.Areaurl = Areaurl;
    }

    public String getBmsurl() {
        return Bmsurl;
    }

    public void setBmsurl(String Bmsurl) {
        this.Bmsurl = Bmsurl;
    }

    public String getCpCode() {
        return CpCode;
    }

    public void setCpCode(String CpCode) {
        this.CpCode = CpCode;
    }

    public String getDetailWebViewUrl() {
        return detailWebViewUrl;
    }

    public void setDetailWebViewUrl(String detailWebViewUrl) {
        this.detailWebViewUrl = detailWebViewUrl;
    }

    public String getNavMobileEpg() {
        return navMobileEpg;
    }

    public void setNavMobileEpg(String navMobileEpg) {
        this.navMobileEpg = navMobileEpg;
    }

    public String getEpgid() {
        return String.valueOf(epgid);
    }

    public void setEpgid(int epgid) {
        this.epgid = epgid;
    }
}
