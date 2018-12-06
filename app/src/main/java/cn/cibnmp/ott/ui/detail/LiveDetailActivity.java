package cn.cibnmp.ott.ui.detail;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.adapter.HomeOneViewType;
import cn.cibnmp.ott.bean.LayoutItem;
import cn.cibnmp.ott.bean.NavigationBlock;
import cn.cibnmp.ott.bean.NavigationInfoBlockBean;
import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.bean.ReserveBeanEvent;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.jni.JNIRequest;
import cn.cibnmp.ott.jni.SimpleHttpResponseListener;
import cn.cibnmp.ott.ui.base.Action;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.ui.detail.bean.AddDetailReserveBeanEvent;
import cn.cibnmp.ott.ui.detail.bean.DetailAutherBean;
import cn.cibnmp.ott.ui.detail.bean.DetailAutherResultBean;
import cn.cibnmp.ott.ui.detail.bean.DetailBean;
import cn.cibnmp.ott.ui.detail.bean.DetailChildBean;
import cn.cibnmp.ott.ui.detail.bean.DetailChildFatherBean;
import cn.cibnmp.ott.ui.detail.bean.DetailInfoBean;
import cn.cibnmp.ott.ui.detail.bean.DetailInfoResultBean;
import cn.cibnmp.ott.ui.detail.bean.LiveUrlBean;
import cn.cibnmp.ott.ui.detail.bean.LiveUrlFatherBean;
import cn.cibnmp.ott.ui.detail.bean.ReservationBean;
import cn.cibnmp.ott.ui.detail.bean.ReserveBean;
import cn.cibnmp.ott.ui.detail.bean.VideoUrlInfoBean;
import cn.cibnmp.ott.ui.detail.bean.VideoUrlResultBean;
import cn.cibnmp.ott.ui.detail.content.DbQueryLiveidListener;
import cn.cibnmp.ott.ui.detail.content.UserReserveHelper;
import cn.cibnmp.ott.ui.detail.player.widgets.LivePlayerFrag;
import cn.cibnmp.ott.ui.detail.presenter.LiveManager;
import cn.cibnmp.ott.ui.detail.view.DetailScrollFrag;
import cn.cibnmp.ott.ui.detail.view.DetailShowEpisodeMaxFragment;
import cn.cibnmp.ott.ui.detail.view.DetailTvEpisodeMaxFragment;
import cn.cibnmp.ott.ui.search.ToastUtils;
import cn.cibnmp.ott.ui.share.ShareDialog;
import cn.cibnmp.ott.utils.DisplayUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.TimeUtils;
import cn.cibnmp.ott.utils.Util;
import de.greenrobot.event.EventBus;

/**
 * Created by axl on 2018/2/4.
 */

public class LiveDetailActivity extends BaseActivity {

    public String epgIdParam;


    private Bitmap thumb = null; //分享缩略图
    public String contentIdParam;

    private LivePlayerFrag playerFrag;

    public String videoType;

    public boolean hasNavBlock;

    private long mysid;

    public String url;

    public boolean need_sid;

    public boolean isLive;//是否是直播


    public final String seriesTypeLive = "2";//直播type

    public int pay_status = 0;//付费状态  // 0免费不付费，1付费，2付费鉴权通过 3通过
    protected int pay_bt = 0;//0不买，1买


    public int collect = 0;//是否收藏

    private static final String TAG = "DetailPrestener";

    public VideoUrlResultBean videoUrlResultBean;
    public VideoUrlInfoBean videoUrlInfoBean;


    public long times;

    private ImageView ivProess;
    private RotateAnimation animation;
    // 0电影 1电视剧 2综艺 4直播
    public int detailType = 4;
    private int childType = 0; // 通用标识 4直播
    private String action = "open_movie_detail_page";


    //每次预约倒计时的总时间
    private long time = 0l;
    //60秒
    private long timemiao = 60;
    //60分
    private long timefen = 60;
    //24小时
    private long timeshi = 24;
    //每次刷新的天数
    private long day = 0;
    //每次刷新的小时
    private long shi = 0;
    //每次刷新的分钟
    private long fen = 0;
    //结束时间
    private long toTime = 0l;
    //开始的时间
    private long goTime = 0l;
    //是否可以直播的开关，0为未开始，1为正在直播，2为已结束，3为回看， 5为数据异常
 public int isPlay = -1;
    //是否预约，1为已预约，2为没有预约
    private int isYuYue = 0;
    //开始直播倒计时
    private final int startTime = 5566;
    private final int UPDATE_LIVE = 5577;
    private final int GET_LIVE_URL = 5588;
    private final int GET_TIME_ERRER = 5590;
    private final int GET_TIME_YES = 5591;
    private final int URL_NULL = 5544;

    private String videoUrl = "";

    private long Lid;
    private long liveSid = -1;


    /**
     * 选集规制 首先遍历有没有正在直播的 没有 就在遍历看有没有
     */


    private LinearLayout uLayout, bLayout, mLayout, vLayout;


    //转播页面的直播状态
    private String playStatus;


    //直播相关
    private DbQueryLiveidListener dbQueryListener;

    private LiveUrlBean liveUrlBean;
    private DetailChildBean liveStatusBean;
    private DetailChildBean thisChildBean;
    private HashMap<Integer, Integer> statusList = new HashMap<>();
    //所有详情顶部文字信息简介
    private DetailScrollFrag scrollFrag;

    //tv展开fragment
    private DetailTvEpisodeMaxFragment tvMaxFragment;
    private DetailShowEpisodeMaxFragment showMaxFragment;


