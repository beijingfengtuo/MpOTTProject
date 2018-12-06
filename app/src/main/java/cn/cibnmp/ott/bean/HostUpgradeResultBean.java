package cn.cibnmp.ott.bean;

import java.io.Serializable;

/**
 * Created by wanqi on 2017/3/27.
 */

public class HostUpgradeResultBean implements Serializable{

    private int code;
    private String msg;
    private HostUpgradeDataBean data;

    public HostUpgradeResultBean() {
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

    public HostUpgradeDataBean getData() {
        return data;
    }

    public void setData(HostUpgradeDataBean data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HostUpgradeResultBean that = (HostUpgradeResultBean) o;

        return data != null ? data.equals(that.data) : that.data == null;

    }

    @Override
    public int hashCode() {
        return data != null ? data.hashCode() : 0;
    }
}
