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

import cn.cibnhp.grand.R;


public class ProgressDialogUtil {
	private static Dialog dialog;
	private static int count = 0;
	
	public static void showDialog(final Activity activity) {
        if (activity != null && !activity.isFinishing()) {
        	if (dialog == null) {
        		synchronized(ProgressDialog.class){
        			if (dialog == null) {
                        dialog = new Dialog(activity , R.style.dialog);
                        View v = LayoutInflater.from(activity).inflate(R.layout.dialog_progress, null);
                        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(v, params);
                        dialog.setCancelable(false);
                        Window window = dialog.getWindow();
                        WindowManager.LayoutParams lp = window.getAttributes();
                        lp.dimAmount = 0.0f;
                        window.setAttributes(lp);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        
                        ImageView ivProess = (ImageView) v.findViewById(R.id.ivProgressIcon);
                        RotateAnimation animation = (RotateAnimation) AnimationUtils.loadAnimation(activity,
                                R.anim.rotating);  
                        LinearInterpolator lir = new LinearInterpolator();
                        animation.setInterpolator(lir);
                        ivProess.startAnimation(animation); 
                        
                    }
        		}
        	}
            
            if (dialog != null && activity != null && !activity.isFinishing() && !dialog.isShowing()) {
//            	Log.d("progressdialog", "progressdialog......" + count++);
                dialog.show();
            }
        }
    }
	
	/**
     * 【关闭】
     */
    public static void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
//        	Log.d("progressdialog", "progressdialog......dismiss");
        	dialog.dismiss();
            dialog = null;
        }
    }

}
