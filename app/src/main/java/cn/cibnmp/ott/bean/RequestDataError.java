package cn.cibnmp.ott.bean;

import java.io.Serializable;

/**
 * Created by wanqi on 2017/5/3.
 */

public class RequestDataError implements Serializable{
    private String url;
    private String errorcode;
    private String errordesc;

    public RequestDataError(String url, String errorcode, String errordesc) {
        this.url = url;
        this.errorcode = errorcode;
        this.errordesc = errordesc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrordesc() {
        return errordesc;
    }

    public void setErrordesc(String errordesc) {
        this.errordesc = errordesc;
    }
}
