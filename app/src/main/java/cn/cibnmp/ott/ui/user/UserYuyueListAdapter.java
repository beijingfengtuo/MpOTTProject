package cn.cibnmp.ott.ui.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.ui.detail.bean.ReserveBean;
import cn.cibnmp.ott.utils.Utils;
import cn.cibnmp.ott.utils.img.ImageFetcher;

/**
 * Created by cibn-lyc on 2018/2/5.
 */

public class UserYuyueListAdapter extends BaseAdapter {
    private Context context;
    private List<ReserveBean> videoList;
    // 编辑状态标识
    private boolean isVG = false;
    // 多选初始化为标识0
    private int slectALL = 0;
    // 多选全部选中的标识
    private final int allSlectYES = 1;
    // 多选全部取消的标识
    private final int allSlectNO = 2;
    public HashMap<Integer, Integer> isSlect;

    public UserYuyueListAdapter(Context context) {
        this.context = context;
    }

    private HashMap<String, String> hashList = new HashMap<>();

    /**
     * 添加数据
     **/
    public void setDate(List<ReserveBean> list) {
        this.videoList = list;
        isSlect = new HashMap<Integer, Integer>();
    }

    @Override
    public void notifyDataSetChanged() {
        hashList.clear();
        super.notifyDataSetChanged();
    }

    /**
     * 添加编辑状态标识
     **/
    public void setVG(boolean isVG) {
        this.isVG = isVG;
        super.notifyDataSetChanged();
    }

    /**
     * 添加全选状态标识
     **/
    public void setSize(int slectALL) {
        this.slectALL = slectALL;
        if (slectALL > 0) {
            int slect = 0;
            if (slectALL == allSlectYES) {
                slect = 1;
            } else if (slectALL == allSlectNO) {
                slect = 0;
            }
            for (int i = 0; i < videoList.size(); i++) {
                isSlect.put(i, slect);
            }
        }
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int position) {
        return videoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReserveBean detailBean = null;
        if (videoList.size() > position) {
            detailBean = videoList.get(position);
        }
        UserYuyueListAdapter.ViewHolderUser viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_yuyue_list_adapter_item, null);
            viewHolder = new UserYuyueListAdapter.ViewHolderUser(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (UserYuyueListAdapter.ViewHolderUser) convertView.getTag();
        }
        if (detailBean != null) {
            ImageFetcher.getInstance().loadImageCenterCrop(detailBean.getPosterFid(),
                    viewHolder.user_left_img, R.color.black_hui8);
            if (detailBean.getName() != null && !detailBean.getName().isEmpty()) {
                viewHolder.vidio_name.setText(detailBean.getName());
            } else {
                viewHolder.vidio_name.setText(Utils.getInterString(context, R.string.unknown));
            }
            if (detailBean.getLiveStartTimeStamp() > 0) {
                viewHolder.vidio_londing.setText(startTime2(detailBean.getLiveStartTimeStamp()));
            } else {
                viewHolder.vidio_londing.setText(Utils.getInterString(context, R.string.unknown));
            }
            getTime(detailBean, viewHolder.yuyue_tag, viewHolder.yuyue_tag2);
        }

        String day = startTime(detailBean.getLiveStartTimeStamp());
        if (!hashList.containsKey(day)) {
            hashList.put(day, detailBean.getLid());
            viewHolder.user_title_day.setText(day);
            viewHolder.user_title_ll.setVisibility(View.VISIBLE);
        } else if (hashList.get(day).equals(detailBean.getLid())) {
            viewHolder.user_title_ll.setVisibility(View.VISIBLE);
            viewHolder.user_title_day.setText(day);
        } else {
            viewHolder.user_title_ll.setVisibility(View.GONE);
        }

        if (isVG) {
            viewHolder.slect_btn.setVisibility(View.VISIBLE);
            if (isSlect.get(position) != null && isSlect.get(position) == 1) {
                viewHolder.slect_btn.setBackgroundResource(R.drawable.checked);
            } else {
                isSlect.put(position, 0);
                viewHolder.slect_btn.setBackgroundResource(R.drawable.unchecked);
            }
        } else {
            isSlect.put(position, 0);
            viewHolder.slect_btn.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolderUser {
        private RelativeLayout user_left_rl;
        // 影片海报
        private ImageView user_left_img;
        // 影片名称
        private TextView vidio_name;
        // 影片观看进度
        private TextView vidio_londing;
        // 选中按钮
        private ImageView slect_btn;
        //顶部时间的layout
        private RelativeLayout user_title_ll;
        //顶部时间
        private TextView user_title_day;
        //预约图片
        private ImageView yuyue_tag, yuyue_tag2;

        public ViewHolderUser(View view) {
            user_title_ll = (RelativeLayout) view.findViewById(R.id.user_title_ll);
            user_title_day = (TextView) view.findViewById(R.id.user_title_day);
            user_left_rl = (RelativeLayout) view.findViewById(R.id.user_left_rl);
            user_left_img = (ImageView) view.findViewById(R.id.user_left_img);
            vidio_name = (TextView) view.findViewById(R.id.vidio_name);
            vidio_londing = (TextView) view.findViewById(R.id.vidio_londing);
            slect_btn = (ImageView) view.findViewById(R.id.slect_btn);
            yuyue_tag = (ImageView) view.findViewById(R.id.yuyue_tag);
            yuyue_tag2 = (ImageView) view.findViewById(R.id.yuyue_tag2);
        }
    }

    //开始日期转换
    private String startTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月");
        Date date = new Date(time);
        return formatter.format(date);
    }

    //预约开始时间日期转换
    private String startTime2(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日  HH:mm");
        Date date = new Date(time);
        return formatter.format(date);
    }

    //获取当前系统时间和按钮状态isPlay
    private int getTime(ReserveBean detailBean, ImageView imageView1, ImageView imageView2) {
        long systemTime = System.currentTimeMillis() + App.timeDvalue;
        int isPlay = 0;
        if (detailBean != null) {
            //获取倒计时的开始时间
            long goTime = detailBean.getLiveStartTimeStamp();
            //获取倒计时的结束时间
            long toTime = detailBean.getLiveEndTimeStamp();
            //获取直播状态
            int livestatus = detailBean.getLivestatus();

//            Lg.i("systemTime", goTime + "------------" + systemTime + "----------->>>>" + toTime);

            if (livestatus == 1 || (goTime < systemTime && toTime > systemTime)) {
                isPlay = 1;//正在直播
                imageView1.setVisibility(View.GONE);
                imageView2.setVisibility(View.VISIBLE);
            } else {
                imageView1.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.GONE);
                if (livestatus == 2) {
                    isPlay = 3;//已结束
                    imageView1.setBackgroundResource(R.drawable.yuyue_tag_bg3);
                } else if (goTime > systemTime && toTime > systemTime) {
                    isPlay = 2;//未开始
                    imageView1.setBackgroundResource(R.drawable.yuyue_tag_bg1);
                } else {
                    isPlay = 3;//已结束
                    imageView1.setBackgroundResource(R.drawable.yuyue_tag_bg3);
                }
            }
        }
        return isPlay;
    }
}