    private int liveNo = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        iniParams(null);
        mHandler.sendEmptyMessageDelayed(255, 200);
//        Lg.i("hhh",""+getIntent().getBundleExtra(Constant.contentIdKey));
        ;
    }

    private void initView() {

        if (uLayout == null) {
            uLayout = (LinearLayout) findViewById(R.id.detail_player);
            bLayout = (LinearLayout) findViewById(R.id.detail_scrollcontent);
            mLayout = (LinearLayout) findViewById(R.id.detail_maxcontent);
            vLayout = (LinearLayout) findViewById(R.id.detail_progresscontent);
            // ImageView的动画
            ivProess = (ImageView) findViewById(R.id.ivProgressIcon);
            animation = (RotateAnimation) AnimationUtils.loadAnimation(this, R.anim.rotating);
            LinearInterpolator lir = new LinearInterpolator();
            animation.setInterpolator(lir);
        }

        // 详情页头布局
        scrollFrag = DetailScrollFrag.newInstance(4, Lid);

        playerFrag = LivePlayerFrag.newInstance(4, Lid);
        playerFrag.setSid(mysid);


        ivProess.startAnimation(animation);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.detail_scrollcontent, scrollFrag);
        transaction.replace(R.id.detail_player, playerFrag);
        if (childType == 1) {// 电视剧选集
            tvMaxFragment = new DetailTvEpisodeMaxFragment();
            transaction.replace(R.id.detail_maxcontent, tvMaxFragment);
        } else if (childType == 2) {// 综艺选集
            showMaxFragment = new DetailShowEpisodeMaxFragment();
            transaction.replace(R.id.detail_maxcontent, showMaxFragment);
        }
        transaction.commit();


    }

    public void initPlayer() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.commit();
    }

    IntentFilter filter;

    private void initFiter() {
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            contentLayoutMax(true);
        }
        registerReceiver( timer_netReceiver,filter);
    }

    public void contentLayoutMax(boolean b) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) uLayout
                .getLayoutParams();
        if (b) {
            params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            //   getWindow().addFlags(WindowManager.LayoutParams.NOT);
            getWindow().clearFlags(WindowManager.LayoutParams.TITLE_CHANGED);
           playerFrag.changeScreenOrientation(1);
        } else {
            params.height = DisplayUtils.getValue(400);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            playerFrag.changeScreenOrientation(0);
        }
        uLayout.setLayoutParams(params);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            contentLayoutMax(false);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            contentLayoutMax(true);
        }
        if (scrollFrag != null) {
            scrollFrag.diamissDialog();
        }
    }



    private void setAction() {
        action = "open_live_detail_page";
    }

    private void requestLiveContent() {
        Lg.i(TAG, "requestLiveContent");

        HttpRequest.getInstance().excute("getLiveDetailContent", new Object[]{
                App.epgUrl, epgIdParam, Lid + "", 10, new SimpleHttpResponseListener<DetailInfoResultBean>() {

            @Override
            public void onSuccess(DetailInfoResultBean response) {
                handleRequestContentSuccess(response);

            }

            @Override
            public void onError(String error) {
                mHandler.sendEmptyMessage(2006);
            }
        }
        });

    }


    protected DetailInfoResultBean resultBean;
    protected DetailBean detailBean;

    protected void handleRequestContentSuccess(DetailInfoResultBean response) {

        try {
            Log.d(TAG, "getDetailNavContent : result--> " + response.toString());
            resultBean = response;
            //数据判断
            if (resultBean == null || !resultBean.getCode().equalsIgnoreCase("1000") || resultBean.getData() == null) {
                Log.e(TAG, "getDetailNavContent response parse to entity failed , data is invalid !!!");
                mHandler.sendEmptyMessage(2006);
                return;
            } else if (resultBean.getCode().equalsIgnoreCase("1001")) {
                Log.e(TAG, "getDetailNavContent result's data is 1001 !!!");
                mHandler.sendEmptyMessage(20064);
                return;
            } else {

                videoType = resultBean.getData().getVideoType();

                if (resultBean.getData().getChild() != null && resultBean.getData().getChild().size() > 0) {
                    if (!need_sid) {
                        mysid = resultBean.getData().getChild().get(0).getSid();
                    }
                } else {
                    Lg.i(TAG, " playerFragment.setPlaySource:null");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            url = "";
                            playerFrag.setPlaySource("", 0l);
                        }
                    }, 0);
                }
                detailBean = resultBean.getData().getEpglive();
                mergeData(resultBean.getData());

                //更新数据
                mHandler.sendEmptyMessageDelayed(2001, 200);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取直播结束状态
    private final int remove_zb = 5599;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case startTime:
                    setStartTime();
                    break;
                case UPDATE_LIVE:
