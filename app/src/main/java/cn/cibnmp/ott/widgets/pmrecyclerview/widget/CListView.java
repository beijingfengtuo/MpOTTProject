package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import cn.cibnmp.ott.utils.DisplayUtils;

public class CListView extends ListView {

	private boolean firstMeasure = true;

	public CListView(Context context) {
		super(context);
	}

	public CListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onAttachedToWindow() {
		if (firstMeasure) {
			android.view.ViewGroup.LayoutParams lp = getLayoutParams();
			if (lp != null
					&& lp instanceof android.widget.FrameLayout.LayoutParams) {
				android.widget.FrameLayout.LayoutParams flp = (android.widget.FrameLayout.LayoutParams) lp;
				flp.width = DisplayUtils.getValue(flp.width);
				flp.height = DisplayUtils.getValue(flp.height);
				flp.leftMargin = DisplayUtils.getValue(flp.leftMargin);
				flp.topMargin = DisplayUtils.getValue(flp.topMargin);
				flp.bottomMargin = DisplayUtils.getValue(flp.bottomMargin);
				flp.rightMargin = DisplayUtils.getValue(flp.rightMargin);
				setPadding(DisplayUtils.getValue(getPaddingLeft()),
						DisplayUtils.getValue(getPaddingTop()),
						DisplayUtils.getValue(getPaddingRight()),
						DisplayUtils.getValue(getPaddingBottom()));
				setLayoutParams(flp);
			} else if (lp != null
					&& lp instanceof android.widget.LinearLayout.LayoutParams) {
				android.widget.LinearLayout.LayoutParams llp = (android.widget.LinearLayout.LayoutParams) lp;
				llp.width = DisplayUtils.getValue(llp.width);
				llp.height = DisplayUtils.getValue(llp.height);
				llp.leftMargin = DisplayUtils.getValue(llp.leftMargin);
				llp.topMargin = DisplayUtils.getValue(llp.topMargin);
				llp.bottomMargin = DisplayUtils.getValue(llp.bottomMargin);
				llp.rightMargin = DisplayUtils.getValue(llp.rightMargin);
				setPadding(DisplayUtils.getValue(getPaddingLeft()),
						DisplayUtils.getValue(getPaddingTop()),
						DisplayUtils.getValue(getPaddingRight()),
						DisplayUtils.getValue(getPaddingBottom()));
				setLayoutParams(llp);
			} else if (lp != null
					&& lp instanceof android.widget.RelativeLayout.LayoutParams) {
				android.widget.RelativeLayout.LayoutParams rlp = (android.widget.RelativeLayout.LayoutParams) lp;
				rlp.width = DisplayUtils.getValue(rlp.width);
				rlp.height = DisplayUtils.getValue(rlp.height);
				rlp.leftMargin = DisplayUtils.getValue(rlp.leftMargin);
				rlp.topMargin = DisplayUtils.getValue(rlp.topMargin);
				rlp.bottomMargin = DisplayUtils.getValue(rlp.bottomMargin);
				rlp.rightMargin = DisplayUtils.getValue(rlp.rightMargin);
				setPadding(DisplayUtils.getValue(getPaddingLeft()),
						DisplayUtils.getValue(getPaddingTop()),
						DisplayUtils.getValue(getPaddingRight()),
						DisplayUtils.getValue(getPaddingBottom()));
				setLayoutParams(rlp);
			} else if (lp != null
					&& lp instanceof android.widget.AbsoluteLayout.LayoutParams) {
				android.widget.AbsoluteLayout.LayoutParams alp = (android.widget.AbsoluteLayout.LayoutParams) lp;
				alp.width = DisplayUtils.getValue(alp.width);
				alp.height = DisplayUtils.getValue(alp.height);
				setPadding(DisplayUtils.getValue(getPaddingLeft()),
						DisplayUtils.getValue(getPaddingTop()),
						DisplayUtils.getValue(getPaddingRight()),
						DisplayUtils.getValue(getPaddingBottom()));
				setLayoutParams(alp);
			} else if (lp != null
					&& lp instanceof android.widget.TableLayout.LayoutParams) {
				android.widget.TableLayout.LayoutParams tlp = (android.widget.TableLayout.LayoutParams) lp;
				tlp.width = DisplayUtils.getValue(tlp.width);
				tlp.height = DisplayUtils.getValue(tlp.height);
				tlp.leftMargin = DisplayUtils.getValue(tlp.leftMargin);
				tlp.topMargin = DisplayUtils.getValue(tlp.topMargin);
				tlp.bottomMargin = DisplayUtils.getValue(tlp.bottomMargin);
				tlp.rightMargin = DisplayUtils.getValue(tlp.rightMargin);
				setPadding(DisplayUtils.getValue(getPaddingLeft()),
						DisplayUtils.getValue(getPaddingTop()),
						DisplayUtils.getValue(getPaddingRight()),
						DisplayUtils.getValue(getPaddingBottom()));
				setLayoutParams(tlp);
			}

			setDividerHeight(DisplayUtils.getValue(getDividerHeight()));
			firstMeasure = false;
		}
		super.onAttachedToWindow();
	}

}
