package cn.cibnmp.ott.ui.detail.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.ui.detail.DetailActivity;
import cn.cibnmp.ott.ui.detail.LiveDetailActivity;
import cn.cibnmp.ott.ui.detail.bean.DetailBean;
import cn.cibnmp.ott.ui.search.ToastUtils;
import cn.cibnmp.ott.utils.Lg;

/**
 * Created by axl on 2018/1/16.
 */

public class DetailContentTitile  extends RelativeLayout implements OnClickListener{

    private Activity mContext;
    private View contextView;

    private ImageView imageView2, imageView3;
    private DetailBean datas;
    private ImageView title_imageView1;
    private TextView title_number1;

    private long vid;
    private int detail_type = 0; // 0电影 1电视剧 2综艺 4直播




    DetailBean data;

    private Bitmap thumu;

    public DetailContentTitile(Context context) {
        super(context);
    }

    public DetailContentTitile(Context context, AttributeSet attrs,long vid,int detail_type,DetailBean datas) {
        super(context, attrs);


        mContext = (Activity) context;
        this.detail_type = detail_type;
        this.data =datas;
        contextView = View.inflate(mContext, R.layout.detail_content_type,this);
        this.vid = vid;

        if(isInEditMode())
            return ;

        initView();
    }

    private void initView() {
    //    EventBus.getDefault().register(this);
        imageView2 = (ImageView) contextView.findViewById(R.id.title_imageView2);
        imageView3 = (ImageView) contextView.findViewById(R.id.title_imageView3);
        title_imageView1 = (ImageView) contextView.findViewById(R.id.title_imageView1);
        title_number1 = (TextView) contextView.findViewById(R.id.title_number1);
        title_imageView1.setVisibility(GONE);
        title_number1.setVisibility(GONE);


        if(detail_type!=4) {
            if (((DetailActivity)mContext).collect == 0) {
                imageView3.setImageResource(R.drawable.collect);
            } else {
                imageView3.setImageResource(R.drawable.collect1);
            }
        }else{
            imageView3.setVisibility(GONE);
        }

        imageView3.setOnClickListener(this);
        imageView2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.title_imageView3:
                    if(((DetailActivity)mContext).collect==0){
                        //imageView2.setImageResource(R.drawable.collect);
                        ((DetailActivity)mContext).addVideoCollect();
                    }else{
                        //imageView2.setImageResource(R.drawable.collect1);
                        ((DetailActivity)mContext).deleteVideoCollect();
                    }
                    break;
                case R.id.title_imageView2:
                    if(mContext instanceof DetailActivity)
                    ((DetailActivity)mContext).shareDialog();
                    else if(mContext instanceof LiveDetailActivity)
                        ((LiveDetailActivity)mContext).shareDialog();
                    break;

            }
    }









    public void upDateRepsonse(String response){
        if(response.equals("addVideoCollect")){
            Lg.i("adddd","addVideoCollect");
            imageView3.setImageResource(R.drawable.collect1);
            ToastUtils.show(mContext,"收藏成功");
        //    ToastUtils.show(mContext,"收藏成功1");
        }else if(response.equals("deleteLocalCollect")){
            imageView3.setImageResource(R.drawable.collect);
            ToastUtils.show(mContext,"取消收藏成功");
         //   ToastUtils.show(mContext,"取消收藏成功1");
        }else if(response.equals("addVideoCollectError")){
            imageView3.setImageResource(R.drawable.collect);
            ToastUtils.show(mContext,"收藏失败");
        }else if(response.equals("deleteLocalCollectError")){
            imageView3.setImageResource(R.drawable.collect1);
            ToastUtils.show(mContext,"取消收藏失败");
        }

    }


    public void setData(DetailBean data) {
        this.data = data;
    }


}
