package cn.cibnmp.ott.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ta.utdid2.android.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.NavigationInfoBean;
import cn.cibnmp.ott.bean.NavigationInfoBlockBean;
import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.bean.NavigationInfoResultBean;
import cn.cibnmp.ott.bean.NavigationResultBean;
import cn.cibnmp.ott.config.CacheConfig;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.jni.SimpleHttpResponseListener;
import cn.cibnmp.ott.ui.base.Action;
import cn.cibnmp.ott.ui.base.BaseFragment;
import cn.cibnmp.ott.ui.categoryList.HomeOnItemClickListener;
import cn.cibnmp.ott.ui.categoryList.LazyFragment;
import cn.cibnmp.ott.utils.HomeJumpDetailUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.NetWorkUtils;
import cn.cibnmp.ott.utils.img.ImageFetcher;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.ViewPagerStop;
import de.greenrobot.event.EventBus;

/**
 * 转播页面
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2017/12/22
 */
public class HomeTwoFragment extends LazyFragment implements View.OnClickListener{
    private static String TAG = HomeTwoFragment.class.getName();

    //页面标题部分
    private View root;
    private View home_title;

    //进度条
    private RelativeLayout octv_looding;
    private ImageView octv_ivpi;
    private RotateAnimation animation;
    private boolean isShow = true;
    //默认刷新数据标识
    private int updateDataType = 0;

    /**导航列表数据*/
    //导航菜单列表数据
    private NavigationResultBean navigationResultBean;
    //导航内容列表数据
    private List<NavigationInfoItemBean> loopContentsList; //滚动图片列表集合
    private NavigationInfoBlockBean infoBlockBean;      //现场、录像模块数据

    //顶部轮播图的数据list
    private ArrayList<View> viewList = new ArrayList<View>();
    private HomeTwoLiveTtitlePagerAdapter titlePagerAdapter = null;
    private LinearLayout viewGroup;
    private ImageView imgNullData;

    //现场、录像Fragment
    private TextView btnScene, btnVideo;
    private ViewPager viewPagerList;
    private ViewPageListAdapter viewPageListAdapter;
    private BaseFragment sceneFragment;
    private BaseFragment videoFragment;
    private List<BaseFragment> fragmentList;

    //接口参数：epgid 1035
    private String epgid;

    /**
     * 加载View
     **/

