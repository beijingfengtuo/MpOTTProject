package cn.cibnmp.ott.ui.user;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.sdk.modelpay.PayReq;

import org.xutils.common.Callback;
import org.xutils.common.util.MD5;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.PaySuccessBean;
import cn.cibnmp.ott.bean.UserWXPayBean;
import cn.cibnmp.ott.bean.VipProductPriceBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.base.Action;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.ui.search.ToastUtils;
import cn.cibnmp.ott.utils.DateUtil;
import cn.cibnmp.ott.utils.MD5FileUtil;
import cn.cibnmp.ott.utils.TimeUtils;
import cn.cibnmp.ott.utils.Utils;
import cn.cibnmp.ott.utils.img.ImageFetcher;
import de.greenrobot.event.EventBus;

import static cn.cibnmp.ott.App.api;
import static cn.cibnmp.ott.App.wxBindUrl;

/**
 * Created by cibn-lyc on 2018/1/18.
 */

public class VipPriceActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "VipPriceActivity";
    private final Double PRICE_CONVERSION = 100.00;
    // title
    private View user_title;
    // 会员中心
    private TextView tv_home_title_pay;
    // 返回
    private ImageButton titleBack;
    // 产品包id
    private String productId;
    private ImageView img_banner;
    private TextView tv_product_name;
    private ListView lv_product_price;
    private RelativeLayout rl_vouchers;
    private TextView tv_pay_vouchers_price;
    private TextView tv_pat_vouchers_name;
    private RelativeLayout rl_kami;
    private RelativeLayout rl_wx_pay;
    private ImageView img_pay_wx_check;
    private TextView tv_pay_sure_price;
    private TextView tv_pay;
    private ProductPriceAdapter productPriceAdapter;
    private List<VipProductPriceBean.VipProductPriceInfoBean> dataList;
    private int seriesId;
    private int seriesType;
    private DecimalFormat df = new DecimalFormat("0.00");

    // 获取产品包价格
    private long mProductPackagePrice;
    //代金券显示优惠价格
    private long vouchersPrice = 0;
    private String voucherName, voucherCode;
    // 微信选择状态
    private boolean wxSelected = true;
    // 协议状态
    private boolean agreementSelected = true;
    // 支付可点击状态
    private boolean payCheck = true;

    private ImageView img_pay_agreement;
    private TextView tv_vip_agreement;
    private UserWXPayBean.WXPayDataBean wxPay;
    private String mPriceId;
    private LinearLayout ll_vip_price;

    private PaySuccessBean.PaySuccessInfo paySuccessData;
    private TextView tv_pay_success_price;
    private TextView tv_pay_success_number;
    private TextView tv_pay_success_validity;
    private ImageView img_pay_success_advertising;
    private Button btn_pay_success_sure;
    // 请求服务器支付成功失败标识（一次）
    private int payFaile = 0;
    // 加载进度条
    private RelativeLayout play_looding;
    private ImageView progress_icon;
    private RotateAnimation animation;

    private final int GET_DATA_SUCCESS = 2000;
    private final int GET_DATA_FAILU = 1000;
    private final int LOADER_PAY_SUCCESS = 4000;
    private final int LOADER_PAY_FAILU = 4001;
    private final int PAY_SUCCESS = 3000;
    private final int PAY_FAILU = 3001;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg != null) {
                switch (msg.what) {
                    case GET_DATA_SUCCESS:
                        productPriceAdapter.setData(dataList.get(0).getPrices());
                        ViewGroup.LayoutParams layoutParams = lv_product_price.getLayoutParams();
                        layoutParams.height = dataList.get(0).getPrices().size() * 163;
                        lv_product_price.setLayoutParams(layoutParams);
                        ImageFetcher.getInstance().loodingImage(dataList.get(0).getBannerImg(),
                                img_banner, R.color.black_hui8);
                        tv_product_name.setText(dataList.get(0).getProductName());
                        mPriceId = dataList.get(0).getPrices().get(0).getPriceId();
                        mProductPackagePrice = dataList.get(0).getPrices().get(0).getProductPrices();
                        tv_pay_sure_price.setText(Utils.getInterString(VipPriceActivity.this, R.string.money) + df.format(mProductPackagePrice / PRICE_CONVERSION));
                        break;
                    case GET_DATA_FAILU:
                        ToastUtils.show(VipPriceActivity.this, Utils.getInterString(VipPriceActivity.this, R.string.error_toast));
                        break;
                    case LOADER_PAY_SUCCESS:
                        if (wxPay.getBuyValidInfo().getPaymentPrice() > 0) {
                            setPayReq();
                        } else {
                            EventBus.getDefault().post(Constant.WX_RETURN_SUCCESS);
                        }
                        break;
                    case LOADER_PAY_FAILU:
                        stopLooding();
                        ToastUtils.show(VipPriceActivity.this, Utils.getInterString(VipPriceActivity.this, R.string.pay_faile));
                        break;
                    case PAY_SUCCESS:
                        ll_vip_price.setVisibility(View.GONE);
                        stopLooding();
                        Double price = paySuccessData.getPaymentAmount() / PRICE_CONVERSION;
                        tv_pay_success_price.setText(Utils.getInterString(VipPriceActivity.this, R.string.money) + df.format(price));
                        tv_pay_success_number.setText(paySuccessData.getTradeNo());
                        tv_pay_success_validity.setText(DateUtil.setTimeStyle(VipPriceActivity.this, paySuccessData.getExpTime()));
                        ImageFetcher.getInstance().loodingImage(paySuccessData.getImageUrl(),
                                img_pay_success_advertising, R.color.black_hui8);
                        break;
                    case PAY_FAILU:
                        if (payFaile == 0) {
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            getPaySuccessData(wxPay.getTradeNo());
                            payFaile = 1;
                            break;
                        }
                        stopLooding();
                        ToastUtils.show(VipPriceActivity.this, Utils.getInterString(VipPriceActivity.this, R.string.pay_faile));
                        break;
                    default:
                        break;
                }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vip_price_activity);
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        seriesId = intent.getIntExtra("seriesId", -1);
        seriesType = intent.getIntExtra("seriesType", -1);
        loaderPriceListData();
        initView();
    }

    private void initView() {
        //加载数据进度条
        play_looding = (RelativeLayout) findViewById(R.id.play_looding);
        progress_icon = (ImageView) findViewById(R.id.progress_icon);
        animation = (RotateAnimation) AnimationUtils.loadAnimation(this,
                R.anim.rotating);
        LinearInterpolator lir = new LinearInterpolator();
        animation.setInterpolator(lir);
        // title
        user_title = findViewById(R.id.user_title);

        //title（付费）
        tv_home_title_pay = (TextView) user_title.findViewById(R.id.home_title_text);
        tv_home_title_pay.setVisibility(View.VISIBLE);
        tv_home_title_pay.setText(Utils.getInterString(this, R.string.pay));

        // 返回
        titleBack = (ImageButton) user_title.findViewById(R.id.home_title_back);
        titleBack.setVisibility(View.VISIBLE);
        titleBack.setOnClickListener(this);

        // 价格页面
        ll_vip_price = (LinearLayout) findViewById(R.id.ll_vip_price);

        // banner图
        img_banner = (ImageView) findViewById(R.id.img_banner);
        // 产品包名称
        tv_product_name = (TextView) findViewById(R.id.tv_product_name);
        // 产品包价格列表
        lv_product_price = (ListView) findViewById(R.id.lv_product_price);
        // 优惠券
        rl_vouchers = (RelativeLayout) findViewById(R.id.rl_vouchers);
        rl_vouchers.setOnClickListener(this);
        // 优惠券显示价格
        tv_pay_vouchers_price = (TextView) findViewById(R.id.tv_pay_vouchers_price);
        // 优惠券显示文字
        tv_pat_vouchers_name = (TextView) findViewById(R.id.tv_pat_vouchers_name);
        // 卡密激活
        rl_kami = (RelativeLayout) findViewById(R.id.rl_kami);
        rl_kami.setOnClickListener(this);
        // 微信支付
        rl_wx_pay = (RelativeLayout) findViewById(R.id.rl_wx_pay);
        rl_wx_pay.setOnClickListener(this);
        // 微信图标
        img_pay_wx_check = (ImageView) findViewById(R.id.img_pay_wx_check);
        // 协议选择图标
        img_pay_agreement = (ImageView) findViewById(R.id.img_pay_agreement);
        img_pay_agreement.setOnClickListener(this);
        // 协议文字
        tv_vip_agreement = (TextView) findViewById(R.id.tv_vip_agreement);
        tv_vip_agreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_vip_agreement.setOnClickListener(this);
        // 合计金额
        tv_pay_sure_price = (TextView) findViewById(R.id.tv_pay_sure_price);
        // 支付
        tv_pay = (TextView) findViewById(R.id.tv_pay);
        tv_pay.setOnClickListener(this);

        // 支付成功：订单金额
        tv_pay_success_price = (TextView) findViewById(R.id.tv_pay_success_price1);
        // 支付成功：订单号
        tv_pay_success_number = (TextView) findViewById(R.id.tv_pay_success_number1);
        // 支付成功：有效期
        tv_pay_success_validity = (TextView) findViewById(R.id.tv_pay_success_validity1);
        // 支付成功：图片
        img_pay_success_advertising = (ImageView) findViewById(R.id.img_pay_success_advertising);
        // 支付成功：确认
        btn_pay_success_sure = (Button) findViewById(R.id.btn_pay_success_sure);
        btn_pay_success_sure.setOnClickListener(this);

        productPriceAdapter = new ProductPriceAdapter(this, new ProductPriceAdapter.OnItemClick() {
            @Override
            public void onItemClick(VipProductPriceBean.VipProductPriceInfoBean.PriceListBean priceListBean) {
                mProductPackagePrice = priceListBean.getProductPrices();
                mPriceId = priceListBean.getPriceId();
                EventBus.getDefault().post(Constant.SELECT_PRODUCT_PRICE);
            }
        });
        lv_product_price.setAdapter(productPriceAdapter);

    }

    // 价格列表
    private void loaderPriceListData() {
        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
        js_request.put("projectId", App.projId);//根据实际需求添加相应键值对
        js_request.put("appId", App.appId);
        js_request.put("channelId", App.channelId);
        if (App.userId > 0) {
            js_request.put("cibnUserId", App.userId);
        }
        js_request.put("termId", App.publicTID);
        js_request.put("sessionId", App.sessionId);
        js_request.put("productId", productId);
        if (seriesId != -1 && seriesType != -1) {
            js_request.put("seriesId", seriesId);
            js_request.put("seriesType", seriesType);
        }

        Log.i(TAG, "loaderPriceListData: " + js_request.toString());
        RequestParams params = new RequestParams(App.bmsurl + VipPayUrlUtil.reqPriceUrl);
        params.setAsJsonContent(true);
        params.setBodyContent(js_request.toString());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "loaderPriceListData=====onSuccess: " + result);
                VipProductPriceBean vipProductPriceBean = JSON.parseObject(result, VipProductPriceBean.class);
                if (vipProductPriceBean != null && vipProductPriceBean.getDataList() != null
                        && vipProductPriceBean.getDataList().size() > 0
                        && vipProductPriceBean.getDataList().get(0).getPrices() != null
                        && vipProductPriceBean.getDataList().get(0).getPrices().size() > 0) {
                    dataList = vipProductPriceBean.getDataList();
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
                Log.i(TAG, "onCancelled: ");
            }

            @Override
            public void onFinished() {
                Log.i(TAG, "onFinished: ");
            }
        });
    }

    // 支付下单接口
    public void loaderPayData() {
        JSONObject js_request = new JSONObject();
        js_request.put("projectId", App.projId);
        js_request.put("appId", App.appId);
        js_request.put("channelId", App.channelId);
        js_request.put("cibnUserId", App.userId);
        js_request.put("productId", productId);
        js_request.put("termId", App.publicTID);
        if (voucherCode != null) {
            js_request.put("voucherId", voucherCode);
        }
        js_request.put("priceId", mPriceId);
        js_request.put("clientId", App.publicTID);
        js_request.put("openId", Utils.getUserOpenId());
//        js_request.put("token", Utils.getTokenId());
        js_request.put("payType", "9");
        js_request.put("body", "东方大剧院");

        Log.i(TAG, "loaderPayData: " + js_request.toString());
        RequestParams params = new RequestParams(App.bmsurl + VipPayUrlUtil.payUrl);
        params.setAsJsonContent(true);
        params.setBodyContent(js_request.toString());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "loaderPayData====onSuccess " + result);
                UserWXPayBean userWXPayBean = JSON.parseObject(result, UserWXPayBean.class);
                if (userWXPayBean != null && userWXPayBean.getData() != null
                        && userWXPayBean.getData().getBuyValidInfo() != null) {
                    wxPay = userWXPayBean.getData();
                    mHandler.sendEmptyMessage(LOADER_PAY_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(LOADER_PAY_FAILU);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i(TAG, "onError: " + ex);
                mHandler.sendEmptyMessage(LOADER_PAY_FAILU);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.i(TAG, "onCancelled: ");
            }

            @Override
            public void onFinished() {
                Log.i(TAG, "onFinished: ");
            }
        });
    }

    // 调微信支付
    private void setPayReq() {
        PayReq req = new PayReq();
        req.appId = Constant.WX_APP_ID;
        req.partnerId = wxPay.getMchId();
        req.prepayId = wxPay.getPrePayId();
        req.nonceStr = wxPay.getNonceStr();
        req.timeStamp = wxPay.getTimeStamp();
        req.packageValue = Constant.WX_APP_PACKAGE;
        req.sign = wxPay.getPaySingMd5();
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api = App.getWxApi();
        api.sendReq(req);
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

    // 获取合计金额
    private Double getCombinedPrice() {
        long mCombinedPrice;
        if (vouchersPrice != 0) {
            if (mProductPackagePrice < vouchersPrice) {
                mCombinedPrice = 0;
            } else {
                mCombinedPrice = mProductPackagePrice - vouchersPrice;
            }
        } else {
            mCombinedPrice = mProductPackagePrice;
        }
        return mCombinedPrice / PRICE_CONVERSION;
    }

    // 设置支付按钮可点击状态
    private void setPayColor() {
        if (wxSelected && agreementSelected) {
            payCheck = true;
            tv_pay.setBackgroundColor(getResources().getColor(R.color.vip_price_pay_sure));
        } else {
            payCheck = false;
            tv_pay.setBackgroundColor(getResources().getColor(R.color.vip_price_pay_unsure));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_title_back:
            case R.id.btn_pay_success_sure:
                finish();
                break;
            case R.id.rl_vouchers:
                // 判断登陆
                if (App.isLogin) {
                    Intent intent = new Intent(this, SelectVouchersActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    ToastUtils.show(this, Utils.getInterString(this, R.string.please_login));
                    startActivity(Action.getActionName(Action.OPEN_USER_SIGN_IN), new Bundle());
                }
                break;
            case R.id.rl_kami:
                // 判断登陆
                if (App.isLogin) {
                    Intent intent1 = new Intent(this, KaMiPayActivity.class);
                    intent1.putExtra("productId", dataList.get(0).getProductId());
                    intent1.putExtra("productName", dataList.get(0).getProductName());
                    startActivityForResult(intent1, 2);
                } else {
                    ToastUtils.show(this, Utils.getInterString(this, R.string.please_login));
                    startActivity(Action.getActionName(Action.OPEN_USER_SIGN_IN), new Bundle());
                }
                break;
            case R.id.rl_wx_pay:
                if (wxSelected) {
                    wxSelected = false;
                    img_pay_wx_check.setImageResource(R.mipmap.pay_wx_uncheck);
                } else {
                    wxSelected = true;
                    img_pay_wx_check.setImageResource(R.mipmap.pay_wx_check);
                }
                setPayColor();
                break;
            case R.id.tv_pay:
                if (payCheck) {
                    if (App.isLogin) {
                        loaderPayData();
                    } else {
                        ToastUtils.show(this, Utils.getInterString(this, R.string.please_login));
                        startActivity(Action.getActionName(Action.OPEN_USER_SIGN_IN), new Bundle());
                    }
                }
                break;
            case R.id.img_pay_agreement:
                if (agreementSelected) {
                    agreementSelected = false;
                    img_pay_agreement.setImageResource(R.mipmap.pay_unagreement);
                } else {
                    agreementSelected = true;
                    img_pay_agreement.setImageResource(R.mipmap.pay_agreement);
                }
                setPayColor();
                break;
            case R.id.tv_vip_agreement:
                Intent intent2 = new Intent(this, VipAgreementActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            vouchersPrice = data.getLongExtra("vouchersPrice", 0);
            voucherName = data.getStringExtra("voucherName");
            voucherCode = data.getStringExtra("voucherCode");
            EventBus.getDefault().post(Constant.SELECT_VOUCHERS_PRICE);
        } else if (resultCode == 2) {
            EventBus.getDefault().post(Constant.SELECT_NO_VOUCHERS_PRICE);
        } else if (resultCode == 3) {
            finish();
        }
    }

    @Override
    public void onEventMainThread(String event) {
        super.onEventMainThread(event);
        if (event.equals(Constant.SELECT_VOUCHERS_PRICE)) {
            tv_pay_vouchers_price.setVisibility(View.VISIBLE);
            tv_pat_vouchers_name.setText(voucherName);
            tv_pay_vouchers_price.setText(Utils.getInterString(VipPriceActivity.this, R.string.money) + df.format(vouchersPrice / PRICE_CONVERSION));
            tv_pay_sure_price.setText(Utils.getInterString(VipPriceActivity.this, R.string.money) + df.format(getCombinedPrice()));
        }else if (event.equals(Constant.SELECT_NO_VOUCHERS_PRICE)) {
            vouchersPrice = 0;
            tv_pay_vouchers_price.setVisibility(View.GONE);
            tv_pat_vouchers_name.setText(R.string.use_voucher);
            tv_pay_sure_price.setText(Utils.getInterString(VipPriceActivity.this, R.string.money) + df.format(getCombinedPrice()));
        } else if (event.equals(Constant.SELECT_PRODUCT_PRICE)) {
            tv_pay_sure_price.setText(Utils.getInterString(VipPriceActivity.this, R.string.money) + df.format(getCombinedPrice()));
        } else if (event.equals(Constant.WX_RETURN_SUCCESS)) {
            startLooding();
            getPaySuccessData(wxPay.getTradeNo());
        }
    }

    /**
     * 开始加载动画
     */
    private void startLooding() {
        play_looding.setVisibility(View.VISIBLE);
        progress_icon.startAnimation(animation);
    }

    /**
     * 停止加载动画
     */
    public void stopLooding() {
        if (play_looding != null && animation != null) {
            play_looding.setVisibility(View.GONE);
            animation.cancel();
            progress_icon.clearAnimation();
        }
    }
}
