package cn.cibnmp.ott.ui.user.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.library.pullable.PullToRefreshLayout;
import cn.cibnmp.ott.library.pullable.PullableListView;
import cn.cibnmp.ott.ui.base.Action;
import cn.cibnmp.ott.ui.base.BaseFragment;
import cn.cibnmp.ott.ui.detail.bean.ReserveBean;
import cn.cibnmp.ott.ui.detail.content.DbQueryListener;
import cn.cibnmp.ott.ui.detail.content.UserReserveHelper;
import cn.cibnmp.ott.ui.user.UserAndColActivity;
import cn.cibnmp.ott.ui.user.UserListInterface;
import cn.cibnmp.ott.ui.user.UserYuyueListAdapter;
import cn.cibnmp.ott.utils.HomeJumpDetailUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.Utils;

/**
 * Created by cibn-lyc on 2018/1/16.
 */

public class UserYuyueListFragment extends BaseFragment implements UserListInterface, View.OnClickListener {
    private final String TAG = "UserYuyueListFragment";
    private FragmentActivity mActivity;
    private View root;
    private PullableListView ListView;
    private UserYuyueListAdapter adapter;
    private LinearLayout user_sign_bottom_ll;
    private TextView slect_all;
    private TextView slect_delet;

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

    // 存储要删除选中的节目
    private List<ReserveBean> removeList = new ArrayList<ReserveBean>();
    /**
     * 是否进入多选模式
     **/
    private boolean isEdit = false;

    private List<ReserveBean> videoList;
    private RelativeLayout user_and_rl;

    private PullToRefreshLayout ptrl;
    private PullToRefreshLayout mToRefreshLayout;
    private boolean setDateTag = true;
    private List<ReserveBean> reserveList;
    private DbQueryListener dbQueryListener;
    private int saveNumber = 0;
    private int getNumber = 50;

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

    private void stopEdit() {
        isEdit = false;
        removeList.clear();
        setNumber(removeList.size());
        user_sign_bottom_ll.setVisibility(View.GONE);
        adapter.setSize(allSlectNO);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.user_yuyue_list_fragment, container, false);
        initView();
        loadListData(false);
        return root;
    }

    private void initView() {
        videoList = new ArrayList<ReserveBean>();

        ptrl = ((PullToRefreshLayout) root.findViewById(R.id.refresh_view));

        user_and_rl = (RelativeLayout) root.findViewById(R.id.user_and_rl);
        user_sign_bottom_ll = (LinearLayout) root.findViewById(R.id.user_sign_bottom_ll);
        slect_all = (TextView) root.findViewById(R.id.slect_all);
        slect_delet = (TextView) root.findViewById(R.id.slect_delet);

        ListView = (PullableListView) root.findViewById(R.id.user_sign_in_list_view);
        ListView.tag = true;
        adapter = new UserYuyueListAdapter(mActivity);
        adapter.setDate(videoList);
        ListView.setAdapter(adapter);
        setLisener();
    }
    private void setLisener() {
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isEdit) {
                    if (adapter.isSlect.get(position) == 0) {
                        adapter.isSlect.put(position, 1);
                        removeList.add(videoList.get(position));
                    } else {
                        adapter.isSlect.put(position, 0);
                        removeList.remove(videoList.get(position));
                    }
                    adapter.setSize(allSlect);
                    setNumber(removeList.size());
                    Lg.i("yuyue", "removeList.size-------->>>>" + removeList.size());
                } else {
                    HomeJumpDetailUtils.actionToDetailByPlayHistory(videoList.get(position), getActivity());
                }
            }
        });

        slect_all.setOnClickListener(UserYuyueListFragment.this);
        slect_delet.setOnClickListener(UserYuyueListFragment.this);

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
                    loadListData(false);
                } else {
                    handler.sendEmptyMessage(STOP_MSG);
                }
            }
        });
    }
    /**
     * 添加删除按钮的数量
     **/
    private void setNumber(int number) {
        StringBuffer sb = new StringBuffer();
        sb.append(Utils.getInterString(getActivity(), R.string.delect) + "(");
        sb.append(number);
        sb.append(")");
        slect_delet.setText(sb.toString());
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
                } else {
                    removeList.clear();
                    saveSize = allSlectNO;
                }
                adapter.setSize(saveSize);
                setNumber(removeList.size());
                break;
            case R.id.slect_delet:
                if (removeList.size() > 0) {
                    for (int i = 0; i < removeList.size(); i++) {
                        deleteLocalData(0, Long.valueOf(removeList.get(i).getLid()),Long.valueOf(removeList.get(i).getSid()));
                    }
                    videoList.removeAll(removeList);
                    removeList.clear();
                    adapter.setSize(allSlectNO);
                    setNumber(0);
                    ListView.smoothScrollBy(0, 0);
                    loadListData(true);
                }
                break;
            default:
                break;
        }
    }
    private final int STOP_MSG = 2000;
    private final int START_MSG = 2001;
    /**
     * 顶部刷新
     **/
    private final int LODING_DATE_UP = 6000;
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
                            Toast.makeText(mActivity, Utils.getInterString(getActivity(), R.string.no_yuyue), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mActivity, Utils.getInterString(getActivity(), R.string.already_load_all), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case START_MSG:
                        if (mToRefreshLayout != null) {
                            mToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        }
                        videoList.addAll(reserveList);
                        saveNumber = videoList.size();
                        reserveList.clear();
                        adapter.notifyDataSetChanged();
                        setDateTag = true;
                        break;
                }
            }
            return false;
        }
    });

    protected void loadListData(final boolean isAdd) {
        if (dbQueryListener == null) {
            dbQueryListener = new DbQueryListener() {
                @Override
                public void query(List<ReserveBean> list) {
                    reserveList = list;
                    if (reserveList == null || reserveList.size() == 0) {
                        handler.sendEmptyMessage(STOP_MSG);
                    } else {
                        if (isAdd) {
                            videoList.clear();
                        }
                        handler.sendEmptyMessage(START_MSG);
                    }
                }
            };
        }
        new Thread() {
            public void run() {
                UserReserveHelper.query(videoList.size(), getNumber, dbQueryListener);
            }
        }.start();
    }

    public void deleteLocalData(final int allflag, final long liveId,final long sid) {
        new Thread() {
            public void run() {
                UserReserveHelper.del(allflag, liveId,sid);
            }
        }.start();
    }

    @Override
    public boolean notifyDate() {
        if (videoList != null) {
            videoList.clear();
            loadListData(false);
        }
        return false;
    }

}
