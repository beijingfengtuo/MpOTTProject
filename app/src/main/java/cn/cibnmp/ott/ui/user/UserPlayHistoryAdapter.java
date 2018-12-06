package cn.cibnmp.ott.ui.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.ui.detail.bean.RecordBean;
import cn.cibnmp.ott.ui.header.StickyListHeadersAdapter;
import cn.cibnmp.ott.utils.Utils;
import cn.cibnmp.ott.utils.img.ImageFetcher;

/**
 * Created by cibn-lyc on 2018/1/17.
 */

public class UserPlayHistoryAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {
    private Context context;
    private List<RecordBean> videoList;
    // 编辑状态标识
    private boolean isVG = false;
    // 多选初始化为标识0
    private int slectALL = 0;
    // 多选全部选中的标识
    private final int allSlectYES = 1;
    // 多选全部取消的标识
    private final int allSlectNO = 2;
    public HashMap<Integer, Integer> isSlect;
    private String[] sections;
    private LayoutInflater inflater;
    private int toHeardPosition = 0;
    // 今天子时的时间戳
    private long fTime;
    // 6天内的时间差
    private final long timeJ = 518400562;

    private int mRightWidth = 0;

    public UserPlayHistoryAdapter(Context context, int rightWidth) {
        this.context = context;
        sections = new String[]{Utils.getInterString(context, R.string.today),
                Utils.getInterString(context, R.string.within_week),
                Utils.getInterString(context, R.string.long_ago)};
        mRightWidth = rightWidth;
        inflater = LayoutInflater.from(context);

        if (fTime <= 0) {
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.set(Calendar.HOUR_OF_DAY, 0);
            mCalendar.set(Calendar.MINUTE, 0);
            mCalendar.set(Calendar.SECOND, 0);
            fTime = mCalendar.getTimeInMillis();
        }
    }

    /**
     * 添加数据
     **/
    public void setDate(List<RecordBean> list) {
        this.videoList = list;
        isSlect = new HashMap<Integer, Integer>();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        RecordBean detailBean = videoList.get(position);
        ViewHolderUser viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_history_item, null);
            viewHolder = new ViewHolderUser(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderUser) convertView.getTag();
        }

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        viewHolder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(mRightWidth,
                LinearLayout.LayoutParams.MATCH_PARENT);
        viewHolder.item_right.setLayoutParams(lp2);

        ImageFetcher.getInstance().loadImage(detailBean.getPosterFid(),
                viewHolder.user_left_img, R.color.black_hui8);
        if (detailBean.getVname() != null && !detailBean.getVname().isEmpty()) {
            viewHolder.vidio_name.setText(detailBean.getVname());
        } else {
            viewHolder.vidio_name.setText(Utils.getInterString(context, R.string.unknown));
        }

        if (detailBean.getDuration() != 0) {
            int pos = (int) (detailBean.getCurrentPos() * 100 / detailBean.getDuration());
            if (pos >= 97) {
                viewHolder.vidio_londing.setText(context.getString(R.string.video_look_finish));
            } else {
                viewHolder.vidio_londing.setText(context.getString(R.string.video_look_to) + pos + Utils.getInterString(context, R.string.percent));
            }
        } else {
            viewHolder.vidio_londing.setText(context.getString(R.string.video_look_to_o));
        }

        viewHolder.item_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position);
                }
            }
        });

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

        RelativeLayout item_left;
        RelativeLayout item_right;

        public ViewHolderUser(View view) {
            user_left_rl = (RelativeLayout) view.findViewById(R.id.user_left_rl);
            user_left_img = (ImageView) view.findViewById(R.id.user_left_img);
            vidio_name = (TextView) view.findViewById(R.id.vidio_name);
            vidio_londing = (TextView) view.findViewById(R.id.vidio_londing);

            item_left = (RelativeLayout) view.findViewById(R.id.item_left);
            item_right = (RelativeLayout) view.findViewById(R.id.item_right);
        }
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.histery_s = (ImageView) convertView.findViewById(R.id.histery_s);
            holder.histery_qiu = (ImageView) convertView.findViewById(R.id.histery_qiu);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        int sectionPosition = getSectionPosition(position);
        if (toHeardPosition == sectionPosition || sectionPosition == 0) {
            if (sectionPosition == 0) {
                holder.histery_s.setVisibility(View.GONE);
            }
            holder.histery_qiu.setBackgroundResource(R.drawable.history_point_se);
            holder.text.setTextColor(context.getResources().getColor(R.color.colore_home3));
        } else {
            if (sectionPosition != 0) {
                holder.histery_s.setVisibility(View.VISIBLE);
            }
            holder.histery_qiu.setBackgroundResource(R.drawable.history_point_se);
            holder.text.setTextColor(context.getResources().getColor(R.color.black));
        }
        String headerText = sections[sectionPosition];
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        int toPosition = getSectionPosition(position);
        if (toHeardPosition != toPosition) {
            toHeardPosition = toPosition;
        }
        return sections[toPosition].charAt(0);
    }

    @Override
    public int getPositionForSection(int section) {
        return getSectionPosition(section);
    }

    @Override
    public int getSectionForPosition(int position) {
        return getSectionPosition(position);
    }

    @Override
    public Object[] getSections() {
        return sections;
    }

    public void clearAll() {
        videoList.clear();
    }

    class HeaderViewHolder {
        TextView text;
        ImageView histery_s;
        ImageView histery_qiu;
    }

    private int getSectionPosition(int section) {
        int position = 0;
        if (videoList != null) {
            long time = videoList.get(section).getTimes();
            if (time > fTime) {
                position = 0;
            } else if (time > fTime - timeJ) {
                position = 1;
            } else {
                position = 2;
            }
        }
        return position;
    }

    /**
     * 单击事件监听器
     */
    private onRightItemClickListener mListener = null;

    public void setOnRightItemClickListener(onRightItemClickListener listener) {
        mListener = listener;
    }

    /**
     * 删除点击接口
     */
    public interface onRightItemClickListener {
        void onRightItemClick(View v, int position);
    }
}
