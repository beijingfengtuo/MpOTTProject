package cn.cibnmp.ott.ui.user.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.BaseUrl;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.glide.GlideDiskCache;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.ui.search.ToastUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.SharePrefUtils;
import cn.cibnmp.ott.utils.Utils;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.HostUpgradeManager;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.NormalDialog;
import de.greenrobot.event.EventBus;

/**
 * Created by cibn-lyc on 2018/1/8.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    // title布局
    private RelativeLayout my_title;
    private TextView titleText;
    private ImageButton titleImage;
    // 播放状态布局
    public RelativeLayout setting_item_rl3;
    private TextView settingItemText3;
    private ImageView settingItemButton3;
    //缓存布局
    public RelativeLayout setting_item_rl5;
    private TextView settingItemText5;
    private TextView settingItemName5;
    // 关于我们布局
    public RelativeLayout setting_item_rl6;
    private TextView settingItemText6;
    // 版本更新布局
    public RelativeLayout setting_item_rl9;
    private TextView settingItemText9;
    private TextView settingItemName9;
    // 退出按钮
    private TextView setting_item_rl8;

    private double cacheValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stting);
        initView();
    }

    // 初始化布局
    private void initView() {
        my_title = (RelativeLayout) findViewById(R.id.my_title);
        //title
        titleText = (TextView) my_title.findViewById(R.id.tv_home_title_my);
        titleText.setVisibility(View.VISIBLE);
        titleText.setText(Utils.getInterString(this, R.string.user_setting));
        //返回
        titleImage = (ImageButton) my_title.findViewById(R.id.home_title_back);
        titleImage.setVisibility(View.VISIBLE);

        //播放状态
        setting_item_rl3 = (RelativeLayout) findViewById(R.id.setting_item_rl3);
        settingItemText3 = (TextView) setting_item_rl3
                .findViewById(R.id.setting_item_text);
        settingItemButton3 = (ImageView) setting_item_rl3
                .findViewById(R.id.setting_item_button);
        //缓存
        setting_item_rl5 = (RelativeLayout) findViewById(R.id.setting_item_rl5);
        settingItemText5 = (TextView) setting_item_rl5
                .findViewById(R.id.setting_item_text);
        settingItemName5 = (TextView) setting_item_rl5
                .findViewById(R.id.setting_item_text2);

        // 将缓存大小写入
        requestComcaGetCache();
        // 关于我们
        setting_item_rl6 = (RelativeLayout) findViewById(R.id.setting_item_rl6);
        settingItemText6 = (TextView) setting_item_rl6
                .findViewById(R.id.setting_item_text);
        // 退出
        setting_item_rl8 = (TextView) findViewById(R.id.setting_item_rl8);
        if (App.isLogin) {
            setting_item_rl8.setVisibility(View.VISIBLE);
        } else {
            setting_item_rl8.setVisibility(View.GONE);
        }
        // 版本更新
        setting_item_rl9 = (RelativeLayout) findViewById(R.id.setting_item_rl9);
        settingItemText9 = (TextView) setting_item_rl9
                .findViewById(R.id.setting_item_text);
        settingItemName9 = (TextView) setting_item_rl9
                .findViewById(R.id.setting_item_text2);
        settingItemText9.setText(getResources().getString(
                R.string.version_update));
        settingItemName9.setText(Utils.getVersionName());

        setItemName();
        setOnClick();
        setBackground();
    }

    private void setItemName() {
        settingItemText3.setText(Utils.getInterString(this, R.string.no_wifi_load));
        settingItemText5.setText(Utils.getInterString(this, R.string.delect_cache));
        settingItemText6.setText(Utils.getInterString(this, R.string.about_me));
    }

    // 监听事件
    private void setOnClick() {
        titleImage.setOnClickListener(this);
        setting_item_rl5.setOnClickListener(this);
        setting_item_rl6.setOnClickListener(this);
        setting_item_rl8.setOnClickListener(this);
        setting_item_rl3.setOnClickListener(this);
        setting_item_rl9.setOnClickListener(this);
        settingItemButton3.setOnClickListener(this);
    }

    private void setBackground() {
        if (!SharePrefUtils.isContains(Constant.SETTING_CONFIG)) {
            SharePrefUtils.saveBoolean(Constant.SETTING_CONFIG, false);
        } else {
            boolean config = SharePrefUtils.getBoolean(Constant.SETTING_CONFIG,
                    true);
            if (config) {
                settingItemButton3.setBackgroundResource(R.mipmap.open);
            } else {
                settingItemButton3.setBackgroundResource(R.mipmap.close);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回
            case R.id.home_title_back:
                finish();
                break;
            // 播放状态
            case R.id.setting_item_button:
                // 改变wifi下播放状态，并保存状态
                boolean config = SharePrefUtils.getBoolean(Constant.SETTING_CONFIG,
                        true);
                if (!config) {
                    settingItemButton3.setBackgroundResource(R.drawable.open);
                    SharePrefUtils.saveBoolean(Constant.SETTING_CONFIG, true);
                } else {
                    settingItemButton3.setBackgroundResource(R.drawable.close);
                    SharePrefUtils.saveBoolean(Constant.SETTING_CONFIG, false);
                }
                break;
            // 缓存
            case R.id.setting_item_rl5:
                NormalDialog.showDialog(this, Utils.getInterString(this, R.string.cancle),
                        Utils.getInterString(this, R.string.user_sign_in_phone_yes),
                        Utils.getInterString(this, R.string.sure_delect_cache),
                        new NormalDialog.IClickOneListener() {

                            @Override
                            public void oneClick() {

                            }
                        },
                        new NormalDialog.IClickTwoListener() {
                            @Override
                            public void twoClick() {
                                if (cacheValue > 0) {
                                    clearCache();
                                }
                            }
                        });
                break;
            // 关于我们
            case R.id.setting_item_rl6:
                Intent intent = new Intent(this,
                        SettingAboutActivity.class);
                startActivity(intent);
                break;
            // 退出
            case R.id.setting_item_rl8:
                stopUser();
                break;
            // 版本更新
            case R.id.setting_item_rl9:
                // 检测版本升级
                int state = 0;
                state |= HostUpgradeManager.setting_upgrade_state;
                App.getHostUpgradeMaganer().checkUpgrade(state, SettingActivity.this);
                break;
            default:
                break;
        }
    }

    private void stopUser() {
        HttpRequest.getInstance().excute("userLogout",
                new Object[]{BaseUrl.utermUrl, new HttpResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        Lg.d("userLogout",
                                "userLogout---iiiiiiiii--------->>>>>"
                                        + response);
                        handler.sendEmptyMessage(USER_CLOSE);
                    }

                    @Override
                    public void onError(String error) {
                        Lg.e("userLogout---cccccccccc--------->>>>>"
                                + error);
                        handler.sendEmptyMessage(USER_ERROR);
                    }
                }});
    }

    private final int USER_CLOSE = 4444;
    private final int USER_ERROR = 4000;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case USER_CLOSE:
                    ToastUtils.show(SettingActivity.this, Utils.getInterString(SettingActivity.this, R.string.exit_success));
                    setting_item_rl8.setVisibility(View.GONE);
                    EventBus.getDefault().post(Constant.HOME_USER_SIGN_OUT);
                    break;
                case USER_ERROR:
                    ToastUtils.show(SettingActivity.this, Utils.getInterString(SettingActivity.this, R.string.exit_faile));
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 清除缓存
     **/
    private void clearCache() {
        GlideDiskCache.getInstance().clearDiskCache(
                new GlideDiskCache.GlideClearDiskCacheCallBack() {

                    @Override
                    public void finish() {
                        cacheValue = 0;
                        setCache();
                    }
                });
    }

    /**
     * 获取缓存大小
     **/
    private void requestComcaGetCache() {
        GlideDiskCache.getInstance().getDiskCacheSize(
                new GlideDiskCache.GlideDisCacheSizeCallBack() {

                    @Override
                    public void getSize(double d) {
                        cacheValue = d;
                        setCache();
                    }
                });
    }

    private void setCache() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                settingItemName5.setText(cacheValue + "M");
            }
        });
    }
}
