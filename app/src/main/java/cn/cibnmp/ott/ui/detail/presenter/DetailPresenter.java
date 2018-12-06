package cn.cibnmp.ott.ui.detail.presenter;

/**
 * Created by axl on 2018/3/12.
 */

public class DetailPresenter {
//    private long vid;
//    private long sid;
//    private String epgIdParam;
//    private static final  String TAG = "DetailPresenter";
//
//
//    private Handler mHandler = new Handler() {
//        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//        @Override
//        public void handleMessage(Message msg) {
//            Lg.i(TAG, msg.what + "..... msg what");
//            switch (msg.what) {
//                case 3:
//                    //  playerFragment.setChangeDataSource("http://hls01.ott.disp.cibntv.net/2017/04/20/efc6aa2da685472a81cd463642e3edbc/1c3c1bf9bf78d414760461de428e4f9b.m3u8?k=99821da1a07dc8237567ae205894c78b&channel=cibn&t=1516587934&ttl=86400",0);
//                    // playerFragment.setPlayData("http://hls01.ott.disp.cibntv.net/2017/04/12/5075efd5840446948574163cd7bde8ba/af96c313f019878301d0bb0aa5216beb.m3u8?k=db978cc634bac33a59b33409d1088f74&channel=cibn&t=1516708623&ttl=86400", 0, new VideoUrlInfoBean());
//                    break;
//
//                case 2006:
//                    //错误的状态 请求
//                    ToastUtils.show(DetailActivity.this, "详情数据错误");
//                    break;
//                case 2001:
//                    //
//                    setDetailData();
//                    break;
//                case 2003:
//                    updateUIFormAuther();
//                    break;
//
//                case 120:
//                    //收藏成功
//                    //   EventBus.getDefault().post("addVideoCollect");
//                    scrollFrag.updateCollect("addVideoCollect");
//                    collect = 1;
//                    break;
//                case 121:
//                    //收藏失败
//                    scrollFrag.updateCollect("addVideoCollectError");
//                    collect = 0;
//                    break;
//                case 122:
//                    //取消收藏成功
//                    //    EventBus.getDefault().post("deleteLocalCollect");
//                    scrollFrag.updateCollect("deleteLocalCollect");
//                    collect = 0;
//                    break;
//                case 123:
//                    //取消收藏失败
//                    //  EventBus.getDefault().post("deleteLocalCollectError");
//                    scrollFrag.updateCollect("deleteLocalCollectError");
//                    collect = 1;
//                    break;
//                case 1003:
//                    reuquestPlayerDataSource();
//                    break;
//                case 56666:
//
//                    long progress = 0;
//                    if (msg.obj != null) {
//                        progress = (long) msg.obj;
//                    }
//                    Lg.i(TAG, "AXLprogress" + progress);
//                    playerFragment.setPlaySource(url, progress);
//                    break;
//                case 45678:
//                    //    reuquestPlayerDataSource();
//                    break;
//                case 567:
//                    showDiaLog();
//                    break;
//                case 140:
//                    initMaxFragment();
//                    break;
//
//            }
//            super.handleMessage(msg);
//
//        }
//    };
//
//
//
//    public long getVid() {
//        return vid;
//    }
//
//    public void setVid(long vid) {
//        this.vid = vid;
//    }
//
//    public long getSid() {
//        return sid;
//    }
//
//    public void setSid(long sid) {
//        this.sid = sid;
//    }
//
//
//
//
//    public void requestDetailContent() {
//        // epgIdParam = "1031";
//        //   vid = 194580L;
//        HttpRequest.getInstance().excute("getDetailContent", new Object[]{App.epgUrl,
//                epgIdParam, vid + "", 10 * 60, new SimpleHttpResponseListener<DetailInfoResultBean>() {
//
//            @Override
//            public void onSuccess(DetailInfoResultBean response) {
//                handleRequestContentSuccess(response);
//                mHandler.sendEmptyMessageDelayed(1003, 200);
//            }
//
//            @Override
//            public void onError(String error) {
//                mHandler.sendEmptyMessage(2006);
//            }
//        }});
//
//    }
//
//
//    protected DetailInfoResultBean resultBean;
//    protected DetailBean detailBean;
//
//    protected void handleRequestContentSuccess(DetailInfoResultBean response) {
//
//        try {
//            Log.d(TAG, "getDetailNavContent : result--> " + response.toString());
//            resultBean = response;
//            //数据判断
//            if (resultBean == null || !resultBean.getCode().equalsIgnoreCase("1000") || resultBean.getData() == null) {
//                Log.e(TAG, "getDetailNavContent response parse to entity failed , data is invalid !!!");
//                mHandler.sendEmptyMessage(2006);
//                return;
//            } else if (resultBean.getCode().equalsIgnoreCase("1001")) {
//                Log.e(TAG, "getDetailNavContent result's data is 1001 !!!");
//                mHandler.sendEmptyMessage(20064);
//                return;
//            } else {
//
//                videoType = resultBean.getData().getVideoType();
//
//                if (resultBean.getData().getChild() != null && resultBean.getData().getChild().size() > 0) {
//                    if (!need_sid) {
//                        mysid = resultBean.getData().getChild().get(0).getSid();
//                    }
//                } else {
//                    Lg.i(TAG, " playerFragment.setPlaySource:null");
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
////                            if (playerPage == null) {
////                                initPlayer(video_C);
////                            }
//                            url = "";
//                            playerFragment.setPlaySource("", 0l);
//                        }
//                    }, 0);
//                }
//
//                mergeData(resultBean.getData());
//
//                //更新数据
//                mHandler.sendEmptyMessageDelayed(2001, 200);
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void reuquestPlayerDataSource() {
//        if (vid == 0l) {
//            url = "";
//            return;
//        }
//        if (mysid <= 0l) {
//            //做容错处理
//            mysid = 1;
//        }
//
//
//        HttpRequest.getInstance().excute("getVideoUrl", new Object[]{App.epgUrl, epgIdParam, vid + "", mysid + "" + pingUrl(),
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
//        return sb.toString();
//    }
//
//
//    public void getDetailProductAuth() {
//        final DetailAutherBean detailAutherBean = new DetailAutherBean();
//
//
//        detailAutherBean.setSeriesId(vid);
//
//        detailAutherBean.setSeriesType(Integer.valueOf(seriesTypeVideo));
//
//
//        HttpRequest.getInstance().excute("getVideoinfProductAuthen", new Object[]{App.bmsurl, JSON.toJSONString(detailAutherBean), new HttpResponseListener() {
//            @Override
//            public void onSuccess(String response) {
//                if (!TextUtils.isEmpty(response)) {
//                    try {
//                        DetailAutherResultBean detailAutherResultBean = JSON.parseObject(response, DetailAutherResultBean.class);
//                        if (detailAutherResultBean != null && detailAutherResultBean.getResultCode().equals("2")) {
//                            pay_status = 2;
//                        }
//                        mHandler.sendEmptyMessage(2003);
//                    } catch (Exception e) {
//                        mHandler.sendEmptyMessage(2003);
//                    }
//
//
//                }
//                //     mHandler.sendEmptyMessage(2003);
//            }
//
//            @Override
//            public void onError(String error) {
//                mHandler.sendEmptyMessage(2003);
//            }
//        }});

    }
