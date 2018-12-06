package cn.cibnmp.ott.utils;

/**
 * Created by wanqi on 2017/4/24.
 */

public class ErrorModel {

    public String errorCode;
    public String handleCode;
    public ErrorModel errorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public ErrorModel handleCode(String handleCode) {
        this.handleCode = handleCode;
        return this;
    }

}
