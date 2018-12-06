package cn.cibnmp.ott.utils;

import android.support.design.widget.TabLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by yanjing on 2017/10/23.
 */

public class ScreeningMenuSetIndicator {

    /**
     * 设置Tab 的宽度
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public static void setIndicator(final TabLayout tabs, final int leftDip, final int rightDip) {
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

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width + left + right;
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

    /**
     * TODO zxy 设置Tab 的宽度
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public static void setTabIndicator(final TabLayout tabs, final int leftDip, final int rightDip) {
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

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        int hight = 0;
                        width = mTextView.getWidth();
                        hight = mTextView.getHeight();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width + left;
                        params.height = hight + left - 20;
                        params.leftMargin = left / 2;
                        params.rightMargin = right / 2;
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

    /**
     * TODO zxy 设置Tab 选中的样式
     *
     * @param tabs
     * @param position
     * @param backgroundID
     */
    public static void setIndicatorStyle(final TabLayout tabs, final int position, final int backgroundID, final int textColorID, final boolean isSelected) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabs.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabs.getChildAt(0);
                    View tabView = mTabStrip.getChildAt(position);
                    //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                    Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                    mTextViewField.setAccessible(true);
                    TextView mTextView = (TextView) mTextViewField.get(tabView);
//                    //焦点处理
//                    if (isSelected && position == 0) {
//                        mTextView.setTextColor(textColorID);
//                    } else if (!isSelected && position == 0) {
//                        mTextView.setTextColor(textColorID);
//                    }

                    if (isSelected) {
                        if (position == 0) {
                            mTextView.setTextColor(textColorID);
                        } else {
                            mTextView.setTextColor(textColorID);
                        }
                    } else {
                        if (position == 0) {
                            mTextView.setTextColor(textColorID);
                        } else {
                            mTextView.setTextColor(textColorID);
                        }
                    }

                    tabView.setBackgroundResource(backgroundID);
                    tabView.invalidate();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
