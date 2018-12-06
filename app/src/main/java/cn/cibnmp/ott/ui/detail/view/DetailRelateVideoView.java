package cn.cibnmp.ott.ui.detail.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.bean.LayoutItem;
import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.ui.detail.adapter.DetailAdapter;
import cn.cibnmp.ott.utils.DisplayUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.widgets.PmRecyclerView;
import cn.cibnmp.ott.widgets.pmrecyclerview.TwoWayLayoutManager;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.HomeSpannableGridLayoutManager;

/**
 * Created by axl on 2018/1/26.
 */

public class DetailRelateVideoView extends LinearLayout {


    private PmRecyclerView pmRecyclerView;

    private DetailAdapter adapter;

    private View contextView;
    private Activity context;

    private RelativeLayout maxLayout;

    private List<LayoutItem> mLaytItemList = new ArrayList<LayoutItem>();
    private List<NavigationInfoItemBean> mInfoItemBeanList = new ArrayList<NavigationInfoItemBean>();

    public DetailRelateVideoView(Context context) {
        super(context);
    }

    public DetailRelateVideoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = (Activity) context;
        contextView = View.inflate(context, R.layout.detail_love_content, this);
    //   maxLayout = (RelativeLayout) contextView.findViewById(R.id.maxLayout);
        initView();

    }


    private void initView() {
        pmRecyclerView = (PmRecyclerView) contextView.findViewById(R.id.recyclerview);
        HomeSpannableGridLayoutManager gridLayoutManager = new
                HomeSpannableGridLayoutManager(TwoWayLayoutManager.Orientation.VERTICAL, 60, 60);
       pmRecyclerView.setLayoutManager(gridLayoutManager);
       // gridLayoutManager.setOrientation(Or);
        pmRecyclerView.setLayoutManager(gridLayoutManager);
        pmRecyclerView.setSpacingWithMargins(DisplayUtils.getValue(20), DisplayUtils.getValue(20));
        adapter = new DetailAdapter(context);

        adapter.setData(mLaytItemList, mInfoItemBeanList);
        pmRecyclerView.setAdapter(adapter);
    }


    public void setData(List<LayoutItem> mLaytItemList, List<NavigationInfoItemBean> mInfoItemBeanList) {
        this.mInfoItemBeanList = mInfoItemBeanList;
        this.mLaytItemList = mLaytItemList;
        adapter.setData(mLaytItemList, mInfoItemBeanList);
        //   adapter.notifyDataSetChanged();
        handler.sendEmptyMessageDelayed(1, 200);


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    View view =  pmRecyclerView.getChildAt( pmRecyclerView.getLastVisiblePosition());


                   ViewGroup.LayoutParams layoutParams= contextView.getLayoutParams();
                   layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
                   if(view!=null)
                   layoutParams.height = (int) (view.getY()+view.getHeight()+50);
                   else{
                       handler.sendEmptyMessageDelayed(1,300);
                       return;
                   }

                   contextView.setLayoutParams(layoutParams);

                    Lg.i("videoView",""+pmRecyclerView.getMeasuredHeight());
                    break;
            }
        }
    };
}
