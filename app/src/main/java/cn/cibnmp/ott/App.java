package cn.cibnmp.ott;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import org.xutils.x;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.cibnhp.grand.BuildConfig;
import cn.cibnmp.ott.bean.UserBean;
import cn.cibnmp.ott.bean.UserInBean;
import cn.cibnmp.ott.config.AssembleConfig;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.config.LogcatHelper;
import cn.cibnmp.ott.config.MyJurisdictionUtils;
import cn.cibnmp.ott.jni.JNIConfig2;
import cn.cibnmp.ott.jni.JNIInterface;
import cn.cibnmp.ott.jni.JNIRequest;
import cn.cibnmp.ott.lib.ACache;
import cn.cibnmp.ott.ui.detail.bean.ReserveBean;
import cn.cibnmp.ott.utils.ApolloUtils;
import cn.cibnmp.ott.utils.AppManager;
import cn.cibnmp.ott.utils.DeviceUtils;
import cn.cibnmp.ott.utils.FileUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.SharePrefUtils;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.HostUpgradeManager;
import cn.cibntv.downloadsdk.DownloadSdk;
import cn.cibntv.downloadsdk.download.DownloadManager;
import cn.cibntv.downloadsdk.download.DownloadService;

/**
 * Created by yanjing on 2017/9/11.
 */

public class App extends Application {
    private static String TAG = App.class.getSimpleName();

    //------------------->>图纸
    public static float ScreenScale = 1.5f; // 屏幕分辨率比例
    public static float DesityScale = 2.0f; // 屏幕密度比例
    public static int ScreenWidth = 0;
    public static int ScreenHeight = 1280;
    public static int ScreenDpi = 240;
    public static float ScreenDesity = 1.5f;
    public final static int DEFAULT_SCREEN_WIDTH = 720;
    public static final float DEFAULT_DESITY = 2.0f;
    //<<-------------------
    public static int currentapiVersion = android.os.Build.VERSION.SDK_INT; //系统版本号
    public static String currentapiVersion_name = android.os.Build.VERSION.RELEASE; //系统版
    public static boolean isWebKitErrorDevices = false;

    public static long handleId = 0l;

    public static boolean is_xiaomi_channel = false;  //是否是小米渠道，有些小米渠道才有的功能通过此flag控制

    private static Application instance = null;

    public static String epgUrl = "";
    public static String cdnPicUrl = "";
    public static String cdnUrl = "";
    public static String wxBindUrl = "";
    public static String searchUrl = "";
    public static String carouselUrl = "";
    public static String bmsurl = "";
    public static String msgUrl = "";
    public static String detailWebViewUrl = "";
    public static String ShareUrl = "";
    public static String publicIpAddr = "";
    public static String publicTID = "";
    public static long appId = 0;  //应用id
    public static long projId = 0; //项目id
    public static long channelId = 0; //渠道id
    public static long userId = 0;  //用户id
    //    public static long userId = 56916;  //用户id
//    public static long userId = 36104;  //用户id
    public static String versionName = "";
    public static boolean isLogin = false;
    public static boolean isVip = false;
    public static String sessionId = "";
    public static String userPic = "";// 头像
    public static String userName = ""; // 昵称
    public static String mobile = ""; // 手机号
    public static String hostEpgId = "";//主epgId

    public static int UserSignInNO;
    public static boolean isForeground = true;

    // IWXAPI 是第三方app和微信通信的openapi接口
    public static IWXAPI api;
    public static String weixinCode = null;
    public static String access_token = null;
    public static String tokenOpenid = null;
    public static String weixinOpenid = null;
    public static String weixinUnionid = null;

    public static long userOptionNoUpradeTime = 0;  //用户选择的忽略升级的时间，本地时间

    //腾讯QQ对象
    public static Tencent mTencent;
    //新浪微博
    public static IWeiboShareAPI mWeiboShareAPI;

    // 存储登录的信息
    public static UserInBean mUserInBean = null;
    public static boolean signIn = false;

    public static DownloadManager downloadManager;
    public static ACache mCache;

    public static AppManager mAppManager;

    private static HostUpgradeManager hostUpgradeManager;

    //是否已初始化
    private static boolean baseAppcationInited = false;

    public static boolean canPlayerTea = false;
    public static boolean is_network_connected = true;  //判断当前网络是否连接
    public static long timeDvalue = 0;   //本地时间与远程时间的差值

    /**
     * true代表不使用软解，false代表默认使用软解
     */
    public static boolean defaultMediaDecode = true;  //当前设备默认使用的视频解码方式
    // 英语
    public static final String ENGLISH = "ENGLISH";
    // 中文
    public static final String CHINESE = "CHINESE";
    // 老挝语
    public static final String LO = "LO";
    //语言初始化
    public static String AppName = "CHINESE";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //语言切换参数--ENGLISH：英语--CHINESE：中文--LO:老挝语
//        Utils.setSay(CHINESE);

