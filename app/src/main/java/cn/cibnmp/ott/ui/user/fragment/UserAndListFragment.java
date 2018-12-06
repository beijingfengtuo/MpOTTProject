package cn.cibnmp.ott.ui.user.fragment;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.bean.RecordListBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.library.pullable.PullToRefreshLayout;
import cn.cibnmp.ott.library.pullable.PullableListView;
import cn.cibnmp.ott.ui.base.BaseFragment;
import cn.cibnmp.ott.ui.detail.bean.RecordBean;
import cn.cibnmp.ott.ui.user.UserAndColAdapter;
import cn.cibnmp.ott.ui.user.UserListInterface;
import cn.cibnmp.ott.utils.HomeJumpDetailUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.Utils;
import de.greenrobot.event.EventBus;

/**
 * Created by cibn-lyc on 2018/1/16.
 */

public class UserAndListFragment extends BaseFragment implements UserListInterface, View.OnClickListener {
    private final String TAG = "UserAndListFragment";
    private Activity mActivity;
    private View root;
    // 无收藏布局
    private RelativeLayout user_and_rl;
    // 每次加载条数
    private int toNumber = 35;
    // 刷新布局
    private PullToRefreshLayout ptrl;
    private PullToRefreshLayout mToRefreshLayout;
    // 有收藏布局
    private LinearLayout user_sign_bottom_ll;
    // 全选
    private TextView slect_all;
    // 删除
    private TextView slect_delet;
    // listView
    private PullableListView list_view;

    private String getLocalCollectList;
    private String deleteDataRequest;
    private List<RecordBean> videoList;
    // 适配器
    private UserAndColAdapter adapter;
    // 数据集合
    private RecordListBean dataList;
    // 存储要删除选中的节目
    private List<RecordBean> removeList = new ArrayList<RecordBean>();
    private List<RecordBean> removeList2 = new ArrayList<RecordBean>();
    /**
     * 是否进入多选模式
     **/
    private boolean isEdit = false;
    /**
     * 多选初始化为标识0
     **/
    private final int allSlect = 0;
    /**
     * 多选全部选中的标识
     **/
    private final int allSlectYES = 1;
    /**
     * 多选全部取消的标识
     **/
    private final int allSlectNO = 2;

    private boolean setDateTag = true;
    private boolean hasLoadedAll = false;
    // 默认显示无收藏布局
    private boolean isUserAndListFragment = true;

    private boolean isClick = false;

    @Override
    public boolean getEdit() {
        if (videoList != null && videoList.size() > 0) {
            if (!isEdit) {
                isEdit = true;
                user_sign_bottom_ll.setVisibility(View.VISIBLE);
            } else {
                stopEdit();
            }
            adapter.setVG(isEdit);
        } else {
            Toast.makeText(mActivity, Utils.getInterString(getActivity(), R.string.no_data), Toast.LENGTH_LONG).show();
        }
        return isEdit;
    }

    @Override
    public boolean notifyDate() {
        if (!isClick) {
            isClick = true;
            handler.sendEmptyMessageDelayed(IS_CLICK, 200);
        }
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
        root = inflater.inflate(R.layout.user_and_list_fragment, container, false);
        initView();
        loadListData(0, toNumber);
        return root;
    }

    private void initView() {
        getLocalCollectList = "getLocalCollectList";
        deleteDataRequest = "deleteLocalCollect";
        videoList = new ArrayList<RecordBean>();
        // 无收藏布局
        user_and_rl = (RelativeLayout) root.findViewById(R.id.user_and_rl);
        // 刷新
        ptrl = ((PullToRefreshLayout) root.findViewById(R.id.refresh_view));
        // 有收藏布局
        user_sign_bottom_ll = (LinearLayout) root.findViewById(R.id.user_sign_bottom_ll);
        // 全选
        slect_all = (TextView) root.findViewById(R.id.slect_all);
        // 删除
        slect_delet = (TextView) root.findViewById(R.id.slect_delet);
        // 数据集合
        list_view = (PullableListView) root.findViewById(R.id.user_sign_in_list_view);
        list_view.tag = true;
        adapter = new UserAndColAdapter(mActivity);
        adapter.setDate(videoList);
        list_view.setAdapter(adapter);
        // 监听事件
        setLisener();
    }

