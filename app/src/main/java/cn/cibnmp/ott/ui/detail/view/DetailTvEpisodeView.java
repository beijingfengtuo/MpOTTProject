package cn.cibnmp.ott.ui.detail.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.ui.detail.DetailActivity;
import cn.cibnmp.ott.ui.detail.adapter.EpisodeTvAdapter;
import cn.cibnmp.ott.ui.detail.bean.DetailBean;

/**
 * Created by axl on 2018/1/25.
 */

public class DetailTvEpisodeView extends LinearLayout implements View.OnClickListener  {

    private View contextView;

    private Context context;

    private LinearLayout llEpisode;
    private RecyclerView list;
    private TextView uptv;
    private TextView null_tv;
    private ImageView imageView1;

    private EpisodeTvAdapter adapter;
    private LinearLayoutManager layoutManager;

    public DetailTvEpisodeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        contextView = View.inflate(context, R.layout.detail_episode_tv_frag, this);

        if (isInEditMode())
            return;

        initView();
    }

    private void initView() {
        llEpisode = (LinearLayout) contextView.findViewById(R.id.ll_episode_tv);
        list = (RecyclerView) contextView.findViewById(R.id.episode_list);
        uptv = (TextView) contextView.findViewById(R.id.episode_update_tv);
        null_tv = (TextView) contextView.findViewById(R.id.null_textview);
        imageView1 = (ImageView) contextView.findViewById(R.id.imageView1);

        adapter = new EpisodeTvAdapter(context);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
        imageView1.setOnClickListener(this);
    }

    public void addEpisodeData(int size) {
        if (size > 1) {
            llEpisode.setVisibility(View.VISIBLE);
        }
        if (size > 0) {
            adapter.addData(size);
            list.setVisibility(VISIBLE);
            null_tv.setVisibility(GONE);
        }
    }

    public void updateUI(DetailBean datas, int size) {
        if (datas!= null && datas.getUpdateNum() > 0 && datas.getSeries() > 0) {
            if (datas.getUpdateNum() < datas.getSeries()) {
                uptv.setText("更新至"+ datas.getUpdateNum() +"集/共" + datas.getSeries() + "集");
            } else {
                uptv.setText("全" + datas.getSeries() + "集");
            }

        }
        addEpisodeData(size);
        adapter.addData(size);
    }

    public void updateEpisodeItem(int s) {
        adapter.updateEpisodeItem(s);
        list.scrollToPosition(s - 1);
    }

    @Override
    public void onClick(View v) {
        if(v==imageView1&&context!=null){
            //展开
            ((DetailActivity)context).alterOpenUp();

        }
    }
}
