package cn.cibnmp.ott.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.VipProductPackageBean;
import cn.cibnmp.ott.bean.VipProductPackageBean.VipProductInfoBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.base.Action;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.ui.search.ToastUtils;
import cn.cibnmp.ott.utils.Utils;
import de.greenrobot.event.EventBus;

/**
 * Created by cibn-lyc on 2018/1/31.
 */

public class InfoEnterProductPackcgeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private final String TAG = "InfoEnterProductPackcgeActivity";
    private View info_product_title;
    private TextView tv_info_enter_product_title;
    private ImageView tv_info_enter_product_back;
    private ListView lv_info_product;
    private List<VipProductInfoBean> productList;
    private InfoEnterProductPackcgeAdapter infoEnterProductPackcgeAdapter;
    private String seriesId;
    private String seriesType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!App.isLogin) {
            ToastUtils.show(this, Utils.getInterString(this, R.string.please_login));
            startActivity(Action.getActionName(Action.OPEN_USER_SIGN_IN), new Bundle());
            finish();
        }
        setContentView(R.layout.info_enter_product_activity);
        EventBus.getDefault().post(Constant.INFO_INTER_PRODUCT_PACKAGE);
        initView();
        Intent intent = getIntent();
        Bundle bundleExtra = intent.getBundleExtra(Constant.activityBundleExtra);
        seriesId = String.valueOf(bundleExtra.getLong("seriesId"));
        seriesType = String.valueOf(bundleExtra.getInt("seriesType"));
        loadProductData();
    }

    private void loadProductData() {
        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
        js_request.put("projectId", App.projId);//根据实际需求添加相应键值对
        js_request.put("appId", App.appId);
        js_request.put("channelId", App.channelId);
        js_request.put("cibnUserId", App.userId);
        js_request.put("seriesId", seriesId);
        js_request.put("termId", App.publicTID);
        js_request.put("seriesType", seriesType);
        js_request.put("sessionId", App.sessionId);

        Log.i(TAG, "loadProductData: " + js_request.toString());
        RequestParams params = new RequestParams(App.bmsurl + VipPayUrlUtil.reqProductUrl);
        params.setAsJsonContent(true);
        params.setBodyContent(js_request.toString());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: " + result);
                VipProductPackageBean productData = JSON.parseObject(result, VipProductPackageBean.class);
                if (productData != null && productData.getDataList() != null
                        && productData.getDataList().size() > 0) {
                    productList = productData.getDataList();
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

    private void initView() {
        // title
        info_product_title = findViewById(R.id.info_product_title);
        // title标题
        tv_info_enter_product_title = (TextView) info_product_title.findViewById(R.id.tv_home_title_my);
        tv_info_enter_product_title.setText(Utils.getInterString(this, R.string.product_package_title));
        tv_info_enter_product_title.setVisibility(View.VISIBLE);
        // 返回
        tv_info_enter_product_back = (ImageView) info_product_title.findViewById(R.id.home_title_back);
        tv_info_enter_product_back.setVisibility(View.VISIBLE);
        tv_info_enter_product_back.setOnClickListener(this);
        // 产品包list
        lv_info_product = (ListView) findViewById(R.id.lv_info_product);
        infoEnterProductPackcgeAdapter = new InfoEnterProductPackcgeAdapter(this);
        lv_info_product.setAdapter(infoEnterProductPackcgeAdapter);
        lv_info_product.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_title_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, VipPriceActivity.class);
        intent.putExtra("productId", productList.get(position).getProductId());
        intent.putExtra("seriesId", seriesId);
        intent.putExtra("seriesType", seriesType);
        startActivity(intent);
    }

    private final int GET_DATA_SUCCESS = 2000;
    private final int GET_DATA_FAILU = 1000;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg != null) {
                switch (msg.what) {
                    case GET_DATA_SUCCESS:
                        infoEnterProductPackcgeAdapter.setData(productList);
                        break;
                    case GET_DATA_FAILU:

                        break;
                }
            }
            return false;
        }
    });

    @Override
    public void onEventMainThread(String event) {
        super.onEventMainThread(event);
        if (event.equals(Constant.PAY_SUCCESS2)) {
            finish();
        }
    }
}
