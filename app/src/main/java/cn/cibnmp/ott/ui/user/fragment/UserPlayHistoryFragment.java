package cn.cibnmp.ott.ui.user.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
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
import cn.cibnmp.ott.ui.base.SettingBaseFragment;
import cn.cibnmp.ott.ui.detail.bean.RecordBean;
import cn.cibnmp.ott.ui.header.StickyListHeadersListView;
import cn.cibnmp.ott.ui.user.UserListInterface;
import cn.cibnmp.ott.ui.user.UserPlayHistoryAdapter;
import cn.cibnmp.ott.utils.HomeJumpDetailUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.Utils;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by cibn-lyc on 2018/1/16.
 */

public class UserPlayHistoryFragment extends SettingBaseFragment implements UserListInterface, AbsListView.OnScrollListener, AdapterView.OnItemClickListener, StickyListHeadersListView.OnHeaderClickListener {
    private final String TAG = "UserPlayHistoryFragment";
    private StickyListHeadersListView ListView;
    private UserPlayHistoryAdapter adapter;

    private View root;
    private Activity mActivity;

    private RecordListBean dataList;
    private String getLocalRecordList;
    private String deleteDataRequest;
    private boolean hasLoadedAll = false;
    private List<RecordBean> videoList;
    private int toNumber = 20;
    private int deleteItem = -1;

