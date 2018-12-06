package cn.cibnmp.ott.ui.detail.bean;

/**
 * Created by wanqi on 2017/3/9.
 */

public class DetailInfoResultBean {

    private String code;

    private String msg;

    private DetailInfoBean data;

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
    public void setData(DetailInfoBean data){
        this.data = data;
    }
    public DetailInfoBean getData(){
        return this.data;
    }

    public DetailInfoResultBean() {
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
