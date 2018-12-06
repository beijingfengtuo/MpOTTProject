package cn.cibnmp.ott.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.BlockContentBean;
import cn.cibnmp.ott.bean.ContentBean;
import cn.cibnmp.ott.config.CacheConfig;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.SimpleHttpResponseListener;
import cn.cibnmp.ott.ui.base.Action;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.utils.Utils;
import de.greenrobot.event.EventBus;

/**
 * Created by cibn-lyc on 2018/1/17.
 */

public class UserVipActivity extends BaseActivity implements View.OnClickListener{

    private final String TAG = "UserVipActivity";
    private View vip_title;
    // 会员中心
    private TextView tv_home_title_my;
    // 返回
    private ImageButton titleBack;
    // listView
    private ListView lv_vip_package;
    // 适配器
    private VipPackageAdapter vipPackageAdapter;
    // 数据集合
    private List<ContentBean> indexContents;
    private ImageView img_home_title_seach;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_vip_activity);
        EventBus.getDefault().post(Constant.USER_INTER_PRODUCT_PACKAGE);
        initView();
        loadListData();
    }

    private void loadListData() {
        HttpRequest.getInstance().excute("getBlockContent", App.epgUrl, App.hostEpgId, 0,
                CacheConfig.cache_no_cache, new SimpleHttpResponseListener<BlockContentBean>() {
                    @Override
                    public void onSuccess(BlockContentBean blockContent) {
                        if (blockContent != null && blockContent.getData() != null &&
                                blockContent.getData().getIndexContents() != null &&
                                blockContent.getData().getIndexContents().size() > 0) {
                            indexContents = blockContent.getData().getIndexContents();
                        }
                        Log.i(TAG, "onSuccess: " + indexContents.toString());
                        mHandler.sendEmptyMessage(GET_DATA_SUCCESS);
                    }

                    @Override
                    public void onError(String error) {
                        Log.i(TAG, "onError: " + error);
                    }
                });
    }

    private void initView() {
        // title
        vip_title =  findViewById(R.id.vip_title);
        //title（会员中心）
        tv_home_title_my = (TextView)vip_title.findViewById(R.id.tv_home_title_my);
        tv_home_title_my.setVisibility(View.VISIBLE);
        tv_home_title_my.setText(Utils.getInterString(this, R.string.user_vip_center));

        // 返回
        titleBack = (ImageButton)vip_title.findViewById(R.id.home_title_back);
        titleBack.setVisibility(View.VISIBLE);
        titleBack.setOnClickListener(this);

        // 详情进入产品包入口
        img_home_title_seach = (ImageView) vip_title.findViewById(R.id.img_home_title_history);
        img_home_title_seach.setVisibility(View.GONE);
        img_home_title_seach.setOnClickListener(this);
        // listView
        lv_vip_package = (ListView) findViewById(R.id.lv_vip_package);
        vipPackageAdapter = new VipPackageAdapter(UserVipActivity.this);
        lv_vip_package.setAdapter(vipPackageAdapter);
        lv_vip_package.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserVipActivity.this, VipPriceActivity.class);
                intent.putExtra("productId", indexContents.get(position).getContentId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 返回
            case R.id.home_title_back:
                finish();
                break;
            case R.id.img_home_title_history:
                startActivity(Action.getActionName(Action.OPEN_PRODUCT_PACKAGE_PAGE),
                        new Bundle());
                break;
        }
    }

    private final int GET_DATA_SUCCESS = 2000;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_DATA_SUCCESS:
                    vipPackageAdapter.setData(indexContents);
                    break;
            }
        }
    };

    @Override
    public void onEventMainThread(String event) {
        super.onEventMainThread(event);
        if (event.equals(Constant.PAY_SUCCESS1)){
            finish();
        }
    }
}
