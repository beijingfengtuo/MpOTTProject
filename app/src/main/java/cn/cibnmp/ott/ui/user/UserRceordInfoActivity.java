package cn.cibnmp.ott.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.DecimalFormat;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.PaySuccessBean;
import cn.cibnmp.ott.bean.UserRecordInfoBean;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.utils.DateUtil;
import cn.cibnmp.ott.utils.Utils;

/**
 * Created by cibn-lyc on 2018/1/25.
 */

public class UserRceordInfoActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "UserRceordInfoActivity";

    private String tradeNo;
    private View user_record_info;
    private ImageButton home_title_back;
    private TextView home_title_text, tv_info_product, tv_info_product_price,
            tv_info_product_pay_price, tv_info_product_pay_method, tv_info_deductible_amount,
            tv_info_pay_time, tv_info_exp_time, tv_info_trad_no;
    private DecimalFormat df = new DecimalFormat("0.00");
    private PaySuccessBean.PaySuccessInfo userRecordInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_record_info_activity);
        Intent intent = getIntent();
        tradeNo = intent.getStringExtra("tradeNo");
        loaderData();
        initView();
    }

    private void initView() {
        user_record_info = findViewById(R.id.user_record_info);
        // 返回
        home_title_back = (ImageButton) user_record_info.findViewById(R.id.home_title_back);
        home_title_back.setOnClickListener(this);
        // 标题
        home_title_text = (TextView) user_record_info.findViewById(R.id.home_title_text);
        home_title_text.setText(Utils.getInterString(this, R.string.record_info));
        // 产品包
        tv_info_product = (TextView) findViewById(R.id.tv_info_product);
        // 订单金额
        tv_info_product_price = (TextView) findViewById(R.id.tv_info_product_price);
        // 支付金额
        tv_info_product_pay_price = (TextView) findViewById(R.id.tv_info_product_pay_price);
        // 支付方式
        tv_info_product_pay_method = (TextView) findViewById(R.id.tv_info_product_pay_method);
        // 所用卡券
        tv_info_deductible_amount = (TextView) findViewById(R.id.tv_info_deductible_amount);
        // 支付时间
        tv_info_pay_time = (TextView) findViewById(R.id.tv_info_pay_time);
        // 到期时间
        tv_info_exp_time = (TextView) findViewById(R.id.tv_info_exp_time);
        // 订单号码
        tv_info_trad_no = (TextView) findViewById(R.id.tv_info_trad_no);
    }

    private void setData() {
        // 产品包
        tv_info_product.setText(userRecordInfo.getProductName());
        // 订单金额
        tv_info_product_price.setText(Utils.getInterString(this, R.string.money) + df.format(userRecordInfo.getProductPrice() / 100.00));
        // 支付金额
        tv_info_product_pay_price.setText(Utils.getInterString(this, R.string.money) + df.format(userRecordInfo.getPaymentAmount() / 100.00));
        // 支付方式
        String payType = Utils.getInterString(this, R.string.else_pay);
        if (userRecordInfo.getPayMethod().equals("0")) {
            payType = Utils.getInterString(this, R.string.wx_pay);
        } else if (userRecordInfo.getPayMethod().equals("1")) {
            payType = Utils.getInterString(this, R.string.alipay);
        } else if (userRecordInfo.getPayMethod().equals("2")) {
            payType = Utils.getInterString(this, R.string.wap);
        } else if (userRecordInfo.getPayMethod().equals("3")) {
            payType = Utils.getInterString(this, R.string.fast_payment);
        } else if (userRecordInfo.getPayMethod().equals("4")) {
            payType = Utils.getInterString(this, R.string.km_pay);
        } else if (userRecordInfo.getPayMethod().equals("5")) {
            payType = Utils.getInterString(this, R.string.voucher);
        } else if (userRecordInfo.getPayMethod().equals("100")) {
            payType = Utils.getInterString(this, R.string.give);
        } else if (userRecordInfo.getPayMethod().equals("110")) {
            payType = Utils.getInterString(this, R.string.else_pay);
        }
        tv_info_product_pay_method.setText(payType);
        // 所用卡券
        tv_info_deductible_amount.setText(Utils.getInterString(this, R.string.money) + df.format(userRecordInfo.getDeductibleAmount() / 100.00));
        // 支付时间
        String payTime = Utils.getInterString(this, R.string.unknown);
        if (userRecordInfo.getPayTime().length() > 0) {
            payTime = DateUtil.setTimeStyle(this, userRecordInfo.getPayTime());
        }
        tv_info_pay_time.setText(payTime);
        // 到期时间
        String expTime = Utils.getInterString(this, R.string.unknown);
        if (userRecordInfo.getExpTime().length() > 0) {
            expTime = DateUtil.setTimeStyle(this, userRecordInfo.getExpTime());
        }
        tv_info_exp_time.setText(expTime);
        // 订单号码
        tv_info_trad_no.setText(userRecordInfo.getTradeNo());
    }

    private void loaderData() {
        if(!"".equals(tradeNo)){
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
                        userRecordInfo = paySuccessBean.getData();
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

    private final int PAY_SUCCESS = 3000;
    private final int PAY_FAILU = 3001;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg != null){
                switch (msg.what){
                    case PAY_FAILU:
                        break;
                    case PAY_SUCCESS:
                        setData();
                        break;
                    default:
                        break;
                }
            }
            return false;
        }
    });

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_title_back:
                finish();
                break;
        }
    }
}
