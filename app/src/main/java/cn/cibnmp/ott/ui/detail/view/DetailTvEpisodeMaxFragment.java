package cn.cibnmp.ott.ui.detail.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.ui.base.BaseFragment;
import cn.cibnmp.ott.ui.detail.DetailActivity;
import cn.cibnmp.ott.ui.detail.LiveDetailActivity;
import cn.cibnmp.ott.ui.detail.adapter.EpisodeTvMaxAdapter;
import cn.cibnmp.ott.ui.detail.bean.DetailBean;
import cn.cibnmp.ott.ui.detail.bean.DetailInfoBean;
import cn.cibnmp.ott.ui.detail.bean.SidEvent;
import de.greenrobot.event.EventBus;

/**
 * Created by axl on 2018/1/23.
 */

public class DetailTvEpisodeMaxFragment extends BaseFragment implements View.OnClickListener {

    private View contextView;

    private GridView grid;
    private TextView uptv;
    private TextView null_tv;
    private ImageView imageView1;//回收

    private EpisodeTvMaxAdapter adapter;

    private Activity mActivity;

    private DetailInfoBean datas;

    private MyItemListener myItemListener;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub\
        mActivity = (DetailActivity)getContext();
        contextView=inflater.inflate(R.layout.detail_episode_tv_max_frag, container, false);

        initView();
        return contextView;
    }


    private void initView() {
        grid = (GridView) contextView.findViewById(R.id.episode_grid);
        uptv = (TextView) contextView.findViewById(R.id.episode_update_tv);
        null_tv = (TextView) contextView.findViewById(R.id.null_textview);
        imageView1= (ImageView) contextView.findViewById(R.id.imageView1);
        //grid.setSelector(Drawable.null);
        grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        imageView1.setOnClickListener(this);

    }


    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        mActivity = (DetailActivity) contextView.getContext();
    }


    @Override
    public void onClick(View v) {
        if(imageView1==v&&mActivity!=null){
            if(mActivity instanceof DetailActivity)
                ((DetailActivity)mActivity).alterOpenUp();
            else if(mActivity instanceof LiveDetailActivity)
                ((LiveDetailActivity)mActivity).alterOpenUp();
        }
    }
    public void setGridViewHeightBasedOnChildren() {
        if (grid == null)
            return;
        //CommentAdapter listAdapter = (CommentAdapter) list.getAdapter();
        EpisodeTvMaxAdapter gridAdapter= (EpisodeTvMaxAdapter) grid.getAdapter();
        if (gridAdapter == null) {
            // pre-condition
            return;
        }
        int rows;
        int colums=0;
        int horinzotalBorderHeight=0;
        Class<?> clazz = grid.getClass();
        try{
            //利用反射获取gridview每行显示的个数
            Field column = clazz.getDeclaredField("mRequestedNumColumns");
            column.setAccessible(true);
            colums= (Integer) column.get(grid);
            //利用反射 取得横向分隔线高度
            Field horizontalSpacing = clazz.getDeclaredField("mRequestedHorizontalSpacing");
            horizontalSpacing.setAccessible(true);
            horinzotalBorderHeight=(Integer)horizontalSpacing.get(grid);
        }catch (Exception e){
            e.printStackTrace();
        }
        //判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
        if(gridAdapter.getCount()%colums>0){
            rows=gridAdapter.getCount()/colums+1;
        }else{
            rows= gridAdapter.getCount()/colums;
        }
        int totalHieght = 0;
        for(int i=0;i<rows;i++){//只计算每行高度
            View listItem = gridAdapter.getView(i,null,grid);
            listItem.measure(0,0);//计算子项view 的宽高
            totalHieght+= listItem.getMeasuredHeight();//统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = grid.getLayoutParams();
        params.height=totalHieght+horinzotalBorderHeight*(rows-1);//最后加上分割线总高度
        grid.setLayoutParams(params);
    }


    public void setDataAndSid(DetailBean bean, DetailInfoBean datas, int sid) {
        // TODO Auto-generated method stub
        this.datas= datas;
        adapter = new EpisodeTvMaxAdapter(getContext(),datas,sid);
        myItemListener=new MyItemListener();
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(myItemListener);
        if (bean.getUpdateNum() < bean.getSeries()) {
            uptv.setText("更新至"+ bean.getUpdateNum() +"集／共" + bean.getSeries() + "集");
        } else {
            uptv.setText( bean.getSeries() + "集全");
        }

    }

    class MyItemListener implements AdapterView.OnItemClickListener {
        //点击之后设置sid 并播放
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
//            ToastUtils.show(context,position);
            if (datas.getChild().size() > position) {
                int sid = (int) (datas.getChild().get(position).getSid());
                //int sid = position+1;
                SidEvent se = new SidEvent();
                se.setSid(sid);
                EventBus.getDefault().post(se);
                adapter.setSid(sid);
                adapter.notifyDataSetChanged();
            }
            //parent.getChildAt(old+1).setBackgroundResource(R.drawable.border_white);
            //view..setBackgroundResource(R.drawable.border_0);
        }

    }
    public void setSidChange(int sid){

        adapter.setSid(sid);
        adapter.notifyDataSetChanged();

    }
}
