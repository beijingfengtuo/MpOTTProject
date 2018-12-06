package cn.cibnmp.ott.widgets.pmrecyclerview.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.cibnhp.grand.R;

/**
 * Created by yangwenwu on 18/1/20.
 */

public class CommonItemHolder extends RecyclerView.ViewHolder {

    public ImageView img;
    public TextView text;

    public CommonItemHolder(Context context, View itemView) {
        super(itemView);
        img = (ImageView) itemView.findViewById(R.id.img);
        text = (TextView) itemView.findViewById(R.id.text);
    }

    public CommonItemHolder(ViewGroup parent) {
        this(parent.getContext(),
                LayoutInflater.from(parent.getContext()).inflate(R.layout.common_item_holder, parent, false));


    }

}
