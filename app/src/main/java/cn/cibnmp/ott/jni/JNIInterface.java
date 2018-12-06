package cn.cibnmp.ott.jni;

public class JNIInterface {

    private static JNIInterface instance;

    private JNIInterface() {
        super();
    }

    static {
         System.loadLibrary("video");
    }

    public static JNIInterface getInstance() {
        if (instance == null) {
            instance = new JNIInterface();
        }
        return instance;
    }

    @JNIMethods(j = "RJModuleInit_jni", c = "RJModuleInit")
    public native long InitJNI(long userID, String json, int json_len);

    @JNIMethods(j = "comca_mgmt_clean_jni", c = "comca_mgmt_clean")
    public native long close(long handleId);


    /**
     * 发起获取图片请求
     *
     * @param handle
     * @param fid
     * @param picType
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "comca_get_file_jni", c = "comca_get_file")
    public native int RequestImage(long handle, int appId, String fid,
                                   int picType, String method_name, int method_name_len);


    /**
     * 获取本地收藏记录
     *
     * @param handle
     * @param appId
     * @param startIndex
     * @param number
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "video_collect_list_jni", c = "video_collect_list")
    public native int RequestVideoCollectList(long handle, int appId,
                                              int startIndex, int number, String method_name, int method_name_len);

    /**
     * 删除本地收藏记录
     *
     * @param handle
     * @param appId
     * @param delAll          1表示删除全部记录，0代表删除指定vid的记录
     * @param vid
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "video_collect_del_jni", c = "video_collect_del")
    public native int DeleteVideoCollect(long handle, int appId, int delAll,
                                         long vid, String method_name, int method_name_len);

    /**
     * 添加本地收藏记录
     *
     * @param handle
     * @param appId
     * @param json
     * @param json_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "video_collect_add_jni", c = "video_collect_add")
    public native int AddVideoCollect(long handle, int appId, String json,
                                      int json_len, String method_name, int method_name_len);

    /**
     * 判断本地播放记录是否存在
     *
     * @param handle
     * @param appId
     * @param id
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "video_record_get_jni", c = "video_record_get")
    public native int RequestVideoRecordGet(long handle, int appId, long id, String method_name, int method_name_len);

    /**
     * 判断本地收藏记录是否存在
     *
     * @param handle
     * @param appId
     * @param id
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "video_collect_get_jni", c = "video_collect_get")
    public native int RequestVideoCollectGet(long handle, int appId, long id, String method_name, int method_name_len);


    /**
     * 获取本地播放记录
     *
     * @param handle
     * @param appId
     * @param startIndex
     * @param number
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "video_record_list_jni", c = "video_record_list")
    public native int RequestVideoRecordList(long handle, int appId,
                                             int startIndex, int number, String method_name, int method_name_len);

    /**
     * 删除本地播放记录
     *
     * @param handle
     * @param appId
     * @param delAll
     * @param vid
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "video_record_del_jni", c = "video_record_del")
    public native int DeleteVideoRecord(long handle, int appId, int delAll,
                                        long vid, String method_name, int method_name_len);

    /**
     * 添加本地播放记录
     *
     * @param handle
     * @param appId
     * @param json
     * @param json_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "video_record_add_jni", c = "video_record_add")
    public native int AddVideoRecord(long handle, int appId, String json,
                                     int json_len, String method_name, int method_name_len);


    /**
     * 上报硬件信息接口
     *
     * @param handle
     * @param appId
     * @param url
     * @param url_len
     * @param json
     * @param json_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "comca_hardware_report_jni", c = "comca_hardware_report")
    public native int ReportHardwarInfo(long handle, int appId, String url,
                                        int url_len, String json, int json_len, String method_name,
                                        int method_name_len);


    /**
     * 上报网络状态信息
     *
     * @param handle
     * @param nettype
     * @param ip
     * @param ip_len
     * @return
     */
    @JNIMethods(j = "comca_set_nettype_jni", c = "comca_set_nettype")
    public native int ReportNetInfo(long handle, int nettype,
                                    String ip, int ip_len);


    /**
     * 播放器上报接口
     *
     * @param handle
     * @param logType
     * @param json
     * @param json_len
     * @return
     */
    @JNIMethods(j = "log_upload_put_jni", c = "log_upload_put")
    public native int ReportPlayLog(long handle, int logType, String json,
                                    int json_len);


