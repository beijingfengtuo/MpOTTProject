package cn.cibnmp.ott.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.tauth.Tencent;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.BaseUrl;
import cn.cibnmp.ott.bean.AboutUsDataBean;
import cn.cibnmp.ott.bean.DeviceAuthenBean;
import cn.cibnmp.ott.bean.DeviceAuthenResultBean;
import cn.cibnmp.ott.bean.EntryDataBean;
import cn.cibnmp.ott.bean.EntryResultBean;
import cn.cibnmp.ott.bean.NavigationItemDataBean;
import cn.cibnmp.ott.bean.NavigationResultBean;
import cn.cibnmp.ott.bean.WelcomeBean;
import cn.cibnmp.ott.config.CacheConfig;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.config.MyJurisdictionUtils;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.jni.JNIInterface;
import cn.cibnmp.ott.jni.JNIRequest;
import cn.cibnmp.ott.jni.ResponseCode;
import cn.cibnmp.ott.jni.SimpleHttpResponseListener;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.ui.base.BaseFragment;
import cn.cibnmp.ott.ui.detail.bean.ReserveBean;
import cn.cibnmp.ott.ui.detail.content.DbQueryListener;
import cn.cibnmp.ott.ui.detail.content.UserReserveHelper;
import cn.cibnmp.ott.ui.detail.presenter.LiveManager;
import cn.cibnmp.ott.ui.home.HomeOneFragment;
import cn.cibnmp.ott.ui.home.HomeThreeFragment;
import cn.cibnmp.ott.ui.home.HomeTwoFragment;
import cn.cibnmp.ott.ui.home.HomefourFragment;
import cn.cibnmp.ott.ui.search.ToastUtils;
import cn.cibnmp.ott.ui.share.QQShareListener;
import cn.cibnmp.ott.utils.DeviceUtils;
import cn.cibnmp.ott.utils.IPv4Utils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.NetWorkUtils;
import cn.cibnmp.ott.utils.SharePrefUtils;
import cn.cibnmp.ott.utils.Utils;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.AppDisableDialog;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.FixedSpeedScroller;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.HomeKeyEventBroadCastReceiver;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.HostUpgradeManager;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.InternetType;
import de.greenrobot.event.EventBus;

/**
 * Created by yanwenwu on 2018/3/1.
 */
public class HomeActivity extends BaseActivity implements IWeiboHandler.Response, OnClickListener, OnPageChangeListener {

    //退出应用时间
    private long mExitTime;
    // 首页推荐
    @ViewInject(R.id.home_btn1)
    private ImageButton home_btn1;
    // 转播
    @ViewInject(R.id.home_btn2)
    private ImageButton home_btn2;
    // 玩票
    @ViewInject(R.id.home_btn3)
    private ImageButton home_btn3;
    // 我的（用户）
    @ViewInject(R.id.home_btn4)
    private ImageButton home_btn4;
    //viewpager
    @ViewInject(R.id.content)
    private ViewPager vPager;
    private FragAdapter fAdapter;
    // 首页推荐
    private HomeOneFragment mFragment1;
    // 转播
    private BaseFragment mFragment2;
    // 玩票
    private BaseFragment mFragment3;
    // 我的（用户）
    private BaseFragment mFragment4;
    //小窗口播放
    private RelativeLayout layoutSmall;
    private FrameLayout layoutSmallVideo;
    private ImageView btnClose, home_error_img;
    //全屏窗口
    private RelativeLayout layoutAll;
    private FrameLayout layoutFullVideo;
    private LinearLayout mHomeTitle;
    private ImageView mHomeImageview;
    private WelcomeBean mWelcomeBean;
    private TextView mHomeTextView;
    private EntryDataBean entryDataBean;
    private RelativeLayout mHomeTimeJump;
    private final int APP_UPDATA = 3333;
    private final int LOAD_SUCCESS = 6666;
    private final int ADD_ANIMATION = 7777;
    private final int HOME_IMAGEOK = 8888;
    private final int TIME_COUNT = 1111;
    private final int TIME_ONERROR = 5555;
    private final int TIME_JUMP = 9999;
    private final int WELCOME_BG = 2222;
    private final int TIME_SOLASHINFO = 1234;
    private final static int DISABLE_USE = 444;  //禁用
    //0代表初始进入，1代表进入锁屏，2代表重新解锁
    private int screenTag = 0;
    private int mTiemCount = 0; //开屏倒计时
    private final String TAG = "HomeActivity";
    private int preNetType = -1; // 0代表有线，1代表无线
    private IntentFilter filter = new IntentFilter();
    private boolean isAppDisabled = false;  //应用是否被禁用
    private NavigationItemDataBean mCurItemDataBean = null;
    private List<Fragment> layouts = new ArrayList<Fragment>();
    //    private ImageView img_home1, img_home2, img_home3, img_home4;
    private RelativeLayout home_rl1, home_rl2, home_rl3, home_rl4;
    private List<ImageButton> mImageButtons = new ArrayList<ImageButton>();
    private HomeKeyEventBroadCastReceiver receiver = new HomeKeyEventBroadCastReceiver();
    private Integer[] drawableSe = {R.mipmap.home1, R.mipmap.list1, R.mipmap.vip1, R.mipmap.my1};

