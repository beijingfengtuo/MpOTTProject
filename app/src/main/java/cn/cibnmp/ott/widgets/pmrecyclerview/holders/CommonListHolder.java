package cn.cibnmp.ott.widgets.pmrecyclerview.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cibnhp.grand.R;


/**
 * Created by wanqi on 2017/3/10.
 */

public class CommonListHolder extends RecyclerView.ViewHolder {

    public RelativeLayout common_list_relativelayout;
    public View common_list_mView;
    public ImageView common_list_imagview;
    public TextView common_list_text, common_list_text1;

    public CommonListHolder(Context context, View itemView) {
        super(itemView);
        common_list_mView = itemView;
        common_list_imagview = (ImageView) itemView.findViewById(R.id.common_list_imagview);
        common_list_text = (TextView) itemView.findViewById(R.id.common_list_text);
        common_list_text1 = (TextView) itemView.findViewById(R.id.common_list_text1);
        common_list_relativelayout = (RelativeLayout)itemView.findViewById(R.id.common_list_relativelayout);
    }

    public CommonListHolder(ViewGroup parent) {
        this(parent.getContext(),
                LayoutInflater.from(parent.getContext()).inflate(R.layout.common_list_item, parent, false));
    }
}
