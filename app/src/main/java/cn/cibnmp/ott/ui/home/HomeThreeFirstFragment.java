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

import com.alibaba.fastjson.JSON;
import com.ta.utdid2.android.utils.StringUtils;

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
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.library.pullable.PullToRefreshLayout;
import cn.cibnmp.ott.ui.base.Action;
import cn.cibnmp.ott.ui.categoryList.HomeOnItemClickListener;
import cn.cibnmp.ott.ui.categoryList.LazyFragment;
import cn.cibnmp.ott.utils.DisplayUtils;
import cn.cibnmp.ott.utils.HomeJumpDetailUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.NetWorkUtils;
import cn.cibnmp.ott.widgets.PmRecyclerView;
import cn.cibnmp.ott.widgets.pmrecyclerview.TwoWayLayoutManager;
import cn.cibnmp.ott.adapter.HomeOneViewType;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.HomeSpannableGridLayoutManager;

/**
 * 玩票页面-第一个选项卡页面
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2017/12/29
 */
public class HomeThreeFirstFragment extends LazyFragment {
    private static String TAG = HomeThreeFirstFragment.class.getName();
    private View root;

    //进度条
    private RelativeLayout octv_looding;
    private ImageView octv_ivpi;
    private RotateAnimation animation;
    private boolean isShow = true;

    //上拉下拉控件
    private PullToRefreshLayout home_three_head;
    //RecyclerView列表控件
    private PmRecyclerView threeRecyclerView;
    //HomeThreeFirstRecyclerViewAdapter数据处理
    private HomeOneHoldersAdapter mHomeAapter;
    private RelativeLayout rlNullData;
    private NavigationInfoBean mResultBean = null; //列表所有数据（item尺寸列表、item列表数据）
    private List<LayoutItem> mLaytItemList = new ArrayList<LayoutItem>(); //item模块布局尺寸列表数据
    private List<NavigationInfoItemBean> mInfoItemBeanList = new ArrayList<NavigationInfoItemBean>(); //item模块列表数据
    private int mBlockCount;
    private NavigationBlock mBlock; //item模块尺寸数据
    private NavigationInfoBlockBean mInfoBlockBean; //item模块数据
    private String mLayout = null; //item模块尺寸具体数据字段
    //item模块尺寸具体数据字段（Json格式）
    private JSONArray mJsonArray = null;
    private JSONObject mJsonObject = null;
    private NavigationInfoItemBean mInfoItemBean = null;
    private JSONObject mObject = null;
    private LayoutItem mLayoutItem = null;

    /**
     * 加载View
     **/
    private final int LODING_DATE = 666;
    private final int LODING_ERROR_500 = 500;
    private final int LODING_DATE_UP = 6000;

    public static HomeThreeFirstFragment newInstance(Bundle bundle) {
        HomeThreeFirstFragment fragment = new HomeThreeFirstFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.home_three_first_fragment, container, false);

        //获取参数
        Bundle bundle = getArguments();
        mResultBean = (NavigationInfoBean) bundle.getSerializable(Constant.HOME_THREE_ITEM_RESULTBEAN);

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

