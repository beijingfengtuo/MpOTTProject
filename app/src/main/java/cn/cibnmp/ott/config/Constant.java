package cn.cibnmp.ott.config;

import cn.cibnmp.ott.utils.PathUtils;
import cn.cibnmp.ott.utils.Utils;

/**
 * Created by yanjing on 2017/9/11.
 */

public class Constant {
    //    public final static long APPID = "1010";  //正式
//    public final static long CHNID = "20081";  //正式
    public final static String APPID = "1011";  //测试
    public final static String CHNID = "10019";  //测试
    public final static String VERSION_NAME = "1.0.0.0";
    public final static String PROJID = "100";

    //东方大剧院
    public static final String WX_APP_ID = "wx98e33db565562eaa";
    public static final String WX_APP_SECRET = "db1e7579dc0cf5adc5b1476dec086ad7";
    public static final String WX_APP_SCOPE = "snsapi_userinfo";
    public static final String WX_APP_SIGN = "cf3430797abd1e5d3bee15e639e0648c";
    public static final String WX_APP_PACKAGE = "Sign=WXPay";
    public static final String WX_APP_PARTNERID = "1339544201";//微信商户id

    //QQ_Appid
    public static String QQ_APPID = "1106030588";
    //TODO weibo 新浪、微信_Appid
    public static String XL_APP_KEY = "1057140527";

    // 注册和登录成功发送的消息
    public final static String HOME_USER_SIGN_IN = "HOME_USER_SIGN_IN";
    // 退出登录发送的消息
    public final static String HOME_USER_SIGN_OUT = "HOME_USER_SIGN_OUT";
    // 首页筛选的名称--会员专区
    public final static String USER_SIGN_IN = "微信登录";
    // 存储用户微信登录的标识
    public final static int USER_UP_number2 = 2;
    // 获取登录信息发送的消息
    public final static String HOME_USER_SIGN_UP = "HOME_USER_SIGN_UP";
    // 第一次链接大剧院标识
    public final static String LINK_BEADS_TAG = "LINK_BEADS_TAG";
    // 存储用户登录的key
    public final static String USER_UID = "USER_UID";
    // 首页清空收藏记录的消息
    public final static String USER_AND_CLEAR = "USER_AND_CLEAR";
    // 选择产品包价格时发送消息
    public final static String SELECT_PRODUCT_PRICE = "SELECT_PRODUCT_PRICE";
    // 选择代金券时发送消息
    public final static String SELECT_VOUCHERS_PRICE = "SELECT_VOUCHERS_PRICE";
    // 取消代金券时发送消息
    public final static String SELECT_NO_VOUCHERS_PRICE = "SELECT_NO_VOUCHERS_PRICE";
    // 微信支付返回成功发送消息
    public final static String WX_RETURN_SUCCESS = "WX_RETURN_SUCCESS";
    // 支付成功发送消息
    public final static String PAY_SUCCESS = "PAY_SUCCESS";
    // 个人中心进入产品包页面支付成功发送消息
    public final static String PAY_SUCCESS1 = "PAY_SUCCESS1";
    // 详情页进入产品包页面支付成功发送消息
    public final static String PAY_SUCCESS2 = "PAY_SUCCESS2";
    // 个人中心进入产品包页面
    public final static String USER_INTER_PRODUCT_PACKAGE = "USER_INTER_PRODUCT_PACKAGE";
    // 详情页进入产品包页面
    public final static String INFO_INTER_PRODUCT_PACKAGE = "INFO_INTER_PRODUCT_PACKAGE";
    // 点击我的刷新播放记录
    public final static String PUSH_PLAY_HISTORY = "PUSH_PLAY_HISTORY";

