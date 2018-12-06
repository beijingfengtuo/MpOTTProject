package cn.cibnmp.ott.bean;

import java.io.Serializable;

/**
 * Created by wanqi on 2017/3/24.
 */

public class DeviceAuthenDataBean implements Serializable {



    private String sessid ;
    private String tid;
    private long firstRegChnid;
    private String firstRegTime;
    private long servTime;
    private String disableUrl;

    public String getSessid() {
        return sessid;
    }

    public void setSessid(String sessid) {
        this.sessid = sessid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public long getFirstRegChnid() {
        return firstRegChnid;
    }

    public void setFirstRegChnid(long firstRegChnid) {
        this.firstRegChnid = firstRegChnid;
    }

    public String getFirstRegTime() {
        return firstRegTime;
    }

    public void setFirstRegTime(String firstRegTime) {
        this.firstRegTime = firstRegTime;
    }

    public long getServTime() {
        return servTime;
    }

    public void setServTime(long servTime) {
        this.servTime = servTime;
    }

    public String getDisableUrl() {
        return disableUrl;
    }

    public void setDisableUrl(String disableUrl) {
        this.disableUrl = disableUrl;
    }
}