    private void setLisener() {
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isEdit) {
                    if (adapter.isSlect.get(position) == 0) {
                        adapter.isSlect.put(position, 1);
                        removeList.add(videoList.get(position));
                    } else {
                        adapter.isSlect.put(position, 0);
                        removeList.remove(videoList.get(position));
                        slect_all.setText(getString(R.string.select_all));
                    }
                    if (videoList.size() == removeList.size()){
                        slect_all.setText(getString(R.string.cancel_select_all));
                    }else {
                        slect_all.setText(getString(R.string.select_all));
                    }
                    adapter.setSize(allSlect);
                    setNumber(removeList.size());
                    Lg.d("--------------------->>>>" + removeList.size());
                } else {
                    HomeJumpDetailUtils.actionToDetailByPlayHistory(videoList.get(position), getActivity());
                }
            }
        });

        slect_all.setOnClickListener(UserAndListFragment.this);
        slect_delet.setOnClickListener(UserAndListFragment.this);

        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                // 下拉刷新操作
                mToRefreshLayout = pullToRefreshLayout;
                handler.sendEmptyMessage(LODING_DATE_UP);
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                mToRefreshLayout = pullToRefreshLayout;
                // 上拉加载操作
                if (setDateTag) {
                    setDateTag = false;
                    if (hasLoadedAll) {
                        handler.sendEmptyMessage(STOP_MSG);
                    } else {
                        loadListData(videoList.size(), toNumber);
                    }
                } else {
                    handler.sendEmptyMessage(STOP_MSG);
                }
            }
        });
    }

    public void loadListData(final int index, final int totalNum) {
        HttpRequest.getInstance().excute(getLocalCollectList, index, totalNum, new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                Log.i(TAG, "onSuccess: " + response.toString());
                if (response == null) {
                    Lg.e(TAG, getLocalCollectList + " response is null .");
                    handler.sendEmptyMessage(STOP_MSG);
                    return;
                }
                dataList = JSON.parseObject(response, RecordListBean.class);
                Log.i("test", "onSuccess: " + dataList.toString());
                if (dataList == null) {
                    Lg.e(TAG, getLocalCollectList + " parseObject response result is null,response is invalid.");
                    handler.sendEmptyMessage(STOP_MSG);
                    return;
                }
                if (dataList.getVideoCollectList() == null || (dataList.getVideoCollectList() != null && totalNum > dataList.getVideoCollectList().size())) {
                    hasLoadedAll = true;
                } else {
                    hasLoadedAll = false;
                }
                if (index == 0) {
                    videoList.clear();
                }
                if (dataList.getVideoCollectList() != null) {
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
        if (index == 0) {
            list_view.setBackgroundColor(0xffffffff);
        }
    }

    // 删除条目的方法
    public void deleteLocalData(Boolean delAll, Long vid) {
        HttpRequest.getInstance().excute(deleteDataRequest, delAll, vid, new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                if (response == null) {
                    Lg.e(TAG, deleteDataRequest + " response is null .");
                    return;
                }
                Log.i("TAG", deleteDataRequest + ":" + response);
            }

            @Override
            public void onError(String error) {
            }
        });
    }

    private final int STOP_MSG = 2000;
    private final int START_MSG = 2001;
    /**
     * 顶部刷新
     **/
    private final int LODING_DATE_UP = 6000;
    private final int IS_CLICK = 445566;
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg != null) {
                switch (msg.what) {
                    case LODING_DATE_UP:
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                        mToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        break;
                    case STOP_MSG:
                        setDateTag = false;
                        if (mToRefreshLayout != null) {
                            mToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        }
                        if (videoList.size() == 0) {
                            user_and_rl.setVisibility(View.VISIBLE);
                            stopEdit();
                            adapter.setVG(isEdit);
                            if (isUserAndListFragment) {
                                Toast.makeText(mActivity, Utils.getInterString(getActivity(), R.string.no_collection), Toast.LENGTH_SHORT).show();
                            } else {
                                isUserAndListFragment = false;
                            }
                            EventBus.getDefault().post(Constant.USER_AND_CLEAR);
                        } else {
                            Toast.makeText(mActivity, Utils.getInterString(getActivity(), R.string.already_load_all), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case START_MSG:
                        if (mToRefreshLayout != null) {
                            mToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        }
                        videoList.addAll(dataList.getVideoCollectList());
                        if (removeList2.size() > 0) {
                            videoList.removeAll(removeList2);
                            removeList2.clear();
                        }
                        adapter.notifyDataSetChanged();
                        setDateTag = true;
                        break;
                    case IS_CLICK:
                        notifyDateList();
                        break;
                }
            }
            return false;
        }
    });

    /**
     * 添加删除按钮的数量
     **/
    private void setNumber(int number) {
        StringBuffer sb = new StringBuffer();
        sb.append("删除(");
        sb.append(number);
        sb.append(")");
        slect_delet.setText(sb.toString());
    }

    private Bundle setDetail(int tag) {
        Bundle b = new Bundle();
        b.putLong(Constant.contentIdKey, videoList.get(tag).getVid());
        return b;
    }

    private void stopEdit() {
        isEdit = false;
        removeList.clear();
        setNumber(removeList.size());
        user_sign_bottom_ll.setVisibility(View.GONE);
        adapter.setSize(allSlectNO);
    }

    private void notifyDateList() {
        isUserAndListFragment = false;
        if (videoList != null) {
            videoList.clear();
            loadListData(0, 20);
            isClick = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.slect_all:
                int saveSize = 0;
                if (removeList.size() < videoList.size()) {
                    if (removeList.size() > 0) {
                        removeList.clear();
                    }
                    removeList.addAll(videoList);
                    saveSize = allSlectYES;
                    slect_all.setText(getString(R.string.cancel_select_all));
                } else {
                    removeList.clear();
                    saveSize = allSlectNO;
                    slect_all.setText(getString(R.string.select_all));
                }
                adapter.setSize(saveSize);
                setNumber(removeList.size());
                break;
            case R.id.slect_delet:
                if (removeList.size() > 0) {
                    for (int i = 0; i < removeList.size(); i++) {
                        deleteLocalData(false, removeList.get(i).getVid());
                    }
                    videoList.removeAll(removeList);
                    removeList2.addAll(removeList);
                    removeList.clear();
                    adapter.setSize(allSlectNO);
                    setNumber(0);
                    loadListData(0, toNumber);
                }
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(Object event) {
         if (event instanceof RecordBean){
            notifyDate();
        }
    }
}
