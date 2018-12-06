package cn.cibnmp.ott.ui.home;

import android.webkit.JavascriptInterface;

public interface JavaScriptObject {


    /**
     * JS调用登录方法（必须加入@JavascriptInterface注解）
     */
    @JavascriptInterface
    public void onCibnWebviewLogin();

    /**
     * JS调用用户信息方法（必须加入@JavascriptInterface注解）
     * @return
     */
    @JavascriptInterface
    public String onCibnWebviewUser();
}