//                    Toast.makeText(LiveDetailActivity.this, "刷新直播", Toast.LENGTH_SHORT).show();
                    updateTimeUI();
                    break;
                case GET_LIVE_URL:
                    long obg = (long) msg.obj;
                    if (liveUrlBean != null && liveUrlBean.getMedia() != null && liveUrlBean.getMedia().size() > 0) {
//
                        if (liveSid == obg) {
                            String fid = liveUrlBean.getMedia().get(0).getFid();
                            playerFrag.setFid(fid);
                            int tag = 0;
                            String myUrl = "";
                            if (fid != null && !fid.equals("")) {
                                myUrl = App.cdnUrl + "/view/" + fid;
                            } else {
                                tag = 2;
                                myUrl = liveUrlBean.getMedia().get(0).getUrl();
                            }
                            myUrl = liveUrlBean.getMedia().get(0).getUrl();

                            if (liveUrlBean.getLiveStatus() == 3) {//回看=点播
                                //   playerPage.setVideoDisc(Lid, liveSid, detailBean.getLiveName(), 0, "", childType, "", myUrl, "", tag);
                                playerFrag.setPlayData(myUrl, (int) liveSid, videoUrlInfoBean, resultBean.getData());
                            } else {//直播
                                //  playerPage.setVideoDisc(Lid, liveSid, detailBean.getLiveName(), 2, "", childType, "", myUrl, "", tag);
                                playerFrag.setPlayData(myUrl, (int) liveSid, videoUrlInfoBean, resultBean.getData());
                            }
                            playerFrag.setPlaySource(myUrl, 0);
                            playerFrag.gongRanTitle();

                            url = myUrl;
                        }
                    } else {
                        Toast.makeText(LiveDetailActivity.this, "播放地址获取失败", Toast.LENGTH_SHORT).show();
                        if (playerFrag != null) {
                            playerFrag.pause();
                        }
                        playerFrag.setPlayData("", (int) liveSid, videoUrlInfoBean, resultBean.getData());
                        playerFrag.gongRanTitle();

                        url = "";
                    }
                    break;
                case remove_zb:
                    requestLiveStatus(thisChildBean);
                    //  isRemove = true;
                    break;
                case GET_TIME_ERRER:
                    Lg.i(TAG, "(long) msg.obj-----------22>>" + resultBean.getData().getEpglive().getLimitTime());
                    if (pay_status == 1) {
                        playerFrag.initFreeTimeLay(true, 0);
                    } else {
                        //     playerFrag.initFreeTimeLay(false, resultBean.getData().getEpglive().getEndDate());
                    }
                    break;
                case GET_TIME_YES:
                    long progress = ((long) msg.obj);
                    Lg.i(TAG, "(long) msg.obj-----------11>>" + progress);
                    if (pay_status == 1) {
                        playerFrag.initFreeTimeLay(true, progress);
                    } else {
                        playerFrag.initFreeTimeLay(false, 0);
                    }
                    break;
                case URL_NULL:
                    Toast.makeText(LiveDetailActivity.this, "播放地址获取失败", Toast.LENGTH_SHORT).show();
                    if (playerFrag != null) {
                        playerFrag.pause();
                    }
                    long sidTo = ((long) msg.obj);
                    liveSid = sidTo;
                    Lid = resultBean.getData().getLiveId();
                    // playerPage.setVideoDisc(resultBean.getData().getLiveId(), sidTo, detailBean.getLiveName(), 2, "", childType, "", "", "", 0);
                    //normalPlayer.setPlayDataSource("", 0l);
                    playerFrag.setErrorLog("001", "获取视频地址失败");
                    url = "";
                    break;

                case 2006:
                    ToastUtils.show(LiveDetailActivity.this, "获取详情数据失败");
                    break;
                case 2001:
                    setDetailData();
                    break;
                case 2003:
                    updateUIFormAuther();
                    break;
                case 255:


                        if (TextUtils.isEmpty(contentIdParam)) {
                            Lid = 0;
                        } else {
                            Lid = Long.parseLong(contentIdParam);
                        }

                        if (TextUtils.isEmpty(((BaseActivity) LiveDetailActivity.this).sid)) {
                            mysid = 1;
                        } else {
                            //    need_sid = true;
                            mysid = Long.parseLong(((BaseActivity) LiveDetailActivity.this).sid);
                            if (mysid > 1)
                                need_sid = true;
                        }

                        if (TextUtils.isEmpty(epgIdParam)) {
                            epgIdParam = "1031";
                        }

                        initView();
                        setAction();


                        getNetState();
                        initFiter();
                   //     initMaxFragment();
                        requestLiveContent();

                    break;
                case 567:
                    showDiaLog();
                    break;
                case 1112:
                    //onResume
                    if(playerFrag!=null) {
                         playerFrag.surface_show = true;
                        playerFrag.popLay();
                    }


                    if(scrollFrag!=null) {
                        scrollFrag.scrollToTop();
                    }
                    break;

            }
        }
    };


    public List<LayoutItem> layoutItemList = new ArrayList<>();

    public List<NavigationInfoItemBean> infoItemBeanList = new ArrayList<>();

    public List<LayoutItem> newLaytItemList = new ArrayList<>();

    public List<NavigationInfoItemBean> newInfoItemBeanList = new ArrayList<>();


    private void mergeData(DetailInfoBean data) {
        newLaytItemList.clear();
        newInfoItemBeanList.clear();
        if (data.getBlocks() == null || data.getBlocks().isEmpty()
                || data.getContent() == null || data.getContent().isEmpty()) {
//            Lg.e(TAG, "mergeData failed , navId = " + navId);
//            sendContentError("数据异常");
            return;
        }

        int blockCount;  //block的数量
        NavigationBlock block;
        NavigationInfoBlockBean infoBlockBean;
        NavigationInfoItemBean infoItemBean;

        blockCount = Math.min(data.getBlocks().size(), data.getContent().size());

        if (newLaytItemList == null || newInfoItemBeanList == null)
            return;

//        newLaytItemList.clear();
//        newInfoItemBeanList.clear();
        //四个按钮集合以及布局
        List<NavigationInfoItemBean> tagInfoItemBeanList = new ArrayList<NavigationInfoItemBean>();
        LayoutItem tagLayoutItem = new LayoutItem();
        NavigationInfoItemBean tagInfoItemBean = null;

        //三张图片类型
        List<NavigationInfoItemBean> tagInfoItemBeanList1 = new ArrayList<NavigationInfoItemBean>();
        LayoutItem tagLayoutItem1 = new LayoutItem();
        NavigationInfoItemBean tagInfoItemBean1 = null;

        JSONObject object = null;
        LayoutItem layoutItem = null;
        JSONObject jsonObject = null;
        JSONArray array = null;
        String layout = null;
        int lineSize = 0;
        NavigationInfoItemBean itemBean;

        for (int j = 0; j < blockCount; j++) {
            block = data.getBlocks().get(j);
            infoBlockBean = data.getContent().get(j);
            if (block.getLayout() != null && !TextUtils.isEmpty(block.getLayout().getLayoutJson())) {
                layout = block.getLayout().getLayoutJson();

                try {

                    //设置block的标题的布局和内容，只要当nameType都是0的时候，才显示标题内容
                    if (block.getNameType() == NavigationInfoBlockBean.SHOWNAV
                            && infoBlockBean.getNameType() == NavigationInfoBlockBean.SHOWNAV) {

                        layoutItem = new LayoutItem();
                        layoutItem.setC(60l);
                        layoutItem.setR(6l);
                        newLaytItemList.add(layoutItem);

                        itemBean = new NavigationInfoItemBean();
                        itemBean.setViewtype(HomeOneViewType.title_viewType);
                        itemBean.setName(infoBlockBean.getName());
                        newInfoItemBeanList.add(itemBean);
                    }
                    jsonObject = new JSONObject(layout);

                    array = jsonObject.getJSONArray("layout");

                    for (int i = 0; i < array.length(); i++) {
                        object = array.getJSONObject(i);
                        layoutItem = new LayoutItem();
                        if (object.has("c"))
                            layoutItem.setC(object.getDouble("c"));
                        else if (object.has("C"))
                            layoutItem.setC(object.getDouble("C"));

                        if (object.has("r"))
                            layoutItem.setR(object.getDouble("r"));
                        else if (object.has("R"))
                            layoutItem.setR(object.getDouble("R"));
                        newLaytItemList.add(layoutItem);

                        if (infoBlockBean.getIndexContents() != null && i < infoBlockBean.getIndexContents().size()) {
                            infoItemBean = infoBlockBean.getIndexContents().get(i);
                            newInfoItemBeanList.add(infoItemBean);
                        } else {
                            newInfoItemBeanList.add(new NavigationInfoItemBean(HomeOneViewType.space_viewType));
                        }
                    }
//                    if (layoutLoadListener != null) {
//                        layoutLoadListener.OnLayoutLoad(navId, newLaytItemList);
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
//                    sendContentError("数据异常");
                }

            } else if (block.getLayout() == null && block.getLayout().getLayoutJson() != null) {

            } else {

                if (infoBlockBean.getIndexContents() != null && infoBlockBean.getIndexContents().size() > 0 && (infoBlockBean.getIndexContents().get(0).getViewtype() == 10 || infoBlockBean.getIndexContents().get(0).getViewtype() == 11)) {
                    layoutItem = new LayoutItem();
                    layoutItem.setC(60l);
                    layoutItem.setR(6l);
                    newLaytItemList.add(layoutItem);

                    itemBean = new NavigationInfoItemBean();
                    itemBean.setViewtype(HomeOneViewType.title_viewType);
                    itemBean.setName(infoBlockBean.getName());
                    newInfoItemBeanList.add(itemBean);
                    //   initTag(infoBlockBean);
                }
//                sendContentError("数据异常");
//                Lg.e(TAG, "mergeLayout : navId = " + navId + " , blockId = " + block.getBlockId()
//                        + " , layout is invalid , layout is null or layoutJson is null !");
            }
        }


        if (newInfoItemBeanList.size() > 0 && newLaytItemList.size() > 0) {
            hasNavBlock = true;
        }

        Log.d(TAG, "------merge layout result --> " + newLaytItemList.toString());

    }


    //获取当前系统时间和按钮状态isPlay
    private long getTime(DetailChildBean detailLiveBean) {
        long systemTime = System.currentTimeMillis();
        //获取倒计时的开始时间
        goTime = detailLiveBean.getStartDate();
        //获取倒计时的结束时间
        toTime = detailLiveBean.getEndDate();
        int livestatus = detailLiveBean.getStatus();
        //获取直播状态
        Lg.i("time+time", goTime + "..." + systemTime + "...." + toTime);
        if (livestatus == 1) {
            isPlay = 1;
        } else if (livestatus == 2) {
            isPlay = 2;
        } else if (livestatus == 3) {
            isPlay = 3;
        } else if (systemTime < goTime && systemTime < toTime) {
            isPlay = 0;
        } else {
            isPlay = 5;
        }

        return systemTime;
    }

    //刷新计时UI
    private void setStartTime() {
        Lg.i(TAG, "setStartTime");
        String timeName = "";
        if (time > 0) {
//            if (time > timemiao) {
//                long ss = time % timemiao;
//                timeName = ":" + (ss >= 10 ? ss : "0" + ss);
//                fen = time / timemiao;
//                if (fen > timefen) {
//                    long mm = fen % timefen;
//                    timeName = ":" + (mm >= 10 ? mm : "0" + mm) + timeName;
//                    shi = fen / timefen;
//                    if (shi > timeshi) {
//                        long hh = shi % timeshi;
//                        timeName = "天  " + (hh >= 10 ? hh : "0" + hh) + timeName;
//                        day = shi / timeshi;
//                        timeName = day + timeName;
//                    } else {
//                        timeName = shi + timeName;
//                    }
//                } else {
//                    timeName = fen + timeName;
//                }
//            } else {
//                timeName = String.valueOf(time);
//            }
//            playerFrag.updateRanTime(timeName);
            --time;
            if(!LiveDetailActivity.this.isFinishing())
            mHandler.sendEmptyMessageDelayed(startTime, 1000);
        } else {
            playerFrag.updateRanTime("00");
            mHandler.removeMessages(startTime);
            requestLiveStatus(thisChildBean);
        }
    }


    //获取预约直播数据
    //先写 只有一个子集的
    private void updateTimeUI(){

        final DetailChildBean detailLiveBean = liveStatusBean == null ? thisChildBean : liveStatusBean;
        Lg.i("thisChildBean...", liveStatusBean + "....");
        if (detailLiveBean != null) {
            Lg.i("thisChildBean", "thisChildBean----------------------->>>>" + detailLiveBean.toString());
            //服务器时间和系统时间的矫正值
            App.timeDvalue = JNIRequest.getInstance().getServeTime();
//            BaseApplication.timeDvalue = 0;
            //获取系统时间
            long systemTime = getTime(detailLiveBean);
            Lg.i("TimeMillis??", systemTime + "-----------------------BaseApplication.timeDvalue >>>>" + App.timeDvalue);
            //kjy 测试
//            handler.sendEmptyMessage(GET_LIVE_URL);

            //开始时间
            playerFrag.startRanTime(TimeUtils.getLiveCurTime2(new SimpleDateFormat("MM月dd日  HH : mm"), goTime));
            if (liveStatusBean.getStatus()==0) {
                //查询用户是否预约某一个直播
                if (dbQueryListener == null) {
                    dbQueryListener = new DbQueryLiveidListener() {
                        @Override
                        public void query(final boolean isReserved) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (isReserved) {
                                        isYuYue = 1;
                                        playerFrag.setLiveBtn3("已预约", 1);
                                        //已预约
                                    } else {
                                        isYuYue = 2;
                                        playerFrag.setLiveBtn3("预约", 1);
                                        //预约
                                    }
                                }
                            });

                            Lg.i("kjy", "getRequestComcaLiveAppointmentAdd--->>YKKKK--->>" + isYuYue);
                        }
                    };
                }
                if (statusList.containsKey(0)) {
                    UserReserveHelper.isReserved(resultBean.getData().getLiveId(), resultBean.getData().getChild().get(statusList.get(0)).getSid(), dbQueryListener);
                }
            }
            Lg.i("hahaha111", "case " + isPlay);
            if(liveStatusBean.getStatus()!=0){
                playerFrag.getLinearLayout3().setVisibility(View.GONE);
            }
            switch (liveStatusBean.getStatus()) {

                case 0:
                    playerFrag.getLinearLayout3().setVisibility(View.VISIBLE);
                    if (toTime > systemTime) {
                        setEvent(getReserveBean(detailLiveBean));
                        //获取当前预约倒计时的时间
                        time = (goTime - systemTime) / 1000;
                        setStartTime();
                    }
                    Lg.i("TimeMillis??", toTime + "----------------------->>>>" + System.currentTimeMillis());
                    break;
                case 1:
                    //正在播出
                    playerFrag.getLinearLayout1().setVisibility(View.VISIBLE);
                    if (pay_status == 1) {
                        playerFrag.setLiveBtn1("试看开始", 1);
                    } else {
                        playerFrag.setLiveBtn1("即将开始", 0);
                    }
                    thisChildBean.setStatus(1);
                    // playerFrag.setPlayData(thisChildBean.getSid(),thisChildBean.getSid(),null,resultBean.getData());
                    initPlayerUrl(thisChildBean.getSid(), 1);
                    playerFrag.setSid(thisChildBean.getSid());
                    if (toTime > systemTime) {
                        setEvent(getReserveBean(detailLiveBean));
                    }
                    break;
                case 2:
                    //直播已结束
                    playerFrag.getLinearLayout1().setVisibility(View.VISIBLE);
                    //       playerFrag.getLinearLayout1().setBackgroundResource(R.drawable.detail_btn_live_stop_selector);
                    playerFrag.setLiveBtn1("已结束", 1);
                    playerFrag.updateRanTime("现场已结束，请等待回看");
                    playerFrag.gongRanTitle();
                    //  playerFrag.getLinearLayout2().setVisibility(View.GONE);
                    //查询用户是否预约某一个直播
                    if (dbQueryListener == null) {
                        dbQueryListener = new DbQueryLiveidListener() {
                            @Override
                            public void query(boolean isReserved) {
                                if (isReserved) {//已预约
                                    //直播已结束
                                    UserReserveHelper.add(getReserveBean(detailLiveBean), false);
                                }
                            }
                        };
                    }
                    UserReserveHelper.isReserved(resultBean.getData().getLiveId(), detailLiveBean.getSid(), dbQueryListener);
                    break;
                case 3://回看
                    //查询用户是否预约某一个直播
                    playerFrag.getLinearLayout1().setVisibility(View.VISIBLE);
                    //    playerFrag.getLinearLayout1().setBackgroundResource(R.drawable.detail_btn_selector);
                    if (pay_status == 1) {
                        playerFrag.setLiveBtn1("试看回看", 1);

                    } else {
                        //  playerFrag.setLiveBtn1("回看", 1);
                    }
                    initPlayerUrl(thisChildBean.getSid(), 3);
                    if (dbQueryListener == null) {
                        dbQueryListener = new DbQueryLiveidListener() {
                            @Override
                            public void query(boolean isReserved) {
                                if (isReserved) {//已预约
                                    //直播变成回看
                                    UserReserveHelper.add(getReserveBean(detailLiveBean), false);
                                }
                            }
                        };
                    }
                    UserReserveHelper.isReserved(resultBean.getData().getLiveId(), detailLiveBean.getSid(), dbQueryListener);
                    break;
            }

        }


    }



