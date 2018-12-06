package cn.cibnmp.ott.widgets.pmrecyclerview.holders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.cibnhp.grand.R;

/**
 * 首页、玩票等页面的底部Foot布局
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2018/1/12
 */
public class FootViewHolder extends RecyclerView.ViewHolder {

    public FootViewHolder(View itemView) {
        super(itemView);
    }

    public FootViewHolder(ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(R.layout.foot_two, parent, false));

    }
}
