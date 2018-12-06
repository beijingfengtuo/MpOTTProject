package cn.cibnmp.ott.jni;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.DisableAppEventBean;
import cn.cibnmp.ott.bean.MarqueeMsgEvent;
import cn.cibnmp.ott.bean.PayResultEventBean;
import cn.cibnmp.ott.bean.UserMsg;
import cn.cibnmp.ott.utils.Lg;
import de.greenrobot.event.EventBus;


public class JNIRequest {

    private static final String TAG = "HttpRequest";
    private ConcurrentHashMap<Integer, HttpResponseListener> requestCache; // 线程安全
    private ConcurrentHashMap<Integer, SimpleHttpResponseListener> requestCache2; // 线程安全
    private static JNIRequest request;
    public static final String CALLBACKMETHODNAME = "callback";
    private static final int CALLBACKMETHODLENTH = CALLBACKMETHODNAME
            .getBytes().length;
    public static final String MESSAGECALLBACK = "messageCallback";
    private static int MESSAGECALLBACKLENGTH = MESSAGECALLBACK.getBytes().length;

    private static final int startappId = 1000;
    private AtomicInteger appIdOffset = new AtomicInteger();
    private AtomicInteger callbackCount = new AtomicInteger();

    private JNIRequest() {
        requestCache = new ConcurrentHashMap<>();
        requestCache2 = new ConcurrentHashMap<>();
    }

    public static JNIRequest getInstance() {
        if (request == null) {
            request = new JNIRequest();
        }
        return request;
    }


