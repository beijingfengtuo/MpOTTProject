package cn.cibnmp.ott.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.bean.NavigationItemBean;
import cn.cibnmp.ott.bean.NavigationItemDataBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.HomeActivity;
import cn.cibnmp.ott.ui.base.Action;
import cn.cibnmp.ott.ui.base.BaseFragment;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.SetIndicator;
import cn.cibnmp.ott.utils.SharedPreferencesUtil;

/**
 * Created by yangwenwu on 2017/12/25.
 */

public class HomeOneFragment extends BaseFragment implements View.OnClickListener, TabLayout.OnTabSelectedListener {

    private View mView;
    private int mPageIndex;
    private View mHomeTitle;
    private final String TAG = "HomeOneFragment";
    private ViewPager mHomeviewpager; //首页viewpager
    private TabLayout mHomeTablayout; //首页频道列表
    private List<String> mTablist = new ArrayList<>(); //首页频道列表集合
    private ImageView mHomechannelmanagement;
    private List<BaseFragment> mViews = new ArrayList<>();
    private HomeChannelAdapter mHomeChannelAdapter = null;
    private NavigationItemDataBean mNavigationItemDataBean;
    private List<NavigationItemBean> mSpfChannelList = new ArrayList<NavigationItemBean>();
    private SharedPreferencesUtil mSharedPreferencesUtil;
    private int mTabLayoutWidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.home_one_fragment, container, false);
        initView();
        return mView;
    }

    private void initView() {
        //title（大剧院）
        mHomeTitle = (View) mView.findViewById(R.id.home_title_head);
        mHomeTitle.findViewById(R.id.img_home_title_my).setVisibility(View.VISIBLE);
        mHomeTitle.findViewById(R.id.img_home_title_my).setOnClickListener(this);
        //title（会员）
        mHomeTitle.findViewById(R.id.home_title_vip).setVisibility(View.VISIBLE);
        mHomeTitle.findViewById(R.id.home_title_vip).setOnClickListener(this);
        //title（播放记录）
        mHomeTitle.findViewById(R.id.img_home_title_history).setVisibility(View.VISIBLE);
        mHomeTitle.findViewById(R.id.img_home_title_history).setOnClickListener(this);
        //title（搜索）
        mHomeTitle.findViewById(R.id.img_home_title_seach).setVisibility(View.VISIBLE);
        mHomeTitle.findViewById(R.id.img_home_title_seach).setOnClickListener(this);
        //首页频道列表tab
        mHomeTablayout = ((TabLayout) mView.findViewById(R.id.home_tablayout));
        mHomeviewpager = ((ViewPager) mView.findViewById(R.id.home_viewpager));
        mHomechannelmanagement = ((ImageView) mView.findViewById(R.id.home_channelmanagement));
        mHomechannelmanagement.setOnClickListener(this);
    }

    /**
     * 判断是否是频道管理返回的数据
     *
     * @param event
     */
    @Override
    public void onEventMainThread(String event) {
        if (event.equals(Constant.CHANNEL_STATUS)) {
            mSharedPreferencesUtil = new SharedPreferencesUtil();
            List<NavigationItemBean> chaneelList = mSharedPreferencesUtil.getList(getActivity(), "mComeChannelList");
            if (chaneelList != null && mSpfChannelList != null) {
                mSpfChannelList.clear();
                mSpfChannelList.addAll(chaneelList);
            }
            mPageIndex = mSharedPreferencesUtil.getInt(getActivity(), "pageIndex", 0);
            initChannel();
        }
    }

    /**
     * 接收HomeActivity传的导航数据
     */
    @Override
    public void setData(Bundle bundle) {
        super.setData(bundle);
        if (bundle != null) {
            mNavigationItemDataBean = (NavigationItemDataBean) bundle.get(Constant.CURITEMDATABEAN);
            initChannel();
        } else {
            HomeActivity homeActivity = new HomeActivity();
            homeActivity.requestHomeNav();
        }
    }

    /**
     * 添加导航数据
     */
    private void initChannel() {
        if (mHomeChannelAdapter != null) {
            //判断是否是频道列表返回的数据
            if (mSpfChannelList != null && mSpfChannelList.size() > 0) {
                mViews.clear();
                mTablist.clear();
                mHomeTablayout.removeAllTabs();
                for (int n = 0; n < mSpfChannelList.size(); n++) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CHANNEL_EPGID, mNavigationItemDataBean.getEpgId() + "");
                    bundle.putString(Constant.CHANNEL_ID, mSpfChannelList.get(n).getSubjectId() + "");
                    if (mSpfChannelList.get(n).getIsDefaultShow() == 1) {
                        mViews.add(new HomeOneItemFragment().newInstance(bundle));
                        mTablist.add(mSpfChannelList.get(n).getName());
                        mHomeTablayout.addTab(mHomeTablayout.newTab().setText(mSpfChannelList.get(n).getName()));
                    }
                }
                mHomeChannelAdapter.setData(mViews, mTablist);
                mHomeviewpager.setCurrentItem(mPageIndex);
                mSharedPreferencesUtil.putInt(getContext(), "pageIndex", 0);
            } else {
                Lg.i(TAG, "mComeSpfChannelList == null");
            }
        } else {
            //首次进入应用
            mViews.clear();
            mSpfChannelList = mSharedPreferencesUtil.getList(getActivity(), "mComeChannelList");
            List<NavigationItemBean> deWeightList = new ArrayList<>();
            List<NavigationItemBean> newAddDataList = new ArrayList<>();
            if (mSpfChannelList != null && mNavigationItemDataBean.getContent() != null) {
                newAddDataList.addAll(mNavigationItemDataBean.getContent());
                //判断用户进入应用是否调过导航，如果调过就对比服务器是否有添加或者删除
                for (NavigationItemBean item1 : mSpfChannelList) {
                    for (NavigationItemBean item2 : newAddDataList) {
                        if (item2.getName().equals(item1.getName())) {
                            item2.setIsDefaultShow(item1.getIsDefaultShow());
                            deWeightList.add(item2);
                            newAddDataList.remove(item2);
                            break;
                        }
                    }
                }
                mSpfChannelList = deWeightList;
                mSpfChannelList.addAll(newAddDataList);
                for (int i = 0; i < mSpfChannelList.size(); i++) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CHANNEL_EPGID, mNavigationItemDataBean.getEpgId() + "");
                    bundle.putString(Constant.CHANNEL_ID, mSpfChannelList.get(i).getSubjectId() + "");
                    if (mSpfChannelList.get(i).getIsDefaultShow() == 1) {
                        mViews.add(new HomeOneItemFragment().newInstance(bundle));
                        mTablist.add(mSpfChannelList.get(i).getName());
                    }
                }
            } else {
                //如果用户没有调过导航就加载服务器数据
                if (mNavigationItemDataBean != null && mNavigationItemDataBean.getContent() != null) {
                    mSpfChannelList = mNavigationItemDataBean.getContent();
                    for (int i = 0; i < mSpfChannelList.size(); i++) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.CHANNEL_EPGID, mNavigationItemDataBean.getEpgId() + "");
                        bundle.putString(Constant.CHANNEL_ID, mSpfChannelList.get(i).getSubjectId() + "");
                        mViews.add(new HomeOneItemFragment().newInstance(bundle));
                        mTablist.add(mSpfChannelList.get(i).getName());
                    }
                } else {
                    Lg.i(TAG, "mNavigationItemDataBean == null");
                }
            }
            initAdapter();
        }
        //设置Tablayout宽度（下划线和字的长度）
        SetIndicator.setIndicator(mHomeTablayout, 24, 24, mTabLayoutWidth, new SetIndicator.SetIndicatorInterface() {
            @Override
            public void getsIndicator(int width) {
                mTabLayoutWidth = width;
            }
        });
    }

    //首次加载数据
    private void initAdapter() {
        mHomeChannelAdapter = new HomeChannelAdapter(
                getActivity().getSupportFragmentManager(), mViews, mTablist);
        mHomeviewpager.setOffscreenPageLimit(3);
        mHomeviewpager.setAdapter(mHomeChannelAdapter);
        mHomeTablayout.setupWithViewPager(mHomeviewpager);
        mHomeTablayout.setTabsFromPagerAdapter(mHomeChannelAdapter);
        mHomeTablayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle mBundle = new Bundle();
        switch (v.getId()) {

            //频道管理
            case R.id.home_channelmanagement:
                if (mSpfChannelList != null && mNavigationItemDataBean != null) {
                    Intent channelManagement = new Intent(getActivity(), ChannelManagementActivity.class);
                    channelManagement.putExtra("DataList", (ArrayList) mSpfChannelList);
                    channelManagement.putExtra("ItemDataBean", mNavigationItemDataBean);
                    startActivity(channelManagement);
                    mHomeviewpager.setCurrentItem(0);
                } else {
                    return;
                }
                break;

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
                if (mNavigationItemDataBean != null && mNavigationItemDataBean.getEpgId() > 0) {
                    mBundle.putString(Constant.CHANNEL_SEARCH_EPGID, mNavigationItemDataBean.getEpgId() + "");
                    startActivity(Action.getActionName(Action.OPEN_SEARCH), mBundle);
                } else {
                    return;
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mHomeviewpager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
}
