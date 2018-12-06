package cn.cibnmp.ott.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.LiveStatusBean;
import cn.cibnmp.ott.bean.LiveStatusRequestEntity;
import cn.cibnmp.ott.bean.NavigationInfoBlockBean;
import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.bean.ScreeningDataBean;
import cn.cibnmp.ott.config.CacheConfig;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.SimpleHttpResponseListener;
import cn.cibnmp.ott.library.pullable.PullToRefreshLayout;
import cn.cibnmp.ott.ui.base.BaseFragment;
import cn.cibnmp.ott.ui.categoryList.HomeOnItemClickListener;
import cn.cibnmp.ott.utils.HomeJumpDetailUtils;
import cn.cibnmp.ott.utils.Lg;
import de.greenrobot.event.EventBus;

/**
 * 直播-录像页面
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2017/9/25
 */
public class HomeTwoLiveVideoFragment extends BaseFragment {
    private static String TAG = HomeTwoLiveVideoFragment.class.getName();
    private View root;

    //上拉下拉控件
    private PullToRefreshLayout mToRefreshLayout;
    //GridView列表控件
    private GridView gridView;
    private HomeTwoLiveSceneGridAdapter threeAdapter;
    private RelativeLayout rlNullData;
    //现场内容列表
    private List<ScreeningDataBean.DataBean.ListcontentBean.ContentBean> contentBeanList;

    //参数：现场、录像模块数据
    private NavigationInfoBlockBean infoBlockBean;
    //参数：现场、录像栏目数据集合（现场索引0、录像索引1）
    private List<NavigationInfoItemBean> contentsList;
    //参数：epgID
    private String epgId;
    //获取节目列表的url
    private String getListProgramDataUrl;
    //接口参数:(录像导航下的数据)
    private String channelID = "";

    //加载View
    private final int LODING_LIST_DATE = 665;
    private final int LODING_DATE = 666;
    private final int LODING_ERROR_500 = 500;
    //网络不可用
    private final int LODING_NETWORK_NO = 700;

