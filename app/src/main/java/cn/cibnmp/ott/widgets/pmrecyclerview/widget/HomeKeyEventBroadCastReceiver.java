package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.cibnmp.ott.App;
import cn.cibnmp.ott.ui.HomeActivity;

/**
 * Created by wanqi on 2017/5/25.
 */

public class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {

    private static final String TAG = HomeKeyEventBroadCastReceiver.class.getSimpleName();

    static final String SYSTEM_REASON = "reason";
    static final String SYSTEM_HOME_KEY = "homekey";//home key
    static final String SYSTEM_RECENT_APPS = "recentapps";//long home key

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_REASON);
            if (reason != null) {
                if (reason.equals(SYSTEM_HOME_KEY)) {
                    // home key处理点
                    Log.d(TAG, "home key click event");
                    if(App.channelId != 20011) {
                        App.getAppManager().finishAllActivityExcept(HomeActivity.class);
                    }
//                    BaseApplication.getAppManager().AppExit(App.getInstance());
                } else if (reason.equals(SYSTEM_RECENT_APPS)) {
                    // long home key处理点
                    Log.d(TAG, "home key long click event");
                }
            }
        }
    }
}