    /**
     * 获取图片资源
     *
     * @param fid
     * @param listener
     */
    protected void getImageByFid(String fid, HttpResponseListener listener) {
        if (TextUtils.isEmpty(fid) || listener == null) {
            Log.e(TAG, "getImageByFid parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getImageByFid --> fid = " + fid + " , appId = " + appId);
        JNIInterface.getInstance().RequestImage(App.handleId, appId, fid, 0,
                CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }


    /**
     * 获取本地收藏记录
     *
     * @param startIndex
     * @param number
     */
    protected void getLocalCollectList(Integer startIndex, Integer number,
                                       HttpResponseListener listener) {
        if (startIndex < 0 || number < 0 || listener == null) {
            Log.e(TAG,
                    "getLocalCollectList parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getLocalCollectList --> startIndex = " + startIndex
                + " , number = " + number);
        JNIInterface.getInstance().RequestVideoCollectList(App.handleId, appId,
                startIndex, number, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 删除本地收藏
     *
     * @param delAll   是否删除所有的记录，true是删除所有.
     * @param vid      节目集id
     * @param listener
     */
    protected void deleteLocalCollect(Boolean delAll, Long vid,
                                      HttpResponseListener listener) {
        if (listener == null) {
            Log.e(TAG,
                    "deleteLocalCollect parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "deleteLocalCollect --> delAll = " + delAll + " , vid = "
                + vid);
        JNIInterface.getInstance().DeleteVideoCollect(App.handleId, appId,
                delAll ? 1 : 0, vid, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);

    }

    /**
     * 添加收藏记录到本地
     *
     * @param json
     * @param listener
     */
    protected void addLocalCollect(String json, HttpResponseListener listener) {
        if (listener == null || json == null) {
            Log.e(TAG,
                    "addLocalCollect parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "addLocalCollect --> json = " + json);
        JNIInterface.getInstance()
                .AddVideoCollect(App.handleId, appId, json,
                        json.getBytes().length, CALLBACKMETHODNAME,
                        CALLBACKMETHODLENTH);

    }

    /**
     * 判断本地播放记录是否存在
     */
    protected void getRequestVideoRecordGet(Long id, HttpResponseListener listener) {
        if (id < 0 || listener == null) {
            Log.e(TAG,
                    "getRequestVideoRecordGet parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getRequestVideoRecordGet --> id = " + id);
        JNIInterface.getInstance().RequestVideoRecordGet(App.handleId, appId, id,
                CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 判断本地收藏记录是否存在
     */
    protected void getRequestVideoCollectGet(Long id, HttpResponseListener listener) {
        if (id < 0 || listener == null) {
            Log.e(TAG,
                    "getRequestVideoCollectGet parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getRequestVideoCollectGet --> id = " + id);
        JNIInterface.getInstance().RequestVideoCollectGet(App.handleId, appId, id,
                CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }


    /**
     * 获取本地播放记录
     *
     * @param startIndex
     * @param number
     */
    protected void getLocalRecordList(Integer startIndex, Integer number,
                                      HttpResponseListener listener) {
        if (startIndex < 0 || number < 0 || listener == null) {
            Log.e(TAG,
                    "getLocalRecordList parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getLocalRecordList --> startIndex = " + startIndex
                + " , number = " + number);
        JNIInterface.getInstance().RequestVideoRecordList(App.handleId, appId,
                startIndex, number, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 删除本地播放记录
     *
     * @param delAll   是否删除所有的记录，true是删除所有.
     * @param vid      节目集id
     * @param listener
     */
    protected void deleteLocalRecord(Boolean delAll, Long vid,
                                     HttpResponseListener listener) {
        if (listener == null) {
            Log.e(TAG,
                    "deleteLocalRecord parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "deleteLocalRecord --> delAll = " + delAll + " , vid = "
                + vid);
        JNIInterface.getInstance().DeleteVideoRecord(App.handleId, appId,
                delAll ? 1 : 0, vid, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);

    }

    /**
     * 添加播放记录到本地
     *
     * @param json
     * @param listener
     */
    protected void addLocalRecord(String json, HttpResponseListener listener) {
        if (listener == null || json == null) {
            Log.e(TAG, "addLocalRecord parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "addLocalRecord --> json = " + json);
        JNIInterface.getInstance()
                .AddVideoRecord(App.handleId, appId, json,
                        json.getBytes().length, CALLBACKMETHODNAME,
                        CALLBACKMETHODLENTH);

    }


    /**
     * 上报硬件信息
     */
    protected void reportHardwareInfo(String url, String json,
                                      HttpResponseListener listener) {
        if (listener == null || json == null || url == null) {
            Log.e(TAG,
                    "reportHardwareInfo parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "reportHardwareInfo --> url = " + url + " , json = " + json
                + " , appId = " + appId);
        JNIInterface.getInstance().ReportHardwarInfo(App.handleId, appId, url,
                url.getBytes().length, json, json.getBytes().length,
                CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }


    /*
    * 上报客户端网络信息
    */
    protected void ReportNetInfo(Integer nettype, String ip) {
        if (ip == null) {
            Log.e(TAG, "ReportNetInfo parameters is invalid , request failed .");
            return;
        }
        JNIInterface.getInstance().ReportNetInfo(App.handleId, nettype, ip,
                ip.getBytes().length);
    }


    /**
     * 获取用户信息接口
     *
     * @param listener
     */
    protected void getUserInfo(HttpResponseListener listener) {
        if (listener == null) {
            Log.e(TAG, "getUserInfo parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getUserInfo --> appId = " + appId);
        JNIInterface.getInstance().RequestUserInfo(App.handleId, appId,
                CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取用户消息列表
     *
     * @param startIndex
     * @param number
     * @param listener
     */
    protected void getUserMessageList(Integer startIndex, Integer number,
                                      HttpResponseListener listener) {
        if (listener == null || startIndex < 0 || number <= 0) {
            Log.e(TAG,
                    "getUserMessageList parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getUserMessageList --> appId = " + appId);
        JNIInterface.getInstance().RequestUserMessage(App.handleId, appId,
                startIndex, number, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 删除用户消息
     *
     * @param msgId 要删除的消息id ，若为0，则清空所有消息
     */
    protected void delUserMessageById(Long msgId) {
        Log.d(TAG, "delUserMessageById --> msgId = " + msgId);
        JNIInterface.getInstance().DeleteUserMessage(App.handleId, msgId);
    }

    /**
     * 获取用户未读消息数量
     *
     * @param listener
     */
    protected void getUnReadMessageCount(HttpResponseListener listener) {
        if (listener == null) {
            Log.e(TAG,
                    "getUnReadMessageCount parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getUnReadMessageCount --> appId = " + appId);
        JNIInterface.getInstance().RequestUnreadMessageCount(App.handleId,
                appId, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 更改未读消息状态为已读
     *
     * @param msgId 要更改的消息id ，若为0，则更改所有消息状态
     */
    protected void updateUnreadMessageState(Long msgId) {
        Log.d(TAG, "updateUnreadMessageState --> msgId = " + msgId);
        JNIInterface.getInstance()
                .UpdateUnreadMessageState(App.handleId, msgId);
    }


    /**
     * 上报硬件信息
     *
     * @param json
     * @param listener
     */
    protected void reportHardwareInfo(String json, HttpResponseListener listener) {
        if (listener == null || json == null) {
            Log.e(TAG,
                    "reportHardwareInfo parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "reportHardwareInfo -->  appId = " + appId + " , json = "
                + json);
        JNIInterface.getInstance().ReportHardwareInfo(App.handleId, appId,
                json, json.getBytes().length, CALLBACKMETHODNAME,
                CALLBACKMETHODLENTH);
    }

    /**
     * 上报已安装的应用信息
     *
     * @param json
     * @param listener
     */
    protected void reportInstalledAppInfo(String json,
                                          HttpResponseListener listener) {
        if (listener == null || json == null) {
            Log.e(TAG,
                    "reportInstalledAppInfo parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "reportInstalledAppInfo -->  appId = " + appId + " , json = "
                + json);
        JNIInterface.getInstance().ReportInstalledAppInfo(App.handleId, appId,
                json, json.getBytes().length, CALLBACKMETHODNAME,
                CALLBACKMETHODLENTH);
    }

    /**
     * 上报用户反馈接口
     *
     * @param json
     * @param listener
     */
    protected void reportUserFeedback(String json, HttpResponseListener listener) {
        if (listener == null || json == null) {
            Log.e(TAG,
                    "reportUserFeedback parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "reportUserFeedback -->  appId = " + appId + " , json = "
                + json);
        JNIInterface.getInstance()
                .ReportUserFeedbck(App.handleId, appId, json,
                        json.getBytes().length, CALLBACKMETHODNAME,
                        CALLBACKMETHODLENTH);
    }

    /**
     * 上报错误日志
     *
     * @param json
     * @param listener
     */
    protected void ReportErrorLog(String json, HttpResponseListener listener) {
        if (listener == null || json == null) {
            Log.e(TAG, "ReportErrorLog parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "ReportErrorLog -->  appId = " + appId + " , json = " + json);
        JNIInterface.getInstance()
                .ReportErrorLog(App.handleId, appId, json,
                        json.getBytes().length, CALLBACKMETHODNAME,
                        CALLBACKMETHODLENTH);
    }

    /**
     * 上报播放日志
     *
     * @param json
     */
    protected void ReportPlayLog(String json) {
        if (json == null) {
            Log.e(TAG, "ReportPlayLog parameters is invalid , request failed .");
            return;
        }
        Log.d(TAG, "ReportPlayLog -->   json = " + json);
        int a = JNIInterface.getInstance().ReportPlayLog(App.handleId, 2, json,
                json.getBytes().length);
    }

    /**
     * 获取缓存大小（设置）
     *
     * @param listener
     */
    protected void getRequestComcaGetCache(HttpResponseListener listener) {
        if (listener == null) {
            Log.e(TAG,
                    "getRequestComcaGetCache parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getRequestComcaGetCache --> appId = " + appId);
        JNIInterface.getInstance().RequestComcaGetCache(App.handleId, appId,
                CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 清理缓存（设置）
     *
     * @param listener
     */
    protected void getRequestComcaCleanCache(HttpResponseListener listener) {
        if (listener == null) {
            Log.e(TAG,
                    "getRequestComcaCleanCache parameters is invalid , request failed .");
            return;
        }
        JNIInterface.getInstance().RequestComcaCleanCache(App.handleId);
    }


    /**
     * 用户退出接口
     *
     * @param listener
     */
    protected void userLogout(String url, HttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "userLogout parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "userLogout --> appId = " + appId);
        JNIInterface.getInstance().UserLogout(App.handleId, appId, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 用户登录
     */
    protected void userLoginSet(Long uid) {
        Log.d(TAG, "userLoginSet --> uid = " + uid);
        JNIInterface.getInstance().UserLoginSet(App.handleId, uid);
    }


    /**
     * 上报界面跳转log
     *
     * @param json
     */
    protected void reportPageSkipLog(String json) {
        if (json == null) {
            Log.e(TAG,
                    "reportPageSkipLog parameters is invalid , request failed .");
            return;
        }
        Log.d(TAG, "reportPageSkipLog -->  , json = " + json);
        JNIInterface.getInstance().ReportPageSkipInfo(App.handleId, json, json
                .getBytes().length);
    }

    /**
     * 上报事件
     *
     * @param json
     */
    protected void reportEventLog(String eventName, String json) {
        if (eventName == null || json == null) {
            Log.e(TAG, "reportEventLog parameters is invalid , request failed .");
            return;
        }
        Log.d(TAG, "reportEventLog -->  , key = " + eventName + "  json = " + json);
        JNIInterface.getInstance().ReportEventInfo(App.handleId, eventName, eventName
                .getBytes().length, json, json.getBytes().length);
    }

    /**
     * 应用退出
     */
    public void reportAppExit() {
        JNIInterface.getInstance().ReportAppExit(App.handleId);
    }


    /**
     * 获取直播相关人物的列表
     *
     * @param url
     * @param liveId
     * @param listener
     */
//    protected void getLiveRelatedPersonList(String url, Long liveId, HttpResponseListener
// listener) {
//        if (url == null || listener == null) {
//            Log.e(TAG,
//                    "getLiveRelatedPersonList parameters is invalid , request failed .");
//            return;
//        }
//        int appId = putIntoRequestQueue(listener);
//        Log.d(TAG, "getLiveRelatedPersonList -->  appId = " + appId + " , liveId = "
//                + liveId);
//        JNIInterface.getInstance().RequestLiveRelatedPersonList(App.handleId,
// appId, liveId,
//                url, url.getBytes().length, CALLBACKMETHODNAME,
//                CALLBACKMETHODLENTH);
//
//    }


    /**
     * 获取服务器上用户信息
     *
     * @param listener
     */
    protected void getRemoteUserInfo(Integer localflag, HttpResponseListener listener) {
        if (listener == null || localflag == null) {
            Log.e(TAG,
                    "getRemoteUserInfo parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getRemoteUserInfo -->  appId = " + appId + " , localflag = " + localflag);
        JNIInterface.getInstance().RequestRemouteUserInfo(App.handleId, appId,
                localflag,
                CALLBACKMETHODNAME,
                CALLBACKMETHODLENTH);

    }


    /**--------------------以下是新版本高清影视新接口---------------------------*/


    /**
     * 获取首页导航数据
     *
     * @param url
     */
    protected void getHomeNavigationList(String url, String epgId, Integer cachetime,
                                         HttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getHomeNavigationList parameters is invalid , request failed .");
            return;
        }

        url = String.format("%s/navigation?epgId=%s", url, epgId);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getHomeNavigationList --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }


    /**
     * 获取首页导航数据
     *
     * @param url
     */
    protected void getHomeNavigationList(String url, String epgId, Integer cachetime,
                                         SimpleHttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getHomeNavigationList parameters is invalid , request failed .");
            return;
        }

        url = String.format("%s/navigation?epgId=%s", url, epgId);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getHomeNavigationList --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取专题数据接口
     *
     * @param url
     */
    protected void getTopicList(String url, String epgId, String navigationOrTopicId, Integer
            cachetime, SimpleHttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getTopicList parameters is invalid , request failed .");
            return;
        }

        url = String.format("%s/indexContent?epgId=%s&navigationOrTopicId=%s", url, epgId,
                navigationOrTopicId);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getTopicList --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取首页导航数据
     *
     * @param url
     */
    protected void getHomeNavContent(String url, String epgId, String navId, Integer cachetime,
                                     HttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getHomeNavContent parameters is invalid , request failed .");
            return;
        }

        url = String.format("%s/indexContent?epgId=%s&navigationOrTopicId=%s", url, epgId, navId);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getHomeNavContent --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }


    /**
     * 获取首页导航数据
     *
     * @param url
     */
    protected void getHomeNavContent(String url, String epgId, String navId, Integer cachetime,
                                     SimpleHttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getHomeNavContent parameters is invalid , request failed .");
            return;
        }

        url = String.format("%s/indexContent?epgId=%s&navigationOrTopicId=%s", url, epgId, navId);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getHomeNavContent --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取启动开机图片信息接口
     *
     * @param url
     * @param listener
     */
    protected void getSplashInfo(String url, HttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getSplashInfo parameters is invalid , request failed .");
            return;
        }

        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getSplashInfo --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().getScreenSplash(App.handleId, appId, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }


    /**
     * 读取列表页菜单接口
     */
    protected void getListMenuData(String url, Integer cachetime, SimpleHttpResponseListener
            listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getListContent parameters is invalid , request failed .");
            return;
        }
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 读取列表页数据接口（1.8.	栏目内容接口）
     */
    protected void getListProgramData(String url, Integer cachetime, SimpleHttpResponseListener
            listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getListContent parameters is invalid , request failed .");
            return;
        }
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取筛选项数据
     *
     * @param url
     * @param cachetime
     * @param listener
     */
    protected void getFilterCondition(String url, Integer cachetime, SimpleHttpResponseListener
            listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getListContent parameters is invalid , request failed .");
            return;
        }
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取经过筛选的数据
     *
     * @param url
     * @param cachetime
     * @param listener
     */
    protected void getFitlerData(String url, Integer cachetime, SimpleHttpResponseListener
            listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getListContent parameters is invalid , request failed .");
            return;
        }
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * TODO Add 获取直播节目的直播状态
     *
     * @param url
     * @param cachetime
     * @param json
     * @param key
     * @param listener
     */
    private void getLiveStatus(String url, Integer cachetime, String json, String key, SimpleHttpResponseListener
            listener){
        if (listener == null || url == null) {
            Log.e(TAG,
                    "getLiveStatus is invalid , request failed .");
            return;
        }
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getLiveStatus -->  appId = " + appId + " , url = " + url);
        JNIInterface.getInstance().LiveStatus(App.handleId, appId, cachetime, json, json.getBytes().length, url, url.getBytes().length, key,
                key.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 轮播频道接口
     *
     * @param url
     */
    protected void getCarouselChannel(String url, String epjid, Integer
            cachetime, SimpleHttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getCarouselChannel parameters is invalid , request failed .");
            return;
        }

        url = String.format("%s/v1/loopChannelList?epgId=%s", url, epjid);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 轮播节目单接口
     *
     * @param url
     */
    protected void getCarouselProgram(String url, String epjid, String channelId, Integer
            cachetime, SimpleHttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getCarouselProgram parameters is invalid , request failed .");
            return;
        }

        url = String.format("%s/v1/loopChannelInfo?epgId=%s&channelId=%s", url, epjid, channelId);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }


    /**
     * 详情页认证接口
     *
     * @param url
     */
    protected void getDetailProductAuth(String url, String json,
                                        HttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getDetailProductAuth parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().productAuth(App.handleId, appId,
                url, url.getBytes().length, json, json.getBytes().length,
                CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 轮播认证接口
     *
     * @param url
     */
    protected void getCarouselProductAuth(String url, String json,
                                          HttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getDetailProductAuth parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().carouselProductAuth(App.handleId, appId,
                url, url.getBytes().length, json, json.getBytes().length,
                CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取详情页数据
     *
     * @param url
     */
    protected void getDetailContent(String url, String epgId, String contentId, Integer
            cachetime, SimpleHttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getDetailContent parameters is invalid , request failed .");
            return;
        }

        url = String.format("%s/video?epgId=%s&contentId=%s", url, epgId, contentId);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取直播详情页数据
     *
     * @param url
     */
    protected void getLiveDetailContent(String url, String epgId, String contentId, Integer
            cachetime, SimpleHttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getLiveDetailContent parameters is invalid , request failed .");
            return;
        }

        url = String.format("%s/live?epgId=%s&contentId=%s", url, epgId, contentId);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取点播地址
     *
     * @param url
     */
    protected void getVideoUrl(String url, String epgId, String contentId, String childId, Integer
            cachetime, SimpleHttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getVideoUrl parameters is invalid , request failed .");
            return;
        }

        url = String.format("%s/videoMedia?epgId=%s&contentId=%s&childId=%s", url, epgId,
                contentId, childId);
//        url = String.format("%s/video?epgId=%s&contentId=%s", url, epgId, contentId);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取直播播放地址
     *
     * @param url
     */
    protected void getRequestLiveUrl(String url, String epgId, String contentId, String
            childId, String status, Integer cachetime, SimpleHttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getRequestLiveUrl parameters is invalid , request failed .");
            return;
        }
        url = String.format("%s/liveMedia?epgId=%s&contentId=%s&childId=%s&status=%s", url, epgId,
                contentId, childId, status);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getRequestLiveUrl --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 详情页认证接口(新的)！！！
     *
     * @param url
     */
    protected void getVideoinfProductAuthen(String url, String json,
                                            HttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "videoinfProductAuthen parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().videoinfProductAuthen(App.handleId, appId,
                url, url.getBytes().length, json, json.getBytes().length,
                CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 刷新直播状态接口
     *
     * @param url
     */
    protected void getRequestLiveStatus(String url, String epgId, String contentId, String
            childId, Integer cachetime, SimpleHttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getRequestLiveStatus parameters is invalid , request failed .");
            return;
        }
        url = String.format("%s/liveStatus?epgId=%s&contentId=%s&childId=%s", url, epgId,
                contentId, childId);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getRequestLiveStatus --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                0, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取导航列表接口
     *
     * @param url
     */
    protected void getChannelList(String url, String epgId, Integer cachetime,
                                  SimpleHttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getChannelList parameters is invalid , request failed .");
            return;
        }
        url = String.format("%s/channel?epgId=%s", url, epgId);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取热推
     *
     * @param url
     */
    protected void getHotword(String url, String epgId, Integer cachetime,
                              SimpleHttpResponseListener
                                      listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getHotword parameters is invalid , request failed .");
            return;
        }
        url = String.format("%s/hotword?epgId=%s", url, epgId);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 板块数据接口
     *
     * @param url
     */
    protected void getBlockContent(String url, String epgId, Integer type, Integer cachetime,
                                   SimpleHttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getBlockContent parameters is invalid , request failed .");
            return;
        }
        url = String.format("%s/blockContent?epgId=%s&type=%d", url, epgId, type);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url, url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }


    /***
     *  模糊搜索
     *
     */
    protected void getFuzzySearchResult(String url, String epgId,Integer subjectId,Integer pageSize,
                                   Integer pageNum, String keywords, Integer cachetime,
                                   SimpleHttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getFuzzySearchResult parameters is invalid , request failed .");
            return;
        }
        //http://114.247.94.86:8010/searchFeferral?epgId=1031&subjectId=0
        // &pageSize=10&pageNum=0&keywords=j
        url = String.format("%s/searchFeferral?epgId=%s&subjectId=%s&pageSize=%s&pageNum=%s&keywords=%s",
                url, epgId,subjectId, pageSize,pageNum ,keywords);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 搜索接口
     *
     * @param url
     */
    protected void getSearchResult(String url, String epgId,
                                   String keywords, Integer subjectId, Integer cachetime,String searchtype,
                                   SimpleHttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getSearchResult parameters is invalid , request failed .");
            return;
        }
        url = String.format("%s/search?epgId=%s&keywords=%s&searchtype=%s",
                url, epgId, keywords, searchtype);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 读取所有预约(高清影视)
     *
     * @return
     */
    protected void getRequestComcaLiveAppointLivestatList(Integer startindex, Integer getnum,
                                                          Integer stat, HttpResponseListener
                                                                  listener) {
        if (startindex == null || getnum == null || listener == null) {
            Log.e(TAG, "getRequestComcaLiveAppointLivestatList parameters is invalid , request " +
                    "failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getRequestComcaLiveAppointLivestatList -->  appId = " + appId + " , startday " +
                "= " + startindex);
        JNIInterface.getInstance().RequestComcaLiveAppointLivestatList(App.handleId,
                appId, startindex, getnum, stat, CALLBACKMETHODNAME,
                CALLBACKMETHODLENTH);
    }

    /**
     * 查询用户预约数据(高清影视)
     */
    protected void getRequestComcaLiveAppointmentConditionList(Integer startindex, Integer
            getnum, Integer condition, HttpResponseListener listener) {
        if (startindex == null || getnum == null || listener == null) {
            Log.e(TAG, "getRequestComcaLiveAppointmentConditionList parameters is invalid , " +
                    "request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getRequestComcaLiveAppointmentConditionList -->  appId = " + appId + " , " +
                "startday = " + startindex);
        JNIInterface.getInstance().RequestComcaLiveAppointmentConditionList(App
                        .handleId, appId, startindex, getnum, condition, CALLBACKMETHODNAME,
                CALLBACKMETHODLENTH);
    }

    /**
     * 读取预约列表（高清影视）
     */
    protected void getRequestComcaLiveAppointmentStatList(Integer startindex, Integer getnum,
                                                          Integer stat, HttpResponseListener
                                                                  listener) {
        if (startindex == null || getnum == null || listener == null) {
            Log.e(TAG, "getRequestComcaLiveAppointmentStatList parameters is invalid , request " +
                    "failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getRequestComcaLiveAppointmentStatList -->  appId = " + appId + " , startday " +
                "= " + startindex);
        JNIInterface.getInstance().RequestComcaLiveAppointmentStatList(App.handleId,
                appId, startindex, getnum, stat, CALLBACKMETHODNAME,
                CALLBACKMETHODLENTH);
    }

    /**
     * 读取预约列表
     */
    protected void getRequestComcaLiveAppointmentList(Integer startindex, Integer getnum,
                                                      HttpResponseListener listener) {
        if (startindex == null || getnum == null || listener == null) {
            Log.e(TAG, "getRequestComcaLiveAppointmentList parameters is invalid , request failed" +
                    " .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getRequestComcaLiveAppointmentList -->  appId = " + appId + " , startday = "
                + startindex);
        JNIInterface.getInstance().RequestComcaLiveAppointmentList(App.handleId,
                appId, startindex, getnum, CALLBACKMETHODNAME,
                CALLBACKMETHODLENTH);
    }

    /**
     * 删除预约
     */
    protected void getRequestComcaLiveAppointmentDel(Integer allflag, Long lid, Long sid,
                                                     HttpResponseListener listener) {
        if (allflag == null || listener == null) {
            Log.e(TAG, "RequestComcaLiveAppointmentDel parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "RequestComcaLiveAppointmentDel -->  appId = " + appId + " , allflag = " +
                allflag);
        JNIInterface.getInstance().RequestComcaLiveAppointmentDel(App.handleId,
                appId, allflag, lid, sid, CALLBACKMETHODNAME,
                CALLBACKMETHODLENTH);
    }

    /**
     * 添加预约
     */
    protected void getRequestComcaLiveAppointmentAdd(String json, HttpResponseListener listener) {
        if (json == null || listener == null) {
            Log.e(TAG, "RequestComcaLiveAppointmentAdd parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "RequestComcaLiveAppointmentAdd -->  appId = " + appId + " , json = " + json);
        JNIInterface.getInstance().RequestComcaLiveAppointmentAdd(App.handleId,
                appId, json, json.getBytes().length, CALLBACKMETHODNAME,
                CALLBACKMETHODLENTH);
    }

    /**
     * 查询是否预约了一个直播
     */
    protected void getRequestComcaLiveAppointmentQueryAdd(Long lid, Long sid, HttpResponseListener
            listener) {
        if (listener == null) {
            Log.e(TAG, "getRequestComcaLiveAppointmentGet parameters is invalid , request failed " +
                    ".");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getRequestComcaLiveAppointmentGet -->  appId = " + appId + " , lid = " + lid);
        JNIInterface.getInstance().RequestComcaLiveAppointmentGet(App.handleId,
                appId, lid, sid, CALLBACKMETHODNAME,
                CALLBACKMETHODLENTH);
    }


    /**
     * 升级检测接口
     */
    protected void getUpgradeInfo(String url, HttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getUpgradeInfo parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getUpgradeInfo -->  appId = " + appId + " , url = " + url);
        JNIInterface.getInstance().UserGetVersion(App.handleId, appId, url, url
                        .getBytes().length, CALLBACKMETHODNAME,
                CALLBACKMETHODLENTH);
    }


    /**
     * 插件升级检测接口
     */
    protected void getPluginUpgradeInfo(String url, HttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getPluginUpgradeInfo parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getPluginUpgradeInfo -->  appId = " + appId + " , url = " + url);
        JNIInterface.getInstance().UserGetPluginVersion(App.handleId, appId, url, url
                        .getBytes().length, CALLBACKMETHODNAME,
                CALLBACKMETHODLENTH);
    }


    /**
     * 关于我们接口,获取配置文件接口
     */
    protected void getAboutUs(String url, HttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getAboutUs parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getAboutUs -->  appId = " + appId + " , url = " + url);
        JNIInterface.getInstance().GetAbountMe(App.handleId, appId, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }


    /**
     * 获取入口地址接口
     */
    protected void getEntryUrl(String url, HttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getEntryUrl parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getEntryUrl -->  appId = " + appId + " , url = " + url);
        JNIInterface.getInstance().getEntryUrl(App.handleId, appId, url, url.getBytes
                        ().length, CALLBACKMETHODNAME,
                CALLBACKMETHODLENTH);
    }


    /**
     * 获取用户登录状态接口，海诺专用
     */
    protected void getUserLoginStatus(String url, HttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getUserLoginStatus parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getUserLoginStatus -->  appId = " + appId + " , url = " + url);
        JNIInterface.getInstance().getLoginState(App.handleId, appId, url, url
                        .getBytes().length, CALLBACKMETHODNAME,
                CALLBACKMETHODLENTH);
    }


    /**
     * 设备认证接口,新版高清影视使用
     */
    protected void deviceAuth(String url, String json, HttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "deviceAuth parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "deviceAuth -->  appId = " + appId + " , url = " + url + " , json = " + json);
        JNIInterface.getInstance().UserDeviceAuthen(App.handleId, appId, json, json
                        .getBytes().length,
                url, url.getBytes().length, CALLBACKMETHODNAME,
                CALLBACKMETHODLENTH);
    }


    protected int JNICallback(int result, int appId, String response) {
        Log.d(TAG, "JNICallback " + callbackCount.addAndGet(1)
                + " --> result = " + result + " , appId = " + appId
                + " , response = " + response);
        if (requestCache != null && requestCache.containsKey(appId)) {
            HttpResponseListener listener = requestCache.get(appId);
            return httpResponseListenerCallBack(appId, result, listener,
                    response);
        } else if (requestCache2 != null && requestCache2.containsKey(appId)) {
            SimpleHttpResponseListener listener2 = requestCache2.get(appId);
            return httpResponseListenerCallBack(appId, result, listener2,
                    response);
        }
        return -1;
    }

    private synchronized int putIntoRequestQueue(HttpResponseListener listener) {
        int appId = startappId + appIdOffset.addAndGet(1);
        if (requestCache.containsKey(appId)) {
            Log.e(TAG, "putIntoRequestQueue appId = " + appId + " has exist .");
        }
        requestCache.put(appId, listener);
        return appId;
    }

    private synchronized int putIntoRequestQueue(SimpleHttpResponseListener listener) {
        int appId = startappId + appIdOffset.addAndGet(1);
        if (requestCache2.containsKey(appId)) {
            Log.e(TAG, "putIntoRequestQueue appId = " + appId + " has exist .");
        }
        requestCache2.put(appId, listener);
        return appId;
    }


    private int httpResponseListenerCallBack(int key, int result,
                                             HttpResponseListener listener, String response) {
        try {
            if (listener == null) {
                Log.e(TAG, "listener is null");
                return -1;
            }
            requestCache.remove(key);
            if (result == ResponseCode.SUCCESS_CODE) {
                listener.onSuccess(response);
            } else {
                listener.onError(result + "");
            }
        } catch (Exception e) {
            // TODO: 18/3/12 开机图片404特殊处理
            listener.onError(result + "");
        }
        return 0;
    }

    private int httpResponseListenerCallBack(int key, int result,
                                             SimpleHttpResponseListener listener, String response) {
        try {
            if (listener == null) {
                Log.e(TAG, "listener is null");
                return -1;
            }
            requestCache2.remove(key);
            if (result == ResponseCode.SUCCESS_CODE) {
                listener.handleResponse(response);
            } else {
                listener.handleError(result);
            }

        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 判断专题收藏是否存在
     */
    protected void getRequestUserTopicCollectGet(Long id, HttpResponseListener listener) {
        if (id < 0 || listener == null) {
            Log.e(TAG,
                    "getRequestUserTopicCollectGet parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getRequestUserTopicCollectGet --> id = " + id);
        JNIInterface.getInstance().RequestUserTopicCollectGet(App.handleId, appId,
                id, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取本地专题收藏
     *
     * @param startIndex
     * @param number
     */
    protected void getTopicCollectList(Integer startIndex, Integer number,
                                       HttpResponseListener listener) {
        if (startIndex < 0 || number < 0 || listener == null) {
            Log.e(TAG,
                    "getTopicCollectList parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getTopicCollectList --> startIndex = " + startIndex
                + " , number = " + number);
        JNIInterface.getInstance().RequestTopicCollectList(App.handleId, appId,
                startIndex, number, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 删除本地专题收藏
     *
     * @param delAll   是否删除所有的记录，true是删除所有.
     * @param vid      节目集id
     * @param listener
     */
    protected void deleteTopicCollect(Boolean delAll, Long vid,
                                      HttpResponseListener listener) {
        if (listener == null) {
            Log.e(TAG,
                    "deleteTopicCollect parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "deleteTopicCollect --> delAll = " + delAll + " , vid = "
                + vid);
        JNIInterface.getInstance().DeleteTopicCollect(App.handleId, appId,
                delAll ? 1 : 0, vid, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);

    }

    /**
     * 添加专题收藏到本地
     *
     * @param json
     * @param listener
     */
    protected void addTopicCollect(String json, HttpResponseListener listener) {
        if (listener == null || json == null) {
            Log.e(TAG, "addTopicCollect parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "addTopicCollect --> json = " + json);
        JNIInterface.getInstance()
                .AddTopicCollect(App.handleId, appId, json,
                        json.getBytes().length, CALLBACKMETHODNAME,
                        CALLBACKMETHODLENTH);

    }

    /**
     * 判断本地人物是否关注
     */
    protected void getRequestUserPpersonFollowGet(String id, HttpResponseListener listener) {
        if (id == null || listener == null) {
            Log.e(TAG,
                    "getRequestUserPpersonFollowGet parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getRequestUserPpersonFollowGet --> id = " + id);
        JNIInterface.getInstance().RequestUserPpersonFollowGet(App.handleId, appId,
                id, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }


    /**
     * 获取本地人物关注
     *
     * @param startIndex
     * @param number
     */
    protected void getPersonFollowList(Integer startIndex, Integer number,
                                       HttpResponseListener listener) {
        if (startIndex < 0 || number < 0 || listener == null) {
            Log.e(TAG,
                    "getPersonFollowList parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getPersonFollowList --> startIndex = " + startIndex
                + " , number = " + number);
        JNIInterface.getInstance().RequestPersonFollowList(App.handleId, appId,
                startIndex, number, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 删除本地人物关注
     *
     * @param delAll   是否删除所有的记录，true是删除所有.
     * @param vid      节目集id
     * @param listener
     */
    protected void deletePersonFollow(Boolean delAll, String vid,
                                      HttpResponseListener listener) {
        if (listener == null) {
            Log.e(TAG,
                    "deletePersonFollow parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "deletePersonFollow --> delAll = " + delAll + " , vid = "
                + vid);
        JNIInterface.getInstance().DeletePersonFollow(App.handleId, appId,
                delAll ? 1 : 0, vid, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);

    }

    /**
     * 添加人物关注到本地
     *
     * @param json
     * @param listener
     */
    protected void addPersonFollow(String json, HttpResponseListener listener) {
        if (listener == null || json == null) {
            Log.e(TAG, "addPersonFollow parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "addPersonFollow --> json = " + json);
        JNIInterface.getInstance()
                .AddPersonFollow(App.handleId, appId, json,
                        json.getBytes().length, CALLBACKMETHODNAME,
                        CALLBACKMETHODLENTH);

    }

    /**
     * 判断本地某个标签是否订阅
     */
    protected void getRequestUserTagSubscribeGet(String id, HttpResponseListener listener) {
        if (id == null || listener == null) {
            Log.e(TAG,
                    "getRequestUserTagSubscribeGet parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getRequestUserTagSubscribeGet --> id = " + id);
        JNIInterface.getInstance().RequestUserTagSubscribeGet(App.handleId, appId,
                id, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }


    /**
     * 获取本地标签订阅
     *
     * @param startIndex
     * @param number
     */
    protected void getTagSubscribeList(Integer startIndex, Integer number,
                                       HttpResponseListener listener) {
        if (startIndex < 0 || number < 0 || listener == null) {
            Log.e(TAG,
                    "getTagSubscribeList parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getTagSubscribeList --> startIndex = " + startIndex
                + " , number = " + number);
        JNIInterface.getInstance().RequestTagSubscribeList(App.handleId, appId,
                startIndex, number, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 删除本地标签订阅
     *
     * @param delAll   是否删除所有的记录，true是删除所有.
     * @param vid      节目集id
     * @param listener
     */
    protected void deleteTagSubscribe(Boolean delAll, String vid,
                                      HttpResponseListener listener) {
        if (listener == null) {
            Log.e(TAG,
                    "deleteTagSubscribe parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "deleteTagSubscribe --> delAll = " + delAll + " , vid = "
                + vid);
        JNIInterface.getInstance().DeleteTagSubscribe(App.handleId, appId,
                delAll ? 1 : 0, vid, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);

    }

    /**
     * 添加标签订阅到本地
     *
     * @param json
     * @param listener
     */
    protected void addTagSubscribe(String json, HttpResponseListener listener) {
        if (listener == null || json == null) {
            Log.e(TAG, "addTagSubscribe parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "addTagSubscribe --> json = " + json);
        JNIInterface.getInstance()
                .AddTagSubscribe(App.handleId, appId, json,
                        json.getBytes().length, CALLBACKMETHODNAME,
                        CALLBACKMETHODLENTH);

    }

    /**
     * 获取代金券
     */
    protected void getVouchersList(String url, Integer pageNum, Integer pageSize,
                                   HttpResponseListener listener) {
        if (pageNum < 0 || pageSize < 0 || listener == null) {
            Log.e(TAG,
                    "getVouchersList parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getVouchersList --> pageNum = " + pageNum
                + " , pageSize = " + pageSize);
        JNIInterface.getInstance().userVouchersList(App.handleId, appId, url, url
                .getBytes().length, pageNum, pageSize, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取交易记录 //flag 0:全部订单， 1：单片订单 2：产品包订单
     */
    protected void getOrderList(String url, Integer pageNum, Integer pageSize, Integer flag,
                                HttpResponseListener listener) {
        if (pageNum < 0 || pageSize < 0 || listener == null) {
            Log.e(TAG, "getOrderList parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getOrderList --> pageNum = " + pageNum + " , pageSize = " + pageSize);
        JNIInterface.getInstance().userOrderList(App.handleId, appId, flag, url, url
                .getBytes().length, pageNum, pageSize, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取产品包订单
     */
    protected void getPaymentList(String url, Integer pageNum, Integer pageSize,
                                  HttpResponseListener listener) {
        if (pageNum < 0 || pageSize < 0 || listener == null) {
            Log.e(TAG, "getPaymentList parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getPaymentList --> pageNum = " + pageNum + " , pageSize = " + pageSize);
        JNIInterface.getInstance().userPaymentList(App.handleId, appId, url, url
                .getBytes().length, pageNum, pageSize, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取支付网页
     */
    protected void getUserProductWeb(String url, String json, HttpResponseListener listener) {
        if (url == null || json == null || listener == null) {
            Log.e(TAG, "getUserProductWeb parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        JNIInterface.getInstance().userProductWeb(App.handleId, appId, url, url
                        .getBytes().length, json, json.getBytes().length, CALLBACKMETHODNAME,
                CALLBACKMETHODLENTH);
    }

    /**
     * 获取用户订单状态
     */
    protected void getUserOrderStatus(String url, String json, HttpResponseListener listener) {
        if (url == null || json == null || listener == null) {
            Log.e(TAG, "getUserOrderStatus parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        JNIInterface.getInstance().userOrderStatus(App.handleId, appId, url, url
                        .getBytes().length, json, json.getBytes().length, CALLBACKMETHODNAME,
                CALLBACKMETHODLENTH);
    }


    /**
     * 获取排行总榜 视频区数据
     *
     * @param url
     */
    protected void getChartsContent(String url, String epgId, String columnId, Integer
            cachetime, SimpleHttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getChartsContent parameters is invalid , request failed .");
            return;
        }
        //分页加载暂时没用，先写死了
        url = String.format("%s/listContent?epgId=%s&columnId=%s&pageSize=0&pageNum=1", url,
                epgId, columnId);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取专题汇总数据
     *
     * @param url
     */
    protected void getTopicContent(String url, String epgId, String columnId, Integer
            cachetime, SimpleHttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getTopicContent parameters is invalid , request failed .");
            return;
        }
        //分页加载暂时没用，先写死了100
        url = String.format("%s/listContent?epgId=%s&columnId=%s&pageSize=0&pageNum=100", url,
                epgId, columnId);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取专题汇总 menu数据
     *
     * @param url
     */
    protected void getTopicMenu(String url, String epgId, String channelId, Integer
            cachetime, SimpleHttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getTopicMenu parameters is invalid , request failed .");
            return;
        }

        url = String.format("%s/column?epgId=%s&channelId=%s", url, epgId, channelId);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取排行总榜 menu数据
     *
     * @param url
     */
    protected void getChartsMenu(String url, String epgId, String channelId, Integer
            cachetime, SimpleHttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getDetailContent parameters is invalid , request failed .");
            return;
        }

        url = String.format("%s/column?epgId=%s&channelId=%s", url, epgId, channelId);
        listener.setRequestUrl(url);
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "v --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().RequestEpgByGetWithCache(App.handleId, appId,
                cachetime, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取服务器时间和本地时间的差值
     */
    public long getServeTime() {
        return JNIInterface.getInstance().getServeTime(App.handleId);
    }

    /**
     * 添加试看的时间
     *
     * @param listener
     */
    protected void getComcaLivePreviewTimeAdd(String json, HttpResponseListener listener) {
        if (listener == null || json == null) {
            Log.e(TAG, "getComcaLivePreviewTimeAdd parameters is invalid , request failed .");
            return;
        }

        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getComcaLivePreviewTimeAdd --> json = " + json + " , appId = " + appId);
        JNIInterface.getInstance().comcaLivePreviewTimeAdd(App.handleId, appId, json,
                json.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取试看的时间
     *
     * @param listener
     */
    protected void getComcaLivePreviewTimeGet(Long lid, Long sid, HttpResponseListener listener) {
        if (listener == null) {
            Log.e(TAG, "getComcaLivePreviewTimeGet parameters is invalid , request failed .");
            return;
        }

        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getComcaLivePreviewTimeGet --> json = " + sid + " , appId = " + appId);
        JNIInterface.getInstance().comcaLivePreviewTimeGet(App.handleId, appId, lid,
                sid, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取登录地址
     */
    protected void getLoginUrl(String url, HttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "getLoginUrl parameters is invalid , request failed .");
            return;
        }

        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getLoginUrl --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().getLoginUrl(App.handleId, appId, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 根据固定的url删除缓存，在解析缓存数据失败时调用
     *
     * @param url
     * @param listener
     */
    protected void delCacheByUrl(String url, HttpResponseListener listener) {
        if (listener == null || url == null) {
            Log.e(TAG, "delCacheByUrl parameters is invalid , request failed .");
            return;
        }

        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "delCacheByUrl --> url = " + url + " , appId = " + appId);
        JNIInterface.getInstance().delCacheByUrl(App.handleId, appId, url,
                url.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取跑马灯消息
     *
     * @param listener
     */
    protected void getMarqueeNotice(HttpResponseListener listener) {
        if (listener == null) {
            Lg.e(TAG,
                    "getMarqueeNotice parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Lg.d(TAG, "getMarqueeNotice -->  appId = " + appId);
        JNIInterface.getInstance().RequestMarqueeNotice(App.handleId, appId,
                CALLBACKMETHODNAME,
                CALLBACKMETHODLENTH);

    }


    /**
     * 获取本地数据
     *
     * @param startIndex
     * @param number
     */
    protected void getLocalDataList(String dbName, String idName, Integer startIndex, Integer
            number, HttpResponseListener listener) {
        if (startIndex < 0 || number < 0 || listener == null) {
            Log.e(TAG, "getLocalDataList " + dbName + " parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getLocalDataList " + dbName + " --> startIndex = " + startIndex
                + " , number = " + number);
        JNIInterface.getInstance().RequestLocalDataList(App.handleId, appId, dbName,
                idName, startIndex, number, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 获取本地数据
     *
     */
    protected void getLocalDataById(String dbName, String idName, String id, HttpResponseListener listener) {
        if (listener == null) {
            Log.e(TAG, "getLocalDataList " + dbName + " parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getLocalDataList " + dbName);
        JNIInterface.getInstance().RequestLocalDataById(App.handleId, appId, dbName,
                idName, id, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 删除本地数据
     *
     * @param delAll   是否删除所有的记录，true是删除所有.
     * @param vid      节目集id
     * @param listener
     */
    protected void deleteLocalData(String dbName, String idName, Boolean delAll, String vid,
                                   HttpResponseListener listener) {
        if (listener == null) {
            Log.e(TAG, "deleteLocalData " + dbName + "parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "deleteLocalData " + dbName + " --> delAll = " + delAll + " , vid = " + vid);
        JNIInterface.getInstance().DeleteLocalData(App.handleId, appId,
                delAll ? 1 : 0, dbName, idName, vid, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);

    }

    /**
     * 添加本地数据
     *
     * @param json
     * @param listener
     */
    protected void addLocalData(String dbName, String idName, String json, HttpResponseListener
            listener) {
        if (listener == null || json == null) {
            Log.e(TAG, "addLocalData " + dbName + " parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "addLocalData " + dbName + "--> json = " + json);
        JNIInterface.getInstance().AddLocalData(App.handleId, appId, dbName, idName,
                json, json.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 判断本地数据是否存在
     */
    protected void RequestLocalDataById(String dbName, String idName, String id,
                                        HttpResponseListener listener) {
        if (id == null || listener == null) {
            Log.e(TAG, "getRequestVideoRecordGet " + dbName + "parameters is invalid , request " +
                    "failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "getRequestVideoRecordGet " + dbName + "--> id = " + id);
        JNIInterface.getInstance().RequestLocalDataById(App.handleId, appId, dbName,
                idName, id, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    /**
     * 微信登录
     */
    private void comcaMobileWxlogin(String url, String json, HttpResponseListener
            listener){
        if (listener == null || json == null) {
            Log.e(TAG, "comcaMobileWxlogin " + url + " parameters is invalid , request failed .");
            return;
        }
        int appId = putIntoRequestQueue(listener);
        Log.d(TAG, "comcaMobileWxlogin " + url + "--> json = " + json);
        JNIInterface.getInstance().ComcaMobileWxlogin(App.handleId, appId, url, url.length(), json,
                json.getBytes().length, CALLBACKMETHODNAME, CALLBACKMETHODLENTH);
    }

    protected int JNIMessageCallback(int result, int appId, String response) {
        Log.d(TAG, "JNIMessageCallback  arrived , msg result ====> "
                + " msgType = " + result + " , response = "
                + (response == null ? "null" : response));
        switch (result) {
            case ResponseCode.REGISTER_RESULT_MSG_TYPE: // 登录回调
//                if (response == null || App.isLogin)
//                    break;
//                UserBean user = JSON.parseObject(response, UserBean.class);
//                if (user != null) {
//                    App.saveUserInfo(user);
//
//                    EventBus.getDefault().post(new UserStateEvent(true));
//                    final Activity activity = App.getAppManager().currentActivity();
//                    if (activity != null) {
////                        UmengHelper.onLoginSuccess(activity);
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
////                                ToastUtils.show_common_style(activity, "您已成功登录CIBN高清影视");
//                                Lg.d("您已成功登录CIBN高清影视");
//                            }
//                        });
//                    }
//                }
//                EventBus.getDefault().post(new RegisterResultEvent(response));
                Log.d(TAG, "-------register result callback successfully------- .");
                break;
            case ResponseCode.HOME_REFRESH_MSG_TYPE: // 首页刷新
                // EventBus.getDefault().post(new RefreshHomeEvent(true, response));
                Log.d(TAG,
                        "-------homeRefresh result callback successfully------- .");
                break;
            case ResponseCode.USER_MESSAGE_MSG_TYPE: // 我的消息

                break;
            case ResponseCode.PAY_RESULT_MSG_TYPE: // 支付成功消息
                EventBus.getDefault().post(new PayResultEventBean(response));
                Log.d(TAG, "-------pay succuss callback successfully------- .");
                break;
            case ResponseCode.SERVER_NOTICE_MSG_TYPE: //服务器通知消息
                // EventBus.getDefault().post(new UserMessageArrivedEvent(true));
                Log.d(TAG, "-------new user message callback successfully------- .");
                handleServerNoticeMsg(response);
                break;

            case ResponseCode.APP_DISABLE_MSG_TYPE:
                EventBus.getDefault().post(new DisableAppEventBean(response));
                break;

            default:
                break;
        }
        return 0;
    }

    private void handleServerNoticeMsg(String response) {
        try {
            UserMsg userMsg = JSON.parseObject(response, UserMsg.class);
            switch (userMsg.getShowtype()) {
                case UserMsg.marquee_showType:  //如果是跑马灯消息类型
                    EventBus.getDefault().post(new MarqueeMsgEvent(true, userMsg.getContent()));
                    break;
                default:
                    Log.e(TAG, "invalid userMsg showtype " + userMsg.getShowtype());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}