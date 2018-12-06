package cn.cibnmp.ott.ui.home;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cibnhp.grand.R;


public class TiltleAdvertisementView extends LinearLayout {
	public RelativeLayout rlayout;
	public ImageView imgView;
	public TextView textView;

	public TiltleAdvertisementView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		inflate(context, R.layout.gg_item_view, this);
		rlayout = (RelativeLayout) findViewById(R.id.gg_item_rl);
		imgView= (ImageView) findViewById(R.id.gg_item_img);
		textView= (TextView) findViewById(R.id.gg_item_tv);
	}

	public ImageView getImg() {
		return imgView;
	}

	public TextView getTVname() {
		return textView;
	}

}
