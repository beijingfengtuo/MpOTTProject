package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.bean.AppUpdateBean;
import cn.cibnmp.ott.utils.Utils;

/**
 * Created by chaoshuai. 清理缓存dialog
 */
public class ImposedUpdateDialog {

	private static Dialog dialog;

	public static void showDialog(final Activity activity,
			final AppUpdateBean updateBean) {
		if (activity != null && !activity.isFinishing()) {
			if (dialog == null) {
				dialog = new Dialog(activity, R.style.dialog);
				View v = LayoutInflater.from(activity).inflate(
						R.layout.dialog_impose_update, null);
				final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.MATCH_PARENT);
				dialog.addContentView(v, params);
				dialog.setCancelable(false);
				Window window = dialog.getWindow();
				WindowManager.LayoutParams lp = window.getAttributes();
				lp.dimAmount = 0.6f;
				window.setAttributes(lp);
				window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

				TextView tv_vosioncode = (TextView) v.findViewById(R.id.tv_vosioncode);
				TextView tv_filesize = (TextView) v.findViewById(R.id.tv_filesize);
				TextView tvDescribe = (TextView) v.findViewById(R.id.tv_describe);
				Button mInstall = (Button) v.findViewById(R.id.installBtn);
				mInstall.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Utils.appInstall(activity, updateBean.getFilePath());
					}
				});
				
				if (updateBean != null) {
					if (updateBean.getLatestVersion()==null) {
						tv_vosioncode.setText("\u3000");
					} else {
						tv_vosioncode.setText("v" + Utils.splitVersionName(updateBean.getLatestVersion()));
					}
					tv_filesize.setText("\u3000(" + Utils.FormetFileSize(updateBean.getFileSize())+")");
					tvDescribe.setText(updateBean.getVerDesc());
				}

			}
			if (dialog != null && activity != null && !activity.isFinishing()
					&& !dialog.isShowing()) {
				dialog.show();
			}
		}
	}

	/**
	 * 【关闭】
	 */
	public static void dismissDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}
}
