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


/**
 * Created by chaoshuai. 提交意见成功
 */
public class FeedBackSuccessDialog {

    private static Dialog dialog;
    private static TextView tv;
    private static Button mCancel;

    public static void showDialog(final Activity activity, String tvName, String btName, final IClickListener dialogClick) {
        if (activity != null && !activity.isFinishing()) {
            if (dialog == null) {
                dialog = new Dialog(activity, R.style.dialog);
                View v = LayoutInflater.from(activity).inflate(
                        R.layout.dialog_feed_success_back, null);
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
                mCancel = (Button) v.findViewById(R.id.cancel);
                tv = (TextView) v.findViewById(R.id.tv_name);
                tv.setText(tvName);
                mCancel.setText(btName);
                mCancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dismissDialog();
                        dialogClick.confirm();
                    }
                });
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

    public interface IClickListener {
        void confirm();
    }
}