    // 首页跳转到我的收藏标识
    public final static int userIntent1 = 1;
    // 首页跳转到订单记录页面标识
    public final static int userIntent6 = 2;
    // 首页跳转到我的消息标识
    public final static int userIntent2 = 11;
    // 首页跳转到播放记录标识
    public final static int userIntent3 = 12;
    // 首页跳转到我的预约页面标识
    public final static int userIntent7 = 4;
    // 存储用户登录的key
    public final static String USER_UP = "USER_UP";
    // 首页跳转搜索页传值
    public final static String CHANNEL_SEARCH_EPGID = "searchepgid";

    /**
     * 默认的动画持续时间是250ms
     */
    public final static long DEFAULT_ANIMATION_DURATION = 250;
    /**
     * 默认的放大倍数
     */
    public final static float DEFAULT_SCALE_SIZE = 1.08f;
    // 英语
    public static final String ENGLISH = "ENGLISH";
    // 泰语
    public static final String TH = "TH";
    // 播放设置清晰度
    public final static String videoClarity = "SETTING_VIDEO_CLARITY";
    // 播放设置画面比例
    public final static String videoProportion = "SETTING_VIDEO_PROPORTION";
    // 播放设置智能解码
    public final static String videoSwich = "SETTING_VIDEO_SWICH";
    // 启动多fragment的Activity时，加载某一fragment的启动参数名称
    public final static String activityLoadFragmentExtra = "activity_load_fragment_extra";
    // 加载设置界面的通用设置fragment
    public final static String generalSettingFrgment = "general_setting_fragment";
    // 加载设置界面的播放设置fragment
    public final static String playSettingFrgment = "play_setting_fragment";
    // 加载设置界面的扫描二维码下载移动app的fragment
    public final static String appDownloadFrgment = "app_download_fragment";
    // 通用设置设备名称
    public final static String deviceName = "SETTING_NAME";
    // 当前用户
    public final static String currentUser = "current_user";
    // handleId的key值
    public final static String handleIdKey = "jni_handle_id";
    //    author mw
    public final static int DEFAULT_SCREEN_WIDTH = 1280;
    public final static int DEFAULT_SCREEN_HEIGHT = 720;
    public final static int DEFAULT_SCREEN_DPI = 160;
    public final static float DEFAULT_SCREEN_DESITY = 1.0f;
    public static final float DEFAULT_DESITY = 1.0f;

    public final static String JNICACHEPATH = PathUtils.getJniCachePath();

    public final static String DOWNLOADPATH = PathUtils.getDownloadPath();

    public final static String DATABASEPATH = "/data/data/"
            + Utils.getAppPackageName() + "/databases";

    public final static String JNICLASSPATH = Utils.getAppPackageNamePath() + "/jni/JNIInterface";


    // 通用设置背景图片KEY
    public final static String SETTING_IMG_BG = "SETTING_IMG_BG";
    //    通用设置屏幕保护时间
    public final static String SCREENPROTECT_TIME = "SCREENPROTECT_TIME";

    // 轮播域名地址
    public final static String carouselDomainUrl = "cibn_carousel_domain";

    // 开机图片key
    public final static String splashScreenImage = "splash_screen_image";
    // 开机视频key
    public final static String splashScreenVideo = "splash_screen_video";
    //开机splash类型
    public final static String splashScreenType = "splash_screen_type";
    //开机图片新的资源
    public final static String splashScreenNew = "splash_screen_new";
    //开机图片本地已缓存的数据
    public final static String splashScreenLocal = "splash_screen_local";
    //开机图片旧的资源
    public final static String splashScreenOld = "splash_screen_old";
    //开机图片-广告系统的最新资源
    public final static String splashAdvertNew = "splash_advert_new";

    public final static String LOCALCACHEEXIST = "1";

    // 微信二维码路径
    public final static String wx_qrcode_img = "wx_qrcode_img";
    public final static String homeExitLogo = "home_exit_logo_fid";
    public final static String homeExitText = "home_exit_logo_text";
    public final static String appDownloadUrl = "mobile_app_download_qrcode";
    // 存储用户登录微信本地文本标识的key
    public final static String USER_OPENID = "USER_OPENID";
    public final static String USER_TOKENID = "USER_TOKENID";


