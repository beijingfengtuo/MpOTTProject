package cn.cibnmp.ott.ui.base;

import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.DeleteDialog;

public class SettingBaseFragment extends BaseFragment {
    public DeleteDialog deleteDialog;

    public void deliteDialog(final Handler handler, String title, String name, final int msg) {
        if (deleteDialog == null) {
            /** Dialog的初始化 **/
            int width = context.getWindowManager().getDefaultDisplay().getWidth();
            int height = context.getWindowManager().getDefaultDisplay().getHeight();
            deleteDialog = new DeleteDialog(context, R.style.dialog);
            Window window = deleteDialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = 0.6f;
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            deleteDialog.getYes().setText("是");
            deleteDialog.getNo().setText("否");

            LayoutParams attributes = deleteDialog.getWindow().getAttributes();
            attributes.width = width;
            attributes.height = height;
            deleteDialog.getWindow().setAttributes(attributes);
            deleteDialog.getNo().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog.cancel();
                }
            });
        }
        deleteDialog.getYes().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(msg);
                deleteDialog.cancel();
            }
        });
        deleteDialog.getTitle().setText(title);
        deleteDialog.getName().setText(name);
        deleteDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.sendEmptyMessage(445544);
            }
        });

        deleteDialog.show();
    }

}