    private int PAGESIZE = 0;
    private int PAGENUM = 20;
    private boolean isLoadMoreFinish = false; //是否加载完成

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.home_two_live_item_video_fragment, container, false);
        Bundle bundle = getArguments();
        setUI();
        return root;
    }

    /**
     * 初始化View
     */
    private void setUI() {
        mToRefreshLayout = (PullToRefreshLayout) root.findViewById(R.id.home_two_live_item_video_refresh);
        gridView = (GridView) root.findViewById(R.id.home_two_live_item_video_grid);
        gridView.setFocusable(false);
        mToRefreshLayout.stopPull();
        rlNullData = (RelativeLayout) root.findViewById(R.id.rl_null_data);

        // 下拉和上拉回调监听
        setRefresh();
    }

    /**
     * 从上个页面传进来的现场数据
     *
     * @param bundle
     */
    @Override
    public void setData(Bundle bundle) {
        super.setData(bundle);
        epgId = (String) bundle.get(Constant.BUNDLE_EPGID);
        infoBlockBean = (NavigationInfoBlockBean) bundle.get(Constant.HOME_TWO_LIVE);
        if (infoBlockBean != null) {
            contentsList = infoBlockBean.getIndexContents();
            if (contentsList != null && contentsList.size() > 0) {
                channelID = contentsList.get(1).getContentId();
                getLiveVideoData(channelID);
            } else {
                handleErrorResponse();
            }
        } else {
            handleErrorResponse();
        }
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
                EventBus.getDefault().post(Constant.HOME_TWO_LIVE_VIDEO);
                getRefresh();
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (pullToRefreshLayout != null) {
                            //加载完毕
                            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
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
            getLiveVideoData(channelID);
        }
    }

    /**
     * Event消息
     * @param event
     */
    @Override
    public void onEventMainThread(String event) {
        super.onEventMainThread(event);
        //当前网络不可用
        if (event.equals(Constant.NETWORK_NO_HOME_TWO_LIVE)) {
            mHandler.sendEmptyMessage(LODING_NETWORK_NO);
        }
    }

    /**
     * 获取现场数据
     *
     * @param channelID
     */
    private void getLiveVideoData(String channelID) {
        getListProgramDataUrl = App.epgUrl + "/listContent?epgId=" + epgId + "&columnId=" + channelID+ "&pageSize=" + PAGESIZE + "&pageNum=" + PAGENUM;
        HttpRequest.getInstance().excute("getListProgramData", new Object[]{getListProgramDataUrl,
                CacheConfig.cache_no_cache, new SimpleHttpResponseListener<ScreeningDataBean>() {

            @Override
            public void onSuccess(ScreeningDataBean screeningDataBean) {
                Lg.i(TAG, "转播页面 - 录像下的数据：" + screeningDataBean.toString());
                handleLoadRecommendListSuccessResponse(screeningDataBean);
            }

            @Override
            public void onError(String error) {
                Lg.i(TAG, "转播页面 - 录像下的数据：getHomeNavContent : error--> " + error);

            }
        }});
    }

    /**
     * 获取数据成功
     *
     * @param screeningDataBean
     */
    private void handleLoadRecommendListSuccessResponse(ScreeningDataBean screeningDataBean) {
        try {
            if (screeningDataBean == null || screeningDataBean.getData() == null || screeningDataBean.getData().getListcontent() == null) {
                handleErrorResponse();
                return;
            }

            List<ScreeningDataBean.DataBean.ListcontentBean.ContentBean> list = screeningDataBean.getData().getListcontent().getContent();

            if (list != null && list.size() > 0) {
                if (contentBeanList != null) {
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

            mHandler.sendEmptyMessage(LODING_LIST_DATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取录像节目直播状态数据
     */
    private void getLiveVideoStatusData() {

        LiveStatusRequestEntity liveStatusRequest = new LiveStatusRequestEntity();
        List<LiveStatusRequestEntity.LiveParamBean> liveParamBeanList = new ArrayList<LiveStatusRequestEntity.LiveParamBean>();
        for (ScreeningDataBean.DataBean.ListcontentBean.ContentBean contentBean : contentBeanList) {
            liveParamBeanList.add(new LiveStatusRequestEntity.LiveParamBean(contentBean.getContentId(), contentBean.getSid()));
        }
        liveStatusRequest.setEpgId(epgId);
        liveStatusRequest.setLiveParam(liveParamBeanList);
        String requestJson = JSON.toJSONString(liveStatusRequest);

        //{"epgId":111,"liveParam":[{"contentId":"111","childId":"111"},{"contentId":"222","childId":"222"}]}
        final String url = App.epgUrl + "/liveListStatus";
        HttpRequest.getInstance().excute("getLiveStatus", new Object[]{url,
                CacheConfig.cache_no_cache, requestJson, getListProgramDataUrl, new SimpleHttpResponseListener<LiveStatusBean>() {

            @Override
            public void onSuccess(LiveStatusBean data) {
                Lg.i(TAG, "转播页面 - 录像下的节目状态数据：" + data.toString());
                handleLoadLiveStatusBeanSuccessResponse(data);
            }

            @Override
            public void onError(String error) {
                Lg.i(TAG, "转播页面 - 录像下的节目状态数据：getLiveStatus : error--> " + error);
                handleErrorResponse();
            }
        }});
    }

    /**
     * 获取数据成功
     *
     * @param data 节目直播状态数据
     */
    private void handleLoadLiveStatusBeanSuccessResponse(LiveStatusBean data) {

        if (data == null || data.getData() == null || data.getData().size() == 0) {
            if (contentBeanList == null || contentsList.size() == 0) {
                handleErrorResponse();
                return;
            } else {
                mHandler.sendEmptyMessage(LODING_DATE);
            }
        } else {
            if (contentBeanList == null || contentsList.size() == 0) {
                handleErrorResponse();
                return;
            }

            //节目列表数据和节目直播状态数据重组
            for (int i = 0; i < contentBeanList.size(); i++) {
                for (int k = 0; k < data.getData().size(); k++) {
                    //判断节目列表和节目状态列表中的ContentId、sid是否一致
                    if (contentBeanList.get(i).getContentId().equals(data.getData().get(k).getLiveId())
                            && contentBeanList.get(i).getSid().equals(data.getData().get(k).getSid())) {
                        contentBeanList.get(i).setStartDate(data.getData().get(k).getStartDate());
                        contentBeanList.get(i).setEndDate(data.getData().get(k).getEndDate());
                        contentBeanList.get(i).setStatus(data.getData().get(k).getStatus());
                    }
                }
            }
            mHandler.sendEmptyMessage(LODING_DATE);
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
        if(threeAdapter == null) {
            threeAdapter = new HomeTwoLiveSceneGridAdapter(context, contentBeanList, new HomeOnItemClickListener() {
                @Override
                public void onItemClickListener(Bundle bundle) {
                    HomeJumpDetailUtils.actionToDetailByScreening((ScreeningDataBean.DataBean.ListcontentBean.ContentBean) bundle.get(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN), getActivity());
                }
            });
            gridView.setAdapter(threeAdapter);
        } else {
            threeAdapter.setData(contentBeanList);
        }
    }

    /**
     * Handler处理
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LODING_LIST_DATE:
                    getLiveVideoStatusData();
                    break;

                case LODING_DATE:
                    gridView.setVisibility(View.VISIBLE);
                    rlNullData.setVisibility(View.GONE);
                    setGridData();
                    break;

                case LODING_ERROR_500:
                    rlNullData.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);
                    Lg.i(TAG, getResources().getString(R.string.error_toast));
//                    Toast.makeText(context, getResources().getString(R.string.error_toast), Toast.LENGTH_LONG).show();
                    break;

                case LODING_NETWORK_NO:
                    //当前网络不可用
                    mHandler.sendEmptyMessage(LODING_ERROR_500);
                    Lg.i(TAG, getResources().getString(R.string.network_no));
//                    Toast.makeText(context, getResources().getString(R.string.network_no), Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

}
