//package cn.cibnmp.ott.view;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//
//import cn.cibnhp.grand.R;
//import cn.cibnmp.ott.bean.NavigationInfoItemBean;
//import cn.cibnmp.ott.config.Constant;
//import cn.cibnmp.ott.utils.HomeJumpDetailUtils;
//import cn.cibnmp.ott.utils.img.ImageFetcher;
//
///**
// * Created by wanqi on 2017/3/8.
// */
//
//public class SpringFragment extends Fragment {
//
//    private NavigationInfoItemBean infoItemBean;
//    private ImageView mViewPagerImag;
//
//    public SpringFragment() {
//    }
//
//    public static SpringFragment newInstance(NavigationInfoItemBean infoItemBean) {
//        SpringFragment springFragment = new SpringFragment();
//        springFragment.infoItemBean = infoItemBean;
//        return springFragment;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        final View root = inflater.inflate(R.layout.spring_frag_layout,
//                container, false);
//        mViewPagerImag = (ImageView) root.findViewById(R.id.img);
//        if (infoItemBean == null || TextUtils.isEmpty(infoItemBean.getImg())) {
//            Glide.with(getActivity()).load(R.drawable.placeholder_828_466).into(mViewPagerImag);
//        } else {
//            ImageFetcher.getInstance().loodingImage(infoItemBean.getImg(), mViewPagerImag, R.drawable.defaultpic);
//        }
//
//        mViewPagerImag.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HomeJumpDetailUtils.actionTo(infoItemBean, getActivity());
//            }
//        });
//        return root;
//    }
//
//}