    //首页底部菜单选项卡索引（0首页、1转播、2玩票、3我的）
    private int pageIndex;
    private boolean isShow = false;
    private boolean isFirstIn;
    private SharedPreferences mSharedPreferences;
    private boolean isDeviceAuth = true;
    private RelativeLayout mHomeNonetwork, home_error_nonetwork;
    private RelativeLayout mHomeLoodingRl;
    private ImageView mHomeLoodingImag;
    private RotateAnimation mAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //判断.0系统权限是否打开
        boolean isQX = MyJurisdictionUtils.myJurisdictionUtils().isJurisdiction(this);
        if (!isQX) {
            isShow = true;
            return;
        }
        initSetUi();
        deviceAuth();
        startTimerServer();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MyJurisdictionUtils.myJurisdictionUtils().REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //调用系统提示
                        MyJurisdictionUtils.myJurisdictionUtils().showMessageOKCancel(this);
                        return;
                    }
                }
                App.startApp();
                initSetUi();
                deviceAuth();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 初始化ui
     */
    private void initSetUi() {

        //小窗口布局
        layoutSmall = (RelativeLayout) findViewById(R.id.rl_home_small_view);
        layoutSmallVideo = (FrameLayout) findViewById(R.id.fl_home_small_video);
        btnClose = (ImageView) findViewById(R.id.img_home_small_close);
        //全屏布局
        layoutAll = (RelativeLayout) findViewById(R.id.home_all);
        layoutFullVideo = (FrameLayout) findViewById(R.id.fl_home_full_video);
        mHomeImageview = (ImageView) findViewById(R.id.welcome_imageview);
        mHomeTitle = (LinearLayout) findViewById(R.id.home_title);
        mHomeNonetwork = (RelativeLayout) findViewById(R.id.home_nonetwork);
        mHomeTextView = (TextView) findViewById(R.id.welcome_time);
        mHomeTimeJump = (RelativeLayout) findViewById(R.id.home_time_jump);
        home_error_nonetwork = (RelativeLayout) findViewById(R.id.home_error_nonetwork);
        home_error_img = (ImageView) findViewById(R.id.home_error_img);

        //进度条布局
        mHomeLoodingRl = (RelativeLayout) findViewById(R.id.home_looding_rl);
        mHomeLoodingImag = (ImageView) findViewById(R.id.home_looding_imag);
        mAnimation = (RotateAnimation) AnimationUtils.loadAnimation(HomeActivity.this, R.anim.rotating);
        LinearInterpolator lir = new LinearInterpolator();
        mAnimation.setInterpolator(lir);

        btnClose.setOnClickListener(this);
        layoutSmall.setOnClickListener(this);
        mHomeTextView.setOnClickListener(this);
        home_error_img.setOnClickListener(this);
        HttpRequest.getInstance().excute("userLoginSet", App.userId);
        // 注入view和事件
        x.view().inject(this);
        //每次应用重启时，清除用户对于升级的选择
        SharePrefUtils.removePreferences(this, HostUpgradeManager.user_option_time);
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(homeInternetReceiver, filter);
        registerReceiver(upgrqadeTimeReceiver, filter);
//        registerReceiver(receiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    @Override
    public void onEventMainThread(String event) {
        super.onEventMainThread(event);
        if (event.equals(Constant.HOME_USER_SIGN_IN)) {
            mFragment4.onEventMainThread(Constant.HOME_USER_SIGN_IN);
        }
    }

    private void setSpreadImg() {
        stopLooding();
        //判断是否是第一次进入
        mSharedPreferences = getSharedPreferences("data" + Utils.getVersionName(), MODE_PRIVATE);
        isFirstIn = mSharedPreferences.getBoolean("isFirstInHome", true);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        //若为true，则是第一次进入
        if (isFirstIn) {
            editor.putBoolean("isFirstInHome", false);
            mHomeTitle.setVisibility(View.VISIBLE);
            mHomeImageview.setVisibility(View.INVISIBLE);
            //初始化UI
            setUI();
            //请求首页数据
            requestHomeNav();
        } else {
            boolean isSdCardExist = Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED);
            // 判断sdcard是否存在
            if (isSdCardExist) {
                //获取sdcard的根路径
                String sdpath = Environment.getExternalStorageDirectory().getAbsolutePath();
                String filepath = sdpath + File.separator + "homebg.jpg";
                File file = new File(filepath);
                if (file.exists()) {
                    Bitmap bm = BitmapFactory.decodeFile(filepath);
                    mHomeImageview.setVisibility(View.VISIBLE);
                    mHomeTitle.setVisibility(View.INVISIBLE);
                    mHomeTimeJump.setVisibility(View.VISIBLE);
                    mHomeTextView.setVisibility(View.VISIBLE);
                    // 将图片显示到ImageView中
                    mHomeImageview.setImageBitmap(bm);
                    if (mWelcomeBean != null && mWelcomeBean.getData() != null && mWelcomeBean.getData().get(0).getDuration() > 0) {
                        mTiemCount = mWelcomeBean.getData().get(0).getDuration();
                        handler.sendEmptyMessage(TIME_COUNT);
                    } else {
                        startHomeActivity();
                    }
                } else {
                    startHomeActivity();
                }
            } else {
                startHomeActivity();
            }
        }
        editor.commit();
    }

    private void startHomeActivity() {
        mHomeImageview.setVisibility(View.INVISIBLE);
        mHomeTimeJump.setVisibility(View.INVISIBLE);
        mHomeTitle.setVisibility(View.VISIBLE);
        //初始化UI
        setUI();
        //请求首页数据
        requestHomeNav();
    }

    /**
     * 初始化UI
     */
    private void setUI() {
        home_rl1 = (RelativeLayout) findViewById(R.id.home_rl1);
        home_rl2 = (RelativeLayout) findViewById(R.id.home_rl2);
        home_rl3 = (RelativeLayout) findViewById(R.id.home_rl3);
        home_rl4 = (RelativeLayout) findViewById(R.id.home_rl4);
        home_rl1.setOnClickListener(this);
        home_rl2.setOnClickListener(this);
        home_rl3.setOnClickListener(this);
        home_rl4.setOnClickListener(this);
//        img_home1 = (ImageView) findViewById(R.id.img_home1);
//        img_home2 = (ImageView) findViewById(R.id.img_home2);
//        img_home3 = (ImageView) findViewById(R.id.img_home3);
//        img_home4 = (ImageView) findViewById(R.id.img_home4);
        mImageButtons.add(home_btn1);
        mImageButtons.add(home_btn2);
        mImageButtons.add(home_btn3);
        mImageButtons.add(home_btn4);
        mFragment1 = new HomeOneFragment();
        layouts.add(mFragment1);
        mFragment2 = new HomeTwoFragment();
        layouts.add(mFragment2);
        mFragment3 = new HomeThreeFragment();
        layouts.add(mFragment3);
        mFragment4 = new HomefourFragment();
        layouts.add(mFragment4);
        initView();
        setScroller();
    }

    private void setScroller() {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(vPager.getContext(), new AccelerateInterpolator(1.5f));
            field.set(vPager, scroller);
            scroller.setmDuration(0);
        } catch (Exception e) {
            Lg.e("RankingListAct", e.toString());
        }
    }

    /**
     * 接口认证
     */
    private void deviceAuth() {
        startLooding();
        final DeviceAuthenBean authenBean = new DeviceAuthenBean();
        String json = JSON.toJSONString(authenBean);
        HttpRequest.getInstance().excute("deviceAuth", new Object[]{BaseUrl.utermUrl,
                json, new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                isDeviceAuth = true;
                //认证接口，只有在认证有返回结果，并且code是禁用情况下才不让用户使用!
                if (TextUtils.isEmpty(response)) {
                    Lg.i(TAG, "deviceAuth onSucess --> response is null or empty !!!");
                    getEntryUrl();
                    return;
                }
                Lg.i(TAG, "deviceAuth onSuccess --> " + response);
                DeviceAuthenResultBean authenResultBean = null;
                try {
                    authenResultBean = JSON.parseObject(response, DeviceAuthenResultBean.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (authenResultBean != null && authenResultBean.getCode() == ResponseCode.uterm_disable_code) {
                    Lg.i(TAG, "deviceAuth onSuccess--> disable status !");
                    Message message = Message.obtain();
                    message.what = DISABLE_USE;
                    if (authenResultBean.getData() != null)
                        message.obj = authenResultBean.getData().getDisableUrl();
                    handler.sendMessage(message);
                    isAppDisabled = true;
                    return;
                } else {
                    getEntryUrl();
                    if (authenResultBean != null && authenResultBean.getData() != null) {
                        App.publicTID = authenResultBean.getData().getTid();
                    }
                }
            }

            @Override
            public void onError(String error) {
                Lg.i(TAG, "deviceAuth onError , " + error == null ? "" : error);
                isDeviceAuth = false;
                getEntryUrl();
            }
        }});
    }

    /**
     * 认证后获取url
     */
    private void getEntryUrl() {
        HttpRequest.getInstance().excute("getEntryUrl", new Object[]{BaseUrl.entryUrl,
                new HttpResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        Lg.i(TAG, "getEntryUrl onSuccess --> " + response);
                        EntryResultBean entryResultBean = JSON.parseObject(response, EntryResultBean.class);
                        entryDataBean = entryResultBean.getData();
                        iniEntryUrl();
                    }

                    @Override
                    public void onError(String error) {
                        Lg.i(TAG, "getEntryUrl onError , " + error == null ? "" : error);
                        handler.sendEmptyMessage(TIME_ONERROR);
                    }
                }});
    }

    //数据初始化操作
    private void iniEntryUrl() {
        iniUrls();
        App.mCache.put(Constant.epg_entry_param, JSON.toJSONString(entryDataBean));
        //请求开机图片数据
        requestSplash();
        //初始化默认的解码方式
        iniDefaultMediaDecode();
        App.timeDvalue = JNIRequest.getInstance().getServeTime();
    }

    /**
     * 请求开机图片
     */
    private void requestSplash() {
        HttpRequest.getInstance().excute("getSplashInfo", new Object[]{BaseUrl.utermUrl,
                new HttpResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        Lg.i(TAG, "getSplashInfo response++++++++" + response);
                        mWelcomeBean = JSON.parseObject(response, WelcomeBean.class);

                        if (mWelcomeBean.getCode() == 404){
                            handler.sendEmptyMessage(HOME_IMAGEOK);
                            return;
                        }

                        if (mWelcomeBean != null && mWelcomeBean.getData() != null) {
                            Message message = new Message();
                            message.obj = mWelcomeBean.getData().get(0).getUrl();
                            message.what = WELCOME_BG;//标志是哪个线程传数
                            handler.sendMessage(message);//发送message信息
                        } else {
                            handler.sendEmptyMessage(HOME_IMAGEOK);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Lg.i(TAG, "getSplashInfo error++++++++" + error.toString());
                        handler.sendEmptyMessage(HOME_IMAGEOK);
                    }
                }});
    }

    /**
     * url赋值
     */
    public void iniUrls() {
        App.epgUrl = Utils.getValidHttpUrl(entryDataBean.getEpgurl(), "http://api.epg2.cibn.cc");
        App.cdnUrl = Utils.getValidHttpUrl(entryDataBean.getCdn(), "http://cdn.cibn.cc");
        App.cdnPicUrl = Utils.getValidHttpUrl2(entryDataBean.getCdnpic(),
                App.cdnUrl, "http://img.cdn.cibn.cc");
        App.detailWebViewUrl = Utils.getValidHttpUrl(entryDataBean.getDetailWebViewUrl(), "http://api.bigme.cibn.cc");
        App.wxBindUrl = Utils.getValidHttpUrl(entryDataBean.getWxBind(), "http://user.uterm.cibn.cc/Login/Index/index");
        App.bmsurl = Utils.getValidHttpUrl(entryDataBean.getBmsurl(), "http://nhdapi.boss.cibn.cc/cibn3api_02");
        App.carouselUrl = Utils.getValidHttpUrl(entryDataBean.getCarouselUrl(), "http://api.epg2.cibn.cc");
        if (!TextUtils.isEmpty(entryDataBean.getEpgid())) {
            setEpgId(entryDataBean.getEpgid());
            App.hostEpgId = getEpgId();
        } else {
            setEpgId("0");
            App.hostEpgId = "0";
        }
    }

    private void iniDefaultMediaDecode() {
        String local = App.mCache.getAsString(Constant.about_us_config);
        if (!TextUtils.isEmpty(local)) {
            try {
                AboutUsDataBean aboutUsDataBean = JSON.parseObject(local, AboutUsDataBean.class);
                if (aboutUsDataBean != null && !TextUtils.isEmpty(aboutUsDataBean.getSoftDecodeDevice())) {
                    String[] devices = aboutUsDataBean.getSoftDecodeDevice().split(";");
                    String model = DeviceUtils.getDeviceModel();
                    if (devices != null) {
                        for (String d : devices) {
                            if (model.equalsIgnoreCase(d)) {
                                App.defaultMediaDecode = false;  //当前设备默认解码方式需是软解
                                //如果默认使用软解，并且用户没有设置过值，就默认用户设置的是软解
                                if (SharePrefUtils.getInt(Constant.videoSwich, -1) == -1) {
                                    SharePrefUtils.saveInt(Constant.videoSwich, 2);
                                }
                                return;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                App.mCache.remove(Constant.about_us_config);
            }
        }
    }

    /**
     * 请求导航数据
     */
    public void requestHomeNav() {
        requestHomeNavigationList(CacheConfig.cache_half_hour);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle bundle = new Bundle();
            switch (msg.what) {
                case ADD_ANIMATION:
                    setSpreadImg();
                    break;
                case TIME_SOLASHINFO:
                    stopLooding();
                    mHomeImageview.setVisibility(View.INVISIBLE);
                    mHomeTimeJump.setVisibility(View.INVISIBLE);
                    mHomeTitle.setVisibility(View.VISIBLE);
                    home_error_nonetwork.setVisibility(View.VISIBLE);
                    break;
                case TIME_ONERROR:
                    stopLooding();
                    mHomeImageview.setVisibility(View.INVISIBLE);
                    mHomeTimeJump.setVisibility(View.INVISIBLE);
                    mHomeTitle.setVisibility(View.INVISIBLE);
                    vPager.setVisibility(View.INVISIBLE);
                    mHomeNonetwork.setVisibility(View.VISIBLE);
                    break;

                case LOAD_SUCCESS:
                    stopLooding();
                    if (home_error_nonetwork.getVisibility() == View.VISIBLE) {
                        home_error_nonetwork.setVisibility(View.INVISIBLE);
                    }
                    bundle.putSerializable(Constant.CURITEMDATABEAN, mCurItemDataBean);
                    mFragment1.setData(bundle);
                    handler.sendEmptyMessageDelayed(APP_UPDATA, 3000);
                    break;

                case HOME_IMAGEOK:
                    mHomeImageview.setVisibility(View.INVISIBLE);
                    mHomeTextView.setVisibility(View.INVISIBLE);
                    mHomeTimeJump.setVisibility(View.INVISIBLE);
                    mHomeTitle.setVisibility(View.VISIBLE);
                    //初始化UI
                    setUI();
                    //请求首页数据
                    requestHomeNav();
                    break;

                case TIME_COUNT:
                    if (mTiemCount == 0) {
                        handler.sendEmptyMessage(HOME_IMAGEOK);
                    } else {
                        mTiemCount -= 1;
                        mHomeTextView.setText(mTiemCount + "  " + getString(R.string.welcome_time));
                        handler.sendEmptyMessageDelayed(TIME_COUNT, 1000);
                    }
                    break;

                case DISABLE_USE:
                    if (msg.obj == null) {
                        new AppDisableDialog.Builder(HomeActivity.this).create().show();
                    } else {
                        new AppDisableDialog.Builder((String) msg.obj, HomeActivity.this).create().show();
                    }
                    break;

                case TIME_JUMP:
                    handler.removeMessages(TIME_COUNT);
                    handler.sendEmptyMessage(HOME_IMAGEOK);
                    break;

                case APP_UPDATA:
                    int state = 0;
                    state |= HostUpgradeManager.boot_upgreade_state;
                    if (pageIndex == 0) {
                        App.getHostUpgradeMaganer().checkUpgrade(state, HomeActivity.this);
                    }
                    break;
                case 10019:
                    //添加预约提醒
                    LiveManager.getLiveManager().startLiveThread();
                    break;

                case WELCOME_BG:
                    String url = (String) msg.obj;
                    Glide.with(HomeActivity.this).load(url).downloadOnly(new SimpleTarget<File>() {
                        @Override
                        public void onResourceReady(File file, GlideAnimation<? super File> glideAnimation) {
                            Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
                            if (bitmap != null) {
                                savePicture(bitmap);//保存图片到SD卡
                                handler.sendEmptyMessage(ADD_ANIMATION);
                            }
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            handler.sendEmptyMessage(HOME_IMAGEOK);
                        }
                    });
                    break;
            }
            return false;
        }
    });

    /**
     * 请求首页导航数据
     */
    public void requestHomeNavigationList(int cachetime) {
        HttpRequest.getInstance().excute("getHomeNavigationList", new Object[]{App.epgUrl,
                getEpgId(), cachetime, new SimpleHttpResponseListener<NavigationResultBean>() {
            @Override
            public void onSuccess(NavigationResultBean response) {
                if (response != null && response.getData() != null && response.getData().getContent() != null) {
                    mCurItemDataBean = response.getData();
                    handler.sendEmptyMessage(LOAD_SUCCESS);
                } else {
                    Lg.i(TAG, "NavigationResultBean response" + response);
                    handler.sendEmptyMessage(TIME_SOLASHINFO);
                }
            }

            @Override
            public void onError(String error) {
                Lg.i(TAG, "getHomeNavigationList onError" + error == null ? "" : error);
                handler.sendEmptyMessage(TIME_SOLASHINFO);
            }
        }});
    }

    public void initView() {
        fAdapter = new FragAdapter(getSupportFragmentManager());
        vPager.setAdapter(fAdapter);
        vPager.setOffscreenPageLimit(layouts.size());
        vPager.setOnPageChangeListener(this);
        vPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.home_rl1:
                if (mCurItemDataBean == null) {
                    return;
                }
                vPager.setCurrentItem(0);
                getTabSelectIndex(0);
//                setSelectImg(R.id.home_rl1);
                updateButtonBG(0);
                break;
            case R.id.home_rl2:
                if (mCurItemDataBean == null) {
                    return;
                }
                vPager.setCurrentItem(1);
                getTabSelectIndex(1);
//                setSelectImg(R.id.home_rl2);
                updateButtonBG(1);
                if (mFragment2 != null) {
                    bundle.putString(Constant.NAVMOBILEEPG2, entryDataBean.getNavMobileEpg2());
                    mFragment2.setData(bundle);
                }
                break;
            case R.id.home_rl3:
                if (mCurItemDataBean == null) {
                    return;
                }
                vPager.setCurrentItem(2);
                getTabSelectIndex(2);
//                setSelectImg(R.id.home_rl3);
                updateButtonBG(2);
                if (mFragment3 != null) {
                    bundle.putString(Constant.NAVMOBILEEPG, entryDataBean.getNavMobileEpg());
                    mFragment3.setData(bundle);
                }
                break;
            case R.id.home_rl4:
                if (mCurItemDataBean == null) {
                    return;
                }
                vPager.setCurrentItem(3);
                getTabSelectIndex(3);
//                setSelectImg(R.id.home_rl4);
                updateButtonBG(3);
                EventBus.getDefault().post(Constant.PUSH_PLAY_HISTORY);
                break;
            case R.id.welcome_time:
                handler.sendEmptyMessage(TIME_JUMP);
                break;
            case R.id.home_error_img:
                //请求首页数据
                requestHomeNav();
            default:
                break;
        }
    }

    /**
     * 获取当前选中的选项卡
     *
     * @param position 底部菜单索引
     */
    private void getTabSelectIndex(int position) {
        //如果当前选中的选项卡是玩票菜单，关闭小视屏播放器
        if (this.pageIndex == 2 && position != 2 && mFragment3 != null) {
            //小视屏播放结束处理
            ((HomeThreeFragment) mFragment3).getCompletion();
        }
        this.pageIndex = position;
    }

//    private void setSelectImg(int id) {
//        switch (id) {
//            case R.id.home_rl1:
//                img_home1.setVisibility(View.VISIBLE);
//                img_home2.setVisibility(View.INVISIBLE);
//                img_home3.setVisibility(View.INVISIBLE);
//                img_home4.setVisibility(View.INVISIBLE);
//                break;
//            case R.id.home_rl2:
//                img_home1.setVisibility(View.INVISIBLE);
//                img_home2.setVisibility(View.VISIBLE);
//                img_home3.setVisibility(View.INVISIBLE);
//                img_home4.setVisibility(View.INVISIBLE);
//                break;
//            case R.id.home_rl3:
//                img_home1.setVisibility(View.INVISIBLE);
//                img_home2.setVisibility(View.INVISIBLE);
//                img_home3.setVisibility(View.VISIBLE);
//                img_home4.setVisibility(View.INVISIBLE);
//                break;
//            case R.id.home_rl4:
//                img_home1.setVisibility(View.INVISIBLE);
//                img_home2.setVisibility(View.INVISIBLE);
//                img_home3.setVisibility(View.INVISIBLE);
//                img_home4.setVisibility(View.VISIBLE);
//                break;
//            default:
//                break;
//        }
//    }

    // 更新选项卡状态和切换
    private void updateButtonBG(int position) {
        home_btn1.setBackgroundResource(R.mipmap.home0);
        home_btn2.setBackgroundResource(R.mipmap.list0);
        home_btn3.setBackgroundResource(R.mipmap.vip0);
        home_btn4.setBackgroundResource(R.mipmap.my0);
        mImageButtons.get(position).setBackgroundResource(drawableSe[position]);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 选项卡适配器
     *
     * @author cibn
     */
    class FragAdapter extends FragmentPagerAdapter {
        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return layouts.get(position);
        }

        @Override
        public int getCount() {
            return layouts.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mFragment3 != null && ((HomeThreeFragment) mFragment3).getOnKeyDown()) {
                return true;
            } else {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(HomeActivity.this, getString(R.string.homeactivity_exittime), Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    //小视屏播放结束处理
                    if (mFragment3 != null) {
                        ((HomeThreeFragment) mFragment3).getCompletion();
                    }
                    finish();
                    System.exit(0);
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // TODO weibo 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        App.getWeiboShare().handleWeiboResponse(intent, this);
    }

    //腾讯QQ分享监听回调
    public QQShareListener qqShareListener = new QQShareListener(this);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, qqShareListener);
    }

    /**
     * TODO weibo 新浪微博分享回调
     *
     * @param baseResponse
     */
    @Override
    public void onResponse(BaseResponse baseResponse) {
        if (baseResponse != null) {
            switch (baseResponse.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    break;
            }
        }
    }

    /**
     * 首页广告图片url转Bitmap
     *
     * @param url
     * @return
     */
    private Bitmap getHttpBitmap(String url) {
        Bitmap bitmap = null;
        try {
            URL pictureUrl = new URL(url);
            InputStream in = pictureUrl.openStream();
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }
        return bitmap;
    }

    /**
     * 首页广告图片保存在本地
     *
     * @param bitmap
     */
    private void savePicture(Bitmap bitmap) {
        boolean sdCardExist = Environment.getExternalStorageState().
                equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            String pictureName = "/mnt/sdcard/" + "homebg" + ".jpg";
            File file = new File(pictureName);
            FileOutputStream out;
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                Lg.i(TAG, "home advertising succeed");
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Lg.i(TAG, "home advertising error");
        }
    }

    private BroadcastReceiver upgrqadeTimeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                if (pageIndex == 0) {
                    checkUpgrade();
                }
            }
        }
    };

    private BroadcastReceiver homeInternetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Lg.i(TAG, "internet changed.");
                refreshInternetStatus(true);
                getThreeFragmentChangedNetWorkInfo();
            } else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                Lg.i(TAG, "wifi state changed.");
                refreshInternetStatus(false);
            } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                //判断是否是从锁屏回来的
                if (screenTag == 1) {
                    screenTag = 2;
                    if (App.is_network_connected) {
                        updateTime();
                    }
                }
            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                //锁屏
                screenTag = 1;
            }
        }
    };

    private void checkUpgrade() {
        //如果还是默认值，就取缓存值，避免频繁读取缓存，需要同步缓存值和内存中的数据
        if (App.userOptionNoUpradeTime == 0) {
            App.userOptionNoUpradeTime = SharePrefUtils.getLong(HostUpgradeManager.user_option_time, -1);
        }
        //用户没有选择忽略升级的状态，比如：在接口返回没有升级情况下，用户没有选择的机会！此时整点去轮询一次升级接口
        if (App.userOptionNoUpradeTime < 0) {
            Calendar cal = Calendar.getInstance();
            int min = cal.get(Calendar.MINUTE);
            if (min == 0) {  //min == 0 整点轮询, (min % 5 == 0 5分钟轮询一次是为了测试)
                int state = 0;
                state |= HostUpgradeManager.first_loop_upgrade_state;
                App.getHostUpgradeMaganer().checkUpgrade(state, HomeActivity.this);
                return;
            }

        }
        //如果用户已经选择了忽略升级的状态，则使用用户选择的那个时刻开始每隔一个小时轮询一次升级接口
        long curTime = System.currentTimeMillis() / 60000 * 60000;  //精确到分钟
        long delta = curTime - App.userOptionNoUpradeTime;
        if (delta >= 0) {
            long t1 = (delta / 1000 / 60 / 60 / 24); //24小时
//            long t1 = (delta / 1000 / 60 / 2); //每20分钟,测试用
            if (t1 >= 1) { //24小时判断，24小时后，再次弹出升级提示框
                int state = 0;
                state |= HostUpgradeManager.day_upgrade_state;
                App.getHostUpgradeMaganer().checkUpgrade(state, HomeActivity.this);
                return;
            }
            long t = (delta / 1000) % (HostUpgradeManager.loop_check_time * 60);
            if (t == 0) { //如果差值恰好是一个小时,则去接口请求升级信息，但不一定继续弹出升级框
                int state = 0;
                state |= HostUpgradeManager.loop_upgrade_state;
                App.getHostUpgradeMaganer().checkUpgrade(state, HomeActivity.this);
            }
        } else {
            App.userOptionNoUpradeTime = -1;
            SharePrefUtils.removePreferences(this, HostUpgradeManager.user_option_time);
        }
    }

    /**
     * 更新网络状态
     */
    private void refreshInternetStatus(boolean isConnectivity) {
        boolean isConnected = NetWorkUtils.isNetworkAvailable(App.getInstance());
        int netType = NetWorkUtils.getNetworkType(App.getInstance());
        Lg.i(TAG, "internet type : " + netType);
        if (isConnected && netType == ConnectivityManager.TYPE_ETHERNET) { // 有线网络连接
            preNetType = 0;
            reportInternetType(InternetType.link_lan);
            App.is_network_connected = true;
        } else if (isConnected && netType == ConnectivityManager.TYPE_WIFI) { // 无线网络连接
            preNetType = 1;
            reportInternetType(InternetType.link_wifi);
            App.is_network_connected = true;
        } else if (!isConnected && preNetType == 1) { // 从无线断开网络时
            reportInternetType(InternetType.no_link);
            App.is_network_connected = false;
        } else {
            reportInternetType(InternetType.no_link);
            App.is_network_connected = false;
        }
        //联网并且是从锁屏重新显示的情况下
        if (App.is_network_connected && screenTag == 2) {
            updateTime();
        }
    }

    private void updateTime() {
        screenTag = 0;
        JNIInterface.getInstance().getUserCheckTime(App.handleId);
    }

    /**
     * 上报网络变化
     *
     * @param type
     */
    private void reportInternetType(int type) {
        HttpRequest.getInstance().excute("ReportNetInfo",
                new Object[]{type, IPv4Utils.getLocalIP()});
    }

    /****************************************玩票页面播放器*******************************************/

    /**
     * 首页布局
     *
     * @returnL
     */
    public RelativeLayout getLayoutAll() {
        return layoutAll == null ? null : layoutAll;
    }

    /**
     * 播放器全屏布局
     *
     * @returnL
     */
    public FrameLayout getLayoutFullVideo() {
        return layoutFullVideo == null ? null : layoutFullVideo;
    }

    /**
     * 播放器小窗口布局
     *
     * @returnL
     */
    public RelativeLayout getLayoutSmall() {
        return layoutSmall == null ? null : layoutSmall;
    }

    /**
     * 播放器小窗口布局
     *
     * @returnL
     */
    public FrameLayout getLayoutSmallVideo() {
        return layoutSmallVideo == null ? null : layoutSmallVideo;
    }

    /**
     * 播放器小窗口关闭按钮
     *
     * @returnL
     */
    public ImageView getBtnClose() {
        return btnClose == null ? null : btnClose;
    }

    /**
     * 第一次onDrow调用
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mFragment3 != null) {
            ((HomeThreeFragment) mFragment3).getOnAttachedToWindow();
        }
    }

    /**
     * 销毁View的时候调用
     */
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mFragment3 != null) {
            ((HomeThreeFragment) mFragment3).getOnDetachedFromWindow();
        }
    }

    /**
     * 在改变屏幕方向、弹出软件盘和隐藏软键盘时，不再去执行onCreate()方法，而是直接执行onConfigurationChanged()
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        if (mFragment3 != null) {
            ((HomeThreeFragment) mFragment3).getOnConfigurationChanged(newConfig);
        }

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

    /****************************************玩票页面播放器*******************************************/

    /**
     * 玩票页面播放视频时，监听网络变化
     */
    private void getThreeFragmentChangedNetWorkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager)

                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
