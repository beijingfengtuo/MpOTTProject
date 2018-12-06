package cn.cibnmp.ott.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.adapter.HomeOneHoldersAdapter;
import cn.cibnmp.ott.adapter.HomeOneViewType;
import cn.cibnmp.ott.bean.LayoutItem;
import cn.cibnmp.ott.bean.NavigationBlock;
import cn.cibnmp.ott.bean.NavigationInfoBean;
import cn.cibnmp.ott.bean.NavigationInfoBlockBean;
import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.bean.NavigationInfoResultBean;
import cn.cibnmp.ott.config.CacheConfig;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.SimpleHttpResponseListener;
import cn.cibnmp.ott.library.pullable.PullToRefreshLayout;
import cn.cibnmp.ott.ui.base.Action;
import cn.cibnmp.ott.ui.categoryList.HomeOnItemClickListener;
import cn.cibnmp.ott.ui.categoryList.LazyFragment;
import cn.cibnmp.ott.utils.DisplayUtils;
import cn.cibnmp.ott.utils.HomeJumpDetailUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.widgets.PmRecyclerView;
import cn.cibnmp.ott.widgets.pmrecyclerview.TwoWayLayoutManager;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.HomeSpannableGridLayoutManager;

/**
 * Created by yangwenwu on 18/1/2.
 * 首页下面viewpager数据Fragment
 */

public class HomeOneItemFragment extends LazyFragment {

    private View mView;
    private int mBlockCount;
    private String mChanneld;
    private ImageView octv_ivpi;
    private String mChanneepgld;
    private String mLayout = null;
    private NavigationBlock mBlock;
    private JSONObject mObject = null;
    private RotateAnimation animation;
    private JSONArray mJsonArray = null;
    private PmRecyclerView mRecyclerView;
    private JSONObject mJsonObject = null;
    private LayoutItem mLayoutItem = null;
    private final int HOME_OK = 54055;
    private final int HOME_UPDATA = 55555;
    private final int SET_DATE_MSG_Y = 54076;
    private final int SET_DATE_MSG_N = 54074;
    private String TAG = "HomeOneItemFragment";
    private HomeOneHoldersAdapter mHomeAapter;
    private NavigationInfoItemBean mInfoItemBean;
    private NavigationInfoBean mResultBean = null;
    private NavigationInfoBlockBean mInfoBlockBean;
    private PullToRefreshLayout mPullToRefreshLayout;
    private List<LayoutItem> mLaytItemList = new ArrayList<>();
    private RelativeLayout octv_looding, homeone_error_nonetwork;
    private List<NavigationInfoItemBean> mInfoItemBeanList = new ArrayList<>();

    public static HomeOneItemFragment newInstance(Bundle bundle) {
        HomeOneItemFragment fragment = new HomeOneItemFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setViewPager() {
    }

    @Override
    public void setPagerDate() {
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home_oneitem, container, false);
        mPullToRefreshLayout = (PullToRefreshLayout) mView.findViewById(R.id.homeone_fragment_ptfresh);
        mRecyclerView = (PmRecyclerView) mView.findViewById(R.id.homeone_fragment_recyclerview);
        octv_looding = (RelativeLayout) mView.findViewById(R.id.homeone_fragment_loading);
        octv_ivpi = (ImageView) mView.findViewById(R.id.octv_ivpi);
        animation = (RotateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.rotating);
        homeone_error_nonetwork = (RelativeLayout) mView.findViewById(R.id.homeone_error_nonetwork);
        LinearInterpolator lir = new LinearInterpolator();
        animation.setInterpolator(lir);
        pullToRefresh();
        homeone_error_nonetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        return mView;
    }

