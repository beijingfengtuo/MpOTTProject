package cn.cibnmp.ott.ui.user;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.bean.UserRecordInfoBean;
import cn.cibnmp.ott.utils.DateUtil;
import cn.cibnmp.ott.utils.Utils;

import static cn.cibnmp.ott.library.pullable.PullToRefreshLayout.TAG;

/**
 * Created by cibn-lyc on 2018/1/25.
 */

public class UserRecordAdapter extends BaseAdapter {

    private Context context;
    private List<UserRecordInfoBean> list;
    private DecimalFormat df = new DecimalFormat("0.00");

    public UserRecordAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<UserRecordInfoBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        UserRecordAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.user_record_item, parent, false);
            viewHolder = new UserRecordAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (UserRecordAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.tv_product_name.setText(list.get(position).getProductName());
        viewHolder.tv_tradeno.setText(list.get(position).getTradeNo());
        Double price = list.get(position).getProductPrice() / 100.00;
        viewHolder.tv_product_price.setText(Utils.getInterString(context, R.string.money) + df.format(price));
        String payTime = Utils.getInterString(context, R.string.unknown);
        if (list.get(position).getPayTime().length() > 0) {
            payTime = DateUtil.setTimeStyle(context, list.get(position).getPayTime());
        }
        viewHolder.tv_time.setText(payTime);
        return convertView;
    }

    class ViewHolder {
        private TextView tv_product_name;
        private TextView tv_tradeno;
        private TextView tv_product_price;
        private TextView tv_time;

        public ViewHolder(View view) {
            tv_product_name = (TextView) view.findViewById(R.id.tv_product_name);
            tv_tradeno = (TextView) view.findViewById(R.id.tv_tradeno);
            tv_product_price = (TextView) view.findViewById(R.id.tv_product_price);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
        }
    }
}
