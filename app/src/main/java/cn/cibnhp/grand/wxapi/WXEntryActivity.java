package cn.cibnhp.grand.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import cn.cibnmp.ott.App;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.utils.SharePrefUtils;

/**
 * Created by cibn-lyc on 2018/1/10.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    private String intentName = null;
    private boolean isShow = false;
    private String weixinCode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent();//数据是使用Intent返回
        intent.putExtra("result", 2);//把返回数据存入Intent
        setResult(RESULT_OK, intent);//设置返回数据

        // 获取IWXAPI的实例
        api = App.getWxApi();
        intentName = getIntent().getStringExtra(Constant.USER_SIGN_IN);
        if (intentName != null) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = Constant.WX_APP_SCOPE;
            req.state = Constant.WX_APP_SECRET;
            api.sendReq(req);
        }

        if (!api.isWXAppInstalled()) {
            // 提醒用户没有安装微信
            Toast.makeText(getApplicationContext(), "没有安装微信", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (intentName == null) {
            finish();
        }
        if (isShow && weixinCode == null) {
            finish();
        } else {
            isShow = true;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        finish();
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        String result = "";
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "发送成功";
                weixinCode = ((SendAuth.Resp) resp).code;
                if (weixinCode != null) {
                    SharePrefUtils.saveString("weixinCode", weixinCode);
                    System.out.println("code------------------------->>>>>>>>>>>>>>" + weixinCode);
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                break;
            default:
                result = "发送返回";
                break;
        }
        System.out.println("code------------------------->>>>>>>>>>>>>>" + result);
//        Toast.makeText(this, resp.errCode + result, Toast.LENGTH_LONG).show();
        finish();
    }
}
