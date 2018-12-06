package cn.cibnmp.ott.ui.detail.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.ui.detail.bean.DetailInfoBean;

/**
 * Created by axl on 2018/1/25.
 */

public class EpisodeShowMaxAdapter extends BaseAdapter {

    private Context context;

    Holder holder;

    private DetailInfoBean bean;

    private int sid;

    public EpisodeShowMaxAdapter(Context context, DetailInfoBean bean, int sid) {
        this.context = context;
        this.bean = bean;
        this.sid = sid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    @Override
    public int getCount() {
        if (bean.getChild() != null) {
            return bean.getChild().size() ;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (bean.getChild() != null) {
            return bean.getChild().get(position ).getSid();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(context,
                    R.layout.detail_episode_show_max_item, null);
            holder.tv1 = (TextView) convertView.findViewById(R.id.episode_tv1);
            holder.show_ll =  (LinearLayout) convertView
                    .findViewById(R.id.show_ll);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.tv1.setText(bean.getChild().get(position ).getSname());
        if (sid == bean.getChild().get(position).getSid()) {
            holder.tv1.setTextColor(Color.WHITE);
            holder.show_ll.setBackgroundColor(context.getResources().getColor(R.color.colore_episode1));
        } else {
            holder.tv1.setTextColor(context.getResources().getColor(R.color.colore_episode2));
            holder.show_ll.setBackgroundColor(Color.WHITE);
        }
//		if (sid == bean.getEpisodelist().get(position + 1).getSid()) {
//			holder.tv1.setTextColor(Color.WHITE);
//			holder.show_ll.setBackgroundResource(R.drawable.anthology_press);
//		} else {
//			holder.tv1.setTextColor(context.getResources().getColor(R.color.colore_episode2));
//			holder.show_ll.setBackgroundResource(R.drawable.anthology);
//		}

        return convertView;
    }

    class Holder {
        TextView tv1;
        LinearLayout show_ll;
    }

}