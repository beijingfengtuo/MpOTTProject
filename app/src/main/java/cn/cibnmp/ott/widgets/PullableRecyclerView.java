package cn.cibnmp.ott.widgets;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import cn.cibnmp.ott.library.pullable.Pullable;

/**
 * PullToRefreshLayout + RecyclerView
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2018/4/9
 */
public class PullableRecyclerView extends RecyclerView implements Pullable {
    public PullableRecyclerView(Context context) {
        super(context);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean canPullDown() {
        if (getChildCount() == 0) {
            return true;
        } else if (getChildAt(0).getTop() >= 0) {
            if (getLayoutManager() instanceof LinearLayoutManager) {
                int firstVisibleItem = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                if (firstVisibleItem == 0) {
                    return true;
                }
            } else if (getLayoutManager() instanceof GridLayoutManager) {
                int firstVisibleItem = ((GridLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                if (firstVisibleItem == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canPullUp() {
        if (getChildCount() == 0) {
            // 没有item的时候也可以上拉加载
            return true;
        } else if (getLastVisiblePosition() == (getLayoutManager().getItemCount() - 1)) {
            View lastChildView = getLayoutManager().getChildAt(getLayoutManager().getChildCount() - 1);
            int lastChildBottom = lastChildView.getBottom();
            //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
            int recyclerBottom = getBottom() - getPaddingBottom();
//            Lg.i("bottom", lastChildBottom + "------------> " + recyclerBottom);
            if (lastChildBottom <= recyclerBottom) {
                return true;
            }
        }
        return false;
    }

    public int getLastVisiblePosition() {
        final int childCount = getChildCount();
        if (childCount == 0)
            return 0;
        else
            return getChildLayoutPosition(getChildAt(childCount - 1));
    }
}
