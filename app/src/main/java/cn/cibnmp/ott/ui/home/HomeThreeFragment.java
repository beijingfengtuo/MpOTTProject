package cn.cibnmp.ott.ui.home;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.NavigationItemBean;
import cn.cibnmp.ott.bean.NavigationResultBean;
import cn.cibnmp.ott.config.CacheConfig;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.SimpleHttpResponseListener;
import cn.cibnmp.ott.ui.base.Action;
import cn.cibnmp.ott.ui.base.BaseFragment;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.SetIndicator;

/**
 * Created by cibn-lyc on 2017/12/22.
 */

public class HomeThreeFragment extends BaseFragment implements View.OnClickListener, TabLayout.OnTabSelectedListener{
    private final String TAG = HomeThreeFragment.class.getName();
    private View root;
    private View home_title;
    private TextView tv_home_vip;

    //ViewPager+TabLayout布局
    private ViewPager mViewPager;
    private List<BaseFragment> layouts = new ArrayList<BaseFragment>();
    private List<String> titles = new ArrayList<>();
    private TabLayout mTabLayout;
    private VarietyVpAdapter vpAdapter;

    //epgId、顶部导航总数据、导航列表
    private String epgid;
    private List<NavigationItemBean> navigationItemBeanList;

    //更新选项卡、暂停动画
    public final int UPDATA_MENU = 0;
    //Handler标识
    private final int LODING_DATE = 666;
    public final int LODING_ERROR_500 = 500;
    private final int LODING_DATE_UP = 6000;
    //当前选中的viewPage索引
    private int pageIndex;

