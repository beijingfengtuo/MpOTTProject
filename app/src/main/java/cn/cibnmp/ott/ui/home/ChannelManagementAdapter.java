package cn.cibnmp.ott.ui.home;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.bean.NavigationItemBean;
import cn.cibnmp.ott.ui.categoryList.OnDragVHListener;
import cn.cibnmp.ott.ui.categoryList.OnItemMoveListener;
import cn.cibnmp.ott.utils.img.ImageFetcher;


/**
 * 拖拽排序 + 增删 频道管理上面的RecyclerView
 * Created by yangwenwu on 2017/12/25.
 */
public class ChannelManagementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnItemMoveListener {

    // 我的频道 标题部分
    public static final int TYPE_MY_CHANNEL_HEADER = 0;
    // 我的频道
    public static final int TYPE_MY = 1;
    // 其他频道
    public static final int TYPE_OTHER = 3;
    // 我的频道之前的header数量  该demo中 即标题部分 为 1
    private static final int COUNT_PRE_MY_HEADER = 1;
    // touch 点击开始时间
    private long mStartTime;
    // touch 间隔时间  用于分辨是否是 "点击"
    private static final long SPACE_TIME = 100;
    private LayoutInflater mInflater;
    private ItemTouchHelper mItemTouchHelper;
    // 是否为 编辑 模式
    private boolean isEditMode;
    private Context mContext;
    private List<NavigationItemBean> mMyChannelItems;
    // 我的频道点击事件
    private OnMyChannelItemClickListener mChannelItemClickListener;

