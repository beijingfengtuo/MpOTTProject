package cn.cibnmp.ott.bean;

import java.io.Serializable;

/**
 * Created by wanqi on 2017/3/24.
 */

public class EntryResultBean implements Serializable{

    private int code ;
    private String msg;
    private EntryDataBean data;

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

    public EntryDataBean getData() {
        return data;
    }

    public void setData(EntryDataBean data) {
        this.data = data;
    }
}
