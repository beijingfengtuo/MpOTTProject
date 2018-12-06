package cn.cibnmp.ott.ui.detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.adapter.HomeOneViewType;
import cn.cibnmp.ott.bean.LayoutItem;
import cn.cibnmp.ott.bean.NavigationBlock;
import cn.cibnmp.ott.bean.NavigationInfoBlockBean;
import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.config.CacheConfig;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.jni.SimpleHttpResponseListener;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.ui.detail.bean.DetailAutherBean;
import cn.cibnmp.ott.ui.detail.bean.DetailAutherResultBean;
import cn.cibnmp.ott.ui.detail.bean.DetailBean;
import cn.cibnmp.ott.ui.detail.bean.DetailInfoBean;
import cn.cibnmp.ott.ui.detail.bean.DetailInfoResultBean;
import cn.cibnmp.ott.ui.detail.bean.RecordBean;
import cn.cibnmp.ott.ui.detail.bean.SidEvent;
import cn.cibnmp.ott.ui.detail.bean.VideoUrlInfoBean;
import cn.cibnmp.ott.ui.detail.bean.VideoUrlResultBean;
import cn.cibnmp.ott.ui.detail.player.widgets.PlayerFrag;
import cn.cibnmp.ott.ui.detail.view.DetailScrollFrag;
import cn.cibnmp.ott.ui.detail.view.DetailShowEpisodeMaxFragment;
import cn.cibnmp.ott.ui.detail.view.DetailTvEpisodeMaxFragment;
import cn.cibnmp.ott.ui.search.ToastUtils;
import cn.cibnmp.ott.ui.share.ShareDialog;
import cn.cibnmp.ott.utils.DisplayUtils;
import cn.cibnmp.ott.utils.Lg;
import de.greenrobot.event.EventBus;

/**
 * 
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2018/12/3
 */
public class DetailActivity extends BaseActivity {


    public Long vid;

    public String videoType;

    public boolean hasNavBlock;

    private long mysid;

    public String url;

    public boolean need_sid;

    public final String seriesTypeVideo = "1";//点播type

    public int pay_status = 0;//付费状态  // 0免费不付费，1付费，3付费鉴权通过

    public int collect = 0;//是否收藏

    private static final String TAG = "DetailPrestener";

    public VideoUrlResultBean videoUrlResultBean;
    public VideoUrlInfoBean videoUrlInfoBean;

    private PlayerFrag playerFragment;

    private ImageView ivProess;
    private RotateAnimation animation;
    // 0电影 1电视剧 2综艺 4直播
    private int childType;

    public long record_current_pos, record_duration;//播放记录


    IntentFilter filter;


    private LinearLayout uLayout, bLayout, mLayout, vLayout;


    //所有详情顶部文字信息简介
    private DetailScrollFrag scrollFrag;

    //tv展开fragment
    private DetailTvEpisodeMaxFragment tvMaxFragment;
    private DetailShowEpisodeMaxFragment showMaxFragment;


    private boolean openUp;

    private Bitmap thumb = null; //分享缩略图

    FragmentManager manager ;


    private Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void handleMessage(Message msg) {
            Lg.i(TAG, msg.what + "..... msg what");
            switch (msg.what) {

                case 2006:
                    //错误的状态 请求
                    ToastUtils.show(DetailActivity.this, "详情数据错误");
                    break;
                case 2001:
                    //
                    setDetailData();
                    break;
                case 2003:
                    updateUIFormAuther();
                    break;

                case 120:
                    //收藏成功
                    //   EventBus.getDefault().post("addVideoCollect");
                  //  infoItemBeanList.re
                    scrollFrag.updateCollect("addVideoCollect");
                    collect = 1;
                    break;
                case 121:
                    //收藏失败
                    scrollFrag.updateCollect("addVideoCollectError");
                    collect = 0;
                    break;
                case 122:
                    //取消收藏成功
                    //    EventBus.getDefault().post("deleteLocalCollect");
                    scrollFrag.updateCollect("deleteLocalCollect");
                    collect = 0;
                    break;
                case 123:
                    //取消收藏失败
                    //  EventBus.getDefault().post("deleteLocalCollectError");
                    scrollFrag.updateCollect("deleteLocalCollectError");
                    collect = 1;
                    break;
                case 1003:
                    reuquestPlayerDataSource();
                    break;
                case 56666:

                    long progress = 0;
                    if (msg.obj != null) {
                        progress = (long) msg.obj;
                    }
                    Lg.i(TAG, "AXLprogress" + progress);
                    playerFragment.setPlaySource(url, progress);
                    break;
                case 45678:
                    //    reuquestPlayerDataSource();
                    break;
                case 567:
                    showDiaLog();
                    break;
                case 140:
                    initMaxFragment();
                    break;

            }
            super.handleMessage(msg);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  iniParams();
        setContentView(R.layout.activity_detail);



        checkParams();
//
        initPlayer();

        initView();

        getNetState();

        getVideoCollect();

        initFiter();

        initDate();

        getVideoCollect();
        getVideoRecord();


    }