        home_three_head = (PullToRefreshLayout) root.findViewById(R.id.home_three_first_head);
        //RecyclerView控件
        threeRecyclerView = (PmRecyclerView) root.findViewById(R.id.rv_home_three_first_recyclerview);
        rlNullData = (RelativeLayout) root.findViewById(R.id.rl_null_data);
    }

    @Override
    protected void initData() {
        // 下拉和上拉回调监听
        setRefresh();
        handleLoadRecommendListSuccessResponse();
    }

    /**
     * 绑定recyclerView控件
     */
    private void setThreeRecyclerViewAdapter() {

        HomeSpannableGridLayoutManager gridLayoutManager = new
                HomeSpannableGridLayoutManager(TwoWayLayoutManager.Orientation.VERTICAL, 660, 660);
        threeRecyclerView.setLayoutManager(gridLayoutManager);
        threeRecyclerView.setSpacingWithMargins(DisplayUtils.getValue(20), DisplayUtils.getValue(20));
        mHomeAapter = new HomeOneHoldersAdapter(context, new HomeOnItemClickListener() {
            @Override
            public void onItemClickListener(Bundle bundle) {
                if (bundle.get(Constant.BUNDLE_ACTION).equals(Action.getActionName(Action.OPEN_NORMAL_DETAIL_PAGE))
                        || bundle.get(Constant.BUNDLE_ACTION).equals(Action.getActionName(Action.OPEN_PERSON_DETAIL_PAGE))
                        || bundle.get(Constant.BUNDLE_ACTION).equals(Action.getActionName(Action.OPEN_DETAIL))) {
                    HomeJumpDetailUtils.actionTo((NavigationInfoItemBean) bundle.get(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN), getActivity());
                } else {
                    startActivity(bundle.getString(Constant.BUNDLE_ACTION), bundle);
                }
            }
        });
        mHomeAapter.setWidthMargins(DisplayUtils.getValue(20));
        mHomeAapter.setData(mLaytItemList, mInfoItemBeanList);
        threeRecyclerView.setAdapter(mHomeAapter);
    }

    /**
     * 上拉下拉回调监听
     */
    private void setRefresh() {
        home_three_head.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            /**
             * 下拉刷新
             *
             * @param pullToRefreshLayout
             */
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                handleLoadRecommendListSuccessResponse();
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
                // 加载操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
//                        ToastUtils.show(context, "施主,没有内容了,请回头吧");
                        if (pullToRefreshLayout != null) {
                            // 加载完毕
                            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        }
                    }
                }.sendEmptyMessageDelayed(0, 500);
            }
        });
    }

    @Override
    public void onEventMainThread(String event) {
        super.onEventMainThread(event);
        if (event.equals(Constant.PULL_STOP)){
            home_three_head.stopPull();
        }
    }

    /**
     * 获取数据成功
     */
    private void handleLoadRecommendListSuccessResponse() {

        if (!NetWorkUtils.isNetworkAvailable(getActivity())) {
            //当前网络不可用
            Lg.i(TAG, "当前网络异常，请重试连接!");
            mHandler.sendEmptyMessage(LODING_ERROR_500);
            return;
        }

        startLooding();

        //获取列表所有数据
        if (mResultBean != null && mResultBean.getBlocks() != null && mResultBean.getBlocks().size() > 0) {
            mHandler.sendEmptyMessage(LODING_DATE);
        } else {
            handleLoadRecommendListErrorResponse();
        }
    }

    /**
     * 获取数据失败
     *
     */
    private void handleLoadRecommendListErrorResponse() {
        mHandler.sendEmptyMessage(LODING_ERROR_500);
    }

    /**
     * 设置item大小
     *
     * @param resultBean 这个列表的所有数据
     * @return
     */
    private boolean setLayoutData(NavigationInfoBean resultBean) {

        if (resultBean.getBlocks() == null || resultBean.getBlocks().isEmpty()
                || resultBean.getContent() == null || resultBean.getContent().isEmpty()) {
            Lg.e(TAG, "数据异常");
            return false;
        }

        //TODO zxy 说明？
        mBlockCount = Math.min(resultBean.getBlocks().size(), resultBean.getContent().size());
        //item布局尺寸、item列表数据
        if (mLaytItemList != null || mInfoItemBeanList != null) {
            mLaytItemList.clear();
            mInfoItemBeanList.clear();
        }

        /*临时数据，将筛选项列表组成一个临时集合*/
        List<NavigationInfoItemBean> tagInfoItemBeanList = new ArrayList<NavigationInfoItemBean>();
        LayoutItem tagLayoutItem = new LayoutItem();
        NavigationInfoItemBean tagInfoItemBean = null;

        List<NavigationInfoItemBean> tagInfoItemBeanList1 = new ArrayList<NavigationInfoItemBean>();
        LayoutItem tagLayoutItem1 = new LayoutItem();
        NavigationInfoItemBean tagInfoItemBean1 = null;

        //遍历所有列表数据
        for (int j = 0; j < mBlockCount; j++) {
            //item模块尺寸数据
            mBlock = resultBean.getBlocks().get(j);
            //item模块数据
            mInfoBlockBean = resultBean.getContent().get(j);

            //判断布局尺寸数据
            if (mBlock.getLayout() != null) {
                if (StringUtils.isEmpty(mBlock.getLayout().getLayoutJson())) {
                    Lg.e(TAG, "布局尺寸数据异常");
                    return false;
                }
                mLayout = mBlock.getLayout().getLayoutJson();
                if (TextUtils.isEmpty(mLayout)) {
                    continue;
                }

                try {
                    try {
                        //将item布局尺寸数据转成json数据格式
                        mJsonObject = new JSONObject(mLayout);
                        mJsonArray = mJsonObject.getJSONArray("layout");
                    } catch (JSONException e) {
                        Lg.e(TAG, "mergeLayout : navId = " + " , blockId = " + mBlock.getBlockId()
                                + " , 布局不合法，布局为空或者不是jsonArray , layout json is : " + mLayout + " , 不展示此布局!");
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

                        if (mInfoBlockBean.getIndexContents() != null && i < mInfoBlockBean.getIndexContents().size()) {
                            mObject = mJsonArray.getJSONObject(i);
                            mLayoutItem = new LayoutItem();
                            if (mInfoBlockBean.getIndexContents().get(i).getViewtype() == HomeOneViewType.bottom_viewType) {
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

                                if (mInfoBlockBean.getIndexContents().get(i).getViewtype() == HomeOneViewType.title_complete_viewType) {
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
                                mInfoItemBean = mInfoBlockBean.getIndexContents().get(i);
                                mInfoItemBeanList.add(mInfoItemBean);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Lg.e(TAG, "mergeLayout : navId = " + " , blockId = " + mBlock.getBlockId()
                            + " , parse layout failed , layout json is : " + mLayout);
                    return false;
                }

            } else {
                //布局尺寸数据异常
                Lg.e(TAG, "mergeLayout : navId = " + " , blockId = " + mBlock.getBlockId()
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

    @Override
    public void setViewPager() {

    }

    @Override
    public void setPagerDate() {

    }


    /**
     * Handler处理
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LODING_DATE:
                    Lg.i(TAG, "页面刷新 ");
                    threeRecyclerView.setVisibility(View.VISIBLE);
                    rlNullData.setVisibility(View.GONE);
                    if (!setLayoutData(mResultBean)) {
                        mHandler.sendEmptyMessageDelayed(LODING_ERROR_500, 500);
                        return;
                    }
                    ;
                    if (mHomeAapter != null) {
                        mHomeAapter.setData(mLaytItemList, mInfoItemBeanList);
                        mHomeAapter.notifyDataSetChanged();
                    } else {
                        setThreeRecyclerViewAdapter();
                    }
                    mHandler.sendEmptyMessageDelayed(LODING_DATE_UP, 500);
                    break;

                case LODING_DATE_UP:
                    stopLooding();
                    // 加载完毕
                    if (home_three_head != null) {
                        home_three_head.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                    break;

                case LODING_ERROR_500:
                    stopLooding();
                    rlNullData.setVisibility(View.VISIBLE);
                    threeRecyclerView.setVisibility(View.GONE);
                    mHandler.sendEmptyMessage(LODING_DATE_UP);
                    Lg.i(TAG, getResources().getString(R.string.error_toast));
//                    Toast.makeText(context, getResources().getString(R.string.error_toast), Toast.LENGTH_LONG).show();
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
}