    /**
     * 获取用户信息接口
     *
     * @param handle
     * @param appId
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "get_user_info_jni", c = "get_user_info")
    public native int RequestUserInfo(long handle, int appId,
                                      String method_name, int method_name_len);

    /**
     * 获取用户消息
     *
     * @param handle
     * @param appId
     * @param startIndex
     * @param number
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_umsg_get_jni", c = "user_umsg_get")
    public native int RequestUserMessage(long handle, int appId,
                                         int startIndex, int number, String method_name, int method_name_len);

    /**
     * 删除用户消息
     *
     * @param handle
     * @param msgId  若msgId是0，则清空所有消息
     * @return
     */
    @JNIMethods(j = "user_umsg_del_jni", c = "user_umsg_del")
    public native int DeleteUserMessage(long handle, long msgId);

    /**
     * 获取未读消息数量
     *
     * @param handle
     * @param appId
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_umsg_unread_num_jni", c = "user_umsg_unread_num")
    public native int RequestUnreadMessageCount(long handle, long appId,
                                                String method_name, int method_name_len);

    /**
     * 修改未读消息为已读消息
     *
     * @param handle
     * @param msgId  若msgId为0，则将所有消息状态修改
     * @return
     */
    @JNIMethods(j = "user_umsg_unread_clean _jni", c = "user_umsg_unread_clean")
    public native int UpdateUnreadMessageState(long handle, long msgId);


    /**
     * 上报硬件信息接口
     *
     * @param handle
     * @param appId
     * @param json
     * @param json_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "log_hardware_report_jni", c = "log_hardware_report")
    public native int ReportHardwareInfo(long handle, int appId, String json,
                                         int json_len, String method_name, int method_name_len);

    /**
     * 上报已安装的应用信息
     *
     * @param handle
     * @param appId
     * @param json
     * @param json_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "log_app_report_jni", c = "log_app_report")
    public native int ReportInstalledAppInfo(long handle, int appId,
                                             String json, int json_len, String method_name, int method_name_len);

    /**
     * 上报用户反馈接口
     *
     * @param handle
     * @param appId
     * @param json
     * @param json_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "log_user_report_jni", c = "log_user_report")
    public native int ReportUserFeedbck(long handle, int appId, String json,
                                        int json_len, String method_name, int method_name_len);

    /**
     * 上报程序错误日志接口
     *
     * @param handle
     * @param appId
     * @param json
     * @param json_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "log_error_report_jni", c = "log_error_report")
    public native int ReportErrorLog(long handle, int appId, String json,
                                     int json_len, String method_name, int method_name_len);


    /**
     * 用户退出接口
     *
     * @param handle
     * @param appId
     * @param url
     * @param url_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_logout_jni", c = "user_logout")
    public native int UserLogout(long handle, int appId, String url,
                                 int url_len, String method_name, int method_name_len);

    /**
     * 用户登录
     *
     * @param handle
     * @param uid
     * @return
     */
    @JNIMethods(j = "user_login_set_jni", c = "user_login_set")
    public native int UserLoginSet(long handle, long uid);


    /**
     * JNI层回调的方法，方法名传给JNI
     *
     * @param resultCode  返回码
     * @param response
     * @param requestCode 请求码
     * @return
     */
    private int callback(int resultCode, String response, int requestCode) {
        return JNIRequest.getInstance().JNICallback(resultCode, requestCode,
                response);
    }

    /**
     * 消息通知的回调，由jni的so发起回调，java层根据不同的消息类型处理.
     *
     * @param resultCode
     * @param response
     * @param requestCode
     * @return
     */
    private int messageCallback(int resultCode, String response, int requestCode) {
        return JNIRequest.getInstance().JNIMessageCallback(resultCode,
                requestCode, response);
    }

    /**
     * 获取缓存大小（设置）
     *
     * @param handle
     * @param appId
     * @param meth
     * @param mlen
     * @return
     */
    @JNIMethods(j = "comca_get_cache_jni", c = "comca_get_cache")
    public native int RequestComcaGetCache(long handle, int appId, String meth,
                                           int mlen);

    /**
     * 清理缓存
     *
     * @param handle
     * @return
     */
    @JNIMethods(j = "comca_clean_cache_jni", c = "comca_clean_cache")
    public native int RequestComcaCleanCache(long handle);


