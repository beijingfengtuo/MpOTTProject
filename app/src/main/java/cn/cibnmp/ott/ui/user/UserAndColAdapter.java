package cn.cibnmp.ott.ui.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.ui.detail.bean.RecordBean;
import cn.cibnmp.ott.utils.Utils;
import cn.cibnmp.ott.utils.img.ImageFetcher;

/**
 * Created by cibn-lyc on 2018/1/16.
 */

public class UserAndColAdapter extends BaseAdapter {

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

    public UserAndColAdapter(Context context) {
        this.context = context;
    }

    /** 添加数据 **/
    public void setDate(List<RecordBean> list) {
        this.videoList = list;
        isSlect = new HashMap<Integer, Integer>();
    }

    /** 添加编辑状态标识 **/
    public void setVG(boolean isVG) {
        this.isVG = isVG;
        super.notifyDataSetChanged();
    }

    /** 添加全选状态标识 **/
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
        RecordBean detailBean =null;
        if(videoList.size()>position){
            detailBean = videoList.get(position);
        }
        ViewHolderUser viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_and_coladapter_item, null);
            viewHolder = new ViewHolderUser(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderUser) convertView.getTag();
        }
        if(detailBean!=null){
            ImageFetcher.getInstance().loadImage(detailBean.getPosterFid(), viewHolder.user_left_img, R.color.black_hui8);
            if (detailBean.getVname() != null && !detailBean.getVname().isEmpty()) {
                viewHolder.vidio_name.setText(detailBean.getVname());
            } else {
                viewHolder.vidio_name.setText(Utils.getInterString(context, R.string.unknown));
            }

//            if (detailBean.getUpdatenum() != null) {
//                viewHolder.vidio_londing.setVisibility(View.VISIBLE);
//                viewHolder.vidio_londing.setText(detailBean.getUpdatenum());
//            }else{
//                viewHolder.vidio_londing.setVisibility(View.GONE);
//            }
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

        public ViewHolderUser(View view) {
            user_left_rl = (RelativeLayout) view.findViewById(R.id.user_left_rl);
            user_left_img = (ImageView) view.findViewById(R.id.user_left_img);
            vidio_name = (TextView) view.findViewById(R.id.vidio_name);
            vidio_londing = (TextView) view.findViewById(R.id.vidio_londing);
            slect_btn = (ImageView) view.findViewById(R.id.slect_btn);
        }
    }
}