    private void setHomeAdapter() {
        if (mHomeAapter != null) {
            mHomeAapter.setData(mLaytItemList, mInfoItemBeanList);
            mHomeAapter.notifyDataSetChanged();
        } else {
            HomeSpannableGridLayoutManager gridLayoutManager = new
                    HomeSpannableGridLayoutManager(TwoWayLayoutManager.Orientation.VERTICAL, 660, 660);
            mRecyclerView.setLayoutManager(gridLayoutManager);
//            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mRecyclerView.getLayoutParams();
//            lp.rightMargin = -DisplayUtils.getValue(6);
//            lp.leftMargin = -DisplayUtils.getValue(6);
//            mRecyclerView.setLayoutParams(lp);
            mRecyclerView.setSpacingWithMargins(DisplayUtils.getValue(20), DisplayUtils.getValue(20));
            mHomeAapter = new HomeOneHoldersAdapter(context, new HomeOnItemClickListener() {
                @Override
                public void onItemClickListener(Bundle bundle) {
                    if (Action.getActionName(Action.OPEN_NORMAL_DETAIL_PAGE).equals(bundle.get(Constant.BUNDLE_ACTION))
                            || Action.getActionName(Action.OPEN_PERSON_DETAIL_PAGE).equals(bundle.get(Constant.BUNDLE_ACTION))
                            || Action.getActionName(Action.OPEN_DETAIL).equals(bundle.get(Constant.BUNDLE_ACTION))
                            || Action.getActionName(Action.OPEN_LIVE_DETAIL_PAGE).equals(bundle.get(Constant.BUNDLE_ACTION))) {
                        HomeJumpDetailUtils.actionTo((NavigationInfoItemBean) bundle.get(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN), getActivity());
                    } else {
                        startActivity(bundle.getString(Constant.BUNDLE_ACTION), bundle);
                    }
                }
            });
            mHomeAapter.setWidthMargins(DisplayUtils.getValue(20));
            mHomeAapter.setData(mLaytItemList, mInfoItemBeanList);
            mRecyclerView.setAdapter(mHomeAapter);
        }
    }

    @Override
    public void onEventMainThread(String event) {
        super.onEventMainThread(event);
        if (event.equals(Constant.PULL_STOP)) {
            mPullToRefreshLayout.stopPull();
        }
    }