    public static HomeThreeFragment newInstance(Bundle bundle) {
        HomeThreeFragment fragment = new HomeThreeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.home_three_fragment, container, false);
        initView();
        return root;
    }

    private void initView() {
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

        //ViewPager布局
        mViewPager = (ViewPager) root.findViewById(R.id.vp_three_variety_ocvt);
        mTabLayout = (TabLayout) root.findViewById(R.id.tl_three_tabs_ocvt);
    }

    /**
     * 获取HomeActivity中参数
     *
     * @param bundle
     */
    @Override
    public void setData(Bundle bundle) {
        super.setData(bundle);

        if (vpAdapter != null && vpAdapter.getCount() > 0) {
            return;
        }
        epgid = bundle.getString(Constant.NAVMOBILEEPG);
        requestNavigationList(epgid, CacheConfig.cache_half_hour);
    }

    /**
     * 请求导航数据
     *
     * @param cachetime
     */
    private void requestNavigationList(String epgid, int cachetime) {
        HttpRequest.getInstance().excute("getHomeNavigationList", new Object[]{App.epgUrl,
                epgid, cachetime, new SimpleHttpResponseListener<NavigationResultBean>() {
            @Override
            public void onSuccess(NavigationResultBean response) {
                handleRequestNavSuccess(response);
            }

            @Override
            public void onError(String error) {
                Lg.e(TAG, "getHomeNavigationList onError , " + error == null ? "" : error);
                sendEntryErrorMsg();

            }
        }});
    }

    /**
     * 获取数据成功
     *
     * @param response
     */
    private void handleRequestNavSuccess(NavigationResultBean response) {
        try {
            if (response == null) {
                Lg.e(TAG, "NavigationResultBean response --> response is null or empty !!!");
                sendEntryErrorMsg();
                return;
            }

            Lg.e(TAG, "NavigationResultBean response -->" + response);

            //导航列表数据
            if (response.getData() != null && response.getData().getContent() != null && response.getData().getContent().size() > 0) {
                navigationItemBeanList = response.getData().getContent();
            }

            mHandler.sendEmptyMessage(UPDATA_MENU);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取数据失败
     *
     */
    private void sendEntryErrorMsg() {
        mHandler.sendEmptyMessage(LODING_ERROR_500);
    }

    private void initData() {
        if (navigationItemBeanList == null) {
            return;
        }

        if(titles != null && titles.size() > 0) {
            titles.clear();
        }

        if (layouts != null && layouts.size() > 0) {
            layouts.clear();
        }

        if (mTabLayout != null && mTabLayout.getChildCount() > 0) {
            mTabLayout.removeAllTabs();
        }

        //显示所有的Fragment 和 导航菜单数据
        for (int i = 0; i < navigationItemBeanList.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.CHANNEL_EPGID, navigationItemBeanList.get(i).getEpgId() + "");
            bundle.putString(Constant.CHANNEL_ID, navigationItemBeanList.get(i).getSubjectId() + "");

            titles.add(navigationItemBeanList.get(i).getName());
            layouts.add(HomeThreeItemFragment.newInstance(bundle));
        }

        if (layouts == null || layouts.size() <= 0 || titles == null || titles.size() <= 0) {
            return;
        }

        if (vpAdapter == null) {
            vpAdapter = new VarietyVpAdapter(
                    getActivity().getSupportFragmentManager(), layouts, titles);
            mViewPager.setOffscreenPageLimit(3);
            mViewPager.setAdapter(vpAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
            mTabLayout.setTabsFromPagerAdapter(vpAdapter);
            mTabLayout.setOnTabSelectedListener(this);
        } else {
            for (int i = 0; i < layouts.size(); i++) {
                mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
            }
            vpAdapter.setData(layouts, titles);
        }
        SetIndicator.setIndicator(mTabLayout, 24, 24);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        Bundle mBundle = new Bundle();
        switch (v.getId()){
            //会员
            case R.id.home_title_vip:
                startActivity(Action.getActionName(Action.OPEN_VIP), mBundle);
                break;

            //播放记录
            case R.id.img_home_title_history:
                mBundle.putLong(Constant.contentIdKey, Constant.userIntent3);
                startActivity(Action.getActionName(Action.OPEN_COLLECTION), mBundle);
                break;

            //搜索
            case R.id.img_home_title_seach:
                startActivity(Action.getActionName(Action.OPEN_SEARCH), mBundle);
                break;

            default:
                break;
        }
    }

    /**
     * 更新选项卡
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATA_MENU:  //更新选项卡
                    initData();
                    break;

                case LODING_ERROR_500:
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
     * TabLayout布局选中监听
     *
     * @param tab
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //如果当前选中的选项卡和记录的上一次选项卡不一致，关闭小视屏播放器
        if (this.pageIndex != tab.getPosition()) {
            getCompletion();
        }
        this.pageIndex = tab.getPosition();
        hideAllTab(pageIndex);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /**
     * 隐藏展示的所有标签，并显示tablayout
     *
     * @param position
     */
    private void hideAllTab(int position) {
        mViewPager.setCurrentItem(position);
    }

    /**
     * 选项卡适配器
     *
     * @author cibn
     */
    class VarietyVpAdapter extends FragmentStatePagerAdapter {
        private List<BaseFragment> layouts = null;
        private List<String> titles = null;

        public VarietyVpAdapter(FragmentManager fm, List<BaseFragment> mLayouts, List<String> titles) {
            super(fm);
            this.layouts = mLayouts;
            this.titles = titles;
        }

        private void setData(List<BaseFragment> mLayouts, List<String> titles) {
            this.layouts = mLayouts;
            this.titles = titles;
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            if (layouts == null || layouts.size() == 0) {
                return null;
            }
            return layouts.get(position);
        }

        @Override
        public int getCount() {
            if (layouts == null || layouts.size() == 0) {
                return 0;
            }
            return layouts.size();
        }

        //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }



    public void getOnAttachedToWindow() {
        if (layouts != null && layouts.size() > 0) {
            ((HomeThreeItemFragment) layouts.get(pageIndex)).getOnAttachedToWindow();
        }
    }

    public void getOnDetachedFromWindow() {
        if (layouts != null && layouts.size() > 0) {
            ((HomeThreeItemFragment) layouts.get(pageIndex)).getOnDetachedFromWindow();
        }
    }

    public void getOnConfigurationChanged(Configuration newConfig) {
        if (layouts != null && layouts.size() > 0) {
            ((HomeThreeItemFragment) layouts.get(pageIndex)).getOnConfigurationChanged(newConfig);
        }
    }

    /**
     * 返回按键处理
     *
     * @return
     */
    public boolean getOnKeyDown() {
        if (layouts != null && layouts.size() > 0) {
            return ((HomeThreeItemFragment) layouts.get(pageIndex)).getOnKeyDown();
        }
        return false;
    }

    /**
     * 返回按键处理
     *
     * @return
     */
    public void getCompletion() {
        if (layouts != null && layouts.size() > 0) {
            ((HomeThreeItemFragment) layouts.get(pageIndex)).getCompletion(false);
        }
    }
}
