package cn.cibnmp.ott.ui.home;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ta.utdid2.android.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.ScreeningDataBean;
import cn.cibnmp.ott.bean.TestRecommendListBean;
import cn.cibnmp.ott.config.CacheConfig;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.jni.SimpleHttpResponseListener;
import cn.cibnmp.ott.library.pullable.PullToRefreshLayout;
import cn.cibnmp.ott.ui.HomeActivity;
import cn.cibnmp.ott.ui.base.BaseFragment;
import cn.cibnmp.ott.ui.categoryList.HomeOnItemClickListener;
import cn.cibnmp.ott.ui.categoryList.HomePlayOnClickListener;
import cn.cibnmp.ott.ui.detail.bean.VideoUrlResultBean;
import cn.cibnmp.ott.ui.media.IjkVideoView;
import cn.cibnmp.ott.ui.media.ui.VideoPlayView;
import cn.cibnmp.ott.ui.share.ShareDialog;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.NetWorkUtils;
import cn.cibnmp.ott.utils.img.ImageFetcher;
import cn.cibnmp.ott.widgets.PullableRecyclerView;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 玩票页面-其他选项卡页面
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2017/12/29
 *
 * 说明：TODO zxy player待处理 TestRecommendListBean类无用
 */
public class HomeThreeOtherFragment extends BaseFragment implements View.OnClickListener{
    private static String TAG = HomeThreeOtherFragment.class.getName();
    private static HomeActivity homeActivity;
    private View root;

    //进度条
    private RelativeLayout octv_looding;
    private ImageView octv_ivpi;
    private RotateAnimation animation;

    //所有列表的填充数据list
    private Bundle mBundle;
    //所有数据对象
    private TestRecommendListBean recommendListBean;
    //节目内容数据
    private ScreeningDataBean.DataBean.ListcontentBean.ContentBean contentBean;

    //上拉下拉控件
    private PullToRefreshLayout mToRefreshLayout;
    //GridView列表控件
    private PullableRecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private HomeThreeOtherGridAdapter adapter;
    private RelativeLayout rlNullData;
    //导航栏目内容列表（京剧 导航栏目下所有视频列表集合）
    private List<ScreeningDataBean.DataBean.ListcontentBean.ContentBean> contentBeanList;
    //临时集合，导航栏目内容列表（京剧 导航栏目下所有视频列表集合）
    private List<ScreeningDataBean.DataBean.ListcontentBean.ContentBean> list;

    //播放器View
    private VideoPlayView videoItemView;
    //小窗口播放
    private RelativeLayout layoutSmall;
    private FrameLayout layoutSmallVideo;
    private ImageView btnClose;
    //全屏窗口
    private RelativeLayout layoutAll;
    private FrameLayout layoutFullVideo;

    //页面\接口参数
    private String epgId; //epgID
    private String contentId; //栏目id

    //分享列表
    private ShareDialog shareDialog;

    /**
     * 加载View
     **/
    private final int LODING_DATE_NAVCONTENT = 600;
    private final int LODING_DATE = 666;
    private final int LODING_ERROR_500 = 500;
    private final int LODING_PLAY_RUL = 601;

    //更新播放器
    private final int PLAY_UPDATE = 700;
    private final int PLAY_START = 701;
    private final int PLAY_PAUSE = 702;
    private final int PLAY_STOP = 703;
    private final int SHARE = 800;

    private int postion; //当前点击的item索引
    private int lastPostion = -1; //下一个item的索引

    private String playUrl;
    private Bitmap thumb = null; //分享缩略图

    //分页加载
    private int PAGESIZE = 0;
    private int PAGENUM = 100;
    private boolean isLoadMoreFinish = false; //是否加载完成
    private int startIndex = 0; //添加item的下标索引
    private boolean isRefresh = false; //下拉/上拉标示 true下拉、false上拉

    private int error_code = -1; //请求接口错误 0 获取列表数据失败、1 获取播放地址失败

