package cn.cibnmp.ott.widgets.pmrecyclerview.holders;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.ta.utdid2.android.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.ActionParams;
import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.base.Action;
import cn.cibnmp.ott.ui.categoryList.HomeOnItemClickListener;
import cn.cibnmp.ott.utils.HomeJumpDetailUtils;
import cn.cibnmp.ott.utils.img.ImageFetcher;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.ScrollerViewPager;
import de.greenrobot.event.EventBus;

/**
 * Created by yangwenwu on 2017/3/8.
 * 首页布局ViewPager滑动和加载数据页面
 */

public class ViewPagerViewHolder extends RecyclerView.ViewHolder {
    public View viewPagerView;
    public ScrollerViewPager mViewPager;
    public LinearLayout mLinearLayout;
    public List<NavigationInfoItemBean> mSetData = new ArrayList<>();
    private int mCurDirection = View.FOCUS_RIGHT;
    public Handler mHandler = new Handler();
    private int mPrePos = 0;
    private Context mContext;
    private List<ImageView> mDots;
    private ImageView mCustomImageView;
    private int ScrollFangxiang = 2; //1左、2右
    private List<View> mImageViewList; //图片集合
    private PagerAdapter mWelcomeAdapter; //引导页面Adapter

    public ViewPagerViewHolder(final Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        viewPagerView = itemView;
        mViewPager = (ScrollerViewPager) viewPagerView.findViewById(R.id.view_pager);
        mViewPager.fixScrollSpeed();
        mLinearLayout = (LinearLayout) viewPagerView.findViewById(R.id.indicator);

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int currentx = (int) event.getRawX();
                if (currentx != 0) {
                    EventBus.getDefault().post(Constant.PULL_STOP);
                }
                return false;
            }
        });
    }

    //详情页跳转
    public void notifyData(List<NavigationInfoItemBean> d) {
        upViewPage(d, null);
    }

    //首页跳转
    public void notifyData(List<NavigationInfoItemBean> d, final HomeOnItemClickListener listener) {
        upViewPage(d, listener);
    }

    public void upViewPage(List<NavigationInfoItemBean> d, final HomeOnItemClickListener listener){
        addViewPageDot(d, listener);
        mSetData.clear();
        mSetData.addAll(d);
        mWelcomeAdapter = new PagerAdapter() {
            @Override//初始化适配器，将view加到container中
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mImageViewList.get(position);
                container.addView(view);
                return view;
            }

            @Override//将view从container中移除
            public void destroyItem(ViewGroup container, int position, Object object) {
                View view = mImageViewList.get(position);
                container.removeView(view);
            }

            @Override
            public int getCount() {
                return mImageViewList.size();
            }

            @Override//判断当前的view是我们需要的对象
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        };
        mViewPager.setAdapter(mWelcomeAdapter);
    }

    public void stopSwitch() {
        isStop = true;
        mHandler.removeCallbacks(runnable);
        mCurDirection = View.FOCUS_RIGHT;
    }

    //是否需要停止
    private boolean isStop = false;

    public void startSwitch() {
        isStop = false;
        mHandler.postDelayed(runnable, 5000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mHandler.removeCallbacks(runnable);
            if(ScrollFangxiang == 2) {
                if (mPrePos == mDots.size() - 1) {
                    ScrollFangxiang = 1;
                    mPrePos = mDots.size() - 2;

                } else {
                    mPrePos += 1;
                }
            } else if (ScrollFangxiang == 1) {
                if (mPrePos == 0) {
                    ScrollFangxiang = 2;
                    mPrePos = 1;
                } else {
                    mPrePos -= 1;
                }
            }
            mViewPager.setCurrentItem(mPrePos);
            if (!isStop) {
                mHandler.postDelayed(runnable, 5000);
            }
        }
    };

    public ViewPagerViewHolder(ViewGroup parent) {
        this(parent.getContext(),
                LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_layout, parent, false));
    }

    public void addViewPageDot(final List<NavigationInfoItemBean> ViewPageData, final HomeOnItemClickListener listener) {

        mDots = new ArrayList<>();
        mDots.clear();
        mLinearLayout.removeAllViews();
        mImageViewList = new ArrayList<View>();
        for (int i = 0; i < ViewPageData.size(); i++) {
            View view = LinearLayout.inflate(mContext, R.layout.spring_frag_layout, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.img);
            if (ViewPageData.get(i) == null || TextUtils.isEmpty(ViewPageData.get(i).getImg())) {
                Glide.with(mContext).load(R.mipmap.viewpager_bg).into(imageView);
            } else {
                ImageFetcher.getInstance().loodingImage(ViewPageData.get(i).getImg(), imageView, R.mipmap.viewpager_bg);
            }
            view.setTag(i);
            mImageViewList.add(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.BUNDLE_ACTION, ViewPageData.get((int) v.getTag()).getAction());
                    bundle.putSerializable(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN, ViewPageData.get((int) v.getTag()));
                    listener.onItemClickListener(bundle);
                }
            });

            mCustomImageView = new ImageView(mContext);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(34, 14);
            mCustomImageView.setLayoutParams(params);
            if (i == 0) {
                mCustomImageView.setImageResource(R.mipmap.home_viewpager_dot_select);
            } else {
                mCustomImageView.setImageResource(R.mipmap.home_viewpager_dot_uncheck);
            }
            if (ViewPageData.size() >1){
                mLinearLayout.addView(mCustomImageView);
            }
            mDots.add(mCustomImageView);
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPrePos = position;
                for (int i = 0; i < mDots.size(); i++){
                    mDots.get(i).setImageResource(R.mipmap.home_viewpager_dot_uncheck);
                }
                mDots.get(position).setImageResource(R.mipmap.home_viewpager_dot_select);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
