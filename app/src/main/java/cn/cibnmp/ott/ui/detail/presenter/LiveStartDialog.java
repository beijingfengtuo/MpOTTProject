package cn.cibnmp.ott.ui.detail.presenter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.utils.ImageUtils;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.DisplayUtils;


/**
 * Created by yanjing on 2017/3/15.
 */

public class LiveStartDialog extends Dialog {

    public LiveStartDialog(Context context) {
        super(context);
    }

    public LiveStartDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public interface RemindLiveListener {
        void start();
    }

    public static class Builder {

        private Context context;
        private ImageView bg;
        private TextView name;
        private Button sure, cancel;
        private String title = "";

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置对话框标题
         *
         * @param title
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        @SuppressLint("StringFormatInvalid")
        public LiveStartDialog create(Context context, final LiveStartDialog.RemindLiveListener navListener) {
            View view = LayoutInflater.from(context).inflate(R.layout.live_start_dialog, null);
            final LiveStartDialog dialog = new LiveStartDialog(context, R.style.transcutestyle);
            dialog.setContentView(view);

            Window window = dialog.getWindow();

            WindowManager.LayoutParams lp = window.getAttributes();
            window.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
            lp.width = (int) (App.getAppManager().currentActivity().getWindowManager().getDefaultDisplay().getWidth() * 0.8); // 宽度
            lp.height = 800; // 高度
            window.setAttributes(lp);

            bg = (ImageView) view.findViewById(R.id.bg);
            name = (TextView) view.findViewById(R.id.name);
            sure = (Button) view.findViewById(R.id.sure);
            cancel = (Button) view.findViewById(R.id.cancel);

            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (navListener != null)
                        navListener.start();
                    dialog.dismiss();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            Bitmap bitmap = null;
            bitmap = ImageUtils.ReadBitmapById(context, R.drawable.pop_bg);
            int bg_width = -DisplayUtils.getPxAdaptValueBaseOnHDpi(50 * bitmap.getHeight() / 212) * 2;
            bitmap.recycle();
            bitmap = null;

            RelativeLayout.LayoutParams focusLp = (RelativeLayout.LayoutParams) bg.getLayoutParams();
            focusLp.leftMargin = bg_width;
            focusLp.rightMargin = bg_width;
            focusLp.topMargin = bg_width;
            focusLp.bottomMargin = bg_width;
            bg.setLayoutParams(focusLp);

           // name.setText(String.format(context.getResources().getString(R.string.app_name), title));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }, 10000);
            return dialog;
        }
    }
}