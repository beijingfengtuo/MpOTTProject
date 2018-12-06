package cn.cibnmp.ott.ui.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import java.lang.reflect.Field;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.ViewPagerStop;

/**
 *
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2017/12/25
 */
public abstract class ImgBaseFragment extends BaseFragment {
    /**
     * 存储白点图的数组
     **/
    public ImageView[] imageViews;
    public ViewPagerStop vPager;
    private boolean isShow = true;

    /**
     * @param size      底部点的总数量
     * @param viewGroup 填充点的父容器
     */
    public void setBottomWhiteImg(int size, ViewGroup viewGroup) {
        imageViews = new ImageView[size];
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(context);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(34, 14);
            if (i == size - 1) {
                layoutParams.setMargins(0, 0, 10, 0);
                imageView.setLayoutParams(layoutParams);
            } else {
                imageView.setLayoutParams(layoutParams);
            }

            imageView.setPadding(10, 0, 10, 0);
            imageViews[i] = imageView;
            if (i == 0)
                imageViews[i].setImageResource(R.mipmap.page_focused);
            else
                imageViews[i].setImageResource(R.mipmap.page_unfocused);

            viewGroup.addView(imageViews[i]);
        }
    }

    /**
     * 添加ViewPager
     **/
    public abstract void setViewPager();

    /**
     * 添加ViewPager内容
     **/
    public abstract void setPagerDate();

    private FixedSpeedScroller mScroller;

    public void setScoller(ViewPager vPager) {
        try {
            this.vPager = (ViewPagerStop) vPager;
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(vPager.getContext());
            mField.set(vPager, mScroller);
            mScroller.setMyDuration(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class FixedSpeedScroller extends Scroller {
        private int mDuration1 = 1000;
        private int mDuration2 = 1000;
        private boolean isOnTouch = false;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator,
                                  boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy,
                                int duration) {
            if (isOnTouch) {
                isOnTouch = false;
                super.startScroll(startX, startY, dx, dy, mDuration2);
            } else {
                super.startScroll(startX, startY, dx, dy, mDuration1);
            }
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            if (isOnTouch) {
                isOnTouch = false;
                super.startScroll(startX, startY, dx, dy, mDuration2);
            } else {
                super.startScroll(startX, startY, dx, dy, mDuration1);
            }
        }

        public void setMyDuration(boolean onTouch) {
            isOnTouch = onTouch;
        }

        public boolean getMyDuration() {
            return isOnTouch;
        }
    }

    // <<<<<<<<<<<<<<<--------------------滑动监听
    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int position) {
            switch (position) {
                case ViewPager.SCROLL_STATE_DRAGGING:
                    handler.sendEmptyMessage(MSG_KEEP_SILENT);
                    break;
                case ViewPager.SCROLL_STATE_IDLE:
                    handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            handler.sendMessage(Message.obtain(handler, MSG_PAGE_CHANGED,
                    position, 0));
            if (imageViews != null) {
                int leng = imageViews.length;
                for (int i = 0; i < leng; i++) {
                    position = position % leng;
                    if (position == i) {
                        imageViews[i].setImageResource(R.mipmap.page_focused);
                    } else {
                        imageViews[i]
                                .setImageResource(R.mipmap.page_unfocused);
                    }
                }
            }
        }
    }

    // <<<<<<<<<<<<<<<--------------------触发监听
    public class MyOnTouchListener implements OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mScroller.setMyDuration(true);
            return false;
        }
    }

    /**
     * 请求更新显示的View
     **/
    protected static final int MSG_UPDATE_IMAGE = 1;
    /**
     * 请求暂停轮播
     **/
    protected static final int MSG_KEEP_SILENT = 2;
    /**
     * 请求恢复轮播
     **/
    protected static final int MSG_BREAK_SILENT = 3;
    /**
     * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
     * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
     */
    protected static final int MSG_PAGE_CHANGED = 4;
    // 轮播间隔时间
    protected static final long MSG_DELAY = 3000;
    // 当前页数
    private int currentItem = 0;
    // 轮播图首次启动的标识
    private boolean startLB = false;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
            if (startLB && handler.hasMessages(MSG_UPDATE_IMAGE)) {
                handler.removeMessages(MSG_UPDATE_IMAGE);
            } else {
                if (msg.what == MSG_UPDATE_IMAGE) {
                    startLB = true;
                }
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    if (isShow) {
                        currentItem++;
                        // vPager.setCurrentItem(currentItem);
                        vPager.setCurrentItem(currentItem, true);
                    }
                    // 准备下次播放
                    handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    // 只要不发送消息就暂停了
                    break;
                case MSG_BREAK_SILENT:
                    handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    // 记录当前的页号，避免播放的时候页面显示不正确。
                    currentItem = msg.arg1;
                    handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(MSG_UPDATE_IMAGE);
//		if (imageViews != null) {
//			for (int i = 0; i < imageViews.length; i++) {
//				RecycleBitmap.recycleImageView(imageViews[i]);
//			}
//		}
    }

    public void resetcurrentItem() {
        currentItem = 0;
        vPager.setCurrentItem(currentItem);
    }

    @Override
    public void onStart() {
        isShow = true;
        super.onStart();
    }

    @Override
    public void onResume() {
        isShow = true;
        super.onResume();
    }

    @Override
    public void onPause() {
        isShow = false;
        super.onPause();
    }

    @Override
    public void onStop() {
        isShow = false;
        super.onStop();
    }

}
