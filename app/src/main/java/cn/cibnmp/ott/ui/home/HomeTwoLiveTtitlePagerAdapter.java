package cn.cibnmp.ott.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ta.utdid2.android.utils.StringUtils;

import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.categoryList.HomeOnItemClickListener;
import cn.cibnmp.ott.utils.img.ImageFetcher;

/**
 * 直播页面-头部轮播图片适配器
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2017/9/27
 */
public class HomeTwoLiveTtitlePagerAdapter extends PagerAdapter {
	private List<View> mListViews;
	private List<NavigationInfoItemBean> loopContentsList; //滚动图片
	private HomeOnItemClickListener onItemClickListener;
	private Context context;

	public HomeTwoLiveTtitlePagerAdapter(List<View> listViews, Context context, List<NavigationInfoItemBean> loopContentsList,
										 HomeOnItemClickListener onItemClickListener) {
		mListViews = listViews;
		this.context = context;
		this.loopContentsList = loopContentsList;
		this.onItemClickListener = onItemClickListener;
        if (listViews.size() == 2) {
            for (int i = 0; i < loopContentsList.size(); i++) {
                TiltleAdvertisementView iv = new TiltleAdvertisementView(context);
                iv.setFocusable(true);
				if (!StringUtils.isEmpty(loopContentsList.get(i).getImgh())) {
					ImageFetcher.getInstance().loadImage(loopContentsList.get(i).getImgh(), iv.getImg());
				} else if (!StringUtils.isEmpty(loopContentsList.get(i).getImg())) {
					ImageFetcher.getInstance().loadImage(loopContentsList.get(i).getImg(), iv.getImg());
				}

                iv.getTVname().setText(loopContentsList.get(i).getDisplayName());
                mListViews.add(iv);
            }
        }

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (mListViews == null || mListViews.size() == 0) {
			return;
		}
		position = position % mListViews.size();
		if (container.getChildCount() > 3) {
			container.removeView(mListViews.get(position));
		}
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		if (mListViews == null || mListViews.size() == 0) {
			return null;
		}
		View view = mListViews.get(position % mListViews.size());
		// 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
		ViewParent vp = view.getParent();
		if (vp != null) {
			ViewGroup parent = (ViewGroup) vp;
			parent.removeView(view);
		}
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onItemClickListener == null) {
					return;
				}

                if (loopContentsList.size() == 2) {
					onItemClickListener.onItemClickListener(
							getBundleInfo(loopContentsList.get(position % 2),
									loopContentsList.get(position % 2).getContentId(),
									loopContentsList.get(position % 2).getAction(),
									loopContentsList.get(position % 2).getState() + "")
							);

                } else {
					onItemClickListener.onItemClickListener(
							getBundleInfo(loopContentsList.get(position % mListViews.size()),
									loopContentsList.get(position % mListViews.size()).getContentId(),
									loopContentsList.get(position % mListViews.size()).getAction(),
									loopContentsList.get(position % mListViews.size()).getState() + "")
					);
                }
			}
		});
		container.addView(view);
		return view;
	}

	/**
	 * 获取页面参数
	 */
	private Bundle getBundleInfo(NavigationInfoItemBean navigationInfoItemBean, String contentid, String action, String status) {
		Bundle bundle = new Bundle();
		bundle.putString(Constant.BUNDLE_CONTENTID, contentid);
		bundle.putString(Constant.BUNDLE_ACTION, action);
		bundle.putString(Constant.BUNDLE_STATES, status);
		bundle.putSerializable(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN, navigationInfoItemBean);
		return bundle;
	}

	@Override
	public int getCount() {
		// return mListViews.size();
		// 设置成最大，使用户看不到边界
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
}
