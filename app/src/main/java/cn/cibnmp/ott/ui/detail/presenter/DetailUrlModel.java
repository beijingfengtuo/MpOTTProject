package cn.cibnmp.ott.ui.detail.presenter;

/**
 * Created by axl on 2018/3/12.
 */

public class DetailUrlModel {
//    private long vid;
//    private long sid;
//    private String url;
//    public int pay_status = 0;//付费状态  // 0免费不付费，1付费，2付费鉴权通过
//    private String epgIdParam;
//
//    public void reuquestPlayerDataSource() {
//
//        if (sid <= 0l) {
//            //做容错处理
//            sid = 1;
//        }
//
//
//        HttpRequest.getInstance().excute("getVideoUrl", new Object[]{App.epgUrl, epgIdParam, vid + "", sid + "" + pingUrl(),
//                CacheConfig.cache_an_hour, new SimpleHttpResponseListener<VideoUrlResultBean>() {
//            @Override
//            public void onSuccess(VideoUrlResultBean response) {
//                handleRequestUrlSuccess(response);
//            }
//
//            @Override
//            public void onError(String error) {
//
//            }
//        }});
//
//    }
//
//
//    //拼接播放地址
//    public String pingUrl() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("&projectId=");
//        sb.append(String.valueOf(App.projId));
//        sb.append("&appId=");
//        sb.append(String.valueOf(App.appId));
//        sb.append("&channelId=");
//        sb.append(String.valueOf(App.channelId));
//        sb.append("&cibnUserId=");
//        sb.append(String.valueOf(App.userId));
//        sb.append("&termId=");
//        sb.append(App.publicTID);
//        sb.append("&sessionId=");
//        sb.append(App.sessionId);
//
//        return sb.toString();
//    }
//
//    public void handleRequestUrlSuccess(VideoUrlResultBean bean) {
//
//        Log.i(TAG, "urlSuccesss");
//
//        videoUrlResultBean = bean;
//        if (bean != null) {
//            playerFragment.setPlayData("", (int) mysid, videoUrlInfoBean, resultBean.getData());
//        }
//
//
//        if (videoUrlResultBean == null || !videoUrlResultBean.getCode().equalsIgnoreCase("1000") || videoUrlResultBean.getData() == null
//                || videoUrlResultBean.getData().getMedia() == null || videoUrlResultBean.getData().getMedia().size() <= 0) {
//            //开启错误播放事件
//            //
//            playerFragment.setPlayData("", (int) mysid, videoUrlInfoBean, resultBean.getData());
//            playerFragment.setErrorLog("001", "获取播放地址失败");
//            return;
//
//        }
//        videoUrlInfoBean = videoUrlResultBean.getData();
//        //延时200毫秒处理
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (videoUrlInfoBean.getMedia() != null && videoUrlInfoBean.getMedia().size() > 0) {
//
//                    if (pay_status == 1) {
//                        if (!videoUrlInfoBean.getAuthCode().equals("3")) {
//
//                            if (videoUrlInfoBean.getPriceType() == 2) {
//                                // 付费片子 但鉴权没通过  第二种状态
//                                playerFragment.initFreeTimeLay(true, videoUrlInfoBean.getStartTime(), videoUrlInfoBean.getEndTime());
//                                record_current_pos = 0;
////                                    video_C = 1;
//                                //   video_C = SharePrefUtils.getInt(Constant.videoSwich, 0);
//                            } else {
//                                //// 付费片子 但鉴权通过  第三种状态
//                                playerFragment.initFreeTimeLay(false, 0, 0);
//                            }
//                        } else {
//                            // 免费片子 不用鉴权 不用付费  第一种状态
//                            playerFragment.initFreeTimeLay(false, 0, 0);
//                        }
//
//                    }
//
//                    url = videoUrlInfoBean.getMedia().get(0).getUrl();
//                    setVideoMsg(url, record_current_pos);
//
//                }
//
//            }
//        }, 200);
//
//    }
}
