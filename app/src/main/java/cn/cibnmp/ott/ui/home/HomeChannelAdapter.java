package cn.cibnmp.ott.ui.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.cibnmp.ott.ui.base.BaseFragment;

/**
 * Created by yangwenwu on 17/12/25.
 * 首页频道列表页Adapte
 */

public class HomeChannelAdapter extends FragmentStatePagerAdapter {

    //这个是viewpager的填充视图
    private List<BaseFragment> mFragment;
    //这个是table导航条里面的内容填充
    private List<String> mTabstrs;

    public HomeChannelAdapter(FragmentManager fm, List<BaseFragment> views, List<String> tabstrs) {
        super(fm);
        this.mFragment = views;
        this.mTabstrs = tabstrs;
    }

    @Override
    public Fragment getItem(int position) {
        if (mFragment == null || mFragment.size() == 0) {
            return null;
        }
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        if (mFragment == null || mFragment.size() == 0) {
            return 0;
        }
        return mFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabstrs.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setData(List<BaseFragment> mViews, List<String> mTablist) {
        this.mFragment = mViews;
        this.mTabstrs = mTablist;
        notifyDataSetChanged();
    }
}
