package cn.cibnmp.ott.ui.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.Glide;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import cn.cibnmp.ott.App;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.detail.DetailActivity;
import cn.cibnmp.ott.ui.detail.LiveDetailActivity;
import cn.cibnmp.ott.ui.home.JsWebViewActivity;
import cn.cibnmp.ott.ui.screening.ScreeningActivity;
import cn.cibnmp.ott.ui.search.SearchActivity;
import cn.cibnmp.ott.ui.search.ToastUtils;
import cn.cibnmp.ott.ui.user.InfoEnterProductPackcgeActivity;
import cn.cibnmp.ott.ui.user.UserAndColActivity;
import cn.cibnmp.ott.ui.user.UserVipActivity;
import cn.cibnmp.ott.ui.user.VouchersActivity;
import cn.cibnmp.ott.ui.user.login.UserSignInActivity;
import cn.cibnmp.ott.ui.user.setting.FeedbackActivity;
import cn.cibnmp.ott.ui.user.setting.SettingActivity;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.Util;
import de.greenrobot.event.EventBus;

public abstract class BaseActivity extends FragmentActivity {
    private final String TAG = "BaseActivity";
    private String epgId = "0";
    public String epgIdParam;
    public String p1Param;
    public String p2Param;
    public String p3Param;
    public String actionName;
    public String actionUrlParam;
    public String contentIdParam;
    private Bundle bundle;
    public String sid;


    public void startActivity(String actionName, Bundle bundle) {
        Action action = Action.createAction(actionName);
        this.bundle = bundle;
        Intent intent = getIntentByAction(action);
        if (intent == null) {
            Lg.e(TAG, "action is invalid.");
            return;
        }
        intent.putExtra(Constant.activityBundleExtra, bundle);
        startActivity(intent);
    }

