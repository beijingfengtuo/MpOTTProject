package cn.cibnmp.ott.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ta.utdid2.android.utils.StringUtils;

import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.bean.ScreeningDataBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.categoryList.HomeOnItemClickListener;
import cn.cibnmp.ott.ui.categoryList.HomePlayOnClickListener;
import cn.cibnmp.ott.ui.search.ToastUtils;
import cn.cibnmp.ott.utils.DisplayUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.NetWorkUtils;
import cn.cibnmp.ott.utils.img.ImageFetcher;


/**
 * 玩票页面（其他Fragment中的GridView控件）列表数据的Adapter
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2017/9/27
 */
public class HomeThreeOtherGridAdapter extends RecyclerView.Adapter<HomeThreeOtherGridAdapter.VideoViewHolder> {
    private static String TAG = HomeThreeOtherGridAdapter.class.getName();
    private Context context;
    //导航栏目内容列表（京剧 导航栏目下所有视频列表集合）
    private List<ScreeningDataBean.DataBean.ListcontentBean.ContentBean> contentBeanList;
    private HomeOnItemClickListener listener;
    private HomePlayOnClickListener playListener;

    public HomeThreeOtherGridAdapter(Context context, List<ScreeningDataBean.DataBean.ListcontentBean.ContentBean> contentBeanList,
                                     HomeOnItemClickListener listener, HomePlayOnClickListener playListener) {
        this.context = context;
        this.contentBeanList = contentBeanList;
        this.listener = listener;
        this.playListener = playListener;
    }

    @Override
    public HomeThreeOtherGridAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_three_other_variety_gridview_item, parent, false);
        HomeThreeOtherGridAdapter.VideoViewHolder holder = new HomeThreeOtherGridAdapter.VideoViewHolder(view);
        view.setTag(holder);
        return new HomeThreeOtherGridAdapter.VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeThreeOtherGridAdapter.VideoViewHolder holder, int position) {
        holder.update(holder, position);
    }

    @Override
    public int getItemCount() {
        return (contentBeanList != null && contentBeanList.size() > 0) ? contentBeanList.size() : 0;
    }

    /**
     * ViewHolder处理
     */
    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private int position;
        private RelativeLayout layoutAll;
        private RelativeLayout showView; //背景图片布局（播放视频时隐藏、结束播放后显示）
        private ImageView imgShowbg; //背景图片（播放视频时隐藏、结束播放后显示）
        private FrameLayout videoLayout; //视频播放布局
        private ImageView imgRePlay;
        private TextView tvTitleName;

        //分享、收藏、点赞
        private ImageView imgShare, imgCollect, imgZan;
        //人物名称、地点、点赞数量
        private TextView tvName, tvAdd, tvZanNum;

        public VideoViewHolder(View itemView) {
            super(itemView);
            layoutAll = (RelativeLayout) itemView.findViewById(R.id.rl_home_three_other_variety_gridview_item_all);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layoutAll.getLayoutParams();
            layoutParams.height = DisplayUtils.getValue(440);
            layoutAll.setLayoutParams(layoutParams);

            videoLayout = (FrameLayout) itemView.findViewById(R.id.layout_video);
            showView = (RelativeLayout) itemView.findViewById(R.id.rl_home_three_other_variety_gridview_item_show);
            imgShowbg = (ImageView) itemView.findViewById(R.id.img_home_three_other_variety_gridview_item_bg);
            imgRePlay = (ImageView) itemView.findViewById(R.id.img_home_three_other_variety_gridview_item_replay_play);

            //分享、收藏、点赞
            imgShare = (ImageView) itemView.findViewById(R.id.img_home_three_other_variety_gridview_item_share);
            imgCollect = (ImageView) itemView.findViewById(R.id.img_home_three_other_variety_gridview_item_collection);
            imgZan = (ImageView) itemView.findViewById(R.id.img_home_three_other_variety_gridview_item_zan);

            tvTitleName = (TextView) itemView.findViewById(R.id.tv_home_three_other_variety_gridview_item_title);
            tvName = (TextView) itemView.findViewById(R.id.tv_home_three_other_variety_gridview_item_name);
            tvAdd = (TextView) itemView.findViewById(R.id.tv_home_three_other_variety_gridview_item_add);
            tvZanNum = (TextView) itemView.findViewById(R.id.tv_home_three_other_variety_gridview_item_zan_num);

        }

        /**
         * 显示数据
         *
         * @param position
         */
        public void update(final HomeThreeOtherGridAdapter.VideoViewHolder viewHolder, final int position) {
            this.position = position;

            ScreeningDataBean.DataBean.ListcontentBean.ContentBean data = contentBeanList.get(position);

            if (StringUtils.isEmpty(data.getImgh())) {
                viewHolder.imgShowbg.setImageResource(R.drawable.videopre_background);
            } else {
                ImageFetcher.getInstance().loadImage(data.getImgh(), viewHolder.imgShowbg, R.drawable.videopre_background);
            }

            if (!StringUtils.isEmpty(data.getSlogan())) {
                viewHolder.tvTitleName.setVisibility(View.VISIBLE);
                viewHolder.tvTitleName.setText(data.getDisplayName());
            } else {
                viewHolder.tvTitleName.setVisibility(View.GONE);
            }

            if (StringUtils.isEmpty(data.getSlogan())) {
                viewHolder.tvName.setText("未知");
            } else {
                viewHolder.tvName.setText(data.getSlogan());
            }

            if (StringUtils.isEmpty(data.getScrollMarquee())) {
                viewHolder.tvAdd.setText("未知");
            } else {
                viewHolder.tvAdd.setText(data.getScrollMarquee());
            }

            if (StringUtils.isEmpty(data.getScore())) {
                viewHolder.tvZanNum.setText("未知");
            } else {
                viewHolder.tvZanNum.setText(data.getScore());
            }

            showView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetWorkUtils.isNetworkAvailable(context)) {
                        //当前网络不可用
                        Lg.i(TAG, "当前网络异常，请重试连接!");
                        ToastUtils.show(context, "当前网络异常，请重试连接!");
                        return;
                    }
                    showView.setVisibility(View.GONE);
                    imgRePlay.setVisibility(View.GONE);
                    if (listener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constant.HOME_THREE_OTHER_RECYCLERVIEW_ITEM_POSITION, position);
                        bundle.putSerializable(Constant.HOME_THREE_OTHER_RECYCLERVIEW_ITEM_DATA, contentBeanList.get(position));
                        listener.onItemClickListener(bundle);
                    }
                }
            });

            imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playListener.getPlayOnClickListener(VideoViewHolder.this, Constant.PLAY_TAG_SHARE, contentBeanList.get(position));
                }
            });

            imgCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playListener.getPlayOnClickListener(VideoViewHolder.this, Constant.PLAY_TAG_COLLECT, contentBeanList.get(position));
                }
            });

            imgZan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playListener.getPlayOnClickListener(VideoViewHolder.this, Constant.PLAY_TAG_ZAN, contentBeanList.get(position));
                }
            });
        }
    }
}