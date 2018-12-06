package cn.cibnmp.ott;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.ui.HomeActivity;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.utils.DisplayUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.Utils;

/**
 * Created by yanwenwu on 2018/3/1.
 */

public class WelcomeActivity extends BaseActivity {

    private ViewPager mViewPager; //引导页面ViewPager
    private PagerAdapter mWelcomeAdapter; //引导页面Adapter
    private int[] mImageView = {R.mipmap.welcome_bg1, R.mipmap.welcome_bg2, R.mipmap.welcome_bg3}; //引导页默认图片
    private List<ImageView> mImageViewList; //图片集合
    private ImageView mImageview; //开机图片
    private ImageView mCustomImageView; //小圆点默认图片
    private LinearLayout mLinearLayout; //添加小圆点的LinearLayout容器
    private List<ImageView> mDotImageViewList; //引导页下面的4个小圆点
    private Button mWeicomeAction; //跳转首页按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**隐藏标题栏以及状态栏**/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题**/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mImageview = (ImageView) findViewById(R.id.imageview);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        mWeicomeAction = (Button) findViewById(R.id.weicomeaction);
        //判断是否是第一次进入
        SharedPreferences sharedPreferences = getSharedPreferences("data" + Utils.getVersionName(), MODE_PRIVATE);
        boolean isFirstIn = sharedPreferences.getBoolean("isFirstIn", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //若为true，则是第一次进入
        if (isFirstIn) {
            editor.putBoolean("isFirstIn", false);
            goMain();
        } else {
            goGuide();
        }
        editor.commit();
    }

    //第一次进入
    private void goMain() {
        mViewPager.setVisibility(View.INVISIBLE);
        mImageview.setVisibility(View.VISIBLE);
        handler.sendEmptyMessageDelayed(0, 3000);
    }

    //第二次进入
    private void goGuide() {
        mImageview.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.INVISIBLE);
        mLinearLayout.setVisibility(View.INVISIBLE);
        mWeicomeAction.setVisibility(View.INVISIBLE);
        handler.sendEmptyMessageDelayed(1, 3000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    initViewPage();
                    break;
                case 1:
                    Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    private void initViewPage() {
        mImageview.setVisibility(View.INVISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        mLinearLayout.setVisibility(View.VISIBLE);
        mImageViewList = new ArrayList<>();
        mDotImageViewList = new ArrayList<>();

        //引导页面默认图片
        for (int i = 0; i < mImageView.length; i++) {
            ImageView imageView = new ImageView(WelcomeActivity.this);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(mImageView[i]);
            mImageViewList.add(imageView);
        }

        //引导页下面小圆点
        for (int i = 0; i < mImageView.length; i++) {
            mCustomImageView = new ImageView(WelcomeActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DisplayUtils.getValue(24), DisplayUtils.getValue(14));
            params.setMargins(DisplayUtils.getValue(10), DisplayUtils.getValue(0),
                    DisplayUtils.getValue(10), DisplayUtils.getValue(0));
            mCustomImageView.setLayoutParams(params);
            if (i == 0) {
                mCustomImageView.setImageResource(R.mipmap.welcome_dot_select);
            } else {
                mCustomImageView.setImageResource(R.mipmap.welcome_dot_uncheck);
            }
            mDotImageViewList.add(mCustomImageView);
            mLinearLayout.addView(mCustomImageView);
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                //滑动圆点跟随
                for (int i = 0; i < mDotImageViewList.size(); i++) {
                    mDotImageViewList.get(i).setImageResource(R.mipmap.welcome_dot_uncheck);
                }
                mDotImageViewList.get(position).setImageResource(R.mipmap.welcome_dot_select);

                //显示隐藏跳转首页按钮
                if (position == 2) {
                    mWeicomeAction.setVisibility(View.VISIBLE);
                } else {
                    mWeicomeAction.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

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

        //跳转首页按钮监听
        mWeicomeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) {
            getResources();
        }
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

}