    public static HomeThreeOtherFragment newInstance(HomeActivity activity, Bundle bundle) {
        homeActivity = activity;
        HomeThreeOtherFragment fragment = new HomeThreeOtherFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.home_three_other_fragment, container, false);
        //获取参数
        Bundle bundle = getArguments();
        epgId = (String) bundle.get(Constant.CHANNEL_EPGID);
        contentId = (String) bundle.get(Constant.BUNDLE_CONTENTID);
        setUI();
        return root;
    }

    /**
     * 初始化UI
     */
    private void setUI() {
        //加载数据进度条
        octv_looding = (RelativeLayout) root.findViewById(R.id.octv_looding);
        octv_ivpi = (ImageView) root.findViewById(R.id.octv_ivpi);
        animation = (RotateAnimation) AnimationUtils.loadAnimation(getActivity(),
                R.anim.rotating);
        LinearInterpolator lir = new LinearInterpolator();
        animation.setInterpolator(lir);

        mToRefreshLayout = (PullToRefreshLayout) root.findViewById(R.id.home_three_other_head);
        //GridView控件
        recyclerView = (PullableRecyclerView) root.findViewById(R.id.rv_home_three_other_recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        rlNullData = (RelativeLayout) root.findViewById(R.id.rl_null_data);

        videoItemView = new VideoPlayView(getActivity());

        //小窗口布局
        layoutSmall = homeActivity.getLayoutSmall();
        layoutSmallVideo = homeActivity.getLayoutSmallVideo();
        btnClose = homeActivity.getBtnClose();

        //全屏布局
        layoutAll = homeActivity.getLayoutAll();
        layoutFullVideo = homeActivity.getLayoutFullVideo();

//         下拉和上拉回调监听
        setRefresh();
        initListener();
        getContentListData();
    }

    /**
     * 上拉下拉回调监听
     */
    private void setRefresh() {
        mToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            /**
             * 下拉刷新
             *
             * @param pullToRefreshLayout
             */
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                isRefresh = true;
                getCompletion(false);
                getContentListData();
                getRefresh();
                // 加载操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
//                        ToastUtils.show(context, "施主,没有内容了,请回头吧");
                        try {
                            if (pullToRefreshLayout != null) {
                                //加载完毕
                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.sendEmptyMessageDelayed(0, 1000);
            }

            /**
             * 上拉加载
             *
             * @param pullToRefreshLayout
             */
            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                isRefresh = false;
                getLoadMore();
                // 加载操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
//                        ToastUtils.show(context, "施主,没有内容了,请回头吧");
                        try {
                            if (pullToRefreshLayout != null) {
                                // 加载完毕
                                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.sendEmptyMessageDelayed(0, 500);
            }
        });
    }

    /**
     * 下拉刷新
     */
    private void getRefresh() {
        if (contentBeanList != null) {
            contentBeanList.clear();
        }
        PAGESIZE = 0;
        isLoadMoreFinish = false;
    }

    /**
     * 上拉加载
     */
    private void getLoadMore() {
        if (!isLoadMoreFinish) {
            PAGESIZE += 1;
            getContentListData();
        }
    }

    @Override
    public void onEventMainThread(String event) {
        super.onEventMainThread(event);
        if (event.equals(Constant.NETWORK_OK_HOME_THREE)) {
            //有可用网络
        } else if (event.equals(Constant.NETWORK_NO_HOME_THREE)) {
            //无可用网络
        }
    }

    /**
     * 获取栏目下内容
     */
    public void getContentListData() {
        if (!NetWorkUtils.isNetworkAvailable(getActivity())) {
            //当前网络不可用
            Lg.i(TAG, "当前网络异常，请重试连接!");
            mHandler.sendEmptyMessage(LODING_ERROR_500);
            return;
        }

        startLooding();

        final String url = App.epgUrl + "/listContent?epgId=" + epgId + "&columnId=" + contentId + "&pageSize=" + PAGESIZE + "&pageNum=" + PAGENUM;
        HttpRequest.getInstance().excute("getListProgramData", new Object[]{url,
                CacheConfig.cache_no_cache, new SimpleHttpResponseListener<ScreeningDataBean>() {

            @Override
            public void onSuccess(ScreeningDataBean screeningDataBean) {
                Lg.i(TAG, "玩票页面 - 导航栏目下的数据：" + screeningDataBean.toString());
                handleLoadContentListDataSuccessResponse(screeningDataBean);
            }

            @Override
            public void onError(String error) {
                Lg.i(TAG, "玩票页面 - 导航栏目下的数据：getHomeNavContent : error--> " + error);
                handleErrorResponse();
            }
        }});
    }

    /**
     * 获取数据成功（导航下的栏目内容，如京剧导航标签下的视频列表数据）
     *
     * @param screeningDataBean
     */
    private void handleLoadContentListDataSuccessResponse(ScreeningDataBean screeningDataBean) {
        try {
            if (screeningDataBean == null || screeningDataBean.getData() == null || screeningDataBean.getData().getListcontent() == null) {
                handleErrorResponse();
                return;
            }
            if (list != null) {
                list.clear();
            }
            list = screeningDataBean.getData().getListcontent().getContent();

            if (list != null && list.size() > 0) {
                if (contentBeanList != null) {
                    startIndex = contentBeanList.size() - 1;
                    contentBeanList.addAll(list);
                } else {
                    contentBeanList = new ArrayList<ScreeningDataBean.DataBean.ListcontentBean.ContentBean>();
                    contentBeanList.addAll(list);
                }

                if (list.size() < PAGENUM) {
                    isLoadMoreFinish = true;
                } else {
                    isLoadMoreFinish = false;
                }
            } else {
                if (PAGESIZE > 0) {
                    PAGESIZE -= 1;
                } else {
                    PAGESIZE = 0;
                }
            }

            if (contentBeanList == null || contentBeanList.size() <= 0) {
                handleErrorResponse();
                return;
            }

            mHandler.sendEmptyMessage(LODING_DATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    /**
     * 获取播放地址
     *
     * @param data
     */
    private void getPlayUrl(ScreeningDataBean.DataBean.ListcontentBean.ContentBean data) {
        String childId = "";
        if (data == null) {
            playUrl = "";
            return;
        }

        if (TextUtils.isEmpty(data.getContentId())) {
            playUrl = "";
            return;
        }

        if (Long.parseLong(data.getSid()) <= 0l) {
            //做容错处理
            childId = "1";
        }

        HttpRequest.getInstance().excute("getVideoUrl", new Object[]{App.epgUrl, data.getEpgId() + "", data.getContentId(), childId + pingUrl(),
                CacheConfig.cache_an_hour, new SimpleHttpResponseListener<VideoUrlResultBean>() {
            @Override
            public void onSuccess(VideoUrlResultBean response) {
                handleLoadVideoUrlResultBeanSuccessResponse(response);
            }

            @Override
            public void onError(String error) {
                error_code = 1;
                handleErrorResponse();
            }
        }});
    }

    /**
     * 获取播放地址数据成功
     *
     * @param videoUrlResultBean 播放地址类
     */
    private void handleLoadVideoUrlResultBeanSuccessResponse(VideoUrlResultBean videoUrlResultBean) {
        try {
            if (videoUrlResultBean == null || videoUrlResultBean.getData() == null || videoUrlResultBean.getData().getMedia() == null || videoUrlResultBean.getData().getMedia().size() == 0) {
                handleErrorResponse();
                return;
            }

            playUrl = videoUrlResultBean.getData().getMedia().get(0).getUrl();
            if (StringUtils.isEmpty(playUrl)) {
                handleErrorResponse();
                return;
            }

            mHandler.sendEmptyMessage(LODING_PLAY_RUL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据失败
     *
     */
    private void handleErrorResponse() {
        mHandler.sendEmptyMessage(LODING_ERROR_500);
    }

    /**
     * 添加列表数据
     **/
    private void setGridData() {
        if(adapter == null) {
            createAdapter();
        } else {
            if (isRefresh) {
                createAdapter();
            } else {
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        adapter.notifyItemChanged(startIndex + i);
                    }
                }
            }
        }
    }

    /**
     * RecyclerView的adapter
     */
    private void createAdapter() {
        adapter = new HomeThreeOtherGridAdapter(context, contentBeanList, new HomeOnItemClickListener() {
            @Override
            public void onItemClickListener(Bundle bundle) {
                onItemClick(bundle.getInt(Constant.HOME_THREE_OTHER_RECYCLERVIEW_ITEM_POSITION)
                        , (ScreeningDataBean.DataBean.ListcontentBean.ContentBean) bundle.getSerializable(Constant.HOME_THREE_OTHER_RECYCLERVIEW_ITEM_DATA));
            }
        }, new HomePlayOnClickListener() {
            @Override
            public void getPlayOnClickListener(HomeThreeOtherGridAdapter.VideoViewHolder viewHolder, int type, Object itemData) {
                getViewClickListener(viewHolder, type, (ScreeningDataBean.DataBean.ListcontentBean.ContentBean)itemData);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * 播放器按钮的点击处理
     *
     * @param type
     */
    private void getViewClickListener(HomeThreeOtherGridAdapter.VideoViewHolder viewHolder, int type, final ScreeningDataBean.DataBean.ListcontentBean.ContentBean contentBean) {
        switch (type) {
            //分享
            case Constant.PLAY_TAG_SHARE:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            thumb = getHttpBitmap(contentBean.getImgh());
                        } catch (Exception e) {
                            thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
                            e.printStackTrace();
                        }

                        Message message = Message.obtain();
                        message.what = SHARE;
                        message.obj = contentBean;
                        mHandler.sendMessage(message);
                    }
                }).start();
                break;

            //收藏
            case Constant.PLAY_TAG_COLLECT:

                break;

            //点赞
            case Constant.PLAY_TAG_ZAN:

                break;
        }
    }

    /**
     * 分享网络图片url转Bitmap
     *
     * @param url
     * @return
     */
    private Bitmap getHttpBitmap(String url) {
        Bitmap bitmap = null;
        try {
            URL pictureUrl = new URL(url);
            InputStream in = pictureUrl.openStream();
            bitmap = ImageFetcher.getInstance().getBitmapThumbnail(BitmapFactory.decodeStream(in), 20, 20, true);
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }
        return bitmap;
    }

    /**
     * Handler处理
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LODING_DATE:
                    stopLooding();
                    recyclerView.setVisibility(View.VISIBLE);
                    rlNullData.setVisibility(View.GONE);
                    setGridData();
                    break;

                case LODING_ERROR_500:
                    stopLooding();
                    if (error_code == 1) {
                        Lg.i(TAG, getResources().getString(R.string.error_play_url));
                        error_code = -1;
                        Toast.makeText(context, getResources().getString(R.string.error_play_url), Toast.LENGTH_LONG).show();
                    } else {
                        rlNullData.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        Lg.i(TAG, getResources().getString(R.string.error_toast));
                    }
//                    Toast.makeText(context, getResources().getString(R.string.error_toast), Toast.LENGTH_LONG).show();
                    break;

                case LODING_PLAY_RUL: //播放节目
//                    //TODO zxy 2018.2.22
//                    if (videoItemView != null) {
//                        videoItemView.stop();
//                        videoItemView.release();
//                    }

                    //日志上传
                    if (contentBean != null && !StringUtils.isEmpty(contentBean.getSid())) {
                        videoItemView.setVideoDisc(Long.valueOf(contentBean.getContentId()), Long.valueOf(contentBean.getSid())
                                , contentBean.getDisplayName(), 1, String.valueOf(contentBean.getVipType())
                                , 4, "", "", "", 2);
                    }

                    //开始播放视频
                    videoItemView.start(playUrl);
                    break;

                case SHARE: //分享
                    shareDialog((ScreeningDataBean.DataBean.ListcontentBean.ContentBean) msg.obj);
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 开始加载动画
     */
    private void startLooding() {
        stopLooding();
        octv_looding.setVisibility(View.VISIBLE);
        octv_ivpi.startAnimation(animation);
    }

    /**
     * 停止加载动画
     */
    public void stopLooding() {
        if (octv_looding != null && animation != null) {
            octv_looding.setVisibility(View.GONE);
            animation.cancel();
            octv_ivpi.clearAnimation();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        diamissDialog();
    }

    public boolean getOnKeyDown() {
        if (shareDialog != null && shareDialog.isShowing()) {
            //判断分享布局是否显示
            diamissDialog();
            return true;

        } else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //当前为横屏显示
            if (videoItemView != null) {
                videoItemView.setScreenOrientationView(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            return true;

        }
//        else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                //当前为竖屏显示
//            if (videoItemView != null && videoItemView.isPlay()) {
//                getCompletion();
//            }
//        }
        return false;
    }

    /**
     * 分享对话框
     *
     * @param contentBean
     */
    private void shareDialog(ScreeningDataBean.DataBean.ListcontentBean.ContentBean contentBean) {
        if (contentBean == null) {
            return;
        }
        if (shareDialog == null) {
            shareDialog = new ShareDialog(getActivity(), R.style.transcutestyle);
            shareDialog.initView(getActivity(), Integer.valueOf(contentBean.getContentId()), thumb, contentBean.getDisplayName(), contentBean.getClsName(), 1);
            int width = (int) (this.getActivity().getWindowManager().getDefaultDisplay().getWidth() * 0.8);
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
            shareDialog.initView(getActivity(), Integer.valueOf(contentBean.getContentId()), thumb, contentBean.getDisplayName(), contentBean.getClsName(), 0);
            shareDialog.show();
        }
    }

    public void diamissDialog() {
        if (shareDialog != null && shareDialog.isShowing()) {
            if (thumb != null) {
                thumb.recycle();
                thumb = null;
            }
            shareDialog.dismiss();
        }
    }
/*********************************播放器***********************************************/

    /**
     * 播放器监听
     */
    private void initListener() {
        btnClose.setOnClickListener(this);
        layoutSmall.setOnClickListener(this);

        /**
         * 播放结束监听
         */
        videoItemView.setPlayerListener(new VideoPlayView.PlayerListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
//                //如果是横屏状态，改为竖屏状态
//                if (videoItemView.getScreenOrientation()
//                        == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                    //横屏播放完毕，重置
//                    videoItemView.setScreenOrientationView(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                }

                if (recyclerView != null && postion != -1) {
                    View view = recyclerView.findViewHolderForAdapterPosition(postion).itemView;
                    FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.layout_video);
                    if (view != null) {
                        getShowPlayView(view, true);
                    }
//                    videoItemView.pause();
//                    videoItemView.release();
                    if (frameLayout != null && frameLayout.getChildCount() > 0) {
                        frameLayout.removeAllViews();
                    }
                }

//                getCompletion(true);
            }
        });

        /**
         *
         */
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                //获取当前View在Adapter中的索引位置
                int index = recyclerView.getChildAdapterPosition(view);
                getShowPlayView(view, false);
//                view.findViewById(R.id.rl_home_three_other_variety_gridview_item_show).setVisibility(View.VISIBLE);
                if (index == postion) {
                    FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.layout_video);
                    frameLayout.removeAllViews();
                    if (videoItemView != null &&
                            ((videoItemView.isPlaying()) || videoItemView.VideoStatus() == IjkVideoView.STATE_PAUSED)) {
                        view.findViewById(R.id.rl_home_three_other_variety_gridview_item_show).setVisibility(View.GONE);
                    }

                    if (videoItemView.VideoStatus() == IjkVideoView.STATE_PAUSED) {
                        if (videoItemView.getParent() != null)
                            ((ViewGroup) videoItemView.getParent()).removeAllViews();
                        frameLayout.addView(videoItemView);
                        return;
                    }

                    if (layoutSmall.getVisibility() == View.VISIBLE && videoItemView != null && videoItemView.isPlaying()) {
                        layoutSmall.setVisibility(View.GONE);
                        layoutSmallVideo.removeAllViews();
                        videoItemView.setShowContoller(true);
                        frameLayout.addView(videoItemView);
                    }
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                int index = recyclerView.getChildAdapterPosition(view);
                if (index == postion) {
                    FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.layout_video);
                    frameLayout.removeAllViews();
                    //显示小视频
//                    if (layoutSmall.getVisibility() == View.GONE && videoItemView != null
//                            && videoItemView.isPlay()) {
//                        layoutSmallVideo.removeAllViews();
//                        videoItemView.setShowContoller(false);
//                        layoutSmallVideo.addView(videoItemView);
//                        layoutSmall.setVisibility(View.VISIBLE);
//                    }

                    //TODO 目前改为滑动后关闭播放器
                    getCompletion(false);
                }
            }
        });
    }

    /**
     * 视频播放结束
     *
     * @param isShow true 显示重播按钮、false 隐藏重播按钮
     */
    public void getCompletion(boolean isShow) {
        //播放完还原播放界面
        if (layoutSmall.getVisibility() == View.VISIBLE) {
            layoutSmallVideo.removeAllViews();
            layoutSmall.setVisibility(View.GONE);
            videoItemView.setShowContoller(true);
        }
        FrameLayout frameLayout = (FrameLayout) videoItemView.getParent();

        videoItemView.stop();
        videoItemView.release();
        if (frameLayout != null && frameLayout.getChildCount() > 0) {
            frameLayout.removeAllViews();
            View itemView = (View) frameLayout.getParent();

            if (itemView != null) {
                getShowPlayView(itemView, isShow);
                //                itemView.findViewById(R.id.rl_home_three_other_variety_gridview_item_show).setVisibility(View.VISIBLE);
            }
        }
        lastPostion = -1;

    }

    /**
     * 设置重播按钮是否显示
     *
     * @param itemView
     * @param isShow true 显示、 false 隐藏
     */
    private void getShowPlayView(View itemView, boolean isShow) {
        if (isShow) {
            //重播按钮显示
            itemView.findViewById(R.id.img_home_three_other_variety_gridview_item_replay_play).setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.img_home_three_other_variety_gridview_item_bg).setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.img_home_three_other_variety_gridview_item_bg2).setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.img_home_three_other_variety_gridview_item_play).setVisibility(View.GONE);

        } else {
            //重新播放按钮隐藏
            itemView.findViewById(R.id.img_home_three_other_variety_gridview_item_replay_play).setVisibility(View.GONE);
            itemView.findViewById(R.id.img_home_three_other_variety_gridview_item_bg).setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.img_home_three_other_variety_gridview_item_bg2).setVisibility(View.GONE);
            itemView.findViewById(R.id.img_home_three_other_variety_gridview_item_play).setVisibility(View.VISIBLE);
        }
        itemView.findViewById(R.id.rl_home_three_other_variety_gridview_item_show).setVisibility(View.VISIBLE);
    }

    /**
     * item按钮点击监听
     *
     * @param position
     */
    private void onItemClick(int position, ScreeningDataBean.DataBean.ListcontentBean.ContentBean data) {
        this.postion = position;
        //当前点击的item和记录的item不一致，停止播放器并释放资源
        if (position == lastPostion) {
            //获取Recyclerview的item中的播放器布局FrameLayout
            View view = recyclerView.findViewHolderForAdapterPosition(postion).itemView;
            FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.layout_video);
            frameLayout.removeAllViews();
            frameLayout.addView(videoItemView);

            videoItemView.seekTo(0);
            videoItemView.reStart();
        }  else {
            videoItemView.stop();
            videoItemView.release();

            //小窗口
            if (layoutSmall.getVisibility() == View.VISIBLE) {
                layoutSmall.setVisibility(View.GONE);
                layoutSmallVideo.removeAllViews();
                videoItemView.setShowContoller(true);
            }

            //设置播放器背景图片控件显示
            if (lastPostion != -1 && position != lastPostion) {
                //获取Recyclerview的item中的播放器布局FrameLayout
                View itemView = recyclerView.findViewHolderForAdapterPosition(lastPostion).itemView;
                if (itemView != null) {

                    FrameLayout frameLayout = (FrameLayout) itemView.findViewById(R.id.layout_video);
                    frameLayout.removeAllViews();
                    getShowPlayView(itemView, false);
                }
            }

            //获取Recyclerview的item中的播放器布局FrameLayout
            View view = recyclerView.findViewHolderForAdapterPosition(postion).itemView;
            FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.layout_video);
            frameLayout.removeAllViews();
            frameLayout.addView(videoItemView);

            videoItemView.setVideoName(data.getDisplayName());
            lastPostion = position;
            contentBean = data;
            getPlayUrl(data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //小窗口布局
            case R.id.rl_home_small_view:
                layoutSmall.setVisibility(View.GONE);
                //切换成全屏
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;

            //小窗口关闭按钮
            case R.id.img_home_small_close:
                if (videoItemView.isPlaying()) {
                    videoItemView.stop();
                    postion = -1;
                    lastPostion = -1;
                    layoutSmallVideo.removeAllViews();
                    layoutSmall.setVisibility(View.GONE);
                }
                break;
        }
    }

    /**
     * 避免一些特殊情况重启Activity
     *
     * @param newConfig
     */
    public void getOnConfigurationChanged(Configuration newConfig) {
        if (videoItemView != null) {
            videoItemView.onChanged(newConfig);
            //如果当前是竖屏
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                //全屏布局隐藏、Recyclerview布局显示
                layoutFullVideo.setVisibility(View.GONE);
                layoutAll.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                layoutFullVideo.removeAllViews();

                //当前item索引 小于等于 最后一个可见item的索引 并且 大于等于 第一个可见item的索引
                if (postion <= mLayoutManager.findLastVisibleItemPosition()
                        && postion >= mLayoutManager.findFirstVisibleItemPosition()) {
                    View view = recyclerView.findViewHolderForAdapterPosition(postion).itemView;
                    FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.layout_video);
                    frameLayout.removeAllViews();
                    frameLayout.addView(videoItemView);

//                    if (videoItemView != null) {
//                        if (videoItemView.isStop()) {
//                            getShowPlayView(view, true);
//                        }
//                    }
//                    videoItemView.setShowContoller(true);
                } else {
                    //小窗口显示
                    layoutSmallVideo.removeAllViews();
                    layoutSmallVideo.addView(videoItemView);
                    videoItemView.setShowContoller(false);
                    layoutSmallVideo.setVisibility(View.VISIBLE);
                }

                //5秒后隐藏播放控制布局
//                videoItemView.setContorllerVisiable();
            } else {
                //当前横屏

                ViewGroup viewGroup = (ViewGroup) videoItemView.getParent();
                if (viewGroup == null) {
                    return;
                }
                viewGroup.removeAllViews();
                layoutFullVideo.addView(videoItemView);
                layoutSmall.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                layoutFullVideo.setVisibility(View.VISIBLE);
                layoutAll.setVisibility(View.GONE);
            }
        } else {
            adapter.notifyDataSetChanged();
            layoutAll.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            layoutFullVideo.setVisibility(View.GONE);
        }
    }

    public void getOnAttachedToWindow() {
        if (videoItemView == null) {
            videoItemView = new VideoPlayView(getActivity());
        }
    }

    public void getOnDetachedFromWindow() {
        if (recyclerView == null) {
            return;
        }

        //移除小窗口
        if (layoutSmall.getVisibility() == View.VISIBLE) {
            layoutSmall.setVisibility(View.GONE);
            layoutSmallVideo.removeAllViews();
        }

        //释放播放器
        if (postion != -1) {
            ViewGroup view = (ViewGroup) videoItemView.getParent();
            if (view != null) {
                view.removeAllViews();
            }
        }
        videoItemView.stop();
        videoItemView.release();
        videoItemView.onDestroy();
        videoItemView = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoItemView != null) {
            videoItemView.getStopCatch();
            videoItemView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (videoItemView != null) {
                if (recyclerView != null && postion != -1) {
                    View view = recyclerView.findViewHolderForAdapterPosition(postion).itemView;
                    if (view != null && (view.findViewById(R.id.img_home_three_other_variety_gridview_item_replay_play).getVisibility() == View.VISIBLE)) {
                        return;
                    }
                }
                videoItemView.getLogCatch();
                videoItemView.reStart();
            }
        } catch (Exception e) {

        }
    }
}
