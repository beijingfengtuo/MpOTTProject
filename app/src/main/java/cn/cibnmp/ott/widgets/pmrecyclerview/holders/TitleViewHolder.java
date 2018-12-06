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

public class TitleViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout rl_title, fu_title_relativelayout;
    public TextView title, fu_title;
    public ImageView img1;

    public TitleViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        img1 = (ImageView) itemView.findViewById(R.id.img1);
        rl_title = (RelativeLayout) itemView.findViewById(R.id.rl_title);
        fu_title = (TextView) itemView.findViewById(R.id.fu_title);
        fu_title_relativelayout = (RelativeLayout) itemView.findViewById(R.id.fu_title_relativelayout);
    }

    public TitleViewHolder(ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(R.layout.title_item, parent, false));
    }

}
