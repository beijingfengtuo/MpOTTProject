package cn.cibnmp.ott.ui.detail.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.ui.detail.bean.DetailInfoBean;

/**
 * Created by axl on 2018/1/25.
 */

public class EpisodeTvMaxAdapter extends BaseAdapter {

    private Context context;

    //String[] a = new String[] { "1", "2", "3", "4", "5", "6", "7", "1", "2", "3", "4", "5", "6", "7" };

    Holder holder;

    private DetailInfoBean bean;

    private int sid ;

    public EpisodeTvMaxAdapter(Context context,DetailInfoBean bean,int sid) {
        this.context = context;
        this.bean = bean;
        this.sid=sid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    @Override
    public int getCount() {
        if(bean.getChild()!=null){
            return bean.getChild().size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(bean.getChild()!=null){
            return bean.getChild().get(position).getSid();
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
            convertView = View.inflate(context, R.layout.detail_episode_tv_item, null);
            holder.tv = (TextView) convertView.findViewById(R.id.episode_tv);

            convertView.setTag(holder);


        } else {
            holder = (Holder) convertView.getTag();
        }
        if(bean.getChild()!=null){
            if(sid==bean.getChild().get(position).getSid()){
                holder.tv.setTextColor(context.getResources().getColor(R.color.white));
                holder.tv.setBackgroundResource(R.drawable.anthology_press);
            }else{
                holder.tv.setTextColor(context.getResources().getColor(R.color.colore_episode2));
                holder.tv.setBackgroundResource(R.drawable.anthology);
            }
            holder.tv.setText(bean.getChild().get(position).getSid() + "");
        }

        return convertView;
    }

    class Holder {
        TextView tv;
    }

}