    /**
     * 上报界面跳转的统计
     *
     * @param handle
     * @param json
     * @param json_len
     * @return
     */
    @JNIMethods(j = "log_page_report_jni", c = "log_page_report")
    public native int ReportPageSkipInfo(long handle, String json, int json_len);

    /**
     * 事件上报
     *
     * @param handle
     * @param json
     * @param json_len
     * @return
     */
    @JNIMethods(j = "log_event_report_jni", c = "log_event_report")
    public native int ReportEventInfo(long handle, String eventname, int en_len, String json, int json_len);

    /**
     * 应用退出
     *
     * @param handle
     * @return
     */
    @JNIMethods(j = "comca_app_exit_jni", c = "comca_app_exit")
    public native int ReportAppExit(long handle);


    /**
     * 获取远程服务器上用户信息
     *
     * @param handle
     * @param appId
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "get_net_user_info_jni", c = "get_net_user_info")
    public native int RequestRemouteUserInfo(long handle, int appId, int localflag, String method_name, int method_name_len);


    @JNIMethods(j = "comca_epg_get_jni", c = "comca_epg_get")
    public native int RequestEpgByGet(long handle, int useCache, int appId,
                                      String url, int url_len, String method_name, int method_name_len);

    /**
     * 获取epg数据接口
     *
     * @param handle
     * @param appId
     * @param cachetime       -1代表无限期缓存，0代表取网络数据，>0代表缓存多长时间
     * @param url
     * @param url_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "comca_epg_get_bytime_jni", c = "comca_epg_get_bytime")
    public native int RequestEpgByGetWithCache(long handle, int appId, int cachetime,
                                               String url, int url_len, String method_name, int method_name_len);


    /**
     * 读取所有预约(高清影视)
     *
     * @return
     */
    @JNIMethods(j = "comca_live_appointment_livestat_list_jni", c = "comca_live_appointment_livestat_list")
    public native int RequestComcaLiveAppointLivestatList(long handle, int appid, int startindex, int getnum, int stat, String meth, int mlen);


    /**
     * 读取预约数据(高清影视)
     *
     * @return
     */
    @JNIMethods(j = "comca_live_appointment_condition_list_jni", c = "comca_live_appointment_condition_list")
    public native int RequestComcaLiveAppointmentConditionList(long handle, int appid, int startindex, int getnum, int condition, String meth, int mlen);

    /**
     * 读取预约列表(高清影视)
     *
     * @return
     */
    @JNIMethods(j = "comca_live_appointment_stat_list_jni", c = "comca_live_appointment_stat_list")
    public native int RequestComcaLiveAppointmentStatList(long handle, int appid, int startindex, int getnum, int stat, String meth, int mlen);

    /**
     * 删除预约
     *
     * @return
     */
    @JNIMethods(j = "comca_live_appointment_del_jni", c = "comca_live_appointment_del")
    public native int RequestComcaLiveAppointmentDel(long handle, int appid, int allflag, long lid, long sid, String meth, int mlen);

    /**
     * 查询是否预约了一个直播
     *
     * @return
     */
    @JNIMethods(j = "comca_live_appointment_get_jni", c = "comca_live_appointment_get")
    public native int RequestComcaLiveAppointmentGet(long handle, int appid, long lid, long sid, String meth, int mlen);
/*------------------------*/

    /**
     * 读取预约列表
     *
     * @return
     */
    @JNIMethods(j = "comca_live_appointment_list_jni", c = "comca_live_appointment_list")
    public native int RequestComcaLiveAppointmentList(long handle, int appid, int startindex, int getnum, String meth, int mlen);


    /**
     * 添加预约
     *
     * @return
     */
    @JNIMethods(j = "comca_live_appointment_add_jni", c = "comca_live_appointment_add")
    public native int RequestComcaLiveAppointmentAdd(long handle, int appid, String json, int jsonlen, String meth, int mlen);


