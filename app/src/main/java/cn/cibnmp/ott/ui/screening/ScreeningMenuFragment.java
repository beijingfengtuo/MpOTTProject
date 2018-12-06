package cn.cibnmp.ott.ui.screening;

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
import android.widget.LinearLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.ScreeningBean;
import cn.cibnmp.ott.config.CacheConfig;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.SimpleHttpResponseListener;
import cn.cibnmp.ott.ui.base.BaseFragment;
import cn.cibnmp.ott.ui.search.ToastUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.NetWorkUtils;
import cn.cibnmp.ott.utils.ScreeningMenuSetIndicator;
import cn.cibnmp.ott.utils.SetIndicator;
import cn.cibnmp.ott.widgets.ScreeTabLayout;
import de.greenrobot.event.EventBus;

/**
 * 筛选页面 - 筛选项列表页面
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2018/1/4
 */
public class ScreeningMenuFragment extends BaseFragment{
    private static String TAG = ScreeningListFragment.class.getName();

    private static int TABMAGIN = 30;
    private ScreeningActivity mActivity;
    private View root;

    //筛选列表
    private LinearLayout llMenuAll;
    private List<ScreeTabLayout> tabLayoutList;

    //筛选菜单列表集合数据
    private List<ScreeningBean.DataBean> screeningMenuList = null;

    private int subjectID, columnID;

    /**
     * epgID：
     * contentID：栏目ID（等于columnID 997/p1）
     * p1：栏目ID（等于columnID 997/contentID）
     * p2：频道ID（等于subjectID /p2/p3）
     * p3：频道ID（等于subjectID /p2/p3）
     */
    private int epgID, contentID, p1, p2, p3;

    /**
     * 加载View
     **/
    private final int LODING_DATE = 666;
    private final int LODING_ERROR_500 = 500;
    private final int LODING_DATE_UP = 6000;
    private LayoutInflater inflater;

    public static ScreeningMenuFragment newInstance(Bundle bundle) {
        ScreeningMenuFragment fragment = new ScreeningMenuFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        root = inflater.inflate(R.layout.screening_menu, container, false);

        //获取页面传来的参数
        Bundle bundle = getArguments();
        epgID = bundle.getInt(Constant.BUNDLE_EPGID, 0);
        contentID = (int) bundle.get(Constant.BUNDLE_CONTENTID);
        p1 = bundle.getInt(Constant.BUNDLE_P1, 0);
        p2 = bundle.getInt(Constant.BUNDLE_P2, 0);
        p3 = bundle.getInt(Constant.BUNDLE_P3, 0);

        if (p1 != 0) {
            columnID = p1;
        } else if (contentID != 0) {
            columnID = contentID;
        }

        if (p2 != 0) {
            subjectID = p2;
        } else if (p3 != 0) {
            subjectID = p3;
        }

        mActivity = (ScreeningActivity) getActivity();

        initView();
        return root;
    }

    private void initView() {
        llMenuAll = (LinearLayout) root.findViewById(R.id.ll_screening_menu_all);
        getScreeningBean();
    }

    /**
     * 获取筛选页面选项卡数据
     */
    private void getScreeningBean() {
        if (!NetWorkUtils.isNetworkAvailable(getActivity())) {
            Lg.i(TAG, "当前网络异常，请重试连接!");
            mHandler.sendEmptyMessage(LODING_ERROR_500);
            return;
        }

        //TODO 参数需重写
//        final String url = App.epgUrl + "/filter?epgId=" + "1031" + "&channelId=" + "997";
        final String url = App.epgUrl + "/filter?epgId=" + epgID + "&channelId=" + columnID;
        HttpRequest.getInstance().excute("getFilterCondition", new Object[]{url,
                CacheConfig.cache_no_cache, new SimpleHttpResponseListener<ScreeningBean>() {

            @Override
            public void onSuccess(ScreeningBean screeningBean) {
                Lg.i(TAG, "筛选项列表：" + screeningBean.toString());
                handleLoadScreeningBeanSuccessResponse(screeningBean);
            }

            @Override
            public void onError(String error) {
                handleLoadScreeningBeanErrorResponse();
            }
        }});
    }

