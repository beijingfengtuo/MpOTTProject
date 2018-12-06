package cn.cibnmp.ott.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.bean.NavigationItemBean;
import cn.cibnmp.ott.utils.img.ImageFetcher;

/**
 * Created by yangwenwu on 18/1/15.
 * 频道管理下面的GridViewAdapter
 */

public class CannelItemsHideAdapter extends BaseAdapter {

    private List<NavigationItemBean> listener;
    private Context mContext;

    public CannelItemsHideAdapter(ChannelManagementActivity channelManagementActivity, List<NavigationItemBean> itemsHideList) {
        this.mContext = channelManagementActivity;
        this.listener = itemsHideList;
    }

    @Override
    public int getCount() {
        return listener.size();
    }

    @Override
    public Object getItem(int position) {
        return listener.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CannelItemsHideAdapter.ViewHolderMove viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.cannelhideltem, parent, false);
            viewHolder = new CannelItemsHideAdapter.ViewHolderMove();
            viewHolder.move_img = (ImageView) convertView.findViewById(R.id.channel_item_image);
            viewHolder.move_text = (TextView) convertView.findViewById(R.id.channel_item_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CannelItemsHideAdapter.ViewHolderMove) convertView.getTag();
        }
        ImageFetcher.getInstance().loadImage(listener.get(position).getImgUrl(), viewHolder.move_img, R.mipmap.defaultpic);
        viewHolder.move_text.setText(listener.get(position).getName());
        return convertView;
    }

    public void setData(List<NavigationItemBean> data) {
        this.listener = data;
        notifyDataSetChanged();
    }

    class ViewHolderMove {
        ImageView move_img;
        TextView move_text;
    }
}
