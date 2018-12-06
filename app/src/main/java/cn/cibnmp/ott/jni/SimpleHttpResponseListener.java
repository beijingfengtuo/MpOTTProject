package cn.cibnmp.ott.jni;


import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.ParameterizedType;

import cn.cibnmp.ott.utils.Lg;

/**
 * Created by wanqi on 2017/5/3.
 * 主要用于EPG接口的统一处理,数据解析，日志上报
 */

public abstract class SimpleHttpResponseListener<T> implements HttpResponseListener2<T> {

    private Class<T> clz;
    private String requestUrl;   //请求url地址

    public SimpleHttpResponseListener() {
        clz = (Class<T>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    public void setRequestUrl(String url) {
        requestUrl = url;
    }

    public void handleResponse(String response) {
        if (TextUtils.isEmpty(response)) {
            onSuccess(null);
            //上报错误日志，返回数据为空
//            Util.reportRequestDataError(requestUrl, "0001", "数据为空");
        } else {
            try {
                onSuccess(JSON.parseObject(response, clz));
            } catch (Exception e) {
                e.printStackTrace();
                onSuccess(null);
                //上报错误日志，返回数据解析失败
//                Util.reportRequestDataError(requestUrl, "0002", "数据异常，解析失败");
                JNIRequest.getInstance().delCacheByUrl(requestUrl, new HttpResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        Lg.i("delCacheByUrl", "delCacheByUrl --> url = " + requestUrl);
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        }
    }

    public void handleError(int error) {
//        if (BaseApplication.is_network_connected) {
//            Util.reportRequestDataError(requestUrl, "0203", "网络连通，访问失败");
//        } else {
//            Util.reportRequestDataError(requestUrl, "0100", "网络未连接");
//        }
        onError(error + "");
    }


}