    private Intent getIntentByAction(Action action) {
        Intent intent = null;
        switch (action) {
            case OPEN_STTING: // 首页
                intent = new Intent(this, SettingActivity.class);
                break;
            case OPEN_COLLECTION: // 我的收藏
                intent = new Intent(this, UserAndColActivity.class);
                break;
            case OPEN_SEARCH: // 搜索
                intent = new Intent(this, SearchActivity.class);
                break;
            case OPEN_VIP: // VIP
                intent = new Intent(this, UserVipActivity.class);
                break;
            case OPEN_FEEDBACK:// 电视剧详情页1
                intent = new Intent(this, FeedbackActivity.class);
                break;
            case OPEN_USER_VOUCHERS: // 代金券页面
                intent = new Intent(this, VouchersActivity.class);
                break;
            case OPEN_USER_SIGN_IN: // 登录页面
                intent = new Intent(this, UserSignInActivity.class);
                break;
//            case USER_REGISTER: // 用户注册页面
//                intent = new Intent(context, UserSignInRegisterActivity.class);
//                break;
//            case OPEN_MOBILE_MORE_VERTICAL: // 电视剧电影类的activity
//                intent = new Intent(context, TVPlayActivity.class);
//                break;
//            case OPEN_MOBILE_MORE_HORIZONTAL: // 综艺类的activity
//                intent = new Intent(context, VarietyActivity.class);
//                break;
//            case OPEN_PERSON_DETAIL_PAGE: // 人物详情页
//                intent = new Intent(context, ActorDetailActivity.class);
//                break;
//            // case OPEN_DETAIL: // 详情页
            case OPEN_NORMAL_DETAIL_PAGE:// 通用详情页（电影）0
                intent = new Intent(this, DetailActivity.class);
                bundle.putInt("detail_type", 1);
                bundle.putInt("detail_ty", 1);
                break;
//            case OPEN_MOVIE_DETAIL_PAGE:// 电影详情页0
//                intent = new Intent(context, DetailActivity.class);
//                bundle.putInt("detail_type", 0);
//                break;
//            case OPEN_VARIETY_DETAIL_PAGE:// 综艺详情页2
//                intent = new Intent(context, DetailActivity.class);
//                bundle.putInt("detail_type", 2);
//                break;
//            case OPEN_TV_DETAIL_PAGE:// 电视剧详情页1
//                intent = new Intent(context, DetailActivity.class);
//                bundle.putInt("detail_type", 1);
//                break;
//            case OPEN_NORMAL_CAROUSEL_PLAYER:// 打开轮播播放器
//                intent = new Intent(context, CarouselAct11.class);
//                break;
            case OPEN_LIVE_DETAIL_PAGE:// 打开直播播放器
                intent = new Intent(this, LiveDetailActivity.class);
                bundle.putInt("detail_type", 4);
                bundle.putInt("detail_ty", 4);
                break;
//            case OPEN_BLE_GUIDE:// 打开蓝牙连接引导页
//                intent = new Intent(context, BLEGuideActivity.class);
//                break;
//            case OPEN_MY_BEADS:// 打开我的佛珠
//                intent = new Intent(context, MyBeadsActivity.class);
//                break;
            case OPEN_NORMAL_LIST_PAGE:// 打开筛选页面
                intent = new Intent(this, ScreeningActivity.class);
                break;
            case OPEN_PRODUCT_PACKAGE_PAGE: // 详情页进入产品包页面
                intent = new Intent(this, InfoEnterProductPackcgeActivity.class);
                break;
            case OPEN_NORMAL_H5_PAGE: // 首页打开H5页面
                intent = new Intent(this, JsWebViewActivity.class);
                break;
            default:
                break;
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 添加Activity到堆栈

        EventBus.getDefault().register(this);
        App.getAppManager().addActivity(this);
        iniParams();
        Glide.get(this).clearMemory();

    }

    @Override
    public void finish() {
        super.finish();
        // 结束Activity&从堆栈中移除
        App.getAppManager().removeActivity(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        System.gc();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    private final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }
        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }
        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }
        if (view != null) return view;
        return super.onCreateView(name, context, attrs);
    }

    public String getEpgId() {
        return epgId;
    }

    public void setEpgId(String epgId) {
        this.epgId = epgId;
    }

    // 背景图片的刷新方法
    public void onEventMainThread(String event) {

    }

    public void startActivity(String actionName, String... params) {
        try {

            Lg.d(TAG, "startActivity actionName --> " + actionName);

            Action action = Action.createAction(actionName);
            if (bundle == null) {
                bundle = new Bundle();
            }
            Intent intent = getIntentByAction(action);
            if (intent == null) {
                Lg.e(TAG, "action is invalid.");
                return;
            }
            intent.putExtra(Constant.activityBundleExtra, bundle);
            if (params != null && params.length > 0) {
                String[] ms;
                for (String s : params) {
                    if (!TextUtils.isEmpty(s) && s.contains("=")) {
                        System.out.println(s);
                        ms = s.split("=", -1);
                        try {
                            intent.putExtra(ms[0], ms[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            startActivity(intent);

        } catch (Throwable e) {
            e.printStackTrace();
            ToastUtils.show(this, "暂不支持此功能 !");
        }

    }

    /**
     * 初始化界面接收的参数
     */
    public void iniParams() {

        try {
            Intent intent = getIntent();
            if (intent != null) {
                Bundle bundle = intent.getBundleExtra(Constant.activityBundleExtra);

                if (bundle == null) {

                    // home_launch = Util.getHomeLaunch2(intent);

                    epgIdParam = Util.getEpgId2(intent);

                    actionName = Util.getAction2(intent);

                    actionUrlParam = Util.getActionUrl2(intent);

                    contentIdParam = Util.getContentId2(intent);

                    p1Param = Util.getActionParamP1_2(intent);

                    p2Param = Util.getActionParamP2_2(intent);

                    p3Param = Util.getActionParamP3_2(intent);

                    sid = Util.getSid2(intent);

                    Lg.i("BaseActivity1", actionName + "...." + contentIdParam + "...." + epgIdParam);
                } else {

                    epgIdParam = Util.getEpgId2(intent);

                    actionName = Util.getAction2(intent);

                    actionUrlParam = Util.getActionUrl2(intent);

                    contentIdParam = Util.getContentId2(intent);

                    p1Param = Util.getActionParamP1_2(intent);

                    p2Param = Util.getActionParamP2_2(intent);

                    p3Param = Util.getActionParamP3_2(intent);

                    sid = Util.getSid2(intent);
                    Lg.i("BaseActivity2", actionName + "...." + contentIdParam + "...." + epgIdParam);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iniParams(Intent intent) {

        try {
            //  Intent intent = getIntent();
            if (intent != null) {
                Bundle bundle = intent.getBundleExtra(Constant.activityBundleExtra);

                if (bundle == null) {

                    // home_launch = Util.getHomeLaunch2(intent);

                    epgIdParam = Util.getEpgId2(intent);

                    actionName = Util.getAction2(intent);

                    actionUrlParam = Util.getActionUrl2(intent);

                    contentIdParam = Util.getContentId2(intent);

                    p1Param = Util.getActionParamP1_2(intent);

                    p2Param = Util.getActionParamP2_2(intent);

                    p3Param = Util.getActionParamP3_2(intent);

                    sid = Util.getSid2(intent);

                    Lg.i("BaseActivity1", actionName + "...." + contentIdParam + "...." + epgIdParam);
                } else {

                    epgIdParam = Util.getEpgId2(intent);

                    actionName = Util.getAction2(intent);

                    actionUrlParam = Util.getActionUrl2(intent);

                    contentIdParam = Util.getContentId2(intent);

                    p1Param = Util.getActionParamP1_2(intent);

                    p2Param = Util.getActionParamP2_2(intent);

                    p3Param = Util.getActionParamP3_2(intent);

                    sid = Util.getSid2(intent);
                    Lg.i("BaseActivity2", actionName + "...." + contentIdParam + "...." + epgIdParam);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        iniParams(intent);
        super.onNewIntent(intent);

        // Math.hypot()
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Lg.i(TAG, "-------onConfigurationChanged---------");
        if (newConfig.fontScale != 1) {
            getResources();
        }
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

}
