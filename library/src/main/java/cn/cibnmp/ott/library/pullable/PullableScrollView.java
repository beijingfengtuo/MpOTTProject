package cn.cibnmp.ott.library.pullable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class PullableScrollView extends ScrollView implements Pullable {

	private OnScrollToBottomListener onScrollToBottom;

	public PullableScrollView(Context context) {
		super(context);
	}

	public PullableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullableScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown() {
		if (getScrollY() == 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean canPullUp() {
		if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
			return true;
		else
			return false;
	}

	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		if (scrollY > 0 && onScrollToBottom != null && clampedY) {
			onScrollToBottom.onScrollBottomListener(scrollY, clampedY);
		}
	}

	public void setOnScrollToBottomLintener(OnScrollToBottomListener listener) {
		onScrollToBottom = listener;
	}

	public interface OnScrollToBottomListener {
		public void onScrollBottomListener(int y, boolean isBottom);
	}

}
