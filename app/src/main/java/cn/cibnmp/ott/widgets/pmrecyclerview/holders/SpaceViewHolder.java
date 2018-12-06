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
 * Created by wanqi on 2017/3/22.
 */

public class SpaceViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout space_relativelayout;
    public View space_view;
    public ImageView space_imagview;
    public TextView space_textviewtime, space_textview, space_textview1;

    public SpaceViewHolder(View itemView) {
        super(itemView);
        space_view = itemView;
        space_imagview = (ImageView) itemView.findViewById(R.id.space_imagview);
        space_textviewtime = (TextView) itemView.findViewById(R.id.space_textviewtime);
        space_textview = (TextView) itemView.findViewById(R.id.space_textview);
        space_textview1 = (TextView) itemView.findViewById(R.id.space_textview1);
        space_relativelayout = (RelativeLayout)itemView.findViewById(R.id.space_relativelayout);
    }

    public SpaceViewHolder(ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(R.layout.space_layout, parent, false));
    }
}
