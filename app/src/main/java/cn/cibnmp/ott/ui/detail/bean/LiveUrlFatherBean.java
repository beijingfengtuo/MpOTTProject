package cn.cibnmp.ott.ui.detail.bean;

import java.io.Serializable;

/**
 * Created by yanjing on 2017/5/8.
 */

public class LiveUrlFatherBean implements Serializable{

    private String code;
    private String msg;
    private LiveUrlBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LiveUrlBean getData() {
        return data;
    }

    public void setData(LiveUrlBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LiveUrlFatherBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
