package cn.cibnmp.ott.ui.detail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.ui.detail.bean.SidEvent;
import de.greenrobot.event.EventBus;

/**
 * Created by axl on 2018/1/25.
 */

public class EpisodeTvAdapter extends RecyclerView.Adapter<EpisodeTvAdapter.ViewHolder> {

    private Context context;

    private int size;
    private int sid = 1;

    public EpisodeTvAdapter(Context context) {
        this.context = context;
    }

    public void addData(int s) {
        size = s;
        notifyDataSetChanged();
    }

    public void updateEpisodeItem(int s) {
        sid = s;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return size;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        holder.tv.setText((pos + 1) + "");
        holder.tv.setTag(pos);
        if (pos == sid - 1) {
            holder.tv.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv.setBackgroundResource(R.drawable.anthology_press);
        } else {
            holder.tv.setTextColor(context.getResources().getColor(R.color.colore_episode2));
            holder.tv.setBackgroundResource(R.drawable.anthology);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int arg1) {
        View view = View.inflate(group.getContext(), R.layout.detail_episode_tv_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.episode_tv);

            tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    SidEvent se = new SidEvent();
                    sid = (Integer) v.getTag() + 1;
                    se.setSid(sid);
                    EventBus.getDefault().post(se);
                    notifyDataSetChanged();
                }
            });
        }

    }
}