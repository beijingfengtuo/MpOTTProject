package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import cn.cibnhp.grand.R;


/**
 * Created by wanqi
 *
 * @TODO 进度条渐变色
 */
public class UpgradeGradientColorProgressView extends View {

    /**
     * 分段颜色
     */
    private static int[] SECTION_COLORS = null;
    private static final String TAG = "SpringProgressView";
    /**
     * 进度条最大值
     */
    private float maxCount;
    /**
     * 进度条当前值
     */
    private float currentCount;
    /**
     * 画笔
     */
    private Paint mPaint;

    private int mWidth, mHeight;
    /**
     * 进度条的高度，默认是20px
     */
    private int seekHeight = 10;
    /**
     * 进度条的高度位置
     */
    private int locationHeight = 0;
    /**
     * 进度条的宽度
     */
    private int locationWidth = 0;

    public UpgradeGradientColorProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public UpgradeGradientColorProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public UpgradeGradientColorProgressView(Context context) {
        super(context);
        initView(context);
    }

    public void setSeekHeight(int seekHeight) {
        this.seekHeight = seekHeight;
    }

    /**
     * 初始化数据
     *
     * @param context
     */
    private void initView(Context context) {
        SECTION_COLORS = getResources().getIntArray(R.array.upgrade_spring_progress_colors);
        if (SECTION_COLORS == null)
            initSpringProgressColors();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        locationHeight = mHeight - 10;
        locationWidth = mWidth;


        mPaint.setColor(getResources().getColor(R.color.upgrade_progress_color_1));
        RectF rectBg = new RectF(0, 0, locationWidth, seekHeight);//背景

        int round = mHeight / 2;

        canvas.drawRoundRect(rectBg, round, round, mPaint);

//        canvas.drawRect(rectBg, mPaint);

        float section = currentCount / maxCount;//进度条百分比
        float unit_section = 1f / SECTION_COLORS.length;
//        int colors_size = (int) ((section / unit_section) + 1);
//        if (colors_size > SECTION_COLORS.length) {
//            colors_size = SECTION_COLORS.length;
//        }
        int colors_size = SECTION_COLORS.length;

        RectF rectProgressBg = new RectF(0, 0, locationWidth * section, seekHeight);


        try {
            int[] colors = new int[colors_size];
            if (section > 0) {

                System.arraycopy(SECTION_COLORS, 0, colors, 0, colors_size);

                if (colors_size < 2) {
                    mPaint.setColor(colors[0]);
                } else {
                    //colors中的数量必须大于2个
                    LinearGradient shader = new LinearGradient(0, locationHeight - seekHeight, locationWidth * section, locationHeight, colors, null, Shader.TileMode.REPEAT);
                    mPaint.setShader(shader);
                }


            } else {
                mPaint.setColor(Color.TRANSPARENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        canvas.drawRoundRect(rectProgressBg, round, round, mPaint);

    }


    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /***
     * 设置最大的进度值
     *
     * @param maxCount
     */
    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }

    /***
     * 设置当前的进度值
     *
     * @param currentCount
     */
    public void setCurrentCount(float currentCount) {
        this.currentCount = currentCount > maxCount ? maxCount : currentCount;
        invalidate();
    }

    public float getMaxCount() {
        return maxCount;
    }

    public float getCurrentCount() {
        return currentCount;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize;
        } else {
            mWidth = 0;
        }
        mHeight = seekHeight;
        setMeasuredDimension(mWidth, mHeight);
    }


    /**
     * 初始化颜色数据值
     */
    private void initSpringProgressColors() {
        SECTION_COLORS = new int[10];
        SECTION_COLORS[0] = getResources().getColor(R.color.upgrade_progress_color_1);
        SECTION_COLORS[1] = getResources().getColor(R.color.upgrade_progress_color_2);
        SECTION_COLORS[2] = getResources().getColor(R.color.upgrade_progress_color_3);
        SECTION_COLORS[3] = getResources().getColor(R.color.upgrade_progress_color_4);
        SECTION_COLORS[4] = getResources().getColor(R.color.upgrade_progress_color_5);
        SECTION_COLORS[5] = getResources().getColor(R.color.upgrade_progress_color_6);
        SECTION_COLORS[6] = getResources().getColor(R.color.upgrade_progress_color_7);
        SECTION_COLORS[7] = getResources().getColor(R.color.upgrade_progress_color_8);
        SECTION_COLORS[8] = getResources().getColor(R.color.upgrade_progress_color_9);
        SECTION_COLORS[9] = getResources().getColor(R.color.upgrade_progress_color_10);
    }

}
