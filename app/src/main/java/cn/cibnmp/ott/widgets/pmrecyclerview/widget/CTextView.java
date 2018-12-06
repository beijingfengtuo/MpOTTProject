package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import cn.cibnmp.ott.widgets.pmrecyclerview.widget.DisplayUtils;

public class CTextView extends TextView {

    private boolean firstMeasure = true;

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
                setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        DisplayUtils.getAdaptValue(getTextSize()));
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
                setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        DisplayUtils.getAdaptValue(getTextSize()));
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
                setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        DisplayUtils.getAdaptValue(getTextSize()));
                setPadding(DisplayUtils.getAdaptValue(getPaddingLeft()),
                        DisplayUtils.getAdaptValue(getPaddingTop()),
                        DisplayUtils.getAdaptValue(getPaddingRight()),
                        DisplayUtils.getAdaptValue(getPaddingBottom()));
                if (Build.VERSION.SDK_INT >= 16) {
                    setLineSpacing(DisplayUtils.getAdaptValue(getLineSpacingExtra()), getLineSpacingMultiplier());
                }
                setLayoutParams(rlp);
            } else if (lp != null
                    && lp instanceof android.widget.AbsoluteLayout.LayoutParams) {
                android.widget.AbsoluteLayout.LayoutParams alp = (android.widget.AbsoluteLayout.LayoutParams) lp;
                alp.width = DisplayUtils.getAdaptValue(alp.width);
                alp.height = DisplayUtils.getAdaptValue(alp.height);
                setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        DisplayUtils.getAdaptValue(getTextSize()));
                setPadding(DisplayUtils.getAdaptValue(getPaddingLeft()),
                        DisplayUtils.getAdaptValue(getPaddingTop()),
                        DisplayUtils.getAdaptValue(getPaddingRight()),
                        DisplayUtils.getAdaptValue(getPaddingBottom()));
                setLayoutParams(alp);
            } else if (lp != null
                    && lp instanceof android.widget.TableLayout.LayoutParams) {
                android.widget.TableLayout.LayoutParams tlp = (android.widget.TableLayout.LayoutParams) lp;
                tlp.width = DisplayUtils.getAdaptValue(tlp.width);
                tlp.height = DisplayUtils.getAdaptValue(tlp.height);
                tlp.leftMargin = DisplayUtils.getAdaptValue(tlp.leftMargin);
                tlp.topMargin = DisplayUtils.getAdaptValue(tlp.topMargin);
                tlp.bottomMargin = DisplayUtils.getAdaptValue(tlp.bottomMargin);
                tlp.rightMargin = DisplayUtils.getAdaptValue(tlp.rightMargin);
                setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        DisplayUtils.getAdaptValue(getTextSize()));
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public CTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CTextView(Context context) {
        super(context);
    }
}
