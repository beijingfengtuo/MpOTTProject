package cn.cibnmp.ott.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

/**
 * Created by cibn-lyc on 2018/1/31.
 */

public class SelectVouchersActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private final String TAG = "SelectVouchersActivity";
    // 代金券接口
    // 优惠券状态（1-未使用  3-已使用 4-已过期）
    private int voucherStatus = 1;
    private List<VoucherListBean.DataBean.VouchersBean> vouchers;
    private View select_vouchers_title;
    private ImageButton select_vouchers_back;
    private TextView tv_title;
    private RelativeLayout rl_no_vouchers;
    private ListView lv_select_vouchers;
    private VouchersAdapter voucherAdapter;
    private TextView home_title_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_vouchers_activity);
        initView();
        loaderData(voucherStatus);
    }

    private void loaderData(int status) {
        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
        js_request.put("projectId", App.projId);//根据实际需求添加相应键值对
        js_request.put("appId", App.appId);
        js_request.put("channelId", App.channelId);
        js_request.put("cibnUserId", App.userId);
        js_request.put("voucherStatus", status);

        RequestParams params = new RequestParams(App.bmsurl + VipPayUrlUtil.reqVoucherUrl);
        params.setAsJsonContent(true);
        params.setBodyContent(js_request.toString());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: " + result);
                VoucherListBean voucherList = JSON.parseObject(result, VoucherListBean.class);
                Log.i(TAG, "onSuccess: " + voucherList.toString());
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
        select_vouchers_title = findViewById(R.id.select_vouchers_title);
        // 返回
        select_vouchers_back = (ImageButton) select_vouchers_title.findViewById(R.id.home_title_back);
        select_vouchers_back.setOnClickListener(this);
        // title
        tv_title = (TextView) select_vouchers_title.findViewById(R.id.home_title_text);
        tv_title.setText(Utils.getInterString(this, R.string.select_voucher));
        tv_title.setVisibility(View.VISIBLE);
        // 取消优惠券
        home_title_right = (TextView) select_vouchers_title.findViewById(R.id.home_title_right);
        home_title_right.setBackgroundResource(R.mipmap.cancel_voucher);
        home_title_right.setText(Utils.getInterString(this, R.string.unuse_voucher));
        home_title_right.setVisibility(View.VISIBLE);
        home_title_right.setOnClickListener(this);

        // 无优惠券
        rl_no_vouchers = (RelativeLayout) findViewById(R.id.rl_no_vouchers);
        // 优惠券列表
        lv_select_vouchers = (ListView) findViewById(R.id.lv_select_vouchers);
        voucherAdapter = new VouchersAdapter(this);
        lv_select_vouchers.setAdapter(voucherAdapter);
        lv_select_vouchers.setOnItemClickListener(this);
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
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_title_back:
                finish();
                break;
            case R.id.home_title_right:
                Intent intent = getIntent();
                setResult(2, intent);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long vouchersPrice = vouchers.get(position).getVoucherMoney();
        Intent intent = getIntent();
        intent.putExtra("vouchersPrice", vouchersPrice);
        intent.putExtra("voucherName", vouchers.get(position).getVoucherName());
        intent.putExtra("voucherCode", vouchers.get(position).getVoucherCode());
        setResult(1, intent);
        this.finish();
    }
}