    /**
     * 判断本地某个专题是否收藏
     *
     * @param handle
     * @param appId
     * @param id
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_topic_collect_get_jni", c = "user_topic_collect_get")
    public native int RequestUserTopicCollectGet(long handle, int appId, long id, String method_name, int method_name_len);

    /**
     * 获取本地专题收藏
     *
     * @param handle
     * @param appId
     * @param startIndex
     * @param number
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_topic_collect_list_jni", c = "user_topic_collect_list")
    public native int RequestTopicCollectList(long handle, int appId,
                                              int startIndex, int number, String method_name, int method_name_len);


    /**
     * 删除本地专题收藏
     *
     * @param handle
     * @param appId
     * @param delAll
     * @param vid
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_topic_collect_del_jni", c = "user_topic_collect_del")
    public native int DeleteTopicCollect(long handle, int appId, int delAll,
                                         long vid, String method_name, int method_name_len);

    /**
     * 添加本地专题收藏
     *
     * @param handle
     * @param appId
     * @param json
     * @param json_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_topic_collect_add_jni", c = "user_topic_collect_add")
    public native int AddTopicCollect(long handle, int appId, String json,
                                      int json_len, String method_name, int method_name_len);

    /**
     * 判断本地某个人物是否关注
     *
     * @param handle
     * @param appId
     * @param id
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_person_follow_get_jni", c = "user_person_follow_get")
    public native int RequestUserPpersonFollowGet(long handle, int appId, String id, String method_name, int method_name_len);


    /**
     * 获取本地人物关注
     *
     * @param handle
     * @param appId
     * @param startIndex
     * @param number
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_person_follow_list_jni", c = "user_person_follow_list")
    public native int RequestPersonFollowList(long handle, int appId,
                                              int startIndex, int number, String method_name, int method_name_len);


    /**
     * 删除本地人物关注
     *
     * @param handle
     * @param appId
     * @param delAll
     * @param vid
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_person_follow_del_jni", c = "user_person_follow_del")
    public native int DeletePersonFollow(long handle, int appId, int delAll,
                                         String vid, String method_name, int method_name_len);

    /**
     * 添加本地人物关注
     *
     * @param handle
     * @param appId
     * @param json
     * @param json_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_person_follow_add_jni", c = "user_person_follow_add")
    public native int AddPersonFollow(long handle, int appId, String json,
                                      int json_len, String method_name, int method_name_len);


    /**
     * 判断本地某个标签是否订阅
     * 00000000000000
     *
     * @param handle
     * @param appId
     * @param id
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_tag_Subscribe_get_jni", c = "user_tag_Subscribe_get")
    public native int RequestUserTagSubscribeGet(long handle, int appId, String id, String method_name, int method_name_len);


    /**
     * 获取本地标签订阅
     *
     * @param handle
     * @param appId
     * @param startIndex
     * @param number
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_tag_Subscribe_list_jni", c = "user_tag_Subscribe_list")
    public native int RequestTagSubscribeList(long handle, int appId,
                                              int startIndex, int number, String method_name, int method_name_len);


    /**
     * 删除本地标签订阅
     *
     * @param handle
     * @param appId
     * @param delAll
     * @param vid
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_tag_Subscribe_del_jni", c = "user_tag_Subscribe_del")
    public native int DeleteTagSubscribe(long handle, int appId, int delAll,
                                         String vid, String method_name, int method_name_len);

    /**
     * 添加本地标签订阅
     *
     * @param handle
     * @param appId
     * @param json
     * @param json_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_tag_Subscribe_add_jni", c = "user_tag_Subscribe_add")
    public native int AddTagSubscribe(long handle, int appId, String json,
                                      int json_len, String method_name, int method_name_len);


    /**
     * 版本升级接口
     *
     * @param handle
     * @param appId
     * @param url
     * @param url_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_get_version_jni", c = "user_get_version")
    public native int UserGetVersion(long handle, int appId, String url,
                                     int url_len, String method_name, int method_name_len);


    /**
     * 插件版本检测
     *
     * @param handle
     * @param appId
     * @param url
     * @param url_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_get_plugin_version_jni", c = "user_get_plugin_version")
    public native int UserGetPluginVersion(long handle, int appId, String url,
                                           int url_len, String method_name, int method_name_len);


    /**
     * 关于我们接口,获取配置文件接口
     *
     * @param handle
     * @param appId
     * @param url
     * @param url_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "comca_get_aboutme_jni", c = "comca_get_aboutme")
    public native int GetAbountMe(long handle, int appId, String url,
                                  int url_len, String method_name, int method_name_len);


    /**
     * 设备认证接口
     *
     * @param handle
     * @param appId
     * @param json
     * @param json_url
     * @param url
     * @param url_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "comca_user_device_authen_jni", c = "comca_user_device_authen")
    public native int UserDeviceAuthen(long handle, int appId, String json, int json_url, String url,
                                       int url_len, String method_name, int method_name_len);


    /**
     * 获取 入口url地址接口
     *
     * @param handle
     * @param appId
     * @param url
     * @param url_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "comca_get_service_entry_jni", c = "comca_get_service_entry")
    public native int getEntryUrl(long handle, int appId, String url,
                                  int url_len, String method_name, int method_name_len);


    /**
     * 获取用户登录状态
     *
     * @param handle
     * @param appId
     * @param url
     * @param url_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "comca_get_login_stat_jni", c = "comca_get_login_stat")
    public native int getLoginState(long handle, int appId, String url,
                                    int url_len, String method_name, int method_name_len);


    /**
     * 产品鉴权接口
     *
     * @param handle
     * @param appId
     * @param url
     * @param url_len
     * @param json
     * @param json_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "product_authen_jni", c = "product_authen")
    public native int productAuth(long handle, int appId, String url,
                                  int url_len, String json, int json_len, String method_name, int method_name_len);

    /**
     * 产品鉴权接口NEW！！！
     *
     * @param handle
     * @param appId
     * @param url
     * @param url_len
     * @param json
     * @param json_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "videoinfo_product_authen_jni", c = "videoinfo_product_authen")
    public native int videoinfProductAuthen(long handle, int appId, String url,
                                            int url_len, String json, int json_len, String method_name, int method_name_len);

    /**
     * 轮播产品鉴权接口
     *
     * @param handle
     * @param appId
     * @param url
     * @param url_len
     * @param json
     * @param json_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "carousel_product_authen_jni", c = "carousel_product_authen")
    public native int carouselProductAuth(long handle, int appId, String url,
                                  int url_len, String json, int json_len, String method_name, int method_name_len);

    /**
     * 获取用户产品订单列表
     *
     * @param handle
     * @param appId
     * @param flag
     * @param url
     * @param url_len
     * @param pageNum
     * @param pageSize
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_order_list_jni", c = "user_order_list")
    public native int userOrderList(long handle, int appId, int flag, String url,
                                    int url_len, int pageNum, int pageSize, String method_name, int method_name_len);

    /**
     * 获取用户订购的产品包
     *
     * @param handle
     * @param appId
     * @param url
     * @param url_len
     * @param pageNum
     * @param pageSize
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_payment_list_jni", c = "user_payment_list")
    public native int userPaymentList(long handle, int appId, String url,
                                      int url_len, int pageNum, int pageSize, String method_name, int method_name_len);


    /**
     * 获取用户代金券列表
     *
     * @param handle
     * @param appId
     * @param url
     * @param url_len
     * @param pageNum
     * @param pageSize
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_vouchers_list_jni", c = "user_vouchers_list")
    public native int userVouchersList(long handle, int appId, String url,
                                       int url_len, int pageNum, int pageSize, String method_name, int method_name_len);

    /**
     * 获取用户订单状态
     *
     * @param handle
     * @param appId
     * @param url
     * @param url_len
     * @param json
     * @param json_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_order_stat_jni", c = "user_order_stat")
    public native int userOrderStatus(long handle, int appId, String url,
                                      int url_len, String json, int json_len, String method_name, int method_name_len);

    /**
     * 用户订单webview
     *
     * @param handle
     * @param appId
     * @param url
     * @param url_len
     * @param json
     * @param json_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "user_product_get_web_jni", c = "user_product_get_web")
    public native int userProductWeb(long handle, int appId, String url,
                                     int url_len, String json, int json_len, String method_name, int method_name_len);


    /**
     * 获取开机启动图片
     *
     * @param handle
     * @param appId
     * @param url
     * @param url_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "comca_get_splash_jni", c = "comca_get_splash")
    public native int getScreenSplash(long handle, int appId, String url,
                                      int url_len, String method_name, int method_name_len);

    /**
     * 获取系统时间
     * <p>
     * * @return
     */
    @JNIMethods(j = "comca_user_get_server_time_jni", c = "comca_user_get_server_time")
    public native long getServeTime(long handle);


