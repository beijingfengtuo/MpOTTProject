package cn.cibnmp.ott.utils;

import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

import cn.cibnhp.grand.R;

/**
 * Created by yanjing on 2017/10/23.
 */

public class SetIndicator {


    public static void setIndicator(final TabLayout tabs, final int leftDip, final int rightDip) {
        setIndicator(tabs, leftDip, rightDip, 0, null);
    }

    public static void setIndicator(final TabLayout tabs, final int leftDip, final int rightDip, final int isWidth, final SetIndicatorInterface setIndicatorInterface) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabs.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabs.getChildAt(0);
                    int left = DisplayUtils.getValue(leftDip);
                    int right = DisplayUtils.getValue(rightDip);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);
                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);
                        TextView mTextView = (TextView) mTextViewField.get(tabView);
                        tabView.setPadding(0, 0, 0, 0);
                        tabView.setBackgroundResource(R.color.transparent);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //第二次调用的时候重新计算宽
                        int leng = mTextView.getText().length();
                        if (isWidth == 0 && setIndicatorInterface != null && width > 0) {
                            setIndicatorInterface.getsIndicator(width / leng);
                        } else if (isWidth > 0 && isWidth != width / leng) {
                            width = isWidth * leng;
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = left;
                        params.rightMargin = right;
                        tabView.setLayoutParams(params);
                        tabView.invalidate();
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface SetIndicatorInterface {
        void getsIndicator(int width);
    }
}
