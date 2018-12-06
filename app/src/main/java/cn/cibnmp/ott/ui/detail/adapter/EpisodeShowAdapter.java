package cn.cibnmp.ott.ui.detail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.ui.detail.bean.DetailInfoBean;
import cn.cibnmp.ott.ui.detail.bean.SidEvent;
import de.greenrobot.event.EventBus;

/**
 * Created by axl on 2018/1/25.
 */

public class EpisodeShowAdapter extends RecyclerView.Adapter<EpisodeShowAdapter.ViewHolder> {
    private Context context;

    private DetailInfoBean datas;
    private int isPos;

    public EpisodeShowAdapter(Context context) {
        this.context=context;
    }

    public void addData(DetailInfoBean datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void updateEpisodeItem(int sid) {
        if (datas != null && datas.getChild() != null) {
            for (int i = 0; i < datas.getChild().size(); i++) {
                if (sid == datas.getChild().get(i).getSid()) {
//					this.sid = i;
                    notifyItemChanged(isPos);
                    isPos=i - 1;
                    notifyItemChanged(isPos);
                    break;
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        if (datas == null || datas.getChild() == null || datas.getChild().size() <= 1) {
            return 0;
        } else {
            return datas.getChild().size() - 1;
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        holder.tv1.setText(datas.getChild().get(pos + 1).getSname());
        holder.lay.setTag(pos);
        if (pos == isPos) {
            holder.tv1.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv1.setBackgroundResource(R.drawable.anthology_press);
        } else {
            holder.tv1.setTextColor(context.getResources().getColor(R.color.colore_episode2));
            holder.tv1.setBackgroundResource(R.drawable.anthology);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int arg1) {
        View view = View.inflate(group.getContext(), R.layout.detail_episode_show_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lay;
        TextView tv1;

        public ViewHolder(View itemView) {
            super(itemView);
            lay = (LinearLayout) itemView.findViewById(R.id.episode_item_lay);
            tv1 = (TextView) itemView.findViewById(R.id.episode_tv1);

            lay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    SidEvent se = new SidEvent();
                    notifyItemChanged(isPos);
                    isPos = (Integer) v.getTag();
                    se.setSid((int) datas.getChild().get(isPos + 1).getSid());
                    EventBus.getDefault().post(se);
                    notifyItemChanged(isPos);
                }
            });

        }

    }

}