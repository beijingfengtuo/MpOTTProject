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
 * Created by yangwenwu on 18/1/20.
 */

public class CommonListItemHolder extends RecyclerView.ViewHolder {

    public RelativeLayout relativelayout;
    public View person_list_item;
    public ImageView imglistview;
    public TextView textlistView;

    public CommonListItemHolder(View itemView) {
        super(itemView);
        person_list_item = itemView;
        imglistview = (ImageView) itemView.findViewById(R.id.imaglistview);
        textlistView = (TextView) itemView.findViewById(R.id.listtext);
        relativelayout = (RelativeLayout)itemView.findViewById(R.id.relativelayout);

    }

    public CommonListItemHolder(ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(R.layout.person_list_item, parent, false));
    }
}