    /**
     * 添加试看的时间
     *
     * @param handle
     * @param appId
     * @param json
     * @param jsonlen
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "comca_live_preview_time_add_jni", c = "comca_live_preview_time_add")
    public native int comcaLivePreviewTimeAdd(long handle, int appId, String json,
                                              int jsonlen, String method_name, int method_name_len);

    /**
     * 获取试看的时间
     *
     * @param handle
     * @param appId
     * @param lid
     * @param sid
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "comca_live_preview_time_get_jni", c = "comca_live_preview_time_get")
    public native int comcaLivePreviewTimeGet(long handle, int appId, long lid, long sid, String method_name, int method_name_len);


    /**
     * 获取登录二维码地址
     *
     * @param handle
     * @param appId
     * @param url
     * @param url_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "comca_tv_login_url_jni", c = "comca_tv_login_url")
    public native int getLoginUrl(long handle, int appId, String url, int url_len, String method_name, int method_name_len);


    /**
     * 获取外网IP
     *
     * @param handle
     * @return
     */
    @JNIMethods(j = "comca_user_get_outip_jni", c = "comca_user_get_outip")
    public native int getOutIP(long handle);


    /**
     * 上报接口错误日志
     * @param handle
     * @param json
     * @param json_len
     * @return
     */
    @JNIMethods(j = "log_data_error_report_jni", c = "log_data_error_report")
    public native int reportRequestDataError(long handle, String json, int json_len);

