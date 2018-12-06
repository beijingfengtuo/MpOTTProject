package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cibnhp.grand.R;


public class DeleteDialog extends Dialog {
	private Context context;
	private LayoutInflater mInflater;
	private RelativeLayout rootLayout;
	private TextView delete_dialog_title;
	private TextView delete_dialog_name;
	private TextView delete_dialog_yes;
	private TextView delete_dialog_no;

	public DeleteDialog(Context context) {
		super(context);
	}

	public DeleteDialog(Activity context, int theme) {
		super(context, theme);
		this.context = context;
		mInflater = LayoutInflater.from(context);
		rootLayout = (RelativeLayout) mInflater.inflate(R.layout.dialog_clean_cache, null);
		setContentView(rootLayout);
		delete_dialog_title = (TextView) rootLayout.findViewById(R.id.title);
		delete_dialog_name = (TextView) rootLayout.findViewById(R.id.tv_name);
		delete_dialog_yes = (TextView) rootLayout.findViewById(R.id.confirm);
		delete_dialog_no = (TextView) rootLayout.findViewById(R.id.cancel);
	}

	public TextView getTitle() {
		return delete_dialog_title;
	}

	public TextView getName() {

		return delete_dialog_name;
	}

	public TextView getYes() {

		return delete_dialog_yes;
	}

	public TextView getNo() {

		return delete_dialog_no;
	}

}
