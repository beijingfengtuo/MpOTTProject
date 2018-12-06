package cn.cibnmp.ott.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import de.greenrobot.event.EventBus;

public class BaseFragment extends Fragment {
    private final String TAG = "BaseFragment";
    public BaseActivity context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // register
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void startActivity(String actionName, Bundle bundle) {
        //统一走Activity的方法
        context.startActivity(actionName, bundle);
    }

    // 回调子类的方法，隐藏布局
    public void setButtom() {

    }

    public void onEventMainThread(String event) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = (BaseActivity) activity;
    }

    public void setShow(int id, String action, int tag) {

    }

    /**
     * 页面数据回调使用
     *
     * @param bundle
     */
    public void setData(Bundle bundle) {

    }


}
