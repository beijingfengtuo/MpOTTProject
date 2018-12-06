package cn.cibnmp.ott.lib;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import cn.cibnmp.ott.utils.DisplayUtils;

/**
 * Created by wanqi on 2017/8/29.
 */

public class CustomGlideDrawableImageViewTarget extends GlideDrawableImageViewTarget {

    private View root;

    public CustomGlideDrawableImageViewTarget(ImageView view) {
        super(view);
    }

    public CustomGlideDrawableImageViewTarget(View root, ImageView view) {
        super(view);
        this.root = root;
    }

    public CustomGlideDrawableImageViewTarget(ImageView view, int maxLoopCount) {
        super(view, maxLoopCount);
    }

    @Override
    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
        if (view != null) {
            if (root != null) {
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) root.getLayoutParams();
                lp.width = DisplayUtils.getValue(60 * resource.getMinimumWidth() / resource.getMinimumHeight());
                root.setLayoutParams(lp);
            } else {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = DisplayUtils.getValue(resource.getMinimumWidth());
                layoutParams.height = DisplayUtils.getValue(resource.getMinimumHeight());
                view.setLayoutParams(layoutParams);
            }
        }
        super.onResourceReady(resource, animation);
    }
}