//            String name = info.getTypeName(); //网络名称
            Lg.i(TAG, "有可用网络");
            if (this.pageIndex == 2) {
                //如果当前选中的是玩票菜单索引
                EventBus.getDefault().post(Constant.NETWORK_OK_HOME_THREE);
            }
            if (mCurItemDataBean == null && !isDeviceAuth) {
                mHomeNonetwork.setVisibility(View.INVISIBLE);
                deviceAuth();
                vPager.setVisibility(View.VISIBLE);
            }

        } else {
            Lg.i(TAG, "没有可用网络");
            ToastUtils.show(this, "当前网络异常，请重试连接!");
            if (this.pageIndex == 2) {
                //如果当前选中的是玩票菜单索引
                EventBus.getDefault().post(Constant.NETWORK_NO_HOME_THREE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //小视屏播放结束处理
        ((HomeThreeFragment) mFragment3).getCompletion();
        unregisterReceiver(homeInternetReceiver);
//        unregisterReceiver(receiver);
        unregisterReceiver(upgrqadeTimeReceiver);
        App.getHostUpgradeMaganer().dismissDialog();
    }

    private void startTimerServer() {
        App.timeDvalue = JNIRequest.getInstance().getServeTime();
        App.userReservedList.clear();
        UserReserveHelper.query(0, 100, new DbQueryListener() {
            @Override
            public void query(List<ReserveBean> list) {
                if (list == null || list.size() == 0) {

                } else {
                    App.addAllList(list);
                }
                handler.sendEmptyMessage(10019);
            }
        });
    }

    /**
     * 开始加载动画
     */
    private void startLooding() {
        mHomeLoodingRl.setVisibility(View.VISIBLE);
        mHomeLoodingImag.startAnimation(mAnimation);
    }

    /**
     * 停止加载动画
     */
    private void stopLooding() {
        if (mHomeLoodingRl != null && mAnimation != null) {
            mHomeLoodingRl.setVisibility(View.GONE);
            mAnimation.cancel();
            mHomeLoodingImag.clearAnimation();
        }
    }

}
