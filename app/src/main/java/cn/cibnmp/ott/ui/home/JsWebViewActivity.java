package cn.cibnmp.ott.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ta.utdid2.android.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.ui.user.login.UserSignInActivity;
import cn.cibnmp.ott.utils.Lg;


@SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
public class JsWebViewActivity extends BaseActivity implements View.OnClickListener {

    private WebView mJsWebView;
    private ProgressBar mProgressBar, mProgressBar1;
    private String TAG = "JsWebViewActivity";
    private String mWebViewUrl;
    private TextView webview_textview;
    private RelativeLayout webview_relativelayout_title;
    private LinearLayout webview_linearlayout_webview;
    private RelativeLayout webview_relativelayout_onerror;
    private ImageView webview_fanhui, webview_guanbi, webview_fanhui2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jswebview);
        initView();
        initData();
        setWebView();
    }

    private void initView() {
        mJsWebView = (WebView) findViewById(R.id.webview);
        mProgressBar = (ProgressBar) findViewById(R.id.progresspar);
        mProgressBar1 = (ProgressBar) findViewById(R.id.progresspar1);
        webview_textview = (TextView) findViewById(R.id.webview_textview);
        webview_fanhui = (ImageView) findViewById(R.id.webview_fanhui);
        webview_fanhui2 = (ImageView) findViewById(R.id.webview_fanhui2);
        webview_guanbi = (ImageView) findViewById(R.id.webview_guanbi);
        webview_relativelayout_title = (RelativeLayout) findViewById(R.id.webview_relativelayout_title);
        webview_relativelayout_onerror = (RelativeLayout) findViewById(R.id.webview_relativelayout_onerror);
        webview_linearlayout_webview = (LinearLayout) findViewById(R.id.webview_linearlayout_webview);
        webview_fanhui.setOnClickListener(JsWebViewActivity.this);
        webview_fanhui2.setOnClickListener(JsWebViewActivity.this);
        webview_guanbi.setOnClickListener(JsWebViewActivity.this);
        webview_relativelayout_onerror.setOnClickListener(JsWebViewActivity.this);
    }

    private void initData() {
        Bundle bundle = null;
        Intent intent = getIntent();
        if (intent != null) {
            bundle = intent.getBundleExtra(Constant.activityBundleExtra);
            if (bundle != null) {
                NavigationInfoItemBean navigationInfoItemBean = (NavigationInfoItemBean) bundle.get(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN);
                if (navigationInfoItemBean != null && !StringUtils.isEmpty(navigationInfoItemBean.getActionUrl())) {
                    mWebViewUrl = navigationInfoItemBean.getActionUrl();
                } else {
                    failToLoad();
                }
            } else {
                failToLoad();
            }
        } else {
            failToLoad();
        }
    }

    private void setWebView() {
        WebSettings webSettings = mJsWebView.getSettings();
        //设置自适应屏幕，两者合用适应控件大小
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //设置与Js交互的权限，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //设置允许Js弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //不使用缓存，只从网络获取数据.模式如下：
        // LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        // LOAD_DEFAULT:（默认）根据cache-control决定是否从网络上取数据。
        // LOAD_NO_CACHE: 不使用缓存，只从网络获取数据
        // LOAD_CACHE_ELSE_NETWORK: 只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            Lg.i(TAG, "网络可用");
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//根据cache-control决定是否从网络上取数据。
        } else {
            Lg.i(TAG, "网络不可用");
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加
        }
        //支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //设置编码
        webSettings.setDefaultTextEncodingName("utf-8");
        // 设置背景色
        mJsWebView.setBackgroundColor(Color.parseColor("#eceae3"));
        //H5Url
//        mJsWebView.loadUrl("http://www.firstsix-new-pc.html/?fv=zz_5993b5deb9f24");//错误测试404
//        mJsWebView.loadUrl("http://dajuyuantest.wx.cibn.cc/activity/Activitywx/app?actid=2901");//正确测试数据
        mJsWebView.loadUrl(mWebViewUrl);
        //打开H5
        startWebView();
        //H5调用安卓登录与用户信息方法
        mJsWebView.addJavascriptInterface(new JavaScriptObject() {

            @JavascriptInterface//注意:此处一定要加该注解,否则在4.1+系统上运行失败
            @Override//微信登录
            public void onCibnWebviewLogin() {
                Intent intent = new Intent(JsWebViewActivity.this, UserSignInActivity.class);
                startActivity(intent);
            }

            @JavascriptInterface//注意:此处一定要加该注解,否则在4.1+系统上运行失败
            @Override//项目id,应用id,渠道id,用户uid
            public String onCibnWebviewUser() {
                String json = "{"
                        + "'projectId':" + App.projId + ","
                        + "'appId':" + App.appId + ","
                        + "'channelId':" + App.channelId + ","
                        + "'cibnUserId':" + App.userId
                        + "}";
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    json = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Lg.i(TAG, "json" + json);
                return json;
            }

        }, "cibn_webview");

    }

    private void startWebView() {
        webview_relativelayout_title.setVisibility(View.VISIBLE);
        webview_linearlayout_webview.setVisibility(View.VISIBLE);
        mJsWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                view.loadUrl(url);
                //设置true在APP内打开
                return true;
            }

            @SuppressWarnings("deprecation")
            //加载失败接收不到404
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                failToLoad();
            }

            @SuppressLint("NewApi")
            @Override //网页加载结束调用js方法
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });

        //设置WebChromeClient类
        mJsWebView.setWebChromeClient(new WebChromeClient() {
            //获取网站标题
            @Override
            public void onReceivedTitle(android.webkit.WebView view, String title) {
                webview_textview.setText(title);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
            }

            //加载网页进度条
            @Override
            public void onProgressChanged(android.webkit.WebView view, int newProgress) {
                if (newProgress != 100) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                } else {
                    mProgressBar.setVisibility(View.GONE);
                }
                if (mProgressBar1.getVisibility() == View.VISIBLE) {
                    if (newProgress != 100) {
                        mProgressBar1.setVisibility(View.VISIBLE);
                        mProgressBar1.setProgress(newProgress);
                    } else {
                        mProgressBar1.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mJsWebView.canGoBack()) {
            mJsWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (mJsWebView != null) {
            mJsWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mJsWebView.clearHistory();
            ((ViewGroup) mJsWebView.getParent()).removeView(mJsWebView);
            mJsWebView.destroy();
            mJsWebView = null;
        }
        super.onDestroy();
    }

    @Override//登录成功刷新H5页面
    public void onEventMainThread(String event) {
        super.onEventMainThread(event);
        if (event.equals(Constant.HOME_USER_SIGN_IN)) {
            mJsWebView.reload();  //刷新
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.webview_fanhui:
                mJsWebView.goBack();
                break;
            case R.id.webview_fanhui2:
                mJsWebView.goBack();
                break;
            case R.id.webview_guanbi:
                if (mJsWebView != null) {
                    mJsWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                    mJsWebView.clearHistory();
                    ((ViewGroup) mJsWebView.getParent()).removeView(mJsWebView);
                    mJsWebView.destroy();
                    mJsWebView = null;
                }
                finish();
                break;
            case R.id.webview_relativelayout_onerror:
                initData();
                break;
        }
    }

    public void failToLoad() {
        webview_relativelayout_onerror.setVisibility(View.VISIBLE);
        webview_relativelayout_title.setVisibility(View.GONE);
        webview_linearlayout_webview.setVisibility(View.GONE);
    }
}

