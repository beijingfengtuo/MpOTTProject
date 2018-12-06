/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;


import cn.cibnhp.grand.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class TabPageIndicator extends HorizontalScrollView implements PageIndicator {
	/** Title text used when no title is provided by the adapter. */
	private static final CharSequence EMPTY_TITLE = "";

	/**
	 * Interface for a callback when the selected tab has been reselected.
	 */
	public interface OnTabReselectedListener {
		/**
		 * Callback when the selected tab has been reselected.
		 * 
		 * @param position
		 *            Position of the current center item.
		 */
		void onTabReselected(int position);
	}

	public interface OnTabChangeListener {
		void onTabChange(boolean isTabChanged, int position);
	}

	private OnTabChangeListener tabChangeListener;

	private Runnable mTabSelector;

	private final OnClickListener mTabClickListener = new OnClickListener() {
		public void onClick(View view) {
			TabView tabView = (TabView) view;
			final int oldSelected = mViewPager.getCurrentItem();
			final int newSelected = tabView.getIndex();
			mViewPager.setCurrentItem(newSelected);
			if (oldSelected == newSelected && mTabReselectedListener != null) {
				mTabReselectedListener.onTabReselected(newSelected);
			}
		}
	};

	private final IcsLinearLayout mTabLayout;

	private ViewPager mViewPager;
	private ViewPager.OnPageChangeListener mListener;

	private int mMaxTabWidth;
	private int mPreSelectedTabIndex = -1;
	private int mSelectedTabIndex;
	private int mItemWidth = 100;

	private OnTabReselectedListener mTabReselectedListener;

	private int focusType = 0; // 0代表万琪样式，1代表沈飞样式

	public TabPageIndicator(Context context) {
		this(context, null);
	}

	public TabPageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		setHorizontalScrollBarEnabled(false);

		mTabLayout = new IcsLinearLayout(context,
				R.attr.vpiTabPageIndicatorStyle);
		addView(mTabLayout, new ViewGroup.LayoutParams(MATCH_PARENT,
				MATCH_PARENT));
	}

	public void setOnTabReselectedListener(OnTabReselectedListener listener) {
		mTabReselectedListener = listener;
	}

	public void setOnTabChangeListener(OnTabChangeListener listener) {
		tabChangeListener = listener;
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
		setFillViewport(lockedExpanded);

		final int childCount = mTabLayout.getChildCount();
		if (childCount > 1
				&& (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
			if (childCount > 2) {
				mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
			} else {
				mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
			}
		} else {
			mMaxTabWidth = -1;
		}

		final int oldWidth = getMeasuredWidth();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int newWidth = getMeasuredWidth();

		if (lockedExpanded && oldWidth != newWidth) {
			// Recenter the tab display if we're at a new (scrollable) size.
			setCurrentItem(mSelectedTabIndex);
		}
	}

	public int getChildTotal() {
		return mTabLayout.getChildCount();
	}

	private void animateToTab(final int position) {
		final View tabView = mTabLayout.getChildAt(position);
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
		mTabSelector = new Runnable() {
			public void run() {
				final int scrollPos = tabView.getLeft()
						- (getWidth() - tabView.getWidth()) / 2;
				smoothScrollTo(scrollPos, 0);
				mTabSelector = null;
			}
		};
		post(mTabSelector);
	}

	public void setItemWidth(int itemwidth) {
		// android.widget.LinearLayout.LayoutParams lp;
		// for (int i = 0; i < mTabLayout.getChildCount(); i++) {
		// View v = mTabLayout.getChildAt(i);
		// lp = (android.widget.LinearLayout.LayoutParams) v.getLayoutParams();
		// lp.width = itemwidth;
		// v.setLayoutParams(lp);
		//
		// }
		mItemWidth = itemwidth;
	}

	private int align = Gravity.CENTER;

	public void setTextAlign(int align) {
		this.align = align;
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mTabSelector != null) {
			// Re-post the selector we saved
			post(mTabSelector);
		}
	}

	public void setSelectedTabColor() {
		if (mPreSelectedTabIndex != -1) {
			((TabView) mTabLayout.getChildAt(mPreSelectedTabIndex))
					.setTextColor(unfocusedColor);
		}
		((TabView) mTabLayout.getChildAt(mSelectedTabIndex))
				.setTextColor(focusedColor);
		mPreSelectedTabIndex = mSelectedTabIndex;
	}

	// 沈飞处理逻辑需要 额外添加的方法
	public void setSelectedTabColor_S() {
		if (hasFocus()) {
			if (mPreSelectedTabIndex != -1) {
				((TabView) mTabLayout.getChildAt(mPreSelectedTabIndex))
						.setTextColor(unfocusedColor);
			}
			((TabView) mTabLayout.getChildAt(mSelectedTabIndex))
					.setTextColor(focusedColor);
			mPreSelectedTabIndex = mSelectedTabIndex;
		} else {
			if (mPreSelectedTabIndex != -1) {
				((TabView) mTabLayout.getChildAt(mPreSelectedTabIndex))
						.setTextColor(unfocusedColor);
			}
			((TabView) mTabLayout.getChildAt(mSelectedTabIndex))
					.setTextColor(selectColor);
			mPreSelectedTabIndex = mSelectedTabIndex;
		}
	}

	// 沈飞处理逻辑需要 额外添加的方法
	public void setSelectedTabColor_D() {
		((TabView) mTabLayout.getChildAt(mSelectedTabIndex))
				.setTextColor(selectColor);
	}

	public void setFocus(int position) {
		if (position >= mTabLayout.getChildCount()) {
			Log.e("TabPageIndicator", "the position index out of bounds.");
			return;
		}
		if (mTabLayout != null && position < getChildTotal())
			mTabLayout.getChildAt(position).requestFocus();
	}

	public void setFocusType(int f) {
		focusType = f;
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
	}

	private OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				if (focusedColor != Integer.MIN_VALUE && v instanceof TabView) {
					if (focusType == 0) {
						((TabView) v).setTextColor(focusedColor);
					} else {
						setSelectedTabColor_S();
					}
				}
				// AnimUtils.startEnlargeScaleAnimation(v);
			} else {
				if (unfocusedColor != Integer.MIN_VALUE && v instanceof TabView) {
					if (focusType == 0) {
						((TabView) v).setTextColor(unfocusedColor);
					} else {
						setSelectedTabColor_D();
					}
				}
				// AnimUtils.startNarrowScaleAnimation(v);
			}
			if (mTabLayout != null) {
				for (int i = 0; i < mTabLayout.getChildCount(); i++) {
					if (i != mSelectedTabIndex
							&& mTabLayout.getChildAt(i).equals(v)) {
						if (tabChangeListener != null) {
							tabChangeListener.onTabChange(true, i);
						}
						setCurrentItem(i);
						// Toast.makeText(getContext(), "" + i * 1111111,
						// Toast.LENGTH_SHORT).show();
					}
				}
			}

		}
	};

	private void addTab(int index, CharSequence text, int iconResId) {
		final TabView tabView = new TabView(getContext());
		tabView.mIndex = index;
		tabView.setFocusable(true);
		tabView.setOnClickListener(mTabClickListener);
		tabView.setText(text);
		tabView.setTextColor(unfocusedColor);
		tabView.setGravity(align);
		tabView.setOnFocusChangeListener(focusChangeListener);

		if (iconResId != 0) {
			tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
		}

		mTabLayout.addView(tabView, new LinearLayout.LayoutParams(mItemWidth,
				MATCH_PARENT));
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		if (mListener != null) {
			mListener.onPageScrollStateChanged(arg0);
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if (mListener != null) {
			mListener.onPageScrolled(arg0, arg1, arg2);
		}
	}

	private int focusedColor = Integer.MIN_VALUE;
	private int unfocusedColor = Integer.MIN_VALUE;
	private int selectColor = Integer.MIN_VALUE;

	public void setTextColor(int focus, int unfocused) {
		this.focusedColor = focus;
		this.unfocusedColor = unfocused;
	}

	public void setTextSelectColor(int color) {
		this.selectColor = color;
	}

	@Override
	public void onPageSelected(int arg0) {
		setCurrentItem(arg0);
		if (mListener != null) {
			mListener.onPageSelected(arg0);
		}
	}

	@Override
	public void setViewPager(ViewPager view) {
		if (mViewPager == view) {
			return;
		}
		if (mViewPager != null) {
			mViewPager.setOnPageChangeListener(null);
		}
		final PagerAdapter adapter = view.getAdapter();
		if (adapter == null) {
			throw new IllegalStateException(
					"ViewPager does not have adapter instance.");
		}
		mViewPager = view;
		view.setOnPageChangeListener(this);
		notifyDataSetChanged();
	}

	public void notifyDataSetChanged() {
		mTabLayout.removeAllViews();
		PagerAdapter adapter = mViewPager.getAdapter();
		IconPagerAdapter iconAdapter = null;
		if (adapter instanceof IconPagerAdapter) {
			iconAdapter = (IconPagerAdapter) adapter;
		}
		final int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			CharSequence title = adapter.getPageTitle(i);
			if (title == null) {
				title = EMPTY_TITLE;
			}
			int iconResId = 0;
			if (iconAdapter != null) {
				iconResId = iconAdapter.getIconResId(i);
			}
			addTab(i, title, iconResId);
		}
		if (mSelectedTabIndex > count) {
			mSelectedTabIndex = count - 1;
		}
		setCurrentItem(mSelectedTabIndex);
		requestLayout();
	}

	@Override
	public void setViewPager(ViewPager view, int initialPosition) {
		setViewPager(view);
		setCurrentItem(initialPosition);
	}

	@Override
	public void setCurrentItem(int item) {
		if (mViewPager == null) {
			throw new IllegalStateException("ViewPager has not been bound.");
		}
		mSelectedTabIndex = item;
		mViewPager.setCurrentItem(item);

		final int tabCount = mTabLayout.getChildCount();
		for (int i = 0; i < tabCount; i++) {
			final View child = mTabLayout.getChildAt(i);
			final boolean isSelected = (i == item);
			child.setSelected(isSelected);
			if (isSelected) {
				animateToTab(item);
			}
		}
	}

	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mListener = listener;
	}

	private class TabView extends TextView {
		private int mIndex;

		public TabView(Context context) {
			super(context, null, R.attr.vpiTabPageIndicatorStyle);
		}

		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);

			// Re-measure if we went beyond our maximum size.
			if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
				super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth,
						MeasureSpec.EXACTLY), heightMeasureSpec);
			}
		}

		public int getIndex() {
			return mIndex;
		}
	}

}