    private final int LODING_NAV_DATE = 660; //获取导航列表
    private final int LODING_DATE_LOOP = 666;
    private final int LODING_DATE_LIVE = 667;
    private final int LODING_ERROR_500 = 500;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.home_two_live_fragment, container, false);
        setUI();
        return root;
    }

    @Override
    protected void initData() {
//        startLooding();
//        getNavigationListData(epgid, CacheConfig.cache_half_hour);
    }

    /**
     * 获取HomeActivity中参数
     *
     * @param bundle
     */
    @Override
    public void setData(Bundle bundle) {
        super.setData(bundle);
        if ((viewPageListAdapter != null && viewPageListAdapter.getCount() > 0)
                || (titlePagerAdapter != null && titlePagerAdapter.getCount() > 0)) {
            return;
        }

        initFragmentList();

        epgid = bundle.getString(Constant.NAVMOBILEEPG2);
        getNavigationListData(epgid, CacheConfig.cache_half_hour);
    }

    /**
     * 刷新页面
     *
     * @param event
     */
    @Override
    public void onEventMainThread(String event) {
        if (event.equals(Constant.HOME_TWO_LIVE_SCENE)) {
            updateDataType = 1;
            getNavigationListData(epgid, CacheConfig.cache_half_hour);
        } else if (event.equals(Constant.HOME_TWO_LIVE_VIDEO)) {
            updateDataType = 2;
            getNavigationListData(epgid, CacheConfig.cache_half_hour);
        }
    }

    /**
     * 初始化UI
     */
    private void setUI() {
        home_title = (View) root.findViewById(R.id.home_title_head);
        // title（logo）
        home_title.findViewById(R.id.img_home_title_my).setVisibility(View.VISIBLE);
        home_title.findViewById(R.id.img_home_title_my).setOnClickListener(this);
        // title（会员）
        home_title.findViewById(R.id.home_title_vip).setVisibility(View.VISIBLE);
        home_title.findViewById(R.id.home_title_vip).setOnClickListener(this);
        //title（播放记录）
        home_title.findViewById(R.id.img_home_title_history).setVisibility(View.VISIBLE);
        home_title.findViewById(R.id.img_home_title_history).setOnClickListener(this);
        //title（搜索）
        home_title.findViewById(R.id.img_home_title_seach).setVisibility(View.VISIBLE);
        home_title.findViewById(R.id.img_home_title_seach).setOnClickListener(this);

        //进度条布局
        octv_looding = (RelativeLayout) root.findViewById(R.id.octv_looding);
        octv_ivpi = (ImageView) root.findViewById(R.id.octv_ivpi);
        animation = (RotateAnimation) AnimationUtils.loadAnimation(getActivity(),
                R.anim.rotating);
        LinearInterpolator lir = new LinearInterpolator();
        animation.setInterpolator(lir);

        //轮播图片的ViewPage布局
        setViewPager();
        viewGroup = (LinearLayout) root.findViewById(R.id.layout_two_live_viewGroup);
        imgNullData = (ImageView) root.findViewById(R.id.img_two_live_null_data);

        //现场、录像布局
        btnScene = (TextView) root.findViewById(R.id.btn_home_two_live_item_scene);
        btnVideo = (TextView) root.findViewById(R.id.btn_home_two_live_item_video);
        viewPagerList = (ViewPager) root.findViewById(R.id.vp_two_live_variety_ocvt);
        viewPagerList.addOnPageChangeListener(onPageChangeListener);

        root.findViewById(R.id.ll_home_two_live_item_scene).setOnClickListener(this);
        root.findViewById(R.id.ll_home_two_live_item_video).setOnClickListener(this);

    }

    /**
     * 创建现场、录像的Fragment
     */
    private void initFragmentList() {
        if (fragmentList == null) {
            sceneFragment = new HomeTwoLiveSceneFragment();
            videoFragment = new HomeTwoLiveVideoFragment();
            fragmentList = new ArrayList<BaseFragment>();
            fragmentList.add(sceneFragment);
            fragmentList.add(videoFragment);

            viewPageListAdapter = new ViewPageListAdapter(
                    getActivity().getSupportFragmentManager(), fragmentList);
            viewPagerList.setOffscreenPageLimit(2);
            viewPagerList.setAdapter(viewPageListAdapter);
        }

    }

    /**
     * 获取导航列表
     *
     * @param epgid
     * @param cachetime
     */
    private void getNavigationListData(String epgid, int cachetime) {
        if (!NetWorkUtils.isNetworkAvailable(getActivity())) {
            handleErrorResponse();
            getNotNetWork(false);
            return;
        }

        startLooding();
        Lg.i(TAG,""+App.epgUrl+"...."+epgid);
        HttpRequest.getInstance().excute("getHomeNavigationList", new Object[]{App.epgUrl,
                epgid, cachetime, new SimpleHttpResponseListener<NavigationResultBean>() {
            @Override
            public void onSuccess(NavigationResultBean response) {
                Lg.i(TAG, "转播页面 - 导航列表数据：" + response);
                handleNavigationListDataSuccess(response);
            }

            @Override
            public void onError(String error) {
                Lg.e(TAG, "转播页面 - 导航列表数据：getHomeNavigationList onError , " + error == null ? "" : error);
                handleErrorResponse();
            }
        }});
    }

    /**
     * 获取导航列表数据成功
     *
     * @param response
     */
    private void handleNavigationListDataSuccess(NavigationResultBean response) {
        if (response == null) {
            handleErrorResponse();
            return;
        }
        navigationResultBean = response;
        mHandler.sendEmptyMessage(LODING_NAV_DATE);
    }

    /**
     * 获取转播数据
     */
    private void getRecommendData() {
        if (!NetWorkUtils.isNetworkAvailable(getActivity())) {
            handleErrorResponse();
            getNotNetWork(false);
            return;
        }

        if (navigationResultBean.getData() == null || navigationResultBean.getData().getContent() == null || navigationResultBean.getData().getContent().size() <= 0) {
            handleErrorResponse();
            return;
        }

        //接口参数
//        epgid = navigationResultBean.getData().getContent().get(0).getEpgId() + "";
        String channelID = navigationResultBean.getData().getContent().get(0).getSubjectId() + "";

        HttpRequest.getInstance().excute("getHomeNavContent", new Object[]{App.epgUrl, epgid,
                channelID, CacheConfig.cache_no_cache, new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                Lg.i(TAG, "转播页面 - 导航下的数据：" + response);
                handleLoadRecommendListSuccessResponse(response);
            }

            @Override
            public void onError(String error) {
                Lg.i(TAG, "转播页面 - 导航下的数据：getHomeNavContent : error--> " + error);
                handleErrorResponse();
            }
        }});
    }

    /**
     * 获取数据成功
     *
     * @param response
     */
    private void handleLoadRecommendListSuccessResponse(String response) {
        try {
            if (StringUtils.isEmpty(response)) {
                handleErrorResponse();
                return;
            }

            NavigationInfoResultBean navigationInfoResultBean = JSON.parseObject(response, NavigationInfoResultBean.class);
            if (navigationInfoResultBean == null) {
                handleErrorResponse();
                return;
            }

            //获取列表所有数据
            NavigationInfoBean mResultBean = navigationInfoResultBean.getData();
            if (mResultBean != null && mResultBean.getContent() != null && mResultBean.getContent().size() > 0) {
                List<NavigationInfoBlockBean> navigationInfoBlockBeanList = mResultBean.getContent();

                List<NavigationInfoItemBean> contentsList = navigationInfoBlockBeanList.get(0).getIndexContents();
                infoBlockBean = navigationInfoBlockBeanList.get(1); //现场、录像数据

                if (contentsList != null && contentsList.size() > 0) {
                    //滚动图片集合
                    loopContentsList = contentsList.get(0).getContents();
                    if (loopContentsList != null && loopContentsList.size() > 0) {
                        mHandler.sendEmptyMessage(LODING_DATE_LOOP);
                    }
                }

                if (infoBlockBean != null) {
                    mHandler.sendEmptyMessage(LODING_DATE_LIVE);
                }

                if (contentsList == null || contentsList.size() <= 0 || infoBlockBean == null) {
                    handleErrorResponse();
                    return;
                }

            } else {
                handleErrorResponse();
                return;
            }
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
     * 获取轮播图片数据
     */
    private void getLiveLoopData() {
        if (loopContentsList != null && loopContentsList.size() > 0) {
//            setViewPager();
            setPagerDate();
            if (loopContentsList.size() > 1) {
                vPager.result = true;
                // 添加Pager内部的下标白点
                viewGroup.removeAllViews();
                setBottomWhiteImg(loopContentsList.size(), viewGroup);
                resetcurrentItem();

                vPager.setVisibility(View.VISIBLE);
                imgNullData.setVisibility(View.GONE);
                // 开始轮播效果
                handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
            }
        }
    }

    /**
     * 传数据到现场、录像页面的数据
     */
    private void getFragmentData() {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.BUNDLE_EPGID, epgid);
        bundle.putSerializable(Constant.HOME_TWO_LIVE, infoBlockBean);
        if (updateDataType == 0) {
            if (sceneFragment != null) {
                sceneFragment.setData(bundle);
            }
            if (videoFragment != null) {
                videoFragment.setData(bundle);
            }
        } else if (updateDataType == 1) {
            if (sceneFragment != null) {
                sceneFragment.setData(bundle);
            }
        } else if (updateDataType == 2) {
            if (videoFragment != null) {
                videoFragment.setData(bundle);
            }
        }
    }

    /**
     * 轮播图片的ViewPager
     */
    @Override
    public void setViewPager() {
        vPager = (ViewPagerStop) root.findViewById(R.id.content_two_live_pager);
        vPager.setOnPageChangeListener(new MyOnPageChangeListener());
        setScoller(vPager);
        vPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
    }

    @Override
    public void setPagerDate() {
        viewList.clear();
        for (int i = 0; i < loopContentsList.size(); i++) {
            if (context != null) {
                TiltleAdvertisementView iv = new TiltleAdvertisementView(context);
                iv.setFocusable(true);
                iv.setOnTouchListener(new MyOnTouchListener());
                if (!StringUtils.isEmpty(loopContentsList.get(i).getImg())) {
                    ImageFetcher.getInstance().loadImage(loopContentsList.get(i).getImg(), iv.getImg());
                }
                iv.getTVname().setText(loopContentsList.get(i).getDisplayName());
                viewList.add(iv);
            }
        }
        titlePagerAdapter = new HomeTwoLiveTtitlePagerAdapter(viewList, context, loopContentsList, new HomeOnItemClickListener() {


            @Override
            public void onItemClickListener(Bundle bundle) {
                HomeJumpDetailUtils.actionTo((NavigationInfoItemBean) bundle.get(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN), getActivity());
            }
        });
        vPager.setAdapter(titlePagerAdapter);
    }

    @Override
    public void onClick(View v) {
        Bundle mBundle = new Bundle();
        switch (v.getId()) {
            //会员按钮
            case R.id.home_title_vip:
                startActivity(Action.getActionName(Action.OPEN_VIP), mBundle);
                break;

            //搜索按钮
            case R.id.img_home_title_seach:
                startActivity(Action.getActionName(Action.OPEN_SEARCH), mBundle);
                break;

            //播放记录
            case R.id.img_home_title_history:
                mBundle.putLong(Constant.contentIdKey, Constant.userIntent3);
                startActivity(Action.getActionName(Action.OPEN_COLLECTION), mBundle);
                break;

            //现场按钮
            case R.id.ll_home_two_live_item_scene:
                viewPagerList.setCurrentItem(0);
                break;

            //录像按钮
            case R.id.ll_home_two_live_item_video:
                viewPagerList.setCurrentItem(1);
                break;
        }
    }


    /**
     * ViewPager滑动监听
     */
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                btnScene.setTextColor(getResources().getColor(R.color.white));
                btnScene.setBackgroundResource(R.mipmap.live_tab_bg);
                btnVideo.setTextColor(getResources().getColor(R.color.channel_inag));
                btnVideo.setBackgroundResource(R.color.transparent);
            } else if (position == 1) {
                btnScene.setTextColor(getResources().getColor(R.color.channel_inag));
                btnScene.setBackgroundResource(R.color.transparent);
                btnVideo.setTextColor(getResources().getColor(R.color.white));
                btnVideo.setBackgroundResource(R.mipmap.live_tab_bg);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    /**
     * 现场、录像页面的viewPager的Adapter
     */
    class ViewPageListAdapter extends FragmentStatePagerAdapter {
        private List<BaseFragment> fragmentLists;

        public ViewPageListAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
            super(fm);
            this.fragmentLists = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = fragmentLists.get(0);
                    break;

                case 1:
                    fragment = fragmentLists.get(1);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return fragmentLists != null ? fragmentLists.size() : 0;
        }
    }

    /**
     * Handler处理
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LODING_NAV_DATE:
                    stopLooding();
                    getRecommendData();
                    break;

                case LODING_DATE_LOOP://轮播图片
                    stopLooding();
                    vPager.setVisibility(View.VISIBLE);
                    imgNullData.setVisibility(View.GONE);
                    getLiveLoopData();
                    break;

                case LODING_DATE_LIVE: //现场、录像
                    stopLooding();
                    getFragmentData();
                    break;

                case LODING_ERROR_500:
                    stopLooding();
                    imgNullData.setVisibility(View.VISIBLE);
                    vPager.setVisibility(View.GONE);
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
     * 网络是否可用处理
     *
     * @param isNotWork true 可用、false 不可用
     */
    private void getNotNetWork(boolean isNotWork) {
        if (!isNotWork) {
            Lg.i(TAG, "当前网络异常，请重试连接!");
            EventBus.getDefault().post(Constant.NETWORK_NO_HOME_TWO_LIVE);
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
    private void stopLooding() {
        if (octv_looding != null && animation != null) {
            octv_looding.setVisibility(View.GONE);
            animation.cancel();
            octv_ivpi.clearAnimation();
        }
    }

}
