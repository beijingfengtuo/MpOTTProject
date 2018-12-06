package cn.cibnmp.ott.ui.user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.ui.base.BaseFragment;
import cn.cibnmp.ott.ui.user.UserListInterface;

/**
 * Created by cibn-lyc on 2018/1/16.
 */

public class UserMesageListFragment extends BaseFragment implements UserListInterface {

    private FragmentActivity mActivity;
    private View root;

    @Override
    public boolean getEdit() {
        return false;
    }

    @Override
    public boolean notifyDate() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.user_mesage_list_fragment, container, false);
        initView();
        return root;
    }

    private void initView() {

    }
}
