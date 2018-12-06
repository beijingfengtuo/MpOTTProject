package cn.cibnmp.ott.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.cibnmp.ott.bean.LayoutItem;
import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.ui.categoryList.HomeOnItemClickListener;
import cn.cibnmp.ott.ui.home.HomeOneViewHolderHelper;
import cn.cibnmp.ott.utils.DisplayUtils;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.CommonListHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.CommonListItemHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.CommonListView;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.CommonViewHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.SpaceViewHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.TagViewHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.TitleViewHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.ViewPagerViewHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.HomeSpannableGridLayoutManager;

/**
 * Created by yangwenwu on 18/1/19.
 */

public class HomeOneHoldersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LayoutItem> mLayoutItemList;
    private List<NavigationInfoItemBean> mLnfoItemBeanList;
    private boolean mDataVisible = false;
    private ViewPagerViewHolder viewpagerholder;
    private Context mContext;
    private HomeOnItemClickListener listener;
    private int fillWidthMargins = 0;

    public HomeOneHoldersAdapter(Context context, HomeOnItemClickListener listener) {
        this.mContext = context;
        this.listener = listener;
    }

    public void setData(List<LayoutItem> layoutItemList, List<NavigationInfoItemBean> infoItemBeanList) {
        this.mLayoutItemList = layoutItemList;
        this.mLnfoItemBeanList = infoItemBeanList;
    }

    public void setWidthMargins(int widthMargins) {
        fillWidthMargins = widthMargins;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {

            //(标题栏)
            case HomeOneViewType.title_viewType:
                viewHolder = new TitleViewHolder(parent);
                break;

            //(带副标题的标题栏)
            case HomeOneViewType.title_complete_viewType:
                viewHolder = new TitleViewHolder(parent);
                break;

            //(4张大图轮播)
            case HomeOneViewType.viewpager_viewType:
                viewHolder = new ViewPagerViewHolder(parent);
                break;

            //(只有四个按钮)
            case HomeOneViewType.bottom_viewType:
                viewHolder = new TagViewHolder(parent);
                break;

            //(四张小图,24号文字)
            case HomeOneViewType.common_viewType:
                viewHolder = new CommonViewHolder(parent);
                break;

            //(四张小图,24文字,22号文字)
            case HomeOneViewType.common_viewType1:
                viewHolder = new CommonListHolder(parent);
                break;

            //(四张小图,24文字,22号文字,时长)
            case HomeOneViewType.persontag_viewType:
                viewHolder = new SpaceViewHolder(parent);
                break;

            //(一张大图,24文字,)
            case HomeOneViewType.imagview_viewType:
                viewHolder = new CommonViewHolder(parent);
                break;


            //(一张大图,24文字,四张小图,24文字,22号文)
            case HomeOneViewType.commonList_viewType:
                viewHolder = new CommonListHolder(parent);
                break;

            //(三张长图,24文字)
            case HomeOneViewType.space_viewType:
                viewHolder = new CommonListView(parent);
                break;

            //(四张小图,24文字,24号文字)
            case HomeOneViewType.foot_viewType:
                viewHolder = new CommonListItemHolder(parent);
                break;

            default:
                viewHolder = new CommonViewHolder(parent);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (mLnfoItemBeanList != null && mLnfoItemBeanList.size() >
                0 && position < mLnfoItemBeanList.size()) {
            return mLnfoItemBeanList.get(position).getViewtype();
        }
        return 0;
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        mDataVisible = true;

        try {
            final View itemView = holder.itemView;
            NavigationInfoItemBean infoItemBean = mLnfoItemBeanList.get(position);
            int type = getItemViewType(position);
            setItemSpans(itemView, mLayoutItemList.get(position), type);
            switch (type) {

                //(标题栏)
                case HomeOneViewType.title_viewType:
                    HomeOneViewHolderHelper.ViewHolderHelper0(
                            position, (TitleViewHolder) holder, infoItemBean, listener);
                    break;

                //(带副标题的标题栏)
                case HomeOneViewType.title_complete_viewType:
                    HomeOneViewHolderHelper.ViewHolderHelper0(
                            position, (TitleViewHolder) holder, infoItemBean, listener);
                    break;

                //(4张大图轮播)
                case HomeOneViewType.viewpager_viewType:
                    viewpagerholder = (ViewPagerViewHolder) holder;
                    HomeOneViewHolderHelper.ViewHolderHelper1(mContext,
                            mDataVisible, (ViewPagerViewHolder) holder, infoItemBean, listener);
                    break;

                //(只有四个按钮)
                case HomeOneViewType.bottom_viewType:
                    HomeOneViewHolderHelper.ViewHolderHelper2
                            ((TagViewHolder) holder, infoItemBean, listener);
                    break;

                //(四张小图,24号文字)
                case HomeOneViewType.common_viewType:
                    HomeOneViewHolderHelper.ViewHolderHelper3(mContext,
                            mDataVisible, (CommonViewHolder) holder, infoItemBean,
                            mLayoutItemList.get(position), listener);
                    break;

                //(四张小图,24文字,22号文字)
                case HomeOneViewType.common_viewType1:
                    HomeOneViewHolderHelper.ViewHolderHelper4(mContext,
                            mDataVisible, (CommonListHolder) holder, infoItemBean,
                            mLayoutItemList.get(position), listener);
                    break;

                //(四张小图,24文字,22号文字,时长)
                case HomeOneViewType.persontag_viewType:
                    HomeOneViewHolderHelper.ViewHolderHelper5(mContext, mDataVisible,
                            (SpaceViewHolder) holder, infoItemBean, mLayoutItemList.get(position), listener);
                    break;

                //(一张大图,24文字)
                case HomeOneViewType.imagview_viewType:
                    HomeOneViewHolderHelper.ViewHolderHelper6(mContext, mDataVisible,
                            (CommonViewHolder) holder, infoItemBean, mLayoutItemList.get(position), listener);
                    break;

                //(一张大图,24文字,四张小图,24文字,24号文字)
                case HomeOneViewType.commonList_viewType:
                    HomeOneViewHolderHelper.ViewHolderHelper7(mContext,
                            mDataVisible, (CommonListHolder) holder, infoItemBean,
                            mLayoutItemList.get(position), listener);
                    break;

                //(三张长图,24文字)
                case HomeOneViewType.space_viewType:
                    HomeOneViewHolderHelper.ViewHolderHelper8(mContext, mDataVisible,
                            (CommonListView) holder, infoItemBean, mLayoutItemList.get(position), listener);
                    break;

                //(四张小图,24文字,24号文字)
                case HomeOneViewType.foot_viewType:
                    HomeOneViewHolderHelper.ViewHolderHelper9(mContext,
                            mDataVisible, (CommonListItemHolder) holder,
                            infoItemBean, mLayoutItemList.get(position), listener);
                    break;
                default:
                    HomeOneViewHolderHelper.ViewHolderHelper3(mContext,
                            mDataVisible, (CommonViewHolder) holder, infoItemBean,
                            mLayoutItemList.get(position), listener);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    /**
     * 设置item的width，height
     *
     * @param view
     * @param layoutItem
     */
    private void setItemSpans(View view, LayoutItem layoutItem, int type) {
        final HomeSpannableGridLayoutManager.LayoutParams lp =
                (HomeSpannableGridLayoutManager.LayoutParams) view.getLayoutParams();
        double colSpan = layoutItem.getC();
        double rowSpan = layoutItem.getR();

        if (type == HomeOneViewType.title_viewType) {
            rowSpan = rowSpan + DisplayUtils.getValue(4);
        } else if (type == HomeOneViewType.viewpager_viewType && fillWidthMargins > 0) {
            view.setPadding(-fillWidthMargins, -fillWidthMargins, -fillWidthMargins, -fillWidthMargins);
//            colSpan = colSpan / 1.5;
            rowSpan = rowSpan / 1.2;
        }

        if (lp.rowSpan != rowSpan || lp.colSpan != colSpan) {
            lp.rowSpan = rowSpan;
            lp.colSpan = colSpan;
        }
        view.setLayoutParams(lp);
    }

    @Override
    public int getItemCount() {
        int size = mLayoutItemList.size() > mLnfoItemBeanList.size()
                ? mLnfoItemBeanList.size() : mLayoutItemList.size();
        return size;
    }

    public void startViewpage() {
        if (viewpagerholder != null) {
            viewpagerholder.startSwitch();
        }
    }

    public void stopViewpage() {
        if (viewpagerholder != null) {
            viewpagerholder.stopSwitch();
        }
    }
}


