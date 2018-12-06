package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import cn.cibnmp.ott.widgets.pmrecyclerview.widget.DisplayUtils;


public class CImageView extends ImageView {

    private boolean firstMeasure = true;

    public CImageView(Context context) {
        super(context);
    }

    public CImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        if (firstMeasure) {
            LayoutParams lp = getLayoutParams();
            if (lp != null
                    && lp instanceof android.widget.FrameLayout.LayoutParams) {
                android.widget.FrameLayout.LayoutParams flp = (android.widget.FrameLayout.LayoutParams) lp;
                flp.width = DisplayUtils.getAdaptValue(flp.width);
                flp.height = DisplayUtils.getAdaptValue(flp.height);
                flp.leftMargin = DisplayUtils.getAdaptValue(flp.leftMargin);
                flp.topMargin = DisplayUtils.getAdaptValue(flp.topMargin);
                flp.bottomMargin = DisplayUtils.getAdaptValue(flp.bottomMargin);
                flp.rightMargin = DisplayUtils.getAdaptValue(flp.rightMargin);
                setPadding(DisplayUtils.getAdaptValue(getPaddingLeft()),
                        DisplayUtils.getAdaptValue(getPaddingTop()),
                        DisplayUtils.getAdaptValue(getPaddingRight()),
                        DisplayUtils.getAdaptValue(getPaddingBottom()));
                setLayoutParams(flp);
            } else if (lp != null
                    && lp instanceof android.widget.LinearLayout.LayoutParams) {
                android.widget.LinearLayout.LayoutParams llp = (android.widget.LinearLayout.LayoutParams) lp;
                llp.width = DisplayUtils.getAdaptValue(llp.width);
                llp.height = DisplayUtils.getAdaptValue(llp.height);
                llp.leftMargin = DisplayUtils.getAdaptValue(llp.leftMargin);
                llp.topMargin = DisplayUtils.getAdaptValue(llp.topMargin);
                llp.bottomMargin = DisplayUtils.getAdaptValue(llp.bottomMargin);
                llp.rightMargin = DisplayUtils.getAdaptValue(llp.rightMargin);
                setPadding(DisplayUtils.getAdaptValue(getPaddingLeft()),
                        DisplayUtils.getAdaptValue(getPaddingTop()),
                        DisplayUtils.getAdaptValue(getPaddingRight()),
                        DisplayUtils.getAdaptValue(getPaddingBottom()));
                setLayoutParams(llp);
            } else if (lp != null
                    && lp instanceof android.widget.RelativeLayout.LayoutParams) {
                android.widget.RelativeLayout.LayoutParams rlp = (android.widget.RelativeLayout.LayoutParams) lp;
                rlp.width = DisplayUtils.getAdaptValue(rlp.width);
                rlp.height = DisplayUtils.getAdaptValue(rlp.height);
                rlp.leftMargin = DisplayUtils.getAdaptValue(rlp.leftMargin);
                rlp.topMargin = DisplayUtils.getAdaptValue(rlp.topMargin);
                rlp.bottomMargin = DisplayUtils.getAdaptValue(rlp.bottomMargin);
                rlp.rightMargin = DisplayUtils.getAdaptValue(rlp.rightMargin);
                setPadding(DisplayUtils.getAdaptValue(getPaddingLeft()),
                        DisplayUtils.getAdaptValue(getPaddingTop()),
                        DisplayUtils.getAdaptValue(getPaddingRight()),
                        DisplayUtils.getAdaptValue(getPaddingBottom()));
                setLayoutParams(rlp);
            }  else if (lp != null
                    && lp instanceof android.widget.TableLayout.LayoutParams) {
                android.widget.TableLayout.LayoutParams tlp = (android.widget.TableLayout.LayoutParams) lp;
                tlp.width = DisplayUtils.getAdaptValue(tlp.width);
                tlp.height = DisplayUtils.getAdaptValue(tlp.height);
                tlp.leftMargin = DisplayUtils.getAdaptValue(tlp.leftMargin);
                tlp.topMargin = DisplayUtils.getAdaptValue(tlp.topMargin);
                tlp.bottomMargin = DisplayUtils.getAdaptValue(tlp.bottomMargin);
                tlp.rightMargin = DisplayUtils.getAdaptValue(tlp.rightMargin);
                setPadding(DisplayUtils.getAdaptValue(getPaddingLeft()),
                        DisplayUtils.getAdaptValue(getPaddingTop()),
                        DisplayUtils.getAdaptValue(getPaddingRight()),
                        DisplayUtils.getAdaptValue(getPaddingBottom()));
                setLayoutParams(tlp);
            }
            firstMeasure = false;
        }
        super.onAttachedToWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
            System.out
                    .println("CImageView  -> onDraw() Canvas: trying to use a recycled bitmap");
        }
    }
}
