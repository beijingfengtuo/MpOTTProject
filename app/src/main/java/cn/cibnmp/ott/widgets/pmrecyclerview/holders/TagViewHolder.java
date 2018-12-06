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

public class TagViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout tag_relativelayout;
    public View tag_item_view;
    public TextView name1, name2, name3, name4;

    public TagViewHolder(View itemView) {
        super(itemView);
        tag_item_view = itemView;
        name1 = (TextView) tag_item_view.findViewById(R.id.home_one_item_tag_texttag_1);
        name2 = (TextView) tag_item_view.findViewById(R.id.home_one_item_tag_texttag_2);
        name3 = (TextView) tag_item_view.findViewById(R.id.home_one_item_tag_texttag_3);
        name4 = (TextView) tag_item_view.findViewById(R.id.home_one_item_tag_texttag_4);
        tag_relativelayout = (RelativeLayout)itemView.findViewById(R.id.tag_relativelayout);
    }

    public TagViewHolder(ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false));

    }

}
