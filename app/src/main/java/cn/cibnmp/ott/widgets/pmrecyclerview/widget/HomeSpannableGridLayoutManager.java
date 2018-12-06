/*
 * Copyright (C) 2014 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.widgets.PmRecyclerView;
import cn.cibnmp.ott.widgets.pmrecyclerview.Lanes;
import cn.cibnmp.ott.widgets.pmrecyclerview.Lanes.LaneInfo;

public class HomeSpannableGridLayoutManager extends GridLayoutManager {
    public static final String LOGTAG = "SpannableGridLM";

    private static final int DEFAULT_NUM_COLS = 3;
    private static final int DEFAULT_NUM_ROWS = 3;

    public static class SpannableItemEntry extends ItemEntry {
        private final double colSpan;
        private final double rowSpan;

        public SpannableItemEntry(int startLane, int anchorLane, double colSpan, double rowSpan) {
            super(startLane, anchorLane);
            this.colSpan = colSpan;
            this.rowSpan = rowSpan;
        }

        public SpannableItemEntry(Parcel in) {
            super(in);
            this.colSpan = in.readInt();
            this.rowSpan = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
//            out.writeInt(colSpan);
//            out.writeInt(rowSpan);

            out.writeDouble(colSpan);
            out.writeDouble(rowSpan);
        }

        public static final Parcelable.Creator<SpannableItemEntry> CREATOR
                = new Parcelable.Creator<SpannableItemEntry>() {
            @Override
            public SpannableItemEntry createFromParcel(Parcel in) {
                return new SpannableItemEntry(in);
            }

            @Override
            public SpannableItemEntry[] newArray(int size) {
                return new SpannableItemEntry[size];
            }
        };
    }

    private boolean mMeasuring;

    public HomeSpannableGridLayoutManager(Context context) {
        this(context, null);
    }

    public HomeSpannableGridLayoutManager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeSpannableGridLayoutManager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, DEFAULT_NUM_COLS, DEFAULT_NUM_ROWS);
    }

    public HomeSpannableGridLayoutManager(Orientation orientation, int numColumns, int numRows) {
        super(orientation, numColumns, numRows);
    }

    private static int getLaneSpan(LayoutParams lp, boolean isVertical) {
        return (int) (isVertical ? lp.colSpan : lp.rowSpan);
    }

    private static int getLaneSpan(SpannableItemEntry entry, boolean isVertical) {
        return (int) (isVertical ? entry.colSpan : entry.rowSpan);
    }

    @Override
    public boolean canScrollHorizontally() {
        return super.canScrollHorizontally() && !mMeasuring;
    }

    @Override
    public boolean canScrollVertically() {
        return super.canScrollVertically() && !mMeasuring;
    }

    @Override
    public int getLaneSpanForChild(View child) {
        return getLaneSpan((LayoutParams) child.getLayoutParams(), isVertical());
    }

    @Override
    public int getLaneSpanForPosition(int position) {
        final SpannableItemEntry entry = (SpannableItemEntry) getItemEntryForPosition(position);
        if (entry == null) {
            // add by zhousuqiang
            View view = getChildAt(position - getFirstVisiblePosition());
            if (null != view) {
                return getLaneSpanForChild(view);
            }
            throw new IllegalStateException("Could not find span for position " + position);
        }

        return getLaneSpan(entry, isVertical());
    }

    // add by zhousuqiang
//    public int getColSpanForPosition(int position) {
//        final SpannableItemEntry entry = (SpannableItemEntry) getItemEntryForPosition(position);
//        if (entry == null) {
//            View view = getChildAt(position - getFirstVisiblePosition());
//            if(null != view) {
//                return getLaneSpan(entry, !isVertical());
//            }
//            throw new IllegalStateException("Could not find span for position " + position);
//        }
//
//        return getLaneSpan(entry, !isVertical());
//    }

    @Override
    public void getLaneForPosition(LaneInfo outInfo, int position, Direction direction) {
        final SpannableItemEntry entry = (SpannableItemEntry) getItemEntryForPosition(position);
        if (entry != null) {
            outInfo.set(entry.startLane, entry.anchorLane);
            return;
        }

        outInfo.setUndefined();
    }

    @Override
    protected void getLaneForChild(LaneInfo outInfo, View child, Direction direction) {
        super.getLaneForChild(outInfo, child, direction);
        if (outInfo.isUndefined()) {
            getLanes().findLane(outInfo, getLaneSpanForChild(child), direction);
        }
    }

    private int getChildWidth(int colSpan) {
        return (int) ((getLanes().getLaneSize()) * colSpan);
    }

    private int getChildWidth(double colSpan) {
        return (int) Math.ceil(getLanes().getLaneSize() * colSpan);
        //return (int)((getLanes().getLaneSize()) * colSpan);
    }

    private int getChildHeight(int rowSpan) {
        return (int) ((getLanes().getLaneSize()) * rowSpan);
    }

    private int getChildHeight(double rowSpan) {
        return (int) Math.ceil(getLanes().getLaneSize() * rowSpan);
        // return (int) ((getLanes().getLaneSize()) * rowSpan);
    }

    private int getWidthUsed(View child) {
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        return getWidth() - getPaddingLeft() - getPaddingRight() - getChildWidth(lp.colSpan);
    }


    private int getHeightUsed(View child) {
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        return getHeight() - getPaddingTop() - getPaddingBottom() - getChildHeight(lp.rowSpan);
    }

    @Override
    protected void measureChildWithMargins(View child) {
        // XXX: This will disable scrolling while measuring this child to ensure that
        // both width and height can use MATCH_PARENT properly.
        mMeasuring = true;

        final int laneCount = getLanes().getCount();

        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int decorated = 30;
        int laneSize;
        if (isVertical()) {
            int parentWidth = getWidth() - getPaddingLeft() - getPaddingRight() - decorated * (laneCount - 1);
            laneSize = parentWidth / laneCount;
        } else {
            int parentHeight = getHeight() - getPaddingTop() - getPaddingBottom() - decorated * (laneCount - 1);
            laneSize = parentHeight / laneCount;
        }
//        lp.width = laneSize * lp.colSpan + decorated * (lp.colSpan - 1);
//        lp.height = laneSize * lp.rowSpan + decorated * (lp.rowSpan - 1);

        getLaneForChild(mTempLaneInfo, child, Direction.END);
        final int lane = mTempLaneInfo.startLane;
        final int laneSpan = getLaneSpan(lp, isVertical());

//        final int itemCount = getItemCount();

//        final boolean isVertical = isVertical();

//        final boolean firstLane = (lane == 0);
//        final boolean secondLane = isSecondLane(lm, itemPosition, lane);

        final boolean lastLane = (lane + laneSpan == laneCount);
//        final boolean beforeLastLane = (lane + laneSpan == laneCount - 1);

//        if(!lastLane) {
//            lp.height = laneSize * lp.rowSpan + decorated * (lp.rowSpan - 1);
//        }

        measureChildWithMargins(child, getWidthUsed(child), getHeightUsed(child));
        mMeasuring = false;
    }

    @Override
    protected void moveLayoutToPosition(int position, int offset, Recycler recycler, State state) {
        final boolean isVertical = isVertical();
        final Lanes lanes = getLanes();

        lanes.reset(0);

        for (int i = 0; i <= position; i++) {
            SpannableItemEntry entry = (SpannableItemEntry) getItemEntryForPosition(i);
            if (entry == null) {
                final View child = recycler.getViewForPosition(i);
                entry = (SpannableItemEntry) cacheChildLaneAndSpan(child, Direction.END);
            }

            mTempLaneInfo.set(entry.startLane, entry.anchorLane);

            // The lanes might have been invalidated because an added or
            // removed item. See BaseLayoutManager.invalidateItemLanes().
            if (mTempLaneInfo.isUndefined()) {
                lanes.findLane(mTempLaneInfo, getLaneSpanForPosition(i), Direction.END);
                entry.setLane(mTempLaneInfo);
            }

            lanes.getChildFrame(mTempRect, getChildWidth(entry.colSpan),
                    getChildHeight(entry.rowSpan), mTempLaneInfo, Direction.END);

            if (i != position) {
                pushChildFrame(entry, mTempRect, entry.startLane, getLaneSpan(entry, isVertical),
                        Direction.END);
            }
        }

        lanes.getLane(mTempLaneInfo.startLane, mTempRect);
        lanes.reset(Direction.END);
        lanes.offset(offset - (isVertical ? mTempRect.bottom : mTempRect.right));
    }

    @Override
    protected ItemEntry cacheChildLaneAndSpan(View child, Direction direction) {
        final int position = getPosition(child);

        mTempLaneInfo.setUndefined();

        SpannableItemEntry entry = (SpannableItemEntry) getItemEntryForPosition(position);
        if (entry != null) {
            mTempLaneInfo.set(entry.startLane, entry.anchorLane);
        }

        if (mTempLaneInfo.isUndefined()) {
            getLaneForChild(mTempLaneInfo, child, direction);
        }

        if (entry == null) {
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            entry = new SpannableItemEntry(mTempLaneInfo.startLane, mTempLaneInfo.anchorLane,
                    lp.colSpan, lp.rowSpan);
            setItemEntryForPosition(position, entry);
        } else {
            entry.setLane(mTempLaneInfo);
        }

        return entry;
    }

    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        super.checkLayoutParams(lp);
        if (lp.width != LayoutParams.MATCH_PARENT ||
                lp.height != LayoutParams.MATCH_PARENT) {
            return false;
        }

        if (lp instanceof LayoutParams) {
            final LayoutParams spannableLp = (LayoutParams) lp;

            if (isVertical()) {
                return (spannableLp.rowSpan >= 1 && spannableLp.colSpan >= 1 &&
                        spannableLp.colSpan <= getLaneCount());
            } else {
                return (spannableLp.colSpan >= 1 && spannableLp.rowSpan >= 1 &&
                        spannableLp.rowSpan <= getLaneCount());
            }
        }

        return false;
    }

    @Override
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        LayoutParams layoutParams = null;
        if (lp instanceof MarginLayoutParams) {
            layoutParams = new LayoutParams((MarginLayoutParams) lp);
        } else {
            layoutParams = new LayoutParams(lp);
        }
        final LayoutParams spannableLp = layoutParams;
        spannableLp.width = LayoutParams.MATCH_PARENT;
        spannableLp.height = LayoutParams.MATCH_PARENT;

        if (lp instanceof LayoutParams) {
            final LayoutParams other = (LayoutParams) lp;
            if (isVertical()) {
                spannableLp.colSpan = Math.max(1, Math.min(other.colSpan, getLaneCount()));
                spannableLp.rowSpan = Math.max(1, other.rowSpan);
            } else {
                spannableLp.colSpan = Math.max(1, other.colSpan);
                spannableLp.rowSpan = Math.max(1, Math.min(other.rowSpan, getLaneCount()));
            }
        }

        return spannableLp;
    }

    @Override
    public LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
        return new LayoutParams(c, attrs);
    }


    @Override
    public View onInterceptFocusSearch(View focused, int direction) {

        View nextView = null;
        if (direction == View.FOCUS_DOWN && focused != null) {
            int focusPos = getPosition(getFocusedChild());
            int fp = getFirstVisiblePosition();
            int fb = focused.getBottom();
            int fl = focused.getLeft();
            int fr = focused.getRight();
            int nt, nl, nb, nr;
            double minDis = Integer.MAX_VALUE;
            int minVertical = Integer.MAX_VALUE;
            int verticalDis;
            double dis;
            View view;
            if (focusPos >= fp && getChildCount() > 0) {
                for (int i = focusPos - fp + 1; i < getChildCount(); i++) {
                    view = getChildAt(i);

                    if (!view.isFocusable() && view.getTag(R.id.is_record_recyclerview_itemView) == null)
                        continue;
                    nl = view.getLeft();
                    if (nl >= fr)
                        continue;
                    nb = view.getBottom();
                    if (nb <= fb)
                        continue;
                    nr = view.getRight();
                    if (nr <= fl)
                        continue;

                    nt = view.getTop();

                    verticalDis = nt - fb;

                    if (verticalDis > minVertical)
                        continue;

                    minVertical = verticalDis;

                    dis = Math.sqrt(Math.pow(nl - fl, 2) + Math.pow(nt - fb, 2));

                    if (dis < minDis) {
                        minDis = dis;
                        nextView = view;
                    }
                }

                if (nextView != null && nextView.getTag(R.id.is_record_recyclerview_itemView) != null)
                    return null;
            }
        }

        return nextView;
    }


    public static class LayoutParams extends PmRecyclerView.LayoutParams {
        private static final int DEFAULT_SPAN = 1;

        public double rowSpan;
        public double colSpan;

        public LayoutParams(int width, int height) {
            super(width, height);
            rowSpan = DEFAULT_SPAN;
            colSpan = DEFAULT_SPAN;
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.TvRecyclerView_SpannableGridViewChild);
            colSpan = Math.max(
                    DEFAULT_SPAN, a.getInt(R.styleable.TvRecyclerView_SpannableGridViewChild_tv_colSpan, -1));
            rowSpan = Math.max(
                    DEFAULT_SPAN, a.getInt(R.styleable.TvRecyclerView_SpannableGridViewChild_tv_rowSpan, -1));
            a.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams other) {
            super(other);
            init(other);
        }

        public LayoutParams(MarginLayoutParams other) {
            super(other);
            init(other);
        }

        private void init(ViewGroup.LayoutParams other) {
            if (other instanceof LayoutParams) {
                final LayoutParams lp = (LayoutParams) other;
                rowSpan = lp.rowSpan;
                colSpan = lp.colSpan;
            } else {
                rowSpan = DEFAULT_SPAN;
                colSpan = DEFAULT_SPAN;
            }
        }
    }
}
