package cn.cibnmp.ott.bean;

import java.io.Serializable;

/**
 * Created by wanqi on 2017/3/9.
 */

public class NavigationInfoResultBean implements Serializable{

    private String code;

    private String msg;

    private NavigationInfoBean data;

    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setData(NavigationInfoBean data){
        this.data = data;
    }
    public NavigationInfoBean getData(){
        return this.data;
    }

    public NavigationInfoResultBean() {
    }

    @Override
    public String toString() {
        return "NavigationInfoResultBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
