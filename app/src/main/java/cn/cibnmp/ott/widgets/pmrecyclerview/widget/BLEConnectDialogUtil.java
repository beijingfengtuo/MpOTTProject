package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cibnhp.grand.R;


public class BLEConnectDialogUtil {
	private static Dialog dialog;
    private static ImageView ivProess;
    private static RotateAnimation animation;

	public static void showDialog(Activity activity, boolean isCancelable, String contant) {
        if (activity != null && !activity.isFinishing()) {
        	if (dialog == null) {
        		synchronized(ProgressDialog.class){
        			if (dialog == null) {
                        dialog = new Dialog(activity , R.style.dialog);
                        View v = LayoutInflater.from(activity).inflate(R.layout.dialog_progress, null);
                        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(v, params);
                        dialog.setCancelable(isCancelable);
                        Window window = dialog.getWindow();
                        WindowManager.LayoutParams lp = window.getAttributes();
                        lp.dimAmount = 0.7f;
                        window.setAttributes(lp);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        
                        ivProess = (ImageView) v.findViewById(R.id.ivProgressIcon);
                        TextView tvContant = (TextView) v.findViewById(R.id.tv_contant);
                        tvContant.setTextColor(activity.getResources().getColor(R.color.white));
                        animation = (RotateAnimation) AnimationUtils.loadAnimation(activity,
                                R.anim.rotating);  
                        LinearInterpolator lir = new LinearInterpolator();
                        animation.setInterpolator(lir);
                        ivProess.startAnimation(animation);
                        tvContant.setText(contant);
                    }
        		}
        	}
            
            if (dialog != null && activity != null && !activity.isFinishing() && !dialog.isShowing()) {
                if (ivProess != null&&animation!=null) {
                    ivProess.startAnimation(animation);
                }
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
