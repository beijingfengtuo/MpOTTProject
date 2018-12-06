package cn.cibnmp.ott.ui.home;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.ta.utdid2.android.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.NavigationInfoBean;
import cn.cibnmp.ott.bean.NavigationInfoResultBean;
import cn.cibnmp.ott.config.CacheConfig;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.ui.HomeActivity;
import cn.cibnmp.ott.ui.base.BaseFragment;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.NetWorkUtils;
import cn.cibnmp.ott.utils.SetIndicator;

/**
 * Created by zxyggdf on 2018/4/16.
 */

public class HomeThreeItemFragment extends BaseFragment {
    private final String TAG = HomeThreeItemFragment.class.getName();
    private HomeActivity homeActivity;
    private View root;

    //页面\接口参数
    private String epgId; //epgID
    private String navId; //导航菜单id

    //ViewPager+TabLayout布局
    private ViewPager mViewPager;
    private VarietyVpAdapter vpAdapter;
    private List<BaseFragment> layouts = null; //Viewpage中的Fragment布局

    private String contentId; //栏目id
    private int viewType; // 页面布局类型：1099 视频列表布局
    private Bundle bundle;

    private NavigationInfoBean mResultBean = null; //列表所有数据（item尺寸列表、item列表数据）

    /**
     * 加载View
     **/
    private final int LODING_DATE_NAVCONTENT = 600;
    private final int LODING_ERROR_500 = 500;

    private final int VIEWTYPE = 1099; //小视屏列表布局

    public static HomeThreeItemFragment newInstance(Bundle bundle) {
        HomeThreeItemFragment fragment = new HomeThreeItemFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.home_three_item_fragment, container, false);
        homeActivity = (HomeActivity) getActivity();
        //获取参数
        Bundle bundle = getArguments();
        epgId = (String) bundle.get(Constant.CHANNEL_EPGID);
        navId = (String) bundle.get(Constant.CHANNEL_ID);
        initView();
        return root;
    }

    /**
     * 初始化View
     */
    private void initView() {
        //ViewPager布局
        mViewPager = (ViewPager) root.findViewById(R.id.vp_three_item_viewpager);
        getHomeNavContent();
    }

    @Override
    public void setData(Bundle bundle) {
        super.setData(bundle);
    }

    /**
     * 获取导航下内容数据
     */
    private void getHomeNavContent() {
        if (!NetWorkUtils.isNetworkAvailable(getActivity())) {
            //当前网络不可用
            Lg.i(TAG, "当前网络异常，请重试连接!");
            mHandler.sendEmptyMessage(LODING_ERROR_500);
            return;
        }

        HttpRequest.getInstance().excute("getHomeNavContent", new Object[]{App.epgUrl, epgId,
                navId, CacheConfig.cache_no_cache, new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                Lg.i(TAG, "玩票-页面栏目数据：" + response);
                handleLoadNavContentSuccessResponse(response);
            }

            @Override
            public void onError(String error) {
                Lg.i(TAG, "玩票-页面栏目数据：getHomeNavContent : error--> " + error);
                handleErrorResponse();
            }
        }});
    }

    /**
     * 获取导航内容（栏目列表）数据成功
     *
     * @param response
     */
    private void handleLoadNavContentSuccessResponse(String response) {
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
            mResultBean = navigationInfoResultBean.getData();
            if (mResultBean != null && mResultBean.getContent() != null && mResultBean.getContent().size() > 0
                    && mResultBean.getContent().get(0) != null && mResultBean.getContent().get(0).getIndexContents() != null
                    && mResultBean.getContent().get(0).getIndexContents().size() > 0) {
                if (mResultBean.getContent().get(0).getIndexContents().get(0) != null) {
                    viewType = mResultBean.getContent().get(0).getIndexContents().get(0).getViewtype();
                }
                contentId = mResultBean.getContent().get(0).getIndexContents().get(0).getContentId();
                mHandler.sendEmptyMessage(LODING_DATE_NAVCONTENT);
            } else {
                handleErrorResponse();
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
     * 根据页面类型显示不同的布局
     */
    private void getShowViewType() {
        try {
            if (layouts == null || layouts.size() == 0) {
                layouts = new ArrayList<BaseFragment>();
            }

            if (bundle == null) {
                bundle = new Bundle();
            }

            if (viewType == VIEWTYPE) {
                bundle.putString(Constant.CHANNEL_EPGID, epgId);
                bundle.putString(Constant.BUNDLE_CONTENTID, contentId);
                layouts.add(HomeThreeOtherFragment.newInstance(homeActivity, bundle));
            } else {
                bundle.putSerializable(Constant.HOME_THREE_ITEM_RESULTBEAN, mResultBean);
                layouts.add(HomeThreeFirstFragment.newInstance(bundle));
            }

            if (vpAdapter == null) {
                vpAdapter = new VarietyVpAdapter(
                        getChildFragmentManager(), layouts);
                mViewPager.setOffscreenPageLimit(1);
                mViewPager.setAdapter(vpAdapter);
            } else {
                vpAdapter.setData(layouts);
            }
            mViewPager.setCurrentItem(0);
        }catch (Exception e){

        }
    }

    /**
     * Handler处理
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LODING_DATE_NAVCONTENT:
                    getShowViewType();
                    break;

                case LODING_ERROR_500:

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

        public VarietyVpAdapter(FragmentManager fm, List<BaseFragment> mLayouts) {
            super(fm);
            this.layouts = mLayouts;
        }

        public void setData(List<BaseFragment> mLayouts) {
            this.layouts = mLayouts;
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
            return null;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public void getOnAttachedToWindow() {
        if (layouts != null && layouts.size() > 0 && viewType == VIEWTYPE) {
            ((HomeThreeOtherFragment) layouts.get(0)).getOnAttachedToWindow();
        }
    }

    public void getOnDetachedFromWindow() {
        if (layouts != null && layouts.size() > 0 && viewType == VIEWTYPE) {
            ((HomeThreeOtherFragment) layouts.get(0)).getOnDetachedFromWindow();
        }
    }

    public void getOnConfigurationChanged(Configuration newConfig) {
        if (layouts != null && layouts.size() > 0 && viewType == VIEWTYPE) {
            ((HomeThreeOtherFragment) layouts.get(0)).getOnConfigurationChanged(newConfig);
        }
    }

    /**
     * 返回按键处理
     *
     * @return
     */
    public boolean getOnKeyDown() {
        if (layouts != null && layouts.size() > 0 && viewType == VIEWTYPE) {
            return ((HomeThreeOtherFragment) layouts.get(0)).getOnKeyDown();
        }
        return false;
    }

    /**
     * 返回按键处理
     *
     * @return
     */
    public void getCompletion(boolean flag) {
        if (layouts != null && layouts.size() > 0 && viewType == VIEWTYPE) {
            ((HomeThreeOtherFragment) layouts.get(0)).getCompletion(flag);
        }
    }
}
