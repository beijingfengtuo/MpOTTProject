package cn.cibnmp.ott.bean;

import java.io.Serializable;

/**
 * Created by cibn-lyc on 2018/1/9.
 */

public class HostUpgradeDataBean implements Serializable{
    public static final int force_upgrade_type = 1;  //强制升级
    public static final int optional_upgrade_type = 0; //非强制，可选择的升级

    private long verno;
    private String intro;
    private String vername;
    private int isforce;
    private String url;
    private String fid;

    public HostUpgradeDataBean() {
    }

    public long getVerno() {
        return verno;
    }

    public void setVerno(long verno) {
        this.verno = verno;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getVername() {
        return vername;
    }

    public void setVername(String vername) {
        this.vername = vername;
    }

    public int getIsforce() {
        return isforce;
    }

    public void setIsforce(int isforce) {
        this.isforce = isforce;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
}
