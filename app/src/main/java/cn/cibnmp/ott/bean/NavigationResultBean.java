package cn.cibnmp.ott.bean;

/**
 * Created by wanqi on 2017/3/9.
 * 首页导航，导航列表等bean类
 */

public class NavigationResultBean {

    private String code;

    private String msg;

    private NavigationItemDataBean data ;

    public NavigationResultBean() {
    }

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

    public NavigationItemDataBean getData() {
        return data;
    }

    public void setData(NavigationItemDataBean data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NavigationResultBean that = (NavigationResultBean) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (msg != null ? !msg.equals(that.msg) : that.msg != null) return false;
        return data != null ? data.equals(that.data) : that.data == null;

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (msg != null ? msg.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "NavigationResultBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
