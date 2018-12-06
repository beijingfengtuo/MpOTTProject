package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.bean.CarouselProgramBean;
import cn.cibnmp.ott.utils.TimeUtils;

/**
 * Created by yanjing on 2016/12/23.
 */

public class ScrollBanner extends LinearLayout {

    private RelativeLayout tv_brl_1, tv_brl_2;
    private TextView mBannerTV1;
    private TextView mBannerTV2;
    private TextView time1;
    private TextView time2;
    private int isShow = 0;
    private List<CarouselProgramBean.Program> list;
    private int position = 0;
    private boolean isRun = true;

    public ScrollBanner(Context context) {
        this(context, null);
    }

    public ScrollBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.view_scroll_banner, this);

        tv_brl_1 = (RelativeLayout) view.findViewById(R.id.tv_brl_1);
        tv_brl_2 = (RelativeLayout) view.findViewById(R.id.tv_brl_2);
        mBannerTV1 = (TextView) view.findViewById(R.id.tv_banner1);
        mBannerTV2 = (TextView) view.findViewById(R.id.tv_banner2);
        time1 = (TextView) view.findViewById(R.id.tv_banner11);
        time2 = (TextView) view.findViewById(R.id.tv_banner22);

        tv_brl_2.setVisibility(GONE);
    }

    private void setDate(TextView textView, int position) {
        if (list.get(position).getStartTime() != null && !list.get(position).getStartTime().equals("")) {
            textView.setText(TimeUtils.getCurTime2(Long.valueOf(list.get(position).getStartTime())));
        } else {
            textView.setText("00:00");
        }
    }


    public List<CarouselProgramBean.Program> getList() {
        return list;
    }

    public void setList(List<CarouselProgramBean.Program> list) {
        this.list = list;
    }

    public void startScroll() {
        handler.removeMessages(START_MSG);
        position = 0;
        handler.sendEmptyMessageDelayed(START_MSG, 2000);
    }

    public void stopScroll() {
        handler.removeMessages(START_MSG);
    }

    public void setShow(boolean isRun) {
        this.isRun = isRun;
    }

    private final int START_MSG = 5555;
    private Handler handler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                case START_MSG:
                    if (list != null && list.size() > 0 && isRun) {
                        int showPosition = position++;
                        if (isShow == 0) {
                            isShow = 1;
                            if (list.get(showPosition).getProgramName() != null && !list.get(showPosition).getProgramName().equals("")) {
                                mBannerTV1.setText(list.get(showPosition).getProgramName());
                            } else {
                                mBannerTV1.setText("未知");
                            }
                            setDate(time1, showPosition);
                        } else {
                            if (position == list.size()) {
                                position = 0;
                            }
                            if (isShow == 1) {
                                tv_brl_2.setVisibility(VISIBLE);
                                if (list.get(showPosition).getProgramName() != null) {
                                    mBannerTV2.setText(list.get(showPosition).getProgramName());
                                } else {
                                    mBannerTV2.setText("未知");
                                }
                                setDate(time2, showPosition);
                                ObjectAnimator.ofFloat(tv_brl_1, "translationY", 0, -140).setDuration(2000).start();
                                ObjectAnimator.ofFloat(tv_brl_2, "translationY", 140, 0).setDuration(2000).start();
                            } else {
                                if (list.get(showPosition).getProgramName() != null && !list.get(showPosition).getProgramName().equals("")) {
                                    mBannerTV1.setText(list.get(showPosition).getProgramName());
                                }
                                setDate(time1, showPosition);
                                ObjectAnimator.ofFloat(tv_brl_2, "translationY", 0, -140).setDuration(2000).start();
                                ObjectAnimator.ofFloat(tv_brl_1, "translationY", 140, 0).setDuration(2000).start();
                            }
                            if (isShow == 1) {
                                isShow = 2;
                            } else {
                                isShow = 1;
                            }
                        }
                    }
                    handler.sendEmptyMessageDelayed(START_MSG, 4000);
                    break;
            }

        }
    };
}