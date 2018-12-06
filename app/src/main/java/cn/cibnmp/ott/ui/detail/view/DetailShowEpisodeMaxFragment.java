package cn.cibnmp.ott.ui.detail.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.ui.base.BaseFragment;
import cn.cibnmp.ott.ui.detail.DetailActivity;
import cn.cibnmp.ott.ui.detail.adapter.EpisodeShowMaxAdapter;
import cn.cibnmp.ott.ui.detail.bean.DetailBean;
import cn.cibnmp.ott.ui.detail.bean.DetailInfoBean;
import cn.cibnmp.ott.ui.detail.bean.SidEvent;
import de.greenrobot.event.EventBus;

/**
 * Created by axl on 2018/1/23.
 */

public class DetailShowEpisodeMaxFragment extends BaseFragment implements View.OnClickListener{
    //
    private View contextView;

    private ListView list;
    private TextView uptv;
    private TextView null_tv;
    private ImageView imageView1;

    private EpisodeShowMaxAdapter adapter;

    private DetailActivity mActivity;

    private DetailInfoBean datas;

    private MyItemOnClickListener myItemOnClickListener;

    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub\

        contextView=inflater.inflate(R.layout.detail_episode_show_max_frag, container, false);
        initView();
        return contextView;
    }

    private void initView() {
        list = (ListView) contextView.findViewById(R.id.episode_list);
        uptv = (TextView) contextView.findViewById(R.id.episode_update_tv);
        null_tv = (TextView) contextView.findViewById(R.id.null_textview);
        imageView1 = (ImageView)contextView.findViewById(R.id.imageView1);


        imageView1.setOnClickListener(this);
    }


    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        mActivity = (DetailActivity)contextView.getContext();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        //设置监听
        if(v==imageView1&&mActivity!=null){
            mActivity.alterOpenUp();
        }
    }

    public void setDataAndSid(DetailBean detailBean, DetailInfoBean datas, int sid) {
        // TODO Auto-generated method stub
        this.datas =datas;
        adapter = new EpisodeShowMaxAdapter(getContext(),datas,sid);
        myItemOnClickListener = new MyItemOnClickListener();
        list.setAdapter(adapter);
        list.setOnItemClickListener(myItemOnClickListener);
        uptv.setText("更新至" + detailBean.getUpdateNum() + "期");
    }

    class MyItemOnClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub
          //  ToastUtils.show(context,position);
            if(datas.getChild().size()>position){
            int sid = (int) (datas.getChild().get(position+1).getSid());
            SidEvent se = new SidEvent();
            se.setSid(sid);
            EventBus.getDefault().post(se);
            adapter.setSid(sid);
            adapter.notifyDataSetChanged();}
          //  mActivity.initPlayerUrl();
          //  mActivity.upDateEpisodeItem(sid);
        }

    }

    public void setSidChange(int sid) {
        // TODO Auto-generated method stub
        adapter.setSid(sid);
        adapter.notifyDataSetChanged();
    }

}
