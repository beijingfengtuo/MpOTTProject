package cn.cibnmp.ott.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import cn.cibnmp.ott.utils.TimeUtils;
import cn.cibnmp.ott.utils.img.ImageFetcher;


/**
 * 直播页面（现场、录像）列表数据的Adapter
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2017/9/27
 */
public class HomeTwoLiveSceneGridAdapter extends BaseAdapter {
    private Context context;
    private HomeOnItemClickListener onItemClickListener;
    private List<ScreeningDataBean.DataBean.ListcontentBean.ContentBean> contentBeanList;

    public HomeTwoLiveSceneGridAdapter(Context context, List<ScreeningDataBean.DataBean.ListcontentBean.ContentBean> data, HomeOnItemClickListener listener) {
        this.context = context;
        this.contentBeanList = data;
        this.onItemClickListener = listener;
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
                    R.layout.home_two_live_variety_gridview_item, parent, false);
            viewHolder = new ViewHolderMove(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderMove) convertView.getTag();
        }

        if (contentBeanList == null || contentBeanList.size() <= 0) {
            goneView(viewHolder);
            return convertView;
        }

        viewHolder.three_item_title_rl.setTag(position);

        viewHolder.varirty_item_rl0.setVisibility(View.VISIBLE);
        if (!StringUtils.isEmpty(contentBeanList.get(position).getImgh())) {
            ImageFetcher.getInstance().loadImage(contentBeanList.get(position).getImgh(), viewHolder.varirty_img0);
        } else if (!StringUtils.isEmpty(contentBeanList.get(position).getImg())) {
            ImageFetcher.getInstance().loadImage(contentBeanList.get(position).getImg(), viewHolder.varirty_img0);
        }
        viewHolder.varirty_name0.setText(contentBeanList.get(position).getDisplayName());
        if (contentBeanList.get(position).getStatus() == 5) {
            String s = contentBeanList.get(position).getPlayTime();
            String time;
            String MM;
            String sTime;
            String hh;
            String dd;
            if (!StringUtils.isEmpty(s) && s.indexOf("-") != -1) {
                time = s.substring(s.indexOf("-") + 1);
                if (!StringUtils.isEmpty(time) && time.indexOf("-") != -1 && time.indexOf(" ") != -1 && (time.indexOf(" ") + 1) != -1) {
                    MM = time.substring(0, time.indexOf("-")) + "月";
                    if (time.indexOf("-") < time.indexOf(" ")) {
                        dd = time.substring(time.indexOf("-") + 1, time.indexOf(" ")) + "日";
                        sTime = time.substring(time.indexOf(" ") + 1);
                        if (!StringUtils.isEmpty(sTime) && sTime.indexOf(":") != -1 && sTime.lastIndexOf(":") != -1) {
                            if (sTime.indexOf(":") != sTime.lastIndexOf(":")) {
                                hh = sTime.substring(0, sTime.lastIndexOf(":"));
                            } else {
                                hh = sTime;
                            }
                            time = MM + dd + "_" + hh;
                            viewHolder.varirty_content0.setText(context.getString(R.string.txt_home_two_yanchushijian) + getStartDateString(time));
                        } else {
                            viewHolder.varirty_content0.setText("");
                        }
                    } else {
                        viewHolder.varirty_content0.setText("");
                    }
                } else {
                    viewHolder.varirty_content0.setText("");
                }
            } else {
                viewHolder.varirty_content0.setText("");
            }
        } else {
            viewHolder.varirty_content0.setText(context.getString(R.string.txt_home_two_yanchushijian) + getStartDateString(TimeUtils.getLiveCurTime4(contentBeanList.get(position).getStartDate())));
        }
        getLiveStatus(contentBeanList.get(position).getStatus() + "", viewHolder.varirty_status0);
        return convertView;
    }

    /**
     * 时间拼接
     *
     * @param startDate 直播开始时间
     * @return
     */
    public String getStartDateString(String startDate) {
        String date = "";
        if (!StringUtils.isEmpty(startDate) && startDate.indexOf("_") != -1 && (startDate.indexOf("_") + 1) != -1) {
            String date1 = startDate.substring(startDate.indexOf("_") + 1);
            String date2 = startDate.substring(0, startDate.indexOf("_"));
            date = date1 + " " + date2;
        }
        return date;
    }

    public void goneView(ViewHolderMove viewHolder) {
        if (viewHolder != null) {
            viewHolder.varirty_item_rl0.setVisibility(View.GONE);
        }
    }

    public void itemClick(int tag) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.BUNDLE_ACTION, getItem(tag).getAction());
        bundle.putSerializable(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN, getItem(tag));
        onItemClickListener.onItemClickListener(bundle);
    }

    private void getLiveStatus(String status, TextView view) {
        if ("0".equals(status)) {
//            view.setText("未开始");
            view.setText(context.getString(R.string.txt_home_two_weikaishi));
            view.setBackgroundColor(context.getResources().getColor(R.color.colore_live2));
        } else if ("1".equals(status)) {
//            view.setText("直播中");
            view.setText(context.getString(R.string.txt_home_two_zhibozhong));
            view.setBackgroundColor(context.getResources().getColor(R.color.red3));
        } else if ("2".equals(status)) {
//            view.setText("已结束");
            view.setText(context.getString(R.string.txt_home_two_yijieshu));
            view.setBackgroundColor(context.getResources().getColor(R.color.black_hui5));
        } else if ("3".equals(status)) {
//            view.setText("回看");
            view.setText(context.getString(R.string.txt_home_two_huikan));
            view.setBackgroundColor(context.getResources().getColor(R.color.colore_live1));
        } else if ("5".equals(status)){
//            view.setText("点播");
            view.setText(context.getString(R.string.txt_home_two_dianbo));
            view.setBackgroundColor(context.getResources().getColor(R.color.colore_live5));
        }
    }

    public class ViewHolderMove implements OnClickListener{
        private RelativeLayout three_item_title_rl;
        // item0
        private View varirty_item_rl0;
        private ImageView varirty_img0;
        private TextView varirty_name0;
        private TextView varirty_content0;
        private TextView varirty_status0;

        public ViewHolderMove(View view) {
            three_item_title_rl = (RelativeLayout) view
                    .findViewById(R.id.rl_home_two_live_variety_gridview_item_layout);
            // item0
            varirty_item_rl0 = view.findViewById(R.id.item0);
            varirty_img0 = (ImageView) varirty_item_rl0
                    .findViewById(R.id.img_home_two_item_horizontal_img);
            varirty_name0 = (TextView) varirty_item_rl0
                    .findViewById(R.id.tv_home_two_item_horizontal_name);
            varirty_content0 = (TextView) varirty_item_rl0
                    .findViewById(R.id.tv_home_two_item_horizontal_text);
            varirty_status0 = (TextView) varirty_item_rl0
                    .findViewById(R.id.tv_home_two_item_horizontal_status);

            varirty_item_rl0.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int tag = (Integer) three_item_title_rl.getTag();
            switch (v.getId()) {
                case R.id.item0:
                    itemClick(tag);
                    break;
                default:
                    break;
            }
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