    private void initFiter() {
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            contentLayoutMax(true);
        }
    }

    private void checkParams(){
        if (TextUtils.isEmpty(contentIdParam)) {
            vid = 0l;
        } else {
            vid = Long.parseLong(contentIdParam);
        }

        if (TextUtils.isEmpty(sid)) {
            mysid = 1;
        } else {
            mysid = Long.parseLong(sid);
            if (mysid > 1)
                need_sid = true;
        }

        if (TextUtils.isEmpty(epgIdParam) || epgIdParam.equals("0")) {
            epgIdParam = "1031";
        }
    }



    private void initMaxFragment() {
        if (childType == 1) {// 电视剧选集
            tvMaxFragment.setDataAndSid(detailBean, resultBean.getData(), (int) mysid);
        } else if (childType == 2) {// 综艺选集

            tvMaxFragment.setDataAndSid(detailBean, resultBean.getData(), (int) mysid);
        }

    }

    private void initDate() {
        requestDetailContent();
    }

    private void initFragment() {


        FragmentTransaction transaction = manager.beginTransaction();
        if (childType == 1) {// 电视剧选集
            tvMaxFragment = new DetailTvEpisodeMaxFragment();
            transaction.replace(R.id.detail_maxcontent, tvMaxFragment);
        } else if (childType == 2) {// 综艺选集
            showMaxFragment = new DetailShowEpisodeMaxFragment();
            transaction.replace(R.id.detail_maxcontent, showMaxFragment);
        }
        try {
            transaction.commit();
        }catch (Exception e){

        }
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

        scrollFrag = DetailScrollFrag.newInstance(0,vid);
        ivProess.startAnimation(animation);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.detail_scrollcontent, scrollFrag);
        transaction.commit();

    }

    public int getLayout() {
        return 0;
    }

    @Override
    public void onResume() {

        if(playerFragment!=null) {
            playerFragment.surface_show = true;
            playerFragment.popLay();
        }
        registerReceiver(timer_netReceiver, filter);
        if(scrollFrag!=null) {

            scrollFrag.scrollToTop();
        }
        super.onResume();

        }

    @Override
    public void onPause() {

        playerFragment.surface_show = false;
        playerFragment.pause();
        playerFragment.btnPause();
        unregisterReceiver(timer_netReceiver);
        super.onPause();

    }

    private BroadcastReceiver timer_netReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                // 每分钟整点上报 日志采集
                playerFragment.logPost_min();
            } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)
                    || action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                getNetState();
            }
        }
    };

    private void getNetState() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        wificonn = wifiInfo.isConnected();
        mobleinconn = mobileInfo.isConnected();
        playerFragment.setNetState(wificonn, mobleinconn);
        if (!wificonn && !mobleinconn) {
            ToastUtils.show(this, "网络已经断开");
        }
    }




    public void requestDetailContent() {
        HttpRequest.getInstance().excute("getDetailContent", new Object[]{App.epgUrl,
                epgIdParam, vid + "", 10 * 60, new SimpleHttpResponseListener<DetailInfoResultBean>() {

            @Override
            public void onSuccess(DetailInfoResultBean response) {
                handleRequestContentSuccess(response);
                mHandler.sendEmptyMessageDelayed(1003, 200);
            }

            @Override
            public void onError(String error) {
                mHandler.sendEmptyMessage(2006);
            }
        }});

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
                            playerFragment.setPlaySource("", 0l);
                        }
                    }, 0);
                }

                mergeData(resultBean.getData());
                //更新数据
                mHandler.sendEmptyMessageDelayed(2001, 200);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 详情页鉴权
     */


    public void getDetailProductAuth() {
        final DetailAutherBean detailAutherBean = new DetailAutherBean();


            detailAutherBean.setSeriesId(vid);

            detailAutherBean.setSeriesType(Integer.valueOf(seriesTypeVideo));


        HttpRequest.getInstance().excute("getVideoinfProductAuthen", new Object[]{App.bmsurl, JSON.toJSONString(detailAutherBean), new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {

                if (!TextUtils.isEmpty(response)) {
                    try {
                        DetailAutherResultBean detailAutherResultBean = JSON.parseObject(response, DetailAutherResultBean.class);
                        Lg.i(TAG,"鉴权鉴权"+detailAutherResultBean.getResultCode());
                        if (detailAutherResultBean != null && detailAutherResultBean.getResultCode().equals("3")) {
                            pay_status = 3;
                        }
                        mHandler.sendEmptyMessage(2003);
                    } catch (Exception e) {
                        mHandler.sendEmptyMessage(2003);
                    }


                }
           //     mHandler.sendEmptyMessage(2003);
            }

            @Override
            public void onError(String error) {

                Lg.i(TAG,"鉴权鉴权失败"+error);
                mHandler.sendEmptyMessage(2003);
            }
        }});

    }

    /**
     * 请求播放地址
     */

    public void reuquestPlayerDataSource() {
        if (vid == 0l) {
            url = "";
            return;
        }
        if (mysid <= 0l) {
            //做容错处理
            mysid = 1;
        }

        HttpRequest.getInstance().excute("getVideoUrl", new Object[]{App.epgUrl, epgIdParam, vid + "", mysid + "" + pingUrl(),
                CacheConfig.cache_an_hour, new SimpleHttpResponseListener<VideoUrlResultBean>() {
            @Override
            public void onSuccess(VideoUrlResultBean response) {

              handleRequestUrlSuccess(response);

            }

            @Override
            public void onError(String error) {

            }
        }});
      //  new HashSet().add();
    }


    public List<LayoutItem> layoutItemList = new ArrayList<>();

    public List<NavigationInfoItemBean> infoItemBeanList = new ArrayList<>();

    public List<LayoutItem> newLaytItemList = new ArrayList<>();

    public List<NavigationInfoItemBean> newInfoItemBeanList = new ArrayList<>();


    /**
     * 带有容错处理的数据合并
     *
     * @param data
     */
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

        newLaytItemList.clear();
        newInfoItemBeanList.clear();


        JSONObject object = null;
        LayoutItem layoutItem = null;
        JSONObject jsonObject = null;
        JSONArray array = null;
        String layout = null;
        System.out.println("hahah");
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
                        layoutItem.setR(3l);
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
                    layoutItem.setR(15l);
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



    //拼接播放地址
    public String pingUrl() {
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

    public void handleRequestUrlSuccess(VideoUrlResultBean bean) {

        Log.i(TAG, "urlSuccesss");

        videoUrlResultBean = bean;

        if (videoUrlResultBean == null || !videoUrlResultBean.getCode().equalsIgnoreCase("1000") || videoUrlResultBean.getData() == null
                || videoUrlResultBean.getData().getMedia() == null || videoUrlResultBean.getData().getMedia().size() <= 0) {
            //开启错误播放事件
            //
            playerFragment.setPlayData("", (int) mysid, videoUrlInfoBean, resultBean.getData());
            playerFragment.setErrorLog("001", "获取播放地址失败");
            return;

        }
        videoUrlInfoBean = videoUrlResultBean.getData();
        if (bean != null) {
            playerFragment.setPlayData("", (int) mysid, videoUrlInfoBean, resultBean.getData());
        }

        //延时200毫秒处理
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (videoUrlInfoBean.getMedia() != null && videoUrlInfoBean.getMedia().size() > 0) {

                    if (pay_status == 1) {
                        if (!videoUrlInfoBean.getAuthCode().equals("3")) {

                            if (videoUrlInfoBean.getPriceType() == 2) {
                                // 付费片子 但鉴权没通过  第二种状态
                               playerFragment.initFreeTimeLay(true, videoUrlInfoBean.getStartTime(), videoUrlInfoBean.getEndTime());
                                record_current_pos = 0;
                            } else {
                                //// 付费片子 但鉴权通过  第三种状态
                                playerFragment.initFreeTimeLay(false, 0, 0);
                            }
                        } else {
                            // 免费片子 不用鉴权 不用付费  第一种状态
                            playerFragment.initFreeTimeLay(false, 0, 0);
                        }

                    }

                    url = videoUrlInfoBean.getMedia().get(0).getUrl();
                    setVideoMsg(url, record_current_pos);

                }

            }
        }, 200);

    }

    private void setVideoMsg(String url, long progress) {
        Lg.i(TAG, "url:" + url + "...." + progress);
        this.url = url;
        mHandler.removeMessages(56666);
        Message message = mHandler.obtainMessage();
        message.what = 56666;
        message.obj = progress;
        mHandler.sendMessageDelayed(message, 400);
    }


    public void initPlayer() {
       manager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        playerFragment = PlayerFrag.newInstance(childType, vid);
        fragmentTransaction.replace(R.id.detail_player, playerFragment);
        fragmentTransaction.commit();
    }

    private boolean wificonn = false;
    private boolean mobleinconn = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if(shareDialog!=null&&shareDialog.isShowing()){
                    diamissDialog();
                }else{
                if (playerFragment != null) {
                    if (playerFragment.screen_o == 1) {
                        playerFragment.setScreenOrientation();
                        return true;
                    }
                }}
                break;
        }
        return super.onKeyDown(keyCode, event);
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

    public boolean need_Playcurrent_pos = false;

    protected void getVideoRecord() {
        Lg.i(TAG, "getVideoRecord");
        HttpRequest.getInstance().excute("getRequestVideoRecordGet",
                new Object[]{vid, new HttpResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        Lg.i(TAG, "getRequestVideoRecordSuccess");
                        if (!need_sid) {
                            try {
                                RecordBean recordBean = JSON.parseObject(response, RecordBean.class);
                                Lg.i(TAG, "getRequestVideoRecordSuccess" + recordBean.toString());
                                if (recordBean != null) {
                                    mysid = recordBean.getSid();
                                    Lg.i(TAG, "getRequestVideoRecordSuccess" + mysid + "");
                                    if (Math.abs(recordBean.getDuration() - recordBean.getCurrentPos()) <= 2000) {
                                        record_current_pos = 0;
                                    } else {
                                        need_Playcurrent_pos = true;
                                        record_current_pos = recordBean.getCurrentPos();
                                        Lg.i(TAG, "getRequestVideoRecordSuccess" + record_current_pos + "");
                                    }
                                }
                                need_sid = false;

                            } catch (Exception e) {

                            }

                        }
                    }

                    @Override
                    public void onError(String error) {
                        Lg.i(TAG, "getRequestVideoRecordSuccessError");
                        record_current_pos = 0;
                        need_sid = false;
                    }
                }});

    }


    protected void getVideoCollect() {
        HttpRequest.getInstance().excute("getRequestVideoCollectGet", new Object[]{
                vid, new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                collect = 1;
            }

            @Override
            public void onError(String error) {
                collect = 0;
            }
        }
        });
    }


    public void addVideoCollect() {

        if (detailBean == null) {
            return;
        }

        RecordBean rb = new RecordBean();
        rb.setAction("open_normal_detail_page");
        rb.setEpgId(Integer.valueOf(epgIdParam));
        rb.setVid(vid);
        rb.setSid(mysid);
        rb.setVname(detailBean.getVname());
        rb.setPosterFid(TextUtils.isEmpty(detailBean.getImgh())?detailBean.getImg():detailBean.getImgh());
        rb.setStoryPlot(detailBean.getStoryPlot());
        rb.setCurrentPos(record_current_pos);
        rb.setDuration(record_duration);

        HttpRequest.getInstance().excute("addLocalCollect", new Object[]{
                JSON.toJSONString(rb), new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                mHandler.sendEmptyMessage(120);
                EventBus.getDefault().post(new RecordBean());
            }

            @Override
            public void onError(String error) {
                mHandler.sendEmptyMessage(121);
            }
        }
        });
    }

    public void deleteVideoCollect() {
        HttpRequest.getInstance().excute("deleteLocalCollect", new Object[]{false, vid, new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                mHandler.sendEmptyMessage(122);
                EventBus.getDefault().post(new RecordBean());
            }

            @Override
            public void onError(String error) {
                mHandler.sendEmptyMessage(123);
            }
        }});
    }


    public void contentLayoutMax(boolean b) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) uLayout
                .getLayoutParams();
        if (b) {
            params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.TITLE_CHANGED);
            playerFragment.changeScreenOrientation(1);
        } else {
            params.height = DisplayUtils.getValue(400);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            playerFragment.changeScreenOrientation(0);
        }
        uLayout.setLayoutParams(params);
    }



    private void setDetailData() {


        Lg.i(TAG, "setDetailData");
        vLayout.setVisibility(View.GONE);
        bLayout.setVisibility(View.VISIBLE);

        //将数据添加
        childType = resultBean.getData().getChildType();
        detailBean = resultBean.getData().getEpgvideo();
        scrollFrag.initMode(childType);

        initFragment();
        //创建点播选集


      //  initMaxFragment();
        //查看详情数据对不对
        if (childType != 0 && resultBean.getData() != null && resultBean.getData().getChild() != null && resultBean.getData().getChild().size() > 0) {
            //有选集
            need_sid = true;
            if (mysid <= 1) {
                mysid = resultBean.getData().getChild().get(0).getSid();
            }
            if (childType != 0) {
                vLayout.setVisibility(View.GONE);
                scrollFrag.setDatas(resultBean.getData());
                scrollFrag.upDataSid((int) mysid);
            }

        }

        vLayout.setVisibility(View.GONE);
        playerFragment.setDetailData(resultBean.getData().getEpgvideo());
        scrollFrag.setDatas(resultBean.getData());
        mHandler.sendEmptyMessageDelayed(140,100);
        if (hasNavBlock) {
            layoutItemList.clear();
            infoItemBeanList.clear();
            layoutItemList.addAll(newLaytItemList);
            infoItemBeanList.addAll(newInfoItemBeanList);
            scrollFrag.setContentData(layoutItemList, newInfoItemBeanList);
        }


        //判断是否付费
        Lg.i(TAG, "鉴权");
//            detailBean.setPriceType(0);
        if (resultBean != null && detailBean.getPriceType() == 2) {
            pay_status = 1;
            playerFragment.setPayBtn(true);
            getDetailProductAuth();
        } else {
            //  headerView.getLinearLayout2().setVisibility(View.GONE);
            pay_status = 0;
            playerFragment.setPayBtn(false);
        }



    }

    protected void updateUIFormAuther() {
        Lg.i(TAG,"update_UIFORMAUTHER"+pay_status);
        if (pay_status == 1) {
            //未通过
            playerFragment.setPayBtn(true);
        } else {
            //鉴权通过  可以正常观看
         //   playerFragment.setPayBtn(false);
            playerFragment.initFreeTimeLay(false, 0, 0);
        }
    }


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

    public void setOpenUp(boolean openUp) {
        this.openUp = openUp;
    }


    public void onEventMainThread(Object event1) {
        if(event1 instanceof SidEvent) {
            if (event1 == null)
                return;

            SidEvent event = (SidEvent) event1;
            if (mysid != event.getSid()) {
                if (playerFragment != null) {
                    playerFragment.pause();
                }

                mysid = event.getSid();
                upDateMaxEpisodeItem((int) mysid);
                upDateEpisodeItem((int) mysid);
                reuquestPlayerDataSource();

            }
        }else if(event1 instanceof  String){
            Lg.i(TAG,"接受到消息 再去鉴权");
            String str = (String) event1;
            if(str.equals(Constant.WX_RETURN_SUCCESS)){
                getDetailProductAuth();
            }else if(str.equals(Constant.HOME_USER_SIGN_IN)){
                getDetailProductAuth();
            }
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        diamissDialog();
        System.gc();

    }

    public boolean isOpenUp() {
        return openUp;
    }

    // 与Max界面的联动
    public void upDateEpisodeItem(int sid) {
        if (childType == 1) {
            scrollFrag.upDataSid(sid);
        } else if (childType == 2) {
            scrollFrag.upDataSid(sid);
        }
    }

    public void upDateMaxEpisodeItem(int sid) {
        if (childType == 1) {
            tvMaxFragment.setSidChange(sid);
        } else if (childType== 2) {
            showMaxFragment.setSidChange(sid);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        iniParams(intent);

        checkParams();

        initPlayer();

        initView();

        getNetState();

        getVideoCollect();

        initFiter();

        initDate();
        getVideoCollect();
        getVideoRecord();
        ;
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        Lg.i(TAG,"windowFocusChanged");
//        if (hasFocus && Build.VERSION.SDK_INT >= 17) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//       }
//    }



    private ShareDialog shareDialog;

   public void shareDialog() {
       Message message = Message.obtain();
       message.what = 567;
       mHandler.sendMessage(message);
    }

    public void diamissDialog() {
        if (shareDialog != null && shareDialog.isShowing()) {
            shareDialog.dismiss();
        }
    }

    private void showDiaLog(){
        if (shareDialog == null) {
            shareDialog = new ShareDialog(this, R.style.transcutestyle);
            shareDialog.initView(this, vid, thumb, detailBean.getStoryPlot(), detailBean.getVname(), 0);
            int width = (int) (this.getWindowManager().getDefaultDisplay().getWidth() * 0.8);
            Window dialogWindow = shareDialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = width;
            lp.gravity = Gravity.CENTER;
//            dialogWindow.setWindowAnimations(R.style.fackstyle1);  //添加动画
            dialogWindow.setAttributes(lp);
            shareDialog.show();
        } else {
            shareDialog.initView(this, vid, thumb, detailBean.getStoryPlot(), detailBean.getVname(), 0);
            shareDialog.show();
        }
    }


}
