package cn.cibnmp.ott.widgets.pmrecyclerview.holders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cibnhp.grand.R;


/**
 * Created by wanqi on 2017/2/28.
 */

public class CommonViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout recyclerview_relativelayout;
    public View mView;
    public ImageView recyclerview_imagview;
    public TextView recyclerview_text;

    public CommonViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        recyclerview_imagview = (ImageView) mView.findViewById(R.id.recyclerview_imagview);
        recyclerview_text = (TextView) mView.findViewById(R.id.recyclerview_text);
        recyclerview_relativelayout = (RelativeLayout)mView.findViewById(R.id.recyclerview_relativelayout);
    }

    public CommonViewHolder(ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_horizontal_item, parent, false));

    }

}
