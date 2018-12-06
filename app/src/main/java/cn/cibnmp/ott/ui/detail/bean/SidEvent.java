package cn.cibnmp.ott.ui.detail.bean;

import java.io.Serializable;

/**
 * Created by axl on 2018/1/22.
 */

public class SidEvent implements Serializable {
    private int sid;

    private int msgType;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }
}
