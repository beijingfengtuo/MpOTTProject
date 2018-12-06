package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import cn.cibnmp.ott.config.Constant;

public class ViewPagerStop extends ViewPager {
	private static final String TAG = "ViewPagerStop";
	public boolean result = false;

	public ViewPagerStop(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ViewPagerStop(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (result)
			return super.onInterceptTouchEvent(arg0);
		else
			return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (result)
			return super.onTouchEvent(arg0);
		else
			return false;
	}
	
	private int downX,downY,moveX,moveY;
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
//			getParent().requestDisallowInterceptTouchEvent(true);
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			Constant.CLASHTAB = true;
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();
			
			int diffX = moveX - downX;
			int diffY = moveY - downY;
			if(Math.abs(diffX) > Math.abs(diffY)) { // 当前是横向滑动, 不让父元素拦截
//				getParent().requestDisallowInterceptTouchEvent(true);
				Constant.CLASHTAB = false;
			} else {
//				getParent().requestDisallowInterceptTouchEvent(false);
				Constant.CLASHTAB = true;
			}
			break;
		case MotionEvent.ACTION_UP:
			Constant.CLASHTAB = true;
//			getParent().requestDisallowInterceptTouchEvent(false);
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