    private RelativeLayout user_history_rl;
    private static final String KEY_LIST_POSITION = "KEY_LIST_POSITION";
    private int firstVisible;
    private PullToRefreshLayout ptrl;
    private PullToRefreshLayout mToRefreshLayout;
    private boolean setDateTag = true;
    // 播放进度刷新判断依据
    private int videoPosition = -1;
    private int isClick = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.user_play_history_fragment, container, false);
        if (savedInstanceState != null) {
            firstVisible = savedInstanceState.getInt(KEY_LIST_POSITION);
        }
        initView();
        loadListData(0, toNumber);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isClick == 1) {
            isClick = 2;
            videoPosition = -1;
            handler.sendEmptyMessageDelayed(IS_CLICK, 500);
        } else if (videoPosition >= 0) {
            loadListData(videoPosition, 1);
        }
    }

    @Override
    public boolean getEdit() {
        if (videoList != null && videoList.size() > 0) {
            deliteDialog(handler, getString(R.string.user_msg_delete_title), getString(R.string.user_play_delete_all_name), DELETE_ALL);
        } else {
            Toast.makeText(mActivity, Utils.getInterString(getActivity(), R.string.no_data), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    public void loadListData(final int index, final int totalNum) {
        HttpRequest.getInstance().excute(getLocalRecordList, new Object[]{index, totalNum, new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                Lg.d(TAG, getLocalRecordList + " response:-------------->>>" + response);
                if (response == null) {
                    Lg.e(TAG, getLocalRecordList + " response is null .");
                    handler.sendEmptyMessage(STOP_MSG);
                    return;
                }
                dataList = JSON.parseObject(response, RecordListBean.class);
                if (dataList == null) {
                    Lg.e(TAG, getLocalRecordList + " parseObject response result is null,response is invalid.");
                    handler.sendEmptyMessage(STOP_MSG);
                    return;
                }
                if (dataList.getVideoCollectList() == null || (dataList.getVideoCollectList() != null && totalNum > dataList.getVideoCollectList().size())) {
                    hasLoadedAll = true;
                } else {
                    hasLoadedAll = false;
                }
                if (index == 0 && videoPosition == -1) {
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
                handler.sendEmptyMessage(STOP_MSG);
            }
        }});
    }

    public void initView() {
        getLocalRecordList = "getLocalRecordList";
        deleteDataRequest = "deleteLocalRecord";
        videoList = new ArrayList<RecordBean>();
        ptrl = ((PullToRefreshLayout) root.findViewById(R.id.refresh_view));

        user_history_rl = (RelativeLayout) root.findViewById(R.id.user_history_rl);
        ListView = (StickyListHeadersListView) root.findViewById(R.id.user_sign_in_list_view);
        ListView.setOnScrollListener(this);
        ListView.setOnItemClickListener(this);
        ListView.setOnHeaderClickListener(this);
//        ListView.setOnItemLongClickListener(this);

        ListView.setPullLoyout(ptrl);

        adapter = new UserPlayHistoryAdapter(mActivity, ListView.getRightViewWidth());
        adapter.setDate(videoList);
        adapter.setOnRightItemClickListener(new UserPlayHistoryAdapter.onRightItemClickListener() {

            @Override
            public void onRightItemClick(View v, int position) {
                if (position != -1) {
                    ListView.deleteItem(ListView.getCurrentItemView());
                    deleteLocalData(false, videoList.get(position).getVid());
                    videoList.remove(position);
                    adapter.notifyDataSetChanged();
                }
                if (videoList.size() == 0) {
                    loadListData(0, toNumber);
                }
            }
        });
        ListView.setAdapter(adapter);
        ListView.setSelection(firstVisible);
        ListView.setDrawingListUnderStickyHeader(true);
        setRefreshListener();
    }

    private void setRefreshListener() {
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                videoPosition = position;
                HomeJumpDetailUtils.actionToDetailByPlayHistory(videoList.get(position), getActivity());
                if (isClick == 0) {
                    isClick = 1;
                }
            }
        });

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

    private final int STOP_MSG = 2000;
    private final int START_MSG = 2001;
    private final int DELETE_ITEM = 4000;
    private final int DELETE_ALL = 4444;
    private final int LODING_DATE_UP = 6000;
    private final int LONG_CLICK = 445544;
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
                    case DELETE_ALL:
                        deleteLocalData(true, 0l);
                        videoList.clear();
                        adapter.notifyDataSetChanged();
                        user_history_rl.setVisibility(View.VISIBLE);
                        break;
                    case DELETE_ITEM:
                        if (deleteItem != -1) {
                            ListView.deleteItem(ListView.getChildAt(deleteItem));
                            deleteLocalData(false, videoList.get(deleteItem).getVid());
                            videoList.remove(deleteItem);
                            adapter.notifyDataSetChanged();
                        }
                        if (videoList.size() == 0) {
                            loadListData(0, toNumber);
                        }
                        break;
                    case STOP_MSG:
                        videoPosition = -1;
                        if (videoList.size() == 0) {
                            user_history_rl.setVisibility(View.VISIBLE);
                            Toast.makeText(mActivity, Utils.getInterString(getActivity(), R.string.no_play_record), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mActivity, Utils.getInterString(getActivity(), R.string.already_load_all), Toast.LENGTH_SHORT).show();
                        }
                        if (mToRefreshLayout != null) {
                            mToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        }
                        setDateTag = false;
                        break;
                    case START_MSG:
                        // 加载完毕
                        if (mToRefreshLayout != null) {
                            mToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        }
                        if (videoPosition >= 0) {
                            if (dataList.getVideoCollectList().size() > 0) {
                                videoList.set(videoPosition, dataList.getVideoCollectList().get(0));
                            }
                        } else {
                            videoList.addAll(dataList.getVideoCollectList());
                        }
                        adapter.notifyDataSetChanged();
                        setDateTag = true;
                        videoPosition = -1;
                        break;
                    case LONG_CLICK:
                        break;
                    case IS_CLICK:
                        isClick = 0;
                        notifyDate();
                        break;
                }
            }
            return false;
        }
    });

    // 删除条目的方法
    public void deleteLocalData(Boolean delAll, Long vid) {
        HttpRequest.getInstance().excute(deleteDataRequest, new Object[]{delAll, vid, new HttpResponseListener() {
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
        }});
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_LIST_POSITION, firstVisible);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisible = firstVisibleItem;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
        Toast.makeText(context, "header " + headerId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(context, "点击了条目：" + position + " clicked!", LENGTH_SHORT).show();
    }

//    @Override
//    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        LongClick = true;
//        deleteItem = position;
//        deliteDialog(handler, getString(R.string.user_msg_delete_title), getString(R.string.user_msg_delete_name), DELETE_ITEM);
//        return false;
//    }

    @Override
    public boolean notifyDate() {
        if (videoList != null) {
            videoList.clear();
            loadListData(0, 20);
        }
        return false;
    }
}