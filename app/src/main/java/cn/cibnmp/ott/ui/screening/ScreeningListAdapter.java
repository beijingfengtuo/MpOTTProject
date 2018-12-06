package cn.cibnmp.ott.ui.screening;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ta.utdid2.android.utils.StringUtils;

import java.util.List;
import cn.cibnhp.grand.R;
import cn.cibnmp.ott.bean.ScreeningDataBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.categoryList.HomeOnItemClickListener;
import cn.cibnmp.ott.utils.img.ImageFetcher;


/**
 * 筛选页面 - 筛选节目列表数据的Adapter
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2017/9/27
 */
public class ScreeningListAdapter extends BaseAdapter {
    private Context context;
    private List<ScreeningDataBean.DataBean.ListcontentBean.ContentBean> contentBeanList;
    private HomeOnItemClickListener listener;

    public ScreeningListAdapter(Context context, List<ScreeningDataBean.DataBean.ListcontentBean.ContentBean> data, HomeOnItemClickListener listener) {
        this.context = context;
        this.contentBeanList = data;
        this.listener = listener;
    }

    public void setData(List<ScreeningDataBean.DataBean.ListcontentBean.ContentBean> data) {
        this.contentBeanList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (contentBeanList != null && contentBeanList.size() > 0) ? contentBeanList.size() : 0;
    }

    @Override
    public ScreeningDataBean.DataBean.ListcontentBean.ContentBean getItem(int position) {

        return contentBeanList.get(position);
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
                    R.layout.screening_list_gridview_item, parent, false);
            viewHolder = new ViewHolderMove(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderMove) convertView.getTag();
        }

        if (contentBeanList == null || contentBeanList.size() == 0) {
            goneView(viewHolder);
            return convertView;
        }
        viewHolder.three_item_title_rl.setTag(position);
        viewHolder.varirty_item_rl0.setVisibility(View.VISIBLE);
        if (!StringUtils.isEmpty(contentBeanList.get(position).getImg())) {
            ImageFetcher.getInstance().loadImage(contentBeanList.get(position).getImg(), viewHolder.varirty_img0);
        } else if (!StringUtils.isEmpty(contentBeanList.get(position).getImgh())) {
            ImageFetcher.getInstance().loadImage(contentBeanList.get(position).getImgh(), viewHolder.varirty_img0);
        }
        viewHolder.varirty_name0.setText(contentBeanList.get(position).getDisplayName());

        return convertView;
    }

    class ViewHolderMove implements View.OnClickListener{
        private RelativeLayout three_item_title_rl;
        // item0
        private RelativeLayout varirty_item_rl0;
        private ImageView varirty_img0;
        private TextView varirty_name0;



        public ViewHolderMove(View view) {
            three_item_title_rl = (RelativeLayout) view.findViewById(R.id.three_rl);
            // item0
            varirty_item_rl0 = (RelativeLayout) view.findViewById(R.id.item0);
            varirty_img0 = (ImageView) varirty_item_rl0
                    .findViewById(R.id.vertical_img);
            varirty_name0 = (TextView) varirty_item_rl0
                    .findViewById(R.id.vertical_text);

            varirty_item_rl0.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int tag = (Integer) three_item_title_rl.getTag();
            switch (v.getId()) {
                case R.id.item0:
                    itemClick(tag);
                    break;
            }
        }
    }

    public void itemClick(int tag) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.BUNDLE_ACTION, getItem(tag).getAction());
        bundle.putSerializable(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN, getItem(tag));
        listener.onItemClickListener(bundle);
    }

    public void goneView(ViewHolderMove viewHolder) {
        if (viewHolder != null) {
            viewHolder.varirty_item_rl0.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}