package cn.cibnmp.ott.ui.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.bean.VipProductPackageBean.VipProductInfoBean;

/**
 * Created by cibn-lyc on 2018/1/31.
 */

public class InfoEnterProductPackcgeAdapter extends BaseAdapter {

    private Context context;
    private List<VipProductInfoBean> productList;

    public InfoEnterProductPackcgeAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<VipProductInfoBean> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return productList == null ? 0 : productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
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
                    R.layout.info_enter_product_item, parent, false);
            viewHolder = new ViewHolderMove(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderMove) convertView.getTag();
        }
        if (position % 4 == 0) {
            viewHolder.img_info_enter_product_bj.setImageResource(R.mipmap.img_info_enter_product_bj1);
        } else if (position % 4 == 1) {
            viewHolder.img_info_enter_product_bj.setImageResource(R.mipmap.img_info_enter_product_bj2);
        } else if (position % 4 == 2) {
            viewHolder.img_info_enter_product_bj.setImageResource(R.mipmap.img_info_enter_product_bj3);
        } else if (position % 4 == 3) {
            viewHolder.img_info_enter_product_bj.setImageResource(R.mipmap.img_info_enter_product_bj4);
        }
        viewHolder.tv_info_enter_product_name.setText(productList.get(position).getProductName());
        viewHolder.tv_info_enter_product_introdesc.setText(productList.get(position).getProductDesc());
        return convertView;
    }

    public class ViewHolderMove {
        ImageView img_info_enter_product_bj;
        TextView tv_info_enter_product_name;
        TextView tv_info_enter_product_introdesc;

        public ViewHolderMove(View view) {
            img_info_enter_product_bj = (ImageView) view.findViewById(R.id.img_info_enter_product_bj);
            tv_info_enter_product_name = (TextView) view.findViewById(R.id.tv_info_enter_product_name);
            tv_info_enter_product_introdesc = (TextView) view.findViewById(R.id.tv_info_enter_product_introdesc);
        }
    }
}
