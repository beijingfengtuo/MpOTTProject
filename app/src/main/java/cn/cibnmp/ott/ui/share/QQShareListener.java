package cn.cibnmp.ott.ui.share;

import android.content.Context;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by 15210 on 2016/8/3.
 */
public class QQShareListener implements IUiListener {
    private Context context;

    public QQShareListener(Context context) {
        this.context = context;
    }

    @Override
    public void onComplete(Object o) {
//        ToastUtils.show(context,"分享成功");
    }

    @Override
    public void onError(UiError uiError) {
//        ToastUtils.show(context,"分享失败");
    }

    @Override
    public void onCancel() {
//        ToastUtils.show(context,"取消分享");
    }
}
