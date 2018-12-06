package cn.cibnmp.ott.widgets;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

/**
 * Created by zxyggdf on 2018/4/13.
 */

public class ScreeTabLayout extends TabLayout {
    private int tags = -1;
    public ScreeTabLayout(Context context) {
        super(context);
        initListener();
    }

    public ScreeTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initListener();
    }

    public ScreeTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initListener();
    }

    private void initListener() {

    }

    public int getTags() {
        return tags;
    }

    public void setTags(int tags) {
        this.tags = tags;
    }

    public void setOnTabSelectedListener(final TabSelectedListener listener) {
        this.setOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                if (listener != null) {
                    listener.onTabSelected(ScreeTabLayout.this, tab);
                }
            }

            @Override
            public void onTabUnselected(Tab tab) {
                if (listener != null) {
                    listener.onTabUnselected(ScreeTabLayout.this, tab);
                }
            }

            @Override
            public void onTabReselected(Tab tab) {
                if (listener != null) {
                    listener.onTabReselected(ScreeTabLayout.this, tab);
                }
            }
        });
    }

    public interface TabSelectedListener {
        public void onTabSelected(ScreeTabLayout tabLayout, Tab tab);
        public void onTabUnselected(ScreeTabLayout tabLayout, Tab tab);
        public void onTabReselected(ScreeTabLayout tabLayout, Tab tab);
    }
}
