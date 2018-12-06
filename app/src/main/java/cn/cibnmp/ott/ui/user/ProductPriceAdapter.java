package cn.cibnmp.ott.ui.user;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.bean.VipProductPriceBean.VipProductPriceInfoBean.PriceListBean;
import cn.cibnmp.ott.utils.Utils;

/**
 * Created by cibn-lyc on 2018/1/31.
 */

public class ProductPriceAdapter extends BaseAdapter {

    private Context context;
    List<PriceListBean> pricesList;
    private DecimalFormat df = new DecimalFormat("0.00");
    private OnItemClick listener;

    public ProductPriceAdapter(Context context, OnItemClick listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setData(List<PriceListBean> prices) {
        this.pricesList = prices;
        if (pricesList == null) {
            return;
        }
        pricesList.get(0).setShowSelectBj(true);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pricesList == null ? 0 : pricesList.size();
    }

    @Override
    public Object getItem(int position) {
        return pricesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolderMove viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.select_package_price_item, parent, false);
            viewHolder = new ViewHolderMove(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderMove) convertView.getTag();
        }
        if (pricesList.get(position).isShowSelectBj()) {
            viewHolder.img_select_package_bj.setImageResource(R.mipmap.select_package);
        } else {
            viewHolder.img_select_package_bj.setImageResource(R.mipmap.unselect_package);
        }
//        if (position == 0) {
//            viewHolder.img_select_package_bj.setImageResource(R.mipmap.select_package);
//        }
        viewHolder.tv_select_package_name.setText(pricesList.get(position).getPriceName());
        Double price =  pricesList.get(position).getProductPrices() / 100.00;
        viewHolder.tv_select_package_price.setText(Utils.getInterString(context, R.string.money) + df.format(price));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(pricesList.get(position));
                    setSelectItemBj(position);
                }
            }
        });
        return convertView;
    }

    public class ViewHolderMove {
        ImageView img_select_package_bj;
        TextView tv_select_package_name;
        TextView tv_select_package_price;

        public ViewHolderMove(View view) {
            img_select_package_bj = (ImageView) view.findViewById(R.id.img_select_package_bj);
            tv_select_package_name = (TextView) view.findViewById(R.id.tv_select_package_name);
            tv_select_package_price = (TextView) view.findViewById(R.id.tv_select_package_price);
        }
    }

    public interface OnItemClick {
        public void onItemClick(PriceListBean priceListBean);
    }

    private void setSelectItemBj(int position) {
        if (pricesList == null) {
            return;
        }
        for (int i = 0; i < pricesList.size(); i++) {
            if (i == position) {
                pricesList.get(i).setShowSelectBj(true);
            } else {
                pricesList.get(i).setShowSelectBj(false);
            }
        }
        notifyDataSetChanged();
    }
}
