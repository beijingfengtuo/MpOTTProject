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
import cn.cibnmp.ott.ui.detail.adapter.EpisodeShowAdapter;
import cn.cibnmp.ott.ui.detail.bean.DetailBean;
import cn.cibnmp.ott.ui.detail.bean.DetailInfoBean;

/**
 * Created by axl on 2018/1/25.
 */

public class DetailShowEpisodeView extends LinearLayout implements View.OnClickListener {
    private View contextView;

    private Context context;

    private LinearLayout llEpisode;
    private RecyclerView list;
    private TextView uptv;
    private TextView null_tv;
    private ImageView imageView1;

    private EpisodeShowAdapter adapter;
    private LinearLayoutManager layoutManager;

    private DetailActivity mActivity;



    public DetailShowEpisodeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        mActivity = (DetailActivity) context;
        contextView = View.inflate(context, R.layout.detail_episode_show_frag, this);

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

        adapter = new EpisodeShowAdapter(context);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
        imageView1.setOnClickListener(this);
    }

    public void addEpisodeData(DetailInfoBean datas) {
        if (datas == null || datas.getChild() == null || datas.getChild().size() <= 1) {

        } else {
            if (datas.getChild() != null && datas.getChild().size() > 1) {
                llEpisode.setVisibility(View.VISIBLE);
            }

            adapter.addData(datas);
            list.setVisibility(VISIBLE);
            null_tv.setVisibility(GONE);
            updateUI(datas.getEpgvideo());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == imageView1 && mActivity != null) {
            mActivity.alterOpenUp();
        }
    }

    public void updateUI(DetailBean datas) {
        if (datas != null && datas.getUpdateNum() > 0) {
            uptv.setText("更新至" + datas.getUpdateNum() + "期");
        }
       // adapter.updateEpisodeItem(sid);
    }

    public void updateEpisodeItem(int sid) {
        adapter.updateEpisodeItem(sid);
        list.scrollToPosition(sid - 1);
    }

}