    private void pullToRefresh() {
        mPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        //下拉刷新
                        handler.hasMessages(HOME_UPDATA);
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 1000);
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        //上拉刷新
                        handler.hasMessages(HOME_UPDATA);
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 1000);
            }
        });
    }

    //获取点击或者滑动首页传的频道id
    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mChanneld = bundle.getString(Constant.CHANNEL_ID);
        mChanneepgld = bundle.getString(Constant.CHANNEL_EPGID);
        getChannelData();
    }

    //加载首页数据
    public void getChannelData() {
        startLooding();
        HttpRequest.getInstance().excute("getHomeNavContent", new Object[]{App.epgUrl,
                mChanneepgld, mChanneld, CacheConfig.cache_no_cache, new SimpleHttpResponseListener<NavigationInfoResultBean>() {

            @Override
            public void onSuccess(NavigationInfoResultBean navigationInfoResultBean) {
                Lg.i(TAG, "getHomeNavContent onSuccess--> " + navigationInfoResultBean);
                handler.sendEmptyMessage(HOME_OK);
                initdatalist(navigationInfoResultBean);
            }

            @Override
            public void onError(String error) {
                Lg.i(TAG, "getHomeNavContent error--> " + error);
                handler.sendEmptyMessage(SET_DATE_MSG_N);
            }
        }});
    }

    private void initdatalist(NavigationInfoResultBean navigationInfoResultBean) {

        if (navigationInfoResultBean != null) {
            mResultBean = navigationInfoResultBean.getData();
        }
        Lg.i(TAG, "getHomeNavigationList --> " + mResultBean.toString());
        if (mResultBean != null && mResultBean.getBlocks() != null && mResultBean.getBlocks().size() > 0) {
            handler.sendEmptyMessage(SET_DATE_MSG_Y);
        } else {
            handler.sendEmptyMessage(SET_DATE_MSG_N);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HOME_UPDATA:
                    initData();
                    break;
                case SET_DATE_MSG_Y:
                    stopLooding();
                    mergeData(mResultBean);
                    setHomeAdapter();
                    break;
                case SET_DATE_MSG_N:
                    stopLooding();
                    homeone_error_nonetwork.setVisibility(View.VISIBLE);
                    mPullToRefreshLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                    octv_looding.setVisibility(View.GONE);
                    break;
                case HOME_OK:
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mPullToRefreshLayout.setVisibility(View.VISIBLE);
                    octv_looding.setVisibility(View.VISIBLE);
                    homeone_error_nonetwork.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };

    private boolean mergeData(NavigationInfoBean resultBean) {

        if (resultBean.getBlocks() == null || resultBean.getBlocks().isEmpty()
                || resultBean.getContent() == null || resultBean.getContent().isEmpty()) {
            Lg.i(TAG, getString(R.string.homeactivity_data_error));
            return false;
        }

        mBlockCount = Math.min(resultBean.getBlocks().size(), resultBean.getContent().size());

        if (mLaytItemList == null || mInfoItemBeanList == null)
            return false;

        mLaytItemList.clear();
        mInfoItemBeanList.clear();

        //四个按钮集合以及布局
        List<NavigationInfoItemBean> tagInfoItemBeanList = new ArrayList<NavigationInfoItemBean>();
        LayoutItem tagLayoutItem = new LayoutItem();
        NavigationInfoItemBean tagInfoItemBean = null;

        for (int j = 0; j < mBlockCount; j++) {
            mBlock = resultBean.getBlocks().get(j);
            mInfoBlockBean = resultBean.getContent().get(j);
            if (mBlock.getLayout() != null) {
                mLayout = mBlock.getLayout().getLayoutJson();
                if (TextUtils.isEmpty(mLayout))
                    continue;
                try {
                    try {
                        mJsonObject = new JSONObject(mLayout);
                        mJsonArray = mJsonObject.getJSONArray("layout");
                    } catch (JSONException e) {
                        Lg.i(TAG, "mergeLayout : navId = " + " , blockId = " + mBlock.getBlockId()
                                + getString(R.string.homeactivity_layout_error) + mLayout +
                                getString(R.string.homeactivity_showlayout_error));
                        continue;
                    }

                    //设置block的标题的布局和内容，只要当nameType都是0的时候，才显示标题内容
                    if (mBlock.getNameType() == NavigationInfoBlockBean.SHOWNAV
                            && mInfoBlockBean.getNameType() == NavigationInfoBlockBean.SHOWNAV) {
                        if (j == 0) {
                            getLayout(720, 0, HomeOneViewType.title_viewType, "");
                        } else {
                            getLayout(720, 80, HomeOneViewType.title_viewType, mInfoBlockBean.getName());
                        }
                    } else {
                        getLayout(720, 0, HomeOneViewType.title_viewType, "");
                    }

                    for (int i = 0; i < mJsonArray.length(); i++) {
                        if (mInfoBlockBean.getIndexContents() != null && i
                                < mInfoBlockBean.getIndexContents().size()) {
                            mObject = mJsonArray.getJSONObject(i);
                            mLayoutItem = new LayoutItem();
                            //判断是四个按钮类型创建布局
                            if (mInfoBlockBean.getIndexContents().get(i).
                                    getViewtype() == HomeOneViewType.bottom_viewType) {
                                NavigationInfoItemBean bean = mInfoBlockBean.getIndexContents().get(i);
                                tagInfoItemBeanList.add(bean);
                                if (tagInfoItemBean == null) {
                                    tagInfoItemBean = new NavigationInfoItemBean();
                                    tagInfoItemBean.setViewtype(HomeOneViewType.bottom_viewType);
                                    tagInfoItemBean.setTagContents(tagInfoItemBeanList);

                                    if (mObject.has("c")) {
                                        tagLayoutItem.setC(720);
                                    } else if (mObject.has("C")) {
                                        tagLayoutItem.setC(720);
                                    }

                                    if (mObject.has("r")) {
                                        tagLayoutItem.setR(100);
                                    } else if (mObject.has("R")) {
                                        tagLayoutItem.setR(100);
                                    }
                                } else {
                                    tagInfoItemBean.setTagContents(tagInfoItemBeanList);
                                }

                                if (mJsonArray.length() > mInfoBlockBean.getIndexContents().size()) {
                                    if (i == mInfoBlockBean.getIndexContents().size() - 1) {
                                        mInfoItemBeanList.add(tagInfoItemBean);
                                        mLaytItemList.add(tagLayoutItem);
                                    }
                                } else if (mJsonArray.length() < mInfoBlockBean.getIndexContents().size()) {
                                    if (i == mJsonArray.length() - 1) {
                                        mInfoItemBeanList.add(tagInfoItemBean);
                                        mLaytItemList.add(tagLayoutItem);
                                    }
                                } else {
                                    if (i == mInfoBlockBean.getIndexContents().size() - 1) {
                                        mInfoItemBeanList.add(tagInfoItemBean);
                                        mLaytItemList.add(tagLayoutItem);
                                    }
                                }
                            } else {
                                getServicerWidthHeight(i, mInfoBlockBean.getIndexContents().get(i).getViewtype());
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Lg.i(TAG, "mergeLayout : navId = " + " , blockId = " + mBlock.getBlockId()
                            + " , parse layout failed , layout json is : " + mLayout);
                    return false;
                }

            } else {
                Lg.i(TAG, "mergeLayout : navId = " + " , blockId = " + mBlock.getBlockId()
                        + " , layout is invalid , layout is null or layoutJson is null !");
                return false;
            }
        }
        return true;
    }

    /**
     * 设置item的尺寸
     *
     * @param c
     * @param r
     */
    private void getLayout(int c, int r, int viewType, String name) {
        LayoutItem mLayoutItem = new LayoutItem();
        mLayoutItem.setC(c);
        mLayoutItem.setR(r);
        mLaytItemList.add(mLayoutItem);

        NavigationInfoItemBean mItemBean = new NavigationInfoItemBean();
        mItemBean.setViewtype(viewType);
        mItemBean.setName(name);
        mInfoItemBeanList.add(mItemBean);
    }

    /**
     * 设置布局宽高
     */
    public void getServicerWidthHeight(int position, int viewType) {
        try {
            if (viewType == HomeOneViewType.title_complete_viewType) {
                if (mObject.has("c")) {
                    mLayoutItem.setC(720);
                } else if (mObject.has("C")) {
                    mLayoutItem.setC(720);
                }

                if (mObject.has("r")) {
                    mLayoutItem.setR(80);
                } else if (mObject.has("R")) {
                    mLayoutItem.setR(80);
                }
            } else {
                if (mObject.has("c")) {
                    mLayoutItem.setC(mObject.getDouble("c"));
                } else if (mObject.has("C")) {
                    mLayoutItem.setC(mObject.getDouble("C"));
                }

                if (mObject.has("r")) {
                    mLayoutItem.setR(mObject.getDouble("r"));
                } else if (mObject.has("R")) {
                    mLayoutItem.setR(mObject.getDouble("R"));
                }
            }

            mLaytItemList.add(mLayoutItem);
            mInfoItemBean = mInfoBlockBean.getIndexContents().get(position);
            mInfoItemBeanList.add(mInfoItemBean);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始加载动画
     */
    private void startLooding() {
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
    public void onPause() {
        super.onPause();
        stopViewpager();
    }

    @Override
    public void onResume() {
        super.onResume();
        startViewpager();
    }

    public void stopViewpager() {
        if (mHomeAapter != null) {
            mHomeAapter.stopViewpage();
        }
    }

    public void startViewpager() {
        if (mHomeAapter != null) {
            mHomeAapter.startViewpage();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mHomeAapter != null) {
            mHomeAapter = null;
        }
    }
}