//    private void updateTimeUI1() {
//
//        if (thisChildBean == null && resultBean.getData().getChild() != null) {
//
//            statusList.clear();
//            int childSize = resultBean.getData().getChild().size();
//            if (childType == 0 && childSize > 0) {
//                childSize = 1;
//            }
//            for (int i = 0; i < childSize; i++) {
//                int status = resultBean.getData().getChild().get(i).getStatus();
//                if (status == 1 || status == 0) {
//                    if (!statusList.containsKey(status)) {
//                        statusList.put(status, i);
//                    }
//                    if (status == 0) {
//                        ++liveNo;
//                    }
//                } else {
//                    statusList.put(status, i);
//                }
//            }
//            if (statusList.containsKey(1)) {
//                thisChildBean = resultBean.getData().getChild().get(statusList.get(1));
//
//                playerFrag.setLiveData(statusList.get(1), resultBean.getData().getChild(), childType);
//
//                playerFrag.getLinearLayout1().setVisibility(View.VISIBLE);
//                if (pay_status == 1) {
//                    playerFrag.setLiveBtn1("试看开始", 2);
//                } else {
//                    playerFrag.setLiveBtn1("即将开始", 0);
//                }
//                if (statusList.containsKey(0)) {
//                    playerFrag.getLinearLayout3().setVisibility(View.VISIBLE);
//                } else {
//                    playerFrag.getLinearLayout3().setVisibility(View.GONE);
//                }
//            } else if (statusList.containsKey(0)) {
//
//                playerFrag.setLiveData(statusList.get(0), resultBean.getData().getChild(), childType);
//
//                thisChildBean = resultBean.getData().getChild().get(statusList.get(0));
//                playerFrag.getLinearLayout3().setVisibility(View.VISIBLE);
//
//                if (statusList.containsKey(3)) {
//
//                    playerFrag.getLinearLayout1().setVisibility(View.VISIBLE);
//                    if (pay_status == 1) {
//                        playerFrag.setLiveBtn1("试看回看", 2);
//                    } else {
//                        playerFrag.setLiveBtn1("回看开始", 1);
//                    }
//                } else {
//                    playerFrag.getLinearLayout1().setVisibility(View.GONE);
//                }
//            } else if (statusList.containsKey(3)) {
//
//
//                thisChildBean = resultBean.getData().getChild().get(statusList.get(3));
//                playerFrag.getLinearLayout1().setVisibility(View.VISIBLE);
//                playerFrag.getLinearLayout3().setVisibility(View.GONE);
//                if (pay_status == 1) {
//                    playerFrag.setLiveBtn1("试看回看", 2);
//
//
//                } else {
//
//                    playerFrag.setLiveBtn1("回看开始", 1);
//                }
//                playerFrag.updateRanTime("现场已结束，您可以回看");
//                playerFrag.gongRanTitle();
//            } else if (statusList.containsKey(2)) {
//
//                thisChildBean = resultBean.getData().getChild().get(statusList.get(2));
//                playerFrag.getLinearLayout1().setVisibility(View.VISIBLE);
//
//                playerFrag.getLinearLayout3().setVisibility(View.GONE);
//
//                playerFrag.setLiveBtn1("已结束", 1);
//
//                playerFrag.updateRanTime("现场已结束，请等待回看");
//                playerFrag.gongRanTitle();
//            }
//        } else if (liveStatusBean != null) {
//            if (liveStatusBean.getStatus() > 0) {
//                if (liveNo > 0) {
//                    --liveNo;
//                }
//                if (liveNo == 0) {
//                    playerFrag.getLinearLayout3().setVisibility(View.GONE);
//                }
//            }
//            switch (liveStatusBean.getStatus()) {
//                case 0:
//                    playerFrag.getLinearLayout3().setVisibility(View.VISIBLE);
//                    break;
//                case 1:
//                    playerFrag.getLinearLayout1().setVisibility(View.VISIBLE);
//                    if (pay_status == 1) {
//                        playerFrag.setLiveBtn1("试看开始", 1);
//                    } else {
//                        playerFrag.setLiveBtn1("即将开始", 0);
//                    }
//                    break;
//                case 2:
//                    //  playerFrag.getLiveBtn1().setTextColor(getResources().getColor(R.color.white3));
//                    playerFrag.getLinearLayout1().setVisibility(View.VISIBLE);
//                    //       playerFrag.getLinearLayout1().setBackgroundResource(R.drawable.detail_btn_live_stop_selector);
//                    playerFrag.setLiveBtn1("已结束", 1);
//                    break;
//                case 3:
//                    playerFrag.getLinearLayout1().setVisibility(View.VISIBLE);
//                    //    playerFrag.getLinearLayout1().setBackgroundResource(R.drawable.detail_btn_selector);
//                    if (pay_status == 1) {
//                        playerFrag.setLiveBtn1("试看回看", 1);
//
//                    } else {
//                        //  playerFrag.setLiveBtn1("回看", 1);
//                    }
//                    break;
//            }
//        }
//        //-------------->>
//        if (pay_status == 1) {
//            getSK(resultBean.getData().getLiveId(), thisChildBean.getSid());
//        }
//
//
//        final DetailChildBean detailLiveBean = liveStatusBean == null ? thisChildBean : liveStatusBean;
//        Lg.i("thisChildBean...", liveStatusBean + "....");
//        if (detailLiveBean != null) {
//            Lg.i("thisChildBean", "thisChildBean----------------------->>>>" + detailLiveBean.toString());
//            //服务器时间和系统时间的矫正值
//            App.timeDvalue = JNIRequest.getInstance().getServeTime();
////            BaseApplication.timeDvalue = 0;
//            //获取系统时间
//            long systemTime = getTime(detailLiveBean);
//            Lg.i("TimeMillis??", systemTime + "-----------------------BaseApplication.timeDvalue >>>>" + App.timeDvalue);
//            //kjy 测试
////            handler.sendEmptyMessage(GET_LIVE_URL);
//
//            //开始时间
//            playerFrag.startRanTime(TimeUtils.getLiveCurTime2(new SimpleDateFormat("MM月dd日  HH : mm"), goTime));
//            if (playerFrag.getLinearLayout3().getVisibility() == View.VISIBLE) {
//                //查询用户是否预约某一个直播
//                if (dbQueryListener == null) {
//                    dbQueryListener = new DbQueryLiveidListener() {
//                        @Override
//                        public void query(final boolean isReserved) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (isReserved) {
//                                        isYuYue = 1;
//                                        playerFrag.setLiveBtn3("已预约", 1);
//                                        //已预约
//                                    } else {
//                                        isYuYue = 2;
//                                        playerFrag.setLiveBtn3("预约", 1);
//                                        //预约
//                                    }
//                                }
//                            });
//
//                            Lg.i("kjy", "getRequestComcaLiveAppointmentAdd--->>YKKKK--->>" + isYuYue);
//                        }
//                    };
//                }
//                if (statusList.containsKey(0)) {
//                    UserReserveHelper.isReserved(resultBean.getData().getLiveId(), resultBean.getData().getChild().get(statusList.get(0)).getSid(), dbQueryListener);
//                }
//            }
//            Lg.i("hahaha111", "case " + isPlay);
//            switch (isPlay) {
//
//                case 0:
//                    if (toTime > systemTime) {
//                        setEvent(getReserveBean(detailLiveBean));
//                        //获取当前预约倒计时的时间
//                        time = (goTime - systemTime) / 1000;
//                        setStartTime();
//                    }
//                    Lg.i("TimeMillis??", toTime + "----------------------->>>>" + System.currentTimeMillis());
//                    break;
//                case 1:
//                    //正在播出
//                    thisChildBean.setStatus(1);
//                    // playerFrag.setPlayData(thisChildBean.getSid(),thisChildBean.getSid(),null,resultBean.getData());
//                    initPlayerUrl(thisChildBean.getSid(), 1);
//                    playerFrag.setSid(thisChildBean.getSid());
//                    if (toTime > systemTime) {
//                        setEvent(getReserveBean(detailLiveBean));
//                    }
//                    break;
//                case 2:
//                    //直播已结束
//                    playerFrag.updateRanTime("现场已结束，请等待回看");
//                    playerFrag.gongRanTitle();
//                    //  playerFrag.getLinearLayout2().setVisibility(View.GONE);
//                    //查询用户是否预约某一个直播
//                    if (dbQueryListener == null) {
//                        dbQueryListener = new DbQueryLiveidListener() {
//                            @Override
//                            public void query(boolean isReserved) {
//                                if (isReserved) {//已预约
//                                    //直播已结束
//                                    UserReserveHelper.add(getReserveBean(detailLiveBean), false);
//                                }
//                            }
//                        };
//                    }
//                    UserReserveHelper.isReserved(resultBean.getData().getLiveId(), detailLiveBean.getSid(), dbQueryListener);
//                    break;
//                case 3://回看
//                    //查询用户是否预约某一个直播
//                    initPlayerUrl(thisChildBean.getSid(), 3);
//                    if (dbQueryListener == null) {
//                        dbQueryListener = new DbQueryLiveidListener() {
//                            @Override
//                            public void query(boolean isReserved) {
//                                if (isReserved) {//已预约
//                                    //直播变成回看
//                                    UserReserveHelper.add(getReserveBean(detailLiveBean), false);
//                                }
//                            }
//                        };
//                    }
//                    UserReserveHelper.isReserved(resultBean.getData().getLiveId(), detailLiveBean.getSid(), dbQueryListener);
//                    break;
//            }
//
//        }
//    }


    public void addReserveBean() {
        final DetailChildBean detailLiveBean = liveStatusBean == null ? thisChildBean : liveStatusBean;
        if (isYuYue == 1) {
            //已经预约
            UserReserveHelper.del(0, Lid, this.thisChildBean.getSid());
        } else if (isYuYue == 2) {
            UserReserveHelper.add(getReserveBean(detailLiveBean), true);
        }
    }

    private ReserveBean getReserveBean(DetailChildBean detailLiveBean) {
        if (detailLiveBean != null) {
            ReserveBean reserveBean = new ReserveBean();
            //goTime---->>测试00000000000000------>>:System.currentTimeMillis() + App.timeDvalue + 1000
            Lg.i("预约的直播开始时间戳", goTime + "------------" + (System.currentTimeMillis() + App.timeDvalue + 10000));
            //预约的直播开始日期
            reserveBean.setLiveStartDate(TimeUtils.getLiveCurTime(detailLiveBean.getStartDate()));
            //预约的直播开始时间戳
            reserveBean.setLiveStartTimeStamp(detailLiveBean.getStartDate());
            //当前的时间戳
            reserveBean.setCurrTimeStamp((System.currentTimeMillis() + App.timeDvalue) / 1000);
            //直播的VID
            reserveBean.setLid(String.valueOf(resultBean.getData().getLiveId()));
            reserveBean.setLivestatus(detailLiveBean.getStatus());
            //直播结束的时间戳
            reserveBean.setLiveEndTimeStamp(detailLiveBean.getEndDate());
            //直播的名称
            reserveBean.setName(resultBean.getData().getEpglive().getLiveName());
            //直播的海报
            reserveBean.setPosterFid(TextUtils.isEmpty(resultBean.getData().getEpglive().getImgh()) ? resultBean.getData().getEpglive().getImg() : resultBean.getData().getEpglive().getImgh());
            //EPG
            reserveBean.setEpgId(epgIdParam == null ? String.valueOf(resultBean.getData().getEpgId()) : epgIdParam);
            //简介
            reserveBean.setStoryPlot(resultBean.getData().getEpglive().getStoryPlot() == null ? "" : detailLiveBean.getStoryPlot());
            reserveBean.setAction(Action.getActionName(Action.OPEN_LIVE_DETAIL_PAGE));
            //子集的sid
            reserveBean.setSid(detailLiveBean.getSid());
            if (detailLiveBean.getStatus() == 2 || detailLiveBean.getStatus() == 3) {
                reserveBean.setLiveFlag(1);
            }
            return reserveBean;
        }
        return null;
    }

    private void setEvent(ReserveBean reserveBean) {
        AddDetailReserveBeanEvent addDetailReserveBeanEvent = new AddDetailReserveBeanEvent(reserveBean);
        LiveManager.getLiveManager().onEvent(addDetailReserveBeanEvent);
        EventBus.getDefault().post(addDetailReserveBeanEvent);
    }


    private int timeY;
    private NotificationManager mNotificationManager;

    private void setYuYueMsg() {
        if (resultBean != null) {
            timeY = (int) System.currentTimeMillis();
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setTicker("已成功预约《" + resultBean.getData().getLiveName() + "》节目，敬请准时观看！") //通知首次出现在通知栏，带上升动画效果的
                    .setContentTitle("已成功预约《" + resultBean.getData().getLiveName() + "》节目")//设置通知栏标题
                    .setContentText("敬请准时观看！")//内容名称
                    .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                    .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                    .setOngoing(false)//ture，设置他为一个正在进行的通知。
                    .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果,使用当前的用户默认设置
                    .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
            Notification notification = mBuilder.getNotification();//将builder对象转换为普通的notification
            notification.flags |= Notification.FLAG_AUTO_CANCEL;//点击通知后通知消失
            mNotificationManager.notify(timeY, notification);
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mNotificationManager.cancel(timeY);
//                }
//            }, 2000);
        }
    }

    //获取播放进度
    private void getSK(long liveId, long sid) {
        HttpRequest.getInstance().excute("getComcaLivePreviewTimeGet", new Object[]{liveId, sid, new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                Lg.i(TAG, "getComcaLivePreviewTimeGet response!!!!!!!----------->>" + response);
                if (TextUtils.isEmpty(response)) {
                    Lg.e(TAG, "getComcaLivePreviewTimeGet response is null or empty!!!");
                    mHandler.sendEmptyMessage(GET_TIME_ERRER);
                    return;
                }
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response);
                if (jsonObject.containsKey("progress")) {
                    long progress = jsonObject.getLongValue("progress");
                    Lg.i(TAG, "getComcaLivePreviewTimeGet progress!!!!!!------->> " + progress);
                    Message msg = mHandler.obtainMessage();
                    msg.what = GET_TIME_YES;
                    msg.obj = progress;
                    mHandler.sendMessage(msg);
                }
            }

            @Override
            public void onError(String error) {
                Lg.i(TAG, "getComcaLivePreviewTimeGet onError!!!!!!------->> " + (error == null ? "" : error));
                mHandler.sendEmptyMessage(GET_TIME_ERRER);
            }
        }});
    }

