package cn.cibnmp.ott.ui.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.VoucherListBean;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.utils.Utils;


public class VouchersActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "VouchersActivity";
    private RelativeLayout user_title;
    private ImageButton home_title_back;
    private TextView home_title_text;
    private TextView tv_vouchers_can_use, tv_vouchers_has_use, tv_vouchers_expired;
    private ListView lv_vouchers;
    private List<VoucherListBean.DataBean.VouchersBean> vouchers;
    private VouchersAdapter voucherAdapter;
    // 优惠券状态（1-未使用  3-已使用 4-已过期）
    private int voucherStatus = 1;
    private RelativeLayout rl_no_vouchers;
    private TextView tv_no_vouchers_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchers);
        initView();
        loaderData(voucherStatus);
    }

    private void loaderData(final int status) {
        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
        js_request.put("projectId", App.projId);//根据实际需求添加相应键值对
        js_request.put("appId", App.appId);
        js_request.put("channelId", App.channelId);
        js_request.put("cibnUserId", App.userId);
        js_request.put("voucherStatus", status);

        Log.i(TAG, "loaderData: "+ js_request.toString());
        RequestParams params = new RequestParams(App.bmsurl + VipPayUrlUtil.reqVoucherUrl);
        params.setAsJsonContent(true);
        params.setBodyContent(js_request.toString());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: " + result);
                VoucherListBean voucherList = JSON.parseObject(result, VoucherListBean.class);
                Log.i(TAG, "onSuccess: " + voucherList.toString());
                voucherStatus = status;
                if (voucherList != null && voucherList.getData() != null && voucherList
                        .getData().getVouchers() != null && voucherList.getData()
                        .getVouchers().size() > 0) {
                    vouchers = voucherList.getData().getVouchers();
                    handler.sendEmptyMessage(3001);
                } else {
                    handler.sendEmptyMessage(3000);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handler.sendEmptyMessage(3000);
                Log.e("TAG", "getVouchersList onError , " + (ex == null ? "" : ex));
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

    private void initView() {
        user_title = (RelativeLayout) findViewById(R.id.user_title);
        // 无优惠券
        rl_no_vouchers = (RelativeLayout) findViewById(R.id.rl_no_vouchers);
        // 无优惠券文案
        tv_no_vouchers_name = (TextView)findViewById(R.id.tv_no_vouchers_name);

        // 返回
        home_title_back = (ImageButton) user_title.findViewById(R.id.home_title_back);
        home_title_back.setOnClickListener(this);

        home_title_text = (TextView) user_title.findViewById(R.id.home_title_text);
        home_title_text.setText(Utils.getInterString(this, R.string.voucher0));
        // 可使用
        tv_vouchers_can_use = (TextView) findViewById(R.id.tv_vouchers_can_use);
        tv_vouchers_can_use.setOnClickListener(this);
        // 已使用
        tv_vouchers_has_use = (TextView) findViewById(R.id.tv_vouchers_has_use);
        tv_vouchers_has_use.setOnClickListener(this);
        // 已过期
        tv_vouchers_expired = (TextView) findViewById(R.id.tv_vouchers_expired);
        tv_vouchers_expired.setOnClickListener(this);
        // listview
        lv_vouchers = (ListView) findViewById(R.id.lv_vouchers);
        voucherAdapter = new VouchersAdapter(this);
        lv_vouchers.setAdapter(voucherAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_title_back:
                finish();
                break;
            case R.id.tv_vouchers_can_use:
                setColor(1);
                loaderData(1);
                break;
            case R.id.tv_vouchers_has_use:
                setColor(2);
                loaderData(3);
                break;
            case R.id.tv_vouchers_expired:
                setColor(3);
                loaderData(4);
                break;
        }
    }

    private void setColor(int id) {
        switch (id) {
            case 1:
                tv_vouchers_can_use.setTextColor(getResources().getColor(R.color.red3));
                tv_vouchers_has_use.setTextColor(getResources().getColor(R.color.orange));
                tv_vouchers_expired.setTextColor(getResources().getColor(R.color.orange));
                break;
            case 2:
                tv_vouchers_can_use.setTextColor(getResources().getColor(R.color.orange));
                tv_vouchers_has_use.setTextColor(getResources().getColor(R.color.red3));
                tv_vouchers_expired.setTextColor(getResources().getColor(R.color.orange));
                break;
            case 3:
                tv_vouchers_can_use.setTextColor(getResources().getColor(R.color.orange));
                tv_vouchers_has_use.setTextColor(getResources().getColor(R.color.orange));
                tv_vouchers_expired.setTextColor(getResources().getColor(R.color.red3));
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3001:
                    rl_no_vouchers.setVisibility(View.GONE);
                    voucherAdapter.setData(vouchers, voucherStatus);
                    break;
                case 3000:
                    rl_no_vouchers.setVisibility(View.VISIBLE);
                    setText();
                    break;
            }
        }
    };

    private void setText() {
        switch (voucherStatus){
            case 1:
                tv_no_vouchers_name.setText(Utils.getInterString(this, R.string.you_no_can_use_voucher));
                break;
            case 3:
                tv_no_vouchers_name.setText(Utils.getInterString(this, R.string.you_no_has_use_voucher));
                break;
            case 4:
                tv_no_vouchers_name.setText(Utils.getInterString(this, R.string.you_no_expired_voucher));
                break;
        }
    }
}
