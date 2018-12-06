package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.utils.AppManager;

/**
 * Created by wanqi on 2017/3/18.
 * 应用禁用的dialog，在认证返回禁用code时弹出
 */

public class AppDisableDialog extends Dialog {

    private Context context;
    private String url;

    public AppDisableDialog(Context context) {
        super(context);
        this.context = context;
    }

    public AppDisableDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public static class Builder {
        WebView webView;

        private Context context;
        private String url;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder(String url, Context context) {
            this.url = url;
            this.context = context;
        }

        public AppDisableDialog create() {
            View view = LayoutInflater.from(context).inflate(R.layout.app_disable_dialog, null);
            final AppDisableDialog dialog = new AppDisableDialog(context, R.style.exitDialog);

            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.exitDialogStyle);

            webView = (WebView) view.findViewById(R.id.webView);

            webView.setBackgroundColor(Color.parseColor("#00000000"));
            webView.setBackgroundResource(R.color.black);

            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);

            if (TextUtils.isEmpty(url))
                webView.loadUrl("file:///android_asset/authentication.html");
            else
                webView.loadUrl(url);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });

            dialog.setContentView(view);

            return dialog;
        }


    }

    @Override
    public void dismiss() {
        AppManager.getAppManager().AppExit(context);
    }
}
