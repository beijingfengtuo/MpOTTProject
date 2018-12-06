package cn.cibnmp.ott.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.KaMiBean;
import cn.cibnmp.ott.bean.PaySuccessBean;
import cn.cibnmp.ott.bean.VipProductPackageBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.ui.search.ToastUtils;
import cn.cibnmp.ott.utils.DateUtil;
import cn.cibnmp.ott.utils.Utils;
import cn.cibnmp.ott.utils.img.ImageFetcher;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.ClearEditText;
import de.greenrobot.event.EventBus;

/**
 * Created by cibn-lyc on 2018/2/1.
 */

public class KaMiPayActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "KaMiPayActivity";
    private View kami_title;
    private ImageButton tv_kami_back;
    private TextView tv_kami_title;
    private ClearEditText et_kami;
    private Button btn_kami_submit;
    private TextView tv_pay_kami_success_validity;
    private Button btn_kami_success;
    private LinearLayout ll_pay_success_show;
    private String productId;
    private String productName;
    private KaMiBean.KaMiOrderBean bossOrder;
    private String showDesc;
    private boolean isPaySuccess = false;
    private PaySuccessBean.PaySuccessInfo paySuccessData;
    private ImageView img_pay_kami_success_advertising;
    private TextView tv_kami_expire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kami_activity);
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        productName = intent.getStringExtra("productName");
        initView();
    }

    private void initView() {
        kami_title = findViewById(R.id.kami_title);
        // 返回
        tv_kami_back = (ImageButton) kami_title.findViewById(R.id.home_title_back);
        tv_kami_back.setOnClickListener(this);
        // 标题
        tv_kami_title = (TextView) kami_title.findViewById(R.id.home_title_text);
        tv_kami_title.setText(Utils.getInterString(this, R.string.kami_activate));
        // 卡密号
        et_kami = (ClearEditText) findViewById(R.id.et_kami);
        // 卡密提示
        tv_kami_expire = (TextView) findViewById(R.id.tv_kami_expire);
        tv_kami_expire.setVisibility(View.INVISIBLE);
        // 提交
        btn_kami_submit = (Button) findViewById(R.id.btn_kami_submit);
        btn_kami_submit.setOnClickListener(this);
        // 显示支付成功
        ll_pay_success_show = (LinearLayout) findViewById(R.id.ll_pay_success_show);
        // 有效期
        tv_pay_kami_success_validity = (TextView) findViewById(R.id.tv_pay_kami_success_validity1);
        // 确定
        btn_kami_success = (Button) findViewById(R.id.btn_kami_success);
        btn_kami_success.setOnClickListener(this);
        // 图片
        img_pay_kami_success_advertising = (ImageView) findViewById(R.id.img_pay_kami_success_advertising);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_title_back:
            case R.id.btn_kami_success:
                if (isPaySuccess){
                    Intent intent = getIntent();
                    setResult(3, intent);
                }
                finish();
                break;
            case R.id.btn_kami_submit:
                String kamiNo = et_kami.getText().toString();
                if (kamiNo == null) {
                    ToastUtils.show(this, Utils.getInterString(this, R.string.please_edit_kami));
                }else {
                    loaderData(kamiNo);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getAction()){
            case KeyEvent.KEYCODE_BACK:
                if (isPaySuccess){
                    Intent intent = getIntent();
                    setResult(3, intent);
                }
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private final int GET_DATA_SUCCESS = 2000;
    private final int GET_DATA_FAILU = 2001;
    private final int PAY_SUCCESS = 3000;
    private final int PAY_FAILU = 3001;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg != null) {
                switch (msg.what) {
                    case GET_DATA_SUCCESS:
                        getPaySuccessData(bossOrder.getTradeNo());
                        break;
                    case GET_DATA_FAILU:
                        isPaySuccess = false;
                        tv_kami_expire.setVisibility(View.VISIBLE);
                        break;
                    case PAY_SUCCESS:
                        EventBus.getDefault().post(Constant.WX_RETURN_SUCCESS);
                        isPaySuccess = true;
                        ll_pay_success_show.setVisibility(View.VISIBLE);
                        String expTime = DateUtil.setTimeStyle(KaMiPayActivity.this, paySuccessData.getExpTime());
                        tv_pay_kami_success_validity.setText(expTime);
                        ImageFetcher.getInstance().loodingImage(paySuccessData.getImageUrl(),
                                img_pay_kami_success_advertising, R.color.black_hui8);
                        break;
                    case PAY_FAILU:
                        isPaySuccess = false;
                        ToastUtils.show(KaMiPayActivity.this, showDesc);
                        break;
                    default:
                        isPaySuccess = false;
                        break;
                }
            }
            return false;
        }
    });


    private void loaderData(String kamiNo) {
        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
        js_request.put("projectId", App.projId);//根据实际需求添加相应键值对
        js_request.put("appId", App.appId);
        js_request.put("channelId", App.channelId);
        js_request.put("cibnUserId", App.userId);
        js_request.put("productId", productId);
        js_request.put("termId", App.publicTID);
        js_request.put("productName", productName);
        js_request.put("camiOrder", kamiNo);

        RequestParams params = new RequestParams(App.bmsurl + VipPayUrlUtil.reqCardUrl);
        params.setAsJsonContent(true);
        params.setBodyContent(js_request.toString());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: " + result);
                KaMiBean kaMiBean = JSON.parseObject(result, KaMiBean.class);
                if (kaMiBean.getResultCode().equals("1")) {
                    bossOrder = kaMiBean.getBossOrder();
                    mHandler.sendEmptyMessage(GET_DATA_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(GET_DATA_FAILU);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i(TAG, "onError: " + ex);
                mHandler.sendEmptyMessage(GET_DATA_FAILU);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.i(TAG, "onCancelled: " + cex);
            }

            @Override
            public void onFinished() {
                Log.i(TAG, "onFinished: ");
            }
        });
    }

    // 支付成功后验证后台订单
    private void getPaySuccessData(final String tradeNo) {
        JSONObject js_request = new JSONObject();
        js_request.put("tradeNo", tradeNo);

        RequestParams params = new RequestParams(App.bmsurl + VipPayUrlUtil.reqOrderInfourl);
        params.setAsJsonContent(true);
        params.setBodyContent(js_request.toString());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "getPaySuccessData===onSuccess: " + result);
                PaySuccessBean paySuccessBean = JSON.parseObject(result, PaySuccessBean.class);
                if (paySuccessBean != null && paySuccessBean.getData() != null && paySuccessBean.getData().getTradeNo() != null) {
                    paySuccessData = paySuccessBean.getData();
                    mHandler.sendEmptyMessage(PAY_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(PAY_FAILU);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i(TAG, "onError: " + ex.toString());
                mHandler.sendEmptyMessage(PAY_FAILU);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
