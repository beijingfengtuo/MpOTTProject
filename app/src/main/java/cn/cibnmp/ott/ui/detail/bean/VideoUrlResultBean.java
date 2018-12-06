package cn.cibnmp.ott.ui.detail.bean;

/**
 * Created by wanqi on 2017/3/9.
 */

public class VideoUrlResultBean {

    private String code;

    private String msg;

    private VideoUrlInfoBean data;

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
    public void setData(VideoUrlInfoBean data) {
        this.data = data;
    }
    public VideoUrlInfoBean getData() {
        return data;
    }

    public VideoUrlResultBean() {
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
