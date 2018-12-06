package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanjing on 2016/12/23.
 */

public class AdTextView extends TextView {

    private boolean openLisener = true;
    private float step = 0f;
    private Paint mPaint = null;
    ;
    private String setText;
    private List<String> textList = null;

    public AdTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AdTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdTextView(Context context) {
        super(context);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        textList = new ArrayList<String>();    //分行保存textview的显示信息。
        openLisener = true;
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (openLisener) {
                    openLisener = false;
                    setText = getText().toString();
                    if (setText == null | setText.length() == 0) {
                        return;
                    }
                    if (setText.contains("/")) {
                        final String[] rs = setText.split("/");
                        textList.add(rs[0]);
                        textList.add(rs[1]);
                    } else {
                        textList.add(setText);
                    }
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

    }

    //下面代码是利用上面计算的显示行数，将文字画在画布上，实时更新。
    @Override
    public void onDraw(Canvas canvas) {
        if (textList == null || textList.size() == 0) return;

        mPaint = new Paint();
        for (int i = 0; i < textList.size(); i++) {
            if (i == 1) {
                mPaint.setTextSize(100f);//设置字体大小
            } else {
                mPaint.setTextSize(50f);//设置字体大小
            }
            canvas.drawText(textList.get(i), 0, this.getHeight() + (i + 1) * mPaint.getTextSize() - step + 30, mPaint);
        }
        invalidate();
        step = step + 0.3f;
        if (step >= this.getHeight() + textList.size() * mPaint.getTextSize()) {
            step = 0;
        }
    }
}
