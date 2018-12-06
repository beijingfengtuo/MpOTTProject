package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.utils.DisplayUtils;

public class CLinearLayout extends LinearLayout {

	private boolean firstMeasure = true;
	private List<Integer> programList = new ArrayList<Integer>();
	private HashMap<Integer, Integer> programList2 = new HashMap<Integer, Integer>();

	public CLinearLayout(Context context) {
		super(context);
	}

	public CLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		inial(context, attrs);
	}

	public CLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inial(context, attrs);
	}

	private void inial(Context context, AttributeSet attrs) {
		programList.clear();
		/* 这里取得declare-styleable集合 */
		TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.CButtonAttr);
		boolean layout_alignParentRight = typeArray.getBoolean(R.styleable.CButtonAttr_layout_alignParentRight, false);
		if (layout_alignParentRight == true) {
			programList.add(RelativeLayout.ALIGN_PARENT_RIGHT);
		}
		boolean layout_alignParentLeft = typeArray.getBoolean(R.styleable.CButtonAttr_layout_alignParentLeft, false);
		if (layout_alignParentLeft == true) {
			programList.add(RelativeLayout.ALIGN_PARENT_LEFT);
		}
		boolean layout_alignParentTop = typeArray.getBoolean(R.styleable.CButtonAttr_layout_alignParentTop, false);
		if (layout_alignParentTop == true) {
			programList.add(RelativeLayout.ALIGN_PARENT_TOP);
		}
		boolean layout_alignParentBottom = typeArray.getBoolean(R.styleable.CButtonAttr_layout_alignParentBottom, false);
		if (layout_alignParentBottom == true) {
			programList.add(RelativeLayout.ALIGN_PARENT_BOTTOM);
		}

		boolean layout_centerVertical = typeArray.getBoolean(R.styleable.CButtonAttr_layout_centerVertical, false);
		if (layout_centerVertical == true) {
			programList.add(RelativeLayout.CENTER_VERTICAL);
		}
		boolean layout_centerHorizontal = typeArray.getBoolean(R.styleable.CButtonAttr_layout_centerHorizontal, false);
		if (layout_centerHorizontal == true) {
			programList.add(RelativeLayout.CENTER_HORIZONTAL);
		}
		boolean layout_centerInParent = typeArray.getBoolean(R.styleable.CButtonAttr_layout_centerInParent, false);
		if (layout_centerInParent == true) {
			programList.add(RelativeLayout.CENTER_IN_PARENT);
		}
		Integer layout_below = typeArray.getResourceId(R.styleable.CButtonAttr_layout_below, 0);
		if (layout_below != 0) {
			programList2.put(RelativeLayout.BELOW, layout_below);
		}
		Integer layout_toLeftOf = typeArray.getResourceId(R.styleable.CButtonAttr_layout_toLeftOf, 0);
		if (layout_toLeftOf != 0) {
			programList2.put(RelativeLayout.LEFT_OF, layout_toLeftOf);
		}
		int layout_toRightOf = typeArray.getResourceId(R.styleable.CButtonAttr_layout_toRightOf, 0);
		if (layout_toRightOf != 0) {
			programList2.put(RelativeLayout.RIGHT_OF, layout_toRightOf);
		}
		Integer layout_above = typeArray.getResourceId(R.styleable.CButtonAttr_layout_above, 0);
		if (layout_above != 0) {
			programList2.put(RelativeLayout.ABOVE, layout_above);
		}

		typeArray.recycle();
	}

	@Override
	protected void onAttachedToWindow() {
		if (firstMeasure) {
			android.view.ViewGroup.LayoutParams lp = getLayoutParams();
			if (lp != null && lp instanceof android.widget.FrameLayout.LayoutParams) {
				android.widget.FrameLayout.LayoutParams flp = (android.widget.FrameLayout.LayoutParams) lp;
				if (flp.width > 0) {
					flp.width = DisplayUtils.getAdaptValue(flp.width);
				}
				if (flp.height > 0) {
					flp.height = DisplayUtils.getAdaptValue(flp.height);
				}
				flp.leftMargin = DisplayUtils.getAdaptValue(flp.leftMargin);
				flp.topMargin = DisplayUtils.getAdaptValue(flp.topMargin);
				flp.bottomMargin = DisplayUtils.getAdaptValue(flp.bottomMargin);
				flp.rightMargin = DisplayUtils.getAdaptValue(flp.rightMargin);
				setPadding(DisplayUtils.getAdaptValue(getPaddingLeft()), DisplayUtils.getAdaptValue(getPaddingTop()), DisplayUtils.getAdaptValue(getPaddingRight()), DisplayUtils.getAdaptValue(getPaddingBottom()));
				setLayoutParams(flp);
			} else if (lp != null && lp instanceof android.widget.LinearLayout.LayoutParams) {
				android.widget.LinearLayout.LayoutParams llp = (android.widget.LinearLayout.LayoutParams) lp;
				if (llp.width > 0) {
					llp.width = DisplayUtils.getAdaptValue(llp.width);
				}
				if (llp.height > 0) {
					llp.height = DisplayUtils.getAdaptValue(llp.height);
				}
				llp.leftMargin = DisplayUtils.getAdaptValue(llp.leftMargin);
				llp.topMargin = DisplayUtils.getAdaptValue(llp.topMargin);
				llp.bottomMargin = DisplayUtils.getAdaptValue(llp.bottomMargin);
				llp.rightMargin = DisplayUtils.getAdaptValue(llp.rightMargin);
				setPadding(DisplayUtils.getAdaptValue(getPaddingLeft()), DisplayUtils.getAdaptValue(getPaddingTop()), DisplayUtils.getAdaptValue(getPaddingRight()), DisplayUtils.getAdaptValue(getPaddingBottom()));
				setLayoutParams(llp);
			} else if (lp != null && lp instanceof android.widget.RelativeLayout.LayoutParams) {
				android.widget.RelativeLayout.LayoutParams rlp = (android.widget.RelativeLayout.LayoutParams) lp;
				if (rlp.width > 0) {
					rlp.width = DisplayUtils.getAdaptValue(rlp.width);
				}
				if (rlp.height > 0) {
					rlp.height = DisplayUtils.getAdaptValue(rlp.height);
				}
				rlp.leftMargin = DisplayUtils.getAdaptValue(rlp.leftMargin);
				rlp.topMargin = DisplayUtils.getAdaptValue(rlp.topMargin);
				rlp.bottomMargin = DisplayUtils.getAdaptValue(rlp.bottomMargin);
				rlp.rightMargin = DisplayUtils.getAdaptValue(rlp.rightMargin);
				setPadding(DisplayUtils.getAdaptValue(getPaddingLeft()), DisplayUtils.getAdaptValue(getPaddingTop()), DisplayUtils.getAdaptValue(getPaddingRight()), DisplayUtils.getAdaptValue(getPaddingBottom()));
				for (int i = 0; i < programList.size(); i++) {
					rlp.addRule(programList.get(i));
				}
				if (programList2.size() > 0) {
					Iterator iter = programList2.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry entry = (Map.Entry) iter.next();
						int key = (Integer) entry.getKey();
						int val = (Integer) entry.getValue();
						rlp.addRule(key, val);
					}
				}
				setLayoutParams(rlp);
				programList.clear();
				programList2.clear();
			} else if (lp != null && lp instanceof android.widget.AbsoluteLayout.LayoutParams) {
				android.widget.AbsoluteLayout.LayoutParams alp = (android.widget.AbsoluteLayout.LayoutParams) lp;
				if (alp.width > 0) {
					alp.width = DisplayUtils.getAdaptValue(alp.width);
				}
				if (alp.height > 0) {
					alp.height = DisplayUtils.getAdaptValue(alp.height);
				}
				setPadding(DisplayUtils.getAdaptValue(getPaddingLeft()), DisplayUtils.getAdaptValue(getPaddingTop()), DisplayUtils.getAdaptValue(getPaddingRight()), DisplayUtils.getAdaptValue(getPaddingBottom()));
				setLayoutParams(alp);
			} else if (lp != null && lp instanceof android.widget.TableLayout.LayoutParams) {
				android.widget.TableLayout.LayoutParams tlp = (android.widget.TableLayout.LayoutParams) lp;
				if (tlp.width > 0) {
					tlp.width = DisplayUtils.getAdaptValue(tlp.width);
				}
				if (tlp.height > 0) {
					tlp.height = DisplayUtils.getAdaptValue(tlp.height);
				}
				tlp.leftMargin = DisplayUtils.getAdaptValue(tlp.leftMargin);
				tlp.topMargin = DisplayUtils.getAdaptValue(tlp.topMargin);
				tlp.bottomMargin = DisplayUtils.getAdaptValue(tlp.bottomMargin);
				tlp.rightMargin = DisplayUtils.getAdaptValue(tlp.rightMargin);
				setPadding(DisplayUtils.getAdaptValue(getPaddingLeft()), DisplayUtils.getAdaptValue(getPaddingTop()), DisplayUtils.getAdaptValue(getPaddingRight()), DisplayUtils.getAdaptValue(getPaddingBottom()));
				setLayoutParams(tlp);
			}
			firstMeasure = false;
		}
		super.onAttachedToWindow();
	}
}
