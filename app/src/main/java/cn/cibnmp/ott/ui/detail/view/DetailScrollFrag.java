package cn.cibnmp.ott.ui.detail.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.bean.LayoutItem;
import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.library.pullable.PullableScrollView;
import cn.cibnmp.ott.ui.base.BaseFragment;
import cn.cibnmp.ott.ui.detail.bean.DetailInfoBean;
import cn.cibnmp.ott.utils.Lg;

/**
 * Created by axl on 2018/1/16.
 */

public class DetailScrollFrag extends BaseFragment {

    private View contextView;

    private PullableScrollView scrollView;


    private long vid;

    private LinearLayout sLayout;


    private static final String VID = "vid";

    private DetailContentView contentView;
    private DetailContentTitile contentTitle;

    private DetailShowEpisodeView episodeView_s;
    private DetailTvEpisodeView episodeView_t;

    //private DetailRelateVideoView videoView;


    List<LayoutItem> mLaytItemList;
    List<NavigationInfoItemBean> mInfoItemBeanList;

    DetailInfoBean datas;
    private DetailRelateVideoView videoView;


 //   private BaseActivity activity;

    private int sid;

    //    private PmRecyclerView pmRecyclerView;
//
//    private DetailAdapter adapter;
//    DetailRelateVideoView videoView
    int detailType;

    public static DetailScrollFrag newInstance(int detailType, long vid) {
        DetailScrollFrag fragment = new DetailScrollFrag();
        Bundle bundle = new Bundle();
        bundle.putLong(VID, vid);
        bundle.putInt("detailType", detailType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contextView = inflater.inflate(R.layout.detail_scroll_frag, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            vid = bundle.getLong(VID);
            detailType = bundle.getInt("detailType");
        }
        scrollView = (PullableScrollView) contextView.findViewById(R.id.detail_scrollview);
        sLayout = (LinearLayout) contextView.findViewById(R.id.detail_scroll_list);

      //  activity = (BaseActivity) getActivity();

        initView();

        return contextView;
    }


    private void initView() {
        scrollView = (PullableScrollView) contextView.findViewById(R.id.detail_scrollview);
        sLayout = (LinearLayout) contextView.findViewById(R.id.detail_scroll_list);

    }


    private static final String TAG = "DetailScroll";
    private int childType;

  public void initMode(int childType) {
      Lg.i("scrollFrag","initMode");
      if(sLayout==null)
          return;
      sLayout.removeAllViews();

        this.childType = childType;
        if(getContext()==null&&getActivity()==null)
            return;
        videoView = new DetailRelateVideoView(getContext(), null);
        contentView = new DetailContentView(getContext(), null);
        if (detailType == 4) {//直播详情页

            contentTitle = new DetailContentTitile(getActivity(), null, vid, 4,null);
            sLayout.addView(contentTitle);
            if (childType == 2) {
                episodeView_s = new DetailShowEpisodeView(getContext(), null);
                sLayout.addView(contentView);
                sLayout.addView(episodeView_s);
                //TODO zxy 去除评论
//                sLayout.addView(commentView);

//			showView.setListViewHeightBasedOnChildren();
//			commentView.setListViewHeightBasedOnChildren();
            } else if (childType == 1) {
                episodeView_t = new DetailTvEpisodeView(getContext(), null);


                sLayout.addView(contentView);
                sLayout.addView(episodeView_t);

                //TODO zxy 去除评论
//                sLayout.addView(commentView);
            } else if (childType == 0) {
                sLayout.addView(contentView);
                //去除演员列表
//                sLayout.addView(castView);
                //TODO zxy 去除评论
//                sLayout.addView(commentView);
            }
            //TODO zxy 直播详情页去除赞和踩布局
//            sLayout.addView(contentBottom);

        } else {
            //添加头部评论和收藏的布局
            contentTitle = new DetailContentTitile(getActivity(), null, vid, childType,null);
            sLayout.addView(contentTitle);

            if (childType == 2) {
                episodeView_s = new DetailShowEpisodeView(getContext(), null);
                sLayout.addView(contentView);
                sLayout.addView(episodeView_s);
                //TODO zxy 去除评论
//                sLayout.addView(commentView);

//			showView.setListViewHeightBasedOnChildren();
//			commentView.setListViewHeightBasedOnChildren();
            } else if (childType == 1) {
                episodeView_t = new DetailTvEpisodeView(getContext(), null);


                sLayout.addView(contentView);
                sLayout.addView(episodeView_t);

                //TODO zxy 去除评论
//                sLayout.addView(commentView);
            } else if (childType == 0) {
                sLayout.addView(contentView);
                //去除演员列表
//                sLayout.addView(castView);
                //TODO zxy 去除评论
//                sLayout.addView(commentView);
            }
        }
        sLayout.addView(videoView);
    }

    public void diamissDialog() {
        if (contentTitle != null) {
            //     contentTitle.diamissDialog();
        }
    }

    public void scrollToTop() {
        if (scrollView != null) {
            scrollView.scrollTo(0, 0);
        }
    }

    public void setContentData(List<LayoutItem> mLaytItemList, List<NavigationInfoItemBean> mInfoItemBeanList) {
        Lg.i(TAG, mLaytItemList.size() + "....." + mInfoItemBeanList.size());
        this.mLaytItemList = mLaytItemList;
        this.mInfoItemBeanList = mInfoItemBeanList;
        if(videoView!=null)
        videoView.setData(mLaytItemList, mInfoItemBeanList);

    }

    public void setDatas(DetailInfoBean datas) {
        this.datas = datas;
        Lg.i(TAG, "setDatas");
        if(contentView==null){
            return;
        }
        contentView.updateContent(datas, detailType);

        if (childType == 2) {
            episodeView_s.addEpisodeData(datas);

        } else if (childType == 1) {
            episodeView_t.updateUI(datas.getEpgvideo(), datas.getChild().size());
        }

        if(detailType==4)
            contentTitle.setData(datas.getEpglive());
        else
            contentTitle.setData(datas.getEpgvideo());


    }


    public void upDataSid(int pos) {
        sid = pos;
        if (childType == 2) {
            episodeView_s.updateEpisodeItem(pos);
        } else if (childType == 1) {
            episodeView_t.updateEpisodeItem(pos);
        }
    }


    public void updateCollect(String response) {
        contentTitle.upDateRepsonse(response);
    }

    public void setDetailType(int detailType){
      this.detailType = detailType;
    }


}
