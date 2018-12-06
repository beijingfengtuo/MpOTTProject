package cn.cibnmp.ott.ui.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.bean.VoucherListBean.DataBean.VouchersBean;
import cn.cibnmp.ott.utils.DateUtil;
import cn.cibnmp.ott.utils.Utils;

/**
 * Created by cibn-lyc on 2018/1/29.
 */

public class VouchersAdapter extends BaseAdapter {

    private Context context;
    private List<VouchersBean> vouchersBeen;
    private int voucherStatus;
    private DecimalFormat df = new DecimalFormat("0.00");

    public VouchersAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<VouchersBean> vouchersBeen, int voucherStatus) {
        this.vouchersBeen = vouchersBeen;
        this.voucherStatus = voucherStatus;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return vouchersBeen == null ? 0 : vouchersBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return vouchersBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.vouchers_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        switch (voucherStatus) {
            case 1:
                viewHolder.img_vouchers_bj.setImageResource(R.mipmap.vouchers_can_use);
                break;
            case 3:
                viewHolder.img_vouchers_bj.setImageResource(R.mipmap.vouchers_has_use);
                break;
            case 4:
                viewHolder.img_vouchers_bj.setImageResource(R.mipmap.vouchers_expired);
                break;
        }
        viewHolder.tv_vouchers_name.setText(vouchersBeen.get(position).getVoucherName());
        viewHolder.tv_vouchers_code.setText(vouchersBeen.get(position).getVoucherCode());
        viewHolder.tv_vouchers_price.setText(Utils.getInterString(context, R.string.money) + df.format((Double)(vouchersBeen.get(position).getVoucherMoney() / 100.00)));
        String expTime = DateUtil.setTimeStyle(context, vouchersBeen.get(position).getExpTime());
        viewHolder.tv_vouchers_time.setText(expTime);

        return convertView;
    }

    public class ViewHolder {
        ImageView img_vouchers_bj;
        TextView tv_vouchers_name;
        TextView tv_vouchers_code;
        TextView tv_vouchers_price;
        TextView tv_vouchers_time;

        public ViewHolder(View view) {
            img_vouchers_bj = (ImageView) view.findViewById(R.id.img_vouchers_bj);
            tv_vouchers_name = (TextView) view.findViewById(R.id.tv_vouchers_name);
            tv_vouchers_code = (TextView) view.findViewById(R.id.tv_vouchers_code);
            tv_vouchers_price = (TextView) view.findViewById(R.id.tv_vouchers_price);
            tv_vouchers_time = (TextView) view.findViewById(R.id.tv_vouchers_time);
        }
    }
}