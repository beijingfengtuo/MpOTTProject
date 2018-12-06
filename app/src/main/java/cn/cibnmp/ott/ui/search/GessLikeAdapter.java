package cn.cibnmp.ott.ui.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.utils.img.ImageFetcher;

/**
 * Created by geshuaipeng on 2017/12/27.
 */

public class GessLikeAdapter  extends RecyclerView.Adapter<GessLikeAdapter.ViewHolder> implements View.OnClickListener{

    private List<GessListBean.DataBean.IndexContentsBean>  listData;
    private Context context;
    private LayoutInflater inf;
    //子view是否充满了手机屏幕
    private boolean isCompleteFill = false;

    public GessLikeAdapter(Context context,List<GessListBean.DataBean.IndexContentsBean> listData){
        this.context = context;
        this.listData = listData;
        inf = LayoutInflater.from(context);
    }

    @Override
    public void onClick(View view) {

    }

    public interface OnRecyclerViewItemListener {
        public void onItemClickListener(View view, int position);

        public void onItemLongClickListener(View view, int position);
    }
    private OnRecyclerViewItemListener mOnRecyclerViewItemListener;

    public void setOnRecyclerViewItemListener(OnRecyclerViewItemListener listener) {
        mOnRecyclerViewItemListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = inf.inflate(R.layout.gesslike_adapter, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (viewHolder == null) {
            return;
        }
        if (mOnRecyclerViewItemListener != null) {
            itemOnClick(viewHolder);
            itemOnLongClick(viewHolder);
        }
        viewHolder.textView.setText(listData.get(position).getName());
        if (listData.get(position).getImg() ==  null){
            ImageFetcher.getInstance().loodingImage(listData.get(position).getImgh(), viewHolder.imageView, R.color.home_bg_color);
        }else {
            ImageFetcher.getInstance().loodingImage(listData.get(position).getImg(), viewHolder.imageView, R.color.home_bg_color);
        }

    }

    //单机事件
    private void itemOnClick(final RecyclerView.ViewHolder holder) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                mOnRecyclerViewItemListener.onItemClickListener(holder.itemView, position);
            }
        });
    }
    //长按事件
    private void itemOnLongClick(final RecyclerView.ViewHolder holder) {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getLayoutPosition();
                mOnRecyclerViewItemListener.onItemLongClickListener(holder.itemView, position);
                //返回true是为了防止触发onClick事件
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    //自定义的ViewHolder,减少findViewById调用次数
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        //在布局中找到所含有的UI组件
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.fragment__internetlog_recycler_item_textview);
            imageView = (ImageView) itemView.findViewById(R.id.fragment__internetlog_recycler_item_iamgeview);
        }
    }

}