//    //存储播放进度  36514
//    private void setSK(long sidTo, long progress) {
//        if (pay_status == 1 && sidTo >= 0) {
//            HashMap<String, String> hashMap = new HashMap<>();
//            hashMap.put("Lid", String.valueOf(Lid));
//            hashMap.put("sid", String.valueOf(sidTo));
//            hashMap.put("progress", String.valueOf(progress));
//            liveSid = -1;
//            Lid = 0;
//            Lg.i(TAG, "getComcaLivePreviewTimeGet progress  progress----------->>" + progress);
//            HttpRequest.getInstance().excute("getComcaLivePreviewTimeAdd", new Object[]{JSON.toJSONString(hashMap), new HttpResponseListener() {
//                @Override
//                public void onSuccess(String response) {
//                    Lg.i(TAG, "getComcaLivePreviewTimeAdd response!!!!!!!----------->>" + response);
//                    if (TextUtils.isEmpty(response)) {
//                        Lg.e(TAG, "getComcaLivePreviewTimeAdd response is null or empty!!!");
//                        //弹出存储失败，请重试，或者其他处理，需要处理，wanqi
//                        return;
//                    }
//
//                }
//
//                @Override
//                public void onError(String error) {
//                    Lg.e(TAG, "getComcaLivePreviewTimeAdd onError!!!!!!------->> " + error);
//                }
//            }});
//        }
//    }

    //刷新直播状态
    protected void requestLiveStatus(DetailChildBean detailChildBean) {
        if (epgIdParam == null || contentIdParam == null) {
            epgIdParam = "";
            contentIdParam = "";
        }
        if (detailChildBean == null) {
            return;
        }
        //        String url = "http://114.247.94.86:8010/liveStatus?epgId=1000&contentId=5&childId=1";  epgIdParam  contentIdParam
        HttpRequest.getInstance().excute("getRequestLiveStatus", new Object[]{App.epgUrl, epgIdParam, contentIdParam, String.valueOf(detailChildBean.getSid()), 0,
                new SimpleHttpResponseListener<DetailChildFatherBean>() {
                    @Override
                    public void onSuccess(DetailChildFatherBean response) {
                        Lg.i(TAG, "getRequestLiveStatus response::----------->>" + response);
                        if (response == null || response.getData() == null) {
                            Lg.e(TAG, "getRequestLiveStatus response is null or empty!!!");
                            //弹出获取失败，请重试，或者其他处理，需要处理，wanqi
                            return;
                        }
                        liveStatusBean = response.getData();
                        if (liveStatusBean == null) {
                            Lg.e(TAG, "getRequestLiveStatus response parse to entity failed , data is invalid !!!");
                            //弹出获取失败，请重试，或者其他处理，需要处理，wanqi
                            return;
                        } else {
                            mHandler.sendEmptyMessage(UPDATE_LIVE);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Lg.e(TAG, "getRequestLiveStatus onError , " + error);
                    }
                }});
    }


    //获取播放地址
    public void initPlayerUrl(final long sidTo, Integer status) {
        mysid = sidTo;
        Lg.i(TAG, "---status ==" + status);
        if (status == 3) {

            if (pay_status == 1) {
                playerFrag.initFreeTimeLay(true, 0, resultBean.getData().getEpglive().getLimitTime());
            } else {
                playerFrag.initFreeTimeLay(false, 0, 0);
            }
        } else {

            if (pay_status == 1) {
                getSK(resultBean.getData().getLiveId(), sidTo);
                playerFrag.initFreeTimeLay(true, 0, resultBean.getData().getEpglive().getLimitTime());
            } else {
                playerFrag.initFreeTimeLay(false, 0, 0);
            }
        }
        videoUrl = "";
        if (epgIdParam == null || contentIdParam == null) {
            epgIdParam = "";
            contentIdParam = "";
        }
//        http://114.247.94.86:8010/liveMedia?epgId=1000&contentId=5&childId=1&status=1
//         http://114.247.94.86:8010/videoMedia?epgId=1000&contentId=164003&childId=1   &projectId=1&appId=1&channelId=1&cibnUserId&termId=1&sessionId=1
//        http://114.247.94.86:8010/liveMedia?epgId=1000&contentId=15&childId=20&status=1&projectId=100&appId=1000&channelId=10000&cibnUserId=0&termId=25202&sessionId=1
        HttpRequest.getInstance().excute("getRequestLiveUrl", new Object[]{App.epgUrl, epgIdParam, contentIdParam, String.valueOf(sidTo), String.valueOf(status) + getVideoURL(), 0,
                new SimpleHttpResponseListener<LiveUrlFatherBean>() {
                    @Override
                    public void onSuccess(LiveUrlFatherBean response) {
                        Lg.i(TAG, "getRequestLiveUrl response::----------->>" + response);
                        if (response == null || response.getData() == null) {
                            Lg.e(TAG, "getRequestLiveUrl response is null or empty!!!");
                            noUrlMsg(sidTo);
                            return;
                        }
                        liveUrlBean = response.getData();
                        liveSid = sidTo;
                        Lid = resultBean.getData().getLiveId();
                        Message msg = mHandler.obtainMessage();
                        msg.what = GET_LIVE_URL;
                        msg.obj = liveSid;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(String error) {
                        Lg.e(TAG, "getRequestLiveUrl onError , " + error);
                        noUrlMsg(sidTo);
                    }
                }});
    }

    private void noUrlMsg(final long sidTo) {
        Message msg = mHandler.obtainMessage();
        msg.what = URL_NULL;
        msg.obj = sidTo;
        mHandler.sendMessage(msg);
    }

    public void setDetailData() {

        Lg.i(TAG, "setDetailData");
        vLayout.setVisibility(View.GONE);
        bLayout.setVisibility(View.VISIBLE);
     //   childType = resultBean.getData().getChildType();

        detailBean = resultBean.getData().getEpglive();
        Lg.i("Live test null","detail bean is not null"+detailBean);
        scrollFrag.initMode(childType);
        upDateChild();

     //   mHandler.sendEmptyMessage(UPDATE_LIVE);



        //将数据添加
        // mHandler.sendEmptyMessageDelayed(UPDATE_TIME, 10);

        //查看详情数据对不对
        if (resultBean.getData() != null && resultBean.getData().getChild() != null && resultBean.getData().getChild().size() > 0) {
            //有选集
            need_sid = true;
            childType = resultBean.getData().getChildType();
          //  initMaxFragment();
            if (mysid <= 1) {
                mysid = resultBean.getData().getChild().get(0).getSid();
            }
            if (childType != 0) {
                vLayout.setVisibility(View.GONE);
                scrollFrag.setDatas(resultBean.getData());
                scrollFrag.upDataSid((int) mysid);
            }

        }
        playerFrag.setDetailData(resultBean.getData().getEpglive());
        vLayout.setVisibility(View.GONE);
        scrollFrag.setDatas(resultBean.getData());

        if (hasNavBlock) {
            layoutItemList.clear();
            infoItemBeanList.clear();
            layoutItemList.addAll(newLaytItemList);
            infoItemBeanList.addAll(newInfoItemBeanList);
            scrollFrag.setContentData(layoutItemList, newInfoItemBeanList);
        }





//        if (detailType == 4 && resultBean != null && resultBean.getData() != null && "record".equals(resultBean.getData().getVideoType())) {
//            //判断是否购买版权
//            if (resultBean != null && detailBean.getTransaction() == 1) {
//                pay_bt = 1;
//
//            } else {
//
//            }
//            pay_status = 0;
//            playerFrag.setPayBtn(false);
//        } else {
            //判断是否付费
            Lg.i(TAG, "鉴权");
//            detailBean.setPriceType(0);
            if (resultBean != null && detailBean.getPriceType() == 2) {
                pay_status = 1;
                playerFrag.setPayBtn(true);
                getDetailProductAuth();
            } else {
                //  headerView.getLinearLayout2().setVisibility(View.GONE);
                pay_status = 0;
                playerFrag.setPayBtn(false);
            }


    }


    private void upDateChild(){

        statusList.clear();
         if(resultBean.getData().getChild()!=null&&resultBean.getData().getChild().size()>0){
             //前期直播无子集
             thisChildBean= resultBean.getData().getChild().get(0);
             statusList.put(0,thisChildBean.getStatus());
             mHandler.sendEmptyMessageDelayed(remove_zb, 50);
         }else{
             //直播无子集信息

         }

    }

    private void initMaxFragment() {
        if (childType == 1) {// 电视剧选集
            tvMaxFragment.setDataAndSid(detailBean, resultBean.getData(), (int) mysid);
        } else if (childType == 2) {// 综艺选集
            tvMaxFragment.setDataAndSid(detailBean, resultBean.getData(), (int) mysid);
        }
    }


    public void onEventMainThread(Object addUserReserveEvent) {
        Lg.i(TAG, "已经收到预约消息");
        if(addUserReserveEvent instanceof ReservationBean){
                 ReservationBean addUserReserveEvent1  = (ReservationBean) addUserReserveEvent;
            if (addUserReserveEvent1.getLid() == Lid && addUserReserveEvent1.getSid() == thisChildBean.getSid()) {
                if (addUserReserveEvent1.getAddReserve()) {
                    isYuYue = 1;
                    playerFrag.setLiveBtn3("已预约", 1);
                    ToastUtils.show(LiveDetailActivity.this, "预约成功");
                    setYuYueMsg();
                    final DetailChildBean detailLiveBean = liveStatusBean == null ? thisChildBean : liveStatusBean;
                    setEvent(getReserveBean(detailLiveBean));
                } else {
                    isYuYue = 2;
                    playerFrag.setLiveBtn3("预约", 1);
                    ToastUtils.show(LiveDetailActivity.this, "取消预约成功");
                }
            }
        }else if (addUserReserveEvent instanceof ReserveBeanEvent) {
            //结束直播刷新的EventBus
            if (thisChildBean != null) {
                ReserveBeanEvent closeBean = (ReserveBeanEvent) addUserReserveEvent;
                if (closeBean.getLiveId() != null ) {
                    long live_id = Long.valueOf(closeBean.getLiveId());
                    if ( live_id == resultBean.getData().getLiveId()) {
                        //isRemove = false;
                       // Lg.i("systemTime", "333333333333333333333--->>" + datas.getLivestatus());
                        mHandler.sendEmptyMessageDelayed(remove_zb, 20000);
                    }
                }
            }
        }else if(addUserReserveEvent instanceof  String){
            Lg.i(TAG,"接受到消息 再去鉴权");
            String str = (String) addUserReserveEvent;
            if(str.equals(Constant.WX_RETURN_SUCCESS)){
                getDetailProductAuth();
            }else if(str.equals(Constant.HOME_USER_SIGN_IN)){
                getDetailProductAuth();
            }
        }

    }




    //获取播放地址后缀
    public String getVideoURL() {
//        &projectId=1&appId=1 &channelId=1 &cibnUserId &termId=1 &sessionId=1
        StringBuilder sb = new StringBuilder();
        sb.append("&projectId=");
        sb.append(String.valueOf(App.projId));
        sb.append("&appId=");
        sb.append(String.valueOf(App.appId));
        sb.append("&channelId=");
        sb.append(String.valueOf(App.channelId));
        sb.append("&cibnUserId=");
        sb.append(String.valueOf(App.userId));
        sb.append("&termId=");
        sb.append(App.publicTID);
        sb.append("&sessionId=");
        sb.append(App.sessionId);

        return sb.toString();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(timer_netReceiver!=null){
            unregisterReceiver(timer_netReceiver);}
        //setSK(liveSid, playerFrag.getCurrentPos());
         System.gc();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Lg.i(TAG,"onNewIntent");
        if(scrollFrag!=null){

        }
        iniParams(intent);
     //   requestLiveContent();
        mHandler.sendEmptyMessageDelayed(255, 200);
    }

    public void iniParams(Intent intent) {

        try {
            if(intent==null){
                intent =getIntent();
            }
      //      Intent intent = getIntent();
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

    public void getDetailProductAuth() {
        final DetailAutherBean detailAutherBean = new DetailAutherBean();


        detailAutherBean.setSeriesId(Lid);

        detailAutherBean.setSeriesType(Integer.valueOf(seriesTypeLive));


        HttpRequest.getInstance().excute("getVideoinfProductAuthen", new Object[]{App.bmsurl, JSON.toJSONString(detailAutherBean), new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                if (!TextUtils.isEmpty(response)) {
                    try {
                        DetailAutherResultBean detailAutherResultBean = JSON.parseObject(response, DetailAutherResultBean.class);
                        if (detailAutherResultBean != null && detailAutherResultBean.getResultCode().equals("3")) {
                            pay_status = 3;
                        }
                        mHandler.sendEmptyMessage(2003);
                    } catch (Exception e) {
                        mHandler.sendEmptyMessage(2003);
                    }


                }
                mHandler.sendEmptyMessage(2003);
            }

            @Override
            public void onError(String error) {
                mHandler.sendEmptyMessage(2003);
            }
        }});
    }

    protected void updateUIFormAuther() {
        Lg.i(TAG,pay_status+"....... paystatus");
        if (pay_status == 1) {
            //未通过
            playerFrag.setPayBtn(true);
        } else {
            //鉴权通过  可以正常观看
            playerFrag.setPayBtn(false);
            playerFrag.initFreeTimeLay(false, 0, 0);
        }
    }

    private ShareDialog shareDialog;




    public void shareDialog() {
                Message message = Message.obtain();
                message.what = 567;
                mHandler.sendMessage(message);
    }


    private void showDiaLog() {
        if (shareDialog == null) {
            shareDialog = new ShareDialog(this, R.style.transcutestyle);
            shareDialog.initView(this, Lid, thumb, detailBean.getStoryPlot(), detailBean.getLiveName(), 1);
            int width = (int) (this.getWindowManager().getDefaultDisplay().getWidth() * 0.8);
//            int height = (int) (this.getActivity().getWindowManager().getDefaultDisplay().getHeight() * 0.6);
            Window dialogWindow = shareDialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = width;
//            lp.height = height;
            lp.gravity = Gravity.CENTER;
//            dialogWindow.setWindowAnimations(R.style.fackstyle1);  //添加动画
            dialogWindow.setAttributes(lp);
            shareDialog.show();
        } else {
            shareDialog.initView(this, Lid, thumb, detailBean.getStoryPlot(), detailBean.getLiveName(), 1);
            shareDialog.show();
        }
    }

    public void diamissDialog() {
        if (shareDialog != null && shareDialog.isShowing()) {
            shareDialog.dismiss();
        }
    }

    private boolean openUp;

    //打开全屏的选集
    public void alterOpenUp() {
        if (isOpenUp()) {
            setOpenUp(false);
            bLayout.setVisibility(View.VISIBLE);
            mLayout.setVisibility(View.GONE);
        } else {
            setOpenUp(true);
            bLayout.setVisibility(View.GONE);
            mLayout.setVisibility(View.VISIBLE);
        }
    }

    public boolean isOpenUp() {
        return openUp;
    }

    public void setOpenUp(boolean openUp) {
        this.openUp = openUp;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (shareDialog != null && shareDialog.isShowing()) {
                    diamissDialog();
                } else {
                    if (playerFrag != null) {
                        if (playerFrag.screen_o == 1) {
                            playerFrag.setScreenOrientation();
                            return true;
                        }
                    }
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean wificonn = false;
    private boolean mobleinconn = false;

    private void getNetState() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        wificonn = wifiInfo.isConnected();
        mobleinconn = mobileInfo.isConnected();
       playerFrag.setNetState(wificonn, mobleinconn);
        //    Lg.e("shenfei", "wificonn = " + wificonn + "----mobileconn = " + mobleconn);
        if (!wificonn && !mobleinconn) {
            ToastUtils.show(this, "网络已经断开");
        }
    }
    @Override
    public void onPause() {
      //  playerFrag.surface_show = false;
        isResume = false;
        if(playerFrag==null)
            return;
        playerFrag.surface_show = false;
        playerFrag.pause();
        playerFrag.btnPause();
        Lg.i("LiveDetailActivity ","timer_netReceiver result Code"+timer_netReceiver.getResultCode());

        super.onPause();
    }

    public boolean isResume = false;
    @Override
    public void onResume() {
        //   playerFragment.pop
        isResume = true;

        mHandler.sendEmptyMessageDelayed(1112,200);

        super.onResume();

    }

    private BroadcastReceiver timer_netReceiver = new BroadcastReceiver() {
        private boolean error_net = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(LiveDetailActivity.this.isResume==true) {
                if (action.equals(Intent.ACTION_TIME_TICK)) {
                    // 每分钟整点上报 日志采集
                    playerFrag.logPost_min();
                } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)
                        || action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                    getNetState();
               }
            }
        }
    };
}