    /**
     * 更新系统时间
     *
     * @param handle
     * @return
     */
    @JNIMethods(j = "user_check_time_jni", c = "user_check_time")
    public native int getUserCheckTime(long handle);


    /**
     * 根据url去删除缓存，在出现缓存数据解析失败时调用此方法
     * @param handle
     * @param appId
     * @param url
     * @param url_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "comca_epg_del_cache_jni", c = "comca_epg_del_cache")
    public native int delCacheByUrl(long handle, int appId, String url, int url_len, String method_name, int method_name_len);


    /**
     * 请求跑马灯消息
     *
     * @param handle
     * @param appId
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "umsg_horseracelamp_get_jni", c = "umsg_horseracelamp_get")
    public native int RequestMarqueeNotice(long handle, int appId, String method_name, int method_name_len);

    /**
     * 判断本地数据是否存在
     *
     * @param handle
     * @param appId
     * @param dbName//方法名
     * @param idName//主键名
     * @param id
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "vcomca_save_data_get_jni", c = "comca_save_data_get")
    public native int RequestLocalDataById(long handle, int appId, String dbName, String idName,
                                           String id, String method_name, int method_name_len);

    /**
     * 获取本地数据
     *
     * @param handle
     * @param appId
     * @param startIndex
     * @param number
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "comca_save_data_list_jni", c = "comca_save_data_list")
    public native int RequestLocalDataList(long handle, int appId, String dbName, String idName,
                                           int startIndex, int number, String method_name, int
                                                   method_name_len);

    /**
     * 删除本地数据
     *
     * @param handle
     * @param appId
     * @param delAll
     * @param vid
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "comca_save_data_del_jni", c = "comca_save_data_del")
    public native int DeleteLocalData(long handle, int appId, int delAll, String dbName, String
            idName, String vid, String method_name, int method_name_len);

    /**
     * 添加本地数据
     *
     * @param handle
     * @param appId
     * @param json
     * @param json_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "comca_save_data_add_jni", c = "comca_save_data_add")
    public native int AddLocalData(long handle, int appId, String dbName, String idName, String
            json, int json_len, String method_name, int method_name_len);

    /**
     * 微信登录
     *
     * @param handle
     * @param appid
     * @param url
     * @param url_len
     * @param json
     * @param json_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "comca_mobile_wxlogin_jni", c = "comca_mobile_wxlogin")
    public native int ComcaMobileWxlogin(long handle, int appid, String url,  int url_len,
                                           String json, int json_len, String method_name, int method_name_len);

    /**
     * 获取直播节目的直播状态
     *
     * @param handle
     * @param appid
     * @param cachetime
     * @param json
     * @param json_len
     * @param url
     * @param url_len
     * @param key
     * @param key_len
     * @param method_name
     * @param method_name_len
     * @return
     */
    @JNIMethods(j = "comca_epg_post_bytime_jni", c = "comca_epg_post_bytime")
    public native int LiveStatus(long handle, int appid, int cachetime,
                                         String json, int json_len, String url, int url_len,
                                 String key, int key_len, String method_name, int method_name_len);
}