        if (!baseAppcationInited) {
            mCache = ACache.get(this);
            initMetaData();
            if (MyJurisdictionUtils.myJurisdictionUtils().isGrantedAllPermission(App.getInstance())) {
                startApp();
            }
        }
        baseAppcationInited = true;
    }

    public static ACache getCache() {
        if (mCache == null)
            mCache = ACache.get(instance);
        return mCache;
    }

    private void initMetaData() {
        ApplicationInfo info;
        try {
            info = this.getPackageManager().getApplicationInfo(
                    getPackageName(), PackageManager.GET_META_DATA);

            String channelId = String.valueOf(info.metaData.get("CHANNEL_VALUE"));
            Log.d("channelInit", "channelId --> " + channelId);


            ACache aCache = ACache.get(this);
            aCache.put("host_appId", String.valueOf(BuildConfig.appId));
            aCache.put("host_projId", String.valueOf(BuildConfig.projId));
            aCache.put("host_versionName", String.valueOf(BuildConfig.VERSION_NAME));
            aCache.put("host_versionCode", String.valueOf(BuildConfig.VERSION_CODE));
            aCache.put("host_channelId", channelId);
            aCache.put("uterm_url", BaseUrl.utermUrl);
            aCache.put("entry_url", BaseUrl.entryUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void startApp() {
        iniJnI();
        getWxApi();
        //TODO 目前只做微信分享、微博分享有问题需修改
//        getQQ();
//        getWeiboShare();
        //适配尺寸初始化
        initSizeDimension();
        //本地时间矫正
        initTimeDiff();
        //错误日志上报
        initErrorHandler();
        //获取登录状态
        initUserInfo();
        if (!FileUtils.isFileExist(Constant.DOWNLOADPATH)) {
            FileUtils.creatDir(Constant.DOWNLOADPATH);
        }
        //下载
        iniDownloadManager();
//        // 小米渠道
//        checkChannel();
        x.Ext.init(getInstance());
        x.Ext.setDebug(BuildConfig.DEBUG);
    }

    //微信初始化
    public static IWXAPI getWxApi() {
        if (api == null) {
            // 通过WXAPIFactory工厂，获取IWXAPI的实例
            api = WXAPIFactory.createWXAPI(getInstance(), Constant.WX_APP_ID, false);
            // 将该app注册到微信
            api.registerApp(Constant.WX_APP_ID);
        }
        return api;
    }

    //QQ初始化
    public static Tencent getQQ() {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(Constant.QQ_APPID, getInstance());
        }
        return mTencent;
    }

    //TODO weibo 新浪微博初始化
    public static IWeiboShareAPI getWeiboShare() {
        if (mWeiboShareAPI == null) {
            // 创建微博分享接口实例
            mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(getInstance(), Constant.XL_APP_KEY);
            // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
            // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
            // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
            mWeiboShareAPI.registerApp();
        }
        return mWeiboShareAPI;
    }

    private static void iniDownloadManager() {
        //wanqi,test
        DownloadSdk.init(getInstance());
        DownloadSdk.getInstance().debug("DownloadSdk");

        if (downloadManager == null) {
            downloadManager = DownloadService.getDownloadManager();
            downloadManager.setTargetFolder(Constant.DOWNLOADPATH);
        }
    }

    //初始化图纸
    private static void initSizeDimension() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager mWm = (WindowManager) getInstance().getSystemService(Context.WINDOW_SERVICE);
        mWm.getDefaultDisplay().getMetrics(dm);
        ScreenWidth = dm.widthPixels;
        ScreenHeight = dm.heightPixels;
        ScreenDpi = dm.densityDpi;
        ScreenDesity = dm.density;
        ScreenScale = (float) ScreenWidth / DEFAULT_SCREEN_WIDTH;
        DesityScale = DEFAULT_DESITY / ScreenDesity + 1;
        Lg.d(TAG, ScreenHeight + "---------------" + "ScreenWidth:" + ScreenWidth + "DesityScale:" + DesityScale + "ScreenDpi:" + ScreenDpi + "ScreenDesity:" + ScreenDesity);
    }

    public static AppManager getAppManager() {
        if (mAppManager == null) {
            mAppManager = AppManager.getAppManager();
        }
        return mAppManager;
    }

    public static Application getInstance() {
        return instance;
    }


    //初始化服务器与本地时间差值
    private static void initTimeDiff() {
        try {
            String diff = App.mCache.getAsString(Constant.server_local_time_diff);
            if (!TextUtils.isEmpty(diff)) {
                timeDvalue = Long.valueOf(diff);
            }
        } catch (Exception e) {
        }
    }

    //系统崩溃日志上报
    private static void initErrorHandler() {
//        CrashHandler handler = CrashHandler.getInstance();
//        handler.init(this);
    }

    //获取登录状态
    private static void initUserInfo() {
        String currentUser = SharePrefUtils.getString(Constant.currentUser, "");
        Log.i(TAG, "initUserInfo: " + currentUser);
        UserBean user;
        if (TextUtils.isEmpty(currentUser)) {
            user = (UserBean) mCache.getAsObject(Constant.currentUser);
            if (user != null) {
                SharePrefUtils.saveString(Constant.currentUser, JSON.toJSONString(user));
            }
        } else {
            user = JSON.parseObject(currentUser, UserBean.class);
        }
        if (user != null) {
            isLogin = true;
            sessionId = user.getSessid();
            userId = user.getUid();
            userName = user.getNickname();
            userPic = user.getPortraiturl();
            mobile = user.getMobile();
        }
    }

    /**
     * 保存登录信息
     *
     * @param user
     */
    public static void saveUserInfo(UserBean user) {
        isLogin = true;
        userId = user.getUid();
        sessionId = user.getSessid();
        userName = user.getNickname();
        userPic = user.getPortraiturl();
        mobile = user.getMobile();
        SharePrefUtils.saveString(Constant.currentUser, JSON.toJSONString(user));
    }

    /**
     * 退出登录,清空数据
     */
    public static void logoutUser() {
        userId = 0;
        sessionId = "";
        isLogin = false;
        userName = "";
        userPic = "";
        isVip = false;
        SharePrefUtils.saveString("weixinCode", "");
        SharePrefUtils.removePreferences(instance, Constant.currentUser);
    }

    //初始化JNI
    private static void iniJnI() {
        String chnid = App.mCache.getAsString("host_channelId");
        if (!TextUtils.isEmpty(chnid)) {
            App.channelId = Long.valueOf(chnid);
            Lg.d(TAG, " host_channelId : " + chnid);
        } else {
        //    App.channelId = 20000;
            //正式环境
            App.channelId = 20161;
//            App.channelId = 10000;
        }

        String projId = App.mCache.getAsString("host_projId");
        if (!TextUtils.isEmpty(projId)) {
            App.projId = Long.valueOf(projId);
            Lg.d(TAG, " host_projId : " + projId);
        } else {
        //    App.projId = 100;
            //正式环境
            App.projId=105;
        }

        String appId = App.mCache.getAsString("host_appId");
        if (!TextUtils.isEmpty(appId)) {
            App.appId = Long.valueOf(appId);
            Lg.d(TAG, " host_appId : " + appId);
        } else {
         //   App.appId = 1000;
            //正时环境
            App.appId = 1021;
        }

        String versionName = App.mCache.getAsString("host_versionName");
        if (TextUtils.isEmpty(versionName)) {
            versionName = "1.0.2.0";  //1.0.2.0,5.0.2.0
        }
        App.versionName = versionName;
        Lg.d(TAG, " host_versionName : " + versionName);

        JNIConfig2 jniConfig = new JNIConfig2();
        jniConfig.setVercode(versionName);
        //wanqi,test
        String usbPath = LogcatHelper.getUSBPath();
        if (AssembleConfig.debug && !TextUtils.isEmpty(usbPath)) {
            jniConfig.setCachepath(LogcatHelper.getUSBPath());
        } else {
            jniConfig.setCachepath(Constant.JNICACHEPATH);
        }
        // jniConfig.setCachepath(Constant.JNICACHEPATH);
        jniConfig.setSldbpath(Constant.DATABASEPATH);
        jniConfig.setAvailsize(ApolloUtils.getSDFreeSize());
        String deviceId = DeviceUtils.getDeviceId(getInstance());
        Lg.d(TAG, "deviceId : " + deviceId);
        jniConfig.setDevId(deviceId); // hid
        jniConfig.setChnId(String.valueOf(App.channelId));
        jniConfig.setMac(DeviceUtils.getLanMac());
        jniConfig.setMac2(DeviceUtils.getWlanMac(getInstance()));
        jniConfig.setAppId(String.valueOf(App.appId));
        jniConfig.setProjId(String.valueOf(App.projId));
        jniConfig.setCallback_class_path(Constant.JNICLASSPATH);
        jniConfig.setChatmsg_method_name(JNIRequest.MESSAGECALLBACK);

        String json = JSON.toJSONString(jniConfig);
        Log.i(TAG, "JNIConfig = " + json);
        handleId = JNIInterface.getInstance().InitJNI(0, json, json.getBytes().length); // uid默认为0
        Log.d(TAG, "jni handleId = " + handleId);
    }

    public static HostUpgradeManager getHostUpgradeMaganer() {
        if (hostUpgradeManager == null) {
            hostUpgradeManager = new HostUpgradeManager(instance);
        }
        return hostUpgradeManager;
    }


    //用户已预约的直播列表,要在用户登录和注销状态改变时重新初始化这个列表
    public static List<ReserveBean> userReservedList = new CopyOnWriteArrayList<>();

    public static void addAllList(List<ReserveBean> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getLivestatus() == 0) {
                userReservedList.add(list.get(i));
            }
        }
    }
}