    public ChannelManagementAdapter(
            Context context, ItemTouchHelper helper,
            List<NavigationItemBean> mMyChannelItems) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mItemTouchHelper = helper;
        this.mMyChannelItems = mMyChannelItems;
    }

    /**
     * 频道标题
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {    // 我的频道 标题部分
            return TYPE_MY_CHANNEL_HEADER;
        } else if (position > 0 && position < mMyChannelItems.size() + 1) {
            return TYPE_MY;
        }
        return TYPE_OTHER;
    }

    /**
     * 频道删除移动
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view;
        switch (viewType) {
            case TYPE_MY_CHANNEL_HEADER:
                view = mInflater.inflate(R.layout.home_channel_adapter_item, parent, false);
                final MyChannelHeaderViewHolder holder = new MyChannelHeaderViewHolder(view);
                return holder;

            case TYPE_MY:
                view = mInflater.inflate(R.layout.home_adaptr_item_my, parent, false);
                final MyViewHolder myHolder = new MyViewHolder(view);

                myHolder.userl_relativelayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        int position = myHolder.getAdapterPosition();
                        if (isEditMode && mMyChannelItems.get(position - COUNT_PRE_MY_HEADER).getIsSolidShow() == 0) {
                            moveMyToOther(myHolder);
                        } else {
                            mChannelItemClickListener.onItemClick(v, position - COUNT_PRE_MY_HEADER);
                        }
                    }
                });

                myHolder.userl_relativelayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        if (isEditMode) {
                            RecyclerView recyclerView = ((RecyclerView) parent);
                            startEditMode(recyclerView);
                            //判断被拖拽的是否是带锁，如果不是则执行拖拽
                            if (mMyChannelItems.get(myHolder.getLayoutPosition() - COUNT_PRE_MY_HEADER).getIsSolidShow() != 1) {
                                mItemTouchHelper.startDrag(myHolder);
                            }
                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.channel_iseditmode), Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });

                myHolder.userl_relativelayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (isEditMode) {
                            switch (MotionEventCompat.getActionMasked(event)) {
                                case MotionEvent.ACTION_DOWN:
                                    mStartTime = System.currentTimeMillis();
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (System.currentTimeMillis() - mStartTime > SPACE_TIME) {
                                        //判断被拖拽的是否是前两个，如果不是则执行拖拽
                                        if (mMyChannelItems.get(myHolder.getLayoutPosition() - COUNT_PRE_MY_HEADER).getIsSolidShow() != 1) {
                                            mItemTouchHelper.startDrag(myHolder);
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_CANCEL:
                                case MotionEvent.ACTION_UP:
                                    mStartTime = 0;
                                    break;
                            }
                        }
                        return false;
                    }
                });
                return myHolder;
        }
        return null;
    }

    /**
     * 频道赋值
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myHolder = (MyViewHolder) holder;
            myHolder.textView.setText(mMyChannelItems.get(
                    position - COUNT_PRE_MY_HEADER).getName());
            ImageFetcher.getInstance().loodingImage
                    (mMyChannelItems.get(position - COUNT_PRE_MY_HEADER).getImgUrl()
                            , myHolder.imgBg, R.mipmap.defaultpic);
            //添加删除已选频道带锁图标控制
            if (isEditMode) {
                if (mMyChannelItems.get(position - COUNT_PRE_MY_HEADER).getIsSolidShow() == 1) {
                    myHolder.channel_lock.setVisibility(View.VISIBLE);
                    myHolder.imgEdit.setVisibility(View.GONE);
                } else {
                    myHolder.channel_lock.setVisibility(View.GONE);
                    myHolder.imgEdit.setVisibility(View.VISIBLE);
                }
            } else {
                myHolder.channel_lock.setVisibility(View.GONE);
                myHolder.imgEdit.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mMyChannelItems.size() + COUNT_PRE_MY_HEADER;
    }


    /**
     * 我的频道 移动到 其他频道
     *
     * @param myHolder
     */
    private void moveMyToOther(MyViewHolder myHolder) {
        mChannelItemClickListener.onItemDeleteClick(myHolder.getAdapterPosition() - COUNT_PRE_MY_HEADER);
    }

    /**
     * 移动方法
     * @param fromPosition
     * @param toPosition
     */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        NavigationItemBean item = mMyChannelItems.get(fromPosition - COUNT_PRE_MY_HEADER);
        mMyChannelItems.remove(fromPosition - COUNT_PRE_MY_HEADER);
        mMyChannelItems.add(toPosition - COUNT_PRE_MY_HEADER, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * 开启编辑模式
     *
     * @param parent
     */
    public void startEditMode(RecyclerView parent) {
        isEditMode = true;
        int visibleChildCount = parent.getChildCount();
        for (int i = 0; i < visibleChildCount; i++) {
            View view = parent.getChildAt(i);
            ImageView imgEdit = (ImageView) view.findViewById(R.id.channel_edit);
            ImageView channel_lock = (ImageView) view.findViewById(R.id.channel_lock);
            if (imgEdit != null) {
                if (mMyChannelItems.get(i - COUNT_PRE_MY_HEADER).getIsSolidShow() == 1) {
                    channel_lock.setVisibility(View.VISIBLE);
                    imgEdit.setVisibility(View.GONE);
                } else {
                    imgEdit.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * 完成编辑模式
     *
     * @param parent
     */
    public void cancelEditMode(RecyclerView parent) {
        isEditMode = false;
        int visibleChildCount = parent.getChildCount();
        for (int i = 0; i < visibleChildCount; i++) {
            View view = parent.getChildAt(i);
            ImageView imgEdit = (ImageView) view.findViewById(R.id.channel_edit);
            ImageView channel_lock = (ImageView) view.findViewById(R.id.channel_lock);
            if (imgEdit != null) {
                imgEdit.setVisibility(View.GONE);
                channel_lock.setVisibility(View.GONE);
            }
        }
    }

    public List<NavigationItemBean> getMyChannelItems() {
        return mMyChannelItems;
    }

    public void setData(List<NavigationItemBean> data) {
        this.mMyChannelItems = data;
        notifyDataSetChanged();
    }

    public interface OnMyChannelItemClickListener {
        void onItemClick(View v, int position);

        //删除item数据的回调
        void onItemDeleteClick(int position);
    }

    public void setOnMyChannelItemClickListener(OnMyChannelItemClickListener listener) {
        this.mChannelItemClickListener = listener;
    }

    /**
     * 我的频道
     */
    class MyViewHolder extends RecyclerView.ViewHolder implements OnDragVHListener {
        private TextView textView;
        private ImageView imgEdit, imgBg, channel_lock;
        private RelativeLayout userl_relativelayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.channel_text);
            imgEdit = (ImageView) itemView.findViewById(R.id.channel_edit);
            imgBg = (ImageView) itemView.findViewById(R.id.channel_bg);
            channel_lock = (ImageView) itemView.findViewById(R.id.channel_lock);
            userl_relativelayout = (RelativeLayout) itemView.findViewById(R.id.channel_relativelayout);
        }

        /**
         * item 被选中时
         */
        @Override
        public void onItemSelected() {
            TranslateAnimation animation = new TranslateAnimation(0, -5, 0, 0);
            animation.setInterpolator(new OvershootInterpolator());
            animation.setDuration(100);
            animation.setRepeatCount(5);
            animation.setRepeatMode(Animation.REVERSE);
            textView.startAnimation(animation);
            imgBg.startAnimation(animation);
        }

        /**
         * item 取消选中时
         */
        @Override
        public void onItemFinish() {
        }
    }

    /**
     * 我的频道  标题部分
     */
    class MyChannelHeaderViewHolder extends RecyclerView.ViewHolder {
        public MyChannelHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