    /**
     * 获取数据成功
     *
     * @param screeningBean
     */
    private void handleLoadScreeningBeanSuccessResponse(ScreeningBean screeningBean) {
        try {
            if (screeningBean == null) {
                handleLoadScreeningBeanErrorResponse();
                return;
            }

            screeningMenuList = screeningBean.getData();
            if (screeningMenuList == null || screeningMenuList.size() <= 0) {
                handleLoadScreeningBeanErrorResponse();
                return;
            }

            for (int i = 0; i < screeningMenuList.size(); i++) {
                List<ScreeningBean.DataBean.ContentBean> tabList = screeningMenuList.get(i).getContent();
                if (tabList != null && tabList.size() > 0) {
                    tabList.add(0, new ScreeningBean.DataBean.ContentBean(epgID, 0, "全部", ""));
                }
            }
            mHandler.sendEmptyMessage(LODING_DATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据失败
     *
     */
    private void handleLoadScreeningBeanErrorResponse() {
        mHandler.sendEmptyMessage(LODING_ERROR_500);
    }

    /**
     * 分类
     */
    private void getScreeningMenuTabDataInfo() {

        //筛选项列表
        if (screeningMenuList != null && screeningMenuList.size() > 0) {
            tabLayoutList = new ArrayList<ScreeTabLayout>();
            //遍历有多少的分类
            for (int i = 0; i < screeningMenuList.size(); i++) {
                List<BaseFragment> layouts = new ArrayList<BaseFragment>();
                //创建Tablayout
                View view = inflater.inflate(R.layout.activity_screening_menu_tab, null);
                ScreeTabLayout tabLayout = (ScreeTabLayout) view.findViewById(R.id.tl_screening_menu_tab_1);
                ViewPager viewPager = (ViewPager) view.findViewById(R.id.vp_screening_menu_tab_1);
                tabLayout.setTags(i); //给TabLayout添加标签
                tabLayoutList.add(tabLayout);
                int tabSelectPos = 0;
                List<ScreeningBean.DataBean.ContentBean> tabList = screeningMenuList.get(i).getContent();
                if (tabList != null && tabList.size() > 0) {
                    //遍历TabLayout中的item，找出选中的item做标识
                    for (int k = 0; k < tabList.size(); k++) {
                        layouts.add(new BaseFragment());
                        if (subjectID == tabList.get(k).getSubjectId()) {
                            tabList.get(k).setIschecked(true);
                            tabSelectPos = k;
                        }
                    }

                    //清空TabLayout中的View
                    if (tabLayout != null && tabLayout.getChildCount() > 0) {
                        tabLayout.removeAllTabs();
                    }

                    //给TabLayout绑定Adapter
                    VarietyVpAdapter vpAdapter = new VarietyVpAdapter(
                            getActivity().getSupportFragmentManager(), layouts, tabList);
                    viewPager.setOffscreenPageLimit(3);
                    viewPager.setAdapter(vpAdapter);
                    tabLayout.setupWithViewPager(viewPager);
                    tabLayout.setTabsFromPagerAdapter(vpAdapter);
                    tabLayout.setOnTabSelectedListener(new ScreeTabLayout.TabSelectedListener() {
                        @Override
                        public void onTabSelected(ScreeTabLayout tabLayout, TabLayout.Tab tab) {
                            if (tabLayoutList != null && tabLayoutList.size() > 0) {
                                if (tabLayout.getTags() < tabLayoutList.size()) {
                                    ScreeningMenuSetIndicator.setIndicatorStyle((TabLayout)(tabLayoutList.get(tabLayout.getTags())), tab.getPosition(), R.mipmap.img_screening_menu_tab_bg, getResources().getColor(R.color.white), true);
                                    if (screeningMenuList != null && screeningMenuList.size() > 0) {
                                        //点击item时，先将所有item的标识改为未选中
                                        for (int i = 0; i < screeningMenuList.size(); i++) {
                                            List<ScreeningBean.DataBean.ContentBean> tabList = screeningMenuList.get(i).getContent();
                                            if (tabList != null && tabList.size() > 0) {
                                                //点击的item是否属于当前行的分类中
                                                if (i == tabLayout.getTags()) {
                                                    //将所有item的标识改为未选中
                                                    for (int k = 0; k < tabList.size(); k++) {
                                                        tabList.get(k).setIschecked(false);
                                                    }
                                                    //设置item选中时的标识
                                                    tabList.get(tab.getPosition()).setIschecked(true);
                                                }
                                            }
                                        }
                                    }
                                    //给列表Fragment传递参数
                                    getScreeningRequest(epgID, columnID, columnID, getKeyValue());
                                }
                            }
                        }

                        @Override
                        public void onTabUnselected(ScreeTabLayout tabLayout, TabLayout.Tab tab) {

                            if (tabLayoutList != null && tabLayoutList.size() > 0) {
                                if (tabLayout.getTags() < tabLayoutList.size()) {
                                    if (tab.getPosition() == 0) {
                                        ScreeningMenuSetIndicator.setIndicatorStyle((TabLayout)(tabLayoutList.get(tabLayout.getTags())), tab.getPosition(), getResources().getColor(R.color.transparent), getResources().getColor(R.color.colore_home11), false);
                                    } else {
                                        ScreeningMenuSetIndicator.setIndicatorStyle((TabLayout)(tabLayoutList.get(tabLayout.getTags())), tab.getPosition(), getResources().getColor(R.color.transparent), getResources().getColor(R.color.black3), false);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onTabReselected(ScreeTabLayout tabLayout, TabLayout.Tab tab) {

                        }
                    });
                    ScreeningMenuSetIndicator.setTabIndicator((TabLayout) tabLayout, TABMAGIN, TABMAGIN);
                    viewPager.setCurrentItem(tabSelectPos);
                    ScreeningMenuSetIndicator.setIndicatorStyle((TabLayout)tabLayout, tabSelectPos, R.mipmap.img_screening_menu_tab_bg, getResources().getColor(R.color.white), true);
                    llMenuAll.addView(view);
                }
            }
        }

    }

    /**
     * 测试数据
     */
    private void getScreeningMenuTabData() {
        getScreeningMenuTabDataInfo();
    }

    /**
     * 拼接筛选词，以封号（;）分割
     *
     * @return
     */
    private String getKeyValue() {
        String values = null;
        try {
            values = "";
            if (screeningMenuList != null && screeningMenuList.size() > 0) {
                for(int i = 0; i < screeningMenuList.size(); i++) {
                    List<ScreeningBean.DataBean.ContentBean> tabList = screeningMenuList.get(i).getContent();
                    if (tabList != null && tabList.size() > 0) {
                        for (int k = 0; k < tabList.size(); k++) {
                            if (i == 0) {
                                if (tabList.get(k).ischecked()) {
                                    values = URLEncoder.encode(tabList.get(k).getScreenKey(), "UTF-8");
                                }
                            } else {
                                if (tabList.get(k).ischecked()) {
                                    values = values + ";" + URLEncoder.encode(tabList.get(k).getScreenKey(), "UTF-8");
                                }
                            }
                        }
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return values;
    }

    /**
     * 获取筛选选项卡参数
     *
     * @param epgID
     * @param subjectID
     * @param keyWords
     */
    private void getScreeningRequest(int epgID, int columnID, int subjectID, String keyWords) {
        ((ScreeningActivity)getActivity()) .getFromeScreeningListFrag(epgID, columnID, subjectID, keyWords);
    }

    /**
     * Handler处理
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LODING_DATE:
                    getScreeningMenuTabData();
                    mHandler.sendEmptyMessageDelayed(LODING_DATE_UP, 500);
                    break;

                case LODING_DATE_UP:

                    break;

                case LODING_ERROR_500:
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
     * 选项卡适配器
     *
     * @author cibn
     */
    class VarietyVpAdapter extends FragmentStatePagerAdapter {
        private List<BaseFragment> layouts = null;
        private List<ScreeningBean.DataBean.ContentBean> tabList = null;

        public VarietyVpAdapter(FragmentManager fm, List<BaseFragment> mLayouts, List<ScreeningBean.DataBean.ContentBean> tabList) {
            super(fm);
            this.layouts = mLayouts;
            this.tabList = tabList;
        }

        private void setData(List<BaseFragment> mLayouts, List<ScreeningBean.DataBean.ContentBean> tabList) {
            this.layouts = mLayouts;
            this.tabList = tabList;
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
            return tabList.get(position).getName();
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
