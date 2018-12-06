package cn.cibnmp.ott.widgets.pmrecyclerview.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.utils.DisplayUtils;

/**
 * Created by wanqi on 2017/3/10.
 */

public class CommonListView extends RecyclerView.ViewHolder {

    public LinearLayout person_linearlayout;
    public View person_item;
    public ImageView person_imageview1, person_imageview2, person_imageview3;
    public TextView person_text1, person_text2, person_text3;

    public CommonListView(View itemView) {
        super(itemView);
        person_item = itemView;
        person_imageview1 = (ImageView) itemView.findViewById(R.id.person_imageview1);
        person_imageview2 = (ImageView) itemView.findViewById(R.id.person_imageview2);
        person_imageview3 = (ImageView) itemView.findViewById(R.id.person_imageview3);
        person_text1 = (TextView) itemView.findViewById(R.id.person_text1);
        person_text2 = (TextView) itemView.findViewById(R.id.person_text2);
        person_text3 = (TextView) itemView.findViewById(R.id.person_text3);
        person_linearlayout = (LinearLayout)itemView.findViewById(R.id.person_linearlayout);

    }

    public CommonListView(ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, parent, false));
    }
}