    public final static String epg_entry_param = "home_epg_entry_param";
    public final static String default_nav_pos = "nav_default_position";   //首页进入时默认选中的位置
    public final static String user_nav_pos = "nav_user_position";  //用户设置的进入首页时默认选择的位置

    public final static String all_nav_sort = "nav_all_sort";  //全部的导航
    public final static String default_nav_sort = "nav_default_sort";  //首页默认的导航顺序，也可作本地数据来使用
    public final static String user_nav_sort = "nav_user_sort";  //用户设置的排序

    // 远程(接口,非认证)服务器时间与本地时间差值，
    public final static String server_local_time_diff = "server_local_time_diff";

    public final static String about_us_config = "config_about_us";

    public final static String historyFrg = "history";
    public final static String videoCollFrg = "video_collect";
    public final static String topicCollFrg = "topic_collect";
    public final static String peronFollFrg = "person_follow";
    public final static String labelFollFrg = "label_follow";
    public final static String infoFrg = "user_info";   //用户信息
    public final static String voucherFrg = "voucher_list";  // 代金券
    public final static String expensesRecordFrg = "expenses_record";  //消费记录

    public final static String persionFrgment = "persion";
    public final static String labelFragment = "lable";


    //首页启动其他界面传参的key值
    public final static String epgIdKey = "epgId";
    public final static String contentIdKey = "contentId";
    public final static String p1ParamKey = "actionParam_p1";
    public final static String p2ParamKey = "actionParam_p2";
    public final static String p3ParamKey = "actionParam_p3";
    public final static String actionKey = "action";
    public final static String actionUrlKey = "actionUrl";
    public final static String sidKey = "sid";


    //TEA 开屏数据key
    public final static String TEAHOMEKEY = "tea_home";
    //TEA 详情页数据
    public static final String TEADETAILKEY = "tea_detail";

    //关闭直播的msg
    public final static String CLOSE_LIVE_MSG = "close_live_msg";
    //关闭点播的msg
    public static final String CLOSE_VIDIO_MSG = "close_vidio_msg";

    public static final String HOST_UPGRADE_KEY = "cibn_host_upgrade_apk";

    public static final String START_HOMEACTIVITY = "start_homeactivity";
    public static final String STOP_HOMEACTIVITY = "stop_homeactivity";

    // apk间msg通信
    public final static int MSG_CODE = 1;
    public final static String MSG_KEY = "msg_key";
    public final static String ACT_EXTRA = "start_act_extra";


    //跑马灯消息等待
    public final static String waitingUserMsg = "marquee_waiting_user_msg";

    // activity直接跳转bundle参数名称
    public final static String activityBundleExtra = "activity_bundle_extra";

    // viewpager 与 scrollview 滑动冲突标志位
    public static boolean CLASHTAB = true;


    //TODO zxy 添加直播数据标签

    // 转播页面的节目直播状态
    public final static String PLAY_STATUS = "playStatus";
    //轮播图片
    public static int djy_picture_tag = 3038;
    //现场直播
    public static int djy_live_tag = 3042;
    //录像
    public static int djy_video_tag = 3743;
    // 更新转播页面现场数据
    public final static String HOME_TWO_LIVE_SCENE = "HOME_TWO_LIVE_SCENE";
    // 更新转播页面录像数据
    public final static String HOME_TWO_LIVE_VIDEO = "HOME_TWO_LIVE_VIDEO";
    // 更新转播页面轮播图片
    public final static String HOME_TWO_LIVE = "HOME_TWO_LIVE";
    //转播页面导航epgid
    public final static String NAVMOBILEEPG2 = "navMobileEpg2";

    //TODO zxy 添加玩票页面数据标签

