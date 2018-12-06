package cn.cibnmp.ott.ui.search;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yangwenwu on 18/2/5.
 */

public class HomeSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public HomeSpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if(parent.getChildPosition(view) != -1) {
            outRect.top = space / 2;
            outRect.bottom = space / 2;
        }
    }
}
