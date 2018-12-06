package cn.cibnmp.ott.jni;

public class ResponseCode {
    public static final int SUCCESS_CODE = 0; // 请求成功
    public static final int NO_LOCAL_DATA_CODE = -100; // 没有本地数据
    public static final int NET_DISCONN_CODE = -500; // 无网络连接
    public static final int DB_FAILED_CODE = -50; // 本地数据库存储操作失败，添加或删除或修改
    public static final int DATA_ERROR_CODE = -1001; // 数据错误code
    public static final int INVALID = Integer.MIN_VALUE; // 异常返回码

    public static final int HOME_REFRESH_MSG_TYPE = 2002; // 首页刷新
    public static final int PAY_RESULT_MSG_TYPE = 2005; // 支付结果回调
    public static final int REGISTER_RESULT_MSG_TYPE = 2004; // 登录回调
    public static final int TERM_BLOCKUP_MSG_TYPE = 2006; // 终端重新认证回调
    public static final int USER_MESSAGE_MSG_TYPE = 2007; // 我的消息回调
    public static final int SERVER_NOTICE_MSG_TYPE = 2020; //服务器通知消息回调
    public static final int APP_DISABLE_MSG_TYPE = 2011; //应用被禁用的回调


    public static final int epg_success_code = 1000;  //返回成功
    public static final int player_disable_code = 1001;  //点播不可用
    public static final int player_nocontent_code = 1002;  //点播无介质
    public static final int live_disable_code = 1003;  //直播不可用
    public static final int live_nocontent_code = 1004;  //直播无介质
    public static final int carousel_disable_code = 1005;  //轮播不可用
    public static final int nav_epg_error_code = 1006;  //导航与EPG无关联
    public static final int column_epg_error_code = 1007;  //栏目与EPG无关联
    public static final int parameter_error_code = 1008;  //参数错误
    public static final int data_error_code = 1009;  //数据异常
    public static final int search_error_code = 1010;  //搜索异常

    /**
     * 用户终端系统返回成功code
     */
    public static final int uterm_success_code = 200;  //返回成功

    public static final int uterm_noupgrade_code = 400;  //版本已经是最新的code

    public static final int uterm_disable_code = 401;  //禁用应用code

}
