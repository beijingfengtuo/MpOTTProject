package cn.cibnmp.ott.widgets.pmrecyclerview.holders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.cibnhp.grand.R;


/**
 * Created by wanqi on 2017/2/28.
 */

public class PersonViewHolder extends RecyclerView.ViewHolder {

    public ImageView imgview;
    public TextView textView;

    public PersonViewHolder(View itemView) {
        super(itemView);
        imgview = (ImageView) itemView.findViewById(R.id.person_imageview1);
        textView = (TextView) itemView.findViewById(R.id.person_text1);

    }

    public PersonViewHolder(ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, parent, false));
    }
}
