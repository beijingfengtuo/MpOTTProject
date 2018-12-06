package cn.cibnmp.ott.ui.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.bean.ContentBean;
import cn.cibnmp.ott.utils.img.ImageFetcher;

/**
 * Created by cibn-lyc on 2018/1/18.
 */

public class VipPackageAdapter extends BaseAdapter {

    private Context context;
    // 数据集合
    private List<ContentBean> indexContents = null;

    public VipPackageAdapter(Context context) {
        this.context = context;

    }

    public void setData(List<ContentBean> indexContents) {
        this.indexContents = indexContents;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return indexContents != null ? indexContents.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return indexContents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderMove viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.vip_package_item, parent, false);
            viewHolder = new ViewHolderMove(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderMove) convertView.getTag();
        }

        ImageFetcher.getInstance().loodingImage(indexContents.get(position).getImg(),
                viewHolder.img, R.color.black_hui8);

        return convertView;
    }

    class ViewHolderMove {
        ImageView img;

        public ViewHolderMove(View view) {
            img = (ImageView) view.findViewById(R.id.img_vip_package_item);
        }
    }
}