    // 玩票页面导航epgID
    public final static String NAVMOBILEEPG = "navMobileEpg";
    //玩票页面 - 播放器中间布局的开始播放按钮标识
    public final static int PLAY_TAG_START_CENTER = 100;
    //玩票页面 - 播放器开始播放按钮标识
    public final static int PLAY_TAG_START = 101;
    //玩票页面 - 播放结束按钮标识
    public final static String PLAY_TAG_STOP = "102";
    //玩票页面 - 播放器全屏按钮标识
    public final static int PLAY_TAG_FULL = 103;
    //玩票页面 - 分享按钮标识
    public final static int PLAY_TAG_SHARE = 104;
    //玩票页面 - 收藏按钮标识
    public final static int PLAY_TAG_COLLECT = 105;
    //玩票页面 - 点赞按钮标识
    public final static int PLAY_TAG_ZAN = 106;

    //玩票页面 - 点击item索引
    public final static String HOME_THREE_OTHER_RECYCLERVIEW_ITEM_POSITION = "recyclerview_item_position";
    //玩票页面 - 点击item中的数据对象
    public final static String HOME_THREE_OTHER_RECYCLERVIEW_ITEM_DATA = "recyclerview_item_data";
    //玩票页面 - 列表数据对象（非1099类型的页面使用）
    public final static String HOME_THREE_ITEM_RESULTBEAN = "ResultBean";



    //TODO yww 添加首页数据标签

    // 首页fragment页面参数频道id
    public final static String CHANNEL_ID = "channelid";
    //首页fragment页面参数频道epgID、玩票页面导航数据epgID
    public final static String CHANNEL_EPGID = "channelepgid";
    // 首页fragment页面参数频道回调状态
    public final static String CHANNEL_STATUS = "channelStatus";
    // 首页HOME导航数据传值homeonefragent
    public final static String CURITEMDATABEAN = "CurItemDataBean";
    // 首页滑动与viewPage冲突回调状态
    public final static String PULL_STOP = "pullstop";
    // 节目列表跳转详情页面参数bean
    public final static String BUBDLE_NAVIGATIONINFOITEMBEAN = "NavigationInfoItemBean";
    //筛选页面传值 p1
    public final static String BUNDLE_P1 = "bundle_p1";
    //筛选页面传值 p2
    public final static String BUNDLE_P2 = "bundle_p2";
    //筛选页面传值 p3
    public final static String BUNDLE_P3 = "bundle_p3";


    //TODO zxy 首页、玩票页面、转播页面点击item传值参数
    // 参数：所有页面-Action
    public final static String BUNDLE_ACTION = "bundle_action";
    // 参数：所有页面 - 节目状态（转播页面：直播中、回看、已结束）
    public final static String BUNDLE_STATES = "bundle_states";
    // 参数：所有页面 - 节目id（玩票页面：获取栏目下的内容、转播页面：）
    public final static String BUNDLE_CONTENTID = "bundle_contentid";
    // 参数：筛选页面 epgID
    public final static String BUNDLE_EPGID = "bundle_epgID";
    // 参数：筛选页面 - 栏目ID columnID
    public final static String BUNDLE_COLUMNID = "bundle_columnID";
    // 参数：筛选页面 - 频道ID(搜索：0全部)
    public final static String BUNDLE_SUBJECTID = "bundle_subjectID";
    // 参数：筛选页面 - 筛选搜索内容
    public final static String BUNDLE_KEYWORDS = "bundle_keyWords";

    //TODO zxy 网络变化判断
    //转播页面：当前网络是否可用 ok 可用、no 不可用
    public final static String NETWORK_OK_HOME_TWO_LIVE = "network_ok_home_two_live";
    public final static String NETWORK_NO_HOME_TWO_LIVE = "network_no_home_two_live";

    //玩票页面：当前网络是否可用 ok 可用、no 不可用
    public final static String NETWORK_OK_HOME_THREE = "network_ok_home_three";
    public final static String NETWORK_NO_HOME_THREE = "network_no_home_three";

    // 存储是否在wifi条件下播放的配置参数
    public final static String SETTING_CONFIG = "SETTING_ITEM_RL3";
}
