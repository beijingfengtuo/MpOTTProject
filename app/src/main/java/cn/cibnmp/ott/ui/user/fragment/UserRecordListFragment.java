package cn.cibnmp.ott.ui.user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.UserRecordBean;
import cn.cibnmp.ott.bean.UserRecordInfoBean;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.ui.base.BaseFragment;
import cn.cibnmp.ott.ui.user.UserListInterface;
import cn.cibnmp.ott.ui.user.UserRceordInfoActivity;
import cn.cibnmp.ott.ui.user.UserRecordAdapter;

/**
 * Created by cibn-lyc on 2018/1/17.
 */

public class UserRecordListFragment extends BaseFragment implements UserListInterface {

    private final String TAG = "UserRecordListFragment";
    private FragmentActivity mActivity;
    private View root;
    private UserRecordBean dataList;
    private List<UserRecordInfoBean> userRecordInfoList;
    private RelativeLayout user_and_rl;
    private ListView list_view;
    private UserRecordAdapter recordAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.user_record_list_fragment, container, false);
        initView();
        loadListData();
        return root;
    }

    private void loadListData() {
        HttpRequest.getInstance().excute("getOrderList", App.bmsurl, 0, 100, 0, new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                Log.i(TAG, "onSuccess: " + response.toString());
                if (response == null) {
                    handler.sendEmptyMessage(STOP_MSG);
                    return;
                }
                dataList = JSON.parseObject(response, UserRecordBean.class);
                if (dataList == null) {
                    handler.sendEmptyMessage(STOP_MSG);
                    return;
                }
                if (dataList.getDataList() != null) {
                    handler.sendEmptyMessage(START_MSG);
                } else {
                    handler.sendEmptyMessage(STOP_MSG);
                    return;
                }
            }

            @Override
            public void onError(String error) {
                Log.i(TAG, "onError: " + error);
            }
        });
    }

    private void initView() {
        userRecordInfoList = new ArrayList<UserRecordInfoBean>();
        // 无收藏布局
        user_and_rl = (RelativeLayout) root.findViewById(R.id.user_and_rl);
        // 数据集合
        list_view = (ListView) root.findViewById(R.id.user_record_list_view);
        recordAdapter = new UserRecordAdapter(mActivity);
        list_view.setAdapter(recordAdapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, UserRceordInfoActivity.class);
                intent.putExtra("tradeNo", userRecordInfoList.get(position).getTradeNo());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean getEdit() {
        return false;
    }

    @Override
    public boolean notifyDate() {
        return false;
    }

    private final int STOP_MSG = 2000;
    private final int START_MSG = 2001;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg != null) {
                switch (msg.what) {
                    case STOP_MSG:
                        user_and_rl.setVisibility(View.VISIBLE);
                        break;
                    case START_MSG:
                        user_and_rl.setVisibility(View.GONE);
                        userRecordInfoList = dataList.getDataList();
                        recordAdapter.setData(userRecordInfoList);
                        break;
                }
            }
            return false;
        }
    });
}
