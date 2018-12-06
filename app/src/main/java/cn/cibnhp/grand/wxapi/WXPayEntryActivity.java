package cn.cibnhp.grand.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.cibnmp.ott.App;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.ui.search.ToastUtils;
import de.greenrobot.event.EventBus;

/**
 * Created by cibn-lyc on 2018/1/23.
 */

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = App.getWxApi();
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCode = resp.errCode;
            if (errCode == -1) {
                ToastUtils.show(this, "支付出现错误");
            } else if (errCode == 0) {
                ToastUtils.show(this, "支付完成");
                EventBus.getDefault().post(Constant.WX_RETURN_SUCCESS);
            }else if (errCode == -2){
                ToastUtils.show(this, "取消支付");
            }
            finish();
        }
    }
}
