package cn.cibnmp.ott.ui.home;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.ui.search.DensityUtils;
import cn.cibnmp.ott.ui.search.HomeSpaceItemDecoration;
import cn.cibnmp.ott.bean.NavigationItemBean;
import cn.cibnmp.ott.bean.NavigationItemDataBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.utils.ItemDragHelperCallback;
import cn.cibnmp.ott.utils.SharedPreferencesUtil;
import de.greenrobot.event.EventBus;

/**
 * Created by yangwenwu on 17/12/29.
 * 频道管理页面
 */

public class ChannelManagementActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private int mOveContent = 0; //判断上锁
    private boolean isEditMode; //判断是否是编辑模式
    private GridView mGridView; //频道管理下面GridView
    private RecyclerView mRecyclerView; //频道管理上面RecyclerView
    private TextView mTvBtnEdit, mDefaultsort; //编辑和默认排序
    private ChannelManagementAdapter mAdapter; //频道管理上面Adapter
    private List<NavigationItemBean> mItemsShow; //频道管理上面数据
    private List<NavigationItemBean> mItemsHide; //频道管理下面数据
    private List<NavigationItemBean> mComeChannelList; //回调首页的数据
    private List<NavigationItemBean> mChannelDataList; //接收首页传的List
    private SharedPreferencesUtil mSharedPreferencesUtil; //传值
    private CannelItemsHideAdapter mCannelItemsHideAdapter; //频道管理下面Adapter
    private NavigationItemDataBean mNavigationItemDataBean; //默认排序数据
    private ImageView mChannelPushButton, mDefaultsortImag, mDelete; //删除默认排序显示或者隐藏空间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channelmanagement);
        initView();
        initData();
    }

    private void initView() {
        mTvBtnEdit = (TextView) findViewById(R.id.tv_btn_edit);
        mDefaultsort = (TextView) findViewById(R.id.defaultsort_text);
        mChannelPushButton = (ImageView) findViewById(R.id.channel_push_button);
        mDefaultsortImag = (ImageView) findViewById(R.id.defaultsort_imag);
        mDelete = (ImageView) findViewById(R.id.sanchu);
        mRecyclerView = (RecyclerView) findViewById(R.id.recy);
        mGridView = (GridView) findViewById(R.id.channel_gridview);
        mTvBtnEdit.setOnClickListener(this);
        mDefaultsort.setOnClickListener(this);
        mChannelPushButton.setOnClickListener(this);
        mDefaultsortImag.setOnClickListener(this);
        mDelete.setOnClickListener(this);
        mGridView.setOnItemClickListener(this);
    }

    private void initData() {
        //接收频道列表页传的数据
        mChannelDataList = (List<NavigationItemBean>)
                getIntent().getSerializableExtra("DataList");
        mItemsShow = new ArrayList<>();
        mItemsHide = new ArrayList<>();
        mOveContent = 0;
        if (mChannelDataList != null) {
            for (NavigationItemBean item : mChannelDataList) {
                //判断是否上锁
                if (item.getIsSolidShow() == 1) {
                    mItemsShow.add(mOveContent, item);
                    mOveContent++;
                    //判断是否显示且最多显示只能8条
                } else if (mItemsShow.size() < 8 && item.getIsDefaultShow() == 1) {
                    mItemsShow.add(item);
                } else {
                    mItemsHide.add(item);
                }
            }
        } else {
            return;
        }
        initShow(mItemsShow);
        initHide(mItemsHide);
    }

    /**
     * 添加频道管理下面的数据
     *
     * @param ItemsHideList
     */
    private void initHide(List<NavigationItemBean> ItemsHideList) {
        mCannelItemsHideAdapter = new CannelItemsHideAdapter(this, ItemsHideList);
        mGridView.setAdapter(mCannelItemsHideAdapter);
    }

    /**
     * 添加频道管理上面的数据
     *
     * @param ItemsShowList
     */
    private void initShow(List<NavigationItemBean> ItemsShowList) {
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        mRecyclerView.addItemDecoration(new HomeSpaceItemDecoration(DensityUtils.dp2px(this, 20)));
        mRecyclerView.setLayoutManager(manager);

        ItemDragHelperCallback callback = new ItemDragHelperCallback(mOveContent);
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        mAdapter = new ChannelManagementAdapter(this, helper, ItemsShowList);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = mAdapter.getItemViewType(position);
                return viewType == ChannelManagementAdapter.TYPE_MY || viewType == ChannelManagementAdapter.TYPE_OTHER ? 1 : 4;
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnMyChannelItemClickListener(new ChannelManagementAdapter.OnMyChannelItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                //进入频道管理未编辑模式下（点击跳转首页频道列表）
                if (!isEditMode && !isFinishing()) {
                    mSharedPreferencesUtil.putInt(ChannelManagementActivity.this, "pageIndex", position);
                    getResuleList();
                    finish();
                }
            }

            @Override
            public void onItemDeleteClick(int position) {
                //进入频道管理编辑模式下删除已选频道
                NavigationItemBean data = mItemsShow.get(position);
                mItemsShow.remove(position);
                mAdapter.setData(mItemsShow);
                mItemsHide.add(0, data);
                mCannelItemsHideAdapter.setData(mItemsHide);
            }
        });
    }

    /**
     * 按返回键回传值给首页
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            getResuleList();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 传首页导航数据
     */
    public void getResuleList() {
        if (mAdapter != null) {
            mComeChannelList = new ArrayList<NavigationItemBean>();
            mItemsShow = mAdapter.getMyChannelItems();
            for (NavigationItemBean item : mItemsHide) {
                item.setIsDefaultShow(0);
            }
            for (NavigationItemBean item : mItemsShow) {
                item.setIsDefaultShow(1);
            }
            mComeChannelList.addAll(mItemsShow);
            mComeChannelList.addAll(mItemsHide);

            //获取sp编辑器
            mSharedPreferencesUtil = new SharedPreferencesUtil();
            mSharedPreferencesUtil.putList(this, "mComeChannelList", mComeChannelList);
            EventBus.getDefault().post(Constant.CHANNEL_STATUS);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //编辑完成模式下
            case R.id.tv_btn_edit:
                if (!isEditMode) {
                    mAdapter.startEditMode(mRecyclerView);
                    mTvBtnEdit.setText(R.string.finish);
                    mTvBtnEdit.setTextColor(android.graphics.Color.RED);
                    mDefaultsortImag.setVisibility(View.VISIBLE);
                    mDefaultsort.setVisibility(View.VISIBLE);
                    mDelete.setVisibility(View.GONE);
                    isEditMode = true;
                } else {
                    mAdapter.cancelEditMode(mRecyclerView);
                    mTvBtnEdit.setText(R.string.eedit);
                    mTvBtnEdit.setTextColor(ContextCompat.getColor(this, R.color.colore_home8));
                    mDefaultsortImag.setVisibility(View.GONE);
                    mDefaultsort.setVisibility(View.GONE);
                    mDelete.setVisibility(View.VISIBLE);
                    isEditMode = false;
                }
                break;
            //默认排序
            case R.id.defaultsort_text:
                mNavigationItemDataBean = (NavigationItemDataBean)
                        getIntent().getSerializableExtra("ItemDataBean");
                mItemsShow.clear();
                mItemsHide.clear();
                mOveContent = 0;
                for (NavigationItemBean item : mNavigationItemDataBean.getContent()) {
                    //判断是否上锁
                    if (item.getIsSolidShow() == 1) {
                        mItemsShow.add(mOveContent, item);
                        mOveContent++;
                        //判断是否显示且最多显示只能8条
                    } else if (mItemsShow.size() < 8 && item.getIsDefaultShow() == 1) {
                        mItemsShow.add(item);
                    } else {
                        mItemsHide.add(item);
                    }
                }
                mCannelItemsHideAdapter.setData(mItemsHide);
                mAdapter.setData(mItemsShow);
                break;
            //返回按钮
            case R.id.channel_push_button:
                getResuleList();
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 进入频道管理编辑模式移动和删除备选频道
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NavigationItemBean data = mItemsHide.get(position);
        if (isEditMode) {
            if (mItemsShow != null && mItemsShow.size() > 0 && mItemsShow.size() < 8) {
                mItemsShow.add(data);
                mAdapter.setData(mItemsShow);
                mItemsHide.remove(position);
                mCannelItemsHideAdapter.setData(mItemsHide);
            } else {
                Toast.makeText(this, getString(R.string.channel_editmode), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.channel_iseditmode), Toast.LENGTH_SHORT).show();
        }
    }
}
