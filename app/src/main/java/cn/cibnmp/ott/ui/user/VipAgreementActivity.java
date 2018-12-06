package cn.cibnmp.ott.ui.user;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.ui.base.BaseActivity;

/**
 * Created by cibn-lyc on 2018/2/3.
 */

public class VipAgreementActivity extends BaseActivity {

    private final String TAG = "VipAgreementActivity";
    private WebView wb_vip_agreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vip_agreement_activity);
        initView();
    }

    private void initView() {
        wb_vip_agreement = (WebView) findViewById(R.id.wb_vip_agreement);
        wb_vip_agreement.loadUrl("http://dajuyuan2.wx.cibn.cc/common/userAgreement");
        wb_vip_agreement.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}
